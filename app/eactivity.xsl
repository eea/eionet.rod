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
	<xsl:include href="editor.xsl"/>
	<xsl:include href="util.xsl"/>

	<xsl:variable name="ro-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_REPORTING/PK_RO_ID"/>
	</xsl:variable>

	<xsl:variable name="src-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="type-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/FK_TYPE_ID"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<table cellspacing="7pts" width="700">
		<tr>
			<td>
				<span class="head1">
						<!--xsl:choose>
							<xsl:when test="//RowSet[@Name='Activity']/Row/T_ACTIVITY/TITLE != ''">
								<xsl:value-of select="//RowSet[@Name='Activity']/Row/T_ACTIVITY/TITLE"/>
							</xsl:when>
							<xsl:otherwise-->
								Reporting Activity
							<!--/xsl:otherwise>
						</xsl:choose-->
						for 
						<xsl:choose>
							<xsl:when test="//RowSet[@Name='Source']/Row/T_REPORTING/ALIAS != ''">
								<xsl:value-of select="//RowSet[@Name='Source']/Row/T_REPORTING/ALIAS"/>
							</xsl:when>
							<xsl:otherwise>
								Obligation
							</xsl:otherwise>
						</xsl:choose>
						from
						<xsl:choose>
							<xsl:when test="//RowSet[@Name='Source']/Row/T_SOURCE/ALIAS != ''">
								<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/ALIAS"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/TITLE"/>
							</xsl:otherwise>
						</xsl:choose>
					<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_RA</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span>
			</td>
		</tr>
		</table>
		<xsl:apply-templates select="RowSet[@Name='Activity']/Row"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Activity']/Row">
		<form name="f" method="POST" action="activity.jsv">
<!--
		<input type="hidden" name="xml-query-string">
			<xsl:attribute name="value"><xsl:value-of select="/XmlData/xml-query-string"/></xsl:attribute>
		</input>
-->
		<input type="hidden" name="dom-update-mode">
			<xsl:attribute name="value">
				<xsl:choose>
					<xsl:when test="../../RowSet[@skeleton='1']">A</xsl:when>
					<xsl:otherwise>U</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/PK_RA_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/PK_RA_ID"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/FK_SOURCE_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$src-id"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/FK_RO_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$ro-id"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/TERMINATE/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/TERMINATE"/></xsl:attribute>
		</input>
		<table cellspacing="15pts" border="0">
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Title:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_TITLE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
				<td colspan="3">
					<input type="text" size="60" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/TITLE/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/TITLE"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Responsible for reporting:</span><br/>(role prefix)
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_RESPONSIBLEFORREPORTING</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="3">
					<input type="text" size="30" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">First reporting:</span><br/>(dd/mm/yyyy)
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_VALIDFROM</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="3">
					<input type="text" size="30" onChange='changedReporting(document.forms["f"].elements["{T_ACTIVITY/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/VALID_TO/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/TERMINATE/@XPath}"])'>
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/FIRST_REPORTING/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/FIRST_REPORTING"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Valid to:</span><br/>(dd/mm/yyyy)
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_VALIDTO</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="3">
					<input type="text" size="30" onChange='changedReporting(document.forms["f"].elements["{T_ACTIVITY/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/VALID_TO/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/TERMINATE/@XPath}"])'>
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/VALID_TO/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/VALID_TO"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Reporting frequency in months:</span><br/>(0 for one-time-only reporting)
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_REPORTINGFREQUENCYINMONTHS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="3">
					<input type="text" size="30" onChange='changedReporting(document.forms["f"].elements["{T_ACTIVITY/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/VALID_TO/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/TERMINATE/@XPath}"])'>
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/REPORT_FREQ_MONTHS/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/REPORT_FREQ_MONTHS"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Next due date:</span><br/>(calculated automatically)
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_NEXTDUEDATE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="3">
					<input type="text" size="30" onChange="changed()" disabled="true">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/NEXT_DEADLINE/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/NEXT_DEADLINE"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td colspan="4">
					<i><b>IMPORTANT!</b> Use the following field <b>only</b> if reporting date (above fields) cannot ge given in date format (for example, 'ASAP'). Otherwise, leave it empty.</i><br/>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Reporting date (text format):
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_REPORTINGDATETEXTFORMAT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span><br/></td>
				<td colspan="3">
					<input type="text" size="60" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/NEXT_REPORTING/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/NEXT_REPORTING"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Date comments:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_DATECOMMENTS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
				<td colspan="3">
					<input type="text" size="60" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/DATE_COMMENTS/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/DATE_COMMENTS"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<!--tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Terminated:</span></td>
				<td colspan="3">
					<select onChange="changed()"><xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/TERMINATE/@XPath"/></xsl:attribute>
						<xsl:for-each select="//RowSet[@Name='YesNo']/T_LOOKUP">
							<xsl:choose>
								<xsl:when test="C_VALUE=//RowSet[@Name='Activity']/Row/T_ACTIVITY/TERMINATE">
									<option selected="true"><xsl:attribute name="value"><xsl:value-of select="C_VALUE"/></xsl:attribute>
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
			</tr-->
			<tr valign="top">
				<td nowrap="true" colspan="4"><span class="head0">Environmental issues:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_ENVIRONMENTALISSUES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
			</tr>
			<tr>
				<td colspan="4">
					<table><tr valign="middle">
						<td width="300" align="center"><xsl:apply-templates select="SubSet[@Name='LnkIssue']"/></td>
						<td width="100" nowrap="true">
							<table cellspacing="5">
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="delValues(lnkIssue)" 
										value="&#160;&#160;-&gt;&#160;&#160;"/>
								</td></tr>
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="addValues(issueLst, lnkIssue, null)" 
										value="&#160;&#160;&lt;-&#160;&#160;"/>
								</td></tr>
							</table>
						</td>
						<td width="300" align="center"><xsl:apply-templates select="//RowSet[@Name='ISSUE']"/></td>
					</tr></table>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" colspan="4"><span class="head0">Parameters:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_PARAMETERS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
			</tr>
			<tr>
				<td colspan="4">
					<table><tr valign="middle">
						<td width="300" align="center" valign="bottom"><xsl:apply-templates select="SubSet[@Name='LnkPar']"/></td>
						<td width="100" nowrap="true">
							<table cellspacing="5">
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="delValues(lnkPar)" 
										value="&#160;&#160;-&gt;&#160;&#160;"/>
								</td></tr>
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="addValues(parLst, lnkPar, document.f.unittype.options[document.f.unittype.selectedIndex])" 
										value="&#160;&#160;&lt;-&#160;&#160;"/>
								</td></tr>
<!--								<tr><td width="100" align="center">
									Unit<br/><input type="text" name="unit" size="7" maxlength="50" width="80" style="width:80"/>
								</td></tr>-->
								<tr><td width="100" align="center">
									Unit type:<br/>
									<select name="unittype"><option value=''/>
										<xsl:for-each select="//RowSet[@Name='UnitType']/T_UNIT">
											<option>
												<xsl:attribute name="value"><xsl:value-of select="PK_UNIT_ID"/></xsl:attribute>
												<xsl:value-of select="UNIT_NAME"/>
											</option>
										</xsl:for-each>
									</select>
								</td></tr>
							</table>
						</td>
						<td width="300" align="left" valign="bottom"><xsl:apply-templates select="//RowSet[@Name='PARAMETER']"/></td>
					</tr></table>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Reporting guidelines:<br/>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_REPORTINGGUIDELINES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
				<td colspan="3">
					<textarea rows="5" cols="55" wrap="soft" width="570" style="width:570" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/REPORTING_FORMAT/@XPath"/></xsl:attribute>
					<xsl:value-of select="T_ACTIVITY/REPORTING_FORMAT"/></textarea>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Name of reporting guidelines:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_NAMEOFREPORTINGGUIDELINES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
				<td colspan="3">
					<input type="text" size="60" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/FORMAT_NAME/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/FORMAT_NAME"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Format valid since:</span><br/>(dd/mm/yyyy)
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_FORMATVALIDSINCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="3">
					<input type="text" size="30" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/VALID_SINCE/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/VALID_SINCE"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">URL to reporting guidelines:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_URLTOREPORTINGGUIDELINES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
				<td colspan="3">
					<input type="text" size="60" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/REPORT_FORMAT_URL/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="T_ACTIVITY/REPORT_FORMAT_URL"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="155"><span class="head0">Comment:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RA_COMMENT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
				<td colspan="3">
					<textarea rows="5" cols="55" wrap="soft" width="570" style="width:570" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_ACTIVITY/COMMENT/@XPath"/></xsl:attribute>
					<xsl:value-of select="T_ACTIVITY/COMMENT"/></textarea>
				</td>
			</tr>
			</table>
			<hr width="700"/>
			<div style="margin-left:20">
				<table cellspacing="7"><tr>
					<td><input type="button" onclick='checkAndSave(document.forms["f"].elements["{T_ACTIVITY/FIRST_REPORTING/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/REPORT_FREQ_MONTHS/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/NEXT_DEADLINE/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/NEXT_REPORTING/@XPath}"], document.forms["f"].elements["{T_ACTIVITY/VALID_TO/@XPath}"])' value="Save changes" width="100" style="width:100"/></td>
			             <td><input type="button" onclick='history.back()' value="Exit"/></td>
				</tr></table>
			</div>
			</form>
                    <script language="JavaScript">

	var lnkIssue = document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkIssue']/Row/T_RAISSUE_LNK/FK_ISSUE_ID"].options;
	var issueLst = document.f.issue_list.options;

	inclSelect(lnkIssue, issueLst);

	var lnkPar = document.f.elements["/XmlData/RowSet[@Name='Activity']/Row/SubSet[@Name='LnkPar']/Row/T_PARAMETER_LNK/FK_PARAMETER_ID"].options;
	var parLst = document.f.par_list.options;

	inclSelect(lnkPar, parLst);

		</script>
	</xsl:template> 
	
	<xsl:template match="SubSet[@Name='LnkIssue']">
		<select multiple="true" size="9" style="width:300"  width="300">
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
		<select multiple="true" size="9" name="issue_list" style="width:300" width="300">
			<xsl:for-each select="T_ISSUE">
				<option><xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/></xsl:attribute>
				<xsl:value-of select="ISSUE_NAME"/></option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="SubSet[@Name='LnkPar']">
		<select multiple="true" size="9" style="width:300"  width="300">
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
	</xsl:template>

	<xsl:template match="RowSet[@Name='PARAMETER']">
		<script language="JavaScript">
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
		<script language="JavaScript">
			fillPicklist('C',document.f.param_group)
		</script>

		<script language="JavaScript">
			<xsl:for-each select="T_PARAMETER">
			multilist.push("<xsl:value-of select="PK_PARAMETER_ID"/>:<xsl:value-of select="PARAMETER_NAME"/>:<xsl:value-of select="FK_GROUP_ID"/>");
			</xsl:for-each>	  
		</script>
		<select multiple="true" size="9" name="par_list" style="width:300" width="300">
			<xsl:for-each select="T_PARAMETER">
				<option><xsl:attribute name="value"><xsl:value-of select="PK_PARAMETER_ID"/></xsl:attribute>
				<xsl:value-of select="PARAMETER_NAME"/></option>
			</xsl:for-each>
		</select>
		<script language="JavaScript">
			fillMultilist(document.f.param_group.options[0].value,document.f.par_list)
		</script>
	</xsl:template>

	<xsl:template match="RowSet[@Name='ParamGroup']">
		<xsl:for-each select="//RowSet[@Name='ParamGroup']/Row/T_PARAM_GROUP">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_GROUP_ID"/>
				</xsl:attribute>
			<xsl:value-of select="GROUP_NAME"/></option>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
