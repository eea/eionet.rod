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

function submitSearchForm() {
	if(document.forms["x1"].ISSUE_ID.selectedIndex == 0 &&
	   document.forms["x1"].COUNTRY_ID.selectedIndex == 0)
		alert("Please select a country or an issue.");
	else
		document.forms["x1"].submit();   
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
				<td valign="bottom"><span class="barfont">Advanced search</span></td>
				<td width="28" valign="bottom"><img src="images/bar_dot.jpg" width="28" height="25"/></td>
			 </tr>
			</table>
		 </td></tr><tr><td></td></tr>
	</table>
	 
	 <div style="margin-left:13">
		<form name="x1" method="get" action="csmain">
		<table  width="600" cellspacing="0" cellpadding="2"  style="border: 1 solid #008080">
				 <tr>
						<td width="120" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">Show reporting:</span>
						</td>
						<td width="223" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For an issue</span>
						</td>
						<td width="215" bgcolor="#FFFFFF" style="border-left: 1 solid #C0C0C0"><span class="smallfont">For a country</span>
						</td>
						<td width="42" bgcolor="#FFFFFF" align="right"><!--img onclick="javascript:openViewHelp('HELP_SEARCH')" border="0" src="images/questionmark.jpg" width="13" height="13" alt="[ HELP ]"/-->
						<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSINDEX_SEARCH</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
						</td>
				</tr>
				<tr>
           <td>
						<span class="barfont">
							Deadlines
						</span>
           </td>
            <td style="border-left: 1 solid #C0C0C0">
									<select name="ISSUE_ID" style="font-size: 8pt; color: #000000; width:223" height="20">
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
						<a>
							<xsl:attribute name="href">javascript:submitSearchForm()</xsl:attribute>
							<img src="images/go.png" alt="" border="0"/>
						</a>
					</td>
					</tr>
					<tr height="50">
					<td style="border-left: 1 solid #C0C0C0">		
					</td>
					<td align="right" style="border-left: 1 solid #C0C0C0" >
							<span class="smallfont">Next deadline: </span></td>
									<td>
										<span class="Mainfont"><input type="text" style="color: #000000; font-size: 10pt;" name="DATE_1" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/><b> - <br/></b></span>
										<span class="Mainfont"><font size="1"><input type="text" style="color: #000000; font-size: 10pt;" name="DATE_2" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/></font></span>
									</td>
									<td></td>
					</tr>
		</table>
		</form>


		<br/><div style="margin-left:20">

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