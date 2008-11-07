<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Obligations due for update">

	<stripes:layout-component name="contents">
	
        <h1>Obligations due for update</h1>
        
        <display:table name="${actionBean.obligations}" class="sortable" sort="list" id="listItem" htmlId="listItem" requestURI="/obligationsdue" decorator="eionet.rod.web.util.ObligationsDueTableDecorator" style="width:100%">
		
			<display:column property="id" title="Id" sortable="true" sortProperty="obligationId"/>
			<display:column property="lastUpdate" title="Last update" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="validatedBy" title="Updated by" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="nextUpdate" title="Next update" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="verified" title="Verified" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="verifiedBy" title="Verified by" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
        	<display:column property="title" title="Title" sortable="true" sortProperty="obligationTitle"/>
        	<display:column property="issues" title="Issues"/>
			
		</display:table>
		
	</stripes:layout-component>
</stripes:layout-render>
