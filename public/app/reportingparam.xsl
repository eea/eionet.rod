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
	<xsl:template match="/">
   	<html lang="en">
			<head>
				<title>Reporting Obligations Database</title>
				<META HTTP-EQUIV="Content-Type" CONTENT="text/html; CHARSET=ISO-8859-1"/>
				<!-- <link type="text/css" rel="stylesheet" href="http://www.eionet.eu.int/eionet.css"> -->
				<link href="eionet.css" rel="stylesheet" type="text/css"/>
		</head>
		<body bgcolor="#f0f0f0" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
		
		<div style="margin-left:13">
		<span class="head1"><br/>Parameters related to reporting obligation</span><br/>
		
		<xsl:choose>
			<xsl:when test="count(//RowSet[@Name='Parameter']/Row)>0">
			<table width="400" colspan="5" border="1">
			<tr>
				<td bgcolor="#646666"><span class="head0"><font color="white">Nr</font></span></td>
				<td bgcolor="#646666"><span class="head0"><font color="white">Parameter</font></span></td>
				<!--td bgcolor="#646666"><span class="head0"><font color="white">Unit</font></span></td-->
			</tr>
			
			<xsl:for-each select="//RowSet[@Name='ParameterGroup']/Row">

			<xsl:call-template name="GroupParameters">
				<xsl:with-param name="groupid"><xsl:value-of select="T_PARAM_GROUP/PK_GROUP_ID"/></xsl:with-param>
				<xsl:with-param name="groupname"><xsl:value-of select="T_PARAM_GROUP/GROUP_NAME"/></xsl:with-param>
			</xsl:call-template>
			</xsl:for-each>
			
			</table>
			</xsl:when>
			<xsl:otherwise>
				<table cellspacing="7pts">
					<tr height="40pts" valign="bottom">
						<td width="10pts">&#160;</td>
						<td>No records found</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
		
		<form>
			<input type="button" name="closebtn" onclick="javascript:self.close()" value="Close window"/>
		</form>
		</div>
		</body>
   	 </html>
	</xsl:template>  

	<xsl:template name="GroupParameters">
		
		<xsl:param name="groupid" select="'Not selected'"/>
		<xsl:param name="groupname" select="'Not selected'"/>

		<xsl:if test="count(//RowSet[@Name='Parameter']/Row/T_PARAMETER[FK_GROUP_ID=$groupid]) > 0">
			<tr><td colspan="3" bgcolor="#646666"><span class="head0"><font color="white">
				<xsl:value-of select="$groupname"/>
			</font></span></td></tr>
<script language="JavaScript">
</script>
		<xsl:for-each select="//RowSet[@Name='Parameter']/Row[T_PARAMETER/FK_GROUP_ID=$groupid]">
			<tr>
				<td>
					<xsl:number format="1."/>
				</td>
				<td>
					<xsl:value-of select="T_PARAMETER/PARAMETER_NAME"/>
				</td>
				<!--td>
					<xsl:choose>
						<xsl:when test="T_PARAMETER_LNK/PARAMETER_UNIT = ''">
							<xsl:value-of select="T_UNIT/UNIT_NAME"/>
							<xsl:if test="T_UNIT/UNIT_NAME=''"><b>&#160;?</b></xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="T_PARAMETER_LNK/PARAMETER_UNIT"/>
						</xsl:otherwise>
					</xsl:choose>
				</td-->
			</tr>
		</xsl:for-each>
		</xsl:if>
	</xsl:template>


</xsl:stylesheet>