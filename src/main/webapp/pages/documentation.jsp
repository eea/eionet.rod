<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<c:choose>
	<c:when test="${empty actionBean.areaId}">
		<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Documentation">
			<stripes:layout-component name="contents">
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
			</stripes:layout-component>
		</stripes:layout-render>
	</c:when>
	<c:otherwise>
		<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Documentation" bread2="Documentation" bread2Url="documentation" bread3="${rodfn:replaceTags(actionBean.doc.description)}">
			<stripes:layout-component name="contents">
				${actionBean.doc.html}
			</stripes:layout-component>
		</stripes:layout-render>
	</c:otherwise>
</c:choose>
