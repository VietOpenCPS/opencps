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
<%@page import="org.opencps.dossiermgt.permission.DossierPartPermission"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.permission.DossierTemplatePermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@ include file="../init.jsp"%>

<%

	String[] names = new String[]{DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE, 
	DossierMgtUtil.TOP_TABS_DOSSIER_SERVICE};
	String value = 
		ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE);

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(),
		ActionKeys.VIEW) && 
			DossierTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW) &&
				DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewDossierURL = renderResponse.createRenderURL();
		viewDossierURL.setParameter("mvcPath", templatePath + "dossiertemplatelist.jsp");
		viewDossierURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE);
		urls.add(viewDossierURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), 
		ActionKeys.VIEW) && 
					DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
				PortletURL viewDossierPartURL = renderResponse.createRenderURL();
				viewDossierPartURL.setParameter("mvcPath", templatePath + "edit_dossier_part.jsp");
				viewDossierPartURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_SERVICE);
				urls.add(viewDossierPartURL.toString());
	} 
%>

<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
 	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
/>

