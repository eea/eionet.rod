<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="${rodfn:replaceTags(actionBean.obligation.title)} - ROD - Eionet" bread2="Obligations" bread2Url="obligations" bread3="${rodfn:replaceTags(actionBean.obligation.title)}" desc="${rodfn:replaceTags2(actionBean.obligation.description,true,true)}">
	<stripes:layout-component name="head">
		<link rel="alternate" type="application/rdf+xml" title="RDF" href="${pageContext.request.contextPath}/obligations/${actionBean.id}" />
	</stripes:layout-component>
	<stripes:layout-component name="contents">
		
		<c:set var="perm_name" value="/obligations/${actionBean.id}"></c:set>	
	
        <div id="tabbedmenu">
			<ul>
				<c:choose>
	    			<c:when test="${empty actionBean.tab || actionBean.tab == 'overview'}">
	    				<li id="currenttab"><span>Overview</span></li>
	    			</c:when>
	    			<c:otherwise>
	    				<li><a href="${pageContext.request.contextPath}/obligations/${actionBean.id}/overview">Overview</a></li>
	    			</c:otherwise>
	    		</c:choose>
	    		<c:choose>
	    			<c:when test="${actionBean.tab == 'legislation'}">
	    				<li id="currenttab"><span>Legislation</span></li>
	    			</c:when>
	    			<c:otherwise>
	    				<li><a href="${pageContext.request.contextPath}/obligations/${actionBean.id}/legislation">Legislation</a></li>
	    			</c:otherwise>
	    		</c:choose>
	    		<c:if test="${!empty actionBean.obligation.fkDeliveryCountryIds}">
		    		<c:choose>
		    			<c:when test="${actionBean.tab == 'deliveries'}">
		    				<li id="currenttab"><span>Deliveries</span></li>
		    			</c:when>
		    			<c:otherwise>
		    				<li><a href="${pageContext.request.contextPath}/obligations/${actionBean.id}/deliveries">Deliveries</a></li>
		    			</c:otherwise>
		    		</c:choose>
		    	</c:if>
		    	<c:if test="${actionBean.productsExist}">
    	    		<c:choose>
    	    			<c:when test="${actionBean.tab == 'products'}">
    	    				<li id="currenttab"><span>Products</span></li>
    	    			</c:when>
    	    			<c:otherwise>
    	    				<li><a href="${pageContext.request.contextPath}/obligations/${actionBean.id}/products">Products</a></li>
    	    			</c:otherwise>
    	    		</c:choose>
	    		</c:if>
		    	<c:if test="${!empty actionBean.obligation.parameters}">
		    		<c:choose>
		    			<c:when test="${actionBean.tab == 'parameters'}">
		    				<li id="currenttab"><span>Parameters</span></li>
		    			</c:when>
		    			<c:otherwise>
		    				<li><a href="${pageContext.request.contextPath}/obligations/${actionBean.id}/parameters">Parameters</a></li>
		    			</c:otherwise>
		    		</c:choose>
		    	</c:if>
		    	<c:choose>
	    			<c:when test="${actionBean.tab == 'history'}">
	    				<li id="currenttab"><span>History</span></li>
	    			</c:when>
	    			<c:otherwise>
	    				<li><a href="${pageContext.request.contextPath}/obligations/${actionBean.id}/history">History</a></li>
	    			</c:otherwise>
	    		</c:choose>
			</ul>
		</div>
		<c:if test="${actionBean.isUserLoggedIn}">
			<ul id="dropdown-operations">
				<li><a href="#">Operations</a>
					<ul>
			      		<c:if test="${rodfn:hasPermission(actionBean.userName,'/obligations','i')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/obligations/new/${actionBean.obligation.fkSourceId}">New obligation</a></li>
						</c:if>
						<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/obligations/${actionBean.id}/edit">Edit obligation</a></li>
						</c:if>
						<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'d')}">
							<li>
				      			<stripes:link href="/obligations" event="delete" onclick="javascript:return confirm('Do you want to delete the reporting obligation?')">
									Delete obligation
									<stripes:param name="id" value="${actionBean.id}"/>
								</stripes:link>
							</li>
						</c:if>
						<li><a href="javascript:openHelpList2('${pageContext.request.contextPath}','RO')">Field descriptions</a></li>
						<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/subscribe?id=${actionBean.id}">Subscribe</a></li>
						</c:if>
					</ul>
				</li>
			</ul>
		</c:if>
		
		<c:choose>
			<c:when test="${empty actionBean.tab || actionBean.tab == 'overview'}">
				<jsp:include page="/pages/obligation_overview.jsp"/>
			</c:when>
			<c:when test="${actionBean.tab == 'legislation'}">
				<jsp:include page="/pages/obligation_legislation.jsp"/>
			</c:when>
			<c:when test="${actionBean.tab == 'deliveries'}">
				<jsp:include page="/pages/obligation_deliveries.jsp"/>
			</c:when>
			<c:when test="${actionBean.tab == 'products'}">
				<jsp:include page="/pages/obligation_products.jsp"/>
			</c:when>
			<c:when test="${actionBean.tab == 'history'}">
				<jsp:include page="/pages/obligation_history.jsp"/>
			</c:when>
			<c:when test="${actionBean.tab == 'parameters'}">
				<jsp:include page="/pages/obligation_parameters.jsp"/>
			</c:when>
			<c:otherwise>
				Not found
			</c:otherwise>
		</c:choose>
        
	</stripes:layout-component>
</stripes:layout-render>
