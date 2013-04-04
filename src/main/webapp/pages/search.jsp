<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Advanced search">

	<stripes:layout-component name="contents">

        <h1>Advanced search</h1>
        <stripes:form action="/deadlines" method="get">
			<table cellspacing="0" cellpadding="2" class="main">
					 <tr>
							<td colspan="2" width="94%" bgcolor="#FFFFFF" class="head">
								<span style="float:right">
									<a href="javascript:openViewHelp('HELP_CSINDEX_SEARCH')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
								</span>
								<b>Show reporting deadlines</b>
							</td>
					</tr>
					<tr>
						<td width="30%" bgcolor="#FFFFFF" class="lab">
							<stripes:label for="spatialId">For a locality</stripes:label>
						</td>
						<td class="dat">
							<stripes:select name="spatialId" id="spatialId" class="dat" size="1">
								<stripes:option value="" label="Any country or territory"/>
								<c:forEach items="${actionBean.countries}" var="country" varStatus="loop">
			    					<stripes:option value="${country.countryId}" label="${country.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" class="lab">
							<stripes:label for="issueId">For an issue</stripes:label>
						</td>
						<td class="dat">
							<stripes:select name="issueId" id="issueId" class="dat">
								<stripes:option value="" label="All issues"/>
								<c:forEach items="${actionBean.issues}" var="issue" varStatus="loop">
			    					<stripes:option value="${issue.issueId}" label="${issue.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" class="lab">
							<stripes:label for="clientId">For an organisation</stripes:label>
						</td>
						<td class="dat">
							<stripes:select name="clientId" id="clientId" class="dat">
								<stripes:option value="" label="Any organisation"/>
								<c:forEach items="${actionBean.clients}" var="client" varStatus="loop">
			    					<stripes:option value="${client.clientId}" label="${client.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" class="lab"><label for="date1">Next deadline</label></td>
						<td align="left" class="dat">
							<stripes:text name="date1" id="date1" size="10" class="dat" onchange="checkDate(this)" value="dd/mm/yyyy"/>
							<stripes:label for="date2" style="font-weight:bold"> -</stripes:label>
							<stripes:text name="date2" id="date2" size="10" class="dat" onchange="checkDate(this)" value="dd/mm/yyyy"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="right">
							<stripes:submit name="search" value="GO" class="go_btn"/>
						</td>
					</tr>
			</table>
		</stripes:form>

        
	</stripes:layout-component>
</stripes:layout-render>
