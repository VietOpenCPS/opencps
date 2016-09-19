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

import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.util.DLFileEntryUtil;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author khoavd
 */
public class DossierMgtBackOfficePortlet extends MVCPortlet {

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void previewAttachmentFile(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long dossierFileId =
			ParamUtil.getLong(
				actionRequest, DossierFileDisplayTerms.DOSSIER_FILE_ID);
		ThemeDisplay themeDisplay =
			(ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String url =
			DLFileEntryUtil.getDossierFileAttachmentURL(
				dossierFileId, themeDisplay);
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		// url = "http://docs.google.com/gview?url=" + url + "&embedded=true";
		jsonObject.put("url", url);
		try {
			PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
		}
		catch (IOException e) {
			_log.error(e);
		}
	}
	
	
	private Log _log =
			LogFactoryUtil.getLog(DossierMgtBackOfficePortlet.class.getName());
}
