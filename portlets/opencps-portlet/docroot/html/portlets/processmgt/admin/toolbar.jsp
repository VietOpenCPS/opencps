<%@page import="org.opencps.processmgt.permissions.ProcessPermission"%>
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
	PortletURL searchURL = renderResponse.createRenderURL();


	boolean isPermission =
	ProcessPermission.contains(
	    themeDisplay.getPermissionChecker(),
	    themeDisplay.getScopeGroupId(), ActionKeys.ADD_PROCESS);

%>
<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		
	</aui:nav>
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input">
					<liferay-ui:input-search 
						id="keywords1"
						name="keywords"
						title="keywords"
						placeholder='<%= LanguageUtil.get(locale, "name") %>' 
					/>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<portlet:renderURL var="editProcessURL">
	<portlet:param name="mvcPath" value='<%= templatePath + "edit_process.jsp" %>'/>
	<portlet:param name="redirectURL" value="<%=currentURL %>"/>
	<portlet:param name="backURL" value="<%=currentURL %>"/>
</portlet:renderURL>

<c:if test="<%= isPermission %>">
	<aui:button-row>
		<aui:button name="add-process" value="add-process" href="<%= editProcessURL %>"></aui:button>
	</aui:button-row>
</c:if>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.admin.toolbar.jsp");
%>