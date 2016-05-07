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
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.accountmgt.permissions.BusinessPermission"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.accountmgt.permissions.CitizenPermission"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.accountmgt.util.AccountMgtUtil"%>
<%@page import="org.opencps.datamgt.search.DictItemDisplayTerms"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>
<%
	String tabs1 = ParamUtil.getString(request, "tabs1", AccountMgtUtil.TOP_TABS_CITIZEN);
	
	PortletURL searchURL = renderResponse.createRenderURL();
	
	int [] accoutStatuses = new int [4];
	accoutStatuses[0] = PortletConstants.ACCOUNT_STATUS_REGISTERED;
	accoutStatuses[1] = PortletConstants.ACCOUNT_STATUS_CONFIRMED;
	accoutStatuses[2] = PortletConstants.ACCOUNT_STATUS_APPROVED;
	accoutStatuses[3] = PortletConstants.ACCOUNT_STATUS_LOCKED;
	
	
%>

<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:if test="<%=CitizenPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_CITIZEN) && tabs1.equals(AccountMgtUtil.TOP_TABS_CITIZEN)%>">
			<%
				searchURL.setParameter("mvcPath", templatePath + "citizenlist.jsp");
				searchURL.setParameter("tabs1", AccountMgtUtil.TOP_TABS_CITIZEN);
			%>
		</c:if>
		<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_BUSINESS) && tabs1.equals(AccountMgtUtil.TOP_TABS_BUSINESS)%>">
			<%
				searchURL.setParameter("mvcPath", templatePath + "businesslist.jsp");
				searchURL.setParameter("tabs1", AccountMgtUtil.TOP_TABS_BUSINESS);
			%>
			
		</c:if>
	</aui:nav>
	
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<c:if test="<%=tabs1.equals(AccountMgtUtil.TOP_TABS_CITIZEN)%>">
						<aui:row>
					
							<aui:col width="50">
								<aui:select name="<%=CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS %>" label="<%=StringPool.BLANK %>">
									<%
										for(int i=0; i<accoutStatuses.length; i++) {
											%>
												<aui:option value="<%=accoutStatuses[i] %>">
													<liferay-ui:message key="<%=PortletUtil.getAccountStatus(accoutStatuses[i], themeDisplay.getLocale()) %>" />
												</aui:option>
											<%
											
										}
									%>
								</aui:select>
							</aui:col>
							<aui:col width="30">
								<liferay-ui:input-search 
									id="keywords1" 
									name="keywords" 
									placeholder='<%= LanguageUtil.get(locale, "name") %>' 			
								/>
							</aui:col>
							
						</aui:row>
					</c:if>
					<c:if test="<%=tabs1.equals(AccountMgtUtil.TOP_TABS_BUSINESS)%>">
						
						<aui:row>
					
							<aui:col width="50">
								<aui:select name="<%=BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS %>" label="<%=StringPool.BLANK %>">
									<%
										for(int i=0; i<accoutStatuses.length; i++) {
											%>
												<aui:option value="<%=accoutStatuses[i] %>">
													<liferay-ui:message key="<%=PortletUtil.getAccountStatus(accoutStatuses[i], themeDisplay.getLocale()) %>" />
												</aui:option>
											<%
											
										}
									%>
								</aui:select>
							</aui:col>
							<aui:col width="30">
								<liferay-ui:input-search 
									id="keywords1" 
									name="keywords" 
									placeholder='<%= LanguageUtil.get(locale, "name") %>' 			
								/>
							</aui:col>
							
						</aui:row>
					</c:if>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.data_management.admin.toolbar.jsp");
%>