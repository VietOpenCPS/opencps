<%
/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 fds * any later version.
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
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="javax.portlet.PortletConfig"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPartPermission"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierPartSearchTerms"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.search.DossierPartSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@ include file="../../init.jsp"%>
<%
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier_common/dossierpartlist.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.DOSSIER_PART_TOOLBAR);
	List<DossierPart> dossierParts = new ArrayList<DossierPart>();
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-no");
	headerNames.add("part-no");
	headerNames.add("part-name");
	headerNames.add("part-type");
	headerNames.add("part-tip");
	
	boolean isPermission =
					DossierPartPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_DOSSIER_PART);
	
	int totalCount = 0;
	long dossierTemplateId = dossierTemplate != null ? dossierTemplate.getDossierTemplateId() : 0L;

	if (isPermission) {
		headerNames.add("action");
	}
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
 // chua sap xep theo sibling
					
%>

<liferay-ui:error
	key="<%= MessageKeys.DOSSIER_PART_DELETE_ERROR %>"
	message="<%= MessageKeys.DOSSIER_PART_DELETE_ERROR %>"
 />

<c:if test="<%=DossierPartPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_PART) %>">
	<div id="<portlet:namespace/>toolbarResponse"></div>
</c:if>

<liferay-ui:search-container searchContainer="<%= new DossierPartSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>">
	<liferay-ui:search-container-results>
		<%
			
			dossierParts = DossierPartLocalServiceUtil.getDossierParts(
					dossierTemplateId);
									
			totalCount = DossierPartLocalServiceUtil.CountByTempalteId(dossierTemplateId);
			
			total = totalCount;
			results = dossierParts;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.dossiermgt.model.DossierPart" 
		modelVar="dossierPart" 
		keyProperty="dossierpartId"
	>
		<%
			String partTypeName = LanguageUtil.get(portletConfig ,themeDisplay.getLocale(), DossierMgtUtil.getNameOfPartType(dossierPart.getPartType(), themeDisplay.getLocale()));
			row.addText(String.valueOf(dossierPart.getSibling()));
			row.addText(dossierPart.getPartNo());
			row.addText(dossierPart.getPartName());
			row.addText(partTypeName);
			row.addText(dossierPart.getPartTip());
			if(isPermission) {
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "dossier_part_actions.jsp", config.getServletContext(), request, response);
			}
		%>
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>