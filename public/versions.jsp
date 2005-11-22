<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.util.*,eionet.rod.services.RODServices,eionet.rod.RODUtil,eionet.rod.countrysrv.servlets.Subscribe"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%!final static int OPTION_MAXLEN=80;%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>EEA - Reporting Obligations Database</title>
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
    </script>
</head>
<body>
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="Previous Versions"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
   <div class="section">
   	<br>
   	<br>
	   	<table class="sortable" cellpadding="2" cellspacing="0" width="600" border="0">
				<tr>
					<td colspan="2">
						<h1>Previous versions</h1>						
					</td>
				</tr>
				<tr>
					<td>
						<strong>Title:<strong>
					</td>
					<td>
						<strong>Version:<strong>
					</td>
					<td>
						
					</td>
				</tr>
				<%
					String id = request.getParameter("id");
					String pid = request.getParameter("pid");
					Vector versions = null;
						if (pid != null && pid != ""){
							versions = RODServices.getDbService().getPreviousVersions(pid);
						} else {
							versions = RODServices.getDbService().getPreviousVersions(id);
						}
						int max = 0;
						
						for (int i=0; i<versions.size(); i++){
							Hashtable h = (Hashtable) versions.elementAt(i);
							String ver = (String) h.get("version");
							if (ver != null && ver != ""){
								int v = Integer.parseInt(ver);
								if(v > max){
									max = v;
								}	
							}
						}
						
						for (int i=0; i<versions.size(); i++){
							String c = "";
							Hashtable hash = (Hashtable) versions.elementAt(i);
							String pkraid = (String) hash.get("id");
							String parentid = (String) hash.get("parentid");
							String title = (String) hash.get("title");
							String version = (String) hash.get("version");
							String source = (String) hash.get("source");
							if (i % 2 == 0){
								c="even";
							}
							%>
								<tr class="<%=c%>">
									<% if (pid != null && pid != ""){ %>
										<td><a href="show.jsv?id=<%=pid%>&aid=<%=source%>&mode=A&sv=T"><%=title%></a></td>
									<%} else { %>
										<td><a href="show.jsv?id=<%=id%>&aid=<%=source%>&mode=A&sv=T"><%=title%></a></td>
									<% } %>
									<td><%=version%></td>
									<td>
									<% if(Integer.parseInt(version) != max){
										if(parentid != null && !parentid.equals("")) {%>
											<a href="restore?id=<%=pkraid%>&pid=<%=parentid%>&latestversion=<%=max%>">Restore</a>
									<% } else { %>
											<a href="restore?id=<%=pkraid%>&pid=<%=pkraid%>&latestversion=<%=max%>">Restore</a>
									<% }	
									} %>
									</td>
								</tr>
							<%
						}
				%>
				
			</table>
		<br>
   </div>
</div> <!-- workarea -->
<jsp:include page="footer.jsp" flush="true">
</jsp:include>
</body>
</html>
