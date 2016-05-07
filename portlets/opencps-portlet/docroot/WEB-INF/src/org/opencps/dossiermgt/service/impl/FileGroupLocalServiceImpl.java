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

import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.service.base.FileGroupLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the file group local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.dossiermgt.service.FileGroupLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.FileGroupLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.FileGroupLocalServiceUtil
 */
public class FileGroupLocalServiceImpl extends FileGroupLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.dossiermgt.service.FileGroupLocalServiceUtil} to
	 * access the file group local service.
	 */

	public FileGroup addFileGroup(
	    long userId, long dossierId, long dossierPartId, String displayName,
	    int syncStatus, ServiceContext serviceContext)
	    throws SystemException {

		long fileGroupId = counterLocalService
		    .increment(FileGroup.class
		        .getName());
		FileGroup fileGroup = fileGroupPersistence
		    .create(fileGroupId);
		Date now = new Date();
		fileGroup
		    .setUserId(userId);
		fileGroup
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		fileGroup
		    .setCompanyId(serviceContext
		        .getCompanyId());
		fileGroup
		    .setCreateDate(now);
		fileGroup
		    .setModifiedDate(now);

		fileGroup
		    .setDisplayName(displayName);
		fileGroup
		    .setDossierId(dossierId);
		fileGroup
		    .setDossierPartId(dossierPartId);
		fileGroup
		    .setSyncStatus(syncStatus);
		fileGroup
		    .setUuid(PortalUUIDUtil
		        .generate());

		return fileGroupPersistence
		    .update(fileGroup);
	}

	/**
	 * @param dossierId
	 * @param dossierPartId
	 * @return
	 * @throws SystemException
	 */
	public List<FileGroup> getFileGroupByD_P(long dossierId, long dossierPartId)
	    throws SystemException {

		return fileGroupPersistence
		    .findByD_P(dossierId, dossierPartId);
	}

	public List<FileGroup> getFileGroupByDossierId(long dossierId)
	    throws SystemException {

		return fileGroupPersistence
		    .findByDossierId(dossierId);
	}
}
