
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

<%@page import="javax.portlet.PortletPreferences"%>
<%@ include file="/init.jsp" %>

<%
	PortletPreferences preferences = renderRequest.getPreferences();
	
	portletResource = ParamUtil.getString(request, "portletResource");
	
	if (Validator.isNotNull(portletResource)) {
		preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
	}
	
	String redirectPaymentURL = preferences.getValue("redirectPaymentURL",StringPool.BLANK);
	
	String orderFieldDossierFile = preferences.getValue("orderFieldDossierFile",StringPool.BLANK);
	
	String orderBydDossierFile = preferences.getValue("orderBydDossierFile",StringPool.BLANK);
	
	boolean displayRecentlyResultWhenSearch = GetterUtil.getBoolean(preferences.getValue("displayRecentlyResultWhenSearch", "false"), false);
	
	boolean displayDossierNo =  GetterUtil.getBoolean(preferences.getValue("displayDossierNo", "false"), false);
			
	boolean showVersionItem = GetterUtil.getBoolean(preferences.getValue("showVersionItem", "true"), true);
	
	boolean showBackToListButton = GetterUtil.getBoolean(preferences.getValue("showBackToListButton", "true"), true);
	
	boolean showServiceDomainIdTree = GetterUtil.getBoolean(preferences.getValue("showServiceDomainIdTree", "true"), true);
	
	boolean hideTabDossierFile = GetterUtil.getBoolean(preferences.getValue("hideTabDossierFile", "false"), false);
	
	boolean showTabDossierResultFirst = GetterUtil.getBoolean(preferences.getValue("showTabDossierResultFirst", "false"), false);
	
	boolean hiddenTreeNodeEqualNone = GetterUtil.getBoolean(preferences.getValue("hiddenTreeNodeEqualNone", "false"), false);
%>

