<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
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

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.PortletUtil"%>

<%@ include file="../init.jsp"%>

<%
	long serviceDomainId = ParamUtil.getLong(request, "serviceDomainId");
	
	long govAgencyId = ParamUtil.getLong(request, "govAgencyId");
	
	String dossierStatus = ParamUtil.getString(request, "dossierStatus", StringPool.BLANK);

	String tabs1 = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	
	String templatesToDisplay_cfg = GetterUtil.getString(portletPreferences.getValue("templatesToDisplay", "default"));
	
	PortletURL searchURL = renderResponse.createRenderURL();
	
	boolean isListServiceConfig = ParamUtil.getBoolean(request, "isListServiceConfig", false);
	
	if(isListServiceConfig && tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER)){
		searchURL.setParameter("mvcPath", templatePath + "frontofficeservicelist.jsp");
		searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
		searchURL.setParameter("isListServiceConfig", String.valueOf(true));
	}else if(!isListServiceConfig && tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER)){
		searchURL.setParameter("mvcPath", templatePath + "frontofficedossierlist.jsp");
		searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	} else {
		searchURL.setParameter("mvcPath", templatePath + "frontofficedossierfilelist.jsp");
		searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_FILE);
	}
	
	if(isListServiceConfig && templatesToDisplay_cfg.equals("20_80")){
		searchURL.setParameter("mvcPath", templatePath + "display/20_80_servicelist_05.jsp");
		searchURL.setParameter("backURL", currentURL);
	}
	
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
%>

<aui:nav-bar cssClass="opencps-toolbar custom-toolbar">
	<c:choose>
		<c:when test="<%=isListServiceConfig%>">

		</c:when>
		<c:otherwise>
			<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left font-pull" >
				<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER) 
					&& tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER)%>">
					
					<portlet:renderURL var="addDossierURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
						<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/frontoffice/frontofficeservicelist.jsp"/>
						<portlet:param name="isListServiceConfig" value="<%=String.valueOf(true) %>"/>
						<portlet:param name="backURL" value="<%=currentURL %>"/>
						<portlet:param name="backURLFromList" value="<%=currentURL %>"/>
					</portlet:renderURL>
					<%-- <aui:nav-item 
						id="addDictItem" 
						label="add-dossier" 
						iconCssClass="icon-plus"  
						href="<%=addDossierURL %>"
						cssClass="action-button"
					/> --%>
					<aui:button icon="icon-plus" href="<%=addDossierURL %>" cssClass="action-button" value="select-service-info"/>
				</c:if>
			</aui:nav>
		</c:otherwise>
	</c:choose>

	<aui:nav-bar-search cssClass="pull-right front-custom-select-search" style="width: 70%;">
		<div class="form-search">
			<aui:form 
				action="<%= searchURL %>" method="post"
				name="fmSearch" 
				onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "doSearch();" %>'
			>
				<c:choose>
					<c:when test="<%=isListServiceConfig %>">
						
						<c:choose>
								<c:when test="<%=templatesToDisplay_cfg.equals(\"20_80\") %>">
									<aui:row>
										<aui:col cssClass="search-col">
											<liferay-ui:input-search 
												id="keywords1"
												name="keywords"
												title='<%= LanguageUtil.get(locale, "keywords") %>'
												placeholder='<%=LanguageUtil.get(locale, "keywords") %>'
												cssClass="search-input input-keyword"
											/>
										</aui:col>
										<style>
											.navbar-search.pull-right.front-custom-select-search{
												width: 100% !important;
											}
										</style>
									</aui:row>
								</c:when>
								<c:otherwise>
									<aui:row>
										<aui:col width="30" cssClass="search-col">
											<%-- <datamgt:ddr 
												depthLevel="1" 
												dictCollectionCode="<%=ServiceUtil.SERVICE_DOMAIN %>" 
												name="serviceDomain"
												inlineField="<%=true%>"
												inlineLabel="left"
												showLabel="<%=false%>"
												emptyOptionLabels="filter-by-service-domain"
												itemsEmptyOption="true"
												itemNames="serviceDomainId"
												selectedItems="<%=String.valueOf(serviceDomainId)%>"
												cssClass="search-input select-box input100"
											/> --%>
											
											<aui:select name="serviceDomainId" label="">
												<aui:option value="">
													<liferay-ui:message key="filter-by-service-domain"/>
												</aui:option>
												<%
													if(dictItems != null){
														for(DictItem dictItem : dictItems){
															if((curDictItem != null && dictItem.getDictItemId() == curDictItem.getDictItemId())||
																	(curDictItem != null && dictItem.getTreeIndex().contains(curDictItem.getDictItemId() + StringPool.PERIOD))){
																continue;
															}
															
															int level = StringUtil.count(dictItem.getTreeIndex(), StringPool.PERIOD);
															String index = "|";
															for(int i = 0; i < level; i++){
																index += "_";
															}
															%>
																<aui:option value="<%=dictItem.getDictItemId() %>"><%=index + dictItem.getItemName(locale) %></aui:option>
															<%
														}
													}
												%>
											</aui:select>
										</aui:col>
										<aui:col width="30" cssClass="search-col">
											<datamgt:ddr 
												depthLevel="1" 
												dictCollectionCode="<%= PortletPropsValues.DATAMGT_MASTERDATA_GOVERNMENT_AGENCY %>" 
												name="govAgency"
												inlineField="<%=true%>"
												inlineLabel="left"
												showLabel="<%=false%>"
												emptyOptionLabels="filter-by-gov-agency"
												itemsEmptyOption="true"
												itemNames="govAgencyId"
												selectedItems="<%=String.valueOf(govAgencyId)%>"
												cssClass="search-input select-box input100"
											/>
										</aui:col>
										<aui:col width="30" cssClass="search-col">
											<liferay-ui:input-search 
												id="keywords1"
												name="keywords"
												title='<%= LanguageUtil.get(locale, "keywords") %>'
												placeholder='<%= LanguageUtil.get(locale, "keywords") %>'
												cssClass="search-input input-keyword"
											/>
										</aui:col>
									</aui:row>
								</c:otherwise>
							</c:choose>
					</c:when>
					<c:otherwise>
						<c:if test="<%=tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER) %>">
							<c:choose>
								<c:when test="<%=templatesToDisplay_cfg.equals(\"20_80\") %>">
									<aui:row>
										<aui:col cssClass="search-col">
											<liferay-ui:input-search 
												id="keywords1"
												name="keywords"
												title='<%= LanguageUtil.get(locale, "keywords") %>'
												placeholder='<%=LanguageUtil.get(locale, "keywords") %>'
												cssClass="search-input input-keyword"
											/>
										</aui:col>
									</aui:row>
								</c:when>
								<c:otherwise>
									<aui:row>
										<aui:col width="30" cssClass="search-col">
											<%-- <datamgt:ddr 
												depthLevel="1" 
												dictCollectionCode="<%=ServiceUtil.SERVICE_DOMAIN %>" 
												name="serviceDomain"
												inlineField="<%=true%>"
												inlineLabel="left"
												showLabel="<%=false%>"
												emptyOptionLabels="filter-by-service-domain"
												itemsEmptyOption="true"
												itemNames="serviceDomainId"
												selectedItems="<%=String.valueOf(serviceDomainId)%>"
												cssClass="search-input select-box input100"
											/> --%>
											
											<aui:select name="serviceDomainId" label="">
												<aui:option value="">
													<liferay-ui:message key="filter-by-service-domain"/>
												</aui:option>
												<%
													if(dictItems != null){
														for(DictItem dictItem : dictItems){
															if((curDictItem != null && dictItem.getDictItemId() == curDictItem.getDictItemId())||
																	(curDictItem != null && dictItem.getTreeIndex().contains(curDictItem.getDictItemId() + StringPool.PERIOD))){
																continue;
															}
															
															int level = StringUtil.count(dictItem.getTreeIndex(), StringPool.PERIOD);
															String index = "|";
															for(int i = 0; i < level; i++){
																index += "_";
															}
															%>
																<aui:option value="<%=dictItem.getDictItemId() %>"><%=index + dictItem.getItemName(locale) %></aui:option>
															<%
														}
													}
												%>
											</aui:select>
										</aui:col>
										<aui:col width="30" cssClass="search-col">
											
											<datamgt:ddr 
												depthLevel="1" 
												dictCollectionCode="DOSSIER_STATUS" 
												showLabel="<%=false%>"
												emptyOptionLabels="dossier-status"
												itemsEmptyOption="true"
												itemNames="<%=DossierDisplayTerms.DOSSIER_STATUS %>"
												optionValueType="code"
												cssClass="search-input select-box input100"
											/>
											
										</aui:col>
										<aui:col width="30" cssClass="search-col">
											<liferay-ui:input-search 
												id="keywords1"
												name="keywords"
												title='<%= LanguageUtil.get(locale, "keywords") %>'
												placeholder='<%=LanguageUtil.get(locale, "keywords") %>'
												cssClass="search-input input-keyword"
											/>
										</aui:col>
									</aui:row>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:otherwise>
				</c:choose>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<aui:script use="liferay-util-list-fields,liferay-portlet-url">

	Liferay.provide(window, '<portlet:namespace/>doSearch', function() {
		
		var A = AUI();
		
		var isListServiceConfig = '<%=isListServiceConfig%>';
		
		var serviceDomainId = '<%=serviceDomainId%>';
		
		if( A.one('#<portlet:namespace/>serviceDomainId') != "undefined" &&
				A.one('#<portlet:namespace/>serviceDomainId') != null){
		
			serviceDomainId = A.one('#<portlet:namespace/>serviceDomainId').val();
		
		}
		
		var govAgencyId;
		
		var dossierStatus = '<%=dossierStatus%>';
		
		if(isListServiceConfig == 'true'){
			
			if( A.one('#<portlet:namespace/>govAgencyId') != "undefined" &&
					A.one('#<portlet:namespace/>govAgencyId') != null){
			
				govAgencyId = A.one('#<portlet:namespace/>govAgencyId').val();
			
			}
			
		}else if( A.one('#<portlet:namespace/>dossierStatus') != "undefined" &&
				A.one('#<portlet:namespace/>dossierStatus') != null){
		
			dossierStatus = A.one('#<portlet:namespace/>dossierStatus').val();

		} 

		var fmSearch = A.one('#<portlet:namespace/>fmSearch');
		
		var action = fmSearch.attr('action');
		
		var portletURL = Liferay.PortletURL.createURL(action);
		portletURL.setParameter("serviceDomainId", serviceDomainId);
		
		if(isListServiceConfig == 'true'){
			portletURL.setParameter("govAgencyId", govAgencyId);
		}else{
			portletURL.setParameter("dossierStatus", dossierStatus);
		} 
		
		fmSearch.setAttribute('action', portletURL.toString());
		
		submitForm(document.<portlet:namespace />fmSearch);
	},['liferay-portlet-url']);
</aui:script>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.toolbar.jsp");
%>
