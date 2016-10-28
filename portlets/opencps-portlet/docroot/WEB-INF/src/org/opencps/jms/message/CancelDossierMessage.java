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

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.jms.SyncServiceContext;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.context.JMSHornetqContext;
import org.opencps.jms.context.JMSLocalContext;
import org.opencps.jms.message.body.DossierMsgBody;
import org.opencps.jms.util.JMSMessageUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author trungnt
 */
public class CancelDossierMessage {

	public CancelDossierMessage(JMSContext context) {

		this.setContext(context);
	}

	public CancelDossierMessage(JMSLocalContext context) {

		this.setLocalContext(context);
	}

	public CancelDossierMessage(JMSHornetqContext context) {

		this.setHornetqContext(context);
	}

	public void sendMessage(long dossierId, long fileGroupId)
		throws JMSException, NamingException {

		try {
			ObjectMessage objectMessage =
				JMSMessageUtil.createObjectMessage(_hornetqContext);

			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);
			objectMessage.setObject(dossier);

			_log.info("####################CancelDossierMessage: Sending object message");

			_hornetqContext.getMessageProducer().send(objectMessage);

			_log.info("####################CancelDossierMessage: Finished sending object message");

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
		LogFactoryUtil.getLog(CancelDossierMessage.class.getName());
}
