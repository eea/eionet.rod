<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<h1>
	Products for ${rodfn:replaceTags(actionBean.obligation.title)}
</h1>

<c:choose>
    <c:when test="${not empty actionBean.products && not empty actionBean.products.rows}">
        <display:table name="${actionBean.products.rows}" class="sortable" defaultsort="2" defaultorder="descending" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/obligations/${actionBean.id}/products" decorator="eionet.rod.web.util.ProductsTableDecorator" style="width:100%">
            <display:column property="title" title="Title" sortable="true" sortProperty="titleLabel"/>
            <display:column property="published" title="Published" sortable="true" format="{0,date,yyyy-MM-dd}"/>
        </display:table>
    </c:when>
    <c:otherwise>
        <div class="system-msg">No products found for this obligation</div>
    </c:otherwise>
</c:choose>