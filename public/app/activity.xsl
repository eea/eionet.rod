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
	<xsl:include href="common.xsl"/>
	<xsl:include href="util.xsl"/>

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

	<xsl:template match="RowSet[@Name='Activity']/Row">
		<!-- form for delete activity action -->
		<xsl:if test="contains($permissions, ',/obligations:d,')='true'">
			<script language="JavaScript">
			<![CDATA[
				function delActivity() {
					if (confirm("Do you want to delete the reporting obligation?"))
						document.f.submit();
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
			</form>
		</xsl:if>

		<!-- navigation bar -->
		<xsl:if test="$printmode='N'">
      <table cellspacing="0" cellpadding="0" width="621" border="0">
			<tr>
         	<td align="bottom" width="20" background="images/bar_filled.jpg" height="25">&#160;</td>
          	<td width="600" background="images/bar_filled.jpg" height="25">
            <table height="8" cellSpacing="0" cellPadding="0" background="" border="0">
            	<tr>
		         	<td valign="bottom">
							<a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a>
						</td>
		            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
		         	<td valign="bottom">
							<a href="index.html"><span class="barfont">ROD</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom">
							<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$src-id"/>&amp;mode=S</xsl:attribute>
							<span class="barfont">Legislative instrument</span></a>
						</td>
            	   <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom"><span class="barfont">Reporting obligation</span></td>
                	<td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		</xsl:if>

		<div style="margin-left:13">

		<table width="610" border ="0">
			<tr>
				<td valign="top" width="76%">
					<span class="headgreen">Reporting obligation for <xsl:value-of select="T_SOURCE/ALIAS"/> &#160; <xsl:value-of select="T_SOURCE/SOURCE_CODE"/> </span>
				</td>
				<td align="right">
					<table width="100%" border="0">
					<tr>
						<td>
							<xsl:call-template name="Print"/>
						</td>
					</tr>
					<xsl:if test="$printmode='N'">
						<tr>
							<td><xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_RA</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
						</tr>
						<xsl:if test="count(//SubSet[@Name='Indicators']/Row) != 0 ">
						<tr>
							<td>
							<a><xsl:attribute name="href">javascript:openPopup("show.jsv", "id=<xsl:value-of select='$ra-id'/>&amp;mode=I")</xsl:attribute>
								<img src="images/indicators.jpg" alt="Show indicators" border="0"/></a><br/>
							</td>
						</tr>
						</xsl:if>
						<xsl:if test="T_OBLIGATION/PARAMETERS != ''">
							<tr>
								<td>
								<a><xsl:attribute name="href">javascript:openPopup("show.jsv", 'mode=M&amp;id=<xsl:value-of select="$ra-id"/>')</xsl:attribute>
									<img src="images/parameters.jpg" alt="Show parameters" border="0"/></a><br/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS != ''">
							<tr>
								<td>
										<a><xsl:attribute name="href">javascript:openPopup('csdeliveries','ACT_DETAILS_ID=<xsl:value-of select="$ra-id"/>&amp;COUNTRY_ID=%%')</xsl:attribute>
										 <img src="images/statusofdeliveries.jpg" alt="Show the status of country deliveries" border="0"/></a><br/>
								</td>
							</tr>
						</xsl:if>
						<tr><td align="center">
							<xsl:if test="$admin='true' and $printmode='N'">
								<xsl:attribute name="bgcolor">#A0A0A0</xsl:attribute>
								<xsl:attribute name="style">BORDER: #000000 1px solid;</xsl:attribute>
								<b><font color="#FFFFFF">Actions</font></b><br/><br/>
							</xsl:if>
						<xsl:if test="contains($permissions, ',/obligations:i,')='true'">
							<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>
								<img src="images/newobligation.png" alt="Create a new reporting obligation" border="0"/></a><br/>
							</xsl:if>
							<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
								<a><xsl:attribute name="href">activity.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute><img src="images/editobligation.png" alt="Edit this obligation" border="0"/></a><br/>
							</xsl:if>

							<xsl:if test="contains($permissions, ',/obligations:d,')='true'">
							<a href="javascript:delActivity()"><img src="images/deleteobligation.png" alt="Delete this obligation" border="0"/></a><br/>
						</xsl:if>				
						<xsl:if test="contains($permissions, ',/Admin:v,')='true'">
						<a>
							<xsl:attribute name="href">javascript:openPopup('history.jsv', 'id=<xsl:value-of select="$ra-id"/>&amp;entity=A')</xsl:attribute>
								<img src="images/showhistory.png" alt="Show history of changes" border="0"/>
							</a><br/>
						</xsl:if>				
						</td>
						</tr>
						</xsl:if><!-- printmode -->
				</table>
			</td>
			</tr>
		</table>

		<table width="600" border="0">
			<tr>
			<td width="100%" style="border:1 solid #006666">
				<table border="0" width="600" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
					<tr bgcolor="#ECECEC">
						<td width="30%" valign="top" align="left" style="border-right: 1 solid #C0C0C0">
							<span class="head0"><font face="Verdana" size="2"><b>Title</b></font></span>
						</td>
						<td width="65%" valign="top" align="left" style="border-right: 1 solid #C0C0C0">
							<xsl:choose>
								<xsl:when test="T_OBLIGATION/TITLE != ''">
									<xsl:value-of select="T_OBLIGATION/TITLE"/>
								</xsl:when>
								<xsl:otherwise>
									Reporting Obligation
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="5%" align="right">&#160;</td>
					</tr>
					<tr valign="top">
						<td style="border-right: 1 solid #C0C0C0"><b>Description</b></td>
						<td style="border-right: 1 solid #C0C0C0">
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
					<td bgcolor="#006666" colspan="3" valign="top" align="left">
						<font color="#FFFFFF"><b>Reporting dates and guidelines</b></font><br/>
					</td>
				</tr>
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><b>National reporting coordinators</b></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:choose>
						<xsl:when test="COORD_ROLE/ROLE_ID!=''">
							<a>
								<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="COORD_ROLE/ROLE_URL"/>')</xsl:attribute>
								<xsl:value-of select="COORD_ROLE/ROLE_NAME"/> (<xsl:value-of select="COORD_ROLE/ROLE_ID"/>)</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="T_OBLIGATION/COORDINATOR_ROLE != ''">
								<font color="#000000"><b>Directory role not found for '<xsl:value-of select="T_OBLIGATION/COORDINATOR_ROLE"/>'</b></font><br/>
							</xsl:if>
							<xsl:value-of select="T_OBLIGATION/COORDINATOR"/>&#160;
								<a target="_blank">
									<xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/COORDINATOR_URL"/></xsl:attribute>
										<xsl:value-of select="T_OBLIGATION/COORDINATOR_URL"/>
								</a>
						</xsl:otherwise>
						</xsl:choose>
					</td>
					<td align="center">
						<xsl:if test="COORD_ROLE/ROLE_ID!=''">
							<a><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="COORD_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
								<img src="images/details.jpg" alt="Additional details for logged-in users" border="0"/>
							</a>
						</xsl:if>
					</td>
				</tr>
				<tr valign="top"  bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><b>National reporting contacts</b></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:choose>
						<xsl:when test="RESP_ROLE/ROLE_ID!=''">
							<a>
								<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESP_ROLE/ROLE_URL"/>')</xsl:attribute>
								<xsl:value-of select="RESP_ROLE/ROLE_NAME"/> (<xsl:value-of select="RESP_ROLE/ROLE_ID"/>)</a>
						</xsl:when>
						<xsl:otherwise>
								<xsl:if test="T_OBLIGATION/RESPONSIBLE_ROLE != ''">
									<font color="#000000"><b>Directory role not found for '<xsl:value-of select="T_OBLIGATION/RESPONSIBLE_ROLE"/>'</b></font><br/>
								</xsl:if>
								<xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT"/>&#160;
								<a target="_blank">
									<xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT_URL"/></xsl:attribute>
										<xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT_URL"/>
								</a>
						</xsl:otherwise>
						</xsl:choose>
					</td>
					<td align="center">
						<xsl:if test="RESP_ROLE/ROLE_ID!=''">
							<a><xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESP_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
								<img src="images/details.jpg" alt="Additional details for logged-in users" border="0"/>
							</a>
						</xsl:if>&#160;
					</td>
				</tr>
				<!--tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Baseline reporting date</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:value-of select="T_OBLIGATION/FIRST_REPORTING"/>
					</td>
					<td style="border-right: 1 solid #C0C0C0"></td>
				</tr-->
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Reporting frequency</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:call-template name="RAReportingFrequency"/>
					</td>
					<td style="border-right: 1 solid #C0C0C0"></td>
				</tr>
				<tr valign="top"   bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Next report due</span></td>
					<td style="border-right: 1 solid #C0C0C0">
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
								<font color="red">terminated</font>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td style="border-right: 1 solid #C0C0C0"></td>
				</tr>
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Date comments</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:value-of select="T_OBLIGATION/DATE_COMMENTS"/>&#160;
					</td>
					<td style="border-right: 1 solid #C0C0C0"></td>
				</tr>
				<tr valign="top"   bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Report to</span></td>
					<td style="border-right: 1 solid #C0C0C0">
							<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>&#160;
					</td>
					<td align="center">
						<xsl:if test="T_CLIENT/PK_CLIENT_ID != ''">
						<a><xsl:attribute name="href">javascript:openPopup('client.jsv', 'id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
							<img src="images/details.jpg" alt="Show client details" border="0"/>
						</a>
						</xsl:if>
					</td>
				</tr>
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Other clients using this reporting</span></td>
					<td style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="OtherClients"/>
					</td>
					<td></td>
				</tr>
				<tr valign="top"   bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Reporting guidelines</span></td>
					<td style="border-right: 1 solid #C0C0C0">
							<xsl:choose>
							<xsl:when test="T_OBLIGATION/REPORT_FORMAT_URL!=''">
								<a target="_blank"><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
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
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Extra information</span></td>
					<td style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="break">
								 <xsl:with-param name="text" select="T_OBLIGATION/REPORTING_FORMAT"/>
							</xsl:call-template>&#160;
					</td>
					<td></td>
				</tr>
				<tr valign="top"   bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Principle repository</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:choose>
							<xsl:when test="T_OBLIGATION/LOCATION_PTR != ''">
								<a target="_blank"><xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/LOCATION_PTR"/></xsl:attribute>						
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
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Type of information reported</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:apply-templates select="SubSet[@Name='InfoType']"/>
					</td>
					<td></td>
				</tr>
				<tr>
					<td bgcolor="#006666" colspan="3" valign="top" align="left"><font color="#FFFFFF"><b>Legal framework</b></font></td>
				</tr>
				<tr valign="top"  bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Parent legislative instrument</span></td>
					<td style="border-right: 1 solid #C0C0C0">
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
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Sibling reporting obligations</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:call-template name="Sibling"/>
					</td>
					<td></td>
				</tr>
				<tr valign="top"  bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Type of obligation</span></td>
					<td style="border-right: 1 solid #C0C0C0">
							<xsl:value-of select="T_LOOKUP/C_TERM"/>
					</td>
					<td></td>
				</tr>
				<tr valign="top">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Countries</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:apply-templates select="//RowSet[@Name='Spatial']"/>
					</td>
					<td align="center">
						<a><xsl:attribute name="href">javascript:openPopup('spatialhistory.jsv', 'ID=<xsl:value-of select="$ra-id"/>')</xsl:attribute>
							<img src="images/details.jpg" alt="Details for countries' joining history" border="0"/>
						</a>
					</td>
				</tr>
				<tr valign="top"   bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Environmental issues</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:apply-templates select="//RowSet[@Name='EnvIssue']"/>&#160;
					</td>
					<td></td>
				</tr>
				<tr valign="top" >
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">General comments</span></td>
					<td style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="break">
								 <xsl:with-param name="text" select="T_OBLIGATION/COMMENT"/>
							</xsl:call-template>&#160;
					</td>
					<td style="border-right: 1 solid #C0C0C0"></td>
				</tr>
				<!--tr valign="top"   bgcolor="#ECECEC">
					<td style="border-right: 1 solid #C0C0C0"><span class="head0">Authority giving rise to the obligation</span></td>
					<td style="border-right: 1 solid #C0C0C0">
						<xsl:value-of select="T_OBLIGATION/AUTHORITY"/>
					</td>
					<td></td>
				</tr-->
			</table>
		</td>
		</tr>
	</table>


		</div>
	</xsl:template>

	<xsl:template match="//RowSet[@Name='Spatial']">
		<table>
			<tr>
				<td>
					<xsl:for-each select="Row">
						<xsl:choose>
							<xsl:when test="T_RASPATIAL_LNK/VOLUNTARY='Y'">
								<span title="Informal participation in the reporting obligation"><xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>*</span>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="position()!=count(//RowSet[@Name='Spatial']/Row)">, </xsl:if>
					</xsl:for-each>
				</td>
			</tr>
			</table>
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

	<xsl:template match="SubSet[@Name='Parameter']">
		<xsl:if test="count(Row)>0">
		<table width="100%" colspan="5" border="1">
			<tr>
				<td bgcolor="#646666"><span class="head0"><font color="white">Nr</font></span></td>
				<td bgcolor="#646666"><span class="head0"><font color="white">Parameter</font></span></td>
			</tr>
			<xsl:for-each select="Row">
				<tr>
					<td>
						<xsl:number format="1."/>
					</td>
					<td>
						<xsl:value-of select="T_PARAMETER/PARAMETER_NAME"/>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		</xsl:if>
	</xsl:template>

	<xsl:template name="OtherClients">
		<table cellpadding="0" cellspacing="0">
			<xsl:for-each select="SubSet[@Name='CCClients']/Row/T_CLIENT">
				<tr>
					<td>
						<a>
							<xsl:attribute name="href">javascript:openPopup('client.jsv', 'id=<xsl:value-of select="PK_CLIENT_ID"/>')</xsl:attribute>
							<xsl:value-of select="CLIENT_NAME"/>
						</a>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

	<xsl:template name="Sibling">
		<table cellpadding="0" cellspacing="0">
			<xsl:for-each select="SubSet[@Name='Sibling']/Row/T_OBLIGATION">
				<tr>
					<td>
						<a>
							<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RA_ID"/>&amp;mode=A&amp;aid=<xsl:value-of select="FK_SOURCE_ID"/></xsl:attribute>
							<xsl:value-of select="TITLE"/>
						</a>
						<xsl:if test="AUTHORITY!=''">
							&#160;[<xsl:value-of select="AUTHORITY"/>]
						</xsl:if>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
