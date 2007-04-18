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
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem">
	<a><xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;amp;mode=C</xsl:attribute>
		Legislative instruments
	</a>
 </div>
 <div class="breadcrumbitemlast">Hierarchy</div>
 <div class="breadcrumbtail"></div>
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
			<div id="operations">
				<ul>
					<li class="help"><a href="javascript:openViewHelp('HELP_HIERARCHY')">Page help</a></li>
					<xsl:if test="contains($permissions, ',/Admin/Helptext:u,')='true'">
						<li class="help"><a href="javascript:openHelp('HELP_HIERARCHY')">Edit help text</a></li>
					</xsl:if>
					<xsl:if test="contains($permissions, ',/Admin:v,')='true'">
						<li>
							<a>
								<xsl:attribute name="href">history.jsp?item_type=L</xsl:attribute>
								Deleted records
							</a>
						</li>
					</xsl:if>
					<xsl:if test="contains($permissions, ',/instruments:i,')='true'">
						<li>
							<a>
								<xsl:attribute name="href">source.jsv?id=-1</xsl:attribute>
								New instrument
							</a>
						</li>
					</xsl:if>	
				</ul>
			</div>
	   <h1>Legislative instruments</h1>
	   <div id="hierarchy">
		   <div class="main">
			 <div class="eurlex">(Eur-lex categories)</div>
			<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!='' or T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
				<div class="class_name">
					<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!=''">
						<xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/>&#160;
					</xsl:if>
					<xsl:if test="T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">		
						<xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/><br/>
						<a>
							<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/>&amp;amp;mode=C</xsl:attribute>
							One level up
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
	    </div>
	</xsl:template>

	<xsl:template match="SubSet[@Name='subClass']" mode="heading">
		<xsl:if test="count(Row)>0">
			<ul class="topcategory">
				<xsl:for-each select="Row">
						<li>
								<a>
									<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;amp;mode=C</xsl:attribute>
									<xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/>
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
									<xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/>&#160;
								</xsl:if>
								<a>
									<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;amp;mode=C</xsl:attribute>
									<xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/>
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
										<span class="normal_weight"><xsl:value-of select="T_SOURCE/ALIAS"/></span>
									</xsl:when>
									<xsl:otherwise>
										<span class="normal_weight"><xsl:value-of select="T_SOURCE/TITLE"/></span>
									</xsl:otherwise>
								</xsl:choose>
							</a>
							<br/>
							<xsl:choose>
								<xsl:when test="T_SOURCE/URL!=''">
									<a>
										<xsl:attribute name="href"><xsl:value-of select="T_SOURCE/URL"/></xsl:attribute>
										Link to legal text
									</a>
								</xsl:when>
								<xsl:otherwise>
									Link to legal text
								</xsl:otherwise>
							</xsl:choose>				
							<br/>
							<xsl:if test="PARENT_SOURCE/PK_SOURCE_ID != ''">
									Parent legislative instrument: 
									<a>
										<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PARENT_SOURCE/PK_SOURCE_ID"/>&amp;amp;mode=S</xsl:attribute>
										<xsl:choose>
											<xsl:when test="PARENT_SOURCE/ALIAS != ''">
												<span class="normal_weight"><xsl:value-of select="PARENT_SOURCE/ALIAS"/></span>
											</xsl:when>
											<xsl:otherwise>
												<span class="normal_weight"><xsl:value-of select="PARENT_SOURCE/TITLE"/></span>
											</xsl:otherwise>
										</xsl:choose>
									</a>
							</xsl:if>
					</li>
				</xsl:for-each>
		</ul>
	</xsl:template>

</xsl:stylesheet>
