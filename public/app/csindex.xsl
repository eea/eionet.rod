<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="pagetitle">
		Country deadlines
	</xsl:variable>

	<xsl:include href="ncommon.xsl"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet/@auth"/>
	</xsl:variable>


	<xsl:variable name="permissions">
		<xsl:value-of select="//RowSet/@permissions"/>
	</xsl:variable>


<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitemlast">Deadlines</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

<xsl:template match="XmlData">

<script type="text/javascript">

function openHarvester(){
	var name = "CSHarvester";
	var features = "location=no, menubar=no, width=280, height=200, top=40, left=60, resizable=no, SCROLLABLE=no";
	var w = window.open( "harvester.jsv", name, features);
	w.focus();

}

var picklist = new Array();

</script>


<div id="workarea">
<form action="rorabrowse.jsv" method="get" name="f"><input value="A" name="mode" type="hidden"/></form>

<div id="operations">
	<ul>
		<li>
			<a href="cssearch">Advanced search</a>
		</li>
		<xsl:if test="contains($permissions, ',/Admin/Harvest:u,')='true'">
			<li>
				<a href="javascript:openHarvester()">Harvest</a>
			</li>
		</xsl:if>
	</ul>
</div>
<h1>Country deadlines</h1>
<p>
This part of ROD helps countries co-ordinate and manage their international
reporting obligations. It provides information about when countries have to
report, who is responsible for reporting, and to which organisation the data
set should be delivered. It is geared towards EEA member countries. You can
browse national deliveries by choosing a country below or query the contents
of ROD and CDR by using the advanced search. 
</p>


<xsl:variable name="noOfCountries"><xsl:value-of select="count(child::RowSet[@Name='Members']/Row/T_SPATIAL)"/></xsl:variable>
<table cellspacing="0" style="min-width: 600px; max-width: 900px">
<colgroup span="3" width="33%"/>
<tr valign="top">
	<th align="left" colspan="3"><b>EEA member countries </b></th>
</tr>
<tr>

<td bgcolor="#FFFFFF" style="border-left: #008080 1px solid; border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
	<xsl:if test="position() &lt; $noOfCountries div 3">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= $noOfCountries div 3 and position() &lt; ($noOfCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="top">
	<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
		<xsl:if test="position() &gt;= ($noOfCountries div 3) * 2">
			<xsl:call-template name="COUNTRYNAME"/>
		</xsl:if>
	</xsl:for-each>
</td>
</tr>

<xsl:variable name="noOfNMCountries"><xsl:value-of select="count(child::RowSet[@Name='NonMembers']/Row/T_SPATIAL)"/></xsl:variable>

<tr valign="top">
	<th colspan="3" align="left"><b>Other countries </b></th>
</tr>
<tr>
<td bgcolor="#FFFFFF" style="border-left: #008080 1px solid; border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &lt; $noOfNMCountries div 3">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= $noOfNMCountries div 3 and position() &lt; ($noOfNMCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= ($noOfNMCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td></tr>
</table>
</div>



<xsl:call-template name="CommonFooter"/>




</xsl:template>
<xsl:template name="COUNTRYNAME">
	<img src="images/Folder_icon.gif" alt=""/>
	<a><xsl:attribute name="href">csmain?COUNTRY_ID=<xsl:value-of select="PK_SPATIAL_ID"/>&amp;amp;ORD=NEXT_REPORTING, DEADLINE</xsl:attribute><xsl:value-of select="SPATIAL_NAME"/></a>
	<br/>
</xsl:template>

</xsl:stylesheet>
