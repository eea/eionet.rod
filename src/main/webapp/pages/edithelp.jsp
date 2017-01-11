<%@page contentType="text/html;charset=UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp"%>	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
		<meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
		
		<link rel="stylesheet" type="text/css" href="https://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
		<link rel="stylesheet" type="text/css" href="https://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />		
		<link rel="stylesheet" type="text/css" href="https://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" title="Eionet 2007 style" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/eionet2007.css"/>" media="screen" title="Eionet 2007 style"/>
		
		<link rel="shortcut icon" href="<c:url value="/favicon.ico"/>" type="image/x-icon"/>
		<script type="text/javascript" src="<c:url value="/script.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/script/pageops.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/script/mark_special_links.js"/>"></script>
        <script type="text/javascript">
			// Sets focus on form's first element
			function setFocus(formName) {
				document.getElementById("title").focus();
			}
		</script>
		
		<title>Edit Help Text</title>
	</head>
	<body class="popup" onload="setFocus('helpForm')">
		<div id="pagehead">
		    <a href="/"><img src="<c:url value="/images/eea-print-logo.gif"/>" alt="Logo" id="logo" /></a>
		    <div id="networktitle">Eionet</div>
		    <div id="sitetitle">Reporting Obligations Database (ROD)</div>
		    <div id="sitetagline">This service is part of Reportnet</div>    
		</div> <!-- pagehead -->
		<div id="operations" style="margin-top:10px">
			<ul>
				<li><a href="javascript:window.close();">Close</a></li>
			</ul>
		</div>
		<div id="workarea" style="clear:right">
			<h3>Edit Help Text</h3>
			<stripes:form action="/help" method="post" name="helpForm" id="helpForm">
				<stripes:hidden name="helpId"/>
				<stripes:hidden name="help.helpId"/>
				<table width="100%">
					<tr>
						<td align="left">
							<b>Title:&#160;</b>
						</td>
						<td align="left">
							<stripes:text name="help.helpTitle" size="70" id="title"/>
						</td>
					</tr>
					<tr>
						<td align="left" colspan="2">
							<br/><b>Help text:</b><br/>
						</td>
					</tr>
					<tr>
						<td align="left" colspan="2">
							<stripes:textarea name="help.helpText" cols="70" rows="18" id="text"/>
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td colspan="2">&#160;</td>
					</tr>
					<tr align="right">
						<td colspan="2" align="right">
							<stripes:submit name="edit" value="Save"/>
							<stripes:button name="cancel" onclick="location.href='${pageContext.request.contextPath}/help/${actionBean.helpId}'" value="Cancel"/>  
						</td>
					</tr>
				</table>
			</stripes:form>
		</div>
	</body>
</html>
