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

	<xsl:variable name="rora">
		<xsl:value-of select="substring(substring-after(/XmlData/xml-query-string,'mode='),1,1)"/>
	</xsl:variable>

	<xsl:variable name="historyMode">
		<xsl:if test="$rora='R'">O</xsl:if>
		<xsl:if test="$rora='A'">A</xsl:if>
		<xsl:if test="$rora='B'">A</xsl:if>
	</xsl:variable>

	<xsl:variable name="analysisMode">
		<xsl:value-of select="substring(substring-after(/XmlData/xml-query-string,'anmode='),1,1)"/>
	</xsl:variable>

	<xsl:variable name="analysisText">
		<xsl:if test="$analysisMode='C'">EEA Core set of indicators</xsl:if>
		<xsl:if test="$analysisMode='P'">EIONET Priority Data flows</xsl:if>
		<xsl:if test="$analysisMode='O'">Delivery process or content overlaps with another obligation</xsl:if>
		<xsl:if test="$analysisMode='F'">Flagged</xsl:if>
	</xsl:variable>

	<xsl:variable name="sel_country">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Country_equals" />
	</xsl:variable>

	<xsl:variable name="sel_issue">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Environmental_issue_equals" />
	</xsl:variable>

	<xsl:variable name="sel_client">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Reporting_client_equals" />
	</xsl:variable>

	<xsl:variable name="sortorder">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Sort_order" />
	</xsl:variable>

	<xsl:template match="XmlData">
	<script lang="Javascript">
	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
		//alert(fld);
	}
	</script>

	<!-- context bar -->
	<xsl:if test="$printmode='N'">
      <table cellspacing="0" cellpadding="0" width="621" border="0">
			<tr>
         	<td align="bottom" width="20" background="images/bar_filled.jpg" height="25"> </td>
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
					<xsl:choose>
					<xsl:when test="$rora='A' or $rora='B'">
						<a href="rorabrowse.jsv?mode=A"><span class="barfont">Reporting obligations</span></a>
					</xsl:when>
					<xsl:otherwise>
						<a href="rorabrowse.jsv?mode=R"><span class="barfont">Reporting obligation</span></a>
					</xsl:otherwise>
				</xsl:choose>
			</td>
          <td valign="bottom" width="28"><img src="images/bar_dot.jpg"/></td>
					</tr>
				</table>
			</td></tr>
			<tr><td></td></tr>
		</table>
		</xsl:if>
		<!-- page -->
		<div style="margin-left:13">

		<br/>
		<table cellspacing="0" border="0" width="602">
			<tr valign="top">
			<td width="76%">
				<span class="head1">Reporting obligations<xsl:if test="$analysisMode!=''">: <xsl:value-of select="$analysisText"/>	</xsl:if>
								<xsl:if test="$sel_country!=''">: <xsl:value-of select="$sel_country"/></xsl:if>
								<xsl:if test="$sel_client!=''">	: <xsl:value-of select="$sel_client"/></xsl:if>
								<xsl:if test="$sel_issue!=''">
									<xsl:if test="$sel_country='' and $sel_client=''">:</xsl:if>
										<xsl:if test="$sel_country!=$sel_issue and $sel_client!=$sel_issue"> <!-- Quickfix -->
											[<xsl:value-of select="$sel_issue"/>]
										</xsl:if>
								</xsl:if>
					<br/><br/>
				</span>

			</td>
		<td>
			<table width="100%">
			<tr><td>
				<xsl:call-template name="Print"/>
			</td></tr>
			<xsl:if test="$printmode='N'">
			<tr>
				<td align="center">
				<xsl:if test="$admin='true'">
					<xsl:attribute name="bgcolor">#A0A0A0</xsl:attribute>
					<xsl:attribute name="style">BORDER: #000000 1px solid;</xsl:attribute>
					<b><font color="#FFFFFF">Actions</font></b><br/><br/>
				</xsl:if>

				<xsl:if test="contains($permissions, ',/Admin:v,')">
					<a>
						<xsl:attribute name="href">javascript:openActionTypeHistory("D","O' OR ITEM_TYPE='A" )</xsl:attribute>
						<img src="images/showdeleted.png" alt="Show history of deleted records" border="0"/>
					</a><br/>
				</xsl:if>
				<xsl:if test="contains($permissions, ',/obligations:i,')">
					<a>
						<xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=X</xsl:attribute>
						<img src="images/newobligation.png" alt="Create a new reporting obligation" border="0"/>
					</a><br/>
				</xsl:if>
			</td>
			</tr>
			</xsl:if>
			</table>
			</td>
			</tr>
		</table>

<!-- Search filters -->
		<xsl:choose>
			<xsl:when test="$printmode='Y'">
			</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="RASearch"/>
		</xsl:otherwise>
		</xsl:choose>

		<xsl:variable name="recCount">
			<xsl:choose>
				<xsl:when test="$analysisMode != ''">
					<xsl:choose>
						<xsl:when test="$analysisMode='C'"><xsl:value-of select="count(child::RowSet[@Name='Search results']/Row/T_OBLIGATION[EEA_CORE='1'])"/></xsl:when>
						<xsl:when test="$analysisMode='P'"><xsl:value-of select="count(child::RowSet[@Name='Search results']/Row/T_OBLIGATION[EEA_PRIMARY='1'])"/></xsl:when>
						<xsl:when test="$analysisMode='O'"><xsl:value-of select="count(child::RowSet[@Name='Search results']/Row/T_OBLIGATION[string-length(normalize-space(OVERLAP_URL)) > 0])"/></xsl:when>
						<xsl:when test="$analysisMode='F'"><xsl:value-of select="count(child::RowSet[@Name='Search results']/Row/T_OBLIGATION[FLAGGED='1'])"/></xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="count(child::RowSet[@Name='Search results']/Row/T_OBLIGATION) + count(child::RowSet[@Name='CCClients']/Row/T_OBLIGATION)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<div class="smallfont" style="font-size: 8pt; font-weight: bold">[<xsl:value-of select="$recCount"/> record(s) returned]</div><br/>

		<!-- header -->
		<TABLE cellSpacing="0" cellPadding="5" width="600" border="0">

		<TR>
		<xsl:if test="$rora='A'">
			<td style="BORDER-TOP: #008080 1px solid;  BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="3%" bgColor="#ffffff">&#160;</td>
			<TD style="BORDER-TOP: #008080 1px solid;  BORDER-RIGHT: #C0C0C0 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="39%" bgColor="#ffffff">

			<TABLE cellSpacing="0" width="100%" border="0">
			<TR>
				<TD><SPAN class="headsmall"><B>
					<font title="Title of the reporting obligation" face="Verdana" size="1">
						<xsl:call-template name="Sorter">
							<xsl:with-param name="order"><xsl:value-of select="$sortorder"/></xsl:with-param><xsl:with-param name="field">T_OBLIGATION.TITLE</xsl:with-param>
						</xsl:call-template>
						Reporting obligation
					</font>
				</B></SPAN>
				</TD>
				<TD> <P align="right"><MAP name="FPMap1"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" 
							href="javascript:setOrder('T_OBLIGATION.TITLE DESC')"/><AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('T_OBLIGATION.TITLE')"/></MAP>
							<IMG height="22"  src="images/arrows.gif" width="17" useMap="#FPMap1"  border="0"/></P>
				</TD>
			</TR>
			</TABLE>
			</TD>
		</xsl:if>

		<TD style="BORDER-RIGHT: #C0C0C0 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="39%" bgColor="#ffffff">

				<TABLE cellSpacing="0" width="100%" border="0">
					<TBODY>
							<TR>
								<TD><SPAN class="headsmall"><B>
								<font title="Title of the Legislative instrument" face="Verdana" size="1">
									<xsl:call-template name="Sorter">
										<xsl:with-param name="order"><xsl:value-of select="$sortorder"/></xsl:with-param><xsl:with-param name="field">T_SOURCE.TITLE</xsl:with-param>
									</xsl:call-template>
									Legislative instrument
								</font>
								</B></SPAN>
								</TD>
								<TD> <P align="right"><MAP name="FPMap3"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" 
											href="javascript:setOrder('T_SOURCE.TITLE DESC')"/><AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('T_SOURCE.TITLE')"/></MAP>
											<IMG height="22"  src="images/arrows.gif" width="17" useMap="#FPMap3"  border="0"/></P>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
				<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="19%" bgColor="#ffffff">

						<TABLE cellSpacing="0" width="100%" border="0">
						<TBODY>
							<TR>
								<TD>
									<SPAN class="headsmall"><B>
									<font title="Reporting client" face="Verdana" size="1">
										<xsl:call-template name="Sorter">
											<xsl:with-param name="order"><xsl:value-of select="$sortorder"/></xsl:with-param><xsl:with-param name="field">T_CLIENT.CLIENT_ACRONYM</xsl:with-param>
										</xsl:call-template>
										Report to
									</font>
									</B></SPAN>
								</TD>
								<TD> <P align="right"><MAP name="FPMap4"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" 
										href="javascript:setOrder('T_CLIENT.CLIENT_ACRONYM, T_CLIENT.CLIENT_NAME DESC')"/><AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('T_CLIENT.CLIENT_ACRONYM, T_CLIENT.CLIENT_NAME')"/></MAP>
									<IMG height="22"  src="images/arrows.gif" width="17" useMap="#FPMap4"  border="0"/></P>
								</TD>
							</TR>
						</TBODY>
						</TABLE>
					</TD>
				</TR>
				<tr>
					<td colspan="4" style="background-color: white; border-left: #008080 1px solid; border-right: #008080 1px solid; border-bottom: #008080 1px solid;"><span class="headsmall">Sort by the columns using the arrows. Sorted column title is highlighted.</span></td>
				</tr>

				<xsl:apply-templates select="RowSet[@Name='Search results']"/>
				<xsl:apply-templates select="RowSet[@Name='CCClients']"/>

			</TABLE>
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/>:<xsl:value-of select="ISSUE_NAME"/></xsl:attribute>
				<xsl:value-of select="ISSUE_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Client']">
		<xsl:for-each select="Row/T_CLIENT">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_CLIENT_ID"/>:<xsl:value-of select="CLIENT_NAME"/></xsl:attribute>
			<xsl:value-of select="CLIENT_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Row">
		<!--ra-->
		<xsl:if test="$rora='A'">
		<td style="BORDER-LEFT: #008080 1px solid;  BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"><img src="images/diamlil.gif" alt=""/></td>
		<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
			<SPAN class="head0n">
				<A> 
					<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_OBLIGATION/FK_SOURCE_ID"/>&amp;mode=A</xsl:attribute>
					<span class="rowitem">
							<xsl:choose>
								<xsl:when test="T_OBLIGATION/TITLE !=''">
									<xsl:value-of select="T_OBLIGATION/TITLE"/>
								</xsl:when>
								<xsl:otherwise>
									Reporting Obligation
								</xsl:otherwise>
							</xsl:choose>
					</span>
				</A>
				<xsl:if test="T_OBLIGATION/TERMINATE = 'Y'"><span class="smallfont" style="color:red"> [terminated]</span></xsl:if>
			</SPAN>&#160;
		</TD>
		</xsl:if>
		<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
			<SPAN class="head0n">
				<A> 
					<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
					<span class="rowitem"><xsl:value-of select="T_SOURCE/TITLE"/></span>
				</A>
			</SPAN>
		</TD>
		<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
			<SPAN class="head0n">
					<span class="rowitem">
					<a title="{T_CLIENT/CLIENT_NAME}"> 
						<xsl:attribute name="href">javascript:openPopup('client.jsv','id=<xsl:value-of select="T_OBLIGATION/FK_CLIENT_ID"/>')</xsl:attribute>
						<span class="rowitem">
							<xsl:choose>
								<xsl:when test="T_CLIENT/CLIENT_ACRONYM != ''"><xsl:value-of select="T_CLIENT/CLIENT_ACRONYM"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="T_CLIENT/CLIENT_NAME"/></xsl:otherwise>
							</xsl:choose>
						</span>
					</a>
					</span>
			</SPAN>
			&#160;
		</TD>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Search results']">
		<xsl:choose>
			<xsl:when test="$analysisMode!=''">
				<xsl:choose>
					<xsl:when test="$analysisMode='C'">
						<xsl:for-each select="Row[T_OBLIGATION/EEA_CORE='1']">
							<tr>
								<xsl:attribute name="bgColor"><xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$analysisMode='P'">
						<xsl:for-each select="Row[T_OBLIGATION/EEA_PRIMARY='1']">
							<tr>
								<xsl:attribute name="bgColor"><xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$analysisMode='O'">
						<xsl:for-each select="Row[string-length(normalize-space(T_OBLIGATION/OVERLAP_URL)) > 0]">
							<tr>
								<xsl:attribute name="bgColor"><xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$analysisMode='F'">
						<xsl:for-each select="Row[T_OBLIGATION/FLAGGED='1']">
							<tr>
								<xsl:attribute name="bgColor"><xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="count(Row)=0">			
						<tr><td><xsl:call-template name="nofound"/></td></tr>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each select="Row">
							<tr>
								<xsl:attribute name="bgColor"><xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="RowSet[@Name='CCClients']">
		<xsl:if test="count(Row) != 0">
			<tr><td colspan="4">&#160;</td></tr>
			<tr>
				<td style="BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top" colspan="4">
					<span class="head1">Indirect reporting obligations</span>&#160;<br/>
				</td>
			</tr>
			<xsl:for-each select="Row">
				<TR>
					<xsl:attribute name="bgColor">
						<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
					</xsl:attribute>
					<xsl:if test="$rora='A'">
					<td style="BORDER-LEFT: #008080 1px solid; BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"><img src="images/diamlil.gif" alt=""/></td>
					<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
						<SPAN class="head0n">
							<A> 
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_OBLIGATION/FK_SOURCE_ID"/>&amp;mode=A</xsl:attribute>
								<span class="rowitem">
										<xsl:choose>
											<xsl:when test="T_OBLIGATION/TITLE !=''">
												<xsl:value-of select="T_OBLIGATION/TITLE"/>
											</xsl:when>
											<xsl:otherwise>
												Reporting Obligation
											</xsl:otherwise>
										</xsl:choose>
								</span>
							</A>
						<xsl:if test="T_OBLIGATION/TERMINATE = 'Y'"><span class="smallfont" style="color:red"> [terminated]</span></xsl:if>
						</SPAN>&#160;
					</TD>
					</xsl:if>
					<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
						<SPAN class="head0n">
							<A> 
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
								<span class="rowitem"><xsl:value-of select="T_SOURCE/TITLE"/></span>
							</A>
						</SPAN>
					</TD>
					<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
						<SPAN class="head0n">
							<span class="rowitem">
							<a title="{T_CLIENT/CLIENT_NAME}"> 
								<xsl:attribute name="href">javascript:openPopup('client.jsv','id=<xsl:value-of select="T_OBLIGATION/FK_CLIENT_ID"/>')</xsl:attribute>
								<span class="rowitem">
									<xsl:choose>
										<xsl:when test="T_CLIENT/CLIENT_ACRONYM != ''"><xsl:value-of select="T_CLIENT/CLIENT_ACRONYM"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="T_CLIENT/CLIENT_NAME"/></xsl:otherwise>
									</xsl:choose>
								</span>
							</a>
							</span>
						</SPAN>
						&#160;
					</TD>
				</TR>
				</xsl:for-each>
			</xsl:if>
	</xsl:template>
	<!-- KL 031027 -->


	<xsl:template match="RowSet[@Name='Search results']/@*">
		<xsl:if test="name(.)!='Name' and name(.)!='order' and name(.)!='auth'">
			<tr><td>
				<xsl:value-of select="translate(name(.),'_',' ')"/>&amp;<b><xsl:value-of select="."/></b>
			</td></tr>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
