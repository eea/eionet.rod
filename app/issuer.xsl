<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:template match="/">

<html lang="en"><head><title>Legal instrument issuer information</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript">
					
Net=1;

var browser = document.all ? 'E' : 'N';


			</script>
			</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">
<br/>
<table width="400">
<tr><td width="100"></td><td width="300">
			<h2>Legal Instrument issuer</h2>
</td></tr>
</table>


<br/><br/>
<table width="400">

		<tr height="30">
			<td width="50"></td>
			<td align="right" width="100"><b>Name:</b></td>
			<td align="left" width="250"><xsl:value-of select="XmlData/RowSet/Row/T_ISSUER/ISSUER_NAME"/></td>
		</tr>
		<tr height="30">
			<td width="50"></td>
			<td align="right" width="100"><b>Address:</b></td>
			<td align="left" width="250"><xsl:value-of select="XmlData/RowSet/Row/T_ISSUER/ISSUER_ADDRESS"/></td>
		</tr>
		<tr height="30">
			<td width="50"></td>
			<td align="right" width="100"><b>Acronym:</b></td>
			<td align="left" width="250"><xsl:value-of select="XmlData/RowSet/Row/T_ISSUER/ISSUER_ACRONYM"/></td>
		</tr>
		<tr height="30">
			<td width="50"></td>
			<td align="right" width="100"><b>URL:</b></td>
			<td align="left" width="250">
				<xsl:if test="XmlData/RowSet/Row/T_ISSUER/ISSUER_URL != ''">
				<a target="_new">
					<xsl:attribute name="href">
						<xsl:value-of select="XmlData/RowSet/Row/T_ISSUER/ISSUER_URL"/>
					</xsl:attribute>
					<xsl:value-of select="XmlData/RowSet/Row/T_ISSUER/ISSUER_URL"/>
				</a>
				</xsl:if>
			</td>
		</tr>
		<tr height="30">
			<td width="50"></td>
			<td align="right" width="100"><b>Description:</b></td>
			<td align="left" width="250"><xsl:value-of select="XmlData/RowSet/Row/T_ISSUER/DESCRIPTION"/></td>
		</tr>

</table>


</body>
</html>

</xsl:template>
</xsl:stylesheet>
