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

<![CDATA[


function openCirca(url){
	//var url = "cspersons?ROLE=" + ROLE; //  + "&#038;mi6";
	//alert(url);
	var name = "CSCIRCA";
	var features = "location=yes, menubar=yes, width=750, height=600, top=30, left=50, resizable=yes, SCROLLBARS=YES";
	var w = window.open( url, name, features);
	w.focus();

}

	function setOrder(fld) {
		changeParamInString(document.URL,'ORD',fld)
		//alert(fld);
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


<!--map name="sortClient">
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
</map-->

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
	<!--xsl:variable name="oneIssue"><xsl:value-of select="count(child::RowSet[@Name='Main']/Row/T_RAISSUE_LNK)"/></xsl:variable>
	<xsl:variable name="selIssue">
	<xsl:if test="$oneIssue &gt; 0">
		<xsl:value-of select="RowSet[@Name='Main']/Row/T_RAISSUE_LNK/FK_ISSUE_ID"/>
	</xsl:if>
	</xsl:variable-->
	<table border="0" width="98%">
	<tr><td width="475">
    <span class="head1"><span lang="en-us">Reporting overview: </span>
		<xsl:if test="$oneCountry=0">
			<xsl:value-of select="RowSet/Row/T_SPATIAL/SPATIAL_NAME"/>
		</xsl:if>
    </span>
		</td><td width="*" align="right" valign="top">
		<!--a href="cssearch"><img border="0" src="images/bb_advsearch.png" alt="Advanced search"/></a><br/-->
		<!--span class="head0">&#160;&#160;&#160;
			<a href="cssearch">[Advanced search]</a>
			<a href="cssearch"><img border="0" src="images/bb_advsearch.png" alt="Advanced search"/></a><br/>
		</span-->
		</td></tr>
	</table>
	<!-- 6 -->
	<br/><div style="margin-left:20">
	<!-- 7 -->
	<!-- 7 -->
	</div>
	<!-- 8 -->
<table><tr>
<TD style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; BORDER-LEFT: #008080 1px solid; BORDER-BOTTOM: #008080 1px solid" 
          width="771">

<!--b>x:</b><xsl:value-of select="$selIssue"/-->
<form name="ff" method="get" action="csmain">
	<input type="hidden" name="COUNTRY_ID">
		<xsl:attribute name="value">
			<xsl:choose>
			<xsl:when test="$oneCountry=0 and RowSet/Row/T_SPATIAL/PK_SPATIAL_ID != '' ">
				<xsl:value-of select="RowSet/Row/T_SPATIAL/PK_SPATIAL_ID"/>
			</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
	</input>
<TABLE cellSpacing="0" cellPadding="3" width="100%" border="1">
	<TBODY>
		<TR>
			<TD vAlign="center" width="34%" bgColor="#ffffff"><FONT face="Verdana" size="1"><B>Select issue:</B></FONT>
			</TD>
			<TD vAlign="center" width="23%" bgColor="#ffffff"><B><FONT face="Verdana" size="1">Select deadline:</FONT></B></TD>
			<TD vAlign="center" width="21%" bgColor="#ffffff">&#160;</TD>
			<TD vAlign="center" width="22%" bgColor="#ffffff">
				<P align="right">
				<xsl:call-template name="Help"><xsl:with-param name="id">HELP_CSMAIN1</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
				<!--IMG height="16" alt="[ HELP ]" src="images/help_green.png" width="16" border="0"/--> 
				</P>
			</TD>
		</TR>
		<TR>
			<TD vAlign="center" width="34%">
				<SELECT style="FONT-SIZE: 8pt; WIDTH: 207px; COLOR: #000000; HEIGHT: 23px; BACKGROUND-COLOR: #ffffff"  size="1" name="ISSUE_ID" height="20">
									<OPTION value="0" selected="true">All issues</OPTION>
									<xsl:apply-templates select="RowSet[@Name='EnvIssue']"/>
				</SELECT>
			</TD>
			<TD vAlign="center" width="23%">
				<SELECT style="FONT-SIZE: 8pt; WIDTH: 129px; COLOR: #000000; HEIGHT: 33px; BACKGROUND-COLOR: #ffffff" size="1" name="DEADLINES"> 
					<OPTION value="0" selected="true">All deadlines</OPTION> 
					<OPTION value="1">In the next month</OPTION> 
					<OPTION value="2">In the next 3 months</OPTION> 
					<OPTION value="3">In the next 6 months</OPTION> 
					<OPTION value="4">Previous months</OPTION>
				</SELECT>
			</TD>
			<TD vAlign="center" width="21%">
                  <P align="left"><INPUT style="BORDER-RIGHT: #008080 1px solid; BORDER-TOP: #008080 1px solid; FONT-WEIGHT: bold; FONT-SIZE: 8pt; BORDER-LEFT: #008080 1px solid; COLOR: #008080; BORDER-BOTTOM: #008080 1px solid; FONT-FAMILY: Verdana; BACKGROUND-COLOR: #cbdcdc" type="submit" value="GO" name="B3"/></P>
			</TD>
					<TD vAlign="center" width="22%">
					<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
          <TBODY>
                    <TR>
                      <TD style="BORDER-RIGHT: #003366 1px solid; BORDER-TOP: #003366 1px solid; BORDER-LEFT: #003366 1px solid; BORDER-BOTTOM: #003366 1px solid" 
                      width="100%" bgColor="#ffffff" height="18">
					<P align="center"><B><FONT face="Verdana" size="1"><A href="cssearch">Advanced search</A></FONT></B></P></TD></TR></TBODY>
					</TABLE>
					
					</TD>
			</TR>
			</TBODY>
			</TABLE>
	</form>
	</TD>
	</tr>
	</table>

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
  				<TD><P align="right"><MAP name="FPMap1"><AREA shape="RECT" alt="Sort by country" coords="0,0,16,7" href="javascript:setOrder('T_ACTIVITY.TITLE DESC')"/>
								<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('T_ACTIVITY.TITLE')"/></MAP>
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
								<AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('CLIENT_NAME DESC')"/>
								<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('CLIENT_NAME')"/>
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
								<AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('DEADLINE DESC')"/>
								<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('DEADLINE')"/>
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
				<TD><P align="right"><MAP name="FPMap4"><AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('DEADLINE2 DESC')"/>
						<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('DEADLINE2')"/></MAP>
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
				<TD><P align="right"><MAP name="FPMap5"><AREA shape="RECT" alt="" coords="0,0,16,7" href="javascript:setOrder('ROLE_NAME DESC')"/>
						<AREA shape="RECT" alt=""  coords="1,13,16,21" href="javascript:setOrder('ROLE_NAME')"/></MAP>
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
	<!--tr>
				<td bgcolor="#646666" align="center" width="147"><span class="headsmall" title="Local organisation, responsible for reporting"><font color="#FFFFFF">Responsible </font><IMG name="updown" border="0" src="images/updown.gif" usemap="#sortRole"></IMG></span></td>
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
						<IMG name="sort" border="0" src="images/updown.gif" usemap="#sortDeadline2"></IMG>
					</font></span>
				</td>
				<xsl:if test="$oneCountry !=0">
				  <td bgcolor="#646666" align="center" width="*"><span class="headsmall"><font color="#FFFFFF">Country <IMG name="updown" border="0" src="images/updown.gif" usemap="#sortCountry"></IMG></font></span></td>
				</xsl:if>
				
	  </tr-->


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
			<img src="images/button_role.gif" alt="Log in to CIRCA and see all role details">
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
<!--td>
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
			<img src="images/button_role.gif" alt="Log in to CIRCA and see all the information">
				<xsl:attribute name="onClick">javascript:openCirca('<xsl:value-of select="RESPONSIBLE/ROLE_MEMBERS_URL"/>')</xsl:attribute>
			</img>
			</xsl:otherwise>
			</xsl:choose>
	</xsl:if>

	</font>
</td-->
<!--td width="10">
<img src="images/diamlil.gif" width="8" height="9"/>
</td-->
<!--td><font color="#646666">
		<a>
		<xsl:attribute name="href">javascript:openClient('<xsl:value-of select="T_CLIENT/PK_CLIENT_ID"/>')</xsl:attribute>
		<xsl:value-of select="T_CLIENT/CLIENT_NAME"/>
		</a>
		</font>
</td-->
<!--td>
		<font color="#999999">
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
	</font>
</td-->
<!--td>
	<span class="head0">
		<a>
			<xsl:attribute name="href">show.jsv?id=<xsl:value-of select="T_ACTIVITY/PK_RA_ID"/>&amp;aid=<xsl:value-of select="T_REPORTING/PK_RO_ID"/>&amp;mode=A</xsl:attribute>
			<xsl:value-of select="T_ACTIVITY/TITLE"/>
		</a>
		</span>
</td-->
<!--td align="center">
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
</td-->
		   <!--td width="140" align="center">
				<xsl:if test="T_ACTIVITY/DEADLINE2 != '' ">	
					<xsl:value-of select="T_ACTIVITY/DEADLINE2"/>
				</xsl:if>
			</td-->
	<!--xsl:if test="$oneCountry !=0">
		<td width="*" align="left"><span lang="en-us"><xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/></span></td>
	</xsl:if-->
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
				<!--xsl:attribute name="selected">
					<xsl:if test="/XmlData/RowSet[@Name='Main']/Row/T_RAISSUE_LNK/FK_ISSUE_ID=PK_ISSUE_ID">true</xsl:if>
				</xsl:attribute-->
				<xsl:value-of select="ISSUE_NAME"/>
			</option>
		</xsl:for-each>
	</xsl:template>


</xsl:stylesheet>
