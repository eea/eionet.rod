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

<xsl:stylesheet
	exclude-result-prefixes="java"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<!--xsl:output indent="yes"/-->   
	<!--xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" omit-xml-declaration="yes"/-->
	<xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd" omit-xml-declaration="yes"/>   

	<xsl:include href="util.xsl"/>
	<xsl:include href="dropdownmenus.xsl"/>
	
	<xsl:param name="req" select="'default value'"/>
	<xsl:variable name="printmode" select="java:eionet.rod.RODUtil.getParameter($req, 'printmode')"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet[position()=1]/@auth"/>
	</xsl:variable>
	<xsl:variable name="username">
		<xsl:value-of select="//RowSet[position()=1]/@username"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="/">
		<html xml:lang="en">
			<xsl:call-template name="html_head"/>
			<body>
			<xsl:attribute name="class">
				<xsl:value-of select="$col_class"/>
			</xsl:attribute>
			<div id="container">
						<!-- MAIN table -->
				<div id="toolribbon">
					<div id="lefttools">
				      <a id="eealink" href="http://www.eea.europa.eu/">EEA</a>
				      <a id="ewlink" href="http://www.ewindows.eu.org/">EnviroWindows</a>
				    </div>
				    <div id="righttools">    
						<xsl:choose>
							<xsl:when test="contains($admin,'true')='true'">
								<a id="logoutlink" href="logout_servlet" title="Log out">Logout <xsl:value-of select="$username"/></a>
							</xsl:when>
							<xsl:otherwise>
								<a>
									<xsl:attribute name="href">
										<xsl:value-of select="java:eionet.rod.EionetCASFilter.getCASLoginURL($req,false())"/>
									</xsl:attribute>
									<xsl:attribute name="title">Login</xsl:attribute>
									<xsl:attribute name="id">loginlink</xsl:attribute>
									Login
								</a>					    
							</xsl:otherwise>
						</xsl:choose>
						<xsl:call-template name="PageHelp"/>
						<a id="printlink" title="Print this page" href="javascript:this.print();"><span>Print</span></a>
						<a id="fullscreenlink" href="javascript:toggleFullScreenMode()" title="Switch to/from full screen mode"><span>Switch to/from full screen mode</span></a>
						<a id="acronymlink" href="http://www.eionet.europa.eu/acronyms" title="Look up acronyms"><span>Acronyms</span></a>
						<form action="http://search.eionet.europa.eu/search.jsp" method="get">
							<div id="freesrchform"><label for="freesrchfld">Search</label>
								<input type="text" id="freesrchfld" name="query"/>
								<input id="freesrchbtn" type="image" src="images/button_go.gif" alt="Go"/>
							</div>
						</form>
				    </div>
				</div> <!-- toolribbon -->

				<div id="pagehead">
				    <a href="/"><img src="images/eea-print-logo.gif" alt="Logo" id="logo" /></a>
				    <div id="networktitle">Eionet</div>
				    <div id="sitetitle">Reporting Obligations Database (ROD)</div>
				    <div id="sitetagline">This service is part of Reportnet</div>
				</div> <!-- pagehead -->


				<div id="menuribbon">
					<xsl:call-template name="DropDownMenu"/>
				</div>
				<xsl:call-template name="breadcrumbs"/>
				<xsl:call-template name="LeftToolbar">
					<xsl:with-param name="admin"><xsl:value-of select="$admin"/></xsl:with-param>
					<xsl:with-param name="username"><xsl:value-of select="$username"/></xsl:with-param>
					<xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param>
				</xsl:call-template>
				<xsl:apply-templates select="XmlData"/>
			</div>
		</body>
		 </html>
	</xsl:template>  

	<xsl:template name="nofound">
		<table cellspacing="7pts">
			<tr height="40pts" valign="bottom">
				<td width="10pts">&#160;</td>
				<td>No records found</td>
			</tr>
			<tr height="40pts" valign="bottom">
				<td width="10pts">&#160;</td>
				<td><a href="javascript:history.back()"><span class="Mainfont">[Back]</span></a></td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="html_head">
		<head>
		<meta content="text/html; CHARSET=UTF-8" http-equiv="Content-Type"/>
		<meta name="title" content="EEA - Reporting Obligations Database" />
		<meta name="description" content="The EEA's reporting obligations database (ROD) contains information describing environmental reporting obligations that countries have towards international organisations." />
		<meta name="keywords" content="reporting obligations, environmental legislation, environmental reporting, environmental dataflows, European Environment Agency, EEA, European, Environmental information, Environmental portal, Eionet, Reportnet, air, waste, water, biodiversity" />
		<meta name="Publisher" content="EEA, The European Environment Agency" />
		<meta name="Rights" content="Copyright EEA Copenhagen 2003" />

		<title><xsl:call-template name="PageTitle"/></title>
		<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
		<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />		
		<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" title="Eionet 2007 style" />
		<link rel="stylesheet" type="text/css" href="eionet2007.css" media="screen" title="Eionet 2007 style"/>
		<link rel="alternate" type="application/rdf+xml" title="All Obligations" href="http://rod.eionet.europa.eu/obligations"/>
		<link rel="alternate" type="application/rdf+xml" title="All Localities" href="http://rod.eionet.europa.eu/countries"/>
		<link rel="alternate" type="application/rdf+xml" title="All Legal instruments" href="http://rod.eionet.europa.eu/instruments"/>
		<link rel="alternate" type="application/rss+xml" title="Obligation deadlines" href="http://rod.eionet.europa.eu/events.rss"/>
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
		<script type="text/javascript" src="script/util.js"></script>
		<script type="text/javascript" src="script/pageops.js"></script>
                <script type="text/javascript" src="script/mark_special_links.js"></script>

		<xsl:if test = "not(java:eionet.rod.EionetCASFilter.isCasLoggedUser($req))">
			<script type="text/javascript">
				<![CDATA[
					function get_cookie( cookie_name )
					{
					  var results = document.cookie.match ( cookie_name + '=(.*?)(;|$)' );
					  if ( results )
					    return ( unescape ( results[1] ) );
					  else
					    return null;
					}
				]]>
				<![CDATA[ eionetCasCookieName="]]><xsl:value-of select="java:eionet.rod.EionetCASFilter.getEionetLoginCookieName()"/><![CDATA[";]]>
				<![CDATA[ eionetCookieLoginURL="]]><xsl:value-of select="java:eionet.rod.EionetCASFilter.getEionetCookieCASLoginURL($req)"/><![CDATA[";]]>
				<![CDATA[
				eionetLoginCookieValue = get_cookie(eionetCasCookieName);
				if (eionetLoginCookieValue != null && eionetLoginCookieValue == "loggedIn"){
					window.location=eionetCookieLoginURL;
				}
				]]>
			</script>
		</xsl:if>

		
		<script type="text/javascript">
					<![CDATA[
<!--
function showhelp(text) {
	if (text != '')
		alert(text);
	else
		alert('No examples for this unit type!');
}


function openActionTypeHistory(MODE,TYPE){

	var url = "history.jsv?entity=" + TYPE + "&mode=" + MODE;
	var name = "History";
	var features = "location=no, menubar=no, width=700, height=400, top=100, left=100, scrollbars=yes";
	var w = window.open(url,name,features);
	w.focus();

}


/*function openHistory(ID,TYPE){

	var url = "history.jsv?entity=" + TYPE + "&id=" + ID;
	var name = "History";
	var features = "location=no, menubar=no, width=640, height=400, top=100, left=200, scrollbars=yes";
	var w = window.open(url,name,features);
	w.focus();

} */


/*
function openClient(ID){

	var url = "client.jsv?id=" + ID;
	var name = "Client";
	var features = "location=no, menubar=no, width=650, height=500, top=50, left=50, scrollbars=no, resizable=yes";
	var w = window.open(url,name,features);
	w.focus();

}
*/
var picklist = new Array();

function fillPicklist(type,list,text) {
      var i,js;
	for (i = list.length; i > 0; --i)
		list.options[i] = null;
	list.options[0] = new Option("Choose a group","-1");
	j = 1;
	for (i = 0; i < picklist.length; i++) {
	  s = new String(picklist[i]);
	  pvalue = s.substring(0,s.indexOf(":"));
	  ptext = s.substring(s.indexOf(":")+1,s.lastIndexOf(":"));
	  ptype = s.substring(s.lastIndexOf(":")+1,s.lastIndexOf(":")+2);
	  if (ptype.valueOf() == type) {
	  	list.options[j] = new Option(ptext.valueOf(), pvalue.valueOf()+":"+ptext.valueOf());
	  	j++;
	  }
	} 
	list.options[0].selected = true;
}

function chkUrl(fld) {
		var s = fld.value;
		if ( s != "" &&  (s.substr(0,7) != "http://") && (s.substr(0,8) != "https://") && (s.substr(0,6) != "ftp://") )	{
			fld.focus()
			alert("Wrong URL format");
			//fld.select();
		}
}

//-->
				]]>
			</script>
		</head>
	</xsl:template>
</xsl:stylesheet>
