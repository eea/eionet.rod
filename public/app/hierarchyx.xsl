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
	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source hierarchy']"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source hierarchy']/Row">
	<!-- context bar -->
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem">
    <a><xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=X</xsl:attribute>
        Legislative instruments
    </a>
 </div>
 <div class="breadcrumbitemlast">Hierarchy</div>
 <div class="breadcrumbtail">&#160;</div>
</div>

		<!-- page -->
		<div id="workarea">
		<!-- page title -->
	   <table width="610" border="0">
		 <tr><td valign="top" width="76%">
			<span class="head1">Select legislative instrument to add reporting obligation to</span></td>
		<td align="left" valign="top">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_HIERARCHY_ADD</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td></tr></table>

	   <br/>
	   <table cellspacing="4" cellpadding="0" width="606" style="border: 1px solid #006666" bgcolor="#FFFFFF"><tr><td>

		<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!='' or T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
		   <table cellspacing="6">
		   	<tr>
				   <td>
						<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!=''">
							<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/></span>&#160;
						</xsl:if>
						<xsl:if test="T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">		
							<a>
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/>&amp;mode=X</xsl:attribute>
								<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span>
							</a>
						</xsl:if>
					</td>
				</tr>
			</table>
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
		
		</td></tr></table>

		</div>
	</xsl:template>

	<xsl:template match="SubSet[@Name='subClass']" mode="heading">
		<xsl:if test="count(Row)>0">
			<div style="margin-left:44px">
				<xsl:for-each select="Row">
					<table cellspacing="3">
						<tr>
							<td width="10pts">
								<img src="images/diamlil.gif" vspace="4"/>
							</td>
							<td>
								<a>
									<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;mode=X</xsl:attribute>
									<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span>
								</a>
							</td>
						</tr>
					</table>
					<xsl:apply-templates select="SubSet[@Name='subClass']" mode="normal"/>
				</xsl:for-each>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="SubSet[@Name='subClass']" mode="normal">
		<xsl:if test="count(Row)>0">
			<div style="margin-left:44px">
				<xsl:for-each select="Row">
					<table cellspacing="3">
						<tr>
							<td>
								<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!=''">
									<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/>&#160;</span>
								</xsl:if>
								<a>
									<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;mode=X</xsl:attribute>
									<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span>
								</a>
							</td>
						</tr>
					</table>
					<xsl:apply-templates select="SubSet[@Name='subClass']" mode="normal"/>
				</xsl:for-each>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Source']"> 
		<div style="margin-left:44px">
			<table cellspacing="6">
				<xsl:for-each select="Row">
					<tr valign="top">
						<td width="10"><img src="images/diamlil.gif" vspace="4"/></td>
						<td colspan="2">
							<a>
								<xsl:attribute name="href">activity.jsv?id=-1&amp;aid=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/></xsl:attribute>
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
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</div>
	</xsl:template>

</xsl:stylesheet>
