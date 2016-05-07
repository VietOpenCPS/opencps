/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.servicemgt.portlet;

import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.search.DictItemDisplayTerms;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.servicemgt.DuplicateFileNameException;
import org.opencps.servicemgt.DuplicateFileNoException;
import org.opencps.servicemgt.IOFileUploadException;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.model.TemplateFile;
import org.opencps.servicemgt.search.ServiceDisplayTerms;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.servicemgt.service.TemplateFileLocalServiceUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.LiferayFileItemException;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.journal.NoSuchFolderException;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author khoavd
 */
public class ServiceMgtPortlet extends MVCPortlet {

	/**
	 * @param renderRequest
	 * @param renderResponse
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceInfo updateService(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		ServiceDisplayTerms displayTerms =
		    new ServiceDisplayTerms(actionRequest);

		long serviceInfoId = displayTerms.getServiceId();

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (serviceInfoId <= 0) {

				// TODO: Validator in here

				// Add ServiceInfo

				ServiceInfoLocalServiceUtil.addService(
				    displayTerms.getServiceNo(), displayTerms.getServiceName(),
				    displayTerms.getShortName(),
				    displayTerms.getServiceProcess(),
				    displayTerms.getServiceMethod(),
				    displayTerms.getServiceDossier(),
				    displayTerms.getServiceCondition(),
				    displayTerms.getServiceDuration(),
				    displayTerms.getServiceActors(),
				    displayTerms.getServiceResults(),
				    displayTerms.getServiceRecords(),
				    displayTerms.getServiceFee(),
				    displayTerms.getServiceInstructions(),
				    displayTerms.getAdministrationCode(),
				    displayTerms.getAdministrationIndex(),
				    displayTerms.getDomainCode(),
				    displayTerms.getDomainIndex(),
				    displayTerms.getActiveStatus(),
				    displayTerms.getOnlineUrl(),
				    displayTerms.setFileTemplateIds(actionRequest), serviceContext);

				// Redirect page

				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}

			}
			else {
				// TODO: Validator in here
				
				// Update ServiceInfo
				ServiceInfoLocalServiceUtil.updateService(
				    serviceInfoId, displayTerms.getServiceNo(),
				    displayTerms.getServiceName(), displayTerms.getShortName(),
				    displayTerms.getServiceProcess(),
				    displayTerms.getServiceMethod(),
				    displayTerms.getServiceDossier(),
				    displayTerms.getServiceCondition(),
				    displayTerms.getServiceDuration(),
				    displayTerms.getServiceActors(),
				    displayTerms.getServiceResults(),
				    displayTerms.getServiceRecords(),
				    displayTerms.getServiceFee(),
				    displayTerms.getServiceInstructions(),
				    displayTerms.getAdministrationCode(),
				    displayTerms.getAdministrationIndex(),
				    displayTerms.getDomainCode(),
				    displayTerms.getDomainIndex(), displayTerms.getOnlineUrl(),
				    displayTerms.setFileTemplateIds(actionRequest), serviceContext);
				
				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}

		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

		return null;
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	public void updateTempalteFile(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long templateFileId =
		    ParamUtil.getLong(actionRequest, "templateFileId");
		String fileNo = ParamUtil.getString(actionRequest, "fileNo");
		String fileName = ParamUtil.getString(actionRequest, "fileName");

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (templateFileId <= 0) {
				FileEntry file = updateFileEntry(actionRequest, actionResponse);

				if (Validator.isNotNull(file)) {
					if (TemplateFileLocalServiceUtil.isDuplicateFileName(
					    serviceContext.getScopeGroupId(), fileName)) {
						throw new DuplicateFileNameException();

					}

					if (TemplateFileLocalServiceUtil.isDuplicateFileNo(
					    serviceContext.getScopeGroupId(), fileNo)) {
						throw new DuplicateFileNoException();
					}

					TemplateFileLocalServiceUtil.addServiceTemplateFile(
					    file.getFileEntryId(), fileNo, fileName, serviceContext);
					SessionMessages.add(
					    actionRequest,
					    MessageKeys.SERVICE_TEMPLATE_FILE_UPDATE_SUCESS);
				}
				else {
					throw new IOFileUploadException("File upload exception");
				}

			}
			else {
				TemplateFileLocalServiceUtil.updateServiceTemplateFile(
				    templateFileId, fileNo, fileName, serviceContext);
				SessionMessages.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_FILE_UPDATE_SUCESS);
			}

			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL);
			}
		}
		catch (Exception e) {
			if (e instanceof DuplicateFileNameException) {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_FILE_NAME_EXCEPTION);
			}
			else if (e instanceof DuplicateFileNoException) {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_FILE_NO_EXCEPTION);

			}
			else if (e instanceof IOFileUploadException) {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_UPLOAD_EXCEPTION);
			}
			else {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.SERVICE_TEMPLATE_EXCEPTION_OCCURRED);
			}

			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#render(javax.portlet.RenderRequest,
	 * javax.portlet.RenderResponse)
	 */
	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long serviceinfoId = ParamUtil.getLong(renderRequest, "serviceinfoId");
		
		long dictItemId = ParamUtil.getLong(renderRequest, DictItemDisplayTerms.DICTITEM_ID);

		long templatefileId =
		    ParamUtil.getLong(renderRequest, "templateFileId");

		TemplateFile templateFile = null;

		ServiceInfo serviceInfo = null;
		
		DictItem dictItem = null;

		try {
			
			if(dictItemId > 0) {
				dictItem = DictItemLocalServiceUtil.getDictItem(dictItemId);
				renderRequest.setAttribute(WebKeys.DICT_ITEM_ENTRY, dictItem);
			}
			
			templateFile =
			    TemplateFileLocalServiceUtil.fetchTemplateFile(templatefileId);

			serviceInfo =
			    ServiceInfoLocalServiceUtil.fetchServiceInfo(serviceinfoId);
		}
		catch (Exception e) {
			_log.info(e);
		}

		renderRequest.setAttribute(WebKeys.SERVICE_ENTRY, serviceInfo);
		renderRequest.setAttribute(WebKeys.SERVICE_TEMPLATE_ENTRY, templateFile);

		super.render(renderRequest, renderResponse);

	}

	/**
	 * Upload file
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @return
	 * @throws Exception
	 */
	protected FileEntry updateFileEntry(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws Exception {

		UploadPortletRequest uploadPortletRequest =
		    PortalUtil.getUploadPortletRequest(actionRequest);

		ThemeDisplay themeDisplay =
		    (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		String cmd = ParamUtil.getString(uploadPortletRequest, Constants.CMD);

		long repositoryId = themeDisplay.getScopeGroupId();

		long folderId = 0;

		Folder folderFile = initFolderService(actionRequest, themeDisplay);

		if (Validator.isNotNull(folderFile)) {
			folderId = folderFile.getFolderId();
		}

		String sourceFileName =
		    uploadPortletRequest.getFileName("uploadedFile");
		String title = ParamUtil.getString(uploadPortletRequest, "fileName");

		String description =
		    ParamUtil.getString(uploadPortletRequest, "description");
		String changeLog =
		    ParamUtil.getString(uploadPortletRequest, "changeLog");

		if (folderId > 0) {
			Folder folder = DLAppServiceUtil.getFolder(folderId);

			if (folder.getGroupId() != themeDisplay.getScopeGroupId()) {
				throw new NoSuchFolderException("{folderId=" + folderId + "}");
			}
		}

		InputStream inputStream = null;

		try {
			String contentType =
			    uploadPortletRequest.getContentType("uploadedFile");

			long size = uploadPortletRequest.getSize("uploadedFile");

			if ((cmd.equals(Constants.ADD) || cmd.equals(Constants.ADD_DYNAMIC)) &&
			    (size == 0)) {

				contentType = MimeTypesUtil.getContentType(title);
			}

			inputStream = uploadPortletRequest.getFileAsStream("uploadedFile");

			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(
			        DLFileEntry.class.getName(), uploadPortletRequest);

			FileEntry fileEntry = null;

			// Add file entry

			fileEntry =
			    DLAppServiceUtil.addFileEntry(
			        repositoryId, folderId, sourceFileName, contentType,
			        sourceFileName, description, changeLog, inputStream, size,
			        serviceContext);

			AssetPublisherUtil.addAndStoreSelection(
			    actionRequest, DLFileEntry.class.getName(),
			    fileEntry.getFileEntryId(), -1);

			AssetPublisherUtil.addRecentFolderId(
			    actionRequest, DLFileEntry.class.getName(), folderId);

			return fileEntry;
		}
		catch (Exception e) {
			UploadException uploadException =
			    (UploadException) actionRequest.getAttribute(WebKeys.UPLOAD_EXCEPTION);

			if (uploadException != null) {
				if (uploadException.isExceededLiferayFileItemSizeLimit()) {
					throw new LiferayFileItemException();
				}
				else if (uploadException.isExceededSizeLimit()) {
					throw new FileSizeException(uploadException.getCause());
				}
			}

			throw e;
		}
		finally {
			StreamUtil.cleanUp(inputStream);
		}
	}

	/**
	 * Create tree folder. Return folder contains template files
	 * 
	 * @param actionRequest
	 * @param themeDisplay
	 * @return
	 */
	public Folder initFolderService(
	    ActionRequest actionRequest, ThemeDisplay themeDisplay) {

		// Folder contains template service files

		String dateFolderName = DateTimeUtil.getStringDate();

		Folder folder = null;

		try {
			// Check ROOT folder exist

			long rootFolderId = 0;

			long parentFolderId = 0;

			boolean isRootFolderExist =
			    isFolderExist(
			        themeDisplay.getScopeGroupId(), 0, ROOT_FOLDER_NAME);

			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (isRootFolderExist) {
				Folder rootFolder = null;

				rootFolder =
				    DLAppServiceUtil.getFolder(
				        themeDisplay.getScopeGroupId(), 0, ROOT_FOLDER_NAME);

				rootFolderId = rootFolder.getFolderId();

			}
			else {
				Folder rootFolder =
				    DLAppServiceUtil.addFolder(
				        themeDisplay.getScopeGroupId(), 0, ROOT_FOLDER_NAME,
				        "All documents of OpenCPS", serviceContext);

				rootFolderId = rootFolder.getFolderId();
			}

			// Check parent folder exist

			boolean isParentFolderExist =
			    isFolderExist(
			        themeDisplay.getScopeGroupId(), rootFolderId,
			        PARENT_FOLDER_NAME);

			if (isParentFolderExist) {

				Folder parentFolder =
				    DLAppServiceUtil.getFolder(
				        themeDisplay.getScopeGroupId(), rootFolderId,
				        PARENT_FOLDER_NAME);

				parentFolderId = parentFolder.getFolderId();

			}
			else {
				Folder parentFolder =
				    DLAppServiceUtil.addFolder(
				        themeDisplay.getScopeGroupId(), rootFolderId,
				        PARENT_FOLDER_NAME,
				        "All documents of Service Template File",
				        serviceContext);

				parentFolderId = parentFolder.getFolderId();
			}

			// Check DateFolder exist
			boolean isDateFolderExist =
			    isFolderExist(
			        themeDisplay.getScopeGroupId(), parentFolderId,
			        dateFolderName);

			Folder dateFolder = null;

			if (isDateFolderExist) {

				dateFolder =
				    DLAppServiceUtil.getFolder(
				        themeDisplay.getScopeGroupId(), parentFolderId,
				        dateFolderName);

			}
			else {
				dateFolder =
				    DLAppServiceUtil.addFolder(
				        themeDisplay.getScopeGroupId(),
				        parentFolderId,
				        dateFolderName,
				        "All documents of Service Template File upload in a day",
				        serviceContext);
			}

			folder = dateFolder;

		}
		catch (Exception e) {
			_log.error(e);
		}

		return folder;
	}

	/**
	 * Check folder exist
	 * 
	 * @param scopeGroupId
	 * @param parentFolderId
	 * @param folderName
	 * @return
	 */
	public boolean isFolderExist(
	    long scopeGroupId, long parentFolderId, String folderName) {

		boolean folderExist = false;
		try {

			DLAppServiceUtil.getFolder(scopeGroupId, parentFolderId, folderName);
			folderExist = true;
		}
		catch (Exception e) {

		}

		return folderExist;
	}

	private String ROOT_FOLDER_NAME = "OPENCPS";

	private String PARENT_FOLDER_NAME = "templates";

	private static final Log _log =
	    LogFactoryUtil.getLog(ServiceMgtPortlet.class);
}
