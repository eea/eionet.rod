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

	<xsl:variable name="showfilters">
		<xsl:value-of select="substring(substring-after(/XmlData/xml-query-string,'showfilters='),1,1)"/>
	</xsl:variable>

	<xsl:variable name="rora">
		<xsl:value-of select="substring(substring-after(/XmlData/xml-query-string,'mode='),1,1)"/>
	</xsl:variable>

	<xsl:variable name="historyMode">
		<xsl:if test="$rora='R'">O</xsl:if>
		<xsl:if test="$rora='A'">A</xsl:if>
		<xsl:if test="$rora='B'">A</xsl:if>
	</xsl:variable>


		<xsl:variable name="sel_country">
			<xsl:value-of select="substring-after(substring-after(/XmlData/xml-query-string, 'country='),'%3A')"/>
		</xsl:variable>

		<xsl:variable name="sel_issue">
			<xsl:value-of select=" translate( translate(substring-before(substring-after(substring-after(/XmlData/xml-query-string, 'env_issue='),'%3A'),'&amp;'),'+',' '), '%26', '&amp;') "/>
		</xsl:variable>

	<xsl:template match="XmlData">
	<script lang="Javascript">
	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
		//alert(fld);
	}
	</script>

	<!-- context bar -->
	
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
					<xsl:when test="$rora='A'">
						<a href="rorabrowse.jsv?mode=A"><span class="barfont">Reporting obligations</span></a>
					</xsl:when>
					<xsl:when test="$rora='B'">
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

		<!-- page -->
		<div style="margin-left:13">

		<br/>
		<table cellspacing="0" border="0" width="600">
			<tr valign="bottom">
			<td width="78%">
				<span class="head1">Reporting obligations
								<xsl:if test="$sel_country!=''">
									: <xsl:value-of select="$sel_country"/>
								</xsl:if>
								<xsl:if test="$sel_issue!=''">
									<xsl:if test="$sel_country=''">:</xsl:if>
										[<xsl:value-of select="$sel_issue"/>]
								</xsl:if>
					<br/><br/>
				</span>

			</td>
		<td>
			<table>
			<tr><td>
				<xsl:call-template name="Print"/><br/>
				<xsl:if test="$showfilters=''">
					<br/>
					<a>
						<xsl:attribute name="href">javascript:window.location.replace(window.location.href+'&amp;showfilters=1')</xsl:attribute>
						<img src="images/bb_advsearch.png" alt="" border="0"/>
					</a><br/>
				</xsl:if>
			</td></tr>
			<tr><td align="center">
					<xsl:if test="$admin='true' and $showfilters=''">
						<xsl:attribute name="bgcolor">#A0A0A0</xsl:attribute>
						<xsl:attribute name="style">BORDER: #000000 1px solid;</xsl:attribute>
						<b><font color="#FFFFFF">Actions</font></b><br/><br/>
					</xsl:if>

				<xsl:if test="contains($permissions, ',/Admin:v,') and $showfilters=''">
					<a>
						<xsl:attribute name="href">javascript:openActionTypeHistory("D","O' OR ITEM_TYPE='A" )</xsl:attribute>
						<img src="images/showdeleted.png" alt="Show history of deleted records" border="0"/>
					</a><br/>
				</xsl:if>
				<xsl:if test="contains($permissions, ',/RO:i,') and $showfilters=''">
					<a>
						<xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=X</xsl:attribute>
						<img src="images/newobligation.png" alt="Create a new reporting obligation" border="0"/>
					</a><br/>
				</xsl:if>
			</td>
			</tr>
			</table>
		<!--table border="1">
			<tr><td>
				<xsl:if test="contains($permissions, ',/Admin:v,') and $showfilters=''">
					<a>
						<xsl:attribute name="href">javascript:openActionTypeHistory('D','<xsl:value-of select="$historyMode"/>')</xsl:attribute>
						<img src="images/showdeleted.png" alt="Show history of deleted records" border="0"/>
					</a><br/>
				</xsl:if>
			</td></tr>
			<tr><td>
				<xsl:if test="$showfilters=''">
					<a>
						<xsl:attribute name="href">javascript:window.location.replace(window.location.href+'&amp;showfilters=1')</xsl:attribute>
						<img src="images/bb_advsearch.png" alt="" border="0"/>
					</a><br/>
				</xsl:if>
			</td></tr>
			<tr><td>
				<xsl:if test="contains($permissions, ',/RO:i,') and $showfilters=''">
					<a>
						<xsl:attribute name="href">show.jsv?id=<xsl:call-template name="DB_Legal_Root_ID"/>&amp;mode=X</xsl:attribute>
						<img src="images/newobligation.png" alt="Create a new reporting obligation" border="0"/>
					</a>
				</xsl:if>
			</td></tr>
			</table-->


			</td>
			</tr>
		</table>

<!-- Search filters -->
		<xsl:choose>
			<xsl:when test="$showfilters=''">
			</xsl:when>
		<xsl:otherwise>
<!-- KL 030227  search form -->
		<form name="x1" method="get" action="rorabrowse.jsv">
		<table  border="0" width="600" cellspacing="0" cellpadding="2"  style="border: 1 solid #008080">
				 <tr>
						<td width="120" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">Show reporting:</span>
						</td>
						<td width="245" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For an issue</span>
						</td>
						<td width="215" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For a country</span>
						</td>
						<td width="42" bgcolor="#FFFFFF" align="right">
						<!--img onclick="javascript:openViewHelp('HELP_SEARCH')" border="0" src="images/questionmark.jpg" width="13" height="13" alt="[ HELP ]"/-->
						<xsl:call-template name="Help"><xsl:with-param name="id">HELP_SEARCH1</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
						</td>
				</tr>
				<tr>
           <td>
								<table>
									<tr><td>
										<input type="hidden" value="A" name="mode"/>
										<!--input type="radio" value="R" name="mode">
												<xsl:if test="$rora='R'">
												<xsl:attribute name="checked"/>
												</xsl:if>
										</input-->
										<span class="barfont">
											Obligations
										</span>
										
									</td></tr>
									<tr><td>
										<!--input type="radio" value="A" name="mode">
												<xsl:if test="$rora='A'">
												<xsl:attribute name="checked"/>
												</xsl:if>
										</input--><!--span class="barfont">Activities</span-->
								</td></tr>
								</table>
            </td>
            <td style="border-left: 1 solid #C0C0C0">
									<select name="env_issue" style="font-size: 8pt; color: #000000; width:240" height="20">
											<option value="-1">All issues</option>
											<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
                  </select>
						</td>
            <td style="border-left: 1 solid #C0C0C0">
										<select name="country" style="color: #000000; font-size: 8pt; width:200" size="1">
											<option value="-1">Any country</option>
											<xsl:call-template name="SpatialTemplate">
												<xsl:with-param name ="type">C</xsl:with-param>
												<xsl:with-param name ="type2"></xsl:with-param>
											</xsl:call-template>
                    </select>
								</td>
                <td align="right">
						<a>
							<xsl:attribute name="href">javascript:document.forms["x1"].submit()</xsl:attribute>
							<img src="images/go.png" alt="" border="0"/>
						</a>
					</td>
					</tr>
		</table>
		</form>
      <span class="head0">or</span>
		<form name="x2" method="get" action="rorabrowse.jsv">
		<table  border="0" width="600" cellspacing="0" cellpadding="2"  style="border: 1 solid #008080">
				 <tr>
						<td width="120" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">Show reporting:</span>
						</td>
						<td width="245" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For an issue</span>
						</td>
						<td width="215" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For an organisation</span>
						</td>
						<td width="42" bgcolor="#FFFFFF" align="right"><!--img onClick="javascript:openViewHelp('HELP_SEARCH')" border="0" src="images/questionmark.jpg" width="13" height="13" alt="[ HELP ]"/-->
						<xsl:call-template name="Help"><xsl:with-param name="id">HELP_SEARCH2</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
						</td>
				</tr>
				 <tr>
               <td>
								<table>
									<tr><td>
										<input type="hidden" value="A" name="mode"/>
										<!--input type="radio" value="R" name="mode">
												<xsl:if test="$rora='R'">
												<xsl:attribute name="checked"/>
												</xsl:if>
										</input--><span class="barfont">Obligations</span>
									</td></tr>
									<tr><td>
										<!--input type="radio" value="A" name="mode">
												<xsl:if test="$rora='A'">
												<xsl:attribute name="checked"/>
												</xsl:if>
										</input><span class="barfont">Activities</span-->
								</td></tr>
								</table>
            </td>
              <td style="border-left: 1 solid #C0C0C0">
									<select name="env_issue" style="font-size: 8pt; color: #000000; width:240" height="20">
										<option value="-1">All issues</option>
										<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
                  </select>
							</td>
              <td style="border-left: 1 solid #C0C0C0">
										<select name="client" style="color: #000000; font-size: 8pt; width:200" size="1">
											<option value="-1">Any organisation</option>
											<xsl:apply-templates select="RowSet[@Name='Client']"/>
		                </select>
								</td>
                <td align="right">
						<a>
							<xsl:attribute name="href">javascript:document.forms["x2"].submit()</xsl:attribute>
							<img src="images/go.png" alt="" border="0"/>
						</a>
					</td>
					</tr>
				</table>
				</form> <!-- search form -->

		</xsl:otherwise>
		</xsl:choose>

		<!-- header -->
		<TABLE cellSpacing="0" cellPadding="5" width="600" border="0">
		<TBODY>
		<TR>
	    <!--TD width="20"></TD-->
		<xsl:if test="$rora='A'">
			<TD style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="172" bgColor="#ffffff">

			<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Title of the reporting activity" face="Verdana" color="#000000" size="1">Reporting activity</FONT></B></SPAN>
				</TD>
				<TD> <P align="right"><MAP name="FPMap1"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" 
							href="javascript:setOrder('T_ACTIVITY.TITLE DESC')"/><AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('T_ACTIVITY.TITLE')"/></MAP>
							<IMG height="22"  src="images/arrows.gif" width="17" useMap="#FPMap1"  border="0"/></P>
				</TD>
			</TR>
			</TBODY>
			</TABLE>
			</TD>
		</xsl:if>
		<TD style="BORDER-LEFT: #008080 1px solid; BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="172" bgColor="#ffffff">

			<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Title of the reporting obligation" face="Verdana" color="#000000" size="1">Reporting obligation</FONT></B></SPAN>
				</TD>
				<TD> <P align="right"><MAP name="FPMap2"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" 
							href="javascript:setOrder('T_REPORTING.ALIAS DESC')"/><AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('T_REPORTING.ALIAS')"/></MAP>
							<IMG height="22"  src="images/arrows.gif" width="17" useMap="#FPMap2"  border="0"/></P>
				</TD>
			</TR>
			</TBODY>
			</TABLE>
			</TD>
		<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="172" bgColor="#ffffff">

			<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Title of the Legislative instrument" face="Verdana" color="#000000" size="1">Legislative instrument</FONT></B></SPAN>
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
						vAlign="center" width="70" bgColor="#ffffff">

			<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Reference number" face="Verdana" color="#000000" size="1">Ref. nr</FONT></B></SPAN>
				</TD>
				<TD> <P align="right"><MAP name="FPMap4"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" 
							href="javascript:setOrder('T_SOURCE.SOURCE_CODE DESC')"/><AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('T_SOURCE.SOURCE_CODE')"/></MAP>
							<IMG height="22"  src="images/arrows.gif" width="17" useMap="#FPMap4"  border="0"/></P>
				</TD>
			</TR>
			</TBODY>
			</TABLE>
			</TD>
		<!--xsl:if test="$rora='A'">
		<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="80" bgColor="#ffffff">

			<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Deadline of the reporting activity" face="Verdana" color="#000000" size="1">Deadline</FONT></B></SPAN>
				</TD>
				<TD> <P align="right"><MAP name="FPMap5"><AREA shape="RECT" alt="Sort 99-0" coords="0,0,16,7" 
							href="javascript:setOrder('T_ACTIVITY.NEXT_DEADLINE DESC')"/><AREA shape="RECT" alt="Sort 0-99" coords="1,13,16,21" href="javascript:setOrder('T_ACTIVITY.NEXT_DEADLINE')"/></MAP>
							<IMG height="22"  src="images/arrows.gif" width="17" useMap="#FPMap5"  border="0"/></P>
				</TD>
			</TR>
			</TBODY>
			</TABLE>
			</TD>
		</xsl:if-->

		</TR>
		<!--TR>
			<TD colspan="5">
			</TD>
		</TR-->
		<xsl:apply-templates select="RowSet[@Name='Search results']"/>
		</TBODY>
		</TABLE>
		<!-- table heading table -->



		<!-- page title -->
		<!--table cellspacing="7" border="0" width="600"><tr><td>
				<xsl:choose>
					<xsl:when test="$rora='A'">
						<span class="head1">Reporting activities</span>
					</xsl:when>
					<xsl:otherwise>
						<span class="head1">Reporting obligations</span>
					</xsl:otherwise>
				</xsl:choose>
		</td>
		<td align="right">
		<xsl:if test="contains($permissions, ',/Admin:v,')='true'">
			<a>
				<xsl:attribute name="href">javascript:openActionTypeHistory('D','<xsl:value-of select="$historyMode"/>')</xsl:attribute>
				<img src="images/showdeleted.png" alt="Show deleted" border="0"/>
			</a><br/>
		</xsl:if>
		</td></tr>
		</table-->
		<!-- page title -->


		<div style="margin-left:20">
		<!--table cellspacing="7pts">
			<xsl:apply-templates select="RowSet[@Name='Search results']/@*"/>
		</table-->
		</div>
			
		<!--xsl:apply-templates select="RowSet[@Name='Old Search results']"/-->
		</div>

		<xsl:call-template name="CommonFooter"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/>:<xsl:value-of select="ISSUE_NAME"/></xsl:attribute>
				<!--xsl:attribute name="selected">
					<xsl:if test="PK_ISSUE_ID=$sel_issue">
						true
					</xsl:if>
				</xsl:attribute-->
				<xsl:value-of select="ISSUE_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="SpatialTemplate">
		<xsl:param name="type" select="'Not selected'"/>
		<xsl:param name="type2" select="'Not selected'"/>
		<xsl:for-each select="RowSet[@Name='Spatial']/Row/T_SPATIAL[SPATIAL_TYPE=$type or SPATIAL_TYPE=$type2]">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_SPATIAL_ID"/>:<xsl:value-of select="SPATIAL_NAME"/>
				</xsl:attribute>
			<xsl:value-of select="SPATIAL_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="RowSet[@Name='ParamGroup']">
		<xsl:for-each select="Row/T_PARAM_GROUP">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_GROUP_ID"/>:<xsl:value-of select="GROUP_NAME"/>
				</xsl:attribute>
			<xsl:value-of select="GROUP_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Client']">
		<xsl:for-each select="Row/T_CLIENT">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_CLIENT_ID"/>:<xsl:value-of select="CLIENT_NAME"/></xsl:attribute>
			<xsl:value-of select="CLIENT_NAME"/></option>
		</xsl:for-each>
	</xsl:template>


	<xsl:template match="RowSet[@Name='Search results']">
	<!-- RA search results -->
		<!--xsl:choose-->
			<!--xsl:when test="$rora='A'"-->
				<xsl:choose>
					<xsl:when test="count(Row)=0">			
						<tr><td><xsl:call-template name="nofound"/></td></tr>
					</xsl:when>
					<xsl:otherwise>
						<!--table cellspacing="0" width="600"-->
							<xsl:for-each select="Row">
							<TR>
								<xsl:attribute name="bgColor">
									<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
								</xsl:attribute>
								<!--ra-->
								<xsl:if test="$rora='A'">
								<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
									<SPAN class="head0n">
										<xsl:if test="T_ACTIVITY/PK_RA_ID !=''">
										<A> 
											<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_ACTIVITY/FK_RO_ID"/>&amp;mode=A</xsl:attribute>
											<FONT face="Verdana" size="2">
													<xsl:choose>
														<xsl:when test="T_ACTIVITY/TITLE !=''">
															<xsl:value-of select="T_ACTIVITY/TITLE"/>
														</xsl:when>
														<xsl:otherwise>
															Reporting Activity
														</xsl:otherwise>
													</xsl:choose>
											</FONT>
										</A>
										</xsl:if>
									</SPAN>&#160;
								</TD>
								</xsl:if>
								<!--ro-->
								<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
									<SPAN class="head0n">
										<A> 
											<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;aid=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=R</xsl:attribute>
											<FONT face="Verdana" size="2">
													<xsl:choose>
														<xsl:when test="T_REPORTING/ALIAS!=''">
															<xsl:value-of select="T_REPORTING/ALIAS"/>
														</xsl:when>
														<xsl:otherwise>
															Obligation
														</xsl:otherwise>
													</xsl:choose>
											</FONT>
										</A>
									</SPAN>&#160;
								</TD>
								<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
									<SPAN class="head0n">
										<A> 
											<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
											<FONT face="Verdana" size="2"><xsl:value-of select="T_SOURCE/TITLE"/></FONT>
										</A>
									</SPAN>
								</TD>
								<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" vAlign="top">
									<SPAN class="head0n">
											<FONT face="Verdana" size="2"><xsl:value-of select="T_SOURCE/SOURCE_CODE"/></FONT>
									</SPAN>
									&#160;
								</TD>
							</TR>
							</xsl:for-each>
							<!--/table-->
						</xsl:otherwise>
					</xsl:choose>



	</xsl:template>

	<xsl:template match="RowSet[@Name='Search results']/@*">
		<xsl:if test="name(.)!='Name' and name(.)!='order' and name(.)!='auth'">
			<tr><td>
				<xsl:value-of select="translate(name(.),'_',' ')"/>&amp;<b><xsl:value-of select="."/></b>
			</td></tr>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
