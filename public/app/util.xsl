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
 * Original Code: Ander Tenno (TietoEnator)
 * -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java" version="1.0">

	<xsl:param name="req" select="'default value'"/>
	<xsl:variable name="printmode" select="java:eionet.rod.RODUtil.getParameter($req, 'printmode')"/>

	<xsl:template name="LIRORAFooter">
		<xsl:param name="table"/>
		<xsl:call-template name="FooterFrame">
				<xsl:with-param name="type"><xsl:value-of select="$table"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="CommonFooter">
		<xsl:call-template name="FooterFrame">
				<xsl:with-param name="type">0</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="FooterFrame">

		<!-- type=0 common, 1-RAROLI -->
		<xsl:param name="type"></xsl:param>

		<script language="JavaScript" src="script/util.js"></script>

		<table>
			<tr height="20"><td></td>
			</tr>
			<tr>
				<td width="600" align="center">
									<span class="barfont">
									<a href="javascript:history.back()">Back</a>
									| <a href="mailto:rod@eea.eu.int">E-mail</a> 
									| <a>
											<xsl:attribute name="href">
												<xsl:call-template name="Disclaimer_URL"/>
											</xsl:attribute>
										Disclaimer
									</a>
									<xsl:choose>
									<xsl:when test="$type=0">
                  | Last updated: 
										<xsl:call-template name="LastUpdated">
											<xsl:with-param name="date1"><xsl:value-of select="RowSet[@Name='ROMetaInfo']/Row/T_REPORTING/LAST_UPDATE"/></xsl:with-param>
											<xsl:with-param name="date2"><xsl:value-of select="RowSet[@Name='RAMetaInfo']/Row/T_ACTIVITY/LAST_UPDATE"/></xsl:with-param>
											<xsl:with-param name="date3"><xsl:value-of select="RowSet[@Name='LIMetaInfo']/Row/T_SOURCE/LAST_UPDATE"/></xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when test="$type='RO'">
												<xsl:call-template name="RM_Dates">
													<xsl:with-param name="last_update"><xsl:value-of select="RowSet[@Name='Reporting']/Row/T_REPORTING/LAST_UPDATE"/></xsl:with-param>
													<xsl:with-param name="next_update"><xsl:value-of select="RowSet[@Name='Reporting']/Row/T_REPORTING/RM_NEXT_UPDATE"/></xsl:with-param>
													<xsl:with-param name="verified"><xsl:value-of select="RowSet[@Name='Reporting']/Row/T_REPORTING/RM_VERIFIED"/></xsl:with-param>
													<xsl:with-param name="verified_by"><xsl:value-of select="RowSet[@Name='Reporting']/Row/T_REPORTING/RM_VERIFIED_BY"/></xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:when test="$type='RA'">
												<xsl:call-template name="RM_Dates">
													<xsl:with-param name="last_update"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_ACTIVITY/LAST_UPDATE"/></xsl:with-param>
													<xsl:with-param name="next_update"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_ACTIVITY/RM_NEXT_UPDATE"/></xsl:with-param>
													<xsl:with-param name="verified"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_ACTIVITY/RM_VERIFIED"/></xsl:with-param>
													<xsl:with-param name="verified_by"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_ACTIVITY/RM_VERIFIED_BY"/></xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:when test="$type='LI'">
												<xsl:call-template name="RM_Dates">
													<xsl:with-param name="last_update"><xsl:value-of select="RowSet[@Name='Source']/Row/T_SOURCE/LAST_UPDATE"/></xsl:with-param>
													<xsl:with-param name="next_update"><xsl:value-of select="RowSet[@Name='Source']/Row/T_SOURCE/RM_NEXT_UPDATE"/></xsl:with-param>
													<xsl:with-param name="verified"><xsl:value-of select="RowSet[@Name='Source']/Row/T_SOURCE/RM_VERIFIED"/></xsl:with-param>
													<xsl:with-param name="verified_by"><xsl:value-of select="RowSet[@Name='Source']/Row/T_SOURCE/RM_VERIFIED_BY"/></xsl:with-param>
												</xsl:call-template>
											</xsl:when>

										</xsl:choose>
<!--											| Last updated: <xsl:value-of select="$last_update"/-->
										
									</xsl:otherwise>
									</xsl:choose>
									|	<a target="_blank"> 
											<xsl:attribute name="href">
												<xsl:call-template name="Feedback_URL"/>
											</xsl:attribute>
									Feedback </a>
									<!--a href="javascript:openPrintable()">Printable Page</a-->
									</span>
                  </td>

									</tr>
									<tr height="15"><td></td></tr>
									<tr>
									<td align="center">
									<span class="barfont">
                  <b><a href="http://www.eea.eu.int" target="_blank">European Environment Agency</a></b><br/>
                  Kgs. Nytorv 6, DK-1050 Copenhagen K, Denmark - Phone: +45 3336
                  7100
									</span>
									</td>
            </tr>
				</table>
	</xsl:template>
	<xsl:template name="Print">
		<xsl:if test="$printmode = 'N'">
			<img src="images/printerfriendly.png" onClick="javascript:openPrintable()" onmouseover="javasript:this.style.cursor='hand'" onmouseout="this.style.cursor='auto'" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="Help">
		<xsl:param name="id">HELP_MAIN</xsl:param>
		<xsl:param name="perm">x</xsl:param>
		<xsl:param name="green">N</xsl:param>
		<xsl:choose>
			<xsl:when test="contains($green, 'Y')">
				<map name="{$id}"><area shape="rect" tabindex="-1" coords="0,0,17,17" href="javascript:openViewHelp('{$id}')" alt="Show help for this form"></area></map>
				<img src="images/gb_help.png" usemap="#{$id}" border="0"></img>
			</xsl:when>
			<xsl:otherwise>
				<map name="{$id}"><area shape="rect" tabindex="-1" coords="0,0,17,17" href="javascript:openViewHelp('{$id}')" alt="Help for logged-in users"></area></map>
				<img src="images/bb_help.png" usemap="#{$id}" border="0"></img>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="EditHelp">
			<xsl:with-param name="id"><xsl:value-of select="$id"/></xsl:with-param>
			<xsl:with-param name="perm"><xsl:value-of select="$perm"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="HelpOverview">
		<xsl:param name="id">HELP_MAIN</xsl:param>
		<xsl:param name="perm">x</xsl:param>
		<map name="{$id}">
			<area shape="rect" tabindex="-1" coords="0,0,120,17" href="javascript:openViewHelp('{$id}')" alt="Context sensitive help"></area>
		</map>
		<img src="images/bb_helpoverview.png" usemap="#{$id}" border="0"></img>
		&#160;
		<xsl:call-template name="EditHelp">
			<xsl:with-param name="id"><xsl:value-of select="$id"/></xsl:with-param>
			<xsl:with-param name="perm"><xsl:value-of select="$perm"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="EditHelp">
		<xsl:param name="id">HELP_MAIN</xsl:param>
		<xsl:param name="perm">x</xsl:param>
		<xsl:if test="contains($perm, ',/Admin/Helptext:u,')='true'">
			<map name="{$id}_Edit">
				<area shape="rect" tabindex="-1" coords="0,0,17,17" href="javascript:openHelp('{$id}')" alt="Edit help text"></area>
			</map>
			<img src="images/bb_edithelp.png" usemap="#{$id}_Edit" border="0"></img>
		</xsl:if>
	</xsl:template>

	<xsl:template name="LeftToolbar">
		<xsl:param name="admin">false</xsl:param>
			<!-- Toolbar -->
			<p><center>
				<table border="0" cellpadding="0" cellspacing="0">
				<tr><td align="right"><font color="#ffffff"><span class="head0">Contents</span></font></td></tr>
				<tr><td align="right">
					<a href="index.html" onMouseOver="Over('img3')" onMouseOut="Out('img3')" onClick="Click('img3')">
						<img name="img3" src="images/off.gif" border="0" alt=""/>
						<img src="images/button_rod.gif" border="0" width="84" height="13" alt="ROD Home"/>
					</a>
				</td></tr>
				<tr><td align="right">
					<a href="show.jsv?id=1&amp;mode=C" onMouseOver="Over('img0')" onMouseOut="Out('img0')" onClick="Click('img0')">
						<img name="img0" src="images/off.gif" border="0" alt=""/>
						<img src="images/button_legislation.gif" border="0" width="84" height="13" alt="Legal Instruments"/>
					</a>
				</td></tr>
				<tr><td align="right">
					<a href="rorabrowse.jsv?mode=A" onMouseOver="Over('img1')" onMouseOut="Out('img1')" onClick="Click('img1')">
						<img name="img1" src="images/off.gif" border="0" alt=""/>
						<img src="images/button_obligations.gif" border="0" width="84" height="13" alt="Reporting Obligations"/>
					</a>
				</td></tr>
				<!--tr><td align="right">
					<a href="rorabrowse.jsv?mode=A" onMouseOver="Over('img4')" onMouseOut="Out('img4')" onClick="Click('img4')">
						<img name="img4" src="images/off.gif" border="0" alt=""/>
						<img src="images/button_activities.gif" border="0" width="84" height="13" alt="Reporting Activities"/>
					</a>
				</td></tr-->
				<tr><td align="right">
					<a href="deliveries.jsv" onMouseOver="Over('img8')" onMouseOut="Out('img8')" onClick="Click('img8')">
						<img name="img8" src="images/off.gif" border="0" alt=""/>
						<img src="images/button_cs.gif" border="0" width="84" height="13" alt="Deliveries"/>
					</a>
				</td></tr>
				<tr><td align="right">
					<!--a href="javascript:openViewHelp('HELP_GENERAL')" onMouseOver="Over('img5')" onMouseOut="Out('img5')" onClick="Click('img5')"-->
					<a href="javascript:Out('img5'); openViewHelp('HELP_GENERAL')" onMouseOver="Over('img5')" onMouseOut="Out('img5')">
						<img name="img5" src="images/off.gif" border="0" alt=""/>
						<img src="images/button_help.gif" border="0" width="84" height="13" alt="General Help"/>
					</a>
				</td></tr>
				<xsl:if test="contains($admin,'true')='true'">
					<tr><td>&#160;</td></tr>
					<tr><td align="right">
						<a href="logout_servlet" onMouseOver="Over('img9')" onMouseOut="Out('img9')" onClick="Click('img9')">
							<img name="img9" src="images/off.gif" border="0" alt=""/>
							<img src="images/button_logout.gif" border="0" width="84" height="13" alt="Log out"/>
						</a>
					</td></tr>
				</xsl:if>
			</table>
		</center></p>
	</xsl:template>

	<xsl:template name="RM_Dates">
		<xsl:param name="last_update"></xsl:param>
		<xsl:param name="next_update"></xsl:param>
		<xsl:param name="verified"></xsl:param>
		<xsl:param name="verified_by"></xsl:param>
			| Last updated: <xsl:value-of select="$last_update"/>
			<xsl:if test="$next_update !=''">
				| Next update: <xsl:value-of select="$next_update"/>
			</xsl:if>
			<xsl:if test="$verified !=''">
				| Verified: <xsl:value-of select="$verified"/>
			</xsl:if>
	</xsl:template>
	
	<xsl:template name="LastUpdated">
		<xsl:param name="date1"></xsl:param>
		<xsl:param name="date2"></xsl:param>
		<xsl:param name="date3"></xsl:param>
		<!--xsl:variable name="date"/-->
		<a href="analysis.jsv">
		<xsl:choose>
			<xsl:when test="$date1>=$date2 and $date1>=$date3">
				<xsl:value-of select="concat(substring($date1,7,2), '/', substring($date1,5,2), '/', substring($date1,3,2)) "/>
			</xsl:when>
			<xsl:when test="$date2>=$date1 and $date2>=$date3 ">
				<xsl:value-of select="concat(substring($date2,7,2), '/', substring($date2,5,2), '/', substring($date2,3,2)) "/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(substring($date3,7,2), '/', substring($date3,5,2), '/', substring($date3,3,2)) "/>
			</xsl:otherwise>
		</xsl:choose>
		</a>
	</xsl:template>

	<xsl:template name="RAReportingFrequency">
				<xsl:choose>
							<xsl:when test="T_ACTIVITY/TERMINATE = 'N'">
								<xsl:choose>
								<xsl:when test="T_ACTIVITY/REPORT_FREQ_MONTHS = '0'">
									One time only
								</xsl:when>
								<xsl:when test="T_ACTIVITY/REPORT_FREQ_MONTHS = '1'">
									Monthly
								</xsl:when>
								<xsl:when test="T_ACTIVITY/REPORT_FREQ_MONTHS = '12'">
									Annually
								</xsl:when>
								<xsl:when test="string-length(T_ACTIVITY/NEXT_DEADLINE) = 0">
									&#160;
								</xsl:when>
								<xsl:otherwise>
									Every <xsl:value-of select="T_ACTIVITY/REPORT_FREQ_MONTHS"/> months
								</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<font color="red">terminated</font>
							</xsl:otherwise>
						</xsl:choose>
		</xsl:template>

</xsl:stylesheet>
