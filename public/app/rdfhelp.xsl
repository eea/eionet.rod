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
            <table height="8" cellSpacing="0" cellPadding="0" background="" border="0" width="100%">
            	<tr>
               	<td valign="bottom">
									<a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a>
								</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom"><a href="index.html"><span class="barfont">ROD</span></a></td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom" width="70"><a href="text.jsv?mode=H"><span class="barfont">General Help</span></a></td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom" width="85"><span class="barfont">Data extraction</span></td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	              <td valign="bottom" align="right" width="300"></td>

					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		<div style="margin-left:13">
		<div class="head1">Data Extraction Help</div>
      <br/>
<table cellspacing="0" cellpadding="0" width="590" border="0">
<tr><td>

<p>
<h2>RDF</h2>
<b><a target="_blank" href="obligations">Reporting Obligations</a></b><br/>
Lists all Reporting Obligations with linked countries and environmental issues<br/>
<b><a target="_blank" href="instruments">Legislative Instruments</a><br/></b>
Lists all Legislative Instruments<br/>
<b><a target="_blank" href="countries">Countries</a><br/></b>
Lists all Countries<br/>
</p>
<hr/>
<p>
<h2>RSS</h2>
<b><a target="_blank" href="events.rss">Reporting Obligation deadlines</a></b><br/>
Used for Eionet calendar, lists all reporting obligations with deadline. 
Only obligations having a deadline are listed<br/>
<b><a target="_blank" href="obligations.rss">Reporting Obligations</a></b><br/>
Lists all obligations linked to specified environmental issues<br/>
<span class="smallfont">Usage: http://rod.eionet.eu.int/obligations.rss?issues=[issueId1],[issueId2],... [issueIdN]</span><br/>
The issue ID's are the primary key values of T_ISSUE table of ROD database.<br/>
<b><a target="_blank" href="instruments.rss">Legislative Instruments</a></b><br/>
Lists all legislative instruments<br/>
</p>
<hr/>

<p>
<h2>XML - RPC</h2>
<b>XML RPC Router:</b> http://rod.eionet.eu.int/rpcrouter<br/>
<b>Service name:</b> WebRODService<br/>
<br/>
Methods<br/>
<b>getActivities()</b>:returns an ARRAY of STRUCTs that contains all the obligations in 
an ARRAY, with each obligation&apos;s information put into a STRUCT<br/>
<b>getROComplete()</b>:returns an ARRAY of STRUCTs that contains all the obligations in 
an ARRAY, with each obligation&apos;s information put into a STRUCT, All the fields of the obligations are returned<br/>
<b>getROSummary()</b>:returns an ARRAY of STRUCTs that contains all the obligations in 
an ARRAY, with each obligation&apos;s information put into a STRUCT. The STRUCT contains link to obligation, title, 
last update and description<br/>
<b>getRODeadlines()</b>:returns an ARRAY of STRUCTs that contains all the obligations in 
an ARRAY, with each obligation&apos;s information put into a STRUCT. The STRUCT 
contains the reporting client name and the next deadline<br/>

</p>

</td></tr>
</table>

		<br/>
		</div>
		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
