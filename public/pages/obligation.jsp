<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Reporting obligation for ${actionBean.obligation.sourcePageTitle}">

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
			</script>
			<form id="f" method="POST" action="activity.jsv">
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
	    				<li><a href="obligation?id=${actionBean.id}&amp;tab=overview">Overview</a></li>
	    			</c:otherwise>
	    		</c:choose>
	    		<c:choose>
	    			<c:when test="${actionBean.tab == 'legislation'}">
	    				<li id="currenttab"><span>Legislation</span></li>
	    			</c:when>
	    			<c:otherwise>
	    				<li><a href="obligation?id=${actionBean.id}&amp;tab=legislation">Legislation</a></li>
	    			</c:otherwise>
	    		</c:choose>
	    		<c:if test="${!empty actionBean.obligation.fkDeliveryCountryIds}">
		    		<c:choose>
		    			<c:when test="${actionBean.tab == 'deliveries'}">
		    				<li id="currenttab"><span>Deliveries</span></li>
		    			</c:when>
		    			<c:otherwise>
		    				<li><a href="obligation?id=${actionBean.id}&amp;tab=deliveries">Deliveries</a></li>
		    			</c:otherwise>
		    		</c:choose>
		    	</c:if>
		    	<c:if test="${!empty actionBean.obligation.parameters}">
		    		<c:choose>
		    			<c:when test="${actionBean.tab == 'parameters'}">
		    				<li id="currenttab"><span>Parameters</span></li>
		    			</c:when>
		    			<c:otherwise>
		    				<li><a href="obligation?id=${actionBean.id}&amp;tab=parameters">Parameters</a></li>
		    			</c:otherwise>
		    		</c:choose>
		    	</c:if>
		    	<c:choose>
	    			<c:when test="${actionBean.tab == 'history'}">
	    				<li id="currenttab"><span>History</span></li>
	    			</c:when>
	    			<c:otherwise>
	    				<li><a href="obligation?id=${actionBean.id}&amp;tab=history">History</a></li>
	    			</c:otherwise>
	    		</c:choose>
			</ul>
		</div>
		<div id="operations">
			<ul>
				<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName,'/obligations','i')}">
					<li><a href="activity.jsv?id=-1&amp;aid=${actionBean.obligation.fkSourceId}">New obligation</a></li>
				</c:if>
				<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
					<li><a href="activity.jsv?id=${actionBean.id}&amp;aid=${actionBean.obligation.fkSourceId}">Edit obligation</a></li>
				</c:if>
				<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName,perm_name,'d')}">
					<li><a href="javascript:delActivity()">Delete obligation</a></li>
				</c:if>
				<c:if test="${actionBean.isUserLoggedIn}">
					<li><a href="javascript:openHelpList('RO')">Field descriptions</a></li>
				</c:if>
				<c:if test="${actionBean.isUserLoggedIn && rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
					<li><a href="subscribe.jsp?id=${actionBean.id}">Subscribe</a></li>
				</c:if>
			</ul>
		</div>
		
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
