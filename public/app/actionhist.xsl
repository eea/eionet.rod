<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:template match="/">


<xsl:variable name="action-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ACTION_TYPE"/>
</xsl:variable>

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


<![CDATA[

function openHistory(ID,TYPE) {
	document.location="history.jsv?entity=" + TYPE + "&id= " + ID;
}

]]>
			</script>
			</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">


	<table border="0" width="100%">
		<tr valign="top">
		<td width="100%" align="center">
		<span class="head0">
			<xsl:choose>
				<xsl:when test="$action-type='I'">
					Inserted
				</xsl:when>
				<xsl:when test="$action-type='U'">
					Updated
				</xsl:when>
				<xsl:when test="$action-type='D'">
					Deleted
				</xsl:when>
			</xsl:choose>
			Items of type 
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
	 </tr>
	 </table>
	 <br/>
	 

<table width="100%" cellspacing="3pts" border="1">


<tr>
<td bgcolor="#646666" align="center" width="10%"><span class="head0"><font color="#FFFFFF">Item ID</font></span></td>
<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF"><span lang="en-us">Type</span></font></span></td>
<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF"><span lang="en-us">Time</span></font></span></td>
<td bgcolor="#646666" align="center" width="15%"><span class="head0"><font color="#FFFFFF">Action</font></span></td>
<td bgcolor="#646666" align="center" width="25%"><span class="head0"><font color="#FFFFFF">User</font></span></td>
<!--td bgcolor="#646666" align="center" width="*"><span class="head0"><font color="#FFFFFF">Description</font></span></td-->
</tr>

<xsl:for-each select="XmlData/RowSet/Row">
<tr>
<!-- table row -->
<td>
  <a title="Show the change record of this item">
	<xsl:attribute name="href">javascript:openHistory('<xsl:value-of select="T_HISTORY/ITEM_ID"/>','<xsl:value-of select="T_HISTORY/ITEM_TYPE"/>')</xsl:attribute>
	<xsl:value-of select="T_HISTORY/ITEM_ID"/>
	</a>
</td>
<td>
	<xsl:choose>
	<xsl:when test="T_HISTORY/ITEM_TYPE='O'">
		Reporting Obligation
	</xsl:when>
	<xsl:when test="T_HISTORY/ITEM_TYPE='A'">
		Reporting Activity
	</xsl:when>
	<xsl:when test="T_HISTORY/ITEM_TYPE='L'">
		Legal Instrument
	</xsl:when>
	</xsl:choose>

</td>
<td align="center">
	<xsl:value-of select="T_HISTORY/TIME_STAMP"/>
</td>
<td>
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
<td>
	<xsl:value-of select="T_HISTORY/USER"/>
</td>
</tr>	
</xsl:for-each>
  </table>

</body>
</html>

</xsl:template>
</xsl:stylesheet>
