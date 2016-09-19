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

package org.opencps.servicemgt.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.opencps.servicemgt.model.ServiceFileTemplate;
import org.opencps.servicemgt.model.TemplateFile;
import org.opencps.servicemgt.portlet.ServiceMgtPortlet;
import org.opencps.servicemgt.service.TemplateFileLocalServiceUtil;
import org.opencps.servicemgt.service.base.TemplateFileLocalServiceBaseImpl;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.WebKeys;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
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
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.journal.NoSuchFolderException;

/**
 * The implementation of the template file local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.servicemgt.service.TemplateFileLocalService} interface.
 * <p> This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @see org.opencps.servicemgt.service.base.TemplateFileLocalServiceBaseImpl
 * @see org.opencps.servicemgt.service.TemplateFileLocalServiceUtil
 */
public class TemplateFileLocalServiceImpl
    extends TemplateFileLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.servicemgt.service.TemplateFileLocalServiceUtil} to
	 * access the template file local service.
	 */
	public List<TemplateFile> searchTemplateFiles(
	    long groupId, String keywords, int start, int end)
	    throws PortalException, SystemException {

		return templateFileFinder.finderByKeywords(groupId, keywords, start, end);

	}

	public int countTemplateFiles(long groupId, String keywords)
	    throws PortalException, SystemException {

		return templateFileFinder.countByKeywords(groupId, keywords);
	}

	/**
	 * Update TemplateFile
	 * 
	 * @param fileEntryId
	 * @param fileNo
	 * @param fileName
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public TemplateFile addServiceTemplateFile(
	    String fileNo, String fileName, 
	    ActionRequest actionRequest, ActionResponse actionResponse,
	    ServiceContext serviceContext)
	    throws PortalException, SystemException {
		
		
		FileEntry file = null;
		long templatefileId =
					CounterLocalServiceUtil.increment(TemplateFile.class.getName());

					Date now = new Date();

					TemplateFile template = templateFilePersistence.create(templatefileId);
		try {
			file = updateFileEntry(actionRequest, actionResponse);
		}
		catch (Exception e) {}
		
		if(Validator.isNotNull(file)) {
			template.setCompanyId(serviceContext.getCompanyId());
			template.setGroupId(serviceContext.getScopeGroupId());
			template.setModifiedDate(now);
			template.setCreateDate(now);
			template.setUserId(serviceContext.getUserId());
			template.setFileEntryId(file.getFileEntryId());
			template.setFileName(fileName);
			template.setFileNo(fileNo);

			templateFilePersistence.update(template);
		}

		return template;
	}

	/**
	 * Check douplicate FileName
	 * 
	 * @param groupId
	 * @param fileName
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public boolean isDuplicateFileName(long templateFileId,long groupId, String fileName)
	    throws PortalException, SystemException {

		boolean isExistTemplate = false;

		try {
			
			TemplateFile templateFile = templateFilePersistence.fetchByG_F_NAME(
			    groupId, fileName);
			//add new
			if (Validator.isNotNull(templateFile) && templateFileId <= 0) {
				isExistTemplate = true;
			}
			//update
			else if(Validator.isNotNull(templateFile) && templateFileId > 0
							&& templateFile.getTemplatefileId() != templateFileId) {
				isExistTemplate = true;
			}
			
		}
		catch (Exception e) {
			isExistTemplate = false;
		}

		return isExistTemplate;

	}

	/**
	 * Check douplicate FileNo
	 * 
	 * @param groupId
	 * @param fileName
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public boolean isDuplicateFileNo(long templateFileId ,long groupId, String fileNo) {

		boolean isExistTemplate = false;	
		try {
			TemplateFile templateFile = templateFilePersistence.fetchByG_F_NO(
			    groupId, fileNo);
			//add new
			if (Validator.isNotNull(templateFile) && templateFileId <= 0) {
				isExistTemplate = true;
			}
			
			//update
			
			else if(Validator.isNotNull(templateFile) && templateFileId > 0 &&
					templateFile.getTemplatefileId() != templateFileId) {
				isExistTemplate = true;
			}
		}
		catch (Exception e) {
			isExistTemplate = false;
		}

		return isExistTemplate;
	}

	/**
	 * Update TemplateFile
	 * 
	 * @param templatefileId
	 * @param fileEntryId
	 * @param fileNo
	 * @param fileName
	 * @param serviceContext
	 * @param serviceInfoIds
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public TemplateFile updateServiceTemplateFile(
	    long templatefileId, String fileNo, String fileName, ActionRequest actionRequest,
	    ActionResponse actionResponse, ServiceContext serviceContext)
	    throws PortalException, SystemException {
		
		UploadPortletRequest uploadPortletRequest =
					    PortalUtil.getUploadPortletRequest(actionRequest);
		
		long size = uploadPortletRequest.getSize("uploadedFile");
		
		Date now = new Date();

		TemplateFile template =
		    templateFilePersistence.fetchByPrimaryKey(templatefileId);
		
		FileEntry file = null;
		
		if(Validator.isNotNull(template)) {
			if(size > 0) {
				try {
					FileEntry entry = DLAppServiceUtil.getFileEntry(template.getFileEntryId());
					if(Validator.isNotNull(entry)) {
						DLAppServiceUtil.deleteFileEntry(template.getFileEntryId());				
					}
					
					file = updateFileEntry(actionRequest, actionResponse);
					
					if(Validator.isNotNull(file)) {
						template.setModifiedDate(now);
						template.setFileEntryId(file.getFileEntryId());
						template.setFileName(fileName);
						template.setFileNo(fileNo);
						templateFilePersistence.update(template);
					}
					
				}
				catch (Exception e) {
					_log.info(e);
				}
				
			} else {	
				template.setModifiedDate(now);

				template.setFileName(fileName);
				template.setFileNo(fileNo);
				templateFilePersistence.update(template);
			}
		}

		return template;
	}
	
	public void addChooseServceInfo(long templatefileId ,long [] serviceInfoIds) throws PortalException, SystemException {
		serviceFileTemplateLocalService.addFileServices(templatefileId, serviceInfoIds);
	}

	/*
	 * (non-Javadoc)
	 * @see org.opencps.servicemgt.service.TemplateFileLocalService#
	 * deleteServiceTemplateFile(long)
	 */
	public void deleteServiceTemplateFile(long templatefileId)
	    throws PortalException, SystemException {

		TemplateFile template =
		    templateFilePersistence.fetchByPrimaryKey(templatefileId);
		List<ServiceFileTemplate> serviceFileTemplates = new ArrayList<ServiceFileTemplate>();
		serviceFileTemplates = serviceFileTemplatePersistence.findByTemplatefileId(templatefileId);
		if (Validator.isNotNull(template)) {

			long fileEntryId = template.getFileEntryId();

			dlAppLocalService.deleteFileEntry(fileEntryId);
			
			for(ServiceFileTemplate serviceFileTemplate : serviceFileTemplates) {
				serviceFileTemplatePersistence.remove(serviceFileTemplate);
			}
			
			templateFilePersistence.remove(template);
		}

	}

	/**
	 * Get TemplateFile of ServiceInfo
	 * 
	 * @param serviceInfoId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<TemplateFile> getServiceTemplateFiles(long serviceInfoId)
	    throws PortalException, SystemException {

		List<ServiceFileTemplate> ls = new ArrayList<ServiceFileTemplate>();

		List<TemplateFile> templateFiles = new ArrayList<TemplateFile>();

		ls = serviceFileTemplatePersistence.findByServiceinfoId(serviceInfoId);

		for (ServiceFileTemplate sft : ls) {
			TemplateFile tf = null;

			tf =
			    templateFilePersistence.fetchByPrimaryKey(sft.getTemplatefileId());

			if (Validator.isNotNull(tf)) {
				templateFiles.add(tf);
			}
		}

		return templateFiles;

	}

	/**
	 * Count TemplateFile in ServiceInfo
	 * 
	 * @param serviceInfoId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countServiceTemplateFile(long serviceInfoId)
	    throws PortalException, SystemException {

		int count =
		    serviceFileTemplatePersistence.countByServiceinfoId(serviceInfoId);

		return count;

	}
	
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
			
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

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
			
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			
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

	/**
	 * get templateFile by FileNo
	 * 
	 * @param groupId
	 * @param fileName
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public TemplateFile getTemplateFileByNo(long groupId, String fileNo) throws SystemException {

		return templateFilePersistence.fetchByG_F_NO(groupId, fileNo);
		
	}
	
	private String ROOT_FOLDER_NAME = "OPENCPS";

	private String PARENT_FOLDER_NAME = "templates";
	
	private static final Log _log =
				    LogFactoryUtil.getLog(ServiceMgtPortlet.class);
}
