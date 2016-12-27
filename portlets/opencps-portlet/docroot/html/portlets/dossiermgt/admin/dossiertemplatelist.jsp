
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
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierTemplatePermission"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateSearch"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateSearchTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.util.MessageKeys"%>

<%@ include file="../init.jsp"%>

<%-- <liferay-util:include page='<%=templatePath + "edit_dossier.jsp" %>' servletContext="<%=application %>" /> --%>

<%
	List<DossierTemplate> dossierTemplates =
		new ArrayList<DossierTemplate>();

	int totalCount = 0;

	boolean isPermission =
		DossierTemplatePermission.contains(
			themeDisplay.getPermissionChecker(),
			themeDisplay.getScopeGroupId(),
			ActionKeys.ADD_DOSSIER_TEMPLATE);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath +
		"dossiertemplatelist.jsp");
%>

<liferay-util:include page='<%= templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />


<liferay-ui:error 
	key="<%=MessageKeys.DOSSIER_TEMPLATE_DELETE_ERROR %>" 
	message="<%=MessageKeys.DOSSIER_TEMPLATE_DELETE_ERROR %>"
/>

<c:if test="<%=DossierTemplatePermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DOSSIER_TEMPLATE) %>">
	<liferay-util:include page='<%= templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />
</c:if>

<div class="opencps-searchcontainer-wrapper default-box-shadow radius8">
	<liferay-ui:search-container 
		searchContainer="<%= new DossierTemplateSearch(renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
	>
		<liferay-ui:search-container-results>
			<%
				DossierTemplateSearchTerms searchTerms =
							(DossierTemplateSearchTerms) searchContainer.getSearchTerms();
				dossierTemplates =
							DossierTemplateLocalServiceUtil.getDossierTemplates(
								searchTerms.getKeywords(),
								searchContainer.getStart(),
								searchContainer.getEnd(),
								searchContainer.getOrderByComparator());
						if (Validator.isNotNull(searchTerms.getKeywords())) {
							totalCount =
								DossierTemplateLocalServiceUtil.countDossierTemplatesByName(searchTerms.getKeywords());
						}
						else {
							totalCount = DossierTemplateLocalServiceUtil.countAll();
						}
						total = totalCount;
						results = dossierTemplates;
						pageContext.setAttribute("results", results);
						pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>
	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.DossierTemplate" 
			modelVar="dossierTemplate" 
			keyProperty="dossierTemplateId"
		>
			
			<%
				String templateNo = dossierTemplate.getTemplateNo();
				String templateName = dossierTemplate.getTemplateName();
				
			%>
			
			<liferay-util:buffer var="rowIndex">
				<div class="row-fluid min-width10">
					<div class="span12 bold">
						<%=row.getPos() + 1 %>
					</div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="name">
			
				<div class="row-fluid">
					
					<div class="span3 bold">
						<liferay-ui:message key="template-number"/>
					</div>
					
					<div class="span9"><%=templateNo%></div>
				</div>
				
				<div class="row-fluid">
					<div class="span3 bold-label">
						<liferay-ui:message key="template_name"/>
					</div>
					
					<div class="span9"><%=dossierTemplate.getTemplateName() %></div>
				</div>
				
				<div class="row-fluid">
					
					<div class="span3 bold-label">
						<liferay-ui:message key="template-description"/>
					</div>
					<div class="span9"><%=dossierTemplate.getDescription() %> </div>
				</div>
				
			</liferay-util:buffer>
			
			<%
				row.addText(rowIndex);
				row.setClassName("opencps-searchcontainer-row");
				row.addText(name);
				if(isPermission) {
					row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "dossier_template_action.jsp", config.getServletContext(), request, response);
				}
			%>
		</liferay-ui:search-container-row>
		
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	</liferay-ui:search-container>
</div>
