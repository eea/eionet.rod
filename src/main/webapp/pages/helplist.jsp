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
		
		<title>Help</title>
	</head>
	<body class="popup">
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
			<c:forEach items="${actionBean.helpList}" var="help" varStatus="loop">
				<h3>
					<c:choose>
		    			<c:when test="${!empty help.helpTitle}">
							${rodfn:replaceTags(help.helpTitle)}
		    			</c:when>
		    			<c:otherwise>
		    				No Title
		    			</c:otherwise>
		    		</c:choose>
				</h3>
				${rodfn:replaceTags(help.helpText)}
			</c:forEach>
		</div>
	</body>
</html>
