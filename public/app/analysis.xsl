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
	<xsl:include href="util.xsl"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet/@auth"/>
	</xsl:variable>

	<xsl:template match="XmlData">

      <table cellspacing="0" cellpadding="0" width="600" border="0">
			<tr>
         	<td align="bottom" width="20" background="images/bar_filled.jpg" height="25">&#160;</td>
          	<td width="600" background="images/bar_filled.jpg" height="25">
            <table height="8" cellSpacing="0" cellPadding="0" background="" border="0">
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
		<div style="margin-left:13">
		<table width="97%" border="0">  
			<tr>
				<td width="70%" align="left"><div class="head1">Database Content Statistics</div></td>
				<td width="30%" align="right"><xsl:call-template name="Print"/></td>
			</tr>
		</table>
		<br/>
		<xsl:value-of select="RowSet[@Name='RAAnalysis']/Row/T_ACTIVITY/TOTAL_RA"/> Reporting Activity records (last update: <xsl:value-of select="RowSet[@Name='RAAnalysis']/Row/T_ACTIVITY/RA_UPDATE"/>)<br/>
		<xsl:value-of select="RowSet[@Name='ROAnalysis']/Row/T_REPORTING/TOTAL_RO"/> Reporting Obligation records (last update: <xsl:value-of select="RowSet[@Name='ROAnalysis']/Row/T_REPORTING/RO_UPDATE"/>)<br/>
		<xsl:value-of select="RowSet[@Name='LIAnalysis']/Row/T_SOURCE/TOTAL_LI"/> Legal Instrument records (last update: <xsl:value-of select="RowSet[@Name='LIAnalysis']/Row/T_SOURCE/LI_UPDATE"/>)
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
