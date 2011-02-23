<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Country Information">

	<stripes:layout-component name="contents">
		
		<h1>${rodfn:replaceTags(actionBean.countryInfo.country)}</h1>
		
		<table class="datatable">
	   		<col style="width:30%" />
			<col style="width:70%" />
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Obligation</th>
				<td>
					${rodfn:replaceTags(actionBean.countryInfo.obligationTitle)}
				</td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Country</th>
				<td>
					${rodfn:replaceTags(actionBean.countryInfo.country)}
				</td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Status</th>
				<td>
					<c:choose>
		    			<c:when test="${actionBean.vol == 'Y'}">
		    				Voluntary
		    			</c:when>
		    			<c:otherwise>
		    				Formal
		    			</c:otherwise>
		    		</c:choose>
				</td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Participation Period</th>
				<td>
					${actionBean.countryInfo.start} to ${actionBean.countryInfo.end}
				</td>
			</tr>
			<tr class="zebraodd">
				<th scope="row" class="scope-row">Responsible Role</th>
				<td>
					<c:if test="${!empty actionBean.countryInfo.role}">
						<a href="responsible?role=${actionBean.countryInfo.role}&amp;member=${actionBean.member}&amp;spatial=${actionBean.countryInfo.twoLetter}">
							${actionBean.countryInfo.role}-${actionBean.rt}-${actionBean.countryInfo.twoLetter}
						</a>
					</c:if>
				</td>
			</tr>
			<tr class="zebraeven">
				<th scope="row" class="scope-row">Deliveries</th>
				<td>
					<c:forEach items="${actionBean.countryInfo.deliveries}" var="delivery" varStatus="loop">
						<a href="${rodfn:replaceTags2(delivery.url, true, true)}">${rodfn:replaceTags(delivery.title)}</a><br/>
					</c:forEach>
					<c:if test="${!empty actionBean.countryInfo.deliveries}">
						<p style="text-align:center">
							Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.<br/>
					        There can be a delay of up to one day before they show up.
						</p>
					</c:if>
				</td>
			</tr>
		</table>

	</stripes:layout-component>
</stripes:layout-render>