
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
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<%@ include file="../init.jsp"%>

<%
	String tabs1 = ParamUtil.getString(request, "tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	PortletURL searchURL = renderResponse.createRenderURL();
%>

<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER) && tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER)%>">
			
			<portlet:renderURL var="addDossierURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
				<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/frontoffice/frontofficeservicelist.jsp"/>
				<portlet:param name="backURL" value="<%=currentURL %>"/>
			</portlet:renderURL>
			<aui:nav-item 
				id="addDictItem" 
				label="add-dossier" 
				iconCssClass="icon-plus"  
				href="<%=addDossierURL %>"
			/>
		</c:if>
	</aui:nav>
	
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<c:if test="<%=tabs1.equals(DossierMgtUtil.TOP_TABS_DOSSIER) %>">
					<%
						searchURL.setParameter("mvcPath", templatePath + "frontofficedossierlist.jsp");
						searchURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
					%>
					<aui:row>
						<aui:col width="50">
							<aui:select name="dossierStatus" label="" inlineField="<%=true %>" inlineLabel="left">
								<aui:option value="-1"><liferay-ui:message key="all"/></aui:option>
								<%
									for(Integer status : PortletUtil.getDossierStatus()){
										%>
											<aui:option value="<%= status%>"><%=PortletUtil.getDossierStatusLabel(status, locale) %></aui:option>
										<%
									}
								%>
							</aui:select>
						</aui:col>
						<aui:col width="50">
							<liferay-ui:input-search 
								id="keywords1"
								name="keywords"
								title="keywords"
								placeholder='<%= LanguageUtil.get(locale, "keywords") %>' 
							/>
						</aui:col>
					</aui:row>
				</c:if>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.toolbar.jsp");
%>