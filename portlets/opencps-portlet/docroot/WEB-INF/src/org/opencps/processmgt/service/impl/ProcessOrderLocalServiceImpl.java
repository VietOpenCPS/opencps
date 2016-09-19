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

package org.opencps.processmgt.service.impl;

import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.processmgt.NoSuchProcessOrderException;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.base.ProcessOrderLocalServiceBaseImpl;
import org.opencps.util.PortletConstants;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the process order local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.processmgt.service.ProcessOrderLocalService} interface.
 * <p> This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungnt
 * @see org.opencps.processmgt.service.base.ProcessOrderLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ProcessOrderLocalServiceUtil
 */
public class ProcessOrderLocalServiceImpl
	extends ProcessOrderLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.processmgt.service.ProcessOrderLocalServiceUtil} to
	 * access the process order local service.
	 */

	/**
	 * @param dossierId
	 * @param fileGroupId
	 * @param serviceProcessId
	 * @param processStepId
	 * @param processWorkflowId
	 * @param actionUserId
	 * @param actionDatetime
	 * @param stepName
	 * @param actionName
	 * @param actionNote
	 * @param assignToUserId
	 * @param dossierStatus
	 * @param daysDoing
	 * @param daysDelay
	 * @param serviceContext
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public ProcessOrder addProcessOrder(
		long dossierId, long fileGroupId, long serviceProcessId,
		long processStepId, long processWorkflowId, long actionUserId,
		Date actionDatetime, String stepName, String actionName,
		String actionNote, long assignToUserId, String dossierStatus,
		int daysDoing, int daysDelay, ServiceContext serviceContext)
		throws SystemException, PortalException {

		long processOrderId = counterLocalService
			.increment(ProcessOrder.class
				.getName());
		ProcessOrder processOrder = processOrderPersistence
			.create(processOrderId);

		Dossier dossier = DossierLocalServiceUtil
			.getDossier(dossierId);

		Date now = new Date();

		processOrder
			.setUserId(serviceContext
				.getUserId());
		processOrder
			.setGroupId(serviceContext
				.getScopeGroupId());
		processOrder
			.setCompanyId(serviceContext
				.getCompanyId());
		processOrder
			.setCreateDate(now);
		processOrder
			.setModifiedDate(now);

		processOrder
			.setActionDatetime(actionDatetime);
		processOrder
			.setActionNote(actionNote);
		processOrder
			.setActionUserId(actionUserId);
		processOrder
			.setAssignToUserId(assignToUserId);
		processOrder
			.setDossierId(dossierId);
		processOrder
			.setDossierStatus(dossierStatus);
		processOrder
			.setDossierTemplateId(dossier
				.getDossierTemplateId());
		// processOrder.setErrorInfo(errorInfo);
		processOrder
			.setFileGroupId(fileGroupId);
		processOrder
			.setGovAgencyCode(dossier
				.getGovAgencyCode());
		processOrder
			.setGovAgencyName(dossier
				.getGovAgencyName());
		processOrder
			.setGovAgencyOrganizationId(dossier
				.getGovAgencyOrganizationId());
		processOrder
			.setProcessOrderId(processOrderId);
		processOrder
			.setProcessStepId(processStepId);
		processOrder
			.setProcessWorkflowId(processWorkflowId);
		processOrder
			.setServiceInfoId(dossier
				.getServiceInfoId());
		processOrder
			.setServiceProcessId(serviceProcessId);

		actionHistoryLocalService
			.addActionHistory(processOrderId, processWorkflowId, actionDatetime,
				stepName, actionName, actionNote, actionUserId, daysDoing,
				daysDelay, serviceContext);

		return processOrderPersistence
			.update(processOrder);
	}

	/**
	 * @param userId
	 * @param dossierId
	 * @param fileGroupId
	 * @param serviceProcessId
	 * @param processStepId
	 * @param processWorkflowId
	 * @param actionUserId
	 * @param actionDatetime
	 * @param stepName
	 * @param actionName
	 * @param actionNote
	 * @param assignToUserId
	 * @param dossierStatus
	 * @param daysDoing
	 * @param daysDelay
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public ProcessOrder addProcessOrder(
		long userId, long dossierId, long fileGroupId, long serviceProcessId,
		long processStepId, long processWorkflowId, long actionUserId,
		Date actionDatetime, String stepName, String actionName,
		String actionNote, long assignToUserId, String dossierStatus,
		int daysDoing, int daysDelay)
		throws SystemException, PortalException {

		long processOrderId = counterLocalService
			.increment(ProcessOrder.class
				.getName());
		ProcessOrder processOrder = processOrderPersistence
			.create(processOrderId);

		Dossier dossier = DossierLocalServiceUtil
			.getDossier(dossierId);

		Date now = new Date();

		processOrder
			.setUserId(userId);
		processOrder
			.setGroupId(dossier
				.getGroupId());
		processOrder
			.setCompanyId(dossier
				.getCompanyId());
		processOrder
			.setCreateDate(now);
		processOrder
			.setModifiedDate(now);

		processOrder
			.setActionDatetime(actionDatetime);
		processOrder
			.setActionNote(actionNote);
		processOrder
			.setActionUserId(actionUserId);
		processOrder
			.setAssignToUserId(assignToUserId);
		processOrder
			.setDossierId(dossierId);
		processOrder
			.setDossierStatus(dossierStatus);
		processOrder
			.setDossierTemplateId(dossier
				.getDossierTemplateId());
		// processOrder.setErrorInfo(errorInfo);
		processOrder
			.setFileGroupId(fileGroupId);
		processOrder
			.setGovAgencyCode(dossier
				.getGovAgencyCode());
		processOrder
			.setGovAgencyName(dossier
				.getGovAgencyName());
		processOrder
			.setGovAgencyOrganizationId(dossier
				.getGovAgencyOrganizationId());
		processOrder
			.setProcessOrderId(processOrderId);
		processOrder
			.setProcessStepId(processStepId);
		processOrder
			.setProcessWorkflowId(processWorkflowId);
		processOrder
			.setServiceInfoId(dossier
				.getServiceInfoId());
		processOrder
			.setServiceProcessId(serviceProcessId);

		actionHistoryLocalService
			.addActionHistory(userId, dossier
				.getGroupId(), dossier
					.getCompanyId(),
				processOrderId, processWorkflowId, actionDatetime, stepName,
				actionName, actionNote, actionUserId, daysDoing, daysDelay,
				PortletConstants.DOSSIER_STATUS_RECEIVING);

		return processOrderPersistence
			.update(processOrder);
	}

	/**
	 * @param serviceInfoId
	 * @param processStepId
	 * @param loginUserId
	 * @param actionUserId
	 * @return
	 */
	public int countProcessOrder(
		long serviceInfoId, long processStepId, long loginUserId,
		long actionUserId) {

		return processOrderFinder
			.countProcessOrder(serviceInfoId, processStepId, loginUserId,
				actionUserId);
	}

	/**
	 * @param serviceInfoId
	 * @param processStepId
	 * @param actionUserId
	 * @return
	 */
	public int countProcessOrderJustFinished(
		long serviceInfoId, long processStepId, long actionUserId) {

		return processOrderFinder
			.countProcessOrderJustFinished(serviceInfoId, processStepId,
				actionUserId);
	}

	/**
	 * @param dossierId
	 * @param fileGroupId
	 * @return
	 * @throws NoSuchProcessOrderException
	 * @throws SystemException
	 */
	public ProcessOrder getProcessOrder(long dossierId, long fileGroupId)
		throws NoSuchProcessOrderException, SystemException {

		return processOrderPersistence
			.findByD_F(dossierId, fileGroupId);
	}

	/**
	 * @param loginUserId
	 * @return
	 */
	public List getProcessOrderServiceByUser(long loginUserId) {

		return processOrderFinder
			.getProcessOrderServiceByUser(loginUserId);
	}

	/**
	 * @param loginUserId
	 * @return
	 */
	public List getProcessOrderServiceJustFinishedByUser(long loginUserId) {

		return processOrderFinder
			.getProcessOrderServiceJustFinishedByUser(loginUserId);
	}

	/**
	 * @param loginUserId
	 * @param serviceInfoId
	 * @return
	 */
	public List getUserProcessStep(long loginUserId, long serviceInfoId) {

		return processOrderFinder
			.getUserProcessStep(loginUserId, serviceInfoId);
	}

	/**
	 * @param loginUserId
	 * @param serviceInfoId
	 * @return
	 */
	public List getUserProcessStepJustFinished(
		long loginUserId, long serviceInfoId) {

		return processOrderFinder
			.getUserProcessStepJustFinished(loginUserId, serviceInfoId);
	}

	/**
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessOrder initProcessOrder()
		throws PortalException, SystemException {

		ProcessOrder order = null;

		long processOrderId = counterLocalService
			.increment(ProcessOrder.class
				.getName());

		order = processOrderPersistence
			.create(processOrderId);

		processOrderPersistence
			.update(order);

		return order;
	}

	/**
	 * @param userId
	 * @param companyId
	 * @param groupId
	 * @param serviceInfoId
	 * @param dossierTempateId
	 * @param govAgencyCode
	 * @param govAgencyName
	 * @param govAgencyOrganizationId
	 * @param serviceProcessId
	 * @param dossierId
	 * @param fileGroupId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessOrder initProcessOrder(
		long userId, long companyId, long groupId, long serviceInfoId,
		long dossierTemplateId, String govAgencyCode, String govAgencyName,
		long govAgencyOrganizationId, long serviceProcessId, long dossierId,
		long fileGroupId)
		throws PortalException, SystemException {

		ProcessOrder order = null;

		long processOrderId = counterLocalService
			.increment(ProcessOrder.class
				.getName());

		order = processOrderPersistence
			.create(processOrderId);

		Date now = new Date();

		order
			.setCreateDate(now);
		order
			.setModifiedDate(now);
		order
			.setUserId(userId);
		order
			.setCompanyId(companyId);
		order
			.setGroupId(groupId);
		order
			.setServiceInfoId(serviceInfoId);
		order
			.setDossierTemplateId(dossierTemplateId);
		order
			.setGovAgencyCode(govAgencyCode);
		order
			.setGovAgencyName(govAgencyName);
		order
			.setGovAgencyOrganizationId(govAgencyOrganizationId);
		order
			.setServiceProcessId(serviceProcessId);
		order
			.setDossierId(dossierId);
		order
			.setFileGroupId(fileGroupId);

		processOrderPersistence
			.update(order);

		return order;
	}

	/**
	 * @param userId
	 * @param companyId
	 * @param groupId
	 * @param serviceInfoId
	 * @param dossierTemplateId
	 * @param govAgencyCode
	 * @param govAgencyName
	 * @param govAgencyOrganizationId
	 * @param serviceProcessId
	 * @param dossierId
	 * @param fileGroupId
	 * @param processWorkflowId
	 * @param actionDatetime
	 * @param stepName
	 * @param actionName
	 * @param actionNote
	 * @param actionUserId
	 * @param daysDoing
	 * @param daysDelay
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessOrder initProcessOrder(
		long userId, long companyId, long groupId, long serviceInfoId,
		long dossierTemplateId, String govAgencyCode, String govAgencyName,
		long govAgencyOrganizationId, long serviceProcessId, long dossierId,
		long fileGroupId, long processWorkflowId, Date actionDatetime,
		String stepName, String actionName, String actionNote,
		long actionUserId, int daysDoing, int daysDelay, String dossierStatus)
		throws PortalException, SystemException {

		ProcessOrder order = null;

		long processOrderId = counterLocalService
			.increment(ProcessOrder.class
				.getName());

		order = processOrderPersistence
			.create(processOrderId);

		Date now = new Date();

		order
			.setCreateDate(now);
		order
			.setModifiedDate(now);
		order
			.setUserId(userId);
		order
			.setCompanyId(companyId);
		order
			.setGroupId(groupId);
		order
			.setServiceInfoId(serviceInfoId);
		order
			.setDossierTemplateId(dossierTemplateId);
		order
			.setGovAgencyCode(govAgencyCode);
		order
			.setGovAgencyName(govAgencyName);
		order
			.setGovAgencyOrganizationId(govAgencyOrganizationId);
		order
			.setServiceProcessId(serviceProcessId);
		order
			.setDossierId(dossierId);
		order
			.setFileGroupId(fileGroupId);
		order
			.setProcessWorkflowId(processWorkflowId);

		processOrderPersistence
			.update(order);

		actionHistoryLocalService
			.addActionHistory(userId, fileGroupId, companyId, processOrderId,
				processWorkflowId, actionDatetime, stepName, actionName,
				actionNote, actionUserId, daysDoing, daysDelay, dossierStatus);

		return order;
	}

	/**
	 * @param serviceInfoId
	 * @param processStepId
	 * @param loginUserId
	 * @param actionUserId
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 */
	public List searchProcessOrder(
		long serviceInfoId, long processStepId, long loginUserId,
		long actionUserId, int start, int end, OrderByComparator orderByComparator) {
		
		return processOrderFinder
			.searchProcessOrder(serviceInfoId, processStepId, loginUserId,
				actionUserId, start, end, orderByComparator);
	}

	/**
	 * @param serviceInfoId
	 * @param processStepId
	 * @param actionUserId
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 */
	public List searchProcessOrderJustFinished(
		long serviceInfoId, long processStepId, long actionUserId, int start,
		int end, OrderByComparator orderByComparator) {

		return processOrderFinder
			.searchProcessOrderJustFinished(serviceInfoId, processStepId,
				actionUserId, start, end, orderByComparator);
	}

	/**
	 * Update Step
	 * 
	 * @param processOrderId
	 * @param processStepId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessOrder updateInitStep(long processOrderId, long processStepId)
		throws PortalException, SystemException {

		ProcessOrder order = processOrderPersistence
			.fetchByPrimaryKey(processOrderId);

		order
			.setProcessStepId(processStepId);

		processOrderPersistence
			.update(order);

		return order;

	}

	/**
	 * @param processOrderId
	 * @param processStepId
	 * @param actionUserId
	 * @param actionDatetime
	 * @param actionNote
	 * @param assignToUserId
	 * @return
	 * @throws NoSuchProcessOrderException
	 * @throws SystemException
	 */
	public ProcessOrder updateProcessOrder(
		long processOrderId, long processStepId, long actionUserId,
		Date actionDatetime, String actionNote, long assignToUserId)
		throws NoSuchProcessOrderException, SystemException {

		ProcessOrder processOrder = processOrderPersistence
			.findByPrimaryKey(processOrderId);

		processOrder
			.setModifiedDate(new Date());

		processOrder
			.setProcessStepId(processStepId);
		processOrder
			.setActionUserId(actionUserId);
		processOrder
			.setActionDatetime(actionDatetime);
		processOrder
			.setActionNote(actionNote);
		processOrder
			.setAssignToUserId(assignToUserId);

		return processOrderPersistence
			.update(processOrder);
	}

	/**
	 * @param processOrderId
	 * @param processStepId
	 * @param processWorkflowId
	 * @param actionUserId
	 * @param actionDatetime
	 * @param actionNote
	 * @param assignToUserId
	 * @param stepName
	 * @param actionName
	 * @param daysDoing
	 * @param daysDelay
	 * @param dossierStatus
	 * @return
	 * @throws NoSuchProcessOrderException
	 * @throws SystemException
	 */
	public ProcessOrder updateProcessOrder(
		long processOrderId, long processStepId, long processWorkflowId,
		long actionUserId, Date actionDatetime, String actionNote,
		long assignToUserId, String stepName, String actionName, int daysDoing,
		int daysDelay, String dossierStatus)
		throws NoSuchProcessOrderException, SystemException {

		ProcessOrder processOrder = processOrderPersistence
			.findByPrimaryKey(processOrderId);

		processOrder
			.setDossierStatus(dossierStatus);

		processOrder
			.setModifiedDate(new Date());

		processOrder
			.setProcessStepId(processStepId);
		processOrder
			.setActionUserId(actionUserId);
		processOrder
			.setActionDatetime(actionDatetime);
		processOrder
			.setActionNote(actionNote);
		processOrder
			.setAssignToUserId(assignToUserId);

		processOrder
			.setProcessWorkflowId(processWorkflowId);

		actionHistoryLocalService
			.addActionHistory(processOrder
				.getUserId(), processOrder
					.getGroupId(),
				processOrder
					.getCompanyId(),
				processOrderId, processWorkflowId, actionDatetime, stepName,
				actionName, actionNote, actionUserId, daysDoing, daysDelay,
				dossierStatus);
		return processOrderPersistence
			.update(processOrder);
	}

	/**
	 * @param processOrderId
	 * @param dossierStatus
	 * @return
	 * @throws NoSuchProcessOrderException
	 * @throws SystemException
	 */
	public ProcessOrder updateProcessOrderStatus(
		long processOrderId, String dossierStatus)
		throws NoSuchProcessOrderException, SystemException {

		ProcessOrder processOrder = processOrderPersistence
			.findByPrimaryKey(processOrderId);
		processOrder
			.setModifiedDate(new Date());
		processOrder
			.setDossierStatus(dossierStatus);
		return processOrderPersistence
			.update(processOrder);

	}
	
	/**
	 * @param serviceinfoId
	 * @return
	 * @throws SystemException
	 */
	public List<ProcessOrder> getProcessOrdersByServiceInfoId(long serviceinfoId) 
					throws SystemException {
		return processOrderPersistence.findByServiceInfoId(serviceinfoId);
	}
}
