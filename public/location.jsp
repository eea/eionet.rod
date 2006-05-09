<%@page import="java.util.*"%>
<div id="pagehead">
 <div id="identification">
  <a href="/"><img src="images/logo.png" alt="Logo" id="logo" border="0" /></a>
  <div class="sitetitle">Reporting Obligations Database (ROD)</div>
  <div class="sitetagline">This service is part of Reportnet </div>
 </div>

<div class="breadcrumbtrail">
 <div class="breadcrumbhead">You are here:</div>
 <div class="breadcrumbitem"><a href="http://www.eionet.europa.eu">EIONET</a></div>

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
</div> <!-- pagehead -->
