<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="EEA - Reporting Obligations Database" colClass="threecolumn">

	<stripes:layout-component name="rightColumn">	
		${actionBean.twoBoxes}
	</stripes:layout-component>

	<stripes:layout-component name="contents">
		${actionBean.introduction}
		<br/>
		
		<stripes:form action="/obligations" method="get" id="x1" class="notprintable">
			<table id="categorysearch">
			<col style="width: 30%"/>
			<col style="width: 65%"/>
			<col style="width: 5%"/>
					 <tr>
							<td colspan="2">
								<b>Show reporting obligations</b>
							</td>
							<td class="center">
								<a href="javascript:openViewHelp('HELP_SEARCH1')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
							</td>
					</tr>
					<tr>
						<td>
								<label class="question">Country</label>
						</td>
						<td>
							<stripes:select name="country" size="1">
								<stripes:option value="-1" label="Any country"/>
			    				<c:forEach items="${actionBean.formCountries}" var="country" varStatus="loop">
			    					<stripes:option value="${country.countryId}" label="${country.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
						<td rowspan="4" style="vertical-align:middle">
							<stripes:hidden name="id"/>
							<stripes:submit name="filter" value="GO" class="go_btn"/>
						</td>
					</tr>
	
					<tr>
						<td><label class="question">Issue</label></td>
						<td>
							<stripes:select name="issue" style="width:223px">
								<stripes:option value="-1" label="All issues"/>
			    				<c:forEach items="${actionBean.formIssues}" var="issue" varStatus="loop">
			    					<stripes:option value="${issue.issueId}" label="${issue.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
					</tr>
	
					<tr>
						<td><label class="question">Organisation</label></td>
						<td>
							<stripes:select name="client" style="width:300px" size="1">
								<stripes:option value="-1" label="Any organisation"/>
								<c:forEach items="${actionBean.formClients}" var="client" varStatus="loop">
			    					<stripes:option value="${client.clientId}" label="${client.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
					</tr>
					<tr>
						<td>&#160;</td>
						<td>
							<stripes:checkbox name="terminated" id="isterminated" value="Y"/>
							<label for="isterminated">Include terminated obligations</label>
						</td>
					</tr>
			</table>
		</stripes:form>
		
		<!-- SiteSearch Google -->
		<br/>
		<form method="get" action="http://search.eionet.europa.eu/search.jsp">
			<input value="rod.eionet.europa.eu" name="qp_site" type="hidden"/>
			<table width="530" style="border: 1px solid #006666">
				<tr>
					<td style="vertical-align:middle" width="30%">
						<label for="queryfld" style="font-weight:bold">Search ROD website:</label>
					</td>
					<td style="vertical-align:middle">
						<input type="text" name="query" id="queryfld" size="44" maxlength="255" value=""/>&#160;
						<input type="submit" value="GO" name="GO" class="go_btn"></input>
					</td>
				</tr>
			</table>
		</form>
		<!-- SiteSearch Google -->
		
	</stripes:layout-component>
	
</stripes:layout-render>
