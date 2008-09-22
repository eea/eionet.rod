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
	
	<xsl:variable name="col_class">
		twocolumns
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
				<table class="formtable">
                                	<col style="width:160px"/>
                                	<col style="width:490px"/>
					<tr>
						<td><label class="question" for="name">Name</label></td>
						<td>
							<input type="hidden">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/PK_CLIENT_ID</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
							</input>
							<input id="name" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_NAME"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="shortname">Short Name</label></td>
						<td>
							<input id="shortname" type="text" size="68" maxlength="100">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_SHORT_NAME</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_SHORT_NAME"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="acronym">Acronym</label></td>
						<td>
							<input id="acronym" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ACRONYM"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="address">Address</label></td>
						<td>
							<input id="address" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_ADDRESS</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_ADDRESS"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="postalcode">Postal code</label></td>
						<td>
							<input id="postalcode" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/POSTAL_CODE</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/POSTAL_CODE"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="city">City</label></td>
						<td>
							<input id="city" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CITY</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CITY"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="country">Country</label></td>
						<td>
							<input id="country" type="text" size="68" maxlength="255">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/COUNTRY</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/COUNTRY"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="homepage">Homepage</label></td>
						<td>
							<input id="homepage" type="text" size="68" maxlength="255" onchange="chkUrl(this)">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/CLIENT_URL</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_URL"/></xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td><label class="question" for="description">Description</label></td>
						<td>
							<textarea id="description" rows="4" cols="53">
								<xsl:attribute name="name">/XmlData/RowSet/Row/T_CLIENT/DESCRIPTION</xsl:attribute>
								<xsl:value-of select="RowSet/Row/T_CLIENT/DESCRIPTION"/>
							</textarea>
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
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
	<xsl:template name="PageHelp"/>
	<xsl:template name="createURL"/>
</xsl:stylesheet>
