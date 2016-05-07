<%@page import="org.opencps.dossiermgt.permissions.ServiceConfigPermission"%>
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
<%@page import="org.opencps.dossiermgt.search.ServiceConfigDisplayTerms"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@ include file="../init.jsp"%>
<%
	ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	ServiceConfig serviceConfig = (ServiceConfig)row.getObject();
	String redirectURL = currentURL;
%>

<liferay-ui:icon-menu>
	<c:if test="<%=ServiceConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="updateServiceConfig">
			<portlet:param 
				name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICECONFIGID %>" 
				value="<%=String.valueOf(serviceConfig.getServiceConfigId()) %>"
			/>
			
			<portlet:param name="backURL" value="<%=currentURL %>"/>
			
			<portlet:param name="mvcPath" value='<%=templatePath + "edit_service_config.jsp"%>'/>
		</portlet:renderURL>
		
		<liferay-ui:icon 
			image="edit" 
			message="edit"
			url="<%=updateServiceConfig.toString()%>" 
		/>
	</c:if>
	
	<c:if test="<%=ServiceConfigPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
		<portlet:actionURL var="deleteServiceConfigURL" name="deleteServiceConfig">
			<portlet:param 
				name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICECONFIGID %>" 
				value="<%=String.valueOf(serviceConfig.getServiceConfigId()) %>"
			/>
			<portlet:param name="CurrentURL" value="<%=currentURL %>"/>
		</portlet:actionURL>
		<liferay-ui:icon image="delete" message="delete"
			url="<%=deleteServiceConfigURL.toString()%>" 
		/>
	</c:if>
</liferay-ui:icon-menu>
