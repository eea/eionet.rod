<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-definition>
	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@page import="eionet.rod.services.RODServices,eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.RODUtil,java.util.*,eionet.rod.Constants,com.tee.uit.security.*"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
		<head>
			<title>
				<c:choose>
		        	<c:when test="${empty pageTitle}">
		        		EEA Reporting Obligations Database
					</c:when>
					<c:otherwise>
						${pageTitle}
					</c:otherwise>
				</c:choose>
			</title>
			
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<c:choose>
	        	<c:when test="${empty desc}">
	        		<c:set var="description" value="The EEA's reporting obligations database (ROD) contains information describing environmental reporting obligations that countries have towards international organisations."></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="description" value="${desc}"></c:set>
				</c:otherwise>
			</c:choose>
			<meta name="description" content="${description}"/>
			<meta name="publisher" content="EEA, The European Environment Agency" />
			
			<!--[if IE]>
			<style type="text/css" media="screen">
				body {
					behavior: url(<c:url value="/css/csshover.htc"/>);
				}
			</style>
			<![endif]-->
			<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/print.css" media="print" />
			<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/handheld.css" media="handheld" />		
			<link rel="stylesheet" type="text/css" href="http://www.eionet.europa.eu/styles/eionet2007/screen.css" media="screen" />
			<link rel="stylesheet" type="text/css" href="<c:url value="/css/eionet2007.css"/>" media="screen" />
			<link rel="stylesheet" type="text/css" href="<c:url value="/css/print.css"/>" media="print" />
			<link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon" />
			<script type="text/javascript" src="<c:url value="/script/util.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/script/pageops.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/script/mark_special_links.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/script/editor.js"/>"></script>
			<script type="text/javascript">
//<![CDATA[
function addsite() {
 f = document.getElementById('freesrchfld');
 f.value = f.value + ' site:rod.eionet.europa.eu';
 return true;
}
//]]>
      </script>
			<stripes:layout-component name="head"/>
		</head>
		<c:choose>
        	<c:when test="${!empty colClass}">
        		<body class="${colClass}">
			</c:when>
			<c:when test="${!empty unLoad}">
        		<body onunload="checkStatus()">
			</c:when>
			<c:otherwise>
				<body>
			</c:otherwise>
		</c:choose>
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
								<a href="${actionBean.logoutURL}" id="logoutlink" title="Logout">Logout ${actionBean.userName}</a>
	                        </c:otherwise>
	                    </c:choose>
	                    <c:if test="${!empty help}">
	                    	<a id="pagehelplink" title="Get help on this page" href="javascript:openViewHelp2('${pageContext.request.contextPath}','${help}')"><span>Page help</span></a>
	                    </c:if>
						<a id="printlink" title="Print this page" href="javascript:this.print();"><span>Print</span></a>
				        <a id="fullscreenlink" href="javascript:toggleFullScreenMode()" title="Switch to/from full screen mode"><span>Switch to/from full screen mode</span></a>
				        <a id="acronymlink" href="http://www.eionet.europa.eu/acronyms" title="Look up acronyms"><span>Acronyms</span></a>
				        <form action="http://google.com/search" method="get" onsubmit="return addsite()">
							<div id="freesrchform"><label for="freesrchfld">Search</label>
								<input type="text" id="freesrchfld" name="q" onfocus="if(this.value=='Search ROD')this.value='';" onblur="if(this.value=='')this.value='Search ROD';" value="Search ROD" size="10"/>
								<input id="freesrchbtn" type="image" src="<c:url value="/images/button_go.gif"/>" alt="Go"/>
							</div>
						</form>
				    </div>
				</div> <!-- toolribbon -->
				
				<div id="pagehead">
				    <a href="/"><img src="${pageContext.request.contextPath}/images/eea-print-logo.gif" alt="Logo" id="logo" /></a>
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
                    		<div class="breadcrumbitem"><a href="${pageContext.request.contextPath}/">ROD</a></div>
                    		<div class="breadcrumbitem"><a href="${pageContext.request.contextPath}/${bread2Url}">${bread2}</a></div>
                    		<div class="breadcrumbitemlast">${rodfn:threeDots(bread3,25)}</div>
						</c:when>
						<c:otherwise>
							<div class="breadcrumbitem"><a href="${pageContext.request.contextPath}/">ROD</a></div>
 							<div class="breadcrumbitemlast">${fn:escapeXml(pageTitle)}</div>
						</c:otherwise>
					</c:choose>
					<div class="breadcrumbtail"></div>
				</div>
				
				<div id="leftcolumn" class="localnav">
					<ul>
						<li><a href="${pageContext.request.contextPath}/index.html" title="ROD Home">Home</a></li>
						<li><a href="${pageContext.request.contextPath}/countrydeadlines" title="Country deadlines">Country deadlines</a></li>
						<li><a href="${pageContext.request.contextPath}/obligations" title="Reporting Obligations">Obligations</a></li>
						<li><a href="${pageContext.request.contextPath}/clients" title="Clients">Clients</a></li>
						<li><a href="${pageContext.request.contextPath}/subscribe" title="Create a UNS Subscription">Subscribe</a></li>
						<li><a href="${pageContext.request.contextPath}/documentation/Help" title="General Help">Help</a></li>
						<c:if test="${!empty actionBean.userName}">
							<li><a href="${pageContext.request.contextPath}/versions">Global History</a></li>
						</c:if>
						<li><a href="${pageContext.request.contextPath}/instruments" title="Navigate to reporting obligations via the Eur-lex legislative instrument categories">Legal instruments</a></li>
						<li><a href="${pageContext.request.contextPath}/obligations?anmode=P" title="Eionet Priority Data flows">Priority dataflows</a></li>
						<li><a href="${pageContext.request.contextPath}/analysis" title="Database statistics">Database statistics</a></li>
						<li><a href="${pageContext.request.contextPath}/search" title="Advanced search">Advanced search</a></li>
						<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName, '/Admin/Harvest', 'u')}">
							<li><a href="${pageContext.request.contextPath}/harvester">Harvest</a></li>
						</c:if>
					</ul>
				</div>
				
				<c:if test="${colClass == 'threecolumn'}">
					<div id="rightcolumn" class="quickjumps">
						<stripes:layout-component name="rightColumn"/>
					</div>
				</c:if>

				<div id="workarea">
				
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
							<c:when test="${actionBean.context.severity == 4}">
								<div class="error-msg">Please correct the flagged fields before continuing
									<ul>
										<stripes:messages/>
									</ul>
								</div>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</stripes:layout-component>
					
					<stripes:layout-component name="contents"/>
				</div>
				<div id="pagefoot">
					<a href="${pageContext.request.contextPath}/documentation/Disclaimer">Disclaimer</a>
					| Copyright: <a rel="license"
					   href="http://creativecommons.org/licenses/by/2.5/dk/deed.en_GB"
					   title="Creative Commons Attribution License">
					  <img alt="Creative Commons Attribution License"
					      src="http://i.creativecommons.org/l/by/2.5/dk/80x15.png" /></a>
					| Last updated: 
					<a href="${pageContext.request.contextPath}/analysis.jsv">${actionBean.lastUpdate}</a>
					|	<a href="mailto:helpdesk@eionet.europa.eu?subject=Feedback%20from%20the%20ROD%20website">Feedback</a>
					<br/>
					<a href="http://www.eea.europa.eu">European Environment Agency</a>
					<br/>
					Kgs. Nytorv 6, DK-1050 Copenhagen K, Denmark - Phone: +45 3336 7100
				</div>
			</div>
		</body>
	</html>
</stripes:layout-definition>
