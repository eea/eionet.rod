<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:include href="util.xsl"/>

<xsl:variable name="admin">
	<xsl:value-of select="/XmlData/RowSet/@auth"/>
</xsl:variable>

<xsl:variable name="permissions">
	<xsl:value-of select="/XmlData/RowSet/@permissions"/>
</xsl:variable>

<xsl:template match="XmlData">

<html lang="en"><head><title>Country Services</title>
<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/>
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript" src="script/util.js"></script>
	<script language="JavaScript">

<![CDATA[
/*
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
} */

/*function showhelp(text) {
	if (text != '')
		alert(text);
	else
		alert('No examples for this unit type!');
} */

/*
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
*/
/*

var browser = document.all ? 'E' : 'N';

var picklist = new Array();
*/
]]>
<![CDATA[
/*
function Out(Target) {
 if (Net != 1){
  document[Target].src = gammel.src;
 }
}
*/
/*
function Over(Target) {
 if (Net != 1){
  gammel.src = document[Target].src;
  document[Target].src = over.src;
 }
}
*/
/*
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
*/

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
				<td valign="bottom"><a href="deliveries.jsv"><span class="barfont">Deadlines</span></a></td>

				<td width="28" valign="top"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td valign="bottom"><span class="barfont">Advanced Search</span></td>
				<td width="28" valign="bottom"><img src="images/bar_dot.jpg" width="28" height="25"/></td>
			 </tr>
			</table>
		 </td></tr><tr><td></td></tr>
	</table>
	 
	 <div style="margin-left:13">
		<form name="x1" method="get" action="csmain">
		<table  width="600" cellspacing="0" cellpadding="2"  style="border: 1 solid #008080">
				 <tr>
						<td width="110" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">Show reporting:</span>
						</td>
						<td width="260" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For an issue</span>
						</td>
						<td width="200" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For a country</span>
						</td>
						<td width="30" bgcolor="#FFFFFF"><!--img onclick="javascript:openViewHelp('HELP_SEARCH')" border="0" src="images/questionmark.jpg" width="13" height="13" alt="[ HELP ]"/-->
						<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSINDEX_SEARCH</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
						</td>
				</tr>
				<tr>
           <td>
						<span class="barfont">
							Deadlines
						</span>
           </td>
            <td style="border-left: 1 solid #C0C0C0">
									<select name="ISSUE_ID" style="font-size: 8pt; color: #000000; width:240" height="20">
											<option value="0">All issues</option>
											<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
                  </select>
						</td>
            <td style="border-left: 1 solid #C0C0C0">
										<select name="COUNTRY_ID" style="color: #000000; font-size: 8pt; width:200" size="1">
											<option value="0">Any country</option>
											<xsl:call-template name="SpatialTemplate">
												<xsl:with-param name ="type">C</xsl:with-param>
												<xsl:with-param name ="type2"></xsl:with-param>
											</xsl:call-template>
                    </select>
								</td>
                <td>
                   <input type="submit" value="GO" name="GO" style="font-family: Verdana; font-size: 8pt; color: #000000; text-align: Center"/>
								</td>
					</tr>
					<tr height="50">
					<td style="border-left: 1 solid #C0C0C0">		
					</td>
					<td align="right" style="border-left: 1 solid #C0C0C0" >
							<span class="smallfont">Next deadline: </span></td>
									<td align="left"><span class="Mainfont"><input type="text" style="color: #000000; font-size: 10pt;" name="DATE_1" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/><b> - </b></span>
										<span class="smallfont"><font size="1"><input type="text" style="color: #000000; font-size: 10pt;" name="DATE_2" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/></font></span>
									</td>
									<td></td>
					</tr>
		</table>
		</form>




<!-- KL -->
		<!--form action="csmain" method="get" name="f">
		<input value="TITLE" name="ORD" type="hidden"/>
		<table width="600" border="0" cellspacing="10">
		<tr><td colspan="3">
			<span class="head0"><span lang="en-us">Reporting</span> deliveries selected by different   filters: 
		<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_CSSEARCH</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
	  </span></td></tr>
		<tr>
		<td width="10">
		  <img src="images/diamlil.gif" width="8" height="9"/>
		</td>
		<td width="250">Countries
				<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSSEARCH_COUNTRIES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td>
		<td width="340"><select name="COUNTRY_ID" style="width:200">
				<option value="0">All countries </option>
					<xsl:for-each select="RowSet[@Name='Countries']/Row/T_SPATIAL">
				<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_SPATIAL_ID"/>
				</xsl:attribute>
				<xsl:value-of select="SPATIAL_NAME"/>
				</option>
				</xsl:for-each>
		</select>		
		</td>
		</tr>
		<tr valign="center"><td>
		<img src="images/diamlil.gif" width="8" height="9"/></td><td>Environmental issues
			<xsl:call-template name="Help"><xsl:with-param name="id">HELP_MAIN_ENVIRONMENTALISSUES</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td>
		<td>
		<select name="ISSUE_ID" style="width:300">
			<option value="0">All issues</option>
			<xsl:for-each select="RowSet[@Name='Issues']/Row/T_ISSUE">
			<option>
			<xsl:attribute name="value">
				<xsl:value-of select="PK_ISSUE_ID"/>
			</xsl:attribute>
			<xsl:value-of select="ISSUE_NAME"/>
			</option>
			</xsl:for-each>
		</select>
		</td></tr>

  <tr>
    <td width="10">
  <img src="images/diamlil.gif" width="8" height="9"/></td>
	<td width="200">Next deadline
		<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSSEARCH_NEXTDEADLINE</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
	</td><td>
		<input type="text" name="DATE_1" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/><b> - </b>	
		<input type="text" name="DATE_2" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/>
		</td>
  </tr>
  <tr><td colspan="2"></td><td>
    <input value="Show selected reporting deliveries" onclick="doSearch()" style="width:300" type="button"/></td></tr></table>
		</form--><br/><div style="margin-left:20">

		<table cellspacing="7pts"></table>
		<xsl:call-template name="CommonFooter"/>		
		</div>
  </div></body></html>
	</xsl:template>

		<xsl:template name="SpatialTemplate">
		<xsl:param name="type" select="'Not selected'"/>
		<xsl:param name="type2" select="'Not selected'"/>
		<xsl:for-each select="RowSet[@Name='Spatial']/Row/T_SPATIAL[SPATIAL_TYPE=$type or SPATIAL_TYPE=$type2]">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="PK_SPATIAL_ID"/>
				</xsl:attribute>
			<xsl:value-of select="SPATIAL_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="RowSet[@Name='EnvIssue']">
		<xsl:for-each select="Row/T_ISSUE">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/></xsl:attribute>
			<xsl:value-of select="ISSUE_NAME"/></option>
		</xsl:for-each>
	</xsl:template>


</xsl:stylesheet>
