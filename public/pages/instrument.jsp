<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="${rodfn:replaceTags(actionBean.instrument.sourceAlias)}" bread2="Legislative instruments" bread2Url="instruments" bread3="${rodfn:replaceTags(actionBean.instrument.sourceAlias)}" help="HELP_LI">

	<stripes:layout-component name="contents">
		
		<c:set var="perm_name" value="/instruments/${actionBean.instId}"></c:set>	
	
		<c:if test="${actionBean.isUserLoggedIn}">
			<ul id="dropdown-operations">
				<li><a href="#">Operations</a>
					<ul>
						<c:if test="${rodfn:hasPermission(actionBean.userName,'/obligations','i')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/obligations/new/${actionBean.instId}">New obligation</a></li>
						</c:if>
						<c:if test="${rodfn:hasPermission(actionBean.userName,'/instruments','i')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/instruments/new">New instrument</a></li>
						</c:if>
						<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/instruments/${actionBean.instId}/edit">Edit instrument</a></li>
						</c:if>
						<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'d')}">
							<li>
								<stripes:link href="/instruments" event="delete" onclick="javascript:return confirm('Do you want to delete the current legislative instrument\nwith all related reporting obligations and activities?')">
									Delete instrument
									<stripes:param name="instId" value="${actionBean.instId}"/>
								</stripes:link>
							</li>
						</c:if>
						<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/subscribe?sid=${actionBean.instId}">Subscribe</a></li>
						</c:if>
						<c:if test="${rodfn:hasPermission(actionBean.userName,perm_name,'u')}">
							<li><a class="link-plain" href="${pageContext.request.contextPath}/versions?id=${actionBean.instId}&amp;tab=T_SOURCE&amp;id_field=PK_SOURCE_ID">Show history</a></li>
						</c:if>
						<li><a href="javascript:openHelpList2('${pageContext.request.contextPath}','LI')">Field descriptions</a></li>
					</ul>
				</li>
			</ul>
		</c:if>
		
		<h1>Legislative instrument details: ${rodfn:replaceTags(actionBean.instrument.sourceAlias)}</h1>

		<table class="datatable">
			<col style="width:25%" />
			<col style="width:75%" />
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Classification</th>
					<td>
						<ul class="menu">
							<c:forEach items="${actionBean.instrument.parents}" var="parent" varStatus="loop">
								<li>
									<c:if test="${!empty parent.classificator}">
										${rodfn:replaceTags(parent.classificator)}&#160;
									</c:if>
									<a href="${pageContext.request.contextPath}/instruments?id=${parent.classId}">
										<span class="head0">${rodfn:replaceTags(parent.className)}</span>
									</a>
								</li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">Legal name</th>
					<td>
						<c:choose>
							<c:when test="${!empty actionBean.instrument.sourceUrl}">
								<a href="${rodfn:replaceTags2(actionBean.instrument.sourceUrl, true, true)}">
									${rodfn:replaceTags(actionBean.instrument.sourceTitle)}
								</a>
							</c:when>
							<c:otherwise>
								${rodfn:replaceTags(actionBean.instrument.sourceTitle)}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Short name</th>
					<td>
						${rodfn:replaceTags(actionBean.instrument.sourceAlias)}
					</td>
				</tr>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">CELEX reference</th>
					<td>
						${rodfn:replaceTags(actionBean.instrument.sourceCelexRef)}
					</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Identification number</th>
					<td>
						${rodfn:replaceTags(actionBean.instrument.sourceCode)}
					</td>
				</tr>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">Issued by</th>
					<td>
						<a href="${pageContext.request.contextPath}/clients/${actionBean.instrument.clientId}">
							${rodfn:replaceTags(actionBean.instrument.clientName)}
						</a>
					</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">URL to issuer</th>
					<td>
						<a href="${rodfn:replaceTags2(actionBean.instrument.sourceIssuedByUrl, true, true)}">
							${rodfn:replaceTags2(actionBean.instrument.sourceIssuedByUrlLabel, true, true)}
						</a>
					</td>
				</tr>
				<tr valign="top">
					<th scope="row" class="scope-row">Parent legislative instrument</th>
					<td>
						<a href="${pageContext.request.contextPath}/instruments/${actionBean.instrument.origin.sourceId}">
							<c:choose>
								<c:when test="${!empty actionBean.instrument.origin.sourceAlias}">
									${rodfn:replaceTags(actionBean.instrument.origin.sourceAlias)}
								</c:when>
								<c:otherwise>
									${rodfn:replaceTags(actionBean.instrument.origin.sourceTitle)}
								</c:otherwise>
							</c:choose>
						</a>
					</td>
				</tr>
				<c:if test="${!empty actionBean.instrument.relatedInstruments}">
					<tr valign="top">
						<th scope="row" class="scope-row">Related instrument(s)</th>
						<td>
							<c:forEach items="${actionBean.instrument.relatedInstruments}" var="relatedInstrument" varStatus="loop">
								<a href="${pageContext.request.contextPath}/instruments/${relatedInstrument.sourceId}">
									<c:choose>
										<c:when test="${!empty relatedInstrument.sourceAlias}">
											${rodfn:replaceTags(relatedInstrument.sourceAlias)}
										</c:when>
										<c:otherwise>
											${rodfn:replaceTags(relatedInstrument.sourceTitle)}
										</c:otherwise>
									</c:choose>
								</a>
								<br/>
							</c:forEach>
						</td>
					</tr>
				</c:if>
				<tr class="zebraeven">
					<th scope="row" class="scope-row">Valid from</th>
					<td>
						${rodfn:replaceTags(actionBean.instrument.sourceValidFrom)}
					</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Abstract</th>
					<td>
				      	${rodfn:replaceTags(actionBean.instrument.sourceAbstract)}
					</td>
				</tr>
				<tr class="zebraeven">
					<td colspan="2" class="dark_green_heading">
						Reporting framework
					</td>
				</tr>
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Reporting obligations</th>
					<td>
						<c:if test="${!empty actionBean.instrument.obligations}">
							<ul class="menu">
								<c:forEach items="${actionBean.instrument.obligations}" var="obligation" varStatus="loop">
									<li>
										<a href="${pageContext.request.contextPath}/obligations/${obligation.obligationId}">
											<c:choose>
												<c:when test="${!empty obligation.title}">
													${rodfn:replaceTags(obligation.title)}
												</c:when>
												<c:otherwise>
													Reporting obligation
												</c:otherwise>
											</c:choose>
										</a>
										<c:if test="${!empty obligation.authority}">
											&#160;[${rodfn:replaceTags(obligation.authority)}]
										</c:if>		
									</li>
								</c:forEach>
							</ul>
						</c:if>						
					</td>
				</tr>
				<c:if test="${!empty actionBean.dgenv}">
					<tr class="zebraodd">
						<th scope="row" class="scope-row">DG Env review of reporting theme</th>
						<td>
							${rodfn:replaceTags(actionBean.dgenv)}
						</td>
					</tr>
				</c:if>
				<c:if test="${!empty actionBean.instrument.sourceGeographicScope}">
					<tr class="zebraeven">
						<th scope="row" class="scope-row">Geographic scope</th>
						<td>
							${rodfn:replaceTags(actionBean.instrument.sourceGeographicScope)}
						</td>
					</tr>
				</c:if>	
				<tr class="zebraodd">
					<th scope="row" class="scope-row">Comments</th>
					<td>
				      	${rodfn:replaceTags(actionBean.instrument.sourceComment)}
					</td>
				</tr>
				<c:forEach items="${actionBean.instrument.parents}" var="parent" varStatus="loop">
					<c:if test="${!empty parent.className && parent.className == 'Conventions'}">
						<tr class="zebraeven">
							<th scope="row" class="scope-row">EC entry into force</th>
							<td>
								${rodfn:replaceTags(actionBean.instrument.sourceEcEntryIntoForce)}
							</td>
						</tr>
						<tr class="zebraodd">
							<th scope="row" class="scope-row">EC accession</th>
							<td>
								${rodfn:replaceTags(actionBean.instrument.sourceEcAccession)}
							</td>
						</tr>
						<tr class="zebraeven">
							<th scope="row" class="scope-row">Convention secretariat</th>
							<td>
								<c:choose>
									<c:when test="${!empty actionBean.instrument.sourceSecretariatUrl}">
										<a href="${rodfn:replaceTags2(actionBean.instrument.sourceSecretariatUrl, true, true)}">
											${rodfn:replaceTags(actionBean.instrument.sourceSecretariat)}
										</a>
									</c:when>
									<c:otherwise>
										${rodfn:replaceTags(actionBean.instrument.sourceSecretariat)}
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>	
				</c:forEach>
		</table>
        
	</stripes:layout-component>
</stripes:layout-render>
