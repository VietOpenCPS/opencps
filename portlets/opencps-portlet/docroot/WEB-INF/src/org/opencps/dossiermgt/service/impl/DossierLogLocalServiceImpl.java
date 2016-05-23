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

import org.opencps.dossiermgt.model.DossierLog;
import org.opencps.dossiermgt.service.base.DossierLogLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the dossier log local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.dossiermgt.service.DossierLogLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.DossierLogLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierLogLocalServiceUtil
 */
public class DossierLogLocalServiceImpl extends DossierLogLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.dossiermgt.service.DossierLogLocalServiceUtil} to
	 * access the dossier log local service.
	 */

	public DossierLog addDossierLog(
	    long userId, long groupId, long companyId, long dossierId,
	    long fileGroupId, int status, String actionInfo, String messageInfo,
	    Date updateDatetime, int level)
	    throws SystemException {

		long dossierLogId = counterLocalService
		    .increment(DossierLog.class
		        .getName());
		DossierLog dossierLog = dossierLogPersistence
		    .create(dossierLogId);

		dossierLog
		    .setGroupId(groupId);
		dossierLog
		    .setCompanyId(companyId);

		Date now = new Date();

		dossierLog
		    .setUserId(userId);
		
		dossierLog
	    	.setCreateDate(now);

		dossierLog
		    .setModifiedDate(now);

		dossierLog
		    .setDossierId(dossierId);
		dossierLog
		    .setFileGroupId(fileGroupId);
		dossierLog
		    .setDossierStatus(status);
		dossierLog
		    .setActionInfo(actionInfo);
		dossierLog
		    .setMessageInfo(messageInfo);
		dossierLog
		    .setUpdateDatetime(updateDatetime);
		dossierLog
		    .setLevel(level);

		return dossierLogPersistence
		    .update(dossierLog);

	}

	public DossierLog addDossierLog(
	    long userId, long dossierId, long fileGroupId, int status,
	    String actionInfo, String messageInfo, Date updateDatetime, int level,
	    ServiceContext serviceContext)
	    throws SystemException {

		long dossierLogId = counterLocalService
		    .increment(DossierLog.class
		        .getName());
		DossierLog dossierLog = dossierLogPersistence
		    .create(dossierLogId);
		Date now = new Date();

		dossierLog
		    .setUserId(userId);
		dossierLog
		    .setGroupId(serviceContext
		        .getScopeGroupId());
		dossierLog
		    .setCompanyId(serviceContext
		        .getCompanyId());
		dossierLog
		    .setCreateDate(now);
		dossierLog
		    .setModifiedDate(now);

		dossierLog
		    .setDossierId(dossierId);
		dossierLog
		    .setFileGroupId(fileGroupId);
		dossierLog
		    .setDossierStatus(status);
		dossierLog
		    .setActionInfo(actionInfo);
		dossierLog
		    .setMessageInfo(messageInfo);
		dossierLog
		    .setUpdateDatetime(updateDatetime);
		dossierLog
		    .setLevel(level);

		return dossierLogPersistence
		    .update(dossierLog);

	}

	public List<DossierLog> getDossierLogByDossierId(long doosierId)
	    throws SystemException {

		return dossierLogPersistence
		    .findByDossierId(doosierId);
	}

	public List<DossierLog> getDossierLogByDossierId(long dossierId, int start, int end, OrderByComparator obc)
		throws SystemException {

		return dossierLogPersistence
			.findByDossierId(dossierId, start, end, obc);
	}
	
	public int countDossierLogByDossierId(long dossierId) throws SystemException {
		return dossierLogPersistence.countByDossierId(dossierId);
	}
}
