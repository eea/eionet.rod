<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Reporting client or issuer details" bread2="Clients" bread2Url="clients" bread3="Client information">

	<stripes:layout-component name="contents">
		
		<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName,'/obligations','c')}">
			<ul id="dropdown-operations">
				<li><a href="#">Operations</a>
					<ul>
						<li>
							<stripes:link href="/clients" event="edit">
								Edit Organisation
								<stripes:param name="client.clientId" value="${actionBean.clientId}"/>
							</stripes:link>
						</li>
					</ul>
				</li>
			</ul>
		</c:if>
		
		<h1>Reporting client or issuer details</h1>
		
		<table border="0">
			<tr valign="top">
				<th scope="row" align="right" width="120">Name:</th>
				<td align="left" width="490">${rodfn:replaceTags(actionBean.client.name)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right" width="120">Short Name:</th>
				<td align="left" width="490">${rodfn:replaceTags(actionBean.client.shortName)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right">Acronym:</th>
				<td align="left" >${rodfn:replaceTags(actionBean.client.acronym)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right">Address:</th>
				<td align="left">${rodfn:replaceTags(actionBean.client.address)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right">Postal code:</th>
				<td align="left">${rodfn:replaceTags(actionBean.client.postalCode)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right">City:</th>
				<td align="left">${rodfn:replaceTags(actionBean.client.city)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right">Country:</th>
				<td align="left">${rodfn:replaceTags(actionBean.client.country)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right">Homepage:</th>
				<td align="left">
					<c:if test="${!empty actionBean.client.url}">
						<a href="${rodfn:replaceTags2(actionBean.client.url, true, true)}">
							${rodfn:replaceTags2(actionBean.client.url, true, true)}
						</a>
					</c:if>
				</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="right">Description:</th>
				<td align="left">
					${rodfn:replaceTags(actionBean.client.description)}
				</td>
			</tr>
		</table>
		
		<c:if test="${!empty actionBean.client.directObligations}">
			<h2>Direct Obligations:</h2>
			<table class="datatable" width="600">
				<tr>
					<th scope="col">ID</th>
					<th scope="col">Title</th>
				</tr>
				<c:forEach items="${actionBean.client.directObligations}" var="obligation" varStatus="loop">
					<tr ${loop.index % 2 == 0 ? 'class="even"' : ''}>
						<td><a href="${pageContext.request.contextPath}/obligations/${obligation.obligationId}">${obligation.obligationId}</a></td>
						<td>${obligation.title}&#160;
							<c:if test="${obligation.terminate == 'Y'}">
								<em>Terminated</em>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		<c:if test="${!empty actionBean.client.indirectObligations}">
			<h2>Indirect Obligations:</h2>
			<table class="datatable" width="600">
				<tr>
					<th scope="col">ID</th>
					<th scope="col">Title</th>
				</tr>
				<c:forEach items="${actionBean.client.indirectObligations}" var="obligation" varStatus="loop">
					<tr ${loop.index % 2 == 0 ? 'class="even"' : ''}>
						<td><a href="${pageContext.request.contextPath}/obligations/${obligation.obligationId}">${obligation.obligationId}</a></td>
						<td>${obligation.title}&#160;
							<c:if test="${obligation.terminate == 'Y'}">
								<em>Terminated</em>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		<c:if test="${!empty actionBean.client.directInstruments}">
			<h2>Direct Instruments:</h2>
			<table class="datatable" width="600">
				<tr>
					<th scope="col">ID</th>
					<th scope="col">Alias</th>
				</tr>
				<c:forEach items="${actionBean.client.directInstruments}" var="instrument" varStatus="loop">
					<tr ${loop.index % 2 == 0 ? 'class="even"' : ''}>
						<td><a href="${pageContext.request.contextPath}/instruments/${instrument.sourceId}">${instrument.sourceId}</a></td>
						<td>${instrument.sourceAlias}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		<c:if test="${!empty actionBean.client.indirectInstruments}">
			<h2>Indirect Instruments:</h2>
			<table class="datatable" width="600">
				<tr>
					<th scope="col">ID</th>
					<th scope="col">Alias</th>
				</tr>
				<c:forEach items="${actionBean.client.indirectInstruments}" var="instrument" varStatus="loop">
					<tr ${loop.index % 2 == 0 ? 'class="even"' : ''}>
						<td><a href="${pageContext.request.contextPath}/instruments/${instrument.sourceId}">${instrument.sourceId}</a></td>
						<td>${instrument.alias}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>

	</stripes:layout-component>
</stripes:layout-render>