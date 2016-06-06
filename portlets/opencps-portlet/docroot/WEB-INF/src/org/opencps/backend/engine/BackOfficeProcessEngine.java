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

import java.util.Date;

import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.backend.util.DossierNoGenerator;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
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
				serviceProcessId =
				    ServiceInfoProcessLocalServiceUtil.getServiceInfo(
				        serviceInfoId).getServiceProcessId();
			}
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

		try {
			if (Validator.isNull(processOrderId)) {

				ProcessOrder processOrder =
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
				ProcessOrder processOrder =
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

			// To Workflow

			if (Validator.isNotNull(processWorkflow)) {
				actionName = processWorkflow.getActionName();

				processStepId = processWorkflow.getPostProcessStepId();

				if (curStepId != 0) {
					stepName =
					    ProcessStepLocalServiceUtil.fetchProcessStep(curStepId).getStepName();
				}

				long changeStepId = processWorkflow.getPostProcessStepId();

				ProcessStep changeStep =
				    ProcessStepLocalServiceUtil.getProcessStep(changeStepId);

				String changeStatus = StringPool.BLANK;

				if (Validator.isNotNull(changeStep)) {
					changeStatus = changeStep.getDossierStatus();
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

				if (changeStatus.equals(Integer.toString(PortletConstants.DOSSIER_STATUS_WAITING))) {
					toBackOffice.setRequestCommand(WebKeys.DOSSIER_LOG_RESUBMIT_REQUEST);
				}
/*				if (changeStatus.equals(Integer.toString(PortletConstants.DOSSIER_STATUS_PAYING))) {
					toBackOffice.setRequestCommand(WebKeys.DOSSIER_LOG_PAYMENT_REQUEST);
				}
*/				toBackOffice.setActionInfo(processWorkflow.getActionName());
				toBackOffice.setMessageInfo(toEngineMsg.getActionNote());
				toBackOffice.setSendResult(0);

				if (changeStatus.equals(Integer.toString(PortletConstants.DOSSIER_STATUS_PAYING))) {
					toBackOffice.setRequestPayment(1);
				}
				else {
					toBackOffice.setRequestPayment(0);
				}

				toBackOffice.setUpdateDatetime(new Date());
				

				if (Validator.isNull(toEngineMsg.getReceptionNo())) {
					toBackOffice.setReceptionNo(DossierNoGenerator.genaratorNoReception(processWorkflow.getReceptionNoPattern()));
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
				} else {
					ownerUserId = dossier.getUserId();
				}
				

				// Update Paying
				if (processWorkflow.getRequestPayment()) {
					PaymentFileLocalServiceUtil.addPaymentFile(
					    toEngineMsg.getDossierId(),
					    toEngineMsg.getFileGroupId(),
					    ownerUserId,
					    ownerOrganizationId,
					    govAgencyOrganizationId, changeStep.getStepName(),
					    toEngineMsg.getActionDatetime(),
					    processWorkflow.getPaymentFee(),
					    toEngineMsg.getActionNote(), StringPool.BLANK);
					
					
					toBackOffice.setRequestCommand(WebKeys.DOSSIER_LOG_PAYMENT_REQUEST);

				}
				else {
					toBackOffice.setRequestPayment(0);
				}

				Message sendToBackOffice = new Message();

				sendToBackOffice.put("toBackOffice", toBackOffice);

				MessageBusUtil.sendMessage(
				    "opencps/backoffice/out/destination", sendToBackOffice);
				
			} else {
				//Send message to backoffice/out/destination
				toBackOffice.setProcessOrderId(processOrderId);
				toBackOffice.setDossierId(toEngineMsg.getDossierId());
				toBackOffice.setFileGroupId(toEngineMsg.getFileGroupId());
				toBackOffice.setDossierStatus(Integer.toString(PortletConstants.DOSSIER_STATUS_ERROR));
				
				Message sendToBackOffice = new Message();
				
				sendToBackOffice.put("toBackOffice", toBackOffice);
				
				MessageBusUtil.sendMessage("opencps/backoffice/out/destination", sendToBackOffice);
			}
			
		}
		catch (Exception e) {
			_log.error(e);
		}

	}
	

	private Log _log = LogFactoryUtil.getLog(BackOfficeProcessEngine.class);

}
