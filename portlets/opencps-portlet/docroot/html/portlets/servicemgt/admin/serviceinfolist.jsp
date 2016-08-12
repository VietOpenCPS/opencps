<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearchTerms"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearch"%>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>

<%@ include file="../init.jsp" %>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "serviceinfolist.jsp");
	iteratorURL.setParameter("tabs1", ServiceUtil.TOP_TABS_SERVICE);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("no");
	/*	headerNames.add("service-no");
	headerNames.add("service-name");
	headerNames.add("service-domain");
	headerNames.add("service-administrator");
	headerNames.add("status");
 */
	boolean isPermission =
				    ServicePermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_SERVICE);

	if (isPermission) {
		headerNames.add("action");
	}
	
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
%>

<liferay-ui:error key="<%=MessageKeys.SERVICE_DELERR_EXITS_SERVICECONFIG %>" message="<%=MessageKeys.SERVICE_DELERR_EXITS_SERVICECONFIG %>"/>
<liferay-ui:error key="<%= MessageKeys.SERVICE_DELERR_EXITS_DOSSIER %>" message="<%=MessageKeys.SERVICE_DELERR_EXITS_DOSSIER %>" />
<liferay-ui:error key="<%= MessageKeys.SERVICE_DELERR_EXITS_PROCESSORDER %>" message="<%=MessageKeys.SERVICE_DELERR_EXITS_DOSSIER %>"/>

<liferay-ui:success key="<%=MessageKeys.SERVICE_DELSUCC %>" message="<%=MessageKeys.SERVICE_DELSUCC %>"/>


<div class="ocps-serviceinfo-list">
<c:if test="<%= ServicePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE) %>">
	<liferay-util:include page='<%= templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
</c:if>

<liferay-util:include page='<%= templatePath + "toolbar.jsp"%>' servletContext="<%=application %>" />

<div class="ocps-serviceinfo-search">
<liferay-ui:search-container searchContainer="<%= new ServiceSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>">
		
	<liferay-ui:search-container-results>
		<%
			ServiceSearchTerms searchTerms = (ServiceSearchTerms) searchContainer.getSearchTerms();

			total = ServiceInfoLocalServiceUtil.countService(scopeGroupId, searchTerms.getKeywords(), 
				searchTerms.getAdministrationCode(), searchTerms.getDomainCode());

			results = ServiceInfoLocalServiceUtil.searchService(scopeGroupId, searchTerms.getKeywords(), 
				searchTerms.getAdministrationCode(), searchTerms.getDomainCode(),
				searchContainer.getStart(), searchContainer.getEnd());
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
		
	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row 
		className="org.opencps.servicemgt.model.ServiceInfo" 
		modelVar="service" 
		keyProperty="serviceinfoId"
	>
		<%
			PortletURL editURL = renderResponse.createRenderURL();
			editURL.setParameter("mvcPath", templatePath + "edit_service-ux.jsp");
			editURL.setParameter("serviceinfoId", String.valueOf(service.getServiceinfoId()));
			editURL.setParameter("backURL", currentURL);
			
			// no column
			row.addText(String.valueOf(row.getPos() + 1), editURL);
		
			/* // service no
			row.addText(service.getServiceNo(), editURL);
			
			// service name
			row.addText(service.getServiceName(), editURL); */
			
			// service domain
			/* row.addText(DictItemUtil.getNameDictItem(service.getDomainCode()) , editURL);
			// service admin
			row.addText(DictItemUtil.getNameDictItem(service.getAdministrationCode()), editURL);
	 */		
			//service status
			
			/* int status = service.getActiveStatus();
			String statusMess = StringPool.BLANK;
			if(status == 0) {
				statusMess = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-private");
			} else if(status == 1) {
				statusMess = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-public");
			} else if(status == 2) {
				statusMess = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-outdate");
			}
			
			row.addText(statusMess , editURL); */
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  templatePath + "service_admin_bound_data_col2.jsp", config.getServletContext(), request, response);
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  templatePath + "service_admin_bound_data.jsp", config.getServletContext(), request, response);
			if(isPermission) {
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "service_actions.jsp", config.getServletContext(), request, response);
			}
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator type="opencs_page_iterator"/>

</liferay-ui:search-container>
</div>
</div>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.servicemgt.admin.serviceinfo.jsp");
%>
