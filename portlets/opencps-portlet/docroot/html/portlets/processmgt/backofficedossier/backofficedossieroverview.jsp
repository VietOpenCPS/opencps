
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="com.liferay.portal.UserLockoutException"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.service.PasswordPolicyLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.PasswordPolicy"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@ include file="../init.jsp"%>
<liferay-util:include page="/html/portlets/processmgt/backofficedossier/toptabs.jsp" servletContext="<%=application %>" />

<%
	String backURL = ParamUtil.getString(request, "backURL");
	User mappingUser = (User)request.getAttribute(WebKeys.USER_MAPPING_ENTRY);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficedossieroverview.jsp");	
	
	Integer dossierId = ParamUtil.getInteger(request, "dossierId");
	_log.info("DOSSIER ID: " + dossierId);
	
	Dossier dossier = null;
	
	try {
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
	}
	catch (NoSuchDossierException ex) {
		
	}
	
	PasswordPolicy passwordPolicy = null;

	if (mappingUser == null) {
		passwordPolicy = PasswordPolicyLocalServiceUtil.getDefaultPasswordPolicy(company.getCompanyId());
	}
	else {
		passwordPolicy = mappingUser.getPasswordPolicy();
	}
	
	String[] processOrderSections = new String[]{"dossieroverview", "dossierfile", "dossierhistory"};
	
	String[][] categorySections = {processOrderSections};
	
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='back'
/>
<div class="ocps-history-process-bound-navigator">

	<liferay-util:buffer var="htmlTop">
		<c:if test="<%= mappingUser != null %>">
			<div class="user-info">
				<div class="float-container">
					<img alt="<%= HtmlUtil.escape(mappingUser.getFullName()) %>" class="user-logo" src="<%= mappingUser.getPortraitURL(themeDisplay) %>" />
	
					<span class="user-name"><%= HtmlUtil.escape(mappingUser.getFullName()) %></span>
				</div>
			</div>
		</c:if> 
	</liferay-util:buffer>
	
	<liferay-util:buffer var="htmlBottom">
	
		<%
		boolean lockedOut = false;
	
		if ((mappingUser != null) && (passwordPolicy != null)) {
			try {
				UserLocalServiceUtil.checkLockout(mappingUser);
			}
			catch (UserLockoutException ule) {
				lockedOut = true;
			}
		}
		%>
	
		<c:if test="<%= lockedOut %>">
			<aui:button-row>
				<div class="alert alert-block"><liferay-ui:message key="this-user-account-has-been-locked-due-to-excessive-failed-login-attempts" /></div>
	
				<%
				String taglibOnClick = renderResponse.getNamespace() + "saveUser('unlock');";
				%>
	
				<aui:button onClick="<%= taglibOnClick %>" value="unlock" />
			</aui:button-row>
		</c:if>
	</liferay-util:buffer>
	<div class="opencps-form-navigator-container">
		<liferay-ui:form-navigator
			backURL="<%= backURL %>"
			categoryNames="<%= ProcessMgtUtil._PROCESS_ORDER_CATEGORY_NAMES %>"
			categorySections="<%= categorySections %>"
			htmlBottom="<%= htmlBottom %>"
			htmlTop="<%= htmlTop %>"
			jspPath='<%=templatePath + "processorders/" %>'
			showButtons="false"
			displayStyle="left-navigator"
		/>
	</div>
</div>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backoffice.backofficedossieroverview.jsp");
%>