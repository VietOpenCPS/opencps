/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.backend.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.backend.util.DossierNoGenerator;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierStatus;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.ServiceInfoProcessLocalServiceUtil;
import org.opencps.util.PortletConstants;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.GetterUtil;
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
		activeEngine(message);
	}
	
	private void _doRecevie(Message message) {

		SendToEngineMsg toEngineMsg =
		    (SendToEngineMsg) message.get("msgToEngine");
		
		long curStepId = 0;
		
		
		long processOrderId = toEngineMsg.getProcessOrderId();
		
		try {
			if (Validator.isNull(processOrderId)) {
				
				ProcessOrder processOrder =
				    ProcessOrderLocalServiceUtil.getProcessOrder(
				        toEngineMsg.getDossierId(),
				        toEngineMsg.getFileGroupId());
				
				if (Validator.isNotNull(processOrder)) {
					//Contains process order
					processOrderId = processOrder.getProcessOrderId();
					
					
					
				} else {
					//Not contains process order
					//Create process order
				}
				
			} else {
				//Find process order by processOrderId
				ProcessOrder processOrder =
				    ProcessOrderLocalServiceUtil.getProcessOrder(processOrderId);
				
				
			}
	        
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
		

	}

	private void activeEngine(Message message) {
		
		// General info
		long userId = GetterUtil.getLong(message.get("userId"));
		long groupId = GetterUtil.getLong(message.get("groupId"));
		long companyId = GetterUtil.getLong(message.get("companyId"));
		
		// Dossier Identifi
		long dossierId = GetterUtil.getLong(message.get("dossierId"));
		long fileGroupId = GetterUtil.getLong(message.get("fileGroupId"));
		
		// ProcessOrder
		long processOrderId = GetterUtil.getLong(message.get("processOrderId"), 0);
		
		// Workflow in Action
		long processWorkflowId =
		    GetterUtil.getLong(message.get("processWorkflowId"));
		
		// Step of Action (Onclick in popup)
		long processStepId = GetterUtil.getLong(message.get("processStepId"));
		
		
		// Infomation in dialog
		long actionUserId = GetterUtil.getLong(message.get("actionUserId"));
		long assignToUserId = GetterUtil.getLong(message.get("assignToUserId"));
		Date actionDatetime = GetterUtil.getDate(message.get("actionDatetime"), new SimpleDateFormat("dd/MM/yyyy : HH/mm"));
		String actionNote = GetterUtil.getString(message.get("actionNote"));
		String receptionNo = GetterUtil.getString(message.get("receptionNo"));

		if (Validator.equals(receptionNo, StringPool.BLANK)) {
			receptionNo = DossierNoGenerator.noGenarator();
		}
		
		ProcessOrder processOrder = null;
		
		Dossier dossier = BackendUtils.getDossier(dossierId);
		
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

			}
			
		}

		long currentStep = 0;

		int dossierStatus = 0;

		String stepName = StringPool.BLANK;

		String actionName = StringPool.BLANK;
		
		try {
			
			if(Validator.equals(processOrderId, 0)) {
				ProcessWorkflow firstProcessWorkflow = BackendUtils.getFirstProcessWorkflow(serviceProcessId);
				
				if (Validator.isNotNull(firstProcessWorkflow)) {
					processWorkflowId = firstProcessWorkflow.getProcessWorkflowId();
					
					actionName = firstProcessWorkflow.getActionName();
					
					actionNote = "system-receive";
				}
				
				// Chua co phieu xu ly
				
				//Kiem tra xy ly cho luong chinh hay luong phu
				
				if (fileGroupId == 0) {
					// luong chinh
				
					//Create ProcessOrder
					
					currentStep = BackendUtils.getFristStepLocalService(serviceProcessId);
					
					dossierStatus = BackendUtils.getDossierStatus(currentStep);
					
					processOrder = ProcessOrderLocalServiceUtil.initProcessOrder(
					    userId, companyId, groupId, serviceInfoId,
					    dossierTemplateId, govAgencyCode, govAgencyName,
					    govAgencyOrganizationId, serviceProcessId, dossierId,
					    fileGroupId, processWorkflowId, actionDatetime,
					    StringPool.BLANK, actionName, actionNote, actionUserId,
					    0, 0, dossierStatus);
					
					ProcessOrderLocalServiceUtil.updateInitStep(processOrder.getProcessOrderId(), currentStep);

				} else {
					// luong phu
					
					// kiem tra phieu xu ly luong phu co ton tai?
					processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(dossierId, fileGroupId);
					
					if(Validator.isNull(processOrder)) {
						// Tao phieu xu ly cho luong phu
						processOrder = ProcessOrderLocalServiceUtil.initProcessOrder(
						    userId, companyId, groupId, serviceInfoId,
						    dossierTemplateId, govAgencyCode, govAgencyName,
						    govAgencyOrganizationId, serviceProcessId, dossierId,
						    fileGroupId, processWorkflowId, actionDatetime,
						    StringPool.BLANK, actionName, actionNote, actionUserId,
						    0, 0, 0);
					} else {
						// co phieu cho luong phu, thuc hien update phieu xu ly
						
//						ProcessOrderLocalServiceUtil.updateProcessOrder(
//						    processOrderId, processStepId, processWorkflowId,
//						    actionUserId, actionDatetime, actionNote,
//						    assignToUserId, stepName, actionName, daysDoing,
//						    daysDelay);
					}
				}
				

				
			} else {
				// Co phieu su ly
				
				processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(processOrderId);
				
				// Khac voi System moi xu ly
				if (!Validator.equals(processOrder.getDossierStatus(), PortletConstants.DOSSIER_STATUS_SYSTEM)) {
					
					Date now = new Date();

					ProcessWorkflow currentProcessWorkflow = ProcessWorkflowLocalServiceUtil.getProcessWorkflow(processWorkflowId);
					
					if (Validator.isNotNull(currentProcessWorkflow)) {
						ProcessStep step = ProcessStepLocalServiceUtil.getProcessStep(currentProcessWorkflow.getPreProcessStepId());
						
						stepName = step.getStepName();
						
						actionName = currentProcessWorkflow.getActionName();
						
						dossierStatus = BackendUtils.getDossierStatus(step.getProcessStepId());
					}
					
				
					if (Validator.equals(dossierStatus, PortletConstants.DOSSIER_STATUS_DONE)) {
						assignToUserId = actionUserId;
					}
					
					ProcessOrderLocalServiceUtil.updateProcessOrder(
					    processOrderId, processStepId, processWorkflowId,
					    actionUserId, now, actionNote,
					    assignToUserId, stepName, actionName, 1,
					    0, dossierStatus);
				}
				
			}
			
			
			//Gui yeu cau dong bo ho so len kenh opencps/backoffice/out/destination
			
			
			Message msgBackofficeOutDestination = new Message();
			
			msgBackofficeOutDestination.put("processOrderId", processOrderId);
			msgBackofficeOutDestination.put("dossierId", dossierId);
			msgBackofficeOutDestination.put("fileGroupId", fileGroupId);
			msgBackofficeOutDestination.put("dossierStatus", dossierStatus);
			msgBackofficeOutDestination.put("actionInfo", actionName);
			msgBackofficeOutDestination.put("messageInfo", actionNote);
			msgBackofficeOutDestination.put(
			    "sendResult", processOrder.getProcessStepId() == 0
			        ? StringPool.TRUE : StringPool.FALSE);
			msgBackofficeOutDestination.put(
			    "requestPayment",
			    processOrder.getDossierStatus() == PortletConstants.DOSSIER_STATUS_PAYING
			        ? StringPool.TRUE : StringPool.FALSE);
			msgBackofficeOutDestination.put("updateDatetime", new Date());
			msgBackofficeOutDestination.put("receptionNo", receptionNo);
			msgBackofficeOutDestination.put(
			    "receiveDatetime", dossier.getReceiveDatetime());
			msgBackofficeOutDestination.put("estimateDatetime", new Date());
			msgBackofficeOutDestination.put(
			    "finishDatetime", processOrder.getProcessStepId() == 0
			        ? new Date() : null);
			
			MessageBusUtil.sendMessage("opencps/backoffice/out/destination", msgBackofficeOutDestination);
			
        }
        catch (Exception e) {
	        _log.error(e);
        }
		

	}
	
	private Log _log = LogFactoryUtil.getLog(BackOfficeProcessEngine.class);

}
