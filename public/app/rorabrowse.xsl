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
		Reporting obligations
	</xsl:variable>
	
	<xsl:variable name="col_class">
		twocolumns
	</xsl:variable>
	
	<xsl:include href="ncommon.xsl"/>

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
	<xsl:variable name="country_param">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Country_param" />
	</xsl:variable>


	<xsl:variable name="sel_issue">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Environmental_issue_equals" />
	</xsl:variable>
	<xsl:variable name="issue_param">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Environmental_issue_param" />
	</xsl:variable>


	<xsl:variable name="sel_client">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Reporting_client_equals" />
	</xsl:variable>
	<xsl:variable name="client_param">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Reporting_client_param" />
	</xsl:variable>
	<xsl:variable name="terminated_param">
		<xsl:value-of select="//RowSet[@Name='Search results']/@terminated_param" />
	</xsl:variable>



	<xsl:variable name="sortorder">
		<xsl:value-of select="//RowSet[@Name='Search results']/@Sort_order" />
	</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
	<xsl:choose>
	<xsl:when test="$rora='A' or $rora='B'">
		<div class="breadcrumbitemlast">Reporting obligations</div>
	</xsl:when>
	<xsl:otherwise>
		<div class="breadcrumbitemlast">Reporting obligation</div>
	</xsl:otherwise>
	</xsl:choose>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

	<xsl:template match="XmlData">
	<script type="text/javascript">
	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
		//alert(fld);
	}
	</script>
	<!-- page -->
	<div id="workarea">
		<xsl:if test="$admin='true'">
			<div id="operations">
				<ul>
				<xsl:if test="contains($permissions, ',/Admin:v,')">
					<li><a title="Show history of deleted records">
						<xsl:attribute name="href">history.jsp?item_type=O</xsl:attribute>
						Show deleted</a></li>
				</xsl:if>
				<xsl:if test="contains($permissions, ',/obligations:i,')">
					<li><a title="Create a new reporting obligation">
						<xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=X</xsl:attribute>
						New obligation</a></li>
				</xsl:if>
				<!--xsl:if test="contains($permissions, ',/obligations:u,')">
					<li><a title="Undo list">
						<xsl:attribute name="href">versions.jsp?id=-1</xsl:attribute>
						Undo</a></li>
				</xsl:if-->
				</ul>
			</div>
		</xsl:if>
				<h1>Reporting obligations<xsl:if test="$analysisMode!=''">: <xsl:value-of select="$analysisText"/>	</xsl:if>
								<xsl:if test="$sel_country!=''">: <xsl:value-of select="$sel_country"/></xsl:if>
								<xsl:if test="$sel_client!=''">	: <xsl:value-of select="$sel_client"/></xsl:if>
								<xsl:if test="$sel_issue!=''">
									[<xsl:value-of select="$sel_issue"/>]
								</xsl:if>
				</h1>
<!-- Search filters -->
			<xsl:call-template name="RASearch">
				<xsl:with-param name="sel_country"><xsl:value-of select="$country_param"/></xsl:with-param>
				<xsl:with-param name="terminated"><xsl:value-of select="$terminated_param"/></xsl:with-param>
			</xsl:call-template>

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
		<table style="table-layout:fixed; width:100%" class="sortable">
			<xsl:if test="$rora='A'">
				<xsl:choose>
				<xsl:when test="$analysisMode='P'">
					<col style="width:36%"/>
					<col style="width:36%"/>
					<col style="width:10%"/>
					<col style="width:10%"/>
					<col style="width:8%"/>
				</xsl:when>
				<xsl:otherwise>
					<col style="width:40%"/>
					<col style="width:40%"/>
					<col style="width:10%"/>
					<col style="width:10%"/>
				</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<thead>
				<tr>
				<xsl:if test="$rora='A'">
					<xsl:call-template name="createSortable">
						<xsl:with-param name="title" select="'Title of the reporting obligation'"/>
						<xsl:with-param name="text" select="'Reporting obligation'"/>
						<!--xsl:with-param name="class" select="'leftcell'"/-->
						<xsl:with-param name="sorted" select="'T_OBLIGATION.TITLE'"/>
						<xsl:with-param name="width" select="'40%'"/>
						<xsl:with-param name="cur_sorted" select="$sortorder"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="createSortable">
					<xsl:with-param name="title" select="'Title of the Legislative instrument'"/>
					<xsl:with-param name="text" select="'Legislative instrument'"/>
					<xsl:with-param name="sorted" select="'T_SOURCE.TITLE'"/>
					<xsl:with-param name="width" select="'40%'"/>
					<xsl:with-param name="cur_sorted" select="$sortorder"/>
				</xsl:call-template>
				<xsl:call-template name="createSortable">
					<xsl:with-param name="title" select="'Reporting client'"/>
					<xsl:with-param name="text" select="'Report to'"/>
					<xsl:with-param name="sorted" select="'T_CLIENT.CLIENT_NAME'"/>
					<xsl:with-param name="width" select="'10%'"/>
					<xsl:with-param name="cur_sorted" select="$sortorder"/>
				</xsl:call-template>
				<xsl:call-template name="createSortable">
					<xsl:with-param name="title" select="'Date of delivery'"/>
					<xsl:with-param name="text" select="'Deadline'"/>
					<xsl:with-param name="sorted" select="'NEXT_REPORTING, NEXT_DEADLINE'"/>
					<xsl:with-param name="width" select="'10%'"/>
					<xsl:with-param name="cur_sorted" select="$sortorder"/>
				</xsl:call-template>
				<xsl:if test="$analysisMode='P'">
					<th scope="col" title="Show deliveries in the repository" width="8%"><span>Deliveries</span></th>
				</xsl:if>
				</tr>
			</thead>
			<xsl:apply-templates select="RowSet[@Name='Search results']"/>
			<xsl:apply-templates select="RowSet[@Name='CCClients']"/>

		</table>
	</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/>:<xsl:value-of select="ISSUE_NAME"/></xsl:attribute>
				<xsl:if test="concat(PK_ISSUE_ID,  ':', ISSUE_NAME)=$issue_param">
					<xsl:attribute name="selected">selected</xsl:attribute>
				</xsl:if>
				<xsl:value-of select="ISSUE_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Client']">
		<xsl:for-each select="Row/T_CLIENT">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_CLIENT_ID"/>:<xsl:value-of select="CLIENT_NAME"/></xsl:attribute>
				<xsl:if test="concat(PK_CLIENT_ID,  ':',CLIENT_NAME)=$client_param">
					<xsl:attribute name="selected">selected</xsl:attribute>
				</xsl:if>
			<xsl:value-of select="CLIENT_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Row">
		<!--ra-->
		<xsl:if test="$rora='A'">
		<td>
				<a> 
					<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;mode=A</xsl:attribute>
							<xsl:choose>
								<xsl:when test="T_OBLIGATION/TITLE !=''">
									<xsl:value-of select="T_OBLIGATION/TITLE"/>
								</xsl:when>
								<xsl:otherwise>
									Reporting Obligation
								</xsl:otherwise>
							</xsl:choose>
				</a>
				<xsl:if test="T_OBLIGATION/TERMINATE = 'Y'"><span class="smallfont" style="color: red"> [terminated]</span></xsl:if>
		</td>
		</xsl:if>
		<td>
				<a> 
					<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
					<xsl:value-of select="T_SOURCE/TITLE"/>
				</a>
		</td>
		<td>
					<a title="{T_CLIENT/CLIENT_NAME}"> 
						<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_OBLIGATION/FK_CLIENT_ID"/></xsl:attribute>
							<xsl:choose>
								<xsl:when test="T_CLIENT/CLIENT_ACRONYM != ''"><xsl:value-of select="T_CLIENT/CLIENT_ACRONYM"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="T_CLIENT/CLIENT_NAME"/></xsl:otherwise>
							</xsl:choose>
					</a>
		</td>
		<td>
			<xsl:attribute name="title"><xsl:value-of select="T_OBLIGATION/DEADLINE"/></xsl:attribute>
			<xsl:choose>
			<xsl:when test="T_OBLIGATION/NEXT_DEADLINE=''">
				<xsl:attribute name="style">color:#006666</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="style">color:#000000</xsl:attribute>
			</xsl:otherwise>			
			</xsl:choose>

			<xsl:call-template name="short">
				<xsl:with-param name="text" select="T_OBLIGATION/DEADLINE"/>
				<xsl:with-param name="length">10</xsl:with-param>
			</xsl:call-template>
		
			<!--xsl:value-of select="T_OBLIGATION/DEADLINE"/-->

		</td>
		<xsl:if test="$analysisMode='P'">
			<td>
				<xsl:choose>
					<xsl:when test="string-length(T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS) &gt; 0">
						<a><xsl:attribute name="href">csdeliveries?ACT_DETAILS_ID=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;COUNTRY_ID=%%</xsl:attribute>
						Show list
						</a>
					</xsl:when>
					<xsl:otherwise>
						None
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</xsl:if>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Search results']">
		<xsl:choose>
			<xsl:when test="$analysisMode!=''">
				<xsl:choose>
					<xsl:when test="$analysisMode='C'">
						<xsl:for-each select="Row[T_OBLIGATION/EEA_CORE='1']">
							<tr>
								<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$analysisMode='P'">
						<xsl:for-each select="Row[T_OBLIGATION/EEA_PRIMARY='1']">
							<tr>
								<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$analysisMode='O'">
						<xsl:for-each select="Row[string-length(normalize-space(T_OBLIGATION/OVERLAP_URL)) > 0]">
							<tr>
								<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$analysisMode='F'">
						<xsl:for-each select="Row[T_OBLIGATION/FLAGGED='1']">
							<tr>
								<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
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
								<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
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
			<tr><td colspan="4" style="border:0">&#160;</td></tr>
			<tr>
				<td style="border-bottom: #c0c0c0 1px solid" colspan="4">
					<span class="head1">Indirect reporting obligations</span>&#160;<br/>
				</td>
			</tr>
			<xsl:for-each select="Row">
				<tr>
					<xsl:attribute name="class">
						<xsl:if test="position() mod 2 = 0">even</xsl:if>
					</xsl:attribute>
					<xsl:if test="$rora='A'">
					<!--td style="border-left: #008080 1px solid; border-right: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid"><img src="images/diamlil.gif" alt=""/></td-->
					<td>
							<a> 
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;mode=A</xsl:attribute>
								<xsl:choose>
									<xsl:when test="T_OBLIGATION/TITLE !=''">
										<xsl:value-of select="T_OBLIGATION/TITLE"/>
									</xsl:when>
									<xsl:otherwise>
										Reporting Obligation
									</xsl:otherwise>
								</xsl:choose>
							</a>
						<xsl:if test="T_OBLIGATION/TERMINATE = 'Y'"><span class="smallfont" style="color:red"> [terminated]</span></xsl:if>
					</td>
					</xsl:if>
					<td>
							<a> 
								<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
								<xsl:value-of select="T_SOURCE/TITLE"/>
							</a>
					</td>
					<td>
						<a title="{T_CLIENT/CLIENT_NAME}"> 
							<xsl:attribute name="href">client.jsv?id=<xsl:value-of select="T_OBLIGATION/FK_CLIENT_ID"/></xsl:attribute>
							<xsl:choose>
								<xsl:when test="T_CLIENT/CLIENT_ACRONYM != ''"><xsl:value-of select="T_CLIENT/CLIENT_ACRONYM"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="T_CLIENT/CLIENT_NAME"/></xsl:otherwise>
							</xsl:choose>
						</a>
					</td>
					<td>
						<xsl:attribute name="title"><xsl:value-of select="T_OBLIGATION/DEADLINE"/></xsl:attribute>
						<xsl:choose>
							<xsl:when test="T_OBLIGATION/NEXT_DEADLINE=''">
								<xsl:attribute name="style">color:#006666</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="style">color:#000000</xsl:attribute>
							</xsl:otherwise>			
						</xsl:choose>
						<xsl:call-template name="short">
							<xsl:with-param name="text" select="T_OBLIGATION/DEADLINE"/>
							<xsl:with-param name="length">10</xsl:with-param>
						</xsl:call-template>
					</td>
					<xsl:if test="$analysisMode='P'">
						<td>
							<xsl:choose>
								<xsl:when test="string-length(T_OBLIGATION/FK_DELIVERY_COUNTRY_IDS) &gt; 0">
									<a><xsl:attribute name="href">csdeliveries?ACT_DETAILS_ID=<xsl:value-of select="T_OBLIGATION/PK_RA_ID"/>&amp;COUNTRY_ID=%%</xsl:attribute>
									Show list
									</a>
								</xsl:when>
								<xsl:otherwise>
									None
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</xsl:if>
				</tr>
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
<!-- EK 050203 template for calculating request URL for sorting -->
	<xsl:template name="createURL">
		<xsl:param name="sorted"/>
		<xsl:variable name="uri">rorabrowse.jsv?mode=<xsl:value-of select="$rora"/></xsl:variable>
		<xsl:variable name="anmode">
			<xsl:if test="string-length($analysisMode) &gt; 0">&amp;anmode=<xsl:value-of select="$analysisMode"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="client">
			<xsl:if test="string-length($client_param) &gt; 0">&amp;client=<xsl:value-of select="$client_param"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="country">
			<xsl:if test="string-length($country_param) &gt; 0">&amp;country=<xsl:value-of select="$country_param"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="env_issue">
			<xsl:if test="string-length($issue_param) &gt; 0">&amp;env_issue=<xsl:value-of select="$issue_param"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="terminated">
			<xsl:if test="string-length($terminated_param) &gt; 0">&amp;terminated=<xsl:value-of select="$terminated_param"/></xsl:if>
		</xsl:variable>
		<xsl:variable name="ORD">
			<xsl:if test="string-length($sorted) &gt; 0">&amp;ORD=<xsl:value-of select="$sorted"/></xsl:if>
		</xsl:variable>
		
		<xsl:value-of select="concat($uri, $anmode, $client, $country, $env_issue, $terminated, $ORD)"/>
	</xsl:template>
	
	<xsl:template name="PageHelp"/>
</xsl:stylesheet>
