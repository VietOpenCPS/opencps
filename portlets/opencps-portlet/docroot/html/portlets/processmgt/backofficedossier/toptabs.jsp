<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierFilePermission"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
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

	String[] names = new String[]{ProcessMgtUtil.TOP_TABS_DOSSIERLIST, ProcessMgtUtil.TOP_TABS_DOSSIERFILELIST};

	String value = ParamUtil.getString(request, "tabs1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);

	List<String> urls = new ArrayList<String>();

	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewDossierListURL = renderResponse.createRenderURL();
		viewDossierListURL.setParameter("mvcPath", templatePath + "backofficedossierlist.jsp");
		viewDossierListURL.setParameter("tabs1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);
		urls.add(viewDossierListURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewDossierFileListURL = renderResponse.createRenderURL();
		viewDossierFileListURL.setParameter("mvcPath", templatePath + "backofficedossierfilelist.jsp");
		viewDossierFileListURL.setParameter("tabs1", ProcessMgtUtil.TOP_TABS_DOSSIERFILELIST);
		urls.add(viewDossierFileListURL.toString());
	}
	
%>
<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
/>