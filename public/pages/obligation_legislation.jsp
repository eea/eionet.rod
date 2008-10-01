<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<h1>Legislation for: ${actionBean.obligation.title}</h1>

<table class="datatable">
	<col style="width:30%" />
	<col style="width:70%" />
		<tr>
			<td colspan="2" class="dark_green_heading">Legal framework</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Legislative instrument title</th>
			<td>
				<a href="show.jsv?id=${actionBean.obligation.sourceId}&amp;mode=S">
					<c:choose>
		    			<c:when test="${!empty actionBean.obligation.sourceAlias}">
		    				${actionBean.obligation.sourceAlias}
		    			</c:when>
		    			<c:otherwise>
		    				${actionBean.obligation.sourceTitle}
		    			</c:otherwise>
		    		</c:choose>
				</a>
				<c:if test="${!empty actionBean.obligation.authority}">
					&#160;[${actionBean.obligation.authority}]
				</c:if>
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">Sibling reporting obligations</th>
			<td>
				<ul class="menu">
					<c:forEach items="${actionBean.siblingObligations}" var="obligation" varStatus="loop">
						<li>
							<a href="obligation?id=${obligation.obligationId}">
								${obligation.title}
							</a>
							<c:if test="${!empty obligation.authority}">
								&#160;[${obligation.authority}]
							</c:if>
						</li>
					</c:forEach>
				</ul>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Type of obligation</th>
			<td>
				${actionBean.obligation.lookupCTerm}
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">Countries</th>
			<td>
				<c:forEach items="${actionBean.countries}" var="country" varStatus="loop">
					<a href="countryinfo.jsp?ra-id=${actionBean.id}&amp;spatial=${country.countryId}&amp;member=${country.isMemberCountry}&amp;vol=${country.voluntary}">
						<c:choose>
			    			<c:when test="${country.voluntary == 'Y'}">
			    				<span title="Informal participation in the reporting obligation">${country.name}*</span>
			    			</c:when>
			    			<c:otherwise>
			    				${country.name}
			    			</c:otherwise>
			    		</c:choose>
					</a><c:if test="${!loop.last}">,</c:if>
				</c:forEach>
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">Environmental issues</th>
			<td>
				<c:forEach items="${actionBean.issues}" var="issue" varStatus="loop">
					${issue.name}<c:if test="${!loop.last}">,</c:if>
				</c:forEach>
				&#160;
			</td>
		</tr>
		<tr class="zebraeven">
			<th scope="row" class="scope-row">General comments</th>
			<td>
				${rodfn:replaceTags(actionBean.obligation.comment)}&#160;
			</td>
		</tr>
		<tr class="zebraodd">
			<th scope="row" class="scope-row">DPSIR</th>
			<td>
				<c:if test="${actionBean.obligation.dpsirD == 'yes'}">
					<acronym title="Driving force">D</acronym>&#160;
				</c:if>
				<c:if test="${actionBean.obligation.dpsirP == 'yes'}">
					<acronym title="Pressure">P</acronym>&#160;
				</c:if>
				<c:if test="${actionBean.obligation.dpsirS == 'yes'}">
					<acronym title="State">S</acronym>&#160;
				</c:if>
				<c:if test="${actionBean.obligation.dpsirI == 'yes'}">
					<acronym title="Impact">I</acronym>&#160;
				</c:if>
				<c:if test="${actionBean.obligation.dpsirR == 'yes'}">
					<acronym title="Response">R</acronym>&#160;
				</c:if>
			</td>
		</tr>
</table>

