<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<h1>
	History of ${rodfn:replaceTags(actionBean.obligation.title)}
</h1>

<c:set var="perm_name" value="/obligations/${actionBean.id}"></c:set>
<c:choose>
	<c:when test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
		<form id="form" method="post" action="${pageContext.request.contextPath}/undo">
			<display:table name="${actionBean.versions}" class="datatable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/obligations/${actionBean.id}/history" decorator="eionet.rod.web.util.VersionsTableDecorator">
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
			<display:table name="${actionBean.versions}" class="datatable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/obligations/${actionBean.id}/history" decorator="eionet.rod.web.util.VersionsTableDecorator">
				<display:column property="time" title="Time"/>
				<display:column property="object" title="Object"/>
				<display:column property="oper" title="Operation"/>
				<display:column property="user" title="User"/>
			</display:table>
	</c:otherwise>
</c:choose>
