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
	<xsl:include href="print.xsl"/>

	<xsl:variable name="ra-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_ACTIVITY/PK_RA_ID"/>
	</xsl:variable>

	<xsl:variable name="src-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="ro-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_REPORTING/PK_RO_ID"/>
	</xsl:variable>
	
	<xsl:variable name="admin">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/@auth"/>
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
	</xsl:template>

	<xsl:template match="RowSet[@Name='Activity']/Row">
		<!-- form for delete activity action -->
		<!-- navigation bar -->
		<!-- page -->
		<table cellspacing="7pts" width="600">
			<tr><td>
				<span class="head1">Details of reporting activity</span>
			</td></tr>
		</table>

		<table cellspacing="7pts" width="600" border="0">
			<tr valign="top">
				<td width="22%"><span class="head0">Title:</span></td>
				<td width="60%">
					<xsl:choose>
						<xsl:when test="T_ACTIVITY/TITLE != ''">
							<xsl:value-of select="T_ACTIVITY/TITLE"/>
						</xsl:when>
						<xsl:otherwise>
							Reporting Activity
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td align="right" nowrap="true">
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Document last modifed:</span></td>
				<td colspan="2">
					<xsl:value-of select="T_ACTIVITY/LAST_UPDATE"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Responsible for reporting (role prefix):</span></td>
				<td colspan="2">
					<xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Related obligation:</span></td>
				<td colspan="2">
						<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;aid=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=R</xsl:attribute>
						<xsl:choose>
							<xsl:when test="T_REPORTING/ALIAS != ''">
								<xsl:value-of select="T_REPORTING/ALIAS"/>
							</xsl:when>
							<xsl:otherwise>
								Obligation
							</xsl:otherwise>
						</xsl:choose>
						</a>
						from
						<xsl:choose>
							<xsl:when test="T_SOURCE/URL!=''">
								<a>
									<xsl:attribute name="href">
										<xsl:value-of select="T_SOURCE/URL"/>
									</xsl:attribute>
									<xsl:attribute name="target">
										_new
									</xsl:attribute>
								<xsl:choose>
									<xsl:when test="T_SOURCE/ALIAS != ''">
										<xsl:value-of select="T_SOURCE/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_SOURCE/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="T_SOURCE/ALIAS != ''">
										<xsl:value-of select="T_SOURCE/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_SOURCE/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>				
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Next reporting:</span></td>
				<td colspan="2">
					<xsl:choose>
						<xsl:when test="T_ACTIVITY/TERMINATE = 'N'">
						<xsl:value-of select="T_ACTIVITY/NEXT_REPORTING"/>
						</xsl:when>
						<xsl:otherwise>
							<font color="red">terminated</font>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Reporting frequency:</span></td>
				<td colspan="2">
						<xsl:choose>
						<xsl:when test="T_ACTIVITY/TERMINATE = 'N'">
							<xsl:value-of select="T_ACTIVITY/REPORT_FREQ"/>&#160;<xsl:value-of select="T_ACTIVITY/REPORT_FREQ_DETAIL"/>
						</xsl:when>
						<xsl:otherwise>
							<font color="red">terminated</font>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Related parameters:</span></td>
				<td colspan="2">
					<xsl:apply-templates select="SubSet[@Name='Parameter']"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Reporting guidelines:</span></td>
				<td colspan="2">
				<xsl:choose>
					<xsl:when test="T_ACTIVITY/REPORTING_FORMAT=''">N/A</xsl:when>
					<xsl:otherwise><xsl:value-of select="T_ACTIVITY/REPORTING_FORMAT"/></xsl:otherwise>
				</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">URL to reporting guidelines:</span></td>
				<td colspan="2">
					<xsl:choose>
						<xsl:when test="T_ACTIVITY/REPORT_FORMAT_URL != ''">
							<a>	<xsl:attribute name="href"><xsl:value-of select="T_ACTIVITY/REPORT_FORMAT_URL"/></xsl:attribute>
							<xsl:value-of select="T_ACTIVITY/FORMAT_NAME"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_ACTIVITY/FORMAT_NAME"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="T_ACTIVITY/VALID_SINCE != ''">
						(valid from <xsl:value-of select="T_ACTIVITY/VALID_SINCE"/>)
					</xsl:if>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Comments:</span></td>
				<td colspan="2">
				<xsl:value-of select="T_ACTIVITY/COMMENT"/>
				</td>
			</tr>
			<tr><td colspan="3"><br/><hr/></td></tr>
			<tr><td colspan="3">
			Contents in this application are maintained by the EEA.
			<a><xsl:attribute name="href">mailto:eea@eea.eu.int</xsl:attribute>Feedback.</a>
			</td></tr>
		</table>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Parameter']">
		<xsl:if test="count(Row)>0">
		<table width="100%" colspan="5" border="1">
			<tr>
				<td bgcolor="#646666"><span class="head0"><font color="white">Nr</font></span></td>
				<td bgcolor="#646666"><span class="head0"><font color="white">Parameter</font></span></td>
<!--
				<td bgcolor="#646666"><span class="head0"><font color="white">Unit type</font></span></td>
-->
			</tr>
			<xsl:for-each select="Row">
				<tr>
					<td>
						<xsl:number format="1."/>
					</td>
					<td>
						<xsl:value-of select="T_PARAMETER/PARAMETER_NAME"/>
					</td>
<!--
					<td>
						<xsl:choose>
							<xsl:when test="T_PARAMETER_LNK/PARAMETER_UNIT = ''">
								<xsl:value-of select="T_UNIT/UNIT_NAME"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="T_PARAMETER_LNK/PARAMETER_UNIT"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
-->
				</tr>
			</xsl:for-each>
		</table>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
