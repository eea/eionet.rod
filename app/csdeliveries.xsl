<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:include href="util.xsl"/>
<xsl:template match="/">

<html lang="en"><head><title>List of deliveries</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script language="JavaScript" src="script/util.js"></script>
	<script language="JavaScript">
					

	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
		//changeParamInsStr(document.URL, 'ORD', fld);
		//alert(fld);
	}
/*function openMetaData(url){
	var name = "CSMetaData";
	var features = "location=no, menubar=yes, width=780, height=550, top=100, left=200, scrollbars=yes";
	var w = window.open( url, name, features);
	w.focus();
} */


			</script>
			</head>
<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

<table  cellspacing="0" cellpadding="0" border="0"><tr valign="top">
	<td></td>

	<td>

		<div style="margin-left:13"><form action="rorabrowse.jsv" method="get" name="f">
		<input value="A" name="mode" type="hidden"/><table width="600" border="0" cellspacing="10">

	</table></form>

	<xsl:variable name="allCountries"><xsl:value-of select="count(child::XmlData/RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>

	<table border="0" width="600">
	<tr><td>
		<font color="#006666" face="Arial"><strong><span class="head2">
			<xsl:call-template name="FirstHeading"/>
		</span></strong></font>
	</td></tr>
	<tr><td>
		<font color="#006666" face="Arial" size="2"><strong><span class="head0">
			<xsl:call-template name="SecondHeading"/>
		</span></strong></font>
	</td></tr>
	<tr><td>&#160;</td></tr>

	</table>
	<table width="600" border="0">
	<!--xsl:for-each select="XmlData/RowSet[@Name='Main']/Row"-->
	<tr><td width="200" align="right">
   	<span class="head1">Status of deliveries: </span>
	</td><td><span class="head1">
		<xsl:choose>
		<xsl:when test="$allCountries=0">
			<xsl:value-of select="XmlData/RowSet[@Name='Main']/Row/T_SPATIAL/SPATIAL_NAME"/>
		</xsl:when>
		<xsl:otherwise>
			all countries
		</xsl:otherwise>
		</xsl:choose>
		</span>
	</td></tr>
	<tr><td colspan="2">&#160;</td></tr>
		<tr>
			<td width="200" align="right"><span class="head0">Reporting activity:</span></td>
			<td width="400"><xsl:value-of select="XmlData/RowSet[@Name='Main']/Row/T_ACTIVITY/TITLE"/></td>
		</tr>
		<tr>
			<td width="200" align="right"><span class="head0">Reporting frequency:</span></td>
			<td width="400">
			<xsl:apply-templates select="XmlData/RowSet[@Name='Main']/Row"/>
			</td>
		</tr>
		<tr>
			<td width="200" align="right"><span class="head0">Client organisation:</span></td>
			<td width="400"><xsl:value-of select="XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
		</tr>
		<tr>
			<td width="200" align="right"><span class="head0">Reporting guidelines:</span></td>
			<td width="400"><a target="RA_giudelines"><xsl:attribute name="href"><xsl:value-of select="XmlData/RowSet/Row/T_ACTIVITY/REPORT_FORMAT_URL"/></xsl:attribute>
				<xsl:value-of select="XmlData/RowSet/Row/T_ACTIVITY/FORMAT_NAME"/></a></td>
		</tr>
	<!--/xsl:for-each-->
	</table>
	<br/><div style="margin-left:20"></div>
	 
<!-- oneCountry=0 one country, one country = 1 all countries -->

<TABLE cellSpacing="0" cellPadding="2" width="900" border="0">
<TBODY>
<TR>
	<!-- country -->
	<xsl:if test="$allCountries=1">
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="150" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR><TD><SPAN class="headsmall"><B><FONT title="Country" face="Verdana" color="#000000" size="1">Country</FONT></B></SPAN></TD>
  				<TD><P align="right"><MAP name="FPMap1"><AREA shape="RECT" alt="Sort by country" coords="0,0,16,7" href="javascript:setOrder('SPATIAL_NAME DESC')"/>
								<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('SPATIAL_NAME')"/></MAP>
								<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap1" border="0"/></P>
				</TD></TR>
			</TBODY>
		</TABLE>
	</TD>
	</xsl:if>
	<!-- contact -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="175" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Responsible persons" face="Verdana" color="#000000" size="1">Contact</FONT></B></SPAN></TD>
				<TD>
						<P align="right">
							<MAP name="FPMap2">
								<AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('ROLE_ID DESC')"/>
								<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('ROLE_ID')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap2" border="0"/>
						</P>
					</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- delivery title -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="350" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Title of delivery" face="Verdana" color="#000000" size="1">Delivery Title</FONT></B></SPAN></TD>
				<TD>
						<P align="right">
							<MAP name="FPMap3">
								<AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('T_DELIVERY.TITLE DESC')"/>
								<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('T_DELIVERY.TITLE')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap3" border="0"/>
						</P>
					</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- delivery date -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="100" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Delivery Date</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap4"><AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('UPLOAD_DATE DESC')"/>
						<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('UPLOAD_DATE')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap4" border="0"/></P>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- period covered -->
	<TD style="BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="125" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Period covered</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap5"><AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('COVERAGE DESC')"/>
						<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('COVERAGE')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap5" border="0"/></P>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- empty td -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="*" bgColor="#ffffff">&#160;
	</TD>
	<!-- END of header ROW -->
	</TR>
	<xsl:for-each select="XmlData/RowSet[@Name='Main']/Row">
	<TR>
		<xsl:attribute name="bgColor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>
	<xsl:if test="$allCountries=1">
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<SPAN class="Mainfont"><FONT face="Verdana" size="2"><xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/></FONT></SPAN>
	</TD>
	</xsl:if>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top"><SPAN class="Mainfont">
			 <FONT face="Verdana" size="2">
				<xsl:if test="T_ACTIVITY/RESPONSIBLE_ROLE != ''">
						<xsl:choose>
						<xsl:when test="T_ROLE/ROLE_NAME=''">
							<xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE"/>-<xsl:value-of select="T_SPATIAL/SPATIAL_TWOLETTER"/>
						</xsl:when>
						<xsl:otherwise>
						<a>
						<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_URL"/>')</xsl:attribute>
							<xsl:value-of select="T_ROLE/ROLE_NAME"/>
						</a>
						<img src="images/button_role.gif" alt="Log in to CIRCA and see all the information">
							<xsl:attribute name="onClick">javascript:openCirca('<xsl:value-of select="T_ROLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
						</img>
						</xsl:otherwise>
						</xsl:choose>
				</xsl:if>
				&#160;
			 </FONT></SPAN>
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top">
			 <SPAN class="Mainfont">
					<a target="ROD_delivery">
					<xsl:attribute name="href">
						<xsl:value-of select="T_DELIVERY/DELIVERY_URL"/>
					</xsl:attribute>
					<xsl:value-of select="T_DELIVERY/TITLE"/>
					</a>
			</SPAN>
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top">
			 <SPAN class="Mainfont">
					<xsl:choose>
					<xsl:when test="T_DELIVERY/UPLOAD_DATE != '0000-00-00'">
						<xsl:value-of select="T_DELIVERY/UPLOAD_DATE"/>
					</xsl:when>
					<xsl:otherwise>
						&lt;No date&gt;
					</xsl:otherwise>
					</xsl:choose>
			 </SPAN>
	</TD>
	<TD style="BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" valign="left">
			 <SPAN class="Mainfont">
				<xsl:value-of select="T_DELIVERY/COVERAGE"/>
			 </SPAN>
			 &#160;
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid" 
       vAlign="top">&#160;
	</TD>
</TR>
</xsl:for-each>
</TBODY></TABLE>


<!--table width="650" cellspacing="2pts" border="1">
<tr>
<td bgcolor="#646666" align="center" width="200"><span class="head0"><font color="#FFFFFF">Delivery</font></span></td>
<td bgcolor="#646666" align="center" width="100"><span class="head0"><font color="#FFFFFF"><span lang="en-us">Date</span></font></span></td>
<td bgcolor="#646666" align="center" width="120"><span class="head0"><font color="#FFFFFF">Metadata</font></span></td>
<xsl:if test="$allCountries=1">
	<td bgcolor="#646666" align="center" width="130"><span class="head0"><font color="#FFFFFF">Country</font></span></td>
</xsl:if>

<td width="*"></td>
</tr>

<xsl:for-each select="XmlData/RowSet[@Name='Main']/Row">
<tr valign="top" height="20">
<td>
	<a target="RA">
	<xsl:attribute name="href">
		<xsl:value-of select="T_DELIVERY/DELIVERY_URL"/>
	</xsl:attribute>
	<xsl:value-of select="T_DELIVERY/TITLE"/>
	</a>
</td>
<td align="center">
	<xsl:choose>
	<xsl:when test="T_DELIVERY/UPLOAD_DATE != '0000-00-00'">
		<xsl:value-of select="T_DELIVERY/UPLOAD_DATE"/>
	</xsl:when>
	<xsl:otherwise>
		&lt;No date&gt;
	</xsl:otherwise>
	</xsl:choose>
</td>

<td>
	<a>
	<xsl:attribute name="href">javascript:openMetaData("<xsl:value-of select='T_DELIVERY/CONTREG_URL'/>")</xsl:attribute>
	Show metadata
	</a>
</td>

<xsl:if test="$allCountries=1">
<td>
	<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
</td>
</xsl:if>
<td></td>
</tr>	
</xsl:for-each>
</table-->

  </div>	</td>
	</tr>
<tr><td></td></tr>
</table>

<!--a href="javascript:close()">close window</a-->


</body>
</html>

</xsl:template>
<xsl:template match="XmlData/RowSet[@Name='Main']/Row">
		<xsl:if test="position()=1">
			<xsl:call-template name="RAReportingFrequency"/>
		</xsl:if>
</xsl:template>
</xsl:stylesheet>
