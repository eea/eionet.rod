<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="${rodfn:replaceTags(actionBean.name)} - ROD - Eionet" bread2="Issues" bread2Url="issues" bread3="Issue information">

	<stripes:layout-component name="contents">
		
		<h1>Issue information</h1>
		
		<table border="0">
			<tr valign="top">
				<th scope="row" align="left" width="100">Issue ID:</th>
				<td align="left">${rodfn:replaceTags(actionBean.idissue)}</td>
			</tr>
			<tr valign="top">
				<th scope="row" align="left" width="100">Issue name:</th>
				<td align="left">${rodfn:replaceTags(actionBean.name)}</td>
			</tr>
		</table>
		<br/>
		<c:if test="${!empty actionBean.obligations}">
			<display:table name="${actionBean.obligations}" class="sortable" pagesize="75" sort="list" id="listItem" htmlId="listItem" requestURI="/issues" decorator="eionet.rod.web.util.IssueObligationsTableDecorator">
				<display:column property="obligationTitle" title="Reporting obligation" sortable="true" sortProperty="title"/>
			</display:table>
		</c:if>

	</stripes:layout-component>
</stripes:layout-render>