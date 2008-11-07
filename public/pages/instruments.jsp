<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Legal instruments sorted on next update">

	<stripes:layout-component name="contents">

        <h1>Legal instruments sorted on next update</h1>

        <display:table name="${actionBean.instruments}" class="sortable" sort="list" id="listItem" htmlId="listItem" requestURI="/instruments">
		
			<display:column property="instrumentId" title="Id" sortable="true"/>
			<display:column property="title" title="Title" sortable="true" href="show.jsv?mode=S" paramId="id" paramProperty="instrumentId" decorator="eionet.rod.web.util.ReplaceTagsWrapper"/>
			<display:column property="nextUpdate" title="Next Update" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="verified" title="Verified" sortable="true" decorator="eionet.rod.web.util.NoneDecorator"/>
			<display:column property="verifiedBy" title="Verified by" sortable="true" decorator="eionet.rod.web.util.ReplaceTagsWrapper"/>
			
		</display:table>

        
	</stripes:layout-component>
</stripes:layout-render>
