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

import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.message.UserActionMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierStatus;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierStatusLocalServiceUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.Validator;


/**
 * @author khoavd
 *
 */
public class SyncFromFrontOffice implements MessageListener{

	/* (non-Javadoc)
     * @see com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay.portal.kernel.messaging.Message)
     */
    @Override
    public void receive(Message message)
        throws MessageListenerException {

        try {
        	_doReceiveDossier(message);
        }
        catch (Exception e) {
            _log.error("Unable to process message " + message, e);
        }	    
    }
    
	/**
	 * @param message
	 */
	private void _doReceiveDossier(Message message) {

		UserActionMsg userActionMgs =
		    (UserActionMsg) message.get("msgToEngine");

		String action = userActionMgs.getAction();

		long dosserId = userActionMgs.getDossierId();

		boolean trustServiceMode = true;//_checkServiceMode(dosserId);

		if (trustServiceMode) {
			try {
				if (Validator.equals(WebKeys.ACTION_SUBMIT_VALUE, action) &&
				    _checkStatus(
				        userActionMgs.getDossierId(),
				        userActionMgs.getFileGroupId())) {

					if (Validator.equals(
					    PortletConstants.DOSSIER_STATUS_NEW,
					    BackendUtils.getDossierStatus(
					        userActionMgs.getDossierId(),
					        userActionMgs.getFileGroupId()))) {

						int logLevel = 0;

						long govAgencyOrgId =
						    BackendUtils.getGovAgencyOrgId(userActionMgs.getDossierId());

						// Change dossier status to SYSTEM
						// Update govAgencyOrgId of dossier and dossierFile
						DossierLocalServiceUtil.updateDossierStatus(
						    userActionMgs.getUserId(),
						    userActionMgs.getDossierId(),
						    govAgencyOrgId,
						    PortletConstants.DOSSIER_STATUS_SYSTEM,
						    PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
						    userActionMgs.getFileGroupId(), logLevel,
						    userActionMgs.getLocale());

						// Create message
						Message msgToEngine = new Message();

						SendToEngineMsg engineMsg = new SendToEngineMsg();

						engineMsg.setDossierId(userActionMgs.getDossierId());
						engineMsg.setFileGroupId(userActionMgs.getFileGroupId());
						engineMsg.setEvent(WebKeys.ACTION_SUBMIT_VALUE);

						msgToEngine.put("msgToEngine", engineMsg);

						// Send message to ...engine/destination
						MessageBusUtil.sendMessage(
						    "opencps/backoffice/engine/destination",
						    msgToEngine);
					}

				}
				else if (Validator.equals(WebKeys.ACTION_RESUBMIT_VALUE, action) &&
				    _checkStatus(
				        userActionMgs.getDossierId(),
				        userActionMgs.getFileGroupId())) {
					
					Message msgToEngine = new Message();

					int logLevel = 0;

					long govAgencyOrgId =
					    BackendUtils.getGovAgencyOrgId(userActionMgs.getDossierId());

					SendToEngineMsg engineMsg = new SendToEngineMsg();

					DossierLocalServiceUtil.updateDossierStatus(
					    userActionMgs.getUserId(),
					    userActionMgs.getDossierId(), govAgencyOrgId,
					    PortletConstants.DOSSIER_STATUS_SYSTEM,
					    PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
					    userActionMgs.getFileGroupId(), logLevel,
					    userActionMgs.getLocale());

					engineMsg.setDossierId(userActionMgs.getDossierId());
					engineMsg.setFileGroupId(userActionMgs.getFileGroupId());
					engineMsg.setEvent(WebKeys.ACTION_CHANGE_VALUE);
					engineMsg.setActionDatetime(new Date());
					engineMsg.setProcessOrderId(userActionMgs.getProcessOrderId());

					msgToEngine.put("msgToEngine", engineMsg);

					// Send message to ...engine/destination
					MessageBusUtil.sendMessage(
					    "opencps/backoffice/engine/destination", msgToEngine);

				}
				else if (Validator.equals(WebKeys.ACTION_REPAIR_VALUE, action)) {
					// Update requestCommand = repair
					
					Dossier dossier =
					    DossierLocalServiceUtil.fetchDossier(userActionMgs.getDossierId());

					DossierLocalServiceUtil.updateDossierStatus(
					    userActionMgs.getDossierId(),
					    userActionMgs.getFileGroupId(),
					    dossier.getDossierStatus(), dossier.getReceptionNo(),
					    dossier.getEstimateDatetime(),
					    dossier.getReceiveDatetime(),
					    dossier.getFinishDatetime(),
					    WebKeys.ACTOR_ACTION_CITIZEN,
					    WebKeys.ACTION_REPAIR_VALUE,
					    WebKeys.ACTION_REPAIR_VALUE,
					    WebKeys.ACTION_REPAIR_VALUE);
				}
				else if (Validator.equals(WebKeys.ACTION_CLOSE_VALUE, action)) {
					Dossier dossier =
					    DossierLocalServiceUtil.fetchDossier(userActionMgs.getDossierId());

					DossierLocalServiceUtil.updateDossierStatus(
					    userActionMgs.getDossierId(),
					    userActionMgs.getFileGroupId(),
					    dossier.getDossierStatus(), dossier.getReceptionNo(),
					    dossier.getEstimateDatetime(),
					    dossier.getReceiveDatetime(),
					    dossier.getFinishDatetime(),
					    WebKeys.ACTOR_ACTION_CITIZEN,
					    WebKeys.ACTION_CLOSE_VALUE,
					    WebKeys.ACTION_CLOSE_VALUE,
					    WebKeys.ACTION_CLOSE_VALUE);
				}

			}
			catch (Exception e) {
				_log.error(e);
			}

		}

	}

    /**
     * @param dossierId
     * @return
     */
    private boolean _checkServiceMode(long dossierId) {
    	boolean trustServiceMode = false;
    	
    	int serviceMode = 0;
    	
    	try {
	        Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierId);
	        
	        if (Validator.isNotNull(dossier)) {
	        	serviceMode = dossier.getServiceMode();
	        }
        }
        catch (Exception e) {
        }
    	
    	if (serviceMode == 3) {
    		trustServiceMode = true;
    	}
    	
    	return trustServiceMode;
    }
    
    private boolean _checkStatus(long dossierId, long fileGroupId) {
    	
    	boolean isValidatorStatus = false;
    	
    	DossierStatus status = null;
    	
    	try {
	        status = DossierStatusLocalServiceUtil.getStatus(dossierId, fileGroupId);
        }
        catch (Exception e) {
        	_log.error(e);
        	
        }
    	
    	if (Validator.isNotNull(status)) {
			if (status.getDossierStatus() == PortletConstants.DOSSIER_STATUS_NEW ||
			    status.getDossierStatus() == PortletConstants.DOSSIER_STATUS_WAITING) {
				isValidatorStatus = true;
			}
    	}
    	
    	return isValidatorStatus;
    }
    
    private Log _log = LogFactoryUtil.getLog(SyncFromFrontOffice.class);

}
