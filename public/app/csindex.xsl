<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet/@auth"/>
	</xsl:variable>


	<xsl:variable name="permissions">
		<xsl:value-of select="//RowSet/@permissions"/>
	</xsl:variable>


<xsl:template match="XmlData">

<html lang="en"><head><title>Country Services</title>
	
	<script language="JavaScript">

function openHarvester(){
	var name = "CSHarvester";
	var features = "location=no, menubar=no, width=280, height=200, top=40, left=60, resizable=no, SCROLLABLE=no";
	var w = window.open( "harvester.jsv", name, features);
	w.focus();

}

var picklist = new Array();

</script></head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

<xsl:if test="$printmode='N'">
<table border="0" width="600" cellpadding="0" cellspacing="0">
<tr>
	<td height="25" background="images/bar_filled.jpg" width="20" align="bottom"></td>
	<td height="25" background="images/bar_filled.jpg" width="600">
 		 <table border="0" background="" cellpadding="0" cellspacing="0" height="8">
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
</xsl:if>
<div style="margin-left:13"><form action="rorabrowse.jsv" method="get" name="f"><input value="A" name="mode" type="hidden"/></form><div style="margin-left:20"><table cellspacing="7pts"></table></div>
<table width="600">
<tr><td>
<table width="100%">
<tr>
	<td width="77%">&#160;</td>
	<td align="right"><xsl:call-template name="Print"/></td>
</tr>
<xsl:if test="$printmode='N'">
<tr>
	<td width="77%">&#160;</td>
	<td align="right"><a href="cssearch"><img border="0" src="images/but_advancedsearch.jpg" alt=""/></a><br/><br/></td>
</tr>
<xsl:if test="contains($permissions, ',/Admin/Harvest:u,')='true'">
	<tr>
	<td width="77%">&#160;</td>
		<td align="center">
			<xsl:if test="$admin='true'">
				<xsl:attribute name="bgcolor">#A0A0A0</xsl:attribute>
				<xsl:attribute name="style">BORDER: #000000 1px solid;</xsl:attribute>
				<b><font color="#FFFFFF">Actions</font></b><br/><br/>
			</xsl:if>
			<img alt="Harvest Reportnet meta-information" border="0" src="images/bb_harvest.png" onclick="javascript:openHarvester()"></img>
			<br/><br/>
		</td>
	</tr>
</xsl:if>
</xsl:if>
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
<table cellspacing="0">
<tr valign="top">
	<th align="left" colspan="3"><b>EEA member countries </b></th>
</tr>
<tr>

<td bgcolor="#FFFFFF" style="BORDER-LEFT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid" width="200" valign="top">
<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
	<xsl:if test="position() &lt; $noOfCountries div 3">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid" width="200" valign="top">
<xsl:for-each select="RowSet[@Name='Members']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= $noOfCountries div 3 and position() &lt; ($noOfCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #008080 1px solid" width="200" valign="top">
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
<td bgcolor="#FFFFFF" style="BORDER-LEFT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid" width="200" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &lt; $noOfNMCountries div 3">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid" width="200" valign="top">
<xsl:for-each select="RowSet[@Name='NonMembers']/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= $noOfNMCountries div 3 and position() &lt; ($noOfNMCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td bgcolor="#FFFFFF" style="BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid; BORDER-RIGHT: #008080 1px solid" width="200" valign="top">
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
	<img src="images/Folder_icon.gif"/>
	<a><xsl:attribute name="href">csmain?COUNTRY_ID=<xsl:value-of select="PK_SPATIAL_ID"/>&#038;ORD=NEXT_REPORTING, NEXT_DEADLINE</xsl:attribute><xsl:value-of select="SPATIAL_NAME"/></a>
	<br/>
</xsl:template>

</xsl:stylesheet>
