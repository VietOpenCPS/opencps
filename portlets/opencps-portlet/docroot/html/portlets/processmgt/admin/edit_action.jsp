<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="com.liferay.portal.kernel.process.ProcessUtil"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="org.opencps.processmgt.model.impl.StepAllowanceImpl"%>
<%@page import="java.util.Collections"%>
<%@page import="org.opencps.processmgt.model.StepAllowance"%>
<%@page import="org.opencps.processmgt.model.ServiceProcess"%>
<%@page import="org.opencps.servicemgt.search.ServiceDisplayTerms"%>
<%@page import="org.opencps.processmgt.model.ProcessStep"%>
<%
/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>

<%@ include file="../init.jsp" %>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	
	ProcessStep step = (ProcessStep) request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);
	
	ServiceProcess serviceProcess  = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	

	List<StepAllowance> stepAllowances = Collections.emptyList();

	int[] stepAllowanceIndexs = null;

	String stepAllowancesIndexesParam = ParamUtil.getString(request, "stepAllowanceIndexs");

	if (Validator.isNotNull(stepAllowancesIndexesParam)) {
		stepAllowances = new ArrayList<StepAllowance>();

		stepAllowanceIndexs = StringUtil.split(stepAllowancesIndexesParam, 0);

		for (int stepAllowancesIndexe : stepAllowanceIndexs) {
			stepAllowances.add(new StepAllowanceImpl());
		}
	}
	else {

		if (Validator.isNotNull(step)) {
			stepAllowances = Collections.emptyList(); 

			stepAllowanceIndexs = new int[stepAllowances.size()];

			for (int i = 0; i < stepAllowances.size() ; i++) {
				stepAllowanceIndexs[i] = i;
			}
		}

		if (stepAllowances.isEmpty()) {
			stepAllowances = new ArrayList<StepAllowance>();

			stepAllowances.add(new StepAllowanceImpl());

			stepAllowanceIndexs = new int[] {0};
		}

		if (stepAllowanceIndexs == null) {
			stepAllowanceIndexs = new int[0];
		}
	}
	
%>

<portlet:actionURL name="updateProcessStep" var="updateProcessStepURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString()%>"/>

<aui:form name="processStepFm" method="POST" action="#">

	<aui:model-context bean="<%= step %>" model="<%= ProcessStep.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= currentURL %>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL %>"/>
	
	<aui:input name="<%= ServiceDisplayTerms.GROUP_ID %>" type="hidden" 
		value="<%= scopeGroupId%>"/>
	<aui:input name="<%= ServiceDisplayTerms.COMPANY_ID %>" type="hidden" 
		value="<%= company.getCompanyId()%>"/>
		
	<aui:input name="serviceProcessId" type="hidden" 
		value="<%= Validator.isNotNull(serviceProcess) ? serviceProcess.getPrimaryKey() : StringPool.BLANK %>"/>
	
	<aui:input name="processStepId" type="hidden" 
		value="<%= Validator.isNotNull(step) ? step.getProcessStepId() : StringPool.BLANK %>"/>

	<aui:row>
		<aui:col width="70">
			<liferay-ui:message key="step-name"/>
			<aui:input name="stepName" inlineLabel="false" label="" inlineField="false"></aui:input>
		</aui:col>
		<aui:col width="30">
			<aui:input name="sequenceNo" inlineLabel="false"></aui:input>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="70">
			<datamgt:ddr 
				cssClass="input100"
				depthLevel="1" 
				dictCollectionCode="DOSSIER_STATUS"
				itemNames="dossierStatus"
				itemsEmptyOption="true"
				selectedItems="<%= Validator.isNotNull(step) ? step.getDossierStatus() : StringPool.BLANK %>"
			>
			</datamgt:ddr>
		</aui:col>
		<aui:col width="30">
			<aui:input name="daysDuration" inlineField="false"></aui:input>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="70">
			<aui:select name="referenceDossierPartId" showEmptyOption="true">
			
			</aui:select>
		</aui:col>
		<aui:col width="30">
			&nbsp;
		</aui:col>
	</aui:row>
	
	<div id="step-allowance">
		<div class="lfr-form-row lfr-form-row-inline">
			<div class="row-fields">
				<aui:select id="roleId0" inlineField="<%= true %>" name="roleId0" showEmptyOption="true">
					<%
						List<Role> roles = ProcessUtils.getRoles(renderRequest);
						
						for(Role role : roles) {
					%>
							<aui:option value="<%= role.getPrimaryKey() %>"><%= role.getName() %></aui:option>
					<%
						}
					%>
				</aui:select>
				<aui:input fieldParam="readOnly0" id="readOnly0" inlineField="<%= true %>" name="readOnly0" type="checkbox"/>
			</div>
		</div>
	</div>
		
	<aui:script use="liferay-auto-fields">
		new Liferay.AutoFields(
			{
				contentBox: '#step-allowance',
				fieldIndexes: '<portlet:namespace />stepAllowanceIndexs',
				namespace: '<portlet:namespace />'
			}
		).render();
	</aui:script>

	<div id="dossier-part">
		<div class="lfr-form-row lfr-form-row-inline">
			<div class="row-fields">
				<aui:select id="dossierPart0" inlineField="<%= true %>" name="dossierPart0" showEmptyOption="true">

				</aui:select>
			</div>
		</div>
	</div>
	
	<aui:script use="liferay-auto-fields">
		new Liferay.AutoFields(
			{
				contentBox: '#dossier-part',
				fieldIndexes: '<portlet:namespace />dossierIndexs',
				namespace: '<portlet:namespace />'
			}
		).render();
	</aui:script>

	<aui:row>
		<aui:col width="100">
			<aui:input name="externalAppUrl" cssClass="input100"></aui:input>
		</aui:col>
	</aui:row>

	<aui:button-row>
		<aui:button name="saveForm" value="<%= Validator.isNotNull(step) ? Constants.ADD : Constants.UPDATE %>"/>
		<aui:button type="cancel" name="closeDialog" />
	</aui:button-row>
	
</aui:form>

<aui:script use="aui-base,aui-io-request">

	Liferay.provide(window,'submitForm', function() {
		var A = AUI();
		A.io.request('<%= updateProcessStepURL.toString() %>',
		{
			method: 'POST',
			form: { id: '<portlet:namespace />processStepFm' },
			on: {
				success: function() {
					Liferay.Util.getOpener().refreshPortlet();
					Liferay.Util.getOpener().closePopup('addstep');
				}
			}
		});
	});


	AUI().ready(function(A){
		A.one('#<portlet:namespace/>saveForm').on('click', function(event) {
			var A = AUI();
			var url = '<%= updateProcessStepURL.toString() %>';
			A.io.request(
				url,
				{
					method: 'POST',
					form: {
						id: '<portlet:namespace/>processStepFm'
					},
					on: {
						success: function() {
							Liferay.Util.getOpener().refreshPortlet();
							Liferay.Util.getOpener().closePopup('addstep');
						},
						error:function() {
							alert("update-fail");
						}
					}
				}
			);
		});
	});
	
</aui:script>

<aui:script use="aui-base">
	A.one('#<portlet:namespace/>closeDialog').on('click', function(event) {
		Liferay.Util.getOpener().closePopup('addstep');
	});
</aui:script>