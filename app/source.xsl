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

	<xsl:variable name="src-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet[@Name='Source']/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source']"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source']/Row[position()=1]">
		<!-- form for delete legislation action -->
		<!--xsl:if test="$admin='true'"-->
		<xsl:if test="contains($permissions, 'X')='true'">
			<script language="JavaScript">
			<![CDATA[
function delLegislation() {
	if (confirm("Do you want to delete the current legal instrument\nwith all related reporting obligations and activities?"))
		document.f.submit();
}
			]]>
			</script>
			<form name="f" method="POST" action="source.jsv">
				<input type="hidden" name="dom-update-mode" value="D"/>
				<input type="hidden" name="/XmlData/RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID">
					<xsl:attribute name="value"><xsl:value-of select="$src-id"/></xsl:attribute>
				</input>
			</form>
		</xsl:if>
      <table cellspacing="0" cellpadding="0" width="621" border="0">
			<tr>
         	<td align="bottom" width="20" background="images/bar_filled.jpg" height="25">&#160;</td>
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
               	<td valign="bottom" align="middle" width="92"><span class="barfont">Legal instrument</span></td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	               <td valign="bottom" align="right" width="240"></td>
					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		<!-- page -->
		<div style="margin-left:13">
		<table cellspacing="7pts" border="0" width="600">
			<tr>
				<td><span class="head1">Details of legal instrument</span></td>
			</tr>
		</table>
		<table cellspacing="7pts" border="0" width="600">
		<!-- temporary field indicating, where is the data imported -->
		</table>
		<!-- classification hierarchy -->
		<table cellspacing="7pts" width="600" border="0">
			<tr>
				<td colspan="2" nowrap="true"><span class="head0">Classification:</span></td>
				<td align="right">
					<xsl:apply-templates select="SubSet[@Name='Obligation']"/>
				</td>
				<!--xsl:if test="$admin='true'"-->
				<xsl:if test="contains($permissions, 'O')='true'">
					<td align="right">
						<a><xsl:attribute name="href">reporting.jsv?id=-1&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>
						<img src="images/newobligation.png" alt="Add a new reporting obligation" border="0"/></a></td>
				</xsl:if>
			</tr>
			<xsl:apply-templates select="SubSet[@Name='Parents']"/>
		</table>

		<table cellspacing="7pts" border="0" width="600">
		<tr><td colspan="5"><hr/></td></tr>
		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Legal name:</span></td>
			<td  colspan="3">
				<xsl:choose>
					<xsl:when test="T_SOURCE/URL!=''">
						<a>
							<xsl:attribute name="href"><xsl:value-of select="T_SOURCE/URL"/></xsl:attribute>
							<xsl:attribute name="target">
								_new
							</xsl:attribute>
							<xsl:value-of select="T_SOURCE/TITLE"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="T_SOURCE/TITLE"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="right" width="10%" rowspan="3" valign="top">
				<!--xsl:if test="$admin='true'"-->
				<xsl:if test="contains($permissions, 'S')='true'">
					<a><xsl:attribute name="href">source.jsv?id=-1</xsl:attribute>
						<img src="images/newinstrument.png" alt="Add a new legal instrument" border="0"/></a><br/>
					</xsl:if>
					<xsl:if test="contains($permissions, 's')='true'">
						<a><xsl:attribute name="href">source.jsv?id=<xsl:value-of select="$src-id"/></xsl:attribute>
						<img src="images/editinstrument.png" alt="Edit legislation" border="0"/></a><br/>
						</xsl:if>
					<xsl:if test="contains($permissions, 'X')='true'">
						<a href="javascript:delLegislation()"><img src="images/deleteinstrument.png" alt="Delete legislation" border="0"/></a><br/>
				</xsl:if>
				<xsl:if test="contains($permissions, 'y')='true'">
					<a>
					<xsl:attribute name="href">javascript:openHistory('<xsl:value-of select="$src-id"/>', 'L')</xsl:attribute>
					<img src="images/showhistory.png" alt="Show history" border="0"/></a><br/>
				</xsl:if>
			</td>
		</tr>
		
		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Alias name:</span></td>
			<td colspan="3"><span class="head0"><xsl:value-of select="T_SOURCE/ALIAS"/></span></td>
		</tr>

		<xsl:apply-templates select="SubSet[@Name='Origin']"/>
		<xsl:apply-templates select="SubSet[@Name='RelatedInstruments']"/>

		<tr valign="top">
			<td nowrap="true"><span class="head0">Issued by:</span></td>
			<td colspan="3">
				<a>
				<xsl:attribute name="href">javascript:openIssuer('<xsl:value-of select="T_ISSUER/PK_ISSUER_ID"/>')</xsl:attribute>
				<xsl:value-of select="T_ISSUER/ISSUER_NAME"/>
				</a>
				<!--xsl:choose>
					<xsl:when test="T_ISSUER/ISSUER_URL!=''">
						<a>
							<xsl:attribute name="href"><xsl:value-of select="T_ISSUER/ISSUER_URL"/></xsl:attribute>
							<xsl:attribute name="target">
								_new
							</xsl:attribute>
							<xsl:value-of select="T_ISSUER/ISSUER_NAME"/></a>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="T_ISSUER/ISSUER_NAME"/>
					</xsl:otherwise>
				</xsl:choose-->
			</td>
		</tr>
		<tr valign="top">
			<td nowrap="true"><span class="head0">Valid from:</span></td>
			<td colspan="3"><xsl:value-of select="T_SOURCE/VALID_FROM"/></td>
		</tr>
		<tr valign="top">
			<td nowrap="true"><span class="head0">Abstract:</span></td>
			<td>
				<xsl:value-of select="T_SOURCE/ABSTRACT"/>
			</td>
			<td nowrap="true"><span class="head0">CELEX ref.:</span></td>
			<td>
				<xsl:value-of select="T_SOURCE/CELEX_REF"/>
			</td>
		</tr>
	<!-- convention specific fields -->
	<xsl:for-each select="SubSet[@Name='Parents']/Row/T_SOURCE_CLASS">
	<xsl:if test="CLASS_NAME = 'Conventions'">
		<tr valign="top">
			<td nowrap="true"><span class="head0">EC entry<BR/>into force (C):</span></td>
			<td><xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/EC_ENTRY_INTO_FORCE"/></td>
			<td nowrap="true"><span class="head0">EC accession (C):</span></td>
			<td><xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/EC_ACCESSION"/></td>
		</tr>
		<tr valign="top">
			<td nowrap="true"><span class="head0">Convention secretariat (C):</span></td>
			<td colspan="5">
				<xsl:choose>
					<xsl:when test="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/SECRETARIAT_URL!=''">
						<a>
							<xsl:attribute name="href"><xsl:value-of select="//RowSet[@Name='Source']/Row[position()=1]/T_SOURCE/SECRETARIAT_URL"/></xsl:attribute>
							<xsl:attribute name="target">
								_new
							</xsl:attribute>
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
		<tr><td colspan="5"><hr/></td></tr>
		<tr valign="top">
			<td nowrap="true"><span class="head0">Comment:</span></td>
			<td colspan="5">
				<xsl:value-of select="T_SOURCE/COMMENT"/>
			</td>
		</tr>
		<!-- show history link KL 021127 -->
		</table>
		</div>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Obligation']">
		<xsl:choose>
			<xsl:when test="count(Row)>0">
				<a><xsl:attribute name="href">rorabrowse.jsv?source=<xsl:value-of select="$src-id"/>&amp;mode=R</xsl:attribute>
				<span class="head0">Related reporting obligations under this instrument</span></a>
			</xsl:when>
			<xsl:otherwise><span class="head0">No reporting obligations</span></xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Parents']">
		<xsl:for-each select="Row/T_SOURCE_CLASS">
			<tr>
				<td nowrap="true">&#160;</td>
				<td nowrap="true">
				<xsl:if test="CLASSIFICATOR!=''">
					<xsl:value-of select="CLASSIFICATOR"/>&#160;
				</xsl:if>
				<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_CLASS_ID"/>&amp;mode=C</xsl:attribute>
				<span class="head0"><xsl:value-of select="CLASS_NAME"/></span></a></td>
			</tr>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="SubSet[@Name='Origin']/Row/LSOURCE">
		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Parent legal instrument:</span></td>
			<td colspan="3"><a>
				<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
				<xsl:choose>
					<xsl:when test="ALIAS != ''">
						<xsl:value-of select="ALIAS"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="TITLE"/>
					</xsl:otherwise>
				</xsl:choose>
			</a></td>
		</tr>
	</xsl:template>

	<xsl:template match="SubSet[@Name='RelatedInstruments']">
		<xsl:if test="count(Row)>0">
		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Related instrument(s):</span></td>
			<td colspan="3"><xsl:for-each select="Row/LSOURCE">
				<a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
				<xsl:choose>
					<xsl:when test="ALIAS != ''">
						<xsl:value-of select="ALIAS"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="TITLE"/>
					</xsl:otherwise>
				</xsl:choose>
				</a><br/>
			</xsl:for-each></td>
		</tr>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
