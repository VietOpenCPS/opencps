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
<%@page import="org.opencps.usermgt.permissions.WorkingUnitPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@ include file="../init.jsp"%>

<%
	ResultRow row =
		(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	WorkingUnit workingUnit = (WorkingUnit)row.getObject();
	String redirectURL = currentURL;
%>

<liferay-ui:icon-menu>
	<portlet:renderURL var="updateWorkingUnit">
		<portlet:param name="mvcPath"
			value="/html/portlets/usermgt/admin/edit_workingunit.jsp" />
		<portlet:param 
			name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ID%>"
			value="<%=String.valueOf(workingUnit.getWorkingunitId())%>" 
		/>
		<portlet:param name="redirectURL" value="<%=redirectURL%>" />
	</portlet:renderURL>
	
	<portlet:renderURL var="updateWorkingUnitChirl">
        <portlet:param name="mvcPath"
            value="/html/portlets/usermgt/admin/edit_workingunit.jsp" />
        <portlet:param 
            name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ID%>"
            value="<%=String.valueOf(workingUnit.getWorkingunitId())%>" 
        />
        <portlet:param name="isAddChild" value="isAddChild"/>
        <portlet:param name="redirectURL" value="<%=redirectURL%>" />
    </portlet:renderURL>
	
	<c:if test="<%=WorkingUnitPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<liferay-ui:icon image="edit" message="edit"
		url="<%=updateWorkingUnit.toString()%>" />
		
		<liferay-ui:icon image="add" message="add-child"
        url="<%=updateWorkingUnitChirl.toString()%>" />
	</c:if>

	<portlet:actionURL var="deleteWorkingUnitURL" name="deleteWorkingUnit">
		
		<portlet:param 
			name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ID%>"
			value="<%=String.valueOf(workingUnit.getWorkingunitId())%>" 
		/>
		
		<portlet:param name="redirectURL" value="<%=redirectURL%>" />
		
	</portlet:actionURL>
	<c:if test="<%=WorkingUnitPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
		
		<liferay-ui:icon image="delete" message="delete"
			url="<%=deleteWorkingUnitURL.toString()%>" />
		</c:if>
	
</liferay-ui:icon-menu>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.workingunit_action.jsp");
%>

