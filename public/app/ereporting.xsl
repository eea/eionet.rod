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

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:variable name="ro-id">
		<xsl:value-of select="//RowSet[@Name='Reporting']/Row/T_REPORTING/PK_RO_ID"/>
	</xsl:variable>

	<xsl:variable name="src-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:template match="XmlData">



		<script language="JScript">
				<![CDATA[
			function addCl() {
				document.forms["f"].silent.value=1;
				save(null,true);
				openAddClientWin();
				document.forms["f"].silent.value=0;
				}
		]]>
		</script>


		<table cellspacing="7pts" width="640">
		<tr>
			<td width="465">
				<span class="head1">Edit/Create Obligation for
					<br/>
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
			<td>
				<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_RO</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
			</td>
		</tr>
		</table>
		<xsl:apply-templates select="RowSet[@Name='Reporting']/Row"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Reporting']/Row">
		<!--form name="f" method="POST" action="reporting.jsv"-->
		<form name="f" method="POST" action="reporting.jsv">
			<input type="hidden" name="silent" value="0"/>
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
				<td nowrap="true"><b>Short name:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_ALIASNAME</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</b></td>
				<td>
					<input type="text" size="50" width="500" style="width:500" maxlength="255" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/ALIAS/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/ALIAS"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>Report to:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_REPORTTO</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</b></td>
				<td>

					<select  width="500" style="width:500" maxlength="255" onChange="changed()">
					<xsl:variable name="selClient">
							<xsl:value-of select="T_CLIENT_LNK/FK_CLIENT_ID"/>
					</xsl:variable>
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/FK_CLIENT_ID/@XPath"/></xsl:attribute>
						<option value='0'></option>
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

		<xsl:if test="contains($permissions, ',/Client:i,')='true'">

				<map name="newClientMap">
					<area alt="Add a new client to client list" shape="rect" coords="0,0,75,25" href="javascript:addCl()"></area>
				</map>
				<img border="0" src="images/bb_new.png" usemap="#newClientMap"></img>
		</xsl:if>
				</td>
			</tr>
<!-- -->
			<tr valign="top">
				<td nowrap="true" colspan="4"><span class="head0">Other clients using this reporting:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_OTHERCLIENTS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
			</tr>
			<tr>
				<td colspan="4">
					<table><tr valign="middle">
						<td width="300" align="center" valign="bottom">
							<xsl:apply-templates select="//RowSet[@Name='Client']"/>
						</td>
						<td width="100" nowrap="true">
							<table cellspacing="5">
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
 										onclick="addFullValues(clientLst, lnkClients, null)" 
										value="&#160;&#160;-&gt;&#160;&#160;"/>
								</td></tr>
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="delValues(lnkClients)" 
										value="&#160;&#160;&lt;-&#160;&#160;"/>
								</td></tr>
							</table>
						</td>
						<td width="300" align="center" valign="bottom">
							<xsl:apply-templates select="SubSet[@Name='CCClients']"/>
						</td>
					</tr></table>
				</td>
			</tr>

<!-- -->

			<tr valign="middle">
				<td nowrap="true" align="left"><b>Obligation type:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_OBLIGATIONTYPE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</b></td>
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
<!-- KL 030214 ISSUES -->
			<!--tr valign="top">
				<td nowrap="true" colspan="4"><span class="head0">Environmental issues from Reporting Activities:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_ENVIRONMENTALISSUES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
			</tr>
			<tr>
				<td colspan="4">
					<table><tr valign="middle">
						<td width="300" align="center"><xsl:apply-templates select="SubSet[@Name='LnkRaIssue']"/></td>
						<td width="100" nowrap="true"></td>
						<td width="300" align="center"></td>
					</tr></table>
				</td>
			</tr-->
<!-- ISSUES -->


			<!--tr valign="top">
				<td nowrap="true" colspan="4"><span class="head0">Spatial coverage:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_SPATIALCOVERAGE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
			</tr>
			<tr>
				<td colspan="4">
					<table><tr valign="middle">
						<td width="300" align="center" valign="bottom">
							<xsl:apply-templates select="//RowSet[@Name='SpatialType']"/>
							<br/>
							<xsl:apply-templates select="//RowSet[@Name='SPATIAL']"/>
						</td>
						<td width="100" nowrap="true">
							<table cellspacing="5">
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="addValues(spatialLst, lnkSpatial, null)" 
										value="&#160;&#160;-&gt;&#160;&#160;"/>
								</td></tr>
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="delValues(lnkSpatial)" 
										value="&#160;&#160;&lt;-&#160;&#160;"/>
								</td></tr>
							</table>
						</td>
						<td width="300" align="center" valign="bottom">
							<xsl:apply-templates select="SubSet[@Name='LnkSpatial']"/>
						</td>
					</tr></table>
				</td>
			</tr-->

<!-- -->
			<!--tr valign="top">
				<td nowrap="true" colspan="4"><span class="head0">Countries reporting voluntarily:
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_VOLUNTARYCOUNTRIES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td>
			</tr-->
			<!--tr>
				<td colspan="4">
					<table><tr valign="middle">
						<td width="300" align="center" valign="bottom">
							<xsl:apply-templates select="//RowSet[@Name='CountryList']"/>
						</td>
						<td width="100" nowrap="true">
							<table cellspacing="5">
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
 										onclick="addFullValues(countryLst, lnkVoluntaryCountries, null)" 
										value="&#160;&#160;-&gt;&#160;&#160;"/>
								</td></tr>
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80" 
										onclick="delValues(lnkVoluntaryCountries)" 
										value="&#160;&#160;&lt;-&#160;&#160;"/>
								</td></tr>
							</table>
						</td>
						<td width="300" align="center" valign="bottom">
							<xsl:apply-templates select="SubSet[@Name='VoluntaryCountries']"/>
						</td>
					</tr></table>
				</td>
			</tr-->


			</table>

			<!-- Record management -->
			<table width="720" border="0">
				<tr>
					<td nowrap="true" width="130"><i><b>Record management</b></i></td>
					<td width="590"><hr/></td>
				</tr>
			</table>
			<table cellspacing="15pts">
			<tr valign="top">
				<td nowrap="true" width="175"><span class="head0">Verified:</span>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_RMVERIFIED</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
					<br/>(dd/mm/yyyy)
				</td>
				<td><input type="text" size="25" maxlength="100" onChange="checkDate(this)">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/RM_VERIFIED/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/RM_VERIFIED"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="175"><span class="head0">Verified by:</span>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_RMVERIFIEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td><input type="text" size="25" maxlength="100" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/RM_VERIFIED_BY/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/RM_VERIFIED_BY"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="175"><span class="head0">Next update due:</span>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_RO_RMNEXTUPDATEDUE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
					<br/>(dd/mm/yyyy)
				</td>
				<td><input type="text" size="25" maxlength="100" onChange="checkDate(this)">
					<xsl:attribute name="name"><xsl:value-of select="T_REPORTING/RM_NEXT_UPDATE/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="T_REPORTING/RM_NEXT_UPDATE"/></xsl:attribute>
				</input></td>
			</tr>
			</table>
			
			<hr width="700"/>
			<div style="margin-left:20">
				<table cellspacing="7"><tr>
					<td><input type="button" onclick="javascript:save(null,false)" value="Save changes" width="100" style="width:100"/></td>
			             <td><input type="button" onclick='history.back()' value="Exit"/></td>
				</tr></table>
			</div>
			</form>
			<script language="JavaScript">

				//var lnkSpatial = document.f.elements["/XmlData/RowSet[@Name='Reporting']/Row/SubSet[@Name='LnkSpatial']/Row/T_SPATIAL_LNK/FK_SPATIAL_ID"].options;
				//var spatialLst = document.f.spatial_list.options;

				//inclSelect(lnkSpatial, spatialLst);

				var lnkClients = document.f.elements["/XmlData/RowSet[@Name='Reporting']/Row/SubSet[@Name='CCClients']/Row/T_CLIENT_LNK/FK_CLIENT_ID"].options;
				var clientLst = document.f.client_list.options;

				inclSelect(lnkClients, clientLst);

				//var lnkVoluntaryCountries = document.f.elements["/XmlData/RowSet[@Name='Reporting']/Row/SubSet[@Name='VoluntaryCountries']/Row/T_SPATIAL_LNK/FK_SPATIAL_ID"].options;
				//var countryLst = document.f.country_list.options;

				//inclSelect(lnkVoluntaryCountries, countryLst);

			</script>

	</xsl:template> 

	<xsl:template match="SubSet[@Name='LnkRaIssue']">
		<select size="9" style="width:300"  width="300">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkRaIssue']/@XPath"/>/Row/T_RAISSUE_LNK/FK_ISSUE_ID</xsl:attribute><xsl:for-each select="Row"><option>
				<!--xsl:attribute name="value">
					<xsl:value-of select="T_RAISSUE_LNK/FK_ISSUE_ID"/>
				</xsl:attribute-->
				<xsl:value-of select="T_ISSUE/ISSUE_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="SubSet[@Name='CCClients']">
		<select multiple="true" size="9" style="width:300"  width="300">
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

	<!--xsl:template match="SubSet[@Name='VoluntaryCountries']">
		<select multiple="true" size="9" style="width:300"  width="300">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='VoluntaryCountries']/@XPath"/>/Row/T_SPATIAL_LNK/FK_SPATIAL_ID</xsl:attribute>
			<xsl:for-each select="Row">
				<option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_SPATIAL_LNK/FK_RO_ID"/>:<xsl:value-of select="T_SPATIAL_LNK/FK_SPATIAL_ID"/>
				</xsl:attribute>
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template-->

	<!--xsl:template match="SubSet[@Name='LnkSpatial']">
		<select multiple="true" size="9" style="width:300"  width="300">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkSpatial']/@XPath"/>/Row/T_SPATIAL_LNK/FK_SPATIAL_ID</xsl:attribute><xsl:for-each select="Row"><option>
				<xsl:attribute name="value">
					<xsl:value-of select="T_SPATIAL_LNK/FK_SPATIAL_ID"/>
				</xsl:attribute>
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:template-->

	<!--xsl:template match="RowSet[@Name='SpatialType']">
		<select name="spatial_type" onchange="fillPicklist(this.options[this.selectedIndex].value,document.f.spatial_list);">
			<xsl:for-each select="T_LOOKUP">
				<option>
					<xsl:attribute name="value">
						<xsl:value-of select="C_VALUE"/>
					</xsl:attribute>
				<xsl:value-of select="C_TERM"/></option>
			</xsl:for-each>
		</select>
	</xsl:template-->

	<!--xsl:template match="RowSet[@Name='SPATIAL']">
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
	</xsl:template-->

	<xsl:template match="RowSet[@Name='Client']">
		<select multiple="true" size="9" name="client_list" style="width:300" width="300">
			<xsl:for-each select="Row/T_CLIENT">
				<option>
					<xsl:attribute name="value">
						<xsl:value-of select="//XmlData/RowSet[@Name='Reporting']/Row/T_REPORTING/PK_RO_ID"/>:<xsl:value-of select="PK_CLIENT_ID"/>
					</xsl:attribute>
					<xsl:value-of select="CLIENT_NAME"/>
				</option>
			</xsl:for-each>
		</select>
		<!--script language="JavaScript">
			fillMultilist('X',document.f.client_list)
		</script-->		
	</xsl:template>

	<xsl:template match="RowSet[@Name='CountryList']">
		<select multiple="true" size="9" name="country_list" style="width:300" width="300">
			<xsl:for-each select="Row/T_SPATIAL">
				<option>
					<xsl:attribute name="value">
						<xsl:value-of select="//XmlData/RowSet[@Name='Reporting']/Row/T_REPORTING/PK_RO_ID"/>:<xsl:value-of select="PK_SPATIAL_ID"/>
					</xsl:attribute>
					<xsl:value-of select="SPATIAL_NAME"/>
				</option>
			</xsl:for-each>
		</select>
		<!--script language="JavaScript">
			fillMultilist('X',document.f.client_list)
		</script-->		
	</xsl:template>

</xsl:stylesheet>
