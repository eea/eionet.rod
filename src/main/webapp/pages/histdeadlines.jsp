<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Historical Deadlines">

	<stripes:layout-component name="contents">
		
		<h1>Historical Deadlines</h1>
		<c:choose>
			<c:when test="${actionBean.isUserLoggedIn}">
				<stripes:form action="/histdeadlines" method="post" name="f">
					<table width="710" cellspacing="0" border="0">
						<tr>
							<td class="headline" align="left">
								Search
							</td>
						</tr>
						<tr bgcolor="#cbdcdc">
							<td>
								<stripes:label for="startDate" class="question">Start date</stripes:label>
								<stripes:text name="startDate" id="startDate" size="10" class="date" maxlength="10"/><span class="input-hint">(dd/mm/yyyy)</span>
								<stripes:label for="endDate" class="question">End date</stripes:label>
								<stripes:text name="endDate" id="endDate" size="10" class="date" maxlength="10"/><span class="input-hint">(dd/mm/yyyy)</span>
								<stripes:submit name="init" value="Search" class="btn"/>
							</td>
						</tr>
					</table>
					<br/>
					<c:if test="${!empty actionBean.deadlines}">
						<display:table name="${actionBean.deadlines}" class="datatable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/histdeadlines" decorator="eionet.rod.web.util.HistDeadlinesTableDecorator">
				        	<display:column property="title" title="Reporting Obligation" sortable="true" sortProperty="obligationTitle"/>
							<display:column property="deadline" title="Deadline" sortable="true"/>
						</display:table>	
					</c:if>
				</stripes:form>				
			</c:when>
			<c:otherwise>
				<div class="error-msg">
					Not authenticated! Please verify that you are logged in (for security reasons,
					the system will log you out after a period of inactivity). If the problem persists, please
					contact the server administrator.
				</div>
			</c:otherwise>
		</c:choose>

	</stripes:layout-component>
</stripes:layout-render>