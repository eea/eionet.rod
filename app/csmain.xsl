<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>


<xsl:template match="/">

<html lang="en"><head><title>Country Services</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>

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

function openCirca(url){
	//var url = "cspersons?ROLE=" + ROLE; //  + "&#038;mi6";
	//alert(url);
	var name = "CSCIRCA";
	var features = "location=yes, menubar=yes, width=750, height=600, top=30, left=50, resizable=yes, SCROLLABLE=YES";
	var w = window.open( url, name, features);
	w.focus();

}

function openFeedback(){
	fwdUrl="http://213.168.23.13:81/countrysrv/public/fb_response.html";
	var name = "CSFeedback";
	var features = "location=no, menubar=yes, width=700, height=500, top=100, left=200, scrollbars=yes, resizable=yes";
	var url = "http://213.168.23.13:81/wftool/feedback.jsp?fwd=" + fwdUrl + "&#038;pn=Country Services&#038;pv=1.0&#038;fn=Main" ;
	var w = window.open( url, name, features);
	w.focus();
}

function openPrintable(){
	var url = document.URL + "&#038;MODE=PR";
	//alert(url);
	var name = "CSPrintMain";
	var features = "location=no, menubar=yes, width=700, height=500, top=100, left=200, scrollbars=yes";
	var w = window.open( url, name, features);
	w.focus();
}
function openPersons(ROLE){
	//alert("role = " + ROLE);
	var url = "cspersons?ROLE=" + ROLE; //  + "&#038;mi6";
	var name = "CSPersons";
	var features = "location=no, menubar=no, width=430, height=300, top=100, left=200";
	var w = window.open( url, name, features);
	w.focus();
	//var o = window.showModalDialog() ; 
}
function openDeliveries(ACT_ID, COUNTRY_ID){
	//alert("actID = " + ACT_ID);
	var url = "csdeliveries?ACT_DETAILS_ID=" + ACT_ID ; // + "&#038;mi6";
	url = url + "&amp;COUNTRY_ID=" + COUNTRY_ID;
	//alert(url);
	var name = "CSDeliveries";
	var features = "location=no, menubar=no, width=640, height=400, top=100, left=200, scrollbars=yes";
	var w = window.open( url, name, features);
	w.focus();

}


<![CDATA[

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

<!-- main table -->
<table cellspacing="0" cellpadding="0" border="0">

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

<map name="sortCountry">
<area shape="rect" coords="0,0, 11,5" href="javascript:changeParamInString(document.URL,'ORD','SPATIAL_NAME')" alt="Click to sort in ascending order" />
<area shape="rect" coords="12,0, 23,5" href="javascript:changeParamInString(document.URL,'ORD','SPATIAL_NAME DESC')" alt="Click to sort in descending order" />
</map>


<tr>
  <td valign="top" bgcolor="#747400" width="130">
  <img alt="" width="130" height="113" src="images/top1.jpg"/></td><td valign="top" width="20">
  <img alt="" src="images/top2.jpg" width="20" height="113"/></td><td valign="top" width="621">

		<!-- table 2 -->
		<table cellspacing="0" cellpadding="0" border="0">
		<tr><td>
			<img alt="" height="35" width="92" src="images/top3.jpg"/></td></tr><tr><td>
			<!-- table 3 -->
			<table width="621" border="0">
				<tr><td width="618">
					<span class="head2"><strong><font color="#006666">Reporting Obligations Database, ROD</font></strong></span><font face="Arial" size="5" color="#006666"><strong><span class="head2">
					</span></strong></font><br/><span class="head0"><strong><font color="#006666">Ver 1.0 beta. The database contents is under establishment</font></strong></span>
				</td><td width="50"></td>
				<td><img border="0" width="66" height="62" alt="" src="images/logo.jpg"/></td></tr>
			</table>
			<!-- 2 -->
		</td></tr>
		</table>
	
	</td></tr>
	</table>
	<!-- main table -->

	<!-- main -->
	<table  cellspacing="0" cellpadding="0" border="0">
	<tr valign="top" height="500">
  <td nowrap="" width="130" bgcolor="#747400">
	<p>
	<center>
	<!-- table 2 -->
	<table cellspacing="0" cellpadding="0" border="0"><tr>
	<td align="center"><span class="head0">Contents</span></td></tr>
  <!--tr><td align="right"><a onMouseOver="Over('img0')" onMouseOut="Out('img0')" href="show.jsv?id=1&#038;mode=C">
			  <img alt="" border="0" src="images/off.gif" name="img0" width="16" height="13"/><img alt="Legislation" height="13" width="84" border="0" src="images/button_legislation.gif"/></a>
			</td>
  </tr-->
  <tr>
  <td align="right">
  <a onClick="Click('img1')" onMouseOut="Out('img1')" onMouseOver="Over('img1')" href="rorabrowse.jsv?mode=R">
    <img alt="" border="0" src="images/off.gif" name="img1" width="16" height="13"/><img alt="Reporting Obligations" height="13" width="84" border="0" src="images/button_obligations.gif"/></a></td>
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
    <img alt="" border="0" src="images/off.gif" name="img3" width="16" height="13"/><img alt="Reported Data Sets" height="13" width="84" border="0" src="images/button_cs.gif"/></a></td>
  </tr>
  <!--tr>
  <td align="right">
  <a onClick="Click('img4')" onMouseOut="Out('img4')" onMouseOver="Over('img4')" href="javascript:openFeedback()">
    <img alt="" border="0" src="images/off.gif" name="img4" width="16" height="13"/><img alt="Send feedback about the application" height="13" width="84" border="0" src="images/button_feedback.gif"/></a></td>
  </tr-->
  </table>
	<!-- 2 -->
	</center></p>
  <p/>
	
	</td>
	<td>
	<!-- 3 -->
	<table border="0" width="621" cellpadding="0" cellspacing="0"><tr><td height="25" background="images/bar_filled.jpg" width="20" align="bottom"> </td>
	<td height="25" background="images/bar_filled.jpg" width="600">
	<!-- 4 -->
	<table border="0" background="" cellPadding="0" cellSpacing="0" height="8">
		<tr><td width="92" align="middle" valign="bottom"><a href="http://www.eionet.eu.int/"><span class="barfont">EIONET</span></a></td>
				<td width="28" valign="bottom"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td width="92" align="middle" valign="bottom"><a href="index.html"><span class="barfont">WebROD</span></a></td>
				<td width="28" valign="bottom"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td width="122"	align="middle" valign="bottom"><a href="deliveries.jsv"><span class="barfont">Deliveries</span></a></td>
				<td width="28" valign="bottom"><img src="images/bar_hole.jpg" width="28" height="24"/></td>
				<td width="122"	align="middle" valign="bottom"><!--a href="csindex"--><span class="barfont">Reporting Overview</span><!--/a--></td>
				<td width="28" valign="bottom"><img src="images/bar_dot.jpg" width="28" height="25"/></td><td width="2 10" align="right" valign="bottom"></td></tr>
	</table>
	<!-- 4 -->
	</td></tr><tr><td> </td></tr>
	</table>
	<!-- 3 -->
	<div style="margin-left:13"><form action="rorabrowse.jsv" method="get" name="f">
	
	<!-- 5 -->
	<table width="600" border="0" cellspacing="10"><tr>
  </tr>
	</table>
	<!-- 5 -->
	</form>
	<!-- 6 -->
	<xsl:variable name="oneCountry"><xsl:value-of select="count(child::XmlData/RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>
	<table border="0">
	<tr><td width="475">
    <span class="head1"><span lang="en-us">Reporting overview: </span>
		<xsl:if test="$oneCountry=0">
			<xsl:value-of select="XmlData/RowSet/Row/T_SPATIAL/SPATIAL_NAME"/>
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


	<table width="630" cellspacing="7pts">

	<xsl:if test="count(child::XmlData/RowSet[@Name='Main']/Row)!=0">
		<tr>
				<td bgcolor="#646666" align="center" width="10"> </td>
				<td bgcolor="#646666" align="center" width="147"><span class="head0" title="Local organisation, responsible for reporting"><font color="#FFFFFF">Responsible</font> </span></td>
				<!--td bgcolor="#646666" align="center" width="10"></td-->
				<td bgcolor="#646666" align="center" width="147"><font color="#FFFFFF"><span class="head0" title="Organisation to report to">Reporting to <IMG name="updown" border="0" src="images/updown.gif" usemap="#sortClient"></IMG></span></font></td>
				<td bgcolor="#646666" align="center" width="100"><span class="head0" title="List of deliveries for the given RA"><font color="#FFFFFF">Deliveries</font></span></td>
				<td bgcolor="#646666" align="center" width="180"><span class="head0"><font color="#FFFFFF" title="Title of the reporting activity">Reporting Activity <IMG name="updown" border="0" src="images/updown.gif" usemap="#sortTitle"></IMG></font></span></td>
			  <td bgcolor="#646666" align="center" width="140">
				<p align="center"/><span class="head0" title="Deadline for reporting">
			  <font color="#FFFFFF">Deadline
					<!--xsl:if test="XmlData/RowSet/Row/T_DEADLINE/DEADLINE !=''"-->
						<IMG name="sort" border="0" src="images/updown.gif" usemap="#sortDeadline"></IMG>
					<!--/xsl:if-->
					</font></span>
				</td>
				<xsl:if test="$oneCountry !=0">
				  <td bgcolor="#646666" align="center" width="*"><span class="head0"><font color="#FFFFFF">Country <IMG name="updown" border="0" src="images/updown.gif" usemap="#sortCountry"></IMG></font></span></td>
				</xsl:if>
				
  </tr>
	</xsl:if>

	<xsl:if test="count(child::XmlData/RowSet[@Name='Main']/Row)=0">
		<b>No deliveries are matching to the search criterias</b>
	</xsl:if>

	<xsl:if test="count(child::XmlData/RowSet[@Name='Main']/Row)!=0">
	<!-- table row start -->
	<xsl:for-each select="XmlData/RowSet/Row">
  <tr>
<td width="10">
<!-- image, if deadline OK -->
<img src="images/diamlil.gif" width="8" height="9"/></td>
<td width="163"><font color="#646666">
			<xsl:choose>
			<xsl:when test="RESPONSIBLE/ROLE_NAME=''">
				<!--No contact-->
				<xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE"/>-<xsl:value-of select="T_SPATIAL/SPATIAL_TWOLETTER"/>
			</xsl:when>
			<xsl:otherwise>
			<a>
			<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_URL"/>')</xsl:attribute>
				<xsl:value-of select="RESPONSIBLE/ROLE_NAME"/>
			</a>
			</xsl:otherwise>
			</xsl:choose>
			</font>
			
</td>
<!--td width="10">
<img src="images/diamlil.gif" width="8" height="9"/>
</td-->
<td width="147"><font color="#646666">
		<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
		</font>
</td>
<td width="100">
		<font color="#999999">
			<xsl:choose>
					<xsl:when test="count(descendant::SubSet[@Name='Delivery']/Row) = 0 ">	
						None
					</xsl:when>
					<xsl:otherwise>
						<a window="delivery">
						<xsl:attribute name="href">javascript:openDeliveries(<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>, <xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>)</xsl:attribute>
							Show list
						</a>
					</xsl:otherwise>
			</xsl:choose>
	</font>
</td>
<td width="250">
	<span class="head0">
		<a>
			<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_ACTIVITY/FK_RO_ID"/>&amp;mode=A</xsl:attribute>
			<xsl:value-of select="T_ACTIVITY/TITLE"/>
		</a>
		</span>
</td>
  <td width="140" align="center">
			<!-- check, where we are -->
			<xsl:value-of select="T_ACTIVITY/DEADLINE"/>
<!-- remove deadline parsing -->
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
		<br/><hr/><br/>
		<b><!--Document last modified: no info for last modifying-->
			<a><xsl:attribute name="href">javascript:openPrintable();</xsl:attribute>
			Printable page</a><br/>
			Contents in this application is maintained by the <a href="mailto:eea@eea.eu.int">EEA</a>.
			<!--a><xsl:attribute name="href">mailto:eea@eea.eu.int</xsl:attribute>Feedback.</a-->
			</b>
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
