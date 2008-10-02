<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Previous Versions - ROD">

	<stripes:layout-component name="contents">

        <h1>Previous Versions</h1>
<c:choose>
	<c:when test="${actionBean.isUserLoggedIn}">
        <form id="form" method="post" action="undo">
	        <display:table name="${actionBean.versions}" class="datatable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/versions" decorator="eionet.rod.web.util.VersionsTableDecorator">
		        <display:caption style="text-align:right;"><input type="submit" name="action" value="Undo selected"/></display:caption>
	        	<display:column property="time" title="Time"/>
				<display:column property="object" title="Object"/>
				<display:column property="oper" title="Operation"/>
				<display:column property="user" title="User"/>
				<display:column property="radio" title=""/>
			</display:table>
		</form>
	</c:when>
	<c:otherwise>
			<display:table name="${actionBean.versions}" class="datatable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/versions" decorator="eionet.rod.web.util.VersionsTableDecorator">
	        	<display:column property="time" title="Time"/>
				<display:column property="object" title="Object"/>
				<display:column property="oper" title="Operation"/>
				<display:column property="user" title="User"/>
			</display:table>
	</c:otherwise>
</c:choose>
	</stripes:layout-component>
</stripes:layout-render>
