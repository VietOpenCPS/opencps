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
<%@ include file="../init.jsp"%>
<%@page import="com.liferay.portal.security.permission.ActionKeys"%>
<%@page import="org.opencps.usermgt.permissions.EmployeePermission"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.usermgt.permissions.WorkingUnitPermission"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%

	String[] names = new String[]{UserMgtUtil.TOP_TABS_WORKINGUNIT, UserMgtUtil.TOP_TABS_EMPLOYEE};

	String value = ParamUtil.getString(request, "tabs1", UserMgtUtil.TOP_TABS_WORKINGUNIT);

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			WorkingUnitPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewWorkingUnitsURL = renderResponse.createRenderURL();
		viewWorkingUnitsURL.setParameter("mvcPath", templatePath + "workingunitdirectory.jsp");
		viewWorkingUnitsURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_WORKINGUNIT);
		urls.add(viewWorkingUnitsURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			EmployeePermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewEmployeesURL = renderResponse.createRenderURL();
		viewEmployeesURL.setParameter("mvcPath", templatePath + "employees.jsp");
		viewEmployeesURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_EMPLOYEE);
		urls.add(viewEmployeesURL.toString());
	}
	
%>
<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
/>