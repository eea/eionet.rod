<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN" >


<%@ page import="eionet.rod.services.RODServices" %>

<%
	//try {
		String[][] countries = RODServices.getDbService().getCountries();
//	} catch (Exception e) {
		
	//}
%>

<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
	<title>Add Client</title>
	<link href="eionet.css" rel="stylesheet" type="text/css"></link>
	<script language="JavaScript">

		function doPost() {
			document.clientForm.submit();
			//alert(document.clientForm.elements["/XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM"].value);
		}
		// Sets focus on form's first element
		//
		function setFocus(formName) {
			if(document.all) {
				if(document.all(formName).elements[0])
					document.all(formName).elements[0].focus();
			}
			else {
				if(document.forms[formName].elements[0])
					document.forms[formName].elements[0].focus();
			}
		}
	</script>
	</head>

	<body onLoad="setFocus('clientForm')" onUnload="window.opener.document.location.reload(true)">
		<center>
		<h2>Add Client Organisation</h2>
		<form name="clientForm" method="post" action="addclient.jsv" charset="iso-8859-1">
			<table width='100%' border="0">
				<tr>
					<td align="right"><b>Name:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME" size="40" maxlength="255"></input>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Acronym:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM" size="40" maxlength="255"></input>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Address:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_ADDRESS" size="40" maxlength="255"></input>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Postal Code:&#160;</b></td>
					<td align="left">
						<table><tr>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/POSTAL_CODE" size="10" maxlength="255"></input>
					</td>
					<td align="right"><b>City:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CITY" size="19" maxlength="255"></input>
					</td>
						</tr></table>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Country:&#160;</b></td>
					<td align="left" >
						<select name="/XmlData/RowSet/Row/T_CLIENT/FK_SPATIAL_ID" width="40" style="width:263">
							<option value="0" selected="true"></option>
							<% for (int i=0; i< countries.length; i++) { %>
								<option value="<%=countries[i][0]%>"><%=countries[i][1]%></option>
							<% } %>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Homepage:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_URL" size="40" maxlength="255"></input>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Email:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_EMAIL" size="40" maxlength="255"></input>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Comments:&#160;</b></td>
					<td align="left">
						<textarea name="/XmlData/RowSet/Row/T_CLIENT/DESCRIPTION" rows="4" cols="34"></textarea>
					</td>
				</tr>
			</table>		
			<table width="100%">		
				<tr><td colspan="2">&#160;</td></tr>
				<tr align="right">
					<td colspan="2" align="right"><input onclick="javascript:doPost()" type="button" value="&#160;&#160;&#160;&#160;OK&#160;&#160;&#160;&#160;"/>
					<input type="button" onclick="javascript:window.close()" value="Cancel"/></td>
				</tr>
			</table>
			<input type="hidden" name="dom-update-mode" value="I"></input>
		</form>
		</center>
	</body>
</html>