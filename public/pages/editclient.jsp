<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Edit Client Organisation">

	<stripes:layout-component name="contents">
		
		<h1>Edit Client Organisation</h1>
		
		<stripes:form action="/clients" method="post">
			<stripes:hidden name="client.clientId"/>
			<table class="formtable">
				<col style="width:160px"/>
				<col style="width:490px"/>
				<tr>
					<td><label class="question" for="name">Name</label></td>
					<td>
						<stripes:text name="client.name" size="68" maxlength="255" id="name"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="shortname">Short Name</label></td>
					<td>
						<stripes:text name="client.shortName" size="68" maxlength="100" id="shortname"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="acronym">Acronym</label></td>
					<td>
						<stripes:text name="client.acronym" size="68" maxlength="255" id="acronym"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="address">Address</label></td>
					<td>
						<stripes:text name="client.address" size="68" maxlength="255" id="address"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="postalcode">Postal code</label></td>
					<td>
						<stripes:text name="client.postalCode" size="68" maxlength="255" id="postalcode"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="city">City</label></td>
					<td>
						<stripes:text name="client.city" size="68" maxlength="255" id="city"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="country">Country</label></td>
					<td>
						<stripes:text name="client.country" size="68" maxlength="255" id="country"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="homepage">Homepage</label></td>
					<td>
						<stripes:text name="client.url" size="68" maxlength="255" onchange="chkUrl(this)" id="homepage"/>
					</td>
				</tr>
				<tr>
					<td><label class="question" for="description">Description</label></td>
					<td>
						<stripes:textarea name="client.description" rows="4" cols="53" id="description"/>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<stripes:submit name="edit" value="OK" style="width:6em"/>  
						<stripes:button name="cancel" onclick="javascript:history.back()" value="Cancel" style="width:6em"/>  
					</td>
				</tr>
			</table>
		</stripes:form>

	</stripes:layout-component>
</stripes:layout-render>