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
 * The Original Code is "EINRC-7 / OPS Project".
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
 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" exclude-result-prefixes="java" xmlns:java="http://xml.apache.org/xslt/java">
<xsl:param name="req" select="'default value'"/>
<xsl:variable name="mode" select="java:eionet.rod.RODUtil.getParameter($req, 'dom-update-mode')"/>
<!--xsl:include href="editor.xsl"/-->
<xsl:template match="/XmlData">
<html lang="en">
<head><title>Client information</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<link rel="stylesheet" type="text/css" href="local2006.css" media="screen" title="Standard"/>
	<script lang="JavaScript" src="script/util.js"></script>
	<script lang="JavaScript">
			function check() {
				if (document.f.elements["dom-update-mode"].value=="D")	
					document.f.submit();
			}
	</script>
</head>


<body style="background: #FFFFFF" bgcolor="#F7F3F7" text="#000000" link="#004D7B" vlink="#808080" alink="#006666" onload="check()" onunload="window.opener.document.location.reload(true)">
	<p><h2>Add or edit indicator</h2></p>
	<form name="f" method="POST" action="indicator.jsv">		
		<input type="hidden" name="dom-update-mode">
			<xsl:attribute name="value">
				<xsl:choose>
					<xsl:when test="RowSet[@skeleton='1']">A</xsl:when>
					<xsl:when test="$mode='D'">D</xsl:when>
					<xsl:otherwise>U</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</input>
	<table border="0" width="500" cellspacing="0" cellpadding="2" style="border: 1px solid #808080" height="125">
  <tr bgcolor="#FFFFFF" height="25" class="border_right">
    <td width="107" valign="top" align="left" class="indicator_label">Indicator title</td>
    <td width="277" valign="top" align="left">
			<input type="hidden" size="20">
				<xsl:attribute name="name"><xsl:value-of select="//RowSet[@Name='Indicator']/Row/T_INDICATOR/PK_INDICATOR_ID/@XPath"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="//RowSet[@Name='Indicator']/Row/T_INDICATOR/PK_INDICATOR_ID"/></xsl:attribute>
			</input>
			<input type="hidden" size="20">
				<xsl:attribute name="name"><xsl:value-of select="//RowSet[@Name='Indicator']/Row/T_INDICATOR/FK_RA_ID/@XPath"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="//RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID"/></xsl:attribute>
			</input>
			<input type="text" size="60">
				<xsl:attribute name="name"><xsl:value-of select="//RowSet[@Name='Indicator']/Row/T_INDICATOR/TITLE/@XPath"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="//RowSet[@Name='Indicator']/Row/T_INDICATOR/TITLE"/></xsl:attribute>
			</input>
		</td>
	</tr>
  <tr height="25" class="border_right">
    <td valign="top" align="left" class="indicator_label">Indicator number</td>
    <td valign="top" align="left">
		<input type="text" size="60">
			<xsl:attribute name="name"><xsl:value-of select="RowSet[@Name='Indicator']/Row/T_INDICATOR/NUMBER/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="RowSet[@Name='Indicator']/Row/T_INDICATOR/NUMBER"/></xsl:attribute>
		</input>
		</td>
	</tr>
  <tr bgcolor="#FFFFFF" height="25" class="border_right">
    <td valign="top" align="left" class="indicator_label">URL</td>
    <td valign="top" align="left">
		<input type="text" size="60" onchange="chkUrl(this)">
			<xsl:attribute name="name"><xsl:value-of select="RowSet[@Name='Indicator']/Row/T_INDICATOR/URL/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="RowSet[@Name='Indicator']/Row/T_INDICATOR/URL"/></xsl:attribute>
		</input>
		</td>
	</tr>
  <tr height="25" class="border_right">
    <td valign="top" align="left" class="indicator_label">Owner</td>
    <td valign="top" align="left">
		<input type="text" size="60">
			<xsl:attribute name="name"><xsl:value-of select="RowSet[@Name='Indicator']/Row/T_INDICATOR/OWNER/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="RowSet[@Name='Indicator']/Row/T_INDICATOR/OWNER"/></xsl:attribute>
		</input>
		</td>
	</tr>
  <tr bgcolor="#FFFFFF" height="25" class="border_right">
    <td valign="top" align="left" >&#160;</td>
    <td valign="top" align="left">
			<input type="submit" value="Save" name="B1" class="btn"></input>
		</td>
	</tr>
	</table>
	</form>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
