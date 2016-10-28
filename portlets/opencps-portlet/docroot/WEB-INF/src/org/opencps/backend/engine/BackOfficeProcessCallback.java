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

import org.opencps.backend.message.SendToCallbackMsg;
import org.opencps.processmgt.service.ActionHistoryLocalServiceUtil;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;


/**
 * @author khoavd
 *
 */
public class BackOfficeProcessCallback implements MessageListener{


    @Override
    public void receive(Message message)
        throws MessageListenerException {

    	_doRecevie(message);
	    
    }
    
	/**
	 * @param message
	 */
	private void _doRecevie(Message message) {

		SendToCallbackMsg msgToCalback =
		    (SendToCallbackMsg) message.get("toCallback");
		
		long processOrderId = msgToCalback.getProcessOrderId();

		try {
			// Update processOrder
			ProcessOrderLocalServiceUtil.updateProcessOrderStatus(
			    msgToCalback.getProcessOrderId(),
			    msgToCalback.getDossierStatus());
			
			// Update action History
			
			ActionHistoryLocalServiceUtil.addActionHistory(
			    msgToCalback.getUserId(), msgToCalback.getGroupId(),
			    msgToCalback.getCompanyId(), processOrderId,
			    msgToCalback.getProcessWorkflowId(),
			    msgToCalback.getActionDatetime(), msgToCalback.getStepName(),
			    msgToCalback.getActionName(), msgToCalback.getActionNote(),
			    msgToCalback.getActionUserId(), msgToCalback.getDaysDoing(),
			    msgToCalback.getDaysDelay(), msgToCalback.getDossierStatus());
			
		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	private Log _log = LogFactoryUtil.getLog(BackOfficeProcessCallback.class);

}
