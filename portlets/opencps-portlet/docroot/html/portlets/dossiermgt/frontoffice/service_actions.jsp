<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
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
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>


<%@ include file="../init.jsp"%>

 
<%
	ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	ServiceConfig service = (ServiceConfig) row.getObject();
%> 

			
 <liferay-ui:icon-menu>
 	<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER) %>">
 		<portlet:renderURL var="addDossierURL">
			<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/frontoffice/edit_dossier.jsp"/>
			<portlet:param name="<%=DossierDisplayTerms.SERVICE_CONFIG_ID %>" value="<%=String.valueOf(service.getServiceConfigId()) %>"/>
			<portlet:param name="<%=Constants.CMD %>" value="<%=Constants.ADD %>"/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:renderURL> 
 		<liferay-ui:icon image="add" message="add" url="<%=addDossierURL.toString() %>" /> 
 	</c:if>
 	  
</liferay-ui:icon-menu> 