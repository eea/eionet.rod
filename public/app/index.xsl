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

	<xsl:template match="XmlData">
	<!-- context bar -->
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
               	<td valign="bottom"><span class="barfont">ROD</span></td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	              <td valign="bottom" align="right" width="360"></td>

					</tr>
 				</table>
			</td></tr>
  			<tr><td>&#160;</td></tr>
		</table>
		<!-- page -->
		<div style="margin-left:13">
		<table width="600" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td valign="full" ><span class="head0n"><xsl:call-template name="IntroductoryText"/></span></td>
			</tr>
		</table>
		<br/>
  
<!--- -->
 	<xsl:call-template name="RASearch"/>

				<table width="600">
           <tr>
              <td width="100%" colspan="4" style="border: 1 solid #006666">
                 <table border="0" width="100%" cellspacing="5" cellpadding="3">
                  <tr>
                    <td width="33%" bgcolor="#CBDCDC" valign="top">
                      <table border="0" width="100%" cellspacing="0" height="90">
                        <tr>
                          <td width="8%" height="25"><img border="0" src="images/diamlil.gif" width="8" height="9"/></td>
                          <td width="92%" height="25"><a href="deliveries.jsv">
													<span class="head0n">Show country deadlines</span></a></td>
                        </tr>
                        <tr>
                          <td width="8%" height="25"><img border="0" src="images/diamlil.gif" width="8" height="9"/></td>
                          <td width="92%" height="25"><a href="show.jsv?id=1&amp;mode=C">
													<span class="head0n">Navigate to reporting obligations via the Eur-lex legislative instrument categories</span></a></td>
                        </tr>
                        <tr>
                          <td width="8%" height="25"><img border="0" src="images/diamlil.gif" width="8" height="9"/></td>
                          <td width="92%" height="25"><a href="rorabrowse.jsv?mode=A&amp;anmode=P"><span class="head0n">Show Eionet Priority Data flows</span></a></td>
                        </tr>
                        <tr>
                          <td width="8%" height="25"><img border="0" src="images/diamlil.gif" width="8" height="9"/></td>
                          <td width="92%" height="25"><a href="analysis.jsv"><span class="head0n">Show database statistics</span></a></td>
 								 </tr>
                      </table>
                    </td>
                    <td width="33%" bgcolor="#CBDCDC" valign="top">
                      <!-- HTML from HAT will be inserted here later -->
                      <table border="0" width="100%" cellspacing="0" height="90">
								 <tr>
									 <td width="100%" height="25" colspan="2">
										<span class="head0"><b>Latest news:</b></span>
									 </td>
								 </tr>
								 <tr>
									 <td width="100%" height="25" colspan="2">
										<span class="head0n">The 2004 ROD annual update has been completed for all obligations apart from air emissions and waste.</span>
									 </td>
								 </tr>
								 <tr>
									 <td width="100%" height="25" colspan="2">
										<span class="head0n">Marine reporting obligations have now been included in ROD. 
										Obligations for Helcom and OSPAR are in the process of being validated by the "report to" organisations.</span>
									 </td>
								 </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
		</table>


		<xsl:call-template name="CommonFooter"/>

		</div>
		
	</xsl:template>

	<xsl:template match="RowSet[@Name='Client']">
		<xsl:for-each select="Row/T_CLIENT">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_CLIENT_ID"/>:<xsl:value-of select="CLIENT_NAME"/></xsl:attribute>
			<xsl:value-of select="CLIENT_NAME"/></option>
		</xsl:for-each>
	</xsl:template>


	<xsl:template match="RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/>:<xsl:value-of select="ISSUE_NAME"/></xsl:attribute>
			<xsl:value-of select="ISSUE_NAME"/></option>
		</xsl:for-each>
	</xsl:template>


</xsl:stylesheet>
