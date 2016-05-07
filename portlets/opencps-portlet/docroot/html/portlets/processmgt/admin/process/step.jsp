<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
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

	long serviceProcessId = 0;
	
	if (Validator.isNotNull(serviceProcess)) {
		serviceProcessId = serviceProcess.getServiceProcessId();
	}

	ProcessStep processStep  = (ProcessStep) request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "process/step.jsp");

	boolean isPermission =
				    ProcessPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_PROCESS);

%>

<liferay-portlet:renderURL var="editStepURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_step.jsp" %>'/>
	<portlet:param name="serviceProcessId" value="<%= Validator.isNotNull(serviceProcess) ? Long.toString(serviceProcess.getServiceProcessId()) : StringPool.BLANK %>"/>
	<portlet:param name="processStepId" value="<%= Validator.isNotNull(processStep) ? Long.toString(processStep.getProcessStepId()) : StringPool.BLANK %>"/>
</liferay-portlet:renderURL>

<liferay-portlet:renderURL var="editStepInlineURL" windowState="<%= LiferayWindowState.NORMAL.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_step.jsp" %>'/>
	<portlet:param name="redirectURL" value="<%= currentURL %>"/>
	<portlet:param name="serviceProcessId" value="<%= Validator.isNotNull(serviceProcess) ? Long.toString(serviceProcess.getServiceProcessId()) : StringPool.BLANK %>"/>
	<portlet:param name="processStepId" value="<%= Validator.isNotNull(processStep) ? Long.toString(processStep.getProcessStepId()) : StringPool.BLANK %>"/>
</liferay-portlet:renderURL>

<aui:button-row>
	<aui:button name="addStep" onClick="showDialog()" value="add-step" ></aui:button>
</aui:button-row>

<aui:button-row>
	<aui:button name="addStep" href="<%= editStepInlineURL.toString() %>" value="add-step-inline" ></aui:button>
</aui:button-row>

<liferay-ui:search-container searchContainer="<%= new StepSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
		
	<liferay-ui:search-container-results>
		<%
		
			StepSearchTerms searchTerms = (StepSearchTerms) searchContainer.getSearchTerms();

			total = ProcessStepLocalServiceUtil.countStepByProcess(serviceProcessId); 

			results = ProcessStepLocalServiceUtil.getStepByProcess(serviceProcessId,
				searchContainer.getStart(), searchContainer.getEnd());
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
		
	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row 
		className="org.opencps.processmgt.model.ProcessStep" 
		modelVar="step" 
		keyProperty="processStepId"
	>
		<%
			// no column
			row.addText(String.valueOf(row.getPos() + 1));
		
			// step no
			row.addText(step.getStepName());
			
			// step name
			row.addText(DictItemUtil.getNameDictItem(step.getDossierStatus()));
			
			// step duration
			row.addText(Integer.toString(step.getDaysDuration()));

			if(isPermission) {
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "step_actions.jsp", config.getServletContext(), request, response);
			}
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator/>

</liferay-ui:search-container>


<aui:script use="liferay-util-window">
	Liferay.provide(window, 'showDialog', function(action) {
		page = '<%= editStepURL %>'
		Liferay.Util.openWindow({
			dialog: {
				cache: false,
				centered: true,
				modal: true,
				resizable: false,
				width: 1000
			},
			id: 'addstep',
			title: Liferay.Language.get("adding-process-step"),
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
