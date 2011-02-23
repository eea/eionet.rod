<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Country deadlines">

	<stripes:layout-component name="contents">
	
        <h1>Country deadlines</h1>
        <p>
		This part of ROD helps countries co-ordinate and manage their international
		reporting obligations. It provides information about when countries have to
		report, who is responsible for reporting, and to which organisation the data
		set should be delivered. It is geared towards EEA member countries. You can
		browse national deliveries by choosing a country below or query the contents
		of ROD and CDR by using the advanced search. 
		</p>
        <table cellspacing="0" style="width: 650px;">
			<colgroup span="3" width="33%"/>
			<tr valign="top">
				<th align="left" colspan="3"><b>EEA member countries</b></th>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" style="border-left: #008080 1px solid; border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
					<c:forEach items="${actionBean.memberCountries}" begin="0" end="${actionBean.membersCount1 - 1}" var="country" varStatus="loop">
							<img src="images/Folder_icon.gif" alt=""/>
							<a href="deadlines?spatialId=${country.countryId}&amp;order=NEXT_REPORTING,DEADLINE">${rodfn:replaceTags(country.name)}</a>
							<c:if test="${!loop.last}">
								<br/>
							</c:if>
					</c:forEach>
				</td>
				<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
					<c:forEach items="${actionBean.memberCountries}" begin="${actionBean.membersCount1}" end="${actionBean.membersCount2 - 1}" var="country" varStatus="loop">
							<img src="images/Folder_icon.gif" alt=""/>
							<a href="deadlines?spatialId=${country.countryId}&amp;order=NEXT_REPORTING,DEADLINE">${rodfn:replaceTags(country.name)}</a>
							<c:if test="${!loop.last}">
								<br/>
							</c:if>
					</c:forEach>
				</td>
				<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="top">
					<c:forEach items="${actionBean.memberCountries}" begin="${actionBean.membersCount2}" var="country" varStatus="loop">
							<img src="images/Folder_icon.gif" alt=""/>
							<a href="deadlines?spatialId=${country.countryId}&amp;order=NEXT_REPORTING,DEADLINE">${rodfn:replaceTags(country.name)}</a>
							<c:if test="${!loop.last}">
								<br/>
							</c:if>
					</c:forEach>
				</td>
			</tr>
			<tr valign="top">
				<th align="left" colspan="3"><b>Other countries </b></th>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" style="border-left: #008080 1px solid; border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
					<c:forEach items="${actionBean.nonMemberCountries}" begin="0" end="${actionBean.nonMembersCount1 - 1}" var="country" varStatus="loop">
							<img src="images/Folder_icon.gif" alt=""/>
							<a href="deadlines?spatialId=${country.countryId}&amp;order=NEXT_REPORTING,DEADLINE">${rodfn:replaceTags(country.name)}</a>
							<c:if test="${!loop.last}">
								<br/>
							</c:if>
					</c:forEach>
				</td>
				<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
					<c:forEach items="${actionBean.nonMemberCountries}" begin="${actionBean.nonMembersCount1}" end="${actionBean.nonMembersCount2 - 1}" var="country" varStatus="loop">
							<img src="images/Folder_icon.gif" alt=""/>
							<a href="deadlines?spatialId=${country.countryId}&amp;order=NEXT_REPORTING,DEADLINE">${rodfn:replaceTags(country.name)}</a>
							<c:if test="${!loop.last}">
								<br/>
							</c:if>
					</c:forEach>
				</td>
				<td bgcolor="#FFFFFF" style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="top">
					<c:forEach items="${actionBean.nonMemberCountries}" begin="${actionBean.nonMembersCount2}" var="country" varStatus="loop">
							<img src="images/Folder_icon.gif" alt=""/>
							<a href="deadlines?spatialId=${country.countryId}&amp;order=NEXT_REPORTING,DEADLINE">${rodfn:replaceTags(country.name)}</a>
							<c:if test="${!loop.last}">
								<br/>
							</c:if>
					</c:forEach>
				</td>
			</tr>
		</table>
        
	</stripes:layout-component>
</stripes:layout-render>
