<%@page import="com.liferay.portal.service.LayoutLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Layout"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
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
<%  
	String servicepage_cfg = GetterUtil.getString(portletPreferences.getValue("servicepage", StringPool.BLANK));
	List<String> portletIdList = themeDisplay.getLayoutTypePortlet().getPortletIds();
	List<Layout> layoutPages = LayoutLocalServiceUtil.getLayouts(scopeGroupId, false);
%>
<liferay-portlet:actionURL var="configurationURL" portletConfiguration="true" />
<liferay-ui:success key="config-stored" message="config-stored"
/>
<liferay-ui:success key="dossier-reindex" message="dossier-reindex-success"
/>
<liferay-ui:success key="dossierfile-reindex" message="dossierfile-reindex-success"
/>
<liferay-ui:error key="dossier-reindex-error" message="dossier-reindex-error" />
<liferay-ui:error key="dossierfile-reindex-error" message="dossierfile-reindex-error" />

<aui:form action="<%= configurationURL.toString() %>" method="post" name="fm">
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	
	<aui:select name="servicepage" label="config-servicepage">
		<%
			for (Layout l : layoutPages) {
				if (l.getFriendlyURL().equals(servicepage_cfg)) {
		%>
		<aui:option selected="<%= true %>" value="<%= l.getFriendlyURL() %>"><%= l.getName() %></aui:option>
		<%
				}
				else {
		%>
		<aui:option selected="<%= false %>" value="<%= l.getFriendlyURL() %>"><%= l.getName() %></aui:option>
		<%
				}
			}
		%>
	</aui:select>
    <aui:input name="action" type="hidden" id="action"></aui:input>
    <aui:button-row>
        <aui:button type="button" id="save" name="save" value="save" />
        <aui:button type="button" id="reindexdossier" name="reindexdossier" value="reindexdossier" />        
        <aui:button type="button" id="reindexdossierfile" name="reindexdossierfile" value="reindexdossierfile" />        
    </aui:button-row>
</aui:form>

<aui:script>
	AUI().ready(function(A) {
		var btnSave = A.one('#<portlet:namespace />save');
		var btnReindexDossier = A.one('#<portlet:namespace />reindexdossier');
		var btnReindexDossierFile = A.one('#<portlet:namespace />reindexdossierfile');
		
		if(btnSave) {
			btnSave.on('click',function() {
				A.one('#<portlet:namespace />action').set('value', 'save');
				document.getElementById('<portlet:namespace />fm').submit();
			});
		}
		if (btnReindexDossier) {
			btnReindexDossier.on('click',function() {
				A.one('#<portlet:namespace />action').set('value', 'reindexdossier');
				document.getElementById('<portlet:namespace />fm').submit();
			});			
		}
		if (btnReindexDossierFile) {
			btnReindexDossierFile.on('click',function() {
				A.one('#<portlet:namespace />action').set('value', 'reindexdossierfile');
				document.getElementById('<portlet:namespace />fm').submit();
			});			
		}
	});
</aui:script>
