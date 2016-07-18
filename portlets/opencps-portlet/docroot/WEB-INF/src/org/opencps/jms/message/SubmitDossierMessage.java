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
import org.opencps.jms.message.body.DossierMsgBody;
import org.opencps.jms.util.JMSMessageBodyUtil;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.util.WebKeys;

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

	public void sendMessage(long dossierId)
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
					JMSMessageBodyUtil.getDossierMsgBody(dossierId);
				dossierMsgBody.setServiceContext(syncServiceContext.getServiceContext());
				byte[] sender =
					JMSMessageUtil.convertObjectToByteArray(dossierMsgBody);

				bytesMessage.writeBytes(sender);

				_context.getMessageProducer().send(bytesMessage);
			}

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			_context.destroy();
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

				bytesMessage.writeBytes(sender);

				_context.getMessageProducer().send(bytesMessage);
			}

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			_context.destroy();
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
		finally {
			_context.destroy();
		}
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

		return _serviceContextMsgBody;
	}

	public void setServiceContextMsgBody(
		SyncServiceContext serviceContextMsgBody) {

		this._serviceContextMsgBody = serviceContextMsgBody;
	}

	private JMSContext _context;

	private DossierMsgBody _dossierMsgBody;

	private SyncServiceContext _serviceContextMsgBody;

	private Log _log =
		LogFactoryUtil.getLog(SubmitDossierMessage.class.getName());
}
