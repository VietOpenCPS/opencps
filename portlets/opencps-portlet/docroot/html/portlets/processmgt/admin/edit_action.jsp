
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
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.impl.WorkflowOutputImpl"%>
<%@page import="org.opencps.processmgt.model.WorkflowOutput"%>
<%@page import="org.opencps.processmgt.service.StepAllowanceLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="com.liferay.portal.kernel.process.ProcessUtil"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="org.opencps.processmgt.model.impl.StepAllowanceImpl"%>
<%@page import="java.util.Collections"%>
<%@page import="org.opencps.processmgt.model.StepAllowance"%>
<%@page import="org.opencps.processmgt.model.ServiceProcess"%>
<%@page import="org.opencps.servicemgt.search.ServiceDisplayTerms"%>
<%@page import="org.opencps.processmgt.model.ProcessStep"%>
<%@ include file="../init.jsp" %>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	
	ProcessWorkflow workflow = (ProcessWorkflow) request.getAttribute(WebKeys.WORKFLOW_ENTRY);
	
	long workflowId = workflow != null ? workflow.getProcessWorkflowId() : 0;
	
	ServiceProcess serviceProcess = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	long dossierTemplateId = 0;

	long serviceProcessId = 0;
	long processWorkflowId = 0;
	
	if (Validator.isNotNull(serviceProcess)) {
		serviceProcessId = serviceProcess.getServiceProcessId();
		dossierTemplateId = serviceProcess.getDossierTemplateId();
	}
	
	if (Validator.isNotNull(workflow)) {
		processWorkflowId = workflow.getProcessWorkflowId();
	}
	
	List<DossierPart> dossiersResults = new ArrayList<DossierPart>();
	
	List<DossierPart> dossiersResult = ProcessUtils.getDossierParts(dossierTemplateId, PortletConstants.DOSSIER_PART_TYPE_RESULT);
	
	List<DossierPart> dossiersResultMulti = ProcessUtils.getDossierParts(dossierTemplateId, PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT);
	
	
	if(dossiersResult!=null && !dossiersResult.isEmpty()){
		dossiersResults.addAll(dossiersResult);
	}
	
	if(dossiersResultMulti!=null && !dossiersResultMulti.isEmpty()){
		dossiersResults.addAll(dossiersResultMulti);
	}
	
	List<ProcessStep> stepAll = ProcessStepLocalServiceUtil.getStepByProcess(serviceProcessId);
	
	List<WorkflowOutput> workflowOutputs = Collections.emptyList();

	int[] outputIndexs = null;

	String outputIndexsParam = ParamUtil.getString(request, "outputIndexs");

	if (Validator.isNotNull(outputIndexsParam)) {
		workflowOutputs = new ArrayList<WorkflowOutput>();

		outputIndexs = StringUtil.split(outputIndexsParam, 0);

		for (int outoutIndex : outputIndexs) {
			workflowOutputs.add(new WorkflowOutputImpl());
		}
	}
	else {

		if (Validator.isNotNull(workflow)) {
			workflowOutputs = WorkflowOutputLocalServiceUtil.getByProcessWF(processWorkflowId);

			outputIndexs = new int[workflowOutputs.size()];

			for (int i = 0; i < workflowOutputs.size() ; i++) {
				outputIndexs[i] = i;
			}
		}

		if (workflowOutputs.isEmpty()) {
			workflowOutputs = new ArrayList<WorkflowOutput>();

			workflowOutputs.add(new WorkflowOutputImpl());

			outputIndexs = new int[] {0};
		}

		if (outputIndexs == null) {
			outputIndexs = new int[0];
		}
	}
	
%>

<portlet:actionURL name="updateAction" var="updateActionURL"/>
<portlet:renderURL var="getAssignUsersURL" windowState="<%=LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "assign_users.jsp" %>'/>
</portlet:renderURL>

<aui:form name="actionFm" method="POST" action="<%= updateActionURL %>">

	<aui:model-context bean="<%= workflow %>" model="<%= ProcessWorkflow.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= redirectURL %>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL %>"/>
	
	<aui:input name="<%= ServiceDisplayTerms.GROUP_ID %>" type="hidden" 
		value="<%= scopeGroupId%>"/>
	<aui:input name="<%= ServiceDisplayTerms.COMPANY_ID %>" type="hidden" 
		value="<%= company.getCompanyId()%>"/>
		
	<aui:input name="serviceProcessId" type="hidden" 
		value="<%= Validator.isNotNull(serviceProcess) ? serviceProcess.getPrimaryKey() : StringPool.BLANK %>"/>
	
	<aui:input name="processWorkflowId" type="hidden" 
		value="<%= Validator.isNotNull(workflow) ? workflow.getProcessWorkflowId() : StringPool.BLANK %>"/>
	
	
	<aui:row>
		<aui:col width="50">
			<aui:input name="actionName"></aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:select name="autoEvent">
				<aui:option value=""> <liferay-ui:message key="no-event"/></aui:option>
				<aui:option value="submit"> <liferay-ui:message key="submit"/></aui:option>
				<aui:option value="onegate"> <liferay-ui:message key="onegate"/></aui:option>
				<aui:option value="repair"> <liferay-ui:message key="repair"/></aui:option>
				<aui:option value="change"> <liferay-ui:message key="change"/></aui:option>
				<aui:option value="minutely"> <liferay-ui:message key="minutely"/></aui:option>
				<aui:option value="5-minutely"> <liferay-ui:message key="5-minutely"/></aui:option>
				<aui:option value="hourly"> <liferay-ui:message key="hourly"/></aui:option>
				<aui:option value="daily"> <liferay-ui:message key="daily"/></aui:option>
			</aui:select>
		</aui:col>	
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:input name="preCondition" ></aui:input>
		</aui:col>
		<aui:col width="25">
			<aui:input name="isMultipled"></aui:input>
		</aui:col>
		
		<aui:col width="25">
				<aui:input name="isFinishStep"></aui:input>
		</aui:col>
	</aui:row>

	<aui:row>
		<aui:col width="50">
			<aui:select name="preProcessStepId">
				<aui:option value="<%= 0 %>">
					<liferay-ui:message key="start-step" />
				</aui:option>
				<%
					for (ProcessStep step : stepAll) {
				%>
						<aui:option value="<%= step.getProcessStepId() %>"> <%= step.getStepName() %></aui:option>
				<%
					}
				%>
			</aui:select>
		</aui:col>
		<aui:col width="50">
			<aui:select name="postProcessStepId">
				<aui:option value="<%= 0 %>">
					<liferay-ui:message key="end-step" />
				</aui:option>
				<%
					for (ProcessStep step : stepAll) {
				%>
						<aui:option value="<%= step.getProcessStepId() %>"> <%= step.getStepName() %></aui:option>
				<%
					}
				%>
			</aui:select>	
		</aui:col>	
	</aui:row>

	<aui:row>
		<aui:col width="50">
			<aui:input name="assignUser" type="checkbox" checked="<%= (workflow != null) ? workflow.getAssignUser() : false %>"></aui:input>
			<div id='<%=renderResponse.getNamespace() + "actionUserBoundary"%>'>
			<aui:select name="actionUserId" showEmptyOption="true" label="">
				
			</aui:select>
			</div>
		</aui:col>
		<aui:col width="50">
			<aui:input name="requestPayment" ></aui:input>
			
			<aui:input name="paymentFee" label=""></aui:input>
		</aui:col>	
	</aui:row>

	<aui:row>
		<aui:col width="50">
			<aui:input name="generateReceptionNo" ></aui:input>
			
			<aui:input name="receptionNoPattern" label=""></aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input name="generateDeadline" ></aui:input>
			
			<aui:input name="deadlinePattern" label=""></aui:input>
		</aui:col>	
	</aui:row>
	
	
	<label class="bold"><liferay-ui:message key="results-values"/></label>
	
	<div id="workflow-output">
		<%
			for (int i = 0; i < outputIndexs.length; i++) {
				int outputIndex = outputIndexs[i];
				
				WorkflowOutput output = workflowOutputs.get(i);
		%>
			<div class="lfr-form-row lfr-form-row-inline">
				<div class="row-fields">
					<aui:input name='<%= "workflowOutputId" + outputIndex %>' type="hidden" value="<%= output.getWorkflowOutputId() %>"/>
					
					<aui:select id='<%= "dossierPartId" + outputIndex %>' inlineField="<%= true %>" name='<%= "dossierPartId" + outputIndex %>' label="" showEmptyOption="true">
						<%							
							for (DossierPart dossier : dossiersResults) {
						%>
								<aui:option selected="<%=  Validator.equals(output.getDossierPartId(), dossier.getDossierpartId()) %>" value="<%= dossier.getDossierpartId() %>"><%= dossier.getPartName() %></aui:option>
						<%
							}
						%>
					</aui:select>

					<aui:input checked="<%= output.getRequired() %>" fieldParam='<%= "required" + outputIndex %>' id='<%= "required" + outputIndex %>' label="required" inlineField="<%= true %>" name='<%= "required" + outputIndex %>' type="checkbox"/>
					<aui:input checked="<%= output.getEsign() %>" fieldParam='<%= "esign" + outputIndex %>' id='<%= "esign" + outputIndex %>' label="esign" inlineField="<%= true %>" name='<%= "esign" + outputIndex %>' type="checkbox"/>
					<aui:input checked="<%= output.getPostback() %>" fieldParam='<%= "postback" + outputIndex %>' id='<%= "postback" + outputIndex %>' label="postback" inlineField="<%= true %>" name='<%= "postback" + outputIndex %>' type="checkbox"/>
				</div>
			</div>
			
		<%
			}
		%>
	
	</div>

	<aui:script use="liferay-auto-fields">
		new Liferay.AutoFields(
			{
				contentBox: '#workflow-output',
				fieldIndexes: '<portlet:namespace />outputIndexs',
				namespace: '<portlet:namespace />'
			}
		).render();
		
		AUI().ready('aui-base','liferay-portlet-url','aui-io', function(A){
			var postProcessStep = A.one('#<portlet:namespace/>postProcessStepId');
			var workflowIdReq = '<%=workflowId%>';
			if(postProcessStep){
				<portlet:namespace/>getAssignUsers(postProcessStep.val(), workflowIdReq);
				postProcessStep.on('change', function(){
					var postProcessStepId = postProcessStep.val();
					<portlet:namespace/>getAssignUsers(postProcessStepId, workflowIdReq);
				});
			}
		});
		
		Liferay.provide(window, '<portlet:namespace/>getAssignUsers', function(postProcessStepId, workflowIdReq) {

			var A = AUI();
			var actionUserBoundary = A.one('#<portlet:namespace/>actionUserBoundary');
			var url = '<%= getAssignUsersURL.toString() %>';
			if(parseInt(postProcessStepId) > 0){
				A.io.request(
					url,
					{
						dataType: 'json',
						data: {
							'<portlet:namespace/>processStepId': postProcessStepId,
							'<portlet:namespace/>workflowId': workflowIdReq
						},
						on: {
							success: function(event, id, obj) {
								var instance = this;
								var res = instance.get('responseData');
								actionUserBoundary.empty();
								actionUserBoundary.append(res);
							},
						    error: function(){}
						}
					}
				);
			}
			
		},['aui-base','liferay-portlet-url','aui-io']);
	</aui:script>

	<aui:button-row>
		<aui:button type="submit" value="<%= Validator.isNotNull(workflow) ? Constants.ADD : Constants.UPDATE %>"/>
		<aui:button type="cancel" name="cencel" />
	</aui:button-row>
	
</aui:form>

