<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>

<xsl:template match="/">


<html lang="en"><head><title>Status of participation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript" src="script/util.js"></script>
	</head>
	<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<div style="margin-left:13">
<br/>

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
					<td width="70"><img src="images/logo.jpg" alt="" height="62" width="66" border="0"/></td>
				</tr>
			</table>

	<!-- green line table -->
	<table cellspacing="0" cellpadding="0" width="700" border="0">
		<tr><td align="bottom" width="700" background="images/bar_filled.jpg" height="25">&#160;</td></tr>
		<tr height="25"><td></td></tr>
	</table>


<table width="680" border="0">
	<tr>
		<td colspan="3" align="right" valign="top">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_SPATIALHISTORY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td>
	</tr>
<tr>
	<td width="3%"></td>
	<td width="35%" valign="top" align="right"><span class="head1">Status of participation:</span></td>
	<td align="62%"></td>
</tr>
<tr>
	<td></td>
	<td align="right"><span class="head0">Reporting obligation:</span></td><td><xsl:value-of select="/XmlData/RowSet/Row/T_OBLIGATION/TITLE"/></td>
</tr>
<tr valign="top">
	<td></td>
	<td align="right"><span class="head0">Client organisation: </span></td><td><xsl:value-of select="/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
</tr>
</table>
<br/>
<table width="680" cellspacing="0" border="0">
	<tr>
		<TD width="30%"  style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" bgColor="#ffffff" align="left">
					<span class="headsmall"><font title="Reporting country" face="Verdana" color="#000000" size="1">Country</font></span></TD>
		<TD width="25%"  style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" bgColor="#ffffff" align="left">
				<span class="headsmall"><font title="Status" face="Verdana" color="#000000" size="1">Status</font></span></TD>
		<TD width="45%"  style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #008080 1px solid;"
						vAlign="center" bgColor="#ffffff" align="left">
				<span class="head0"><font title="Period when the country participated in reporting" face="Verdana" color="#000000" size="1">Participation period</font></span></TD>
	</tr>

<xsl:for-each select="XmlData/RowSet/Row">
<tr valign="top">
		<xsl:attribute name="bgColor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>
<td style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top"><SPAN class="Mainfont">
			 <FONT face="Verdana" size="2">
	<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
		</FONT></SPAN>
</td>
<td style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top"><SPAN class="Mainfont">
			 <FONT face="Verdana" size="2">
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='Y'">
			Voluntary reporting
		</xsl:when>
		<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='N'">
			Formal reporting
		</xsl:when>
	</xsl:choose>
		</FONT></SPAN>
</td>
<td style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top"><SPAN class="Mainfont">
			 <FONT face="Verdana" size="2">
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/START_DATE='' or T_SPATIAL_HISTORY/START_DATE='00/00/0000'">
			Prior to start of ROD (2003)
		</xsl:when>
		<xsl:otherwise>
			From <xsl:value-of select="T_SPATIAL_HISTORY/START_DATE"/>
		</xsl:otherwise>
	</xsl:choose>
	to 
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/END_DATE != ''">
			<xsl:value-of select="T_SPATIAL_HISTORY/END_DATE"/>
		</xsl:when>
		<xsl:otherwise>
				present
		</xsl:otherwise>
	</xsl:choose>
		</FONT></SPAN>
</td>

</tr>	
</xsl:for-each>
</table>
</div>
</body>
</html>

</xsl:template>
</xsl:stylesheet>
