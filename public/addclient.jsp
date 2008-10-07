<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page import="eionet.rod.services.RODServices" %>

<%
	//try {
		String[][] countries = RODServices.getDbService().getSpatialDao().getCountryIdPairs();
//	} catch (Exception e) {
		
	//}
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-gb">
	<head>
	<%@ include file="headerinfo.txt" %>
	<title>Add Client</title>

	<script type="text/javascript">
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
	<body onload="setFocus('clientForm')" onunload="window.opener.document.location.reload(true)">
		<h2 style="text-align:center">Add Client Organisation</h2>
		<form id="clientForm" method="post" action="addclient.jsv">
			<table width='100%' border="0">
				<tr>
					<td><label for="name" class="question">Name</label></td>
					<td align="left">
						<input type="text" id="name" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_NAME" size="40" maxlength="255"/>
					</td>
				</tr>
				<tr>
					<td><label for="acronym" class="question">Acronym</label></td>
					<td align="left">
						<input type="text" id="acronym" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_ACRONYM" size="40" maxlength="255"/>
					</td>
				</tr>
				<tr>
					<td><label for="address" class="question">Address</label></td>
					<td align="left">
						<input type="text" id="address" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_ADDRESS" size="40" maxlength="255"/>
					</td>
				</tr>
				<tr>
					<td><label for="postal_code" class="question">Postal Code</label></td>
					<td align="left">
						<table><tr>
					<td align="left">
						<input type="text" id="postal_code" name="/XmlData/RowSet/Row/T_CLIENT/POSTAL_CODE" size="10" maxlength="255"/>
					</td>
					<td><label for="city" class="question">City</label></td>
					<td align="left">
						<input type="text" id="city" name="/XmlData/RowSet/Row/T_CLIENT/CITY" size="19" maxlength="255"/>
					</td>
						</tr></table>
					</td>
				</tr>
				<tr>
					<td><label for="country" class="question">Country</label></td>
					<td align="left">
						<input type="text" id="country" name="/XmlData/RowSet/Row/T_CLIENT/COUNTRY" size="40" maxlength="255"/>
					</td>
				</tr>
				<tr>
					<td><label for="client_url" class="question">Homepage</label></td>
					<td align="left">
						<input type="text" id="client_url" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_URL" size="40" maxlength="255" onchange="chkUrl(this)"/>
					</td>
				</tr>
				<tr>
					<td><label for="client_email" class="question">Email</label></td>
					<td align="left">
						<input type="text" id="client_email" name="/XmlData/RowSet/Row/T_CLIENT/CLIENT_EMAIL" size="40" maxlength="255"/>
					</td>
				</tr>
				<tr valign="top">
					<td><label for="description" class="question">Comments</label></td>
					<td align="left">
						<textarea id="description" name="/XmlData/RowSet/Row/T_CLIENT/DESCRIPTION" rows="4" cols="34"></textarea>
					</td>
				</tr>
			</table>		
			<table width="100%">		
				<tr><td colspan="2">&#160;</td></tr>
				<tr align="right">
					<td colspan="2" align="right">
					<input onclick="javascript:doPost()" type="button" value="&#160;&#160;&#160;&#160;OK&#160;&#160;&#160;&#160;"/>
					<input type="button" onclick="javascript:window.close()" value="Cancel"/>
					<input type="hidden" name="dom-update-mode" value="I"/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
