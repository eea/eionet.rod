<?xml version="1.0"?>

<!--
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is "EINRC-4 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 * -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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
					Reporting Obligation:
				</xsl:when>
				<xsl:when test="$item-type='L'">
					Legislative instrument:
				</xsl:when>
			</xsl:choose>
		</span>
   </td>
	 <td align="left" width="40%"><b> ID=<xsl:value-of select="XmlData/RowSet/Row/T_HISTORY/ITEM_ID"/> </b></td>
	 </tr>
	 </table>
	</xsl:if>
	 <br/>

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td style="border-right: 1 solid #008080; border-left: 1 solid #008080; border-top: 1 solid #008080;border-bottom: 1 solid #008080" bgcolor="#FFFFFF" align="center" width="25%"><span class="head0">Time</span></td>
		<td style="border-right: 1 solid #008080; border-top: 1 solid #008080;border-bottom: 1 solid #008080; " bgcolor="#FFFFFF" align="center" width="15%"><span class="head0">Action</span></td>
		<td style="border-right: 1 solid #008080; border-top: 1 solid #008080;border-bottom: 1 solid #008080; " bgcolor="#FFFFFF" align="center" width="25%"><span class="head0">User</span></td>
		<td style="border-right: 1 solid #008080; border-top: 1 solid #008080;border-bottom: 1 solid #008080; " bgcolor="#FFFFFF" align="center" width="35%"><span class="head0">Description</span></td>
	</tr>


<xsl:for-each select="XmlData/RowSet/Row">
<tr valign="top">
	<xsl:attribute name="bgcolor">
		<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
	</xsl:attribute>
	<td align="center" style="border-right: 1 solid #C0C0C0; border-left: 1 solid #008080; border-bottom: 1 solid #C0C0C0">
		<xsl:value-of select="T_HISTORY/TIME_STAMP"/>
	</td>
	<td style="border-right: 1 solid #C0C0C0; border-bottom: 1 solid #C0C0C0">
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
	<td style="border-right: 1 solid #C0C0C0; border-bottom: 1 solid #C0C0C0">
		<xsl:value-of select="T_HISTORY/USER"/>
	</td>
	<td style="border-right: 1 solid #008080; border-bottom: 1 solid #C0C0C0">
		<xsl:value-of select="T_HISTORY/DESCRIPTION"/>&#160;
	</td>
</tr>	
</xsl:for-each>
</table>

</body>
</html>

</xsl:template>
</xsl:stylesheet>
