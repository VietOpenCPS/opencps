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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="org.opencps.usermgt.permissions.EmployeePermission"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../init.jsp"%>

 
<%
	ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Employee employee = (Employee) row.getObject();
%> 

			
 <liferay-ui:icon-menu>
 	<c:if test="<%=EmployeePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_EMPLOYEE) %>">
 		<portlet:renderURL var="updateEmployeeURL">
			<portlet:param name="mvcPath" value='<%=templatePath + "edit_employee.jsp" %>'/>
			<portlet:param name="<%=EmployeeDisplayTerm.EMPLOYEE_ID %>" value="<%=String.valueOf(employee.getEmployeeId()) %>"/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:renderURL> 
		
 		<liferay-ui:icon image="edit" message="edit" url="<%=updateEmployeeURL.toString() %>" /> 
 	</c:if>
 	
 	<c:if test="<%=EmployeePermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) 
 		&& employee.getWorkingStatus() == PortletConstants.WORKING_STATUS_DEACTIVATE%>">
 		<portlet:actionURL var="updateEmployeeWorkingStatusURL" name="updateEmployeeWorkingStatus">
			<portlet:param name="<%=EmployeeDisplayTerm.EMPLOYEE_ID %>" value="<%=String.valueOf(employee.getEmployeeId()) %>"/>
			<portlet:param name="<%=EmployeeDisplayTerm.WORKING_STATUS %>" value="<%=String.valueOf(employee.getWorkingStatus()) %>"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:actionURL> 
		<liferay-ui:icon image="activate" url="<%= updateEmployeeWorkingStatusURL %>"/>
 	</c:if>
 	
 	<c:if test="<%=EmployeePermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) 
 		&& employee.getWorkingStatus() == PortletConstants.WORKING_STATUS_ACTIVATE%>">
 		<portlet:actionURL var="updateEmployeeWorkingStatusURL" name="updateEmployeeWorkingStatus">
			<portlet:param name="<%=EmployeeDisplayTerm.EMPLOYEE_ID %>" value="<%=String.valueOf(employee.getEmployeeId()) %>"/>
			<portlet:param name="<%=EmployeeDisplayTerm.WORKING_STATUS %>" value="<%=String.valueOf(employee.getWorkingStatus()) %>"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:actionURL> 
		<liferay-ui:icon-deactivate url="<%=updateEmployeeWorkingStatusURL.toString() %>"/>
 	</c:if>
 	
 	<c:if test="<%=EmployeePermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE)
 		&& employee.getWorkingStatus() == PortletConstants.WORKING_STATUS_DEACTIVATE%>">
 		<portlet:actionURL var="deleteEmployeeURL" name="deleteEmployee" >
			<portlet:param name="<%=EmployeeDisplayTerm.EMPLOYEE_ID %>" value="<%=String.valueOf(employee.getEmployeeId()) %>"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:actionURL> 
		<liferay-ui:icon-delete image="delete" confirmation="are-you-sure-delete-entry" message="delete"  url="<%=deleteEmployeeURL.toString() %>" />
 	</c:if>
	  
</liferay-ui:icon-menu> 