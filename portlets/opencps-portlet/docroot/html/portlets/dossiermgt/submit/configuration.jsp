<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.model.Layout"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.service.LayoutLocalServiceUtil"%>
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

<%@ include file="init.jsp" %>

<liferay-ui:success key="potlet-config-saved" message="portlet-configuration-have-been-successfully-saved" />

<liferay-portlet:actionURL var="configurationActionURL" portletConfiguration="true"/>

<%
	boolean privateLayout = true;
	
	List<Layout> privLayouts = LayoutLocalServiceUtil.getLayouts(scopeGroupId, true);
	List<Layout> pubLayouts = LayoutLocalServiceUtil.getLayouts(scopeGroupId, false);
	
	List<Layout> allLayout = new ArrayList<Layout>();
	
	for (Layout privLayout : privLayouts) {
		allLayout.add(privLayout);
	}

	for (Layout pubLayout : pubLayouts) {
		allLayout.add(pubLayout);
	}
%>

<aui:form action="<%= configurationActionURL %>" method="post" name="configurationForm">
	
	<aui:row>
		<aui:select name="plid" id="plid" label="plid-submit">
			<%
				for (Layout lout : allLayout) {
			%>
				<aui:option value="<%= lout.getPlid() %>" selected="<%=Long.valueOf(plidRes) ==  lout.getPlid()%>"><%= lout.getName(locale) %></aui:option>
			<%
				}
			%>
		</aui:select>
	</aui:row>
	
	<aui:row>
		<aui:select name="plidServiceDetail" id="plidServiceDetail" label="plid-service-detail">
			<%
				for (Layout lout : allLayout) {
			%>
				<aui:option value="<%= lout.getPlid() %>" selected="<%=Long.valueOf(plidServiceDetail) ==  lout.getPlid()%>"><%= lout.getName(locale) %></aui:option>
			<%
				}
			%>
		</aui:select>
	</aui:row>

	<aui:button type="submit" name="Save" value="save"></aui:button>

</aui:form>
