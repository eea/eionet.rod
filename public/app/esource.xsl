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

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>

	<xsl:template match="XmlData">
		<xsl:apply-templates select="RowSet[@Name='Source']/Row/T_SOURCE"/>
	</xsl:template>

	<xsl:template match="RowSet[@Name='Source']/Row/T_SOURCE">
		<form name="f" method="POST" action="source.jsv">
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

      <div align="left">
     	<table border="0" width="621" cellpadding="0">
		<tr>
			<td width="471">
				<span class="headgreen" id="lblTitle">Edit/Create a Legislative Instrument</span>
			</td>
			<td>
				<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_LI</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
			</td>
		</tr>
      <tr>
      	<td width="615" height="10" colspan="3"></td>
     	</tr>
      <tr>
	      <td width="615" style="border: 1 solid #006666" colspan="3">
   		   <table border="0" cellspacing="0" width="100%" cellpadding="2">
			      <tr valign="top" bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Legal name</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_LEGALNAME</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<textarea rows="4" cols="47" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="TITLE/@XPath"/></xsl:attribute>
								<xsl:value-of select="TITLE"/>
							</textarea>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Short name</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ALIASNAME</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="255" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="ALIAS/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="ALIAS"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Identification number</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_IDENTIFICATIONNUMBER</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
	                 <table border="0" width="100%" cellspacing="0" cellpadding="0">
   	                 <tr>
                          <td width="38%">
										<input type="text" size="13" maxlength="50" style="width:145" onChange="changed()">
											<xsl:attribute name="name"><xsl:value-of select="SOURCE_CODE/@XPath"/></xsl:attribute>
											<xsl:attribute name="value"><xsl:value-of select="SOURCE_CODE"/></xsl:attribute>
										</input>
                          </td>
                          <td width="11%"><span class="head0">Draft</span></td>
                          <td width="12%">
										<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_DRAFT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
                          </td>
                          <td width="47%">
										<select style="width:50">
											<xsl:attribute name="name"><xsl:value-of select="DRAFT/@XPath"/></xsl:attribute>
											<xsl:apply-templates select="//RowSet[@Name='Yes/No']"/>
										</select>
						         </td>
								</tr>
							</table>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">URL to official text</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_URLTOOFFICIALSOURCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="255" style="width:400" onChange="chkUrl(this)">
								<xsl:attribute name="name"><xsl:value-of select="URL/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="URL"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">CELEX reference</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_CELEXREFERENCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="255" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="CELEX_REF/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="CELEX_REF"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Issued by</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ISSUEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
	                 <table border="0" width="100%" cellspacing="0" cellpadding="0">
   	                 <tr>
   	                 		<td>
										<xsl:variable name="selIssuer">
											<xsl:value-of select="../T_CLIENT_LNK/FK_CLIENT_ID"/>
										</xsl:variable>
										<select style="width:400" maxlength="255" onChange="changeIssuer(this)">
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
									</td>
									<td>
										<map name="newIssuerMap">
											<area alt="Add a new client to client list" shape="rect" coords="0,0,23,15" href="javascript:openAddClientWin()"></area>
										</map>
										&#160;<img border="0" height="15" width="23" src="images/but_new_blue.jpg" usemap="#newIssuerMap"></img>
									</td>
								</tr>
							</table>
						</td>
			      </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">URL to issuer</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_URLTOISSUER</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="255" style="width:400" onChange="chkUrl(this)">
								<xsl:attribute name="name"><xsl:value-of select="ISSUED_BY_URL/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="ISSUED_BY_URL"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Parent legislative instrument</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_PARENTLEGALINSTRUMENT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<select name="" style="width:400">
								<xsl:attribute name="name"><xsl:value-of select="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID/@XPath"/></xsl:attribute>
								<option/>
								<xsl:if test="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
									<option selected="true">
										<xsl:attribute name="value">
											<xsl:value-of select="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/>
										</xsl:attribute>
										<xsl:value-of select="../PARENT/TITLE"/>
									</option>
								</xsl:if>
								<xsl:apply-templates select="//RowSet[@Name='Sources']"/>
							</select>
						</td>
			      </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Eur-lex categories</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_CLASSIFICATION</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3"></td>
			      </tr>
			      <tr>
				      <td width="83%" colspan="5">
							<table>
								<tr valign="middle">
									<td width="240" align="center">
										<xsl:apply-templates select="//RowSet[@Name='SOURCE_CLASS']"/>
									</td>
									<td width="100" nowrap="true">
										<table cellspacing="5">
											<tr>
												<td width="100" align="center">
													<input type="button" width="80" style="width:80" onclick="mvValues(parentLst, lnkParent, null)" value="&#160;&#160;-&gt;&#160;&#160;"/>
												</td>
											</tr>
											<tr>
												<td width="100" align="center">
													<input type="button" width="80" style="width:80" onclick="mvValues(lnkParent, parentLst, null)" value="&#160;&#160;&lt;-&#160;&#160;"/><br/>
												</td>
											</tr>
										</table>
									</td>
									<td width="240" align="center">
										<xsl:apply-templates select="../SubSet[@Name='LnkParent']"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">DG Env review of reporting theme</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_DGENVREVIEW</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<select style="width:400">
								<xsl:attribute name="name"><xsl:value-of select="DGENV_REVIEW/@XPath"/></xsl:attribute>
								<xsl:apply-templates select="//RowSet[@Name='DGEnv']"/>
							</select>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Valid from</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_VALIDFROM</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
	                 <table border="0" width="100%" cellspacing="0" cellpadding="0">
   	                 <tr>
   	                 		<td width="108">
										<input type="text" size="8" maxlength="10" style="width:100" onChange="checkDate(this)">
											<xsl:attribute name="name"><xsl:value-of select="VALID_FROM/@XPath"/></xsl:attribute>
											<xsl:attribute name="value"><xsl:value-of select="VALID_FROM"/></xsl:attribute>
										</input>
									</td>
									<td>
										<span class="smallfont">(dd/mm/yyyy)</span>
									</td>
								</tr>
							</table>
						</td>
			      </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Geographic scope</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_GSCOPE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="255" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="GEOGRAPHIC_SCOPE/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="GEOGRAPHIC_SCOPE"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr valign="top">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Abstract</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ABSTRACT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<textarea rows="10" cols="47" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="ABSTRACT/@XPath"/></xsl:attribute>
								<xsl:value-of select="ABSTRACT"/>
							</textarea>
						</td>
			      </tr>
			      <tr valign="top" bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Comments</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_COMMENT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<textarea rows="4" cols="47" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="COMMENT/@XPath"/></xsl:attribute>
								<xsl:value-of select="COMMENT"/>
							</textarea>
						</td>
			      </tr>
               <tr>
        	      	<td width="26%" valign="middle" colspan="2" bgcolor="#B7DBDB" height="40">
        	      		<span class="head0">Conventions</span>
        	      	</td>
               	<td width="57%" valign="middle" colspan="3" bgcolor="#B7DBDB" height="40">
               		<hr noshade="true" color="#006666"/>
               	</td>
               </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">EC entry into force </span><span class="smallfont">(dd/mm/yyyy)</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ECENTRYINTOFORCE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
	                 <table border="0" width="100%" cellspacing="0" cellpadding="0">
   	                 <tr>
                          <td width="34%">
										<input type="text" size="10" maxlength="10" style="width:100" onChange="checkDate(this)">
											<xsl:attribute name="name"><xsl:value-of select="EC_ENTRY_INTO_FORCE/@XPath"/></xsl:attribute>
											<xsl:attribute name="value"><xsl:value-of select="EC_ENTRY_INTO_FORCE"/></xsl:attribute>
										</input>
                          </td>
                          <td width="24%">
        						      <span class="head0">EC accession </span><span class="smallfont">(dd/mm/yyyy)</span> 
                          </td>
                          <td width="12%">
										<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_ECACCESSION</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
                          </td>
                          <td>
										<input type="text" size="10" maxlength="10" style="width:100" onChange="checkDate(this)">
											<xsl:attribute name="name"><xsl:value-of select="EC_ACCESSION/@XPath"/></xsl:attribute>
											<xsl:attribute name="value"><xsl:value-of select="EC_ACCESSION"/></xsl:attribute>
										</input>
						         </td>
								</tr>
							</table>
						</td>
			      </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Secretariat</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_SECRETARIAT</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="255" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="SECRETARIAT/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="SECRETARIAT"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">URL to Secretariat homepage</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_SECRETARIATHOMEPAGE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="255" style="width:400" onChange="chkUrl(this)">
								<xsl:attribute name="name"><xsl:value-of select="SECRETARIAT_URL/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="SECRETARIAT_URL"/></xsl:attribute>
							</input>
						</td>
			      </tr>
               <tr>
        	      	<td width="26%" valign="middle" colspan="2" bgcolor="#B7DBDB" height="40">
        	      		<span class="head0">Record management</span>
        	      	</td>
               	<td width="57%" valign="middle" colspan="3" bgcolor="#B7DBDB" height="40">
               		<hr noshade="true" color="#006666"/>
               	</td>
               </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Verified</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_RMVERIFIED</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
	                 <table border="0" width="100%" cellspacing="0" cellpadding="0">
   	                 <tr>
   	                 		<td width="108">
										<input type="text" size="10" maxlength="10" style="width:100" onChange="checkDate(this)">
											<xsl:attribute name="name"><xsl:value-of select="RM_VERIFIED/@XPath"/></xsl:attribute>
											<xsl:attribute name="value"><xsl:value-of select="RM_VERIFIED"/></xsl:attribute>
										</input>
									</td>
									<td>
										<span class="smallfont">(dd/mm/yyyy)</span>
									</td>
								</tr>
							</table>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Verified by</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_RMVERIFIEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="100" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="RM_VERIFIED_BY/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RM_VERIFIED_BY"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr bgcolor="#FFFFFF">
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Next update due</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_RMNEXTUPDATEDUE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
	                 <table border="0" width="100%" cellspacing="0" cellpadding="0">
   	                 <tr>
   	                 		<td width="108">
										<input type="text" size="10" maxlength="10" style="width:100" onChange="checkDate(this)">
											<xsl:attribute name="name"><xsl:value-of select="RM_NEXT_UPDATE/@XPath"/></xsl:attribute>
											<xsl:attribute name="value"><xsl:value-of select="RM_NEXT_UPDATE"/></xsl:attribute>
										</input>
									</td>
									<td>
										<span class="smallfont">(dd/mm/yyyy)</span>
									</td>
								</tr>
							</table>
						</td>
			      </tr>
			      <tr>
				      <td width="19%" style="border-right: 1 solid #C0C0C0">
					      <span class="head0">Validated by</span> 
				      </td>
				      <td width="7%" align="center" style="border-right: 1 solid #C0C0C0">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_LI_RMVALIDATEDBY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				      </td>
				      <td width="57%" colspan="3">
							<input type="text" size="56" maxlength="100" style="width:400" onChange="changed()">
								<xsl:attribute name="name"><xsl:value-of select="RM_VALIDATED_BY/@XPath"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="RM_VALIDATED_BY"/></xsl:attribute>
							</input>
						</td>
			      </tr>
			      <tr>
			      	<td colspan="5" align="center" valign="middle" height="40" style="border-top: 2 solid #B7DBDB;" bgcolor="#669999">
		      			<input style="font-weight: bold; width: 120px; color: #000000; background-image: url('images/bgr_form_buttons.jpg')" onclick="save(null,false)" type="button" value="Save changes"/>
	   		   		<input style="font-weight: bold; color: #000000; background-image: url('images/bgr_form_buttons.jpg')" onclick="history.back()" type="button" value="Exit"/>
				      </td>
			      </tr>
			   </table>
			</td>
		</tr>
     	</table>
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
		<select multiple="true" size="4" style="width:240">
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
		<select multiple="true" size="4" name="parent_list" style="width:240">
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
				<xsl:value-of select="TITLE"/>
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

	<xsl:template match="RowSet[@Name='DGEnv']">
		<option/>
		<xsl:for-each select="T_LOOKUP">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="C_VALUE"/></xsl:attribute>
				<xsl:if test="C_VALUE=//RowSet/Row/T_SOURCE/DGENV_REVIEW">
					<xsl:attribute name="selected">true</xsl:attribute>
				</xsl:if>
				<xsl:value-of select="C_TERM"/>
			</option>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>
