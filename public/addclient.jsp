<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page import="eionet.rod.services.RODServices" %>

<%
	//try {
		String[][] countries = RODServices.getDbService().getSpatialDao().getCountryIdPairs();
//	} catch (Exception e) {
		
	//}
%>


<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
	<title>Add Client</title>
	<link href="eionet.css" rel="stylesheet" type="text/css"></link>
	<!--SCRIPT language="JavaScript" src="script/util.js"></SCRIPT-->

	<script language="JavaScript">
	//<![CDATA[

	//COPY+PASTE from util.js needs re-engineering in the future
	function chkUrl(fld) {
		var s = fld.value;
		if ( s != "" &&  (s.substr(0,7) != "http://") && (s.substr(0,8) != "https://") && (s.substr(0,6) != "ftp://") )	{
			fld.focus()
			alert("Wrong URL format");
			//fld.select();
		}
}


		function doPost() {
			document.forms["clientForm"].submit();
			//alert(document.forms["clientForm"].elements["/XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM"].value);
		}
		// Sets focus on form's first element
		//
		function setFocus(formName) {
			document.getElementById("name").focus();
		}
	//]]>
	</script>
	</head>

	<body onLoad="setFocus('clientForm')" onUnload="window.opener.document.location.reload(true)">
	<!--body onLoad="setFocus('clientForm')"-->
		<center>
		<h2>Add Client Organisation</h2>
		<form id="clientForm" method="post" action="addclient.jsv" charset="iso-8859-1">
			<table width='100%' border="0">
				<tr>
					<td align="right"><b>Name:&#160;</b></td>
					<td align="left">
						<input type="text" id="name" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME" size="40" maxlength="255"></input>
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
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/COUNTRY" size="40" maxlength="255"></input>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Homepage:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_URL" size="40" maxlength="255" onchange="chkUrl(this)"></input>
					</td>
				</tr>
				<tr>
					<td align="right"><b>Email:&#160;</b></td>
					<td align="left">
						<input type="text" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_EMAIL" size="40" maxlength="255"></input>
					</td>
				</tr>
				<tr valign="top">
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
