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

package org.opencps.backend.exc;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.opencps.backend.message.UserActionMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.jms.context.JMSHornetqContext;
import org.opencps.jms.message.CancelDossierMessage;
import org.opencps.jms.message.SubmitDossierMessage;
import org.opencps.jms.message.SubmitPaymentFileMessage;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;

/**
 * @author khoavd
 */
public class MsgOutFrontOffice implements MessageListener {

	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doReceiveDossier(message);
	}

	private void _doReceiveDossier(Message message) {

		_log.info("####################MsgOutFrontOffice: Started receive message bus");

		UserActionMsg userActionMgs =
			(UserActionMsg) message.get("msgToEngine");

		long dossierId = userActionMgs.getDossierId();

		boolean trustServiceMode = BackendUtils.checkServiceMode(dossierId);

		if (!trustServiceMode) {
			JMSHornetqContext context = null;
			try {

				// JMSContext context =
				// JMSMessageUtil.createProducer(
				// userActionMgs.getCompanyId(),
				// userActionMgs.getGovAgencyCode(), true,
				// WebKeys.JMS_QUEUE_OPENCPS.toLowerCase(),
				// WebKeys.JMS_QUEUE_OPENCPS.toLowerCase(), "remote",
				// "jmscore");

				context =
					JMSMessageUtil.createHornetqProducer(
						userActionMgs.getCompanyId(),
						userActionMgs.getGovAgencyCode(), true,
						WebKeys.JMS_QUEUE_OPENCPS_FRONTOFFICE.toLowerCase(),
						WebKeys.JMS_QUEUE_OPENCPS_FRONTOFFICE.toLowerCase(),
						"remote", "hornetq");

				if (userActionMgs.getAction().equals(WebKeys.ACTION_PAY_VALUE)) {

					SubmitPaymentFileMessage submitPaymentFileMessage =
						new SubmitPaymentFileMessage(context);

					PaymentFile paymentFile =
						PaymentFileLocalServiceUtil.getPaymentFile(userActionMgs.getPaymentFileId());

					submitPaymentFileMessage.sendMessageByHornetq(
						paymentFile, paymentFile.getConfirmFileEntryId() > 0
							? WebKeys.SYNC_PAY_SEND_CONFIRM
							: WebKeys.SYNC_PAY_CONFIRM);

					// TODO add log

					_log.info("####################MsgOutFrontOffice: Sended Synchronized JMSPaymentMessage");

				}
				else if (userActionMgs.getAction().equals(
					WebKeys.ACTION_CANCEL_VALUE)) {

					CancelDossierMessage cancelDossierMessage =
						new CancelDossierMessage(context);
					cancelDossierMessage.sendMessage(
						userActionMgs.getDossierId(),
						userActionMgs.getFileGroupId());
					// TODO add log
					_log.info("####################MsgOutFrontOffice: Sended Synchronized JMSCancelDossierMessage");
				}
				else {
					SubmitDossierMessage submitDossierMessage =
						new SubmitDossierMessage(context);

					submitDossierMessage.sendMessageByHornetq(
						userActionMgs.getDossierId(),
						userActionMgs.getFileGroupId());
					// TODO add log
					_log.info("####################MsgOutFrontOffice: Sended Synchronized JMSDossierMessage");

					// Dong bo trang thai giay to

					DossierFileLocalServiceUtil.updateDossierFileSyncStatus(
						userActionMgs.getUserId(),
						userActionMgs.getDossierId(),
						PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC,
						PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
						0);
				}

			}
			catch (Exception e) {
				_log.error(e);
			}
			finally {
				if (context != null) {
					try {
						context.destroy();
					}
					catch (JMSException e) {
						_log.error(e);
					}
					catch (NamingException e) {
						_log.error(e);
					}
				}
			}
		}

	}

	private Log _log = LogFactoryUtil.getLog(MsgInFrontOffice.class);

}
