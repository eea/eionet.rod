<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:template match="/">

<html lang="en"><head><title>
		Country Services
	</title><META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/>
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript">
	<![CDATA[
	Net=1;
	]]>

<![CDATA[

if ((navigator.appName.substring(0,5) == "Netsc"
  && navigator.appVersion.charAt(0) > 2)
  || (navigator.appName.substring(0,5) == "Micro"
  && navigator.appVersion.charAt(0) > 3)) {
 Net=0;

 over = new Image;
 out = new Image;
 gammel = new Image;

 over.src = "images/on.gif";
 out.src = "images/off.gif";
 
 gTarget = 'img1';
}

function showhelp(text) {
	if (text != '')
		alert(text);
	else
		alert('No examples for this unit type!');
}

function openFeedback(){
	fwdUrl="http://213.168.23.13:81/countrysrv/public/fb_response.html";
	var name = "CSFeedback";
	var features = "location=no, menubar=yes, width=700, height=500, top=100, left=200, scrollbars=yes, resizable=yes";
	var url = "http://213.168.23.13:81/wftool/feedback.jsp?fwd=" + fwdUrl + "&#038;pn=Country Services&#038;pv=1.0&#038;fn=Search" ;
	var w = window.open( url, name, features);
	w.focus();
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



var browser = document.all ? 'E' : 'N';

var picklist = new Array();

]]>
<![CDATA[

function Out(Target) {
 if (Net != 1){
  document[Target].src = gammel.src;
 }
}

function Over(Target) {
 if (Net != 1){
  gammel.src = document[Target].src;
  document[Target].src = over.src;
 }
}

function fillPicklist(type,list,text) {
      var i,js;
	for (i = list.length; i > 0; --i)
		list.options[i] = null;
	list.options[0] = new Option("Choose a group","-1");
	j = 1;
	for (i = 0; i < picklist.length; i++) {
	  s = new String(picklist[i]);
	  pvalue = s.substring(0,s.indexOf(":"));
	  ptext = s.substring(s.indexOf(":")+1,s.lastIndexOf(":"));
	  ptype = s.substring(s.lastIndexOf(":")+1,s.lastIndexOf(":")+2);
	  if (ptype.valueOf() == type) {
	  	list.options[j] = new Option(ptext.valueOf(), pvalue.valueOf()+":"+ptext.valueOf());
	  	j++;
	  }
	} 
	list.options[0].selected = true;
}


function checkDate(oControl){
	if ( checkDateFormat(oControl.value) == 1  )
		oControl.value = 'dd/mm/yyyy' ;

	}



//
function checkDateFormat(dateString){ 

		
   day = dateString.substring(0, dateString.indexOf("/")); 

   if(dateString == '' || dateString == 'dd/mm/yyyy' )
      return 0;

   if((isNaN(day)) || (day.length != "2")){ 
      alert("Day " + day + " is not in a correct format."); 
      return 1; 
   } 
   else if(day > 31 || day < 1){
      alert("Day " + day + "is not correct "); 
      return 1;}



   month = dateString.substring(dateString.indexOf("/") + 1, dateString.lastIndexOf("/")); 
   if((isNaN(month)) || (month.length != "2")){ 
      alert("Month " + month + " is not in a correct format."); 
      return 1; 
   }                
   else if(month > 12 || month < 1 ){ 
      alert("Month " + month + " is not correct."); 
      return 1; 
   }               
   
   year = dateString.substring(dateString.lastIndexOf("/") + 1); 
   if((isNaN(year)) || (year.length != "4")){ 
      alert("Year " + year + " is not in correct format."); 
      return 1; 
   } 

  if (day > 30 && ( month == 4 || month == 6 || month == 9 || month == 11   )  ){ 
      alert("There are 30 days in this month."); 
      return 1; 
   } 

  if (day > 29 &&  month == 2){ 
      alert("There are max 29 days in February"); 
      return 1; 
  }

  var bY;
  bY = false;

  if ( year/4 == Math.floor(year/4 )   )
    bY = true;
  
  if (day == 29 &&  month == 2 &&  bY == false){ 
      alert("In this year there are 28 days in February."); 
      return 1;
	}

   if (day == '' || month == '' || year == '')
     {
		alert('Error in date format');
		return 1;
     }

   return 0; 
}//end-checkDateFormath 

//

function doSearch(){

	var c = document.f.COUNTRY_ID;

	if (c.options[c.selectedIndex].value == -1 )
		alert("Please select a country");
	else {
	
		if( document.f.DATE_1.value=='' )
			document.f.DATE_1.value = "dd/mm/yyyy";

		if( document.f.DATE_2.value=='' )
			document.f.DATE_2.value = "dd/mm/yyyy";
		
	
		document.f.submit();
	}
}
]]>


				
		</script></head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">
<!-- main -->
<table cellspacing="0" cellpadding="0" border="0"><tr>
  <td valign="top" bgcolor="#747400" width="130">
  <img alt="" width="130" height="113" src="images/top1.jpg"/></td><td valign="top" width="20">
  <img alt="" src="images/top2.jpg" width="20" height="113"/></td><td valign="top" width="621">
	<!-- 2 -->
	<table cellspacing="0" cellpadding="0" border="0"><tr><td>
    <img alt="" height="35" width="92" src="images/top3.jpg"/></td></tr><tr><td>
		<!-- 3 -->
		<table width="621" border="0"><tr><td width="618">
		  <span class="head2"><strong><font color="#006666">Reporting Obligations Database, ROD</font></strong></span><font face="Arial" size="5" color="#006666"><strong><span class="head2">
			</span></strong></font><br/><span class="head0"><strong>
	  <font color="#006666">Ver 1.0 beta. The database contents is under establishment</font></strong></span>
  </td><td width="50"></td><td><img border="0" width="66" height="62" alt="" src="images/logo.jpg"/></td></tr>
	</table>
	<!-- 3 -->
	</td></tr>
	</table>
	<!-- 2 -->
	</td></tr>
	</table>
	<!-- main -->

	<table  cellspacing="0" cellpadding="0" border="0">
	<tr valign="top" height="500"> <td nowrap="" align="center" width="130" bgcolor="#747400">

<table cellspacing="0" cellpadding="0" border="0"><tr><td align="center"><span class="head0">Contents</span></td></tr>
  <!--tr>
  <td align="right">
  <a onMouseOver="
									Over('img0')
								" onMouseOut="
									Out('img0')
								" href="show.jsv?id=1&#038;mode=C">
  <img alt="" border="0" src="images/off.gif" name="img0" width="16" height="13"/><img alt="Legislation" height="13" width="84" border="0" src="images/button_legislation.gif"/></a></td>
  </tr-->
  <tr>
  <td align="right">
  <a onClick="Click('img1')" onMouseOut="Out('img1')" onMouseOver="Over('img1')" href="rorabrowse.jsv?mode=R">
    <img alt="" border="0" src="images/off.gif" name="img1" width="16" height="13"/>
		<img alt="Reporting Obligations" height="13" width="84" border="0" src="images/button_obligations.gif"/></a></td>
  </tr>

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
    <img alt="" border="0" src="images/off.gif" name="img2" width="16" height="13"/>
		<img alt="Reporting Activities" height="13" width="84" border="0" src="images/button_activities.gif"/></a></td>
  </tr>
  <tr>
  <td align="right">
  <a onClick="Click('img3')" onMouseOut="Out('img3')" onMouseOver="Over('img3')" href="deliveries.jsv">
    <img alt="" border="0" src="images/off.gif" name="img3" width="16" height="13"/>
		<img alt="Reported Data Sets" height="13" width="84" border="0" src="images/button_cs.gif"/></a></td>
  </tr>
  <!--tr>
  <td align="right">
  <a onClick="Click('img4')" onMouseOut="Out('img4')" onMouseOver="Over('img4')" href="javascript:openFeedback()">
    <img alt="" border="0" src="images/off.gif" name="img4" width="16" height="13"/><img alt="Send feedback about the application" height="13" width="84" border="0" src="images/button_feedback.gif"/></a></td>
  </tr-->
  </table>  


  </td><td><table border="0" width="621" cellpadding="0" cellspacing="0"><tr><td height="25" background="images/bar_filled.jpg" width="20" align="bottom"></td><td height="25" background="images/bar_filled.jpg" width="600"><table border="0" background="" cellPadding="0" cellSpacing="0" height="8"><tr><td width="92" align="middle" valign="bottom"><a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a></td><td width="28" valign="bottom">
    <img src="images/bar_hole.jpg" width="28" height="24"/></td><td width="92" align="middle" valign="bottom"><a href="index.html"><span class="barfont">WebROD</span></a></td>

				<td width="28" valign="bottom"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td width="122"	align="middle" valign="bottom"><a href="deliveries.jsv"><span class="barfont">Deliveries</span></a></td>

		<td width="28" valign="bottom"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
		<td width="122" align="middle" valign="bottom"><span class="barfont"><!--a href="cssearch.html"-->Advanced Search<!--/a--></span></td>
		<td width="28" valign="bottom"><img src="images/bar_dot.jpg" width="28" height="25"/></td><td width="2 10" align="right" valign="bottom"></td>
		</tr></table></td></tr><tr><td></td></tr></table><div style="margin-left:13">
		<form action="csmain" method="get" name="f">
		<input value="TITLE" name="ORD" type="hidden"/>
		<table width="600" border="0" cellspacing="10"><tr><td colspan="3">
  <span class="head0"><span lang="en-us">Reporting</span> deliveries selected by different 
  filters: </span></td></tr>
			<tr><td width="10">
    <img src="images/diamlil.gif" width="8" height="9"/></td>
		<td width="100">Countries</td>
		<td>	<select name="COUNTRY_ID">
			<option value="0">All countries </option>
			<xsl:for-each select="XmlData/RowSet[@Name='Countries']/Row/T_SPATIAL">
			<option>
			<xsl:attribute name="value">
				<xsl:value-of select="PK_SPATIAL_ID"/>
			</xsl:attribute>
			<xsl:value-of select="SPATIAL_NAME"/>
			</option>
			</xsl:for-each>
		</select>		</td></tr>
		<tr valign="center"><td width="10">

		<img src="images/diamlil.gif" width="8" height="9"/></td><td width="200">Environmental issues</td><td>
		<select name="ISSUE_ID">
			<option value="0">All issues</option>
			<xsl:for-each select="XmlData/RowSet[@Name='Issues']/Row/T_ISSUE">
			<option>
			<xsl:attribute name="value">
				<xsl:value-of select="PK_ISSUE_ID"/>
			</xsl:attribute>
			<xsl:value-of select="ISSUE_NAME"/>
			</option>
			</xsl:for-each>
		</select>
		</td></tr>
		<!--tr valign="center"><td width="10"></td><td width="200"> River runoff areas</td><td><select name="river">
		<option>Choose a river</option>
		</select></td></tr-->
  <tr>
    <td width="10"><!--img src="images/diamlil.gif" width="8" height="9"/--></td><td width="200">
    <!--Specific parameters--></td><td>
		
</td>
  </tr>
	<!-- temporarily commented off until the deadline generating mechanism is not ready -->


		<!--input type="hidden" name="DATE_1" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/>
		<input type="hidden" name="DATE_2" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/-->

  <tr>
    <td width="10">
  <img src="images/diamlil.gif" width="8" height="9"/></td>
	<td width="200">Next deadline</td><td>
		<input type="text" name="DATE_1" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/><b> - </b>	
		<input type="text" name="DATE_2" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/>
		</td>
  </tr>
  <tr><td colspan="2"></td><td>
    <input value="Show selected reporting deliveries" onclick="doSearch()" style="width:300" type="button"/></td></tr></table>
		</form><br/><div style="margin-left:20"><table cellspacing="7pts"></table></div>
  </div></td></tr></table></body></html>

	</xsl:template>
</xsl:stylesheet>
