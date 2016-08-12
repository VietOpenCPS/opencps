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
<%@page import="org.opencps.dossiermgt.search.DossierTemplateDisplayTerms"%>
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
	
	/* headerNames.add("row-no"); */
	headerNames.add("service-name");
	headerNames.add("govAgency-Name");
	headerNames.add("service-mode");
	
	boolean isPermission =
					ServiceConfigPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_SERVICE_CONFIG);

	long dossierTemplateId = dossierTemplate != null ? dossierTemplate.getDossierTemplateId() : 0L;
	
	int totalCount = 0;
	
	try{
		totalCount = ServiceConfigLocalServiceUtil.countByDossierTemplateId(dossierTemplateId);
	} catch (Exception e) {}
	
	if (isPermission) {
		headerNames.add("action");
	}
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
%>

<portlet:renderURL var="editServiceConfigURL" >
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_service_config.jsp" %>'/>
	<portlet:param name="redirectURL" value="<%=currentURL %>"/>
	<portlet:param name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID %>" value="<%=String.valueOf(dossierTemplateId) %>"/>
</portlet:renderURL>

<c:if test="<%=ServiceConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE_CONFIG) %>">
		<%-- <div id="<portlet:namespace/>toolbarResponse"></div> --%>
		<aui:button href="<%= editServiceConfigURL.toString() %>" value="add-service-config"/>
</c:if>
<div class="opencps-searchcontainer-wrapper">
	<liferay-ui:search-container searchContainer="<%= new ServiceConfigSearch(renderRequest, totalCount, iteratorURL) %>" 
		headerNames="<%= headers %>"> 
		
			<liferay-ui:search-container-results>
			<%
				serviceConfigs = ServiceConfigLocalServiceUtil.getServiceConfigs(dossierTemplateId);
				
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
			%>
			<liferay-util:buffer var="boundcol1">
				<div class="row-fluid">
					<div class="span1"></div>
					
					<div class="span2 bold-label">
						<liferay-ui:message key="service-name"/>
					</div>
					<div class="span9"><%=serviceInfo.getServiceName()%></div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="boundcol2">
				<div class="row-fluid">
					<div class="span1"></div>
					
					<div class="span2 bold-label">
						<liferay-ui:message key="govAgency-Name"/>
					</div>
					<div class="span9"><%=serviceConfig.getGovAgencyName() %> </div>
				</div>
				
				<div class="row-fluid">
					<div class="span1"></div>
					
					<div class="span2 bold-label">
						<liferay-ui:message key="service-mode"/>
					</div>
					<div class="span9"><%=serviceConfigModeName %> </div>
				</div>
			</liferay-util:buffer>
			<%
				row.setClassName("opencps-searchcontainer-row");
				row.addText(String.valueOf(row.getPos() + 1));
				row.addText(boundcol1);
				row.addText(boundcol2);
				if(isPermission) {
					row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "service_config_actions.jsp", config.getServletContext(), request, response);
				}
			%>
			
		</liferay-ui:search-container-row>
		
		<liferay-ui:search-iterator type="opencs_page_iterator" paginate="<%=false %>"/>
	</liferay-ui:search-container>
</div>
