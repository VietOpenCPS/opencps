
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

<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.bean.DossierBean"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierTemplateException"%>
<%@page import="org.opencps.dossiermgt.RequiredDossierPartException"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.util.DictItemUtil"%>


<%@ include file="../init.jsp"%>


<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />

<%
	//get config to load jsp display dossier
	String templatesToDisplay_cfg = GetterUtil.getString(portletPreferences.getValue("templatesToDisplay", "default"));

%>

<liferay-util:include page='<%=templatePath + "display/" + templatesToDisplay_cfg + ".jsp" %>' servletContext="<%=application %>" />

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.frontofficedossierlist.jsp");
%>
