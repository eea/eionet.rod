<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.util.*,eionet.rod.ROUser,java.text.*,eionet.rod.services.RODServices,eionet.rod.RODUtil"%>

<%
	String role_name = request.getParameter("role");
	String spatial = request.getParameter("spatial");
	String member = request.getParameter("member");
	
	String id = "";
	String rt = "";
	if(member != null){
		if(member.equalsIgnoreCase("Y")){
			rt = "mc";
		} else if(member.equalsIgnoreCase("N")){
			rt = "cc";
		}
	}
	if(role_name != null && spatial != null){
		id = role_name + "-" + rt + "-" + spatial;
		id = id.toLowerCase();
	}
	
	if(role_name == null || role_name.equals("") || spatial == null || spatial.equals("") || member == null || member.equals("")){
		response.sendError(response.SC_NOT_FOUND, "No such role");
	} else if(!RODServices.getDbService().getRoleDao().checkRole(role_name) || !RODServices.getDbService().getSpatialDao().checkCountry(spatial)){
		response.sendError(response.SC_NOT_FOUND, "No such role");
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%!final static int OPTION_MAXLEN=80;%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<%@ include file="headerinfo.txt" %>
  <title>Responsible role</title>
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
<div id="container">
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="Responsible role"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
	<%

	Hashtable role = RODServices.getDbService().getRoleDao().getRoleDesc(id);
	String name = (String)role.get("name");
	String email = (String)role.get("email");
	String role_url = (String)role.get("role_url");
	String members_url = (String)role.get("members_url");
	String last_harvested = (String)role.get("last_harvested");
	
	Vector occupants = (Vector)role.get("occupants");
	
	Vector obligations = new Vector();
	if(role_name != null){
		obligations = RODServices.getDbService().getRoleDao().getRoleObligations(role_name);
	}
	%>
	<h1>Responsible role</h1>
	<table class="datatable">
		<%if(name != null && !name.equals("")){%>
		<tr>
			<th scope="row" class="scope-row">Role name</th>
			<td><%=RODUtil.replaceTags(name)%></td>
		</tr>
		<% } %>
		<%if(email != null && !email.equals("")){%>
		<tr>
			<th scope="row" class="scope-row">E-mail</th>
			<td><%=RODUtil.replaceTags(email)%></td>
		</tr>
		<% } %>
		<%if(role_url != null && !role_url.equals("")){%>
		<tr>
			<th scope="row" class="scope-row">Role URL</th>
			<td><%=RODUtil.replaceTags(role_url,false)%></td>
		</tr>
		<% } %>
		<%if(members_url != null && !members_url.equals("")){%>
		<tr>
			<th scope="row" class="scope-row">Role members URL</th>
			<td><%=RODUtil.replaceTags(members_url,false)%></td>
		</tr>
		<% } %>
		<% 
		if(occupants != null && occupants.size() > 0){
			%>
			<tr>
				<th scope="row" class="scope-row">Persons</th>
				<td>
			<%
			for (int i=0; i<occupants.size(); i++){
				Hashtable hash = (Hashtable) occupants.elementAt(i);
				String person = (String)hash.get("person");
			%>
				<%=RODUtil.replaceTags(person)%>
			<%
				if(i != (occupants.size() - 1)){%>
					<br/>	
				<%
				}
			}%>
				</td>
			</tr>
			<%
		}		
		%>
		<%if(last_harvested != null && !last_harvested.equals("")){%>
		<tr>
			<th scope="row" class="scope-row">Last harvested</th>
			<td><%=last_harvested%></td>
		</tr>
		<% }
		if(obligations != null && obligations.size() > 0){
			%>
			<tr>
				<th scope="row" class="scope-row">Obligations</th>
				<td>
			<%
			for (int i=0; i<obligations.size(); i++){
				Hashtable hash = (Hashtable) obligations.elementAt(i);
				String title = (String)hash.get("title");
				String ra_id = (String)hash.get("ra_id");
				String sid = (String)hash.get("sid");
			%>
				<a href="show.jsv?id=<%=ra_id%>&amp;mode=A"><%=RODUtil.replaceTags(title)%></a>
			<%
				if(i != (obligations.size() - 1)){%>
					<br/>	
				<%
				}
			}%>
				</td>
			</tr>
			<%
		}		
		%>
	</table>
   	
</div> <!-- workarea -->
</div>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
