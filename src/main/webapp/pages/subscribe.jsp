<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Subscribe to notifications - ROD">

	<stripes:layout-component name="contents">
		
		<h1>Get notifications in your email</h1>
		<div class="note-msg">
			<strong>Note</strong>
			<p>This will make an additional subscription even if you have subscribed before.
					To change or delete your existing subscriptions,
					go to the <a href="${actionBean.mySubscriptionUrl}">Unified Notification Service (UNS)</a>.</p>
		</div>
		
		<stripes:form action="/subscribe" method="post">
			<table class="formtable" style="border: 1px solid #008080" cellpadding="2" cellspacing="0" width="600" border="0">
				<tr>
					<td scope="row">
						<strong>My interests:</strong>
					</td>
					<td>
						<stripes:checkbox name="eventType" value="Approaching deadline" id="deadlines"/><label for="deadlines">Approaching deadlines</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<stripes:checkbox name="eventType" value="Obligation change" id="changes"/><label for="changes">Changes to obligations</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<stripes:checkbox name="eventType" value="New obligation" id="newobl"/><label for="newobl">New obligations</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<stripes:checkbox name="eventType" value="Instrument change" id="changes_inst"/><label for="changes_inst">Changes to instruments</label>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<stripes:checkbox name="eventType" value="New instrument" id="newinst"/><label for="newinst">New instrument</label>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Country:</strong>
					</td>
					<td>
						<stripes:select name="selectedCountry"> 
							<stripes:option value="" label="All countries"/>
		    				<c:forEach items="${actionBean.countries}" var="country" varStatus="loop">
		    					<stripes:option value="${country.name}" label="${country.name}"/>
		    				</c:forEach>
					  	</stripes:select>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Issue:</strong>
					</td>
					<td>
						<stripes:select name="selectedIssue"> 
							<stripes:option value="" label="All issues"/>
		    				<c:forEach items="${actionBean.issues}" var="issue" varStatus="loop">
		    					<stripes:option value="${rodfn:threeDots(issue.name,80)}" label="${rodfn:threeDots(issue.name,80)}"/>
		    				</c:forEach>
					  	</stripes:select>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Organisation:</strong>
					</td>
					<td>
						<c:choose>
							<c:when test="${!empty actionBean.id}">
								${actionBean.clientName}
								<stripes:hidden name="selectedClient" value="${actionBean.clientName}"/>
							</c:when>
							<c:otherwise>
								<stripes:select name="selectedClient"> 
									<stripes:option value="" label="All organisations"/>
				    				<c:forEach items="${actionBean.clients}" var="client" varStatus="loop">
				    					<c:set var="fullName" value="${client.acronym} - ${client.name}"></c:set>
				    					<c:choose>
											<c:when test="${!empty client.acronym}">
												<stripes:option value="${fullName}" label="${rodfn:threeDots(fullName,80)}"/>
											</c:when>
											<c:otherwise>
												<stripes:option value="${client.name}" label="${rodfn:threeDots(client.name,80)}"/>
											</c:otherwise>
										</c:choose>
				    				</c:forEach>
							  	</stripes:select>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Obligation:</strong>
					</td>
					<td>
						<c:choose>
							<c:when test="${!empty actionBean.id}">
								${rodfn:replaceTags(actionBean.obligationName)}
								<stripes:hidden name="selectedObligation" value="${actionBean.obligationName}"/>
							</c:when>
							<c:otherwise>
								<stripes:select name="selectedObligation"> 
									<stripes:option value="" label="All obligations"/>
				    				<c:forEach items="${actionBean.obligations}" var="obligation" varStatus="loop">
				    					<stripes:option value="${obligation}" label="${rodfn:threeDots(obligation,80)}"/>
				    				</c:forEach>
							  	</stripes:select>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td scope="row">
						<strong>Instrument:</strong>
					</td>
					<td>
						<c:choose>
							<c:when test="${!empty actionBean.sid}">
								${rodfn:replaceTags(actionBean.instrumentName)}
								<stripes:hidden name="selectedInstrument" value="${actionBean.instrumentName}"/>
							</c:when>
							<c:otherwise>
								<stripes:select name="selectedInstrument"> 
									<stripes:option value="" label="All instruments"/>
				    				<c:forEach items="${actionBean.instruments}" var="instrument" varStatus="loop">
				    					<stripes:option value="${instrument}" label="${rodfn:threeDots(instrument,80)}"/>
				    				</c:forEach>
							  	</stripes:select>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<stripes:submit name="subscribe" value="Subscribe"/>  
					</td>
				</tr>
			</table>
		</stripes:form>

	</stripes:layout-component>
</stripes:layout-render>