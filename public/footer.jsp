<%@page import="eionet.rod.services.RODServices"%>
<script src="script/util.js" type="text/javascript"></script>
<%
String last_update = RODServices.getDbService().getLastUpdate();
%>
<div id="pagefoot">
	<a href="javascript:history.back()">Back</a>
	| <a href="text.jsv?mode=D">Disclaimer</a>
	| Last updated: 
	<a href="analysis.jsv"><%=last_update%></a>
	|	<a target="_blank" href="mailto:helpdesk@eionet.eu.int?subject=Feedback%20from%20the%20ROD%20website">Feedback </a>
	<br>
	<b><a target="_blank" href="http://www.eea.eu.int">European Environment Agency</a></b>
	<br>
	Kgs. Nytorv 6, DK-1050 Copenhagen K, Denmark - Phone: +45 3336
	7100
</div>