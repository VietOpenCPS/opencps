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

import org.opencps.jms.context.JMSHornetqContext;
import org.opencps.jms.util.JMSMessageBodyUtil;
import org.opencps.jms.util.JMSMessageUtil;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PortalUtil;

/**
 * @author khoavd
 */
public class MsgInFrontOffice implements MessageListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay
	 * .portal.kernel.messaging.Message)
	 */
	@Override
	public void receive(Message message)
		throws MessageListenerException {

		_doReceive(message);
	}

	private void _doReceive(Message message) {

		_log.info("####################MsgInFrontOffice: Started receive jms message");

		long[] companyIds = PortalUtil.getCompanyIds();

		long companyId = 0;

		if (companyIds != null && companyIds.length > 0) {
			for (int i = 0; i < companyIds.length; i++) {
				if (PortletUtil.checkJMSConfig(companyIds[i])) {
					companyId = companyIds[i];
					break;
				}
			}
		}

		if (companyId > 0) {
			/*
			 * JMSContext context = JMSMessageUtil.createConsumer( companyId,
			 * StringPool.BLANK, true,
			 * WebKeys.JMS_QUEUE_OPENCPS_FRONTOFFICE.toLowerCase(),
			 * WebKeys.JMS_QUEUE_OPENCPS_FRONTOFFICE.toLowerCase(), "local",
			 * "jmscore");
			 */

			JMSHornetqContext context =
				JMSMessageUtil.createHornetqConsumer(
					companyId, StringPool.BLANK, true,
					WebKeys.JMS_QUEUE_OPENCPS_FRONTOFFICE.toLowerCase(),
					WebKeys.JMS_QUEUE_OPENCPS_FRONTOFFICE.toLowerCase(),
					"local", "hornetq");
			try {

				int receiveNumber = 50;

				int count = 1;

				while (count <= receiveNumber) {

					javax.jms.Message jsmMessage =
						context.getMessageConsumer().receive(1000);

					if (jsmMessage != null) {
						JMSMessageBodyUtil.receiveMessage(context, jsmMessage);
					}
					else {
					}

					count++;
				}
			}
			catch (Exception e) {
				_log.error(e);
			}
			finally {
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
		else {
			_log.info("Cannot create connection to JMS Queue..................");
		}

	}
	private Log _log = LogFactoryUtil.getLog(MsgInFrontOffice.class);
}
