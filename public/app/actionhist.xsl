<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!--xsl:include href="common.xsl"/-->
<xsl:template match="/">


<xsl:variable name="action-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ACTION_TYPE"/>
</xsl:variable>

<xsl:variable name="item-type">
		<xsl:value-of select="/XmlData/RowSet/Row/T_HISTORY/ITEM_TYPE"/>
</xsl:variable>

<html lang="en"><head><title>History of changes</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript">
					
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
					Reporting obligation
				</xsl:when>
				<xsl:when test="$item-type='L'">
					Legislative instrument
				</xsl:when>
			</xsl:choose>
		</span>
   </td>
	 </tr>
	 </table>
	 <br/>

<div style="margin-left:2">

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td style="border-right: 1px solid #008080; border-left: 1px solid #008080; border-top: 1px solid #008080;border-bottom: 1px solid #008080" bgcolor="#FFFFFF" align="center" width="15%"><span class="head0">Item ID</span></td>
		<td style="border-right: 1px solid #008080; border-top: 1px solid #008080;border-bottom: 1px solid #008080; " bgcolor="#FFFFFF" align="center" width="25%"><span class="head0">Type</span></td>
		<td style="border-right: 1px solid #008080; border-top: 1px solid #008080;border-bottom: 1px solid #008080; " bgcolor="#FFFFFF" align="center" width="25%"><span class="head0">Time</span></td>
		<td style="border-right: 1px solid #008080; border-top: 1px solid #008080;border-bottom: 1px solid #008080; " bgcolor="#FFFFFF" align="center" width="15%"><span class="head0">Action</span></td>
		<td style="border-right: 1px solid #008080; border-top: 1px solid #008080;border-bottom: 1px solid #008080; " bgcolor="#FFFFFF" align="center" width="25%"><span class="head0">User</span></td>
	</tr>

<xsl:for-each select="XmlData/RowSet/Row">
<tr>
	<xsl:attribute name="bgcolor">
		<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
	</xsl:attribute>
	<td style="border-right: 1px solid #C0C0C0; border-left: 1px solid #008080; border-bottom: 1px solid #C0C0C0">
		<a title="Show the change record of this item">
		<xsl:attribute name="href">javascript:openHistory('<xsl:value-of select="T_HISTORY/ITEM_ID"/>','<xsl:value-of select="T_HISTORY/ITEM_TYPE"/>')</xsl:attribute>
		<xsl:value-of select="T_HISTORY/ITEM_ID"/>
		</a>
	</td>
	<td style="border-right: 1px solid #C0C0C0; border-bottom: 1px solid #C0C0C0">
		<xsl:choose>
		<xsl:when test="T_HISTORY/ITEM_TYPE='O'">
			Reporting Obligation
		</xsl:when>
		<xsl:when test="T_HISTORY/ITEM_TYPE='A'">
			Reporting Activity
		</xsl:when>
		<xsl:when test="T_HISTORY/ITEM_TYPE='L'">
			Legislative Instrument
		</xsl:when>
		</xsl:choose>

	</td>
	<td align="center" style="border-right: 1px solid #C0C0C0; border-bottom: 1px solid #C0C0C0">
		<xsl:value-of select="T_HISTORY/TIME_STAMP"/>
	</td>
	<td style="border-right: 1px solid #C0C0C0; border-bottom: 1px solid #C0C0C0">
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
	<td style="border-right: 1px solid #008080; border-bottom: 1px solid #C0C0C0">
		<xsl:value-of select="T_HISTORY/USER"/>
	</td>
</tr>	
</xsl:for-each>
</table>

</div>

</body>
</html>

</xsl:template>
</xsl:stylesheet>
