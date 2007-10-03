<%@page import="eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.RODUtil,java.util.*"%>
<%
String appName = getServletContext().getInitParameter(Attrs.APPPARAM);
ROUser rouser = (ROUser) session.getAttribute(Attrs.USERPREFIX + appName);
%>
<div id="toolribbon">
	<div id="lefttools">
      <a id="eealink" href="http://www.eea.europa.eu/">EEA</a>
      <a id="ewlink" href="http://www.ewindows.eu.org/">EnviroWindows</a>
    </div>
    <div id="righttools">    
		<%  
			if (rouser!=null){
			%>
			<a id="logoutlink" href="logout_servlet" title="Logout">Logout <%=rouser.getUserName()%></a><%
		}
		else{ %>
			<a id="loginlink" href="login.jsp" title="Login">Login</a><%
		}%>
		<a id="printlink" title="Print this page" href="javascript:this.print();"><span>Print</span></a>
        <a id="fullscreenlink" href="javascript:toggleFullScreenMode()" title="Switch to/from full screen mode"><span>Switch to/from full screen mode</span></a>
        <a id="acronymlink" href="http://www.eionet.europa.eu/acronyms" title="Look up acronyms"><span>Acronyms</span></a>
        <form action="http://search.eionet.europa.eu/search.jsp" method="get">
			<div id="freesrchform"><label for="freesrchfld">Search</label>
				<input type="text" id="freesrchfld" name="query"/>

				<input id="freesrchbtn" type="image" src="images/button_go.gif" alt="Go"/>
			</div>
		</form>
    </div>
</div> <!-- toolribbon -->

<div id="pagehead">
    <a href="/"><img src="images/eealogo.gif" alt="Logo" id="logo" /></a>
    <div id="networktitle">Eionet</div>
    <div id="sitetitle">Reporting Obligations Database (ROD)</div>
    <div id="sitetagline">This service is part of Reportnet</div>
</div> <!-- pagehead -->


<div id="menuribbon">
	<%@ include file="dropdownmenus.txt" %>
</div>
<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem eionetaccronym"><a href="http://www.eionet.europa.eu">Eionet</a></div>

<%
   String oHName=request.getParameter("name");
   if (oHName==null) {  %>
 <div class="breadcrumbitemlast">ROD</div>
<% } %>
<%  if (oHName!=null) { %>
 <div class="breadcrumbitem"><a href='index.html'>ROD</a></div>
 <div class="breadcrumbitemlast"><%=oHName%></div>
<% } %>
 <div class="breadcrumbtail"></div>
</div>
