<?xml version="1.0" encoding="UTF-8"?>

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
	<xsl:variable name="pagetitle">
		Edit/Create Reporting Obligation for <xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/TITLE"/>
	</xsl:variable>
	<xsl:include href="editor.xsl"/>

	<!--xsl:variable name="ro-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_REPORTING/PK_RO_ID"/>
	</xsl:variable-->

	<xsl:variable name="src-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="type-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/FK_TYPE_ID"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Reporting obligation</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
        <!-- page -->
        <div id="workarea">

		<table cellspacing="7pts" width="621" border="0">
		<tr>
			<td width="459">
				<span class="head1">
					Edit/Create Reporting Obligation for 
					<xsl:choose>
						<xsl:when test="//RowSet[@Name='Source']/Row/T_SOURCE/ALIAS != ''">
							<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/ALIAS"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/TITLE"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="//RowSet[@Name='Source']/Row/T_SOURCE/SOURCE_CODE != ''">
						&#160;(<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/SOURCE_CODE"/>)&#160;
					</xsl:if>
				</span>
			</td>
			<td width="152" align="right">
				<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_RA</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
			</td>
		</tr>
		</table>
		<xsl:apply-templates select="RowSet[@Name='Activity']/Row"/>
</div>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Activity']/Row">
		<form name="f" method="POST" action="activity.jsv" acceptcharset="UTF-8">
		<input type="hidden" name="silent" value="0"/> <!--silent save -->
		<input type="hidden" name="dom-update-mode">
			<xsl:attribute name="value">
				<xsl:choose>
					<xsl:when test="../../RowSet[@skeleton='1']">A</xsl:when>
					<xsl:otherwise>U</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/PK_RA_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/PK_RA_ID"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/FK_SOURCE_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$src-id"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/VERSION/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/VERSION"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/TERMINATE/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/TERMINATE"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/NEXT_DEADLINE2/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/NEXT_DEADLINE2"/></xsl:attribute>
		</input>

		<table border="0" width="615" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" style="border: 1px solid #006666">
					<table>
						<tr bgcolor="#FFFFFF">
							<td width="20%" valign="top" align="left" style="border-right: 1px solid #C0C0C0">
								<span class="head0">Title</span> 
							 </td>
							 <td width="7%" valign="top" align="center" style="border-right: 1px solid #C0C0C0">
									<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_TITLE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							 </td>
							 <td width="73%" valign="top" align="left">
									<input type="text" size="68" onchange="changed()">
										<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/TITLE/@XPath"/></xsl:attribute>
										<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/TITLE"/></xsl:attribute>
									</input>&#160;
							 </td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Description</span></td>
							<td style="border-right: 1px solid #C0C0C0"><xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_DESCRIPTION</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
							<td>
								<textarea rows="5" cols="55" wrap="soft" width="440" style="width:440" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/DESCRIPTION/@XPath"/></xsl:attribute>
									<xsl:value-of select="T_OBLIGATION/DESCRIPTION"/>
								</textarea>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Baseline reporting date</span></td>
							<td style="border-right: 1px solid #C0C0C0"><xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_VALIDFROM</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
							<td>
								<input type="text" size="30" onchange='changedReporting(document.forms["f"].elements["{T_OBLIGATION/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/VALID_TO/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/TERMINATE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_DEADLINE2/@XPath}"])'>
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/FIRST_REPORTING/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/FIRST_REPORTING"/></xsl:attribute>
								</input><span class="smallfont">&#160;(dd/mm/yyyy)</span>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Valid to</span></td>
							<td style="border-right: 1px solid #C0C0C0"><xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_VALIDTO</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
							<td>
								<input type="text" size="30" onchange='changedReporting(document.forms["f"].elements["{T_OBLIGATION/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/VALID_TO/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/TERMINATE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_DEADLINE2/@XPath}"])'>
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/VALID_TO/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/VALID_TO"/></xsl:attribute>
								</input><span class="smallfont">&#160;(dd/mm/yyyy)</span>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Reporting frequency in months</span></td>
							<td style="border-right: 1px solid #C0C0C0"><xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_REPORTINGFREQUENCYINMONTHS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
							<td>
								<input type="text" size="30" onchange='changedReporting(document.forms["f"].elements["{T_OBLIGATION/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/VALID_TO/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/TERMINATE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_DEADLINE2/@XPath}"])'>
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/REPORT_FREQ_MONTHS/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/REPORT_FREQ_MONTHS"/></xsl:attribute>
								</input><br/><span class="smallfont">For one-time-only reporting, enter 0 and choose a reasonable Valid To</span>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Next due date</span></td>
							<td style="border-right: 1px solid #C0C0C0"><xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_NEXTDUEDATE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
							<td>
								<input type="text" size="30" onchange="changed()" disabled="disabled">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/NEXT_DEADLINE/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/NEXT_DEADLINE"/></xsl:attribute>
								</input><span class="smallfont">&#160;(calculated automatically)</span>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td align="left" colspan="3" bgcolor="#FFCA95" style="border-top: 1px solid #000000; border-bottom: 1px solid #000000">
                <i><b>IMPORTANT!</b>&#160;Use the following field <b> only </b> if reporting dates (above fields)
                      cannot be given in numerical date format but have to be given in text format (for example, 'ASAP')</i>
								</td>
            </tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Reporting date</span></td>
							<td style="border-right: 1px solid #C0C0C0"><xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_REPORTINGDATETEXTFORMAT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
							<td>
								<input type="text" size="68" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/NEXT_REPORTING/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/NEXT_REPORTING"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Date comments</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_DATECOMMENTS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/DATE_COMMENTS/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/DATE_COMMENTS"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Report to</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_REPORTTO</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<select  width="410" style="width:410" maxlength="255" onchange="changed()">
									<xsl:variable name="selClient">
											<xsl:value-of select="T_CLIENT_LNK/FK_CLIENT_ID"/>
									</xsl:variable>
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/FK_CLIENT_ID/@XPath"/></xsl:attribute>
										<option value='0'></option>
										<xsl:for-each select="//RowSet[@Name='Client']/Row">
											<option>
												<xsl:attribute name="value">
													<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>
												</xsl:attribute>
												<xsl:if test="T_CLIENT/PK_CLIENT_ID = $selClient">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
												<!--xsl:choose>
													<xsl:when test="T_CLIENT/CLIENT_ACRONYM!=''">
														<xsl:value-of select="T_CLIENT/CLIENT_ACRONYM"/>&#160;-&#160;<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
													</xsl:otherwise>
												</xsl:choose-->
											</option>
										</xsl:for-each>
								</select>
								<xsl:if test="contains($permissions, ',/Client:i,')='true'">
									<map name="newClientMap">
										<area alt="Add a new client to client list" shape="rect" coords="0,0,23,15" href="javascript:addCl()"></area>
									</map>
									<img border="0" src="images/but_new_blue.jpg" usemap="#newClientMap"></img>
								</xsl:if>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF" >
							<td colspan="2" style="border-right: 1px solid #C0C0C0"><span class="head0">Other clients using this reporting</span></td>
							<td>
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_OTHERCLIENTS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
						</tr>
						<tr valign="top">
							<td colspan="3">
								<table width="100%">
									<tr>
										<td width="40%">
											<xsl:apply-templates select="//RowSet[@Name='Client']"/>
										</td>
										<td width="20%">
											<table cellspacing="5">
												<tr><td width="100" align="center">
													<input type="button" width="80" style="width: 80px; background-image: url('images/bgr_form_buttons.jpg')" 
														onclick="addClientValues(clientLst, lnkClients, null)" 
														value="&#160;&#160;-&gt;&#160;&#160;"/>
												</td></tr>
												<tr><td width="100" align="center">
													<input type="button" width="80" style="width: 80px; background-image: url('images/bgr_form_buttons.jpg')"
														onclick="delValues(lnkClients)" 
														value="&#160;&#160;&lt;-&#160;&#160;"/>
												</td></tr>
											</table>
										</td>
										<td width="40%">
											<xsl:apply-templates select="SubSet[@Name='CCClients']"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Name of reporting guidelines</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_NAMEOFREPORTINGGUIDELINES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/FORMAT_NAME/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">URL to reporting guidelines</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_URLTOREPORTINGGUIDELINES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="chkUrl(this); changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Format valid since</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_FORMATVALIDSINCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="30" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/VALID_SINCE/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/VALID_SINCE"/></xsl:attribute>
								</input><span class="smallfont">&#160;(dd/mm/yyyy)</span>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Reporting guidelines -Extra info.</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_REPORTINGGUIDELINES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<textarea rows="5" cols="55" wrap="soft" width="440" style="width:440" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/REPORTING_FORMAT/@XPath"/></xsl:attribute>
									<xsl:value-of select="T_OBLIGATION/REPORTING_FORMAT"/>
								</textarea>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Name of repository</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_LOCATIONINFO</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/LOCATION_INFO/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/LOCATION_INFO"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">URL to repository</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_LOCATIONPTR</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="chkUrl(this); changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/LOCATION_PTR/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/LOCATION_PTR"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td colspan="2" style="border-right: 1px solid #C0C0C0"><span class="head0">Countries reporting formally</span></td>
							<td>
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_SPATIALCOVERAGE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
								<xsl:apply-templates select="//RowSet[@Name='SpatialType']"/>									
							</td>
						</tr>
						<tr valign="top">
							<td colspan="3">
								<table width="100%">
									<tr valign="top">
										<td width="40%" align="left">
											<xsl:apply-templates select="//RowSet[@Name='SPATIAL']"/>
										</td>
										<td width="20%" valign="center">
											<table cellspacing="5" width="100%">
												<tr><td align="center">
													<input type="button" width="80" style="width:80; background-image: url('images/bgr_form_buttons.jpg')" onclick="addValues(spatialLst, lnkSpatial, null,clist,lnkVoluntaryCountries)" value="&#160;&#160;-&gt;&#160;&#160;"/>
												</td></tr>
												<tr><td align="center">
													<input type="button" width="80" style="width:80; background-image: url('images/bgr_form_buttons.jpg')" 
														onclick="delValues(lnkSpatial)" 
														value="&#160;&#160;&lt;-&#160;&#160;"/>
												</td></tr>
											</table>
										</td>
										<td width="40%">
												<xsl:apply-templates select="SubSet[@Name='LnkSpatial']"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td colspan="2" style="border-right: 1px solid #C0C0C0"><span class="head0">Countries reporting voluntarily</span></td>
							<td>
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_VOLUNTARYCOUNTRIES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
						</tr>
						<tr valign="top">
							<td colspan="3">
								<table width="100%">
									<tr valign="top">
										<td width="40%" align="left">
												<xsl:apply-templates select="//RowSet[@Name='CountryList']"/>
										</td>
										<td width="20%" valign="center">
											<table cellspacing="5" width="100%">
												<tr><td align="center">
													<input type="button" width="80" style="width:80; background-image: url('images/bgr_form_buttons.jpg')" 
														onclick="addFullValues(countryLst, lnkVoluntaryCountries,clist,lnkSpatial)" 
														value="&#160;&#160;-&gt;&#160;&#160;"/>
												</td></tr>
												<tr><td align="center">
													<input type="button" width="80" style="width:80; background-image: url('images/bgr_form_buttons.jpg')" 
														onclick="delValues(lnkVoluntaryCountries)" 
														value="&#160;&#160;&lt;-&#160;&#160;"/>
												</td></tr>
											</table>
										</td>
										<td width="40%">
											<xsl:apply-templates select="SubSet[@Name='VoluntaryCountries']"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">National reporting coordinators</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_REPORTINGCOORDINATOR</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<table width="100%">
									<tr>
										<td style="border-bottom:1px solid #C0C0C0"><span class="head0">Role</span></td>
										<td style="border-bottom:1px solid #C0C0C0">
											<input type="text" size="15" onchange="changed()">
												<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/COORDINATOR_ROLE/@XPath"/></xsl:attribute>
												<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/COORDINATOR_ROLE"/></xsl:attribute>
											</input>
											<input type="hidden">
												<xsl:attribute name="name">
													<xsl:value-of select="T_OBLIGATION/COORDINATOR_ROLE_SUF/@XPath"/>
												</xsl:attribute>
												<xsl:attribute name="value">
														<xsl:value-of select='T_OBLIGATION/COORDINATOR_ROLE_SUF'/>
												</xsl:attribute>
											</input>
										</td>
										<td style="border-bottom:1px solid #C0C0C0">
											<input type="checkbox">
												<xsl:attribute name="onclick">chkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/COORDINATOR_ROLE_SUF/@XPath'/>"], true)</xsl:attribute>
												<xsl:if test="T_OBLIGATION/COORDINATOR_ROLE_SUF='0'">
														<xsl:attribute name="checked"/>
												</xsl:if>
											</input>
										</td>
										<td style="border-bottom:1px solid #C0C0C0">
											<span class="head0">do not append country suffix</span>
										</td>
									</tr>
									<tr>
										<td style="border-bottom:1px solid #C0C0C0" colspan="4" align="center">
											<i>- OR -</i>
										</td>
									</tr>
									<tr valign="top">
										<td><span class="head0">Name</span></td>
										<td>
											<input type="text" size="15" onchange="changed()">
												<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/COORDINATOR/@XPath"/></xsl:attribute>
												<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/COORDINATOR"/></xsl:attribute>
											</input>
										</td>
										<td>
											<span class="head0">URL</span>
										</td>
										<td>
											<input type="text" size="33" onchange="chkUrl(this); changed()" >
												<xsl:attribute name="name">
													<xsl:value-of select="T_OBLIGATION/COORDINATOR_URL/@XPath"/>
												</xsl:attribute>
												<xsl:attribute name="value">
													<xsl:value-of select='T_OBLIGATION/COORDINATOR_URL'/>
												</xsl:attribute>
											</input>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">National reporting contacts</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_RESPONSIBLEFORREPORTING</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<table width="100%">
									<tr>
										<td style="border-bottom:1px solid #C0C0C0"><span class="head0">Role</span></td>
										<td style="border-bottom:1px solid #C0C0C0">
											<input type="text" size="15" onchange="changed()">
												<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/RESPONSIBLE_ROLE/@XPath"/></xsl:attribute>
												<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/RESPONSIBLE_ROLE"/></xsl:attribute>
											</input>
											<input type="hidden">
												<xsl:attribute name="name">
													<xsl:value-of select="T_OBLIGATION/RESPONSIBLE_ROLE_SUF/@XPath"/>
												</xsl:attribute>
												<xsl:attribute name="value">
														<xsl:value-of select='T_OBLIGATION/RESPONSIBLE_ROLE_SUF'/>
												</xsl:attribute>
											</input>
										</td>
										<td style="border-bottom:1px solid #C0C0C0">
											<input type="checkbox">
												<xsl:attribute name="onclick">chkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/RESPONSIBLE_ROLE_SUF/@XPath'/>"], true)</xsl:attribute>
												<xsl:if test="T_OBLIGATION/RESPONSIBLE_ROLE_SUF='0'">
														<xsl:attribute name="checked"/>
												</xsl:if>
											</input>
										</td>
										<td style="border-bottom:1px solid #C0C0C0">
											<span class="head0">do not append country suffix</span>
										</td>
									</tr>
									<tr>
										<td style="border-bottom:1px solid #C0C0C0" colspan="4" align="center">
											<i>- OR -</i>
										</td>
									</tr>
									<tr valign="top">
										<td><span class="head0">Name</span></td>
										<td>
											<input type="text" size="15" onchange="changed()">
												<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT/@XPath"/></xsl:attribute>
												<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT"/></xsl:attribute>
											</input>
										</td>
										<td>
											<span class="head0">URL</span>
										</td>
										<td>
											<input type="text" size="30" onchange="chkUrl(this); changed()">
												<xsl:attribute name="name">
													<xsl:value-of select="T_OBLIGATION/NATIONAL_CONTACT_URL/@XPath"/>
												</xsl:attribute>
												<xsl:attribute name="value">
														<xsl:value-of select='T_OBLIGATION/NATIONAL_CONTACT_URL'/>
												</xsl:attribute>
											</input>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Obligation type</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_OBLIGATIONTYPE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<select onchange="changed()"><xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/LEGAL_MORAL/@XPath"/></xsl:attribute>
									<xsl:for-each select="//RowSet[@Name='LegalMoral']/T_LOOKUP">
										<xsl:choose>
											<xsl:when test="C_VALUE=//RowSet[@Name='Activity']/Row/T_OBLIGATION/LEGAL_MORAL">
												<option selected="selected"><xsl:attribute name="value"><xsl:value-of select="C_VALUE"/></xsl:attribute>
												<xsl:value-of select="C_TERM"/></option>
											</xsl:when>
											<xsl:otherwise>
												<option><xsl:attribute name="value"><xsl:value-of select="C_VALUE"/></xsl:attribute>
												<xsl:value-of select="C_TERM"/></option>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</select>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Type of info reported</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_INFOTYPE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<select name="info_lnk" multiple="multiple" width="180" style="width:180">
									<xsl:attribute name="onchange">
										var txt =document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkInfo']/Row/T_INFO_LNK/FK_INFO_IDS"];
										changeMulti(txt, this);
									</xsl:attribute>
									<xsl:for-each select="//RowSet[@Name='InfoType']/T_LOOKUP">
											<option>
											<xsl:attribute name="value"><xsl:value-of select="C_VALUE"/></xsl:attribute>
											<xsl:value-of select="C_TERM"/>
										</option>
									</xsl:for-each>
								</select>
								<xsl:apply-templates select="SubSet[@Name='LnkInfo']"/>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td colspan="2" style="border-right: 1px solid #C0C0C0"><span class="head0">Environmental issues</span></td>
							<td>
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_ENVIRONMENTALISSUES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
						</tr>
						<tr valign="top">
							<td colspan="3">
								<table width="100%">
									<tr>
										<td width="40%">
											<xsl:apply-templates select="//RowSet[@Name='ISSUE']"/>
										</td>
										<td width="20%">
											<table cellspacing="5">
												<tr><td align="center">
													<input type="button" width="80" style="width:80; background-image: url('images/bgr_form_buttons.jpg')" 
														onclick="addValues(issueLst, lnkIssue, null)" 
														value="&#160;&#160;-&gt;&#160;&#160;"/>
												</td></tr>
												<tr><td width="100" align="center">
													<input type="button" width="80" style="width:80; background-image: url('images/bgr_form_buttons.jpg')" onclick="delValues(lnkIssue)" value="&#160;&#160;&lt;-&#160;&#160;"/>
												</td></tr>
											</table>
										</td>
										<td width="40%">
											<xsl:apply-templates select="SubSet[@Name='LnkIssue']"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Parameters</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_PARAMETERS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<textarea rows="5" cols="55" wrap="soft" width="440" style="width:440" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/PARAMETERS/@XPath"/></xsl:attribute>
									<xsl:value-of select="T_OBLIGATION/PARAMETERS"/>
								</textarea>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">This obligation is</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_FLAGS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/EEA_PRIMARY/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/EEA_PRIMARY'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">chkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/EEA_PRIMARY/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/EEA_PRIMARY='1'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;EIONET Priority Data flow<br/>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/EEA_CORE/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/EEA_CORE'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">chkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/EEA_CORE/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/EEA_CORE='1'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;used for EEA Core set of indicators<br/>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/FLAGGED/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/FLAGGED'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">chkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/FLAGGED/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/FLAGGED='1'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;flagged<br/>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">DPSIR</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_DPSIR</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/DPSIR_D/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/DPSIR_D'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">dpsirChkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/DPSIR_D/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/DPSIR_D='yes'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;D<br/>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/DPSIR_P/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/DPSIR_P'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">dpsirChkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/DPSIR_P/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/DPSIR_P='yes'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;P<br/>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/DPSIR_S/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/DPSIR_S'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">dpsirChkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/DPSIR_S/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/DPSIR_S='yes'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;S<br/>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/DPSIR_I/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/DPSIR_I'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">dpsirChkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/DPSIR_I/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/DPSIR_I='yes'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;I<br/>
								<input type="hidden">
									<xsl:attribute name="name">
										<xsl:value-of select="T_OBLIGATION/DPSIR_R/@XPath"/>
									</xsl:attribute>
									<xsl:attribute name="value">
											<xsl:value-of select='T_OBLIGATION/DPSIR_R'/>
									</xsl:attribute>
								</input>
								<input type="checkbox">
									<xsl:attribute name="onclick">dpsirChkValue(this, document.forms['f'].elements["<xsl:value-of select='T_OBLIGATION/DPSIR_R/@XPath'/>"])</xsl:attribute>
									<xsl:if test="T_OBLIGATION/DPSIR_R='yes'">
											<xsl:attribute name="checked"/>
									</xsl:if>
								</input>
								&#160;R<br/>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">URL of overlapping obligation</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_OVERLAPURL</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="chkUrl(this); changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/OVERLAP_URL/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/OVERLAP_URL"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Indicators</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_INDICATORS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF" >
							<td colspan="3">
								<xsl:apply-templates select="SubSet[@Name='Indicators']"/>								
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">General comments</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_COMMENT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<textarea rows="5" cols="55" wrap="soft" width="440" style="width:440" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/COMMENT/@XPath"/></xsl:attribute>
									<xsl:value-of select="T_OBLIGATION/COMMENT"/>
								</textarea>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Authority giving rise to the obligation</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_AUTHORITY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input size="68" type="text" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/AUTHORITY/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/AUTHORITY"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top" align="left" bgcolor="#B7DBDB">
							<td align="left" style="border-right: 1px solid #C0C0C0" bgcolor="#B7DBDB">
									<span class="head0">Record management</span>
							</td>
							<td colspan="2" valign="center">
								<hr noshade="noshade" color="#006666"></hr>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Verified</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_RMVERIFIED</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="30" onchange="checkDate(this)">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/RM_VERIFIED/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/RM_VERIFIED"/></xsl:attribute>
								</input>&#160;<span class="smallfont">(dd/mm/yyyy)</span>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Verified by</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_RMVERIFIEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/RM_VERIFIED_BY/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/RM_VERIFIED_BY"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr valign="top">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Next update due</span></td>
							<td style="border-right: 1px solid #C0C0C0">
								<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_RMNEXTUPDATEDUE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="30" onchange="checkDate(this)">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/RM_NEXT_UPDATE/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/RM_NEXT_UPDATE"/></xsl:attribute>
								</input>&#160;<span class="smallfont">(dd/mm/yyyy)</span>
							</td>
						</tr>
						<tr valign="top" bgcolor="#FFFFFF">
							<td style="border-right: 1px solid #C0C0C0"><span class="head0">Validated by</span></td>
							<td style="border-right: 1px solid #C0C0C0">
									<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_VALIDATEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
							</td>
							<td>
								<input type="text" size="68" onchange="changed()">
									<xsl:attribute name="name"><xsl:value-of select="T_OBLIGATION/VALIDATED_BY/@XPath"/></xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="T_OBLIGATION/VALIDATED_BY"/></xsl:attribute>
								</input>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<script type="text/javascript">
					function getUrl() {
						var u = window.location.href;
						document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/REDIRECT_URL"].value = u;
					}
				</script>
				<td valign="middle" align="middle" style="border-top: 3 solid #B7DBDB" colspan="2" height="40" bgcolor="#008080">
					<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/REDIRECT_URL" value=""></input>
					<input style="font-weight: bold; width: 120px; color: #000000; background-image: url('images/bgr_form_buttons.jpg')" onclick='getUrl(); checkAndSave(document.forms["f"].elements["{T_OBLIGATION/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/NEXT_REPORTING/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/VALID_TO/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/TERMINATE/@XPath}"], document.forms["f"].elements["{T_OBLIGATION/FK_CLIENT_ID/@XPath}"]);' type="button" value="Save changes" width="120"></input>
					&#160;
					<input onclick="history.back()" type="button" value="Exit" style="font-weight: bold; color: #000000; background-image: url('images/bgr_form_buttons.jpg')"></input>
				</td>
      </tr>

		</table>

</form>
			<script type="text/javascript">

				var txtInfo=document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkInfo']/Row/T_INFO_LNK/FK_INFO_IDS"];
				var lnkInfo=document.f.info_lnk;
				
				//pre selects type-of info values
				selectMultilist(txtInfo, lnkInfo);
				
				var lnkIssue = document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkIssue']/Row/T_RAISSUE_LNK/FK_ISSUE_ID"].options;
				var issueLst = document.f.issue_list.options;

				inclSelect(lnkIssue, issueLst);

				//var lnkPar = document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkPar']/Row/T_PARAMETER_LNK/FK_PARAMETER_ID"].options;
				//var parLst = document.f.par_list.options;

				//inclSelect(lnkPar, parLst);


				var lnkSpatial = document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkSpatial']/Row/T_RASPATIAL_LNK/FK_SPATIAL_ID"].options;
				var spatialLst = document.f.spatial_list.options;
				inclSelect(lnkSpatial, spatialLst);


				var lnkVoluntaryCountries = document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='VoluntaryCountries']/Row/T_RASPATIAL_LNK/FK_SPATIAL_ID"].options;
				var countryLst = document.f.country_list.options;

				inclSelect(lnkVoluntaryCountries, countryLst);


					var lnkClients = document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='CCClients']/Row/T_CLIENT_LNK/FK_CLIENT_ID"].options;
					var clientLst = document.f.client_list.options;

					inclSelect(lnkClients, clientLst);

				</script>
	</xsl:template> 
	
	<xsl:template match="SubSet[@Name='LnkIssue']">
		<select multiple="multiple" size="9" style="width:240"  width="240">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkIssue']/@XPath"/>/Row/T_RAISSUE_LNK/FK_ISSUE_ID</xsl:attribute><xsl:for-each select="Row"><option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_RAISSUE_LNK/FK_ISSUE_ID"/>
				</xsl:attribute>
				<xsl:value-of select="T_ISSUE/ISSUE_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="RowSet[@Name='ISSUE']">
		<select multiple="multiple" size="9" name="issue_list" style="width:240" width="240">
			<xsl:for-each select="T_ISSUE">
				<option><xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/></xsl:attribute>
				<xsl:value-of select="ISSUE_NAME"/></option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<!--xsl:template match="SubSet[@Name='LnkPar']">
		<select multiple="multiple" size="9" style="width:300"  width="300">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkPar']/@XPath"/>/Row/T_PARAMETER_LNK/FK_PARAMETER_ID</xsl:attribute><xsl:for-each select="Row"><option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_PARAMETER_LNK/FK_PARAMETER_ID"/>:<xsl:value-of select="T_UNIT/PK_UNIT_ID"/></xsl:attribute>
				<xsl:value-of select="T_PARAMETER/PARAMETER_NAME"/>
				<xsl:if test="T_UNIT/UNIT_NAME != ''">
					&#160;[<xsl:value-of select="T_UNIT/UNIT_NAME"/>]
				</xsl:if>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template-->

	<!--xsl:template match="RowSet[@Name='PARAMETER']">
		<script type="text/javascript">
			<xsl:for-each select="//RowSet[@Name='ParamGroup']/Row/T_PARAM_GROUP">
				picklist.push("<xsl:value-of select="PK_GROUP_ID"/>:<xsl:value-of select="GROUP_NAME"/>:<xsl:value-of select="GROUP_TYPE"/>");
			</xsl:for-each>	  
		</script>
					<select name="param_type" onchange="fillPicklist(this.options[this.selectedIndex].value,document.f.param_group);fillMultilist(document.f.param_group.options[0].value,document.f.par_list)">
						<xsl:for-each select="//RowSet[@Name='GroupType']/Row/T_LOOKUP">
							<option>
								<xsl:attribute name="value">
									<xsl:value-of select="C_VALUE"/>
								</xsl:attribute>
							<xsl:value-of select="C_TERM"/></option>
						</xsl:for-each>
					</select><br/>
					<select name="param_group" style="width:300" onchange="fillMultilist(this.options[this.selectedIndex].value,document.f.par_list)">
						<option value="-1">Choose a group</option>
						<xsl:apply-templates select="RowSet[@Name='ParamGroup']"/>
					</select>
		<script type="text/javascript">
			fillPicklist('C',document.f.param_group)
		</script>

		<script type="text/javascript">
			<xsl:for-each select="T_PARAMETER">
				multilist.push("<xsl:value-of select="PK_PARAMETER_ID"/>:<xsl:value-of select="PARAMETER_NAME"/>:<xsl:value-of select="FK_GROUP_ID"/>");
			</xsl:for-each>	  
		</script>
		<select multiple="multiple" size="9" name="par_list" style="width:300" width="300">
			<xsl:for-each select="T_PARAMETER">
				<option><xsl:attribute name="value"><xsl:value-of select="PK_PARAMETER_ID"/></xsl:attribute>
				<xsl:value-of select="PARAMETER_NAME"/></option>
			</xsl:for-each>
		</select>
		<script type="text/javascript">
			fillMultilist(document.f.param_group.options[0].value,document.f.par_list)
		</script>
	</xsl:template-->


	<xsl:template match="RowSet[@Name='Client']">
		<select multiple="multiple" size="9" name="client_list" style="width:240" width="240">
			<xsl:for-each select="Row/T_CLIENT">
				<option>
					<xsl:attribute name="value">
						<xsl:value-of select="//XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID"/>:<xsl:value-of select="PK_CLIENT_ID"/>
					</xsl:attribute>
					<xsl:value-of select="CLIENT_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="SubSet[@Name='CCClients']">
		<select multiple="multiple" size="9" style="width:240"  width="240">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='CCClients']/@XPath"/>/Row/T_CLIENT_LNK/FK_CLIENT_ID</xsl:attribute>
			<xsl:for-each select="Row">
				<option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_CLIENT_LNK/FK_OBJECT_ID"/>:<xsl:value-of select="T_CLIENT_LNK/FK_CLIENT_ID"/>
				</xsl:attribute>
				<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>


	<!--xsl:template match="RowSet[@Name='ParamGroup']">
		<xsl:for-each select="//RowSet[@Name='ParamGroup']/Row/T_PARAM_GROUP">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_GROUP_ID"/>
				</xsl:attribute>
			<xsl:value-of select="GROUP_NAME"/></option>
		</xsl:for-each>
	</xsl:template-->

	<xsl:template match="SubSet[@Name='VoluntaryCountries']">
		<select multiple="multiple" size="9" style="width:240"  width="240">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='VoluntaryCountries']/@XPath"/>/Row/T_RASPATIAL_LNK/FK_SPATIAL_ID</xsl:attribute>
			<xsl:for-each select="Row">
				<option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_RASPATIAL_LNK/FK_RA_ID"/>:<xsl:value-of select="T_RASPATIAL_LNK/FK_SPATIAL_ID"/>
				</xsl:attribute>
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Indicators']">
		 <table border="1" width="100%" cellspacing="0" bordercolorlight="#C0C0C0" bordercolordark="#C0C0C0">
	     <tr valign="top" align="left">
					<th width="23%">Title</th>
          <th width="13%">Number</th>
          <th width="28%">URL</th>
          <th width="23%">Owner</th>
          <th width="7%"></th>
          <th width="7%"></th>
       </tr>
			 <xsl:for-each select="Row">
					<tr bgcolor="#FFFFFF">
						<td><xsl:value-of select="T_INDICATOR/TITLE"/></td>					
						<td><xsl:value-of select="T_INDICATOR/NUMBER"/></td>
						<td><xsl:value-of select="T_INDICATOR/URL"/></td>
						<td><xsl:value-of select="T_INDICATOR/OWNER"/></td>
						<td><input type="button" value="Edit" style="color: #000000; font-family: Verdana; font-size: 8pt; font-weight: bold; background-image: url('images/bgr_form_buttons_wide.jpg')" name="edit">
								<xsl:attribute name="onclick">javascript:updIndicator('<xsl:value-of select="T_INDICATOR/PK_INDICATOR_ID"/>','<xsl:value-of select="T_INDICATOR/FK_RA_ID"/>')</xsl:attribute>
							</input>
						</td>
					<td>
						<input type="hidden" name="/XmlData/RowSet[@Name='Indicator']/Row/T_INDICATOR/PK_INDICATOR_ID">
								<xsl:attribute name="value"><xsl:value-of select="T_INDICATOR/PK_INDICATOR_ID"/></xsl:attribute>
						</input>
						<input type="button" value="Delete" style="color: #000000; font-family: Verdana; font-size: 8pt; font-weight: bold; background-image: url('images/bgr_form_buttons_wide.jpg')" name="delete">
							<xsl:attribute name="onclick">javascript:delIndicator('<xsl:value-of select="T_INDICATOR/PK_INDICATOR_ID"/>','<xsl:value-of select="T_INDICATOR/FK_RA_ID"/>', '<xsl:value-of select="T_INDICATOR/FK_RA_ID"/>', '<xsl:value-of select="$src-id"/>')</xsl:attribute>
						</input>
					</td>
				</tr>
			 </xsl:for-each>
			 <tr bgcolor="#008080">
				<td colspan="2"></td>
				<td colspan="2">
						<input type="button" value="Add new indicator" style="color: #000000; font-family: Verdana; font-size: 10pt; font-weight: bold; background-image: url('images/bgr_form_buttons_wide.jpg')" name="addIndicator">
							<xsl:attribute name="onclick">
							<xsl:choose>
								<xsl:when test="//RowSet[@skeleton='1']">
									alert("You have to save this obligation first before adding any indicators");
								</xsl:when>
							<xsl:otherwise>
								updIndicator('-1','<xsl:value-of select="//XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID"/>');
							</xsl:otherwise>
							</xsl:choose>
							</xsl:attribute>
						</input>
				</td>
				<td colspan="2"></td>
			 </tr>
		</table>
	</xsl:template>


	<xsl:template match="SubSet[@Name='LnkSpatial']">
		<select multiple="multiple" size="9" style="width:240"  width="240">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkSpatial']/@XPath"/>/Row/T_RASPATIAL_LNK/FK_SPATIAL_ID</xsl:attribute><xsl:for-each select="Row"><option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_RASPATIAL_LNK/FK_SPATIAL_ID"/>
				</xsl:attribute>
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="SubSet[@Name='LnkInfo']">
		<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkInfo']/Row/T_INFO_LNK/FK_INFO_IDS">
			<!--xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='SubSpatial']/@XPath"/>/Row/T_RASPATIAL_LNK/FK_SPATIAL_ID</xsl:attribute-->
			<xsl:attribute name="value"><xsl:for-each select="Row">|<xsl:value-of select="T_INFO_LNK/FK_INFO_ID"/>|</xsl:for-each></xsl:attribute>
			</input>
	</xsl:template>


	<xsl:template match="RowSet[@Name='CountryList']">
		<select multiple="multiple" size="9" name="country_list" style="width:240" width="240">
			<option><xsl:attribute name="value"><xsl:value-of select="//XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID"/>:0</xsl:attribute>All EU countries</option>
			<xsl:for-each select="Row/T_SPATIAL">
				<option>
					<xsl:attribute name="value">
						<xsl:value-of select="//XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID"/>:<xsl:value-of select="PK_SPATIAL_ID"/>
					</xsl:attribute>
					<xsl:value-of select="SPATIAL_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="RowSet[@Name='SpatialType']">
		<select name="spatial_type" onchange="fillclist(this.options[this.selectedIndex].value,document.f.spatial_list);" style="visibility:hidden" disabled="disabled">
			<xsl:for-each select="T_LOOKUP">
				<option>
					<xsl:attribute name="value">
						<xsl:value-of select="C_VALUE"/>
					</xsl:attribute>
				<xsl:value-of select="C_TERM"/></option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="RowSet[@Name='SPATIAL']">
		<script type="text/javascript">
			clist.push("0:All EU Countries:C:");
			<xsl:for-each select="T_SPATIAL">
				clist.push("<xsl:value-of select="PK_SPATIAL_ID"/>:<xsl:value-of select="SPATIAL_NAME"/>:<xsl:value-of select="SPATIAL_TYPE"/>:<xsl:value-of select="SPATIAL_TWOLETTER"/>");
			</xsl:for-each>	  
		</script>
		<select multiple="multiple" size="9" name="spatial_list" style="width:240" width="240">
			<option><xsl:attribute name="value">0</xsl:attribute>All EU countries</option>
			<xsl:for-each select="T_SPATIAL">
				<option><xsl:attribute name="value"><xsl:value-of select="PK_SPATIAL_ID"/></xsl:attribute>
				<xsl:value-of select="SPATIAL_NAME"/></option>
			</xsl:for-each>
		</select>

		<script type="text/javascript">
			fillclist('C',document.f.spatial_list)
		</script>		

	</xsl:template>


</xsl:stylesheet>
