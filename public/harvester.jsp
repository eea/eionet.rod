<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.lang.*,eionet.rod.ROUser,eionet.rod.Constants,java.text.*,eionet.rod.services.RODServices,eionet.rod.RODUtil,eionet.rod.countrysrv.Extractor,com.tee.uit.security.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%!final static int OPTION_MAXLEN=80;%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<%@ include file="headerinfo.txt" %>
  <title>Harvest deliveries, roles and parameters</title>
	<script language = "javascript" src="script/util.js" type="text/javascript"></script>
    <script language = "javascript" type="text/javascript">
    //<![CDATA[ 
    	function harvest( mode ) {
      		var ff = document.f;
      		ff.action = ff.action + '?MODE=' + mode;
      		document.body.style.cursor='wait';
      		ff.submit();
      	} 
    //]]>
	</script>
</head>
<body>
<div id="container">
    <jsp:include page="location.jsp" flush='true'>
        <jsp:param name="name" value="Harvest"/>
    </jsp:include>
    <%@ include file="menu.jsp" %>
<div id="workarea">
	<h1>Harvest deliveries, roles and parameters</h1>
	<br/>
		<%
		String msg = "";
		String error1 = "";
		
		HashMap acls = AccessController.getAcls();
		AccessControlListIF acl = (AccessControlListIF) acls.get(Constants.ACL_HARVEST_NAME);
		if (rouser!=null){
			if  (!acl.checkPermission( rouser.getUserName(), Constants.ACL_UPDATE_PERMISSION )){
				%>
					<div class="error-msg">
						Insufficient permissions
					</div>
				<%
			} else {
				//// handle the POST request//////////////////////
				//////////////////////////////////////////////////
				if (request.getMethod().equals("POST")){
					try {
						int mode = Integer.parseInt( request.getParameter("MODE") );
						Extractor ext = new Extractor();
				    
						ext.harvest(mode, rouser.getUserName() );
						msg = "Harvested! See log for details";
					} catch (Exception e ) {
						error1 = e.toString();
					}
				}
	
				if(msg != null && !msg.equals("")){%>
					<div class="system-msg">
						<%=msg%>
					</div>
				<% }
				if(error1 != null && !error1.equals("")){%>
					<div class="error-msg">
						<%=error1%>
					</div>
				<%
				}%>	
				<a href="history.jsv?id=0&amp;entity=H">Show harvesting history</a><br/>
			    <form name="f" method="post" action="harvester.jsp">
				    <b>Select data, you want to be harvested:</b><br/>
				    <div class="note-msg">
					 	<strong>Note</strong>
					 	<p>It will take some to harvest the data. Please be patient</p>
					</div>
				    <input style="width: 200px;" type="button" onclick="javascript:harvest(0)" value="All"></input>
				    <br/><input style="width: 200px;" type="button" onclick="javascript:harvest(1)" value="Deliveries"></input> 
				    <br/><input style="width: 200px;" type="button" onclick="javascript:harvest(2)" value="Roles"></input>
				    <br/><input style="width: 200px;" type="button" onclick="javascript:harvest(3)" value="Parameters"></input>
			    </form>		
			<%}
			} else { %>
				<br/>
				<b>Not authenticated! Please verify that you are logged in (for security reasons, <br/>
				the system will log you out after a period of inactivity). If the problem persists, please <br/>
				contact the server administrator.</b>
			<% } %>
</div> <!-- workarea -->
</div>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
