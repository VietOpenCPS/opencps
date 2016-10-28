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

package org.opencps.jms.message;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.opencps.jms.SyncServiceContext;
import org.opencps.jms.business.SubmitPaymentFile;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.context.JMSHornetqContext;
import org.opencps.jms.context.JMSLocalContext;
import org.opencps.jms.message.body.PaymentFileMsgBody;
import org.opencps.jms.util.JMSMessageBodyUtil;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

/**
 * @author trungnt
 */
public class SubmitPaymentFileMessage {

	public SubmitPaymentFileMessage(JMSContext context) {

		this.setContext(context);
	}

	public SubmitPaymentFileMessage(JMSLocalContext context) {

		this.setLocalContext(context);
	}

	public SubmitPaymentFileMessage(JMSHornetqContext hornetqContext) {

		this.setHornetqContext(hornetqContext);
	}

	/**
	 * @param paymentFile
	 * @param typeUpdate
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendMessageByHornetq(PaymentFile paymentFile, String typeUpdate)
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				JMSMessageUtil.createByteMessage(_hornetqContext);
			long companyId =
				GetterUtil.getLong(_hornetqContext.getProperties().getProperty(
					WebKeys.JMS_COMPANY_ID));

			long groupId =
				GetterUtil.getLong(_hornetqContext.getProperties().getProperty(
					WebKeys.JMS_GROUP_ID));

			long userId =
				GetterUtil.getLong(_hornetqContext.getProperties().getProperty(
					WebKeys.JMS_USER_ID));

			if (companyId > 0 && groupId > 0 && userId > 0) {
				SyncServiceContext syncServiceContext =
					new SyncServiceContext(
						companyId, groupId, userId, true, true);

				PaymentFileMsgBody paymentFileMsgBody =
					JMSMessageBodyUtil.getPaymentFileMsgBody(paymentFile);

				paymentFileMsgBody.setServiceContext(syncServiceContext.getServiceContext());

				paymentFileMsgBody.setTypeUpdate(typeUpdate);

				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(paymentFileMsgBody);

				_log.info("####################SubmitPaymentFileMessage: Sending byte message");

				bytesMessage.writeBytes(sender);

				_hornetqContext.getMessageProducer().send(bytesMessage);

				_log.info("####################SubmitPaymentFileMessage: Finished sending byte message");
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
	public void receiveMessageByHornetq()
		throws JMSException, NamingException {

		try {
			BytesMessage bytesMessage =
				(BytesMessage) _hornetqContext.getMessageConsumer().receive();

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			PaymentFileMsgBody paymentFileBody = (PaymentFileMsgBody) object;

			setPaymentFileMsgBody(paymentFileBody);

			SubmitPaymentFile submitPaymentFile = new SubmitPaymentFile();

			submitPaymentFile.syncPaymentFile(paymentFileBody);

		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	/**
	 * @param paymentFile
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendMessage(PaymentFile paymentFile)
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

				PaymentFileMsgBody paymentBody =
					JMSMessageBodyUtil.getPaymentFileMsgBody(paymentFile);

				paymentBody.setServiceContext(syncServiceContext.getServiceContext());

				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(paymentBody);

				_log.info("####################SubmitPaymentFileMessage: Sending byte message");

				bytesMessage.writeBytes(sender);

				_context.getMessageProducer().send(bytesMessage);

				_log.info("####################SubmitPaymentFileMessage: Finished sending byte message");
			}

		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	/**
	 * @param paymentFileMsgBody
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void reviceLocalMessage(PaymentFileMsgBody paymentFileMsgBody)
		throws SystemException, PortalException {

		setPaymentFileMsgBody(paymentFileMsgBody);

		SubmitPaymentFile submitPayment = new SubmitPaymentFile();

		submitPayment.syncPaymentFile(paymentFileMsgBody);
	}

	/**
	 * @return the _context
	 */
	public JMSContext getontext() {

		return _context;
	}

	/**
	 * @param _context
	 *            the _context to set
	 */
	public void setContext(JMSContext _context) {

		this._context = _context;
	}

	/**
	 * @return the _localContext
	 */
	public JMSLocalContext getLocalContext() {

		return _localContext;
	}

	/**
	 * @param _localContext
	 *            the _localContext to set
	 */
	public void setLocalContext(JMSLocalContext _localContext) {

		this._localContext = _localContext;
	}

	/**
	 * @return the _paymentFileMsgBody
	 */
	public PaymentFileMsgBody getPaymentFileMsgBody() {

		return _paymentFileMsgBody;
	}

	/**
	 * @param _paymentFileMsgBody
	 *            the _paymentFileMsgBody to set
	 */
	public void setPaymentFileMsgBody(PaymentFileMsgBody _paymentFileMsgBody) {

		this._paymentFileMsgBody = _paymentFileMsgBody;
	}

	/**
	 * @return the _serviceContext
	 */
	public SyncServiceContext getServiceContext() {

		return _serviceContext;
	}

	/**
	 * @param _serviceContext
	 *            the _serviceContext to set
	 */
	public void setServiceContext(SyncServiceContext _serviceContext) {

		this._serviceContext = _serviceContext;
	}

	/**
	 * @return the _hornetqContext
	 */
	public JMSHornetqContext getHornetqContext() {

		return _hornetqContext;
	}

	/**
	 * @param _hornetqContext
	 *            the _hornetqContext to set
	 */
	public void setHornetqContext(JMSHornetqContext _hornetqContext) {

		this._hornetqContext = _hornetqContext;
	}

	private JMSHornetqContext _hornetqContext;

	private JMSContext _context;

	private JMSLocalContext _localContext;

	private PaymentFileMsgBody _paymentFileMsgBody;

	private SyncServiceContext _serviceContext;

	private Log _log = LogFactoryUtil.getLog(SubmitPaymentFileMessage.class);

}
