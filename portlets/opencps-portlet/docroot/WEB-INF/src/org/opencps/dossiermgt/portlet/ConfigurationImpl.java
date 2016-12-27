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
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletURLFactoryUtil;

/**
 * @author trungnt
 *
 */
public class ConfigurationImpl implements ConfigurationAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.liferay.portal.kernel.portlet.ConfigurationAction#processAction(javax
	 * .portlet.PortletConfig, javax.portlet.ActionRequest,
	 * javax.portlet.ActionResponse)
	 */
	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {

		String portletResource = ParamUtil.getString(actionRequest,
				"portletResource");

		PortletPreferences preferences = PortletPreferencesFactoryUtil
				.getPortletSetup(actionRequest, portletResource);

		String tabs2 = ParamUtil.getString(actionRequest, "tabs2",
				"dossier-list");

		if (tabs2.equals("dossier-list")) {
			updateDossierList(preferences, actionRequest, actionResponse);
		} else if (tabs2.equals("dossier")) {
			updateDossier(preferences, actionRequest, actionResponse);
		} else if (tabs2.equals("dossier-file-list")) {
			updateDossierFileList(preferences, actionRequest, actionResponse);
		}

		preferences.store();

		SessionMessages.add(actionRequest, "potlet-config-saved");

	}

	protected void updateDossier(PortletPreferences preferences,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws ReadOnlyException {

		long plid = ParamUtil.getLong(actionRequest, "plid");

		PortletURL redirectURL = PortletURLFactoryUtil.create(
				PortalUtil.getHttpServletRequest(actionRequest),
				WebKeys.PAYMENT_MGT_PORTLET, plid, PortletRequest.RENDER_PHASE);

		redirectURL
				.setParameter("mvcPath",
						"/html/portlets/paymentmgt/frontoffice/frontofficeconfirmkeypay.jsp");

		String dossierDisplayStyle = ParamUtil.getString(actionRequest,
				"dossierDisplayStyle", "default");

		String dossierTabFocus = ParamUtil.getString(actionRequest,
				"dossierTabFocus", "dossier_part");

		String suggestionDossierStatus = ParamUtil.getString(actionRequest,
				"suggestionDossierStatus");

		String uploadFileTypes = ParamUtil.getString(actionRequest,
				"uploadFileTypes", "pdf,doc,docx,xls,png");

		boolean allowQuickCreateDossier = ParamUtil.getBoolean(actionRequest,
				"allowQuickCreateDossier");

		boolean showBackToListButton = ParamUtil.getBoolean(actionRequest,
				"showBackToListButton");

		boolean showDossierFileVersion = ParamUtil.getBoolean(actionRequest,
				"showDossierFileVersion", true);

		boolean allowQuickViewResult = ParamUtil.getBoolean(actionRequest,
				"showDossierFileVersion", true);

		float maxTotalUploadFileSize = ParamUtil.getFloat(actionRequest,
				"maxTotalUploadFileSize");

		String maxTotalUploadFileSizeUnit = ParamUtil.getString(actionRequest,
				"maxTotalUploadFileSizeUnit");

		float maxUploadFileSize = ParamUtil.getFloat(actionRequest,
				"maxUploadFileSize");

		String maxUploadFileSizeUnit = ParamUtil.getString(actionRequest,
				"maxUploadFileSizeUnit");

		preferences.setValue("dossierDisplayStyle", dossierDisplayStyle);
		preferences.setValue("dossierTabFocus", dossierTabFocus);
		preferences
				.setValue("suggestionDossierStatus", suggestionDossierStatus);
		preferences.setValue("uploadFileTypes", uploadFileTypes);

		preferences.setValue("allowQuickCreateDossier",
				String.valueOf(allowQuickCreateDossier));
		preferences.setValue("showBackToListButton",
				String.valueOf(showBackToListButton));
		preferences.setValue("showDossierFileVersion",
				String.valueOf(showDossierFileVersion));
		preferences.setValue("allowQuickViewResult",
				String.valueOf(allowQuickViewResult));

		preferences.setValue("maxTotalUploadFileSize",
				String.valueOf(maxTotalUploadFileSize));
		preferences.setValue("maxTotalUploadFileSizeUnit",
				maxTotalUploadFileSizeUnit);
		preferences.setValue("maxUploadFileSize",
				String.valueOf(maxUploadFileSize));
		preferences.setValue("maxUploadFileSizeUnit", maxUploadFileSizeUnit);

		preferences.setValue("redirectPaymentURL", redirectURL.toString());
	}

	protected void updateDossierList(PortletPreferences preferences,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws ReadOnlyException {

		String itemCode_cfg = ParamUtil
				.getString(actionRequest, "itemCode_cfg");

		String war_opencpsportlet_26_cfg = ParamUtil.getString(actionRequest,
				"war_opencpsportlet_26_cfg");

		String dossierListDisplayStyle = ParamUtil.getString(actionRequest,
				"dossierListDisplayStyle", "default");

		boolean hiddenTreeNodeEqualNone = ParamUtil.getBoolean(actionRequest,
				"hiddenTreeNodeEqualNone", true);

		boolean showServiceDomainTree = ParamUtil.getBoolean(actionRequest,
				"showServiceDomainTree", false);

		boolean displayDossierNo = ParamUtil.getBoolean(actionRequest,
				"displayDossierNo", false);

		boolean displayRecentlyResultWhenSearch = ParamUtil.getBoolean(
				actionRequest, "displayRecentlyResultWhenSearch", true);

		int dossierRecentItemDisplay = ParamUtil.getInteger(actionRequest,
				"dossierRecentItemDisplay", 2);

		String[] dossierStatusCodes = ParamUtil.getParameterValues(
				actionRequest, "dossierStatusCodes");

		preferences.setValue("showServiceDomainTree",
				String.valueOf(showServiceDomainTree));
		preferences
				.setValue("dossierListDisplayStyle", dossierListDisplayStyle);
		preferences.setValue("displayDossierNo",
				String.valueOf(displayDossierNo));
		preferences.setValue("hiddenTreeNodeEqualNone",
				String.valueOf(hiddenTreeNodeEqualNone));
		preferences.setValue("dossierRecentItemDisplay",
				String.valueOf(dossierRecentItemDisplay));
		preferences.setValue("displayRecentlyResultWhenSearch",
				String.valueOf(displayRecentlyResultWhenSearch));
		preferences.setValue("dossierStatusCodes",
				String.valueOf(StringUtil.merge(dossierStatusCodes)));

		preferences.setValue("war_opencpsportlet_26_cfg",
				war_opencpsportlet_26_cfg);

		preferences.setValue("itemCode_cfg", itemCode_cfg);

	}

	protected void updateDossierFileList(PortletPreferences preferences,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws ReadOnlyException {

		String dossierFileDisplayStyle = ParamUtil.getString(actionRequest,
				"dossierFileDisplayStyle", "default");

		String dossierFileListOrderByField = ParamUtil.getString(actionRequest,
				"dossierFileListOrderByField");

		String dossierFileListOrderByType = ParamUtil.getString(actionRequest,
				"dossierFileListOrderByType");

		boolean hideTabDossierFile = ParamUtil.getBoolean(actionRequest,
				"hideTabDossierFile");

		preferences
				.setValue("dossierFileDisplayStyle", dossierFileDisplayStyle);

		preferences.setValue("dossierFileListOrderByField",
				dossierFileListOrderByField);
		preferences.setValue("dossierFileListOrderByType",
				dossierFileListOrderByType);
		preferences.setValue("hideTabDossierFile",
				String.valueOf(hideTabDossierFile));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.liferay.portal.kernel.portlet.ConfigurationAction#render(javax.portlet
	 * .PortletConfig, javax.portlet.RenderRequest,
	 * javax.portlet.RenderResponse)
	 */
	@Override
	public String render(PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		return "/html/portlets/dossiermgt/frontoffice/configuration_.jsp";
	}

}
