<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.io.*,java.lang.*,eionet.rod.ROUser,eionet.rod.Constants,java.text.*,eionet.rod.services.RODServices,eionet.rod.RODUtil,eionet.rod.countrysrv.Extractor,com.tee.uit.security.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%!final static int OPTION_MAXLEN=80;%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<%@ include file="headerinfo.txt" %>
  <title>Harvest deliveries, roles and parameters</title>
	<script src="script/util.js" type="text/javascript"></script>
    <script type="text/javascript">
    //<![CDATA[ 
    	function setwait(theform) {
      		document.body.style.cursor='wait';
					theform.submitbtn.value='Harvesting';
					return true;
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
		<%
		String msg = "";
		String error1 = "";
		
		if (rouser!=null){
			if  (!acl.checkPermission( rouser.getUserName(), Constants.ACL_UPDATE_PERMISSION )){
				%>
					<div class="error-msg">
						Insufficient permission
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
				<ul>
				<li><a href="history">Show harvesting history</a></li>
				</ul>
					<h2>Select the data you want to be harvested</h2>
			    <form onsubmit="javascript:setwait(this)" method="post" action="harvester.jsp">
					<div>
					  <input type="radio" name="MODE" id="mode0" value="0" checked="checked"/> <label for="mode0">All</label><br/>
					  <input type="radio" name="MODE" id="mode1" value="1"/> <label for="mode1">Deliveries</label><br/>
					  <input type="radio" name="MODE" id="mode2" value="2"/> <label for="mode2">Roles</label><br/>
					  <input type="radio" name="MODE" id="mode3" value="3"/> <label for="mode3">Parameters</label><br/>
						<input style="width: 200px;" type="submit" id="submitbtn" value="Harvest"/>
						</div>
			    </form>		
					<div class="note-msg">
					<strong>Note</strong>
					<p>It will take some to harvest the data. Please be patient</p>
					</div>
			<%}
			} else { %>
				<div class="error-msg">
				Not authenticated! Please verify that you are logged in (for security reasons,
				the system will log you out after a period of inactivity). If the problem persists, please
				contact the server administrator.
				</div>
			<% } %>
</div> <!-- workarea -->
</div>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
