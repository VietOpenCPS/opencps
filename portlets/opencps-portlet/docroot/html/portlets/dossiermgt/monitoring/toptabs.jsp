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

	String[] names = new String[]{DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SEARCH, 
		DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_NEW_UPDATE,
		DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_NEW_LOG,
		DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_LIST};
	String value = 
		ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SEARCH);

	List<String> urls = new ArrayList<String>();

	PortletURL viewDossierMonitoringSearchURL = renderResponse.createRenderURL();
	viewDossierMonitoringSearchURL.setParameter("mvcPath", templatePath + "dossiermonitoringsearch.jsp");
	viewDossierMonitoringSearchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_SEARCH);
	urls.add(viewDossierMonitoringSearchURL.toString());

	PortletURL viewDossierMonitoringNewUpdateURL = renderResponse.createRenderURL();
	viewDossierMonitoringNewUpdateURL.setParameter("mvcPath", templatePath + "dossiermonitoringnewupdate.jsp");
	viewDossierMonitoringNewUpdateURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_NEW_UPDATE);
	urls.add(viewDossierMonitoringNewUpdateURL.toString());
	
	PortletURL viewDossierMonitoringNewLogURL = renderResponse.createRenderURL();
	viewDossierMonitoringNewLogURL.setParameter("mvcPath", templatePath + "dossiermonitoringnewlog.jsp");
	viewDossierMonitoringNewLogURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_NEW_LOG);
	urls.add(viewDossierMonitoringNewLogURL.toString());
	
	PortletURL viewDossierMonitoringListURL = renderResponse.createRenderURL();
	viewDossierMonitoringListURL.setParameter("mvcPath", templatePath + "dossiermonitoringlist.jsp");
	viewDossierMonitoringListURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_MONITORING_LIST);
	urls.add(viewDossierMonitoringListURL.toString());
%>

<liferay-ui:tabs
	names="<%= StringUtil.merge(names) %>"
	param="tabs1"
	url0="<%=urls != null && urls.size() > 0 ? urls.get(0): StringPool.BLANK %>"
 	url1="<%=urls != null && urls.size() > 1 ? urls.get(1): StringPool.BLANK %>"
 	url2="<%=urls != null && urls.size() > 2 ? urls.get(2): StringPool.BLANK %>"
 	url3="<%=urls != null && urls.size() > 3 ? urls.get(3): StringPool.BLANK %>"
/>

