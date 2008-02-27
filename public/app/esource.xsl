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
  <xsl:variable name="pagetitle">
		Edit/Create a Legislative Instrument
	</xsl:variable>
  <xsl:variable name="col_class">
		twocolumns
	</xsl:variable>
  <xsl:include href="editor.xsl"/>
  <xsl:variable name="permissions">
    <xsl:value-of select="/XmlData/RowSet/@permissions"/>
  </xsl:variable>
  <xsl:template name="breadcrumbs">
    <div class="breadcrumbtrail">
      <div class="breadcrumbhead">You are here:</div>
      <div class="breadcrumbitem eionetaccronym">
        <a href="http://www.eionet.europa.eu">Eionet</a>
      </div>
      <div class="breadcrumbitem">
        <a href="index.html">ROD</a>
      </div>
      <div class="breadcrumbitemlast">Legislative instrument</div>
      <div class="breadcrumbtail"/>
    </div>
  </xsl:template>
  <xsl:template match="XmlData">
<!-- page -->
    <div id="workarea">
      <xsl:apply-templates select="RowSet[@Name='Source']/Row/T_SOURCE"/>
    </div>
  </xsl:template>
  <xsl:template name="PageHelp">
    <a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp('HELP_LI')" onclick="pop(this.href);return false;">
      <span>Page help</span>
    </a>
  </xsl:template>
  <xsl:template match="RowSet[@Name='Source']/Row/T_SOURCE">
<!-- page title -->
    <h1>Edit/Create a Legislative Instrument</h1>
    <form name="f" method="POST" action="source.jsv">
      <input type="hidden" name="silent" value="0"/>
<!--silent save -->
      <input type="hidden" name="dom-update-mode">
        <xsl:attribute name="value">
          <xsl:choose>
            <xsl:when test="//RowSet[@Name='Source'][@skeleton='1']">A</xsl:when>
            <xsl:otherwise>U</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
      </input>
      <input type="hidden">
        <xsl:attribute name="name">
          <xsl:value-of select="PK_SOURCE_ID/@XPath"/>
        </xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="PK_SOURCE_ID"/>
        </xsl:attribute>
      </input>
      <fieldset>
        <legend>Identification</legend>
        <table border="0" cellspacing="0" width="100%" cellpadding="2">
          <col style="width: 19%"/>
          <col style="width: 7%"/>
          <col style="width: 74%"/>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">Legal name</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_LEGALNAME</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <textarea rows="4" cols="47" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="TITLE/@XPath"/>
                </xsl:attribute>
                <xsl:value-of select="TITLE"/>
              </textarea>
            </td>
          </tr>
          <tr>
            <td class="border_right">
              <span class="label">Short name</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_ALIASNAME</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="255" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="ALIAS/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="ALIAS"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">Identification number</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_IDENTIFICATIONNUMBER</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="38%">
                    <input type="text" size="13" maxlength="50" style="width:145" onchange="changed()">
                      <xsl:attribute name="name">
                        <xsl:value-of select="SOURCE_CODE/@XPath"/>
                      </xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="SOURCE_CODE"/>
                      </xsl:attribute>
                    </input>
                  </td>
                  <td width="11%">
                    <span class="label">Draft</span>
                  </td>
                  <td width="12%">
                    <xsl:call-template name="Help">
                      <xsl:with-param name="id">HELP_LI_DRAFT</xsl:with-param>
                      <xsl:with-param name="perm">
                        <xsl:value-of select="$permissions"/>
                      </xsl:with-param>
                    </xsl:call-template>
                  </td>
                  <td width="47%">
                    <select style="width:50">
                      <xsl:attribute name="name">
                        <xsl:value-of select="DRAFT/@XPath"/>
                      </xsl:attribute>
                      <xsl:apply-templates select="//RowSet[@Name='Yes/No']"/>
                    </select>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td class="border_right">
              <span class="label">URL to official text</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_URLTOOFFICIALSOURCE</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="255" class="es_object" onchange="chkUrl(this)">
                <xsl:attribute name="name">
                  <xsl:value-of select="URL/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="URL"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">CELEX reference</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_CELEXREFERENCE</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="255" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="CELEX_REF/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="CELEX_REF"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
          <tr>
            <td class="border_right">
              <span class="label">Issued by</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_ISSUEDBY</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                  <td>
                    <xsl:variable name="selIssuer">
                      <xsl:value-of select="../T_CLIENT_LNK/FK_CLIENT_ID"/>
                    </xsl:variable>
                    <select class="es_object" onchange="changeIssuer(this)">
                      <xsl:attribute name="name">
                        <xsl:value-of select="FK_CLIENT_ID/@XPath"/>
                      </xsl:attribute>
                      <option value=""/>
                      <xsl:for-each select="//RowSet[@Name='Issuer']/Row">
                        <option>
                          <xsl:attribute name="value">
                            <xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>
                          </xsl:attribute>
                          <xsl:if test="T_CLIENT/PK_CLIENT_ID = $selIssuer">
                            <xsl:attribute name="selected">selected</xsl:attribute>
                          </xsl:if>
                          <xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
                        </option>
                      </xsl:for-each>
                    </select>
                  </td>
                  <td><map name="newIssuerMap"><area alt="Add a new client to client list" shape="rect" coords="0,0,23,15" href="javascript:addCl()"/></map>
										&#xA0;<img border="0" height="15" width="23" src="images/but_new_blue.jpg" usemap="#newIssuerMap" alt=""/></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">URL to issuer</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_URLTOISSUER</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="255" class="es_object" onchange="chkUrl(this)">
                <xsl:attribute name="name">
                  <xsl:value-of select="ISSUED_BY_URL/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="ISSUED_BY_URL"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
          <tr>
            <td class="border_right">
              <span class="label">Parent legislative instrument</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_PARENTLEGALINSTRUMENT</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <select name="" class="es_object">
                <xsl:attribute name="name">
                  <xsl:value-of select="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID/@XPath"/>
                </xsl:attribute>
                <option/>
                <xsl:if test="../T_SOURCE_LNK/FK_SOURCE_PARENT_ID!=''">
                  <option selected="selected">
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
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">Eur-lex categories</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_CLASSIFICATION</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3"/>
          </tr>
          <tr>
            <td colspan="5">
              <table>
                <tr valign="middle">
                  <td width="240" align="center">
                    <xsl:apply-templates select="//RowSet[@Name='SOURCE_CLASS']"/>
                  </td>
                  <td width="100" nowrap="nowrap">
                    <table cellspacing="5">
                      <tr>
                        <td width="100" align="center">
                          <input type="button" style="width:80" onclick="mvValues(parentLst, lnkParent, null)" value="&#xA0;&#xA0;-&gt;&#xA0;&#xA0;"/>
                        </td>
                      </tr>
                      <tr>
                        <td width="100" align="center">
                          <input type="button" style="width:80" onclick="mvValues(lnkParent, parentLst, null)" value="&#xA0;&#xA0;&lt;-&#xA0;&#xA0;"/>
                          <br/>
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
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">DG Env review of reporting theme</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_DGENVREVIEW</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <select class="es_object">
                <xsl:attribute name="name">
                  <xsl:value-of select="DGENV_REVIEW/@XPath"/>
                </xsl:attribute>
                <xsl:apply-templates select="//RowSet[@Name='DGEnv']"/>
              </select>
            </td>
          </tr>
          <tr>
            <td class="border_right">
              <span class="label">Valid from</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_VALIDFROM</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="108">
                    <input type="text" size="8" maxlength="10" style="width:100" onchange="checkDate(this)">
                      <xsl:attribute name="name">
                        <xsl:value-of select="VALID_FROM/@XPath"/>
                      </xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="VALID_FROM"/>
                      </xsl:attribute>
                    </input>
                  </td>
                  <td>
                    <span class="smallfont">(dd/mm/yyyy)</span>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">Geographic scope</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_GSCOPE</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="255" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="GEOGRAPHIC_SCOPE/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="GEOGRAPHIC_SCOPE"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
          <tr valign="top">
            <td class="border_right">
              <span class="label">Abstract</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_ABSTRACT</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <textarea rows="10" cols="47" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="ABSTRACT/@XPath"/>
                </xsl:attribute>
                <xsl:value-of select="ABSTRACT"/>
              </textarea>
            </td>
          </tr>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">Comments</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_COMMENT</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <textarea rows="4" cols="47" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="COMMENT/@XPath"/>
                </xsl:attribute>
                <xsl:value-of select="COMMENT"/>
              </textarea>
            </td>
          </tr>
        </table>
      </fieldset>
      <fieldset>
        <legend>Conventions</legend>
        <table border="0" cellspacing="0" width="100%" cellpadding="2">
          <col style="width: 19%"/>
          <col style="width: 7%"/>
          <col style="width: 74%"/>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">EC entry into force </span>
              <span class="smallfont">(dd/mm/yyyy)</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_ECENTRYINTOFORCE</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="34%">
                    <input type="text" size="10" maxlength="10" style="width:100" onchange="checkDate(this)">
                      <xsl:attribute name="name">
                        <xsl:value-of select="EC_ENTRY_INTO_FORCE/@XPath"/>
                      </xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="EC_ENTRY_INTO_FORCE"/>
                      </xsl:attribute>
                    </input>
                  </td>
                  <td width="24%">
                    <span class="label">EC accession </span>
                    <span class="smallfont">(dd/mm/yyyy)</span>
                  </td>
                  <td width="12%">
                    <xsl:call-template name="Help">
                      <xsl:with-param name="id">HELP_LI_ECACCESSION</xsl:with-param>
                      <xsl:with-param name="perm">
                        <xsl:value-of select="$permissions"/>
                      </xsl:with-param>
                    </xsl:call-template>
                  </td>
                  <td>
                    <input type="text" size="10" maxlength="10" style="width:100" onchange="checkDate(this)">
                      <xsl:attribute name="name">
                        <xsl:value-of select="EC_ACCESSION/@XPath"/>
                      </xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="EC_ACCESSION"/>
                      </xsl:attribute>
                    </input>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr class="zebraodd">
            <td class="border_right">
              <span class="label">Secretariat</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_SECRETARIAT</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="255" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="SECRETARIAT/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="SECRETARIAT"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">URL to Secretariat homepage</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_SECRETARIATHOMEPAGE</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="255" class="es_object" onchange="chkUrl(this)">
                <xsl:attribute name="name">
                  <xsl:value-of select="SECRETARIAT_URL/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="SECRETARIAT_URL"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
        </table>
      </fieldset>
      <fieldset>
        <legend>Record management</legend>
        <table border="0" cellspacing="0" width="100%" cellpadding="2">
          <col style="width: 19%"/>
          <col style="width: 7%"/>
          <col style="width: 74%"/>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">Verified</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_RMVERIFIED</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="108">
                    <input type="text" size="10" maxlength="10" style="width:100" onchange="checkDate(this)">
                      <xsl:attribute name="name">
                        <xsl:value-of select="RM_VERIFIED/@XPath"/>
                      </xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="RM_VERIFIED"/>
                      </xsl:attribute>
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
            <td class="border_right">
              <span class="label">Verified by</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_RMVERIFIEDBY</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="100" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="RM_VERIFIED_BY/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="RM_VERIFIED_BY"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
          <tr class="zebraeven">
            <td class="border_right">
              <span class="label">Next update due</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_RMNEXTUPDATEDUE</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="108">
                    <input type="text" size="10" maxlength="10" style="width:100" onchange="checkDate(this)">
                      <xsl:attribute name="name">
                        <xsl:value-of select="RM_NEXT_UPDATE/@XPath"/>
                      </xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="RM_NEXT_UPDATE"/>
                      </xsl:attribute>
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
            <td class="border_right">
              <span class="label">Validated by</span>
            </td>
            <td class="border_right center">
              <xsl:call-template name="Help">
                <xsl:with-param name="id">HELP_LI_RMVALIDATEDBY</xsl:with-param>
                <xsl:with-param name="perm">
                  <xsl:value-of select="$permissions"/>
                </xsl:with-param>
              </xsl:call-template>
            </td>
            <td colspan="3">
              <input type="text" size="56" maxlength="100" class="es_object" onchange="changed()">
                <xsl:attribute name="name">
                  <xsl:value-of select="RM_VALIDATED_BY/@XPath"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="RM_VALIDATED_BY"/>
                </xsl:attribute>
              </input>
            </td>
          </tr>
        </table>
      </fieldset>
      <table border="0" cellspacing="0" width="100%" cellpadding="2">
        <col style="width: 19%"/>
        <col style="width: 7%"/>
        <col style="width: 74%"/>
        <tr>
          <td colspan="5" align="center" valign="middle" class="save"><input class="btn" onclick="save(null,false)" type="button" value="Save changes"/>
					&#xA0;
	   		   		<input class="btn" onclick="history.back()" type="button" value="Exit"/></td>
        </tr>
      </table>
    </form>
    <script type="text/javascript"><![CDATA[

function changeIssuer(oControl) {
	document.f.elements["]]><xsl:value-of select="ISSUED_BY_URL/@XPath"/><![CDATA["].value=oControl.options[oControl.selectedIndex].url;
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
]]></script>
  </xsl:template>
  <xsl:template match="SubSet[@Name='LnkParent']">
    <select multiple="multiple" size="4" style="width:240">
      <xsl:attribute name="name"><xsl:value-of select="//SubSet[@Name='LnkParent']/@XPath"/>/Row/T_SOURCE_LNK/FK_SOURCE_PARENT_ID</xsl:attribute>
      <xsl:for-each select="Row">
        <option>
          <xsl:attribute name="value">
            <xsl:value-of select="T_SOURCE_LNK/FK_SOURCE_PARENT_ID"/>
          </xsl:attribute>
          <xsl:if test="string-length(T_SOURCE_CLASS/CLASSIFICATOR)&gt;0"><xsl:value-of select="T_SOURCE_CLASS/CLASSIFICATOR"/>&#xA0;
				</xsl:if>
          <xsl:value-of select="substring(T_SOURCE_CLASS/CLASS_NAME, 0, 40 - string-length(T_SOURCE_CLASS/CLASSIFICATOR))"/>
        </option>
      </xsl:for-each>
    </select>
  </xsl:template>
  <xsl:template match="RowSet[@Name='SOURCE_CLASS']">
    <select multiple="multiple" size="4" name="parent_list" style="width:240">
      <xsl:for-each select="T_SOURCE_CLASS">
        <option>
          <xsl:attribute name="value">
            <xsl:value-of select="PK_CLASS_ID"/>
          </xsl:attribute>
          <xsl:if test="string-length(CLASSIFICATOR)&gt;0"><xsl:value-of select="CLASSIFICATOR"/>&#xA0;
				</xsl:if>
          <xsl:value-of select="substring(CLASS_NAME, 0, 40 - string-length(CLASSIFICATOR))"/>
        </option>
      </xsl:for-each>
    </select>
  </xsl:template>
  <xsl:template match="RowSet[@Name='Sources']">
    <xsl:for-each select="T_SOURCE">
      <option>
        <xsl:attribute name="value">
          <xsl:value-of select="PK_SOURCE_ID"/>
        </xsl:attribute>
        <xsl:value-of select="TITLE"/>
      </option>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="RowSet[@Name='Yes/No']">
    <option/>
    <xsl:for-each select="T_LOOKUP">
      <option>
        <xsl:attribute name="value">
          <xsl:value-of select="C_VALUE"/>
        </xsl:attribute>
        <xsl:if test="C_VALUE=//RowSet/Row/T_SOURCE/DRAFT">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        <xsl:value-of select="C_TERM"/>
      </option>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="RowSet[@Name='DGEnv']">
    <option/>
    <xsl:for-each select="T_LOOKUP">
      <option>
        <xsl:attribute name="value">
          <xsl:value-of select="C_VALUE"/>
        </xsl:attribute>
        <xsl:if test="C_VALUE=//RowSet/Row/T_SOURCE/DGENV_REVIEW">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        <xsl:value-of select="C_TERM"/>
      </option>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="createURL"/>
</xsl:stylesheet>
