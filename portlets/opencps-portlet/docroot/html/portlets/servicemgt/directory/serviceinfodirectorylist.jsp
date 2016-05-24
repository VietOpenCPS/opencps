
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
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearch"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearchTerms"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="../init.jsp" %>


<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "serviceinfodirectorylist.jsp");
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("no");
	headerNames.add("service-no");
	headerNames.add("service-name");
	headerNames.add("service-domain");
	headerNames.add("service-administrator");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
	
	PortletURL searchURL = renderResponse.createRenderURL();
	
	String administrationCode = ParamUtil.getString(request, ServiceDisplayTerms.SERVICE_ADMINISTRATION);
	
	String domainCode = ParamUtil.getString(request, ServiceDisplayTerms.SERVICE_DOMAINCODE);
	
	request.setAttribute(ServiceDisplayTerms.SERVICE_ADMINISTRATION, administrationCode);
	
	request.setAttribute(ServiceDisplayTerms.SERVICE_DOMAINCODE, domainCode);
%>

<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<aui:row>
						<aui:col width="25">
							<datamgt:ddr cssClass="input100"
								depthLevel="1" 
								dictCollectionCode="SERVICE_ADMINISTRATION"
								itemNames="<%= ServiceDisplayTerms.SERVICE_ADMINISTRATION %>"
								itemsEmptyOption="true"
								selectedItems="<%= administrationCode %>"	
							>
							</datamgt:ddr>

						</aui:col>
						<aui:col width="25">
							<datamgt:ddr cssClass="input100"
								depthLevel="1" 
								dictCollectionCode="SERVICE_DOMAIN"
								itemNames="<%= ServiceDisplayTerms.SERVICE_DOMAINCODE %>"
								itemsEmptyOption="true"	
								selectedItems="<%= domainCode %>"
							>
							</datamgt:ddr>

						</aui:col>
						<aui:col width="45">
							<label>
								<liferay-ui:message key="keywords"/>
							</label>
							<liferay-ui:input-search 
								id="keywords1"
								name="keywords"
								title="keywords"
								placeholder='<%= LanguageUtil.get(locale, "name") %>' 
							/>
						</aui:col>
					</aui:row>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

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
			PortletURL viewURL = renderResponse.createRenderURL();
			viewURL.setParameter("mvcPath", templatePath + "service_detail.jsp");
			viewURL.setParameter("serviceinfoId", String.valueOf(service.getServiceinfoId()));
			viewURL.setParameter("backURL", currentURL);
			
			if(service.getActiveStatus() !=0) {
				// no column
				row.addText(String.valueOf(row.getPos() + 1), viewURL);
			
				// service no
				row.addText(service.getServiceNo(), viewURL);
				
				// service name
				row.addText(service.getServiceName(), viewURL);
				
				// service domain
				row.addText(DictItemUtil.getNameDictItem(service.getDomainCode()) , viewURL);
				
				// service admin
				row.addText(DictItemUtil.getNameDictItem(service.getAdministrationCode()), viewURL);
			}
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator/>

</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.servicemgt.directory.serviceinfo.jsp");
%>

