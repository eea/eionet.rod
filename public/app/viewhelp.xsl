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
	<xsl:include href="util.xsl"/>

	<xsl:template match="/">
		<html>
			<head>
				<title>Help</title>
				<link href="eionet.css" rel="stylesheet" type="text/css"/>
			</head>
			<body bgcolor="#F0F0F0">
				<h3>
					<xsl:choose>
						<xsl:when test="XmlData/RowSet/Row/T_HELP/HELP_TITLE">
							<xsl:value-of select="XmlData/RowSet/Row/T_HELP/HELP_TITLE"/>
						</xsl:when>
						<xsl:otherwise>&lt;No Title&gt;</xsl:otherwise>
					</xsl:choose>
				</h3>
	      	<xsl:call-template name="break">
	   	       <xsl:with-param name="text" select="XmlData/RowSet/Row/T_HELP/HELP_TEXT"/>
		      </xsl:call-template>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>