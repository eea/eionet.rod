<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>

	<xsl:variable name="admin">
		<xsl:value-of select="/XmlData/RowSet/@auth"/>
	</xsl:variable>


<xsl:template match="/">

<html lang="en"><head><title>Country Services</title>
	
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/>
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript">
					
Net=1;
if ((navigator.appName.substring(0,5) == "Netsc" 
  &amp;&amp; navigator.appVersion.charAt(0) > 2)
  || (navigator.appName.substring(0,5) == "Micro"
  &amp;&amp; navigator.appVersion.charAt(0) > 3)) {
 Net=0;

 over = new Image;
 out = new Image;
 gammel = new Image;

 over.src = "images/on.gif";
 out.src = "images/off.gif";
 
 gTarget = 'img1';
}


function openHarvester(){
	var name = "CSHarvester";
	var features = "location=no, menubar=no, width=280, height=200, top=40, left=60, resizable=no, SCROLLABLE=no";
	var w = window.open( "harvester.jsv", name, features);
	w.focus();

}

function showhelp(text) {
	if (text != '')
		alert(text);
	else
		alert('No examples for this unit type!');
}

function Click(Target) {
 if (Net != 1){
  if (Target != gTarget) {
   document[Target].src = over.src;
   document[gTarget].src = out.src;
   gTarget = Target;
   gammel.src = document[Target].src;
  }
 }
}

function Over(Target) {
 if (Net != 1){
  gammel.src = document[Target].src;
  document[Target].src = over.src;
 }
}

function Out(Target) {
 if (Net != 1){
  document[Target].src = gammel.src;
 }
}

var browser = document.all ? 'E' : 'N';

var picklist = new Array();

function openFeedback(){
	fwdUrl="http://213.168.23.13:81/countrysrv/public/fb_response.html";
	var name = "CSFeedback";
	var features = "location=no, width=700, height=500, menubar=yes,  top=100, left=200, scrollbars=yes, resizable=yes";
	var url = "http://213.168.23.13:81/wftool/feedback.jsp?fwd=" + fwdUrl + "&#038;pn=Country Services&#038;pv=1.0&#038;fn=Index" ;
	var w = window.open( url, name, features);
	w.focus();
}



				
</script></head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">


<table cellspacing="0" cellpadding="0" border="0"><tr>
  <td valign="top" bgcolor="#747400" width="130">
  <img alt="" width="130" height="113" src="images/top1.jpg"/></td><td valign="top" width="20">
  <img alt="" src="images/top2.jpg" width="20" height="113"/></td><td valign="top" width="621"><table cellspacing="0" cellpadding="0" border="0"><tr><td>
    <img alt="" height="35" width="92" src="images/top3.jpg"/></td></tr><tr><td><table width="621" border="0"><tr><td width="618">
  <span class="head2"><strong><font color="#006666">Reporting Obligations Database, ROD</font></strong></span><br/><span class="head0"><strong>
  <font color="#006666">Ver 1.0 beta. The database contents is under establishment</font></strong></span>
  </td><td width="50"> </td><td><img border="0" width="66" height="62" alt="" src="images/logo.jpg"/></td></tr></table></td></tr></table></td></tr></table><table  cellspacing="0" cellpadding="0" border="0"><tr valign="top">
  <td nowrap="" width="130" bgcolor="#747400"><p><center><table cellspacing="0" cellpadding="0" border="0"><tr><td align="center"><span class="head0">Contents</span></td></tr>
  <!--tr>
  <td align="right">
  <a onMouseOver="Over('img0')" onMouseOut="Out('img0')" href="show.jsv?id=1&#038;mode=C">
  <img alt="" border="0" src="images/off.gif" name="img0" width="16" height="13"/><img alt="Legislation" height="13" width="84" border="0" src="images/button_legislation.gif"/></a></td>
  </tr-->
  <tr>
  <td align="right">
  <a onClick="Click('img1')" onMouseOut="Out('img1')" onMouseOver="Over('img1')" href="rorabrowse.jsv?mode=R">
    <img alt="" border="0" src="images/off.gif" name="img1" width="16" height="13"/><img alt="Reporting Obligations" height="13" width="84" border="0" src="images/button_obligations.gif"/></a></td>
  </tr>
  <tr>

						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=14,25,2,3,4,5,6,7,8,9,10,11,12,13:EU%20legislation%20obligations">
								<img src="images/button_eulegislation_sub.gif" border="0" width="100" height="13" alt="EU legislation obligations"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=15:Conventions'%20obligations">
								<img src="images/button_conventions_sub.gif" border="0" width="100" height="13" alt="Conventions obligations"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=21:EEA%20requests">
								<img src="images/button_eearequests_sub.gif" border="0" width="100" height="13" alt="EEA requests"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=22:Eurostat%20requests">
								<img src="images/button_eurostatrequests_sub.gif" border="0" width="100" height="13" alt="Eurostat requests"/>
							</a>
						</td></tr>
						<tr><td align="right">
							<a href="rorabrowse.jsv?mode=R&amp;type=23:Other%20requests">
								<img src="images/button_otherrequests_sub.gif" border="0" width="100" height="13" alt="Other requests"/>
							</a>
						</td></tr>
  <tr>
  <td align="right">
  <a onClick="Click('img2')" onMouseOut="Out('img2')" onMouseOver="Over('img2')" href="rorabrowse.jsv?mode=A">
    <img alt="" border="0" src="images/off.gif" name="img2" width="16" height="13"/><img alt="Reporting Activities" height="13" width="84" border="0" src="images/button_activities.gif"/></a></td>
  </tr>


  <td align="right">
  <a onClick="Click('img3')" onMouseOut="Out('img3')" onMouseOver="Over('img3')" href="deliveries.jsv">
    <img alt="" border="0" src="images/off.gif" name="img3" width="16" height="13"/><img alt="Reported Data Sets" height="13" width="84" border="0" src="images/button_cs.gif"/></a></td>
  </tr>
  <!--tr>
  <td align="right">
  <a onClick="Click('img4')" onMouseOut="Out('img4')" onMouseOver="Over('img4')" href="javascript:openFeedback()">
    <img alt="" border="0" src="images/off.gif" name="img4" width="16" height="13"/><img alt="Send feedback about the application" height="13" width="84" border="0" src="images/button_feedback.gif"/></a></td>
  </tr-->
  </table>
  <p> </p>
  </center></p></td><td><table border="0" width="621" cellpadding="0" cellspacing="0"><tr><td height="25" background="images/bar_filled.jpg" width="20" align="bottom"> </td><td height="25" background="images/bar_filled.jpg" width="600"><table border="0" background="" cellPadding="0" cellSpacing="0" height="8"><tr><td width="92" align="middle" valign="bottom"><a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a></td><td width="28" valign="bottom">
    <img src="images/bar_hole.jpg" width="28" height="24"/></td><td width="92" align="middle" valign="bottom"><a href="index.html"><span class="barfont">WebROD</span></a></td><td width="28" valign="bottom">
    <img src="images/bar_hole.jpg" width="28" height="24"/></td><td width="122" align="middle" valign="bottom">
    <span class="barfont"><!--a href="csbrowse.html"-->Deliveries<!--/a--></span></td><td width="28" valign="bottom">
    <img src="images/bar_dot.jpg" width="28" height="25"/></td><td width="2 10" align="right" valign="bottom"></td></tr></table></td></tr><tr><td> </td></tr></table><div style="margin-left:13"><form action="rorabrowse.jsv" method="get" name="f"><input value="A" name="mode" type="hidden"/></form><div style="margin-left:20"><table cellspacing="7pts"></table></div>
  <table width="600"><tr><td>

<!-- p class="MsoNormal">
<span lang="EN-GB">
This tool aims at facilitating EEA member countries to co-ordinate and to supervise their international reporting. It tells which country has to report what, when is the deadline, is the reported data set in ReportNet, who was responsible for delivering, and to whom was the data set delivered.<br/><br/>

<xsl:if test="$admin = 'true'">
				<map name="openHarvesterMap">
					<area alt="Harvest data from other services" shape="rect" coords="0,0,20,20" href="javascript:openHarvester()"></area>
				</map>
				<img border="0" height="25" width="25" src="images/doc.gif" usemap="#openHarvesterMap"></img>
		&#160;<br/>
</xsl:if>

</span>You can browse your national deliveries by choosing a country below or query the contents by using the advanced search.<br/><br/>
<b><a href="cssearch">Advanced search</a></b></p-->

<table>
<tr><td>
This tool aims at facilitating EEA member countries to co-ordinate and to
supervise their international reporting. It tells which country has to
report what, when is the deadline, is the reported data set in ReportNet,
who was responsible for delivering, and to whom was the data set delivered.
  
You can browse your national deliveries by choosing a country below or query
the contents by using the advanced search.
</td><td valign="top" align="right">
<a href="cssearch"><img border="0" src="images/bb_advsearch.png" alt="Advanced search"/></a><br/>

<!--a href="javascript:openHarvester()"><img border="0" src="images/bb_harvest.png"/></a-->

<xsl:if test="$admin = 'true'">
				<map name="openHarvesterMap">
					<area alt="Harvest data from other services" shape="rect" coords="0,0,120,17" href="javascript:openHarvester()"></area>
				</map>
				<img border="0" heigth="17" witdh="120" src="images/bb_harvest.png" usemap="#openHarvesterMap"></img>
		&#160;<br/>
</xsl:if>

</td></tr></table>




<table width="100%">

<xsl:variable name="noOfCountries"><xsl:value-of select="count(child::XmlData/RowSet/Row/T_SPATIAL)"/></xsl:variable>
<table><tr>
<td width="200" valign="top">
<xsl:for-each select="XmlData/RowSet/Row/T_SPATIAL">
	<xsl:if test="position() &lt; $noOfCountries div 3">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td width="200" valign="top">
<xsl:for-each select="XmlData/RowSet/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= $noOfCountries div 3 and position() &lt; ($noOfCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>
</td>
<td width="200" valign="top">
<xsl:for-each select="XmlData/RowSet/Row/T_SPATIAL">
	<xsl:if test="position() &gt;= ($noOfCountries div 3) * 2">
		<xsl:call-template name="COUNTRYNAME"/>
	</xsl:if>
</xsl:for-each>

</td>
</tr>
<tr><td  colspan="3"><hr/><br/></td></tr>
<tr><td  colspan="3">	<b>
	Disclaimer: EEA does not guarantee that all possible deliveries are shown, nor that the recorded deliveries 
	totally cover all aspects a country has to comply with regarding a specific reporting activity. 
	If you find any inconsistencies in the database, please <a href="mailto:eea@eea.eu.int">report back</a> to EEA.  
	</b>
</td></tr>

<!--tr><td colspan="3"><hr/></td></tr>
<tr><td colspan="3"><b><a target="CSFeedback" href="/wftool/feedback.jsp?fwd=/countrysrv/public/csindex&#038;pn=Country Services&#038;pv=1.0&#038;fn=Index">Feedback</a> about this page.</b></td></tr-->
</table>
</table>

	<br/>

	</td></tr>
	</table>
  </div>
	</td></tr>
	</table>

	</body>
	</html>

</xsl:template>

<xsl:template name="COUNTRYNAME">
		<img src="images/Folder_icon.gif" width="16" height="16"/>
		<a><xsl:attribute name="href">csmain?COUNTRY_ID=<xsl:value-of select="PK_SPATIAL_ID"/>&#038;ORD=TITLE</xsl:attribute><xsl:value-of select="SPATIAL_NAME"/></a>
		<br/>
</xsl:template>

</xsl:stylesheet>
