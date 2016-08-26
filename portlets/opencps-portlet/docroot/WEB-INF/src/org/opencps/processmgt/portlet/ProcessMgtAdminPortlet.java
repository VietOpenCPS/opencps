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

package org.opencps.processmgt.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.model.impl.ServiceConfigImpl;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.processmgt.NoSuchServiceProcessException;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessStepDossierPart;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.ServiceInfoProcess;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.model.StepAllowance;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.processmgt.service.ProcessStepDossierPartLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.ServiceInfoProcessLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.processmgt.service.StepAllowanceLocalServiceUtil;
import org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil;
import org.opencps.processmgt.util.ProcessMgtUtil;
import org.opencps.processmgt.util.ProcessUtils;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author khoavd
 */
public class ProcessMgtAdminPortlet extends MVCPortlet {
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws SystemException
	 * @throws IOException
	 */
	public void deteleRelaSeInfoAndProcess (ActionRequest actionRequest, ActionResponse actionResponse) 
					throws SystemException, IOException {
		long serviceProcessId = ParamUtil.getLong(actionRequest, "serviceProcessId");
		long serviceConfigId = ParamUtil.getLong(actionRequest, "serviceConfigId");
		String backURL = ParamUtil.getString(actionRequest, "backURL");
		
		ServiceConfig serviceConfig = null;
		
		try {
			serviceConfig = ServiceConfigLocalServiceUtil.getServiceConfig(serviceConfigId);
			//remove process id from serviceconfig
			serviceConfig.setServiceProcessId(0);
			ServiceConfigLocalServiceUtil.updateServiceConfig(serviceConfig);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		ServiceInfoProcessLocalServiceUtil.deleteServiceInfoProcess(serviceProcessId, serviceConfigId);
		
		if(Validator.isNotNull(backURL)) {
			actionResponse.sendRedirect(backURL);
		}
	}
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws SystemException
	 * @throws IOException
	 */
	public void chooseServiceInfoFromProcess(ActionRequest actionRequest, ActionResponse actionResponse) throws SystemException, IOException {
		long serviceProcessId = ParamUtil.getLong(actionRequest, "serviceProcessId");
		long [] serviceConfigIds = ParamUtil
					    .getLongValues(actionRequest, "rowIds");
		String backURL = ParamUtil.getString(actionRequest, "backURL");
		
		
		if(serviceConfigIds.length > 0) {
			for(int index = 0; index < serviceConfigIds.length; index ++) {
				try {
					ServiceConfig serviceConfig = new ServiceConfigImpl();
					serviceConfig = ServiceConfigLocalServiceUtil.getServiceConfig(serviceConfigIds[index]);
					serviceConfig.setServiceProcessId(serviceProcessId);
					ServiceConfigLocalServiceUtil.updateServiceConfig(serviceConfig);
				}
				catch (Exception e) {
					_log.error(e);
				}
			}
		}
		
		ServiceInfoProcessLocalServiceUtil.addProcessServiceInfos(serviceProcessId, serviceConfigIds);
	
		if(Validator.isNotNull(backURL)) {
			actionResponse.sendRedirect(backURL);
		}
	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void sendMessage(
	    ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortletException{
		
		Message message = new Message();
		
		message.put("curTime", new Date());
		
		try {
	        MessageBusUtil.sendMessage("opencps/backoffice/out/destination", message);
        }
        catch (Exception e) {
		    _log.error(e);
        }
		
	}
	
	/**
	 * Update action
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateAction(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

		long processWorkflowId =
		    ParamUtil.getLong(actionRequest, "processWorkflowId");

		long serviceProcessId =
		    ParamUtil.getLong(actionRequest, "serviceProcessId");

		long preProcessStepId =
		    ParamUtil.getLong(actionRequest, "preProcessStepId");

		long postProcessStepId =
		    ParamUtil.getLong(actionRequest, "postProcessStepId");

		String autoEvent = ParamUtil.getString(actionRequest, "autoEvent");

		String actionName = ParamUtil.getString(actionRequest, "actionName");

		boolean assignUser = ParamUtil.getBoolean(actionRequest, "assignUser");
		
		long actionUserId = ParamUtil.getLong(actionRequest, "actionUserId");

		boolean requestPayment =
		    ParamUtil.getBoolean(actionRequest, "requestPaymentCheckbox");

		String paymentFee = ParamUtil.getString(actionRequest, "paymentFee");

		boolean generateReceptionNo =
		    ParamUtil.getBoolean(actionRequest, "generateReceptionNoCheckbox");

		String receptionNoPattern =
		    ParamUtil.getString(actionRequest, "receptionNoPattern");

		boolean generateDeadline =
		    ParamUtil.getBoolean(actionRequest, "generateDeadlineCheckbox");

		String deadlinePattern =
		    ParamUtil.getString(actionRequest, "deadlinePattern");
		
		boolean isFinishStep =
					    ParamUtil.getBoolean(actionRequest, "isFinishStepCheckbox");
		
		String preCondition = ParamUtil.getString(actionRequest, "preCondition");
		
		boolean isMultipled = ParamUtil.getBoolean(actionRequest, "isMultipledCheckbox");
		
		
		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (processWorkflowId <= 0) {

				// Add Workflow

				ProcessWorkflow workflow =
				    ProcessWorkflowLocalServiceUtil.addWorkflow(
				        serviceProcessId, preProcessStepId, postProcessStepId,
				        autoEvent, actionName, assignUser, actionUserId,
				        requestPayment, paymentFee, generateReceptionNo,
				        receptionNoPattern, generateDeadline, deadlinePattern, isFinishStep, preCondition, isMultipled,
				        serviceContext);

				// Add WorkflowOutput

				List<WorkflowOutput> workflowOutputs =
				    ProcessUtils.getWorkflowOutput(
				        actionRequest, workflow.getProcessWorkflowId());

				for (WorkflowOutput output : workflowOutputs) {
					WorkflowOutputLocalServiceUtil.addWorkflowOutput(
					    output.getDossierPartId(),
					    output.getProcessWorkflowId(), output.getRequired(),
					    output.getEsign(), output.getPostback());
				}

			}
			else {
				// Update Workflow

				ProcessWorkflow workflow =
				    ProcessWorkflowLocalServiceUtil.updateWorkflow(
				        processWorkflowId, preProcessStepId, postProcessStepId,
				        autoEvent, actionName, assignUser, actionUserId,
				        requestPayment, paymentFee, generateReceptionNo,
				        receptionNoPattern, generateDeadline, deadlinePattern,
				        isFinishStep, preCondition, isMultipled, serviceContext);

				// Add WorkflowOutput

				List<WorkflowOutput> workflowOutputs =
				    ProcessUtils.getWorkflowOutput(
				        actionRequest, workflow.getProcessWorkflowId());

				List<WorkflowOutput> beforeList =
				    WorkflowOutputLocalServiceUtil.getByProcessWF(workflow.getProcessWorkflowId());

				List<WorkflowOutput> removeWorkflow =
				    ProcessUtils.getWorkflowOutputRemove(
				        beforeList, workflowOutputs);

				for (WorkflowOutput output : removeWorkflow) {
					WorkflowOutputLocalServiceUtil.deleteWorkflowOutput(output);
				}

				for (WorkflowOutput output : workflowOutputs) {
					if (output.getWorkflowOutputId() > 0) {
						WorkflowOutputLocalServiceUtil.updateWorkflowOutput(
						    output.getWorkflowOutputId(),
						    output.getDossierPartId(),
						    output.getProcessWorkflowId(),
						    output.getRequired(), output.getEsign(),
						    output.getPostback());
					}
					else {
						WorkflowOutputLocalServiceUtil.addWorkflowOutput(
						    output.getDossierPartId(),
						    output.getProcessWorkflowId(),
						    output.getRequired(), output.getEsign(),
						    output.getPostback());
					}
				}

			}

			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL +
				    "#_15_WAR_opencpsportlet_tab=_15_WAR_opencpsportlet_action");
			}
		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}
	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void deleteAction( ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		long processWorkflowId = ParamUtil.getLong(actionRequest, "processWorkflowId");

		try {

			if (isRemoveProcessWorkflow(processWorkflowId)) {
				ProcessWorkflowLocalServiceUtil.deleteWorkflow(processWorkflowId);
			}

			// Redirect page
			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL +
				    "#_15_WAR_opencpsportlet_tab=_15_WAR_opencpsportlet_action");
			}

		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL +
				    "#_15_WAR_opencpsportlet_tab=_15_WAR_opencpsportlet_action");
			}
		}

	}

	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void deleteStep(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		long processStepId = ParamUtil.getLong(actionRequest, "processStepId");

		try {

			if (isRemoveProcessStep(processStepId)) {
				ProcessStepLocalServiceUtil.removeStep(processStepId);
			}

			// Redirect page
			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL +
				    "#_15_WAR_opencpsportlet_tab=_15_WAR_opencpsportlet_step");
			}

		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL +
				    "#_15_WAR_opencpsportlet_tab=_15_WAR_opencpsportlet_step");
			}
		}

	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateProcessStep(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {


		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		long processStepId = ParamUtil.getLong(actionRequest, "processStepId");

		String stepName = ParamUtil.getString(actionRequest, "stepName");

		int sequenceNo = ParamUtil.getInteger(actionRequest, "sequenceNo");

		String dossierStatus =
		    ParamUtil.getString(actionRequest, "dossierStatus");

		int daysDuration = ParamUtil.getInteger(actionRequest, "daysDuration");

		long referenceDossierPartId =
		    ParamUtil.getLong(actionRequest, "referenceDossierPartId");
		
		String externalAppUrl =
		    ParamUtil.getString(actionRequest, "externalAppUrl");

		long serviceProcessId =
		    ParamUtil.getLong(actionRequest, "serviceProcessId");

		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);
			if (processStepId <= 0) {
				// TODO: Update validator here
				
				// Add ProcessStep

				ProcessStep step = ProcessStepLocalServiceUtil.addStep(
				    serviceProcessId, stepName, sequenceNo, dossierStatus,
				    daysDuration, referenceDossierPartId, externalAppUrl,
				    serviceContext);
				
				if (Validator.isNotNull(step)) {
					List<StepAllowance> stepAllowances =
					    ProcessUtils.getStepAllowance(
					        actionRequest, step.getProcessStepId());

					List<ProcessStepDossierPart> stepDossiers =
					    ProcessUtils.getStepDossiers(
					        actionRequest, step.getProcessStepId());

					// Add StepAllowane
					for (StepAllowance stepAllowance : stepAllowances) {

						if (stepAllowance.getRoleId() == 0) {
							continue;
						}

						if (stepAllowance.getStepAllowanceId() == 0) {
							StepAllowanceLocalServiceUtil.addAllowance(
							    step.getProcessStepId(),
							    stepAllowance.getRoleId(),
							    stepAllowance.getReadOnly());
						}
						else {
							StepAllowanceLocalServiceUtil.updateAllowance(
							    stepAllowance.getStepAllowanceId(),
							    stepAllowance.getRoleId(),
							    stepAllowance.getReadOnly());
						}
					}

					// Add ProcessStepDossiserPart
					/*
					 * for (long dossierPartId : dossiserParts) {
					 * ProcessStepDossierPartLocalServiceUtil.addPSDP(
					 * step.getProcessStepId(), dossierPartId); }
					 */
					for (ProcessStepDossierPart stepDossierPart : stepDossiers) {
						ProcessStepDossierPartLocalServiceUtil.addPSDP(
						    stepDossierPart.getProcessStepId(),
						    stepDossierPart.getDossierPartId());
					}
				}

				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL +
					    "#_15_WAR_opencpsportlet_tab=_15_WAR_opencpsportlet_step");
				}
			}
			else {
				// TODO: Update validator here

				// Update ProcessStep
				ProcessStep step =
				    ProcessStepLocalServiceUtil.updateStep(
				        processStepId, serviceProcessId, stepName, sequenceNo,
				        dossierStatus, daysDuration, referenceDossierPartId,
				        externalAppUrl, serviceContext);


				//StepAllowanceLocalServiceUtil.removeProcessStepByProcessId(step.getProcessStepId());

				List<StepAllowance> stepAllowances =
				    ProcessUtils.getStepAllowance(
				        actionRequest, step.getProcessStepId());

				List<ProcessStepDossierPart> stepDossiers =
							    ProcessUtils.getStepDossiers(
							        actionRequest, step.getProcessStepId());

				List<StepAllowance> before = StepAllowanceLocalServiceUtil.getByProcessStep(step.getProcessStepId());
				
				List<ProcessStepDossierPart> beforeStepDossier = ProcessStepDossierPartLocalServiceUtil.getByStep(step.getProcessStepId());
				
				//Remove ProcessStepDossier
				
				List<ProcessStepDossierPart> removeDossier = ProcessUtils.getStepDossierRemove(beforeStepDossier, stepDossiers);
				
				ProcessStepDossierPartLocalServiceUtil.removeStepDossier(removeDossier);
				
				
				// Remove StepAllowance
				List<StepAllowance> removeStep = ProcessUtils.getStepAllowanceRemove(before, stepAllowances);
				
				
				StepAllowanceLocalServiceUtil.removeProcessStepByProcessId(removeStep);

				for (StepAllowance stepAllowance : stepAllowances) {

					if (stepAllowance.getRoleId() == 0) {
						continue;
					}

					if (stepAllowance.getStepAllowanceId() == 0) {
						StepAllowanceLocalServiceUtil.addAllowance(
						    step.getProcessStepId(), stepAllowance.getRoleId(),
						    stepAllowance.getReadOnly());
					}
					else {
						StepAllowanceLocalServiceUtil.updateAllowance(
						    stepAllowance.getStepAllowanceId(),
						    stepAllowance.getRoleId(),
						    stepAllowance.getReadOnly());
					}
				}
				
				//Add dossierPart
				for (ProcessStepDossierPart stepDossierPart : stepDossiers) {
					ProcessStepDossierPartLocalServiceUtil.addPSDP(
					    stepDossierPart.getProcessStepId(),
					    stepDossierPart.getDossierPartId());
				}

				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL +
					    "#_15_WAR_opencpsportlet_tab=_15_WAR_opencpsportlet_step");
				}
				
			}
		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}
	
	/**
	 * @param processId
	 * @return
	 */
	private boolean isRemoveProcessStep(long processId) {
		//TODO: implement in here
		
		return true;
	}
	
	/**
	 * @param processWorkflowId
	 * @return
	 */
	private boolean isRemoveProcessWorkflow(long processWorkflowId) {
		//TODO: implement in here
		return true;
	}


	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateProcess(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long serviceProcessId =
		    ParamUtil.getLong(actionRequest, "serviceProcessId");

		String processNo = ParamUtil.getString(actionRequest, "processNo");

		String processName = ParamUtil.getString(actionRequest, "processName");

		String description = ParamUtil.getString(actionRequest, "description");

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String returnURL = ParamUtil.getString(actionRequest, "returnURL");
		
		long dossierTemplateId = ParamUtil.getLong(actionRequest, "dossierTemplateId");
		
		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (serviceProcessId <= 0) {
				// TODO: Update validator

				// Add ServiceProcess
				ServiceProcessLocalServiceUtil.addProcess(
				    processNo, processName, description, dossierTemplateId, serviceContext);
				// Redirect page

				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}

			}
			else {
				// TODO: Update validator here


				// Update ServiceProcess

				ServiceProcessLocalServiceUtil.updateProcess(
				    serviceProcessId, processNo, processName, dossierTemplateId, description);

				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}

		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}
	
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws NoSuchServiceProcessException
	 * @throws SystemException
	 * @throws IOException
	 */
	public void deleteProcess(ActionRequest actionRequest, ActionResponse actionResponse)
					throws NoSuchServiceProcessException, SystemException, IOException {
		long serviceProcessId = ParamUtil.getLong(actionRequest, "serviceProcessId");
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		
		List<ProcessStep> processSteps = new ArrayList<ProcessStep>();
		List<ServiceInfoProcess> infoProcesses = new ArrayList<ServiceInfoProcess>();
		List<ProcessWorkflow> processWorkflows = new ArrayList<ProcessWorkflow>();
		
		try {
			processSteps = ProcessStepLocalServiceUtil.getStepByProcess(serviceProcessId);
		}
		catch (Exception e) {
		}
		
		try {
			infoProcesses = ServiceInfoProcessLocalServiceUtil
							.getServiceInfoProcessByProcessId(serviceProcessId); 
		}
		catch (Exception e) {
		}
		
		try {
			processWorkflows = ProcessWorkflowLocalServiceUtil
							.getWorkFlowByProcess(serviceProcessId);
		}
		catch (Exception e) {
		}
		
		if(processSteps.isEmpty() && infoProcesses.isEmpty() && processWorkflows.isEmpty()) {
			ServiceProcessLocalServiceUtil.deleteProcess(serviceProcessId);
			
			SessionMessages.add(actionRequest, ProcessMgtUtil.DEL_PROCESS_SUCC);
		} 
		
		else if(!processSteps.isEmpty()) {
			SessionErrors.add(actionRequest, ProcessMgtUtil.DELERR_EXIST_STEP);
		} 
		
		else if(!infoProcesses.isEmpty()) {
			SessionErrors.add(actionRequest, ProcessMgtUtil.DELERR_EXIST_SERVICE_CONFIG);
		}
		
		else if(!processWorkflows.isEmpty()) {
			SessionErrors.add(actionRequest, ProcessMgtUtil.DELERR_EXIST_WORKFLOW);
		}
		
		if(Validator.isNotNull(redirectURL)) {
			actionResponse.sendRedirect(redirectURL);
		}
	}

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {
		

		long serviceProcessId =
		    ParamUtil.getLong(renderRequest, "serviceProcessId");
		
		long stepAllowanceId = ParamUtil.getLong(renderRequest, "stepAllowanceId");
		
		long processStepId = ParamUtil.getLong(renderRequest, "processStepId");
		
		long processWorkflowId = ParamUtil.getLong(renderRequest, "processWorkflowId");

		ServiceProcess serviceProcess = null;
		
		StepAllowance stepAllowance = null;
		
		ProcessStep processStep = null;
		
		ProcessWorkflow processWorkflow = null;

		try {
			serviceProcess =
			    ServiceProcessLocalServiceUtil.fetchServiceProcess(serviceProcessId);
			
			stepAllowance = StepAllowanceLocalServiceUtil.fetchStepAllowance(stepAllowanceId);
			
			processStep = ProcessStepLocalServiceUtil.fetchProcessStep(processStepId);
			
			processWorkflow = ProcessWorkflowLocalServiceUtil.fetchProcessWorkflow(processWorkflowId);
		}
		catch (Exception e) {
			_log.error(e);
		}

		renderRequest.setAttribute(
		    WebKeys.SERVICE_PROCESS_ENTRY, serviceProcess);
		
		renderRequest.setAttribute(WebKeys.STEP_ALLOWANCE_ENTRY, stepAllowance);
		
		renderRequest.setAttribute(WebKeys.PROCESS_STEP_ENTRY, processStep);
		
		renderRequest.setAttribute(WebKeys.WORKFLOW_ENTRY, processWorkflow);
		
		super.render(renderRequest, renderResponse);

	}

	private Log _log = LogFactoryUtil.getLog(ProcessMgtAdminPortlet.class);

}
