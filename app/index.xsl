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
	<xsl:include href="util.xsl"/>

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
		<table width="600" cellspacing="7">
			<tr valign="middle">
				<td colspan="2"><span class="head1"><xsl:call-template name="IntroductoryTitle"/></span></td>
			</tr>
			<tr>
				<td colspan="2"><span class="head0"><xsl:call-template name="IntroductoryText"/></span></td>
			</tr>
		</table>
		<table cellspacing="10" border="0" width="600">
			<tr><td><table cellspacing="10" border="0">
				<tr>
					<td colspan="2"><span class="head0">Reporting Obligations:</span></td>
				</tr>
				<tr valign="center">
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590"><a href="rorabrowse.jsv?mode=R"><span class="head0">All obligations</span></a></td>
				</tr>
				<tr>
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590"><a><xsl:attribute name="href">rorabrowse.jsv?mode=R&amp;type=<xsl:call-template name="DB_EUObligation_ID"/>:EU legislation obligations</xsl:attribute><span class="head0">EU legislation obligations</span></a>
					</td>
				</tr>
				<tr>
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590">
						<a><xsl:attribute name="href">rorabrowse.jsv?mode=R&amp;type=<xsl:call-template name="DB_ConventionObligation_ID"/>:Conventions' obligations</xsl:attribute><span class="head0">Conventions' obligations</span></a>
					</td>
				</tr>
				<tr>
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590">
						<a><xsl:attribute name="href">rorabrowse.jsv?mode=R&amp;type=<xsl:call-template name="DB_EEARequest_ID"/>:EEA requests</xsl:attribute><span class="head0">EEA requests</span></a>
					</td>
				</tr>
				<tr>
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590">
						<a><xsl:attribute name="href">rorabrowse.jsv?mode=R&amp;type=<xsl:call-template name="DB_EurostatRequest_ID"/>:Eurostat requests</xsl:attribute><span class="head0">Eurostat requests</span></a>
					</td>
				</tr>
				<tr>
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590">
						<a><xsl:attribute name="href">rorabrowse.jsv?mode=R&amp;type=<xsl:call-template name="DB_OtherRequest_ID"/>:Other requests</xsl:attribute><span class="head0">Other requests</span></a>
					</td>
				</tr>
			</table></td>
			<td valign="top"><table cellspacing="10" border="0">
				<tr>
					<td colspan="2"><span class="head0">Reporting Activities:</span></td>
				</tr>
				<tr valign="center">
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590"><a href="rorabrowse.jsv?mode=A"><span class="head0">All activities</span></a></td>
				</tr>
				<tr>
					<td colspan="2"><span class="head0">Legal Instruments:</span></td>
				</tr>
				<tr valign="center">
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590">
						<a><xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=C</xsl:attribute>
						<span class="head0">All instruments</span></a>
					</td>
				</tr>
				<tr>
					<td colspan="2"><span class="head0">Deadlines &amp; Deliveries:</span></td>
				</tr>
				<tr valign="center">
					<td width="10"><img src="images/diamlil.gif"/></td>
					<td width="590">
						<a><xsl:attribute name="href">deliveries.jsv</xsl:attribute>
						<span class="head0">Deadlines by country</span></a>
					</td>
				</tr>

				</table></td>
			</tr>
			<tr valign="center">
				<td colspan="2"><hr/></td>
			</tr>
		</table>

<!-- Reporting obligation search filters -->
		<form name="f" method="get" action="rorabrowse.jsv">
		<input type="hidden" name="mode" value="R"/>
		<table cellspacing="10" border="0" width="600">
			<tr><td colspan="3">
				<span class="head0">Reporting obligations/activities selected by different filters:
					<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_MAIN</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span>
				</td>
			</tr>
			<tr valign="center">
				<td width="10"><img src="images/diamlil.gif"/></td>
				<td width="245">Environmental issues
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_MAIN_ENVIRONMENTALISSUES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td>
					<select name="env_issue">
						<option value="-1">Choose an issue</option>
						<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
					</select>
				</td>
			</tr>
			<tr>
				<td width="10"><img src="images/diamlil.gif"/></td>
				<td width="245">Spatial Coverage
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_MAIN_SPATIALCOVERAGE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td></td>
			</tr>
			<tr valign="center">
				<td width="10"></td>
				<td width="245">Countries</td>
				<td>
					<select name="country">
						<option value="-1">Choose a country</option>
						<xsl:call-template name="SpatialTemplate">
							<xsl:with-param name ="type">C</xsl:with-param>
							<xsl:with-param name ="type2"></xsl:with-param>
						</xsl:call-template>
					</select>
				</td>
			</tr>
			<tr valign="center">
				<td width="10"><img src="images/diamlil.gif"/></td>
				<td width="245">Specific parameters
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_MAIN_SPECIFICPARAMETERS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td>
		<script language="JavaScript">
			<xsl:for-each select="RowSet[@Name='ParamGroup']/Row/T_PARAM_GROUP">
			picklist.push("<xsl:value-of select="PK_GROUP_ID"/>:<xsl:value-of select="GROUP_NAME"/>:<xsl:value-of select="GROUP_TYPE"/>");
			</xsl:for-each>	  
		</script>
					<select name="param_type" onchange="fillPicklist(this.options[this.selectedIndex].value,f.param_group)">
						<xsl:for-each select="RowSet[@Name='GroupType']/Row/T_LOOKUP">
							<option>
								<xsl:attribute name="value">
									<xsl:value-of select="C_VALUE"/>
								</xsl:attribute>
							<xsl:value-of select="C_TERM"/></option>
						</xsl:for-each>
					</select><br/>
					<select name="param_group">
						<option value="-1">Choose a group</option>
						<xsl:apply-templates select="RowSet[@Name='ParamGroup']"/>
					</select>
		<script language="JavaScript">
			fillPicklist('C',document.f.param_group)
		</script>
				</td>
			</tr>
			<tr>
				<td colspan="2"></td>
				<!--td><input type="submit" style="width:300" value="Show selected reporting obligations"></input></td-->
				<td><input type="button" style="width:300" onclick="javascript:document.f.mode.value = 'R';document.f.submit();" value="Show selected reporting obligations"></input></td>
			</tr>
			<tr>
				<td colspan="2"></td>
				<td><input type="button" style="width:300" onclick="javascript:document.f.mode.value = 'A';document.f.submit();" value="Show selected reporting activities"></input></td>
			</tr>
		</table>
		</form>

<!-- -->
		<table cellspacing="10" border="0" width="600">
			<tr><td colspan="2"><hr/></td></tr>
			<tr valign="center">
				<td colspan="2"><br/><span class="head0">
					To date	<xsl:value-of select="RowSet[@Name='ROMetaInfo']/Row/T_REPORTING/TOTAL_RO"/>
					reporting obligations and
					<xsl:value-of select="RowSet[@Name='RAMetaInfo']/Row/T_ACTIVITY/TOTAL_RA"/>
					activities are stored in the database.
					Latest update of the database:
					<xsl:choose>
						<xsl:when test="RowSet[@Name='ROMetaInfo']/Row/T_REPORTING/RO_UPDATE
							&gt; RowSet[@Name='RAMetaInfo']/Row/T_ACTIVITY/RA_UPDATE">
							<xsl:value-of select="RowSet[@Name='ROMetaInfo']/Row/T_REPORTING/RO_UPDATE"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="RowSet[@Name='RAMetaInfo']/Row/T_ACTIVITY/RA_UPDATE"/>
						</xsl:otherwise>
					</xsl:choose>
				<br/><br/>

	<xsl:call-template name="Disclaimer"/>
				</span></td>
			</tr>

            <tr valign="center"><td colspan="2">
                    <span class="head0"><a href="javascript:history.back()">Back</a></span></td>
            </tr>
		</table>
		</div>
		
	</xsl:template>

	<xsl:template match="RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/>:<xsl:value-of select="ISSUE_NAME"/></xsl:attribute>
			<xsl:value-of select="ISSUE_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

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

	<xsl:template match="RowSet[@Name='ParamGroup']">
		<xsl:for-each select="Row/T_PARAM_GROUP">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_GROUP_ID"/>:<xsl:value-of select="GROUP_NAME"/>
				</xsl:attribute>
			<xsl:value-of select="GROUP_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>
