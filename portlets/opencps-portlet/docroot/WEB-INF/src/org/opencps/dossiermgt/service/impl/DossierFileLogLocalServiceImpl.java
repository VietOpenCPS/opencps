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

package org.opencps.dossiermgt.service.impl;

import java.util.Date;
import java.util.UUID;

import org.opencps.dossiermgt.model.DossierFileLog;
import org.opencps.dossiermgt.service.base.DossierFileLogLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the dossier file log local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.dossiermgt.service.DossierFileLogLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.DossierFileLogLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierFileLogLocalServiceUtil
 */
public class DossierFileLogLocalServiceImpl
	extends DossierFileLogLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.dossiermgt.service.DossierFileLogLocalServiceUtil} to access the dossier file log local service.
	 */
	
	/**
	 * Add new DossierFileLog 
	 * 
	 * @param userId
	 * @param userName
	 * @param dossierId
	 * @param fileGroupId
	 * @param stepId
	 * @param isUpdate
	 * @param fileName
	 * @param fileVersion
	 * @param fileLink
	 * @param actionCode
	 * @param fileEntryId
	 * @return
	 * @throws PortalException - if a portal exception occurred
	 * @throws SystemException - if a system exception occurred
	 */
	public DossierFileLog addFileLog(
	    long userId, String userName, long dossierId, long fileGroupId,
	    long stepId, boolean isUpdate, String fileName, int fileVersion,
	    String fileLink, int actionCode, long fileEntryId)
	    throws PortalException, SystemException {

		DossierFileLog dossierFileLog = null;

		long dossierFileLogId =
		    counterLocalService.increment(DossierFileLog.class.getName());

		dossierFileLog = dossierFileLogPersistence.create(dossierFileLogId);

		int count =
		    dossierFileLogPersistence.countByD_F_S(
		        dossierId, fileGroupId, stepId);

		dossierFileLog.setCount_(count); 

		dossierFileLog.setUserId(userId);
		dossierFileLog.setUserName(userName);
		dossierFileLog.setDossierId(dossierId);
		dossierFileLog.setStepId(stepId);
		dossierFileLog.setIsUpdate(isUpdate);
		dossierFileLog.setFileName(fileName);
		dossierFileLog.setFileVersion(fileVersion);
		dossierFileLog.setFileLink(fileLink);
		dossierFileLog.setActionCode(actionCode);
		dossierFileLog.setOId(UUID.randomUUID().toString());
		dossierFileLog.setFileEntryId(fileEntryId);
		dossierFileLog.setModifiedDate(new Date());

		dossierFileLogPersistence.update(dossierFileLog);

		return dossierFileLog;
	}
}