<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Database Content Statistics">

	<stripes:layout-component name="contents">
	
        <h1>Database Content Statistics</h1>
        <c:out value="${actionBean.totalRa}"/> Reporting Obligation records (last update: <c:out value="${actionBean.lastUpdateRa}"/>)<br/>
        <c:out value="${actionBean.totalLi}"/> Legislative Instrument records (last update: <c:out value="${actionBean.lastUpdateLi}"/>)<br/>
        <br/>
        <table cellpadding="5" cellspacing="0" width="584" style="border: #c0c0c0 1px solid">  
			<tr>
				<td width="70%" class="border_right">Number of reporting obligations used for the EEA Core set of indicators</td>
				<td width="7%" align="center" class="border_right"><c:out value="${actionBean.eeaCore}"/></td>
				<td width="15%" align="center">
					<c:if test="${actionBean.eeaCore > 0}">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=C">Show list</a>
					</c:if>
				</td>
				<td align="center" class="border_left">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_EEACORE')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr bgcolor="#f6f6f6">
				<td class="border_right">Number of reporting obligations used for the EIONET Priority Data flows</td>
				<td align="center" class="border_right"><c:out value="${actionBean.eeaPriority}"/></td>
				<td align="center">
					<c:if test="${actionBean.eeaPriority > 0}">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=P">Show list</a>
					</c:if>
				</td>
				<td align="center" class="border_left">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_EIONETPRIORITY')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr>
				<td class="border_right">Number of reporting obligations where the delivery process or content overlaps with another reporting obligation</td>
				<td align="center" class="border_right"><c:out value="${actionBean.overlapRa}"/> </td>
				<td align="center">
					<c:if test="${actionBean.overlapRa > 0}">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=O">Show list</a>
					</c:if>
				</td>
				<td align="center" class="border_left">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_OVERLAPPING')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr bgcolor="#f6f6f6">
				<td class="border_right">Number of reporting obligations flagged&#160;</td>
				<td align="center" class="border_right"><c:out value="${actionBean.flaggedRa}"/></td>
				<td align="center">
					<c:if test="${actionBean.flaggedRa > 0}">
						<a href="rorabrowse.jsv?mode=A&amp;anmode=F">Show list</a>
					</c:if>
				</td>
				<td align="center" class="border_left">
					<a href="javascript:openViewHelp('HELP_ANALYSIS_FLAGGED')"><img src="images/info_icon.gif" alt="Show help" border="0"/></a>
				</td>
			</tr>
			<tr>
				<td class="border_right">100 most recent ROD updates</td>
				<td align="center" class="border_right">100</td>
				<td align="center">
						<a href="updatehistory">Show list</a>
				</td>
				<td align="center" class="border_left">
					&nbsp;
				</td>
			</tr>
		</table>
        
	</stripes:layout-component>
</stripes:layout-render>
