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
		Edit Client Organisation
	</xsl:variable>
	<xsl:include href="ncommon.xsl"/>
	
	<xsl:variable name="client-id">
		<xsl:value-of select="/XmlData/RowSet/Row/T_CLIENT/PK_CLIENT_ID"/>
	</xsl:variable>
	
<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Edit Client Organisation</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<div id="workarea">
			<h1>Edit Client Organisation</h1>
			<form id="editClientForm" method="post" action="editclient.jsv">
				<table width="650" border="0">
					<tr valign="top">
						<th align="right"><label for="name">Name:</label></th>
						<td align="left" width="490">
							<input type="hidden" size="20">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/PK_CLIENT_ID</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
							</input>
							<input id="name" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_NAME"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="name">Short Name:</label></th>
						<td align="left" width="490">
							<input id="name" type="text" size="68" maxlength="100">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_SHORT_NAME</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_SHORT_NAME"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="acronym">Acronym:</label></th>
						<td align="left">
							<input id="acronym" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ACRONYM"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="address">Address:</label></th>
						<td align="left">
							<input id="address" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_ADDRESS</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ADDRESS"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="postalcode">Postal code:</label></th>
						<td align="left">
							<input id="postalcode" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/POSTAL_CODE</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/POSTAL_CODE"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="city">City:</label></th>
						<td align="left">
							<input id="city" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CITY</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CITY"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="country">Country:</label></th>
						<td align="left">
							<input id="country" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/COUNTRY</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/COUNTRY"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="homepage">Homepage:</label></th>
						<td align="left">
							<input id="homepage" type="text" size="68" maxlength="255" onchange="chkUrl(this)">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_URL</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_URL"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr valign="top">
						<th align="right"><label for="description">Description:</label></th>
						<td align="left">
							<textarea id="description" rows="4" cols="53">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/DESCRIPTION</xsl:attribute>
								<xsl:value-of select="RowSet/Row/T_CLIENT/DESCRIPTION"/>
							</textarea>
						</td>
					</tr>
					<tr>
						<td></td>
						<td align="left">
							<input type="submit" value="OK" style="width:6em"/>
							<input type="button" onclick="javascript:history.back()" value="Cancel" style="width:6em"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="dom-update-mode" value="U"/>
			</form>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
