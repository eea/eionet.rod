<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:variable name="pagetitle">
	Status of participation
</xsl:variable>
<xsl:include href="common.xsl"/>

<xsl:template match="/">

<html lang="en"><head><title>Status of participation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="eionet.css"/>
	<script type="text/javascript" src="script/util.js"></script>
	<script type="text/javascript">
		<![CDATA[
			function loadPage(){
				if(document.f2){
					document.f2.from.focus();
				}
			}
		]]>
	</script>
	</head>
	<body onLoad="loadPage()" marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#f0f0f0">

	<xsl:variable name="permissions">
		<xsl:value-of select="/XmlData/RowSet/@permissions"/>
	</xsl:variable>
	
	<xsl:variable name="ra-id">
		<xsl:value-of select="/XmlData/RowSet/@ID"/>
	</xsl:variable>
	
	<xsl:variable name="spatialID">
		<xsl:value-of select="/XmlData/RowSet/@spatialID"/>
	</xsl:variable>
	
	<xsl:variable name="spatialHistoryID">
		<xsl:value-of select="/XmlData/RowSet/@spatialHistoryID"/>
	</xsl:variable>

	<div id="workarea">
<br/>
			<table width="700" border="0">
				<tr>
					<td width="20">&#160;</td>
					<td width="560">
							<font color="#006666" face="Arial"><strong><span class="head2">
								<xsl:call-template name="FirstHeading"/>
							</span></strong></font>
							<br/>
							<font color="#006666" face="Arial" size="2">
							<strong><span class="head0">
								<xsl:call-template name="SecondHeading"/>
							</span></strong>
						</font>
					</td>
					<td width="70"><img src="images/logo.jpg" id="logo" alt="" height="62" width="66" border="0"/></td>
				</tr>
			</table>

	<!-- green line table -->
	<table cellspacing="0" cellpadding="0" width="700" border="0">
		<tr><td align="bottom" width="700" background="images/bar_filled.jpg" height="25">&#160;</td></tr>
		<tr height="25"><td></td></tr>
	</table>


<table width="680" border="0">
	<tr>
		<td colspan="3" align="right" valign="top">
			<xsl:call-template name="HelpOverview"><xsl:with-param name="id">HELP_SPATIALHISTORY</xsl:with-param><xsl:with-param name="perm"><xsl:value-of select="$permissions"/></xsl:with-param></xsl:call-template>
		</td>
	</tr>
<tr>
	<td width="3%"></td>
	<td width="35%" valign="top" align="right"><span class="head1">Status of participation:</span></td>
	<td align="62%"></td>
</tr>
<tr>
	<td></td>
	<td align="right"><span class="head0">Reporting obligation:</span></td><td><xsl:value-of select="/XmlData/RowSet/Row/T_OBLIGATION/TITLE"/></td>
</tr>
<tr valign="top">
	<td></td>
	<td align="right"><span class="head0">Client organisation: </span></td><td><xsl:value-of select="/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME"/></td>
</tr>
</table>
<br/>
<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
	<table width="680" cellspacing="0" border="0">
		<tr>
			<td style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" 
							valign="center" bgcolor="#ffffff" align="left">
				<span class="headsmall"><font title="Edit period" face="Verdana" color="#000000" size="1">Edit period</font></span>
			</td>
		</tr>
		<form name="f1" method="POST" action="editperiod" acceptcharset="UTF-8">
			<input type="hidden">
				<xsl:attribute name="name">ra_id</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="$ra-id"/></xsl:attribute>
			</input>
			<tr valign="top" bgcolor="#cbdcdc">
				<td style="border-left: #c0c0c0 1px solid; border-right: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid" valign="middle"><span class="Mainfont">
					<font face="Verdana" size="2">
						From
						<input size="7" type="text">
							<xsl:attribute name="name">from</xsl:attribute>
							<xsl:attribute name="value"></xsl:attribute>
						</input>
						to
						<input size="7" type="text">
							<xsl:attribute name="name">to</xsl:attribute>
							<xsl:attribute name="value"></xsl:attribute>
						</input>
						&#160;
						<input type="submit" value="Save" style="font-weight: normal; color: #000000; background-image: url('images/bgr_form_buttons.jpg')"></input>
					</font></span>
				</td>
			</tr>
		</form>
	</table>
	<br/>
</xsl:if>
<table width="680" cellspacing="0" border="0">
	<tr>
		<td width="25%"  style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid" 
						valign="center" bgcolor="#ffffff" align="left">
					<span class="headsmall"><font title="Reporting country" face="Verdana" color="#000000" size="1">Country</font></span></td>
		<td width="25%"  style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid" 
						valign="center" bgcolor="#ffffff" align="left">
				<span class="headsmall"><font title="Status" face="Verdana" color="#000000" size="1">Status</font></span></td>
		<td width="40%"  style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid"
						valign="center" bgcolor="#ffffff" align="left">
				<span class="head0"><font title="Period when the country participated in reporting" face="Verdana" color="#000000" size="1">Participation period</font></span></td>
		<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
			<td width="10%"  style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid;"
							valign="center" bgcolor="#ffffff" align="left">
					<span class="head0"><font title="Edit period" face="Verdana" color="#000000" size="1">Edit period</font></span></td>
		</xsl:if>
	</tr>
<xsl:for-each select="XmlData/RowSet/Row">
<tr valign="top">
		<xsl:attribute name="bgcolor">
				<xsl:if test="position() mod 2 = 0">#cbdcdc</xsl:if>
		</xsl:attribute>
<td style="border-left: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid" 
       valign="top"><span class="Mainfont">
			 <font face="Verdana" size="2">
	<xsl:value-of select="T_SPATIAL/SPATIAL_NAME"/>
		</font></span>
</td>
<td style="border-left: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid" 
       valign="top"><span class="Mainfont">
			 <font face="Verdana" size="2">
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='Y'">
			Voluntary reporting
		</xsl:when>
		<xsl:when test="T_SPATIAL_HISTORY/VOLUNTARY='N'">
			Formal reporting
		</xsl:when>
	</xsl:choose>
		</font></span>
</td>
<td style="border-left: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid; border-right: #c0c0c0 1px solid" 
       valign="top"><span class="Mainfont">
			 <font face="Verdana" size="2">
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/START_DATE='' or T_SPATIAL_HISTORY/START_DATE='00/00/0000'">
			Prior to start of ROD (2003)
		</xsl:when>
		<xsl:otherwise>
			From <xsl:value-of select="T_SPATIAL_HISTORY/START_DATE"/>
		</xsl:otherwise>
	</xsl:choose>
	to 
	<xsl:choose>
		<xsl:when test="T_SPATIAL_HISTORY/END_DATE != ''">
			<xsl:value-of select="T_SPATIAL_HISTORY/END_DATE"/>
		</xsl:when>
		<xsl:otherwise>
				present
		</xsl:otherwise>
	</xsl:choose>
		</font></span>
</td>
<xsl:if test="contains($permissions, ',/obligations:u,')='true'">
	<td style="border-right: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid" valign="top"><span class="Mainfont">
		 <font face="Verdana" size="2">
			<a><xsl:attribute name="href">javascript:openPopup('spatialhistory.jsv', 'ID=<xsl:value-of select="$ra-id"/>&amp;spatialID=<xsl:value-of select="T_SPATIAL/PK_SPATIAL_ID"/>&amp;spatialHistoryID=<xsl:value-of select="T_SPATIAL_HISTORY/PK_SPATIAL_HISTORY_ID"/>')</xsl:attribute>
				edit
			</a>
		 </font></span>
	</td>
</xsl:if>
</tr>
<xsl:if test="$spatialID = T_SPATIAL/PK_SPATIAL_ID">
	<xsl:if test="$spatialHistoryID = T_SPATIAL_HISTORY/PK_SPATIAL_HISTORY_ID">
	
		<form name="f2" method="POST" action="editperiod" acceptcharset="UTF-8">
			<input type="hidden">
				<xsl:attribute name="name">spatialHistoryID</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="T_SPATIAL_HISTORY/PK_SPATIAL_HISTORY_ID"/></xsl:attribute>
			</input>
			<tr valign="top" bgcolor="#FFCCCC">
				<td colspan="2" style="border-left: #c0c0c0 1px solid; border-right: #c0c0c0 0px solid; border-bottom: #c0c0c0 1px solid" valign="top"><span class="Mainfont">
				&#160;
				</span>
				</td>
				<td style="border-left: #c0c0c0 0px solid; border-right: #c0c0c0 0px solid; border-bottom: #c0c0c0 1px solid" valign="middle"><span class="Mainfont">
					<font face="Verdana" size="2">
						From
						<input size="7" type="text">
							<xsl:attribute name="name">from</xsl:attribute>
								<xsl:choose>
									<xsl:when test="T_SPATIAL_HISTORY/START_DATE='' or T_SPATIAL_HISTORY/START_DATE='00/00/0000'">
										<xsl:attribute name="value">Prior to start of ROD (2003)</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="value"><xsl:value-of select="T_SPATIAL_HISTORY/START_DATE"/></xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
						</input>
						to
						<input size="7" type="text">
							<xsl:attribute name="name">to</xsl:attribute>
								<xsl:choose>
									<xsl:when test="T_SPATIAL_HISTORY/END_DATE != ''">
										<xsl:attribute name="value"><xsl:value-of select="T_SPATIAL_HISTORY/END_DATE"/></xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="value">present</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
						</input>
					</font></span>
				</td>
				<td style="border-left: #c0c0c0 0px solid; border-right: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid" valign="top"><span class="Mainfont">
					<input type="submit" value="Save" style="font-weight: normal; color: #000000; background-image: url('images/bgr_form_buttons.jpg')"></input>
					</span>
				</td>
			</tr>
		</form>
	</xsl:if>
</xsl:if>
</xsl:for-each>
</table>
</div>
</body>
</html>
</xsl:template>
</xsl:stylesheet>