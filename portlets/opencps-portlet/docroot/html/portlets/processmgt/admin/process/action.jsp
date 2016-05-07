<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="com.liferay.portal.kernel.process.ProcessUtil"%>
<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.WorkflowSearchTerms"%>
<%@page import="org.opencps.processmgt.search.WorkflowSearch"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
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

<%@ include file="../../init.jsp" %>

<%

	ServiceProcess serviceProcess  = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);

	long serviceProcessId = 0l;
	
	if (Validator.isNotNull(serviceProcess)) {
		serviceProcessId = serviceProcess.getServiceProcessId();
	}

	ProcessWorkflow workflow  = (ProcessWorkflow) request.getAttribute(WebKeys.PROCESS_WORKFLOW_ENTRY);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "process/action.jsp");

	boolean isPermission =
				    ProcessPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_PROCESS);
%>

<liferay-portlet:renderURL var="editActionURL" windowState="<%= LiferayWindowState.NORMAL.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_action.jsp" %>'/>
	<portlet:param name="serviceProcessId" value="<%= Validator.isNotNull(serviceProcess) ? Long.toString(serviceProcess.getServiceProcessId()) : StringPool.BLANK %>"/>
	<portlet:param name="processWorkflowId" value="<%= Validator.isNotNull(workflow) ? Long.toString(workflow.getProcessWorkflowId()) : StringPool.BLANK %>"/>
</liferay-portlet:renderURL>

<aui:button-row>
	<aui:button name="addAction" href="<%= editActionURL %>" value="add-action" ></aui:button>
</aui:button-row>

<liferay-ui:search-container searchContainer="<%= new WorkflowSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
		
	<liferay-ui:search-container-results>
		<%
			WorkflowSearchTerms searchTerms = (WorkflowSearchTerms) searchContainer.getSearchTerms();
		
			total = ProcessWorkflowLocalServiceUtil.countWorkflow(serviceProcessId); 

			results = ProcessWorkflowLocalServiceUtil.searchWorkflow(serviceProcessId, searchContainer.getStart(), searchContainer.getEnd());
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
		
	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row 
		className="org.opencps.processmgt.model.ProcessWorkflow" 
		modelVar="processWorkflow" 
		keyProperty="processWorkflowId"
	>
		<%
			// no column
			row.addText(String.valueOf(row.getPos() + 1));
		
			
			// Pre StepName
			row.addText(ProcessUtils.getProcessStepName(processWorkflow.getPreProcessStepId()));

			// workflow name
			row.addText(processWorkflow.getActionName());

			// Post StepName
			row.addText(ProcessUtils.getProcessStepName(processWorkflow.getPostProcessStepId()));
			
			if(isPermission) {
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "workflow_actions.jsp", config.getServletContext(), request, response);
			}
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator/>

</liferay-ui:search-container>

<aui:script use="liferay-util-window">
	Liferay.provide(window, 'addAction', function(action) {
		page = '<%= editActionURL %>'
		Liferay.Util.openWindow({
			dialog: {
				cache: false,
				centered: true,
				modal: true,
				resizable: false,
				width: 1000
			},
			id: 'addaction',
			title: 'adding-process-workflow',
			uri: page
		});
	});
</aui:script>

<aui:script>
	Liferay.provide(window, 'refreshPortlet', function() 
		{	
			window.location.reload();
		},
		['aui-dialog','aui-dialog-iframe']
	);
</aui:script>

<aui:script>
	Liferay.provide(window, 'closePopup', function(dialogId) 
		{
			var A = AUI();
			var dialog = Liferay.Util.Window.getById(dialogId);
			dialog.destroy();
		},
		['liferay-util-window']
	);
</aui:script>