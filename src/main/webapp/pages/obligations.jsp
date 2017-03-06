<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Reporting obligations">

	<stripes:layout-component name="contents">
	
		<c:if test="${actionBean.isUserLoggedIn}">
			<ul id="dropdown-operations">
				<li><a href="#">Operations</a>
					<ul>
			      		<li><a class="link-plain" href="updatehistory?type=O">Show deleted</a></li>
			      		<li><a class="link-plain" href="instruments?mode=X">New obligation</a></li>
					</ul>
				</li>
			</ul>
		</c:if>

        <h1>Reporting obligations
	        <c:if test="${!empty actionBean.anmode}">: <c:if test="${actionBean.anmode == 'P'}">
    				Eionet core data flows
    			</c:if>
    			<c:if test="${actionBean.anmode == 'C'}">
    				EEA Core set of indicators
    			</c:if>
    			<c:if test="${actionBean.anmode == 'O'}">
    				Delivery process or content overlaps with another obligation
    			</c:if>
    			<c:if test="${actionBean.anmode == 'F'}">
    				Delivery process is managed by EEA
    			</c:if>
    			<c:if test="${actionBean.anmode == 'NI'}">
    				No issue allocated
    			</c:if>
	        </c:if>
	        <c:if test="${!empty actionBean.countryName}">
	        	: ${actionBean.countryName}
	        </c:if>
	        <c:if test="${!empty actionBean.clientName}">
	        	: ${actionBean.clientName} 
	        </c:if>
	        <c:if test="${!empty actionBean.issueName}">
	        	[${actionBean.issueName}]
	        </c:if>
        </h1>
        
        <stripes:form action="/obligations" method="get" id="x1" class="notprintable">
			<table class="formtable" border="0" width="530" cellspacing="0" cellpadding="2"  style="border: 1px solid #008080">
					 <tr>
							<td colspan="2" width="95%" bgcolor="#FFFFFF" style="border-bottom: 1px solid #008080; border-right: 1px solid #C0C0C0">
								<b>Show reporting obligations</b>
							</td>
							<td bgcolor="#FFFFFF" align="center" style="border-bottom: 1px solid #008080; border-right: 1px solid #C0C0C0">
								<a href="javascript:openViewHelp('HELP_SEARCH1')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
							</td>
					</tr>
					<tr>
						<td valign="top" bgcolor="#FFFFFF">
								<b>Country</b>
						</td>
						<td style="border-left: 1px solid #C0C0C0">
							<stripes:select name="country" style="color: #000000; font-size: 9pt; width:223px" size="1">
								<stripes:option value="-1" label="Any country or territory"/>
			    				<c:forEach items="${actionBean.formCountries}" var="country" varStatus="loop">
			    					<stripes:option value="${country.countryId}" label="${country.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
						<td rowspan="4" valign="middle" style="border-left: 1px solid #C0C0C0">
							<stripes:hidden name="id"/>
							<stripes:submit name="filter" value="GO" class="go_btn"/>
						</td>
					</tr>
	
					<tr>
						<td align="left" bgcolor="#FFFFFF"><b>Issue</b></td>
						<td style="border-left: 1px solid #C0C0C0">
							<stripes:select name="issue" style="font-size: 9pt; color: #000000; width:223px">
								<stripes:option value="-1" label="All issues"/>
								<stripes:option value="NI" label="No issue allocated"/>
			    				<c:forEach items="${actionBean.formIssues}" var="issue" varStatus="loop">
			    					<stripes:option value="${issue.issueId}" label="${issue.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
					</tr>
	
					<tr>
						<td valign="middle" align="left" bgcolor="#FFFFFF"><b>Organisation</b></td>
						<td style="border-left: 1px solid #C0C0C0">
							<stripes:select name="client" style="color: #000000; font-size: 9pt; width:300px" size="1">
								<stripes:option value="-1" label="Any organisation"/>
								<c:forEach items="${actionBean.formClients}" var="client" varStatus="loop">
			    					<stripes:option value="${client.clientId}" label="${client.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">&#160;</td>
						<td valign="middle" align="left" style="border-left: 1px solid #C0C0C0" bgcolor="#FFFFFF">
							<stripes:checkbox name="terminated" id="isterminated" value="Y"/>
							<b><label for="isterminated">Include terminated obligations</label></b>
						</td>
					</tr>
			</table>
		</stripes:form>
        
        <c:if test="${!empty actionBean.obligations}">
	        <display:table name="${actionBean.obligations}" class="sortable" pagesize="150" sort="list" id="listItem" htmlId="listItem" requestURI="/obligations" decorator="eionet.rod.web.util.ObligationsTableDecorator" style="width:100%">
			
				<display:column property="title" title="Reporting obligation" sortable="true" sortProperty="obligationTitle"/>
				<display:column property="instrument" title="Legislative instrument" sortable="true" sortProperty="sourceTitle"/>
				<display:column property="client" title="Report to" sortable="true" sortProperty="clientDescr"/>
				<display:column property="deadline" title="Deadline" sortable="true"/>
				<c:if test="${actionBean.anmode == 'P'}">
					<display:column property="deliveries" title="Deliveries" sortable="true"/>
				</c:if>
				
			</display:table>
		</c:if>
		<br/><br/>
		<c:if test="${!empty actionBean.indirectObligations}">
			<display:table name="${actionBean.indirectObligations}" class="sortable" pagesize="150" sort="list" id="listItem2" htmlId="listItem2" requestURI="/obligations" decorator="eionet.rod.web.util.ObligationsTableDecorator" style="width:100%">
				<display:caption><span class="head1">Indirect reporting obligations</span></display:caption>
				<display:column property="title" title="Reporting obligation" sortable="true" sortProperty="obligationTitle"/>
				<display:column property="instrument" title="Legislative instrument" sortable="true" sortProperty="sourceTitle"/>
				<display:column property="client" title="Report to" sortable="true" sortProperty="clientDescr"/>
				<display:column property="deadline" title="Deadline" sortable="true"/>
				<c:if test="${actionBean.anmode == 'P'}">
					<display:column property="deliveries" title="Deliveries" sortable="true"/>
				</c:if>
				
			</display:table>
		</c:if>
	</stripes:layout-component>
</stripes:layout-render>
