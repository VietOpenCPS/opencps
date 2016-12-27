/**

 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.processmgt.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

/**
 * @author trungnt
 *
 */
public class ConfigurationProcessOrderImpl implements ConfigurationAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.liferay.portal.kernel.portlet.ConfigurationAction#processAction(javax
	 * .portlet.PortletConfig, javax.portlet.ActionRequest,
	 * javax.portlet.ActionResponse)
	 */
	@Override
	public void processAction(PortletConfig arg0, ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {

		String portletResource = ParamUtil.getString(actionRequest,
				"portletResource");

		PortletPreferences preferences = PortletPreferencesFactoryUtil
				.getPortletSetup(actionRequest, portletResource);

		String tabs2 = ParamUtil.getString(actionRequest, "tabs2", "todolist");

		if (tabs2.equals("todolist")) {
			updateToDoList(preferences, actionRequest, actionResponse);
		} else if (tabs2.equals("justfinishedlist")) {
			updateJustFinishedList(preferences, actionRequest, actionResponse);
		} else if (tabs2.equals("processorder")) {
			updateProcessOrder(preferences, actionRequest, actionResponse);
		} else if (tabs2.equals("digital-signature")) {
			updateDigitalSignature(preferences, actionRequest, actionResponse);
		}

		boolean hiddenTreeNodeEqualNone = ParamUtil.getBoolean(actionRequest, "hiddenTreeNodeEqualNone");
		preferences.setValue("hiddenTreeNodeEqualNone", String.valueOf(hiddenTreeNodeEqualNone));
		
		preferences.store();

		SessionMessages.add(actionRequest, "potlet-config-saved");

	}

	protected void updateProcessOrder(PortletPreferences preferences,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws ReadOnlyException {
		String[] reportTypes = ParamUtil.getParameterValues(actionRequest,
				"reportType", new String[] { ".pdf" });
		
		String processOrderViewer = ParamUtil.getString(actionRequest,
				"processOrderViewer", "default");

		preferences.setValue("reportTypes",
				String.valueOf(StringUtil.merge(reportTypes)));
		
		preferences.setValue("processOrderViewer", processOrderViewer);

	}

	protected void updateToDoList(PortletPreferences preferences,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws ReadOnlyException {
		String todolistDisplayStyle = ParamUtil.getString(actionRequest,
				"todolistDisplayStyle", "default");
		String todolistOrderByField = ParamUtil.getString(actionRequest,
				"todolistOrderByField", ProcessOrderDisplayTerms.MODIFIEDDATE);
		String todolistOrderByType = ParamUtil.getString(actionRequest,
				"todolistOrderByType", WebKeys.ORDER_BY_DESC);
		String assignFormDisplayStyle = ParamUtil.getString(actionRequest,
				"assignFormDisplayStyle", "popup");
		boolean hiddenToDoListTreeMenuEmptyNode = ParamUtil.getBoolean(
				actionRequest, "hiddenToDoListTreeMenuEmptyNode", false);

		preferences.setValue("todolistOrderByField", todolistOrderByField);
		preferences.setValue("todolistDisplayStyle", todolistDisplayStyle);
		preferences.setValue("todolistOrderByType", todolistOrderByType);
		preferences.setValue("assignFormDisplayStyle", assignFormDisplayStyle);
		preferences.setValue("hiddenToDoListTreeMenuEmptyNode",
				String.valueOf(hiddenToDoListTreeMenuEmptyNode));

	}

	protected void updateJustFinishedList(PortletPreferences preferences,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws ReadOnlyException {
		String justfinishedlistDisplayStyle = ParamUtil.getString(
				actionRequest, "justfinishedlistDisplayStyle", "default");
		String justfinishedlistOrderByType = ParamUtil.getString(actionRequest,
				"justfinishedlistOrderByType", WebKeys.ORDER_BY_DESC);

		String justfinishedlistOrderByField = ParamUtil.getString(
				actionRequest, "justfinishedlistOrderByField",
				ProcessOrderDisplayTerms.MODIFIEDDATE);

		boolean hiddenJustFinishedListEmptyNode = ParamUtil.getBoolean(
				actionRequest, "hiddenJustFinishedListEmptyNode", false);

		preferences.setValue("justfinishedlistDisplayStyle",
				justfinishedlistDisplayStyle);
		preferences.setValue("justfinishedlistOrderByType",
				justfinishedlistOrderByType);
		preferences.setValue("justfinishedlistOrderByField",
				justfinishedlistOrderByField);

		preferences.setValue("hiddenJustFinishedListEmptyNode",
				String.valueOf(hiddenJustFinishedListEmptyNode));

	}

	protected void updateDigitalSignature(PortletPreferences preferences,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws ReadOnlyException {

		boolean assignTaskAfterSign = ParamUtil.getBoolean(actionRequest,
				"assignTaskAfterSign", false);

		double offsetX = ParamUtil.getDouble(actionRequest, "offsetX", 0.0);

		double offsetY = ParamUtil.getDouble(actionRequest, "offsetY", 0.0);

		double imageZoom = ParamUtil.getDouble(actionRequest, "imageZoom", 1.0);

		preferences.setValue("assignTaskAfterSign",
				String.valueOf(assignTaskAfterSign));

		preferences.setValue("offsetX", String.valueOf(offsetX));

		preferences.setValue("offsetY", String.valueOf(offsetY));

		preferences.setValue("imageZoom", String.valueOf(imageZoom));

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

		return "/html/portlets/processmgt/processorder/configuration_.jsp";
	}

}
