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
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */
public class DossierMgtFrontOfficePortlet extends MVCPortlet {

	public void addTempFile(
	    ActionRequest actionRequest, ActionResponse actionResponse) {

		UploadPortletRequest uploadPortletRequest = PortalUtil
		    .getUploadPortletRequest(actionRequest);
		
		String jsonData = ParamUtil
					    .getString(uploadPortletRequest, "jsonData");

		int index = ParamUtil.getInteger(uploadPortletRequest, "index");
		
		long folderId = ParamUtil
		    .getLong(uploadPortletRequest, DossierFileDisplayTerms.FOLDE_ID);

		long dossierPartId = ParamUtil
		    .getLong(
		        uploadPortletRequest, DossierFileDisplayTerms.DOSSIER_PART_ID);

		String tempFolderName = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.TEMP_FOLDER_NAME);

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
		    .getAttribute(WebKeys.THEME_DISPLAY);

		String redirectURL = ParamUtil
		    .getString(uploadPortletRequest, "redirectURL");

		String displayName = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.DISPAY_NAME);

		String dossierFileNo = ParamUtil
		    .getString(
		        uploadPortletRequest, DossierFileDisplayTerms.DOSSIER_FILE_NO);

		String dossierFileDate = ParamUtil
		    .getString(
		        uploadPortletRequest,
		        DossierFileDisplayTerms.DOSSIER_FILE_DATE);

		String sourceFileName = uploadPortletRequest
		    .getFileName(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		sourceFileName = sourceFileName
		    .concat(PortletConstants.TEMP_RANDOM_SUFFIX).concat(StringUtil
		        .randomString());

		InputStream inputStream = null;

		JSONArray jsonArray = null;
		
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		try {
			//Cho phep upload nhieu file
			//jsonArray = JSONFactoryUtil.createJSONArray(jsonData);
			
			//Cho phep upload 1 file, file sau de len file truoc
			
			//remove file cu jsonData
			jsonArray = JSONFactoryUtil.createJSONArray();
			
			inputStream = uploadPortletRequest
			    .getFileAsStream(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			String contentType = uploadPortletRequest
			    .getContentType(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			FileEntry fileEntry = DLAppServiceUtil
			    .addTempFileEntry(themeDisplay
			        .getScopeGroupId(), folderId, sourceFileName,
			        tempFolderName, inputStream, contentType);

			jsonObject
			    .put(DossierFileDisplayTerms.DOSSIER_FILE_NO, dossierFileNo);

			jsonObject
			    .put(DossierFileDisplayTerms.DISPAY_NAME, displayName);

			jsonObject
			    .put(
			        DossierFileDisplayTerms.DOSSIER_FILE_DATE, dossierFileDate);

			jsonObject
			    .put(DossierFileDisplayTerms.FILE_TITLE, fileEntry
			        .getTitle());
			jsonObject
			    .put(DossierFileDisplayTerms.FILE_ENTRY_ID, fileEntry
			        .getFileEntryId());
			jsonObject
			    .put(DossierFileDisplayTerms.FOLDE_ID, fileEntry
			        .getFolderId());

			jsonObject
			    .put(DossierFileDisplayTerms.DOSSIER_PART_ID, dossierPartId);
			
			jsonObject
		    	.put("index", index);
			
			jsonArray.put(jsonObject);

		}
		catch (Exception e) {
			_log.error(e);
			SessionErrors
			    .add(actionRequest, "upload-error");
		}
		finally {
			StreamUtil
			    .cleanUp(inputStream);
			HttpServletRequest request = PortalUtil
			    .getHttpServletRequest(actionRequest);
			request
			    .setAttribute(
			        WebKeys.RESPONSE_UPLOAD_TEMP_DOSSIER_FILE, jsonArray);
			
			if (Validator.isNotNull(redirectURL)) {
				actionResponse
				    .setRenderParameter(
				        "jspPage",
				        "/html/portlets/dossiermgt/frontoffice/upload_dossier_file.jsp");
			}
		}
	}

	public void deleteTempFile(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
		    .getAttribute(WebKeys.THEME_DISPLAY);

		String tempFolderName = ParamUtil
		    .getString(actionRequest, "tempFolderName");

		long folderId = ParamUtil
		    .getLong(actionRequest, "folderId");
		String fileName = ParamUtil
		    .getString(actionRequest, "fileName");

		JSONObject jsonObject = JSONFactoryUtil
		    .createJSONObject();

		try {
			DLAppServiceUtil
			    .deleteTempFileEntry(themeDisplay
			        .getScopeGroupId(), folderId, fileName, tempFolderName);

			jsonObject
			    .put("deleted", Boolean.TRUE);
		}
		catch (Exception e) {
			String errorMessage = themeDisplay
			    .translate(
			        "an-unexpected-error-occurred-while-deleting-the-file");

			jsonObject
			    .put("deleted", Boolean.FALSE);
			jsonObject
			    .put("errorMessage", errorMessage);
		}

		writeJSON(actionRequest, actionResponse, jsonObject);
	}

	protected void writeJSON(
	    UploadPortletRequest uploadPortletRequest,
	    ActionResponse actionResponse, Object json)
	    throws IOException {

		HttpServletResponse response = PortalUtil
		    .getHttpServletResponse(actionResponse);

		response
		    .setContentType(ContentTypes.APPLICATION_JSON);

		ServletResponseUtil
		    .write(response, json
		        .toString());
		response
		    .flushBuffer();

	}

	private Log _log = LogFactoryUtil.getLog(DossierMgtFrontOfficePortlet.class.getName());
}
