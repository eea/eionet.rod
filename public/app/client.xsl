<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!--xsl:include href="common.xsl"/-->
<xsl:template match="/">

<html lang="en"><head><title>Client information</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript">
					
		Net=1;

		var	browser = document.all ? 'E' : 'N';

	</script>
</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">
<br/>
<!-- main header table -->
<table width="650" border="0">
	<tr>
		<td width="20">&#160;</td>
		<td width="560">
				<font color="#006666" face="Arial"><strong><span class="head2">
					<xsl:call-template name="FirstHeading"/>
				</span></strong></font>
			<font color="#006666" face="Arial" size="2">
				<strong><span class="head0">
					<xsl:call-template name="SecondHeading"/>
				</span></strong>
			</font>

		</td>
		<td width="70"><img src="images/logo.jpg" alt="" height="62" width="66" border="0"/></td>
	</tr>
</table>

<table cellspacing="0" cellpadding="0" width="650" border="0">
	<tr>
     	<td align="bottom" width="650" background="images/bar_filled.jpg" height="25">&#160;</td>
	</tr>
	<tr height="25"><td></td></tr>
</table>
<table>
	<tr>
		<td width="20"></td>
		<td width="630" align="left">
			<h2>Reporting client or issuer details</h2>
		</td>
	</tr>
</table>
<table width="650" border="0">
		<tr height="30" valign="top">
			<td width="20"></td>
			<td align="right" width="100"><b>Name:</b></td>
			<td align="left" width="490"><xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
			<td width="10"></td>
		</tr>
		<tr height="30" valign="top">
			<td></td>
			<td align="right"><b>Acronym:</b></td>
			<td align="left" ><xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM"/></td>
			<td></td>
		</tr>
		<tr height="30" valign="top">
			<td></td>
			<td align="right"><b>Address:</b></td>
			<td align="left"><xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/CLIENT_ADDRESS"/></td>
			<td></td>
		</tr>
		<tr height="30" valign="top">
			<td></td>
			<td align="right"><b>Postal code:</b></td>
			<td align="left"><xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/POSTAL_CODE"/></td>
			<td></td>
		</tr>
		<tr height="30" valign="top">
			<td></td>
			<td align="right"><b>City:</b></td>
			<td align="left"><xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/CITY"/></td>
			<td></td>
		</tr>
		<tr height="30" valign="top">
			<td></td>
			<td align="right"><b>Country:</b></td>
			<td align="left"><xsl:value-of select="XmlData/RowSet/Row/T_SPATIAL/SPATIAL_NAME"/></td>
		</tr>
		<tr height="30" valign="top">
			<td></td>
			<td align="right"><b>Homepage:</b></td>
			<td align="left">
				<xsl:if test="XmlData/RowSet/Row/T_CLIENT/CLIENT_URL != ''">
				<a target="_new">
					<xsl:attribute name="href">
						<xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/CLIENT_URL"/>
					</xsl:attribute>
					<xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/CLIENT_URL"/>
				</a>
				</xsl:if>
			</td>
		</tr>
		<tr height="30" valign="top">
			<td></td>
			<td align="right"><b>Description:</b></td>
			<td align="left"><xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/DESCRIPTION"/></td>
		</tr>

</table>


</body>
</html>
</xsl:template>
</xsl:stylesheet>
