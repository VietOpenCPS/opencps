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
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.dossiermgt.permissions.ServiceConfigPermission"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigSearch"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigSearchTerm"%>
<%@page import="org.opencps.processmgt.service.ServiceProcessLocalServiceUtil"%>

<liferay-util:include page='<%= templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
<c:if test="<%=ServiceConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE_CONFIG) %>">
    <liferay-util:include page='<%= templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />
</c:if>
<%

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier_common/dossierpartlist.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_SERVICE_CONFIG);
	
	List<ServiceConfig> serviceConfigs = new ArrayList<ServiceConfig>();
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-no");
	headerNames.add("service-name");
	headerNames.add("govAgency-Name");
	headerNames.add("template-name");
	headerNames.add("service-mode");
	headerNames.add("process");
	
	boolean isPermission =
					ServiceConfigPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_SERVICE_CONFIG);

	
	int totalCount = 0;
	if (isPermission) {
		headerNames.add("action");
	}
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	String govAdencyCode = ParamUtil.getString(request, ServiceConfigDisplayTerms.SERVICE_CONFIG_GOVAGENCYCODE);
%>

<c:if test="<%=ServiceConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE_CONFIG) %>">
		<div id="<portlet:namespace/>toolbarResponse"></div>
</c:if>

<liferay-ui:search-container searchContainer="<%= new ServiceConfigSearch (renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>"> 
	
		<liferay-ui:search-container-results>
		<%
			ServiceConfigSearchTerm searchTerm = (ServiceConfigSearchTerm) searchContainer.getSearchTerms();
			
			serviceConfigs = ServiceConfigLocalServiceUtil.searchServiceConfig(
				scopeGroupId, searchTerm.getKeywords(), govAdencyCode, 
				searchTerm.getDomainCode(), searchContainer.getStart(), searchContainer.getEnd());						
			
			totalCount = ServiceConfigLocalServiceUtil.countServiceConfig(
				scopeGroupId, searchTerm.getKeywords(), govAdencyCode, 
				searchTerm.getDomainCode());
			
			total = totalCount;
			results = serviceConfigs;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.dossiermgt.model.ServiceConfig" 
		modelVar="serviceConfig" 
		keyProperty="serviceConfigId"
	>
		<%
			ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceConfig.getServiceInfoId());
			DossierTemplate dossierTemplate = DossierTemplateLocalServiceUtil.getDossierTemplate(serviceConfig.getDossierTemplateId());
		
			String serviceConfigModeName = LanguageUtil.get(portletConfig ,themeDisplay.getLocale(), DossierMgtUtil.getNameOfServiceConfigMode(serviceConfig.getServiceMode(), themeDisplay.getLocale()));
			
			int countProcess = ServiceProcessLocalServiceUtil.countByG_T(themeDisplay.getScopeGroupId(), dossierTemplate.getDossierTemplateId());
			
			String process = "<i class=\"opencps-icon checked\"></i>";
			
			if(countProcess == 0) {
				process = "<i class=\"opencps-icon removed\"></i>";
			}
			
			row.addText(String.valueOf(row.getPos() + 1));
			row.addText(serviceInfo.getServiceName());
			row.addText(serviceConfig.getGovAgencyName());
			row.addText(dossierTemplate.getTemplateName());
			row.addText(serviceConfigModeName);
			row.addText(process);
			if(isPermission) {
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "service_config_actions.jsp", config.getServletContext(), request, response);
			}
		%>
		
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>