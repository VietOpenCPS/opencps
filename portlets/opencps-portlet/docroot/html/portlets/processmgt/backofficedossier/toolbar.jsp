
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessDisplayTerms"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
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

<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="../init.jsp"%>

<%
	PortletURL searchURL = renderResponse.createRenderURL();	

	String tabs1 = ParamUtil.getString(request, "tabs1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);	
%>

<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		
	</aui:nav>
	<c:choose>
		<c:when test="<%= tabs1.equals(ProcessMgtUtil.TOP_TABS_DOSSIERLIST)%>">
			<%
				String administrationCode = ParamUtil.getString(request, ProcessDisplayTerms.PROCESS_ADMINISTRATIONCODE);
				
				request.setAttribute(ProcessDisplayTerms.PROCESS_ADMINISTRATIONCODE, administrationCode);
						
				int domainCode = ParamUtil.getInteger(request, DossierDisplayTerms.SERVICE_DOMAIN_CODE, 0);
				int dossierStatus = ParamUtil.getInteger(request, DossierDisplayTerms.DOSSIER_STATUS, -1);	
				
				request.setAttribute(DossierDisplayTerms.SERVICE_DOMAIN_CODE, domainCode);
				request.setAttribute(DossierDisplayTerms.DOSSIER_STATUS, dossierStatus);
				
				DictCollection dc = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, ServiceUtil.SERVICE_DOMAIN);
				
				List<DictItem> ls = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(dc.getDictCollectionId());
		
				searchURL.setParameter("tabs1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);
				searchURL.setParameter("mvcPath", templatePath + "backofficedossierlist.jsp");
			%>
				<aui:nav-bar-search cssClass="pull-left" style="width: 98%;">
					<div class="form-search">
						<aui:form action="<%= searchURL %>" method="post" name="fm">
							<div class="toolbar_search_input">
								<aui:row>
									<aui:col width="30">
										<liferay-ui:input-search 
											id="keywords"
											name="keywords"
											title='<%= LanguageUtil.get(portletConfig, locale, "keywords") %>'
											placeholder="<%= LanguageUtil.get(portletConfig, locale, \"keywords\") %>" 
										/>
									</aui:col>
									<aui:col width="30">
										<aui:select label="" name="<%= DossierDisplayTerms.SERVICE_DOMAIN_CODE %>" style="width: 100%;">
											<aui:option value="">
												<liferay-ui:message key="filter-by-service-domain"></liferay-ui:message>
											</aui:option>
											<%
												for (DictItem di : ls ) {	
													if (di.getDictItemId() == domainCode) {
											%>
											<aui:option selected="<%= true %>" value="<%= di.getDictItemId() %>"><%= di.getItemName(locale) %></aui:option>							
											<%
													}
													else {
											%>
											<aui:option selected="<%= false %>" value="<%= di.getDictItemId() %>"><%= di.getItemName(locale) %></aui:option>							
											<%
													}
												}
											%>	
										</aui:select>						
									</aui:col>
									<aui:col width="30">
										<aui:select label="" name="<%= DossierDisplayTerms.DOSSIER_STATUS %>" style="width: 100%;">
											<aui:option value="-1">
												<liferay-ui:message key="filter-by-dossier-status"></liferay-ui:message>
											</aui:option>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_NEW %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_NEW %>"><liferay-ui:message key="dossier-status-new"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_NEW %>"><liferay-ui:message key="dossier-status-new"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_RECEIVING %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_RECEIVING %>"><liferay-ui:message key="dossier-status-receiving"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_RECEIVING %>"><liferay-ui:message key="dossier-status-receiving"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_WAITING %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_WAITING %>"><liferay-ui:message key="dossier-status-waiting"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_WAITING %>"><liferay-ui:message key="dossier-status-waiting"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_PAYING %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_PAYING %>"><liferay-ui:message key="dossier-status-paying"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_PAYING %>"><liferay-ui:message key="dossier-status-paying"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_PROCESSING %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_PROCESSING %>"><liferay-ui:message key="dossier-status-processing"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_PROCESSING %>"><liferay-ui:message key="dossier-status-processing"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_DONE %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_DONE %>"><liferay-ui:message key="dossier-status-done"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_DONE %>"><liferay-ui:message key="dossier-status-done"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_SYSTEM %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_SYSTEM %>"><liferay-ui:message key="dossier-status-system"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_SYSTEM %>"><liferay-ui:message key="dossier-status-system"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="<%= dossierStatus == PortletConstants.DOSSIER_STATUS_ERROR %>">
													<aui:option selected="<%= true %>" value="<%= PortletConstants.DOSSIER_STATUS_ERROR %>"><liferay-ui:message key="dossier-status-error"></liferay-ui:message></aui:option>
												</c:when>
												<c:otherwise>
													<aui:option selected="<%= false %>" value="<%= PortletConstants.DOSSIER_STATUS_ERROR %>"><liferay-ui:message key="dossier-status-error"></liferay-ui:message></aui:option>
												</c:otherwise>
											</c:choose>
										</aui:select>						
									</aui:col>
								</aui:row>
							</div>
						</aui:form>
					</div>			
				</aui:nav-bar-search>
	
		</c:when>
		<c:when test="<%= tabs1.equals(ProcessMgtUtil.TOP_TABS_DOSSIERFILELIST)%>">
			<%
				List<DossierTemplate> lsTemplates = DossierTemplateLocalServiceUtil.getAll();
				searchURL.setParameter("tabs1", ProcessMgtUtil.TOP_TABS_DOSSIERFILELIST);
				searchURL.setParameter("mvcPath", templatePath + "backofficedossierfilelist.jsp");
				long dossierTemplateId = ParamUtil.getLong(request, ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID);
				boolean onlyViewFileResult = ParamUtil.getBoolean(request, "onlyViewFileResult");
			%>
				<aui:nav-bar-search cssClass="pull-left" style="width: 98%;">
					<div class="form-search">
						<aui:form action="<%= searchURL %>" method="post" name="fm">
							<div class="toolbar_search_input">
								<aui:row>
									<aui:col width="50">
										<liferay-ui:input-search 
											id="keywords"
											name="keywords"
											title='<%= LanguageUtil.get(portletConfig, locale, "keywords") %>'
											placeholder="<%= LanguageUtil.get(portletConfig, locale, \"keywords\") %>" 
										/>
									</aui:col>
									<aui:col width="50">
										<aui:select label="" name="<%= ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID %>" style="width: 100%;">
											<aui:option value="<%= DossierMgtUtil.DOSSIERFILETYPE_ALL %>">
												<liferay-ui:message key="filter-by-dossier-template"></liferay-ui:message>
											</aui:option>
											<%
												for (DossierTemplate template : lsTemplates) {
													if (dossierTemplateId == template.getDossierTemplateId()) {
											%>
											<aui:option selected="true" value="<%= template.getDossierTemplateId() %>"><%= template.getTemplateName() %></aui:option>
											<%
													}
													else {
											%>
											<aui:option selected="false" value="<%= template.getDossierTemplateId() %>"><%= template.getTemplateName() %></aui:option>
											<% 
													}
												}
											%>
										</aui:select>						
									</aui:col>
								</aui:row>
								<aui:row>
									<aui:col>
										<aui:input checked="<%= onlyViewFileResult %>" name="onlyViewFileResult" value="false" type="checkbox" label=""><liferay-ui:message key="only-view-file-result"/></aui:input>
									</aui:col>
								</aui:row>
							</div>
						</aui:form>
					</div>			
				</aui:nav-bar-search>
		</c:when>
	</c:choose>
</aui:nav-bar>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backofficedossier.toolbar.jsp");
%>