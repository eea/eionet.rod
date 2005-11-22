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
	<xsl:include href="ncommon.xsl"/>
	
	<xsl:variable name="client-id">
		<xsl:value-of select="/XmlData/RowSet/Row/T_CLIENT/PK_CLIENT_ID"/>
	</xsl:variable>
	
	<xsl:variable name="admin">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/@auth"/>
	</xsl:variable>
	
<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Edit Client Organisation</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<div id="workarea">
			<table>
				<tr>
					<td width="20"></td>
					<td width="630" align="left">
						<h2>Edit Client Organisation</h2>
					</td>
				</tr>
			</table>
			<form name="editClientForm" method="POST" action="editclient.jsv" acceptcharset="UTF-8">
				<table width="650" border="0">
					<tr height="30" valign="top">
						<td width="20"></td>
						<td align="right" width="100"><b>Name:</b></td>
						<td align="left" width="490">
							<input type="hidden" size="20">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/PK_CLIENT_ID</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
							</input>
							<input type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_NAME"/></xsl:attribute>
							</input>
						</td>
						<td width="10"></td>
					</tr>
					<tr height="30" valign="top">
						<td></td>
						<td align="right"><b>Acronym:</b></td>
						<td align="left" >
							<input type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ACRONYM"/></xsl:attribute>
							</input>
						</td>
						<td></td>
					</tr>
					<tr height="30" valign="top">
						<td></td>
						<td align="right"><b>Address:</b></td>
						<td align="left">
							<input type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_ADDRESS</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ADDRESS"/></xsl:attribute>
							</input>
						</td>
						<td></td>
					</tr>
					<tr height="30" valign="top">
						<td></td>
						<td align="right"><b>Postal code:</b></td>
						<td align="left">
							<input type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/POSTAL_CODE</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/POSTAL_CODE"/></xsl:attribute>
							</input>
						</td>
						<td></td>
					</tr>
					<tr height="30" valign="top">
						<td></td>
						<td align="right"><b>City:</b></td>
						<td align="left">
							<input type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CITY</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CITY"/></xsl:attribute>
							</input>
						</td>
						<td></td>
					</tr>
					<tr height="30" valign="top">
						<td></td>
						<td align="right"><b>Country:</b></td>
						<td align="left">
							<input type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/COUNTRY</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/COUNTRY"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr height="30" valign="top">
						<td></td>
						<td align="right"><b>Homepage:</b></td>
						<td align="left">
							<input type="text" size="68" maxlength="255" onchange="chkUrl(this)">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_URL</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_URL"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr height="30" valign="top">
						<td></td>
						<td align="right"><b>Description:</b></td>
						<td align="left">
							<textarea rows="4" cols="53" wrap="soft">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/DESCRIPTION</xsl:attribute>
								<xsl:value-of select="RowSet/Row/T_CLIENT/DESCRIPTION"/>
							</textarea>
						</td>
					</tr>
					<tr>
						<td></td>
						<td></td>
						<td align="left">
							<input type="button" onclick="javascript:document.editClientForm.submit();" value="&#160;&#160;&#160;&#160;OK&#160;&#160;&#160;&#160;"/>
							<input type="button" onclick="javascript:history.back()" value="Cancel"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="dom-update-mode" value="U"></input>
			</form>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
