<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Legislative instruments" bread2="Legislative instruments" bread2Url="instruments" bread3="Hierarchy" help="HELP_HIERARCHY">

	<stripes:layout-component name="contents">
	
		<c:if test="${actionBean.isUserLoggedIn}">
			<ul id="dropdown-operations">
				<li><a href="#">Operations</a>
					<ul>
						<c:if test="${rodfn:hasPermission(actionBean.userName,'/Admin','v')}">
			      			<li><a class="link-plain" href="${pageContext.request.contextPath}/updatehistory?type=S">Deleted records</a></li>
			      		</c:if>
			      		<c:if test="${rodfn:hasPermission(actionBean.userName,'/instruments','i')}">
			      			<li><a class="link-plain" href="${pageContext.request.contextPath}/source.jsv?id=-1">New instrument</a></li>
			      		</c:if>
					</ul>
				</li>
			</ul>
		</c:if>

        <h1>Legislative instruments</h1>
        
        <div id="hierarchy">
			<div class="main">
				<div class="eurlex">(Eur-lex categories)</div>
				<c:if test="${!empty actionBean.hierarchyInstrument.classificator || !empty actionBean.hierarchyInstrument.parentId}">
					<div class="class_name">
						<c:if test="${!empty actionBean.hierarchyInstrument.classificator}">
							${rodfn:replaceTags(actionBean.hierarchyInstrument.classificator)}&#160;
						</c:if>
						<c:if test="${!empty actionBean.hierarchyInstrument.parentId}">
							${rodfn:replaceTags(actionBean.hierarchyInstrument.className)}<br/>
							<a href="${pageContext.request.contextPath}/instruments?id=${actionBean.hierarchyInstrument.parentId}">One level up</a>
						</c:if>
					</div>
				</c:if>
				${actionBean.hierarchyTree}
				<ul class="topcategory">
					<c:forEach items="${actionBean.hierarchyInstruments}" var="instrument" varStatus="loop">
						<li>
							<a href="${pageContext.request.contextPath}/instruments/${instrument.sourceId}">
								<c:choose>
					    			<c:when test="${!empty instrument.sourceAlias}">
										<span class="normal_weight">${rodfn:replaceTags(instrument.sourceAlias)}</span>
					    			</c:when>
					    			<c:otherwise>
					    				<span class="normal_weight">${rodfn:replaceTags(instrument.sourceTitle)}</span>
					    			</c:otherwise>
					    		</c:choose>
							</a>
							<br/>
							<c:choose>
				    			<c:when test="${!empty instrument.sourceUrl}">
									<a href="${rodfn:replaceTags2(instrument.sourceUrl, true, true)}">Link to legal text</a>
				    			</c:when>
				    			<c:otherwise>
				    				Link to legal text
				    			</c:otherwise>
				    		</c:choose>
				    		<br/>
				    		<c:if test="${!empty instrument.parentSourceId}">
				    			Parent legislative instrument: 
				    			<a href="${pageContext.request.contextPath}/instruments/${instrument.parentSourceId}">
									<c:choose>
						    			<c:when test="${!empty instrument.parentSourceAlias}">
											<span class="normal_weight">${rodfn:replaceTags(instrument.parentSourceAlias)}</span>
						    			</c:when>
						    			<c:otherwise>
						    				<span class="normal_weight">${rodfn:replaceTags(instrument.parentSourceTitle)}</span>
						    			</c:otherwise>
						    		</c:choose>
								</a>
				    		</c:if>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
			 
	</stripes:layout-component>
</stripes:layout-render>
