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


	<xsl:variable name="src-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Reporting']/Row/T_REPORTING/FK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="ro-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Reporting']/Row/T_REPORTING/PK_RO_ID"/>
	</xsl:variable>

	<xsl:variable name="admin">
		<xsl:value-of select="/XmlData/RowSet[@Name='Reporting']/@auth"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:choose>
			<xsl:when test="count(RowSet[@Name='Reporting']/Row)>0">
				<xsl:apply-templates select="RowSet[@Name='Reporting']"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="nofound"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Reporting']/Row">
		<!-- form for delete obligation action -->
		<!-- page -->
		<table cellspacing="7pts" width="600">
			<tr><td width="82%">
				<span class="head1">Details of reporting obligation</span>
<!--
			[
				<xsl:choose>
					<xsl:when test="T_REPORTING/RECOGNIZED = 'N'">
						<span class="head0"><font color="red">Unrecognized</font></span>
					</xsl:when>
					<xsl:otherwise>
						<span class="head0">Recognized</span>
					</xsl:otherwise>
				</xsl:choose>]
-->
			</td>
			</tr>
		</table>
		<table cellspacing="7pts" width="600" border="0">
			<tr valign="top">
				<td width="22%"><span class="head0">Alias name:</span></td>
				<td width="60%">
					<xsl:choose>
						<xsl:when test="T_REPORTING/ALIAS != ''">
							<xsl:value-of select="T_REPORTING/ALIAS"/>
						</xsl:when>
						<xsl:otherwise>
							Obligation
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Document last modified:</span></td>
				<td colspan="2">
					<xsl:value-of select="T_REPORTING/LAST_UPDATE"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Related legal instrument:</span></td>
				<td colspan="2"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
						<xsl:choose>
						<xsl:when test="T_SOURCE/ALIAS != ''">
							<xsl:value-of select="T_SOURCE/ALIAS"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_SOURCE/TITLE"/>
						</xsl:otherwise>
					</xsl:choose>
					</a>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Report to:</span></td>
				<td colspan="2">
					<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					<!--xsl:value-of select="T_REPORTING/REPORT_TO"/-->
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Type:</span></td>
				<td colspan="2">
					<xsl:choose>
						<xsl:when test="T_REPORTING/LEGAL_MORAL='L'">
							Legal obligation
						</xsl:when>
						<xsl:when test="T_REPORTING/LEGAL_MORAL='M'">
							Moral obligation
						</xsl:when>
					</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Valid from:</span></td>
				<td colspan="2">
					<xsl:value-of select="T_REPORTING/VALID_FROM"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Linked environmental issues:</span></td>
				<td colspan="2">
					<xsl:apply-templates select="//RowSet[@Name='EnvIssue']"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Linked spatial attributes:</span></td>
				<td colspan="2">
					<xsl:apply-templates select="//RowSet[@Name='Spatial']"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%">
					<span class="head0">Reporting activities:</span><br/>
				</td>
				<td colspan="2">
					<xsl:apply-templates select="//RowSet[@Name='Activity']"/>
				</td>
			</tr>
			<xsl:if test="count(//RowSet[@Name='Activity']/Row) > 1">
			<tr>
				<td></td>
				<td colspan="2">
					<span class="head0"><a><xsl:attribute name="href">show.jsv?mode=ROP&amp;id=<xsl:value-of select="$ro-id"/></xsl:attribute>
					<xsl:attribute name="target">_new</xsl:attribute>
						Show all related parameters
					</a></span>
				</td>
			</tr>
			</xsl:if>
<!--
			<xsl:call-template name="RelatedInformation">
				<xsl:with-param name="type">ER</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="RelatedInformation">
				<xsl:with-param name="type">NS</xsl:with-param>
			</xsl:call-template>
-->
			
			<tr><td colspan="3"><br/><hr/></td></tr>
			<tr><td colspan="3">
			Contents in this application are maintained by the EEA.
			<a><xsl:attribute name="href">mailto:eea@eea.eu.int</xsl:attribute>Feedback.</a>
			</td></tr>
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

	<xsl:template match="//RowSet[@Name='Spatial']">
		<xsl:for-each select="Row/T_SPATIAL">
		<xsl:choose>
			<xsl:when test="position()!=count(//RowSet[@Name='Spatial']/Row/T_SPATIAL)">
				<xsl:value-of select="SPATIAL_NAME"/>, 
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="SPATIAL_NAME"/></xsl:otherwise>
		</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="//RowSet[@Name='Activity']">
		<xsl:if test="count(Row)>0">
		<table width="100%" colspan="5" border="1">
			<tr>
				<td width="60%"><span class="head0">Title</span></td>
				<td><span class="head0">Reporting frequency</span></td>
			</tr>
			<xsl:for-each select="Row/T_ACTIVITY">
				<tr>
					<td><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RA_ID"/>&amp;mode=A&amp;aid=<xsl:value-of select="$ro-id"/></xsl:attribute>
						<xsl:choose>
							<xsl:when test="TITLE != ''">
								<xsl:value-of select="TITLE"/>
							</xsl:when>
							<xsl:otherwise>
								Reporting activity
							</xsl:otherwise>
						</xsl:choose>
					</a>
					</td>
					<td>
						<xsl:choose>
							<xsl:when test="TERMINATE = 'N'">
								<!--xsl:value-of select="T_ACTIVITY/REPORT_FREQ"/>&#160;<xsl:value-of select="T_ACTIVITY/REPORT_FREQ_DETAIL"/-->
								<xsl:choose>
								<xsl:when test="REPORT_FREQ_MONTHS = '0'">
									One time only
								</xsl:when>
								<xsl:when test="REPORT_FREQ_MONTHS = '1'">
									Monthly
								</xsl:when>
								<xsl:when test="REPORT_FREQ_MONTHS = '12'">
									Annually
								</xsl:when>
								<xsl:when test="string-length(REPORT_FREQ_MONTHS) > 0">
									Every <xsl:value-of select="REPORT_FREQ_MONTHS"/> months
								</xsl:when>
								<xsl:otherwise>
									See activity
								</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<font color="red">terminated</font>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		</xsl:if>
	</xsl:template>

	<xsl:template name="RelatedInformation">
		<xsl:param name="type" select="'Not selected'"/>
		<!--<xsl:if test="count(Row)>0">-->
			<tr></tr>
			<tr>
				<td colspan="3"><span class="head0">
				<xsl:choose>
					<xsl:when test="$type='ER'">Links to EEA databases, reports and indicators:</xsl:when>
					<xsl:otherwise>Available country submissions regarding this obligation:</xsl:otherwise>
				</xsl:choose>
				</span></td>
			</tr>
			<xsl:for-each select="SubSet[@Name='RelatedInformation']/Row/T_INFORMATION[INFORMATION_TYPE=$type]">
				<tr><td colspan="3">
					<a>	<xsl:attribute name="href">
					<xsl:value-of select="URL_CR"/></xsl:attribute>
					<xsl:attribute name="target">_new</xsl:attribute>
						<xsl:value-of select="INFORMATION_NAME"/>
					</a>				
				</td></tr>
			</xsl:for-each>
		<!--</xsl:if>-->
	</xsl:template>
</xsl:stylesheet>
