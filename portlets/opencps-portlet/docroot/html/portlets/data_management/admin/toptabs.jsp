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
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.datamgt.permissions.DictCollectionPermission"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="org.opencps.datamgt.permissions.DictItemPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.datamgt.util.DataMgtUtil"%>
<%@ include file="../init.jsp"%>

<%

	String[] names = new String[]{DataMgtUtil.TOP_TABS_DICTITEM, DataMgtUtil.TOP_TABS_DICTCOLLECTION};

	String value = ParamUtil.getString(request, "tabs1", DataMgtUtil.TOP_TABS_DICTITEM);

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			DictItemPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewDictItemURL = renderResponse.createRenderURL();
		viewDictItemURL.setParameter("mvcPath", templatePath + "dictitems.jsp");
		viewDictItemURL.setParameter("tabs1", DataMgtUtil.TOP_TABS_DICTITEM);
		urls.add(viewDictItemURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			DictCollectionPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewStaffURL = renderResponse.createRenderURL();
		viewStaffURL.setParameter("mvcPath", templatePath + "dictcollection.jsp");
		viewStaffURL.setParameter("tabs1", DataMgtUtil.TOP_TABS_DICTCOLLECTION);
		urls.add(viewStaffURL.toString());
	}
%>
<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
/>
