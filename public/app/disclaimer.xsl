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

<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Disclaimer</div>
 <div class="breadcrumbtail">&#160;</div>
</div>

            <table height="8" cellspacing="0" cellpadding="0" background="" border="0">
            	<tr>
               	<td valign="bottom">
									<a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a>
								</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom"><a href="index.html"><span class="barfont">ROD</span></a></td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom"><span class="barfont">Disclaimer</span></td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	              <td valign="bottom" align="right" width="360"></td>

					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		<div id="workarea">
		<div class="head1">Disclaimer</div>
      <br/>
		<xsl:value-of select="//HLP_AREA[AREA_ID='Disclaimer']/HTML" disable-output-escaping="yes"/>
<!--table cellspacing="0" cellpadding="0" width="590" border="0">
<tr><td>

<h2>Summary</h2>

<p>The Reporting Obligations database is under development. The database
exists, in part, because there is some uncertainty about the details of
some reporting obligations that are themselves often under development
and changing. Each record in the database has been quality assessed by
the EEA and partners.</p>
<p>The footer of each record displays quality information relating to
each record.</p>
<ul>
<li>Last updated: The last time when a record was updated</li>
<li>Next update:  The date when the next update is due</li>
<li>Verified: The date on which the record was verified or quality
assessed</li>
</ul>

<p>Records that are verified are accepted as correct until the regular
update cycle date is reached. If this information is not displayed in the
footer, then the record has not been verified or quality assessed. </p>

<h2>Limitations</h2>

<p>The database contains reporting obligations for Eionet dataflows as
well as international reporting obligations for air, waste and water
themes for EEA member countries. These themes correspond to around half
of all environmental reporting obligations for member countries. Other
themes are in the process of being added to the ROD.</p>

<p>Current known issues with the database contents that are awaiting resolution:</p>

<p>Marine conventions (OSPAR, HELCOM, Barcelona, Wadden Sea) are missing some
reporting obligations.</p>

<p>The old categories of reporting obligations "legal" and "moral" have not been
fully replaced with the new categories "compulsory", "expected" and "voluntary".</p>

<p>Material in the ROD is not necessarily comprehensive, complete,
accurate or up to date and contains links to external sites over which
the EEA has no control. The information in the database is supplied as
is. You use it at your own risk.</p>
<p>Our goal is to keep the ROD timely and accurate. If errors are brought
to our attention, we will correct them.</p>
<hr/>

<h2>ROD content management processes</h2>

<h3>Quality assessment</h3>

<p>Quality Assessment is a one-off process to check the current
contents of ROD thematically. It was carried out in April of 2003. It
confirmed that the contents (legislation, reporting obligations,
reporting activities, links to guidelines and reporting organisations)
are correct.</p>

<h3>Updating</h3>

<p>Updating is the regular process of reviewing the contents of the
database to ensure that it is kept up-to-date. Updating is the review
of legislation, reporting obligations, and reporting activities in
ROD to check for information that is out-of-date or incorrect. This
information is updated with the correct contents. Expert judgement is
used to evaluate what the update cycle should be, i.e. when the contents
should next be updated and this is noted for each record. It is also
the process of identifying new reporting obligations that have been
introduced since the database was last updated and need to be included
in the database. These legislative references and reporting obligations
are added to the database as part of updating.</p>

<h3>Verification</h3>

<p>Verification is a structured quality assurance process with an
integrated feedback mechanism carried out by the EEA to quality control
new coverage and new depth added to the database. Thematic experts carry
out verification. Contents that are verified are accepted as correct
until the regular update cycle date is reached.</p>

<h3>Validation</h3>

<p>Validation is a quality assurance carried out by the <i>owners</i> of
information in the database in particular for non EEA / EIONET reporting
obligations. Legislative authoritative bodies carry out validation for
their own reporting obligations.</p>

<h3>New coverage</h3>

<p>ROD currently covers the air, water and waste themes. Adding an
increased level of detail to one of these subjects or adding coverage
for other themes is adding new coverage for ROD. New information added
to ROD is subsequently verified by thematic experts and enters an update
cycle.</p>

</td></tr>
</table-->

		<br/>
		</div>
		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
