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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.backend.util.DossierNoGenerator;
import org.opencps.backend.util.PaymentRequestGenerator;
import org.opencps.backend.util.PaymentUrlGenerator;
import org.opencps.dossiermgt.bean.AccountBean;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierLog;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierLogLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.dossiermgt.util.ActorBean;
import org.opencps.holidayconfig.util.HolidayUtils;
import org.opencps.notificationmgt.message.SendNotificationMessage;
import org.opencps.notificationmgt.utils.NotificationEventKeys;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.model.impl.PaymentFileImpl;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.StepAllowance;
import org.opencps.processmgt.model.impl.ProcessStepImpl;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.StepAllowanceLocalServiceUtil;
import org.opencps.processmgt.util.ProcessMgtUtil;
import org.opencps.processmgt.util.ProcessUtils;
import org.opencps.usermgt.model.Employee;
import org.opencps.util.AccountUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author khoavd
 */
public class BackOfficeProcessEngine implements MessageListener {

	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doRecevie(message);
	}

	private void _doRecevie(Message message) {

		SendToEngineMsg toEngineMsg = (SendToEngineMsg) message.get("msgToEngine");

		List<SendNotificationMessage> lsNotification = new ArrayList<SendNotificationMessage>();

		List<String> employEvents = new ArrayList<String>();
		List<String> citizenEvents = new ArrayList<String>();

		String actionName = StringPool.BLANK;
		String stepName = StringPool.BLANK;
		ProcessOrder processOrder = null;
		long curStepId = 0;
		long processStepId = 0;

		long ownerUserId = 0;
		long ownerOrganizationId = 0;

		Dossier dossier = BackendUtils.getDossier(toEngineMsg.getDossierId());

		long serviceInfoId = 0;
		long dossierTemplateId = 0;
		String govAgencyCode = StringPool.BLANK;
		String govAgencyName = StringPool.BLANK;
		long govAgencyOrganizationId = 0;
		long serviceProcessId = 0;

		long actionUserId = toEngineMsg.getActionUserId();

		ActorBean actorBean = new ActorBean(toEngineMsg.getActorType(), actionUserId);

		if (Validator.isNotNull(dossier)) {
			serviceInfoId = dossier.getServiceInfoId();
			dossierTemplateId = dossier.getDossierTemplateId();
			govAgencyCode = dossier.getGovAgencyCode();
			govAgencyName = dossier.getGovAgencyName();
			govAgencyOrganizationId = dossier.getGovAgencyOrganizationId();

			try {

				ServiceConfig serviceConfig =
					ServiceConfigLocalServiceUtil.getServiceConfigByG_S_G(
						toEngineMsg.getGroupId(), serviceInfoId, govAgencyCode);
				serviceProcessId = serviceConfig.getServiceProcessId();

			}
			catch (Exception e) {
				_log.error(e);
			}
		}

		SendToBackOfficeMsg toBackOffice = new SendToBackOfficeMsg();

		toBackOffice.setSubmitDateTime(toEngineMsg.getActionDatetime());

		toBackOffice.setActor(actorBean.getActor());
		toBackOffice.setActorId(actorBean.getActorId());
		toBackOffice.setActorName(actorBean.getActorName());

		long processWorkflowId = toEngineMsg.getProcessWorkflowId();

		long processOrderId = toEngineMsg.getProcessOrderId();

		try {
			if (Validator.isNull(processOrderId)) {
				// Check processOrder
				processOrder =
					BackendUtils.getProcessOrder(
						toEngineMsg.getDossierId(), toEngineMsg.getFileGroupId());

				if (Validator.isNull(processOrder)) {

					// Init process order
					processOrder =
						ProcessOrderLocalServiceUtil.initProcessOrder(
							toEngineMsg.getUserId(), toEngineMsg.getCompanyId(),
							toEngineMsg.getGroupId(), serviceInfoId, dossierTemplateId,
							govAgencyCode, govAgencyName, govAgencyOrganizationId,
							serviceProcessId, toEngineMsg.getDossierId(),
							toEngineMsg.getFileGroupId(), toEngineMsg.getProcessWorkflowId(),
							toEngineMsg.getActionDatetime(), StringPool.BLANK, StringPool.BLANK,
							StringPool.BLANK, 0, 0, 0, PortletConstants.DOSSIER_STATUS_SYSTEM);

					// Add DossierLog for create ProcessOrder

					ActorBean actorBeanSys = new ActorBean(0, 0);

					DossierLog dossierLog =
						DossierLogLocalServiceUtil.addDossierLog(
							toEngineMsg.getUserId(), toEngineMsg.getGroupId(),
							toEngineMsg.getCompanyId(), toEngineMsg.getDossierId(),
							toEngineMsg.getFileGroupId(), PortletConstants.DOSSIER_STATUS_SYSTEM,
							PortletConstants.DOSSIER_ACTION_CREATE_PROCESS_ORDER,
							PortletConstants.DOSSIER_ACTION_CREATE_PROCESS_ORDER, new Date(), 0, 0,
							actorBeanSys.getActor(), actorBeanSys.getActorId(),
							actorBeanSys.getActorName(), BackOfficeProcessEngine.class.getName() +
								".createProcessOrder()", processOrder.getProcessOrderId(), 0, false);

					toBackOffice.setDossierLogOId(dossierLog.getOId());
				}

				processOrderId = processOrder.getProcessOrderId();

			}
			else {
				// Find process order by processOrderId
				processOrder = ProcessOrderLocalServiceUtil.fetchProcessOrder(processOrderId);

				processOrderId = processOrder.getProcessOrderId();

				curStepId = processOrder.getProcessStepId();
			}

			long assignToUserId = toEngineMsg.getAssignToUserId();

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

			if (Validator.isNull(assignToUserId)) {

				assignToUserId =
					ProcessMgtUtil.getAssignUser(
						processWorkflow.getProcessWorkflowId(), processOrderId,
						processWorkflow.getPostProcessStepId());
			}

			// Do Workflow

			if (Validator.isNotNull(processWorkflow)) {

				_log.info("=====processWorkflow:" + processWorkflow);
				actionName = processWorkflow.getActionName();

				processStepId = processWorkflow.getPostProcessStepId();

				long changeStepId = processWorkflow.getPostProcessStepId();

				ProcessStep changeStep = ProcessUtils.getPostProcessStep(changeStepId);

				ProcessStep currStep = new ProcessStepImpl();

				if (curStepId != 0) {
					currStep = ProcessUtils.getPostProcessStep(curStepId);
					stepName = currStep.getStepName();
				}
				_log.info("=====changeStep.getProcessStepId():" + changeStep.getProcessStepId());
				_log.info("=====currStep.getProcessStepId():" + currStep.getProcessStepId());

				// Add noti's events

				if (changeStep.getDossierStatus().contains(
					PortletConstants.DOSSIER_STATUS_RECEIVING)) {
					// dossier receiving
					employEvents.add(NotificationEventKeys.OFFICIALS.EVENT1);

					citizenEvents.add(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT1);
				}

				if (currStep.getDossierStatus().contains(PortletConstants.DOSSIER_STATUS_WAITING)) {
					// Dossier add new documents
					employEvents.add(NotificationEventKeys.OFFICIALS.EVENT2);
				}

				if ((Validator.isNotNull(currStep.getDossierStatus()) ||
					!currStep.getDossierStatus().contains(PortletConstants.DOSSIER_STATUS_WAITING) || !currStep.getDossierStatus().contains(
					PortletConstants.DOSSIER_STATUS_RECEIVING)) &&
					changeStep.getDossierStatus().contains(
						PortletConstants.DOSSIER_STATUS_PROCESSING)) {

					employEvents.add(NotificationEventKeys.OFFICIALS.EVENT6);
				}

				if (currStep.getDossierStatus().contains(PortletConstants.DOSSIER_STATUS_RECEIVING) &&
					changeStep.getDossierStatus().contains(
						PortletConstants.DOSSIER_STATUS_PROCESSING)) {

					citizenEvents.add(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT2);

				}

				if (currStep.getDossierStatus().contains(PortletConstants.DOSSIER_STATUS_RECEIVING) &&
					!changeStep.getDossierStatus().contains(
						PortletConstants.DOSSIER_STATUS_PROCESSING) &&
					processWorkflow.getPostProcessStepId() == 0 &&
					processWorkflow.getIsFinishStep()) {

					citizenEvents.add(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT3);

				}

				if (changeStep.getDossierStatus().contains(PortletConstants.DOSSIER_STATUS_WAITING)) {

					citizenEvents.add(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT4);

				}

				if (changeStep.getDossierStatus().contains(PortletConstants.DOSSIER_STATUS_DONE)) {

					citizenEvents.add(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT6);

				}

				String changeStatus = StringPool.BLANK;

				boolean isResubmit = false;

				if (changeStepId != 0) {

					// Set Receive Date

					if (currStep.getDossierStatus().contains(
						PortletConstants.DOSSIER_STATUS_RECEIVING) &&
						changeStep.getDossierStatus().contains("processing")) {
						toBackOffice.setReceiveDatetime(new Date());
					}

					if (Validator.isNotNull(changeStep)) {
						changeStatus = changeStep.getDossierStatus();

						if (Validator.equals(
							changeStep.getDossierStatus(), PortletConstants.DOSSIER_STATUS_WAITING)) {

							isResubmit = true;
						}

					}
				}
				else {
					changeStatus = PortletConstants.DOSSIER_STATUS_DONE;
				}

				int syncStatus = 0;

				if (!changeStatus.contentEquals(toEngineMsg.getDossierStatus())) {
					syncStatus = 2;
				}

				// Update process order to SYSTEM
				ProcessOrderLocalServiceUtil.updateProcessOrderStatus(
					processOrderId, PortletConstants.DOSSIER_STATUS_SYSTEM);

				// Update process order
				ProcessOrderLocalServiceUtil.updateProcessOrder(
					processOrderId, processStepId, processWorkflow.getProcessWorkflowId(),
					toEngineMsg.getActionUserId(), toEngineMsg.getActionDatetime(),
					toEngineMsg.getActionNote(), assignToUserId, stepName, actionName, 0, 0,
					PortletConstants.DOSSIER_STATUS_SYSTEM);

				toBackOffice.setStepName(stepName);
				toBackOffice.setProcessWorkflowId(processWorkflow.getProcessWorkflowId());

				toBackOffice.setProcessOrderId(processOrderId);
				toBackOffice.setDossierId(toEngineMsg.getDossierId());
				toBackOffice.setFileGroupId(toEngineMsg.getFileGroupId());
				toBackOffice.setDossierStatus(changeStatus);
				toBackOffice.setSyncStatus(syncStatus);

				if (changeStatus.equals(PortletConstants.DOSSIER_STATUS_WAITING)) {
					toBackOffice.setRequestCommand(WebKeys.DOSSIER_LOG_RESUBMIT_REQUEST);
				}

				toBackOffice.setActionInfo(processWorkflow.getActionName());
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

						toBackOffice.setReceptionNo(DossierNoGenerator.genaratorNoReception(
							pattern, toEngineMsg.getDossierId()));
						// Add log create dossier

					}
					else {
						toBackOffice.setReceptionNo(dossier.getReceptionNo());
					}
				}
				else {
					toBackOffice.setReceptionNo(toEngineMsg.getReceptionNo());
				}

				if (processWorkflow.getIsFinishStep()) {
					toBackOffice.setFinishDatetime(new Date());
				}

				toBackOffice.setCompanyId(toEngineMsg.getCompanyId());
				toBackOffice.setGovAgencyCode(govAgencyCode);

				toBackOffice.setUserActorAction(toEngineMsg.getActionUserId());

				if (dossier.getOwnerOrganizationId() != 0) {
					ownerUserId = 0;
					ownerOrganizationId = dossier.getOwnerOrganizationId();
				}
				else {
					ownerUserId = dossier.getUserId();
				}

				boolean isPayment = false;

				PaymentFile paymentFile = new PaymentFileImpl();

				// Update Paying
				if (processWorkflow.getRequestPayment()) {

					int totalPayment =
						PaymentRequestGenerator.getTotalPayment(
							processWorkflow.getPaymentFee(), dossier.getDossierId());

					List<String> paymentMethods =
						PaymentRequestGenerator.getPaymentMethod(processWorkflow.getPaymentFee());

					String paymentOptions = StringUtil.merge(paymentMethods);

					List<String> paymentMessages =
						PaymentRequestGenerator.getMessagePayment(processWorkflow.getPaymentFee());

					String paymentName =
						(paymentMessages.size() != 0) ? paymentMessages.get(0) : StringPool.BLANK;

					paymentFile =
						PaymentFileLocalServiceUtil.addPaymentFile(
							toEngineMsg.getDossierId(), toEngineMsg.getFileGroupId(), ownerUserId,
							ownerOrganizationId, govAgencyOrganizationId, paymentName, new Date(),
							(double) totalPayment, paymentName, StringPool.BLANK, paymentOptions);

					if (paymentMethods.contains(PaymentRequestGenerator.PAY_METHOD_KEYPAY)) {

						paymentFile =
								PaymentUrlGenerator.generatorPayURL(
								processWorkflow.getGroupId(), govAgencyOrganizationId,
								paymentFile.getPaymentFileId(), processWorkflow.getPaymentFee(),
								toEngineMsg.getDossierId());

					}

					citizenEvents.add(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT5);

					isPayment = true;

					toBackOffice.setRequestCommand(WebKeys.DOSSIER_LOG_PAYMENT_REQUEST);
					toBackOffice.setPaymentFile(paymentFile);

					Locale vnLocale = new Locale("vi", "VN");

					NumberFormat vnFormat = NumberFormat.getCurrencyInstance(vnLocale);

					// setPayment message in pattern in message Info

					StringBuffer sb = new StringBuffer();

					sb.append(paymentMessages.get(0));
					sb.append(StringPool.SPACE);
					sb.append(StringPool.OPEN_PARENTHESIS);
					sb.append(vnFormat.format(totalPayment));
					sb.append(StringPool.CLOSE_PARENTHESIS);
					sb.append(StringPool.SEMICOLON);
					sb.append(toEngineMsg.getActionNote());

					toBackOffice.setMessageInfo(sb.toString());

				}
				else {
					toBackOffice.setRequestPayment(0);

					toBackOffice.setMessageInfo(toEngineMsg.getActionNote());
				}

				toBackOffice.setPayment(isPayment);
				toBackOffice.setResubmit(isResubmit);
				toBackOffice.setEstimateDatetime(toEngineMsg.getEstimateDatetime());
				//TODO 
				//receiveDateTime alway set???
//				toBackOffice.setReceiveDatetime(toEngineMsg.getReceiveDate());

				long preProcessStepId = -1;
				String autoEvent = StringPool.BLANK;
				Date estimateDatetime = null;

				preProcessStepId = processWorkflow.getPreProcessStepId();
				autoEvent = processWorkflow.getAutoEvent();

				_log.info("=====preProcessStepId:" + preProcessStepId);
				_log.info("=====autoEvent:" + autoEvent);
				_log.info("=====dossier.getDossierStatus():" + dossier.getDossierStatus());
				_log.info("=====processWorkflow.getGenerateDeadline():" +
					processWorkflow.getGenerateDeadline());
				

				if (preProcessStepId == 0 &&
					autoEvent.equals(WebKeys.AUTO_EVENT_SUBMIT) &&
					processWorkflow.getGenerateDeadline() &&
					changeStep.getDossierStatus().contains(
						PortletConstants.DOSSIER_STATUS_RECEIVING) &&
					currStep.getDossierStatus().equals(StringPool.BLANK)) {

					estimateDatetime =
						HolidayUtils.getEndDate(new Date(), processWorkflow.getDeadlinePattern());

					toBackOffice.setEstimateDatetime(estimateDatetime);

				}
				_log.info("======estimateDatetime:" + estimateDatetime);
				_log.info("citizenEvents:" + citizenEvents);
				_log.info("employEvents:" + employEvents);

				lsNotification =
					getListNoties(
						citizenEvents, employEvents, dossier.getUserId(), dossier.getGroupId(),
						assignToUserId, processWorkflow, dossier.getDossierId(),
						paymentFile.getPaymentFileId(), processOrderId);

				toBackOffice.setListNotifications(lsNotification);

				Message sendToBackOffice = new Message();

				sendToBackOffice.put("toBackOffice", toBackOffice);

				MessageBusUtil.sendMessage("opencps/backoffice/out/destination", sendToBackOffice);

			}
			else {
				// Send message to backoffice/out/destination
				toBackOffice.setProcessOrderId(processOrderId);
				toBackOffice.setDossierId(toEngineMsg.getDossierId());
				toBackOffice.setFileGroupId(toEngineMsg.getFileGroupId());
				toBackOffice.setDossierStatus(PortletConstants.DOSSIER_STATUS_ERROR);
				toBackOffice.setCompanyId(toEngineMsg.getCompanyId());
				toBackOffice.setGovAgencyCode(govAgencyCode);
				toBackOffice.setReceptionNo(toEngineMsg.getReceptionNo());
				toBackOffice.setUserActorAction(toEngineMsg.getActionUserId());
				toBackOffice.setStepName(stepName);

				citizenEvents.add(NotificationEventKeys.ADMINTRATOR.EVENT1);


				lsNotification =
					getListNoties(
						citizenEvents, employEvents, dossier.getUserId(), dossier.getGroupId(),
						assignToUserId, processWorkflow, dossier.getDossierId(), 0, processOrderId);

				toBackOffice.setListNotifications(lsNotification);

				Message sendToBackOffice = new Message();

				sendToBackOffice.put("toBackOffice", toBackOffice);

				MessageBusUtil.sendMessage("opencps/backoffice/out/destination", sendToBackOffice);

			}

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	private List<SendNotificationMessage> getListNoties(
		List<String> citizenEvents, List<String> employEvents, long citizenUserId, long groupId,
		long assignToUserId, ProcessWorkflow processWorkflow, long dossierId, long paymentFileId,
		long processOrderId) {

		List<SendNotificationMessage> ls = new ArrayList<SendNotificationMessage>();

		AccountBean accountBean = AccountUtil.getAccountBean(citizenUserId, groupId, null);

		Citizen citizen = null;
		Business bussines = null;

		if (accountBean.isCitizen()) {
			citizen = (Citizen) accountBean.getAccountInstance();
		}
		if (accountBean.isBusiness()) {
			bussines = (Business) accountBean.getAccountInstance();
		}

		for (String event : citizenEvents) {


			SendNotificationMessage notiMsg = new SendNotificationMessage();

			notiMsg.setDossierId(dossierId);
			notiMsg.setNotificationEventName(event);
			notiMsg.setProcessOrderId(processOrderId);
			notiMsg.setPaymentFileId(paymentFileId);
			notiMsg.setType("SMS, INBOX, EMAIL");

			SendNotificationMessage.InfoList info = new SendNotificationMessage.InfoList();

			info.setGroupId(groupId);

			List<SendNotificationMessage.InfoList> infoList =
				new ArrayList<SendNotificationMessage.InfoList>();

			infoList.add(info);

			notiMsg.setInfoList(infoList);

			if (Validator.isNotNull(citizen)) {
				info.setUserId(citizen.getUserId());
				info.setUserMail(citizen.getEmail());
				info.setUserPhone(citizen.getTelNo());
				info.setFullName(citizen.getFullName());
			}

			if (Validator.isNotNull(bussines)) {
				info.setUserId(bussines.getUserId());
				info.setUserMail(bussines.getEmail());
				info.setUserPhone(bussines.getTelNo());
				info.setFullName(bussines.getName());

			}

			if (event.contains(NotificationEventKeys.USERS_AND_ENTERPRISE.EVENT5)) {
				info.setGroup(NotificationEventKeys.GROUP3);
				Locale vnLocale = new Locale("vi", "VN");

				notiMsg.setNotificationContent(LanguageUtil.get(
					vnLocale, "phieu-yeu-cau-thanh-toan"));

			}
			else {
				info.setGroup(NotificationEventKeys.GROUP2);
				notiMsg.setNotificationContent(processWorkflow.getActionName());

			}

			ls.add(notiMsg);

		}

		for (String employEvent : employEvents) {

			SendNotificationMessage notiMsg = new SendNotificationMessage();

			notiMsg.setDossierId(dossierId);
			notiMsg.setNotificationEventName(employEvent);
			notiMsg.setProcessOrderId(processOrderId);
			notiMsg.setPaymentFileId(paymentFileId);
			notiMsg.setType("SMS, INBOX, EMAIL");

			if (assignToUserId != 0) {

				SendNotificationMessage.InfoList infoEmploy =
					new SendNotificationMessage.InfoList();

				List<SendNotificationMessage.InfoList> infoListEmploy =
					new ArrayList<SendNotificationMessage.InfoList>();

				AccountBean accountEmploy =
					AccountUtil.getAccountBean(assignToUserId, groupId, null);


				if (accountEmploy.isEmployee()) {

					Employee employee = (Employee) accountEmploy.getAccountInstance();

					infoEmploy.setUserId(employee.getMappingUserId());
					infoEmploy.setUserMail(employee.getEmail());
					infoEmploy.setUserPhone(employee.getTelNo());
					infoEmploy.setGroupId(groupId);
					infoEmploy.setFullName(employee.getFullName());
				}

				infoListEmploy.add(infoEmploy);

				if (employEvent.contains(NotificationEventKeys.OFFICIALS.EVENT3)) {
					infoEmploy.setGroup(NotificationEventKeys.GROUP4);

					Locale vnLocale = new Locale("vi", "VN");
					notiMsg.setNotificationContent(LanguageUtil.get(
						vnLocale, "phieu-yeu-cau-thanh-toan-moi-thuc-hien"));

				}
				else {
					infoEmploy.setGroup(NotificationEventKeys.GROUP1);
					notiMsg.setNotificationContent(processWorkflow.getActionName());
				}

				notiMsg.setInfoList(infoListEmploy);

				ls.add(notiMsg);

			}
			else {
				List<SendNotificationMessage.InfoList> infoListEmploy =
					new ArrayList<SendNotificationMessage.InfoList>();

				List<Employee> employees = getListEmploy(processWorkflow,groupId);

				for (Employee employee : employees) {

					SendNotificationMessage.InfoList infoEmploy =
						new SendNotificationMessage.InfoList();

					infoEmploy.setUserId(employee.getMappingUserId());
					infoEmploy.setUserMail(employee.getEmail());
					infoEmploy.setUserPhone(employee.getTelNo());
					infoEmploy.setGroupId(groupId);
					infoEmploy.setFullName(employee.getFullName());

					if (employEvent.contains(NotificationEventKeys.OFFICIALS.EVENT3)) {
						infoEmploy.setGroup(NotificationEventKeys.GROUP4);
					}
					else {
						infoEmploy.setGroup(NotificationEventKeys.GROUP1);
					}
					//processWorkflow --> processStepId
					// + roleId --> check readyOnly
					// --> add sendmail if readyOnly = 0
					boolean flag = false;
					try {
						List<Role> listRole = RoleLocalServiceUtil.getUserRoles(employee.getMappingUserId());
						for (Role role : listRole) {
							StepAllowance stepAllowance = StepAllowanceLocalServiceUtil.getStepAllowance(processWorkflow.getPostProcessStepId(), role.getRoleId());
							if(Validator.isNotNull(stepAllowance) && !stepAllowance.getReadOnly()){
								flag = true;
								break;
							}
						}
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(flag){
						infoListEmploy.add(infoEmploy);
					}
					
				}

				if (employEvent.contains(NotificationEventKeys.OFFICIALS.EVENT3)) {

					Locale vnLocale = new Locale("vi", "VN");

					notiMsg.setNotificationContent(LanguageUtil.get(
						vnLocale, "phieu-yeu-cau-thanh-toan-moi-thuc-hien"));
				}
				else {
					notiMsg.setNotificationContent(processWorkflow.getActionName());
				}

				notiMsg.setInfoList(infoListEmploy);

				ls.add(notiMsg);

			}
		}
		return ls;
	}

	/**
	 * @param processWorkflow
	 * @param assignToUserId
	 * @return
	 */
	private List<Employee> getListEmploy(ProcessWorkflow processWorkflow,long groupId) {

		List<Employee> ls = new ArrayList<>();

		try {
			List<User> users =
				ProcessUtils.getAssignUsers(processWorkflow.getPostProcessStepId(), 3);

			for (User user : users) {
				AccountBean accountEmploy =
					AccountUtil.getAccountBean(user.getUserId(), groupId, null);

				Employee employee = (Employee) accountEmploy.getAccountInstance();

				ls.add(employee);

			}

		}
		catch (Exception e) {
			
		}

		return ls;

	}

	private Log _log = LogFactoryUtil.getLog(BackOfficeProcessEngine.class);

}