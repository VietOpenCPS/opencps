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

<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearchTerms"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearch"%>

<%@ include file="../init.jsp" %>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "serviceinfolist.jsp");
	iteratorURL.setParameter("tabs1", ServiceUtil.TOP_TABS_SERVICE);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-index");
	headerNames.add("service-info");
	headerNames.add("service-domain-administrator");

	boolean isPermission =
		ServicePermission.contains(
			themeDisplay.getPermissionChecker(),
			themeDisplay.getScopeGroupId(), ActionKeys.ADD_SERVICE);

	if (isPermission) {
		headerNames.add("action");
	}

	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
%>

<liferay-ui:error 
	key="<%=MessageKeys.SERVICE_DELERR_EXITS_SERVICECONFIG %>" 
	message="<%=MessageKeys.SERVICE_DELERR_EXITS_SERVICECONFIG %>"
/>

<liferay-ui:error 
	key="<%= MessageKeys.SERVICE_DELERR_EXITS_DOSSIER %>" 
	message="<%=MessageKeys.SERVICE_DELERR_EXITS_DOSSIER %>" 
/>

<liferay-ui:error 
	key="<%= MessageKeys.SERVICE_DELERR_EXITS_PROCESSORDER %>" 
	message="<%=MessageKeys.SERVICE_DELERR_EXITS_DOSSIER %>"
/>

<liferay-ui:success 
	key="<%=MessageKeys.SERVICE_DELSUCC %>" 
	message="<%=MessageKeys.SERVICE_DELSUCC %>"
/>

<c:if test="<%= ServicePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE) %>">
	<liferay-util:include page='<%= templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
</c:if>

<liferay-util:include page='<%= templatePath + "toolbar.jsp"%>' servletContext="<%=application %>" />

<div class="opencps-searchcontainer-wrapper default-box-shadow radius8">

	<liferay-ui:search-container 
		searchContainer="<%= new ServiceSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
		headerNames="<%= headers %>"
	>
			
		<liferay-ui:search-container-results>
			<%
				ServiceSearchTerms searchTerms =
					(ServiceSearchTerms) searchContainer.getSearchTerms();

				total =
					ServiceInfoLocalServiceUtil.countService(
						scopeGroupId, searchTerms.getKeywords(),
						searchTerms.getAdministrationCode(),
						searchTerms.getDomainCode());

				results =
					ServiceInfoLocalServiceUtil.searchService(
						scopeGroupId, searchTerms.getKeywords(),
						searchTerms.getAdministrationCode(),
						searchTerms.getDomainCode(),
						searchContainer.getStart(),
						searchContainer.getEnd());

				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
			
		</liferay-ui:search-container-results>
	
		<liferay-ui:search-container-row 
			className="org.opencps.servicemgt.model.ServiceInfo" 
			modelVar="serviceInfo" 
			keyProperty="serviceinfoId"
		>
		
			<%
				PortletURL editURL = renderResponse.createRenderURL();
				editURL.setParameter("mvcPath", templatePath + "edit_service-ux.jsp");
				editURL.setParameter("serviceinfoId", String.valueOf(serviceInfo.getServiceinfoId()));
				editURL.setParameter("backURL", currentURL);
			
				int status = serviceInfo.getActiveStatus();
				String statusLabel = StringPool.BLANK;
				
				if(status == 0) {
					statusLabel = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-private");
				} else if(status == 1) {
					statusLabel = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-public");
				} else if(status == 2) {
					statusLabel = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "service-outdate");
				}
			
			%>

			<liferay-util:buffer var="rowIndex">
				<div class="row-fluid min-width10">
					<div class="span12 bold">
						<%=row.getPos() + 1 %>
					</div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="service">
				<div class="row-fluid">
					<div class="span3 bold">
						<liferay-ui:message key="service-no" />
					</div>
					<div class="span9">
						<%= serviceInfo.getServiceNo() %>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span3 bold">
						<liferay-ui:message key="service-name" />
					</div>
					<div class="span9">
						<%= serviceInfo.getServiceName() %>
					</div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="domain">
				<div class="row-fluid">
					<div class="span5 bold">
						<liferay-ui:message key="service-domain" />
					</div>
					<div class="span7">
						<%= DictItemUtil.getNameDictItem(serviceInfo.getDomainCode()) %>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span5 bold">
						<liferay-ui:message key="service-administrator" />
					</div>
					<div class="span7">
						<%=DictItemUtil.getNameDictItem(serviceInfo.getAdministrationCode()) %>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span5 bold">
						<liferay-ui:message key="status" />
					</div>
					<div class="span7">
						<%=statusLabel %>
					</div>
				</div>
			</liferay-util:buffer>
			<%
				row.setClassName("opencps-searchcontainer-row");
				
				row.addText(rowIndex);
				
				row.addText(service);
				
				row.addText(domain);
				
				if(isPermission) {
					
					row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "service_actions.jsp", config.getServletContext(), request, response);
				}
			%>	
		
		</liferay-ui:search-container-row>	
	
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	
	</liferay-ui:search-container>
</div>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.servicemgt.admin.serviceinfo.jsp");
%>
