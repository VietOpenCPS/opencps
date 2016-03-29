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

<%

	String[] names = new String[]{ServiceUtil.TOP_TABS_SERVICE, 
		ServiceUtil.TOP_TABS_TEMPLATE, ServiceUtil.TOP_TABS_DOMAIN,
		ServiceUtil.TOP_TABS_ADMINISTRATION};
	System.out.print(names);
	String value = 
		ParamUtil.getString(request, "tabs1", ServiceUtil.TOP_TABS_SERVICE);

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(),
		ActionKeys.VIEW) && 
			ServicePermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewServiceURL = renderResponse.createRenderURL();
		viewServiceURL.setParameter("mvcPath", templatePath + "serviceinfolist.jsp");
		viewServiceURL.setParameter("tabs1", ServiceUtil.TOP_TABS_SERVICE);
		urls.add(viewServiceURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), 
		ActionKeys.VIEW) && 
					DocumentPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
				PortletURL viewTemplareURL = renderResponse.createRenderURL();
				viewTemplareURL.setParameter("mvcPath", templatePath + "servicetemplatefilelist.jsp");
				viewTemplareURL.setParameter("tabs1", ServiceUtil.TOP_TABS_TEMPLATE);
				urls.add(viewTemplareURL.toString());
	}

	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), 
		ActionKeys.VIEW) && 
					CategoryPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
				PortletURL viewCategoryURL = renderResponse.createRenderURL();
				viewCategoryURL.setParameter("mvcPath", templatePath + "sevicedomainlist.jsp");
				viewCategoryURL.setParameter("tabs1", ServiceUtil.TOP_TABS_DOMAIN);
				urls.add(viewCategoryURL.toString());
	}

	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), 
		ActionKeys.VIEW) && 
					LevelPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
				PortletURL viewLevelURL = renderResponse.createRenderURL();
				viewLevelURL.setParameter("mvcPath", templatePath + "serviceadministrationlist.jsp");
				viewLevelURL.setParameter("tabs1", ServiceUtil.TOP_TABS_ADMINISTRATION);
				urls.add(viewLevelURL.toString());
	}
	
%>

<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
	url2="<%=urls != null && urls.size() > 2 ? urls.get(2): StringPool.BLANK %>"
	url3="<%=urls != null && urls.size() > 3 ? urls.get(3): StringPool.BLANK %>"
/>
