<?xml version="1.0"?>
<!--
/**
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
 * The Original Code is "EINRC-5 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Ander Tenno (TietoEnator)
 */
 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:strip-space elements="HELP_TEXT"/>
	<xsl:variable name="permissions"/>
	<xsl:variable name="pagetitle">
		Help
	</xsl:variable>
	<xsl:include href="util.xsl"/>

	<xsl:template match="/">
		<html xml:lang="en">
			<head>
				<meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
				
				<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
				<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />		
				<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" title="Eionet 2007 style" />
				<link rel="stylesheet" type="text/css" href="eionet2007.css" media="screen" title="Eionet 2007 style"/>
				
				<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
				<script type="text/javascript" src="script.js"></script>
				<script type="text/javascript" src="pageops.js"></script>
				
				<title>Help</title>
			</head>
			<body class="popup">
				<div id="pagehead">
				    <a href="/"><img style="padding-right: 5px;" src="images/eealogo.gif" alt="Logo" id="logo" /></a>
				    <div id="networktitle">Eionet</div>
				    <div id="sitetitle">Reporting Obligations Database (ROD)</div>
				    <div id="sitetagline">This service is part of Reportnet</div>    
				</div> <!-- pagehead -->
				<div id="operations" style="margin-top:10px">
					<ul>
						<li><a href="javascript:window.close();">Close</a></li>
					</ul>
				</div>
				<div id="workarea" style="clear:right">
					<xsl:for-each select="XmlData/RowSet/Row/T_HELP">
						<h3>
							<xsl:choose>
								<xsl:when test="HELP_TITLE">
									<xsl:value-of select="HELP_TITLE"/>
								</xsl:when>
								<xsl:otherwise>&lt;No Title&gt;</xsl:otherwise>
							</xsl:choose>
						</h3>
						<xsl:call-template name="break">
							<xsl:with-param name="text" select="HELP_TEXT"/>
						</xsl:call-template>
					</xsl:for-each>
				</div>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="createURL"/>
</xsl:stylesheet>
