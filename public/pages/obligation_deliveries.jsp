<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<h1>
	Deliveries for ${actionBean.obligation.title}
</h1>

<display:table name="${actionBean.deliveries}" class="sortable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/countrydeliveries" decorator="eionet.rod.web.util.CountryDeliveriesTableDecorator" style="width:100%">
		
	<display:column property="contact" title="Contact" sortable="true" sortProperty="roleName"/>
	<display:column property="title" title="Delivery Title" sortable="true" sortProperty="deliveryTitle"/>
	<display:column property="date" title="Delivery Date" sortable="true" sortProperty="deliveryUploadDate"/>
	<display:column property="deliveryCoverage" title="Period covered" sortable="true"/>
	<display:column property="spatialName" title="Country" sortable="true"/>
	
</display:table>