<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-definition>
	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@page import="eionet.rod.services.RODServices,eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.RODUtil,java.util.*,eionet.rod.Constants,com.tee.uit.security.*"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<meta name="Publisher" content="EEA, The European Environment Agency" />
			<meta name="Rights" content="Copyright EEA Copenhagen 2003-2008" />
			
			<title>EEA Reporting Obligations Database - ${pageTitle}</title>
			
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<meta name="description" content="The EEA's reporting obligations database (ROD) contains information describing environmental reporting obligations that countries have towards international organisations." />
			<meta name="keywords" content="reporting obligations, environmental legislation, environmental reporting, environmental dataflows, European Environment Agency, EEA, European, Environmental information, Environmental portal, Eionet, Reportnet, air, waste, water, biodiversity" />
			<meta name="Publisher" content="EEA, The European Environment Agency" />
			<meta name="Rights" content="Copyright EEA Copenhagen 2003-2006" />
		
			<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
			<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />		
			<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" title="Eionet 2007 style" />
			<link rel="stylesheet" type="text/css" href="<c:url value="/eionet2007.css"/>" media="screen" title="Eionet 2007 style"/>
			<link rel="shortcut icon" href="<c:url value="/favicon.ico"/>" type="image/x-icon" />
			<script type="text/javascript" src="<c:url value="/script/util.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/script/pageops.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/script/mark_special_links.js"/>"></script>
		</head>
		<body>
			<div id="container">
				<div id="toolribbon">
					<div id="lefttools">
						<a id="eealink" href="http://www.eea.europa.eu/">EEA</a>
						<a id="ewlink" href="http://www.ewindows.eu.org/">EnviroWindows</a>
				    </div>
				    <div id="righttools">
						<c:choose>
                        	<c:when test="${empty actionBean.userName}">    
                        		<a href="${actionBean.loginURL}" id="loginlink" title="Login">Login</a>
					    	</c:when>
	                        <c:otherwise>
								<a href="${pageContext.request.contextPath}/logout_servlet" id="logoutlink" title="Logout">Logout ${actionBean.userName}</a>
	                        </c:otherwise>
	                    </c:choose>
						<a id="printlink" title="Print this page" href="javascript:this.print();"><span>Print</span></a>
				        <a id="fullscreenlink" href="javascript:toggleFullScreenMode()" title="Switch to/from full screen mode"><span>Switch to/from full screen mode</span></a>
				        <a id="acronymlink" href="http://www.eionet.europa.eu/acronyms" title="Look up acronyms"><span>Acronyms</span></a>
				        <form action="http://search.eionet.europa.eu/search.jsp" method="get">
							<div id="freesrchform"><label for="freesrchfld">Search</label>
								<input type="text" id="freesrchfld" name="query"/>
				
								<input id="freesrchbtn" type="image" src="<c:url value="/images/button_go.gif"/>" alt="Go"/>
							</div>
						</form>
				    </div>
				</div> <!-- toolribbon -->
				
				<div id="pagehead">
				    <a href="/"><img src="images/eea-print-logo.gif" alt="Logo" id="logo" /></a>
				    <div id="networktitle">Eionet</div>
				    <div id="sitetitle">Reporting Obligations Database (ROD)</div>
				    <div id="sitetagline">This service is part of Reportnet</div>
				</div> <!-- pagehead -->
				
				<div id="menuribbon">
					<%@ include file="/pages/common/dropdownmenus.txt" %>
				</div>
				
				<div class="breadcrumbtrail">
					<div class="breadcrumbhead">You are here:</div>
					<div class="breadcrumbitem eionetaccronym">
						<a href="http://www.eionet.europa.eu">Eionet</a>
					</div>
					<c:choose>
                    	<c:when test="${empty pageTitle}">
                    		<div class="breadcrumbitemlast">ROD</div>
						</c:when>
						<c:when test="${(!empty bread2) && (!empty bread3)}">
                    		<div class="breadcrumbitem"><a href="${pageContext.request.contextPath}">ROD</a></div>
                    		<div class="breadcrumbitem"><a href="${pageContext.request.contextPath}/${bread2Url}">${bread2}</a></div>
                    		<div class="breadcrumbitemlast">${rodfn:threeDots(bread3,25)}</div>
						</c:when>
						<c:otherwise>
							<div class="breadcrumbitem"><a href="${pageContext.request.contextPath}">ROD</a></div>
 							<div class="breadcrumbitemlast">${fn:escapeXml(pageTitle)}</div>
						</c:otherwise>
					</c:choose>
					<div class="breadcrumbtail"></div>
				</div>
				
				<div id="leftcolumn" class="localnav">
					<ul>
						<li><a href="${pageContext.request.contextPath}/index.html" title="ROD Home">Home </a></li>
						<li><a href="${pageContext.request.contextPath}/countrydeadlines" title="Country deadlines">Country deadlines </a></li>
						<li><a href="${pageContext.request.contextPath}/rorabrowse.jsv?mode=A" title="Reporting Obligations">Obligations </a></li>
						<li><a href="${pageContext.request.contextPath}/subscribe.jsp" title="Create a UNS Subscription">Subscribe </a></li>
						<li><a href="${pageContext.request.contextPath}/text.jsv?mode=H" title="General Help">Help </a></li>
						<c:if test="${!empty actionBean.userName}">
							<li><a href="${pageContext.request.contextPath}/versions">Global History </a></li>
						</c:if>
						<li><a href="${pageContext.request.contextPath}/show.jsv?id=1&amp;mode=C" title="Navigate to reporting obligations via the Eur-lex legislative instrument categories">Legal instruments </a></li>
						<li><a href="${pageContext.request.contextPath}/rorabrowse.jsv?mode=A&amp;anmode=P" title="Eionet Priority Data flows">Priority dataflows </a></li>
						<li><a href="${pageContext.request.contextPath}/analysis" title="Database statistics">Database statistics </a></li>
						<li><a href="${pageContext.request.contextPath}/search" title="Advanced search">Advanced search </a></li>
						<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName, '/Admin/Harvest', 'u')}">
							<li><a href="${pageContext.request.contextPath}/harvester.jsp">Harvest </a></li>
						</c:if>
					</ul>
				</div>

				<div id="workarea">
					<stripes:layout-component name="errors">
						<stripes:errors/>
					</stripes:layout-component>
					
					<stripes:layout-component name="messages">
						<c:choose>
							<c:when test="${actionBean.context.severity == 1}">
								<div class="system-msg">
									<stripes:messages/>
								</div>
							</c:when>
							<c:when test="${actionBean.context.severity == 2}">
								<div class="caution-msg">
									<strong>Warning ...</strong>		
									<stripes:messages/>
								</div>
							</c:when>
							<c:when test="${actionBean.context.severity == 3}">
								<div class="warning-msg">
									<strong>Errors ...</strong>		
									<stripes:messages/>
								</div>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</stripes:layout-component>
					
					<stripes:layout-component name="contents"/>
				</div>
				<div id="pagefoot">
					<a href="text.jsv?mode=D">Disclaimer</a>
					| Last updated: 
					<a href="analysis.jsv">${actionBean.lastUpdate}</a>
					|	<a href="mailto:helpdesk@eionet.europa.eu?subject=Feedback%20from%20the%20ROD%20website">Feedback </a>
					<br/>
					<b><a href="http://www.eea.europa.eu">European Environment Agency</a></b>
					<br/>
					Kgs. Nytorv 6, DK-1050 Copenhagen K, Denmark - Phone: +45 3336
					7100
				</div>
			</div>
		</body>
	</html>
</stripes:layout-definition>