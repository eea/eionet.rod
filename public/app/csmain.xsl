<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="common.xsl"/>
<xsl:include href="util.xsl"/>

	<xsl:variable name="admin">
		<xsl:value-of select="//RowSet[position()=1]/@auth"/>
	</xsl:variable>
		
		<xsl:variable name="sel_country">
				<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'COUNTRY_ID='),'&amp;')"/>
		</xsl:variable>


		<xsl:variable name="sel_issue">
			<xsl:value-of select="substring-before(substring-after(/XmlData/xml-query-string,'ISSUE_ID='),'&amp;')"/>
			<!--xsl:value-of select="translate(substring-before(substring-after(substring-after(/XmlData/xml-query-string, 'env_issue='),'%3A'),'&amp;'),'+',' ')"/-->
		</xsl:variable>

		<xsl:variable name="sel_period">
			<xsl:value-of select="substring-after(/XmlData/xml-query-string,'DEADLINES=')"/>
		</xsl:variable>


<xsl:template match="XmlData">

<html lang="en"><head><title>Country Services</title>
	<META CONTENT="text/html; CHARSET=ISO-8859-1" HTTP-EQUIV="Content-Type"/><link type="text/css" rel="stylesheet" href="eionet.css"/>

<script language="JavaScript" src="script/util.js"></script>
<script language="JavaScript">

<![CDATA[


function openCirca(url){
	var name = "CSCIRCA";
	var features = "location=yes, menubar=yes, width=750, height=600, top=30, left=50, resizable=yes, SCROLLBARS=YES";
	var w = window.open( url, name, features);
	w.focus();

}

	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
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

	<!-- main -->
	<table  cellspacing="0" cellpadding="0" border="0">
	<tr valign="top" height="500">
	<td>

	<div style="margin-left:13">
	
	<form action="rorabrowse.jsv" method="get" name="f">
	
	<!-- 5 -->
	<table width="600" border="1" cellspacing="10"><tr>
  </tr>
	</table>
	<!-- 5 -->
	</form>
	<!-- 6 -->
	<xsl:variable name="oneCountry"><xsl:value-of select="count(child::RowSet[@Name='Dummy']/Row/T_DUMMY)"/></xsl:variable>
	<table border="0" width="98%">
	<tr><td width="90%">
    <span class="head1"><span lang="en-us">Reporting overview: </span>
		<xsl:if test="$oneCountry=0">
			<xsl:value-of select="//RowSet[@Name='CountryData']/Row/T_SPATIAL/SPATIAL_NAME"/>
		</xsl:if>
		<xsl:if test="$sel_issue!='' and $sel_issue != '0'">
		 [<xsl:value-of select="//RowSet[@Name='IssueData']/Row/T_ISSUE/ISSUE_NAME"/>] 
 		</xsl:if>
    </span>
		</td><td width="*" align="right" valign="top">
		</td></tr>
	</table>
	<!-- 6 -->
	<br/><div style="margin-left:20">
	<!-- 7 -->
	<!-- 7 -->
	</div>
	<!-- 8 -->
<form name="ff" method="get" action="csmain">
<table border="0"><tr>
<TD width="771">

	<input type="hidden" name="COUNTRY_ID">
		<xsl:attribute name="value">
				<xsl:value-of select="$sel_country"/>
		</xsl:attribute>
	</input>
<TABLE cellSpacing="0" cellPadding="3" width="100%" border="0">
		<TR>
			<TD vAlign="center" width="33%" bgColor="#ffffff" style="BORDER-LEFT: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid; BORDER-TOP: #008080 1px solid;"><FONT face="Verdana" size="1"><B>Select issue:</B></FONT></TD>
			<TD vAlign="center" width="19%" bgColor="#ffffff" style="BORDER-TOP: #008080 1px solid;"><B><FONT face="Verdana" size="1">Select deadline:</FONT></B></TD>
			<TD vAlign="center" width="7%" bgColor="#ffffff" style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid;">
				<P align="right">
				<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSMAIN1</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param><xsl:with-param name="green">Y</xsl:with-param></xsl:call-template>
				</P>
			</TD>
			<TD vAlign="top" align="right" width="41%" rowspan="2">
				<xsl:call-template name="Print"/><br/><br/>
				<a>
					<xsl:attribute name="href">cssearch</xsl:attribute>
					<img src="images/bb_advsearch.png" alt="" border="0"/>
				</a>
			</TD>
		</TR>
		<TR>
			<TD vAlign="center" style="BORDER-LEFT: #008080 1px solid; BORDER-RIGHT: #C0C0C0 1px solid; BORDER-BOTTOM: #008080 1px solid">
				<SELECT style="FONT-SIZE: 8pt; WIDTH: 240px; COLOR: #000000; HEIGHT: 23px; BACKGROUND-COLOR: #ffffff"  size="1" name="ISSUE_ID" height="20">
									<OPTION value="0" selected="true">All issues</OPTION>
									<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
				</SELECT>
			</TD>
			<TD vAlign="center" style="BORDER-BOTTOM: #008080 1px solid">
				<SELECT style="FONT-SIZE: 8pt; WIDTH: 129px; COLOR: #000000; HEIGHT: 33px; BACKGROUND-COLOR: #ffffff" size="1" name="DEADLINES"> 
					<OPTION value="0">
						<xsl:if test="$sel_period='' or $sel_period='0'"> 
							<xsl:attribute name="selected"/>
						</xsl:if>
					All deadlines
					</OPTION> 
					<OPTION value="1">
						<xsl:if test="$sel_period='1'"> 
							<xsl:attribute name="selected"/>
						</xsl:if>
					In the next month</OPTION> 
					<OPTION value="2"><xsl:if test="$sel_period='2'"><xsl:attribute name="selected"/></xsl:if>
							In the next 3 months</OPTION> 
					<OPTION value="3"><xsl:if test="$sel_period='3'"><xsl:attribute name="selected"/></xsl:if>In the next 6 months</OPTION> 
					<OPTION value="4"><xsl:if test="$sel_period='4'"><xsl:attribute name="selected"/></xsl:if>Previous months</OPTION>
				</SELECT>
			</TD>
			<TD vAlign="center" align="right" style="BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid">
				<a>
					<xsl:attribute name="href">javascript:document.forms["ff"].submit()</xsl:attribute>
					<img src="images/go.png" alt="" border="0"/>
				</a>
			</TD>
		</TR>
		</TABLE>
	</TD>
	</tr>
	</table>
	</form>

	&#160;
	<table width="777" cellspacing="0" border="0">

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">
		<TR>
	<!-- country -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="200" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR><TD><SPAN class="headsmall"><B><FONT title="Reporting Activity" face="Verdana" color="#000000" size="1">Reporting Activity</FONT></B></SPAN></TD>
  				<TD><P align="right"><MAP name="FPMap1"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('T_ACTIVITY.TITLE DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z"  coords="1,13,16,21" href="javascript:setOrder('T_ACTIVITY.TITLE')"/></MAP>
								<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap1" border="0"/></P>
				</TD></TR>
			</TBODY>
		</TABLE>
	</TD>

	<!-- client -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="70" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Responsible persons" face="Verdana" color="#000000" size="1">Reporting to</FONT></B></SPAN></TD>
				<TD>
						<P align="right">
							<MAP name="FPMap2">
								<AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('CLIENT_NAME DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z"  coords="1,13,16,21" href="javascript:setOrder('CLIENT_NAME')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap2" border="0"/>
						</P>
					</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- deadline -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
						vAlign="center" width="70" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Title of delivery" face="Verdana" color="#000000" size="1">Deadline</FONT></B></SPAN></TD>
				<TD>
						<P align="right">
							<MAP name="FPMap3">
								<AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('DEADLINE DESC')"/>
								<AREA shape="RECT" alt="Sort A-Z"  coords="1,13,16,21" href="javascript:setOrder('DEADLINE')"/>
							</MAP>
							<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap3" border="0"/>
						</P>
					</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- dl2 -->
	<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="70" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Next DL</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap4"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('DEADLINE2 DESC')"/>
						<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('DEADLINE2')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap4" border="0"/></P>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- responsible -->
	<TD style="BORDER-TOP: #008080 1px solid; BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="150" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Responsible</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap5"><AREA shape="RECT" alt="Sort Z-A" coords="0,0,16,7" href="javascript:setOrder('ROLE_NAME DESC')"/>
						<AREA shape="RECT" alt="Sort A-Z" coords="1,13,16,21" href="javascript:setOrder('ROLE_NAME')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap5" border="0"/></P>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<!-- empty td -->
	<TD style="BORDER-TOP: #008080 1px solid; BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="77" bgColor="#ffffff">

		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Deliveries</FONT></B></SPAN></TD>
				<TD>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>
	<xsl:if test="$oneCountry !=0">
	<TD style="BORDER-TOP: #008080 1px solid; BORDER-RIGHT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
		vAlign="center" width="*" bgColor="#ffffff">
		<TABLE cellSpacing="0" width="100%" border="0">
			<TBODY>
			<TR>
				<TD><SPAN class="headsmall"><B><FONT title="Date of delivery" face="Verdana" color="#000000" size="1">Country</FONT></B></SPAN></TD>
				<TD><P align="right"><MAP name="FPMap6"><AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('SPATIAL_NAME DESC')"/>
						<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('SPATIAL_NAME')"/></MAP>
						<IMG height="22" src="images/arrows.gif" width="17" useMap="#FPMap6" border="0"/></P>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</TD>

	</xsl:if>

	<!-- END of header ROW -->
	</TR>	
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)=0">
		<b>No deliveries match the search criteria</b>
	</xsl:if>

	<xsl:if test="count(child::RowSet[@Name='Main']/Row)!=0">

<!-- table row start -->
<xsl:for-each select="RowSet[@Name='Main']/Row">

  <tr valign="top">
		<xsl:attribute name="bgColor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>

	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-LEFT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="Mainfont">
			<a>
				<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;mode=A</xsl:attribute>
				<xsl:value-of select="T_ACTIVITY/TITLE"/>
			</a>
			<xsl:if test="T_SOURCE/SOURCE_CODE!=''">
				(<a>
					<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_SOURCE/PK_SOURCE_ID"/>&amp;mode=S</xsl:attribute>
					<xsl:value-of select="T_SOURCE/SOURCE_CODE"/>
				</a>)
			</xsl:if>
		</span>&#160;
	</TD>

	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="Mainfont">
			<a>
				<xsl:attribute name="href">javascript:openClient('<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
				<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
			</a>
		</span>&#160;
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="Mainfont">
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
		</span>&#160;
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="Mainfont">
				<xsl:if test="T_ACTIVITY/DEADLINE2 != '' ">	
					<xsl:value-of select="T_ACTIVITY/DEADLINE2"/>
				</xsl:if>
		</span>&#160;
	</TD>

	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="Mainfont">
	<xsl:if test="T_ACTIVITY/RESPONSIBLE_ROLE != ''">
			<xsl:choose>
			<xsl:when test="RESPONSIBLE/ROLE_NAME=''">
				<xsl:value-of select="T_ACTIVITY/RESPONSIBLE_ROLE"/>-<xsl:value-of select="T_SPATIAL/SPATIAL_TWOLETTER"/>
			</xsl:when>
			<xsl:otherwise>
			<a>
			<xsl:attribute name="href">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_URL"/>')</xsl:attribute>
				<xsl:value-of select="RESPONSIBLE/ROLE_NAME"/>
			</a>&#160;
			<img src="images/button_role.png" alt="Additional details for logged-in users">
				<xsl:attribute name="onClick">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
			</img>
			</xsl:otherwise>
			</xsl:choose>
	</xsl:if>
		</span>&#160;
	</TD>
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="Mainfont">
			<xsl:choose>
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
		</span>&#160;
	</TD>
	<xsl:if test="$oneCountry !=0">
	<TD style="BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid"  vAlign="top">
		<span class="Mainfont">
				<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
		</span>&#160;
	</TD>
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
		<xsl:call-template name="CommonFooter"/>
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

	<xsl:template match="RowSet[@Name='EnvIssue']">

		<xsl:for-each select="Row/T_ISSUE">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="PK_ISSUE_ID"/></xsl:attribute>
					<xsl:if test="PK_ISSUE_ID=$sel_issue">
							<xsl:attribute name="selected"/>
					</xsl:if>
				<xsl:value-of select="ISSUE_NAME"/>
			</option>
		</xsl:for-each>
	</xsl:template>


</xsl:stylesheet>
