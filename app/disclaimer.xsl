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
	<xsl:include href="util.xsl"/>

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
               	<td valign="bottom"><span class="barfont">Disclaimer</span></td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	              <td valign="bottom" align="right" width="360"></td>

					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		<div style="margin-left:13">
		<div class="head1">Disclaimer</div>
      <br/>
      <table cellspacing="0" cellpadding="0" width="590" border="0">
			<tr><td>
				<p>The European Environment Agency accepts no responsibility or liability whatsoever with regard to the information on this site and the information does not necessarily reflect the official opinion of EEA or other European Communities bodies and institutions.</p>
				<p>The material on this site is not necessarily comprehensive, complete, accurate or up to date and may contain links to external sites over which the EEA services have no control and for which the EEA assumes no responsibility. Neither the European Environment Agency nor any person or company acting on behalf of the Agency is responsible for the contents of this website and the use that may be made of it.</p> 
				<p>Our goal is to keep this information timely and accurate. If errors are brought to our attention, we will try to correct them.</p>
		</td></tr>
		</table>
		<br/>
		</div>
		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

</xsl:stylesheet>
