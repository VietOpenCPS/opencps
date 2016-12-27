
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

<%@page import="com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateUtil"%>
<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>

<%@ include file="/init.jsp" %>

<%
	PortletPreferences preferences = renderRequest.getPreferences();
	
	portletResource = ParamUtil.getString(request, "portletResource");
	
	if (Validator.isNotNull(portletResource)) {
		preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
	}
	
	Date now = new Date();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(now);
	
	int currentMonth = calendar.get(Calendar.MONTH) + 1;
	int currentYear = calendar.get(Calendar.YEAR);
	
	
	String displayStyle = GetterUtil.getString(portletPreferences
			.getValue("displayStyle", StringPool.BLANK));
	
	long displayStyleGroupId = GetterUtil.getLong(
			portletPreferences.getValue("displayStyleGroupId", null),
			scopeGroupId);

	long portletDisplayDDMTemplateId = PortletDisplayTemplateUtil
			.getPortletDisplayTemplateDDMTemplateId(
					displayStyleGroupId, displayStyle);
%>

