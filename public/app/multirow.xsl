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
	<xsl:template match="RowSet">
		<table cellspacing="3" border="0" style="margin-left:20">
			<xsl:call-template name="make-row"/>
		</table>
	</xsl:template>

	<xsl:template name="make-row">
		<tr valign="top">
			<xsl:for-each select="Row">
				<xsl:apply-templates select="."/>
				<xsl:if test="position() mod 2 = 0 and position() != last()">
					<xsl:text disable-output-escaping="yes"><![CDATA[</tr><tr valign="top">]]></xsl:text>
				</xsl:if>
			</xsl:for-each>
		</tr>
	</xsl:template>
</xsl:stylesheet>