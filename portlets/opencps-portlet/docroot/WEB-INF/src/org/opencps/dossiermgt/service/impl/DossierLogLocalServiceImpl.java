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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.backend.util.BackendUtils;
import org.opencps.dossiermgt.NoSuchDossierLogException;
import org.opencps.dossiermgt.comparator.DossierLogUpdateDatetimeComparator;
import org.opencps.dossiermgt.model.DossierLog;
import org.opencps.dossiermgt.service.base.DossierLogLocalServiceBaseImpl;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.impl.ProcessStepImpl;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
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

	/**
	 * @param userId
	 * @param groupId
	 * @param companyId
	 * @param dossierId
	 * @param fileGroupId
	 * @param status
	 * @param actionInfo
	 * @param messageInfo
	 * @param updateDatetime
	 * @param level
	 * @param syncStatus
	 * @param actor
	 * @param actorId
	 * @param actorName
	 * @param className
	 * @param commandRequest
	 * @return
	 * @throws SystemException
	 */
	public DossierLog addCommandRequest(
		long userId, long groupId, long companyId, long dossierId,
		long fileGroupId, String status, String actionInfo, String messageInfo,
		Date updateDatetime, int level, int syncStatus, int actor,
		long actorId, String actorName, String className, String commandRequest)
		throws SystemException {

		long dossierLogId =
			counterLocalService.increment(DossierLog.class.getName());
		DossierLog dossierLog = dossierLogPersistence.create(dossierLogId);
		
		ProcessStep processStep = BackendUtils.getProcessStepByDossierId(dossierId);

		dossierLog.setGroupId(groupId);
		dossierLog.setCompanyId(companyId);

		Date now = new Date();

		dossierLog.setUserId(userId);

		dossierLog.setCreateDate(now);

		dossierLog.setModifiedDate(now);
		dossierLog.setOId(PortalUUIDUtil.generate());
		dossierLog.setDossierId(dossierId);
		dossierLog.setFileGroupId(fileGroupId);
		dossierLog.setDossierStatus(status);
		dossierLog.setActionInfo(actionInfo);
		dossierLog.setMessageInfo(messageInfo);
		dossierLog.setUpdateDatetime(updateDatetime);
		dossierLog.setLevel(level);
		dossierLog.setActor(actor);
		dossierLog.setActorId(actorId);
		dossierLog.setActorName(actorName);
		dossierLog.setClassName(className);
		dossierLog.setSyncStatus(syncStatus);
		dossierLog.setRequestCommand(commandRequest);
		dossierLog.setStepId(processStep.getProcessStepId());
		dossierLog.setStepName(processStep.getStepName());
		
		return dossierLogPersistence.update(dossierLog);

	}

	/**
	 * @param userId
	 * @param groupId
	 * @param companyId
	 * @param dossierId
	 * @param fileGroupId
	 * @param dossierStatus
	 * @param actor
	 * @param requestCommand
	 * @param actionInfo
	 * @param messageInfo
	 * @param level
	 * @return
	 * @throws SystemException
	 */
	public DossierLog addDossierLog(
		long userId, long groupId, long companyId, long dossierId,
		long fileGroupId, String dossierStatus, int actor, long actorId,
		String actorName, String requestCommand, String actionInfo,
		String messageInfo, int level)
		throws SystemException {

		long dossierLogId =
			counterLocalService.increment(DossierLog.class.getName());
		DossierLog dossierLog = dossierLogPersistence.create(dossierLogId);
		Date now = new Date();

		dossierLog.setUserId(userId);
		dossierLog.setGroupId(groupId);
		dossierLog.setCompanyId(companyId);
		dossierLog.setCreateDate(now);
		dossierLog.setModifiedDate(now);
		dossierLog.setOId(PortalUUIDUtil.generate());
		dossierLog.setDossierId(dossierId);
		dossierLog.setFileGroupId(fileGroupId);
		dossierLog.setDossierStatus(dossierStatus);
		dossierLog.setActionInfo(actionInfo);
		dossierLog.setMessageInfo(messageInfo);
		dossierLog.setUpdateDatetime(now);
		dossierLog.setActor(actor);
		dossierLog.setActorId(actorId);
		dossierLog.setActorName(actorName);

		dossierLog.setLevel(level);

		dossierLog.setActionInfo(actionInfo);

		dossierLog.setRequestCommand(requestCommand);

		return dossierLogPersistence.update(dossierLog);

	}

	/**
	 * @param userId
	 * @param groupId
	 * @param companyId
	 * @param dossierId
	 * @param fileGroupId
	 * @param status
	 * @param actionInfo
	 * @param messageInfo
	 * @param updateDatetime
	 * @param level
	 * @return
	 * @throws SystemException
	 */
	public DossierLog addDossierLog(
		long userId, long groupId, long companyId, long dossierId,
		long fileGroupId, String status, String actionInfo, String messageInfo,
		Date updateDatetime, int level)
		throws SystemException {

		// TODO check lai, neu khong can bo di

		long dossierLogId =
			counterLocalService.increment(DossierLog.class.getName());
		DossierLog dossierLog = dossierLogPersistence.create(dossierLogId);

		dossierLog.setGroupId(groupId);
		dossierLog.setCompanyId(companyId);

		Date now = new Date();

		dossierLog.setUserId(userId);
		dossierLog.setCreateDate(now);
		dossierLog.setModifiedDate(now);
		dossierLog.setOId(PortalUUIDUtil.generate());
		dossierLog.setDossierId(dossierId);
		dossierLog.setFileGroupId(fileGroupId);
		dossierLog.setDossierStatus(status);
		dossierLog.setActionInfo(actionInfo);
		dossierLog.setMessageInfo(messageInfo);
		dossierLog.setUpdateDatetime(updateDatetime);
		dossierLog.setLevel(level);

		return dossierLogPersistence.update(dossierLog);

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.opencps.dossiermgt.service.DossierLogLocalService#addDossierLog(long,
	 * long, long, long, long, java.lang.String, java.lang.String,
	 * java.lang.String, java.util.Date, int, java.lang.String)
	 */
	public DossierLog addDossierLog(
		long userId, long groupId, long companyId, long dossierId,
		long fileGroupId, String status, String actionInfo, String messageInfo,
		Date updateDatetime, int level, int syncStatus, int actor,
		long actorId, String actorName, String className)
		throws SystemException {

		long dossierLogId =
			counterLocalService.increment(DossierLog.class.getName());
		DossierLog dossierLog = dossierLogPersistence.create(dossierLogId);
		ProcessStep processStep = BackendUtils.getProcessStepByDossierId(dossierId);

		dossierLog.setGroupId(groupId);
		dossierLog.setCompanyId(companyId);

		Date now = new Date();

		dossierLog.setUserId(userId);

		dossierLog.setCreateDate(now);

		dossierLog.setModifiedDate(now);
		dossierLog.setOId(PortalUUIDUtil.generate());
		dossierLog.setDossierId(dossierId);
		dossierLog.setFileGroupId(fileGroupId);
		if (Validator.isNotNull(status)) {
			dossierLog.setDossierStatus(status);
		}
		dossierLog.setActionInfo(actionInfo);
		dossierLog.setMessageInfo(messageInfo);
		dossierLog.setUpdateDatetime(updateDatetime);
		dossierLog.setLevel(level);
		dossierLog.setActor(actor);
		dossierLog.setActorId(actorId);
		dossierLog.setActorName(actorName);
		dossierLog.setClassName(className);
		dossierLog.setSyncStatus(syncStatus);
		dossierLog.setStepId(processStep.getProcessStepId());
		dossierLog.setStepName(processStep.getStepName());

		return dossierLogPersistence.update(dossierLog);

	}

	public DossierLog addDossierLog(
		long userId, long groupId, long companyId, long dossierId,
		long fileGroupId, String status, String actionInfo, String messageInfo,
		Date updateDatetime, int level, int actor, long actorId,
		String actorName)
		throws SystemException {

		// TODO check lai, neu khong can bo di

		long dossierLogId =
			counterLocalService.increment(DossierLog.class.getName());
		DossierLog dossierLog = dossierLogPersistence.create(dossierLogId);

		dossierLog.setGroupId(groupId);
		dossierLog.setCompanyId(companyId);

		Date now = new Date();

		dossierLog.setUserId(userId);

		dossierLog.setCreateDate(now);

		dossierLog.setModifiedDate(now);
		dossierLog.setOId(PortalUUIDUtil.generate());
		dossierLog.setDossierId(dossierId);
		dossierLog.setFileGroupId(fileGroupId);
		dossierLog.setDossierStatus(status);
		dossierLog.setActionInfo(actionInfo);
		dossierLog.setMessageInfo(messageInfo);
		dossierLog.setUpdateDatetime(updateDatetime);
		dossierLog.setLevel(level);
		dossierLog.setActor(actor);
		dossierLog.setActorId(actorId);
		dossierLog.setActorName(actorName);

		return dossierLogPersistence.update(dossierLog);

	}

	public DossierLog addDossierLog(
		long userId, long dossierId, long fileGroupId, String status,
		int actor, long actorId, String actorName, String actionInfo,
		String messageInfo, Date updateDatetime, int level,
		ServiceContext serviceContext)
		throws SystemException {

		long dossierLogId =
			counterLocalService.increment(DossierLog.class.getName());
		DossierLog dossierLog = dossierLogPersistence.create(dossierLogId);
		Date now = new Date();

		dossierLog.setUserId(userId);
		dossierLog.setGroupId(serviceContext.getScopeGroupId());
		dossierLog.setCompanyId(serviceContext.getCompanyId());
		dossierLog.setCreateDate(now);
		dossierLog.setModifiedDate(now);
		dossierLog.setOId(PortalUUIDUtil.generate());
		dossierLog.setDossierId(dossierId);
		dossierLog.setFileGroupId(fileGroupId);
		dossierLog.setDossierStatus(status);
		dossierLog.setActionInfo(actionInfo);
		dossierLog.setMessageInfo(messageInfo);
		dossierLog.setUpdateDatetime(updateDatetime);
		dossierLog.setLevel(level);
		dossierLog.setActor(actor);
		dossierLog.setActorId(actorId);
		dossierLog.setActorName(actorName);

		return dossierLogPersistence.update(dossierLog);

	}

	/**
	 * @param userId
	 * @param dossierId
	 * @param fileGroupId
	 * @param status
	 * @param actionInfo
	 * @param messageInfo
	 * @param updateDatetime
	 * @param level
	 * @param serviceContext
	 * @return
	 * @throws SystemException
	 */
	public DossierLog addDossierLog(
		long userId, long dossierId, long fileGroupId, String status,
		String actor, String actionInfo, String messageInfo,
		Date updateDatetime, int level, ServiceContext serviceContext)
		throws SystemException {

		long dossierLogId =
			counterLocalService.increment(DossierLog.class.getName());
		DossierLog dossierLog = dossierLogPersistence.create(dossierLogId);
		Date now = new Date();

		dossierLog.setUserId(userId);
		dossierLog.setGroupId(serviceContext.getScopeGroupId());
		dossierLog.setCompanyId(serviceContext.getCompanyId());
		dossierLog.setCreateDate(now);
		dossierLog.setModifiedDate(now);
		dossierLog.setOId(PortalUUIDUtil.generate());
		dossierLog.setDossierId(dossierId);
		dossierLog.setFileGroupId(fileGroupId);
		dossierLog.setDossierStatus(status);
		dossierLog.setActionInfo(actionInfo);
		dossierLog.setMessageInfo(messageInfo);
		dossierLog.setUpdateDatetime(updateDatetime);
		dossierLog.setLevel(level);

		return dossierLogPersistence.update(dossierLog);

	}

	/**
	 * @return
	 * @throws SystemException
	 */
	public int CountAllDossierLog()
		throws SystemException {

		return dossierLogPersistence.countAll();
	}

	/**
	 * @param fromUpdateDatetime
	 * @param toUpdateDatetime
	 * @param level
	 * @param dossierStatus
	 * @return
	 */
	public int countAnminLog(
		Date fromUpdateDatetime, Date toUpdateDatetime, int level,
		String dossierStatus) {

		return dossierLogFinder.countAdminLog(
			fromUpdateDatetime, toUpdateDatetime, level, dossierStatus);
	}

	/**
	 * @param dossierId
	 * @param requestCommand
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countDossierByRequestCommand(
		long dossierId, String requestCommand)
		throws PortalException, SystemException {

		return dossierLogPersistence.countByD_RC(dossierId, requestCommand);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.opencps.dossiermgt.service.DossierLogLocalService#countDossierLog
	 * (int, long)
	 */
	public int countDossierLog(int actor, long dossierId)
		throws PortalException, SystemException {

		int count = 0;
		switch (actor) {
		case 0:
			count = dossierLogPersistence.countByDossierId(dossierId);
			break;
		case 1:
			count = dossierLogFinder.countDossierLogByCitizen(dossierId);
		case 2:
			count = dossierLogFinder.countDossierLogByEmployee(dossierId);
		default:
			break;
		}

		return count;
	}

	/**
	 * @param dossierId
	 * @return
	 * @throws SystemException
	 */
	public int countDossierLogByDossierId(long dossierId)
		throws SystemException {

		return dossierLogPersistence.countByDossierId(dossierId);
	}
	
	/**
	 * @param oId
	 * @return
	 * @throws NoSuchDossierLogException
	 * @throws SystemException
	 */
	public DossierLog getDossierLogByOId(String oId)
		throws NoSuchDossierLogException, SystemException{
		return dossierLogPersistence.findByOId(oId);
	}

	public List<DossierLog> findDossierByRequestCommand(
		long dossierId, String requestCommand)
		throws PortalException, SystemException {

		return dossierLogPersistence.findByD_RC(dossierId, requestCommand);
	}

	/**
	 * @param actor
	 * @param dossierId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<DossierLog> findDossierLog(
		int actor, long dossierId, int start, int end)
		throws PortalException, SystemException {

		List<DossierLog> ls = new ArrayList<DossierLog>();

		switch (actor) {
		case 0:
			ls = dossierLogPersistence.findByDossierId(dossierId);
			break;
		case 1:
			ls =
				dossierLogFinder.findDossierLogByCitizen(dossierId, start, end);
			break;
		case 2:
			ls =
				dossierLogFinder.findDossierLogByEmployee(dossierId, start, end);
			break;
		default:
			break;
		}

		return ls;

	}

	/**
	 * @param dossierId
	 * @param actors
	 * @param requestCommands
	 * @return
	 * @throws SystemException
	 */
	public List<DossierLog> findRequiredProcessDossier(
		long dossierId, String[] actors, String[] requestCommands)
		throws SystemException {

		return dossierLogFinder.findRequiredProcessDossier(
			dossierId, actors, requestCommands);

	}

	/**
	 * @param start
	 * @param end
	 * @return
	 * @throws SystemException
	 */
	public List<DossierLog> getAllDossierLog(int start, int end)
		throws SystemException {

		boolean orderByAsc = true;
		DossierLogUpdateDatetimeComparator comparator =
			new DossierLogUpdateDatetimeComparator(orderByAsc);
		return dossierLogPersistence.findAll(start, end, comparator);
	}

	/**
	 * @param doosierId
	 * @return
	 * @throws SystemException
	 */
	public List<DossierLog> getDossierLogByDossierId(long doosierId)
		throws SystemException {

		return dossierLogPersistence.findByDossierId(doosierId);
	}

	/**
	 * @param dossierId
	 * @param start
	 * @param end
	 * @return
	 * @throws SystemException
	 */
	public List<DossierLog> getDossierLogByDossierId(
		long dossierId, int start, int end)
		throws SystemException {

		boolean orderByAsc = false;

		DossierLogUpdateDatetimeComparator orderByComparator =
			new DossierLogUpdateDatetimeComparator(orderByAsc);
		return dossierLogPersistence.findByDossierId(
			dossierId, start, end, orderByComparator);

	}

	/**
	 * @param dossierId
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 * @throws SystemException
	 */
	public List<DossierLog> getDossierLogByDossierId(
		long dossierId, int start, int end, OrderByComparator obc)
		throws SystemException {

		return dossierLogPersistence.findByDossierId(dossierId, start, end, obc);
	}

	/**
	 * @param doosierId
	 * @param syncStatus
	 * @return
	 * @throws SystemException
	 */
	public List<DossierLog> getDossierLogByDossierId(
		long dossierId, int[] syncStatus)
		throws SystemException {

		return dossierLogPersistence.findByDossierIdSync(dossierId, syncStatus);
	}

	/**
	 * @param fromUpdateDatetime
	 * @param toUpdateDatetime
	 * @param level
	 * @param dossierStatus
	 * @param start
	 * @param end
	 * @return
	 */
	public List<DossierLog> searchAdminLog(
		Date fromUpdateDatetime, Date toUpdateDatetime, int level,
		String dossierStatus, int start, int end) {

		return dossierLogFinder.searchAdminLog(
			fromUpdateDatetime, toUpdateDatetime, level, dossierStatus, start,
			end);

	}

	/**
	 * @param dossierLogId
	 * @param status
	 * @return
	 * @throws SystemException
	 */
	public DossierLog updateDossierLog(long dossierLogId, String status)
		throws SystemException {

		DossierLog dossierLog =
			dossierLogPersistence.fetchByPrimaryKey(dossierLogId);

		Date now = new Date();

		dossierLog.setModifiedDate(now);

		dossierLog.setDossierStatus(status);

		return dossierLogPersistence.update(dossierLog);

	}

}
