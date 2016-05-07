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
	
	ProcessWorkflow workflow = (ProcessWorkflow) request.getAttribute(WebKeys.WORKFLOW_ENTRY);
	
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
	

	List<DossierPart> dossiersResult = ProcessUtils.getDossierParts(dossierTemplateId, PortletConstants.DOSSIER_PART_TYPE_RESULT);

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
				<aui:option value="create"> <liferay-ui:message key="create"/></aui:option>
				<aui:option value="resubmit"> <liferay-ui:message key="resubmit"/></aui:option>
				<aui:option value="pay"> <liferay-ui:message key="pay"/></aui:option>
				<aui:option value="cancel-event"> <liferay-ui:message key="cancel"/></aui:option>
				<aui:option value="delay-mm"> <liferay-ui:message key="delay-mm"/></aui:option>
				<aui:option value="payok"> <liferay-ui:message key="payok"/></aui:option>
				<aui:option value="tag-label"> <liferay-ui:message key="tag-label"/></aui:option>
			</aui:select>
		</aui:col>	
	</aui:row>

	<aui:row>
		<aui:col width="50">
			<aui:select name="preProcessStepId" showEmptyOption="true">
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
			<aui:select name="postProcessStepId" showEmptyOption="true" >
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
			<aui:input name="assignUser" type="checkbox"></aui:input>
			
			<aui:select name="actionUserId" showEmptyOption="true" label="">
				<%
					List<User> assignUsers = ProcessUtils.getAssignUsers(0);
					for (User userSel : assignUsers) {
				%>	
					<aui:option value="<%= userSel.getUserId() %>"><%= userSel.getFullName() %></aui:option>
				<%
					}
				%>
			</aui:select>
		</aui:col>
		<aui:col width="50">
			<aui:input name="requestPayment" type="checkbox"></aui:input>
			
			<aui:input name="paymentFee" label=""></aui:input>
		</aui:col>	
	</aui:row>

	<aui:row>
		<aui:col width="50">
			<aui:input name="generateReceptionNo" type="checkbox"></aui:input>
			
			<aui:input name="receptionNoPattern" label=""></aui:input>
		</aui:col>
		<aui:col width="50">
			<aui:input name="generateDeadline" type="checkbox"></aui:input>
			
			<aui:input name="deadlinePattern" label=""></aui:input>
		</aui:col>	
	</aui:row>
	
	<label class="bold"><liferay-ui:message key="result-action"/></label>
	
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
							for (DossierPart dossier : dossiersResult) {
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
	</aui:script>

	<aui:button-row>
		<aui:button type="submit" value="<%= Validator.isNotNull(workflow) ? Constants.ADD : Constants.UPDATE %>"/>
		<aui:button type="cancel" name="cencel" />
	</aui:button-row>
	
</aui:form>

