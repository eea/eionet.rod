<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>

<xsl:template match="/">

<html lang="en"><head><title>Status of deliveries</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript" src="script/util.js"></script>
	<script language="JavaScript">
			function setOrder(fld) {
				changeParamInString(document.URL,'ORD',fld)
			}
	</script>
	</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

<!-- general table for all the screen -->
<xsl:variable name="allCountries"><xsl:value-of select="count(child::XmlData/RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>
	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet[@Name='Main']/@permissions"/>
	</xsl:variable>

<table cellspacing="0" cellpadding="0" border="0" width="720">
	<tr height="30">
		<td width="20"></td>
		<td width="700">

			<div id="workarea">
			<!-- header -->
			<!-- quick fix ... show header tables only if 1st position -->
			<table width="700" border="0">
				<tr>
					<td width="20">&#160;</td>
					<td width="560">
							<font color="#006666" face="Arial"><strong><span class="head2">
								<xsl:call-template name="FirstHeading"/>
							</span></strong></font>
							<br/>
							<font color="#006666" face="Arial" size="2">
							<strong><span class="head0">
								<xsl:call-template name="SecondHeading"/>
							</span></strong>
						</font>

					</td>
					<td width="70"><a href="http://www.eea.eu.int" target="_blank"><img src="images/logo.jpg" id="logo" alt="" height="62" width="66" border="0"/></a></td>
				</tr>
			</table>
	<!-- green line table -->
	<table cellspacing="0" cellpadding="0" width="700" border="0">
		<tr>
				<td align="bottom" width="700" background="images/bar_filled.jpg" height="25">&#160;</td>
		</tr>
		<tr height="25"><td></td></tr>
	</table>

	<!-- header -->
	<xsl:for-each select="XmlData/RowSet[@Name='RA']/Row">
	<table width="100%" border="0">
	<tr>
		<td colspan="2" align="right" valign="top">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_DELIVERIES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td>
	</tr>
	<tr>
		<td width="35%" align="right" valign="top">
   		<span class="head1">Status of deliveries:</span>
		</td>
		<td width="65%"> <span class="head1">
			<xsl:choose>
				<xsl:when test="$allCountries=0">
					<xsl:value-of select="//XmlData/RowSet[@Name='Main']/Row/T_SPATIAL/SPATIAL_NAME"/>
				</xsl:when>
				<xsl:otherwise>
					all countries
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</td></tr>
	<xsl:if test="contains($permissions, ',/Admin/Harvest:v,')='true'">
		<tr><td></td><td><i>last harvested: <xsl:value-of select="T_OBLIGATION/LAST_HARVESTED"/>&#160;</i></td></tr>
	</xsl:if>
	<tr><td></td><td>&#160;</td></tr>
		<tr valign="top">
			<td align="right"><span class="head0">Reporting obligation:</span></td>
			<td ><xsl:value-of select="T_OBLIGATION/TITLE"/></td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Reporting frequency:</span></td>
			<td>
				<xsl:call-template name="RAReportingFrequency"/>
			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Client organisation:</span></td>
			<td>
					<A>
						<xsl:attribute name="href">javascript:openPopup('client.jsv','id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</A>
			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Other clients using this reporting:</span></td>
			<td>
				<xsl:for-each select="SubSet[@Name='CCClients']/Row">
					<A>
						<xsl:attribute name="href">javascript:openPopup('client.jsv','id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</A><br/>
				</xsl:for-each>

			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Reporting guidelines:</span></td>
			<td><a target="RA_guidelines"><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
				<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/></a></td>
		</tr>
	</table>
	</xsl:for-each>

	<br/>
	 
<!-- oneCountry=0 one country, one country = 1 all countries -->

<TABLE cellspacing="0" cellpadding="0" width="700" border="0">

<!--xsl:if test="position()=1"-->
<TR>
	<!-- contact -->
	<TD width="20%"  style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" bgColor="#ffffff" align="right">

		<TABLE cellspacing="0" width="100%" border="0">
			<TBODY>
			<TR> 
				<TD><SPAN class="headsmall"><B><FONT title="Responsible persons" face="Verdana" color="#000000" size="1">&#160;Contact</FONT></B></SPAN></TD>
				<TD align="right">
						<P align="right">
							<MAP name="FPMap2">
								<AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('ROLE_DESCR DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('ROLE_DESCR')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap2" border="0"/>
						</P>
					</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- delivery title -->
	<TD width="24%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" bgColor="#ffffff">

		<TABLE cellspacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Title of delivery" face="Verdana" color="#000000" size="1">&#160;Delivery Title</FONT></B></SPAN></TD>
				<TD>
						<P align="right">
							<MAP name="FPMap3">
								<AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('T_DELIVERY.TITLE DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('T_DELIVERY.TITLE')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap3" border="0"/>
						</P>
					</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- delivery date -->
	<TD width="11%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" bgColor="#ffffff">

		<TABLE cellspacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Delivery Date</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap4"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('UPLOAD_DATE DESC')"/>
						<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('UPLOAD_DATE')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap4" border="0"/></P>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- period covered -->
	<TD width="31%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center"  bgColor="#ffffff">

		<TABLE cellspacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Period covered by this delivery" face="Verdana" color="#000000" size="1">&#160;Period covered</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap5"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('COVERAGE DESC')"/>
						<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('COVERAGE')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap5" border="0"/></P>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- country -->
	<xsl:if test="$allCountries=1">
	<TD width="14%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #008080 1px solid" 
						vAlign="center"  bgColor="#ffffff">

		<TABLE cellspacing="0" width="100%" border="0">
			<TBODY>
			<TR><TD><SPAN class="headsmall"><B><FONT title="Country" face="Verdana" color="#000000" size="1">&#160;Country</FONT></B></SPAN></TD>
  				<TD><P align="right"><MAP name="FPMap1"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('SPATIAL_NAME DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('SPATIAL_NAME')"/></MAP>
								<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap1" border="0"/></P>
				</TD></TR>
			</TBODY>
		</TABLE>
	</TD>
	</xsl:if>

	</TR>


	<xsl:for-each select="XmlData/RowSet[@Name='Main']/Row">
	<TR>
		<xsl:attribute name="bgColor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>

	<TD style="BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top"><SPAN class="Mainfont">

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
</SPAN>
	</TD>
	<TD style="border-left: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid" 
       vAlign="top">
			 <SPAN class="Mainfont">
					<a target="ROD_delivery">
					<xsl:attribute name="href">
						<xsl:value-of select="T_DELIVERY/DELIVERY_URL"/>
					</xsl:attribute>
					<xsl:value-of select="T_DELIVERY/TITLE"/>
					</a>
			</SPAN>
	</TD>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top">
			 <SPAN class="Mainfont">
					<xsl:choose>
					<xsl:when test="T_DELIVERY/UPLOAD_DATE != '0000-00-00'">
						<xsl:value-of select="T_DELIVERY/UPLOAD_DATE"/>
					</xsl:when>
					<xsl:otherwise>
						&lt;No date&gt;
					</xsl:otherwise>
					</xsl:choose>
			 </SPAN>
	</TD>
	<TD valign="left">
		<xsl:attribute name="style">
		<xsl:choose>
		<xsl:when test="$allCountries=1">BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid</xsl:when>
		<xsl:otherwise>BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid;BORDER-RIGHT: #008080 1px solid</xsl:otherwise>
		</xsl:choose>
		</xsl:attribute>

			 <SPAN class="Mainfont">
				<xsl:value-of select="T_DELIVERY/COVERAGE"/>
			 </SPAN>
			 &#160;
	</TD>
	<xsl:if test="$allCountries=1">
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid; BORDER-RIGHT: #008080 1px solid"  vAlign="top">
		<SPAN class="Mainfont"><FONT face="Verdana" size="2"><xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/></FONT></SPAN>
	</TD>
	</xsl:if>
</TR>
</xsl:for-each>

</TABLE>
<!--/xsl:for-each-->

  </div>	
	</td>
	</tr>
<tr><td></td></tr>
</table>
<br/><br/><span class="Mainfont">&#160;&#160;&#160;&#160;Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.</span>
</body>
</html>

</xsl:template>
</xsl:stylesheet>
