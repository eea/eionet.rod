<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Previous Actions">

	<stripes:layout-component name="contents">
		
		<h1>Undo information</h1>
		<c:choose>
			<c:when test="${actionBean.isUserLoggedIn}">
				<table class="datatable" width="50%">
					<thead>
						<tr>
							<th scope="col" class="scope-col">Time</th>
							<th scope="col" class="scope-col">Operation</th>
							<th scope="col" class="scope-col">User</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${actionBean.date}</td>
							<td>${actionBean.action}</td>
							<td>${actionBean.user}</td>
						</tr>
					</tbody>
				</table>
				<br/>
			   	<table class="datatable" width="100%">
					<thead>
						<tr>
							<th scope="col" class="scope-col">Table</th>
							<th scope="col" class="scope-col">Sub#</th>
							<th scope="col" class="scope-col">Column</th>
							<th scope="col" class="scope-col">Undo Value</th>
							<c:if test="${actionBean.op != 'D' && actionBean.op != 'UD' && actionBean.op != 'UDD'}">
								<th scope="col" class="scope-col">Current Value</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${actionBean.undoList}" var="undo" varStatus="loop">
							<tr class="${loop.index % 2 == 0 ? 'zebraodd' : 'zebraeven'}">
								<td>${undo.tabel}</td>
								<td>${undo.subTransNr}</td>
								<td>${undo.column}</td>
								<td>${rodfn:replaceTags2(undo.value, true, true)}</td>
								<c:if test="${actionBean.op != 'D' && actionBean.op != 'UD' && actionBean.op != 'UDD'}">
									<td ${undo.diff ? '' : 'style="background-color:#FFFFCC"'}>${rodfn:replaceTags2(undo.currentValue, true, true)}</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br/>
				<b>Countries reporting formally</b>
				<table class="datatable" width="100%">
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<thead>
						<tr>
							<th scope="col" class="scope-col">Undo Countries</th>
							<th scope="col" class="scope-col">Current Countries</th>
							<th scope="col" class="scope-col">Added Countries</th>
							<th scope="col" class="scope-col">Removed Countries</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${actionBean.diffCountries.undo}</td>
							<td>${actionBean.diffCountries.current}</td>
							<td style="background-color:#CCFFCC">${actionBean.diffCountries.added}</td>
							<td style="background-color:#FFCCCC">${actionBean.diffCountries.removed}</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<b>Countries reporting voluntarily</b>
				<table class="datatable" width="100%">
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<thead>
						<tr>
							<th scope="col" class="scope-col">Undo Countries</th>
							<th scope="col" class="scope-col">Current Countries</th>
							<th scope="col" class="scope-col">Added Countries</th>
							<th scope="col" class="scope-col">Removed Countries</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${actionBean.diffVolCountries.undo}</td>
							<td>${actionBean.diffVolCountries.current}</td>
							<td style="background-color:#CCFFCC">${actionBean.diffVolCountries.added}</td>
							<td style="background-color:#FFCCCC">${actionBean.diffVolCountries.removed}</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<b>Environmental issues</b>
				<table class="datatable" width="100%">
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<thead>
						<tr>
							<th scope="col" class="scope-col">Undo Issues</th>
							<th scope="col" class="scope-col">Current Issues</th>
							<th scope="col" class="scope-col">Added Issues</th>
							<th scope="col" class="scope-col">Removed Issues</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${actionBean.diffIssues.undo}</td>
							<td>${actionBean.diffIssues.current}</td>
							<td style="background-color:#CCFFCC">${actionBean.diffIssues.added}</td>
							<td style="background-color:#FFCCCC">${actionBean.diffIssues.removed}</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<b>Other clients using this reporting</b>
				<table class="datatable" width="100%">
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<thead>
						<tr>
							<th scope="col" class="scope-col">Undo Clients</th>
							<th scope="col" class="scope-col">Current Clients</th>
							<th scope="col" class="scope-col">Added Clients</th>
							<th scope="col" class="scope-col">Removed Clients</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${actionBean.diffClients.undo}</td>
							<td>${actionBean.diffClients.current}</td>
							<td style="background-color:#CCFFCC">${actionBean.diffClients.added}</td>
							<td style="background-color:#FFCCCC">${actionBean.diffClients.removed}</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<b>Type of info reported</b>
				<table class="datatable" width="100%">
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<col style="width:25%"/>
					<thead>
						<tr>
							<th scope="col" class="scope-col">Undo Info Types</th>
							<th scope="col" class="scope-col">Current Info Types</th>
							<th scope="col" class="scope-col">Added Info Typse</th>
							<th scope="col" class="scope-col">Removed Info Types</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${actionBean.diffInfo.undo}</td>
							<td>${actionBean.diffInfo.current}</td>
							<td style="background-color:#CCFFCC">${actionBean.diffInfo.added}</td>
							<td style="background-color:#FFCCCC">${actionBean.diffInfo.removed}</td>
						</tr>
					</tbody>
				</table>
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