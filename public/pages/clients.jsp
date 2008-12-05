<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Clients">

	<stripes:layout-component name="contents">

        <h1>Clients</h1>

        <display:table name="${actionBean.clients}" class="sortable" sort="list" id="listItem" htmlId="listItem" requestURI="/clients" decorator="eionet.rod.web.util.ClientsTableDecorator">
		
			<display:column property="clientName" title="Client name" sortable="true"/>
			<display:column property="acronym" title="Client acronym" sortable="true" decorator="eionet.rod.web.util.ReplaceTagsWrapper"/>
			
		</display:table>

        
	</stripes:layout-component>
</stripes:layout-render>
