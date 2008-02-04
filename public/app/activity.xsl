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
	
	<xsl:variable name="col_class">
		twocolumns
	</xsl:variable>
	
	<xsl:include href="ncommon.xsl"/>
	
	<xsl:variable name="ra-id">
		<xsl:value-of select="/XmlData/RowSet/@ID"/>
	</xsl:variable>

	<xsl:variable name="src-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>
	
	<xsl:variable name="latest">
		<xsl:value-of select="/XmlData/RowSet/@latest"/>
	</xsl:variable>
	
	<xsl:variable name="admin">
		<xsl:value-of select="/XmlData/RowSet/@auth"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>
	
	<xsl:variable name="tab">
		<xsl:value-of select="/XmlData/RowSet/@tab"/>
	</xsl:variable>
	
	<xsl:variable name="spatialID">
		<xsl:value-of select="/XmlData/RowSet/@spatialID"/>
	</xsl:variable>
	
	<xsl:variable name="spatialHistoryID">
		<xsl:value-of select="/XmlData/RowSet/@spatialHistoryID"/>
	</xsl:variable>
	
	<xsl:variable name="allCountries"><xsl:value-of select="count(child::XmlData/RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>
	
	<xsl:variable name="sel_actdetails">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ACT_DETAILS_ID='),'&amp;')"/>
	</xsl:variable>
	
	<xsl:variable name="sel_country">
		<xsl:value-of select="substring-after(/XmlData/xml-query-string,'COUNTRY_ID=')"/>
	</xsl:variable>
	
	<xsl:variable name="sortorder">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ORD='),'&amp;')"/>
	</xsl:variable>
	
	<xsl:variable name="country_name">
		<xsl:value-of select="//XmlData/RowSet[@Name='Main']/Row/T_SPATIAL/SPATIAL_NAME"/>
	</xsl:variable>
	
	<xsl:variable name="sel_actdetails">
		<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ACT_DETAILS_ID='),'&amp;')"/>
	</xsl:variable>
	
	<xsl:variable name="item-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ITEM_TYPE"/>
	</xsl:variable>

	<!--xsl:template match="XmlData">
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
	</xsl:template-->

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

	<xsl:template match="XmlData">
	
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
				<xsl:choose>
					<xsl:when test="$tab='participation'">
						<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID">
							<xsl:attribute name="value"><xsl:value-of select="/XmlData/RowSet[@Name='Spatialhistory']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>
						</input>
					</xsl:when>
					<xsl:when test="$tab='deliveries'">
						<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID">
							<xsl:attribute name="value"><xsl:value-of select="/XmlData/RowSet[@Name='RA']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>
						</input>
					</xsl:when>
					<xsl:when test="$tab='history'">
						<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID">
							<xsl:attribute name="value"><xsl:value-of select="/XmlData/RowSet[@Name='History']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>
						</input>
					</xsl:when>
					<xsl:otherwise>
						<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID">
							<xsl:attribute name="value"><xsl:value-of select="$src-id"/></xsl:attribute>
						</input>
					</xsl:otherwise>
				</xsl:choose>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/REDIRECT_URL" value=""></input>
			</form>
		</xsl:if>
		<div id="workarea">
			<div id="tabbedmenu">
			    <ul>
				<xsl:choose>
					<xsl:when test="$tab='overview'">
						<li id="currenttab"><span>Overview</span></li>
					</xsl:when>
					<xsl:otherwise>
						<li><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$ra-id"/>&amp;mode=A&amp;tab=overview</xsl:attribute>Overview</a></li>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$tab='legislation'">
						<li id="currenttab"><span>Legislation</span></li>
					</xsl:when>
					<xsl:otherwise>
						<li><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$ra-id"/>&amp;mode=A&amp;tab=legislation</xsl:attribute>Legislation</a></li>				
					</xsl:otherwise>
				</xsl:choose>
				<!--xsl:choose>
					<xsl:when test="$tab='participation'">
						<li id="currenttab"><span>Participation</span></li>
					</xsl:when>
					<xsl:otherwise>
						<li><a><xsl:attribute name="href">show.jsv?ID=<xsl:value-of select="$ra-id"/>&amp;mode=A&amp;tab=participation</xsl:attribute>Participation</a></li>				
					</xsl:otherwise>
				</xsl:choose-->
				<xsl:if test="(//RowSet/Row/T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS != '') or (/XmlData/RowSet[@Name='Spatialhistory']/Row/T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS != '') or (/XmlData/RowSet[@Name='History']/Row/T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS != '') or (/XmlData/RowSet[@Name='RA']/Row/T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS != '')">
					<xsl:choose>
						<xsl:when test="$tab='deliveries'">
							<li id="currenttab"><span>Deliveries</span></li>
						</xsl:when>
						<xsl:otherwise>
							<li><a><xsl:attribute name="href">show.jsv?ACT_DETAILS_ID=<xsl:value-of select="$ra-id"/>&amp;COUNTRY_ID=%%&amp;mode=A&amp;tab=deliveries</xsl:attribute>Deliveries</a></li>				
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:if test="(//RowSet/Row/T_OBLIGATION/PARAMETERS != '') or (/XmlData/RowSet[@Name='Spatialhistory']/Row/T_OBLIGATION/PARAMETERS != '') or (/XmlData/RowSet[@Name='History']/Row/T_OBLIGATION/PARAMETERS != '') or (/XmlData/RowSet[@Name='RA']/Row/T_OBLIGATION/PARAMETERS != '')">
					<xsl:choose>
						<xsl:when test="$tab='parameters'">
							<li id="currenttab"><span>Parameters</span></li>
						</xsl:when>
						<xsl:otherwise>
							<li><a><xsl:attribute name="href">show.jsv?mode=M&amp;id=<xsl:value-of select="$ra-id"/>&amp;tab=parameters</xsl:attribute>Parameters</a></li>				
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$tab='history'">
						<li id="currenttab"><span>History</span></li>
					</xsl:when>
					<xsl:otherwise>
						<li><a><xsl:attribute name="href">show.jsv?mode=A&amp;id=<xsl:value-of select="$ra-id"/>&amp;entity=A&amp;tab=history</xsl:attribute>History</a></li>				
					</xsl:otherwise>
				</xsl:choose>
			    </ul>
			</div>
			<div id="operations">
				<ul>
					<!--li class="help"><a href="javascript:openViewHelp('HELP_RA')">Page help</a></li-->
					<!--xsl:if test="count(//SubSet[@Name='Indicators']/Row) != 0 ">
						<li>
						<a><xsl:attribute name="href">javascript:openPopup("show.jsv", "id=<xsl:value-of select='$ra-id'/>&amp;mode=I")</xsl:attribute>
							Indicators</a>
						</li>
					</xsl:if-->
					<!--li>
						<a><xsl:attribute name="href">show.jsv?mode=M&amp;id=<xsl:value-of select="$ra-id"/></xsl:attribute>
						Parameters</a>
					</li-->
					<!--xsl:if test="//RowSet/Row/T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS != ''">
						<li>
							<a><xsl:attribute name="href">csdeliveries?ACT_DETAILS_ID=<xsl:value-of select="$ra-id"/>&amp;COUNTRY_ID=%%</xsl:attribute>
							Status of deliveries</a>
						</li>
					</xsl:if-->
					<xsl:if test="$latest != 'n'">
						<xsl:if test="contains($permissions, ',/obligations:i,')='true'">
							<li>
								<xsl:choose>
									<xsl:when test="$tab='participation'">
										<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="/XmlData/RowSet[@Name='Spatialhistory']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>New obligation</a>
									</xsl:when>
									<xsl:when test="$tab='deliveries'">
										<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="/XmlData/RowSet[@Name='RA']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>New obligation</a>
									</xsl:when>
									<xsl:when test="$tab='history'">
										<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="/XmlData/RowSet[@Name='History']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>New obligation</a>
									</xsl:when>
									<xsl:otherwise>
										<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>New obligation</a>
									</xsl:otherwise>
								</xsl:choose>
							</li>
						</xsl:if>
						<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':u,'))='true'">
							<li>
								<xsl:choose>
									<xsl:when test="$tab='participation'">
										<a><xsl:attribute name="href">activity.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="/XmlData/RowSet[@Name='Spatialhistory']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>Edit obligation</a>
									</xsl:when>
									<xsl:when test="$tab='deliveries'">
										<a><xsl:attribute name="href">activity.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="/XmlData/RowSet[@Name='RA']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>Edit obligation</a>
									</xsl:when>
									<xsl:when test="$tab='history'">
										<a><xsl:attribute name="href">activity.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="/XmlData/RowSet[@Name='History']/Row/T_OBLIGATION/FK_SOURCE_ID"/></xsl:attribute>Edit obligation</a>
									</xsl:when>
									<xsl:otherwise>
										<a><xsl:attribute name="href">activity.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>Edit obligation</a>
									</xsl:otherwise>
								</xsl:choose>
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
						<!--li>
							<a>
								<xsl:attribute name="href">versions.jsp?id=<xsl:value-of select="$ra-id"/>&amp;tab=T_OBLIGATION&amp;id_field=PK_RA_ID</xsl:attribute>
								Show history
							</a>
						</li-->
					</xsl:if>
				</ul>
			</div>
			<xsl:choose>
				<xsl:when test="$tab='overview'">
					<xsl:apply-templates select="RowSet[@Name='Activity']"/>
				</xsl:when>
				<xsl:when test="$tab='legislation'">
					<xsl:apply-templates select="RowSet[@Name='Activity']"/>
				</xsl:when>
				<xsl:when test="$tab='participation'">
					<xsl:apply-templates select="RowSet[@Name='Spatialhistory']"/>
				</xsl:when>
				<xsl:when test="$tab='deliveries'">
					<xsl:apply-templates select="RowSet[@Name='RA']"/>
				</xsl:when>
				<xsl:when test="$tab='parameters'">
					<xsl:apply-templates select="RowSet[@Name='Activity']"/>
				</xsl:when>
				<xsl:when test="$tab='history'">
					<xsl:apply-templates select="RowSet[@Name='History']"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="nofound"/>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		<xsl:call-template name="LIRORAFooter">
			<xsl:with-param name="table">RA</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="RowSet[@Name='Activity']/Row">
	
		<xsl:if test="$tab='overview'">
			
			<h1>Reporting obligation for <xsl:value-of select="T_OBLIGATION/TITLE"/></h1>
			<table class="datatable">
			<col style="width:30%" />
			<col style="width:65%" />
			<col style="width:5%" />
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Title</th>
					<td>
						
						<xsl:choose>
							<xsl:when test="T_OBLIGATION/TITLE != ''">
								<!--xsl:value-of select="T_SOURCE/ALIAS"/> &#160; <xsl:value-of select="T_SOURCE/SOURCE_CODE"/-->
								Reporting obligation for <xsl:value-of select="T_OBLIGATION/TITLE"/>
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
								<xsl:value-of select="COORD_ROLE/ROLE_NAME"/> (<xsl:value-of select="COORD_ROLE/ROLE_ID"/>)</a><br/>
								<a title="Role details on CIRCA for members"><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="COORD_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
									Additional details for logged-in  users
								</a>
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
								<br/>
								<a title="Role details on CIRCA"><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESP_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
									Additional details for logged-in  users
								</a>
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
						<!--xsl:if test="RESP_ROLE/ROLE_ID!=''">
							<a title="Role details on CIRCA"><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESP_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
								<img src="images/details.gif" alt="Additional details for logged-in users" border="0"/>
							</a>
						</xsl:if-->
					</td>
				</tr>
				<!--tr valign="top">
					<td style="border-right: 1px solid #C0C0C0"><span class="head0">Baseline reporting date</span></td>
					<td style="border-right: 1px solid #C0C0C0">
						<xsl:value-of select="T_OBLIGATION/FIRST_REPORTING"/>
					</td>
					<td style="border-right: 1px solid #C0C0C0"></td>
				</tr-->
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Reporting frequency</th>
					<td>
						<xsl:call-template name="RAReportingFrequency"/>
					</td>
					<td></td>
				</tr>
				<tr class="zebraeven">
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
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Date comments</th>
					<td>
						<xsl:value-of select="T_OBLIGATION/DATE_COMMENTS"/>&#160;
					</td>
					<td></td>
				</tr>
				<tr class="zebraeven">
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
				<tr class="zebraodd">
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
		</table>
	</xsl:if>
	<xsl:if test="$tab='legislation'">
			
			<h1>Legislation for: <xsl:value-of select="T_OBLIGATION/TITLE"/></h1>
			<table class="datatable">
			<col style="width:30%" />
			<col style="width:65%" />
			<col style="width:5%" />
				<tr>
					<td colspan="3" class="dark_green_heading">Legal framework</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Legislative instrument title</th>
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
						<!--a title="History of participation"><xsl:attribute name="href">spatialhistory.jsv?ID=<xsl:value-of select="$ra-id"/></xsl:attribute>
							<img src="images/details.gif" alt="Status of country participation" border="0"/>
						</a-->
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
	</xsl:if>
	<xsl:if test="$tab='parameters'">
		<xsl:for-each select="//RowSet[@Name='Activity']/Row">
			<h1>Parameters for <xsl:value-of select="T_OBLIGATION/TITLE"/></h1>
			<!--table class="datatable">
				<tr valign="top">
					<th scope="row" class="scope-row">Reporting obligation:</th>
					<td ><xsl:value-of select="T_OBLIGATION/TITLE"/></td>
				</tr>
				<tr valign="top">
					<th scope="row" class="scope-row">Reporting frequency:</th>
					<td>
						<xsl:call-template name="RAReportingFrequency"/>
					</td>
				</tr>
				<tr valign="top">
					<th scope="row" class="scope-row">Client organisation:</th>
					<td>
							<a>
								<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
								<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
							</a>
					</td>
				</tr>
				<tr valign="top">
					<th scope="row" class="scope-row">Other clients using this reporting:</th>
					<td>
						<xsl:for-each select="SubSet[@Name='CCClients']/Row">
							<a>
								<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
								<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
							</a><br/>
						</xsl:for-each>
		
					</td>
				</tr>
				<tr valign="top">
					<th scope="row" class="scope-row">Reporting guidelines:</th>
					<td>
						<xsl:choose>
							<xsl:when test="T_OBLIGATION/REPORT_FORMAT_URL!=''">
								<a>
									<xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
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
					</td>
				</tr>
			</table-->
			 
		<!-- oneCountry=0 one country, one country = 1 all countries -->
		<xsl:if test="T_OBLIGATION/PARAMETERS != ''">
		<table class="datatable">
		
		<tr>
			<th scope="col" class="scope-col" style="text-align: left">Parameters</th>
		</tr>
		<tr>
			<td>
				<xsl:call-template name="break">
					<xsl:with-param name="text" select="T_OBLIGATION/PARAMETERS"/>
				</xsl:call-template>
			</td>
		</tr>
		
		</table>
		</xsl:if>
		
		</xsl:for-each>
		
		<xsl:if test="count(//RowSet[@Name='Activity']/Row/DDPARAM) > 0">
		<h2>Parameters from Data Dictionary</h2>
		<table class="datatable">
			<col style="width:40%"/>
			<col style="width:30%"/>
			<col style="width:30%"/>
			<tr>
				<th scope="col" class="scope-col">Parameter name</th>
				<th scope="col" class="scope-col">Table name</th>
				<th scope="col" class="scope-col">Dataset name</th>
			</tr>
			<xsl:for-each select="//RowSet[@Name='Activity']/Row/DDPARAM">
				<tr>
					<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">zebraeven</xsl:if></xsl:attribute>
					<td>
						<a href="{ELEMENT_URL}" title="View parameter details in Data Dictionary"><xsl:value-of select="ELEMENT_NAME"/></a>
					</td>
					<td>
						<xsl:value-of select="TABLE_NAME"/>
					</td>
					<td>
						<xsl:value-of select="DATASET_NAME"/>
					</td>
				</tr>
			</xsl:for-each>
		
		</table>
		<br/>
		</xsl:if>
	</xsl:if>
	</xsl:template>
	
	<xsl:template match="RowSet[@Name='History']">
				<xsl:if test="//RowSet[@Name='History']/Row/T_HISTORY/ITEM_ID != 0">
					<h1>History of <xsl:value-of select="//RowSet[@Name='History']/Row/T_OBLIGATION/TITLE"/></h1>
				</xsl:if>
			
				<table class="datatable">
				<tr>
					<th scope="col">Time</th>
					<th scope="col">Action</th>
					<th scope="col">User</th>
					<th scope="col">Description</th>
				</tr>
			
			
			<xsl:for-each select="Row">
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
	</xsl:template>
	
	<xsl:template match="RowSet[@Name='Spatialhistory']">
		<h1>Participation for <xsl:value-of select="//RowSet/Row/T_OBLIGATION/TITLE"/></h1>
			<!--table class="datatable">
				<tr>
					<th scope="row" class="scope-row">Reporting obligation:</th>
					<td><xsl:value-of select="//RowSet/Row/T_OBLIGATION/TITLE"/></td>
				</tr>
				<tr valign="top">
					<th scope="row" class="scope-row">Client organisation: </th>
					<td><xsl:value-of select="//RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
				</tr>
			</table-->

			<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':u,'))='true'">
				<form name="f1" method="POST" action="editperiod">
					<span style="font-weight: bold;">Edit period:</span>
					<fieldset>
						<input type="hidden"><xsl:attribute name="name">ra_id</xsl:attribute><xsl:attribute name="value"><xsl:value-of select="$ra-id"/></xsl:attribute></input>
						From <input size="7" type="text"><xsl:attribute name="name">from</xsl:attribute><xsl:attribute name="value"></xsl:attribute></input>
						to <input size="7" type="text"><xsl:attribute name="name">to</xsl:attribute><xsl:attribute name="value"></xsl:attribute></input>
						&#160;<input type="submit" value="Save"></input>
					</fieldset>
				</form>
				<br/>
			</xsl:if>
			<table class="datatable">
			<xsl:choose>
			<xsl:when test="contains($permissions, concat(',/obligations/',$ra-id,':u,'))='true'">
				<col style="width:25%"/>
				<col style="width:25%"/>
				<col style="width:40%"/>
				<col style="width:10%"/>
			</xsl:when>
			<xsl:otherwise>
				<col style="width:30%"/>
				<col style="width:30%"/>
				<col style="width:40%"/>
			</xsl:otherwise>
			</xsl:choose>
				<tr>
					<th scope="col" class="scope-col">
						Country
					</th>
					<th scope="col" class="scope-col">
						Status
					</th>
					<th scope="col" class="scope-col">
						Participation period
					</th>
					<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':u,'))='true'">
						<th scope="col" class="scope-col">
							Edit period
						</th>
					</xsl:if>
				</tr>
			<xsl:for-each select="Row">
			<tr>
					<xsl:attribute name="class">
							<xsl:if test="position() mod 2 = 0">zebraeven</xsl:if>
					</xsl:attribute>
			<td>
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='Y'">
						Voluntary reporting
					</xsl:when>
					<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='N'">
						Formal reporting
					</xsl:when>
				</xsl:choose>
			</td>
			<td>
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
			<xsl:if test="contains($permissions, concat(',/obligations/',$ra-id,':u,'))='true'">
				<td class="center">
					<a><xsl:attribute name="href">show.jsv?ID=<xsl:value-of select="$ra-id"/>&amp;spatialID=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>&amp;spatialHistoryID=<xsl:value-of select="T_SPATIAL_HISTORY/PK_SPATIAL_HISTORY_ID"/>&amp;mode=A&amp;tab=participation</xsl:attribute>
						Edit
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
								<input type="submit" value="Save"></input>
							</td>
						</tr>
					</form>
				</xsl:if>
			</xsl:if>
			</xsl:for-each>
			</table>
	</xsl:template>
	
	<xsl:template match="RowSet[@Name='RA']">
		<xsl:for-each select="Row">
			<h1>Deliveries for <xsl:value-of select="T_OBLIGATION/TITLE"/></h1>
			<!--table class="datatable">
			<xsl:if test="contains($permissions, ',/Admin/Harvest:v,')='true'">
				<tr><td></td><td><i>last harvested: <xsl:value-of select="T_OBLIGATION/LAST_HARVESTED"/>&#160;</i></td></tr>
			</xsl:if>
				<tr>
					<th scope="row" class="scope-row">Reporting obligation:</th>
					<td ><xsl:value-of select="T_OBLIGATION/TITLE"/></td>
				</tr>
				<tr>
					<th scope="row" class="scope-row">Reporting frequency:</th>
					<td>
						<xsl:call-template name="RAReportingFrequency"/>
					</td>
				</tr>
				<tr>
					<th scope="row" class="scope-row">Client organisation:</th>
					<td>
							<a>
								<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
								<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
							</a>
					</td>
				</tr>
				<tr>
					<th scope="row" class="scope-row">Other clients using this reporting:</th>
					<td>
						<xsl:for-each select="SubSet[@Name='CCClients']/Row">
							<a>
								<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
								<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
							</a><br/>
						</xsl:for-each>
		
					</td>
				</tr>
				<tr>
					<th scope="row" class="scope-row">Reporting guidelines:</th>
					<td><a><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
						<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/></a></td>
				</tr>
			</table-->
		</xsl:for-each>
	
		<!-- set the default sortorder  -->
			<xsl:variable name="cur_sortorder">
				<xsl:choose>
					<xsl:when test="string-length($sortorder) = 0">
						<xsl:choose>
							<xsl:when test="$allCountries=1">T_SPATIAL.SPATIAL_NAME</xsl:when>
							<xsl:otherwise>UPLOAD_DATE DESC</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise><xsl:value-of select="translate($sortorder,'%20',' ')"/></xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="recCount">
				<xsl:value-of select="count(//RowSet[@Name='Main']/Row/T_DELIVERY)"/>
			</xsl:variable>
		
			<!--div class="smallfont" style="font-size: 8pt; font-weight: bold">[<xsl:value-of select="$recCount"/> record(s) returned]</div-->
			<p class="headsmall">[<xsl:value-of select="$recCount"/> record(s) returned]</p>	
		<br/>
		
		
		<!-- oneCountry=0 one country, one country = 1 all countries -->
		<table style="table-layout:fixed; width:100%" class="sortable">
			<xsl:if test="$allCountries=1">
			<col style="width:20%"/>
			<col style="width:25%"/>
			<col style="width:15%"/>
			<col style="width:27%"/>
			<col style="width:13%"/>
			</xsl:if>
			<xsl:if test="$allCountries!=1">
			<col style="width:20%"/>
			<col style="width:25%"/>
			<col style="width:25%"/>
			<col style="width:30%"/>
			</xsl:if>
			<thead>
				<tr>
				<!-- contact -->
					<xsl:call-template name="createSortable">
						<xsl:with-param name="title" select="'Responsible persons'"/>
						<xsl:with-param name="text" select="'Contact'"/>
						<xsl:with-param name="sorted" select="'ROLE_DESCR'"/>
						<xsl:with-param name="width" select="'25%'"/>
						<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
					</xsl:call-template>
				<!-- delivery title -->
					<xsl:call-template name="createSortable">
						<xsl:with-param name="title" select="'Title of delivery'"/>
						<xsl:with-param name="text" select="'Delivery Title'"/>
						<xsl:with-param name="sorted" select="'T_DELIVERY.TITLE'"/>
						<xsl:with-param name="width" select="'24%'"/>
						<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
					</xsl:call-template>
				<!-- delivery date -->
					<xsl:call-template name="createSortable">
						<xsl:with-param name="title" select="'Date of delivery'"/>
						<xsl:with-param name="text" select="'Delivery Date'"/>
						<xsl:with-param name="sorted" select="'UPLOAD_DATE'"/>
						<xsl:with-param name="width" select="'11%'"/>
						<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
					</xsl:call-template>
				<!-- period covered -->
					<xsl:call-template name="createSortable">
						<xsl:with-param name="title" select="'Period covered by this delivery'"/>
						<xsl:with-param name="text" select="'Period covered'"/>
						<xsl:with-param name="sorted" select="'COVERAGE'"/>
						<xsl:with-param name="width" select="'27%'"/>
						<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
					</xsl:call-template>
				<!-- country -->
					<xsl:if test="$allCountries=1">
						<xsl:call-template name="createSortable">
							<xsl:with-param name="title" select="'Country'"/>
							<xsl:with-param name="text" select="'Country'"/>
							<xsl:with-param name="sorted" select="'SPATIAL_NAME'"/>
							<xsl:with-param name="width" select="'13%'"/>
							<xsl:with-param name="cur_sorted" select="$cur_sortorder"/>
						</xsl:call-template>
					</xsl:if>
				</tr>
			</thead>
		
			<xsl:for-each select="//RowSet[@Name='Main']/Row">
				<tr>
					<xsl:attribute name="class">
						<xsl:if test="position() mod 2 = 0">zebraeven</xsl:if>
					</xsl:attribute>
					<td>
							<xsl:if test="T_OBLIGATION/RESPONSIBLE_ROLE != ''">
								<xsl:choose>
									<xsl:when test="T_ROLE/ROLE_DESCR=''">
										<xsl:choose>
											<xsl:when test="T_SPATIAL/SPATIAL_ISMEMBERCOUNTRY='Y'">
												<xsl:attribute name="title"><xsl:value-of select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-mc-',T_SPATIAL/SPATIAL_TWOLETTER)"/></xsl:attribute>
												<xsl:call-template name="short">
													<xsl:with-param name="text" select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-mc-',T_SPATIAL/SPATIAL_TWOLETTER)"/>
													<xsl:with-param name="length">40</xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:attribute name="title"><xsl:value-of select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-cc-',T_SPATIAL/SPATIAL_TWOLETTER)"/></xsl:attribute>
												<xsl:call-template name="short">
													<xsl:with-param name="text" select="concat(T_OBLIGATION/RESPONSIBLE_ROLE,'-cc-',T_SPATIAL/SPATIAL_TWOLETTER)"/>
													<xsl:with-param name="length">40</xsl:with-param>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when>
								<xsl:otherwise>
									<!--a>
										<xsl:attribute name="title"><xsl:value-of select="T_ROLE/ROLE_DESCR"/></xsl:attribute>
										<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_URL"/>')</xsl:attribute>
										<xsl:call-template name="short">
											<xsl:with-param name="text" select="T_ROLE/ROLE_DESCR"/>
											<xsl:with-param name="length">30</xsl:with-param>
										</xsl:call-template>
									</a>&#160;
									<img src="images/details.gif" alt="Additional details for logged-in users">
										<xsl:attribute name="onclick">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
									</img-->
									<a>
										<xsl:attribute name="href">responsible.jsp?role=<xsl:value-of select="T_OBLIGATION/RESPONSIBLE_ROLE"/>&amp;spatial=<xsl:value-of select="T_SPATIAL/SPATIAL_TWOLETTER"/>&amp;member=<xsl:value-of select="T_SPATIAL/SPATIAL_ISMEMBERCOUNTRY"/></xsl:attribute>
										<xsl:call-template name="short">
											<xsl:with-param name="text" select="T_ROLE/ROLE_DESCR"/>
											<xsl:with-param name="length">15</xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						&#160;
				</td>
				<td>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="T_DELIVERY/DELIVERY_URL"/>
						</xsl:attribute>
						<xsl:value-of select="T_DELIVERY/TITLE"/>
					</a>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="T_DELIVERY/UPLOAD_DATE != '0000-00-00'">
							<xsl:value-of select="T_DELIVERY/UPLOAD_DATE"/>
						</xsl:when>
						<xsl:otherwise>
							&lt;No date&gt;
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td>
					<xsl:value-of select="T_DELIVERY/COVERAGE"/>
					 &#160;
				</td>
				<xsl:if test="$allCountries=1">
					<td>
						<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
					</td>
				</xsl:if>
			</tr>
		</xsl:for-each>
		</table>
		<p style="text-align:center">
			Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.<br/>
			There can be a delay of up to one day before they show up.
		</p>
	</xsl:template>
	
	<!-- EK 050210 template for calculating request URL for sorting -->
	<xsl:template name="createURL">
		<xsl:param name="sorted"/>
		<xsl:variable name="uri">show.jsv</xsl:variable>
		<xsl:variable name="actdetails_param">
			<xsl:if test="string-length($sel_actdetails) &gt; 0">&amp;ACT_DETAILS_ID=<xsl:value-of select="$sel_actdetails"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="country_param">
			<xsl:if test="string-length($sel_country) &gt; 0">&amp;COUNTRY_ID=<xsl:value-of select="$sel_country"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="ORD">
			<xsl:if test="string-length($sorted) &gt; 0">&amp;ORD=<xsl:value-of select="$sorted"/></xsl:if>
		</xsl:variable>
		
		<xsl:variable name="params">
			<xsl:value-of select="concat($ORD,$actdetails_param,$country_param)"/>
		</xsl:variable>
		<xsl:value-of select="concat($uri,'?',substring($params,2))"/>
	</xsl:template>
	
	<xsl:template match="//RowSet[@Name='Spatial']">
		<xsl:for-each select="Row">
			<a><xsl:attribute name="href">countryinfo.jsp?ra-id=<xsl:value-of select="$ra-id"/>&amp;spatial=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>&amp;member=<xsl:value-of select="T_SPATIAL/SPATIAL_ISMEMBERCOUNTRY"/>&amp;vol=<xsl:value-of select="T_RASPATIAL_LNK/VOLUNTARY"/></xsl:attribute>
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
						<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RA_ID"/>&amp;mode=A</xsl:attribute>
						<xsl:value-of select="TITLE"/>
					</a>
					<xsl:if test="AUTHORITY!=''">
						&#160;[<xsl:value-of select="AUTHORITY"/>]
					</xsl:if>
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>
	
	<xsl:template name="PageHelp">
		<xsl:choose>
			<xsl:when test="$tab='participation'">
				<a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp('HELP_SPATIALHISTORY')" onclick="pop(this.href);return false;"><span>Page help</span></a>
			</xsl:when>
			<xsl:when test="$tab='parameters'">
				<a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp('HELP_PARAMETERS')" onclick="pop(this.href);return false;"><span>Page help</span></a>
			</xsl:when>
			<xsl:when test="$tab='deliveries'">
				<a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp('HELP_DELIVERIES')" onclick="pop(this.href);return false;"><span>Page help</span></a>
			</xsl:when>
			<xsl:otherwise>
				<a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp('HELP_RA')" onclick="pop(this.href);return false;"><span>Page help</span></a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--xsl:template name="createURL"/-->
</xsl:stylesheet>
