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
import org.opencps.dossiermgt.service.DossierLogLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierStatusLocalServiceUtil;
import org.opencps.dossiermgt.util.ActorBean;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
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
 */
public class SyncFromFrontOffice implements MessageListener {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.messaging.MessageListener#receive(com.liferay
	 * .portal.kernel.messaging.Message)
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

		long dossierId = userActionMgs.getDossierId();

		boolean trustServiceMode = BackendUtils.checkServiceMode(dossierId);

		if (trustServiceMode) {
			try {
				int actor = WebKeys.DOSSIER_ACTOR_CITIZEN;
				long actorId = 0;
				String actorName = "SYSTEM";

				if (Validator.equals(WebKeys.ACTION_SUBMIT_VALUE, action) &&
				    _checkStatus(
				        userActionMgs.getDossierId(),
				        userActionMgs.getFileGroupId())) {

					int logLevel = 0;

					long govAgencyOrgId =
					    BackendUtils.getGovAgencyOrgId(userActionMgs.getDossierId());

					DossierLocalServiceUtil.updateDossierStatus(
					    userActionMgs.getUserId(),
					    userActionMgs.getDossierId(), govAgencyOrgId,
					    PortletConstants.DOSSIER_STATUS_SYSTEM,
					    PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
					    userActionMgs.getFileGroupId(), logLevel,
					    userActionMgs.getLocale(), actor, actorId, actorName);

					// Create message
					Message msgToEngine = new Message();

					SendToEngineMsg engineMsg = new SendToEngineMsg();

					engineMsg.setDossierId(userActionMgs.getDossierId());
					engineMsg.setFileGroupId(userActionMgs.getFileGroupId());
					engineMsg.setEvent(WebKeys.ACTION_SUBMIT_VALUE);
					engineMsg.setGroupId(userActionMgs.getGroupId());
					engineMsg.setDossierStatus(userActionMgs.getDossierStatus());
					engineMsg.setActionUserId(userActionMgs.getUserId());
					engineMsg.setActorType(WebKeys.DOSSIER_ACTOR_CITIZEN);
					engineMsg.setActionNote(userActionMgs.getActionNote());

					msgToEngine.put("msgToEngine", engineMsg);

					// Send message to ...engine/destination
					MessageBusUtil.sendMessage(
					    "opencps/backoffice/engine/destination", msgToEngine);

					// Update DossierLog (Listener revice mgs)

					ActorBean actorBean =
					    new ActorBean(1, userActionMgs.getUserId());

					DossierLogLocalServiceUtil.addDossierLog(
					    userActionMgs.getUserId(), userActionMgs.getGroupId(),
					    userActionMgs.getCompanyId(), dossierId, 0,
					    PortletConstants.DOSSIER_STATUS_SYSTEM,
					    PortletConstants.DOSSIER_ACTION_REVICE,
					    PortletConstants.DOSSIER_ACTION_REVICE, new Date(), 0,
					    0, actorBean.getActor(), actorBean.getActorId(),
					    actorBean.getActorName(),
					    SyncFromFrontOffice.class.getName());

				}
				else if (Validator.equals(WebKeys.ACTION_RESUBMIT_VALUE, action) &&
				    _checkStatus(
				        userActionMgs.getDossierId(),
				        userActionMgs.getFileGroupId())) {

					Message msgToEngine = new Message();

					ProcessOrder processOrder =
					    ProcessOrderLocalServiceUtil.getProcessOrder(
					        dossierId, userActionMgs.getFileGroupId());

					int logLevel = 0;

					long govAgencyOrgId =
					    BackendUtils.getGovAgencyOrgId(userActionMgs.getDossierId());

					SendToEngineMsg engineMsg = new SendToEngineMsg();

					// TODO update new function add dossier status
					DossierLocalServiceUtil.updateDossierStatus(
					    userActionMgs.getUserId(),
					    userActionMgs.getDossierId(), govAgencyOrgId,
					    PortletConstants.DOSSIER_STATUS_SYSTEM,
					    PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
					    userActionMgs.getFileGroupId(), logLevel,
					    userActionMgs.getLocale(), actor, actorId, actorName);

					engineMsg.setDossierId(userActionMgs.getDossierId());
					engineMsg.setFileGroupId(userActionMgs.getFileGroupId());
					engineMsg.setEvent(WebKeys.ACTION_CHANGE_VALUE);
					engineMsg.setActionDatetime(new Date());
					engineMsg.setProcessOrderId(processOrder.getProcessOrderId());
					engineMsg.setGroupId(userActionMgs.getGroupId());
					engineMsg.setDossierStatus(userActionMgs.getDossierStatus());
					engineMsg.setActionUserId(userActionMgs.getUserId());
					engineMsg.setActorType(WebKeys.DOSSIER_ACTOR_CITIZEN);
					engineMsg.setActionNote(userActionMgs.getActionNote());

					msgToEngine.put("msgToEngine", engineMsg);

					// Send message to ...engine/destination
					MessageBusUtil.sendMessage(
					    "opencps/backoffice/engine/destination", msgToEngine);

					ActorBean actorBean = new ActorBean(0, 0);

					DossierLogLocalServiceUtil.addDossierLog(
					    userActionMgs.getUserId(), userActionMgs.getGroupId(),
					    userActionMgs.getCompanyId(), dossierId, 0,
					    PortletConstants.DOSSIER_STATUS_SYSTEM,
					    PortletConstants.DOSSIER_ACTION_REVICE,
					    PortletConstants.DOSSIER_ACTION_REVICE, new Date(), 0,
					    0, actorBean.getActor(), actorBean.getActorId(),
					    actorBean.getActorName(),
					    SyncFromFrontOffice.class.getName());

				}
				else if (Validator.equals(WebKeys.ACTION_REPAIR_VALUE, action)) {
					// Update requestCommand = repair
					// TODO check again
					Dossier dossier =
					    DossierLocalServiceUtil.fetchDossier(userActionMgs.getDossierId());

					ActorBean actorBean = new ActorBean(1, dossier.getUserId());

					DossierLogLocalServiceUtil.addCommandRequest(
					    dossier.getUserId(), dossier.getGroupId(),
					    dossier.getCompanyId(), dossierId, 0,
					    dossier.getDossierStatus(),
					    PortletConstants.DOSSIER_ACTION_REPAIR_DOSSIER,
					    PortletConstants.DOSSIER_ACTION_REPAIR_DOSSIER,
					    new Date(), 0, 2, actorBean.getActor(),
					    actorBean.getActorId(), actorBean.getActorName(),
					    SyncFromFrontOffice.class.getName() +
					        ".repairDossier()", WebKeys.ACTION_REPAIR_VALUE);
				}
				else if (Validator.equals(WebKeys.ACTION_CANCEL_VALUE, action)) {
					Dossier dossier =
					    DossierLocalServiceUtil.fetchDossier(userActionMgs.getDossierId());

					ActorBean actorBean = new ActorBean(1, dossier.getUserId());

					DossierLogLocalServiceUtil.addCommandRequest(
					    dossier.getUserId(), dossier.getGroupId(),
					    dossier.getCompanyId(), dossierId, 0,
					    dossier.getDossierStatus(),
					    PortletConstants.DOSSIER_ACTION_CANCEL_DOSSIER,
					    PortletConstants.DOSSIER_ACTION_CANCEL_DOSSIER,
					    new Date(), 0, 2, actorBean.getActor(),
					    actorBean.getActorId(), actorBean.getActorName(),
					    SyncFromFrontOffice.class.getName() +
					        ".repairDossier()", WebKeys.ACTION_CANCEL_VALUE);

				}

			}
			catch (Exception e) {
				_log.error(e);
			}

		}

	}

	private boolean _checkStatus(long dossierId, long fileGroupId) {

		boolean isValidatorStatus = false;

		DossierStatus status = null;

		try {
			status =
				DossierStatusLocalServiceUtil.getStatus(dossierId, fileGroupId);
		}
		catch (Exception e) {
			_log.error(e);

		}

		if (Validator.isNotNull(status)) {
			if (status.getDossierStatus().equals(
				PortletConstants.DOSSIER_STATUS_NEW) ||
				status.getDossierStatus().equals(
					PortletConstants.DOSSIER_STATUS_WAITING)) {
				isValidatorStatus = true;
			}
		}

		return isValidatorStatus;
	}

	private Log _log = LogFactoryUtil.getLog(SyncFromFrontOffice.class);

}
