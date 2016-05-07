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

<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.permissions.ServiceConfigPermission"%>

<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigSearchTerm"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@ include file="../../init.jsp"%>

<%
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier_common/dossierpartlist.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.SERVICE_CONFIG_TOOLBAR);
	
	List<ServiceConfig> serviceConfigs = new ArrayList<ServiceConfig>();
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-no");
	headerNames.add("service-name");
	headerNames.add("govAgency-Name");
	headerNames.add("service-mode");
	
	boolean isPermission =
					ServiceConfigPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_SERVICE_CONFIG);

	long dossierTemplateId = dossierTemplate != null ? dossierTemplate.getDossierTemplateId() : 0L;
	
	int totalCount = 0;
	if (isPermission) {
		headerNames.add("action");
	}
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
%>


<c:if test="<%=ServiceConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE_CONFIG) %>">
		<div id="<portlet:namespace/>toolbarResponse"></div>
</c:if>

<liferay-ui:search-container searchContainer="<%= new ServiceConfigSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>"> 
	
		<liferay-ui:search-container-results>
		<%
			serviceConfigs = ServiceConfigLocalServiceUtil.getServiceConfigs(dossierTemplateId);
									
			totalCount = ServiceConfigLocalServiceUtil.countByDossierTemplateId(dossierTemplateId);
			
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
			
			String serviceConfigModeName = LanguageUtil.get(portletConfig ,themeDisplay.getLocale(), DossierMgtUtil.getNameOfServiceConfigMode(serviceConfig.getServiceMode(), themeDisplay.getLocale()));
		
			row.addText(String.valueOf(row.getPos() + 1));
			row.addText(serviceInfo.getServiceName());
			row.addText(serviceConfig.getGovAgencyName());
			row.addText(serviceConfigModeName);
			if(isPermission) {
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "service_config_actions.jsp", config.getServletContext(), request, response);
			}
		%>
		
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

