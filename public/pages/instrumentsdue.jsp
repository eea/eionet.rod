<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Legal instruments sorted on next update">

	<stripes:layout-component name="contents">

        <h1>Legal instruments sorted on next update</h1>

        <display:table name="${actionBean.instruments}" class="sortable" sort="list" id="listItem" htmlId="listItem" requestURI="/instrumentsdue" decorator="eionet.rod.web.util.InstrumentsDueTableDecorator">
		
			<display:column property="instrumentId" title="Id" sortable="true"/>
			<display:column property="instrumentTitle" title="Title" sortable="true"/>
			<display:column property="nextUpdate" title="Next Update" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="verified" title="Verified" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="verifiedBy" title="Verified by" sortable="true" decorator="eionet.rod.web.util.ReplaceTagsWrapper"/>
			
		</display:table>

        
	</stripes:layout-component>
</stripes:layout-render>
