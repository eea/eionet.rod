<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<h1>Parameters for ${actionBean.obligation.title}</h1>

<c:if test="${!empty actionBean.obligation.parameters}">
	<table class="datatable">
		<tr>
			<th scope="col" class="scope-col" style="text-align: left">Parameters</th>
		</tr>
		<tr>
			<td>
				${rodfn:replaceTags(actionBean.obligation.parameters)}
			</td>
		</tr>
	</table>
</c:if>
<c:if test="${fn:length(actionBean.ddparameters) > 0}">
	<h2>Parameters from Data Dictionary</h2>
	<table class="datatable">
		<col style="width:40%"/>
		<col style="width:30%"/>
		<col style="width:30%"/>
		<tr>
			<th scope="col" class="scope-col">Parameter name</th>
			<th scope="col" class="scope-col">Table name</th>
			<th scope="col" class="scope-col">Dataset name</th>
		</tr>
		<c:forEach items="${actionBean.ddparameters}" var="param" varStatus="loop">
			<td>
				<a href="${param.elementUrl}" title="View parameter details in Data Dictionary">
					${param.elementName}
				</a>
			</td>
			<td>${param.tableName}</td>
			<td>${param.datasetName}</td>
		</c:forEach>
	</table>
	<br/>
</c:if>