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
	<xsl:include href="editor.xsl"/>
	<xsl:include href="util.xsl"/>

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source']/Row/T_SOURCE"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source']/Row/T_SOURCE">
		<table width="640">
		<tr>
			<td width="465">
				<span class="head1" id="lblTitle">Edit/Create a Legal Instrument</span>
			</td>
			<td>
				<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_LI</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
			</td>
		</tr>
		</table>
		<form name="f" method="POST" action="source.jsv">
<!--
		<input type="hidden" name="xml-query-string">
			<xsl:attribute name="value"><xsl:value-of select="/XmlData/xml-query-string"/></xsl:attribute>
		</input>
-->
		<input type="hidden" name="dom-update-mode">
			<xsl:attribute name="value">
				<xsl:choose>
					<xsl:when test="//RowSet[@Name='Source'][@skeleton='1']">A</xsl:when>
					<xsl:otherwise>U</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="PK_SOURCE_ID/@XPath"/></xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="PK_SOURCE_ID"/></xsl:attribute>
		</input>
		<table cellspacing="15pts" border="0" width="700">
			<tr valign="middle">
				<td width="160" nowrap="true" align="left"><b>Identification number:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_IDENTIFICATIONNUMBER</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td width="145">
					<input type="text" size="13" maxlength="50" width="145" style="width:145" onChange="changed()">
						<xsl:attribute name="name"><xsl:value-of select="SOURCE_CODE/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="SOURCE_CODE"/></xsl:attribute>
					</input>
				</td>
				<td width="100" nowrap="true" align="left"><b>Draft:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_DRAFT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td width="50">
					<select width="50" style="width:50">
						<xsl:attribute name="name"><xsl:value-of select="DRAFT/@XPath"/></xsl:attribute>
						<xsl:apply-templates select="//RowSet[@Name='Yes/No']"/>
					</select>
				</td>
				<td width="125">&#160;</td>
				<td width="125">&#160;</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="160"><b>Legal name:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_LEGALNAME</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5"><textarea rows="4" cols="55" wrap="soft" width="570" style="width:570"  onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="TITLE/@XPath"/></xsl:attribute>
					<xsl:value-of select="TITLE"/></textarea></td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="160"><b>Short name:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ALIASNAME</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5"><textarea rows="4" cols="55" wrap="soft" width="570" style="width:570" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="ALIAS/@XPath"/></xsl:attribute>
					<xsl:value-of select="ALIAS"/></textarea></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>CELEX reference:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_CELEXREFERENCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5"><input type="text" size="56" maxsize="255" width="570" style="width:570" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="CELEX_REF/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="CELEX_REF"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>URL to official source:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_URLTOOFFICIALSOURCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5"><input type="text" size="56" maxsize="255" width="570" style="width:570" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="URL/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="URL"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>Issued by:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ISSUEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5">
					<!--input type="text" size="56" maxsize="255" width="570" style="width:570" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="ISSUED_BY/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="ISSUED_BY"/></xsl:attribute>
					</input-->

					<xsl:variable name="selIssuer">
							<xsl:value-of select="FK_CLIENT_ID"/>
					</xsl:variable>
				<select  width="500" style="width:500" maxlength="255" onChange="changeIssuer(this)">
					<xsl:attribute name="name"><xsl:value-of select="FK_CLIENT_ID/@XPath"/></xsl:attribute>
						<option value=''></option>
						<xsl:for-each select="//RowSet[@Name='Issuer']/Row">
							<option>
								<xsl:attribute name="value">
									<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>
								</xsl:attribute>
									<xsl:if test="T_CLIENT/PK_CLIENT_ID = $selIssuer">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
								<xsl:attribute name="url"><xsl:value-of select="T_CLIENT/CLIENT_URL"/></xsl:attribute>
								<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
							</option>
						</xsl:for-each>
				</select>
				<map name="newIssuerMap">
					<area alt="Add a new client" shape="rect" coords="0,0,25,25" href="javascript:openAddClientWin()"></area>
				</map>
				<img border="0" height="25" width="25" src="images/new.gif" usemap="#newIssuerMap"></img>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>URL to issuer:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_URLTOISSUER</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5"><input type="text" size="56" maxsize="255" width="570" style="width:570" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="ISSUED_BY_URL/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="ISSUED_BY_URL"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td width="160" nowrap="true" align="left"><b>Parent legal instrument:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_PARENTLEGALINSTRUMENT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5" width="140">
					<select name="" width="570" style="width:570">
						<xsl:attribute name="name"><xsl:value-of select="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID/@XPath"/></xsl:attribute>
						<option/>
						<xsl:if test="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
							<option selected="true">
								<xsl:attribute name="value">
									<xsl:value-of select="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/>
								</xsl:attribute>
								<xsl:choose>
									<xsl:when test="../PARENT/ALIAS != '' and ../PARENT/ALIAS != '...'">
										<xsl:value-of select="../PARENT/ALIAS"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="../PARENT/TITLE"/>
									</xsl:otherwise>
								</xsl:choose>
							</option>
						</xsl:if>
						<xsl:apply-templates select="//RowSet[@Name='Sources']"/>
					</select>
				</td>
			</tr>
			<tr valign="top">
				<td nowrap="true" colspan="6"><b>Eurlex classification hierarchy:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_CLASSIFICATION</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
			</tr>
			<tr>
				<td colspan="6">
					<table><tr valign="middle">
						<td width="300" align="center"><xsl:apply-templates select="../SubSet[@Name='LnkParent']"/></td>
						<td width="100" nowrap="true">
							<table cellspacing="5">
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80"
										onclick="mvValues(lnkParent, parentLst, null)" 
										value="&#160;&#160;-&gt;&#160;&#160;"/>
								</td></tr>
								<tr><td width="100" align="center">
									<input type="button" width="80" style="width:80"
										onclick="mvValues(parentLst, lnkParent, null)" 
										value="&#160;&#160;&lt;-&#160;&#160;"/><br/>
								</td></tr>
							</table>
						</td>
						<td width="300" align="center"><xsl:apply-templates select="//RowSet[@Name='SOURCE_CLASS']"/></td>
					</tr></table>
				</td>
			</tr>
		</table>
		<table cellspacing="15pts" border="0" width="700">
			<tr valign="top">
				<td width="160" nowrap="true" align="left"><b>Valid from:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_VALIDFROM</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				<br/>(dd/mm/yyyy)</td>
				<td width="100"><input type="text" size="8" maxlength="10" width="100" style="width:100" onChange="checkDate(this)">
						<xsl:attribute name="name"><xsl:value-of select="VALID_FROM/@XPath"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="VALID_FROM"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="160"><b>Abstract:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ABSTRACT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5"><textarea rows="4" cols="55" wrap="soft" width="580" style="width:580" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="ABSTRACT/@XPath"/></xsl:attribute>
					<xsl:value-of select="ABSTRACT"/></textarea></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>Comment:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_COMMENT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="5"><textarea rows="4" cols="55" wrap="soft" width="580" style="width:580" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="COMMENT/@XPath"/></xsl:attribute>
					<xsl:value-of select="COMMENT"/></textarea></td>
			</tr>
		</table>
		<table width="720" border="0">
			<tr>
				<td nowrap="true" width="130"><i><b>Conventions</b></i></td>
				<td width="590"><hr/></td>
			</tr>
		</table>
		<table cellspacing="15pts" border="0" width="700">
			<tr valign="top">
				<td width="200" nowrap="true"><b>EC entry into force (C):</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ECENTRYINTOFORCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				<br/>(dd/mm/yyyy)</td>
				<td width="200"><input type="text" size="8" maxlength="10" width="100" style="width:100" onChange="checkDate(this)">
					<xsl:attribute name="name"><xsl:value-of select="EC_ENTRY_INTO_FORCE/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="EC_ENTRY_INTO_FORCE"/></xsl:attribute>
				</input></td>
				<td width="160" nowrap="true" align="left"><b>EC accession (C):</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ECACCESSION</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				<br/>(dd/mm/yyyy)</td>
				<td width="300"><input type="text" size="8" maxlength="10" width="100" style="width:100" onChange="checkDate(this)">
					<xsl:attribute name="name"><xsl:value-of select="EC_ACCESSION/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="EC_ACCESSION"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>Secretariat:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_SECRETARIAT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="4"><input type="text" size="50" maxsize="255" width="580" style="width:580" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="SECRETARIAT/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="SECRETARIAT"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true"><b>URL to Secretariat homepage:</b>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_SECRETARIATHOMEPAGE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td colspan="4"><input type="text" size="50" maxsize="255" width="580" style="width:580" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="SECRETARIAT_URL/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="SECRETARIAT_URL"/></xsl:attribute>
				</input></td>
			</tr>
			</table>

			<!-- Record management -->
			<table width="720" border="0">
				<tr>
					<td nowrap="true" width="130"><i><b>Record management</b></i></td>
					<td width="590"><hr/></td>
				</tr>
			</table>
			<table cellspacing="15pts">
			<tr valign="top">
				<td nowrap="true" width="175"><span class="head0">Verified:</span>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_RMVERIFIED</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
					<br/>(dd/mm/yyyy)
				</td>
				<td><input type="text" size="25" maxlength="100" onChange="checkDate(this)">
					<xsl:attribute name="name"><xsl:value-of select="RM_VERIFIED/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="RM_VERIFIED"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="175"><span class="head0">Verified by:</span>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_RMVERIFIEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				</td>
				<td><input type="text" size="25" maxlength="100" onChange="changed()">
					<xsl:attribute name="name"><xsl:value-of select="RM_VERIFIED_BY/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="RM_VERIFIED_BY"/></xsl:attribute>
				</input></td>
			</tr>
			<tr valign="top">
				<td nowrap="true" width="175"><span class="head0">Next update due:</span>
					<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_RMNEXTUPDATEDUE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
					<br/>(dd/mm/yyyy)
				</td>
				<td><input type="text" size="25" maxlength="100" onChange="checkDate(this)">
					<xsl:attribute name="name"><xsl:value-of select="RM_NEXT_UPDATE/@XPath"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="RM_NEXT_UPDATE"/></xsl:attribute>
				</input></td>
			</tr>
			</table>

			<hr width="700"/>
			<div style="margin-left:20">
				<table cellspacing="7"><tr>
					<td><input type="button" onclick="save()" value="Save changes" width="100" style="width:100"/></td>
			             <td><input type="button" onclick='history.back()' value="Exit"/></td>
<!--					<td><input type="button" onclick="add()" value="New record" width="100" style="width:100"/></td>

					<td><input type="button" onclick="del()" value="Delete" width="100" style="width:100"/></td>
-->
				</tr></table>
			</div>
			</form>
		<script language="JavaScript">
			
<![CDATA[

function changeIssuer(oControl) {
	document.f.elements["]]><xsl:value-of select='ISSUED_BY_URL/@XPath'/><![CDATA["].value=oControl.options[oControl.selectedIndex].url;
	changed();
}
function add() {
	var elem;
	var i, j;

	isChanged = true;

	// reset all form elements except hidden fields
	for (i = 0; i < document.f.length; ++i) {
		elem = document.f.elements[i];

		if (elem.type == 'select-multiple' ||
			 elem.type == 'select-one') {
			 elem.selectedIndex = -1;
		}
		else if (elem.type == 'text' ||
					elem.type == 'textarea') {
			elem.value = '';
		}
	}

	// transfer the content of registered selects from linked values to the value pool
	for (i = 0; i < selects.length; ++i) {
		var lnk = selects[i][0];
		selValues(lnk);
		mvValues(lnk, selects[i][1]);
	}

	document.f.elements["dom-update-mode"].value="A";
}


var lnkParent = document.f.elements["/XmlData/RowSet[@Name='Source']/Row/SubSet[@Name='LnkParent']/Row/T_SOURCE_LNK/FK_SOURCE_PARENT_ID"].options;
var parentLst = document.f.parent_list.options;

inclSelect(lnkParent, parentLst);
compField("legislation title", document.f.elements["/XmlData/RowSet[@Name='Source']/Row/T_SOURCE/TITLE"]);
]]>
		</script>
	</xsl:template> 
	
	<xsl:template match="SubSet[@Name='LnkParent']">
		<select multiple="true" size="4" style="width:300" width="300">
			<xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkParent']/@XPath"/>/Row/T_SOURCE_LNK/FK_SOURCE_PARENT_ID</xsl:attribute>
			<xsl:for-each select="Row">
				<option><xsl:attribute name="value"><xsl:value-of select="T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/></xsl:attribute>
				<xsl:if test="string-length(T_SOURCE_CLASS/CLASSIFICATOR)>0">
					<xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/>&#160;
				</xsl:if>
				<xsl:value-of select="substring(T_SOURCE_CLASS/CLASS_NAME, 0, 40 - string-length(T_SOURCE_CLASS/CLASSIFICATOR))"/></option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="RowSet[@Name='SOURCE_CLASS']">
		<select multiple="true" size="4" name="parent_list" style="width:300" width="300">
			<xsl:for-each select="T_SOURCE_CLASS">
				<option><xsl:attribute name="value"><xsl:value-of select="PK_CLASS_ID"/></xsl:attribute>
				<xsl:if test="string-length(CLASSIFICATOR)>0">
					<xsl:value-of select="CLASSIFICATOR"/>&#160;
				</xsl:if>
				<xsl:value-of select="substring(CLASS_NAME, 0, 40 - string-length(CLASSIFICATOR))"/></option>
			</xsl:for-each>
		</select>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Sources']">
		<xsl:for-each select="T_SOURCE">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="PK_SOURCE_ID"/></xsl:attribute>
				<xsl:choose>
					<xsl:when test="ALIAS != '' and ../ALIAS != '...'">
						<xsl:value-of select="ALIAS"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="TITLE"/>
					</xsl:otherwise>
				</xsl:choose>
			</option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Yes/No']">
		<option/>
		<xsl:for-each select="T_LOOKUP">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="C_VALUE"/></xsl:attribute>
				<xsl:if test="C_VALUE=//RowSet/Row/T_SOURCE/DRAFT">
					<xsl:attribute name="selected">true</xsl:attribute>
				</xsl:if>
				<xsl:value-of select="C_TERM"/>
			</option>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>
