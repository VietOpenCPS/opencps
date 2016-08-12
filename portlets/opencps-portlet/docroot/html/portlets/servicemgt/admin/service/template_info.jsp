<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.util.UnicodeFormatter"%>
<%@page import="org.opencps.servicemgt.service.TemplateFileLocalServiceUtil"%>
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

<%@ include file="../../init.jsp" %>

<%
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_ENTRY);
	
	List<TemplateFile> templates = new ArrayList<TemplateFile>();
	
	int count = 0;

	if (Validator.isNotNull(serviceInfo)) {
		templates = TemplateFileLocalServiceUtil.getServiceTemplateFiles(serviceInfo.getServiceinfoId());
		count = TemplateFileLocalServiceUtil.countServiceTemplateFile(serviceInfo.getServiceinfoId());
	}
	
	String templateIdsString = ParamUtil.getString(request, "templateSearchContainerPrimaryKeys");
	
%>


<aui:input name="addTemplateIds" type="hidden" value="<%= templateIdsString %>" />
<aui:input name="deleteTemplateIds" type="hidden" />

<liferay-util:buffer var="removeTemplateIcon">
	<liferay-ui:icon
		image="unlink"
		label="<%= true %>"
		message="remove"
	/>
</liferay-util:buffer>

<liferay-ui:error-marker key="errorSection" value="roles" />

<liferay-ui:membership-policy-error />

<liferay-ui:search-container
	headerNames="fileno,filename,null"
	id="templateSearchContainer"
>
	<liferay-ui:search-container-results
		results="<%= templates %>"
		total="<%= count %>"
	/>

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
			<a class="modify-link" data-rowId="<%= template.getTemplatefileId() %>" href="javascript:;"><%= removeTemplateIcon %></a>
		</liferay-ui:search-container-column-text>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator type="opencs_page_iterator" paginate="<%= false %>" />
</liferay-ui:search-container>


<liferay-ui:icon
	cssClass="modify-link"
	iconCssClass="icon-search"
	id="selectTemplateLink"
	label="<%= true %>"
	linkCssClass="btn"
	message="select"
	method="get"
	url="javascript:;"
/>

<aui:script use="liferay-search-container">
	var AArray = A.Array;
	var Util = Liferay.Util;

	var addTemplateIds = [];

	var templateValues = document.<portlet:namespace />fm.<portlet:namespace />addTemplateIds.value;

	if (addTemplateIds) {
		addTemplateIds.push(templateValues);
	}

	var deleteTemplateIds = [];

	var searchContainer = Liferay.SearchContainer.get('<portlet:namespace />templateSearchContainer');

	var searchContainerContentBox = searchContainer.get('contentBox');

	searchContainerContentBox.delegate(
		'click',
		function(event) {
			var link = event.currentTarget;

			var rowId = link.attr('data-rowId');

			var tr = link.ancestor('tr');

			var selectTemplate = Util.getWindow('<portlet:namespace />selectTemplate');

			if (selectTemplate) {
				var selectButton = selectTemplate.iframe.node.get('contentWindow.document').one('.selector-button[data-templatefileid="' + rowId + '"]');

				Util.toggleDisabled(selectButton, false);
			}

			searchContainer.deleteRow(tr, rowId);

			AArray.removeItem(addTemplateIds, rowId);

			deleteTemplateIds.push(rowId);

			document.<portlet:namespace />fm.<portlet:namespace />addTemplateIds.value = addTemplateIds.join(',');
			document.<portlet:namespace />fm.<portlet:namespace />deleteTemplateIds.value = deleteTemplateIds.join(',');
		},
		'.modify-link'
	);

	Liferay.on(
		'<portlet:namespace />enableRemovedTemplates',
		function(event) {
			event.selectors.each(
				function(item, index, collection) {
					var templatefileid = item.attr('data-templatefileid');

					if (AArray.indexOf(deleteTemplateIds, templatefileid) != -1) {
						Util.toggleDisabled(item, false);
					}
				}
			);
		}
	);

	var selectTemplateLink = A.one('#<portlet:namespace />selectTemplateLink');

	if (selectTemplateLink) {
		selectTemplateLink.on(
			'click',
			function(event) {
				Util.selectEntity(
					{
						dialog: {
							constrain: true,
							modal: true,
							width: 600
						},
						id: '<portlet:namespace />selectTemplate',
						title: '<liferay-ui:message arguments="template-file" key="select-x" />',
						uri: '<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcPath" value="/html/portlets/servicemgt/admin/select_template_file.jsp" /></portlet:renderURL>'
					},

					function(event) {
						var rowColumns = [];
						rowColumns.push(event.fileno);
						rowColumns.push(event.filename);
						rowColumns.push('<a class="modify-link" data-rowId="' + event.templatefileid + '" href="javascript:;"><%= UnicodeFormatter.toString(removeTemplateIcon) %></a>');

						searchContainer.addRow(rowColumns, event.templatefileid);

						searchContainer.updateDataStore();

						AArray.removeItem(deleteTemplateIds, event.templatefileid);

						addTemplateIds.push(event.templatefileid);

						document.<portlet:namespace />fm.<portlet:namespace />addTemplateIds.value = addTemplateIds.join(',');
						document.<portlet:namespace />fm.<portlet:namespace />deleteTemplateIds.value = deleteTemplateIds.join(',');
					}
				);
			}
		);
	}
</aui:script>
