<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
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
	ProcessWorkflow workflow = (ProcessWorkflow) row.getObject();
	ServiceProcess serviceProcess  = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
%> 

<liferay-portlet:renderURL var="editActionURL" windowState="<%= LiferayWindowState.NORMAL.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_action.jsp" %>'/>
	<portlet:param name="redirectURL" value="<%= currentURL %>"/>
	<portlet:param name="serviceProcessId" value="<%= Validator.isNotNull(serviceProcess) ? Long.toString(serviceProcess.getServiceProcessId()) : StringPool.BLANK %>"/>
	<portlet:param name="processWorkflowId" value="<%= Validator.isNotNull(workflow) ? Long.toString(workflow.getProcessWorkflowId()) : StringPool.BLANK %>"/>
</liferay-portlet:renderURL>
			
 <liferay-ui:icon-menu>
	<c:if test="<%= ProcessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_PROCESS) %>">
		<liferay-ui:icon image="edit" url="<%= editActionURL %>" />
 	</c:if>

 	<c:if test="<%= ProcessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
 		<portlet:actionURL var="deleteActionURL" name="deleteAction" >
			<portlet:param name="processWorkflowId" value="<%=String.valueOf(workflow.getProcessWorkflowId()) %>"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			<portlet:param name="returnURL" value="<%=currentURL %>"/>
		</portlet:actionURL> 
		<liferay-ui:icon-delete image="delete" confirmation="are-you-sure-delete-entry" message="delete" url="<%= deleteActionURL.toString() %>" />
 	</c:if>
</liferay-ui:icon-menu> 
