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
	//]]>
    </script>
</head>
<body>
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="Historical Deadlines"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
		<%
		if (rouser!=null){
			String start_date = request.getParameter("start_date");
			String end_date = request.getParameter("end_date");
			String start = "";
			String end = "";
			if(start_date != null){
				start = start_date;
			}
			if(end_date != null){
				end = end_date;
			}
				
		%>
		<h1>Historical Deadlines</h1>						

						<form name="form" method="post" action="histDeadlines">
							<table width="680" cellspacing="0" border="0">
								<tr>
									<td style="border-top: #008080 1px solid; border-left: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" 
													valign="center" bgcolor="#ffffff" align="left">
										<span class="headsmall"><font title="Search" face="Verdana" color="#000000" size="1">Search</font></span>
									</td>
								</tr>
								<tr valign="top" bgcolor="#cbdcdc">
									<td style="border-left: #c0c0c0 1px solid; border-right: #c0c0c0 1px solid; border-bottom: #c0c0c0 1px solid" valign="middle"><span class="Mainfont">
										<font face="Verdana" size="2">
											<b>Start date</b>
											<input type="text" value="<%=start%>" name="start_date" size="10" maxlength="10"></input><span class="smallfont">&#160;(dd/mm/yyyy)</span>
											<b>End date</b>
											<input type="text" value="<%=end%>" name="end_date" size="10" maxlength="10"></input><span class="smallfont">&#160;(dd/mm/yyyy)</span>
											&#160;
											<input type="submit" value="Search" style="font-weight: normal; color: #000000; background-image: url('images/bgr_form_buttons.jpg')"></input>
										</font></span>
									</td>
								</tr>
							</table>
						</form>
						<%
						Vector deadlines = null;
						if(start_date != null && end_date != null){
							deadlines = RODServices.getDbService().getHistoricDeadlines(start_date, end_date);
						}
						if(deadlines != null){
						%>
						<table class="datatable" width="680">
							<thead>
								<tr>
									<th scope="col">Reporting Obligation</th>
									<th scope="col">Deadline</th>
								</tr>
							</thead>
							<tbody>
						<%
							int s = 0;
							for (int i=0; i<deadlines.size(); i++){
								String c = "";
								Hashtable hash = (Hashtable) deadlines.elementAt(i);
										
										String id = (String) hash.get("id");
										String source_id = (String) hash.get("source");
										String title = (String) hash.get("title");
										String deadline = (String) hash.get("deadline");
	
										if (s % 2 == 0){
											c="even";
										}
										%>
										<tr class="<%=c%>">
											<td><a href="show.jsv?id=<%=id%>&aid=<%=source_id%>&mode=A"><%=title%></td>
											<td width="80"><%=deadline%></td>
										</tr>
										<%
										s++;
							} %>
						</tbody>
						</table>	
						<% } 
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