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

import org.opencps.processmgt.NoSuchProcessWorkflowException;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.processmgt.service.base.ProcessWorkflowLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the process workflow local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.processmgt.service.ProcessWorkflowLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.processmgt.service.base.ProcessWorkflowLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil
 */
public class ProcessWorkflowLocalServiceImpl
	extends ProcessWorkflowLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil} to access the process workflow local service.
	 */
	
	/**
	 * @param serviceProcessId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessWorkflow getFirstProcessWorkflow(long serviceProcessId)
	    throws PortalException, SystemException {
		return processWorkflowPersistence.findBySPI_(serviceProcessId, 0);
	}	
	
	/**
	 * Update workflow
	 * 
	 * @param processWorkflowId
	 * @param preProcessStepId
	 * @param postProcessStepId
	 * @param autoEvent
	 * @param actionName
	 * @param assignUser
	 * @param actionUserId
	 * @param requestPayment
	 * @param paymentFee
	 * @param generateReceptionNo
	 * @param receptionNoPattern
	 * @param generateDeadline
	 * @param deadlinePattern
	 * @param context
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessWorkflow updateWorkflow(
	    long processWorkflowId, long preProcessStepId, long postProcessStepId,
	    String autoEvent, String actionName, boolean assignUser,
	    long actionUserId, boolean requestPayment, double paymentFee,
	    String generateReceptionNo, String receptionNoPattern,
	    boolean generateDeadline, String deadlinePattern, boolean isFinishStep, String preCondition, boolean isMultipled,
	    ServiceContext context)
	    throws PortalException, SystemException {

		ProcessWorkflow workflow =
		    processWorkflowPersistence.fetchByPrimaryKey(processWorkflowId);

		if (Validator.isNotNull(workflow)) {
			// update modified date

			workflow.setModifiedDate(new Date());
			
			
			//update other filed
			
			workflow.setPreProcessStepId(preProcessStepId);
			workflow.setAutoEvent(autoEvent);
			workflow.setActionName(actionName);
			workflow.setAssignUser(assignUser);
			workflow.setActionUserId(actionUserId);
			workflow.setRequestPayment(requestPayment);
			workflow.setPaymentFee(paymentFee);
			workflow.setGenerateReceptionNo(generateReceptionNo);
			workflow.setReceptionNoPattern(receptionNoPattern);
			workflow.setGenerateDeadline(generateDeadline);
			workflow.setDeadlinePattern(deadlinePattern);
			workflow.setIsFinishStep(isFinishStep);
			workflow.setIsMultipled(isMultipled);
			workflow.setPreCondition(preCondition);
			
			processWorkflowPersistence.update(workflow);
		}

		return workflow;

	}
	
	/**
	 * @param processWorkflowId
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void deleteWorkflow(long processWorkflowId) throws PortalException, SystemException {
		List<WorkflowOutput> outputs = workflowOutputPersistence.findByP_W_ID(processWorkflowId);
		
		for (WorkflowOutput output : outputs) {
			workflowOutputPersistence.remove(output);
		}
		
		processWorkflowPersistence.remove(processWorkflowId);
		
	}
	
	/**
	 * Add Workflow
	 * 
	 * @param serviceProcessId
	 * @param preProcessStepId
	 * @param postProcessStepId
	 * @param autoEvent
	 * @param actionName
	 * @param assignUser
	 * @param actionUserId
	 * @param requestPayment
	 * @param paymentFee
	 * @param generateReceptionNo
	 * @param receptionNoPattern
	 * @param generateDeadline
	 * @param deadlinePattern
	 * @param context
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	
	public ProcessWorkflow addWorkflow(
	    long serviceProcessId, long preProcessStepId, long postProcessStepId,
	    String autoEvent, String actionName, boolean assignUser,
	    long actionUserId, boolean requestPayment, double paymentFee,
	    String generateReceptionNo, String receptionNoPattern,
	    boolean generateDeadline, String deadlinePattern, boolean isFinishStep,
	    String preCondition, boolean isMultipled, ServiceContext context)
	    throws PortalException, SystemException {
		
		long processWorkflowId = counterLocalService.increment(ProcessWorkflow.class.getName());
		
		ProcessWorkflow workflow = processWorkflowPersistence.create(processWorkflowId);
		
		Date now = new Date();
		
		if (Validator.isNotNull(workflow)) {
			
			// Add AuditField
			
			workflow.setCompanyId(context.getCompanyId());
			workflow.setGroupId(context.getScopeGroupId());
			workflow.setUserId(context.getUserId());
			workflow.setCreateDate(now);
			workflow.setModifiedDate(now);
			
			// Add OtherField
			
			workflow.setServiceProcessId(serviceProcessId);
			workflow.setPreProcessStepId(preProcessStepId);
			workflow.setPostProcessStepId(postProcessStepId);
			workflow.setAutoEvent(autoEvent);
			workflow.setActionName(actionName);
			workflow.setAssignUser(assignUser);
			workflow.setActionUserId(actionUserId);
			workflow.setRequestPayment(requestPayment);
			workflow.setPaymentFee(paymentFee);
			workflow.setGenerateReceptionNo(generateReceptionNo);
			workflow.setReceptionNoPattern(receptionNoPattern);
			workflow.setGenerateDeadline(generateDeadline);
			workflow.setDeadlinePattern(deadlinePattern);
			workflow.setIsFinishStep(isFinishStep);
			workflow.setPreCondition(preCondition);
			workflow.setIsMultipled(isMultipled);
			
			processWorkflowPersistence.update(workflow);
		}
		
		return workflow;
	}
	
	/**
	 * Search ProcessWorkflow
	 * 
	 * @param serviceProcessId
	 * @param start
	 * @param end
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ProcessWorkflow> searchWorkflow(
	    long serviceProcessId, int start, int end)
	    throws PortalException, SystemException {

		return processWorkflowPersistence.findByS_P_ID(
		    serviceProcessId, start, end);
	}

	/**
	 * Count ProcessWorkflow
	 * 
	 * @param serviceProcessId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countWorkflow(long serviceProcessId)
	    throws PortalException, SystemException {

		return processWorkflowPersistence.countByS_P_ID(serviceProcessId);
	}
	
	/**
	 * @param serviceProcessId
	 * @param preProcessStepId
	 * @return
	 * @throws NoSuchProcessWorkflowException
	 * @throws SystemException
	 */
	public ProcessWorkflow getPreProcessWorkflow(long serviceProcessId, long preProcessStepId)
		throws NoSuchProcessWorkflowException, SystemException{
		//preProcessStepId current step as postProcessStepId of pre step
		return processWorkflowPersistence.findByS_PPSID(serviceProcessId, preProcessStepId);
	}
	
	public List<ProcessWorkflow> getPostProcessWorkflow(long serviceProcessId, long preProcessStepId) 
		throws SystemException{
		return processWorkflowPersistence.findByS_PRE_PS_ID(serviceProcessId, preProcessStepId);
	}
}