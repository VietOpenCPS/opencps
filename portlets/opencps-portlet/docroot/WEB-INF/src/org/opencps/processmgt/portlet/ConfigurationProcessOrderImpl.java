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
/**
 * 
 */
package org.opencps.processmgt.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;


/**
 * @author dunglt
 *
 */
public class ConfigurationProcessOrderImpl implements ConfigurationAction{

	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.ConfigurationAction#processAction(javax.portlet.PortletConfig, javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	@Override
	public void processAction(
		PortletConfig arg0, ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String oderByToDo = ParamUtil.getString(actionRequest, "oderByToDo", ProcessOrderDisplayTerms.MODIFIEDDATE);
		String oderByJustFinish = ParamUtil.getString(actionRequest, "oderByJustFinish", ProcessOrderDisplayTerms.MODIFIEDDATE);
		String oderFieldToDo = ParamUtil.getString(actionRequest, "oderFieldToDo", WebKeys.ORDER_BY_DESC);
		String oderFieldJustFinish = ParamUtil.getString(actionRequest, "oderFieldJustFinish", WebKeys.ORDER_BY_DESC);
		String portletResource =
					    ParamUtil.getString(actionRequest, "portletResource");

		String templatesToDisplay = ParamUtil.getString(actionRequest, "templatesToDisplay", "default");
		
		boolean hiddenTreeNodeEqualNone = ParamUtil.getBoolean(actionRequest, "hiddenTreeNodeEqualNone");
		
		PortletPreferences preferences =
		    PortletPreferencesFactoryUtil.getPortletSetup(
		        actionRequest, portletResource);
					
		preferences.setValue("oderByToDo", String.valueOf(oderByToDo));
		preferences.setValue("oderByJustFinish", String.valueOf(oderByJustFinish));
		preferences.setValue("oderFieldToDo", oderFieldToDo);
		preferences.setValue("oderFieldJustFinish", oderFieldJustFinish);
		preferences.setValue("templatesToDisplay", String.valueOf(templatesToDisplay));
		
		preferences.setValue("hiddenTreeNodeEqualNone", String.valueOf(hiddenTreeNodeEqualNone));
		
		preferences.store();

		SessionMessages.add(actionRequest, "potlet-config-saved");

	}

	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.ConfigurationAction#render(javax.portlet.PortletConfig, javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	public String render(
		PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return "/html/portlets/processmgt/processorder/configuration.jsp";
	}

}
