<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!--xsl:include href="common.xsl"/-->
<xsl:template match="/">

<html lang="en">
<head><title>History of countries</title>
<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>
</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">
<br/>
<span class="head1">Reporting activitiy: <xsl:value-of select="/XmlData/RowSet/Row/T_ACTIVITY/TITLE"/></span>
<table width="100%" cellspacing="3pts" border="1">
	<tr>
		<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF">Country</font></span></td>
		<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF">Reporting</font></span></td>
		<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF">Joined</font></span></td>
		<td bgcolor="#646666" align="center" width="35%"><span class="head0"><font color="#FFFFFF">Left</font></span></td>
	</tr>

<xsl:for-each select="XmlData/RowSet/Row">
<tr valign="top">
<td>
	<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
</td>
<td>
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='Y'">
			Voluntary reporting
		</xsl:when>
		<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='N'">
			Required
		</xsl:when>
	</xsl:choose>
</td>
<td>
	<xsl:value-of select="T_SPATIAL_HISTORY/START_DATE"/>
</td>
<td>
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/END_DATE != ''">
			<xsl:value-of select="T_SPATIAL_HISTORY/END_DATE"/>
		</xsl:when>
		<xsl:otherwise>
			<i>Active</i>
		</xsl:otherwise>
	</xsl:choose>
</td>

</tr>	
</xsl:for-each>
</table>

</body>
</html>

</xsl:template>
</xsl:stylesheet>
