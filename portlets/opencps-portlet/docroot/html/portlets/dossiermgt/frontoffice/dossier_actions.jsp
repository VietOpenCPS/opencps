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
	Dossier dossier = (Dossier) row.getObject();
%> 

			
 <liferay-ui:icon-menu>
 	<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER) %>">
 		<portlet:renderURL var="updateDossierURL">
			<portlet:param name="mvcPath" value="/html/portlets/data_management/admin/edit_dictitem.jsp"/>
			<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID %>" value="<%=String.valueOf(dossier.getDossierId()) %>"/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:renderURL> 
 		<liferay-ui:icon image="edit" message="edit" url="<%=updateDossierURL.toString() %>" /> 
 	</c:if>
 	
 	<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
 		<portlet:actionURL var="deleteDossierURL" name="deleteDossier" >
			<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID %>" value="<%=String.valueOf(dossier.getDossierId()) %>"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:actionURL> 
		<liferay-ui:icon-delete image="delete" confirmation="are-you-sure-delete-entry" message="delete"  url="<%=deleteDossierURL.toString() %>" />
 	</c:if>
	  
</liferay-ui:icon-menu> 