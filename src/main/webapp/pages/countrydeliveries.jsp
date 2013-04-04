<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Advanced search">

	<stripes:layout-component name="contents">

        <h1>Status of deliveries<c:if test="${!empty actionBean.spatialName}">: ${actionBean.spatialName}</c:if></h1>

        <table class="datatable">
        	<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName, '/Admin/Harvest', 'v')}">
	        	<tr>
	        		<td></td>
	        		<td><i>last harvested: ${actionBean.deliveryData.obligationLastHarvested}&#160;</i></td>
	        	</tr>
        	</c:if>
			<tr>
				<th scope="row" class="scope-row">Reporting obligation:</th>
				<td>${rodfn:replaceTags(actionBean.deliveryData.obligationTitle)}</td>
			</tr>
			<tr>
				<th scope="row" class="scope-row">Reporting frequency:</th>
				<td>${actionBean.reportFreq}</td>
			</tr>
			<tr>
				<th scope="row" class="scope-row">Client organisation:</th>
				<td><a href="clients/${actionBean.deliveryData.clientId}">${rodfn:replaceTags(actionBean.deliveryData.clientName)}</a></td>
			</tr>
			<tr>
				<th scope="row" class="scope-row">Other clients using this reporting:</th>
				<td>
					<c:forEach items="${actionBean.clients}" var="client" varStatus="loop">
						<a href="clients/${client.clientId}">${rodfn:replaceTags(client.name)}</a><br/>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<th scope="row" class="scope-row">Reporting guidelines:</th>
				<td><a href="${rodfn:replaceTags2(actionBean.deliveryData.obligationReportFormatUrl, true, true)}">${rodfn:replaceTags(actionBean.deliveryData.obligationFormatName)}</a></td>
			</tr>
		</table>


        <display:table name="${actionBean.deliveries}" class="sortable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/countrydeliveries" decorator="eionet.rod.web.util.CountryDeliveriesTableDecorator" style="width:100%">

			<display:column property="title" title="Delivery Title" sortable="true" sortProperty="deliveryTitle"/>
			<display:column property="date" title="Delivery Date" sortable="true" sortProperty="deliveryUploadDate"/>
			<display:column property="deliveryCoverage" title="Period covered" sortable="true"/>
			<display:column property="deliveryCoverageNote" title="Coverage note" sortable="true"/>
			<c:if test="${actionBean.allCountries}">
				<display:column property="spatialName" title="Coverage" sortable="true"/>
			</c:if>

		</display:table>

		<p style="text-align:center">
			Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.<br/>
	        There can be a delay of up to one day before they show up.
		</p>

	</stripes:layout-component>
</stripes:layout-render>
