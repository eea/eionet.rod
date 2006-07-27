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
 <div class="breadcrumbitem"><a href="http://www.eionet.europa.eu">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Client information</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<div id="workarea">
			<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
				<div id="operations">
					<ul>
						<li>
							<a>
							<xsl:attribute name="href">eclient.jsv?id=<xsl:value-of select='$client-id'/></xsl:attribute>
							Edit Organisation
							</a>
						</li>
					</ul>
				</div>
			</xsl:if>
			<h1>Reporting client or issuer details</h1>
			<table border="0">
					<tr valign="top">
						<th scope="row" align="right" width="120">Name:</th>
						<td align="left" width="490"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
					</tr>
					<tr valign="top">
						<th scope="row" align="right" width="120">Short Name:</th>
						<td align="left" width="490"><xsl:value-of select="RowSet/Row/T_CLIENT/CLIENT_SHORT_NAME"/></td>
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
			<xsl:if test="RowSet[@Name='DirectObligations']/Row/T_OBLIGATION/PK_RA_ID !=''">
				<h2>Direct Obligations:</h2>
				<table class="datatable" width="600">
						<tr>
							<th scope="col">ID</th>
							<th scope="col">Title</th>
						</tr>
					
						<xsl:for-each select="RowSet[@Name='DirectObligations']/Row/T_OBLIGATION">
								<tr>
									<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
									<td><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RA_ID"/>&amp;amp;aid=<xsl:value-of select="FK_SOURCE_ID"/>&amp;amp;mode=A</xsl:attribute><xsl:value-of select="PK_RA_ID"/></a></td>
									<td><xsl:value-of select="TITLE"/>&#160;
									<xsl:if test="TERMINATE ='Y'">
										<em>Terminated</em>
									</xsl:if>
									</td>
								</tr>
						</xsl:for-each>
					
				</table>
			</xsl:if>
			<xsl:if test="RowSet[@Name='IndirectObligations']/Row/T_OBLIGATION/PK_RA_ID !=''">
				<h2>Indirect Obligations:</h2>
				<table class="datatable" width="600">
						<tr>
							<th scope="col">ID</th>
							<th scope="col">Title</th>
						</tr>
					
						<xsl:for-each select="RowSet[@Name='IndirectObligations']/Row/T_OBLIGATION">
								<tr>
									<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
									<td><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RA_ID"/>&amp;amp;aid=<xsl:value-of select="FK_SOURCE_ID"/>&amp;amp;mode=A</xsl:attribute><xsl:value-of select="PK_RA_ID"/></a></td>
									<td><xsl:value-of select="TITLE"/>&#160;
									<xsl:if test="TERMINATE ='Y'">
										<em>Terminated</em>
									</xsl:if>
									</td>
								</tr>
						</xsl:for-each>
					
				</table>
			</xsl:if>
			<xsl:if test="RowSet[@Name='DirectInstruments']/Row/T_SOURCE/PK_SOURCE_ID !=''">
				<h2>Direct Instruments:</h2>
				<table class="datatable" width="600">
						<tr>
							<th scope="col">ID</th>
							<th scope="col">Alias</th>
						</tr>
					
						<xsl:for-each select="RowSet[@Name='DirectInstruments']/Row/T_SOURCE">
								<tr>
									<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
									<td><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_SOURCE_ID"/>&amp;amp;mode=S</xsl:attribute><xsl:value-of select="PK_SOURCE_ID"/></a></td>
									<td><xsl:value-of select="ALIAS"/></td>
								</tr>
						</xsl:for-each>
					
				</table>
			</xsl:if>
			<xsl:if test="RowSet[@Name='IndirectInstruments']/Row/T_SOURCE/PK_SOURCE_ID !=''">
				<h2>Indirect Instruments:</h2>
				<table class="datatable" width="600">
						<tr>
							<th scope="col">ID</th>
							<th scope="col">Alias</th>
						</tr>
					
						<xsl:for-each select="RowSet[@Name='IndirectInstruments']/Row/T_SOURCE">
								<tr>
									<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
									<td><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_SOURCE_ID"/>&amp;amp;mode=S</xsl:attribute><xsl:value-of select="PK_SOURCE_ID"/></a></td>
									<td><xsl:value-of select="ALIAS"/></td>
								</tr>
						</xsl:for-each>
					
				</table>
			</xsl:if>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
