
<%@page import="java.text.DecimalFormat"%>
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
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.WebKeys"%>

<%@ include file="/init.jsp" %>

<%
	DecimalFormat doubleFomart=new DecimalFormat("#,###.##");

	PortletPreferences preferences = renderRequest.getPreferences();
	
	portletResource = ParamUtil.getString(request, "portletResource");
	
	if (Validator.isNotNull(portletResource)) {
		preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
	}
	
	/* dossier list  */
	String dossierListDisplayStyle = preferences.getValue("dossierListDisplayStyle", "default");
	
	String suggestionDossierStatus = preferences.getValue("suggestionDossierStatus",StringPool.BLANK);
	
	boolean hiddenTreeNodeEqualNone = GetterUtil.getBoolean(preferences.getValue("hiddenTreeNodeEqualNone", "false"), false);
	
	boolean showServiceDomainTree = GetterUtil.getBoolean(preferences.getValue("showServiceDomainTree", "true"), true);
	
	boolean displayDossierNo =  GetterUtil.getBoolean(preferences.getValue("displayDossierNo", "false"), false);
	
	int dossierRecentItemDisplay = GetterUtil.getInteger(portletPreferences.getValue("dossierRecentItemDisplay", "2"));
	
	String[] defaultDossierStatusCodes = new String[]{PortletConstants.DOSSIER_STATUS_NEW, PortletConstants.DOSSIER_STATUS_RECEIVING, PortletConstants.DOSSIER_STATUS_PROCESSING};
	
	String[] dossierStatusCodes = StringUtil.split(portletPreferences.getValue("dossierStatusCodes", StringUtil.merge(defaultDossierStatusCodes)));
	
	String war_opencpsportlet_26_cfg = GetterUtil.getString(portletPreferences.getValue("war_opencpsportlet_26_cfg", ""));
	
	String itemCode_cfg = GetterUtil.getString(portletPreferences.getValue("itemCode_cfg", ""));
	
	/* dossier  */
	String dossierDisplayStyle = preferences.getValue("dossierDisplayStyle", "default");
	
	String redirectPaymentURL = preferences.getValue("redirectPaymentURL",StringPool.BLANK);
	
	boolean allowQuickCreateDossier = GetterUtil.getBoolean(preferences.getValue("allowQuickCreateDossier", "false"), false);
	
	boolean allowQuickViewResult = GetterUtil.getBoolean(preferences.getValue("allowQuickViewResult", "false"), false);
	
	String dossierTabFocus = GetterUtil.getString(preferences.getValue("dossierTabFocus", StringPool.BLANK), StringPool.BLANK);
	

	boolean showDossierFileVersion = GetterUtil.getBoolean(preferences.getValue("showDossierFileVersion", "true"), true);
	
	boolean showBackToListButton = GetterUtil.getBoolean(preferences.getValue("showBackToListButton", "true"), true);
	
	String uploadFileTypes = preferences.getValue("uploadFileTypes", "pdf,doc,docx,xls,png");

	
	float maxTotalUploadFileSize = GetterUtil.getFloat(preferences.getValue("maxTotalUploadFileSize", StringPool.BLANK));
	
	String maxTotalUploadFileSizeUnit = preferences.getValue("maxTotalUploadFileSizeUnit", StringPool.BLANK);
	
	float maxUploadFileSize = GetterUtil.getFloat(preferences.getValue("maxUploadFileSize", StringPool.BLANK));
	
	String maxUploadFileSizeUnit = preferences.getValue("maxUploadFileSizeUnit", StringPool.BLANK);
	
	/*dossier file list  */
	
	String dossierFileDisplayStyle = preferences.getValue("dossierFileListOrderByField", "default");
	
	String dossierFileListOrderByField = preferences.getValue("dossierFileListOrderByField", StringPool.BLANK);
	
	String dossierFileListOrderByType = preferences.getValue("dossierFileListOrderByType", WebKeys.ORDER_BY_DESC);
	
	boolean hideTabDossierFile = GetterUtil.getBoolean(preferences.getValue("hideTabDossierFile", "false"), false);
	
	//String orderFieldDossierFile = preferences.getValue("orderFieldDossierFile",StringPool.BLANK);
	
	//String orderBydDossierFile = preferences.getValue("orderBydDossierFile",StringPool.BLANK);
	
	long plidRes = GetterUtil.getLong(preferences.getValue("plid", "0"), 0);
	
	boolean displayRecentlyResultWhenSearch = GetterUtil.getBoolean(preferences.getValue("displayRecentlyResultWhenSearch", "false"), false);

%>

