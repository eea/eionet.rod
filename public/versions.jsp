<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.util.*,eionet.rod.ROUser,java.text.*,eionet.rod.services.RODServices,eionet.rod.RODUtil"%>
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
		<h1>Previous actions</h1>						
				<%
						String aid = request.getParameter("id");
						String tabel = request.getParameter("tab");
						String id_field = request.getParameter("id_field");
						Vector versions = RODServices.getDbService().getPreviousActions(aid, tabel, id_field);
						String id = null;
						String user = null;
						String show_object = null;
						if(versions.size() > 0){%>
						<form name="form" method="post" action="undo">
						<table class="datatable" width="600">
							<thead>
								<tr>
									<td colspan="5" align="right">
										<input type="submit" name="action" value="Undo selected"/>
									</td>
								</tr>
								<tr>
									<th scope="col">Time</th>
									<th scope="col">Object</th>
									<th scope="col">Operation</th>
									<th scope="col">User</th>
									<th scope="col"></th>
								</tr>
							</thead>
							<tbody>
						<%
							int s = 0;
							for (int i=0; i<versions.size(); i++){
								String c = "";
								Hashtable hash = (Hashtable) versions.elementAt(i);
										
										String ts = (String) hash.get("undo_time");
										String tab = (String) hash.get("tab");
										String col = (String) hash.get("col");
										String operation = (String) hash.get("operation");
										String value = (String) hash.get("value");
										System.out.println("|"+ts+" | "+tab+" | "+col+" | "+operation+" | "+value+"|");
	
										if(col.equalsIgnoreCase("PK_RA_ID") || col.equalsIgnoreCase("PK_SOURCE_ID")){
											id = (String)hash.get("value");
											show_object = (String) hash.get("show_object");
											s++;
										}
										if(col.equalsIgnoreCase("A_USER")){
											user = (String)hash.get("value");	
										}									
										if (s % 2 == 0){
											c="even";
										}
										if(user != null && id != null && tab != null){
											if(show_object.equals("y")){
												%>
													<tr class="<%=c%>">
														<td>
															<%
																DateFormat df = new SimpleDateFormat ("yyyy-MM-dd' 'HH:mm:ss");
																Date date = new Date(new Long(ts).longValue());
																String d = df.format(date);
															%>
															<%=d%>
														</td>
														<td>
															<%if(tab.equals("T_OBLIGATION")){%>
																<a href="undoinfo.jsp?ts=<%=ts%>&amp;tab=<%=tab%>&amp;op=<%=operation%>&amp;id=<%=id%>&amp;user=<%=user%>">OBLIGATION</a>
															<%} else if(tab.equals("T_SOURCE")){%>
																<a href="undoinfo.jsp?ts=<%=ts%>&amp;tab=<%=tab%>&amp;op=<%=operation%>&amp;id=<%=id%>&amp;user=<%=user%>">INSTRUMENT</a>
															<% } %>
														</td>
														<td>
															<%if(operation.equals("D")){%>
																DELETE
															<%} else if(operation.equals("I")){%>
																INSERT
															<%} else if(operation.equals("U")){%>
																UPDATE
															<%}
															%>
														</td>
														<td><%=user%></td>
														<td align="center">
														<%
															if(!operation.equals("D") || RODServices.getDbService().isIdAvailable(id,tab)){ %>
																	<input type="radio" name="group" value="<%=ts%>,<%=tab%>,<%=operation%>,<%=id%>"/>
															<% } %>
														</td>
													</tr>
												<%
											}
											id = null;
											user = null;
											tab = null;
											show_object = null;
										}
							} %>
						</tbody>
						</table>	
						</form>
						<% }
				if(tabel != null){ %>
				<br/>
				<br/>
						<%
						Vector history_list = RODServices.getDbService().getHistory(aid,tabel);
						if(history_list.size() > 0){
						if(tabel.equals("T_OBLIGATION")){ %>
							<b>History of changing data of Reporting Obligation: ID=<%=aid%></b>
						<% } else if(tabel.equals("T_SOURCE")){ %>
							<b>History of changing data of Legislative instrument: ID=<%=aid%></b>				
						<% } %>
						<table class="datatable" width="600">
						<thead>
							<tr>
								<th scope="col">Time</th>
								<th scope="col">Action</th>
								<th scope="col">User</th>
								<th scope="col">Description</th>
							</tr>
						</thead>
						<tbody>
						<%
							int z = 0;
							for (int i=0; i<history_list.size(); i++){
								String cl = "";
								
								if (z % 2 == 0){
									cl="even";
								}
								z++;
								
								Hashtable history = (Hashtable) history_list.elementAt(i);
								String time = (String) history.get("time");
								String action = (String) history.get("action");
								String huser = (String) history.get("user");
								String desc = (String) history.get("description");
								%>
								<tr class="<%=cl%>">
									<td><%=time%></td>
									<td>
										<%if(action.equals("D")){%>
											Delete
										<% } else if(action.equals("I")){%>
											Insert
										<% } else if(action.equals("U")){%>
											Update
										<% } %>
									</td>
									<td><%=huser%></td>
									<td><%=desc%></td>
								</tr>
							<%  } %>
						</tbody>
						</table> 
						<% } %>
			<% } %>
		<% } else { %>
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
