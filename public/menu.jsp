<%@ page import="eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.RODUtil,eionet.rod.Constants,com.tee.uit.security.*" %>
<div id="leftcolumn" class="localnav">
 		  <%
			String appName = getServletContext().getInitParameter(Attrs.APPPARAM);
			ROUser rouser = (ROUser) session.getAttribute(Attrs.USERPREFIX + appName);
			HashMap acls = AccessController.getAcls();
			AccessControlListIF acl = (AccessControlListIF) acls.get(Constants.ACL_HARVEST_NAME);%>
			  <ul>
			    <li><a href="index.html" title="ROD Home">Home </a></li>
			    <li><a href="countrydeadlines" title="Country deadlines">Country deadlines </a></li>
			    <li><a href="rorabrowse.jsv?mode=A" title="Reporting Obligations">Obligations </a></li>
			    <% if (rouser!=null){%>
			    	<li><a href="subscribe.jsp" title="Create a UNS Subscription">Subscribe </a></li>
			    <% } else { %>
			    	<li><a href="login.jsp?rd=subscribe" title="Create a UNS Subscription">Subscribe </a></li>
			    <% } %>
			    <li><a href="text.jsv?mode=H" title="General Help">Help </a></li>
				<% if (rouser!=null){%>
					    <li><a href="versions">Global History </a></li>
				<% } %>
				<li><a href="show.jsv?id=1&amp;mode=C" title="Navigate to reporting obligations via the Eur-lex legislative instrument categories">Legal instruments </a></li>
				<li><a href="rorabrowse.jsv?mode=A&amp;anmode=P" title="Eionet Priority Data flows">Priority dataflows </a></li>
				<li><a href="analysis" title="Database statistics">Database statistics </a></li>
				<li><a href="search" title="Advanced search">Advanced search </a></li>
				<%if (rouser!=null){
					if (acl.checkPermission( rouser.getUserName(), Constants.ACL_UPDATE_PERMISSION )){ %>
						<li><a href="harvester.jsp">Harvest </a></li>
				<% }} %>
			</ul>
</div>
