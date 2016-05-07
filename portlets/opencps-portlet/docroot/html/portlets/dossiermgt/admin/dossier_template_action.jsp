
<%@page import="org.opencps.dossiermgt.permissions.DossierTemplatePermission"%>
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
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateDisplayTerms"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%
	ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	DossierTemplate dossierTemplate = (DossierTemplate)row.getObject();
	String redirectURL = currentURL;
%>

<liferay-ui:icon-menu>
	<c:if test="<%=DossierTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="updateDossierTemplate">
			<portlet:param 
				name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID %>" 
				value="<%=String.valueOf(dossierTemplate.getDossierTemplateId()) %>"
			/>
			
			<portlet:param name="backURL" value="<%=currentURL %>"/>
			
			<portlet:param name="mvcPath" value='<%=templatePath + "edit_dossier.jsp"%>'/>
		</portlet:renderURL>
		
		<liferay-ui:icon 
			image="edit" 
			message="edit"
			url="<%=updateDossierTemplate.toString()%>" 
		/>
	</c:if>
	
	<c:if test="<%=DossierTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
		<portlet:actionURL var="deleteDossierTemplateURL" name="deleteDossierTemplate">
			<portlet:param 
				name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID %>" 
				value="<%=String.valueOf(dossierTemplate.getDossierTemplateId()) %>"
			/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:actionURL>
		<liferay-ui:icon image="delete" message="delete"
			url="<%=deleteDossierTemplateURL.toString()%>" 
		/>
	</c:if>
</liferay-ui:icon-menu>