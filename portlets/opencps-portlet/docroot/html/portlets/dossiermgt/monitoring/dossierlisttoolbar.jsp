<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
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

<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="../init.jsp"%>
<%
	PortletURL searchURL = renderResponse.createRenderURL();
	searchURL.setParameter("mvcPath", templatePath + "dossiermonitoringdossierlist.jsp");
	String keywordSearch = ParamUtil.getString(request, "keywords");
%>
<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer"
		cssClass="nav-display-style-buttons pull-left">

	</aui:nav>
	<aui:nav-bar-search cssClass="pull-left" style="width: 98%">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<div class="toolbar_search_input" style="width: 98%">
					<aui:row>
						<aui:col width="65">
							<label> <liferay-ui:message key="keywords" />
							</label>
							<liferay-ui:input-search id="keywords" name="keywords"
								title='<%= LanguageUtil.get(portletConfig, locale, "keywords") %>'
								placeholder="<%= LanguageUtil.get(portletConfig, locale, \"keywords\") %>" />
						</aui:col>
					</aui:row>
				</div>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.dossierlisttoolbar.jsp");
%>