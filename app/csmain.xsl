<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:include href="util.xsl"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet[position()=1]/@auth"/>
	</xsl:variable>



<xsl:template match="XmlData">

<html lang="en"><head><title>Country Services</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>

<script language="JavaScript" src="script/util.js"></script>
<script language="JavaScript">
/*
					
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
*/
<![CDATA[

function openClient(ID){

	var url = "client.jsv?id=" + ID;
	var name = "Client";
	var features = "location=no, menubar=no, width=640, height=400, top=100, left=200, scrollbars=yes";
	var w = window.open(url,name,features);
	w.focus();

}

function openCirca(url){
	//var url = "cspersons?ROLE=" + ROLE; //  + "&#038;mi6";
	//alert(url);
	var name = "CSCIRCA";
	var features = "location=yes, menubar=yes, width=750, height=600, top=30, left=50, resizable=yes, SCROLLBARS=YES";
	var w = window.open( url, name, features);
	w.focus();

}


/*function openPrintable(){
	var url = document.URL + "&#038;MODE=PR";
	//alert(url);
	var name = "CSPrintMain";
	var features = "location=no, menubar=yes, width=700, height=500, top=100, left=200, scrollbars=yes";
	var w = window.open( url, name, features);
	w.focus();
} */
/*
function openPersons(ROLE){
	//alert("role = " + ROLE);
	var url = "cspersons?ROLE=" + ROLE; //  + "&#038;mi6";
	var name = "CSPersons";
	var features = "location=no, menubar=no, width=430, height=300, top=100, left=200";
	var w = window.open( url, name, features);
	w.focus();

}*/

/*
function openDeliveries(ACT_ID, COUNTRY_ID){

	var url = "csdeliveries?ACT_DETAILS_ID=" + ACT_ID ; // + "&#038;mi6";
	//alert(escape('%%'));
	url = url + "&COUNTRY_ID=" + escape(COUNTRY_ID);
	var name = "CSDeliveries";
	var features = "location=no, menubar=no, width=640, height=400, top=100, left=200, scrollbars=yes";
	var w = window.open( url, name, features);
	w.focus();

}
*/



	function changeParamInString(sUrl, sName, sValue){
		var  i, j,  sBeg, sEnd, sStr;
		
		//KL 021009 -> in some reason does not work anymore :(
		//sValue=escape(sValue);

		i = sUrl.indexOf(sName + '=');
		if (i > 0) {
			sBeg=sUrl.substr(0, i); 
			sStr=sUrl.substr(i);
			j = sStr.indexOf('&');
			if (j > 0)
			   sEnd = sStr.substr(j);
			else
			   sEnd= '';

			sUrl=sBeg + sName + '=' + sValue + sEnd ;

			}
		else
			{

			j = sUrl.indexOf('?');
			if (j>0)
				sUrl = sUrl + '&' + sName + '=' + sValue;
			else
				sUrl = sUrl + '?' + sName + '=' + sValue;
			}
		//return sUrl ;
		redirect(sUrl);
		}

	function redirect(url){
		//document.URL=url;
		document.location=url;

	}
]]>
var browser = document.all ? 'E' : 'N';

var picklist = new Array();

				
			</script>
			</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">
<table border="0" width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td height="25" background="images/bar_filled.jpg" width="20" align="bottom"></td>
	<td height="25" background="images/bar_filled.jpg" width="600">
 		 <!--table border="1" background="" cellPadding="0" cellSpacing="0" height="8"-->
     <table height="8" cellSpacing="0" cellPadding="0" background="" border="0">
		 <tr valign="top">
		  <td valign="bottom"><a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a></td>
		  <td width="28" valign="top">
				<img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td valign="bottom"><a href="index.html"><span class="barfont">ROD</span></a></td>
				<td width="28" ><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td valign="bottom"><a href="deliveries.jsv"><span class="barfont">Deadlines</span></a></td>

				<td width="28" valign="top"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td valign="bottom"><span class="barfont">Country</span></td>
				<td width="28" valign="bottom"><img src="images/bar_dot.jpg" width="28" height="25"/></td>
			 </tr>
			</table>
		 </td></tr><tr><td></td></tr>
	</table>


<map name="sortClient">
<area shape="rect" coords="0,0, 12,7" href="javascript:changeParamInString(document.URL,'ORD','CLIENT_NAME')" alt="Click to sort in ascending order" />
<area shape="rect" coords="12,0, 24,7" href="javascript:changeParamInString(document.URL,'ORD','CLIENT_NAME DESC')" alt="Click to sort in descending order" />
</map>


<map name="sortTitle">
<area shape="rect" coords="0,0, 12,7" href="javascript:changeParamInString(document.URL,'ORD','TITLE')" alt="Click to sort in ascending order" />
<area shape="rect" coords="12,0, 24,7" href="javascript:changeParamInString(document.URL,'ORD','TITLE DESC')" alt="Click to sort in descending order" />
</map>

<map name="sortDeadline">
<area shape="rect" coords="0,0, 11,5" href="javascript:changeParamInString(document.URL,'ORD','DEADLINE')" alt="Click to sort in ascending order" />
<area shape="rect" coords="12,0, 23,5" href="javascript:changeParamInString(document.URL,'ORD','DEADLINE DESC')" alt="Click to sort in descending order" />
</map>

<map name="sortDeadline2">
<area shape="rect" coords="0,0, 11,5" href="javascript:changeParamInString(document.URL,'ORD','DEADLINE2')" alt="Click to sort in ascending order" />
<area shape="rect" coords="12,0, 23,5" href="javascript:changeParamInString(document.URL,'ORD','DEADLINE2 DESC')" alt="Click to sort in descending order" />
</map>

<map name="sortCountry">
<area shape="rect" coords="0,0, 11,5" href="javascript:changeParamInString(document.URL,'ORD','SPATIAL_NAME')" alt="Click to sort in ascending order" />
<area shape="rect" coords="12,0, 23,5" href="javascript:changeParamInString(document.URL,'ORD','SPATIAL_NAME DESC')" alt="Click to sort in descending order" />
</map>

<map name="sortRole">
<area shape="rect" coords="0,0, 11,5" href="javascript:changeParamInString(document.URL,'ORD','ROLE_NAME')" alt="Click to sort in ascending order" />
<area shape="rect" coords="12,0, 23,5" href="javascript:changeParamInString(document.URL,'ORD','ROLE_NAME DESC')" alt="Click to sort in descending order" />
</map>

	<!-- main -->
	<table  cellspacing="0" cellpadding="0" border="0">
	<tr valign="top" height="500">
	<td>

	<div style="margin-left:13">
	
	<form action="rorabrowse.jsv" method="get" name="f">
	
	<!-- 5 -->
	<table width="600" border="0" cellspacing="10"><tr>
  </tr>
	</table>
	<!-- 5 -->
	</form>
	<!-- 6 -->
	<xsl:variable name="oneCountry"><xsl:value-of select="count(child::RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>
	<table border="0" width="98%">
	<tr><td width="475">
    <span class="head1"><span lang="en-us">Reporting overview: </span>
		<xsl:if test="$oneCountry=0">
			<xsl:value-of select="RowSet/Row/T_SPATIAL/SPATIAL_NAME"/>
		</xsl:if>
    </span>
		</td><td width="*" align="right" valign="top">
		<a href="cssearch"><img border="0" src="images/bb_advsearch.png" alt="Advanced search"/></a><br/>
		<!--span class="head0">&#160;&#160;&#160;
			<a href="cssearch">[Advanced search]</a>
			<a href="cssearch"><img border="0" src="images/bb_advsearch.png" alt="Advanced search"/></a><br/>
		</span-->
		</td></tr>
	</table>
	<!-- 6 -->
	<br/><div style="margin-left:20">
	<!-- 7 -->
	<table cellspacing="7pts">
	</table>
	<!-- 7 -->
	</div>
	<!-- 8 -->


	<table width="777" cellspacing="7pts">

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">
		<tr>
				<td bgcolor="#646666" align="center" width="10"> </td>
				<td bgcolor="#646666" align="center" width="147"><span class="headsmall" title="Local organisation, responsible for reporting"><font color="#FFFFFF">Responsible </font><IMG name="updown" border="0" src="images/updown.gif" usemap="#sortRole"></IMG></span></td>
				<!--td bgcolor="#646666" align="center" width="10"></td-->
				<td bgcolor="#646666" align="center" width="147"><font color="#FFFFFF"><span class="headsmall" title="Organisation to report to">Reporting to <IMG name="updown" border="0" src="images/updown.gif" usemap="#sortClient"></IMG></span></font></td>
				<td bgcolor="#646666" align="center" width="100"><span class="headsmall" title="List of deliveries for the given RA"><font color="#FFFFFF">Deliveries</font></span></td>
				<td bgcolor="#646666" align="center" width="180"><span class="headsmall"><font color="#FFFFFF" title="Title of the reporting activity">Reporting Activity <IMG name="updown" border="0" src="images/updown.gif" usemap="#sortTitle"></IMG></font></span></td>
			  <td bgcolor="#646666" align="center" width="140">
				<p align="center"/><span class="headsmall" title="Deadline for reporting">
			  <font color="#FFFFFF">Deadline
						<IMG name="sort" border="0" src="images/updown.gif" usemap="#sortDeadline"></IMG>
					</font></span>
				</td>
			  <td bgcolor="#646666" align="center" width="140">
				<p align="center"/><span class="headsmall" title="Next deadline for reporting">
			  <font color="#FFFFFF">Next DL
					<!--xsl:if test="XmlData/RowSet/Row/T_DEADLINE/DEADLINE !=''"-->
						<IMG name="sort" border="0" src="images/updown.gif" usemap="#sortDeadline2"></IMG>
					<!--/xsl:if-->
					</font></span>
				</td>
				<xsl:if test="$oneCountry !=0">
				  <td bgcolor="#646666" align="center" width="*"><span class="headsmall"><font color="#FFFFFF">Country <IMG name="updown" border="0" src="images/updown.gif" usemap="#sortCountry"></IMG></font></span></td>
				</xsl:if>
				
  </tr>
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)=0">
		<b>No deliveries match the search criteria</b>
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">
	<!-- table row start -->

<xsl:for-each select="RowSet[@Name='Main']/Row">

  <tr valign="top">
<td width="10">


<!-- image, if deadline OK -->
<img src="images/diamlil.gif" width="8" height="9"/></td>
<td width="163">
<font color="#646666">

	<xsl:if test="T_ACTIVITY/RESPONSIBLE_ROLE != ''">
			<xsl:choose>
			<xsl:when test="RESPONSIBLE/ROLE_NAME=''">
				<xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE"/>-<xsl:value-of select="T_SPATIAL/SPATIAL_TWOLETTER"/>
			</xsl:when>
			<xsl:otherwise>
			<a>
			<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_URL"/>')</xsl:attribute>
				<xsl:value-of select="RESPONSIBLE/ROLE_NAME"/>
			</a>
			</xsl:otherwise>
			</xsl:choose>
	</xsl:if>

	</font>
			
</td>
<!--td width="10">
<img src="images/diamlil.gif" width="8" height="9"/>
</td-->
<td width="147"><font color="#646666">
		<a>
		<xsl:attribute name="href">javascript:openClient('<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
		<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
		</a>
		</font>
</td>
<td width="100">
		<font color="#999999">
			<xsl:choose>
					<!--xsl:when test="count(descendant::SubSet[@Name='Delivery']/Row) = 0 "-->	
					<xsl:when test="contains(T_ACTIVITY/FK_DELIVERY_COUNTRY_IDS, concat(',' , T_SPATIAL/PK_SPATIAL_ID , ',') )='true'">
						<a window="delivery">
						<xsl:attribute name="href">javascript:openDeliveries(<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>,<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>)</xsl:attribute>
							Show list
						</a>
					</xsl:when>
					<xsl:otherwise>
							None
					</xsl:otherwise>
			</xsl:choose>
	</font>
</td>
<td width="250">
	<span class="head0">
		<a>
			<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;mode=A</xsl:attribute>
			<xsl:value-of select="T_ACTIVITY/TITLE"/>
		</a>
		</span>
</td>
  <td width="140" align="center">
			<!-- check, where we are -->
			<!--xsl:value-of select="T_ACTIVITY/DEADLINE"/-->
<!-- remove deadline parsing -->
			<xsl:choose>
				<xsl:when test="T_ACTIVITY/DEADLINE != '' ">	
					<xsl:value-of select="T_ACTIVITY/DEADLINE"/>
				</xsl:when>
				<xsl:otherwise>
					<font color="#006666">
					<xsl:value-of select="T_ACTIVITY/NEXT_REPORTING"/>
					</font>
				</xsl:otherwise>
			</xsl:choose>
			<!--xsl:choose>
			<xsl:when test="T_DEADLINE/DEADLINE != '' ">	
				<xsl:value-of select="T_DEADLINE/DEADLINE"/>
			</xsl:when>
			<xsl:otherwise>
			<xsl:choose>

						<xsl:when test="count(descendant::SubSet[@Name='FutureDeadlines']/Row) = 0 ">	
							<xsl:choose>
									<xsl:when test="SubSet[@Name='PassedDeadlines']/Row/T_DEADLINE/DEADLINE =''">	
											<xsl:value-of select="SubSet[@Name='PassedDeadlines']/Row/T_DEADLINE/DEADLINE_TEXT"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="SubSet[@Name='PassedDeadlines']/Row/T_DEADLINE/DEADLINE"/>
									</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
									<xsl:when test="SubSet[@Name='FutureDeadlines']/Row/T_DEADLINE/DEADLINE =''">	
											<xsl:value-of select="SubSet[@Name='FutureDeadlines']/Row/T_DEADLINE/DEADLINE_TEXT"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="SubSet[@Name='FutureDeadlines']/Row/T_DEADLINE/DEADLINE"/>
									</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose-->
			</td>
		   <td width="140" align="center">
				<xsl:if test="T_ACTIVITY/DEADLINE2 != '' ">	
					<xsl:value-of select="T_ACTIVITY/DEADLINE2"/>
				</xsl:if>
			</td>
	<xsl:if test="$oneCountry !=0">
		<td width="*" align="left"><span lang="en-us"><xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/></span></td>
	</xsl:if>
  </tr>
</xsl:for-each>

</xsl:if>
<!-- end of table row -->
	<tr><td>
		<xsl:attribute name="colspan">
			<xsl:value-of select="6+$oneCountry"/>
		</xsl:attribute>
		<br/>
		
		<xsl:variable name="recCount"><xsl:value-of select="count(child::RowSet[@Name='Main']/Row/T_ACTIVITY)"/></xsl:variable>
		<b><xsl:value-of select="$recCount"/> record(s) returned</b>
		<!--hr/><br/-->
		<xsl:call-template name="CommonFooter"/>
		<!--b>
			<a><xsl:attribute name="href">javascript:openPrintable();</xsl:attribute>
			Printable page</a><br/>
			Contents in this application is maintained by the <a href="mailto:eea@eea.eu.int">EEA</a>
			</b-->
	</td></tr>
  </table>
	<!-- 8 -->


  </div>
	</td>
	</tr>
</table>
<!-- main -->

<table>
<tr><td>
</td></tr>
</table>
		
	</body>
	</html>

</xsl:template>
</xsl:stylesheet>
