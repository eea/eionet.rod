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
	<xsl:variable name="pagetitle">
		Legislative instrument details: <xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/ALIAS"/>
	</xsl:variable>
	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="src-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet[@Name='Source']/@permissions"/>
	</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Legislative instrument</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source']"/>
			<xsl:call-template name="LIRORAFooter">
			<xsl:with-param name="table">LI</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source']/Row[position()=1]">
		<!-- form for delete legislation action -->
		<xsl:if test="contains($permissions, ',/instruments:d,')='true'">
			<script type="text/javascript">
			<![CDATA[
				function delLegislation() {
					if (confirm("Do you want to delete the current legislative instrument\nwith all related reporting obligations and activities?")){
						var u = window.location.href;
						document.f.elements["/XmlData/RowSet[@Name='Source']/Row/T_SOURCE/REDIRECT_URL"].value = u;
						document.f.submit();
					}
				}
			]]>
			</script>
			<form name="f" method="POST" action="source.jsv">
				<input type="hidden" name="dom-update-mode" value="D"/>
				<input type="hidden" name="/XmlData/RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID">
					<xsl:attribute name="value"><xsl:value-of select="$src-id"/></xsl:attribute>
				</input>
				<input type="hidden" name="/XmlData/RowSet[@Name='Source']/Row/T_SOURCE/REDIRECT_URL" value=""></input>
			</form>
		</xsl:if>

		<!-- page -->
		<div id="workarea">
						<div style="float:right"><xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_LI</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></div>
						<br style="clear:right"/>
						<div style="float:right">
								<xsl:if test="$admin='true'">
									<xsl:attribute name="style">border: 1px solid black; background-color:#a0a0a0; float:right</xsl:attribute>
									<div style="font-weight:bold; color:white; text-align:center">Actions</div>
								</xsl:if>
								<xsl:if test="contains($permissions, ',/obligations:i,')='true'">
									<a><xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>
									<img src="images/newobligation.png" alt="Create a new reporting obligation" border="0"/></a><br/>
								</xsl:if>
								<xsl:if test="contains($permissions, ',/instruments:i,')='true'">
									<a><xsl:attribute name="href">source.jsv?id=-1</xsl:attribute>
									<img src="images/newinstrument.png" alt="Create a new legislative instrument" border="0"/></a><br/>
								</xsl:if>
								<xsl:if test="contains($permissions, ',/instruments:u,')='true'">
									<a><xsl:attribute name="href">source.jsv?id=<xsl:value-of select="$src-id"/></xsl:attribute>
									<img src="images/editinstrument.png" alt="Edit this instrument" border="0"/></a><br/>
								</xsl:if>
								<xsl:if test="contains($permissions, ',/instruments:d,')='true'">
									<a href="javascript:delLegislation()">
									<img src="images/deleteinstrument.png" alt="Delete this instrument" border="0"/></a><br/>
								</xsl:if>
								<xsl:if test="contains($permissions, ',/instruments:u,')='true'">
									<a><xsl:attribute name="href">versions.jsp?id=<xsl:value-of select="$src-id"/>&amp;tab=T_SOURCE&amp;id_field=PK_SOURCE_ID</xsl:attribute>
									<img src="images/showhistory.png" alt="Show Previous Actions" border="0"/></a><br/>
								</xsl:if>
								<xsl:if test="$admin='true'">
									<a href="javascript:openHelpList('LI')"><img src="images/bb_fielddescr.png" alt="View field descriptions" border="0"/></a><br/>
								</xsl:if>
						</div>
					<h1>Legislative instrument details: <xsl:value-of select="T_SOURCE/ALIAS"/></h1>

				<table class="datatable">
				<col style="width:25%" />
				<col style="width:75%" />
					<tr class="zebraodd">
						<th scope="row" class="scope-row">Classification</th>
						<td>
							<xsl:apply-templates select="SubSet[@Name='Parents']"/>
						</td>
					</tr>
					<tr class="zebraeven">
						<th scope="row" class="scope-row">Legal name</th>
						<td>
							<xsl:choose>
								<xsl:when test="T_SOURCE/URL!=''">
									<a>
										<xsl:attribute name="href"><xsl:value-of select="T_SOURCE/URL"/></xsl:attribute>
										<xsl:attribute name="target">_new</xsl:attribute>
				      				<xsl:call-template name="break">
			   				   	<xsl:with-param name="text" select="T_SOURCE/TITLE"/>
								      </xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="T_SOURCE/TITLE"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<tr class="zebraodd">
						<th scope="row" class="scope-row">Short name</th>
						<td>
							<xsl:value-of select="T_SOURCE/ALIAS"/>						
						</td>
					</tr>
					<tr class="zebraeven">
						<th scope="row" class="scope-row">CELEX reference</th>
						<td>
							<xsl:value-of select="T_SOURCE/CELEX_REF"/>						
						</td>
					</tr>
					<tr class="zebraodd">
						<th scope="row" class="scope-row">Identification number</th>
						<td>
							<xsl:value-of select="T_SOURCE/SOURCE_CODE"/>						
						</td>
					</tr>
					<tr class="zebraeven">
						<th scope="row" class="scope-row">Issued by</th>
						<td>
							<a>
								<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
								<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
							</a>
						</td>
					</tr>
					<tr class="zebraodd">
						<th scope="row" class="scope-row">URL to issuer</th>
						<td>
							<a target="_new">
								<xsl:attribute name="href"><xsl:value-of select="T_SOURCE/ISSUED_BY_URL"/></xsl:attribute>
								<xsl:value-of select="T_SOURCE/ISSUED_BY_URL_LABEL"/>
							</a>
						</td>
					</tr>
					<xsl:apply-templates select="SubSet[@Name='Origin']"/>
					<xsl:apply-templates select="SubSet[@Name='RelatedInstruments']"/>
					<tr class="zebraeven">
						<th scope="row" class="scope-row">Valid from</th>
						<td>
							<xsl:value-of select="T_SOURCE/VALID_FROM"/>
						</td>
					</tr>
					<tr class="zebraodd">
						<th scope="row" class="scope-row">Abstract</th>
						<td>
				      	<xsl:call-template name="break">
	   				   	<xsl:with-param name="text" select="T_SOURCE/ABSTRACT"/>
					      </xsl:call-template>
						</td>
					</tr>
					<tr class="zebraeven">
						<td colspan="2" style="background-color:#006666; color: white; font-weight:bold">
							Reporting framework
						</td>
					</tr>
					<tr class="zebraodd">
						<th scope="row" class="scope-row">Reporting obligations</th>
						<td>
							<xsl:apply-templates select="//SubSet[@Name='Obligation']"/>
						</td>
					</tr>
					<xsl:if test="//RowSet[@Name='DGEnv']/Row/T_LOOKUP/C_TERM!=''">
						<tr class="zebraodd">
							<th scope="row" class="scope-row">DG Env review of reporting theme</th>
							<td>
								<xsl:value-of select="//RowSet[@Name='DGEnv']/Row/T_LOOKUP/C_TERM"/>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="T_SOURCE/GEOGRAPHIC_SCOPE!=''">
						<tr class="zebraeven">
							<th scope="row" class="scope-row">Geographic scope</th>
							<td>
								<xsl:value-of select="T_SOURCE/GEOGRAPHIC_SCOPE"/>
							</td>
						</tr>
					</xsl:if>
					<tr class="zebraodd">
						<th scope="row" class="scope-row">Comments</th>
						<td>
				      	<xsl:call-template name="break">
   					   	<xsl:with-param name="text" select="T_SOURCE/COMMENT"/>
					      </xsl:call-template>
						</td>
					</tr>
					<xsl:for-each select="SubSet[@Name='Parents']/Row/T_SOURCE_CLASS">
						<xsl:if test="CLASS_NAME = 'Conventions'">
							<tr class="zebraeven">
								<th scope="row" class="scope-row">EC entry into force</th>
								<td>
									<xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/EC_ENTRY_INTO_FORCE"/>
								</td>
							</tr>
							<tr class="zebraodd">
								<th scope="row" class="scope-row">EC accession</th>
								<td>
									<xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/EC_ACCESSION"/>
								</td>
							</tr>
							<tr class="zebraeven">
								<th scope="row" class="scope-row">Convention secretariat</th>
								<td>
									<xsl:choose>
										<xsl:when test="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/SECRETARIAT_URL!=''">
											<a>
												<xsl:attribute name="href"><xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/SECRETARIAT_URL"/></xsl:attribute>
												<xsl:attribute name="target">_new	</xsl:attribute>
												<xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/SECRETARIAT"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/SECRETARIAT"/>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
					</xsl:if>
				</xsl:for-each>
				</table>
		</div>
	</xsl:template>

	<xsl:template match="//SubSet[@Name='Obligation']">
		<xsl:if test="count(Row)>0">
			<ul class="menu">
				<xsl:for-each select="Row/T_OBLIGATION">
					<li>
							<a>
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RA_ID"/>&amp;mode=A&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>
								<xsl:choose>
									<xsl:when test="TITLE != ''">
										<xsl:value-of select="TITLE"/>
									</xsl:when>
									<xsl:otherwise>
										Reporting obligation
									</xsl:otherwise>
								</xsl:choose>
							</a>
							<xsl:if test="AUTHORITY != ''">
								&#160;[<xsl:value-of select="AUTHORITY"/>]
							</xsl:if>
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Parents']">
		<ul class="menu">
		<xsl:for-each select="Row/T_SOURCE_CLASS">
			<li>
				<xsl:if test="CLASSIFICATOR!=''">
					<xsl:value-of select="CLASSIFICATOR"/>&#160;
				</xsl:if>
				<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_CLASS_ID"/>&amp;mode=C</xsl:attribute>
				<span class="head0"><xsl:value-of select="CLASS_NAME"/></span></a>
			</li>
		</xsl:for-each>
		</ul>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Origin']/Row/LSOURCE">
		<tr valign="top">
			<th scope="row" class="scope-row">Parent legislative instrument</th>
			<td>
				<a>
					<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
					<xsl:choose>
						<xsl:when test="ALIAS != ''">
							<xsl:value-of select="ALIAS"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="TITLE"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="SubSet[@Name='RelatedInstruments']">
		<xsl:if test="count(Row)>0">
			<tr valign="top">
				<th scope="row" class="scope-row">Related instrument(s)</th>
				<td>
					<xsl:for-each select="Row/LSOURCE">
						<a>
							<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
							<xsl:choose>
								<xsl:when test="ALIAS != ''">
									<xsl:value-of select="ALIAS"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="TITLE"/>
								</xsl:otherwise>
							</xsl:choose>
						</a>
						<br/>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
