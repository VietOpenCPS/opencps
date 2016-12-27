package org.opencps.statisticsmgt.dashboard.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

/**
 * @author trungnt
 *
 */
public class TreeDashBoardConfigurationImpl extends
		DefaultConfigurationAction {

	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		// TODO Auto-generated method stub
		String portletResource = ParamUtil.getString(actionRequest,
				"portletResource");

		PortletPreferences preferences = PortletPreferencesFactoryUtil
				.getPortletSetup(actionRequest, portletResource);
		String displayStyle = ParamUtil
				.getString(actionRequest, "displayStyle");
		preferences.setValue("displayStyle", displayStyle);
		preferences.store();
		
		super.processAction(portletConfig, actionRequest, actionResponse);
	}
}