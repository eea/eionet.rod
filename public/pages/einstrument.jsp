<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>

<c:set var="title" value="Edit a Legislative Instrument"></c:set>
<c:if test="${!empty actionBean.instId && actionBean.instId == 'new'}">
	<c:set var="title" value="Create a Legislative Instrument"></c:set>
</c:if>
<stripes:layout-render name="/pages/common/template.jsp" pageTitle="${title}" help="HELP_LI" unLoad="true">

	<stripes:layout-component name="contents">

		<h1>${title}</h1>
		
		<stripes:form action="/instruments" method="post" name="f">
			<stripes:hidden name="instId"/>
			<fieldset><legend>Identification</legend>
				<table class="formtable">
	           		<col class="labelcol"/>
	           		<col class="entrycol"/>
				<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_LEGALNAME')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceTitle" class="required question">Legal name</stripes:label>
						</td>
						<td>
							<stripes:textarea name="instrument.sourceTitle" cols="55" rows="5" style="height:5em" onchange="checkMdText(this); changed()" class="full" id="instrument.sourceTitle"/>
							<stripes:errors field="instrument.sourceTitle"/>
						</td>
				</tr>
				<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_ALIASNAME')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceAlias" class="question">Short name</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceAlias" size="68" onchange="changed()" class="full" id="instrument.sourceAlias"/>
						</td>
				</tr>
				<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_IDENTIFICATIONNUMBER')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceCode" class="question">Identification number</stripes:label>
						</td>
						<td>
							<table border="0" width="100%" cellspacing="0" cellpadding="0">
   	                 			<tr class="zebraeven">
                          			<td width="40%">
                          				<stripes:text name="instrument.sourceCode" size="30" onchange="changed()" id="instrument.sourceCode"/>
                          			</td>
                          			<td width="15%">
                          				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_DRAFT')" title="Help on this field"><span>Help on field</span></a>
                          				<stripes:label for="instrument.sourceDraft" class="question">Draft</stripes:label>
                          			</td>
                          			<td width="45%">
                          				<stripes:radio name="instrument.sourceDraft" id="sourceDraftY" value="Y" /><stripes:label for="sourceDraftY">Yes</stripes:label>
							<stripes:radio name="instrument.sourceDraft" id="sourceDraftN" value="N" /><stripes:label for="sourceDraftN">No</stripes:label>
                          			</td>
                          		</tr>
                          	</table>
						</td>
				</tr>
				<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_URLTOOFFICIALSOURCE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceUrl" class="question">URL to official text</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceUrl" size="68" onchange="chkUrl(this); changed()" class="full" id="instrument.sourceUrl"/>
							<stripes:errors field="instrument.sourceUrl"/>
						</td>
				</tr>
				<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_CELEXREFERENCE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceCelexRef" class="question">CELEX reference</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceCelexRef" size="68" onchange="changed()" class="full" id="instrument.sourceCelexRef"/>
						</td>
				</tr>
				<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_ISSUEDBY')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceFKClientId" class="question">Issued by</stripes:label>
						</td>
						<td>
							<stripes:select name="instrument.sourceFKClientId" onchange="changed()" class="full" id="instrument.sourceFKClientId">
								<stripes:option value="0" label=""/>
			    				<c:forEach items="${actionBean.clients}" var="client" varStatus="loop">
			    					<stripes:option value="${client.clientId}" label="${client.name}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
				</tr>
				<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_URLTOISSUER')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceIssuedByUrl" class="question">URL to issuer</stripes:label>
			                <stripes:errors field="instrument.sourceIssuedByUrl"/>
						</td>
						<td>
							<stripes:text name="instrument.sourceIssuedByUrl" size="68" onchange="chkUrl(this); changed()" class="full" id="instrument.sourceIssuedByUrl"/>
							<stripes:errors field="instrument.sourceIssuedByUrl"/>
						</td>
				</tr>
				<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_PARENTLEGALINSTRUMENT')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="parentInstrumentId" class="question">Parent legislative instrument</stripes:label>
						</td>
						<td>
							<stripes:select name="parentInstrumentId" onchange="changed()" class="full" id="parentInstrumentId">
								<stripes:option value="0" label=""/>
			    				<c:forEach items="${actionBean.parentInstrumentsList}" var="parent" varStatus="loop">
			    					<stripes:option value="${parent.sourceId}" label="${rodfn:threeDots(parent.sourceTitle, 100)}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
				</tr>
				<tr class="zebraeven">
					<td>
		                	<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_CLASSIFICATION')" title="Help on this field"><span>Help on field</span></a>
				      		<label for="selSourceClasses" class="question">Eur-lex categories</label>
					</td>
					<td></td>
				</tr>
				<tr>
						<td colspan="2">
							<table class="fancyselect">
								<tr>
									<td>
										<stripes:select name="selSourceClasses" multiple="true" size="9" class="multiple" id="selSourceClasses"> 
						    				<c:forEach items="${actionBean.sourceClasses}" var="class" varStatus="loop">
						    					<stripes:option value="${class.classId}" label="${class.classificator} ${class.className}"/>
						    				</c:forEach>
									  	</stripes:select>
									</td>
									<td>
										<div class="arrow_btnbox">
											<input type="button" style="width:80" onclick="mvValues(document.f.selSourceClasses.options, document.f.selectedSourceClasses.options, null)" value="&#160;&#160;-&gt;&#160;&#160;"/>
										</div>
										<div class="arrow_btnbox">
											<input type="button" style="width:80" onclick="mvValues(document.f.selectedSourceClasses.options, document.f.selSourceClasses.options, null)" value="&#160;&#160;&lt;-&#160;&#160;"/><br/>
										</div>
									</td>
									<td>
										<stripes:select name="selectedSourceClasses" multiple="true" size="9" class="multiple"> 
						    				<c:forEach items="${actionBean.instrumentSourceClasses}" var="class" varStatus="loop">
						    					<stripes:option value="${class.classId}" label="${class.classificator} ${class.className}"/>
						    				</c:forEach>
									  	</stripes:select>
									</td>
								</tr>
							</table>
						</td>
				</tr>
				<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_DGENVREVIEW')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceDgenvReview" class="question">DG Env review of reporting theme</stripes:label>
						</td>
						<td>
							<stripes:select name="instrument.sourceDgenvReview" onchange="changed()" class="full" id="instrument.sourceDgenvReview">
								<stripes:option value="0" label=""/>
			    				<c:forEach items="${actionBean.dgenvlist}" var="dgenv" varStatus="loop">
			    					<stripes:option value="${dgenv.cvalue}" label="${dgenv.cterm}"/>
			    				</c:forEach>
							</stripes:select>
						</td>
				</tr>
				<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_VALIDFROM')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceValidFrom" class="question">Valid from</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceValidFrom" size="30" onchange="checkDateSimple(this); changed()" class="date" id="instrument.sourceValidFrom"/>
							<span class="input-hint">(dd/mm/yyyy)</span>
							<stripes:errors field="instrument.sourceValidFrom"/>
						</td>
				</tr>
				<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_ABSTRACT')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceAbstract" class="question">Abstract</stripes:label>
						</td>
						<td>
							<stripes:textarea name="instrument.sourceAbstract" cols="55" rows="10" onchange="changed()" class="full" id="instrument.sourceAbstract"/>
						</td>
				</tr>
				</table>
			</fieldset>
			<fieldset><legend>Reporting framework</legend>
				<table class="formtable">
	           		<col class="labelcol"/>
	           		<col class="entrycol"/>
		            <tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_GSCOPE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceGeographicScope" class="question">Geographic scope</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceGeographicScope" size="68" onchange="changed()" class="full" id="instrument.sourceGeographicScope"/>
						</td>
		            </tr>
		            <tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_COMMENT')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceComment" class="question">Comments</stripes:label>
						</td>
						<td>
							<stripes:textarea name="instrument.sourceComment" cols="55" rows="5" onchange="changed()" class="full" id="instrument.sourceComment"/>
						</td>
				</tr>
	           		<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_ECENTRYINTOFORCE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceEcEntryIntoForce" class="question">EC entry into force</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceEcEntryIntoForce" size="30" onchange="checkDateSimple(this); changed()" class="date" id="instrument.sourceEcEntryIntoForce"/>
							<span class="input-hint">(dd/mm/yyyy)</span>
							<stripes:errors field="instrument.sourceEcEntryIntoForce"/>
						</td>
		            </tr>
		            <tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_ECACCESSION')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceEcAccession" class="question">EC accession</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceEcAccession" size="30" onchange="checkDateSimple(this); changed()" class="date" id="instrument.sourceEcAccession"/>
							<span class="input-hint">(dd/mm/yyyy)</span>
							<stripes:errors field="instrument.sourceEcAccession"/>
						</td>
		            </tr>
		            <tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_SECRETARIAT')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceSecretariat" class="question">Secretariat</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceSecretariat" size="68" onchange="changed()" class="full" id="instrument.sourceSecretariat"/>
						</td>
		            </tr>
		            <tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_SECRETARIATHOMEPAGE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceSecretariatUrl" class="question">URL to Secretariat homepage</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceSecretariatUrl" size="68" onchange="chkUrl(this); changed()" class="full" id="instrument.sourceSecretariatUrl"/>
							<stripes:errors field="instrument.sourceSecretariatUrl"/>
						</td>
		            </tr>
				</table>
			</fieldset>
			<fieldset><legend>Record management</legend>
				<table class="formtable">
	           		<col class="labelcol"/>
	           		<col class="entrycol"/>
	           		<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_RMVERIFIED')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceVerified" class="question">Verified</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceVerified" size="30" onchange="checkDateSimple(this); changed()" class="date" id="instrument.sourceVerified"/>
							<span class="input-hint">(dd/mm/yyyy)</span>
							<stripes:errors field="instrument.sourceVerified"/>
						</td>
		            </tr>
		            <tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_RMVERIFIEDBY')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceVerifiedBy" class="question">Verified by</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceVerifiedBy" size="68" onchange="changed()" class="full" id="instrument.sourceVerifiedBy"/>
						</td>
		            </tr>
		            <tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_RMNEXTUPDATEDUE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceNextUpdate" class="question">Next update due</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceNextUpdate" size="30" onchange="checkDateSimple(this); changed()" class="date" id="instrument.sourceNextUpdate"/>
							<span class="input-hint">(dd/mm/yyyy)</span>
							<stripes:errors field="instrument.sourceNextUpdate"/>
						</td>
		            </tr>
		            <tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_LI_RMVALIDATEDBY')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="instrument.sourceValidatedBy" class="question">Validated by</stripes:label>
						</td>
						<td>
							<stripes:text name="instrument.sourceValidatedBy" size="68" onchange="changed()" class="full" id="instrument.sourceValidatedBy"/>
						</td>
		            </tr>
				</table>
			</fieldset>
			<script type="text/javascript">
				function selectAll(){
					selValues(document.f.selectedSourceClasses.options);
					isChanged = false;
				}
			</script>
			<table class="formtable">
			<col class="labelcol"/>
			<col class="entrycol"/>
            	<tr>
              		<td colspan="2" class="save" align="center">
              			<c:choose>
							<c:when test="${!empty actionBean.instId && actionBean.instId == 'new'}">
								<stripes:submit name="add" id="addBtn" value="Add" class="btn" onclick="javascript:selectAll()"/>
							</c:when>
							<c:otherwise>
								<stripes:submit name="edit" id="editBtn" value="Save changes" class="btn" onclick="javascript:selectAll()"/>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${!empty actionBean.instId && actionBean.instId == 'new'}">
								<stripes:button name="cancel" onclick="location.href='${pageContext.request.contextPath}/instruments'" value="Exit" class="btn"/>  
							</c:when>
							<c:otherwise>
								<stripes:button name="cancel" onclick="location.href='${pageContext.request.contextPath}/instruments/${actionBean.id}'" value="Exit" class="btn"/>  
							</c:otherwise>
						</c:choose>
		 			</td>
            	</tr>
          	</table>
		</stripes:form>

	</stripes:layout-component>
</stripes:layout-render>
