<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Harvest">

	<stripes:layout-component name="contents">
		
		<h1>Harvest deliveries, roles and parameters</h1>
		<c:choose>
			<c:when test="${actionBean.isUserLoggedIn}">
				<ul>
					<li><a href="history">Show harvesting history</a></li>
				</ul>
				<h2>Select the data you want to be harvested</h2>
				<stripes:form action="/harvester" onsubmit="javascript:setwait(this)" method="post" name="f">
					<div>
						<stripes:radio name="mode" value="0"/>All
						<stripes:radio name="mode" value="1"/>Deliveries
						<stripes:radio name="mode" value="2"/>Roles
						<stripes:radio name="mode" value="3"/>Parameters
						
						<stripes:submit name="harvest" value="Harvest" class="btn"/>
					</div>
				</stripes:form>
				<div class="note-msg">
					<strong>Note</strong>
					<p>It will take some to harvest the data. Please be patient</p>
				</div>
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