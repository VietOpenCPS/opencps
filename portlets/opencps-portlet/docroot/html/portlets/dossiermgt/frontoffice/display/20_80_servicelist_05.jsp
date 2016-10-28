<%@page import="javax.portlet.PortletMode"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.processmgt.util.ProcessOrderUtils"%>
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
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.bean.ServiceBean"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearch"%>
<%@page import="org.opencps.dossiermgt.search.ServiceSearchTerms"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>

<%@ include file="../../init.jsp"%>

<%
	
	String backURL = ParamUtil.getString(request, "backURL");

	long serviceDomainId = ParamUtil.getLong(request, "serviceDomainId");
	
	long administrationId = ParamUtil.getLong(request, "administrationId");
	
	String tabs1 = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "display/20_80_servicelist_05.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	iteratorURL.setParameter("isListServiceConfig", String.valueOf(true));
	iteratorURL.setParameter("backURL", currentURL);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-index");
	headerNames.add("service-name");
	headerNames.add("action");
	headerNames.add("hidden");
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="service-list"
/>

<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<div class="payment-ld">
<div class="content">
<div class="opcs-serviceinfo-list-label">
	<div class="title_box">
          <p class="file_manage_title"><liferay-ui:message key="list-service-config-dvc"/></p>
		  <p class="count"></p>
    </div>
</div>

<liferay-ui:search-container searchContainer="<%= new ServiceSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
				headerNames="<%=headers %>"
			>
			
				<liferay-ui:search-container-results>
					<%
					
						ServiceSearchTerms searchTerms = (ServiceSearchTerms)searchContainer.getSearchTerms();
					
						DictItem domainItem = null;
						
						String treeIndex = StringPool.BLANK;
						
						String administrationIndex = StringPool.BLANK;
						
						int totalCount = 0;
						
						List<ServiceInfo> result = new ArrayList<ServiceInfo>();
						
						try{
							if(serviceDomainId > 0){
								
								domainItem = DictItemLocalServiceUtil.getDictItem(serviceDomainId);
								
								treeIndex = domainItem.getTreeIndex();
								
							}
							if(administrationId > 0){
								
								domainItem = DictItemLocalServiceUtil.getDictItem(administrationId);
								
								administrationIndex = domainItem.getTreeIndex();
								
							}
							
							result = ServiceInfoLocalServiceUtil.getServiceInFosByG_DI_Status(themeDisplay.getScopeGroupId(), 
									treeIndex, 
									administrationIndex, 
									1, 
									searchTerms.getKeywords(),
									QueryUtil.ALL_POS, QueryUtil.ALL_POS, searchContainer.getOrderByComparator());
							
							totalCount = ServiceInfoLocalServiceUtil.countServiceInFosByG_DI_Status(themeDisplay.getScopeGroupId(), 
									treeIndex, 
									administrationIndex, 
									1, 
									searchTerms.getKeywords());
									
						}catch(Exception e){
							_log.error(e);
						}
					
						total = totalCount;
						results = result;
						
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
							
							
							PortletURL linkURL = PortletURLFactoryUtil.create(request, WebKeys.P26_SUBMIT_ONLINE, 
									PortalUtil.getPlidFromPortletId(themeDisplay.getScopeGroupId(),  WebKeys.P26_SUBMIT_ONLINE), 
									PortletRequest.RENDER_PHASE);
							linkURL.setWindowState(WindowState.MAXIMIZED);
							linkURL.setPortletMode(PortletMode.VIEW);
							linkURL.setParameter("mvcPath", "/html/portlets/dossiermgt/submit/dossier_submit_online.jsp");
							linkURL.setParameter("backURL", currentURL);
							linkURL.setParameter("serviceinfoId", String.valueOf(serviceInfo.getServiceinfoId()));
							
							row.setClassName("opencps-searchcontainer-row");
							row.addText(String.valueOf(row.getPos() + 1 + searchContainer.getStart()));
							row.addText(serviceInfo.getServiceName());
							row.addText("<a class=\"button\" href=\""+linkURL.toString()+"\" >" + LanguageUtil.get(pageContext, "service-description") +"</a>");
							row.addText("");
						%>
					</liferay-ui:search-container-row> 
				
				<liferay-ui:search-iterator paginate="false"/>
			</liferay-ui:search-container>

</div>
</div>
<aui:script>
    AUI().ready(function(A) {
    	var allTable = A.all(".table-striped");
    	allTable.each(function (taskNode) {
			taskNode.addClass('payment');
	    });
    });
</aui:script>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.frontofficeservicelist.jsp");
%>
