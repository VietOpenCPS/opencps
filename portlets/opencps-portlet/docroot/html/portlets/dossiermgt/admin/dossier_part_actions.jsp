<%@page import="org.opencps.dossiermgt.permissions.DossierPartPermission"%>
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
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.search.DossierPartDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@ include file="../init.jsp"%>

<%
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);

	ResultRow row =
	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	DossierPart dossierPart = (DossierPart)row.getObject();

	long dossierTemplateId = dossierTemplate != null ? dossierTemplate.getDossierTemplateId() : 0L;
	long dossierPartId = dossierPart != null ? dossierPart.getDossierpartId() : 0L;

	PortletURL updateDossierPartURL = renderResponse.createRenderURL();
	
	updateDossierPartURL.setParameter(
		DossierPartDisplayTerms.DOSSIERPART_DOSSIERPARTID, 
		String.valueOf(dossierPartId));
	
	updateDossierPartURL.setParameter(
		DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID, 
		String.valueOf(dossierTemplateId));
	
	updateDossierPartURL.setParameter("backURL", currentURL);
	updateDossierPartURL.setParameter("mvcPath", templatePath + "edit_dossier_part.jsp" );
	
	PortletURL updateDossierPartChildsURL = renderResponse.createRenderURL();
	updateDossierPartChildsURL = updateDossierPartURL;
	
	

%>

<liferay-ui:icon-menu>
	<c:if test="<%=DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_PART) 
				&&	DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>"
	>		
			<liferay-ui:icon image="edit" message="edit"
				url="<%=updateDossierPartURL.toString()%>" />
			
			<c:if test="<%= dossierPart.getPartType() == PortletConstants.DOSSIER_TYPE_OTHER_PAPERS_GROUP
							|| dossierPart.getPartType() == PortletConstants.DOSSIER_TYPE_GROUPS_OPTIONAL 
							|| dossierPart.getPartType() == PortletConstants.DOSSIER_TYPE_OWN_RECORDS %>">
				<%
					updateDossierPartChildsURL.setParameter("isAddChild", "isAddChild");
				%> 
			
				<liferay-ui:icon image="add" message="add-childs-part"
					url="<%=updateDossierPartURL.toString()%>" />
			
			</c:if>	
	</c:if>
	
	<c:if test="<%=DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
		<portlet:actionURL var="deleteDossierParttURL" name="deleteDossierPart">
			<portlet:param name="<%=DossierPartDisplayTerms.DOSSIERPART_DOSSIERPARTID %>" value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"/>
			<portlet:param name="CurrentURL" value="<%=currentURL %>"/>
		</portlet:actionURL>
		
		<liferay-ui:icon image="delete" message="delete"
				url="<%=deleteDossierParttURL.toString()%>" />
	</c:if>
</liferay-ui:icon-menu> 