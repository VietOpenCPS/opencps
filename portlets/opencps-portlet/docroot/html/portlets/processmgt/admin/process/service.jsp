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
	ServiceProcess serviceProcess = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);

%>

<liferay-portlet:renderURL var="editServiceURL" windowState="<%= LiferayWindowState.NORMAL.toString() %>">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_service.jsp" %>'/>
	<portlet:param name="serviceProcessId" value="<%= Validator.isNotNull(serviceProcess) ? Long.toString(serviceProcess.getServiceProcessId()) : StringPool.BLANK %>"/>
</liferay-portlet:renderURL>

<aui:button-row>
	<aui:button name="addAction" href="<%= editServiceURL %>" value="add-service" ></aui:button>
</aui:button-row>

<portlet:actionURL name="sendMessage" var="sendMessagenURL"/>


