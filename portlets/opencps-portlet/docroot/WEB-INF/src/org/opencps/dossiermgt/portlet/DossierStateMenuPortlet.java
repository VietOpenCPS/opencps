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


package org.opencps.dossiermgt.portlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletResponse;

import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.util.ProcessOrderUtils;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */

public class DossierStateMenuPortlet extends MVCPortlet {
 
	public void menuCounterAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortalException, SystemException {
		Map<String, String> par = new HashMap<String, String>();
 		
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long groupId = themeDisplay.getScopeGroupId();

        // now read your parameters, e.g. like this:
        // long someParameter = ParamUtil.getLong(request, "someParameter");
        
		long counterVal = 0;
		
		for (String item : PortletUtil.getDossierStatus()){
		
			counterVal = DossierLocalServiceUtil.countDossier(groupId, 
				StringPool.BLANK, item);
			
			par.put("badge_" + item, String.valueOf(counterVal));
		
		}
		
		ajaxReturn(actionRequest ,actionResponse, par);
	}
	
	private void ajaxReturn(ActionRequest actionRequest, ActionResponse actionResponse,Map<String, String> par) {
		try {
			
			HttpServletResponse httpResponse = PortalUtil.getHttpServletResponse(actionResponse);
			
			httpResponse.setContentType("text");
			
			JSONObject payloadJSON = JSONFactoryUtil.createJSONObject();
			
			for (Map.Entry<String,String> entry : par.entrySet()){
			
				payloadJSON.put(entry.getKey(), HtmlUtil.escape(entry.getValue()));
			
			}
			
			httpResponse.getWriter().print(payloadJSON.toString());
			httpResponse.flushBuffer();
			
			SessionMessages.add(actionRequest,PortalUtil.getPortletId(actionRequest)
							+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionMessages.add(actionRequest,PortalUtil.getPortletId(actionRequest)
							+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
}
