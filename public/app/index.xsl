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

<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:variable name="pagetitle">
		EEA - Reporting Obligations Database
	</xsl:variable>
	<xsl:include href="ncommon.xsl"/>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitemlast">ROD</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<!-- page -->
<div id="workarea">
		<xsl:value-of select="//HLP_AREA[AREA_ID='Two_boxes']/HTML" disable-output-escaping="yes"/>
		<xsl:value-of select="//HLP_AREA[AREA_ID='Introduction']/HTML" disable-output-escaping="yes"/>
		<br/>
  
<!--- -->
<xsl:call-template name="RASearch"/>


<!-- SiteSearch Google -->
<br/>
<form method="get" action="http://search.eionet.europa.eu/search.jsp">
	<input value="rod.eionet.europa.eu" name="qp_site" type="hidden"/>
	<table width="530" style="border: 1px solid #006666">
	<tr>
		<td valign="middle" width="30%">
			<label for="queryfld" style="font-weight:bold">Search ROD website:</label>
		</td>
		<td valign="middle">
			<input type="text" name="query" id="queryfld" size="44" maxlength="255" value=""/>&#160;
			<xsl:call-template name="go"/>
			</td>
		</tr>
	</table>
</form>
<!-- SiteSearch Google -->
</div> <!-- workarea -->

		<xsl:call-template name="CommonFooter"/>


		
	</xsl:template>

	<xsl:template match="RowSet[@Name='Client']">
		<xsl:for-each select="Row/T_CLIENT">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_CLIENT_ID"/>:<xsl:value-of select="CLIENT_NAME"/></xsl:attribute>
			<xsl:value-of select="CLIENT_NAME"/></option>
		</xsl:for-each>
	</xsl:template>


	<xsl:template match="RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/>:<xsl:value-of select="ISSUE_NAME"/></xsl:attribute>
			<xsl:value-of select="ISSUE_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

        <xsl:template name="createURL"/>

</xsl:stylesheet>
