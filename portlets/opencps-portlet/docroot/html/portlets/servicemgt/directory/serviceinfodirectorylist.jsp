
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayPortletMode"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
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
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearch"%>
<%@page import="org.opencps.servicemgt.search.ServiceSearchTerms"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="../init.jsp" %>


<%
	
	String plidServiceDetail = preferences.getValue("plidServiceDetail","0");

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "serviceinfodirectorylist.jsp");
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-index");
	/* headerNames.add("service-no"); */
	headerNames.add("service-name");
	/* headerNames.add("service-domain");
	headerNames.add("service-administrator");
	 */
	headerNames.add("service-bound-data");
	
	headerNames.add("action");
	 
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
	
	PortletURL searchURL = renderResponse.createRenderURL();
	
	String administrationCode = ParamUtil.getString(request, ServiceDisplayTerms.SERVICE_ADMINISTRATION);
	
	String domainCode = ParamUtil.getString(request, ServiceDisplayTerms.SERVICE_DOMAINCODE);
	
	request.setAttribute(ServiceDisplayTerms.SERVICE_ADMINISTRATION, administrationCode);
	
	request.setAttribute(ServiceDisplayTerms.SERVICE_DOMAINCODE, domainCode);
	
	DictCollection collectionDomain = null;
	DictItem curDictItem = null;
	try {
		collectionDomain = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, WebKeys.SERVICE_DOMAIN);
	} catch (Exception e) {
		
	}
	
	List<DictItem> dictItems = new ArrayList<DictItem>();
	
	if(Validator.isNotNull(collectionDomain)) {
		dictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(collectionDomain.getDictCollectionId());
	}
	
	String myComboTree = ProcessOrderUtils.generateComboboxTree(PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_DOMAIN, PortletConstants.TREE_VIEW_ALL_ITEM, 
			PortletConstants.TREE_VIEW_LEVER_2, false, renderRequest);
	
	iteratorURL.setParameter(ServiceDisplayTerms.SERVICE_ADMINISTRATION, administrationCode);
	iteratorURL.setParameter(ServiceDisplayTerms.SERVICE_DOMAINCODE, domainCode);
	iteratorURL.setParameter("keywords", ParamUtil.getString(request, "keywords"));
%>

<aui:script use="aui-base,aui-io">
$(document).ready(function(){
	var myComboTree = '<%=myComboTree %>';
	var domainCode = '<%=domainCode%>';
	var comboboxTree = $('#comboboxTree').comboTree({  
		boundingBox: 'comboboxTree',
		name: '#<portlet:namespace /><%=ServiceDisplayTerms.SERVICE_DOMAINCODE %>',
		form: document.<portlet:namespace />fm,
		formSubmit: true,
		isMultiple: false,
	    source: JSON.parse(myComboTree)
	});

	comboboxTree.setValue(domainCode);
	
	$("#<portlet:namespace />administrationCode").change(function() {
		<portlet:namespace />onSelectSubmit();
	});
	Liferay.provide(window, '<portlet:namespace/>onSelectSubmit', function() {
		var A = AUI();
		
		submitForm(document.<portlet:namespace />fm);
	});
});

</aui:script>

<aui:nav-bar cssClass="opencps-toolbar custom-toolbar">
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<aui:row>
						<aui:col width="30" cssClass="search-col">
							<datamgt:ddr
								cssClass="search-input select-box"
								depthLevel="1" 
								dictCollectionCode="SERVICE_ADMINISTRATION"
								itemNames="<%= ServiceDisplayTerms.SERVICE_ADMINISTRATION %>"
								itemsEmptyOption="true"
								selectedItems="<%= administrationCode %>"
								emptyOptionLabels="<%=ServiceDisplayTerms.SERVICE_ADMINISTRATION %>"
								showLabel="false"
							>
							</datamgt:ddr>

						</aui:col>
						<aui:col width="30" cssClass="search-col">
							<%-- <datamgt:ddr 
								depthLevel="1" 
								dictCollectionCode="SERVICE_DOMAIN"
								itemNames="<%= ServiceDisplayTerms.SERVICE_DOMAINCODE %>"
								itemsEmptyOption="true"	
								selectedItems="<%= domainCode %>"
								emptyOptionLabels="<%=ServiceDisplayTerms.SERVICE_DOMAINCODE %>"
								cssClass="search-input select-box"
								showLabel="false"
							>
							</datamgt:ddr> --%>
							
							<aui:input name="<%=ServiceDisplayTerms.SERVICE_DOMAINCODE %>" type="hidden" value="<%=domainCode %>"></aui:input>
							<input type="text" id="comboboxTree" class="opencps-combotree" readonly="readonly" />
						</aui:col>
						<aui:col width="30" cssClass="search-col">
							<%-- <label>
								<liferay-ui:message key="keywords"/>
							</label> --%>
							<liferay-ui:input-search 
								cssClass="search-input input-keyword"
								id="keywords1"
								name="keywords"
								title='<%= LanguageUtil.get(locale, "keywords") %>'
								placeholder='<%= LanguageUtil.get(portletConfig, locale, "put-keyword") %>' 
							/>
						</aui:col>
					</aui:row>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>


<div class="opencps-searchcontainer-wrapper">
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
			indexVar="index"
		>
			<%
				PortletURL viewURL = renderResponse.createRenderURL();
				viewURL.setParameter("mvcPath", templatePath + "service_detail.jsp");
				viewURL.setParameter("serviceinfoId", String.valueOf(service.getServiceinfoId()));
				viewURL.setParameter("backURL", currentURL);
				
				PortletURL renderToSubmitOnline = PortletURLFactoryUtil.create(request, WebKeys.P26_SUBMIT_ONLINE, Long.valueOf(plidServiceDetail), PortletRequest.RENDER_PHASE);
				renderToSubmitOnline.setWindowState(LiferayWindowState.NORMAL);
				renderToSubmitOnline.setPortletMode(LiferayPortletMode.VIEW);
				renderToSubmitOnline.setParameter("mvcPath", "/html/portlets/dossiermgt/submit/dossier_submit_online.jsp");
				renderToSubmitOnline.setParameter("serviceinfoId", String.valueOf(service.getServiceinfoId()));
				renderToSubmitOnline.setParameter("backURL", currentURL);
			%>
				<liferay-util:buffer var="boundcol1">
					
					<div class="row-fluid">
						<div class="span12">
							<a href="<%=viewURL.toString() %>"><%=service.getServiceName() %></a>
						</div>
					</div>
				</liferay-util:buffer>
				
				<liferay-util:buffer var="boundcol2">
					<div class="row-fluid">
						<div class="span5 bold-label">
							<liferay-ui:message key="service-no"/>
						</div>

						<div class="span7">
							<a href="<%=viewURL.toString() %>"><%=service.getServiceNo() %></a>
						</div>
					</div>

					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="service-domain"/>
						</div>
						<div class="span7">
							<a href="<%=viewURL.toString() %>"><%=DictItemUtil.getNameDictItem(service.getDomainCode())%></a>
						</div>
					</div>
					
					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="service-administrator"/>
						</div>
						<div class="span7">
							<a href="<%=viewURL.toString() %>"><%=DictItemUtil.getNameDictItem(service.getAdministrationCode())%></a>
						</div>
					</div>
				</liferay-util:buffer>
				
				<liferay-util:buffer var="boundcol3">
					
					<aui:button href="<%= renderToSubmitOnline.toString() %>" cssClass="des-sub-button radius20" value="service-description"></aui:button>
					
				</liferay-util:buffer>
			<%
				if(service.getActiveStatus() !=0) {
					row.setClassName("opencps-searchcontainer-row");
					
					// no column
					row.addText(String.valueOf((searchContainer.getCur() - 1) * searchContainer.getDelta() + index + 1), viewURL);
				
					
					row.addText(boundcol1);
					
					
					row.addText(boundcol2); 
					
					row.addText(boundcol3); 
				}
			%>	
		
		</liferay-ui:search-container-row>	
	
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	
	</liferay-ui:search-container>
</div>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.servicemgt.directory.serviceinfo.jsp");
%>

