<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:template match="/">


<xsl:variable name="item-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ITEM_TYPE"/>
	</xsl:variable>

<html lang="en">
<head><title>History of changes</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>
</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

	<xsl:if test="XmlData/RowSet/Row/T_HISTORY/ITEM_ID != 0">
	<table border="0" width="100%">
		<tr>
			<td width="60%" align="right">
		<span class="head0">
			History of changing data of 
			<xsl:choose>
				<xsl:when test="$item-type='O'">
					Reporting obligation:
				</xsl:when>
				<xsl:when test="$item-type='A'">
					Reporting Activity:
				</xsl:when>
				<xsl:when test="$item-type='L'">
					Legal instrument:
				</xsl:when>
			</xsl:choose>
		</span>
   </td>
	 <td align="left" width="40%"><b> ID=<xsl:value-of select="XmlData/RowSet/Row/T_HISTORY/ITEM_ID"/> </b></td>
	 </tr>
	 </table>
	</xsl:if>
	 <br/>
	 
<table width="100%" cellspacing="3pts" border="1">


<tr>

<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF"><span lang="en-us">Time</span></font></span></td>
<td bgcolor="#646666" align="center" width="15%"><span class="head0"><font color="#FFFFFF">Action</font></span></td>
<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF">User</font></span></td>
<td bgcolor="#646666" align="center" width="35%"><span class="head0"><font color="#FFFFFF">Description</font></span></td>
</tr>

<xsl:for-each select="XmlData/RowSet/Row">
<tr valign="top">
<td align="center">
	<xsl:value-of select="T_HISTORY/TIME_STAMP"/>
</td>
<td>
	<xsl:choose>
	<xsl:when test="T_HISTORY/ACTION_TYPE='I'">
		Insert
	</xsl:when>
	<xsl:when test="T_HISTORY/ACTION_TYPE='U'">
		Update
	</xsl:when>
	<xsl:when test="T_HISTORY/ACTION_TYPE='D'">
		Delete
	</xsl:when>
	<xsl:when test="T_HISTORY/ACTION_TYPE='X'">
		Execute
	</xsl:when>
	</xsl:choose>
</td>
<td>
	<xsl:value-of select="T_HISTORY/USER"/>
</td>
<td>
	<xsl:value-of select="T_HISTORY/DESCRIPTION"/>
</td>

</tr>	
</xsl:for-each>
</table>

</body>
</html>

</xsl:template>
</xsl:stylesheet>
