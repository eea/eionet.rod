<%@page import="eionet.rod.services.RODServices"%>
<%
String last_update = RODServices.getDbService().getGenericlDao().getLastUpdate();
%>
<div id="pagefoot">
	<a href="documentation/Disclaimer">Disclaimer</a>
	| Last updated: 
	<a href="analysis.jsv"><%=last_update%></a>
	|	<a href="mailto:helpdesk@eionet.europa.eu?subject=Feedback%20from%20the%20ROD%20website">Feedback </a>
	<br/>
	<b><a href="http://www.eea.europa.eu">European Environment Agency</a></b>
	<br/>
	Kgs. Nytorv 6, DK-1050 Copenhagen K, Denmark - Phone: +45 3336
	7100
</div>
