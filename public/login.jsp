<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Administrator login</title>
<link href="eionet.css" rel="stylesheet" type="text/css" />
</head>

<body bgcolor="#f0f0f0" onload="document.forms['LOGIN'].elements[0].focus()">
<!--form name="f" id="LOGIN" method="POST" action="login_servlet"-->
<table width="200">
	<tr><td colspan="2"><span class="head1">Administrator login</span></td></tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
	<form name="f" id="LOGIN" method="POST" action="login_servlet">
	
		<td style="color:#999"><span class="Mainfont">User name:</span></td>
		<td align="right"><input id="USERNAME" type="text" name="j_username" size="15" maxlength="50"width="200" style="width:200"/></td>

	</tr>
	<tr>
		<td style="color:#999"><span class="Mainfont">Password:</span></td>
		<td align="right"><input id="PASSWORD" type="password" name="j_password" size="15" maxlength="50" width="200" style="width:200"/></td>

	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>&nbsp;</td>
		<td>
		<table>
		<tr>
		<td>
		<%
			String rd = request.getParameter("rd");
			if(rd != null && !rd.equals("")){%>
				<input type="hidden" name="rd" value="<%=rd%>"/>
			<%
			}
		%>
		<input type="submit" value="Authorize"/></form></td>
		<td><!--input type="reset" value="Clear"/--></td><td><form name="lo" method="POST" action="logout_servlet">
			<input type="submit" value="Logout"/>
		</form></td>
		</tr>
		</table>
</td>
	</tr>
</table>
<!--/form-->

</body>
</html>