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


package org.opencps.backend.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.backend.util.DossierNoGenerator;
import org.opencps.backend.util.KeypayUrlGenerator;
import org.opencps.backend.util.PaymentRequestGenerator;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.SchedulerJobsLocalServiceUtil;
import org.opencps.processmgt.service.ServiceInfoProcessLocalServiceUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author khoavd
 */
public class BackOfficeProcessEngine implements MessageListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay
	 * .portal.kernel.messaging.Message)
	 */
	@Override
	public void receive(Message message)
	    throws MessageListenerException {

		//doReceive(message);
		//activeEngine(message);
		_doRecevie(message);
	}
	
	private void _doRecevie(Message message) {

		SendToEngineMsg toEngineMsg =
		    (SendToEngineMsg) message.get("msgToEngine");

		SendToBackOfficeMsg toBackOffice = new SendToBackOfficeMsg();

		Dossier dossier = BackendUtils.getDossier(toEngineMsg.getDossierId());

		long serviceInfoId = 0;
		long dossierTemplateId = 0;
		String govAgencyCode = StringPool.BLANK;
		String govAgencyName = StringPool.BLANK;
		long govAgencyOrganizationId = 0;
		long serviceProcessId = 0;

		if (Validator.isNotNull(dossier)) {
			serviceInfoId = dossier.getServiceInfoId();
			dossierTemplateId = dossier.getDossierTemplateId();
			govAgencyCode = dossier.getGovAgencyCode();
			govAgencyName = dossier.getGovAgencyName();
			govAgencyOrganizationId = dossier.getGovAgencyOrganizationId();
			
			

			try {
				ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil.getServiceConfigByG_S_G(toEngineMsg.getGroupId(), serviceInfoId, govAgencyCode);
				serviceProcessId = serviceConfig.getServiceProcessId();
/*				    ServiceInfoProcessLocalServiceUtil.getServiceInfo(
				        serviceInfoId).getServiceProcessId();
*/			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		long curStepId = 0;

		long processWorkflowId = toEngineMsg.getProcessWorkflowId();

		long processOrderId = toEngineMsg.getProcessOrderId();

		long processStepId = 0;

		String actionName = StringPool.BLANK;
		String stepName = StringPool.BLANK;
		ProcessOrder processOrder = null;
		try {
			if (Validator.isNull(processOrderId)) {

				processOrder =
				    BackendUtils.getProcessOrder(
				        toEngineMsg.getDossierId(),
				        toEngineMsg.getFileGroupId());

				if (Validator.isNull(processOrder)) {

					// Init process order
					processOrder =
					    ProcessOrderLocalServiceUtil.initProcessOrder(
					        toEngineMsg.getUserId(),
					        toEngineMsg.getCompanyId(),
					        toEngineMsg.getGroupId(), serviceInfoId,
					        dossierTemplateId, govAgencyCode, govAgencyName,
					        govAgencyOrganizationId, serviceProcessId,
					        toEngineMsg.getDossierId(),
					        toEngineMsg.getFileGroupId(),
					        toEngineMsg.getProcessWorkflowId(),
					        toEngineMsg.getActionDatetime(), StringPool.BLANK,
					        StringPool.BLANK, StringPool.BLANK, 0, 0, 0,
					        PortletConstants.DOSSIER_STATUS_SYSTEM);
				}

				processOrderId = processOrder.getProcessOrderId();

			}
			else {
				// Find process order by processOrderId
				processOrder =
				    ProcessOrderLocalServiceUtil.fetchProcessOrder(processOrderId);

				processOrderId = processOrder.getProcessOrderId();

				curStepId = processOrder.getProcessStepId();
			}

			ProcessWorkflow processWorkflow = null;

			// Find workflow
			if (Validator.isNull(processWorkflowId)) {
				processWorkflow =
				    ProcessWorkflowLocalServiceUtil.getProcessWorkflowByEvent(
				        serviceProcessId, toEngineMsg.getEvent(), curStepId);
			}
			else {
				processWorkflow =
				    ProcessWorkflowLocalServiceUtil.fetchProcessWorkflow(processWorkflowId);

			}

			// Do Workflow

			if (Validator.isNotNull(processWorkflow)) {
				actionName = processWorkflow.getActionName();

				processStepId = processWorkflow.getPostProcessStepId();

				if (curStepId != 0) {
					stepName =
					    ProcessStepLocalServiceUtil.fetchProcessStep(curStepId).getStepName();
				}

				long changeStepId = processWorkflow.getPostProcessStepId();
				
				String changeStatus = StringPool.BLANK;

				if (changeStepId != 0 ) {
					ProcessStep changeStep = 
					    ProcessStepLocalServiceUtil.getProcessStep(changeStepId);
					
					if (Validator.isNotNull(changeStep)) {
						changeStatus = changeStep.getDossierStatus();
						
						//Get AutoEvent of change step
						_updateSchedulerJob(
						    processStepId, serviceProcessId,
						    processOrder.getDossierId(),
						    processOrder.getFileGroupId());
					}
					
				} else {
					changeStatus = PortletConstants.DOSSIER_STATUS_DONE;
				}

				// Update process order to SYSTEM
				ProcessOrderLocalServiceUtil.updateProcessOrderStatus(
				    processOrderId, PortletConstants.DOSSIER_STATUS_SYSTEM);

				// Update process order
				ProcessOrderLocalServiceUtil.updateProcessOrder(
				    processOrderId, processStepId,
				    processWorkflow.getProcessWorkflowId(),
				    toEngineMsg.getActionUserId(),
				    toEngineMsg.getActionDatetime(),
				    toEngineMsg.getActionNote(),
				    toEngineMsg.getAssignToUserId(), stepName, actionName, 0,
				    0, PortletConstants.DOSSIER_STATUS_SYSTEM);

				toBackOffice.setProcessOrderId(processOrderId);
				toBackOffice.setDossierId(toEngineMsg.getDossierId());
				toBackOffice.setFileGroupId(toEngineMsg.getFileGroupId());
				toBackOffice.setDossierStatus(changeStatus);

				if (changeStatus.equals(PortletConstants.DOSSIER_STATUS_WAITING)) {
					toBackOffice.setRequestCommand(WebKeys.DOSSIER_LOG_RESUBMIT_REQUEST);
				}

				toBackOffice.setActionInfo(processWorkflow.getActionName());
				toBackOffice.setMessageInfo(toEngineMsg.getActionNote());
				toBackOffice.setSendResult(0);

				if (changeStatus.equals(PortletConstants.DOSSIER_STATUS_PAYING)) {
					toBackOffice.setRequestPayment(1);
				}
				else {
					toBackOffice.setRequestPayment(0);
				}

				toBackOffice.setUpdateDatetime(new Date());

				if (Validator.isNull(toEngineMsg.getReceptionNo())) {
					String pattern = processWorkflow.getReceptionNoPattern();
					if (Validator.isNotNull(pattern) && StringUtil.trim(pattern).length() != 0) {
						
						toBackOffice.setReceptionNo(DossierNoGenerator.genaratorNoReception(pattern)); 
					} else {
						toBackOffice.setReceptionNo(dossier.getReceptionNo());
					}
				}
				else {
					toBackOffice.setReceptionNo(toEngineMsg.getReceptionNo());
				}

				toBackOffice.setReceiveDatetime(new Date());

				toBackOffice.setEstimateDatetime(toEngineMsg.getEstimateDatetime());

				if (processWorkflow.getIsFinishStep()) {
					toBackOffice.setFinishDatetime(new Date());
				}

				toBackOffice.setProcessWorkflowId(processWorkflowId);

				long ownerUserId = 0;
				long ownerOrganizationId = 0;

				if (dossier.getOwnerOrganizationId() != 0) {
					ownerUserId = 0;
					ownerOrganizationId = dossier.getOwnerOrganizationId();
				}
				else {
					ownerUserId = dossier.getUserId();
				}

				// Update Paying
				if (processWorkflow.getRequestPayment()) {

					int totalPayment =
					    PaymentRequestGenerator.getTotalPayment(processWorkflow.getPaymentFee());

					List<String> paymentMethods =
					    PaymentRequestGenerator.getPaymentMethod(processWorkflow.getPaymentFee());

					String paymentOptions = StringUtil.merge(paymentMethods);

					List<String> paymentMessages =
					    PaymentRequestGenerator.getMessagePayment(processWorkflow.getPaymentFee());

					String paymentName =
					    (paymentMessages.size() != 0)
					        ? paymentMessages.get(0) : StringPool.BLANK;

					PaymentFile paymentFile =
					    PaymentFileLocalServiceUtil.addPaymentFile(
					        toEngineMsg.getDossierId(),
					        toEngineMsg.getFileGroupId(), ownerUserId,
					        ownerOrganizationId, govAgencyOrganizationId,
					        paymentName, new Date(),
					        (double) totalPayment, paymentName,
					        StringPool.BLANK, paymentOptions);

					if (paymentMethods.contains(PaymentRequestGenerator.PAY_METHOD_KEYPAY)) {
						KeypayUrlGenerator.generatorKeypayURL(
						    processWorkflow.getGroupId(),
						    govAgencyOrganizationId,
						    paymentFile.getPaymentFileId(),
						    processWorkflow.getPaymentFee(),
						    toEngineMsg.getDossierId());
					}

					toBackOffice.setRequestCommand(WebKeys.DOSSIER_LOG_PAYMENT_REQUEST);

				}
				else {
					toBackOffice.setRequestPayment(0);
				}

				Message sendToBackOffice = new Message();

				sendToBackOffice.put("toBackOffice", toBackOffice);

				MessageBusUtil.sendMessage(
				    "opencps/backoffice/out/destination", sendToBackOffice);
				
				
			}
			else {
				// Send message to backoffice/out/destination
				toBackOffice.setProcessOrderId(processOrderId);
				toBackOffice.setDossierId(toEngineMsg.getDossierId());
				toBackOffice.setFileGroupId(toEngineMsg.getFileGroupId());
				toBackOffice.setDossierStatus(PortletConstants.DOSSIER_STATUS_ERROR);

				Message sendToBackOffice = new Message();

				sendToBackOffice.put("toBackOffice", toBackOffice);

				MessageBusUtil.sendMessage(
				    "opencps/backoffice/out/destination", sendToBackOffice);
			}

		}
		catch (Exception e) {
			_log.error(e);
		}
	}
	
	/**
	 * @param processStepId
	 * @param serviceProcessId
	 * @param dossierId
	 * @param fileGroupId
	 */
	private void _updateSchedulerJob(
	    long processStepId, long serviceProcessId, long dossierId,
	    long fileGroupId) {

		List<ProcessWorkflow> ls = new ArrayList<ProcessWorkflow>();

		try {
			ls =
			    ProcessWorkflowLocalServiceUtil.findInScheduler(
			        processStepId, serviceProcessId);

			for (ProcessWorkflow processWorkflow : ls) {
				SchedulerJobsLocalServiceUtil.addScheduler(
				    dossierId, fileGroupId,
				    processWorkflow.getProcessWorkflowId(),
				    _getSchedulerType(processWorkflow.getAutoEvent()), 0,
				    processWorkflow.getPreCondition());
			}

		}
		catch (Exception e) {

		}
	}

	/**
	 * @param autoEvent
	 * @return
	 */
	private int _getSchedulerType(String autoEvent) {

		int type = 0;

		switch (autoEvent) {
		case WebKeys.AUTO_EVENT_MINUTELY:
			type = 1;

			break;
		case WebKeys.AUTO_EVENT_5_MINUTELY:
			type = 2;

			break;
		case WebKeys.AUTO_EVENT_HOURLY:
			type = 3;

			break;
		case WebKeys.AUTO_EVENT_DAILY:
			type = 4;

			break;

		case WebKeys.AUTO_EVENT_WEEKLY:
			type = 5;

			break;

		default:
			break;
		}
		
		return type;
		
	}

	private Log _log = LogFactoryUtil.getLog(BackOfficeProcessEngine.class);

}
