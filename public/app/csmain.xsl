<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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

<xsl:template match="XmlData">

<script language="JavaScript">

<![CDATA[


	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
	}


]]>
				var picklist = new Array();

				
</script>


<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem"><a href="deliveries.jsv">Deadlines</a></div>
 <div class="breadcrumbitemlast">Country</div>
 <div class="breadcrumbtail">&#160;</div>
</div>

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
	<form name="ff" method="get" action="csmain" class="notprintable">
	<table border="0"><tr>
	<TD width="771">

	<TABLE cellspacing="0" cellpadding="3" width="100%" border="0">
			<TR>
				<TD vAlign="center" width="33%" bgColor="#ffffff" style="border-left: #008080 1px solid; border-right: #C0C0C0 1px solid; border-top: #008080 1px solid;"><FONT face="Verdana" size="1"><B>Select issue:</B></FONT></TD>
				<TD vAlign="center" width="19%" bgColor="#ffffff" style="border-top: #008080 1px solid;"><B><FONT face="Verdana" size="1">Select deadline:</FONT></B></TD>
				<TD vAlign="center" width="7%" bgColor="#ffffff" style="border-right: #008080 1px solid; border-top: #008080 1px solid;">
					<P align="right">
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSMAIN1</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
					</P>
				</TD>
				<TD vAlign="top" align="left" width="41%" rowspan="2">
					<table>
							<tr>
								<td width="50%">&#160;</td>
								<td>
									<xsl:call-template name="Print"/>&#160;<br/>
									<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_CSMAIN</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>&#160;<br/>
									<a><xsl:attribute name="href">cssearch</xsl:attribute><img src="images/but_advancedsearch.jpg" alt="" border="0"/></a>
								</td>
							</tr>
					</table>
				</TD>
			</TR>
			<TR>
				<TD vAlign="center" style="border-left: #008080 1px solid; border-right: #C0C0C0 1px solid; border-bottom: #008080 1px solid">
					<SELECT style="font-size: 9pt; width: 240px; color: #000000; background-color: #ffffff"  size="1" name="ISSUE_ID" height="20">
										<OPTION value="0" selected="true">All issues</OPTION>
										<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
					</SELECT>
				</TD>
				<TD vAlign="center" style="border-bottom: #008080 1px solid">
					<SELECT style="font-size: 9pt; width: 129px; color: #000000; background-color: #ffffff" size="1" name="DEADLINES"> 
						<OPTION value="0">
							<xsl:if test="$sel_period='' or $sel_period='0'"> 
								<xsl:attribute name="selected"/>
							</xsl:if>
						All deadlines
						</OPTION> 
						<OPTION value="1">
							<xsl:if test="$sel_period='1'"> 
								<xsl:attribute name="selected"/>
							</xsl:if>
						In the next month</OPTION> 
						<OPTION value="2"><xsl:if test="$sel_period='2'"><xsl:attribute name="selected"/></xsl:if>
								In the next 3 months</OPTION> 
						<OPTION value="3"><xsl:if test="$sel_period='3'"><xsl:attribute name="selected"/></xsl:if>In the next 6 months</OPTION> 
						<OPTION value="4"><xsl:if test="$sel_period='4'"><xsl:attribute name="selected"/></xsl:if>Previous months</OPTION>
					</SELECT>
				</TD>
				<TD vAlign="center" align="right" style="border-right: #008080 1px solid; border-bottom: #008080 1px solid">

					<input type="hidden" name="CLIENT_ID">
						<xsl:attribute name="value">
							<xsl:value-of select="$sel_client"/>
						</xsl:attribute>
					</input>
					<input type="hidden" name="COUNTRY_ID">
						<xsl:attribute name="value">
							<xsl:value-of select="$sel_country"/>
						</xsl:attribute>
					</input>

					<xsl:call-template name="go"/>
				</TD>
			</TR>
			</TABLE>
		</TD>
		</tr>
		</table>
		</form>
	<xsl:variable name="recCount"><xsl:value-of select="count(child::RowSet[@Name='Main']/Row/T_OBLIGATION)"/></xsl:variable>
	<div class="smallfont" style="font-size: 8pt; font-weight: bold">[<xsl:value-of select="$recCount"/> record(s) returned]</div>	<br/>
<table width="100%">
	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">
	<thead>
		<tr class="sortable">
		
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Title of the reporting obligation'"/>
				<xsl:with-param name="text" select="'Reporting obligation'"/>
				<xsl:with-param name="sorted" select="'T_OBLIGATION.TITLE'"/>
				<xsl:with-param name="width" select="'25%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- client -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Reporting client'"/>
				<xsl:with-param name="text" select="'Reporting to'"/>
				<xsl:with-param name="sorted" select="'CLIENT_DESCR'"/>
				<xsl:with-param name="width" select="'14%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- deadline -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Date of delivery'"/>
				<xsl:with-param name="text" select="'Deadline'"/>
				<xsl:with-param name="sorted" select="'NEXT_REPORTING, NEXT_DEADLINE'"/>
				<xsl:with-param name="width" select="'9%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- dl2 -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Next deadline'"/>
				<xsl:with-param name="text" select="'Next DL'"/>
				<xsl:with-param name="sorted" select="'DEADLINE2'"/>
				<xsl:with-param name="width" select="'9%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- responsible -->
			<xsl:call-template name="createSortable">
				<xsl:with-param name="title" select="'Responsible person or role'"/>
				<xsl:with-param name="text" select="'Responsible'"/>
				<xsl:with-param name="sorted" select="'ROLE_DESCR'"/>
				<xsl:with-param name="width" select="'13%'"/>
				<xsl:with-param name="cur_sorted" select="$sortorder"/>
			</xsl:call-template>
	<!-- empty td -->
		<th scope="col" class="notsorted" title="Show deliveries in the repository" width="8%">Deliveries</th>
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

  <tr valign="top">
		<xsl:attribute name="class">
				<xsl:if test="position() mod 2 = 0">even</xsl:if>
		</xsl:attribute>

	
	<td  valign="top">
		<span class="rowitem">
			<a>
				<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;mode=A</xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="T_OBLIGATION/TITLE"/></xsl:attribute>
				<xsl:call-template name="short">
					<xsl:with-param name="text" select="T_OBLIGATION/TITLE"/>
					<xsl:with-param name="length">40</xsl:with-param>
				</xsl:call-template>
			</a>
			<xsl:if test="T_SOURCE/SOURCE_CODE!=''">
				<br/>
				(<a>
					<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
					<xsl:value-of select="T_SOURCE/SOURCE_CODE"/>
				</a>)
			</xsl:if>
		</span>&#160;
	</td>

	<td valign="top">
		<span class="rowitem">
			<a>
				<xsl:attribute name="href">javascript:openPopup('client.jsv', 'id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="T_CLIENT/CLIENT_NAME"/></xsl:attribute>
						<xsl:call-template name="short">
							<xsl:with-param name="text" select="T_CLIENT/CLIENT_DESCR"/>
							<xsl:with-param name="length">20</xsl:with-param>
						</xsl:call-template>
			</a>
		</span>&#160;
	</td>
	<td valign="top">
		<span class="rowitem">
			<xsl:attribute name="title"><xsl:value-of select="T_OBLIGATION/DEADLINE"/></xsl:attribute>
			<font>
			<xsl:choose>
			<xsl:when test="T_OBLIGATION/NEXT_DEADLINE=''">
				<xsl:attribute name="color">#006666</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="color">#000000</xsl:attribute>
			</xsl:otherwise>			
			</xsl:choose>

			<xsl:call-template name="short">
				<xsl:with-param name="text" select="T_OBLIGATION/DEADLINE"/>
				<xsl:with-param name="length">10</xsl:with-param>
			</xsl:call-template>
			
			<!--xsl:value-of select="T_OBLIGATION/DEADLINE"/-->

			</font>
		</span>&#160;
	</td>
	<td valign="top">
		<span class="rowitem">
				<xsl:if test="T_OBLIGATION/DEADLINE2 != '' ">	
					<xsl:value-of select="T_OBLIGATION/DEADLINE2"/>
				</xsl:if>
		</span>&#160;
	</td>

	<td valign="top">
		<span class="rowitem">
			<xsl:if test="T_OBLIGATION/RESPONSIBLE_ROLE != ''">
				<xsl:choose>
				<xsl:when test="RESPONSIBLE/ROLE_DESCR=''">
					<xsl:attribute name="title"><xsl:value-of select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-',T_SPATIAL/SPATIAL_TWOLETTER)"/></xsl:attribute>
					<xsl:call-template name="short">
						<xsl:with-param name="text" select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-',T_SPATIAL/SPATIAL_TWOLETTER)"/>
						<xsl:with-param name="length">25</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
				<a>
				<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_URL"/>')</xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="RESPONSIBLE/ROLE_DESCR"/></xsl:attribute>
							<xsl:call-template name="short">
								<xsl:with-param name="text" select="RESPONSIBLE/ROLE_DESCR"/>
								<xsl:with-param name="length">15</xsl:with-param>
							</xsl:call-template>
				</a>&#160;
				<a><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
					<img src="images/details.jpg" alt="Additional details for logged-in users" border="0"/>
				</a>
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</span>&#160;
	</td>
	<td valign="top">
		<span class="rowitem">
			<xsl:choose>
					<xsl:when test="contains(T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS, concat(',' , T_SPATIAL/PK_SPATIAL_ID , ',') )='true'">
						<a window="delivery">
						<xsl:attribute name="href">javascript:openPopup('csdeliveries', 'ACT_DETAILS_ID=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;COUNTRY_ID=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>')</xsl:attribute>
							Show list
						</a>
					</xsl:when>
					<xsl:otherwise>
							None
					</xsl:otherwise>
			</xsl:choose>
		</span>&#160;
	</td>
	<xsl:if test="$sel_country='0' or $sel_country=''">
		<td  valign="top">
			<span class="rowitem">
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
			</span>&#160;
		</td>
	</xsl:if>
  </tr>
</xsl:for-each>

</xsl:if>
<!-- end of table row -->
  </table>
	<!-- 8 -->


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
							<xsl:attribute name="selected"/>
					</xsl:if>
				<xsl:value-of select="ISSUE_NAME"/>
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
		<xsl:value-of select="concat($uri,'?',substring($params,2))"/>
	</xsl:template>

</xsl:stylesheet>
