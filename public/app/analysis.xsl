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
	<xsl:include href="common.xsl"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet/@auth"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
			<xsl:if test="$printmode='N'">
				<table cellspacing="0" cellpadding="0" width="600" border="0">
				<tr>
						<td align="bottom" width="20" background="images/bar_filled.jpg" height="25">&#160;</td>
							<td width="600" background="images/bar_filled.jpg" height="25">
							<table height="8" cellspacing="0" cellpadding="0" background="" border="0">
								<tr>
									<td valign="bottom">
										<a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a>
									</td>
									<td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
									<td valign="bottom"><a href="index.html"><span class="barfont">ROD</span></a></td>
									<td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
									<td valign="bottom"><span class="barfont">Analysis</span></td>
									<td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
									<td valign="bottom" align="right" width="360"></td>

						</tr>
					</table>
				</td></tr>
				<tr><td>&#160;</td></tr>
			</table>
		</xsl:if>
		<div style="margin-left:13">
		<table width="97%" border="0">  
			<tr>
				<td width="70%" align="left"><div class="head1">Database Content Statistics</div></td>
				<td width="30%" align="right"><xsl:call-template name="Print"/></td>
			</tr>
		</table>
		<br/>
		<xsl:value-of select="RowSet[@Name='RAAnalysis']/Row/T_OBLIGATION/TOTAL_RA"/> Reporting Obligation records (last update: <xsl:value-of select="RowSet[@Name='RAAnalysis']/Row/T_OBLIGATION/RA_UPDATE"/>)<br/>
		<xsl:value-of select="RowSet[@Name='LIAnalysis']/Row/T_SOURCE/TOTAL_LI"/> Legislative Instrument records (last update: <xsl:value-of select="RowSet[@Name='LIAnalysis']/Row/T_SOURCE/LI_UPDATE"/>)<br/>
		<br/>
		
		<table cellpadding="5" cellspacing="0" width="584" style="border: #008080 1px solid">  
			<tr>
				<td width="70%" style="border-right: #c0c0c0 1px solid">Number of reporting obligations used for the EEA Core set of indicators</td>
				<td width="7%" align="right" style="border-right: #c0c0c0 1px solid"><xsl:value-of select="RowSet[@Name='RACoreSet']/Row/T_OBLIGATION/TOTAL_RA"/></td>
				<td width="15%" align="right">
					<xsl:if test="RowSet[@Name='RACoreSet']/Row/T_OBLIGATION/TOTAL_RA > 0">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=C">Show list</a>
					</xsl:if>
				</td>
				<td align="right" style="border-left: #c0c0c0 1px solid">
					<xsl:call-template name="Help">
						<xsl:with-param name="id">HELP_ANALYSIS_EEACORE</xsl:with-param>
						<xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param>
						<xsl:with-param name="green">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr bgcolor="#CBDCDC">
				<td style="border-right: #c0c0c0 1px solid">Number of reporting obligations used for the EIONET Priority Data flows</td>
				<td align="right" style="border-right: #c0c0c0 1px solid"><xsl:value-of select="RowSet[@Name='RAEEAPriority']/Row/T_OBLIGATION/TOTAL_RA"/></td>
				<td align="right">
					<xsl:if test="RowSet[@Name='RAEEAPriority']/Row/T_OBLIGATION/TOTAL_RA > 0">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=P">Show list</a>
					</xsl:if>
				</td>
				<td align="right" style="border-left: #c0c0c0 1px solid">
					<xsl:call-template name="Help">
						<xsl:with-param name="id">HELP_ANALYSIS_EIONETPRIORITY</xsl:with-param>
						<xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param>
						<xsl:with-param name="green">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td style="border-right: #c0c0c0 1px solid">Number of reporting obligations where the delivery process or content overlaps with another reporting obligation</td>
				<td align="right" style="border-right: #c0c0c0 1px solid"><xsl:value-of select="RowSet[@Name='RAOverLap']/Row/T_OBLIGATION/TOTAL_RA"/></td>
				<td align="right">
					<xsl:if test="RowSet[@Name='RAOverLap']/Row/T_OBLIGATION/TOTAL_RA > 0">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=O">Show list</a>
					</xsl:if>
				</td>
				<td align="right" style="border-left: #c0c0c0 1px solid">
					<xsl:call-template name="Help">
						<xsl:with-param name="id">HELP_ANALYSIS_OVERLAPPING</xsl:with-param>
						<xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param>
						<xsl:with-param name="green">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr bgcolor="#CBDCDC">
				<td style="border-right: #c0c0c0 1px solid">Number of reporting obligations flagged<br/>&#160;</td>
				<td align="right" style="border-right: #c0c0c0 1px solid"><xsl:value-of select="RowSet[@Name='RAFlagged']/Row/T_OBLIGATION/TOTAL_RA"/></td>
				<td align="right">
					<xsl:if test="RowSet[@Name='RAFlagged']/Row/T_OBLIGATION/TOTAL_RA > 0">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=F">Show list</a>
					</xsl:if>
				</td>
				<td align="right" style="border-left: #c0c0c0 1px solid">
					<xsl:call-template name="Help">
						<xsl:with-param name="id">HELP_ANALYSIS_FLAGGED</xsl:with-param>
						<xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param>
						<xsl:with-param name="green">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
