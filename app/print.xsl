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
 * Original Code: Andre Karpistsenko (TietoEnator)
 * -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet[position()=1]/@auth"/>
	</xsl:variable>

	<xsl:template match="/">
   	<html lang="en">
			<head>
				<title><xsl:call-template name="PageTitle"/></title>
				<META HTTP-EQUIV="Content-Type" CONTENT="text/html; CHARSET=ISO-8859-1"/>
				<!--<link type="text/css" rel="stylesheet" href="http://www.eionet.eu.int/eionet.css">-->
				<link href="eionet.css" rel="stylesheet" type="text/css"/>
		</head>
		<body bgcolor="#f0f0f0" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">


			<table border="0">
				<tr valign="top">
				<td>
					<xsl:apply-templates select="XmlData"/>
				</td>
			</tr></table>
			</body>
   	 </html>
	</xsl:template>  


	<xsl:template name="nofound">
		<table cellspacing="7pts">
			<tr height="40pts" valign="bottom">
				<td width="10pts">&#160;</td>
				<td>No records found</td>
			</tr>
			<tr height="40pts" valign="bottom">
				<td width="10pts">&#160;</td>
				<td><a href="javascript:history.back()"><span class="Mainfont">[Back]</span></a></td>
			</tr>
		</table>
	</xsl:template>

	<xsl:include href="static.xsl"/>
</xsl:stylesheet>