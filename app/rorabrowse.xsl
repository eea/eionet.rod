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

	<xsl:variable name="showfilters">
		<xsl:value-of select="substring(substring-after(/XmlData/xml-query-string,'showfilters='),1,1)"/>
	</xsl:variable>

	<xsl:variable name="rora">
		<xsl:value-of select="substring(substring-after(/XmlData/xml-query-string,'mode='),1,1)"/>
	</xsl:variable>

	<xsl:variable name="historyMode">
		<xsl:if test="$rora='R'">O</xsl:if>
		<xsl:if test="$rora='A'">A</xsl:if>
	</xsl:variable>

	<xsl:template match="XmlData">
	<!-- context bar -->
      <table cellspacing="0" cellpadding="0" width="621" border="0">
			<tr>
         	<td align="bottom" width="20" background="images/bar_filled.jpg" height="25"> </td>
          	<td width="600" background="images/bar_filled.jpg" height="25">
            <table height="8" cellSpacing="0" cellPadding="0" background="" border="0">
            	<tr>
               	<td valign="bottom" align="middle" width="92">
							<a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom" align="middle" width="92">
							<a href="index.html"><span class="barfont">WebROD</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom" align="middle" width="122">
				<xsl:choose>
					<xsl:when test="$rora='A'">
						<a href="rorabrowse.jsv?mode=A"><span class="barfont">Reporting activity</span></a>
					</xsl:when>
					<xsl:otherwise>
						<a href="rorabrowse.jsv?mode=R"><span class="barfont">Reporting obligation</span></a>
					</xsl:otherwise>
				</xsl:choose>
			</td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	               <td valign="bottom" align="right" width="2 10"></td>
					</tr>
				</table>
			</td></tr>
			<tr><td> </td></tr>
		</table>

		<!-- page -->
		<div style="margin-left:13">

<!-- Search filters -->
		<xsl:choose>
			<xsl:when test="$showfilters=''">
				<table cellspacing="10" border="0" width="600">
					<tr><td colspan="3" align="right"><span class="head0">
						<!--a href="javascript:window.location.replace(window.location.href+'&amp;showfilters=1')">Show filters</a-->
						<a href="javascript:window.location.replace(window.location.href+'&amp;showfilters=1')"><img border="0" src="images/bb_advsearch.png"/></a>
					</span>	</td></tr>
				</table>
			</xsl:when>
		<xsl:otherwise>
			<form name="f" method="get" action="rorabrowse.jsv">
			<xsl:choose>
				<xsl:when test="$rora='A'">
					<input type="hidden" name="mode" value="A"/>
				</xsl:when>
				<xsl:otherwise>
					<input type="hidden" name="mode" value="R"/>
				</xsl:otherwise>
			</xsl:choose>
			<table cellspacing="10" border="0" width="600">
				<tr><td colspan="3"><span class="head0">
					<xsl:choose>
						<xsl:when test="$rora='A'">
							Reporting activities selected by different filters:
						</xsl:when>
						<xsl:otherwise>
							Reporting obligations selected by different filters:
						</xsl:otherwise>
					</xsl:choose>
					<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_RORA</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</span></td></tr>
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
							<xsl:with-param name="type">C</xsl:with-param>
							<xsl:with-param name="type2"></xsl:with-param>
						</xsl:call-template>
					</select>
				</td>
			</tr>
			<!--tr valign="center">
				<td width="10"></td>
				<td width="245">River runoff areas</td>
				<td>
					<select name="river">
						<option value="-1">Choose a river</option>
						<xsl:call-template name="SpatialTemplate">
							<xsl:with-param name="type">R</xsl:with-param>
							<xsl:with-param name="type2"></xsl:with-param>
						</xsl:call-template>
					</select>
				</td>
			</tr>
			<tr valign="center">
				<td width="10"></td>
				<td width="245">Seas</td>
				<td>
					<select name="sea">
						<option value="-1">Choose a sea</option>
						<xsl:call-template name="SpatialTemplate">
							<xsl:with-param name="type">S</xsl:with-param>
							<xsl:with-param name="type2"></xsl:with-param>
						</xsl:call-template>
					</select>
				</td>
			</tr>
			<tr valign="center">
				<td width="10"></td>
				<td width="245">Lakes and reservoirs</td>
				<td>
					<select name="lake">
						<option value="-1">Choose a lake or reservoir</option>
						<xsl:call-template name="SpatialTemplate">
							<xsl:with-param name="type">L</xsl:with-param>
							<xsl:with-param name="type2">O</xsl:with-param>
						</xsl:call-template>1
					</select>
				</td>
			</tr-->
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
				<xsl:choose>
					<xsl:when test="$rora='A'">
						<td><input type="submit" value="Show selected reporting activities"></input></td>
					</xsl:when>
					<xsl:otherwise>
						<td><input type="submit" value="Show selected reporting obligations"></input></td>
					</xsl:otherwise>
				</xsl:choose>
			</tr>
		</table>
		</form>
		</xsl:otherwise>
		</xsl:choose>

		<!-- page title -->
		<table cellspacing="7" border="0" width="100%"><tr><td>
				<xsl:choose>
					<xsl:when test="$rora='A'">
						<span class="head1">Reporting activities</span>
					</xsl:when>
					<xsl:otherwise>
						<span class="head1">Reporting obligations</span>
					</xsl:otherwise>
				</xsl:choose>
		</td>
		<td align="right">
		<xsl:if test="contains($permissions, 'y')='true'">
			<a>
				<xsl:attribute name="href">javascript:openActionTypeHistory('D','<xsl:value-of select="$historyMode"/>')</xsl:attribute>
				<img src="images/showdeleted.png" alt="Show deleted" border="0"/>
			</a><br/>
		</xsl:if>
		</td></tr>
		</table>
		<div style="margin-left:20">
		<!--table cellspacing="7pts">
			<xsl:apply-templates select="RowSet[@Name='Search results']/@*"/>
		</table-->
		</div>
				
		<xsl:apply-templates select="RowSet[@Name='Search results']"/>
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

	<xsl:template match="RowSet[@Name='Search results']">
<!-- RA search results -->
		<xsl:choose>
			<xsl:when test="$rora='A'">
				<xsl:choose>
					<xsl:when test="count(Row)=0">			
						<xsl:call-template name="nofound"/>
					</xsl:when>
					<xsl:otherwise>
						<table cellspacing="7pts" width="600">
							<xsl:for-each select="Row">
								<tr valign="top">
									<td width="10"><img src="images/diamlil.gif" vspace="4"/></td>
									<td colspan="2">
										<span class="head0n"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_ACTIVITY/FK_RO_ID"/>&amp;mode=A</xsl:attribute>
									<xsl:choose>
										<xsl:when test="T_ACTIVITY/TITLE != ''">
											<xsl:value-of select="T_ACTIVITY/TITLE"/>
									</xsl:when>
									<xsl:otherwise>
										Reporting Activity</xsl:otherwise>	
									</xsl:choose></a></span>
									<b> for </b>
									<span class="head0n"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;aid=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=R</xsl:attribute>
									<xsl:choose>
										<xsl:when test="T_REPORTING/ALIAS != ''">
											<xsl:value-of select="T_REPORTING/ALIAS"/></xsl:when>
										<xsl:otherwise>
											Obligation</xsl:otherwise></xsl:choose></a></span>
											<b> from </b><i>
												<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
											<xsl:choose>
												<xsl:when test="T_SOURCE/ALIAS != ''">
													<xsl:value-of select="T_SOURCE/ALIAS"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="T_SOURCE/TITLE"/>
												</xsl:otherwise>
											</xsl:choose>
											</a></i>						
<!--
						<xsl:choose>
							<xsl:when test="T_SOURCE/URL!=''">
								<a>	
									<xsl:attribute name="href">
										<xsl:value-of select="T_SOURCE/URL"/>
									</xsl:attribute>
									<xsl:attribute name="target">
										_new
									</xsl:attribute>
								<xsl:choose>
									<xsl:when test="T_SOURCE/ALIAS != ''">
										<xsl:value-of select="T_SOURCE/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_SOURCE/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
								</a>						
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="T_SOURCE/ALIAS != ''">
										<xsl:value-of select="T_SOURCE/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_SOURCE/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
-->				
						<br/>
						</td>
					</tr>
					<xsl:if test="T_ACTIVITY/NEXT_REPORTING != '' or T_ACTIVITY/NEXT_DEADLINE != '' or T_ACTIVITY/TERMINATE='Y'">
					<tr><td/>
						<td><span class="head0">Next reporting: </span> 
							<xsl:choose>
								<xsl:when test="T_ACTIVITY/TERMINATE  = 'Y'">
									<font color="red">terminated</font>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="T_ACTIVITY/NEXT_DEADLINE != ''">
											<xsl:value-of select="T_ACTIVITY/NEXT_DEADLINE"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="T_ACTIVITY/NEXT_REPORTING"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					</xsl:if>
				</xsl:for-each>
				</table>
			</xsl:otherwise>
		</xsl:choose>	
		
		</xsl:when>
<!-- RO search results -->
		<xsl:otherwise>
		<xsl:choose>
			<xsl:when test="count(Row)=0">			
				<xsl:call-template name="nofound"/>
			</xsl:when>
			<xsl:otherwise>
				<table cellspacing="7pts" width="600">
				<xsl:for-each select="Row">
					<tr valign="top">
						<td width="10"><img src="images/diamlil.gif" vspace="4"/></td>
						<td colspan="2">
						<span class="head0n"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;aid=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=R</xsl:attribute>
						<xsl:choose>
							<xsl:when test="T_REPORTING/ALIAS != ''">
								<xsl:value-of select="T_REPORTING/ALIAS"/></xsl:when>
							<xsl:otherwise>
								Obligation</xsl:otherwise></xsl:choose></a>
						</span>
						<b> from </b><i>
						<a>	
							<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
							<xsl:choose>
								<xsl:when test="T_SOURCE/ALIAS != ''">
									<xsl:value-of select="T_SOURCE/ALIAS"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="T_SOURCE/TITLE"/>
								</xsl:otherwise>
							</xsl:choose>
						</a></i>						
<!--
						<xsl:choose>
							<xsl:when test="T_SOURCE/URL!=''">
								<a>	
									<xsl:attribute name="href">
										<xsl:value-of select="T_SOURCE/URL"/>
									</xsl:attribute>
									<xsl:attribute name="target">
										_new
									</xsl:attribute>
								<xsl:choose>
									<xsl:when test="T_SOURCE/ALIAS != ''">
										<xsl:value-of select="T_SOURCE/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_SOURCE/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
								</a>						
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="T_SOURCE/ALIAS != ''">
										<xsl:value-of select="T_SOURCE/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_SOURCE/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>				
-->
						<br/>
						</td>
					</tr>
				</xsl:for-each>
				</table>
			</xsl:otherwise>
		</xsl:choose>		

		</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Search results']/@*">
		<xsl:if test="name(.)!='Name' and name(.)!='order' and name(.)!='auth'">
			<tr><td>
				<xsl:value-of select="translate(name(.),'_',' ')"/>&amp;<b><xsl:value-of select="."/></b>
			</td></tr>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
