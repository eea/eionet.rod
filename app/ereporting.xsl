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

	<xsl:variable name="ro-id">
		<xsl:value-of select="//RowSet[@Name='Reporting']/Row/T_REPORTING/PK_RO_ID"/>
	</xsl:variable>

	<xsl:variable name="src-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<table cellspacing="7pts" width="700">
		<tr>
			<td>
				<span class="head1">Edit/Create Obligation
<!--
					<xsl:choose>
						<xsl:when test="//RowSet[@Name='Reporting']/Row/T_REPORTING/ALIAS != ''">
							<xsl:value-of select="//RowSet[@Name='Reporting']/Row/T_REPORTING/ALIAS"/>
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
-->
				</span>
			</td>
		</tr>
		</table>
		<table cellspacing="15pts">
			<tr valign="top">
				<td nowrap="true" width="134"><span class="head0">Obligation name:</span></td>
				<td width="500">
					<span class="head0">
						<xsl:choose>
							<xsl:when test="//RowSet[@Name='Reporting']/Row/T_REPORTING/ALIAS != ''">
								<xsl:value-of select="//RowSet[@Name='Reporting']/Row/T_REPORTING/ALIAS"/>
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
					</span>
				</td>
			</tr>
		</table>
		<xsl:apply-templates select="RowSet[@Name='Reporting']/Row"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Reporting']/Row">
		<form name="f" method="POST" action="reporting.jsv">
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
			<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/PK_RO_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$ro-id"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/FK_SOURCE_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$src-id"/></xsl:attribute>
		</input>
		<table cellspacing="15pts">
			<tr valign="top">
				<td nowrap="true" width="120"><span class="head0">Recognized:</span></td>
				<td>
					<select onChange="changed()"><xsl:attribute name="name"><xsl:value-of select="T_REPORTING/RECOGNIZED/@XPath"/></xsl:attribute>
						<xsl:for-each select="//RowSet[@Name='YesNo']/T_LOOKUP">
							<xsl:choose>
								<xsl:when test="C_VALUE=//RowSet[@Name='Reporting']/Row/T_REPORTING/RECOGNIZED">
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
				<input type="text" size="25" maxlength="100" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/RECOGNIZED_DETAIL/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/RECOGNIZED_DETAIL"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>Alias name:</b></td>
				<td><input type="text" size="50" width="500" style="width:500" maxlength="255" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/ALIAS/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/ALIAS"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>Report to:</b></td>
				<!--td><input type="text" size="50" width="500" style="width:500" maxlength="255" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/REPORT_TO/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/REPORT_TO"/></xsl:attribute>
				</input></td-->
				<td><select  width="500" style="width:500" maxlength="255" onChange="changed()">
					<xsl:variable name="selClient">
							<xsl:value-of select="T_REPORTING/FK_CLIENT_ID"/>
					</xsl:variable>
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/FK_CLIENT_ID/@XPath"/></xsl:attribute>
						<option value=''></option>
						<xsl:for-each select="//RowSet[@Name='Client']/Row">
							<option>
								<xsl:attribute name="value">
									<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>
								</xsl:attribute>
									<xsl:if test="T_CLIENT/PK_CLIENT_ID = $selClient">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
								<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
							</option>
						</xsl:for-each>
					<!--xsl:attribute name="value"><xsl:value-of select="T_REPORTING/REPORT_TO"/></xsl:attribute-->
				</select>
				<map name="newClientMap">
					<area alt="Add a new client" shape="rect" coords="0,0,25,25" href="javascript:openAddClientWin()"></area>
				</map>
				<img border="0" height="25" width="25" src="images/new.gif" usemap="#newClientMap"></img>
				</td>
			</tr>
			<tr valign="middle">
				<td nowrap="true" align="left"><b>Obligation type:</b></td>
				<td>
					<select onChange="changed()"><xsl:attribute name="name"><xsl:value-of select="T_REPORTING/LEGAL_MORAL/@XPath"/></xsl:attribute>
						<xsl:for-each select="//RowSet[@Name='LegalMoral']/T_LOOKUP">
							<xsl:choose>
								<xsl:when test="C_VALUE=//RowSet[@Name='Reporting']/Row/T_REPORTING/LEGAL_MORAL">
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
			</tr>
			<tr valign="middle">
				<td nowrap="true" align="left"><b>Valid from:</b><br/>(dd/mm/yyyy)</td>
				<td><input type="text" size="25" maxlength="100" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/VALID_FROM/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/VALID_FROM"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true" colspan="4"><span class="head0">Environmental issues:</span></td>
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
				<td nowrap="true" colspan="4"><span class="head0">Spatial coverage:</span></td>
			</tr>
			<tr>
				<td colspan="4">
					<table><tr valign="middle">
						<td width="300" align="center" valign="bottom"><xsl:apply-templates select="SubSet[@Name='LnkSpatial']"/></td>
						<td width="100" nowrap="true">
							<table cellspacing="5">
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="delValues(lnkSpatial)" 
										value="&#160;&#160;-&gt;&#160;&#160;"/>
								</td></tr>
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="addValues(spatialLst, lnkSpatial, null)" 
										value="&#160;&#160;&lt;-&#160;&#160;"/>
								</td></tr>
							</table>
						</td>
						<td width="300" align="center" valign="bottom">
							<xsl:apply-templates select="//RowSet[@Name='SpatialType']"/>
							<br/>
							<xsl:apply-templates select="//RowSet[@Name='SPATIAL']"/>
						</td>
					</tr></table>
				</td>
			</tr>
			</table>
			<hr width="700"/>
			<div style="margin-left:20">
				<table cellspacing="7"><tr>
					<td><input type="button" onclick="save()" value="Save changes" width="100" style="width:100"/></td>
			             <td><input type="button" onclick='history.back()' value="Exit"/></td>
				</tr></table>
			</div>
			</form>
                    <script language="JavaScript">

	var lnkSpatial = document.f.elements["/XmlData/RowSet[@Name='Reporting']/Row/SubSet[@Name='LnkSpatial']/Row/T_SPATIAL_LNK/FK_SPATIAL_ID"].options;
	var spatialLst = document.f.spatial_list.options;

	inclSelect(lnkSpatial, spatialLst);

	var lnkIssue = document.f.elements["/XmlData/RowSet[@Name='Reporting']/Row/SubSet[@Name='LnkIssue']/Row/T_ISSUE_LNK/FK_ISSUE_ID"].options;
	var issueLst = document.f.issue_list.options;

	inclSelect(lnkIssue, issueLst);

		</script>

	</xsl:template> 
	

	<xsl:template match="SubSet[@Name='LnkIssue']">
		<select multiple="true" size="9" style="width:300"  width="300">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkIssue']/@XPath"/>/Row/T_ISSUE_LNK/FK_ISSUE_ID</xsl:attribute><xsl:for-each select="Row"><option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_ISSUE_LNK/FK_ISSUE_ID"/>
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

	<xsl:template match="SubSet[@Name='LnkSpatial']">
		<select multiple="true" size="9" style="width:300"  width="300">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkSpatial']/@XPath"/>/Row/T_SPATIAL_LNK/FK_SPATIAL_ID</xsl:attribute><xsl:for-each select="Row"><option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_SPATIAL_LNK/FK_SPATIAL_ID"/>
				</xsl:attribute>
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="RowSet[@Name='SpatialType']">
		<select name="spatial_type" onchange="fillPicklist(this.options[this.selectedIndex].value,document.f.spatial_list);">
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
		<script language="JavaScript">
			<xsl:for-each select="T_SPATIAL">
			picklist.push("<xsl:value-of select="PK_SPATIAL_ID"/>:<xsl:value-of select="SPATIAL_NAME"/>:<xsl:value-of select="SPATIAL_TYPE"/>");
			</xsl:for-each>	  
		</script>
		<select multiple="true" size="9" name="spatial_list" style="width:300" width="300">
			<xsl:for-each select="T_SPATIAL">
				<option><xsl:attribute name="value"><xsl:value-of select="PK_SPATIAL_ID"/></xsl:attribute>
				<xsl:value-of select="SPATIAL_NAME"/></option>
			</xsl:for-each>
		</select>
		<script language="JavaScript">
			fillPicklist('C',document.f.spatial_list)
		</script>		
	</xsl:template>

</xsl:stylesheet>
