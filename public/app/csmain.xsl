<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>


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
			<xsl:value-of select="substring-after(/XmlData/xml-query-string,'DEADLINES=')"/>
		</xsl:variable>


<xsl:template match="XmlData">

<html lang="en">
<head><title>Country Services</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>

<script language="JavaScript" src="script/util.js"></script>
<script language="JavaScript">

<![CDATA[


	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
	}


]]>
				var browser = document.all ? 'E' : 'N';
				var picklist = new Array();

				
</script>
</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

<xsl:if test="$printmode='N'">
	<table border="0" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td height="25" background="images/bar_filled.jpg" width="20" align="bottom"></td>
		<td height="25" background="images/bar_filled.jpg" width="600">
			 <table height="8" cellSpacing="0" cellPadding="0" background="" border="0">
			 <tr valign="top">
				<td valign="bottom"><a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a></td>
				<td width="28" valign="top">
					<img src="images/bar_hole.jpg" width="28" height="24"/></td>
					<td valign="bottom"><a href="index.html"><span class="barfont">ROD</span></a></td>
					<td width="28" ><img src="images/bar_hole.jpg" width="28" height="24"/></td>
					<td valign="bottom"><a href="deliveries.jsv"><span class="barfont">Deadlines</span></a></td>

					<td width="28" valign="top"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
					<td valign="bottom"><span class="barfont">Country</span></td>
					<td width="28" valign="bottom"><img src="images/bar_dot.jpg" width="28" height="25"/></td>
				 </tr>
				</table>
			 </td></tr><tr><td></td></tr>
		</table>
	</xsl:if>
	<!-- main -->
	<table  cellspacing="0" cellpadding="0" border="0">
	<tr valign="top" height="500">
	<td>

	<div style="margin-left:13">
	

	<table border="0" width="98%">
	<tr><td width="90%">
    <span class="head1"><span lang="en-us">Reporting overview: </span>
		<xsl:if test="$sel_country!='0'">
			<xsl:value-of select="//RowSet[@Name='CountryData']/Row/T_SPATIAL/SPATIAL_NAME"/>
		</xsl:if>
		<xsl:if test="$sel_issue!='0' and $sel_issue!=''">
		 [<xsl:value-of select="//RowSet[@Name='IssueData']/Row/T_ISSUE/ISSUE_NAME"/>] 
 		</xsl:if>
		<xsl:if test="$sel_client!='0' and $sel_client!='' ">
		 <br/>for: <xsl:value-of select="//RowSet[@Name='ClientData']/Row/T_CLIENT/CLIENT_NAME"/>
 		</xsl:if>
		</span>
		</td><td width="*" align="right" valign="top">
		</td></tr>
	</table>
	<br/>
	<!-- 8 -->
<xsl:if test="$printmode='N'">
	<form name="ff" method="get" action="csmain">
	<table border="0"><tr>
	<TD width="771">

	<TABLE cellSpacing="0" cellPadding="3" width="100%" border="0">
			<TR>
				<TD vAlign="center" width="33%" bgColor="#ffffff" style="BORDER-LEFT: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid; BORDER-TOP: #008080 1px solid;"><FONT face="Verdana" size="1"><B>Select issue:</B></FONT></TD>
				<TD vAlign="center" width="19%" bgColor="#ffffff" style="BORDER-TOP: #008080 1px solid;"><B><FONT face="Verdana" size="1">Select deadline:</FONT></B></TD>
				<TD vAlign="center" width="7%" bgColor="#ffffff" style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid;">
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
									<a>
										<xsl:attribute name="href">cssearch</xsl:attribute>
										<img src="images/but_advancedsearch.jpg" alt="" border="0"/>
									</a>&#160;
								</td>
							</tr>
					</table>
				</TD>
			</TR>
			<TR>
				<TD vAlign="center" style="BORDER-LEFT: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid; BORDER-BOTTOM: #008080 1px solid">
					<SELECT style="FONT-SIZE: 8pt; WIDTH: 240px; COLOR: #000000; HEIGHT: 23px; BACKGROUND-COLOR: #ffffff"  size="1" name="ISSUE_ID" height="20">
										<OPTION value="0" selected="true">All issues</OPTION>
										<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
					</SELECT>
				</TD>
				<TD vAlign="center" style="BORDER-BOTTOM: #008080 1px solid">
					<SELECT style="FONT-SIZE: 8pt; WIDTH: 129px; COLOR: #000000; HEIGHT: 33px; BACKGROUND-COLOR: #ffffff" size="1" name="DEADLINES"> 
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
				<TD vAlign="center" align="right" style="BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid">

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
	</xsl:if>
	&#160;
	<table width="777" cellspacing="0" border="0">

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">
	<TR>
	<!-- country -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="26%" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TR><TD><SPAN class="headsmall"><B><FONT title="Reporting Obligation" face="Verdana" color="#000000" size="1">Reporting Obligation</FONT></B></SPAN></TD>
  				<TD><P align="right"><MAP name="FPMap1"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('T_OBLIGATION.TITLE DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z"  coords="1,13,16,21" href="javascript:setOrder('T_OBLIGATION.TITLE')"/></MAP>
								<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap1" border="0"/></P>
				</TD></TR>
		</TABLE>
	</TD>

	<!-- client -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="15%" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Reporting client" face="Verdana" color="#000000" size="1">Reporting to</FONT></B></SPAN></TD>
				<TD>
						<P align="right">
							<MAP name="FPMap2">
								<AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('CLIENT_DESCR DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z"  coords="1,13,16,21" href="javascript:setOrder('CLIENT_DESCR')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap2" border="0"/>
						</P>
					</TD>
			</TR>
		</TABLE>
	</TD>
	<!-- deadline -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="9%" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Deadline</FONT></B></SPAN></TD>
				<TD>
						<P align="right">
							<MAP name="FPMap3">
								<AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('NEXT_DEADLINE, NEXT_REPORTING DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z"  coords="1,13,16,21" href="javascript:setOrder('NEXT_DEADLINE, NEXT_REPORTING')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap3" border="0"/>
						</P>
					</TD>
			</TR>
		</TABLE>
	</TD>
	<!-- dl2 -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="9%" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Next deadline" face="Verdana" color="#000000" size="1">Next DL</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap4"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('DEADLINE2 DESC')"/>
						<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('DEADLINE2')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap4" border="0"/></P>
				</TD>
			</TR>
		</TABLE>
	</TD>

	<!-- responsible -->
	<TD style="BORDER-TOP: #008080 1px solid; BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="13%" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Responsible person or role" face="Verdana" color="#000000" size="1">Responsible</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap5"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('ROLE_DESCR DESC')"/>
						<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('ROLE_DESCR')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap5" border="0"/></P>
				</TD>
			</TR>
		</TABLE>
	</TD>

	<!-- empty td -->
	<TD style="BORDER-TOP: #008080 1px solid; BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="8%" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Show deliveries in the repository" face="Verdana" color="#000000" size="1">Deliveries</FONT></B></SPAN>
				</TD>
				<TD>
				</TD>
			</TR>
		</TABLE>
	</TD>
	<xsl:if test="$sel_country='0' or $sel_country=''">
	<TD style="BORDER-TOP: #008080 1px solid; BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="10%" bgColor="#ffffff">
		<TABLE cellSpacing="0" width="100%" border="0">
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Country</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap6"><AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('SPATIAL_NAME DESC')"/>
						<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('SPATIAL_NAME')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap6" border="0"/></P>
				</TD>
			</TR>
		</TABLE>
	</TD>

	</xsl:if>

	<!-- END of header ROW -->
	</TR>	
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)=0">
		<b>No deliveries match the search criteria</b>
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">

<!-- table row start -->
<xsl:for-each select="RowSet[@Name='Main']/Row">

  <tr valign="top">
		<xsl:attribute name="bgColor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>

	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="smallfont">
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
	</TD>

	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="smallfont">
			<a>
				<xsl:attribute name="href">javascript:openPopup('client.jsv', 'id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="T_CLIENT/CLIENT_NAME"/></xsl:attribute>
						<xsl:call-template name="short">
							<xsl:with-param name="text" select="T_CLIENT/CLIENT_DESCR"/>
							<xsl:with-param name="length">20</xsl:with-param>
						</xsl:call-template>
			</a>
		</span>&#160;
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="smallfont">
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
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="smallfont">
				<xsl:if test="T_OBLIGATION/DEADLINE2 != '' ">	
					<xsl:value-of select="T_OBLIGATION/DEADLINE2"/>
				</xsl:if>
		</span>&#160;
	</TD>

	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="smallfont">
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
				<img src="images/details.jpg" alt="Additional details for logged-in users">
					<xsl:attribute name="onClick">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
				</img>
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</span>&#160;
	</TD>
	<TD vAlign="top">
		<xsl:attribute name="style">
			<xsl:choose>
				<xsl:when test="$sel_country ='0' or $sel_country=''">BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid</xsl:when>
				<xsl:otherwise>BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<span class="smallfont">
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
	</TD>
	<xsl:if test="$sel_country='0' or $sel_country=''">
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="smallfont">
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
		</span>&#160;
	</TD>
	</xsl:if>
  </tr>
</xsl:for-each>

</xsl:if>
<!-- end of table row -->
	<tr><td colspan="6">
		<br/>
		
		<xsl:variable name="recCount"><xsl:value-of select="count(child::RowSet[@Name='Main']/Row/T_OBLIGATION)"/></xsl:variable>
		<b><xsl:value-of select="$recCount"/> record(s) returned</b>
		<xsl:call-template name="CommonFooter"/>
	</td></tr>
  </table>
	<!-- 8 -->


  </div>
	</td>
	</tr>
</table>
<!-- main -->

<table>
<tr><td>
</td></tr>
</table>
		
	</body>
	</html>

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


</xsl:stylesheet>
