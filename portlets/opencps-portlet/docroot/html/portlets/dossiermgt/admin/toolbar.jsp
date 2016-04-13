<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.dossiermgt.permission.DossierPartPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.permission.DossierTemplatePermission"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
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


<%@ include file="../init.jsp"%>

<%
	String tabs1 = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE);
	PortletURL searchURL = renderResponse.createRenderURL();
%>
<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:choose>
			
			<c:when test="<%= tabs1.contentEquals(DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE) %>">
				<%
					searchURL.setParameter("mvcPath", templatePath + "dossiertemplatelist.jsp");
					searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_TEMPLATE);
				%>
				
				<portlet:renderURL var="editDossierTemplateURL">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_dossier.jsp" %>'/>
					<%-- <portlet:param name="redirectURL" value="<%=currentURL %>"/> --%>
					<portlet:param name="backURL" value="<%=currentURL %>"/> 
				</portlet:renderURL>
				
				<c:if 
						test="<%=DossierTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_TEMPLATE) 
						&& DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.VIEW)
					%>"
				>
					<aui:nav-item 
						id="addDossierTemplate" 
						label="add-dosier-template" 
						iconCssClass="icon-plus"  
						href="<%=editDossierTemplateURL %>"
					/>
				</c:if>
				
			</c:when>
			
			<%-- <c:when test="<%= tabs1.contentEquals(DossierMgtUtil.TOP_TABS_DOSSIER_PART) %>">
				<%
					searchURL.setParameter("mvcPath", templatePath + "DossierConfig.jsp");
					searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER_PART);
				%>
				<portlet:renderURL var="editDossierServiceURL">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_dossier_config.jsp" %>'/>
					<portlet:param name="redirectURL" value="<%=currentURL %>"/>
					<portlet:param name="backURL" value="<%=currentURL %>"/>
				</portlet:renderURL>
				
				<aui:row>
					<c:if test="<%= DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_TEMPLATE) %>">
						<aui:nav-item 
							id="addDossierConfig" 
							label="add-dossier-config" 
							iconCssClass="icon-plus"  
							href="<%=editTemplateURL %>"
						/>
					</c:if>
				</aui:row>
			</c:when> --%>
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
						
						<%-- <c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_TEMPLATE) %>">
							<liferay-ui:input-search 
								id="keywords1" 
								name="keywords" 
								placeholder='<%= LanguageUtil.get(locale, "name") %>' 
							/>
						</c:when>
						
						<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_DOMAIN) %>">
						
						</c:when>
						
						<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_SERVICE) %>">
							
						</c:when> --%>
						
					</c:choose>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.admin.toolbar.jsp");
%>