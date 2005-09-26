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
	<xsl:include href="ncommon.xsl"/>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Problem</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
	<div id="workarea">
	<h1>Problem</h1>
	<table cellspacing="7pts" width="600">
	<xsl:for-each select="Error">
		<tr valign="top">
			<td width="10pts"><img src="images/diamlil.gif" vspace="4"/></td>
			<td colspan="2">
				<b><xsl:value-of select="text()"/></b>
			</td>
		</tr>
		<xsl:if test="@Reason != ''">
			<tr valign="top">
				<td width="10pts">&#160;</td>
				<td width="10pts">&#160;</td>
				<td>
					<xsl:value-of select="@Reason"/>
					<xsl:if test="Data != ''">
						when executing <code><xsl:value-of select="Data"/></code>
					</xsl:if>
				</td>
			</tr>
		</xsl:if>
	</xsl:for-each>
	</table>
	<table cellspacing="7pts">
		<tr height="40pts" valign="bottom">
			<td width="10pts">&#160;</td>
			<td><a href="javascript:history.back()"><span class="Mainfont">[Back]</span></a></td>
		</tr>
	</table>
	</div>
	</xsl:template>

</xsl:stylesheet>
