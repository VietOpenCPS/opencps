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
	TemplateFileSearch searchContainerTemplate = (TemplateFileSearch)request.getAttribute("liferay-ui:search:searchContainer");

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "select_template_file.jsp");
	String eventName = ParamUtil.getString(request, "eventName", liferayPortletResponse.getNamespace() + "selectTemplate");
	
	PortletURL portletURL = renderResponse.createRenderURL();
	portletURL.setParameter("eventName", eventName);
	portletURL.setParameter("mvcPath", templatePath + "select_template_file.jsp");

	User selUser = PortalUtil.getSelectedUser(request);
%>

<aui:form action="<%= portletURL.toString() %>" method="post" name="selectTemplateFm">

	<liferay-ui:search-container
		headerNames="file-no,file-name,null"
		searchContainer="<%= new TemplateFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
	>
		<%
			TemplateSearchTerms searchTerms = (TemplateSearchTerms) searchContainer.getSearchTerms();
		%>
		
		<liferay-ui:search-form page='<%= templatePath + "template_search.jsp" %>' servletContext="<%= application %>"/>
		
		<liferay-ui:search-container-results>
			<%
				total = TemplateFileLocalServiceUtil.countTemplateFiles(scopeGroupId, searchTerms.getKeywords());
				results = TemplateFileLocalServiceUtil.searchTemplateFiles(scopeGroupId, searchTerms.getKeywords(), searchContainer.getStart(), searchContainer.getEnd());
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="org.opencps.servicemgt.model.TemplateFile"
			keyProperty="templatefileId"
			modelVar="template"
		>

			<liferay-ui:search-container-column-text
				name="fileNo"
				value="<%= HtmlUtil.escape(template.getFileNo()) %>"
			/>

			<liferay-ui:search-container-column-text
				name="fileName"
				value="<%= HtmlUtil.escape(template.getFileName()) %>"
			/>

			<liferay-ui:search-container-column-text>
				<%
				Map<String, Object> data = new HashMap<String, Object>();

				data.put("templatefileId", template.getTemplatefileId());
				data.put("fileno", HtmlUtil.escapeAttribute(template.getFileNo()));
				data.put("filename", HtmlUtil.escapeAttribute(template.getFileName()));
				%>

				<aui:button cssClass="selector-button" data="<%= data %>" value="choose" />
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	</liferay-ui:search-container>
</aui:form>

<aui:script use="aui-base">
	var Util = Liferay.Util;

	var openingLiferay = Util.getOpener().Liferay;

	openingLiferay.fire(
		'<portlet:namespace />enableRemovedTemplates',
		{
			selectors: A.all('.selector-button:disabled')
		}
	);

	Util.selectEntityHandler('#<portlet:namespace />selectTemplateFm', '<%= HtmlUtil.escapeJS(eventName) %>', <%= selUser != null %>);
</aui:script>