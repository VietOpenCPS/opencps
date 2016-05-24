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

import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.NoSuchDossierFileException;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.service.base.DossierFileLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the dossier file local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.dossiermgt.service.DossierFileLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.DossierFileLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierFileLocalServiceUtil
 */
public class DossierFileLocalServiceImpl
    extends DossierFileLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.dossiermgt.service.DossierFileLocalServiceUtil} to
	 * access the dossier file local service.
	 */
	
	

	public DossierFile addDossierFile(
	    long userId, long dossierId, long dossierPartId, String templateFileNo,
	    long groupFileId, long ownerUserId, long ownerOrganizationId,
	    String displayName, String formData, long fileEntryId,
	    int dossierFileMark, int dossierFileType, String dossierFileNo,
	    Date dossierFileDate, int original, int syncStatus,
	    ServiceContext serviceContext)
	    throws SystemException {

		long dossierFileId = counterLocalService
		    .increment(DossierFile.class
		        .getName());
		DossierFile dossierFile = dossierFilePersistence
		    .create(dossierFileId);

		Date now = new Date();

		dossierFile
		    .setUserId(userId);
		dossierFile
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		dossierFile
		    .setCompanyId(serviceContext
		        .getCompanyId());
		dossierFile
		    .setCreateDate(now);
		dossierFile
		    .setModifiedDate(now);
		dossierFile
		    .setDisplayName(displayName);
		dossierFile
		    .setDossierFileDate(dossierFileDate);
		dossierFile
		    .setDossierFileMark(dossierFileMark);
		dossierFile
		    .setDossierFileNo(dossierFileNo);
		dossierFile
		    .setDossierFileType(dossierFileType);
		dossierFile
		    .setDossierId(dossierId);
		dossierFile
		    .setDossierPartId(dossierPartId);
		dossierFile
		    .setFileEntryId(fileEntryId);
		dossierFile
		    .setFormData(formData);
		dossierFile
		    .setGroupFileId(groupFileId);
		dossierFile
		    .setOriginal(original);
		dossierFile
		    .setOwnerOrganizationId(ownerOrganizationId);
		dossierFile
		    .setUuid(PortalUUIDUtil
		        .generate());

		return dossierFilePersistence
		    .update(dossierFile);
	}

	public void deleteDossierFile(long dossierFileId, long fileEntryId)
	    throws PortalException, SystemException {

		if (fileEntryId > 0) {
			dlFileEntryLocalService
			    .deleteDLFileEntry(fileEntryId);
		}

		dossierFilePersistence
		    .remove(dossierFileId);
	}

	/**
	 * @param dossierId
	 * @param dossierPartId
	 * @return
	 * @throws NoSuchDossierFileException
	 * @throws SystemException
	 */
	public DossierFile getDossierFileByD_P(long dossierId, long dossierPartId)
	    throws NoSuchDossierFileException, SystemException {

		return dossierFilePersistence
		    .findByD_P(dossierId, dossierPartId);
	}

	public List<DossierFile> getDossierFileByDossierId(long dossierId)
	    throws SystemException {

		return dossierFilePersistence
		    .findByDossierId(dossierId);
	}

	public List<DossierFile> getDossierFileByD_F(
	    long dossierId, long groupFileId)
	    throws SystemException {

		return dossierFilePersistence
		    .findByD_F(dossierId, groupFileId);
	}
	
	public DossierFile updateDossierFile(
	    long dossierFileId, long ownerUserId, long ownerOrganizationId,
	    long fileEntryId, String displayName)
	    throws NoSuchDossierFileException, SystemException {

		DossierFile dossierFile = dossierFilePersistence
		    .findByPrimaryKey(dossierFileId);

		Date now = new Date();

		dossierFile
		    .setModifiedDate(now);

		dossierFile
		    .setDisplayName(displayName);

		dossierFile
		    .setFileEntryId(fileEntryId);
		dossierFile
		    .setOwnerUserId(ownerUserId);
		dossierFile
		    .setOwnerOrganizationId(ownerOrganizationId);
		return dossierFilePersistence
		    .update(dossierFile);
	}

	public List<DossierFile> searchDossierFile(long groupId, String keyword, long dossierTemplateId, long fileEntryId, boolean onlyViewFileResult, int start, int end, OrderByComparator obc)
				    throws SystemException {
		return dossierFileFinder.searchDossierFile(groupId, keyword, dossierTemplateId, fileEntryId, onlyViewFileResult, start, end, obc);
	}

	public int countDossierFile(long groupId, String keyword, long dossierTemplateId, long fileEntryId, boolean onlyViewFileResult) throws SystemException {
		return dossierFileFinder.countDossierFile(groupId, keyword, dossierTemplateId, fileEntryId, onlyViewFileResult);
	}
	
	public List<DossierFile> getDossierFileByDossierAndDossierPart(long dossierId, long dossierPartId) throws SystemException {
		return dossierFilePersistence.findByD_P_C(dossierId, dossierPartId);
	}
	
	public DossierFile updateDossierFile(
	    long dossierFileId, long userId, long dossierId, long dossierPartId,
	    String templateFileNo, long groupFileId, long ownerUserId,
	    long ownerOrganizationId, String displayName, String formData,
	    long fileEntryId, int dossierFileMark, int dossierFileType,
	    String dossierFileNo, Date dossierFileDate, int original,
	    int syncStatus, ServiceContext serviceContext)
	    throws NoSuchDossierFileException, SystemException {

		DossierFile dossierFile = dossierFilePersistence
		    .findByPrimaryKey(dossierFileId);

		Date now = new Date();

		dossierFile
		    .setModifiedDate(now);
		dossierFile
		    .setUserId(userId);
		dossierFile
		    .setDisplayName(displayName);
		dossierFile
		    .setDossierFileDate(dossierFileDate);
		dossierFile
		    .setDossierFileMark(dossierFileMark);
		dossierFile
		    .setDossierFileNo(dossierFileNo);
		dossierFile
		    .setDossierFileType(dossierFileType);
		dossierFile
		    .setDossierId(dossierId);
		dossierFile
		    .setDossierPartId(dossierPartId);
		dossierFile
		    .setFileEntryId(fileEntryId);
		dossierFile
		    .setFormData(formData);
		dossierFile
		    .setGroupFileId(groupFileId);
		dossierFile
		    .setOriginal(original);
		dossierFile
		    .setOwnerOrganizationId(ownerOrganizationId);
		return dossierFilePersistence
		    .update(dossierFile);
	}
}
