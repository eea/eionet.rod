<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Legislative instruments" bread2="Legislative instruments" bread2Url="instruments?mode=X" bread3="Hierarchy" help="HELP_HIERARCHY_ADD">

	<stripes:layout-component name="contents">
	
        <h1>Select legislative instrument to add reporting obligation to</h1>
        
        <div id="hierarchy">
			<div class="main">
				<c:if test="${!empty actionBean.hierarchyInstrument.classificator || !empty actionBean.hierarchyInstrument.parentId}">
					<div class="class_name">
						<c:if test="${!empty actionBean.hierarchyInstrument.classificator}">
							${rodfn:replaceTags(actionBean.hierarchyInstrument.classificator)}&#160;
						</c:if>
						<c:if test="${!empty actionBean.hierarchyInstrument.parentId}">
							<a href="${pageContext.request.contextPath}/instruments?id=${actionBean.hierarchyInstrument.parentId}&amp;mode=X">
								${rodfn:replaceTags(actionBean.hierarchyInstrument.className)}
							</a>
						</c:if>
					</div>
				</c:if>
				${actionBean.hierarchyTree}
				<ul class="topcategory">
					<c:forEach items="${actionBean.hierarchyInstruments}" var="instrument" varStatus="loop">
						<li>
							<a href="${pageContext.request.contextPath}/eobligation?id=-1&amp;aid=${instrument.sourceId}">
								<c:choose>
					    			<c:when test="${!empty instrument.sourceAlias}">
										<span class="normal_weight">${rodfn:replaceTags(instrument.sourceAlias)}</span>
					    			</c:when>
					    			<c:otherwise>
					    				<span class="normal_weight">${rodfn:replaceTags(instrument.sourceTitle)}</span>
					    			</c:otherwise>
					    		</c:choose>
							</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		
	</stripes:layout-component>
	
</stripes:layout-render>