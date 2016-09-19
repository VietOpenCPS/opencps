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
<%@ include file="../../init.jsp" %>
<%@page import="org.opencps.servicemgt.search.ServiceSearchTerms"%>
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearch"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "choose_serviceinfos/check_serviceinfos.jsp");
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-index");
	headerNames.add("service-no");
	headerNames.add("service-name");
	headerNames.add("service-domain");
	headerNames.add("service-administrator");

	boolean isPermission =
				    ServicePermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_SERVICE);

	/* if (isPermission) {
		headerNames.add("action");
	} */
	
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
%>

<liferay-ui:search-container searchContainer="<%= new ServiceSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>" 
	rowChecker="<%=new RowChecker(renderResponse)%>"	
>
		
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
			editURL.setParameter("mvcPath", templatePath + "edit_service.jsp");
			editURL.setParameter("serviceinfoId", String.valueOf(service.getServiceinfoId()));
			editURL.setParameter("backURL", currentURL);
			
			// no column
			row.addText(String.valueOf(row.getPos() + 1), editURL);
		
			// service no
			row.addText(service.getServiceNo(), editURL);
			
			// service name
			row.addText(service.getServiceName(), editURL);
			
			// service admin
			row.addText(DictItemUtil.getNameDictItem(service.getAdministrationCode()), editURL);

			// service domain
			row.addText(DictItemUtil.getNameDictItem(service.getDomainCode()) , editURL);

			/* if(isPermission) {
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "service_actions.jsp", config.getServletContext(), request, response);
			} */
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator type="opencs_page_iterator"/>

</liferay-ui:search-container>