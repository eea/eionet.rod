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
 * Original Code: Ander Tenno (TietoEnator)
 * -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template name="Help">
		<xsl:param name="id">HELP_MAIN</xsl:param>
		<xsl:param name="perm">x</xsl:param>
		<map name="{$id}">
			<area shape="rect" coords="0,0,17,17" href="javascript:openViewHelp('{$id}')" alt="View help for this field"></area>
		</map>
		&#160;<img src="images/bb_help.png" usemap="#{$id}" border="0"></img>
		<xsl:call-template name="EditHelp">
			<xsl:with-param name="id"><xsl:value-of select="$id"/></xsl:with-param>
			<xsl:with-param name="perm"><xsl:value-of select="$perm"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="HelpOverview">
		<xsl:param name="id">HELP_MAIN</xsl:param>
		<xsl:param name="perm">x</xsl:param>
		<map name="{$id}">
			<area shape="rect" coords="0,0,120,17" href="javascript:openViewHelp('{$id}')" alt="See overview of this form"></area>
		</map>
		&#160;<img src="images/bb_helpoverview.png" usemap="#{$id}" border="0"></img>
		<xsl:call-template name="EditHelp">
			<xsl:with-param name="id"><xsl:value-of select="$id"/></xsl:with-param>
			<xsl:with-param name="perm"><xsl:value-of select="$perm"/></xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="EditHelp">
		<xsl:param name="id">HELP_MAIN</xsl:param>
		<xsl:param name="perm">x</xsl:param>
		<xsl:if test="contains($perm, 'h')='true'">
			<map name="{$id}_Edit">
				<area shape="rect" coords="0,0,17,17" href="javascript:openHelp('{$id}')" alt="Edit help text"></area>
			</map>
			<img src="images/bb_edithelp.png" usemap="#{$id}_Edit" border="0"></img>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
