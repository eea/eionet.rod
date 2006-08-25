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
	
	<xsl:variable name="item-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ITEM_TYPE"/>
	</xsl:variable>
	
<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">History of changes</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>
	
	<xsl:template match="XmlData">
		<div id="workarea">
				<xsl:if test="RowSet/Row/T_HISTORY/ITEM_ID != 0">
					<h1>
						History of changing data of 
						<xsl:choose>
							<xsl:when test="$item-type='O'">
								Reporting obligation:
							</xsl:when>
							<xsl:when test="$item-type='A'">
								Reporting Obligation:
							</xsl:when>
							<xsl:when test="$item-type='L'">
								Legislative instrument:
							</xsl:when>
						</xsl:choose>
				  ID=<xsl:value-of select="RowSet/Row/T_HISTORY/ITEM_ID"/>
					 </h1>
				</xsl:if>
			
				<table class="datatable">
				<tr>
					<th scope="col">Time</th>
					<th scope="col">Action</th>
					<th scope="col">User</th>
					<th scope="col">Description</th>
				</tr>
			
			
			<xsl:for-each select="RowSet/Row">
			<tr valign="top">
				<xsl:attribute name="class">
					<xsl:if test="position() mod 2 = 0">zebraeven</xsl:if>
				</xsl:attribute>
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
						<xsl:when test="T_HISTORY/ACTION_TYPE='X'">
							Execute
						</xsl:when>
					</xsl:choose>
				</td>
				<td>
					<xsl:value-of select="T_HISTORY/USER"/>
				</td>
				<td>
					<xsl:value-of select="T_HISTORY/DESCRIPTION"/>&#160;
				</td>
			</tr>	
			</xsl:for-each>
			</table>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
