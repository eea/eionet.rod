<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Edit Reporting Obligation" help="HELP_RA" unLoad="true">

	<stripes:layout-component name="contents">

		<h1>Edit Reporting Obligation for </h1>
		
		<stripes:form action="/obligations" method="post" name="f">
			<stripes:hidden name="id"/>
			<stripes:hidden name="aid"/>
			<stripes:hidden name="obligation.obligationId"/>
			<stripes:hidden name="obligation.fkSourceId"/>
			<stripes:hidden name="obligation.terminate"/>
			<stripes:hidden name="obligation.nextDeadline2"/>
			<fieldset><legend>Identification</legend>
				<table class="formtable">
	           		<col class="labelcol"/>
	           		<col class="entrycol"/>
	           		<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_TITLE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="title" class="required question">Title</stripes:label>
						</td>
						<td>
							<stripes:text name="obligation.title" size="68" onchange="checkMdText(this); changed()" class="full" id="title" title="This is a short identifier for the reporting obligation. The field should show the name that is commonly used for this obligation and ideally reflect the type of reporting or information collected."/>
							<stripes:errors field="obligation.title"/>
						</td>
		            </tr>
		            <tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_DESCRIPTION')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="description" class="required question">Description</stripes:label>
						</td>
						<td>
							<stripes:textarea name="obligation.description" cols="55" rows="5" onchange="checkMdText(this); changed()" class="full" id="description" title="This is for a short description of the reporting obligation to expand on the title. Guide length: three lines or less."/>
							<stripes:errors field="obligation.description"/>
						</td>
		            </tr>
				</table>
			</fieldset>
			<fieldset><legend>Reporting dates</legend>
				<table class="formtable">
					<col class="labelcol"/>
           			<col class="entrycol"/>
					<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_VALIDFROM')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="first_reporting" class="question">Baseline reporting date</stripes:label>
						</td>
						<td>
							<stripes:text name="obligation.firstReporting" size="30" onchange="changedReporting()" class="date" id="first_reporting"/>
							<span class="input-hint">(dd/mm/yyyy)</span>
							<stripes:errors field="obligation.firstReporting"/>
						</td>
					</tr>
					<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_VALIDTO')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="valid_to" class="question">Valid to</stripes:label>
						</td>
						<td>
							<stripes:text name="obligation.validTo" size="30" onchange="changedReporting()" class="date" id="valid_to"/>
							<span class="input-hint">(dd/mm/yyyy)</span>
							<stripes:errors field="obligation.validTo"/>
						</td>
					</tr>
					<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_REPORTINGFREQUENCYINMONTHS')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="report_freq_months" class="question">Reporting frequency in months</stripes:label>
						</td>
						<td>
							<stripes:text name="obligation.reportFreqMonths" size="30" onchange="changedReporting(); contReporting();" class="date" id="report_freq_months"/>
							<span class="input-hint">For one-time-only reporting, enter 0 and choose a reasonable Valid to</span>
						</td>
					</tr>
					<c:choose>
						<c:when test="${empty actionBean.obligation.reportFreqMonths}">
							<tr class="zebraeven" id="contReporting">
						</c:when>
						<c:otherwise>
							<tr class="zebraeven" id="contReporting" style="display: none;">
						</c:otherwise>
					</c:choose>
						<td>
						</td>
						<td>
							<stripes:checkbox name="obligation.continousReporting" onchange="contReportingChanged()" checked="yes" value="yes" id="continousReporting"/>
		                    <stripes:label for="continousReporting">continuous reporting</stripes:label>
						</td>
					</tr>
					<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_NEXTDUEDATE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="next_deadline" class="question">Next due date</stripes:label>
						</td>
						<td>
							<stripes:text name="obligation.nextDeadline" size="30" onchange="changed()" readonly="readonly" class="date" id="next_deadline"/>
							<span class="input-hint">(calculated automatically)</span>
						</td>
					</tr>
					<tr class="zebraeven">
						<td class="important" colspan="2">
			                <em><strong>IMPORTANT!</strong> Use the following field <strong>only</strong> if reporting dates (above fields)
							cannot be given in numerical date format but have to be given in text format (for example, 'ASAP')</em>
		              	</td>
					</tr>
					<tr>
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_REPORTINGDATETEXTFORMAT')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="next_reporting" class="question">Reporting date</stripes:label>
						</td>
						<td>
							<stripes:text name="obligation.nextReporting" size="68" onchange="changed()" class="full" id="next_reporting"/>
						</td>
					</tr>
					<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_DATECOMMENTS')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="date_comments" class="question">Date comments</stripes:label>
						</td>
						<td>
							<stripes:text name="obligation.dateComments" size="68" onchange="changed()" class="full" id="date_comments"/>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset><legend>Clients</legend>
				<table class="formtable">
				   <col class="labelcol"/>
           			<col class="entrycol"/>
           			<tr class="zebraeven">
						<td>
							<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RO_REPORTTO')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="report_to" class="required question">Report to</stripes:label>
						</td>
						<td>
							<stripes:select name="obligation.fkClientId" onchange="changed()" class="full" id="report_to">
								<stripes:option value="0" label=""/>
			    				<c:forEach items="${actionBean.clients}" var="client" varStatus="loop">
			    					<stripes:option value="${client.clientId}" label="${client.name}"/>
			    				</c:forEach>
							</stripes:select>
							<stripes:errors field="obligation.fkClientId"/>
						</td>
					</tr>
					<tr>
		              	<td>
		                	<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RO_OTHERCLIENTS')" title="Help on this field"><span>Help on field</span></a>
				      		<label for="client_list" class="question">Other clients using this reporting</label>
		              	</td>
		              	<td></td>
		            </tr>
		            <tr>
						<td colspan="2">
							<table class="fancyselect">
								<tr>
									<td>
										<stripes:select name="selClients" multiple="true" size="9" class="multiple" id="client_list"> 
						    				<c:forEach items="${actionBean.clients}" var="client" varStatus="loop">
						    					<stripes:option value="${client.clientId}" label="${client.name}"/>
						    				</c:forEach>
									  	</stripes:select>
									</td>
									<td>
										<div class="arrow_btnbox">
											<input value="-&gt;" onclick="addClientValues(document.f.selClients.options, document.f.selectedClients.options)" class="arrow_btn add" type="button" />
										</div>
										<div class="arrow_btnbox">
											<input value="&lt;-" onclick="delValues(document.f.selectedClients.options)" class="arrow_btn delete" type="button" />
										</div>
									</td>
									<td>
										<stripes:select name="selectedClients" multiple="true" size="9" class="multiple"> 
						    				<c:forEach items="${actionBean.obligationClients}" var="client" varStatus="loop">
						    					<stripes:option value="${client.clientId}" label="${client.name}"/>
						    				</c:forEach>
									  	</stripes:select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset><legend>How to report</legend>
          		<table class="formtable">
           			<col class="labelcol"/>
           			<col class="entrycol"/>
			            <tr class="zebraeven">
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_NAMEOFREPORTINGGUIDELINES')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.formatName" class="question">Name of reporting guidelines</stripes:label>
              				</td>
              				<td>
                				<stripes:text name="obligation.formatName" size="68" onchange="changed()" class="full" id="obligation.formatName"/>
              				</td>
            			</tr>
            			<tr>
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_URLTOREPORTINGGUIDELINES')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.reportFormatUrl" class="question">URL to reporting guidelines</stripes:label>
              				</td>
              				<td>
              					<stripes:text name="obligation.reportFormatUrl" size="68" onchange="chkUrl(this); changed()" class="full" id="obligation.reportFormatUrl"/>
              					<stripes:errors field="obligation.reportFormatUrl"/>
              				</td>
            			</tr>
            			<tr class="zebraeven">
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_FORMATVALIDSINCE')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.validSince" class="question">Format valid since</stripes:label>
              				</td>
              				<td>
                				<stripes:text name="obligation.validSince" size="30" onchange="checkDateSimple(this); changed()" class="date" id="obligation.validSince"/>
                				<span class="input-hint">(dd/mm/yyyy)</span>
                				<stripes:errors field="obligation.validSince"/>
              				</td>
            			</tr>
            			<tr>
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_REPORTINGGUIDELINES')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.reportingFormat" class="question">Reporting guidelines -Extra info.</stripes:label>
              				</td>
              				<td>
              					<stripes:textarea name="obligation.reportingFormat" cols="55" rows="5" onchange="changed()" class="full" id="obligation.reportingFormat"/>
              				</td>
            			</tr>
            			<tr class="zebraeven">
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_LOCATIONINFO')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.locationInfo" class="question">Name of repository</stripes:label>
              				</td>
              				<td>
                				<stripes:text name="obligation.locationInfo" size="68" onchange="changed()" class="full" id="obligation.locationInfo"/>
              				</td>
            			</tr>
            			<tr>
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_LOCATIONPTR')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.locationPtr" class="question">URL to repository</stripes:label>
              				</td>
              				<td>
              					<stripes:text name="obligation.locationPtr" size="68" onchange="chkUrl(this); changed()" class="full" id="obligation.locationPtr"/>
              					<stripes:errors field="obligation.locationPtr"/>
              				</td>
            			</tr>
            			<tr class="zebraeven">
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_DATAUSEDFOR')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.dataUsedFor" class="question">Data used for (name)</stripes:label>
              				</td>
              				<td>
                				<stripes:text name="obligation.dataUsedFor" size="68" onchange="changed()" class="full" id="obligation.dataUsedFor"/>
              				</td>
            			</tr>
            			<tr>
              				<td>
                				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_DATAUSEDFOR')" title="Help on this field"><span>Help on field</span></a>
				                <stripes:label for="obligation.dataUsedForUrl" class="question">Data used for (URL)</stripes:label>
              				</td>
              				<td>
              					<stripes:text name="obligation.dataUsedForUrl" size="68" onchange="chkUrl(this); changed()" class="full" id="obligation.dataUsedForUrl"/>
              					<stripes:errors field="obligation.dataUsedForUrl"/>
              				</td>
            			</tr>
				</table>
			</fieldset>
			<fieldset><legend>Data providers</legend>
          		<table class="formtable">
           			<col class="labelcol"/>
           			<col class="entrycol"/>
            		<tr class="zebraeven">
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_SPATIALCOVERAGE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="forCountries" class="question">Countries reporting formally</stripes:label>
			            </td>
              			<td>
              			</td>
            		</tr>
            		<tr>
            			<td colspan="2">
	            			<table id="countries_formally" class="fancyselect">
								<tr>
	                    			<td>
	                    				<script type="text/javascript">
	                    					//<![CDATA[
											clist.push("0:All EU Countries:C:");
											<c:forEach items="${actionBean.allcountries}" var="country" varStatus="loop">
												clist.push("${country.countryId}:${country.name}:${country.type}:${country.twoletter}");
											</c:forEach>
											//]]>
										</script>
										<stripes:select name="forCountries" id="forCountries" multiple="true" size="9" class="multiple"> 
											<stripes:option value="0" label="All EU countries"/>
						    				<c:forEach items="${actionBean.allcountries}" var="country" varStatus="loop">
						    					<stripes:option value="${country.countryId}" label="${country.name}"/>
						    				</c:forEach>
									  	</stripes:select>
									  	<script type="text/javascript">
									  		//<![CDATA[
											fillclist('C',document.f.forCountries);
											//]]>
										</script>
	                    			</td>
	                    			<td>
	                          			<div class="arrow_btnbox">
	                            			<input value="-&gt;" onclick="addValues(document.f.forCountries.options,document.f.selectedFormalCountries.options,null,clist,document.f.selectedVoluntaryCountries.options)" class="arrow_btn add" type="button" />
	                          			</div>
	                          			<div class="arrow_btnbox">
	                            			<input value="&lt;-" onclick="delValues(document.f.selectedFormalCountries.options)" class="arrow_btn delete" type="button" />
	                          			</div>
	                    			</td>
	                    			<td>
										<stripes:select name="selectedFormalCountries" multiple="true" size="9" class="multiple"> 
						    				<c:forEach items="${actionBean.formalCountries}" var="country" varStatus="loop">
						    					<stripes:option value="${country.countryId}" label="${country.name}"/>
						    				</c:forEach>
									  	</stripes:select>
	                    			</td>
								</tr>
	                		</table>
                		</td>
            		</tr>
            		<tr class="zebraeven">
						<td>
		                	<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_VOLUNTARYCOUNTRIES')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="volCountries" class="question">Countries reporting voluntarily</stripes:label>
		              	</td>
		              	<td>
		              	</td>
		            </tr>
		            <tr>
		              	<td colspan="2">
		                	<table id="countries_volun" class="fancyselect">
		                  		<tr>
		                    		<td align="left">
		                      			<stripes:select name="volCountries" id="volCountries" multiple="true" size="9" class="multiple"> 
											<stripes:option value="0" label="All EU countries"/>
						    				<c:forEach items="${actionBean.allcountries}" var="country" varStatus="loop">
						    					<stripes:option value="${country.countryId}" label="${country.name}"/>
						    				</c:forEach>
									  	</stripes:select>
		                    		</td>
		                    		<td>
		                          		<div class="arrow_btnbox">
		                            		<input value="-&gt;" onclick="addFullValues(document.f.volCountries.options,document.f.selectedVoluntaryCountries.options,clist,document.f.selectedFormalCountries.options)" class="arrow_btn add" type="button" />
		                          		</div>
		                          		<div class="arrow_btnbox">
		                            		<input value="&lt;-" onclick="delValues(document.f.selectedVoluntaryCountries.options)" class="arrow_btn delete" type="button" />
		                          		</div>
		                    		</td>
		                    		<td>
		                      			<stripes:select name="selectedVoluntaryCountries" multiple="true" size="9" class="multiple"> 
						    				<c:forEach items="${actionBean.voluntaryCountries}" var="country" varStatus="loop">
						    					<stripes:option value="${country.countryId}" label="${country.name}"/>
						    				</c:forEach>
									  	</stripes:select>
		                    		</td>
		                  		</tr>
		                	</table>
						</td>
		            </tr>
		            <tr class="zebraeven">
		              	<td>
		                	<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_REPORTINGCOORDINATOR')" title="Help on this field"><span>Help on field</span></a>
			                <label class="question">National reporting coordinators</label>
		              	</td>
		              	<td>
		                	<ul class="menu">
		                  		<li>
		                      		<stripes:label for="obligation.coordinatorRole">Role</stripes:label>
		                      		<stripes:text name="obligation.coordinatorRole" size="15" onchange="changed()" id="obligation.coordinatorRole"/>
		                      		
		                      		<stripes:checkbox name="obligation.coordinatorRoleSuf" checked="1" value="1" id="obligation.coordinatorRoleSuf"/>
		                      		<stripes:label for="obligation.coordinatorRoleSuf">do not append country suffix</stripes:label>
		                  		</li>
		                  		<li style="text-align: center; font-style: italic">
		                      		- OR -
		                  		</li>
		                  		<li>
		                  			<stripes:label for="obligation.coordinator">Name</stripes:label>
		                      		<stripes:text name="obligation.coordinator" size="15" onchange="changed()" id="obligation.coordinator"/>
		                      		
		                      		<stripes:label for="obligation.coordinatorUrl">URL</stripes:label>
		                      		<stripes:text name="obligation.coordinatorUrl" size="33" onchange="chkUrl(this); changed()" id="obligation.coordinatorUrl"/>
		                      		<stripes:errors field="obligation.coordinatorUrl"/>
								</li>
		                	</ul>
		              	</td>
		            </tr>
		            <tr>
		              	<td>
		                	<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_RESPONSIBLEFORREPORTING')" title="Help on this field"><span>Help on field</span></a>
			                <label class="question">National reporting contacts</label>
		              	</td>
		              	<td>
		                	<ul class="menu">
		                  		<li>
		                      		<stripes:label for="obligation.responsibleRole">Role</stripes:label>
		                      		<stripes:text name="obligation.responsibleRole" size="15" onchange="changed()" id="obligation.responsibleRole"/>
		                      		
		                      		<stripes:checkbox name="obligation.responsibleRoleSuf" checked="1" value="1" id="obligation.responsibleRoleSuf"/>
		                      		<stripes:label for="obligation.responsibleRoleSuf">do not append country suffix</stripes:label>
		                  		</li>
		                  		<li style="text-align: center; font-style: italic">
		                      		- OR -
		                  		</li>
		                  		<li>
		                  			<stripes:label for="obligation.nationalContact">Name</stripes:label>
		                      		<stripes:text name="obligation.nationalContact" size="15" onchange="changed()" id="obligation.nationalContact"/>
		                      		
		                      		<stripes:label for="obligation.nationalContactUrl">URL</stripes:label>
		                      		<stripes:text name="obligation.nationalContactUrl" size="33" onchange="chkUrl(this); changed()" id="obligation.nationalContactUrl"/>
		                      		<stripes:errors field="obligation.nationalContactUrl"/>
								</li>
		                	</ul>
		              	</td>
		            </tr>
		            <tr class="zebraeven">
          				<td>
            				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RO_OBLIGATIONTYPE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="obligation.legalMoral" class="question">Obligation type</stripes:label>
          				</td>
          				<td>
            				<stripes:select name="obligation.legalMoral" onchange="changed()" id="obligation.legalMoral"> 
			    				<c:forEach items="${actionBean.legalMoral}" var="lookup" varStatus="loop">
			    					<stripes:option value="${lookup.cvalue}" label="${lookup.cterm}"/>
			    				</c:forEach>
						  	</stripes:select>
          				</td>
        			</tr>
        			<tr>
          				<td>
            				<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RO_INFOTYPE')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="obligation.selectedInfoTypes" class="question">Type of info reported</stripes:label>
          				</td>
          				<td>
            				<stripes:select name="selectedInfoTypes" multiple="true" id="obligation.selectedInfoTypes"> 
			    				<c:forEach items="${actionBean.infoTypeList}" var="lookup" varStatus="loop">
			    					<stripes:option value="${lookup.cvalue}" label="${lookup.cterm}"/>
			    				</c:forEach>
						  	</stripes:select>
          				</td>
        			</tr>
            	</table>
			</fieldset>
			<fieldset><legend>Data use</legend>
          		<table class="formtable">
           			<col class="labelcol"/>
           			<col class="entrycol"/>
           			<tr class="zebraeven">
		              	<td>
		                	<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_ENVIRONMENTALISSUES')" title="Help on this field"><span>Help on field</span></a>
			                <stripes:label for="selIssues" class="question">Environmental issues</stripes:label>
		              	</td>
		              	<td>
		              	</td>
		            </tr>
           			<tr>
            			<td colspan="2">
	            			<table id="issue_list" class="fancyselect">
								<tr>
	                    			<td>
										<stripes:select name="selIssues" id="selIssues" multiple="true" size="9" class="multiple"> 
						    				<c:forEach items="${actionBean.issues}" var="issue" varStatus="loop">
						    					<stripes:option value="${issue.issueId}" label="${issue.name}"/>
						    				</c:forEach>
									  	</stripes:select>
	                    			</td>
	                    			<td>
										<div class="arrow_btnbox">
                            				<input value="-&gt;" onclick="addValuesEnv(document.f.selIssues.options, document.f.selectedIssues.options, null)" class="arrow_btn add" type="button" />
                          				</div>
                          				<div class="arrow_btnbox">
                            				<input value="&lt;-" onclick="delValues(document.f.selectedIssues.options)" class="arrow_btn delete" type="button" />
                          				</div>
                    				</td>
	                    			<td>
										<stripes:select name="selectedIssues" multiple="true" size="9" class="multiple"> 
						    				<c:forEach items="${actionBean.obligationIssues}" var="issue" varStatus="loop">
						    					<stripes:option value="${issue.issueId}" label="${issue.name}"/>
						    				</c:forEach>
									  	</stripes:select>
	                    			</td>
								</tr>
	                		</table>
                		</td>
            		</tr>
            		<tr class="zebraeven">
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_PARAMETERS')" title="Help on this field"><span>Help on field</span></a>
		                	<stripes:label for="obligation.parameters" class="question">Parameters</stripes:label>
              			</td>
              			<td>
                			<stripes:textarea name="obligation.parameters" rows="5" cols="55" onchange="changed()" class="full" id="obligation.parameters"/>
              			</td>
            		</tr>
            		<tr>
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_FLAGS')" title="Help on this field"><span>Help on field</span></a>
                			<label class="question">This obligation is</label>
              			</td>
              			<td>
              				<stripes:checkbox name="obligation.eeaPrimary" checked="1" value="1" id="obligation.eeaPrimary"/>
		                    <stripes:label for="obligation.eeaPrimary">Eionet core data flow</stripes:label>
		                    <br/>
		                    <stripes:checkbox name="obligation.eeaCore" checked="1" value="1" id="obligation.eeaCore"/>
		                    <stripes:label for="obligation.eeaCore">used for EEA Core set of indicators</stripes:label>
		                    <br/>
		                    <stripes:checkbox name="obligation.flagged" checked="1" value="1" id="obligation.flagged"/>
		                    <stripes:label for="obligation.flagged">flagged</stripes:label>
		                    <br/>
						</td>
            		</tr>
            		<tr class="zebraeven">
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_DPSIR')" title="Help on this field"><span>Help on field</span></a>
                			<label class="question">DPSIR</label>
              			</td>
              			<td>
              				<stripes:checkbox name="obligation.dpsirD" checked="yes" value="yes" id="obligation.dpsirD"/>
		                    <stripes:label for="obligation.dpsirD">Driving force</stripes:label>
		                    <br/>
		                    <stripes:checkbox name="obligation.dpsirP" checked="yes" value="yes" id="obligation.dpsirP"/>
		                    <stripes:label for="obligation.dpsirP">Pressure</stripes:label>
		                    <br/>
		                    <stripes:checkbox name="obligation.dpsirS" checked="yes" value="yes" id="obligation.dpsirS"/>
		                    <stripes:label for="obligation.dpsirS">State</stripes:label>
		                    <br/>
		                    <stripes:checkbox name="obligation.dpsirI" checked="yes" value="yes" id="obligation.dpsirI"/>
		                    <stripes:label for="obligation.dpsirI">Impact</stripes:label>
		                    <br/>
		                    <stripes:checkbox name="obligation.dpsirR" checked="yes" value="yes" id="obligation.dpsirR"/>
		                    <stripes:label for="obligation.dpsirR">Response</stripes:label>
		                    <br/>
						</td>
            		</tr>
            		<tr>
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_OVERLAPURL')" title="Help on this field"><span>Help on field</span></a>
                			<stripes:label for="obligation.overlapUrl" class="question">URL of overlapping obligation</stripes:label>
              			</td>
              			<td>
                			<stripes:text name="obligation.overlapUrl" size="68" onchange="chkUrl(this); changed()" class="full" id="obligation.overlapUrl"/>
		                    <stripes:errors field="obligation.overlapUrl"/>
             			</td>
            		</tr>
            		<tr class="zebraeven">
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_COMMENT')" title="Help on this field"><span>Help on field</span></a>
                			<stripes:label for="obligation.comment" class="question">General comments</stripes:label>
              			</td>
              			<td>	
                			<stripes:textarea name="obligation.comment" rows="5" cols="55" onchange="changed()" class="full" id="obligation.comment"/>
              			</td>
            		</tr>
            		<tr>
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_AUTHORITY')" title="Help on this field"><span>Help on field</span></a>
                			<stripes:label for="obligation.authority" class="question">Authority giving rise to the obligation</stripes:label>
              			</td>
              			<td>
                			<stripes:text name="obligation.authority" size="68" onchange="changed()" class="full" id="obligation.authority"/>
              			</td>
            		</tr>
            	</table>
			</fieldset>
			<fieldset><legend>Record management</legend>
          		<table class="formtable">
           			<col class="labelcol"/>
           			<col class="entrycol"/>
            		<tr>
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_RMVERIFIED')" title="Help on this field"><span>Help on field</span></a>
                			<stripes:label for="obligation.rmVerified" class="question">Verified</stripes:label>
              			</td>
              			<td>
              				<stripes:text name="obligation.rmVerified" size="30" class="date" id="obligation.rmVerified" onchange="checkDateSimple(this); changed()"/>
                  			<span class="input-hint">(dd/mm/yyyy)</span>
                  			<stripes:errors field="obligation.rmVerified"/>
						</td>
            		</tr>
            		<tr class="zebraeven">
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_RMVERIFIEDBY')" title="Help on this field"><span>Help on field</span></a>
                			<stripes:label for="obligation.rmVerifiedBy" class="question">Verified by</stripes:label>
              			</td>
              			<td>
                			<stripes:text name="obligation.rmVerifiedBy" size="68" class="full" id="obligation.rmVerifiedBy" onchange="changed()"/>
              			</td>
            		</tr>
            		<tr>
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_RMNEXTUPDATEDUE')" title="Help on this field"><span>Help on field</span></a>
                			<stripes:label for="obligation.rmNextUpdate" class="question">Next update due</stripes:label>
              			</td>
              			<td>
              				<stripes:text name="obligation.rmNextUpdate" size="30" class="date" id="obligation.rmNextUpdate" onchange="checkDateSimple(this); changed()" />
                  			<span class="input-hint">(dd/mm/yyyy)</span>
                  			<stripes:errors field="obligation.rmNextUpdate"/>
						</td>
            		</tr>
            		<tr class="zebraeven">
              			<td>
                			<a class="field-help" href="javascript:openViewHelp2('${pageContext.request.contextPath}','HELP_RA_VALIDATEDBY')" title="Help on this field"><span>Help on field</span></a>
                			<stripes:label for="obligation.validatedBy" class="question">Validated by</stripes:label>
              			</td>
              			<td>
                			<stripes:text name="obligation.validatedBy" size="68" class="full" id="obligation.validatedBy" onchange="changed()"/>
              			</td>
            		</tr>
            	</table>
			</fieldset>
			<script type="text/javascript">
				function selectAll(){
					selValues(document.f.selectedClients.options);
					selValues(document.f.selectedFormalCountries.options);
					selValues(document.f.selectedVoluntaryCountries.options);
					selValues(document.f.selectedIssues.options);
					isChanged = false;
				}
			</script>
			<table class="formtable">
           		<col class="labelcol"/>
           		<col class="entrycol"/>
            	<tr>
              		<td colspan="2" class="save" align="center">
              			<c:choose>
							<c:when test="${!empty actionBean.id && actionBean.id == 'new'}">
								<stripes:submit name="add" id="addBtn" value="Add" class="btn" onclick="javascript:selectAll()"/>
							</c:when>
							<c:otherwise>
								<stripes:submit name="edit" id="editBtn" value="Save changes" class="btn" onclick="javascript:selectAll()"/>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${!empty actionBean.id && actionBean.id == 'new'}">
								<stripes:button name="cancel" onclick="location.href='${pageContext.request.contextPath}/obligations'" value="Exit" class="btn"/>  
							</c:when>
							<c:otherwise>
								<stripes:button name="cancel" onclick="location.href='${pageContext.request.contextPath}/obligations/${actionBean.id}'" value="Exit" class="btn"/>  
							</c:otherwise>
						</c:choose>
		 			</td>
            	</tr>
          </table>
		</stripes:form>

	</stripes:layout-component>
</stripes:layout-render>
