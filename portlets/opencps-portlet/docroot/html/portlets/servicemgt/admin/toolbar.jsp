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
	String tabs1 = ParamUtil.getString(request, "tabs1", UserMgtUtil.TOP_TABS_WORKINGUNIT);
	PortletURL searchURL = renderResponse.createRenderURL();
%>

<c:choose>
	
	<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_SERVICE) %>">
		
		<portlet:renderURL var="editServiceURL">
			<portlet:param name="mvcPath" value='<%= templatePath + "edit_service.jsp" %>'/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:renderURL>
		<c:if test="<%=ServicePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_SERVICE) %>">
			<aui:button name="add-service" value="add-service" href="<%= editServiceURL%>"/>
		</c:if>
		
	</c:when>
	
	<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_DOCUMENT) %>">
	
	</c:when>
	
	<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_CATEGORY) %>">
	
	</c:when>
	
	<c:when test="<%= tabs1.contentEquals(ServiceUtil.TOP_TABS_LEVEL) %>">
	
	</c:when>
	
</c:choose>

