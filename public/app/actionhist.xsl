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
		History of changes
	</xsl:variable>
	
	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="action-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ACTION_TYPE"/>
	</xsl:variable>
	
	<xsl:variable name="item-type">
			<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ITEM_TYPE"/>
	</xsl:variable>
	
<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">History of changes</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>
	<xsl:template match="XmlData">
		<div id="workarea">
			<h1>
					<xsl:choose>
						<xsl:when test="$action-type='I'">
							Inserted
						</xsl:when>
						<xsl:when test="$action-type='U'">
							Updated
						</xsl:when>
						<xsl:when test="$action-type='D'">
							Deleted
						</xsl:when>
					</xsl:choose>
					Items of type 
					<xsl:choose>
						<xsl:when test="$item-type='O'">
							Reporting obligation
						</xsl:when>
						<xsl:when test="$item-type='A'">
							Reporting obligation
						</xsl:when>
						<xsl:when test="$item-type='L'">
							Legislative instrument
						</xsl:when>
					</xsl:choose>
			 </h1>
		
		<div style="margin-left:2">
		
		<table class="datatable">
			<tr>
				<th scope="col">Item ID</th>
				<th scope="col">Type</th>
				<th scope="col">Time</th>
				<th scope="col">Action</th>
				<th scope="col">User</th>
			</tr>
		
		<xsl:for-each select="RowSet/Row">
		<tr>
			<xsl:attribute name="class">
				<xsl:if test="position() mod 2 = 0">even</xsl:if>
			</xsl:attribute>
			<td>
				<a title="Show the change record of this item">
				<xsl:attribute name="href">history.jsv?entity=<xsl:value-of select="T_HISTORY/ITEM_TYPE"/>&amp;amp;id=<xsl:value-of select="T_HISTORY/ITEM_ID"/></xsl:attribute>
				<xsl:value-of select="T_HISTORY/ITEM_ID"/>
				</a>
			</td>
			<td>
				<xsl:choose>
				<xsl:when test="T_HISTORY/ITEM_TYPE='O'">
					Reporting Obligation
				</xsl:when>
				<xsl:when test="T_HISTORY/ITEM_TYPE='A'">
					Reporting Activity
				</xsl:when>
				<xsl:when test="T_HISTORY/ITEM_TYPE='L'">
					Legislative Instrument
				</xsl:when>
				</xsl:choose>
		
			</td>
			<td align="center">
				<xsl:value-of select="T_HISTORY/TIME_STAMP"/>
			</td>
			<td>
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
				</xsl:choose>
			</td>
			<td>
				<xsl:value-of select="T_HISTORY/USER"/>
			</td>
		</tr>	
		</xsl:for-each>
		</table>
		</div>
		</div>
		<xsl:call-template name="CommonFooter"/>
	</xsl:template>
</xsl:stylesheet>
