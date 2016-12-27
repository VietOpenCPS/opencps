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

package org.opencps.backend.sync;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.backend.message.SendToCallbackMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierLog;
import org.opencps.dossiermgt.model.impl.DossierLogImpl;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLogLocalServiceUtil;
import org.opencps.dossiermgt.util.ActorBean;
import org.opencps.holidayconfig.util.HolidayCheckUtils;
import org.opencps.jms.SyncServiceContext;
import org.opencps.notificationmgt.message.SendNotificationMessage;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil;
import org.opencps.util.MessageBusKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.SubscriptionSender;
import com.liferay.util.PwdGenerator;

/**
 * @author khoavd
 */
public class SyncFromBackOffice implements MessageListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay
	 * .portal.kernel.messaging.Message)
	 */
	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doRecevie(message);

	}

	private void _doRecevie(Message message) {
		
		

		SendToBackOfficeMsg toBackOffice =
			(SendToBackOfficeMsg) message.get("toBackOffice");
		
		List<SendNotificationMessage> lsNotification = toBackOffice.getListNotifications();

		boolean trustServiceMode =
			BackendUtils.checkServiceMode(toBackOffice.getDossierId());

		if (trustServiceMode) {
			boolean statusUpdate = false;

			try {
				_log.info("Estimate date________________________________________:" + toBackOffice.getEstimateDatetime());
				_log.info("Submit date________________________________________:" + toBackOffice.getSubmitDateTime());
				_log.info("Fisnished date________________________________________:" + toBackOffice.getFinishDatetime());
				_log.info("Receive dateXXXXXXXXXXXXXXXXXXXXXXXX_________________:" + toBackOffice.getReceiveDatetime());
				
				statusUpdate =
					DossierLocalServiceUtil.updateDossierStatus(
						toBackOffice.getDossierId(),
						toBackOffice.getFileGroupId(),
						toBackOffice.getDossierStatus(),
						toBackOffice.getReceptionNo(),
						toBackOffice.getSubmitDateTime(),
						toBackOffice.getEstimateDatetime(),
						toBackOffice.getReceiveDatetime(),
						toBackOffice.getFinishDatetime(),
						toBackOffice.getActor(), toBackOffice.getActorId(),
						toBackOffice.getActorName(),
						toBackOffice.getRequestCommand(),
						toBackOffice.getActionInfo(),
						toBackOffice.getMessageInfo());

				List<WorkflowOutput> workflowOutputs =
					WorkflowOutputLocalServiceUtil.getByProcessWFPostback(
						toBackOffice.getProcessWorkflowId(), true);
				
				_log.info("=====workflowOutputs.size():" + workflowOutputs.size());
				
				// Lat co trang thai dossier file
				DossierFileLocalServiceUtil.updateDossierFileResultSyncStatus(
						0, toBackOffice.getDossierId(),
						PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC,
						PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
						0, workflowOutputs);

				// Update DossierLog

				Dossier dossier =
					DossierLocalServiceUtil.fetchDossier(toBackOffice.getDossierId());

				ActorBean actorBean =
				    new ActorBean(
				        toBackOffice.getActor(), toBackOffice.getActorId());
				
				boolean isPayment = toBackOffice.isPayment();

				boolean isResubmit = toBackOffice.isResubmit();

				if (isPayment) {
					DossierLogLocalServiceUtil.addCommandRequest(
					    dossier.getUserId(), dossier.getGroupId(),
					    dossier.getCompanyId(), toBackOffice.getDossierId(),
					    toBackOffice.getFileGroupId(),
					    toBackOffice.getDossierStatus(),
					    toBackOffice.getActionInfo(),
					    toBackOffice.getMessageInfo(), new Date(), 0,
					    toBackOffice.getSyncStatus(), actorBean.getActor(),
					    actorBean.getActorId(), actorBean.getActorName(),
					    SyncFromBackOffice.class.getName(),
					    WebKeys.DOSSIER_LOG_PAYMENT_REQUEST,
					    toBackOffice.getProcessOrderId(),
					    toBackOffice.getProcessWorkflowId());
				}

				if (isResubmit) {
					DossierLogLocalServiceUtil.addCommandRequest(
						dossier.getUserId(), dossier.getGroupId(),
						dossier.getCompanyId(), toBackOffice.getDossierId(),
						toBackOffice.getFileGroupId(),
						toBackOffice.getDossierStatus(),
						toBackOffice.getActionInfo(),
						toBackOffice.getMessageInfo(), new Date(), 0,
						toBackOffice.getSyncStatus(), actorBean.getActor(),
						actorBean.getActorId(), actorBean.getActorName(),
						SyncFromBackOffice.class.getName(),
						WebKeys.DOSSIER_LOG_RESUBMIT_REQUEST,
					    toBackOffice.getProcessOrderId(),
					    toBackOffice.getProcessWorkflowId());
				}
				
				DossierLog dossierLog = new DossierLogImpl();
				
				dossierLog = DossierLogLocalServiceUtil.addDossierLog(
					dossier.getUserId(), dossier.getGroupId(),
					dossier.getCompanyId(), toBackOffice.getDossierId(),
					toBackOffice.getFileGroupId(),
					toBackOffice.getDossierStatus(),
					toBackOffice.getActionInfo(),
					toBackOffice.getMessageInfo(), new Date(), 0,
					toBackOffice.getSyncStatus(), actorBean.getActor(),
					actorBean.getActorId(), actorBean.getActorName(),
					SyncFromBackOffice.class.getName(),
				    toBackOffice.getProcessOrderId(),
				    toBackOffice.getProcessWorkflowId(), true);

				SendToCallbackMsg toCallBack = new SendToCallbackMsg();

				int dayDelay = HolidayCheckUtils.getDayDelay(toBackOffice.getProcessOrderId(), toBackOffice.getProcessWorkflowId());
				int daysDoing = 0;
				
				toCallBack.setProcessOrderId(toBackOffice.getProcessOrderId());
				toCallBack.setDossierStatus(toBackOffice.getDossierStatus());
				toCallBack.setUserId(toBackOffice.getActorId());
				toCallBack.setGroupId(dossier.getGroupId());
				toCallBack.setCompanyId(dossier.getCompanyId());
				toCallBack.setProcessWorkflowId(toBackOffice.getProcessWorkflowId());
				toCallBack.setActionDatetime(new Date());
				toCallBack.setStepName(toBackOffice.getStepName());
				toCallBack.setActionName(toBackOffice.getActionInfo());
				toCallBack.setActionNote(toBackOffice.getMessageInfo());
				toCallBack.setActionUserId(actorBean.getActorId());
				toCallBack.setDaysDoing(daysDoing);
				toCallBack.setDaysDelay(dayDelay);
				toCallBack.setSyncStatus(statusUpdate ? "ok" : "error");
				toCallBack.setLogId(dossierLog.getDossierLogId());
				
				Message sendToCallBack = new Message();

				sendToCallBack.put("toCallback", toCallBack);

				MessageBusUtil.sendMessage(
					"opencps/backoffice/engine/callback", sendToCallBack);
				
				Message msgNoti = new Message();
				
				msgNoti.put(MessageBusKeys.Message.NOTIFICATIONS, lsNotification);
				
				MessageBusUtil.sendMessage(MessageBusKeys.Destination.NOTIFICATIONS, msgNoti);
				
			}
			catch (Exception e) {
				_log.error(e);
			}

/*			
			if (toBackOffice.getSyncStatus() == 2) {
				sendEmailCustomer(toBackOffice.getDossierId());
			}
*/
		}

	}
	
	
	/**
	 * @param dossierId
	 */
	public void sendEmailCustomer(long dossierId) {
		
		try {
			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);
			
			SyncServiceContext syncServiceContext =
			    new SyncServiceContext(
			        dossier.getCompanyId(), dossier.getGroupId(),
			        dossier.getUserId(), true, true);
			Locale locale = new Locale("vi", "VN");

			
			String fromName = PrefsPropsUtil
			    .getString(dossier.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
			
			String fromAddress = PrefsPropsUtil
			    .getString(dossier.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);

			String toName = dossier.getContactName();
			
			String toAddress = dossier.getContactEmail();

			String subject = PortletPropsValues.SUBJECT_TO_CUSTOMER + LanguageUtil.get(locale, dossier.getDossierStatus());
			
			String emailBody = PortletPropsValues.CONTENT_TO_CUSTOMER;
			
			emailBody = StringUtil.replace(emailBody, "{receptionNo}", Long.toString(dossierId));

			
			emailBody = emailBody + LanguageUtil.get(locale, dossier.getDossierStatus());
			
			SubscriptionSender subscriptionSender = new SubscriptionSender();

			subscriptionSender
			    .setBody(emailBody);
			subscriptionSender
			    .setCompanyId(dossier.getCompanyId());
			
			subscriptionSender
			    .setFrom(fromAddress, fromName);
			subscriptionSender
			    .setHtmlFormat(true);
			subscriptionSender
			    .setMailId("user", dossier.getUserId(), System
			            .currentTimeMillis(),
			        PwdGenerator
			            .getPassword());
			subscriptionSender
			    .setServiceContext(syncServiceContext.getServiceContext());
			subscriptionSender
			    .setSubject(subject);
			subscriptionSender
			    .setUserId(dossier.getUserId());

			subscriptionSender
			    .addRuntimeSubscribers(toAddress, toName);

			subscriptionSender
			    .flushNotificationsAsync();
	        
        }
        catch (Exception e) {
	      _log.error(e);
        }
		
	}
	private Log _log = LogFactoryUtil.getLog(SyncFromBackOffice.class);

}