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
<%@ include file="../../init.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.permission.DossierPartPermission"%>
<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiertemplatelist.jsp");
	List<DossierPart> dossierTemplates = new ArrayList<DossierPart>();
	int totalCount = 0;
	
	boolean isPermission =
				    DossierPartPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_DOSSIER_PART);
%>

<portlet:renderURL var="editDossierPartURL">
	<portlet:param name="mvcPath" value='<%=templatePath + "edit_dossier_part.jsp" %>'/>
</portlet:renderURL>
<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:if 
				test="<%=DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_PART) 
			%>"
		>
			<aui:nav-item 
				id="addDossierPart" 
				label="add-dosier-part" 
				iconCssClass="icon-plus"  
				href="<%=editDossierPartURL %>"
			/>
		</c:if>
	</aui:nav>
</aui:nav-bar>
