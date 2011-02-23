<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="${rodfn:replaceTags(actionBean.spatial.name)} - ROD - Eionet" bread2="Spatial" bread2Url="spatial" bread3="Spatial information">
	<stripes:layout-component name="head">
		<link rel="alternate" type="application/rdf+xml" title="RDF" href="${pageContext.request.contextPath}/spatial/${actionBean.idspatial}" />
	</stripes:layout-component>
	<stripes:layout-component name="contents">
		
		<h1>Spatial information</h1>
		
		<table border="0">
			<tr valign="top">
				<th scope="row" align="left" width="160">Spatial ID:</th>
				<td align="left">${rodfn:replaceTags(actionBean.idspatial)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="left" width="160">Spatial name:</th>
				<td align="left">${rodfn:replaceTags(actionBean.spatial.name)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="left" width="160">Spatial type:</th>
				<td align="left">${rodfn:replaceTags(actionBean.spatial.type)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="left" width="160">Spatial twoletter:</th>
				<td align="left">${rodfn:replaceTags(actionBean.spatial.twoletter)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="left" width="160">Is member country:</th>
				<td align="left">${rodfn:replaceTags(actionBean.spatial.isMember)}</td>
			</tr>
		</table>
		<br/>
		<c:if test="${!empty actionBean.obligations}">
			<display:table name="${actionBean.obligations}" class="sortable" pagesize="75" sort="list" id="listItem" htmlId="listItem" requestURI="/spatial" decorator="eionet.rod.web.util.IssueObligationsTableDecorator">
				<display:column property="obligationTitle" title="Reporting obligation" sortable="true" sortProperty="title"/>
			</display:table>
		</c:if>

	</stripes:layout-component>
</stripes:layout-render>