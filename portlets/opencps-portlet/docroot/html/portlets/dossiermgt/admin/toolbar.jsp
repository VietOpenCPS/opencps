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
<%@page import="org.opencps.dossiermgt.search.DossierTemplateDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.dossiermgt.permissions.ServiceConfigPermission"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPartPermission"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierTemplatePermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.servicemgt.search.ServiceDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigDisplayTerms"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@ include file="../init.jsp"%>

<%
	String tabs1 = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE);
	PortletURL searchURL = renderResponse.createRenderURL();
	
	Long dossierTemplateId = (Long) session.getAttribute(DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID);
	DictCollection dictCollectionServiceAdmin = null;
	List<DictItem> dictItemsServiceAdmin = new ArrayList<DictItem>();
	String currURL = ParamUtil.getString(request, "currURL");
	try {
		dictCollectionServiceAdmin = DictCollectionLocalServiceUtil
	                    .getDictCollection(scopeGroupId, PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_ADMINISTRATION);
		if(Validator.isNotNull(dictCollectionServiceAdmin)) {
			dictItemsServiceAdmin = DictItemLocalServiceUtil
							.getDictItemsByDictCollectionId(dictCollectionServiceAdmin.getDictCollectionId());
		}
		
	}catch (Exception e) {
		//no thing to do
	}
	
	DictCollectionLocalServiceUtil
					.getDictCollection(scopeGroupId, PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_ADMINISTRATION);
%>
<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:choose>
			
			<c:when test="<%=tabs1.contentEquals(DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE) %>">
				<%
					searchURL.setParameter("mvcPath", templatePath + "dossiertemplatelist.jsp");
					searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE);
				%>
				
				<portlet:renderURL var="editDossierTemplateURL">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_dossier.jsp" %>'/>
					<portlet:param name="backURL" value="<%=currentURL %>"/> 
				</portlet:renderURL>
				
				<c:if 
					test="<%=DossierTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_TEMPLATE)%>"
				>
					<aui:nav-item 
						id="addDossierTemplate" 
						label="add-dossier-template" 
						iconCssClass="icon-plus"  
						href="<%=editDossierTemplateURL %>"
					/>
				</c:if>
				
			</c:when>
			<c:when test="<%=tabs1.contentEquals(DossierMgtUtil.DOSSIER_PART_TOOLBAR) %>">
				<%
					searchURL.setParameter("mvcPath", templatePath + "dossier_common/dossierpartlist.jsp");
					searchURL.setParameter("tabs1", DossierMgtUtil.DOSSIER_PART_TOOLBAR);
				%>
				<portlet:renderURL var="editDossierPartURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_dossier_part.jsp" %>'/>
					<portlet:param name="partListURL" value="<%=currURL %>"/>
					<portlet:param name="backURL" value="<%=currentURL %>"/> 
					<portlet:param name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID %>" value="<%=String.valueOf(dossierTemplateId) %>"/>
				</portlet:renderURL>
				
				<c:if 
						test="<%=DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_PART)%>"
				>
					<aui:nav-item 
						id="addDossierPart" 
						label="add-dossier-part" 
						iconCssClass="icon-plus"  
						href="<%=editDossierPartURL %>"
					/>
				</c:if>
			</c:when>
			
			 <c:when test="<%= tabs1.contentEquals(DossierMgtUtil.TOP_TABS_SERVICE_CONFIG) 
			 	|| tabs1.contentEquals(DossierMgtUtil.SERVICE_CONFIG_TOOLBAR)
			 %>">
				<%
					searchURL.setParameter("mvcPath", templatePath + "serviceconfiglist.jsp");
					searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_SERVICE_CONFIG);
				%>
				<portlet:renderURL var="editServiceConfigURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_service_config.jsp" %>'/>
					<portlet:param name="redirectURL" value="<%=currentURL %>"/>
					<portlet:param name="tabs1" value="<%= tabs1 %>"/>
					<portlet:param name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID %>" value="<%=String.valueOf(dossierTemplateId) %>"/>
				
				</portlet:renderURL>
				
					<c:if test="<%= ServiceConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE_CONFIG) %>">
						<aui:nav-item 
							id="addServiceConfig" 
							label="add-service-config" 
							iconCssClass="icon-plus"  
							href="<%=editServiceConfigURL %>"
						/>
					</c:if>
			</c:when>
		</c:choose>
	</aui:nav>
	
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<c:choose>
						
						<c:when test="<%= tabs1.contentEquals(DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE) %>">
								
								<aui:row>
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
								
						</c:when>
						<c:when test="<%= tabs1.contentEquals(DossierMgtUtil.TOP_TABS_SERVICE_CONFIG) %>">
							<aui:row>
									<aui:col width="30">

										<aui:select name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_GOVAGENCYCODE %>">
										  
										  <aui:option value="<%=StringPool.BLANK %>">
										      <liferay-ui:message key="fill-by-service-admin" />
										  </aui:option>
										  
										  <%
										    for(DictItem item : dictItemsServiceAdmin) {
										    	%>
										    	     <aui:option value="<%=item.getItemCode() %>">
										    	         <%=item.getItemName(locale, true) %>
										    	     </aui:option>
										    	<%
										    }
										  %>
										</aui:select>
									</aui:col>
									<aui:col width="30">

										<datamgt:ddr cssClass="input30"
											depthLevel="1" 
											dictCollectionCode="SERVICE_DOMAIN"
											itemNames="<%= ServiceDisplayTerms.SERVICE_DOMAINCODE %>"
											itemsEmptyOption="true"	
										>
										</datamgt:ddr>

									</aui:col>
									<aui:col width="30">
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
						</c:when>
					</c:choose>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.admin.toolbar.jsp");
%>