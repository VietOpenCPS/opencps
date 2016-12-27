
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
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>

<%@ include file="../init.jsp"%>

<%
	boolean isMultiAssign = false;
	
	String todolistDisplayStyle = GetterUtil.getString(portletPreferences.getValue("todolistDisplayStyle", "default"));
	
	String justfinishedlistDisplayStyle = GetterUtil.getString(portletPreferences.getValue("justfinishedlistDisplayStyle", "default"));
	
	String todolistOrderByType = preferences.getValue("todolistOrderByType", WebKeys.ORDER_BY_DESC);
	String justfinishedlistOrderByType = preferences.getValue("justfinishedlistOrderByType", WebKeys.ORDER_BY_DESC);
	
	String todolistOrderByField = preferences.getValue("todolistOrderByField", ProcessOrderDisplayTerms.MODIFIEDDATE);
	String justfinishedlistOrderByField = preferences.getValue("justfinishedlistOrderByField", ProcessOrderDisplayTerms.MODIFIEDDATE);
	
	boolean hiddenToDoListTreeMenuEmptyNode = GetterUtil.getBoolean(preferences.getValue("hiddenToDoListTreeMenuEmptyNode", "false"), false);
	
	boolean hiddenJustFinishedListEmptyNode = GetterUtil.getBoolean(preferences.getValue("hiddenJustFinishedListEmptyNode", "false"), false);
	
	boolean assignTaskAfterSign = GetterUtil.getBoolean(preferences.getValue("assignTaskAfterSign", "false"), false);

	String assignFormDisplayStyle = preferences.getValue("assignFormDisplayStyle", "popup");
	
	String[] reportTypes = StringUtil.split(preferences.getValue("reportTypes", ".pdf"));
	
	double offsetX = GetterUtil.getDouble(preferences.getValue("offsetX", "0.0"), 0.0);
	
	double offsetY = GetterUtil.getDouble(preferences.getValue("offsetY", "0.0"), 0.0);
	
	double imageZoom = GetterUtil.getDouble(preferences.getValue("imageZoom", "1.0"), 1.0);
	
	String processOrderDetailPage = GetterUtil.getString(portletPreferences.getValue("processOrderViewer", "default"));

	boolean hiddenTreeNodeEqualNone = GetterUtil.getBoolean(preferences.getValue("hiddenTreeNodeEqualNone", "false"));

%>