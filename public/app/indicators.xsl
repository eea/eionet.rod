<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">

<html lang="en"><head><title>Indicators</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
	<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />		
	<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" title="Eionet 2007 style" />
	<link rel="stylesheet" type="text/css" href="eionet2007.css" media="screen" title="Eionet 2007 style"/>
	<script type="text/javascript" src="script/util.js"></script>
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
					<td width="70"><img src="images/logo.jpg" id="logo" alt="" height="62" width="66" border="0"/></td>
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
					<a>
						<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a>
			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Other clients using this reporting:</span></td>
			<td>
				<xsl:for-each select="SubSet[@Name='CCClients']/Row">
					<a>
						<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a><br/>
				</xsl:for-each>

			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Reporting guidelines:</span></td>
			<td>
				<xsl:choose>
					<xsl:when test="T_OBLIGATION/REPORT_FORMAT_URL!=''">
						<a>
							<xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
							<xsl:choose>
								<xsl:when test="T_OBLIGATION/FORMAT_NAME!=''">
									<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</table>
	</xsl:for-each>

	<br/>
	 
<table style="border: #008080 1px solid" cellspacing="0" cellpadding="0" width="700" border="0">
<tr>
	<th width="20%" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span title="Indicator number" class="headsmall"><b><font size="1">&#160;Title</font></b></span>
	</th>
	<th width="20%"  style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span class="headsmall"><b><font title="Indicator title" size="1">&#160;Number</font></b></span>
	</th>
	<th width="40%"  style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span class="headsmall"><b><font title="Indicator URL" size="1">&#160;URL</font></b></span>
	</th>
	<th width="20%"  style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span class="headsmall"><b><font title="Indicator owner organisation" size="1">&#160;Owner</font></b></span>
	</th>
</tr>
<span class="Mainfont">
	<xsl:for-each select="XmlData/RowSet[@Name='Indicators']/Row">
	<tr>
		<xsl:attribute name="bgcolor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>

	<td style="border-top: #c0c0c0 1px solid"  valign="top">
		<xsl:value-of select="T_INDICATOR/TITLE"/>&#160;
	</td>
	<td style="border-left: #c0c0c0 1px solid; border-top: #c0c0c0 1px solid" valign="top">
		<xsl:value-of select="T_INDICATOR/NUMBER"/>&#160;
	</td>
	<td style="border-left: #c0c0c0 1px solid; border-top: #c0c0c0 1px solid"  valign="top">
		<xsl:if test="T_INDICATOR/URL!=''">
			<a>
				<xsl:attribute name="href"><xsl:value-of select="T_INDICATOR/URL"/></xsl:attribute>
				<xsl:value-of select="T_INDICATOR/URL"/>&#160;
			</a>
		</xsl:if>
	</td>
	<td style="border-left: #c0c0c0 1px solid; border-top: #c0c0c0 1px solid" valign="left">
		<xsl:value-of select="T_INDICATOR/OWNER"/>&#160;	</td>
</tr>
</xsl:for-each>
</span>

</table>
<!--/xsl:for-each-->
</td>
</tr>

</table>
</body>
</html>

</xsl:template>
</xsl:stylesheet>
