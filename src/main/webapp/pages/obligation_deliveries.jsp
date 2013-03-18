<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>

<h1>
	Deliveries for ${rodfn:replaceTags(actionBean.obligation.title)}
</h1>

<display:table name="${actionBean.deliveries}" class="sortable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/obligations/${actionBean.id}/deliveries" decorator="eionet.rod.web.util.CountryDeliveriesTableDecorator" style="width:100%">

	<display:column property="title" title="Delivery Title" sortable="true" sortProperty="deliveryTitle"/>
	<display:column property="date" title="Delivery Date" sortable="true" sortProperty="deliveryUploadDate"/>
	<display:column property="deliveryCoverage" title="Period covered" sortable="true"/>
	<display:column sortProperty="spatialName" title="Country" sortable="true">
	    <stripes:link beanclass="${actionBean.countryDeliveriesActionBeanClass.name}">
	        <stripes:param name="spatialId" value="${listItem.spatialId}"/>
	        <stripes:param name="actDetailsId" value="${actionBean.id}"/>
	        <c:out value="${listItem.spatialName}"/>
	    </stripes:link>
	</display:column>
	<display:column property="deliveryCoverageNote" title="Coverage note" sortable="true"/>

</display:table>