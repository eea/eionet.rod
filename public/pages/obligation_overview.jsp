<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<h1>
	<c:choose>
		<c:when test="${empty actionBean.obligation.title}">
			Reporting Obligation
		</c:when>
		<c:otherwise>
			Reporting obligation for ${rodfn:replaceTags(actionBean.obligation.title)}
		</c:otherwise>
	</c:choose>
</h1>

<table class="datatable">
	<col style="width:30%" />
	<col style="width:70%" />
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Title</th>
			<td>
				<c:choose>
	    			<c:when test="${empty actionBean.obligation.title}">
	    				Reporting Obligation
	    			</c:when>
	    			<c:otherwise>
	    				Reporting obligation for ${rodfn:replaceTags(actionBean.obligation.title)}
	    			</c:otherwise>
	    		</c:choose>
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">Description</th>
			<td>
				${rodfn:replaceTags(actionBean.obligation.description)}
				
				<c:if test="${actionBean.obligation.eeaPrimary == 1}">
					<br/>This reporting obligation is an EIONET Priority Data flow<br/>
				</c:if>
				<c:if test="${actionBean.obligation.eeaCore == 1}">
					<br/>Reporting under this obligation is used for EEA Core set of indicators<br/>
				</c:if>
				<c:if test="${actionBean.obligation.flagged == 1}">
					<br/>This reporting obligation is flagged<br/>
				</c:if>
				<c:if test="${!empty actionBean.obligation.overlapUrl}">
					<br/>Delivery process or content of this obligation overlaps with another reporting obligation (
					<a href="${rodfn:replaceTags2(actionBean.obligation.overlapUrl, true, true)}">${rodfn:replaceTags2(actionBean.obligation.overlapUrl, true, true)}</a>)
				</c:if>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Legislative instrument title</th>
			<td>
				<a href="${pageContext.request.contextPath}/show.jsv?id=${actionBean.obligation.sourceId}&amp;mode=S">
					<c:choose>
		    			<c:when test="${!empty actionBean.obligation.sourceAlias}">
		    				${rodfn:replaceTags(actionBean.obligation.sourceAlias)}
		    			</c:when>
		    			<c:otherwise>
		    				${rodfn:replaceTags(actionBean.obligation.sourceTitle)}
		    			</c:otherwise>
		    		</c:choose>
				</a>	
			</td>
		</tr>
		<tr>
			<td colspan="2" class="dark_green_heading">
				Reporting dates and guidelines
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">National reporting coordinators</th>
			<td>
				<c:choose>
	    			<c:when test="${!empty actionBean.obligation.coordRoleId}">
	    				<a title="Public role information" href="javascript:openCirca('${rodfn:replaceTags2(actionBean.obligation.coordRoleUrl, true, true)}')">
	    					${rodfn:replaceTags(actionBean.obligation.coordRoleName)} (${rodfn:replaceTags(actionBean.obligation.coordRoleId)})
	    				</a><br/>
	    				<a title="Role details on CIRCA for members" href="javascript:openCirca('${rodfn:replaceTags2(actionBean.obligation.coordRoleMembersUrl, true, true)}')">
	    					Additional details for logged-in users
	    				</a>
	    			</c:when>
	    			<c:otherwise>
	    				<c:if test="${!empty actionBean.obligation.coordinatorRole}">
							<div class="role_not_found">Directory role not found for '${rodfn:replaceTags(actionBean.obligation.coordinatorRole)}'</div><br/>
						</c:if>
						${rodfn:replaceTags(actionBean.obligation.coordinator)}
						<a href="${rodfn:replaceTags2(actionBean.obligation.coordinatorUrl, true, true)}">${rodfn:replaceTags2(actionBean.obligation.coordinatorUrl, true, true)}</a>
	    			</c:otherwise>
	    		</c:choose>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">National reporting contacts</th>
			<td>
				<c:choose>
	    			<c:when test="${!empty actionBean.obligation.respRoleId}">
	    				<a title="Public role information" href="javascript:openCirca('${rodfn:replaceTags2(actionBean.obligation.respRoleUrl, true, true)}')">
	    					${rodfn:replaceTags(actionBean.obligation.respRoleName)} (${rodfn:replaceTags(actionBean.obligation.respRoleId)})
	    				</a><br/>
	    				<a title="Role details on CIRCA" href="javascript:openCirca('${rodfn:replaceTags2(actionBean.obligation.respRoleMembersUrl, true, true)}')">
	    					Additional details for logged-in users
	    				</a>
	    			</c:when>
	    			<c:otherwise>
	    				<c:if test="${!empty actionBean.obligation.responsibleRole}">
							<div class="role_not_found">Directory role not found for '${rodfn:replaceTags(actionBean.obligation.responsibleRole)}'</div><br/>
						</c:if>
						${rodfn:replaceTags(actionBean.obligation.nationalContact)}
						<a href="${rodfn:replaceTags2(actionBean.obligation.nationalContactUrl, true, true)}">${rodfn:replaceTags2(actionBean.obligation.nationalContactUrl, true, true)}</a>
	    			</c:otherwise>
	    		</c:choose>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Reporting frequency</th>
			<td>
				<c:choose>
	    			<c:when test="${actionBean.obligation.terminate == 'N'}">
	    				<c:choose>
			    			<c:when test="${actionBean.obligation.reportFreqMonths == 0}">
			    				One time only
			    			</c:when>
			    			<c:when test="${actionBean.obligation.reportFreqMonths == 1}">
			    				Monthly
			    			</c:when>
			    			<c:when test="${actionBean.obligation.reportFreqMonths == 12}">
			    				Annually
			    			</c:when>
			    			<c:when test="${fn:length(actionBean.obligation.nextDeadline) == 0}">
			    				&#160;
			    			</c:when>
			    			<c:otherwise>
			    				Every ${actionBean.obligation.reportFreqMonths} months
			    			</c:otherwise>
			    		</c:choose>
	    			</c:when>
	    			<c:otherwise>
	    				<span class="terminated">terminated</span>
	    			</c:otherwise>
	    		</c:choose>
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">Next report due</th>
			<td>
				<c:choose>
	    			<c:when test="${actionBean.obligation.terminate == 'N'}">
	    				<c:choose>
			    			<c:when test="${fn:length(actionBean.obligation.nextReporting) == 0}">
			    				${actionBean.obligation.nextDeadline}
			    			</c:when>
			    			<c:otherwise>
			    				${actionBean.obligation.nextReporting}
			    			</c:otherwise>
			    		</c:choose>
	    			</c:when>
	    			<c:otherwise>
	    				<span class="terminated">terminated</span>
	    			</c:otherwise>
	    		</c:choose>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Date comments</th>
			<td>
				${rodfn:replaceTags(actionBean.obligation.dateComments)}&#160;
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">Report to</th>
			<td>
				<c:choose>
	    			<c:when test="${!empty actionBean.obligation.clientId}">
						<a href="${pageContext.request.contextPath}/client.jsv?id=${actionBean.obligation.clientId}">
							${rodfn:replaceTags(actionBean.obligation.clientName)}
						</a>
	    			</c:when>
	    			<c:otherwise>
	    				${rodfn:replaceTags(actionBean.obligation.clientName)}
	    			</c:otherwise>
	    		</c:choose>	
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Other clients using this reporting</th>
			<td>
				<c:if test="${fn:length(actionBean.clients) > 0}">
					<ul class="menu">
						<c:forEach items="${actionBean.clients}" var="client" varStatus="loop">
							<li>
								<a href="${pageContext.request.contextPath}/client.jsv?id=${client.clientId}">
									${rodfn:replaceTags(client.name)}
								</a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Reporting guidelines</th>
			<td>
				<c:choose>
	    			<c:when test="${!empty actionBean.obligation.reportFormatUrl}">
						<a href="${rodfn:replaceTags2(actionBean.obligation.reportFormatUrl, true, true)}">
							<c:choose>
				    			<c:when test="${!empty actionBean.obligation.formatName}">
									${rodfn:replaceTags(actionBean.obligation.formatName)}
				    			</c:when>
				    			<c:otherwise>
				    				${rodfn:replaceTags2(actionBean.obligation.reportFormatUrl, true, true)}
				    			</c:otherwise>
				    		</c:choose>
						</a>
	    			</c:when>
	    			<c:otherwise>
	    				${rodfn:replaceTags(actionBean.obligation.formatName)}
	    			</c:otherwise>
	    		</c:choose>		
	    		<c:if test="${!empty actionBean.obligation.validSince}">
					[Valid since ${actionBean.obligation.validSince}]
				</c:if>&#160;
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">Extra information</th>
			<td>
				${rodfn:replaceTags(actionBean.obligation.reportingFormat)}
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Principle repository</th>
			<td>
				<c:choose>
	    			<c:when test="${!empty actionBean.obligation.locationPtr}">
						<a href="${actionBean.obligation.locationPtr}">
							<c:choose>
				    			<c:when test="${!empty actionBean.obligation.locationInfo}">
									${actionBean.obligation.locationInfo}
				    			</c:when>
				    			<c:otherwise>
				    				${actionBean.obligation.locationPtr}
				    			</c:otherwise>
				    		</c:choose>
						</a>
	    			</c:when>
	    			<c:otherwise>
	    				${rodfn:replaceTags(actionBean.obligation.locationInfo)}
	    			</c:otherwise>
	    		</c:choose>&#160;	
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">Data used for</th>
			<td>
				<c:choose>
	    			<c:when test="${!empty actionBean.obligation.dataUsedForUrl}">
						<a href="${actionBean.obligation.dataUsedForUrl}">
							${actionBean.obligation.dataUsedFor}
						</a>
	    			</c:when>
	    			<c:otherwise>
	    				${actionBean.obligation.dataUsedFor}
	    			</c:otherwise>
	    		</c:choose>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Type of information reported</th>
			<td>
				<c:forEach items="${actionBean.infoTypeList}" var="infoType" varStatus="loop">
					${infoType.cterm}<c:if test="${!loop.last}">,</c:if>
				</c:forEach>	
			</td>
		</tr>
</table>

