
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
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierFilePermission"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@ include file="../init.jsp"%>

<%

	//String[] names = new String[]{DossierMgtUtil.TOP_TABS_DOSSIER, DossierMgtUtil.TOP_TABS_DOSSIER_FILE, DossierMgtUtil.TOP_TABS_EXTERNAL_DOSSIER};
	
	String[] names = null;
	if(!hideTabDossierFile) {
		names =new String[]{DossierMgtUtil.TOP_TABS_DOSSIER, DossierMgtUtil.TOP_TABS_DOSSIER_FILE};	
	} else {
		names =new String[]{DossierMgtUtil.TOP_TABS_DOSSIER};
	}
	String value = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);

	List<String> urls = new ArrayList<String>();
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewDossierURL = renderResponse.createRenderURL();
		viewDossierURL.setParameter("mvcPath", templatePath + "frontofficedossierlist.jsp");
		viewDossierURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
		urls.add(viewDossierURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			DossierFilePermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewDossierFileURL = renderResponse.createRenderURL();
		viewDossierFileURL.setParameter("mvcPath", templatePath + "frontofficedossierfilelist.jsp");
		viewDossierFileURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_FILE);
		urls.add(viewDossierFileURL.toString());
	}
	
	if (PortletPermissionUtil.contains(permissionChecker, plid, portletDisplay.getId(), ActionKeys.VIEW) && 
			DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)) {
		PortletURL viewExternalDossierURL = renderResponse.createRenderURL();
		viewExternalDossierURL.setParameter("mvcPath", templatePath + "frontofficeexternaldossierlist.jsp");
		viewExternalDossierURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_FILE);
		urls.add(viewExternalDossierURL.toString());
	}
%>
<%-- <div class="opencps-toptabs">
	<div class="container">
		<liferay-ui:tabs  
			names="<%= StringUtil.merge(names) %>"
			param="tabs1"
			url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
			url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
			url2="<%=urls != null && urls.size() > 2 ? urls.get(2): StringPool.BLANK %>"
		/>
	</div>
</div> --%>

<div class="opencps-toptabs">
	<div class="container">
		<c:choose>
			<c:when test="<%= hideTabDossierFile %>">
				<liferay-ui:tabs  
					names="<%= StringUtil.merge(names) %>"
					param="tabs1"
					url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
				/>
			</c:when>
			<c:otherwise>
				<liferay-ui:tabs  
					names="<%= StringUtil.merge(names) %>"
					param="tabs1"
					url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
					url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
				/>
			</c:otherwise>
		</c:choose>
	</div>
</div>


