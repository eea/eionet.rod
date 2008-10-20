<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="${actionBean.obligation.title} - ROD - Eionet" bread2="Obligations" bread2Url="rorabrowse.jsv?mode=A" bread3="${actionBean.obligation.title}">

	<stripes:layout-component name="contents">
		
		<c:set var="perm_name" value="/obligations/${actionBean.id}"></c:set>	
	
		<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName,perm_name,'d')}">
			<script type="text/javascript">
				function delActivity() {
					if (confirm("Do you want to delete the reporting obligation?")){
						var u = window.location.href;
						document.forms["f"].elements["/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/REDIRECT_URL"].value = u;
						document.forms["f"].submit();
					}
				}
				
				function openFieldDescriptions(mode){
					var url = "${pageContext.request.contextPath}/helplist.jsv?mode=" + mode;
					var name = "Help";
					var features = "location=no, menubar=no, width=730, height=480, top=100, left=200, scrollbars=yes, resizable=yes";
					var w = window.open(url,name,features);
					w.focus();
				}
			</script>
			<form id="f" method="POST" action="${pageContext.request.contextPath}/activity.jsv">
				<input type="hidden" name="dom-update-mode" value="D"/>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/PK_RA_ID" value="${actionBean.id}"/>
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID" value="${actionBean.obligation.sourceId}">
				<input type="hidden" name="/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/REDIRECT_URL" value=""></input>
			</form>
		</c:if>

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
			<div id="operations">
				<ul>
					<c:if test="${rodfn:hasPermission(actionBean.userName,'/obligations','i')}">
						<li><a href="${pageContext.request.contextPath}/activity.jsv?id=-1&amp;aid=${actionBean.obligation.fkSourceId}">New obligation</a></li>
					</c:if>
					<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
						<li><a href="${pageContext.request.contextPath}/activity.jsv?id=${actionBean.id}&amp;aid=${actionBean.obligation.fkSourceId}">Edit obligation</a></li>
					</c:if>
					<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'d')}">
						<li><a href="javascript:delActivity()">Delete obligation</a></li>
					</c:if>
					<li><a href="javascript:openFieldDescriptions('RO')">Field descriptions</a></li>
					<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
						<li><a href="${pageContext.request.contextPath}/subscribe.jsp?id=${actionBean.id}">Subscribe</a></li>
					</c:if>
				</ul>
			</div>
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
