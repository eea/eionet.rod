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


function openPrintable(){
	var url = document.URL + "&#038;MODE=PR";
	//alert(url);
	var name = "CSPrintMain";
	var features = "location=no, menubar=yes, width=500, height=500, top=100, left=200";
	window.open( url, name, features);
}
function openPersons(ROLE){
	//alert("role = " + ROLE);
	var url = "cspersons?ROLE=" + ROLE; //  + "&#038;mi6";
	var name = "CSPersons";
	var features = "location=no, menubar=no, width=350, height=300, top=100, left=200";
	window.open( url, name, features);
}
function openDeliveries(ACT_ID){
	//alert("actID = " + ACT_ID);
	var url = "csdeliveries?ACT_DETAILS_ID=" + ACT_ID ; // + "&#038;mi6";
	var name = "CSDeliveries";
	var features = "location=no, menubar=no, width=500, height=400, top=100, left=200";
	window.open( url, name, features);

}


<![CDATA[

	function changeParamInString(sUrl, sName, sValue){
		var  i, j,  sBeg, sEnd, sStr;

		sValue=escape(sValue);

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

	<!-- main -->
	<table  cellspacing="0" cellpadding="0" border="0">
	<tr valign="top" height="500">
  <!--td nowrap="" width="130" bgcolor="#747400">
  <p/>
	
	</td-->
	<td>
	<div style="margin-left:13"><form action="rorabrowse.jsv" method="get" name="f">
	
	</form>
	<!-- 6 -->
	<xsl:variable name="oneCountry"><xsl:value-of select="count(child::XmlData/RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>
	<table border="0" width="600" cellspacing="7"><tr><td>
    <span class="head1"><span lang="en-us">Reporting overview: </span>
		<xsl:if test="$oneCountry=0">
			<xsl:value-of select="XmlData/RowSet/Row/T_SPATIAL/SPATIAL_NAME"/>
		</xsl:if>
    </span>
		<!--span class="head0">&#160;&#160;&#160;<a href="cssearch">[Advanced search]</a></span--></td></tr>
	</table>
	<!-- 6 -->
	<br/><div style="margin-left:20">
	</div>
	<!-- 8 -->


	<table width="630" cellspacing="7pts">

	<xsl:if test="count(child::XmlData/RowSet[@Name='Main']/Row)!=0">
		<tr>
				<td bgcolor="#646666" align="center" width="10"> </td>
				<td bgcolor="#646666" align="center" width="147"><span class="head0"><font color="#FFFFFF">Reporting org.</font> </span></td>
				<td bgcolor="#646666" align="center" width="147"><font color="#FFFFFF"><span class="head0">Reporting to</span></font></td>
				<td bgcolor="#646666" align="center" width="100"><span class="head0"><font color="#FFFFFF">Deliveries</font></span></td>
				<td bgcolor="#646666" align="center" width="180"><span class="head0"><font color="#FFFFFF">Reporting Activity </font></span></td>
			  <td bgcolor="#646666" align="center" width="140">
				<p align="center"/><span class="head0">
			  <font color="#FFFFFF">Deadline
					</font></span>
				</td>
				<xsl:if test="$oneCountry !=0">
				  <td bgcolor="#646666" align="center" width="*"><span class="head0"><font color="#FFFFFF">Country</font></span></td>
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
					<xsl:value-of select="T_ACTIVITY_DETAILS/RESPONSIBLE_ROLE"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="RESPONSIBLE/ROLE_NAME"/>
			</xsl:otherwise>
			</xsl:choose>
			</font>
</td>
<td width="147"><font color="#646666">
				<xsl:value-of select="T_ACTIVITY_DETAILS/REPORT_TO"/>
			</font>
</td>
<td width="100">
		<font color="#999999">
			<xsl:choose>
					<xsl:when test="count(descendant::SubSet[@Name='Delivery']/Row) = 0 ">	
						None
					</xsl:when>
					<xsl:otherwise>
						Exist
					</xsl:otherwise>
			</xsl:choose>
	</font>
</td>
<td width="250">
	<span class="head0">
			<xsl:value-of select="T_ACTIVITY_DETAILS/TITLE"/>
		</span>
</td>
  <td width="140" align="center">
			<!-- check, where we are -->
			<xsl:choose>
			<xsl:when test="T_DEADLINE/DEADLINE != '' ">	
				<xsl:value-of select="T_DEADLINE/DEADLINE"/>
			</xsl:when>
			<xsl:otherwise>
			<xsl:choose>
						<!-- if there are future deadlines, we show the 1st from the future, otherwise last one from the past -->
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
			</xsl:choose>
	</td>
	<xsl:if test="$oneCountry !=0">
		<td width="*" align="left"><span lang="en-us"><xsl:value-of select="T_COUNTRY/COUNTRY_NAME"/></span></td>
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
			Contents in this application is maintained by the EEA.
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
