<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.util.*,eionet.rod.services.RODServices,eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.services.FileServiceIF,eionet.rod.EionetCASFilter,eionet.rod.countrysrv.servlets.Subscribe"%>
<%
	String app = getServletContext().getInitParameter(Attrs.APPPARAM);
	ROUser user = (ROUser) session.getAttribute(Attrs.USERPREFIX + app);
	if (user == null){
		response.sendRedirect(EionetCASFilter.getCASLoginURL(request,true));
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%!final static int OPTION_MAXLEN=80;%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<%@ include file="headerinfo.txt" %>
  <title>Subscribe to notifications - ROD</title>
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
		
			var url = "clients/" + ID;
			var name = "Client";
			var features = "location=no, menubar=no, width=650, height=500, top=50, left=50, scrollbars=no, resizable=yes";
			var w = window.open(url,name,features);
			w.focus();
		
		}
		*/
	//]]>
    </script>
</head>
<body>
<div id="container">
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="UNS Subscription"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
	<h1>Get notifications in your email</h1>
   	<form id="form" method="post" action="Subscribe">
   	
   		<%
   		
   		Object success = session.getAttribute("SUCCESS");
   		if (success!=null){
	   		session.removeAttribute("SUCCESS");
	   		%>
	   		<p>Subscription successful!</p>
	   		<%	   		
   		}
   		
   		String mySubscriptionsUrl = RODServices.getFileService().getStringProperty(FileServiceIF.UNS_MY_SUBSCRIPTIONS_URL) + RODServices.getFileService().getStringProperty(FileServiceIF.UNS_CHANNEL_NAME);
   		%>
   		
			<div class="note-msg">
				<strong>Note</strong>
				<p>This will make an additional subscription even if you have subscribed before.
						To change or delete your existing subscriptions,
						go to the <a href="<%=mySubscriptionsUrl%>">Unified Notification Service (UNS)</a>.</p>
			</div>
	   	<table class="formtable" style="border: 1px solid #008080" cellpadding="2" cellspacing="0" width="600" border="0">
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
					<td>
						&nbsp;
					</td>
					<td>
						<input type="checkbox" id="changes_inst" name="event_type" value="Instrument change"/><label for="changes">Changes to instruments</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<input type="checkbox" id="newinst" name="event_type" value="New instrument"/><label for="newobl">New instrument</label>
					</td>
				</tr>

				<tr>
					<td scope="row">
						<strong>Country:</strong>
					</td>
					<td>
						<% 
						Vector countries = RODServices.getDbService().getSpatialDao().getCountries();
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
						    <option><%=name%></option>
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
						Vector issues = RODServices.getDbService().getIssueDao().getIssues();
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
						    <option><%=name2%></option>
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
							Vector client_vector = RODServices.getDbService().getObligationDao().getObligationById(Integer.valueOf(request.getParameter("id")));
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
							Vector organisations = RODServices.getDbService().getClientDao().getOrganisations();
							if (organisations!=null && organisations.size()>0){
							 %>
							  <select name="organisation">
							  <option value="">All organisations</option>
							  <%
							  for (int i=0; i<organisations.size(); i++){
							   Hashtable hash3 = (Hashtable) organisations.elementAt(i);
							   String name3 = (String)hash3.get("name");
							   String name3_short = RODUtil.threeDots(name3,OPTION_MAXLEN);
							   name3 = RODUtil.replaceTags(name3);
							   name3_short = RODUtil.replaceTags(name3_short);
							   String id3 = (String)hash3.get("uri");
							    %>
							    <option value="<%=name3%>"><%=name3_short%></option>
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
							Vector title_vector = RODServices.getDbService().getObligationDao().getObligationById(Integer.valueOf(request.getParameter("id")));
							if (title_vector!=null && title_vector.size()>0){
									Hashtable title_hash = (Hashtable) title_vector.elementAt(0);
									String title = (String) title_hash.get("title");
									String obligation_id = (String) title_hash.get("obligationID");
								%>
								<%=title%>
								<input type="hidden" name="obligation" value="<%=RODUtil.replaceTags(title)%>"/>
							<%
							}
						} else {
							Vector obligations = RODServices.getDbService().getObligationDao().getObligations();
							if (obligations!=null && obligations.size()>0){
							 %>
							  <select id="obligations" name="obligation">
							  	<option value="">All obligations</option>
							  <%
							  for (int i=0; i<obligations.size(); i++){
							   Hashtable hash4 = (Hashtable) obligations.elementAt(i);
							   String name4 = (String)hash4.get("TITLE");
							   String name4_short = RODUtil.threeDots(name4,OPTION_MAXLEN);
							   name4_short = RODUtil.replaceTags(name4_short);
							   name4 = RODUtil.replaceTags(name4);
							    %>
							    <option value="<%=name4%>"><%=name4_short%></option>
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
						<strong>Instrument:</strong>
					</td>
					<td>
						<% 
							if(request.getParameter("sid") != null && request.getParameter("sid") != "") {
							Vector title_vector = RODServices.getDbService().getSourceDao().getInstrumentById(Integer.valueOf(request.getParameter("sid")));
							if (title_vector!=null && title_vector.size()>0){
									Hashtable title_hash = (Hashtable) title_vector.elementAt(0);
									String title = (String) title_hash.get("TITLE");
									String instrument_id = (String) title_hash.get("instrumentID");
								%>
								<%=title%>
								<input type="hidden" name="instrument" value="<%=RODUtil.replaceTags(title)%>"/>
							<%
							}
							} else {
								Vector instruments = RODServices.getDbService().getSourceDao().getInstruments();
								if (instruments!=null && instruments.size()>0){
								 %>
								  <select id="instruments" name="instrument">
								  	<option value="">All instruments</option>
								  <%
								  for (int i=0; i<instruments.size(); i++){
								   Hashtable hash = (Hashtable) instruments.elementAt(i);
								   String name = (String)hash.get("TITLE");
								   name = RODUtil.threeDots(name,OPTION_MAXLEN);
							   	   name = RODUtil.replaceTags(name);
								    %>
								    <option><%=name%></option>
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
</div>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
