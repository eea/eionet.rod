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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java" version="1.0">
	<!--xsl:output indent="yes"/-->   
	<!--xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" omit-xml-declaration="yes"/-->
	<xsl:output indent="yes" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd" omit-xml-declaration="yes"/>   

	<xsl:include href="util.xsl"/>

	<xsl:param name="req" select="'default value'"/>
	<xsl:variable name="printmode" select="java:eionet.rod.RODUtil.getParameter($req, 'printmode')"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet[position()=1]/@auth"/>
	</xsl:variable>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="/">
   	<html lang="en">
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="title" content="EEA - Reporting Obligations Database" />
		<meta name="description" content="The EEA's reporting obligations database (ROD) contains information describing environmental reporting obligations that countries have towards international organisations." />
		<meta name="keywords" content="reporting obligations, environmental legislation, environmental reporting, environmental dataflows, European Environment Agency, EEA, European, Environmental information, Environmental portal, Eionet, Reportnet, air, waste, water, biodiversity" />
		<meta name="Publisher" content="EEA, The European Environment Agency" />
		<meta name="Rights" content="Copyright EEA Copenhagen 2003" />

		<title><xsl:call-template name="PageTitle"/></title>
		<link rel="stylesheet" type="text/css" href="layout-screen.css" media="screen"/>
		<link rel="stylesheet" type="text/css" href="layout-print.css" media="print" />
		<link rel="stylesheet" type="text/css" href="layout-handheld.css" media="handheld" />
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
		<script type="text/javascript" src="script/util.js"></script>
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

//-->
				]]>
			</script>
		</head>
		<body>

			<!-- MAIN table -->

<div id="pagehead">
 <div id="identification">
  <a href="/" title="Frontpage of website"><img src="images/logo.png" alt="Logo" id="logo" border="0" /></a>
								<div class="sitetitle"><xsl:call-template name="FirstHeading"/></div>
								<div class="sitetagline"><xsl:call-template name="SecondHeading"/></div>
 </div>


</div> <!-- page head -->

					<xsl:call-template name="LeftToolbar">
						<xsl:with-param name="admin"><xsl:value-of select="$admin"/></xsl:with-param>
					</xsl:call-template>
					<xsl:apply-templates select="XmlData"/>
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
</xsl:stylesheet>
