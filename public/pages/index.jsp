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
								<stripes:option value="-1" label="Any country"/>
			    				<stripes:options-collection collection="${actionBean.formCountries}" label="name" value="countryId"/>
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
			    				<stripes:options-collection collection="${actionBean.formIssues}" label="name" value="issueId"/>
							</stripes:select>
						</td>
					</tr>
	
					<tr>
						<td valign="middle" align="left" bgcolor="#FFFFFF"><b>Organisation</b></td>
						<td style="border-left: 1px solid #C0C0C0">
							<stripes:select name="client" style="color: #000000; font-size: 9pt; width:300px" size="1">
								<stripes:option value="-1" label="Any organisation"/>
								<stripes:options-collection collection="${actionBean.formClients}" label="name" value="clientId"/>
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
		
		<!-- SiteSearch Google -->
		<br/>
		<form method="get" action="http://search.eionet.europa.eu/search.jsp">
			<input value="rod.eionet.europa.eu" name="qp_site" type="hidden"/>
			<table width="530" style="border: 1px solid #006666">
				<tr>
					<td valign="middle" width="30%">
						<label for="queryfld" style="font-weight:bold">Search ROD website:</label>
					</td>
					<td valign="middle">
						<input type="text" name="query" id="queryfld" size="44" maxlength="255" value=""/>&#160;
						<input type="submit" value="GO" name="GO" class="go_btn"></input>
					</td>
				</tr>
			</table>
		</form>
		<!-- SiteSearch Google -->
		
	</stripes:layout-component>
	
</stripes:layout-render>
