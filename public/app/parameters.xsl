<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="pagetitle">
		Overview of parameters
	</xsl:variable>
<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="src-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID"/>
	</xsl:variable>
	<xsl:variable name="ra-id">
		<xsl:value-of select="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID"/>
	</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
	<div class="breadcrumbhead">You are here:</div>
	<div class="breadcrumbitem"><a href="http://www.eionet.eu.int">EIONET</a></div>
	<div class="breadcrumbitem"><a href="index.html">ROD</a></div>
	<div class="breadcrumbitem"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$src-id"/>&amp;mode=S</xsl:attribute>
Legislative instrument</a></div>
	<div class="breadcrumbitem"><a><xsl:attribute name="href">show.jsv?id=<xsl:value-of select="$ra-id"/>&amp;aid=<xsl:value-of select="$src-id"/>&amp;mode=A</xsl:attribute> Reporting obligation</a></div>
	<div class="breadcrumbitemlast">Parameters</div>
	<div class="breadcrumbtail">&#160;</div>
</div>
</xsl:template>


<xsl:template match="XmlData">
	<!-- page -->
	<div id="workarea">

	<xsl:for-each select="//RowSet[@Name='Activity']/Row">
	<table border="0" style="float:right">
		<tr>
			<td align="right">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_PARAMETERS</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
			</td>
		</tr>
	</table>
	<h1>Overview of parameters</h1>
	<table>
		<tr valign="top">
			<td align="right"><span class="head0">Reporting obligation:</span></td>
			<td ><xsl:value-of select="T_OBLIGATION/TITLE"/></td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Reporting frequency:</span></td>
			<td>
				<xsl:call-template name="RAReportingFrequency"/>
			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Client organisation:</span></td>
			<td>
					<a>
						<xsl:attribute name="href">javascript:openPopup('client.jsv','id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a>
			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Other clients using this reporting:</span></td>
			<td>
				<xsl:for-each select="SubSet[@Name='CCClients']/Row">
					<a>
						<xsl:attribute name="href">javascript:openPopup('client.jsv','id=<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a><br/>
				</xsl:for-each>

			</td>
		</tr>
		<tr valign="top">
			<td align="right"><span class="head0">Reporting guidelines:</span></td>
			<td>
				<xsl:choose>
					<xsl:when test="T_OBLIGATION/REPORT_FORMAT_URL!=''">
						<a target="RA_guidelines">
							<xsl:attribute name="href"><xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/></xsl:attribute>
							<xsl:choose>
								<xsl:when test="T_OBLIGATION/FORMAT_NAME!=''">
									<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="T_OBLIGATION/REPORT_FORMAT_URL"/>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="T_OBLIGATION/FORMAT_NAME"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</table>

	<br/>
	 
<!-- oneCountry=0 one country, one country = 1 all countries -->
<xsl:if test="T_OBLIGATION/PARAMETERS != ''">
<table border="1">

<tr>
	<th width="100%" style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-left: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span title="Parameters" class="head0">&#160;Parameters</span>
	</th>
</tr>
<tr>
	<td style="border-left: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid"  valign="top">
		<xsl:call-template name="break">
	     <xsl:with-param name="text" select="T_OBLIGATION/PARAMETERS"/>
		</xsl:call-template>
	</td>
</tr>

</table>
</xsl:if>

</xsl:for-each>

<xsl:if test="count(//RowSet[@Name='Activity']/Row/DDPARAM) > 0">
<br/>
<table border="0">
	<tr>
		<th colspan="3" width="100%" style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span class="head0">&#160;Parameters from Data Dictionary</span>
		</th>
	</tr>
	<tr>
		<td width="40%" style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span class="headsmall">&#160;Parameter name</span>
		</td>
		<td width="30%" style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span class="headsmall">&#160;Table name</span>
		</td>
		<td style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="center" bgcolor="#ffffff" align="left">
			<span class="headsmall">&#160;Dataset name</span>
		</td>
	</tr>
	<xsl:for-each select="//RowSet[@Name='Activity']/Row/DDPARAM">
		<tr>
			<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">even</xsl:if></xsl:attribute>
			<td style="border-left: #008080 1px solid; border-bottom: #c0c0c0 1px solid" valign="top">
				<span class="rowitem"><a href="{ELEMENT_URL}" title="View parameter details in Data Dictionary" target="_blank"><xsl:value-of select="ELEMENT_NAME"/></a></span>
			</td>
			<td style="border-left: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid"  valign="top">
				<span class="rowitem"><xsl:value-of select="TABLE_NAME"/></span>
			</td>
			<td style="border-left: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid; border-right: #008080 1px solid"  valign="top">
				<span class="rowitem"><xsl:value-of select="DATASET_NAME"/></span>
			</td>
		</tr>
	</xsl:for-each>

</table>
<br/>
</xsl:if>

</div><!-- workarea -->
<xsl:call-template name="CommonFooter"/>

</xsl:template>
</xsl:stylesheet>
