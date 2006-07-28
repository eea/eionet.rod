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
		Status of participation
	</xsl:variable>
	
	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>
	
	<xsl:variable name="ra-id">
		<xsl:value-of select="/XmlData/RowSet/@ID"/>
	</xsl:variable>
	
	<xsl:variable name="spatialID">
		<xsl:value-of select="/XmlData/RowSet/@spatialID"/>
	</xsl:variable>
	
	<xsl:variable name="spatialHistoryID">
		<xsl:value-of select="/XmlData/RowSet/@spatialHistoryID"/>
	</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.europa.eu">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$ra-id"/>&amp;amp;mode=A</xsl:attribute>
 Reporting obligation</a></div>
 <div class="breadcrumbitemlast">Status of participation</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<div id="workarea">
			<div id="operations">
				<ul>
					<li class="help"><a href="javascript:openViewHelp('HELP_SPATIALHISTORY')">Page help</a></li>
					<xsl:if test="contains($permissions, ',/Admin/Helptext:u,')='true'">
						<li class="help"><a href="javascript:openHelp('HELP_SPATIALHISTORY')">Edit help text</a></li>
					</xsl:if>
				</ul>
			</div>
			<h1>Status of participation:</h1>
			<table class="datatable">
				<tr>
					<th scope="row" class="scope-row">Reporting obligation:</th>
					<td><xsl:value-of select="//RowSet/Row/T_OBLIGATION/TITLE"/></td>
				</tr>
				<tr valign="top">
					<th scope="row" class="scope-row">Client organisation: </th>
					<td><xsl:value-of select="//RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
				</tr>
			</table>
			<br/>
			<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
				<form name="f1" method="POST" action="editperiod">
					<span style="font-weight: bold;">Edit period:</span>
					<fieldset>
						<input type="hidden"><xsl:attribute name="name">ra_id</xsl:attribute><xsl:attribute name="value"><xsl:value-of select="$ra-id"/></xsl:attribute></input>
						From <input size="7" type="text"><xsl:attribute name="name">from</xsl:attribute><xsl:attribute name="value"></xsl:attribute></input>
						to <input size="7" type="text"><xsl:attribute name="name">to</xsl:attribute><xsl:attribute name="value"></xsl:attribute></input>
						&#160;<input type="submit" value="Save" style="font-weight: normal; color: #000000; background-image: url('images/bgr_form_buttons.jpg')"></input>
					</fieldset>
				</form>
				<br/>
			</xsl:if>
			<table cellspacing="0" class="datatable">
				<tr>
					<th width="25%">
						Country
					</th>
					<th width="25%">
						Status
					</th>
					<th width="40%">
						Participation period
					</th>
					<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
						<th width="10%">
							Edit period
						</th>
					</xsl:if>
				</tr>
			<xsl:for-each select="//RowSet/Row">
			<tr valign="top">
					<xsl:attribute name="class">
							<xsl:if test="position() mod 2 = 0">zebraeven</xsl:if>
					</xsl:attribute>
			<td valign="top">
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
			</td>
			<td valign="top">
				<xsl:choose>
					<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='Y'">
						Voluntary reporting
					</xsl:when>
					<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='N'">
						Formal reporting
					</xsl:when>
				</xsl:choose>
			</td>
			<td valign="top">
				<xsl:choose>
					<xsl:when test="T_SPATIAL_HISTORY/START_DATE='' or T_SPATIAL_HISTORY/START_DATE='00/00/0000'">
						Prior to start of ROD (2003)
					</xsl:when>
					<xsl:otherwise>
						From <xsl:value-of select="T_SPATIAL_HISTORY/START_DATE"/>
					</xsl:otherwise>
				</xsl:choose>
				to 
				<xsl:choose>
					<xsl:when test="T_SPATIAL_HISTORY/END_DATE != ''">
						<xsl:value-of select="T_SPATIAL_HISTORY/END_DATE"/>
					</xsl:when>
					<xsl:otherwise>
							present
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
				<td valign="top" align="center">
					<a><xsl:attribute name="href">spatialhistory.jsv?ID=<xsl:value-of select="$ra-id"/>&amp;amp;spatialID=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>&amp;amp;spatialHistoryID=<xsl:value-of select="T_SPATIAL_HISTORY/PK_SPATIAL_HISTORY_ID"/></xsl:attribute>
						edit
					</a>
				</td>
			</xsl:if>
			</tr>
			<xsl:if test="$spatialID = T_SPATIAL/PK_SPATIAL_ID">
				<xsl:if test="$spatialHistoryID = T_SPATIAL_HISTORY/PK_SPATIAL_HISTORY_ID">
				
					<form name="f2" method="POST" action="editperiod">
						<input type="hidden">
							<xsl:attribute name="name">spatialHistoryID</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="T_SPATIAL_HISTORY/PK_SPATIAL_HISTORY_ID"/></xsl:attribute>
						</input>
						<input type="hidden">
							<xsl:attribute name="name">ra_id</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="$ra-id"/></xsl:attribute>
						</input>
						<tr valign="top" bgcolor="#FFCCCC">
							<td colspan="2" valign="top">
							&#160;
							</td>
							<td valign="middle">
									From
									<input size="7" type="text">
										<xsl:attribute name="name">from</xsl:attribute>
											<xsl:choose>
												<xsl:when test="T_SPATIAL_HISTORY/START_DATE='' or T_SPATIAL_HISTORY/START_DATE='00/00/0000'">
													<xsl:attribute name="value">Prior to start of ROD (2003)</xsl:attribute>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="value"><xsl:value-of select="T_SPATIAL_HISTORY/START_DATE"/></xsl:attribute>
												</xsl:otherwise>
											</xsl:choose>
									</input>
									to
									<input size="7" type="text">
										<xsl:attribute name="name">to</xsl:attribute>
											<xsl:choose>
												<xsl:when test="T_SPATIAL_HISTORY/END_DATE != ''">
													<xsl:attribute name="value"><xsl:value-of select="T_SPATIAL_HISTORY/END_DATE"/></xsl:attribute>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="value">present</xsl:attribute>
												</xsl:otherwise>
											</xsl:choose>
									</input>
							</td>
							<td valign="top" align="center">
								<input type="submit" value="Save" style="font-weight: normal; color: #000000; background-image: url('images/bgr_form_buttons.jpg')"></input>
							</td>
						</tr>
					</form>
				</xsl:if>
			</xsl:if>
			</xsl:for-each>
			</table>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
