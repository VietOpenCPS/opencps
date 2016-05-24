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

<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@ include file="../init.jsp"%>

<%
	String[] names = new String[]{ DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SEARCH, DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_DOSSIER_FILE_LIST, DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SERVICE };

	String value = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SEARCH);

	List<String> urls = new ArrayList<String>();

	PortletURL viewMonitoringSearchURL = renderResponse.createRenderURL();
	viewMonitoringSearchURL.setParameter("mvcPath", templatePath + "dossiermonitoringsearch.jsp");
	viewMonitoringSearchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SEARCH);
	urls.add(viewMonitoringSearchURL.toString());	
	
	PortletURL viewMonitoringDossierFileListURL = renderResponse.createRenderURL();
	viewMonitoringDossierFileListURL.setParameter("mvcPath", templatePath + "dossiermonitoringdossierfilelist.jsp");
	viewMonitoringDossierFileListURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_DOSSIER_FILE_LIST);
	urls.add(viewMonitoringDossierFileListURL.toString());	
	
	PortletURL viewMonitoringServiceURL = renderResponse.createRenderURL();
	viewMonitoringServiceURL.setParameter("mvcPath", templatePath + "dossiermonitoringservice.jsp");
	viewMonitoringServiceURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SERVICE);
	urls.add(viewMonitoringServiceURL.toString());		
%>
<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
	url1="<%=urls != null && urls.size() > 0 ? urls.get(1): StringPool.BLANK %>"
	url2="<%=urls != null && urls.size() > 0 ? urls.get(2): StringPool.BLANK %>"
/>