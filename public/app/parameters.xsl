<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="pagetitle">
		Overview of parameters
	</xsl:variable>
	
	<xsl:variable name="col_class">
		twocolumns
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
	<div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
	<div class="breadcrumbitem"><a href="index.html">ROD</a></div>
	<div class="breadcrumbitem"><a><xsl:attribute name="href">instruments/<xsl:value-of select="$src-id"/></xsl:attribute>
Legislative instrument</a></div>
	<div class="breadcrumbitem"><a><xsl:attribute name="href">obligations/<xsl:value-of select="$ra-id"/></xsl:attribute> Reporting obligation</a></div>
	<div class="breadcrumbitemlast">Parameters</div>
	<div class="breadcrumbtail"></div>
</div>
</xsl:template>

<xsl:template name="PageHelp">
	<a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp('HELP_PARAMETERS')" onclick="pop(this.href);return false;"><span>Page help</span></a>
</xsl:template>

<xsl:template match="XmlData">
	<!-- page -->
	<div id="workarea">

	<xsl:for-each select="//RowSet[@Name='Activity']/Row">
	<h1>Overview of parameters</h1>
	<table class="datatable">
		<tr valign="top">
			<th scope="row" class="scope-row">Reporting obligation:</th>
			<td ><xsl:value-of select="T_OBLIGATION/TITLE"/></td>
		</tr>
		<tr valign="top">
			<th scope="row" class="scope-row">Reporting frequency:</th>
			<td>
				<xsl:call-template name="RAReportingFrequency"/>
			</td>
		</tr>
		<tr valign="top">
			<th scope="row" class="scope-row">Client organisation:</th>
			<td>
					<a>
						<xsl:attribute name="href">clients/<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a>
			</td>
		</tr>
		<tr valign="top">
			<th scope="row" class="scope-row">Other clients using this reporting:</th>
			<td>
				<xsl:for-each select="SubSet[@Name='CCClients']/Row">
					<a>
						<xsl:attribute name="href">clients/<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/></xsl:attribute>
						<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
					</a><br/>
				</xsl:for-each>

			</td>
		</tr>
		<tr valign="top">
			<th scope="row" class="scope-row">Reporting guidelines:</th>
			<td>
				<xsl:choose>
					<xsl:when test="T_OBLIGATION/REPORT_FORMAT_URL!=''">
						<a>
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
<table border="1" class="datatable">

<tr>
	<th scope="col" class="scope-col" style="text-align: left">&#160;Parameters</th>
</tr>
<tr>
	<td valign="top">
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
<table class="datatable">
	<tr>
		<th scope="col" class="scope-col" style="text-align: left" colspan="3" width="100%">Parameters from Data Dictionary</th>
	</tr>
	<tr>
		<th scope="col" class="scope-col" style="text-align: left; border-right: 2px solid white;" width="40%">Parameter name</th>
		<th scope="col" class="scope-col" style="text-align: left; border-right: 2px solid white;" width="30%">&#160;Table name</th>
		<th scope="col" class="scope-col" style="text-align: left" width="30%">&#160;Dataset name</th>
	</tr>
	<xsl:for-each select="//RowSet[@Name='Activity']/Row/DDPARAM">
		<tr>
			<xsl:attribute name="class"><xsl:if test="position() mod 2 = 0">zebraeven</xsl:if></xsl:attribute>
			<td style="border-right: #c0c0c0 1px solid;">
				<a href="{ELEMENT_URL}" title="View parameter details in Data Dictionary"><xsl:value-of select="ELEMENT_NAME"/></a>
			</td>
			<td style="border-right: #c0c0c0 1px solid;">
				<xsl:value-of select="TABLE_NAME"/>
			</td>
			<td>
				<xsl:value-of select="DATASET_NAME"/>
			</td>
		</tr>
	</xsl:for-each>

</table>
<br/>
</xsl:if>

</div><!-- workarea -->
<xsl:call-template name="CommonFooter"/>

</xsl:template>
<xsl:template name="createURL"/>
</xsl:stylesheet>
