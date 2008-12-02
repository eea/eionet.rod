<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Documentation">

	<stripes:layout-component name="contents">

        <c:choose>
			<c:when test="${empty actionBean.areaId}">
				<h1>Documentation</h1>
				<c:if test="${!empty actionBean.docList}">
					<ul>
						<c:forEach items="${actionBean.docList}" var="document" varStatus="loop">
							<li>
								<a href="${pageContext.request.contextPath}/documentation/${document.areaId}">${document.description}</a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</c:when>
			<c:otherwise>
				${actionBean.doc.html}
			</c:otherwise>
		</c:choose>

        
	</stripes:layout-component>
</stripes:layout-render>
