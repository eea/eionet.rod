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
 * The Original Code is "EINRC-5 / ROD Project".
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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="common.xsl"/>

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
               	<td valign="bottom"><span class="barfont">General Help</span></td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	              <td valign="bottom" align="right" width="360"></td>

					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		<div style="margin-left:13">
		<div class="head1">General Help</div>
      <br/>
<table cellspacing="0" cellpadding="0" width="590" border="0">
<tr><td>


<p>This database contains information describing reporting obligations that countries have towards international organisations for air, waste, water, biodiversity and EEA priority data flows. These themes have been quality assessed.</p>

<p>The database has 3 functions:</p>
* to assist in the analysis of member countries reporting obligation's burden;<br/>
* to support member countries in planning and fulfilling reporting obligations;<br/>
* to assist in streamlining the flow of data to the EEA and other international organisations.<br/>
<br/>
<hr/>

The database covers EEA member countries as well as countries included in the following groupings:<br/>
* Central and eastern European Countries (CEEC)<br/>
* Eastern Europe, Caucasus and Central Asia (EECCA)<br/>
* Countries participating in the Desertification Information System for the Mediterranean (DISMED)<br/>
<hr/>

<p>The ROD database contains records at 2 levels. The top level is the legislative instrument (LI) level. 
Underneath this level is a summary reporting obligation (RO) level. 
A legislative instrument record is the legal basis for one or more reporting obligations.
</p>

<p>The Reporting obligation record is a summary display 
presenting information from both the RO and LI levels in the database.
</p>

<p>
Reporting obligation is taken to mean:<br/>
Any requirement to provide specified information to (or to "inform") the secretariat, coordinating body or governing body of the instrument, or their representatives, whether or not there is a prescribed format or fixed frequency, or whether or not the word "report" is used. 
</p>

<p>ROD focuses on environmental reporting for EEA member countries to international organisations. 
Coverage extends to pan European reporting.
</p>

<p>
Excluded from ROD are non-environmental reporting, requirements for proposals or the seeking of permits, 
requirements for specific actions to be taken (as opposed to reporting on actions taken), 
and administrative processes such as notifications of contact points, instances of issuance/withdrawal of 
permits (rather than reports that summarise permits issued), notifications of legal transposition and 
implementation measures.
</p>

<!--p>Reporting activity level corresponds crudely to data or information deliveries that arise 
from a reporting obligation. It is in this view that the details of reporting obligation requirements 
are shown. In particular, the focus is on dates (when deliveries are due) and the thematic issues covered. 
It is proposed to merge the RO and RA level in the near future.
</p-->

<p><a href="text.jsv?mode=R">RSS XML/RPC data extraction help</a></p>

</td></tr>
</table>

		<br/>
		</div>
		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
