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

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */
public class DossierMornitoringPortlet extends MVCPortlet {
 
	public void searchAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		String receptionNo = ParamUtil.getString(request, "keywords");
		System.out.println("RECEPTION NO: " + receptionNo);
		Dossier ds = null;
		try {
			ds = DossierLocalServiceUtil.getDossierByReceptionNo(receptionNo);
		}
		catch (SystemException ex) {
			
		}
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		String portletName = (String)request.getAttribute(WebKeys.PORTLET_ID);
		PortletURL redirectURL = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(request),
			portletName,
			themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE); 
		addProcessActionSuccessMessage = false;
		
		if (ds != null) {
			redirectURL.setParameter("jspPage", templatePath + "dossiermonitoringresult.jsp");
			redirectURL.setParameter("dossierId", String.valueOf(ds.getDossierId()));
			response.sendRedirect(redirectURL.toString());
		}
		else if (Validator.isNotNull(receptionNo) && !"".equals(receptionNo)) {		
			redirectURL.setParameter("jspPage", templatePath + "dossiermonitoringdossierlist.jsp");
			redirectURL.setParameter("keywords", receptionNo);
			response.sendRedirect(redirectURL.toString());
		}			
	}
	
	public void searchServiceAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
	    PortletPreferences prefs = request.getPreferences();

	    String servicePage = prefs.getValue(
	        "servicepage", "/");	
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);	
		String portletName = "10_WAR_opencpsportlet";
		String keywords = ParamUtil.getString(request, "keywords");
		
		long plid = 0L;
		try {
			plid = LayoutLocalServiceUtil.getFriendlyURLLayout(themeDisplay.getScopeGroupId(), false, servicePage).getPlid();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PortletURL redirectURL = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(request),portletName,plid, PortletRequest.RENDER_PHASE);
		redirectURL.setParameter("keywords", keywords);
		response.sendRedirect(redirectURL.toString());
	}	
}
