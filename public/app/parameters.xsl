<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>

<xsl:template match="/">

<html lang="en"><head><title>Parameters</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
	<table width="100%" border="0">
	<tr>
		<td width="33%" align="right">
		</td>
		<td width="67%" align="right">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_PARAMETERS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="2">
   		<span class="head1">Overview of parameters</span>
		</td>
	</tr>
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
			<td>
				<xsl:choose>
					<xsl:when test="T_OBLIGATION/REPORT_FORMAT_URL!=''">
						<a target="RA_guidelines">
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

	<br/>
	 
<!-- oneCountry=0 one country, one country = 1 all countries -->
<xsl:if test="T_OBLIGATION/PARAMETERS != ''">
<TABLE cellspacing="0" cellpadding="0" width="700" border="1">

<TR>
	<th width="100%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN title="Parameters" class="headsmall"><B><FONT size="1">&#160;Parameters</FONT></B></SPAN>
	</th>
</TR>
<TR>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<xsl:call-template name="break">
	     <xsl:with-param name="text" select="T_OBLIGATION/PARAMETERS"/>
		</xsl:call-template>
	</TD>
</TR>

</TABLE>
</xsl:if>

</xsl:for-each>

<xsl:if test="count(XmlData/RowSet[@Name='Activity']/Row/DDPARAM) > 0">
<br/>
<TABLE cellspacing="0" cellpadding="0" width="700" border="0">

<TR>
	<th colspan="3" width="100%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN class="headsmall"><B><FONT size="1">&#160;Parameters from Data Dictionary</FONT></B></SPAN>
	</th>
</TR>
<TR>
	<td width="40%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN class="headsmall"><B><FONT size="1">&#160;Parameter name</FONT></B></SPAN>
	</td>
	<td width="30%" style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN class="headsmall"><B><FONT size="1">&#160;Table name</FONT></B></SPAN>
	</td>
	<td style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #008080 1px solid" vAlign="center" bgColor="#ffffff" align="left">
			<SPAN class="headsmall"><B><FONT size="1">&#160;Dataset name</FONT></B></SPAN>
	</td>
</TR>
<xsl:for-each select="XmlData/RowSet[@Name='Activity']/Row/DDPARAM">
<TR>
	<xsl:attribute name="bgColor">
		<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
	</xsl:attribute>
	<TD style="BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
		<span class="rowitem"><a href="{ELEMENT_URL}" title="View parameter details in Data Dictionary" target="_blank"><xsl:value-of select="ELEMENT_NAME"/></a></span>
	</TD>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top"><span class="rowitem"><xsl:value-of select="TABLE_NAME"/></span></TD>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid; BORDER-RIGHT: #008080 1px solid"  vAlign="top"><span class="rowitem"><xsl:value-of select="DATASET_NAME"/></span></TD>
</TR>
</xsl:for-each>

</TABLE>
<br/>
</xsl:if>

<!--/xsl:for-each-->
</td>
</tr>

</table>
</body>
</html>

</xsl:template>
</xsl:stylesheet>
