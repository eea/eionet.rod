<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:include href="util.xsl"/>


	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet/@auth"/>
	</xsl:variable>


	<xsl:variable name="permissions">
		<xsl:value-of select="//RowSet/@permissions"/>
	</xsl:variable>


<xsl:template match="XmlData">

<html lang="en"><head><title>Country Services</title>
	
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/>
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript">

function openHarvester(){
	var name = "CSHarvester";
	var features = "location=no, menubar=no, width=280, height=200, top=40, left=60, resizable=no, SCROLLABLE=no";
	var w = window.open( "harvester.jsv", name, features);
	w.focus();

}



var browser = document.all ? 'E' : 'N';
var picklist = new Array();

</script></head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">
<table border="0" width="600" cellpadding="0" cellspacing="0">
<tr>
	<td height="25" background="images/bar_filled.jpg" width="20" align="bottom"></td>
	<td height="25" background="images/bar_filled.jpg" width="600">
 		 <table border="0" background="" cellPadding="0" cellSpacing="0" height="8">
		 <tr>
		  <td valign="bottom"><a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a></td>
		  <td width="28" valign="top">
				<img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td valign="bottom"><a href="index.html"><span class="barfont">ROD</span></a></td>
				<td width="28" valign="top"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td valign="bottom"><span class="barfont">Deadlines</span></td>
				<td width="28" valign="bottom"><img src="images/bar_dot.jpg" width="28" height="25"/></td>
			 </tr>
			</table>
		 </td></tr><tr><td></td></tr>
	</table>

<div style="margin-left:13"><form action="rorabrowse.jsv" method="get" name="f"><input value="A" name="mode" type="hidden"/></form><div style="margin-left:20"><table cellspacing="7pts"></table></div>
<table width="600">
<tr><td>
<table width="100%">
<tr>
	<td></td>
	<td valign="top" align="right">
		<xsl:if test="contains($permissions, ',/Admin/Harvest:u,')='true'">
				<img alt="Harvest Reportnet meta-information" border="0" src="images/bb_harvest.png" onClick="javascript:openHarvester()"></img>
		<br/>		<br/>
</xsl:if>
	<xsl:call-template name="Print"/><br/><br/>
	<a href="cssearch"><img border="0" src="images/bb_advsearch.png" alt=""/></a><br/><br/>

</td></tr>
<tr><td valign="top" colspan="2"><div class="Mainfont" align="justify">
This part of ROD helps countries co-ordinate and manage their international
reporting obligations. It provides information about when countries have to
report, who is responsible for reporting, and to which organisation the data
set should be delivered. It is geared towards EEA member countries. You can
browse national deliveries by choosing a country below or query the contents
of ROD and CDR by using the advanced search. 
<br/><br/>
</div>
</td>
</tr></table>

<table width="100%">

<xsl:variable name="noOfCountries"><xsl:value-of select="count(child::RowSet[@Name='Members']/Row/T_SPATIAL)"/></xsl:variable>
<table>
<tr valign="top">
	<td colspan="3"><b>EEA member countries </b><hr/></td>
</tr>
<tr>
<td width="200" valign="top">
<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
	<xsl:if test="position() &lt; $noOfCountries div 3">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td width="200" valign="top">
<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= $noOfCountries div 3 and position() &lt; ($noOfCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td width="200" valign="top">
<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= ($noOfCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>

</td>
</tr>
</table>

<xsl:variable name="noOfNMCountries"><xsl:value-of select="count(child::RowSet[@Name='NonMembers']/Row/T_SPATIAL)"/></xsl:variable>
<table>
<tr valign="top">
	<td colspan="3"><b>Other countries </b><hr/></td>
</tr>
<tr>
<td width="200" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &lt; $noOfNMCountries div 3">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td width="200" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= $noOfNMCountries div 3 and position() &lt; ($noOfNMCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td width="200" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= ($noOfNMCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>

</td>
</tr>
</table>


<xsl:call-template name="CommonFooter"/>
</table>

<br/>
</td></tr>
</table>
</div>

	</body>
	</html>



</xsl:template>
<xsl:template name="COUNTRYNAME">
		<img src="images/Folder_icon.gif" width="16" height="16"/>
		<a><xsl:attribute name="href">csmain?COUNTRY_ID=<xsl:value-of select="PK_SPATIAL_ID"/>&#038;ORD=NEXT_DEADLINE</xsl:attribute><xsl:value-of select="SPATIAL_NAME"/></a>
		<br/>
</xsl:template>

</xsl:stylesheet>
