
/*******************************************************************************
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package org.opencps.dossiermgt.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.NoSuchDossierStatusException;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierStatus;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.dossiermgt.service.base.DossierLocalServiceBaseImpl;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;

/**
 * The implementation of the dossier local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.dossiermgt.service.DossierLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.DossierLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierLocalServiceUtil
 */
public class DossierLocalServiceImpl extends DossierLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.dossiermgt.service.DossierLocalServiceUtil} to access
	 * the dossier local service.
	 */
	
	/**
	 * @param userId
	 * @param groupId
	 * @param companyId
	 * @param dossierId
	 * @param fileGroupId
	 * @param receptionNo
	 * @param estimateDatetime
	 * @param receiveDatetime
	 * @param finishDatetime
	 * @param dossierStatus
	 * @param actionInfo
	 * @param messageInfo
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void updateDossierStatus(
	    long userId, long groupId, long companyId, long dossierId,
	    long fileGroupId, String receptionNo, Date estimateDatetime,
	    Date receiveDatetime, Date finishDatetime, int dossierStatus,
	    String actionInfo, String messageInfo)
	    throws PortalException, SystemException {

		Dossier dossier = dossierPersistence.fetchByPrimaryKey(dossierId);

		dossier.setReceptionNo(receptionNo);
		dossier.setEstimateDatetime(estimateDatetime);
		dossier.setReceiveDatetime(receiveDatetime);
		dossier.setFinishDatetime(finishDatetime);
		dossier.setDossierStatus(dossierStatus);

		dossierPersistence.update(dossier);

		// add DossierLog

		dossierLogLocalService.addDossierLog(
		    userId, groupId, companyId, dossierId, fileGroupId, 2, actionInfo,
		    messageInfo, new Date(), 1);
	}

	public Dossier addDossier(
	    long userId, long ownerOrganizationId, long dossierTemplateId,
	    String templateFileNo, long serviceConfigId, long serviceInfoId,
	    String serviceDomainIndex, long govAgencyOrganizationId,
	    String govAgencyCode, String govAgencyName, int serviceMode,
	    String serviceAdministrationIndex, String cityCode, String cityName,
	    String districtCode, String districtName, String wardName,
	    String wardCode, String subjectName, String subjectId, String address,
	    String contactName, String contactTelNo, String contactEmail,
	    String note, int dossierSource, int dossierStatus,
	    List<String> normalDossierSchemas,
	    HashMap<String, List<String>> groupPrivateDossier, long parentFolderId,
	    ServiceContext serviceContext)
	    throws SystemException, PortalException {

		long dossierId = counterLocalService
		    .increment(Dossier.class
		        .getName());

		Dossier dossier = dossierPersistence
		    .create(dossierId);

		int count = dossierLocalService
		    .countByGroupId(serviceContext
		        .getScopeGroupId());

		int dossierNo = count + 1;

		Date now = new Date();

		serviceContext
		    .setAddGroupPermissions(true);
		serviceContext
		    .setAddGuestPermissions(true);

		dossier
		    .setUserId(userId);
		dossier
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		dossier
		    .setCompanyId(serviceContext
		        .getCompanyId());
		dossier
		    .setCreateDate(now);
		dossier
		    .setModifiedDate(now);

		dossier
		    .setAddress(address);
		dossier
		    .setCityCode(cityCode);
		dossier
		    .setCityName(cityName);
		dossier
		    .setContactEmail(contactEmail);
		dossier
		    .setContactName(contactName);
		dossier
		    .setContactTelNo(contactTelNo);
		dossier
		    .setCounter(dossierNo);
		dossier
		    .setDistrictCode(districtCode);
		dossier
		    .setDistrictName(districtName);
		dossier
		    .setDossierSource(dossierSource);
		dossier
		    .setDossierStatus(dossierStatus);
		dossier
		    .setDossierTemplateId(dossierTemplateId);
		dossier
		    .setGovAgencyCode(govAgencyCode);
		dossier
		    .setGovAgencyName(govAgencyName);
		dossier
		    .setGovAgencyOrganizationId(govAgencyOrganizationId);
		dossier
		    .setNote(note);
		dossier
		    .setOwnerOrganizationId(ownerOrganizationId);
		dossier
		    .setServiceAdministrationIndex(serviceAdministrationIndex);
		dossier
		    .setServiceConfigId(serviceConfigId);
		dossier
		    .setServiceDomainIndex(serviceDomainIndex);
		dossier
		    .setServiceInfoId(serviceInfoId);
		dossier
		    .setServiceMode(serviceMode);
		dossier
		    .setSubjectId(subjectId);
		dossier
		    .setSubjectName(subjectName);
		dossier
		    .setUuid(PortalUUIDUtil
		        .generate());
		dossier
		    .setWardCode(wardCode);
		dossier
		    .setWardName(wardName);

		dossier = dossierPersistence
		    .update(dossier);

		dossierStatusLocalService
		    .addDossierStatus(
		        userId, dossierId, 0, PortletConstants.DOSSIER_STATUS_NEW,
		        PortletUtil
		            .getActionInfo(
		                PortletConstants.DOSSIER_STATUS_NEW, serviceContext
		                    .getLocale()),
		        PortletUtil
		            .getMessageInfo(
		                PortletConstants.DOSSIER_STATUS_NEW, serviceContext
		                    .getLocale()),
		        now, PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
		        serviceContext);
		dossierLogLocalService
		    .addDossierLog(
		        userId, dossierId, 0, PortletConstants.DOSSIER_STATUS_NEW,
		        PortletUtil
		            .getActionInfo(
		                PortletConstants.DOSSIER_STATUS_NEW, serviceContext
		                    .getLocale()),
		        PortletUtil
		            .getMessageInfo(
		                PortletConstants.DOSSIER_STATUS_NEW, serviceContext
		                    .getLocale()),
		        now, PortletConstants.DOSSIER_LOG_NORMAL, serviceContext);

		DLFolder dossierNoFolder = dlFolderLocalService
		    .addFolder(userId, serviceContext
		        .getScopeGroupId(), serviceContext
		            .getScopeGroupId(),
		        false, parentFolderId, String
		            .valueOf(dossierNo),
		        StringPool.BLANK, false, serviceContext);

		for (Map.Entry<String, List<String>> entry : groupPrivateDossier
		    .entrySet()) {

			String groupName = entry
			    .getKey();
			List<String> dossierSchemas = entry
			    .getValue();
			// Add file group
			FileGroup fileGroup = null;
			if (Validator
			    .isNotNull(groupName) && dossierSchemas != null &&
			    !dossierSchemas
			        .isEmpty()) {

				if (Validator
				    .isNotNull(groupName)) {
					fileGroup = fileGroupLocalService
					    .addFileGroup(
					        userId, dossierId, 0, groupName,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);

					dossierStatusLocalService
					    .addDossierStatus(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, PortletUtil
					            .getActionInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        PortletUtil
					            .getMessageInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        now,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);
					dossierLogLocalService
					    .addDossierLog(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, PortletUtil
					            .getActionInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        PortletUtil
					            .getMessageInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        now, PortletConstants.DOSSIER_LOG_NORMAL,
					        serviceContext);

				}
			}

			if (fileGroup != null) {
				for (String dossierSchema : dossierSchemas) {
					JSONObject jsonObject = null;

					jsonObject = JSONFactoryUtil
					    .createJSONObject(dossierSchema);
					long dossierPartId = jsonObject
					    .getLong(DossierFileDisplayTerms.DOSSIER_PART_ID);
					long fileEntryId = jsonObject
					    .getLong(DossierFileDisplayTerms.FILE_ENTRY_ID);

					int dossierFileOriginal = jsonObject
					    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);
					int dossierFileType = jsonObject
					    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_TYPE);
					String formData = jsonObject
					    .getString(DossierFileDisplayTerms.FORM_DATA);

					String displayName = jsonObject
					    .getString(DossierFileDisplayTerms.DISPLAY_NAME);
					String mimeType = jsonObject
					    .getString(DossierFileDisplayTerms.MIME_TYPE);
					/*
					 * String fileName = jsonObject
					 * .getString(DossierFileDisplayTerms.FILE_NAME);
					 */
					String dossierFileDate = jsonObject
					    .getString(DossierFileDisplayTerms.DOSSIER_FILE_DATE);
					String dossierFileNo = jsonObject
					    .getString(DossierFileDisplayTerms.DOSSIER_FILE_NO);

					FileEntry tempFileEntry = null;

					// tempFileEntry = TempFileUtil.getTempFile(groupId, userId,
					// sourceFileName, tempFolderName);

					FileEntry fileEntry = null;

					if (fileEntryId > 0) {
						tempFileEntry = DLAppServiceUtil
						    .getFileEntry(fileEntryId);

						InputStream inputStream = tempFileEntry
						    .getContentStream();

						long size = tempFileEntry
						    .getSize();

						String sourceFileName = tempFileEntry
						    .getTitle();

						fileEntry = DLAppServiceUtil
						    .addFileEntry(serviceContext
						        .getScopeGroupId(), dossierNoFolder
						            .getFolderId(),
						        sourceFileName, mimeType, displayName,
						        StringPool.BLANK, StringPool.BLANK, inputStream,
						        size, serviceContext);
					}
					if (fileEntry != null) {

						Date fileDate = null;

						if (Validator
						    .isNotNull(dossierFileDate)) {
							fileDate = DateTimeUtil
							    .convertStringToDate(dossierFileDate);
						}

						// Add dossier file
						dossierFileLocalService
						    .addDossierFile(
						        userId, dossierId, dossierPartId,
						        templateFileNo, fileGroup != null ? fileGroup
						            .getFileGroupId() : 0L,
						        userId, ownerOrganizationId, displayName,
						        formData, fileEntry
						            .getFileEntryId(),
						        PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
						        dossierFileType, dossierFileNo, fileDate,
						        dossierFileOriginal,
						        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
						        serviceContext);
					}

				}
			}

		}

		for (int i = 0; i < normalDossierSchemas
		    .size(); i++) {
			String schema = normalDossierSchemas
			    .get(i);

			JSONObject jsonObject = null;

			jsonObject = JSONFactoryUtil
			    .createJSONObject(schema);
			long dossierPartId = jsonObject
			    .getLong(DossierFileDisplayTerms.DOSSIER_PART_ID);
			long fileEntryId = jsonObject
			    .getLong(DossierFileDisplayTerms.FILE_ENTRY_ID);

			int dossierFileOriginal = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);
			int dossierFileType = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_TYPE);

			String formData = jsonObject
			    .getString(DossierFileDisplayTerms.FORM_DATA);
			String displayName = jsonObject
			    .getString(DossierFileDisplayTerms.DISPLAY_NAME);
			String mimeType = jsonObject
			    .getString(DossierFileDisplayTerms.MIME_TYPE);
			/*
			 * String fileName = jsonObject
			 * .getString(DossierFileDisplayTerms.FILE_NAME);
			 */
			String dossierFileDate = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_DATE);
			String dossierFileNo = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_NO);

			FileEntry tempFileEntry = null;

			// tempFileEntry = TempFileUtil.getTempFile(groupId, userId,
			// sourceFileName, tempFolderName);

			FileEntry fileEntry = null;

			if (fileEntryId > 0) {
				tempFileEntry = DLAppServiceUtil
				    .getFileEntry(fileEntryId);

				InputStream inputStream = tempFileEntry
				    .getContentStream();

				long size = tempFileEntry
				    .getSize();

				String sourceFileName = tempFileEntry
				    .getTitle();

				fileEntry = DLAppServiceUtil
				    .addFileEntry(serviceContext
				        .getScopeGroupId(), dossierNoFolder
				            .getFolderId(),
				        sourceFileName, mimeType, displayName, StringPool.BLANK,
				        StringPool.BLANK, inputStream, size, serviceContext);
			}
			if (fileEntry != null) {

				Date fileDate = null;

				if (Validator
				    .isNotNull(dossierFileDate)) {
					fileDate = DateTimeUtil
					    .convertStringToDate(dossierFileDate);
				}

				// Add dossier file
				dossierFileLocalService
				    .addDossierFile(
				        userId, dossierId, dossierPartId, templateFileNo, 0,
				        userId, ownerOrganizationId, displayName, formData,
				        fileEntry
				            .getFileEntryId(),
				        PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
				        dossierFileType, dossierFileNo, fileDate,
				        dossierFileOriginal,
				        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				        serviceContext);
			}

		}

		long classTypeId = 0;

		assetEntryLocalService
		    .updateEntry(userId, serviceContext
		        .getScopeGroupId(), ServiceInfo.class
		            .getName(),
		        dossier
		            .getDossierId(),
		        dossier
		            .getUuid(),
		        classTypeId, serviceContext
		            .getAssetCategoryIds(),
		        serviceContext
		            .getAssetTagNames(),
		        false, now, null, null, ContentTypes.TEXT_HTML, dossier
		            .getSubjectName(),
		        StringPool.BLANK, StringPool.BLANK, null, null, 0, 0, 0, false);

		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
                Dossier.class);

		indexer.reindex(dossier);

		return dossier;
	}

	public void deleteDossierByDossierId(long userId, long dossierId)
	    throws NoSuchDossierException, SystemException, PortalException {

		Date now = new Date();
		Dossier dossier = dossierPersistence
		    .findByPrimaryKey(dossierId);
		List<FileGroup> fileGroups = fileGroupLocalService
		    .getFileGroupByDossierId(dossierId);
		List<DossierFile> dossierFiles = dossierFileLocalService
		    .getDossierFileByDossierId(dossierId);

		if (fileGroups != null) {
			for (FileGroup fileGroup : fileGroups) {
				fileGroup
				    .setRemoved(PortletConstants.DOSSIER_FILE_REMOVED);

				fileGroup
				    .setModifiedDate(now);
				fileGroup
				    .setUserId(userId);
				fileGroupLocalService
				    .updateFileGroup(fileGroup);
			}
		}

		if (dossierFiles != null) {
			for (DossierFile dossierFile : dossierFiles) {
				dossierFile
				    .setRemoved(PortletConstants.DOSSIER_FILE_REMOVED);
				dossierFile
				    .setModifiedDate(now);
				dossierFile
				    .setUserId(userId);
				dossierFileLocalService
				    .updateDossierFile(dossierFile);
			}
		}

		dossier
		    .setModifiedDate(now);
		dossier
		    .setUserId(userId);
		
		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
                Dossier.class);

		indexer.delete(dossier);

		dossierPersistence
		    .update(dossier);
	};

	public Dossier updateDossierStatus(
	    long userId, long dossierId, long govAgencyOrganizationId, int status,
	    int syncStatus, long fileGroupId, int level, Locale locale)
	    throws SystemException, NoSuchDossierStatusException, PortalException {

		Date now = new Date();

		Dossier dossier = dossierPersistence
		    .findByPrimaryKey(dossierId);
		dossier =
		    getDossier(dossier, userId, govAgencyOrganizationId, syncStatus);
		/*DossierStatus dossierStatus = dossierStatusLocalService
		    .getDossierStatus(dossierId);
		dossierStatus = getDossierStatus(
		    dossierStatus, userId, govAgencyOrganizationId, syncStatus);*/

		if (fileGroupId > 0) {
			FileGroup fileGroup = fileGroupLocalService
			    .getFileGroup(fileGroupId);
			List<DossierFile> dossierFiles = dossierFileLocalService
			    .getDossierFileByD_F(dossierId, fileGroup
			        .getFileGroupId());
			if (dossierFiles != null) {
				for (DossierFile dossierFile : dossierFiles) {
					dossierFile
					    .setSyncStatus(syncStatus);
					dossierFile
					    .setModifiedDate(now);
					dossierFile
					    .setUserId(userId);
					dossierFileLocalService
					    .updateDossierFile(dossierFile);
				}
			}
			fileGroup
			    .setSyncStatus(syncStatus);
			fileGroup
			    .setModifiedDate(now);
			fileGroup
			    .setUserId(userId);
			fileGroupLocalService
			    .updateFileGroup(fileGroup);
		}

		List<DossierFile> dossierFiles = dossierFileLocalService
		    .getDossierFileByD_F(dossierId, 0);
		if (dossierFiles != null) {
			for (DossierFile dossierFile : dossierFiles) {
				dossierFile
				    .setSyncStatus(syncStatus);
				dossierFile
				    .setModifiedDate(now);
				dossierFile
				    .setUserId(userId);
				dossierFileLocalService
				    .updateDossierFile(dossierFile);
			}
		}

		dossierLogLocalService
		    .addDossierLog(userId, dossier
		        .getGroupId(), dossier
		            .getCompanyId(),
		        dossierId, fileGroupId, status, PortletUtil
		            .getAccountStatus(status, locale),
		        PortletUtil
		            .getMessageInfo(status, locale),
		        now, level);

		/*dossierStatusLocalService
		    .updateDossierStatus(dossierStatus);*/

		dossierPersistence
		    .update(dossier);
		return dossier;
	}

	protected Dossier getDossier(
	    Dossier dossier, long userId, long govAgencyOrganizationId,
	    int status) {

		dossier
		    .setUserId(userId);
		dossier
		    .setModifiedDate(new Date());
		if (govAgencyOrganizationId >= 0) {
			dossier
			    .setGovAgencyOrganizationId(govAgencyOrganizationId);
		}

		dossier
		    .setDossierStatus(status);
		return dossier;
	}

	protected DossierStatus getDossierStatus(
	    DossierStatus dossierStatus, long userId, long govAgencyOrganizationId,
	    int syncStatus) {

		dossierStatus
		    .setUserId(userId);
		dossierStatus
		    .setModifiedDate(new Date());
		dossierStatus
		    .setSyncStatus(syncStatus);
		return dossierStatus;
	}

	public Dossier updateDossier(
	    long dossierId, long userId, long ownerOrganizationId,
	    long dossierTemplateId, String templateFileNo, long serviceConfigId,
	    long serviceInfoId, String serviceDomainIndex,
	    long govAgencyOrganizationId, String govAgencyCode,
	    String govAgencyName, int serviceMode,
	    String serviceAdministrationIndex, String cityCode, String cityName,
	    String districtCode, String districtName, String wardName,
	    String wardCode, String subjectName, String subjectId, String address,
	    String contactName, String contactTelNo, String contactEmail,
	    String note, List<String> normalDossierSchemas,
	    HashMap<String, List<String>> groupPrivateDossier, long dossierFolderId,
	    ServiceContext serviceContext)
	    throws SystemException, PortalException {

		Dossier dossier = dossierPersistence
		    .findByPrimaryKey(dossierId);

		Date now = new Date();

		serviceContext
		    .setAddGroupPermissions(true);
		serviceContext
		    .setAddGuestPermissions(true);

		dossier
		    .setUserId(userId);
		dossier
		    .setModifiedDate(now);

		dossier
		    .setAddress(address);
		dossier
		    .setCityCode(cityCode);
		dossier
		    .setCityName(cityName);
		dossier
		    .setContactEmail(contactEmail);
		dossier
		    .setContactName(contactName);
		dossier
		    .setContactTelNo(contactTelNo);
		dossier
		    .setDistrictCode(districtCode);
		dossier
		    .setDistrictName(districtName);
		dossier
		    .setDossierTemplateId(dossierTemplateId);
		dossier
		    .setGovAgencyCode(govAgencyCode);
		dossier
		    .setGovAgencyName(govAgencyName);
		dossier
		    .setGovAgencyOrganizationId(govAgencyOrganizationId);
		dossier
		    .setNote(note);
		dossier
		    .setOwnerOrganizationId(ownerOrganizationId);
		dossier
		    .setServiceAdministrationIndex(serviceAdministrationIndex);
		dossier
		    .setServiceConfigId(serviceConfigId);
		dossier
		    .setServiceDomainIndex(serviceDomainIndex);
		dossier
		    .setServiceInfoId(serviceInfoId);
		dossier
		    .setServiceMode(serviceMode);
		dossier
		    .setSubjectId(subjectId);
		dossier
		    .setSubjectName(subjectName);
		dossier
		    .setUuid(PortalUUIDUtil
		        .generate());
		dossier
		    .setWardCode(wardCode);
		dossier
		    .setWardName(wardName);

		for (Map.Entry<String, List<String>> entry : groupPrivateDossier
		    .entrySet()) {

			String groupName = entry
			    .getKey();
			List<String> dossierSchemas = entry
			    .getValue();
			// Add file group
			FileGroup fileGroup = null;
			if (Validator
			    .isNotNull(groupName) && dossierSchemas != null &&
			    !dossierSchemas
			        .isEmpty()) {

				if (Validator
				    .isNotNull(groupName)) {
					fileGroup = fileGroupLocalService
					    .addFileGroup(
					        userId, dossierId, 0, groupName,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);

					dossierStatusLocalService
					    .addDossierStatus(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, PortletUtil
					            .getActionInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        PortletUtil
					            .getMessageInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        now,
					        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					        serviceContext);
					dossierLogLocalService
					    .addDossierLog(userId, dossierId, fileGroup
					        .getFileGroupId(),
					        PortletConstants.DOSSIER_STATUS_NEW, PortletUtil
					            .getActionInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        PortletUtil
					            .getMessageInfo(
					                PortletConstants.DOSSIER_STATUS_NEW,
					                serviceContext
					                    .getLocale()),
					        now, PortletConstants.DOSSIER_LOG_NORMAL,
					        serviceContext);

				}
			}

			if (fileGroup != null) {
				for (String dossierSchema : dossierSchemas) {
					JSONObject jsonObject = null;

					jsonObject = JSONFactoryUtil
					    .createJSONObject(dossierSchema);
					long dossierPartId = jsonObject
					    .getLong(DossierFileDisplayTerms.DOSSIER_PART_ID);
					long fileEntryId = jsonObject
					    .getLong(DossierFileDisplayTerms.FILE_ENTRY_ID);

					int dossierFileOriginal = jsonObject
					    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);
					int dossierFileType = jsonObject
					    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_TYPE);
					String formData = jsonObject
					    .getString(DossierFileDisplayTerms.FORM_DATA);

					String displayName = jsonObject
					    .getString(DossierFileDisplayTerms.DISPLAY_NAME);
					String mimeType = jsonObject
					    .getString(DossierFileDisplayTerms.MIME_TYPE);
					/*
					 * String fileName = jsonObject
					 * .getString(DossierFileDisplayTerms.FILE_NAME);
					 */
					String dossierFileDate = jsonObject
					    .getString(DossierFileDisplayTerms.DOSSIER_FILE_DATE);
					String dossierFileNo = jsonObject
					    .getString(DossierFileDisplayTerms.DOSSIER_FILE_NO);

					FileEntry tempFileEntry = null;

					// tempFileEntry = TempFileUtil.getTempFile(groupId, userId,
					// sourceFileName, tempFolderName);

					FileEntry fileEntry = null;

					if (fileEntryId > 0) {
						tempFileEntry = DLAppServiceUtil
						    .getFileEntry(fileEntryId);

						InputStream inputStream = tempFileEntry
						    .getContentStream();

						long size = tempFileEntry
						    .getSize();

						String sourceFileName = tempFileEntry
						    .getTitle();

						fileEntry = DLAppServiceUtil
						    .addFileEntry(serviceContext
						        .getScopeGroupId(), dossierFolderId,
						        sourceFileName, mimeType, displayName,
						        StringPool.BLANK, StringPool.BLANK, inputStream,
						        size, serviceContext);
					}
					if (fileEntry != null) {

						Date fileDate = null;

						if (Validator
						    .isNotNull(dossierFileDate)) {
							fileDate = DateTimeUtil
							    .convertStringToDate(dossierFileDate);
						}

						// Add dossier file
						dossierFileLocalService
						    .addDossierFile(
						        userId, dossierId, dossierPartId,
						        templateFileNo, fileGroup != null ? fileGroup
						            .getFileGroupId() : 0L,
						        userId, ownerOrganizationId, displayName,
						        formData, fileEntry
						            .getFileEntryId(),
						        PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
						        dossierFileType, dossierFileNo, fileDate,
						        dossierFileOriginal,
						        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
						        serviceContext);
					}

				}
			}

		}

		for (int i = 0; i < normalDossierSchemas
		    .size(); i++) {
			String schema = normalDossierSchemas
			    .get(i);

			JSONObject jsonObject = null;

			jsonObject = JSONFactoryUtil
			    .createJSONObject(schema);
			long dossierPartId = jsonObject
			    .getLong(DossierFileDisplayTerms.DOSSIER_PART_ID);
			long fileEntryId = jsonObject
			    .getLong(DossierFileDisplayTerms.FILE_ENTRY_ID);

			int dossierFileOriginal = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);
			int dossierFileType = jsonObject
			    .getInt(DossierFileDisplayTerms.DOSSIER_FILE_TYPE);

			String formData = jsonObject
			    .getString(DossierFileDisplayTerms.FORM_DATA);
			String displayName = jsonObject
			    .getString(DossierFileDisplayTerms.DISPLAY_NAME);
			String mimeType = jsonObject
			    .getString(DossierFileDisplayTerms.MIME_TYPE);
			/*
			 * String fileName = jsonObject
			 * .getString(DossierFileDisplayTerms.FILE_NAME);
			 */
			String dossierFileDate = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_DATE);
			String dossierFileNo = jsonObject
			    .getString(DossierFileDisplayTerms.DOSSIER_FILE_NO);

			FileEntry tempFileEntry = null;

			// tempFileEntry = TempFileUtil.getTempFile(groupId, userId,
			// sourceFileName, tempFolderName);

			FileEntry fileEntry = null;

			if (fileEntryId > 0) {
				tempFileEntry = DLAppServiceUtil
				    .getFileEntry(fileEntryId);

				InputStream inputStream = tempFileEntry
				    .getContentStream();

				long size = tempFileEntry
				    .getSize();

				String sourceFileName = tempFileEntry
				    .getTitle();

				fileEntry = DLAppServiceUtil
				    .addFileEntry(serviceContext
				        .getScopeGroupId(), dossierFolderId, sourceFileName,
				        mimeType, displayName, StringPool.BLANK,
				        StringPool.BLANK, inputStream, size, serviceContext);
			}
			if (fileEntry != null) {

				Date fileDate = null;

				if (Validator
				    .isNotNull(dossierFileDate)) {
					fileDate = DateTimeUtil
					    .convertStringToDate(dossierFileDate);
				}

				// Add dossier file
				dossierFileLocalService
				    .addDossierFile(
				        userId, dossierId, dossierPartId, templateFileNo, 0,
				        userId, ownerOrganizationId, displayName, formData,
				        fileEntry
				            .getFileEntryId(),
				        PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
				        dossierFileType, dossierFileNo, fileDate,
				        dossierFileOriginal,
				        PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				        serviceContext);
			}

		}

		dossier = dossierPersistence
			    .update(dossier); 
		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
                Dossier.class);

		indexer.reindex(dossier);
		return dossier;
	}
	
	public boolean updateDossierStatus(
	    long dossierId, long fileGroupId, int dossierStatus, String receptionNo,
	    Date estimateDatetime, Date receiveDatetime, Date finishDatetime,
	    String actor, String requestCommand, String actionInfo,
	    String messageInfo) {

		boolean result = false;
		try {

			Dossier dossier = dossierPersistence
			    .findByPrimaryKey(dossierId);
			dossier
			    .setReceptionNo(receptionNo);
			dossier
			    .setEstimateDatetime(estimateDatetime);
			dossier
			    .setReceiveDatetime(receiveDatetime);
			dossier
			    .setFinishDatetime(finishDatetime);
			
			dossier.setDossierStatus(dossierStatus);

			int level = 0;
			if (dossier
			    .getDossierStatus() == PortletConstants.DOSSIER_STATUS_ERROR) {
				level = 2;
			}
			else if (dossier
			    .getDossierStatus() == PortletConstants.DOSSIER_STATUS_WAITING ||
			    dossier
			        .getDossierStatus() == PortletConstants.DOSSIER_STATUS_PAYING) {
				level = 1;
			}
			dossierLogLocalService
			    .addDossierLog(dossier
			        .getUserId(), dossier
			            .getGroupId(),
			        dossier
			            .getCompanyId(),
			        dossierId, fileGroupId, dossierStatus,
			        actor, requestCommand, actionInfo, messageInfo,
			        level);

			dossierPersistence
			    .update(dossier);

			result = true;
		}
		catch (Exception e) {
			result = false;
		}

		return result;
	}

	public int countByGroupId(long groupId)
	    throws SystemException {

		return dossierPersistence
		    .countByGroupId(groupId);
	}

	public int countDossier(long groupId, String keyword, int dossierStatus) {

		return dossierFinder
		    .countDossier(groupId, keyword, dossierStatus);
	}

	public List<Dossier> getDossierByGroupId(long groupId)
	    throws SystemException {

		return dossierPersistence
		    .filterFindByGroupId(groupId);
	}

	public List<Dossier> getDossier(
	    long groupId, String keyword, int dossierStatus, int start, int end,
	    OrderByComparator obc) {

		return dossierFinder
		    .searchDossier(groupId, keyword, dossierStatus, start, end, obc);
	}

	public int countDossierByStatus(long groupId, int dossierStatus) throws SystemException {
		return dossierPersistence.countByG_DS(groupId, dossierStatus);
	}
	
	public List<Dossier> getDossierByStatus(
	    long groupId, int dossierStatus, int start, int end,
	    OrderByComparator obc) throws SystemException {

		return dossierPersistence
		    .findByG_DS(groupId, dossierStatus, start, end, obc);
	}
	
	public int countDossierByKeywordDomainAndStatus(long groupId, String keyword, String domainCode, int dossierStatus) {
		return dossierFinder.countDossierByKeywordDomainAndStatus(groupId, keyword, domainCode, dossierStatus);
	}
	
	public List<Dossier> searchDossierByKeywordDomainAndStatus(long groupId, String keyword, String domainCode, int dossierStatus, int start, int end, OrderByComparator obc) {
		return dossierFinder.searchDossierByKeywordDomainAndStatus(groupId, keyword, domainCode, dossierStatus, start, end, obc);
	}
	
	public Dossier getDossierByReceptionNo(String receptionNo) throws SystemException {
		return dossierPersistence.fetchByReceptionNo(receptionNo);
	}
	
	
	/**
	 * @param userId
	 * @param dossierId
	 * @param syncStatus
	 * @throws SystemException
	 * @throws NoSuchDossierStatusException
	 * @throws PortalException
	 */
	public void updateDossierStatus(long userId, long dossierId, int syncStatus)
	    throws SystemException, NoSuchDossierStatusException, PortalException {

		Date now = new Date();

		List<DossierFile> dossierFiles =
		    dossierFileLocalService.getDossierFileByD_F(dossierId, 0);
		
		
		
		if (dossierFiles != null) {
			for (DossierFile dossierFile : dossierFiles) {
				dossierFile.setSyncStatus(syncStatus);
				dossierFile.setModifiedDate(now);
				if (userId != 0) {
					dossierFile.setUserId(userId);
				}
				dossierFileLocalService.updateDossierFile(dossierFile);
			}
		}

	}
	
	/* (non-Javadoc)
	 * @see org.opencps.dossiermgt.service.DossierLocalService#updateDossierStatus(long, long, int)
	 */
	public void updateDossierStatus(
	    long userId, long dossierId, int syncStatus,
	    List<WorkflowOutput> worklows)
	    throws SystemException, NoSuchDossierStatusException, PortalException {

		Date now = new Date();

		List<DossierFile> dossierFiles =
		    dossierFileLocalService.getDossierFileByD_F(dossierId, 0);
		
		for (WorkflowOutput output : worklows) {
			
			DossierFile dossierFile = dossierFileLocalService.getDossierFileByD_P(dossierId, output.getDossierPartId());
			
			dossierFile.setSyncStatus(syncStatus);
			dossierFile.setModifiedDate(now);
			
			if (userId != 0) {
				dossierFile.setUserId(userId);
			}
			
			dossierFileLocalService.updateDossierFile(dossierFile);

		}
		

	}

}
