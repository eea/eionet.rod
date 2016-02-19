<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Database Content Statistics">

	<stripes:layout-component name="contents">
	
        <h1>Database Content Statistics</h1>
        <c:out value="${actionBean.totalRa}"/> Reporting Obligation records (last update: <c:out value="${actionBean.lastUpdateRa}"/>)<br/>
        <c:out value="${actionBean.totalLi}"/> Legislative Instrument records (last update: <c:out value="${actionBean.lastUpdateLi}"/>)<br/>
        <br/>
        <table id="analysis" style="width:90%">
	<col style="width:70%"/>
	<col style="width:7%"/>
	<col style="width:15%"/>
	<col style="width:8%"/>
			<tr>
				<td>Number of reporting obligations used for the EEA Core set of indicators</td>
				<td class="center"><c:out value="${actionBean.eeaCore}"/></td>
				<td class="center">
					<c:if test="${actionBean.eeaCore > 0}">
						<a href="obligations?anmode=C">Show list</a>
					</c:if>
				</td>
				<td class="center">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_EEACORE')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr class="zebraeven">
				<td>Number of reporting obligations used for the Eionet core data flows</td>
				<td class="center"><c:out value="${actionBean.eeaPriority}"/></td>
				<td class="center">
					<c:if test="${actionBean.eeaPriority > 0}">
						<a href="obligations?anmode=P">Show list</a>
					</c:if>
				</td>
				<td class="center">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_EIONETPRIORITY')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr>
				<td>Number of reporting obligations where the delivery process or content overlaps with another reporting obligation</td>
				<td class="center"><c:out value="${actionBean.overlapRa}"/> </td>
				<td class="center">
					<c:if test="${actionBean.overlapRa > 0}">
						<a href="obligations?anmode=O">Show list</a>
					</c:if>
				</td>
				<td class="center">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_OVERLAPPING')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr class="zebraeven">
				<td>Number of reporting obligations flagged&#160;</td>
				<td class="center"><c:out value="${actionBean.flaggedRa}"/></td>
				<td class="center">
					<c:if test="${actionBean.flaggedRa > 0}">
						<a href="obligations?anmode=F">Show list</a>
					</c:if>
				</td>
				<td class="center">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_FLAGGED')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr>
				<td>Most recent ROD updates</td>
				<td class="center">100</td>
				<td class="center">
						<a href="updatehistory">Show list</a>
				</td>
				<td class="center">
					&nbsp;
				</td>
			</tr>
			<tr class="zebraeven">
				<td>Legal instruments sorted on next update</td>
				<td class="center"><c:out value="${actionBean.instrumentsDue}"/></td>
				<td class="center">
					<c:if test="${actionBean.instrumentsDue > 0}">
						<a href="instrumentsdue">Show list</a>
					</c:if>
				</td>
				<td class="center">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>Obligations due for update</td>
				<td class="center"><c:out value="${actionBean.totalRa}"/></td>
				<td class="center">
					<c:if test="${actionBean.totalRa > 0}">
						<a href="obligationsdue">Show list</a>
					</c:if>
				</td>
				<td class="center">
					&nbsp;
				</td>
			</tr>
			<tr class="zebraeven">
				<td>No issue allocated</td>
				<td class="center"><c:out value="${actionBean.noIssue}"/></td>
				<td class="center">
					<c:if test="${actionBean.noIssue > 0}">
						<a href="obligations?anmode=NI">Show list</a>
					</c:if>
				</td>
				<td class="center">
					&nbsp;
				</td>
			</tr>
		</table>
        
	</stripes:layout-component>
</stripes:layout-render>
