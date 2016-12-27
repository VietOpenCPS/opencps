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

package org.opencps.dossiermgt.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletURLFactoryUtil;


/**
 * @author khoavd
 *
 */
public class ConfigurationImpl_ implements ConfigurationAction{

	/* (non-Javadoc)
     * @see com.liferay.portal.kernel.portlet.ConfigurationAction#processAction(javax.portlet.PortletConfig, javax.portlet.ActionRequest, javax.portlet.ActionResponse)
     */
	@Override
	public void processAction(
	    PortletConfig portletConfig, ActionRequest actionRequest,
	    ActionResponse actionResponse)
	    throws Exception {

		long plid = ParamUtil.getLong(actionRequest, "plid");
		
		long itemsToDisplay = ParamUtil.getLong(actionRequest, "itemsToDisplay");
		
		long timeToReLoad = ParamUtil.getLong(actionRequest, "timeToReLoad", 0);
		
		String templatesToDisplay = ParamUtil.getString(actionRequest, "templatesToDisplay", "default");
		
		String orderFieldDossierFile = ParamUtil.getString(actionRequest, "orderFieldDossierFile");
		
		String orderBydDossierFile = ParamUtil.getString(actionRequest, "orderBydDossierFile");
		
		String dossierStatusConfig = ParamUtil.getString(actionRequest, "dossierStatusConfig");
		
		boolean displayDossierNo = ParamUtil.getBoolean(actionRequest, "displayDossierNo");
		
		boolean displayRecentlyResultWhenSearch = ParamUtil.getBoolean(actionRequest, "displayRecentlyResultWhenSearch");
		
		boolean showVersionItem = ParamUtil.getBoolean(actionRequest, "showVersionItem");
		
		boolean showBackToListButton = ParamUtil.getBoolean(actionRequest, "showBackToListButton");
		
		boolean showServiceDomainIdTree = ParamUtil.getBoolean(actionRequest, "showServiceDomainIdTree");
		
		boolean hideTabDossierFile = ParamUtil.getBoolean(actionRequest, "hideTabDossierFile");
		
		boolean showTabDossierResultFirst = ParamUtil.getBoolean(actionRequest, "showTabDossierResultFirst");
		
		boolean hiddenTreeNodeEqualNone = ParamUtil.getBoolean(actionRequest, "hiddenTreeNodeEqualNone");
		
		boolean allowResultQuickView = ParamUtil.getBoolean(actionRequest, "allowResultQuickView");
		
		boolean allowQuickCreateDossier = ParamUtil.getBoolean(actionRequest, "allowQuickCreateDossier");

		String fileTypes = ParamUtil.getString(actionRequest, "fileTypes");
		
		float maxTotalUploadFileSize = ParamUtil.getFloat(actionRequest, "maxTotalUploadFileSize");
		
		String maxTotalUploadFileSizeUnit = ParamUtil.getString(actionRequest, "maxTotalUploadFileSizeUnit");
		
		float maxUploadFileSize = ParamUtil.getFloat(actionRequest, "maxUploadFileSize");
		
		String maxUploadFileSizeUnit = ParamUtil.getString(actionRequest, "maxUploadFileSizeUnit");
		
		PortletURL redirectURL =
		    PortletURLFactoryUtil.create(
		        PortalUtil.getHttpServletRequest(actionRequest),
		        WebKeys.PAYMENT_MGT_PORTLET, plid, PortletRequest.RENDER_PHASE);

		redirectURL.setParameter(
		    "mvcPath",
		    "/html/portlets/paymentmgt/frontoffice/frontofficeconfirmkeypay.jsp");

		String portletResource =
		    ParamUtil.getString(actionRequest, "portletResource");

		PortletPreferences preferences =
		    PortletPreferencesFactoryUtil.getPortletSetup(
		        actionRequest, portletResource);
		
		preferences.setValue("plid", String.valueOf(plid));
		preferences.setValue("redirectPaymentURL", redirectURL.toString());
		preferences.setValue("displayDossierNo", String.valueOf(displayDossierNo));
		preferences.setValue("displayRecentlyResultWhenSearch", String.valueOf(displayRecentlyResultWhenSearch));
		
		preferences.setValue("itemsToDisplay", String.valueOf(itemsToDisplay));
		preferences.setValue("templatesToDisplay", String.valueOf(templatesToDisplay));
		preferences.setValue("timeToReLoad", String.valueOf(timeToReLoad));
		
		preferences.setValue("showVersionItem", String.valueOf(showVersionItem));
		
		preferences.setValue("showBackToListButton", String.valueOf(showBackToListButton));
		
		preferences.setValue("orderFieldDossierFile", orderFieldDossierFile);
		
		preferences.setValue("dossierStatusConfig", dossierStatusConfig);
		
		preferences.setValue("orderBydDossierFile", orderBydDossierFile);
		
		preferences.setValue("showServiceDomainIdTree", String.valueOf(showServiceDomainIdTree));
		
		preferences.setValue("hideTabDossierFile", String.valueOf(hideTabDossierFile));
		
		preferences.setValue("showTabDossierResultFirst", String.valueOf(showTabDossierResultFirst));
		
		preferences.setValue("hiddenTreeNodeEqualNone", String.valueOf(hiddenTreeNodeEqualNone));
		
		preferences.setValue("allowResultQuickView", String.valueOf(allowResultQuickView));

		preferences.setValue("fileTypes", fileTypes);
		
		preferences.setValue("maxTotalUploadFileSize", String.valueOf(maxTotalUploadFileSize));
		
		preferences.setValue("maxTotalUploadFileSizeUnit", maxTotalUploadFileSizeUnit);
		
		preferences.setValue("maxUploadFileSize", String.valueOf(maxUploadFileSize));
		
		preferences.setValue("maxUploadFileSizeUnit", maxUploadFileSizeUnit);
		
		preferences.setValue("allowQuickCreateDossier", String.valueOf(allowQuickCreateDossier));
		
		preferences.store();

		SessionMessages.add(actionRequest, "potlet-config-saved");

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.portlet.ConfigurationAction#render(javax.portlet
	 * .PortletConfig, javax.portlet.RenderRequest,
	 * javax.portlet.RenderResponse)
	 */
	@Override
	public String render(
	    PortletConfig portletConfig, RenderRequest renderRequest,
	    RenderResponse renderResponse)
	    throws Exception {
		return "/html/portlets/dossiermgt/frontoffice/configuration.jsp";
	}

}
