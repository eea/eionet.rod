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

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source hierarchy']"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source hierarchy']/Row">
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
               	<td valign="bottom">
							<a href="index.html"><span class="barfont">ROD</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom">
							<a>	<xsl:attribute name="href">
									show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=X
								</xsl:attribute>
								<span class="barfont">Legal instruments</span>
							</a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	               <td valign="bottom" align="right" width="2 10"></td>
					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>

		<!-- page -->
		<div style="margin-left:13">
		<!-- page title -->
	   <table cellspacing="7" border="0">
	   	<tr><td valign="top"><span class="head1">Select legal instrument to add reporting obligation to</span></td>
			<td align="right" valign="top">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_HIERARCHY_ADD</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
			<br/>
			</td></tr>
		</table>

	   <table cellspacing="7" width="600" border="0"><tr><td>
			<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!=''">
				<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/></span>&#160;
			</xsl:if>
			<xsl:choose>		
				<xsl:when test="T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
					<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/>&amp;mode=X</xsl:attribute>
					<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span></a>
				</xsl:when>
			</xsl:choose>
		</td></tr></table>
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
	</xsl:template>

	<xsl:template match="SubSet[@Name='subClass']" mode="heading">
		<xsl:if test="count(Row)>0">
		<div style="margin-left:30">
			<xsl:for-each select="Row">
				<table cellspacing="3" border="0">
					<tr><td width="10pts"><img src="images/diamlil.gif" vspace="4"/></td><td>
						<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;mode=X</xsl:attribute>
						<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span></a>
					</td></tr>
				</table>
				<xsl:apply-templates select="SubSet[@Name='subClass']" mode="normal"/>
			</xsl:for-each>
		</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="SubSet[@Name='subClass']" mode="normal">
		<xsl:if test="count(Row)>0">
		<div style="margin-left:30">
			<xsl:for-each select="Row">
				<table cellspacing="3" border="0"><tr><td>
					<xsl:if test="T_SOURCE_CLASS/CLASSIFICATOR!=''">
						<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/>&#160;</span>
					</xsl:if>
					<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE_CLASS/PK_CLASS_ID"/>&amp;mode=X</xsl:attribute>
					<span class="head0"><xsl:value-of select="T_SOURCE_CLASS/CLASS_NAME"/></span></a>
				</td></tr></table>
				<xsl:apply-templates select="SubSet[@Name='subClass']" mode="normal"/>
			</xsl:for-each>
		</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Source']"> 
		<div style="margin-left:30">
			<table cellspacing="7pts" width="600">
			<xsl:for-each select="Row">
				<tr valign="top">
					<td width="10"><img src="images/diamlil.gif" vspace="4"/></td>
					<td colspan="2"><a>
						<xsl:attribute name="href">reporting.jsv?id=-1&amp;aid=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/></xsl:attribute>
					<xsl:choose>
						<xsl:when test="T_SOURCE/ALIAS != ''">
							<xsl:value-of select="T_SOURCE/ALIAS"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_SOURCE/TITLE"/>
						</xsl:otherwise>
					</xsl:choose>
					</a><br/>
					</td>
				</tr>

			</xsl:for-each>
			</table>
		</div>
	</xsl:template>

</xsl:stylesheet>