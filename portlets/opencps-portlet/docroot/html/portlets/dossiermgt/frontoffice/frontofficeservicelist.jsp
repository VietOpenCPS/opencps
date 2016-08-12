
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

<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.dossiermgt.bean.ServiceBean"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearch"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@ include file="../init.jsp"%>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficeservicelist.jsp");
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	List<ServiceBean> serviceBeans =  new ArrayList<ServiceBean>();
	
	List<ServiceBean> serviceBeansRecent =  new ArrayList<ServiceBean>();
	
	int totalCount = 0;
	
%>
<liferay-ui:header
	backURL="<%= backURL %>"
	title="service-list"
/>

<h5>
	<liferay-ui:message key="service-recent"/>
</h5>


<liferay-ui:search-container searchContainer="<%= new ServiceSearch(renderRequest, 5, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			try{
				serviceBeansRecent = ServiceConfigLocalServiceUtil.getServiceConfigRecent(scopeGroupId, themeDisplay.getUserId(), 1, -1, -1, citizen != null ? 1 : -1, business != null ? 1 :-1, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
				
			}catch(Exception e){}
			total = serviceBeansRecent.size();
			results = serviceBeansRecent;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
			
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.bean.ServiceBean" 
			modelVar="serviceBean" 
			keyProperty="serviceConfigId"
		>
			<%
				
				//id column
				row.addText(String.valueOf(row.getPos() + 1 + searchContainer.getStart()));

				row.addText(Validator.isNotNull(serviceBean.getServiceName()) ? serviceBean.getServiceName() : StringPool.BLANK);
				
				DictItem dictItem = null;
				String domainName = StringPool.DASH;
				
				try{
					dictItem = DictItemLocalServiceUtil.getDictItem(GetterUtil.getLong(serviceBean.getDomainCode()));
					domainName = dictItem.getItemName(locale);
				}catch(Exception e){
					
				}
				
				row.addText(domainName);
				
				row.addText(serviceBean.getGovAgencyName());
				
				row.addText(String.valueOf(serviceBean.getLevel()));
				
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN,"/html/portlets/dossiermgt/frontoffice/service_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>

<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<liferay-ui:search-container searchContainer="<%= new ServiceSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			ServiceSearchTerms searchTerms = (ServiceSearchTerms)searchContainer.getSearchTerms();
		
		
			long serviceDomainId = ParamUtil.getLong(request, "serviceDomainId");
			
			long govAgencyId = ParamUtil.getLong(request, "govAgencyId");
	
			DictItem domainItem = null;
			DictItem govAgencygovItem = null;
		
			try{
				if(serviceDomainId > 0){
					domainItem = DictItemLocalServiceUtil.getDictItem(serviceDomainId);
				}
				
				if(govAgencyId > 0){
					govAgencygovItem = DictItemLocalServiceUtil.getDictItem(serviceDomainId);
				}

				if(domainItem != null){
					searchTerms.setServiceDomainIndex(domainItem.getTreeIndex());
				}
				
				if(govAgencygovItem != null){
					searchTerms.setGovAgencyIndex(govAgencygovItem.getTreeIndex());
				}
				
				%>
					<%@include file="/html/portlets/dossiermgt/frontoffice/service_search_results.jspf" %>
				<%
			}catch(Exception e){
				_log.error(e);
			}
		
			total = totalCount;
			results = serviceBeans;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
			
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.bean.ServiceBean" 
			modelVar="serviceBean" 
			keyProperty="serviceConfigId"
		>
			<%
				
				//id column
				row.addText(String.valueOf(row.getPos() + 1 + searchContainer.getStart()));

				row.addText(Validator.isNotNull(serviceBean.getServiceName()) ? serviceBean.getServiceName() : StringPool.BLANK);
				
				DictItem dictItem = null;
				String domainName = StringPool.DASH;
				
				try{
					dictItem = DictItemLocalServiceUtil.getDictItem(GetterUtil.getLong(serviceBean.getDomainCode()));
					domainName = dictItem.getItemName(locale);
				}catch(Exception e){
					
				}
				
				row.addText(domainName);
				
				row.addText(serviceBean.getGovAgencyName());
				
				row.addText(String.valueOf(serviceBean.getLevel()));
				
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN,"/html/portlets/dossiermgt/frontoffice/service_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.frontofficeservicelist.jsp");
%>
