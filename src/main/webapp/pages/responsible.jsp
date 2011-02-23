<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Responsible role">

	<stripes:layout-component name="contents">
		
		<h1>Responsible role</h1>
		<table class="datatable">
		<c:if test="${!empty actionBean.responsibleRole.name}">
			<tr>
				<th scope="row" class="scope-row">Role name</th>
				<td>${rodfn:replaceTags(actionBean.responsibleRole.name)}</td>
			</tr>
		</c:if>
		<c:if test="${!empty actionBean.responsibleRole.email}">
			<tr>
				<th scope="row" class="scope-row">E-mail</th>
				<td>${rodfn:replaceTags(actionBean.responsibleRole.email)}</td>
			</tr>
		</c:if>
		<c:if test="${!empty actionBean.responsibleRole.roleUrl}">
			<tr>
				<th scope="row" class="scope-row">Role URL</th>
				<td>${rodfn:replaceTags2(actionBean.responsibleRole.roleUrl, false, false)}</td>
			</tr>
		</c:if>
		<c:if test="${!empty actionBean.responsibleRole.membersUrl}">
			<tr>
				<th scope="row" class="scope-row">Role members URL</th>
				<td>${rodfn:replaceTags2(actionBean.responsibleRole.membersUrl, false, false)}</td>
			</tr>
		</c:if>
		<c:if test="${!empty actionBean.responsibleRole.occupants}">
			<tr>
				<th scope="row" class="scope-row">Persons</th>
				<td>
					<c:forEach items="${actionBean.responsibleRole.occupants}" var="occupant" varStatus="loop">
						${rodfn:replaceTags(occupant.person)}<br/>
					</c:forEach>
				</td>
			</tr>
		</c:if>
		<tr>
			<th scope="row" class="scope-row">Last harvested</th>
			<td>${actionBean.responsibleRole.lastHarvested}</td>
		</tr>
		<c:if test="${!empty actionBean.responsibleRole.obligations}">
			<tr>
				<th scope="row" class="scope-row">Obligations</th>
				<td>
					<c:forEach items="${actionBean.responsibleRole.obligations}" var="obligation" varStatus="loop">
						<a href="obligations/${obligation.obligationId}">${rodfn:replaceTags(obligation.title)}</a><br/>
					</c:forEach>
				</td>
			</tr>
		</c:if>
	</table>

	</stripes:layout-component>
</stripes:layout-render>