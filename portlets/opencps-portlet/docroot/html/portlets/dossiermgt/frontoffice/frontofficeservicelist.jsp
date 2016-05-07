<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
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

<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearch"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.dossiermgt.bean.Service"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.portlet.PortletURL"%>
<%@ include file="../init.jsp"%>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficeservicelist.jsp");
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	List<Service> services =  new ArrayList<Service>();
	
	List<ServiceConfig> serviceConfigs = new ArrayList<ServiceConfig>();
	
	int totalCount = 0;
	
%>
<liferay-ui:header
	backURL="<%= backURL %>"
	title="service-list"
/>


<liferay-ui:search-container searchContainer="<%= new ServiceSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			ServiceSearchTerms searchTerms = (ServiceSearchTerms)searchContainer.getSearchTerms();
		
			String[] itemNames = null;
			
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				itemNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			try{
				
				%>
					<%@include file="/html/portlets/dossiermgt/frontoffice/service_search_results.jspf" %>
				<%
			}catch(Exception e){
				_log.error(e);
			}
		
			total = 0;
			results = services;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.bean.Service" 
			modelVar="service" 
			keyProperty="serviceConfigId"
		>
			<%

				//id column
				row.addText(String.valueOf(row.getPos() + 1));

				row.addText(service.getServiceName());
				row.addText(service.getDomainCode());
				row.addText(service.getGovAgencyName());
				
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN,"/html/portlets/dossiermgt/frontoffice/service_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>


<liferay-ui:search-container searchContainer="<%= new ServiceSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			ServiceSearchTerms searchTerms = (ServiceSearchTerms)searchContainer.getSearchTerms();
		
			String[] itemNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				itemNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			try{
				
				%>
					<%@include file="/html/portlets/dossiermgt/frontoffice/service_search_results.jspf" %>
				<%
			}catch(Exception e){
				_log.error(e);
			}
		
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
				ServiceInfo serviceInfo = null;
			
				try{
					serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceConfig.getServiceInfoId());
				}catch(Exception e){
					
				}
				
				//id column
				row.addText(String.valueOf(row.getPos() + 1));

				row.addText(serviceInfo != null ? serviceInfo.getServiceName() : StringPool.BLANK);
				
				row.addText(serviceConfig.getDomainCode());
				
				row.addText(serviceConfig.getGovAgencyName());
				
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN,"/html/portlets/dossiermgt/frontoffice/service_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.frontofficeservicelist.jsp");
%>
