<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.util.*,eionet.rod.services.RODServices,eionet.rod.services.FileServiceIF,eionet.rod.RODUtil,eionet.rod.countrysrv.servlets.Subscribe"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%!final static int OPTION_MAXLEN=80;%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>Subscribe to notifications - ROD</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="title" content="EEA - Reporting Obligations Database" />
	<meta name="description" content="The EEA's reporting obligations database (ROD) contains information describing environmental reporting obligations that countries have towards international organisations." />
	<meta name="keywords" content="reporting obligations, environmental legislation, environmental reporting, environmental dataflows, European Environment Agency, EEA, European, Environmental information, Environmental portal, Eionet, Reportnet, air, waste, water, biodiversity" />
	<meta name="Publisher" content="EEA, The European Environment Agency" />
	<meta name="Rights" content="Copyright EEA Copenhagen 2003" />

    <link rel="stylesheet" type="text/css" href="layout-print.css" media="print" />
    <link rel="stylesheet" type="text/css" href="layout-handheld.css" media="handheld" />
    <link rel="stylesheet" type="text/css" href="layout-screen.css" media="screen" title="EIONET style" />
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	<script type="text/javascript" src="script/util.js"></script>
	<script type="text/javascript">
	//<![CDATA[
    	function showhelp(text) {
			if (text != '')
				alert(text);
			else
				alert('No examples for this unit type!');
		}
		function openActionTypeHistory(MODE,TYPE){
		
			var url = "history.jsv?entity=" + TYPE + "&mode=" + MODE;
			var name = "History";
			var features = "location=no, menubar=no, width=700, height=400, top=100, left=100, scrollbars=yes";
			var w = window.open(url,name,features);
			w.focus();
		
		}
		
		
		/*function openHistory(ID,TYPE){
		
			var url = "history.jsv?entity=" + TYPE + "&id=" + ID;
			var name = "History";
			var features = "location=no, menubar=no, width=640, height=400, top=100, left=200, scrollbars=yes";
			var w = window.open(url,name,features);
			w.focus();
		
		} */
		
		
		/*
		function openClient(ID){
		
			var url = "client.jsv?id=" + ID;
			var name = "Client";
			var features = "location=no, menubar=no, width=650, height=500, top=50, left=50, scrollbars=no, resizable=yes";
			var w = window.open(url,name,features);
			w.focus();
		
		}
		*/
		var picklist = new Array();
		
		function fillPicklist(type,list,text) {
		      var i,js;
			for (i = list.length; i > 0; --i)
				list.options[i] = null;
			list.options[0] = new Option("Choose a group","-1");
			j = 1;
			for (i = 0; i < picklist.length; i++) {
			  s = new String(picklist[i]);
			  pvalue = s.substring(0,s.indexOf(":"));
			  ptext = s.substring(s.indexOf(":")+1,s.lastIndexOf(":"));
			  ptype = s.substring(s.lastIndexOf(":")+1,s.lastIndexOf(":")+2);
			  if (ptype.valueOf() == type) {
			  	list.options[j] = new Option(ptext.valueOf(), pvalue.valueOf()+":"+ptext.valueOf());
			  	j++;
			  }
			} 
			list.options[0].selected = true;
		}
	//]]>
    </script>
</head>
<body>
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="UNS Subscription"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
	<h1>Get notifications in your email</h1>
   	<form name="form" method="post" action="Subscribe">
   	
   		<%
   		
   		Object success = session.getAttribute("SUCCESS");
   		if (success!=null){
	   		session.removeAttribute("SUCCESS");
	   		%>
	   		<p>Subscription successful!</p>
	   		<%	   		
   		}
   		
   		String mySubscriptionsUrl = RODServices.getFileService().getStringProperty(FileServiceIF.UNS_MY_SUBSCRIPTIONS_URL) + Subscribe.CHANNEL_NAME;
   		%>
   		
	   	<table style="border: 1px solid #008080" cellpadding="2" cellspacing="0" width="600" border="0">
				<tr>
					<td colspan="2">
						<strong>Note:</strong> This will make an additional subscription even if you have subscribed before.
						To change or delete your existing subscriptions, go to the <a href="<%=mySubscriptionsUrl%>">Unified Notification Service (UNS)</a>.
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>My interests:</strong>
					</td>
					<td>
						<input type="checkbox" id="deadlines" name="event_type" value="Approaching deadline"/><label for="deadlines">Approaching deadlines</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<input type="checkbox" id="changes" name="event_type" value="Obligation change"/><label for="changes">Changes to obligations</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<input type="checkbox" id="newobl" name="event_type" value="New obligation"/><label for="newobl">New obligations</label>
					</td>
				</tr>

				<tr>
					<td scope="row">
						<strong>Country:</strong>
					</td>
					<td>
						<% 
						Vector countries = RODServices.getDbService().getCountries();
						if (countries!=null && countries.size()>0){
						 %>
						  <select name="country">
						  <option value="">All countries</option>
						  <%
						  for (int i=0; i<countries.size(); i++){
						   Hashtable hash1 = (Hashtable) countries.elementAt(i);
						   String name = (String)hash1.get("name");
						   String uri = (String)hash1.get("uri");
						    %>
						    <option value="<%=RODUtil.replaceTags(name)%>"><%=RODUtil.replaceTags(name)%></option>
						    <%
						   }
						  %>
						 </select>
						 <%
						}
						%>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Issue:</strong>
					</td>
					<td>
						<% 
						Vector issues = RODServices.getDbService().getIssues();
						if (issues!=null && issues.size()>0){
						 %>
						  <select name="issue">
						  <option value="">All issues</option>
						  <%
						  for (int i=0; i<issues.size(); i++){
						   Hashtable hash2 = (Hashtable) issues.elementAt(i);
						   String name2 = (String)hash2.get("name");
						   name2 = RODUtil.threeDots(name2,OPTION_MAXLEN);
						   String id2 = (String)hash2.get("uri");
						    %>
						    <option value="<%=RODUtil.replaceTags(name2)%>"><%=RODUtil.replaceTags(name2)%></option>
						    <%
						   }
						  %>
						 </select>
						 <%
						}
						%>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Organisation:</strong>
					</td>
					<td>
						<% 
						if(request.getParameter("id") != null && request.getParameter("id") != "") { 
							Vector client_vector = RODServices.getDbService().getObligationById(request.getParameter("id"));
							if (client_vector!=null && client_vector.size()>0){
								Hashtable client_hash = (Hashtable) client_vector.elementAt(0);
								String client = (String)client_hash.get("client");
								String client_id = (String)client_hash.get("clientID");
								%>
								<%=client%>
								<input type="hidden" name="organisation" value="<%=RODUtil.replaceTags(client_id)%>"/>
							<%
							}
						} else { 
							Vector organisations = RODServices.getDbService().getOrganisations();
							if (organisations!=null && organisations.size()>0){
							 %>
							  <select name="organisation">
							  <option value="">All organisations</option>
							  <%
							  for (int i=0; i<organisations.size(); i++){
							   Hashtable hash3 = (Hashtable) organisations.elementAt(i);
							   String name3 = (String)hash3.get("name");
							   name3 = RODUtil.threeDots(name3,OPTION_MAXLEN);
							   String id3 = (String)hash3.get("uri");
							    %>
							    <option value="<%=RODUtil.replaceTags(name3)%>"><%=RODUtil.replaceTags(name3)%></option>
							    <%
							   }
							  %>
							 </select>
							 <%
							}
						}
						%>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Obligation:</strong>
					</td>
					<td>
						<% 
						if(request.getParameter("id") != null && request.getParameter("id") != "") {
							Vector title_vector = RODServices.getDbService().getObligationById(request.getParameter("id"));
							if (title_vector!=null && title_vector.size()>0){
									Hashtable title_hash = (Hashtable) title_vector.elementAt(0);
									String title = (String) title_hash.get("title");
									String obligation_id = (String) title_hash.get("obligationID");
								%>
								<%=title%>
								<input type="hidden" name="obligation" value="<%=RODUtil.replaceTags(obligation_id)%>"/>
							<%
							}
						} else {
							Vector obligations = RODServices.getDbService().getObligations();
							if (obligations!=null && obligations.size()>0){
							 %>
							  <select id="obligations" name="obligation">
							  	<option value="">All obligations</option>
							  <%
							  for (int i=0; i<obligations.size(); i++){
							   Hashtable hash4 = (Hashtable) obligations.elementAt(i);
							   String name4 = (String)hash4.get("TITLE");
							   name4 = RODUtil.threeDots(name4,OPTION_MAXLEN);
							   String id4 = (String)hash4.get("uri");
							    %>
							    <option value="<%=RODUtil.replaceTags(name4)%>"><%=RODUtil.replaceTags(name4)%></option>
							    <%
							   }
							  %>
							 </select>
							 <%
							}
						}
						%>
					</td>
					
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input type="submit" name="action" value="Subscribe"/>
					</td>
				</tr>
			</table>
		</form>
</div> <!-- workarea -->
<jsp:include page="footer.jsp" flush="true">
</jsp:include>
</body>
</html>
