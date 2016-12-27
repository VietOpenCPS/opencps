
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

<%@page import="org.opencps.statisticsmgt.model.DossiersStatistics"%>
<%@page import="com.liferay.portal.kernel.template.TemplateHandlerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.template.TemplateHandler"%>

<%@ include file="../../init.jsp" %>


<liferay-portlet:actionURL var="configurationActionURL" portletConfiguration="true"/>

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:fieldset>
		<div class="display-template">
			<%
				TemplateHandler templateHandler = TemplateHandlerRegistryUtil.getTemplateHandler(DossiersStatistics.class.getName());
			%>
		<liferay-ui:ddm-template-selector
			classNameId="<%= PortalUtil.getClassNameId(templateHandler.getClassName()) %>"
			displayStyle="<%= displayStyle %>"
			displayStyleGroupId="<%= displayStyleGroupId %>"
			refreshURL="<%= PortalUtil.getCurrentURL(request) %>"
			showEmptyOption="<%= true %>"
		/>
		</div>
	</aui:fieldset>
	
	<aui:button type="submit" name="submit"></aui:button>
</aui:form>