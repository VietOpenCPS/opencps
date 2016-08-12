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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>

<%@ include file="../init.jsp" %>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "servicetemplatefilelist.jsp");
	iteratorURL.setParameter("tabs1", ServiceUtil.TOP_TABS_TEMPLATE);
%>
<div class="ocps-serviceinfo-list">

<liferay-util:include page='<%= templatePath + "toptabs.jsp"%>' servletContext="<%=application %>" />
<liferay-util:include page='<%= templatePath + "toolbar.jsp"%>' servletContext="<%=application %>" />

<liferay-ui:search-container searchContainer="<%= new TemplateFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	<liferay-ui:search-container-results>
		<%
			TemplateSearchTerms searchTerms = (TemplateSearchTerms) searchContainer.getSearchTerms();
			
			String[] fullNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				fullNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			total = TemplateFileLocalServiceUtil.countTemplateFiles(scopeGroupId, searchTerms.getKeywords());
			results = TemplateFileLocalServiceUtil.searchTemplateFiles(scopeGroupId, searchTerms.getKeywords(), searchContainer.getStart(), searchContainer.getEnd());
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
		
	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row 
		className="org.opencps.servicemgt.model.TemplateFile" 
		modelVar="template" 
		keyProperty="templatefileId"
	>
		<%
			PortletURL editURL = renderResponse.createRenderURL();
			editURL.setParameter("mvcPath", templatePath + "edit_template.jsp");
			editURL.setParameter("templateFileId", String.valueOf(template.getTemplatefileId()));
			editURL.setParameter("backURL", currentURL);
			
			// no column
			row.addText(String.valueOf(row.getPos() + 1), editURL);
		
			// file no
			row.addText(template.getFileNo(), editURL);
			
			// file name
			row.addText(template.getFileName(), editURL);
			

			//action column
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "template_actions.jsp", config.getServletContext(), request, response);
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator type="opencs_page_iterator"/>

</liferay-ui:search-container>
</div>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.servicemgt.admin.servicetemplate.jsp");
%>

