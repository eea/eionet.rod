<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:template match="/">


<xsl:variable name="item-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ITEM_TYPE"/>
	</xsl:variable>

<html lang="en"><head><title>History of changes</title>
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
		<tr><td width="400" align="right">
		<span class="head0">
			History of changing data of 
			<xsl:choose>
				<xsl:when test="$item-type='O'">
					Reporting obligation
				</xsl:when>
				<xsl:when test="$item-type='A'">
					Reporting Activity
				</xsl:when>
				<xsl:when test="$item-type='L'">
					Legal instrument
				</xsl:when>
			</xsl:choose>
		</span>
   </td>
	 <td align="left"><b> ID=<xsl:value-of select="XmlData/RowSet/Row/T_HISTORY/ITEM_ID"/> </b></td>
	 </tr>
	 </table>
	 <br/><div style="margin-left:20"><table cellspacing="7pts"></table></div>
	 
<table width="525" cellspacing="7pts">


<tr>
<!--td bgcolor="#646666" align="center" width="75"><span class="head0"><font color="#FFFFFF">Item ID</font></span></td-->
<td bgcolor="#646666" align="center" width="100"><span class="head0"><font color="#FFFFFF"><span lang="en-us">Time</span></font></span></td>
<td bgcolor="#646666" align="center" width="70"><span class="head0"><font color="#FFFFFF">Action</font></span></td>
<td bgcolor="#646666" align="center" width="100"><span class="head0"><font color="#FFFFFF">User</font></span></td>
<td bgcolor="#646666" align="center" width="*"><span class="head0"><font color="#FFFFFF">Description</font></span></td>
</tr>

<xsl:for-each select="XmlData/RowSet/Row">
<tr valign="top">
<!-- table row -->
<!--td>
	<xsl:value-of select="T_HISTORY/ITEM_ID"/>
</td-->
<td align="center" width="100">
	<xsl:value-of select="T_HISTORY/TIME_STAMP"/>
</td>
<td width="70">
	<xsl:choose>
	<xsl:when test="T_HISTORY/ACTION_TYPE='I'">
		Insert
	</xsl:when>
	<xsl:when test="T_HISTORY/ACTION_TYPE='U'">
		Update
	</xsl:when>
	<xsl:when test="T_HISTORY/ACTION_TYPE='D'">
		Delete
	</xsl:when>
	</xsl:choose>
</td>
<td width="100">
	<xsl:value-of select="T_HISTORY/USER"/>
</td>
<td width="*">
	<xsl:value-of select="T_HISTORY/DESCRIPTION"/>
</td>

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
