<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:template match="/">

<html lang="en"><head><title>List of deliveries</title>
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

var browser = document.all ? 'E' : 'N';

var picklist = new Array();



function openMetaData(url){
	//var url = document.URL + "&#038;MODE=PR";
	//alert(url);
	var name = "CSMetaData";
	var features = "location=no, menubar=yes, width=780, height=550, top=100, left=200, scrollbars=yes";
	var w = window.open( url, name, features);
	w.focus();
}


			</script>
			</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

<table  cellspacing="0" cellpadding="0" border="0"><tr valign="top">
	<td></td>

	<td>

		<div style="margin-left:13"><form action="rorabrowse.jsv" method="get" name="f">
		<input value="A" name="mode" type="hidden"/><table width="600" border="0" cellspacing="10">

	</table></form>

	<table border="0" width="525">
	<tr><td>
   	<span class="head1">Reporting deliveries by <xsl:value-of select="XmlData/RowSet/Row/T_SPATIAL/SPATIAL_NAME"/></span>
	</td></tr>
		<tr><td>
		<span class="head0"><a target="RA"> <!-- need to direct to another window ????? -->
		<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="XmlData/RowSet/Row/T_ACTIVITY/PK_RA_ID"/>&amp;aid=<xsl:value-of select="XmlData/RowSet/Row/T_ACTIVITY/FK_RO_ID"/>&amp;mode=A</xsl:attribute>
		<xsl:value-of select="XmlData/RowSet/Row/T_ACTIVITY/TITLE"/>
		</a>
		</span>
   </td></tr>
	 </table>
	 <br/><div style="margin-left:20"><table cellspacing="7pts"></table></div>
	 
<table width="525" cellspacing="7pts">


<tr>
<td bgcolor="#646666" align="center" width="175"><span class="head0"><font color="#FFFFFF">Delivery</font></span></td>
<td bgcolor="#646666" align="center" width="100"><span class="head0"><font color="#FFFFFF"><span lang="en-us">Date</span></font></span></td>
<td bgcolor="#646666" align="center" width="70"><span class="head0"><font color="#FFFFFF">Format</font></span></td>
<td bgcolor="#646666" align="center" width="100"><span class="head0"><font color="#FFFFFF">Metadata</font></span></td>
<td width="*"></td>
</tr>

<xsl:for-each select="XmlData/RowSet/Row">
<tr>
<!-- table row -->
<td width="175">
	<a target="RA">
	<xsl:attribute name="href">
		<xsl:value-of select="T_DELIVERY/DELIVERY_URL"/>
	</xsl:attribute>
	<xsl:value-of select="T_DELIVERY/TITLE"/>
	</a>
</td>
<td align="center" width="100">
	<xsl:value-of select="T_DELIVERY/UPLOAD_DATE"/>
</td>
<td width="70">
	<xsl:value-of select="T_DELIVERY/FORMAT"/>
</td>
<td width="100">
	<a>
	<xsl:attribute name="href">javascript:openMetaData("<xsl:value-of select='T_DELIVERY/CONTREG_URL'/>")</xsl:attribute>
	Show metadata
	</a>
</td>
<!-- end of table row -->
<td width="*"></td>
</tr>	
</xsl:for-each>
  </table>

  </div>	</td>
	</tr>
<tr><td></td></tr>
</table>

<!--a href="javascript:close()">close window</a-->


</body>
</html>

</xsl:template>
</xsl:stylesheet>
