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
	<xsl:variable name="query-string">
		<xsl:value-of select="substring-before(/XmlData/xml-query-string,'page=')"/>
	</xsl:variable>

	<xsl:variable name="page-number">
		<xsl:value-of select="substring-after(/XmlData/xml-query-string,'page=')"/>
	</xsl:variable>

	<xsl:template name="links">
		<xsl:param name="num-rows">0</xsl:param>
		<xsl:if test="$page-number>0 or $num-rows=100">
		<table width="600" border="0" cellspacing="7">
			<tr>
				<xsl:choose>
					<xsl:when test="$page-number>0">
						<td nowrap="true"><a><xsl:attribute name="href">
							<xsl:value-of select="$query-string"/>page=<xsl:value-of select="$page-number - 1"/>
						</xsl:attribute><span class="head0"><i>Previous 100 records ...</i></span></a></td>
					</xsl:when>
					<xsl:otherwise>
						<td>&#160;</td>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="$num-rows=100">
					<td nowrap="true" align="right"><a><xsl:attribute name="href">
						<xsl:value-of select="$query-string"/>page=<xsl:value-of select="$page-number + 1"/>
					</xsl:attribute><span class="head0"><i>Next 100 records ...</i></span></a></td>
				</xsl:if>
			</tr>
		</table>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>