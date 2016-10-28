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
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>

<%
	PortletPreferences preferences1 = renderRequest.getPreferences();
	
	portletResource = ParamUtil.getString(request, "portletResource");
	
	if (Validator.isNotNull(portletResource)) {
		preferences1 = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
	}
	
	String oderByToDo = preferences1.getValue("oderByToDo", WebKeys.ORDER_BY_DESC);
	String oderByJustFinish = preferences1.getValue("oderByJustFinish", WebKeys.ORDER_BY_DESC);
	
	String oderFieldToDo = preferences1.getValue("oderFieldToDo", ProcessOrderDisplayTerms.MODIFIEDDATE);
	String oderFieldJustFinish = preferences1.getValue("oderFieldJustFinish", ProcessOrderDisplayTerms.MODIFIEDDATE);
	
	boolean hiddenTreeNodeEqualNone = GetterUtil.getBoolean(preferences.getValue("hiddenTreeNodeEqualNone", "false"), false);
	
	
%>