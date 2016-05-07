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

package org.opencps.processmgt.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletRequest;

import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletMode;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;


/**
 * @author trungnt
 *
 */
public class ProcessOrderPortlet extends MVCPortlet{

	
	public void menuAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException, SystemException, WindowStateException, PortletModeException, IOException {
		String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
		
		String active = ParamUtil.getString(actionRequest, WebKeys.MENU_ACTIVE);
		
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		PortletURL renderUrl = PortletURLFactoryUtil.create(actionRequest, themeDisplay.getPortletDisplay().getId(), themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);
		
		renderUrl.setWindowState(LiferayWindowState.NORMAL);
		renderUrl.setPortletMode(LiferayPortletMode.VIEW);
		renderUrl.setParameter("mvcPath", mvcPath);
		
		HttpServletRequest httpRequest = PortalUtil.getHttpServletRequest(actionRequest);
		
		httpRequest.getSession().invalidate();
		if(Validator.isNotNull(active))httpRequest.getSession().setAttribute(WebKeys.MENU_ACTIVE, active);
		
		actionResponse.sendRedirect(renderUrl.toString());
	}
	
	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {
		

		long processOrderId = 0;
//		    ParamUtil.getLong(renderRequest, ProcessOrderDisplayTerms.PROCESSORDERID);

		ProcessOrder processOrder = null;

		try {
			processOrder =
				ProcessOrderLocalServiceUtil.fetchProcessOrder(processOrderId);
		}
		catch (Exception e) {
			_log.error(e);
		}

		renderRequest.setAttribute(
		    WebKeys.PROCESS_ORDER, processOrder);
		super.render(renderRequest, renderResponse);

	}
	
	private Log _log = LogFactoryUtil.getLog(ProcessOrderPortlet.class);
}
