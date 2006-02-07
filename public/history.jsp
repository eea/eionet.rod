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
        <jsp:param name="name" value="History of changes"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
		<%
		if (rouser!=null){
			String item_type = request.getParameter("item_type");
			String mode = request.getParameter("mode");

						Vector versions = RODServices.getDbService().getDeletedFromUndo(item_type);
						String id = null;
						String user = null;
						String show_object = null;
						if(versions.size() > 0){
							if(item_type.equals("O' OR ITEM_TYPE='A")){ %>		
								<h1>Deleted Obligations</h1>
							<%} else if(item_type.equals("L")){ %>					
								<h1>Deleted Legislative instruments</h1>
							<% } %>
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
						<% } %>
				<br/>
				<br/>
						<%
						Vector history_list = RODServices.getDbService().getDeletedItemsVector(item_type);
						if(history_list.size() > 0){
						if(item_type.equals("O' OR ITEM_TYPE='A")){ %>
							<h1>Deleted Items of type Reporting obligation</h1>
						<% } else if(item_type.equals("L")){ %>
							<h1>Deleted Items of type Legislative instrument </h1>				
						<% } %>
						<table class="datatable" width="600">
						<thead>
							<tr>
								<th scope="col">Item ID</th>
								<th scope="col">Type</th>
								<th scope="col">Time</th>
								<th scope="col">Action</th>
								<th scope="col">User</th>
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
								String item_id = (String) history.get("ITEM_ID");
								String item_t = (String) history.get("ITEM_TYPE");
								String time = (String) history.get("LOG_TIME");
								String action = (String) history.get("ACTION_TYPE");
								String huser = (String) history.get("USER");
								%>
								<tr class="<%=cl%>">
									<td><a href="history.jsv?entity=<%=item_t%>&amp;id=<%=item_id%>"><%=item_id%></a></td>
									<td>
										<%if(item_t.equals("O")){%>
											Reporting Obligation
										<% } else if(item_t.equals("A")){%>
											Reporting Activity
										<% } else if(item_t.equals("L")){%>
											Legislative Instrument
										<% } %>
									</td>
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
								</tr>
							<%  } %>
						</tbody>
						</table> 
						<% }
		} else { %>
		</br>
		<b>Not authenticated user. Please verify that you are logged in (for security reasons, </br>
		the system will log you out after a period of inactivity). If the problem persists, please </br>
		contact the server administrator.</b>
		<% } %>
</div> <!-- workarea -->
<jsp:include page="footer.jsp" flush="true">
</jsp:include>
</body>
</html>
