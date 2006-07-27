<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:variable name="pagetitle">
		Status of deliveries
	</xsl:variable>

	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="allCountries"><xsl:value-of select="count(child::XmlData/RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>
	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet[@Name='Main']/@permissions"/>
	</xsl:variable>
	<xsl:variable name="sel_actdetails">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ACT_DETAILS_ID='),'&amp;')"/>
	</xsl:variable>
	<xsl:variable name="sel_country">
		<xsl:value-of select="substring-after(/XmlData/xml-query-string,'COUNTRY_ID=')"/>
	</xsl:variable>
	<xsl:variable name="sortorder">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ORD='),'&amp;')"/>
	</xsl:variable>
	<xsl:variable name="country_name">
		<xsl:value-of select="//XmlData/RowSet[@Name='Main']/Row/T_SPATIAL/SPATIAL_NAME"/>
	</xsl:variable>
	<xsl:variable name="sel_actdetails">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ACT_DETAILS_ID='),'&amp;')"/>
	</xsl:variable>
	<xsl:variable name="src-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='RA']/Row/T_OBLIGATION/FK_SOURCE_ID"/>
	</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.europa.eu">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 	<xsl:choose>
		<xsl:when test="$allCountries=0">
			 <div class="breadcrumbitem"><a href="deliveries.jsv">Deadlines</a></div>
			 <div class="breadcrumbitem">
			 	<a>
			 		<xsl:attribute name="href">csmain?COUNTRY_ID=<xsl:value-of select="$sel_country"/>&amp;ORD=NEXT_REPORTING,%20DEADLINE</xsl:attribute><xsl:value-of select="$country_name"/></a></div>
		</xsl:when>
		<xsl:otherwise>
			 <div class="breadcrumbitem"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$src-id"/>&amp;mode=S</xsl:attribute>
Legislative instrument</a></div>
			 <div class="breadcrumbitem"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$sel_actdetails"/>&amp;aid=<xsl:value-of select="$src-id"/>&amp;mode=A</xsl:attribute> Reporting obligation</a></div>
		</xsl:otherwise>
	</xsl:choose>
	<div class="breadcrumbitemlast">Deliveries</div>
	 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

<xsl:template match="XmlData">
	<!-- page -->
	<div id="workarea">
	<!-- header -->
	<xsl:for-each select="//RowSet[@Name='RA']/Row">

	<div id="operations">
		<ul>
			<li class="help"><a href="javascript:openViewHelp('HELP_DELIVERIES')">Page help</a></li>
			<xsl:if test="contains($permissions, ',/Admin/Helptext:u,')='true'">
				<li class="help"><a href="javascript:openHelp('HELP_DELIVERIES')">Edit help text</a></li>
			</xsl:if>
		</ul>
	</div>
	
	<h1>Status of deliveries:
			<xsl:choose>
				<xsl:when test="$allCountries=0">
					<xsl:value-of select="$country_name"/>
				</xsl:when>
				<xsl:otherwise>
					all countries
				</xsl:otherwise>
			</xsl:choose>
	</h1>
	<table class="datatable">
	<xsl:if test="contains($permissions, ',/Admin/Harvest:v,')='true'">
		<tr><td></td><td><i>last harvested: <xsl:value-of select="T_OBLIGATION/LAST_HARVESTED"/>&#160;</i></td></tr>
	</xsl:if>
		<tr>
			<th scope="row" class="scope-row">Reporting obligation:</th>
			<td ><xsl:value-of select="T_OBLIGATION/TITLE"/></td>
		</tr>
		<tr>
			<th scope="row" class="scope-row">Reporting frequency:</th>
			<td>
				<xsl:call-template name="RAReportingFrequency"/>
			</td>
		</tr>
		<tr>
			<th scope="row" class="scope-row">Client organisation:</th>
			<td>
					<a>
						<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a>
			</td>
		</tr>
		<tr>
			<th scope="row" class="scope-row">Other clients using this reporting:</th>
			<td>
				<xsl:for-each select="SubSet[@Name='CCClients']/Row">
					<a>
						<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a><br/>
				</xsl:for-each>

			</td>
		</tr>
		<tr>
			<th scope="row" class="scope-row">Reporting guidelines:</th>
			<td><a target="RA_guidelines"><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
				<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/></a></td>
		</tr>
	</table>
	</xsl:for-each>

	<br/>

<!-- set the default sortorder  -->
	<xsl:variable name="cur_sortorder">
		<xsl:choose>
			<xsl:when test="string-length($sortorder) = 0">
				<xsl:choose>
					<xsl:when test="$allCountries=1">T_SPATIAL.SPATIAL_NAME</xsl:when>
					<xsl:otherwise>UPLOAD_DATE DESC</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="translate($sortorder,'%20',' ')"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="recCount">
		<xsl:value-of select="count(//RowSet[@Name='Main']/Row/T_DELIVERY)"/>
	</xsl:variable>

	<!--div class="smallfont" style="font-size: 8pt; font-weight: bold">[<xsl:value-of select="$recCount"/> record(s) returned]</div-->
	<p class="headsmall">[<xsl:value-of select="$recCount"/> record(s) returned]</p>	
<br/>


<!-- oneCountry=0 one country, one country = 1 all countries -->
<table width="100%" class="sortable">
	<thead>
		<tr>
		<!-- contact -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Responsible persons'"/>
				<xsl:with-param name="text" select="'Contact'"/>
				<xsl:with-param name="sorted" select="'ROLE_DESCR'"/>
				<xsl:with-param name="width" select="'25%'"/>
				<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
			</xsl:call-template>
		<!-- delivery title -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Title of delivery'"/>
				<xsl:with-param name="text" select="'Delivery Title'"/>
				<xsl:with-param name="sorted" select="'T_DELIVERY.TITLE'"/>
				<xsl:with-param name="width" select="'24%'"/>
				<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
			</xsl:call-template>
		<!-- delivery date -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Date of delivery'"/>
				<xsl:with-param name="text" select="'Delivery Date'"/>
				<xsl:with-param name="sorted" select="'UPLOAD_DATE'"/>
				<xsl:with-param name="width" select="'11%'"/>
				<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
			</xsl:call-template>
		<!-- period covered -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Period covered by this delivery'"/>
				<xsl:with-param name="text" select="'Period covered'"/>
				<xsl:with-param name="sorted" select="'COVERAGE'"/>
				<xsl:with-param name="width" select="'27%'"/>
				<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
			</xsl:call-template>
		<!-- country -->
			<xsl:if test="$allCountries=1">
				<xsl:call-template name="createSortable">
					<xsl:with-param name="title" select="'Country'"/>
					<xsl:with-param name="text" select="'Country'"/>
					<xsl:with-param name="sorted" select="'SPATIAL_NAME'"/>
					<xsl:with-param name="width" select="'13%'"/>
					<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
				</xsl:call-template>
			</xsl:if>
		</tr>
	</thead>

	<xsl:for-each select="//RowSet[@Name='Main']/Row">
		<tr>
			<xsl:attribute name="class">
				<xsl:if test="position() mod 2 = 0">zebraeven</xsl:if>
			</xsl:attribute>
			<td>
					<xsl:if test="T_OBLIGATION/RESPONSIBLE_ROLE != ''">
						<xsl:choose>
							<xsl:when test="T_ROLE/ROLE_DESCR=''">
								<xsl:call-template name="short">
									<xsl:with-param name="text" select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-',T_SPATIAL/SPATIAL_TWOLETTER)"/>
									<xsl:with-param name="length">40</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
						<xsl:otherwise>
							<a>
								<xsl:attribute name="title"><xsl:value-of select="T_ROLE/ROLE_DESCR"/></xsl:attribute>
								<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_URL"/>')</xsl:attribute>
								<xsl:call-template name="short">
									<xsl:with-param name="text" select="T_ROLE/ROLE_DESCR"/>
									<xsl:with-param name="length">30</xsl:with-param>
								</xsl:call-template>
							</a>&#160;
							<img src="images/details.jpg" alt="Additional details for logged-in users">
								<xsl:attribute name="onclick">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
							</img>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				&#160;
		</td>
		<td>
			<a target="ROD_delivery">
				<xsl:attribute name="href">
					<xsl:value-of select="T_DELIVERY/DELIVERY_URL"/>
				</xsl:attribute>
				<xsl:value-of select="T_DELIVERY/TITLE"/>
			</a>
		</td>
		<td>
			<xsl:choose>
				<xsl:when test="T_DELIVERY/UPLOAD_DATE != '0000-00-00'">
					<xsl:value-of select="T_DELIVERY/UPLOAD_DATE"/>
				</xsl:when>
				<xsl:otherwise>
					&lt;No date&gt;
				</xsl:otherwise>
			</xsl:choose>
		</td>
		<td>
			<xsl:value-of select="T_DELIVERY/COVERAGE"/>
			 &#160;
		</td>
		<xsl:if test="$allCountries=1">
			<td>
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
			</td>
		</xsl:if>
	</tr>
</xsl:for-each>

</table>

	<br/><br/>
	&#160;&#160;&#160;&#160;Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.
	<br/><br/>
  </div> <!-- workarea -->
<xsl:call-template name="CommonFooter"/>

<!---/body>
</html-->

</xsl:template>
<!-- EK 050210 template for calculating request URL for sorting -->
	<xsl:template name="createURL">
		<xsl:param name="sorted"/>
		<xsl:variable name="uri">csdeliveries</xsl:variable>
		<xsl:variable name="actdetails_param">
			<xsl:if test="string-length($sel_actdetails) &gt; 0">&amp;ACT_DETAILS_ID=<xsl:value-of select="$sel_actdetails"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="country_param">
			<xsl:if test="string-length($sel_country) &gt; 0">&amp;COUNTRY_ID=<xsl:value-of select="$sel_country"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="ORD">
			<xsl:if test="string-length($sorted) &gt; 0">&amp;ORD=<xsl:value-of select="$sorted"/></xsl:if>
		</xsl:variable>
		
		<xsl:variable name="params">
			<xsl:value-of select="concat($ORD,$actdetails_param,$country_param)"/>
		</xsl:variable>
		<xsl:value-of select="concat($uri,'?',substring($params,2))"/>
	</xsl:template>
</xsl:stylesheet>
