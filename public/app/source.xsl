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

	<xsl:variable name="src-id">
		<xsl:value-of select="//RowSet[@Name='Source']/Row/T_SOURCE/PK_SOURCE_ID"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet[@Name='Source']/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source']"/>
			<xsl:call-template name="LIRORAFooter">
			<xsl:with-param name="table">LI</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source']/Row[position()=1]">
		<!-- form for delete legislation action -->
		<xsl:if test="contains($permissions, ',/instruments:d,')='true'">
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
               	<td valign="bottom">
							<a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom">
							<a href="index.html"><span class="barfont">ROD</span></a>
						</td>
   	            <td valign="bottom" width="28"><img src="images/bar_hole.jpg"/></td>
               	<td valign="bottom"><span class="barfont">Legal instrument</span></td>
   	            <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
	               <td valign="bottom" align="right" width="240"></td>
					</tr>
				</table>
			</td></tr>
			<tr><td>&#160;</td></tr>
		</table>
		<!-- page -->
		<div style="margin-left:13">
		<table cellspacing="5pts" border="0" width="600">
			<tr>
				<td><span class="head1">Details of legal instrument</span></td>
			</tr>
		</table>
		<!-- classification hierarchy -->

		<table cellspacing="3pts" width="600" border="0">
			<tr>
				<td colspan="2" nowrap="true"><span class="head0">Classification:</span></td>

					<td align="right"></td>

			</tr>

			<xsl:apply-templates select="SubSet[@Name='Parents']"/>
		</table>

		<table cellspacing="5pts" border="0" width="600">
		<tr><td colspan="3"><hr/></td></tr>

		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Legal name:</span></td>
			<td width="50%">
				<xsl:choose>
					<xsl:when test="T_SOURCE/URL!=''">
						<a>
							<xsl:attribute name="href"><xsl:value-of select="T_SOURCE/URL"/></xsl:attribute>
							<xsl:attribute name="target">_new</xsl:attribute>
							<xsl:value-of select="T_SOURCE/TITLE"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="T_SOURCE/TITLE"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>

			
			<td align="center" width="25%" rowspan="4" valign="top">

				<table><tr><td>					<xsl:call-template name="Print"/><br/><br/></td></tr>
					<tr><td align="center">
					<xsl:if test="$admin='true'">
						<xsl:attribute name="bgcolor">#A0A0A0</xsl:attribute>
						<xsl:attribute name="style">BORDER: #000000 1px solid;</xsl:attribute>
						<b><font color="#FFFFFF">Actions</font></b><br/><br/>
					</xsl:if>
				<xsl:if test="contains($permissions, ',/obligations:i,')='true'">
					<a><xsl:attribute name="href">reporting.jsv?id=-1&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>
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
						<a href="javascript:delLegislation()"><img src="images/deleteinstrument.png" alt="Delete this instrument" border="0"/></a><br/>
				</xsl:if>
				<xsl:if test="contains($permissions, ',/Admin:v,')='true'">
					<a>
					<xsl:attribute name="href">javascript:openHistory('<xsl:value-of select="$src-id"/>', 'L')</xsl:attribute>
					<img src="images/showhistory.png" alt="Show history of changes" border="0"/></a><br/>
				</xsl:if>
				</td>
				</tr>
				</table>
			</td>
		</tr>
		
		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Short name:</span></td>
			<td width="50%"><xsl:value-of select="T_SOURCE/ALIAS"/></td>
		</tr>

		<xsl:apply-templates select="SubSet[@Name='Origin']"/>

	<!-- 2nd -->


		<xsl:apply-templates select="SubSet[@Name='RelatedInstruments']"/>

		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Issued by:</span></td>
			<td width="50%">
				<a>
				<xsl:attribute name="href">javascript:openClient('<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
				<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
				</a>
			</td>
		</tr>
		<tr valign="top">
			<td nowrap="true" width="10%"><span class="head0">Issued by URL:</span></td>
			<td colspan="2">
				<a href="_new">
				<xsl:attribute name="href"><xsl:value-of select="T_SOURCE/ISSUED_BY_URL"/></xsl:attribute>
				<xsl:value-of select="T_SOURCE/ISSUED_BY_URL_LABEL"/>
				</a>
			</td>
		</tr>
		<tr valign="top">
			<td nowrap="true"><span class="head0">CELEX number:</span></td>
			<td colspan="2"><xsl:value-of select="T_SOURCE/CELEX_REF"/></td>
		</tr>
		<tr valign="top">
			<td nowrap="true"><span class="head0">Valid from:</span></td>
			<td colspan="2"><xsl:value-of select="T_SOURCE/VALID_FROM"/></td>
		</tr>
		<tr valign="top">
			<td nowrap="true"><span class="head0">Abstract:</span></td>
			<td colspan="2"><xsl:value-of select="T_SOURCE/ABSTRACT"/></td>
		</tr>

					<!-- KL 030220 Obligations -->
		<tr valign="top">
			<td width="22%">
				<span class="head0">Reporting obligations:</span><br/>
			</td>
			<td colspan="2">
				<xsl:apply-templates select="//SubSet[@Name='Obligation']"/>
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

		<tr valign="top">
			<td nowrap="true"><span class="head0">Comment:</span></td>
			<td colspan="2">
				<xsl:value-of select="T_SOURCE/COMMENT"/>
			</td>
		</tr>
		<tr><td colspan="6"><hr/></td></tr>
		</table>
		</div>
	</xsl:template>

	<xsl:template match="//SubSet[@Name='Obligation']">
		<xsl:if test="count(Row)>0">
		<table width="100%" colspan="5" border="1">
			<tr>
				<td width="60%"><span class="head0">Title</span></td>
			</tr>
			<xsl:for-each select="Row/T_REPORTING">
				<tr>
					<td>
					<a>	<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="PK_RO_ID"/>&amp;mode=R&amp;aid=<xsl:value-of select="$src-id"/></xsl:attribute>
						<xsl:choose>
							<xsl:when test="ALIAS != ''">
								<xsl:value-of select="ALIAS"/>
							</xsl:when>
							<xsl:otherwise>
								Reporting obligation
							</xsl:otherwise>
						</xsl:choose>
					</a>
					</td>
				</tr>
			</xsl:for-each>
		</table>
		</xsl:if>
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
			<td width="10%"><span class="head0">Parent legal instrument:</span></td>
			<td width="50%"><a>
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
			<td width="50%"><xsl:for-each select="Row/LSOURCE">
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
