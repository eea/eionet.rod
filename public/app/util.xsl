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
	<xsl:include href="static.xsl"/>

	<xsl:param name="req" select="'default value'"/>
	<xsl:variable name="printmode" select="java:eionet.rod.RODUtil.getParameter($req, 'printmode')"/>

	<xsl:template name="SpatialTemplate">
		<xsl:param name="type" select="'Not selected'"/>
		<xsl:param name="type2" select="'Not selected'"/>
		<xsl:for-each select="RowSet[@Name='Spatial']/Row/T_SPATIAL[SPATIAL_TYPE=$type or SPATIAL_TYPE=$type2]">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_SPATIAL_ID"/>:<xsl:value-of select="SPATIAL_NAME"/>
				</xsl:attribute>
			<xsl:value-of select="SPATIAL_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="Sorter">
		<xsl:param name="order"/>
		<xsl:param name="field"/>
		<xsl:param name="color" select="'#993300'"/>
		<xsl:choose>
			<xsl:when test="contains($order, $field)">
				<xsl:attribute name="color"><xsl:value-of select="$color"/></xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="color">#000000</xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

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

		<div id="pagefoot">
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
											<xsl:with-param name="date1"><xsl:value-of select="RowSet[@Name='RAMetaInfo']/Row/T_OBLIGATION/LAST_UPDATE"/></xsl:with-param>
											<xsl:with-param name="date2"><xsl:value-of select="RowSet[@Name='LIMetaInfo']/Row/T_SOURCE/LAST_UPDATE"/></xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when test="$type='RA'">
												<xsl:call-template name="RM_Dates">
													<xsl:with-param name="last_update"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_OBLIGATION/LAST_UPDATE"/></xsl:with-param>
													<xsl:with-param name="next_update"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_OBLIGATION/RM_NEXT_UPDATE"/></xsl:with-param>
													<xsl:with-param name="verified"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_OBLIGATION/RM_VERIFIED"/></xsl:with-param>
													<xsl:with-param name="verified_by"><xsl:value-of select="RowSet[@Name='Activity']/Row/T_OBLIGATION/RM_VERIFIED_BY"/></xsl:with-param>
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
										
									</xsl:otherwise>
									</xsl:choose>
									|	<a target="_blank"> 
											<xsl:attribute name="href">
												<xsl:call-template name="Feedback_URL"/>
											</xsl:attribute>
									Feedback </a>
									<br/>
                  <b><a href="http://www.eea.eu.int" target="_blank">European Environment Agency</a></b><br/>
                  Kgs. Nytorv 6, DK-1050 Copenhagen K, Denmark - Phone: +45 3336
                  7110
				</div>
	</xsl:template>
	<xsl:template name="Print">
		<xsl:if test="$printmode='N'"><img src="images/printerfriendly.jpg" onclick="javascript:openPrintable()" border="0" onmouseover="javasript:this.style.cursor='hand'" onmouseout="this.style.cursor='auto'"/></xsl:if>
	</xsl:template>

	<xsl:template name="Help">
		<xsl:param name="id">HELP_MAIN</xsl:param>
		<xsl:param name="perm">x</xsl:param>
		<xsl:param name="green">N</xsl:param>
		<xsl:choose>
			<xsl:when test="contains($green, 'Y')">
				<map name="{$id}"><area shape="rect" tabindex="-1" coords="0,0,17,17" href="javascript:openViewHelp('{$id}')" alt="Show help"></area></map>
				<img src="images/but_questionmark.jpg" usemap="#{$id}" border="0"></img>
			</xsl:when>
			<xsl:otherwise>
				<map name="{$id}"><area shape="rect" tabindex="-1" coords="0,0,17,17" href="javascript:openViewHelp('{$id}')" alt="Help for logged-in users"></area></map>
				<img src="images/but_questionmark_blue.jpg" usemap="#{$id}" border="0"></img>
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
		<img src="images/pagehelp.jpg" usemap="#{$id}" border="0"></img>
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
			<img src="images/checkmark.jpg" usemap="#{$id}_Edit" border="0"></img>
		</xsl:if>
	</xsl:template>

	<xsl:template name="LeftToolbar">
		<xsl:param name="admin">false</xsl:param>
		<xsl:param name="username"/>
			<!-- Toolbar -->
		<div id="globalnav">
		  <h2>Contents</h2>
		  <ul>
		    <xsl:choose>
				 <xsl:when test="//HOMEPAGE != ''"><li><span>ROD</span></li></xsl:when>
				 <xsl:otherwise><li><a href="index.html" title="ROD Home">ROD</a></li></xsl:otherwise>
			 </xsl:choose>
		    <li><a href="deliveries.jsv" title="Country deadlines">Deadlines</a></li>
		    <li><a href="rorabrowse.jsv?mode=A" title="Reporting Obligations">Obligations</a></li>
		    <li><a href="text.jsv?mode=H" title="General Help">Help</a></li>
		    </ul>
		       <xsl:choose>
				<xsl:when test="contains($admin,'true')='true'">
					<h2>Logged in as<br/><xsl:value-of select="$username"/></h2>
				    	<ul>
					    <li><a href="logout_servlet" title="Log out">Logout</a></li>
					</ul>
				</xsl:when>
				<xsl:otherwise>
				    	<h2>Not logged in</h2>
					<ul>
					    <li><a href="login.html" title="Login">Login</a></li>
					</ul>
				</xsl:otherwise>
			</xsl:choose>
		  <h2>Reportnet</h2>
		  <ul>
		    <li><a href="http://cdr.eionet.eu.int/" title="Central Data Repository">CDR</a></li>
		    <li><a href="http://dd.eionet.eu.int/" title="Data Dictionary">DD</a></li>
		    <li><a href="http://cr.eionet.eu.int/" title="Content Registry">CR</a></li>
		  </ul>
		</div>
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
		<a href="analysis.jsv">
		<xsl:choose>
			<xsl:when test="$date1>=$date2">
				<xsl:value-of select="concat(substring($date1,7,2), '/', substring($date1,5,2), '/', substring($date1,3,2)) "/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(substring($date2,7,2), '/', substring($date2,5,2), '/', substring($date2,3,2)) "/>
			</xsl:otherwise>
		</xsl:choose>
		<!--xsl:choose>
			<xsl:when test="$date1>=$date2 and $date1>=$date3">
				<xsl:value-of select="concat(substring($date1,7,2), '/', substring($date1,5,2), '/', substring($date1,3,2)) "/>
			</xsl:when>
			<xsl:when test="$date2>=$date1 and $date2>=$date3 ">
				<xsl:value-of select="concat(substring($date2,7,2), '/', substring($date2,5,2), '/', substring($date2,3,2)) "/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(substring($date3,7,2), '/', substring($date3,5,2), '/', substring($date3,3,2)) "/>
			</xsl:otherwise>
		</xsl:choose-->
		</a>
	</xsl:template>

	<xsl:template name="RAReportingFrequency">
				<xsl:choose>
							<xsl:when test="T_OBLIGATION/TERMINATE = 'N'">
								<xsl:choose>
								<xsl:when test="T_OBLIGATION/REPORT_FREQ_MONTHS = '0'">
									One time only
								</xsl:when>
								<xsl:when test="T_OBLIGATION/REPORT_FREQ_MONTHS = '1'">
									Monthly
								</xsl:when>
								<xsl:when test="T_OBLIGATION/REPORT_FREQ_MONTHS = '12'">
									Annually
								</xsl:when>
								<xsl:when test="string-length(T_OBLIGATION/NEXT_DEADLINE) = 0">
									&#160;
								</xsl:when>
								<xsl:otherwise>
									Every <xsl:value-of select="T_OBLIGATION/REPORT_FREQ_MONTHS"/> months
								</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<font color="red">terminated</font>
							</xsl:otherwise>
						</xsl:choose>
		</xsl:template>
	<xsl:template name="go">
		<input type="submit" value="GO" name="GO" style="font-family: Verdana; font-size: 10pt; color: #000000; text-align: Center; background-color: #CBDCDC; font-weight: bold; border-left: 1px solid #008080; border-right: 2 solid #006666; border-top: 1px solid #008080; border-bottom: 2 solid #006666"></input>
	</xsl:template>

	<xsl:template name="RASearch">
		<form name="x1" method="get" action="rorabrowse.jsv" class="notprintable">
		<input type="hidden" name="mode" value="A"></input>
		<table  border="0" width="600" cellspacing="0" cellpadding="2"  style="border: 1px solid #008080">
				 <tr>
						<td width="36%" bgcolor="#FFFFFF" style="border-bottom: 1px solid #008080; border-right: 1px solid #C0C0C0">
							<b>Show reporting obligations</b>
						</td>
						<td width="59%" bgcolor="#FFFFFF" style="border-bottom: 1px solid #008080; border-right: 1px solid #C0C0C0">
							<b>For a country</b>
						</td>
						<td bgcolor="#FFFFFF" align="center" style="border-bottom: 1px solid #008080; border-right: 1px solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_SEARCH1</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
						</td>
				</tr>
				<tr>
					<td rowspan="4" valign="top" style="border-right: 1px solid #C0C0C0">
					</td>
					<td style="border-left: 1px solid #C0C0C0">
						<select name="country" style="color: #000000; font-size: 9pt; width:223" size="1">
								<option value="-1">Any country</option>
									<xsl:call-template name="SpatialTemplate">
										<xsl:with-param name ="type">C</xsl:with-param>
										<xsl:with-param name ="type2"></xsl:with-param>
									</xsl:call-template>
            </select>
					</td>
					<td rowspan="7" valign="center" style="border-left: 1px solid #C0C0C0"><xsl:call-template name="go"/></td>
				</tr>

				<tr>
					<td style="border-bottom: 1px solid #008080; border-left: 1px solid #C0C0C0">&#160;</td>
				</tr>
				<tr>
           <td valign="middle" align="left" style="border-bottom: 1px solid #008080" bgcolor="#FFFFFF"><b>For an issue</b></td>
				</tr>
				<tr>
					<td style="border-left: 1px solid #C0C0C0">
							<select width="280" name="env_issue" style="font-size: 9pt; color: #000000; width:223" height="20">
									<option value="-1">All issues</option>
									<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
							</select>
						</td>
				</tr>

				<tr>
					<td>&#160;</td><td style="border-bottom: 1px solid #008080; border-left: 1px solid #C0C0C0">&#160;</td>
				</tr>
				<tr>
           <td>&#160;</td><td valign="middle" align="left" style="border-bottom: 1px solid #008080; border-left: 1px solid #C0C0C0" bgcolor="#FFFFFF"><b>For an organisation</b></td>
				</tr>
				<tr><td>&#160;</td>
					<td style="border-left: 1px solid #C0C0C0">
								<select name="client" style="color: #000000; font-size: 9pt; width:350" size="1" width="280">
										<option value="-1">Any organisation</option>
										<xsl:apply-templates select="RowSet[@Name='Client']"/>
								</select>
						</td>
				</tr>

		</table>
		</form>
	</xsl:template>
	
	<!-- Replaces line breaks with <br/> tags -->
	<xsl:template name="break">
   	<xsl:param name="text" select="."/>
	   <xsl:choose>
	   	<xsl:when test="contains($text, '&#xa;')">
		      <xsl:value-of select="substring-before($text, '&#xa;')"/>
   		   <br/>
	      	<xsl:call-template name="break">
	   	       <xsl:with-param name="text" select="substring-after($text, '&#xa;')"/>
		      </xsl:call-template>
	   	</xsl:when>
	   	<xsl:otherwise>
				<xsl:value-of select="$text"/>
	   	</xsl:otherwise>
   	</xsl:choose>
	</xsl:template>

	<xsl:template name="short">
	   	<xsl:param name="text" select="."/>
			<xsl:param name="length" select="."/>
				<xsl:choose>						
					<xsl:when test="string-length($text) &gt; $length">
						<xsl:value-of select="substring($text,0,$length)"/>...
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$text"/>
					</xsl:otherwise>
				</xsl:choose>	
	</xsl:template>
<!-- EK 050210 template for creating sortable column headers.
	The method  is using createURL template from the main xsl, because the urls are page specific -->
	<xsl:template name="createSortable">
		<xsl:param name="title"/>
		<xsl:param name="text"/>
		<xsl:param name="sorted"/>
		<xsl:param name="class"/>
		<xsl:param name="width"/>
		<xsl:param name="cur_sorted" select="'T_OBLIGATION.TITLE'"></xsl:param>

		<th scope="col">
			<xsl:if test="string-length($class) &gt; 0"><xsl:attribute name="class"><xsl:value-of select="$class"/></xsl:attribute></xsl:if>
			<xsl:attribute name="width"><xsl:value-of select="$width"/></xsl:attribute>
			<xsl:choose>
			<!-- the column is sorted A .. Z -->
				<xsl:when test="contains($cur_sorted, $sorted) and contains($cur_sorted, ' DESC')=false">
					<xsl:attribute name="title"><xsl:value-of select="$title"/> - Sorted A..Z - Click to reverse</xsl:attribute>
					<a>
						<xsl:attribute name="href">
							<xsl:call-template name="createURL"><xsl:with-param name="sorted" select="concat($sorted, ' DESC') "/></xsl:call-template>
						</xsl:attribute>
						<xsl:value-of select="$text"/>
						<img src="images/rodsortup.gif" width="12" height="12" alt=""/>				
					</a>
				</xsl:when>
			<!-- the column is sorted Z...A -->
				<xsl:when test="contains($cur_sorted, $sorted) and contains($cur_sorted, ' DESC')">
					<xsl:attribute name="title"><xsl:value-of select="$title"/> - Sorted Z..A - Click to reverse</xsl:attribute>
					<a>
						<xsl:attribute name="href">
							<xsl:call-template name="createURL"><xsl:with-param name="sorted" select="$sorted"/></xsl:call-template>
						</xsl:attribute>
						<xsl:value-of select="$text"/>
						<img src="images/rodsortdown.gif" width="12" height="12" alt=""/>
					</a>
				</xsl:when>
			<!-- sortable, but not sorted -->
				<xsl:otherwise>
					<xsl:attribute name="title"><xsl:value-of select="$title"/> - Sortable - Click to sort A..Z</xsl:attribute>
					<a>
						<xsl:attribute name="href">
							<xsl:call-template name="createURL"><xsl:with-param name="sorted" select="$sorted"/></xsl:call-template>
						</xsl:attribute>
						<xsl:value-of select="$text"/>
						<img src="images/sortnot.gif" width="12" height="12" alt=""/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
		 </th>
	</xsl:template>

</xsl:stylesheet>
