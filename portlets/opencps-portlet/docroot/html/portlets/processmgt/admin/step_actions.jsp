<%@page import="org.opencps.processmgt.model.ProcessStep"%>
<%@page import="org.opencps.processmgt.permissions.ProcessPermission"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
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

<%@ include file="../init.jsp"%>

<%
	ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	ProcessStep step = (ProcessStep) row.getObject();
	ServiceProcess serviceProcess  = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
%> 

<liferay-portlet:renderURL var="editStepURL" windowState="<%= LiferayWindowState.NORMAL.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_step.jsp" %>'/>
	<portlet:param name="redirectURL" value="<%= currentURL %>"/>
	<portlet:param name="serviceProcessId" value="<%= Validator.isNotNull(serviceProcess) ? Long.toString(serviceProcess.getServiceProcessId()) : StringPool.BLANK %>"/>
	<portlet:param name="processStepId" value="<%= Validator.isNotNull(step) ? Long.toString(step.getProcessStepId()) : StringPool.BLANK %>"/>
</liferay-portlet:renderURL>
			
 <liferay-ui:icon-menu>
	<c:if test="<%= ProcessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_PROCESS) %>">
		<liferay-ui:icon image="edit" url="<%= editStepURL %>" />
 	</c:if>

 	<c:if test="<%= ProcessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
 		<portlet:actionURL var="deleteStepURL" name="deleteStep" >
			<portlet:param name="processStepId" value="<%=String.valueOf(step.getProcessStepId()) %>"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			<portlet:param name="currentURL" value="<%=currentURL %>"/>
		</portlet:actionURL> 
		<liferay-ui:icon-delete image="delete" confirmation="are-you-sure-delete-entry" message="delete" url="<%= deleteStepURL.toString() %>" />
 	</c:if>
</liferay-ui:icon-menu> 

<aui:script use="liferay-util-window">
	Liferay.provide(window, 'showDialog', function(action) {
		page = '<%=editStepURL%>'
		Liferay.Util.openWindow({
			dialog: {
				cache: false,
				centered: true,
				modal: true,
				resizable: false,
				width: 1000
			},
			id: 'addstep',
			title: 'adding-process-step',
			uri: page
		});
	});
</aui:script>
