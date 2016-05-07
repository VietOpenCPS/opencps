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

import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessStepDossierPart;
import org.opencps.processmgt.model.StepAllowance;
import org.opencps.processmgt.service.base.ProcessStepLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the process step local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.processmgt.service.ProcessStepLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @see org.opencps.processmgt.service.base.ProcessStepLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ProcessStepLocalServiceUtil
 */
public class ProcessStepLocalServiceImpl
    extends ProcessStepLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.processmgt.service.ProcessStepLocalServiceUtil} to
	 * access the process step local service.
	 */

	/**
	 * @param serviceProcessId
	 * @param start
	 * @param end
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ProcessStep> getStepByProcess(long serviceProcessId, int start, int end)
	    throws PortalException, SystemException {

		return processStepPersistence.findByS_P_ID(serviceProcessId, start, end);
	}

	/**
	 * @param serviceProcessId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ProcessStep> getStepByProcess(long serviceProcessId)
	    throws PortalException, SystemException {
		return processStepPersistence.findByS_P_ID(serviceProcessId);
	}
	
	/**
	 * @param serviceProcessId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countStepByProcess(long serviceProcessId)
	    throws PortalException, SystemException {
		return processStepPersistence.countByS_P_ID(serviceProcessId);
	}
	
	/**
	 * @param processStepId
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void removeStep(long processStepId)
	    throws PortalException, SystemException {
		
		List<ProcessStepDossierPart> stepDossiers = processStepDossierPartPersistence.findByProcessStepId(processStepId);
		
		List<StepAllowance> stepAllowances = stepAllowancePersistence.findByprocessStepId(processStepId);
		
		for (ProcessStepDossierPart stepDossier : stepDossiers) {
			processStepDossierPartPersistence.remove(stepDossier);
		}
		
		for (StepAllowance stepAllowance : stepAllowances) {
			stepAllowancePersistence.remove(stepAllowance);
		}
		
		processStepPersistence.remove(processStepId);
		
	}

	/**
	 * @param groupId
	 * @param start
	 * @param end
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ProcessStep> searchStep(long groupId, int start, int end)
	    throws PortalException, SystemException {

		return processStepPersistence.findByGroupId(groupId, start, end);
	}

	/**
	 * @param groupId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countStep(long groupId)
	    throws PortalException, SystemException {

		return processStepPersistence.countByGroupId(groupId);
	}

	/**
	 * Add ProcessStep
	 * 
	 * @param processStepId
	 * @param serviceProcessId
	 * @param stepName
	 * @param stepNo
	 * @param dossierStatus
	 * @param daysDuration
	 * @param externalAppUrl
	 * @param context
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessStep addStep(
	    long serviceProcessId, String stepName,
	    int stepNo, String dossierStatus, int daysDuration,
	    long referenceDossierPartId, String externalAppUrl, ServiceContext context)
	    throws PortalException, SystemException {
		
		ProcessStep step = null;
		
		long processStepId = counterLocalService.increment(ProcessStep.class.getName());
		
		step = processStepPersistence.create(processStepId);
		
		Date now = new Date();
		
		if (Validator.isNotNull(step)) {
			//Add audit field
			
			step.setCompanyId(context.getCompanyId());
			step.setGroupId(context.getScopeGroupId());
			step.setUserId(context.getUserId());
			step.setCreateDate(now);
			step.setModifiedDate(now);
			
			//Add content entry
			
			step.setServiceProcessId(serviceProcessId);
			step.setStepName(stepName);
			step.setSequenceNo(stepNo);
			step.setDossierStatus(dossierStatus);
			step.setDaysDuration(daysDuration);
			step.setExternalAppUrl(externalAppUrl);
			step.setReferenceDossierPartId(referenceDossierPartId);
			
			processStepPersistence.update(step);
		}
		
		return step;
		
	}
	
	/**
	 * Update ProcessStep
	 * 
	 * @param processStepId
	 * @param serviceProcessId
	 * @param stepName
	 * @param stepNo
	 * @param dossierStatus
	 * @param daysDuration
	 * @param externalAppUrl
	 * @param context
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	
	public ProcessStep updateStep(
	    long processStepId, long serviceProcessId, String stepName,
	    int stepNo, String dossierStatus, int daysDuration,
	    long referenceDossierPartId, String externalAppUrl, ServiceContext context)
	    throws PortalException, SystemException {

		ProcessStep step = null;

		step = processStepPersistence.fetchByPrimaryKey(processStepId);

		Date now = new Date();

		if (Validator.isNotNull(step)) {
			// update modified date

			step.setModifiedDate(now);

			// Add content entry

			step.setServiceProcessId(serviceProcessId);
			step.setStepName(stepName);
			step.setSequenceNo(stepNo);
			step.setDossierStatus(dossierStatus);
			step.setDaysDuration(daysDuration);
			step.setExternalAppUrl(externalAppUrl);
			step.setReferenceDossierPartId(referenceDossierPartId);

			processStepPersistence.update(step);
		}

		return step;

	}
}