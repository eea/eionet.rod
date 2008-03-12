<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="pagetitle">
		Advanced search
	</xsl:variable>
	
	<xsl:variable name="col_class">
		twocolumns
	</xsl:variable>
	
<xsl:include href="ncommon.xsl"/>

<xsl:variable name="admin">
	<xsl:value-of select="/XmlData/RowSet/@auth"/>
</xsl:variable>

<xsl:variable name="permissions">
	<xsl:value-of select="/XmlData/RowSet/@permissions"/>
</xsl:variable>

<xsl:template name="breadcrumbs">
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>
 <div class="breadcrumbitem"><a href="index.html">ROD</a></div>
 <div class="breadcrumbitem"><a href="deliveries.jsv">Deadlines</a></div>
 <div class="breadcrumbitemlast">Advanced search</div>
 <div class="breadcrumbtail"></div>
</div>
</xsl:template>

<xsl:template match="XmlData">

	<script type="text/javascript">

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

	var c = document.forms["f"].COUNTRY_ID;

	if (c.options[c.selectedIndex].value == -1 )
		alert("Please select a country");
	else {
	
		if( document.forms["f"].DATE_1.value=='' )
			document.forms["f"].DATE_1.value = "dd/mm/yyyy";

		if( document.forms["f"].DATE_2.value=='' )
			document.forms["f"].DATE_2.value = "dd/mm/yyyy";
		
	
		document.forms["f"].submit();
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


				
		</script>


	 <div id="workarea">
		<h1>Advanced search</h1>
		<form id="x1" method="get" action="csmain">
		<table cellspacing="0" cellpadding="2" class="main">
				 <tr>
						<td colspan="2" width="94%" bgcolor="#FFFFFF" class="head">
							<span style="float:right">
							<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSINDEX_SEARCH</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
							</span>
							<b>Show reporting deadlines</b>
						</td>
				</tr>
				<tr>
					<td width="30%" bgcolor="#FFFFFF" class="lab">
						<label for="countryid">For a country</label>
					</td>
					<td class="dat">
						<select id="countryid" name="COUNTRY_ID" class="dat" style="width:223" size="1">
							<option value="">Any country</option>
							<xsl:call-template name="SpatialTemplate2">
								<xsl:with-param name ="type">C</xsl:with-param>
								<xsl:with-param name ="type2"></xsl:with-param>
							</xsl:call-template>
						</select>
					</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" class="lab"><label for="issueid">For an issue</label>
					</td>
					<td class="dat">
						<select id="issueid" name="ISSUE_ID" style="width:223" class="dat">
								<option value="">All issues</option>
								<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
						</select>
					</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" class="lab"><label for="clientid">For an organisation</label>	</td>
					<td class="dat">
						<select id="clientid" name="CLIENT_ID" style="width:350" class="dat">
								<option value="">Any organisation</option>
								<xsl:apply-templates select="RowSet[@Name='Client']"/>
						</select>
					</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" class="lab"><label for="date1">Next deadline</label></td>
					<td align="left" class="dat">
						<input type="text" class="dat" name="DATE_1" id="date1" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/><label for="date2" title="to" style="font-weight:bold"> -</label>
						<input type="text" class="dat" name="DATE_2" id="date2" size="10" onchange="checkDate(this)" value="dd/mm/yyyy"/>
						</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<xsl:call-template name="go"/>
					</td>
				</tr>
		</table>
		</form>
  </div>
	<xsl:call-template name="CommonFooter"/>		
	</xsl:template>

	<xsl:template name="SpatialTemplate2">
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

	<xsl:template match="RowSet[@Name='Client']">
		<xsl:for-each select="Row/T_CLIENT">
			<option><xsl:attribute name="value"><xsl:value-of select="PK_CLIENT_ID"/></xsl:attribute>
			<xsl:value-of select="CLIENT_NAME"/></option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="createURL"/>
	<xsl:template name="PageHelp"/>
</xsl:stylesheet>
