<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Advanced search">

	<stripes:layout-component name="contents">

        <h1>Reporting overview<c:if test="${!empty actionBean.spatialName}">: ${actionBean.spatialName}</c:if>
        </h1>
        
        <stripes:form action="/csmain" method="get">
        	<table cellspacing="0" cellpadding="3" width="600" border="0">
				<tr>
					<td valign="middle" width="30%" class="select_issue">Select issue:</td>
					<td valign="middle" width="20%" class="select_deadline">Select deadline:</td>
					<td valign="middle" width="40%" class="select_client">Select client:</td>
					<td valign="middle" width="10%" class="help_btn">
						<p align="right">
							<a href="javascript:openViewHelp('HELP_CSMAIN1')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
						</p>
					</td>
				</tr>
				<tr>
					<td valign="middle" class="issues">
						<stripes:select name="issueId" class="issues" size="1">
							<stripes:option value="0" label="All issues"/>
		    				<stripes:options-collection collection="${actionBean.issues}" label="name" value="issueId"/>
						</stripes:select>
					</td>
					<td valign="middle" class="deadlines">
						<stripes:select name="deadlines" class="deadlines" size="1">
							<stripes:option value="0" label="All deadlines"/>
		    				<stripes:option value="1" label="In the next month"/>
		    				<stripes:option value="2" label="In the next 3 months"/>
		    				<stripes:option value="3" label="In the next 6 months"/>
		    				<stripes:option value="4" label="Previous months"/>
						</stripes:select>
					</td>
					<td valign="middle" class="client">
						<stripes:select name="clientId" class="client" size="1">
							<stripes:option value="" label="All clients"/>
							<stripes:options-collection collection="${actionBean.clients}" label="name" value="clientId"/>
						</stripes:select>
					</td>
					<td valign="middle" align="right" class="go_btn">
						<stripes:hidden name="spatialId"/>
						<stripes:submit name="search" value="GO" class="go_btn"/>
					</td>
				</tr>
			</table>
        </stripes:form>
        <div class="smallfont" style="font-size: 10pt; font-weight: normal">The list includes also recently passed deadlines, until 10% of the time difference between last deadline<br/>and next deadline has passed - 3 days for a monthly deadline, 36 days for a yearly deadline etc.</div><br/>
        <display:table name="${actionBean.searchList}" class="sortable" pagesize="50" sort="list" id="listItem" htmlId="listItem" requestURI="/cssearch" decorator="eionet.rod.web.util.CSMainTableDecorator" style="width:100%">
		
			<display:column property="title" title="Reporting obligation" sortable="true" sortProperty="obligationTitle"/>
			<display:column property="client" title="Reporting to" sortable="true" sortProperty="clientDescr"/>
			<display:column property="deadline" title="Deadline" sortable="true" sortProperty="obligationDeadline"/>
			<display:column property="role" title="Responsible" sortable="true" sortProperty="roleDescr"/>
			<display:column property="hasDelivery" title="Deliveries" sortable="true" sortProperty="obligationHasDelivery"/>
			
		</display:table>
		
		<p style="text-align:center">
			Note: This page currently only shows deliveries made to the Reportnet Central Data Repository.<br/>
	        There can be a delay of up to one day before they show up.
		</p>
        
	</stripes:layout-component>
</stripes:layout-render>