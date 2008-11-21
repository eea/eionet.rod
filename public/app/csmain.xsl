<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="pagetitle">
		Reporting overview
	</xsl:variable>
	
	<xsl:variable name="col_class">
		twocolumns
	</xsl:variable>
	
	<xsl:include href="ncommon.xsl"/>


	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet[position()=1]/@auth"/>
	</xsl:variable>
		
	<xsl:variable name="sel_country">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'COUNTRY_ID='),'&amp;')"/>
	</xsl:variable>

	<xsl:variable name="sel_client">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'CLIENT_ID='),'&amp;')"/>
	</xsl:variable>

	<xsl:variable name="sel_issue">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ISSUE_ID='),'&amp;')"/>
		<!--xsl:value-of select="translate(substring-before(substring-after(substring-after(/XmlData/xml-query-string, 'env_issue='),'%3A'),'&amp;'),'+',' ')"/-->
	</xsl:variable>

	<xsl:variable name="sel_period">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'DEADLINES='),'&amp;')"/>
	</xsl:variable>

	<xsl:variable name="sel_period_start">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'DATE_1='),'&amp;')"/>
	</xsl:variable>

	<xsl:variable name="sel_period_end">
		<xsl:value-of select="substring-after(/XmlData/xml-query-string,'DATE_2=')"/>
	</xsl:variable>

	<xsl:variable name="sortorder">
		<xsl:value-of select="//RowSet[@Name='Main']/@Sort_order" />
	</xsl:variable>

<!-- Called to show breadcrumbs -->
<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem"><a href="deliveries">Deadlines</a></div>
 <div class="breadcrumbitemlast">Country</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

<xsl:template name="PageHelp">
	<a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp('HELP_CSMAIN')" onclick="pop(this.href);return false;"><span>Page help</span></a>
</xsl:template>

<xsl:template match="XmlData">

<script type="text/javascript">

<![CDATA[


	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
	}


]]>
				var picklist = new Array();

				
</script>

	<!-- main -->

	<div id="workarea">
	
	<h1>Reporting overview:
		<xsl:if test="$sel_country!='0'">
			<xsl:value-of select="//RowSet[@Name='CountryData']/Row/T_SPATIAL/SPATIAL_NAME"/>
		</xsl:if>
		<xsl:if test="$sel_issue!='0' and $sel_issue!=''">
		 [<xsl:value-of select="//RowSet[@Name='IssueData']/Row/T_ISSUE/ISSUE_NAME"/>] 
 		</xsl:if>
 		
		<xsl:if test="$sel_period!='0' and $sel_period!=''">
			<xsl:if test="$sel_period='1'"> (Next month)</xsl:if>
			<xsl:if test="$sel_period='2'"> (Next 3 months)</xsl:if>
			<xsl:if test="$sel_period='3'"> (Next 6 months)</xsl:if>
			<xsl:if test="$sel_period='4'"> (Previous months)</xsl:if>
 		</xsl:if>

		<xsl:if test="$sel_period_start!='' and contains($sel_period_start, 'dd') = false and $sel_period_end!=''">
			(<xsl:value-of select="concat(substring($sel_period_start, 1, 2), '/', substring($sel_period_start, 6, 2), '/', substring($sel_period_start, 11, 4))"/> - 
			 <xsl:value-of select="concat(substring($sel_period_end, 1, 2), '/', substring($sel_period_end, 6, 2), '/', substring($sel_period_end, 11, 4))"/>)
 		</xsl:if>

		<xsl:if test="$sel_client!='0' and $sel_client!='' ">
		 <br/>for: <xsl:value-of select="//RowSet[@Name='ClientData']/Row/T_CLIENT/CLIENT_NAME"/>
 		</xsl:if>
	</h1>

	<!-- 8 -->
	<form id="ff" method="get" action="csmain" class="notprintable">
	<table cellspacing="0" cellpadding="3" width="600" border="0">
			<tr>
				<td valign="middle" width="30%" class="select_issue">Select issue:</td>
				<td valign="middle" width="20%" class="select_deadline">Select deadline:</td>
				<td valign="middle" width="40%" class="select_client">Select client:</td>
				<td valign="middle" width="10%" class="help_btn">
					<p align="right">
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSMAIN1</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
					</p>
				</td>
			</tr>
			<tr>
				<td valign="middle" class="issues">
					<select class="issues" size="1" name="ISSUE_ID">
						<option value="0" selected="selected">All issues</option>
						<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
					</select>
				</td>
				<td valign="middle" class="deadlines">
					<select class="deadlines" size="1" name="DEADLINES"> 
						<option value="0">
							<xsl:if test="$sel_period='' or $sel_period='0'"> 
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
						All deadlines
						</option> 
						<option value="1">
							<xsl:if test="$sel_period='1'"> 
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
						In the next month</option> 
						<option value="2"><xsl:if test="$sel_period='2'"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if>
								In the next 3 months</option> 
						<option value="3"><xsl:if test="$sel_period='3'"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if>In the next 6 months</option> 
						<option value="4"><xsl:if test="$sel_period='4'"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if>Previous months</option>
					</select>
				</td>
				<td valign="middle" class="client">
					<select class="client" size="1" name="CLIENT_ID">
						<option value="" selected="selected">All clients</option>
						<xsl:apply-templates select="RowSet[@Name='Client']"/>
					</select>
				</td>
				<td valign="middle" align="right" class="go_btn">
					<!--input type="hidden" name="CLIENT_ID">
						<xsl:attribute name="value">
							<xsl:value-of select="$sel_client"/>
						</xsl:attribute>
					</input-->
					<input type="hidden" name="COUNTRY_ID">
						<xsl:attribute name="value">
							<xsl:value-of select="$sel_country"/>
						</xsl:attribute>
					</input>
					<xsl:call-template name="go"/>
				</td>
			</tr>
			</table>
		</form>
	<xsl:variable name="recCount"><xsl:value-of select="count(child::RowSet[@Name='Main']/Row/T_OBLIGATION)"/></xsl:variable>
	<div class="smallfont" style="font-size: 8pt; font-weight: bold">[<xsl:value-of select="$recCount"/> record(s) returned]</div><br/>
	<div class="smallfont" style="font-size: 10pt; font-weight: normal">The list includes also recently passed deadlines, until 10% of the time difference between last deadline<br/>and next deadline has passed - 3 days for a monthly deadline, 36 days for a yearly deadline etc.</div><br/>
<table style="table-layout:fixed; width:100%" class="sortable">
	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">
	<col style="width:40%"/>
	<col style="width:20%"/>
	<col style="width:12%"/>
	<col style="width:16%"/>
	<col style="width:12%"/>
	<xsl:if test="$sel_country='0' or $sel_country=''">
	<col style="width:10%"/> <!-- fix me: adds up to 110%  -->
	</xsl:if>
	<thead>
		<tr>
		
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Title of the reporting obligation'"/>
				<xsl:with-param name="text" select="'Reporting obligation'"/>
				<!--xsl:with-param name="sorted" select="'T_OBLIGATION.TITLE'"/-->
				<xsl:with-param name="sorted" select="'TITLE'"/>
				<xsl:with-param name="width" select="'30%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- client -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Reporting client'"/>
				<xsl:with-param name="text" select="'Reporting to'"/>
				<xsl:with-param name="sorted" select="'CLIENT_DESCR'"/>
				<xsl:with-param name="width" select="'18%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- deadline -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Date of delivery'"/>
				<xsl:with-param name="text" select="'Deadline'"/>
				<!--xsl:with-param name="sorted" select="'NEXT_REPORTING, NEXT_DEADLINE'"/-->
				<xsl:with-param name="sorted" select="'NEXT_REPORTING, DEADLINE'"/>
				<xsl:with-param name="width" select="'9%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- dl2 -->
			<!--xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Next deadline'"/>
				<xsl:with-param name="text" select="'Next DL'"/>
				<xsl:with-param name="sorted" select="'DEADLINE2'"/>
				<xsl:with-param name="width" select="'9%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template-->
	<!-- responsible -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Responsible person or role'"/>
				<xsl:with-param name="text" select="'Responsible'"/>
				<xsl:with-param name="sorted" select="'ROLE_DESCR'"/>
				<xsl:with-param name="width" select="'13%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- empty td -->
		<!--th scope="col" title="Show deliveries in the repository" width="8%"><span>Deliveries</span></th-->
		<xsl:call-template name="createSortable">
			<xsl:with-param name="title" select="'Show deliveries in the repository'"/>
			<xsl:with-param name="text" select="'Deliveries'"/>
			<xsl:with-param name="sorted" select="'HAS_DELIVERY'"/>
			<xsl:with-param name="width" select="'8%'"/>
			<xsl:with-param name="cur_sorted" select="$sortorder"/>
		</xsl:call-template>
		<xsl:if test="$sel_country='0' or $sel_country=''">
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Country'"/>
				<xsl:with-param name="text" select="'Country'"/>
				<xsl:with-param name="sorted" select="'SPATIAL_NAME'"/>
				<xsl:with-param name="width" select="'10%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
		</xsl:if>

	<!-- END of header ROW -->
	</tr>	
	</thead>
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)=0">
		<b>No deliveries match the search criteria</b>
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">

<!-- table row start -->
<xsl:for-each select="RowSet[@Name='Main']/Row">

  <tr>
		<xsl:attribute name="class">
				<xsl:if test="position() mod 2 = 0">even</xsl:if>
		</xsl:attribute>

	
	<td>
		<a>
			<xsl:attribute name="href">obligations/<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/></xsl:attribute>
			<xsl:attribute name="title"><xsl:value-of select="T_OBLIGATION/TITLE"/></xsl:attribute>
			<xsl:call-template name="short">
				<xsl:with-param name="text" select="T_OBLIGATION/TITLE"/>
				<xsl:with-param name="length">40</xsl:with-param>
			</xsl:call-template>
		</a>
		<xsl:if test="T_SOURCE/SOURCE_CODE!=''">
			<br/>
			(<a>
				<xsl:attribute name="href">instruments/<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/></xsl:attribute>
				<xsl:value-of select="T_SOURCE/SOURCE_CODE"/>
			</a>)
		</xsl:if>
	</td>

	<td>
		<a>
			<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
			<xsl:attribute name="title"><xsl:value-of select="T_CLIENT/CLIENT_NAME"/></xsl:attribute>
					<xsl:call-template name="short">
						<xsl:with-param name="text" select="T_CLIENT/CLIENT_DESCR"/>
						<xsl:with-param name="length">20</xsl:with-param>
					</xsl:call-template>
		</a>
	</td>
	<td>
		<xsl:attribute name="title"><xsl:value-of select="T_OBLIGATION/DEADLINE"/></xsl:attribute>
		<xsl:choose>
		<xsl:when test="T_OBLIGATION/NEXT_DEADLINE=''">
			<xsl:attribute name="style">color:#006666</xsl:attribute>
		</xsl:when>
		<xsl:otherwise>
			<xsl:attribute name="style">color:#000000</xsl:attribute>
		</xsl:otherwise>			
		</xsl:choose>

		<xsl:call-template name="short">
			<xsl:with-param name="text" select="T_OBLIGATION/DEADLINE"/>
			<xsl:with-param name="length">10</xsl:with-param>
		</xsl:call-template>
		
		<!--xsl:value-of select="T_OBLIGATION/DEADLINE"/-->
	</td>
	<!--td>
		<xsl:if test="T_OBLIGATION/DEADLINE2 != '' ">	
			<xsl:value-of select="T_OBLIGATION/DEADLINE2"/>
		</xsl:if>
	</td-->

	<td>
		<xsl:if test="T_OBLIGATION/RESPONSIBLE_ROLE != ''">
			<xsl:choose>
			<xsl:when test="RESPONSIBLE/ROLE_DESCR=''">
				<xsl:choose>
					<xsl:when test="T_SPATIAL/SPATIAL_ISMEMBERCOUNTRY='Y'">
						<xsl:attribute name="title"><xsl:value-of select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-mc-',T_SPATIAL/SPATIAL_TWOLETTER)"/></xsl:attribute>
						<xsl:call-template name="short">
							<xsl:with-param name="text" select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-mc-',T_SPATIAL/SPATIAL_TWOLETTER)"/>
							<xsl:with-param name="length">35</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="title"><xsl:value-of select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-cc-',T_SPATIAL/SPATIAL_TWOLETTER)"/></xsl:attribute>
						<xsl:call-template name="short">
							<xsl:with-param name="text" select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-cc-',T_SPATIAL/SPATIAL_TWOLETTER)"/>
							<xsl:with-param name="length">35</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
			<!--a>
			<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_URL"/>')</xsl:attribute>
			<xsl:attribute name="title"><xsl:value-of select="RESPONSIBLE/ROLE_DESCR"/></xsl:attribute>
						<xsl:call-template name="short">
							<xsl:with-param name="text" select="RESPONSIBLE/ROLE_DESCR"/>
							<xsl:with-param name="length">15</xsl:with-param>
						</xsl:call-template>
			</a>&#160;
			<a><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
				<img src="images/details.gif" alt="Additional details for logged-in users" border="0"/>
			</a-->
			<a>
				<xsl:attribute name="href">responsible.jsp?role=<xsl:value-of select="T_OBLIGATION/RESPONSIBLE_ROLE"/>&amp;spatial=<xsl:value-of select="T_SPATIAL/SPATIAL_TWOLETTER"/>&amp;member=<xsl:value-of select="T_SPATIAL/SPATIAL_ISMEMBERCOUNTRY"/></xsl:attribute>
				<xsl:call-template name="short">
					<xsl:with-param name="text" select="RESPONSIBLE/ROLE_DESCR"/>
					<xsl:with-param name="length">15</xsl:with-param>
				</xsl:call-template>
			</a>
			</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</td>
	<td>
		<xsl:choose>
				<!--xsl:when test="contains(T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS, concat(',' , T_SPATIAL/PK_SPATIAL_ID , ',') )='true'"-->
				<xsl:when test="T_OBLIGATION/HAS_DELIVERY=1">
					<a>
					<!--xsl:attribute name="href">javascript:openPopup('csdeliveries', 'ACT_DETAILS_ID=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;COUNTRY_ID=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>')</xsl:attribute-->
					<xsl:attribute name="href">csdeliveries?actDetailsId=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;spatialId=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/></xsl:attribute>
						Show list
					</a>
				</xsl:when>
				<xsl:otherwise>
						None
				</xsl:otherwise>
		</xsl:choose>
	</td>
	<xsl:if test="$sel_country='0' or $sel_country=''">
		<td>
			<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
		</td>
	</xsl:if>
  </tr>
</xsl:for-each>

</xsl:if>
<!-- end of table row -->
  </table>
	<!-- 8 -->
	<p style="text-align:center">
	Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.<br/>
        There can be a delay of up to one day before they show up.
	</p>


  </div> <!-- workarea -->
		<xsl:call-template name="CommonFooter"/>
<!-- main -->

<table>
<tr><td>
</td></tr>
</table>
		

</xsl:template>

	<xsl:template match="RowSet[@Name='EnvIssue']">

		<xsl:for-each select="Row/T_ISSUE">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/></xsl:attribute>
					<xsl:if test="PK_ISSUE_ID=$sel_issue">
							<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
				<xsl:value-of select="ISSUE_NAME"/>
			</option>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="RowSet[@Name='Client']">

		<xsl:for-each select="Row/T_CLIENT">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="PK_CLIENT_ID"/></xsl:attribute>
					<xsl:if test="PK_CLIENT_ID=$sel_client">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
				<xsl:value-of select="CLIENT_NAME"/>
			</option>
		</xsl:for-each>
	</xsl:template>

<!-- EK 050210 template for calculating request URL for sorting -->
	<xsl:template name="createURL">
		<xsl:param name="sorted"/>
		<xsl:variable name="uri">csmain</xsl:variable>
		<xsl:variable name="country_param">
			<xsl:if test="string-length($sel_country) &gt; 0">&amp;COUNTRY_ID=<xsl:value-of select="$sel_country"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="client_param">
			<xsl:if test="string-length($sel_client) &gt; 0">&amp;CLIENT_ID=<xsl:value-of select="$sel_client"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="issue_param">
			<xsl:if test="string-length($sel_issue) &gt; 0">&amp;ISSUE_ID=<xsl:value-of select="$sel_issue"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="period_param">
			<xsl:if test="string-length($sel_period) &gt; 0">&amp;DEADLINES=<xsl:value-of select="$sel_period"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="date1_param">
			<xsl:if test="string-length($sel_period_start) &gt; 0">&amp;DATE_1=<xsl:value-of select="$sel_period_start"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="date2_param">
			<xsl:if test="string-length($sel_period_end) &gt; 0">&amp;DATE_2=<xsl:value-of select="$sel_period_end"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="ORD">
			<xsl:if test="string-length($sorted) &gt; 0">&amp;ORD=<xsl:value-of select="$sorted"/></xsl:if>
		</xsl:variable>
		
		<xsl:variable name="params">
			<xsl:value-of select="concat($country_param,$client_param,$issue_param,$period_param,$date1_param,$ORD,$date2_param)"/>
		</xsl:variable>
		<xsl:value-of select="concat($uri,'?',substring($params,6))"/>
	</xsl:template>

</xsl:stylesheet>
