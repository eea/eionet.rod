<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Contacts">

	<stripes:layout-component name="contents">
		
		<h1>${rodfn:replaceTags(actionBean.role.description)}</h1>
		<table class="datatable" style="width:50em; max-width:100%">
		  <col style="width:10em"/>
			<col />
			<tr>
				<th scope="row" class="scope-row">CIRCA site</th>
				<td>
					<a href="${rodfn:replaceTags2(actionBean.role.membersUrl,true,true)}">
	    				Additional details for authenticated users
	    			</a>
	    	</td>
			</tr>
			<tr>
				<th scope="row" class="scope-row">Parent role</th>
				<td><a href="contacts?roleId=${actionBean.parentRoleId}">${actionBean.parentRoleId}</a></td>
			</tr>
		</table>
		<c:if test="${!empty actionBean.role.subroles}">
			<table class="datatable" style="width:50em; max-width:100%">
				<tr>
					<th>Subroles</th>
				</tr>
				<c:forEach items="${actionBean.role.subroles}" var="subrole" varStatus="loop">
					<tr class="${loop.index % 2 == 0 ? 'zebraodd' : 'zebraeven'}">
						<td>
							<a href="contacts?roleId=${subrole.id}">${rodfn:replaceTags(subrole.id)} (${rodfn:replaceTags(subrole.description)})</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		<c:if test="${!empty actionBean.role.members}">
			<table class="datatable" style="width:50em; max-width:100%">
				<tr>
					<th>Members</th>
				</tr>
				<c:forEach items="${actionBean.role.members}" var="member" varStatus="loop">
					<tr class="${loop.index % 2 == 0 ? 'zebraodd' : 'zebraeven'}">
						<td>
							<c:if test="${!empty member.fullName}">
								<b>${rodfn:replaceTags(member.fullName)}</b>
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
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	
	</stripes:layout-component>
</stripes:layout-render>
