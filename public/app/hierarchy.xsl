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
		Legislative instruments
	</xsl:variable>
	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem">
	<a><xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;amp;mode=C</xsl:attribute>
		Legislative instruments
	</a>
 </div>
 <div class="breadcrumbitemlast">Hierarchy</div>
 <div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source hierarchy']"/>
		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source hierarchy']/Row">
		<!-- page -->
		<div id="workarea">
		<!-- page title -->

			<table width="150" border="0" class="notprintable" style="float:right">
					<tr>
						<td><xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_HIERARCHY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template></td>
					</tr>
					<tr>
						<td align="center">
							<xsl:if test="$admin='true'">
								<xsl:attribute name="bgcolor">#A0A0A0</xsl:attribute>
								<xsl:attribute name="style">border: #000000 1px solid;</xsl:attribute>
								<b><font color="#FFFFFF">Actions</font></b><br/><br/>
							</xsl:if>
							<xsl:if test="contains($permissions, ',/Admin:v,')='true'">
								<a>
									<xsl:attribute name="href">history.jsp?item_type=L</xsl:attribute>
									<img src="images/showdeleted.png" border="0" alt="Show history of deleted records"/>
								</a>
								<br/>
							</xsl:if>
							<xsl:if test="contains($permissions, ',/instruments:i,')='true'">
								<a>
									<xsl:attribute name="href">source.jsv?id=-1</xsl:attribute>
									<img src="images/newinstrument.png" border="0" alt="Create a new legislative instrument"/>
								</a>
								<br/>
							</xsl:if>
						</td>
					</tr>
			</table>
	   <h1>Legislative instruments</h1>
	   <div style="background-color: white; width:606px; border: 1px solid #006666">
		 <div class="head0" style="font-size: 10pt">(Eur-lex categories)</div>
		<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!='' or T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
		   <div style="padding:.5em .5em 0 .5em">
						<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!=''">
							<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/></span>&#160;
						</xsl:if>
						<xsl:if test="T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">		
							<a>
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/>&amp;amp;mode=C</xsl:attribute>
								<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span>
							</a>
						</xsl:if>
			</div>
		</xsl:if>

		<xsl:choose>		
			<xsl:when test="T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
				<xsl:apply-templates select="SubSet[@Name='subClass']" mode="normal"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="SubSet[@Name='subClass']" mode="heading"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="SubSet[@Name='Source']"/>
		
		</div>

		</div>
	</xsl:template>

	<xsl:template match="SubSet[@Name='subClass']" mode="heading">
		<xsl:if test="count(Row)>0">
			<ul class="topcategory">
				<xsl:for-each select="Row">
						<li>
								<a>
									<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;amp;mode=C</xsl:attribute>
									<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span>
								</a>
					<xsl:apply-templates select="SubSet[@Name='subClass']" mode="normal"/>
						</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="SubSet[@Name='subClass']" mode="normal">
		<xsl:if test="count(Row)>0">
			<ul class="category">
				<xsl:for-each select="Row">
						<li>
								<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!=''">
									<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/>&#160;</span>
								</xsl:if>
								<a>
									<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;amp;mode=C</xsl:attribute>
									<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span>
								</a>
					<xsl:apply-templates select="SubSet[@Name='subClass']" mode="normal"/>
						</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Source']"> 
		<ul class="topcategory">
				<xsl:for-each select="Row">
					<li>
							<a>
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;amp;mode=S</xsl:attribute>
								<xsl:choose>
									<xsl:when test="T_SOURCE/ALIAS != ''">
										<xsl:value-of select="T_SOURCE/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="T_SOURCE/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
							</a>
							<br/>
							<xsl:choose>
								<xsl:when test="T_SOURCE/URL!=''">
									<a>
										<xsl:attribute name="href"><xsl:value-of select="T_SOURCE/URL"/></xsl:attribute>
										<xsl:attribute name="target">_new</xsl:attribute>
										<span class="head0">Link to legal text</span>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<span class="head0">Link to legal text</span>
								</xsl:otherwise>
							</xsl:choose>				
							<br/>
							<xsl:if test="PARENT_SOURCE/PK_SOURCE_ID != ''">
									<span class="head0">Parent legislative instrument: </span>
									<a>
										<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PARENT_SOURCE/PK_SOURCE_ID"/>&amp;amp;mode=S</xsl:attribute>
										<xsl:choose>
											<xsl:when test="PARENT_SOURCE/ALIAS != ''">
												<xsl:value-of select="PARENT_SOURCE/ALIAS"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="PARENT_SOURCE/TITLE"/>
											</xsl:otherwise>
										</xsl:choose>
									</a>
							</xsl:if>
					</li>
				</xsl:for-each>
		</ul>
	</xsl:template>

</xsl:stylesheet>
