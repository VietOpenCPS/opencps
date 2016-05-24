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

<aui:form action="<%= configurationURL.toString() %>" method="post" name="fm">
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	
	<aui:select name="servicepage" label="config-servicepage">
		<%
			for (Layout l : layoutPages) {
				if (l.getName().equals(servicepage_cfg)) {
		%>
		<aui:option selected="<%= true %>" value="<%= l.getFriendlyURL() %>"><%= l.getName() %></aui:option>
		<%
				}
				else {
		%>
		<aui:option value="<%= l.getFriendlyURL() %>"><%= l.getName() %></aui:option>
		<%
				}
			}
		%>
	</aui:select>
    
    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>
</aui:form>

