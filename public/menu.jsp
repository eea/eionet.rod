<%@ page import="eionet.rod.ROUser,eionet.rod.Attrs,eionet.rod.RODUtil" %>
<div id="globalnav">
		  <h2>Contents</h2>
		  <ul>
			 <li><a href="index.html" title="ROD Home">ROD</a></li>
		    <li><a href="deliveries.jsv" title="Country deadlines">Deadlines</a></li>
		    <li><a href="rorabrowse.jsv?mode=A" title="Reporting Obligations">Obligations</a></li>
		    <li><a href="text.jsv?mode=H" title="General Help">Help</a></li>
		    </ul>
			    		<%
						String appName = getServletContext().getInitParameter(Attrs.APPPARAM);
						ROUser rouser = (ROUser) session.getAttribute(Attrs.USERPREFIX + appName);
						if (rouser!=null){
							String userName = rouser.getUserName();
						%>
						<h2>Logged in as<br/><%=userName%></h2>
					    	<ul>
						    <li><a href="logout_servlet" title="Log out">Logout</a></li>
						</ul>
						<%
						} else {
						%>
				    	<h2>Not logged in</h2>
						<ul>
							
						    <li><a href="login.html" title="Login">Login</a></li>
						</ul>
						<% } 
						
		  if(rouser!=null){ %>
			  <ul>
			    <li><a href="subscribe.jsp" title="Create a UNS Subscription">Subscribe</a></li>
			  	<li><a href="versions.jsp?id=-1">Global History</a></li>
			  </ul>
		  <% } %>
		  <h2>Reportnet</h2>
		  <ul>
		    <li><a href="http://cdr.eionet.eu.int/" title="Central Data Repository">CDR Repository</a></li>
		    <li><a href="http://dd.eionet.eu.int/">Data Dictionary</a></li>
		    <li><a href="http://cr.eionet.eu.int/">Content Registry</a></li>
		  </ul>
</div>

