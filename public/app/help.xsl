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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java" version="1.0">
	<xsl:strip-space elements="HELP_TEXT"/>
	<xsl:param name="req" select="'default value'"/>
	<xsl:variable name="id" select="java:eionet.rod.RODUtil.getParameter($req, 'helpID')"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>Edit Help Text</title>
				<link href="eionet.css" rel="stylesheet" type="text/css"/>
				<script language="JavaScript">
					// Sets focus on form's first element
					//
					function setFocus(formName) {
						if(document.all) {
							if(document.all(formName).elements[0])
								document.all(formName).elements[0].focus();
						}
						else {
							if(document.forms[formName].elements[0])
								document.forms[formName].elements[0].focus();
						}
					}
					
					<![CDATA[
						function saveHelp() {
//alert("1");
							document.helpForm.submit();
//alert("2");
						//	window.close();
						}
					]]>

				</script>
			</head>
			<body onLoad="setFocus('helpForm')">
				<center>
					<h3>Edit Help Text</h3>
					<!--form onsubmit="javascript:window.close()" name="helpForm" method="post" action="help.jsv"-->
					<form name="helpForm" method="post" action="help.jsv">
						<table width="100%">
							<tr>
								<td align="left">
									<b>Title:&#160;</b>
								</td>
								<td align="left">
									<input type="text" size="55" maxlength="255">
										<xsl:attribute name="name">/XmlData/RowSet/Row/T_HELP/HELP_TITLE</xsl:attribute>
										<xsl:attribute name="value"><xsl:value-of select="/XmlData/RowSet/Row/T_HELP/HELP_TITLE"/></xsl:attribute>
									</input>
								</td>
							</tr>
							<tr>
								<td align="left" colspan="2">
									<br/><b>Help text:</b><br/>
								</td>
							</tr>
							<tr>
								<td align="left" colspan="2">
									<textarea rows="18" cols="55">
										<xsl:attribute name="name">/XmlData/RowSet/Row/T_HELP/HELP_TEXT</xsl:attribute>
										<xsl:value-of select="/XmlData/RowSet/Row/T_HELP/HELP_TEXT"/>
									</textarea>
								</td>
							</tr>
						</table>
						<table width="100%">
							<tr>
								<td colspan="2">&#160;</td>
							</tr>
							<tr align="right">
								<td colspan="2" align="right">
									<input type="submit" value="&#160;&#160;&#160;&#160;OK&#160;&#160;&#160;&#160;"/>&#160;
									<!--input type="button" onClick="javascript:saveHelp()" value="&#160;&#160;&#160;&#160;OK&#160;&#160;&#160;&#160;"/-->
									<input type="button" onclick="javascript:window.close()" value="Cancel"/>
								</td>
							</tr>
						</table>
						<xsl:choose>
							<xsl:when test="/XmlData/RowSet/Row/T_HELP/PK_HELP_ID">
								<input type="hidden" name="dom-update-mode" value="U"/>
								<input type="hidden">
									<xsl:attribute name="name">/XmlData/RowSet/Row/T_HELP/PK_HELP_ID</xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="/XmlData/RowSet/Row/T_HELP/PK_HELP_ID"/></xsl:attribute>
								</input>
							</xsl:when>
							<xsl:otherwise>
								<input type="hidden" name="dom-update-mode" value="I"/>
								<input type="hidden">
									<xsl:attribute name="name">/XmlData/RowSet/Row/T_HELP/PK_HELP_ID</xsl:attribute>
									<xsl:attribute name="value"><xsl:value-of select="$id"/></xsl:attribute>
								</input>
							</xsl:otherwise>
						</xsl:choose>
					</form>
				</center>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
