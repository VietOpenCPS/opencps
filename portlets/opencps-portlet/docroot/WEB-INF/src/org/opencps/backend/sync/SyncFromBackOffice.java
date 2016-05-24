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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;


/**
 * @author khoavd
 *
 */
public class SyncFromBackOffice implements MessageListener{

	/* (non-Javadoc)
     * @see com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay.portal.kernel.messaging.Message)
     */
    @Override
    public void receive(Message message)
        throws MessageListenerException {
    	
    	doRevice(message);

    }
    
    
    
    private void doRevice(Message message) {
    	
    	long processOrderId = GetterUtil.getLong(message.get("processOrderId"), 0);
    	long dossierId = GetterUtil.getLong(message.get("dossierId"), 0);
    	long fileGroupId = GetterUtil.getLong(message.get("fileGroupId"), 0);
    	int dossierStatus = GetterUtil.getInteger(message.get("dossierStatus"), 0);
    	String actionInfo = GetterUtil.getString(message.get("actionInfo"), StringPool.BLANK);
    	String messageInfo = GetterUtil.getString(message.get("messageInfo"), StringPool.BLANK);
    	boolean sendResult = GetterUtil.getBoolean(message.get("sendResult"), false);
    	boolean requestPayment = GetterUtil.getBoolean(message.get("requestPayment"), false);
    	Date updateDatetime = GetterUtil.getDate(message.get("updateDatetime"), new SimpleDateFormat("dd/MM/yyyy mm:HH"));
    	String receptionNo = GetterUtil.getString(message.get("receptionNo"), StringPool.BLANK);
    	Date receiveDatetime = GetterUtil.getDate(message.get("receiveDatetime"), new SimpleDateFormat("dd/MM/yyyy mm:HH"));
    	Date estimateDatetime = GetterUtil.getDate(message.get("estimateDatetime"), new SimpleDateFormat("dd/MM/yyyy mm:HH"));
    	Date finishDatetime = GetterUtil.getDate(message.get("finishDatetime"), new SimpleDateFormat("dd/MM/yyyy mm:HH"));
    	
    	long userId = GetterUtil.getLong(message.get("userId"));
    	long groupId = GetterUtil.getLong(message.get("groupId"));
    	long companyId = GetterUtil.getLong(message.get("companyId"));
    	
    	try {
			DossierLocalServiceUtil.updateDossierStatus(
			    userId, groupId, companyId, dossierId, fileGroupId,
			    receptionNo, estimateDatetime, receiveDatetime, finishDatetime,
			    dossierStatus, actionInfo, messageInfo);
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
    	
    	Message msg = new Message();
    	
    	msg.put("_processOrderId", processOrderId);
    	msg.put("_syncStatus", "ok");
    	msg.put("_dossierStatus", dossierStatus);
    	
    	MessageBusUtil.sendMessage("opencps/backoffice/engine/callback", msg);
    	
    }

}
