<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.util.*,eionet.rod.services.RODServices,eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.services.FileServiceIF,eionet.rod.RODUtil,eionet.rod.countrysrv.servlets.Subscribe"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>Country Information</title>
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
        <jsp:param name="name" value="Country Information"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
    
<div id="workarea">

	<%
		String ra_id = request.getParameter("ra-id");
		String spatial_id = request.getParameter("spatial");
		String vol = request.getParameter("vol");
		
		Hashtable hash = RODServices.getDbService().getCountryInfo(ra_id, spatial_id);
		
		Hashtable obligation_info = (Hashtable) hash.get("obligationinfo");
		String obligation = (String) obligation_info.get("title");
		String role = (String) obligation_info.get("role");
		
		Hashtable spatial_info = (Hashtable) hash.get("spatialinfo");
		String country = (String) spatial_info.get("name");
		String two_letter = (String) spatial_info.get("two");
		two_letter = two_letter.toLowerCase();
		
		Hashtable period = (Hashtable) hash.get("period");
		String start = (String) period.get("start");
		if(start == null || start.equals("") || start.equals("00/00/0000") || start.equals("0000-00-00")){
			start = "Prior to start of ROD (2003)";
		} else {
			start = "From " + start;	
		}
		String end = (String) period.get("end");
		if(end == null || end.equals("") || end.equals("00/00/0000") || start.equals("0000-00-00")){
			end = "present";
		}
		
		Vector deliveries = (Vector) hash.get("deliveries");
	%>
	
	<h1><%=country%></h1>
   		
	   	<table class="datatable">
	   	<col style="width:30%" />
		<col style="width:70%" />
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Obligation</th>
					<td>
						<%=obligation%>
					</td>
				</tr>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">Country</th>
					<td>
						<%=country%>
					</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Status</th>
					<td>
						<%if(vol.equals("Y")){%>
							Voluntary
						<%} else { %>
							Formal
						<% } %>
					</td>
				</tr>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">Participation Period</th>
					<td>
						<%=start%> to <%=end%>
					</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Responsible Role</th>
					<td>
						<%
						String person = null;
						String institute = null;
						String role_url = null;
						if(role != null && !role.equals("")){ 
							Hashtable role_desc = RODServices.getDbService().getRoleDesc(role+"-"+two_letter);
							person = (String) role_desc.get("person");
							institute = (String) role_desc.get("institute");
							role_url = (String) role_desc.get("role_url");
						 }%>
						 <%if(role_url != null && !role_url.equals("")){ %>
						 	<a href="<%=role_url%>" target="_blank">
						 <% } %>
							 <%if(person != null && !person.equals("")){ %>
							 	<%=person%>&nbsp;
							 <% } 
							 if(institute != null && !institute.equals("")){ %>
							 	[<%=institute%>]&nbsp;
							 <% } 
							 if(role != null && !role.equals("")){ %>
							 	(<%=role%>-<%=two_letter%>)
							 <% } %>
						 <%if(role_url != null && !role_url.equals("")){ %>
						 	</a>
						 <% } %>
					</td>
				</tr>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">Deliveries</th>
					<td>
						<%
							for (int i=0; i<deliveries.size(); i++){
								Hashtable delivery = (Hashtable) deliveries.elementAt(i);
								String title = (String) delivery.get("title");
								String url = (String) delivery.get("url");
								%>
									<a href="<%=url%>" target="_blank"><%=title%></a><br/>
								<%
							}
						%>
					</td>
				</tr>
			</table>
</div> <!-- workarea -->
<jsp:include page="footer.jsp" flush="true">
</jsp:include>
</body>
</html>