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

	<xsl:variable name="pagetitle">
		Reporting client or issuer details
	</xsl:variable>
	
	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="client-id">
		<xsl:value-of select="/XmlData/RowSet/Row/T_CLIENT/PK_CLIENT_ID"/>
	</xsl:variable>
	
<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Client information</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<div id="workarea">
			<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
				<div id="operations" style="width:125px; text-align:center; border: 1px solid black; background-color:#A0A0A0">
					<div style="color:#FFFFFF; font-weight:bold; border-bottom:1px dotted black">Actions</div>
					<p>
						<a>
						<xsl:attribute name="href">eclient.jsv?id=<xsl:value-of select='$client-id'/></xsl:attribute>
						<img src="images/editorganisation.png" alt="Edit organisation" border="0"/>
						</a>
					</p>
				</div>
			</xsl:if>
			<h1>Reporting client or issuer details</h1>
			<table border="0">
					<tr valign="top">
						<th scope="row" align="right" width="100">Name:</th>
						<td align="left" width="490"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right">Acronym:</th>
						<td align="left" ><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ACRONYM"/></td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right">Address:</th>
						<td align="left"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ADDRESS"/></td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right">Postal code:</th>
						<td align="left"><xsl:value-of select="RowSet/Row/T_CLIENT/POSTAL_CODE"/></td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right">City:</th>
						<td align="left"><xsl:value-of select="RowSet/Row/T_CLIENT/CITY"/></td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right">Country:</th>
						<td align="left"><xsl:value-of select="RowSet/Row/T_CLIENT/COUNTRY"/></td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right">Homepage:</th>
						<td align="left">
							<xsl:if test="RowSet/Row/T_CLIENT/CLIENT_URL != ''">
							<a target="_new">
								<xsl:attribute name="href">
									<xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_URL"/>
								</xsl:attribute>
								<xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_URL"/>
							</a>
							</xsl:if>
						</td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right">Description:</th>
						<td align="left">
					<xsl:call-template name="break">
					       <xsl:with-param name="text" select="RowSet/Row/T_CLIENT/DESCRIPTION"/>
					      </xsl:call-template>
						</td>
					</tr>
			</table>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
