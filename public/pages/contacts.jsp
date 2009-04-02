<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Contacts">

	<stripes:layout-component name="contents">
		
		<h1>${rodfn:replaceTags(actionBean.roleDescription)}</h1>
		<table class="datatable" width="700">
			<tr>
				<th colspan="2">
					<a title="Role details on CIRCA for members" href="${rodfn:replaceTags2(actionBean.roleUrl,true,true)}">
	    				Additional details for logged-in users
	    			</a>
	    		</th>
			</tr>
			<tr>
				<th scope="row" class="scope-row">Parent role</th>
				<td><a href="contacts?roleId=${actionBean.parentRoleId}">${actionBean.parentRoleId}</a></td>
			</tr>
		</table>
		<c:if test="${!empty actionBean.subroles}">
			<table class="datatable" width="700">
				<tr>
					<th colspan="2">Subroles</th>
				</tr>
				<c:forEach items="${actionBean.subroles}" var="subrole" varStatus="loop">
					<tr>
						<th width="250">
							<a href="contacts?roleId=${subrole.id}">${rodfn:replaceTags(subrole.description)}</a>
						</th>
						<td>
							<c:forEach items="${subrole.members}" var="member" varStatus="loop">
								<c:if test="${!empty member.fullName}">
									${rodfn:replaceTags(member.fullName)} 
								</c:if>
								<c:if test="${!empty member.mail}">
									<a href="mailto:${member.mail}">${member.mail}</a>
								</c:if>
								<c:if test="${!empty member.description && member.description != ' '}">
									<br/>${rodfn:replaceTags(member.description)}
								</c:if>
								<c:if test="${!empty member.phone}">
									<br/>Tel: ${member.phone} 
								</c:if>
								<c:if test="${!empty member.fax}">
									Fax: ${member.fax}
								</c:if>
					    		<c:if test="${!empty member.organisation.name && !empty member.organisation.url}">
					    			<br/><a href="${rodfn:replaceTags2(member.organisation.url,true,true)}">${rodfn:replaceTags(member.organisation.name)}</a>
					    		</c:if>
					    		<c:if test="${!empty member.organisation.name && empty member.organisation.url}">
					    			<br/>${rodfn:replaceTags(member.organisation.name)}
					    		</c:if>
								<br/><br/>
							</c:forEach>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	
	</stripes:layout-component>
</stripes:layout-render>