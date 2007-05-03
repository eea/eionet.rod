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

	<xsl:variable name="pagetitle">
		Reporting obligation for <xsl:value-of select="//RowSet[@Name='Activity']/Row/T_SOURCE/ALIAS"/> &#160; <xsl:value-of select="//RowSet[@Name='Activity']/Row/T_SOURCE/SOURCE_CODE"/>
	</xsl:variable>
	
	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="ra-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID"/>
	</xsl:variable>

	<xsl:variable name="src-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>
	
	<xsl:variable name="admin">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/@auth"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/@permissions"/>
	</xsl:variable>
	
	<xsl:variable name="latest">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/@latest"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:choose>
			<xsl:when test="count(RowSet[@Name='Activity']/Row)>0">
				<xsl:apply-templates select="RowSet[@Name='Activity']"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="nofound"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="LIRORAFooter">
			<xsl:with-param name="table">RA</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$src-id"/>&amp;mode=S</xsl:attribute>
Legislative instrument</a></div>
 <div class="breadcrumbitemlast">Reporting obligation</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

	<xsl:template match="RowSet[@Name='Activity']/Row">
		<!-- form for delete activity action -->
		<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':d,'))='true'">
			<script type="text/javascript">
			<![CDATA[
				function delActivity() {
					if (confirm("Do you want to delete the reporting obligation?")){
						var u = window.location.href;
						document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/REDIRECT_URL"].value = u;
						document.f.submit();
					}
				}
			]]>
			</script>
			<form name="f" method="POST" action="activity.jsv">
				<input type="hidden" name="dom-update-mode" value="D"/>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID">
					<xsl:attribute name="value"><xsl:value-of select="$ra-id"/></xsl:attribute>
				</input>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID">
					<xsl:attribute name="value"><xsl:value-of select="$src-id"/></xsl:attribute>
				</input>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/REDIRECT_URL" value=""></input>
			</form>
	</xsl:if>

		<div id="workarea">
			
		<div id="operations">
			<ul>
				<li class="help"><a href="javascript:openViewHelp('HELP_RA')">Page help</a></li>
				<xsl:if test="contains($permissions, ',/Admin/Helptext:u,')='true'">
					<li class="help"><a href="javascript:openHelp('HELP_RA')">Edit help text</a></li>
				</xsl:if>
				<xsl:if test="count(//SubSet[@Name='Indicators']/Row) != 0 ">
					<li>
					<a><xsl:attribute name="href">javascript:openPopup("show.jsv", "id=<xsl:value-of select='$ra-id'/>&amp;mode=I")</xsl:attribute>
						Indicators</a>
					</li>
				</xsl:if>
				<li>
					<a><xsl:attribute name="href">show.jsv?mode=M&amp;id=<xsl:value-of select="$ra-id"/></xsl:attribute>
					Parameters</a>
				</li>
				<xsl:if test="T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS != ''">
					<li>
						<a><xsl:attribute name="href">csdeliveries?ACT_DETAILS_ID=<xsl:value-of select="$ra-id"/>&amp;COUNTRY_ID=%%</xsl:attribute>
						Status of deliveries</a>
					</li>
				</xsl:if>
				<xsl:if test="$latest != 'n'">
					<xsl:if test="contains($permissions, ',/obligations:i,')='true'">
						<li>
							<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>
							New obligation</a>
						</li>
					</xsl:if>
					<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':u,'))='true'">
						<li>
							<a><xsl:attribute name="href">activity.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>Edit obligation</a>
						</li>
					</xsl:if>
					<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':d,'))='true'">
						<li>
							<a href="javascript:delActivity()">Delete obligation</a>
						</li>
					</xsl:if>				
					<xsl:if test="$admin='true'">
						<li>
							<a href="javascript:openHelpList('RO')">Field descriptions</a>
						</li>
					</xsl:if>
					<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':u,'))='true'">
						<li>
							<a>
								<xsl:attribute name="href">subscribe.jsp?id=<xsl:value-of select="$ra-id"/></xsl:attribute>
								Subscribe
							</a>
						</li>
					</xsl:if>
					<li>
						<a>
							<xsl:attribute name="href">versions.jsp?id=<xsl:value-of select="$ra-id"/>&amp;tab=T_OBLIGATION&amp;id_field=PK_RA_ID</xsl:attribute>
							Show history
						</a>
					</li>
				</xsl:if>
			</ul>
		</div>

		<h1>Reporting obligation for <xsl:value-of select="T_SOURCE/ALIAS"/> &#160; <xsl:value-of select="T_SOURCE/SOURCE_CODE"/></h1>
		<table class="datatable">
		<col style="width:30%" />
		<col style="width:65%" />
		<col style="width:5%" />
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Title</th>
				<td>
					<xsl:choose>
						<xsl:when test="T_OBLIGATION/TITLE != ''">
							<xsl:value-of select="T_OBLIGATION/TITLE"/>
						</xsl:when>
						<xsl:otherwise>
							Reporting Obligation
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td>&#160;</td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Description</th>
				<td>
					<xsl:call-template name="break">
						 <xsl:with-param name="text" select="T_OBLIGATION/DESCRIPTION"/>
					</xsl:call-template>
					<xsl:if test="T_OBLIGATION/EEA_PRIMARY=1">
							 <br/>This reporting obligation is an EIONET Priority Data flow<br/>
					</xsl:if>
					<xsl:if test="T_OBLIGATION/EEA_CORE=1">
						<br/>Reporting under this obligation is used for EEA Core set of indicators<br/>
					</xsl:if>
					<xsl:if test="T_OBLIGATION/FLAGGED=1">
						<br/>This reporting obligation is flagged<br/>
					</xsl:if>
					<xsl:if test="T_OBLIGATION/OVERLAP_URL!=''">
						<br/>Delivery process or content of this obligation overlaps with another reporting obligation (
						<a><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/OVERLAP_URL"/></xsl:attribute>
						<xsl:value-of select="T_OBLIGATION/OVERLAP_URL"/>
						</a>
						)
					</xsl:if>&#160;
				</td>
				<td>&#160;</td>
			</tr>
			<tr>
				<td colspan="3" class="dark_green_heading">
					Reporting dates and guidelines
				</td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">National reporting coordinators</th>
				<td>
					<xsl:choose>
					<xsl:when test="COORD_ROLE/ROLE_ID!=''">
						<a title="Public role information">
							<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="COORD_ROLE/ROLE_URL"/>')</xsl:attribute>
							<xsl:value-of select="COORD_ROLE/ROLE_NAME"/> (<xsl:value-of select="COORD_ROLE/ROLE_ID"/>)</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="T_OBLIGATION/COORDINATOR_ROLE != ''">
							<div class="role_not_found">Directory role not found for '<xsl:value-of select="T_OBLIGATION/COORDINATOR_ROLE"/>'</div><br/>
						</xsl:if>
						<xsl:value-of select="T_OBLIGATION/COORDINATOR"/>&#160;
							<a>
								<xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/COORDINATOR_URL"/></xsl:attribute>
									<xsl:value-of select="T_OBLIGATION/COORDINATOR_URL"/>
							</a>
					</xsl:otherwise>
					</xsl:choose>
				</td>
				<td class="center">
					<xsl:if test="COORD_ROLE/ROLE_ID!=''">
						<a title="Role details on CIRCA for members"><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="COORD_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
							<img src="images/details.gif" alt="Additional details for logged-in users" border="0"/>
						</a>
					</xsl:if>
				</td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">National reporting contacts</th>
				<td>
					<xsl:choose>
					<xsl:when test="RESP_ROLE/ROLE_ID!=''">
						<a title="Public role information">
							<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESP_ROLE/ROLE_URL"/>')</xsl:attribute>
							<xsl:value-of select="RESP_ROLE/ROLE_NAME"/> (<xsl:value-of select="RESP_ROLE/ROLE_ID"/>)</a>
					</xsl:when>
					<xsl:otherwise>
							<xsl:if test="T_OBLIGATION/RESPONSIBLE_ROLE != ''">
								<div class="role_not_found">Directory role not found for '<xsl:value-of select="T_OBLIGATION/RESPONSIBLE_ROLE"/>'</div><br/>
							</xsl:if>
							<xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT"/>&#160;
							<a>
								<xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT_URL"/></xsl:attribute>
									<xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT_URL"/>
							</a>
					</xsl:otherwise>
					</xsl:choose>
				</td>
				<td class="center">
					<xsl:if test="RESP_ROLE/ROLE_ID!=''">
						<a title="Role details on CIRCA"><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESP_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
							<img src="images/details.gif" alt="Additional details for logged-in users" border="0"/>
						</a>
					</xsl:if>&#160;
				</td>
			</tr>
			<!--tr valign="top">
				<td style="border-right: 1px solid #C0C0C0"><span class="head0">Baseline reporting date</span></td>
				<td style="border-right: 1px solid #C0C0C0">
					<xsl:value-of select="T_OBLIGATION/FIRST_REPORTING"/>
				</td>
				<td style="border-right: 1px solid #C0C0C0"></td>
			</tr-->
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Reporting frequency</th>
				<td>
					<xsl:call-template name="RAReportingFrequency"/>
				</td>
				<td></td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Next report due</th>
				<td>
					<xsl:choose>
						<xsl:when test="T_OBLIGATION/TERMINATE = 'N'">
							<xsl:choose>
							<xsl:when test="string-length(T_OBLIGATION/NEXT_REPORTING) = 0">
								<xsl:value-of select="T_OBLIGATION/NEXT_DEADLINE"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="T_OBLIGATION/NEXT_REPORTING"/>
							</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<div class="terminated">terminated</div>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td></td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Date comments</th>
				<td>
					<xsl:value-of select="T_OBLIGATION/DATE_COMMENTS"/>&#160;
				</td>
				<td></td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Report to</th>
				<td>
					<xsl:choose>
						<xsl:when test="T_CLIENT/PK_CLIENT_ID != ''">
						<a><xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
							<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
						</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td>
				</td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Other clients using this reporting</th>
				<td>
					<xsl:call-template name="OtherClients"/>
				</td>
				<td></td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Reporting guidelines</th>
				<td>
					<xsl:choose>
						<xsl:when test="T_OBLIGATION/REPORT_FORMAT_URL!=''">
							<a><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
								<xsl:choose>
								<xsl:when test="T_OBLIGATION/FORMAT_NAME!=''">
									<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/>
								</xsl:otherwise>
								</xsl:choose>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="T_OBLIGATION/VALID_SINCE!=''">
						[Valid since <xsl:value-of select="T_OBLIGATION/VALID_SINCE"/>]
					</xsl:if>&#160;
				</td>
				<td></td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Extra information</th>
				<td>
						<xsl:call-template name="break">
							 <xsl:with-param name="text" select="T_OBLIGATION/REPORTING_FORMAT"/>
						</xsl:call-template>&#160;
				</td>
				<td></td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Principle repository</th>
				<td>
					<xsl:choose>
						<xsl:when test="T_OBLIGATION/LOCATION_PTR != ''">
							<a><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/LOCATION_PTR"/></xsl:attribute>						
								<xsl:choose>
									<xsl:when test="T_OBLIGATION/LOCATION_INFO != ''">
										<xsl:value-of select="T_OBLIGATION/LOCATION_INFO"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_OBLIGATION/LOCATION_PTR"/>
									</xsl:otherwise>
								</xsl:choose>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_OBLIGATION/LOCATION_INFO"/>
						</xsl:otherwise>
					</xsl:choose>&#160;
				</td>
				<td></td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Data used for</th>
				<td>
					<a><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/DATA_USED_FOR"/></xsl:attribute>
						<xsl:value-of select="T_OBLIGATION/DATA_USED_FOR"/>
					</a>
				</td>
				<td></td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Type of information reported</th>
				<td>
					<xsl:apply-templates select="SubSet[@Name='InfoType']"/>
				</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="3" class="dark_green_heading">Legal framework</td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Parent legislative instrument</th>
				<td>
					<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
					<xsl:choose>
						<xsl:when test="T_SOURCE/ALIAS != ''">
							<xsl:value-of select="T_SOURCE/ALIAS"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_SOURCE/TITLE"/>
						</xsl:otherwise>
					</xsl:choose>
					</a>
					<xsl:if test="T_OBLIGATION/AUTHORITY!=''">
						&#160;[<xsl:value-of select="T_OBLIGATION/AUTHORITY"/>]
					</xsl:if>
				</td>
				<td></td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Sibling reporting obligations</th>
				<td>
					<xsl:call-template name="Sibling"/>
				</td>
				<td></td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Type of obligation</th>
				<td>
					<xsl:value-of select="T_LOOKUP/C_TERM"/>
				</td>
				<td></td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Countries</th>
				<td>
					<xsl:apply-templates select="//RowSet[@Name='Spatial']"/>
				</td>
				<td class="center">
					<a title="History of participation"><xsl:attribute name="href">spatialhistory.jsv?ID=<xsl:value-of select="$ra-id"/></xsl:attribute>
						<img src="images/details.gif" alt="Status of country participation" border="0"/>
					</a>
				</td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Environmental issues</th>
				<td>
					<xsl:apply-templates select="//RowSet[@Name='EnvIssue']"/>&#160;
				</td>
				<td></td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">General comments</th>
				<td>
					<xsl:call-template name="break">
						 <xsl:with-param name="text" select="T_OBLIGATION/COMMENT"/>
					</xsl:call-template>&#160;
				</td>
				<td></td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">DPSIR</th>
				<td>
					<xsl:if test="T_OBLIGATION/DPSIR_D='yes'">
						<acronym title="Driving force">D</acronym>&#160;
					</xsl:if>
					<xsl:if test="T_OBLIGATION/DPSIR_P='yes'">
						<acronym title="Pressure">P</acronym>&#160;
					</xsl:if>
					<xsl:if test="T_OBLIGATION/DPSIR_S='yes'">
						<acronym title="State">S</acronym>&#160;
					</xsl:if>
					<xsl:if test="T_OBLIGATION/DPSIR_I='yes'">
						<acronym title="Impact">I</acronym>&#160;
					</xsl:if>
					<xsl:if test="T_OBLIGATION/DPSIR_R='yes'">
						<acronym title="Response">R</acronym>&#160;
					</xsl:if>
				</td>
				<td></td>
			</tr>
			<!--tr valign="top"   bgcolor="#ECECEC">
				<td style="border-right: 1px solid #C0C0C0"><span class="head0">Authority giving rise to the obligation</span></td>
				<td style="border-right: 1px solid #C0C0C0">
					<xsl:value-of select="T_OBLIGATION/AUTHORITY"/>
				</td>
				<td></td>
			</tr-->
	</table>


		</div>
	</xsl:template>

	<xsl:template match="//RowSet[@Name='Spatial']">
		<xsl:for-each select="Row">
			<a><xsl:attribute name="href">countryinfo.jsp?ra-id=<xsl:value-of select="$ra-id"/>&amp;spatial=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>&amp;vol=<xsl:value-of select="T_RASPATIAL_LNK/VOLUNTARY"/></xsl:attribute>
				<xsl:choose>
					<xsl:when test="T_RASPATIAL_LNK/VOLUNTARY='Y'">
						<span title="Informal participation in the reporting obligation"><xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>*</span>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
					</xsl:otherwise>
				</xsl:choose>
			</a>
			<xsl:if test="position()!=count(//RowSet[@Name='Spatial']/Row)">, </xsl:if>
		</xsl:for-each>
	</xsl:template>


	<xsl:template match="//RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
		<xsl:choose>
			<xsl:when test="position()!=count(//RowSet[@Name='EnvIssue']/Row/T_ISSUE)">
				<xsl:value-of select="ISSUE_NAME"/>, 
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="ISSUE_NAME"/></xsl:otherwise>
		</xsl:choose></xsl:for-each>
	</xsl:template>
	<!--xsl:value-of select="count(//SubSet[@Name='InfoType']/Row/T_INFO_LNK)"/>
	<xsl:value-of select="position()"/>-->
	<xsl:template match="SubSet[@Name='InfoType']">
		<xsl:for-each select="Row/T_LOOKUP">
				<xsl:value-of select="C_TERM"/><xsl:if test="position()!=count(//SubSet[@Name='InfoType']/Row)">,&#160;</xsl:if>
		</xsl:for-each>&#160;
	</xsl:template>

	<xsl:template name="OtherClients">
		<ul class="menu">
			<xsl:for-each select="SubSet[@Name='CCClients']/Row/T_CLIENT">
				<li>
					<a>
						<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="PK_CLIENT_ID"/></xsl:attribute>
						<xsl:value-of select="CLIENT_NAME"/>
					</a>
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>

	<xsl:template name="Sibling">
		<ul class="menu">
			<xsl:for-each select="SubSet[@Name='Sibling']/Row/T_OBLIGATION">
				<li>
					<a>
						<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RA_ID"/>&amp;mode=A&amp;aid=<xsl:value-of select="FK_SOURCE_ID"/></xsl:attribute>
						<xsl:value-of select="TITLE"/>
					</a>
					<xsl:if test="AUTHORITY!=''">
						&#160;[<xsl:value-of select="AUTHORITY"/>]
					</xsl:if>
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>

</xsl:stylesheet>
