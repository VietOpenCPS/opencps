/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.jms.message;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.jms.SyncServiceContext;
import org.opencps.jms.business.SubmitDossier;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.context.JMSHornetqContext;
import org.opencps.jms.context.JMSLocalContext;
import org.opencps.jms.message.body.DossierMsgBody;
import org.opencps.jms.util.JMSMessageBodyUtil;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

/**
 * @author trungnt
 */
public class SubmitDossierMessage {

	public SubmitDossierMessage(JMSContext context) {

		this.setContext(context);

	}

	public SubmitDossierMessage(JMSLocalContext context) {

		this.setLocalContext(context);

	}

	public SubmitDossierMessage(JMSHornetqContext context) {

		this.setHornetqContext(context);
	}

	/**
	 * @param dossierId
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendMessage(long dossierId, long fileGroupId)
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				JMSMessageUtil.createByteMessage(_context);

			long companyId =
				GetterUtil.getLong(_context.getProperties().getProperty(
					WebKeys.JMS_COMPANY_ID));
			long groupId =
				GetterUtil.getLong(
					_context.getProperties().getProperty(WebKeys.JMS_GROUP_ID),
					0L);
			long userId =
				GetterUtil.getLong(
					_context.getProperties().getProperty(WebKeys.JMS_USER_ID),
					0L);

			if (companyId > 0 && groupId > 0 && userId > 0) {
				SyncServiceContext syncServiceContext =
					new SyncServiceContext(
						companyId, groupId, userId, true, true);
				DossierMsgBody dossierMsgBody =
					JMSMessageBodyUtil.getDossierMsgBody(dossierId, fileGroupId);
				dossierMsgBody.setServiceContext(syncServiceContext.getServiceContext());
				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(dossierMsgBody);

				_log.info("####################SubmitDossierMessage: Sending byte message");

				bytesMessage.writeBytes(sender);

				_context.getMessageProducer().send(bytesMessage);

				_log.info("####################SubmitDossierMessage: Finished sending byte message");
			}

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @param dossierId
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendMessageByHornetq(long dossierId, long fileGroupId)
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				JMSMessageUtil.createByteMessage(_hornetqContext);

			long companyId =
				GetterUtil.getLong(_hornetqContext.getProperties().getProperty(
					WebKeys.JMS_COMPANY_ID));
			long groupId =
				GetterUtil.getLong(
					_hornetqContext.getProperties().getProperty(
						WebKeys.JMS_GROUP_ID), 0L);
			long userId =
				GetterUtil.getLong(
					_hornetqContext.getProperties().getProperty(
						WebKeys.JMS_USER_ID), 0L);

			if (companyId > 0 && groupId > 0 && userId > 0) {
				SyncServiceContext syncServiceContext =
					new SyncServiceContext(
						companyId, groupId, userId, true, true);
				DossierMsgBody dossierMsgBody =
					JMSMessageBodyUtil.getDossierMsgBody(dossierId, fileGroupId);
				dossierMsgBody.setServiceContext(syncServiceContext.getServiceContext());
				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(dossierMsgBody);

				_log.info("####################SubmitDossierMessage: Sending byte message");

				bytesMessage.writeBytes(sender);

				_hornetqContext.getMessageProducer().send(bytesMessage);

				_log.info("####################SubmitDossierMessage: Finished sending byte message");
			}

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @param dossier
	 * @throws NamingException
	 * @throws JMSException
	 */
	public void sendMessage(Dossier dossier)
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				JMSMessageUtil.createByteMessage(_context);

			long companyId =
				GetterUtil.getLong(_context.getProperties().getProperty(
					WebKeys.JMS_COMPANY_ID));
			long groupId =
				GetterUtil.getLong(
					_context.getProperties().getProperty(WebKeys.JMS_GROUP_ID),
					0L);
			long userId =
				GetterUtil.getLong(
					_context.getProperties().getProperty(WebKeys.JMS_USER_ID),
					0L);

			if (companyId > 0 && groupId > 0 && userId > 0) {
				SyncServiceContext syncServiceContext =
					new SyncServiceContext(
						companyId, groupId, userId, true, true);
				DossierMsgBody dossierMsgBody =
					JMSMessageBodyUtil.getDossierMsgBody(dossier);
				dossierMsgBody.setServiceContext(syncServiceContext.getServiceContext());
				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(dossierMsgBody);

				_log.info("####################SubmitDossierMessage: Sending byte message");

				bytesMessage.writeBytes(sender);

				_context.getMessageProducer().send(bytesMessage);

				_log.info("####################SubmitDossierMessage: Finished sending byte message");
			}

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @param dossier
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendMessageByHornetq(Dossier dossier)
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				JMSMessageUtil.createByteMessage(_hornetqContext);

			long companyId =
				GetterUtil.getLong(_hornetqContext.getProperties().getProperty(
					WebKeys.JMS_COMPANY_ID));
			long groupId =
				GetterUtil.getLong(
					_hornetqContext.getProperties().getProperty(
						WebKeys.JMS_GROUP_ID), 0L);
			long userId =
				GetterUtil.getLong(
					_hornetqContext.getProperties().getProperty(
						WebKeys.JMS_USER_ID), 0L);

			if (companyId > 0 && groupId > 0 && userId > 0) {
				SyncServiceContext syncServiceContext =
					new SyncServiceContext(
						companyId, groupId, userId, true, true);
				DossierMsgBody dossierMsgBody =
					JMSMessageBodyUtil.getDossierMsgBody(dossier);
				dossierMsgBody.setServiceContext(syncServiceContext.getServiceContext());
				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(dossierMsgBody);

				bytesMessage.writeBytes(sender);

				_hornetqContext.getMessageProducer().send(bytesMessage);
			}

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void receiveMessage()
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				(BytesMessage) _context.getMessageConsumer().receive();

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			DossierMsgBody dossierMsgBody = (DossierMsgBody) object;

			setDossierMsgBody(dossierMsgBody);

			SubmitDossier submitDossier = new SubmitDossier();

			submitDossier.syncDossier(dossierMsgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void receiveMessageByHornetq()
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				(BytesMessage) _hornetqContext.getMessageConsumer().receive();

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			DossierMsgBody dossierMsgBody = (DossierMsgBody) object;

			setDossierMsgBody(dossierMsgBody);

			SubmitDossier submitDossier = new SubmitDossier();

			submitDossier.syncDossier(dossierMsgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void receiveLocalMessage()
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				(BytesMessage) _localContext.getQueueReceiver().receive();

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			DossierMsgBody dossierMsgBody = (DossierMsgBody) object;

			setDossierMsgBody(dossierMsgBody);

			SubmitDossier submitDossier = new SubmitDossier();

			submitDossier.syncDossier(dossierMsgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @param bytesMessage
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void receiveMessage(BytesMessage bytesMessage)
		throws JMSException, NamingException {

		try {

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			DossierMsgBody dossierMsgBody = (DossierMsgBody) object;

			setDossierMsgBody(dossierMsgBody);

			SubmitDossier submitDossier = new SubmitDossier();

			submitDossier.syncDossier(dossierMsgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @param bytesMessage
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void receiveLocalMessage(BytesMessage bytesMessage)
		throws JMSException, NamingException {

		try {

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			DossierMsgBody dossierMsgBody = (DossierMsgBody) object;

			setDossierMsgBody(dossierMsgBody);

			SubmitDossier submitDossier = new SubmitDossier();

			submitDossier.syncDossier(dossierMsgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	/**
	 * @param dossierMsgBody
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void receiveMessage(DossierMsgBody dossierMsgBody)
		throws JMSException, NamingException {

		try {

			setDossierMsgBody(dossierMsgBody);

			SubmitDossier submitDossier = new SubmitDossier();

			submitDossier.syncDossier(dossierMsgBody);

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	/**
	 * @param dossierMsgBody
	 * @throws JMSException
	 * @throws NamingException
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void receiveLocalMessage(DossierMsgBody dossierMsgBody)
		throws JMSException, NamingException, PortalException, SystemException {

		setDossierMsgBody(dossierMsgBody);

		SubmitDossier submitDossier = new SubmitDossier();

		submitDossier.syncDossier(dossierMsgBody);

	}

	public JMSContext getContext() {

		return _context;
	}

	public void setContext(JMSContext context) {

		this._context = context;
	}

	public DossierMsgBody getDossierMsgBody() {

		return _dossierMsgBody;
	}

	public void setDossierMsgBody(DossierMsgBody dossierMsgBody) {

		this._dossierMsgBody = dossierMsgBody;
	}

	public SyncServiceContext getServiceContextMsgBody() {

		return _serviceContext;
	}

	public void setServiceContextMsgBody(
		SyncServiceContext serviceContextMsgBody) {

		this._serviceContext = serviceContextMsgBody;
	}

	public JMSLocalContext getLocalContext() {

		return _localContext;
	}

	public void setLocalContext(JMSLocalContext localContext) {

		this._localContext = localContext;
	}

	public JMSHornetqContext getHornetqContext() {

		return _hornetqContext;
	}

	public void setHornetqContext(JMSHornetqContext hornetqContext) {

		this._hornetqContext = hornetqContext;
	}

	private JMSHornetqContext _hornetqContext;

	private JMSContext _context;

	private JMSLocalContext _localContext;

	private DossierMsgBody _dossierMsgBody;

	private SyncServiceContext _serviceContext;

	private Log _log =
		LogFactoryUtil.getLog(SubmitDossierMessage.class.getName());
}
