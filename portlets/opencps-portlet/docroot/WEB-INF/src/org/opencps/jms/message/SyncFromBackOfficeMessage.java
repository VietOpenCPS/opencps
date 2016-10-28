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

import org.opencps.jms.SyncServiceContext;
import org.opencps.jms.business.SyncFromBackOffice;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.context.JMSHornetqContext;
import org.opencps.jms.context.JMSLocalContext;
import org.opencps.jms.message.body.SyncFromBackOfficeMsgBody;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

/**
 * @author trungnt
 */
public class SyncFromBackOfficeMessage {

	public SyncFromBackOfficeMessage(JMSContext context) {

		this.setContext(context);
	}

	public SyncFromBackOfficeMessage(JMSLocalContext context) {

		this.setLocalContext(context);
	}

	public SyncFromBackOfficeMessage(JMSHornetqContext context) {

		this.setHornetqContext(context);
	}

	/**
	 * @param syncFromBackOfficeMsgBody
	 * @throws NamingException
	 * @throws JMSException
	 */
	public synchronized void receiveLocalMessage(
		SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody)
		throws JMSException, NamingException {

		setSyncFromBackOfficeMsgBody(syncFromBackOfficeMsgBody);

		SyncFromBackOffice syncFromBackOffice = new SyncFromBackOffice();

		try {
			syncFromBackOffice.syncDossierStatus(syncFromBackOfficeMsgBody);
		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	public JMSContext getContext() {

		return _context;
	}

	public void setContext(JMSContext context) {

		this._context = context;
	}

	public JMSLocalContext getLocalContext() {

		return _localContext;
	}

	public void setLocalContext(JMSLocalContext localContext) {

		this._localContext = localContext;
	}

	public SyncFromBackOfficeMsgBody getSyncFromBackOfficeMsgBody() {

		return _syncFromBackOfficeMsgBody;
	}

	public void setSyncFromBackOfficeMsgBody(
		SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody) {

		this._syncFromBackOfficeMsgBody = syncFromBackOfficeMsgBody;
	}

	public SyncServiceContext getServiceContext() {

		return _serviceContext;
	}

	public void setServiceContext(SyncServiceContext serviceContext) {

		this._serviceContext = serviceContext;
	}

	public JMSHornetqContext getHornetqContext() {

		return _hornetqContext;
	}

	public void setHornetqContext(JMSHornetqContext hornetqContext) {

		this._hornetqContext = hornetqContext;
	}

	/**
	 * @param msgBody
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendMessage(SyncFromBackOfficeMsgBody msgBody)
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

				msgBody.setServiceContext(syncServiceContext.getServiceContext());

				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(msgBody);

				_log.info("####################SyncFromBackOfficeMessage: Sending byte message");

				bytesMessage.writeBytes(sender);

				_context.getMessageProducer().send(bytesMessage);

				_log.info("####################SyncFromBackOfficeMessage: Finished sending byte message");

			}

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	/**
	 * @param msgBody
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendMessageByHornetq(SyncFromBackOfficeMsgBody msgBody)
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

				msgBody.setServiceContext(syncServiceContext.getServiceContext());

				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(msgBody);

				bytesMessage.writeBytes(sender);
				
				_log.info("####################SyncFromBackOfficeMessage: Sending byte message");

				_hornetqContext.getMessageProducer().send(bytesMessage);

				_log.info("####################SyncFromBackOfficeMessage: Finished sending byte message");

			}

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	private JMSContext _context;

	private JMSHornetqContext _hornetqContext;

	private JMSLocalContext _localContext;

	private SyncFromBackOfficeMsgBody _syncFromBackOfficeMsgBody;

	private SyncServiceContext _serviceContext;

	private Log _log = LogFactoryUtil.getLog(SyncFromBackOfficeMessage.class);
}
