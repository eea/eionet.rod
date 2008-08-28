<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Advanced search">

	<stripes:layout-component name="contents">

        <h1>Advanced search</h1>
        <stripes:form action="/cssearch" method="get">
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
							<stripes:label for="spatialId">For a country</stripes:label>
						</td>
						<td class="dat">
							<stripes:select name="spatialId" id="spatialId" class="dat" style="width:223" size="1">
								<stripes:option value="" label="Any country"/>
								<stripes:options-collection collection="${actionBean.countries}" label="name" value="countryId"/>
							</stripes:select>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" class="lab">
							<stripes:label for="issueId">For an issue</stripes:label>
						</td>
						<td class="dat">
							<stripes:select name="issueId" id="issueId" class="dat" style="width:223">
								<stripes:option value="" label="All issues"/>
								<stripes:options-collection collection="${actionBean.issues}" label="name" value="issueId"/>
							</stripes:select>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" class="lab">
							<stripes:label for="clientId">For an organisation</stripes:label>
						</td>
						<td class="dat">
							<stripes:select name="clientId" id="clientId" class="dat" style="width:350">
								<stripes:option value="" label="Any organisation"/>
								<stripes:options-collection collection="${actionBean.clients}" label="name" value="clientId"/>
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
