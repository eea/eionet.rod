<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:include href="util.xsl"/>
<xsl:template match="/">

<html lang="en"><head><title>Indicators</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/>
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript" src="script/util.js"></script>
	</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">
	<table cellspacing="0" cellpadding="0" border="0" width="720">
		<tr height="30">
			<td width="20"></td>
			<td width="700">
				<table width="700" border="0">
					<tr>
						<td width="20">&#160;</td>
						<td width="560"><font color="#006666" face="Arial"><strong><span class="head2">
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
		<tr>
				<td align="bottom" width="700" background="images/bar_filled.jpg" height="25">&#160;</td>
		</tr>
		<tr height="25"><td></td></tr>
	</table>

	<!-- header -->
	<xsl:for-each select="XmlData/RowSet[@Name='Activity']/Row">
	<table width="700" border="0">
	<tr>
		<td colspan="2" align="right">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_INDOCATORS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td>
	</tr>
	<tr>
		<td colspan="2">
   		<span class="head1">Indicators making use of information provided by this reporting obligation</span>
		</td>
	</tr>
	<tr><td></td><td>&#160;</td></tr>
		<tr valign="top">
			<td width="30%" align="right"><span class="head0">Reporting obligation:</span></td>
			<td width="70%"><xsl:value-of select="T_OBLIGATION/TITLE"/></td>
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
	 
<TABLE style="BORDER: #008080 1px solid" cellSpacing="0" cellPadding="0" width="700" border="0">
<TR>
	<th width="20%" style="BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN title="Indicator number" class="headsmall"><B><FONT size="1">&#160;Title</FONT></B></SPAN>
	</th>
	<th width="20%"  style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN class="headsmall"><B><FONT title="Indicator title" size="1">&#160;Number</FONT></B></SPAN>
	</th>
	<th width="40%"  style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN class="headsmall"><B><FONT title="Indicator URL" size="1">&#160;URL</FONT></B></SPAN>
	</th>
	<th width="20%"  style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN class="headsmall"><B><FONT title="Indicator owner organisation" size="1">&#160;Owner</FONT></B></SPAN>
	</th>
</TR>
<span class="Mainfont">
	<xsl:for-each select="XmlData/RowSet[@Name='Indicators']/Row">
	<TR>
		<xsl:attribute name="bgColor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>

	<TD style="BORDER-TOP: #c0c0c0 1px solid"  vAlign="top">
		<xsl:value-of select="T_INDICATOR/TITLE"/>&#160;
	</TD>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-TOP: #c0c0c0 1px solid" vAlign="top">
		<xsl:value-of select="T_INDICATOR/NUMBER"/>&#160;
	</TD>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-TOP: #c0c0c0 1px solid"  vAlign="top">
		<xsl:if test="T_INDICATOR/URL!=''">
			<a target="_blank">
				<xsl:attribute name="href"><xsl:value-of select="T_INDICATOR/URL"/></xsl:attribute>
				<xsl:value-of select="T_INDICATOR/URL"/>&#160;
			</a>
		</xsl:if>
	</TD>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-TOP: #c0c0c0 1px solid" valign="left">
		<xsl:value-of select="T_INDICATOR/OWNER"/>&#160;	</TD>
</TR>
</xsl:for-each>
</span>

</TABLE>
<!--/xsl:for-each-->
</td>
</tr>

</table>
</body>
</html>

</xsl:template>
</xsl:stylesheet>
