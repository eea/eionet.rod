<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,eionet.rod.services.RODServices,eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.services.FileServiceIF,eionet.rod.RODUtil,eionet.rod.countrysrv.servlets.Subscribe"%>

<%
	String ra_id = request.getParameter("ra-id");
	String spatial_id = request.getParameter("spatial");
	String member = request.getParameter("member");
	boolean show_content = true;
	
	if(ra_id == null || ra_id.equals("") || spatial_id == null || spatial_id.equals("") || member == null || member.equals("")){
		show_content = false;
		response.sendError(response.SC_NOT_FOUND);
	} else if(!RODServices.getDbService().getObligationDao().checkObligationById(ra_id) || !RODServices.getDbService().getSpatialDao().checkCountryById(spatial_id) || (!member.equals("Y") && !member.equals("N"))){
		show_content = false;
		response.sendError(response.SC_NOT_FOUND);
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<%@ include file="headerinfo.txt" %>
  <title>Country Information</title>
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
	//]]>
    </script>
</head>
<body>
<div id="container">
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="Country Information"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
    
<div id="workarea">

	<%
	if(show_content){
		
		String rt = "";
		if(member != null){
			if(member.equalsIgnoreCase("Y")){
				rt = "mc";
			} else if(member.equalsIgnoreCase("N")){
				rt = "cc";
			}
		}
		
		String vol = request.getParameter("vol");
		Hashtable hash = RODServices.getDbService().getSpatialDao().getCountryInfo(Integer.valueOf(ra_id).intValue(), Integer.valueOf(spatial_id).intValue() );
		
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
	
	<h1><%=RODUtil.replaceTags(country)%></h1>
   		
	   	<table class="datatable">
	   	<col style="width:30%" />
		<col style="width:70%" />
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Obligation</th>
					<td>
						<%=RODUtil.replaceTags(obligation)%>
					</td>
				</tr>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">Country</th>
					<td>
						<%=RODUtil.replaceTags(country)%>
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
						if(role != null && !role.equals("")){ %>
						 	<a href="responsible.jsp?role=<%=role%>&amp;member=<%=member%>&amp;spatial=<%=two_letter%>">
							 	<%=role%>-<%=rt%>-<%=two_letter%>
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
									<a href="<%=RODUtil.replaceTags(url,true)%>"><%=RODUtil.replaceTags(title)%></a><br/>
								<%
							}
							if(deliveries.size() > 0){ %>
								<p style="text-align:center">
									Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.<br/>
							        There can be a delay of up to one day before they show up.
								</p>	
							<% }
						%>
					</td>
				</tr>
			</table>
	<% } %>
</div> <!-- workarea -->
</div>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
