<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.util.*,java.text.*,eionet.rod.ROUser,eionet.rod.services.RODServices,eionet.rod.RODUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%!final static int OPTION_MAXLEN=80;%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>Previous Versions - ROD</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="title" content="EEA - Reporting Obligations Database" />
	<meta name="description" content="The EEA's reporting obligations database (ROD) contains information describing environmental reporting obligations that countries have towards international organisations." />
	<meta name="keywords" content="reporting obligations, environmental legislation, environmental reporting, environmental dataflows, European Environment Agency, EEA, European, Environmental information, Environmental portal, Eionet, Reportnet, air, waste, water, biodiversity" />
	<meta name="Publisher" content="EEA, The European Environment Agency" />
	<meta name="Rights" content="Copyright EEA Copenhagen 2003" />

    <link rel="stylesheet" type="text/css" href="layout-print.css" media="print" />
    <link rel="stylesheet" type="text/css" href="layout-screen.css" media="screen" title="EIONET style" />
    <link rel="stylesheet" type="text/css" href="layout-handheld.css" media="handheld" />
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
		function undoAlert(vec,url) {
			var question = "Obligations with IDs " + vec + " cannot be restored, because their IDs are not free anymore! Do you want to continue?";
		    var answer = confirm(question);
			    if(answer){
					window.location = url;  
			    }
		    }
	//]]>
    </script>
</head>
<body>
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="Previous Actions"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
		<%
		if (rouser!=null){
		%>
		
		<h1>Undo information</h1>
		
		<%
			String ts = request.getParameter("ts");
			String id = request.getParameter("id");
			String tab = request.getParameter("tab");
			String op = request.getParameter("op");
			String user = request.getParameter("user");
		%>						
		<table class="datatable" width="50%">
			<thead>
				<tr>
					<th scope="col">Time</th>
					<th scope="col">Operation</th>
					<th scope="col">User</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<%
							DateFormat df = new SimpleDateFormat ("yyyy-MM-dd' 'HH:mm:ss");
							Date date = new Date(new Long(ts).longValue());
							String d = df.format(date);
						%>
						<%=d%>
					</td>
					<td>
						<%if(op.equals("D")){%>
							DELETE
						<%} else if(op.equals("I")){%>
							INSERT
						<%} else if(op.equals("U")){%>
							UPDATE
						<%} else if(op.equals("UN") || op.equals("UD") || op.equals("UDD")){%>
							UNDO
						<%}
						%>
					</td>
					<td><%=user%></td>
				</tr>
			</tbody>
		</table>
		<br/>
	   	<table class="datatable" width="100%">
			<thead>
				<tr>
					<th scope="col">Table</th>
					<th scope="col">Sub#</th>
					<th scope="col">Column</th>
					<th scope="col">Undo Value</th>
					<% if(!op.equals("D") && !op.equals("UD") && !op.equals("UDD")){ %>
						<th scope="col">Current Value</th>
					<% } %>
				</tr>
			</thead>
			<tbody>
				<%
						Vector rows = RODServices.getDbService().getUndoInformation(ts,op,tab,id);
						
						int s = 0;
						for (int i=0; i<rows.size(); i++){
							String c = "";
							String c2 = "";
							Hashtable hash = (Hashtable) rows.elementAt(i);
									
							String ut = (String) hash.get("undo_time");
							String tabel = (String) hash.get("tab");
							String col = (String) hash.get("col");
							String operation = (String) hash.get("operation");
							String value = (String) hash.get("value");
							String sub_trans_nr = (String) hash.get("sub_trans_nr");
							String currentValue = RODServices.getDbService().getDifferences(ut,tabel,col);
							boolean diff = value.equals(currentValue);
							
							if (s % 2 == 0){
								c="#cbdcdc";
								c2 = "#cbdcdc";
							}
							if(!diff){
								c2 = "#FFFFCC";
							}
							%>
								<tr class="<%=c%>">
									<td bgcolor="<%=c%>"><%=RODUtil.replaceTags(tabel)%></td>
									<td bgcolor="<%=c%>"><%=sub_trans_nr%></td>
									<td bgcolor="<%=c%>"><%=RODUtil.replaceTags(col)%></td>
									<td bgcolor="<%=c%>"><%=RODUtil.replaceTags(value)%></td>
									<% if(!op.equals("D") && !op.equals("UD") && !op.equals("UDD")){ %>
										<td bgcolor="<%=c2%>"><%=RODUtil.replaceTags(currentValue)%></td>
									<% } %>
								</tr>
							<% 	s++;
						}%>
				</tbody>
			</table>
			<br/>
			<% if(!op.equals("D") && !op.equals("UD") && !op.equals("UDD")){ %>
			<% if(tab.equals("T_OBLIGATION")){%>
				<b>Countries reporting formally</b>
				<table class="datatable" width="100%">
					<thead>
						<tr>
							<th width="25%" scope="col">Undo Countries</th>
							<th width="25%" scope="col">Current Countries</th>
							<th width="25%" scope="col">Added Countries</th>
							<th width="25%" scope="col">Removed Countries</th>
						</tr>
					</thead>
					<tbody>
						<% 
							Hashtable h = RODServices.getDbService().getDifferencesInCountries(ts,id,"N",op); 
							String undoCountries = (String) h.get("undo");
							String currentCountries = (String) h.get("current");
							String addedCountries = (String) h.get("added");
							String removedCountries = (String) h.get("removed");
						%>
						<tr>
							<td width="25%"><%=undoCountries%></td>
							<td width="25%"><%=currentCountries%></td>
							<td width="25%" bgcolor="#CCFFCC"><%=addedCountries%></td>
							<td width="25%" bgcolor="#FFCCCC"><%=removedCountries%></td>
						</tr>
					</tbody>
				</table>
				<br/>
				<b>Countries reporting voluntarily</b>
				<table class="datatable" width="100%">
					<thead>
						<tr>
							<th width="25%" scope="col">Undo Countries</th>
							<th width="25%" scope="col">Current Countries</th>
							<th width="25%" scope="col">Added Countries</th>
							<th width="25%" scope="col">Removed Countries</th>
						</tr>
					</thead>
					<tbody>
						<% 
							Hashtable h_v = RODServices.getDbService().getDifferencesInCountries(ts,id,"Y",op); 
							String undoCountries_v = (String) h_v.get("undo");
							String currentCountries_v = (String) h_v.get("current");
							String addedCountries_v = (String) h_v.get("added");
							String removedCountries_v = (String) h_v.get("removed");
						%>
						<tr>
							<td width="25%"><%=undoCountries_v%></td>
							<td width="25%"><%=currentCountries_v%></td>
							<td width="25%" bgcolor="#CCFFCC"><%=addedCountries_v%></td>
							<td width="25%" bgcolor="#FFCCCC"><%=removedCountries_v%></td>
						</tr>
					</tbody>
				</table>
				<br/>
		<% }
			if(tab.equals("T_OBLIGATION")){%>
				<b>Environmental issues</b>
				<table class="datatable" width="100%">
					<thead>
						<tr>
							<th width="25%" scope="col">Undo Issues</th>
							<th width="25%" scope="col">Current Issues</th>
							<th width="25%" scope="col">Added Issues</th>
							<th width="25%" scope="col">Removed Issues</th>
						</tr>
					</thead>
					<tbody>
						<% 
							Hashtable h_i = RODServices.getDbService().getDifferencesInIssues(ts,id,op); 
							String undoIssues = (String) h_i.get("undo");
							String currentIssues = (String) h_i.get("current");
							String addedIssues = (String) h_i.get("added");
							String removedIssues = (String) h_i.get("removed");
						%>
						<tr>
							<td width="25%"><%=undoIssues%></td>
							<td width="25%"><%=currentIssues%></td>
							<td width="25%" bgcolor="#CCFFCC"><%=addedIssues%></td>
							<td width="25%" bgcolor="#FFCCCC"><%=removedIssues%></td>
						</tr>
					</tbody>
				</table>
				<br/>
		<% } 
			if(tab.equals("T_OBLIGATION")){%>
				<b>Other clients using this reporting</b>
				<table class="datatable" width="100%">
					<thead>
						<tr>
							<th width="25%" scope="col">Undo Clients</th>
							<th width="25%" scope="col">Current Clients</th>
							<th width="25%" scope="col">Added Clients</th>
							<th width="25%" scope="col">Removed Clients</th>
						</tr>
					</thead>
					<tbody>
						<% 
							Hashtable h_c = RODServices.getDbService().getDifferencesInClients(ts,id,"C",op,"A"); 
							String undoClients = (String) h_c.get("undo");
							String currentClients = (String) h_c.get("current");
							String addedClients = (String) h_c.get("added");
							String removedClients = (String) h_c.get("removed");
						%>
						<tr>
							<td width="25%"><%=undoClients%></td>
							<td width="25%"><%=currentClients%></td>
							<td width="25%" bgcolor="#CCFFCC"><%=addedClients%></td>
							<td width="25%" bgcolor="#FFCCCC"><%=removedClients%></td>
						</tr>
					</tbody>
				</table>
				<br/>
		<% } 
			if(tab.equals("T_OBLIGATION")){%>
				<b>Type of info reported</b>
				<table class="datatable" width="100%">
					<thead>
						<tr>
							<th width="25%" scope="col">Undo Info Types</th>
							<th width="25%" scope="col">Current Info Types</th>
							<th width="25%" scope="col">Added Info Typse</th>
							<th width="25%" scope="col">Removed Info Types</th>
						</tr>
					</thead>
					<tbody>
						<% 
							Hashtable h_i = RODServices.getDbService().getDifferencesInInfo(ts,id,op,"I"); 
							String undoInfo = (String) h_i.get("undo");
							String currentInfo = (String) h_i.get("current");
							String addedInfo = (String) h_i.get("added");
							String removedInfo = (String) h_i.get("removed");
						%>
						<tr>
							<td width="25%"><%=undoInfo%></td>
							<td width="25%"><%=currentInfo%></td>
							<td width="25%" bgcolor="#CCFFCC"><%=addedInfo%></td>
							<td width="25%" bgcolor="#FFCCCC"><%=removedInfo%></td>
						</tr>
					</tbody>
				</table>
		<% } 
		}
		} else { %>
		</br>
		<b>Not authenticated! Please verify that you are logged in (for security reasons, </br>
		the system will log you out after a period of inactivity). If the problem persists, please </br>
		contact the server administrator.</b>
		<% } %>
</div> <!-- workarea -->
<jsp:include page="footer.jsp" flush="true">
</jsp:include>
</body>
</html>
