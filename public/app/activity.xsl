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

	<xsl:variable name="ra-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_ACTIVITY/PK_RA_ID"/>
	</xsl:variable>

	<xsl:variable name="src-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="ro-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_REPORTING/PK_RO_ID"/>
	</xsl:variable>
	
	<xsl:variable name="admin">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/@auth"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:choose>
			<xsl:when test="count(RowSet[@Name='Activity']/Row)>0">
				<xsl:apply-templates select="RowSet[@Name='Activity']"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="nofound"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="LIRORAFooter">
			<xsl:with-param name="table">RA</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Activity']/Row">
		<!-- form for delete activity action -->
		<xsl:if test="contains($permissions, ',/obligations:d,')='true'">
			<script language="JavaScript">
			<![CDATA[
function delActivity() {
	if (confirm("Do you want to delete the reporting activity?"))
		document.f.submit();
}
			]]>
			</script>
			<form name="f" method="POST" action="activity.jsv">
				<input type="hidden" name="dom-update-mode" value="D"/>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_ACTIVITY/PK_RA_ID">
					<xsl:attribute name="value"><xsl:value-of select="$ra-id"/></xsl:attribute>
				</input>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_ACTIVITY/FK_RO_ID">
					<xsl:attribute name="value"><xsl:value-of select="$ro-id"/></xsl:attribute>
				</input>
			</form>
		</xsl:if>
		<!-- navigation bar -->
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
		         	<td valign="bottom">
							<a href="index.html"><span class="barfont">ROD</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom">
							<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$src-id"/>&amp;mode=S</xsl:attribute>
							<span class="barfont">Legal instrument</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
      	         <td valign="bottom">
							<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$ro-id"/>&amp;aid=<xsl:value-of select="$src-id"/>&amp;mode=R</xsl:attribute>
							<span class="barfont">Reporting obligation</span></a>
						</td>
            	   <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom"><span class="barfont">Reporting activity</span></td>
                	<td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		<!-- page -->
		<div style="margin-left:13">
		<table cellspacing="7pts" width="600">
			<tr><td>
				<span class="head1">Details of reporting activity</span>
			</td></tr>
		</table>
		<table cellspacing="7pts" width="600" border="0">
			<tr valign="top">
				<td width="22%"><span class="head0">Title:</span></td>
				<td width="60%">
					<xsl:choose>
						<xsl:when test="T_ACTIVITY/TITLE != ''">
							<xsl:value-of select="T_ACTIVITY/TITLE"/>
						</xsl:when>
						<xsl:otherwise>
							Reporting Activity
						</xsl:otherwise>
					</xsl:choose>
				</td>
			<!-- DGB -->
				<td width="*" align="right" rowspan="3" >
					<table width="100%">
					<tr>
					<td>
						<xsl:call-template name="Print"/>  <br/><br/>
						<xsl:if test="T_ACTIVITY/FK_DELIVERY_COUNTRY_IDS != ''">
							<a><xsl:attribute name="href">javascript:openDeliveries('<xsl:value-of select="$ra-id"/>','%%')</xsl:attribute>
							 <img src="images/bb_show_deliveries.png" alt="Show the status of country deliveries" border="0"/></a><br/>
						</xsl:if>
					</td>
					</tr>
					<tr><td align="center">
					<xsl:if test="$admin='true'">
						<xsl:attribute name="bgcolor">#A0A0A0</xsl:attribute>
						<xsl:attribute name="style">BORDER: #000000 1px solid;</xsl:attribute>
						<b><font color="#FFFFFF">Actions</font></b><br/><br/>
					</xsl:if>
					<xsl:if test="contains($permissions, ',/obligations:i,')='true'">
						<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="$ro-id"/></xsl:attribute>
							<img src="images/newactivity.png" alt="Create a new reporting activity" border="0"/></a><br/>
						</xsl:if>
						<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
						<a><xsl:attribute name="href">activity.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="$ro-id"/></xsl:attribute><img src="images/editactivity.png" alt="Edit this activity" border="0"/></a><br/>
						</xsl:if>
						<xsl:if test="contains($permissions, ',/obligations:d,')='true'">
						<a href="javascript:delActivity()"><img src="images/deleteactivity.png" alt="Delete this activity" border="0"/></a><br/>
					</xsl:if>				
					<xsl:if test="contains($permissions, ',/Admin:v,')='true'">
					<a>
					<xsl:attribute name="href">javascript:openHistory('<xsl:value-of select="$ra-id"/>', 'A')</xsl:attribute>
					<img src="images/showhistory.png" alt="Show history of changes" border="0"/>
					</a><br/>
					</xsl:if>				
					</td>
					</tr>
					</table>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Parent reporting obligation:</span></td>
				<td width="60%">
						<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;aid=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=R</xsl:attribute>
						<xsl:choose>
							<xsl:when test="T_REPORTING/ALIAS != ''">
								<xsl:value-of select="T_REPORTING/ALIAS"/>
							</xsl:when>
							<xsl:otherwise>
								Obligation
							</xsl:otherwise>
						</xsl:choose>
						</a>
						from
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
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Baseline reporting date:</span></td>
				<td>
					<xsl:value-of select="T_ACTIVITY/FIRST_REPORTING"/>
				</td>
			</tr>
			<xsl:if test="string-length(T_ACTIVITY/NEXT_REPORTING) = 0">
				<tr valign="top">
					<td width="22%"><span class="head0">Reporting frequency:</span></td>
					<td colspan="2">
						<xsl:call-template name="RAReportingFrequency"/>
					</td>
				</tr>
			</xsl:if>
			<tr valign="top">
				<td width="22%"><span class="head0">Next report due:</span></td>
				<td colspan="2">
					<xsl:choose>
						<xsl:when test="T_ACTIVITY/TERMINATE = 'N'">
							<xsl:choose>
							<xsl:when test="string-length(T_ACTIVITY/NEXT_REPORTING) = 0">
								<xsl:value-of select="T_ACTIVITY/NEXT_DEADLINE"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="T_ACTIVITY/NEXT_REPORTING"/>
							</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<font color="red">terminated</font>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Date comments:</span></td>
				<td colspan="2">
					<xsl:value-of select="T_ACTIVITY/DATE_COMMENTS"/>
				</td>
			</tr>
			<!-- kl 031013 -->
			<tr valign="top">
				<td width="22%"><span class="head0">Countries:</span></td>
				<td colspan="2">
					<xsl:apply-templates select="//RowSet[@Name='Spatial']"/>
				</td>
			</tr>


			<tr valign="top">
				<td width="22%"><span class="head0">National Reporting contacts:</span></td>
				<td colspan="2">
					<xsl:choose>
					<xsl:when test="T_ROLE/ROLE_ID!=''">
						<a>
							<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_URL"/>')</xsl:attribute>
							<xsl:value-of select="T_ROLE/ROLE_NAME"/> (<xsl:value-of select="T_ROLE/ROLE_ID"/>)</a>
						&#160;<img src="images/button_role.png" alt="Additional details for logged-in users">
							<xsl:attribute name="onClick">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
						</img>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="T_ACTIVITY/RESPONSIBLE_ROLE != ''">
							<font color="#ff0000"><b>Role '<xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE"/>' does not exist in the Directory!</b></font>
						</xsl:if>
					</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Environmental issues:</span></td>
				<td colspan="2">
					<xsl:apply-templates select="//RowSet[@Name='EnvIssue']"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Reported parameters:</span></td>
				<td colspan="2">
					<xsl:apply-templates select="SubSet[@Name='Parameter']"/>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">Reporting guidelines:</span></td>
				<td colspan="2">
				<xsl:choose>
					<xsl:when test="T_ACTIVITY/REPORTING_FORMAT=''">N/A</xsl:when>
					<xsl:otherwise><xsl:value-of select="T_ACTIVITY/REPORTING_FORMAT"/></xsl:otherwise>
				</xsl:choose>
				</td>
			</tr>
			<tr valign="top">
				<td width="22%"><span class="head0">URL to reporting guidelines:</span></td>
				<td colspan="2">
					<xsl:choose>
						<xsl:when test="T_ACTIVITY/REPORT_FORMAT_URL != ''">
							<a>	<xsl:attribute name="href"><xsl:value-of select="T_ACTIVITY/REPORT_FORMAT_URL"/></xsl:attribute>
							<xsl:choose>
								<xsl:when test="T_ACTIVITY/FORMAT_NAME != ''">								
									<xsl:value-of select="T_ACTIVITY/FORMAT_NAME"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="T_ACTIVITY/REPORT_FORMAT_URL"/>
								</xsl:otherwise>
							</xsl:choose>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_ACTIVITY/FORMAT_NAME"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="T_ACTIVITY/VALID_SINCE != ''">
						(valid from <xsl:value-of select="T_ACTIVITY/VALID_SINCE"/>)
					</xsl:if>
				</td>
			</tr>

			<tr valign="top">
				<td width="22%"><span class="head0">URL to repository:</span></td>
				<td colspan="2">
					<a>	<xsl:attribute name="href"><xsl:value-of select="T_ACTIVITY/LOCATION_PTR"/></xsl:attribute>
					<xsl:choose>
						<xsl:when test="T_ACTIVITY/LOCATION_INFO != ''">								
							<xsl:value-of select="T_ACTIVITY/LOCATION_INFO"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_ACTIVITY/LOCATION_PTR"/>
						</xsl:otherwise>
					</xsl:choose>
					</a>
				</td>
			</tr>


			<tr valign="top">
				<td width="22%"><span class="head0">Comments:</span></td>
				<td colspan="2">
				<xsl:value-of select="T_ACTIVITY/COMMENT"/>
				</td>
			</tr>
			<tr><td colspan="3"><br/><hr/></td></tr>
		</table>
		</div>
	</xsl:template>

	<xsl:template match="//RowSet[@Name='Spatial']">
		<table>
			<tr>
				<td>
					<xsl:for-each select="Row">
						<xsl:choose>
							<xsl:when test="T_RASPATIAL_LNK/VOLUNTARY='Y'">
								<span title="Informal participation in the reporting activity"><xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>*</span>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="position()!=count(//RowSet[@Name='Spatial']/Row)">, </xsl:if>
					</xsl:for-each>
				</td>
				<td  valign="top">
					<img src="images/button_spatial.png" alt="Details for countries' joining history">
						<xsl:attribute name="onClick">javascript:openSpatialHist('<xsl:value-of select="$ra-id"/>')</xsl:attribute>
					</img>
				</td>
			</tr>
			</table>
	</xsl:template>


	<xsl:template match="//RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
		<xsl:choose>
			<xsl:when test="position()!=count(//RowSet[@Name='EnvIssue']/Row/T_ISSUE)">
				<xsl:value-of select="ISSUE_NAME"/>, 
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="ISSUE_NAME"/></xsl:otherwise>
		</xsl:choose></xsl:for-each>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Parameter']">
		<xsl:if test="count(Row)>0">
		<table width="100%" colspan="5" border="1">
			<tr>
				<td bgcolor="#646666"><span class="head0"><font color="white">Nr</font></span></td>
				<td bgcolor="#646666"><span class="head0"><font color="white">Parameter</font></span></td>
			</tr>
			<xsl:for-each select="Row">
				<tr>
					<td>
						<xsl:number format="1."/>
					</td>
					<td>
						<xsl:value-of select="T_PARAMETER/PARAMETER_NAME"/>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
