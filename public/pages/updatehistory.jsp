<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Most recent ROD updates">

	<stripes:layout-component name="contents">

        <h1>Most recent ROD updates</h1>
		
		<display:table name="${actionBean.list}" class="sortable" sort="list" id="listItem" htmlId="listItem" requestURI="/updatehistory" decorator="eionet.rod.web.util.UpdateHistoryTableDecorator">
			<display:column title="#">
				<c:out value="${listItem_rowNum}"/>
	    	</display:column>
			<display:column property="desc" title="Description" sortable="true"/>
			<display:column property="object" title="Object" sortable="true"/>
			<display:column property="user" title="User" sortable="true"/>
			<display:column property="oper" title="Action type" sortable="true"/>
			<display:column property="time" title="Log time" sortable="true"/>
		</display:table>
		
	</stripes:layout-component>
</stripes:layout-render>
