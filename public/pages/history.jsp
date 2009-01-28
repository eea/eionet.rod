<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Harvesting history">

	<stripes:layout-component name="contents">
	
        <h1>Harvesting history</h1>

        <display:table name="${actionBean.list}" class="sortable" sort="list" id="listItem" htmlId="listItem" requestURI="/history">
		
			<display:column property="timestamp" title="Time"/>
			<display:column value="Execute" title="Action"/>
			<display:column property="user" title="User"/>
			<display:column property="description" title="Description" decorator="eionet.rod.web.util.ReplaceTagsWrapper"/>
			
		</display:table>

        
	</stripes:layout-component>
</stripes:layout-render>
