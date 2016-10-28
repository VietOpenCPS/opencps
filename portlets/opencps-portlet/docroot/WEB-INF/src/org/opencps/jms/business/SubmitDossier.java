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

package org.opencps.jms.business;

import java.util.LinkedHashMap;
import java.util.List;

import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.jms.message.body.DossierFileMsgBody;
import org.opencps.jms.message.body.DossierMsgBody;
import org.opencps.jms.util.JMSMessageBodyUtil.AnalyzeDossierFile;
import org.opencps.processmgt.NoSuchProcessOrderException;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

/**
 * @author trungnt
 */
public class SubmitDossier {

	/**
	 * @param submitDossierMessage
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public Dossier syncDossier(DossierMsgBody dossierMsgBody)
		throws PortalException, SystemException {

		Dossier dossier = null;

		Dossier syncDossier = dossierMsgBody.getDossier();

		ServiceContext serviceContext = dossierMsgBody.getServiceContext();

		LinkedHashMap<DossierFile, DossierPart> syncDossierFiles = null;

		LinkedHashMap<String, FileGroup> syncFileGroups = null;

		LinkedHashMap<Long, DossierPart> syncFileGroupDossierParts = null;

		LinkedHashMap<String, DLFileEntry> syncDLFileEntries = null;

		LinkedHashMap<String, byte[]> data = null;

		DossierTemplate syncDossierTemplate =
			dossierMsgBody.getDossierTemplate();

		List<DossierFileMsgBody> dossierFileMsgBodies =
			dossierMsgBody.getLstDossierFileMsgBody();

		if (dossierFileMsgBodies != null) {
			AnalyzeDossierFile analyzeDossierFile =
				new AnalyzeDossierFile(dossierFileMsgBodies);

			syncDossierFiles = analyzeDossierFile.getSyncDossierFiles();
			syncFileGroups = analyzeDossierFile.getSyncFileGroups();
			syncFileGroupDossierParts =
				analyzeDossierFile.getSyncFileGroupDossierParts();
			syncDLFileEntries = analyzeDossierFile.getSyncDLFileEntries();
			data = analyzeDossierFile.getData();
		}

		if (syncDossier != null && syncDossierFiles != null &&
			syncDLFileEntries != null && data != null &&
			syncDossierTemplate != null && serviceContext != null) {

			boolean isNew = false;

			Dossier dossierBackend = null;

			try {
				dossierBackend =
					DossierLocalServiceUtil.getDossierByOId(syncDossier.getOid());
			}
			catch (Exception e) {

			}

			if (Validator.isNull(dossierBackend)) {
				isNew = true;
			}

			if (isNew) {
				dossier =
					DossierLocalServiceUtil.syncDossier(
						syncDossier, syncDossierFiles, syncFileGroups,
						syncFileGroupDossierParts, syncDLFileEntries, data,
						syncDossierTemplate, dossierMsgBody.getServiceConfig(),
						dossierMsgBody.getServiceInfo(), serviceContext);
				// TODO add log
			}
			else {
				dossier =
					DossierLocalServiceUtil.syncReSubmitDossier(
						syncDossier, syncDossierFiles, syncFileGroups,
						syncFileGroupDossierParts, syncDLFileEntries, data,
						syncDossierTemplate, serviceContext);
				// TODO add log
			}

			// Dong bo trang thai giay to

			DossierFileLocalServiceUtil.updateDossierFileSyncStatus(
				dossier.getUserId(), dossier.getDossierId(),
				PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC,
				PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS, 0);

			// TODO add log

			sendToBackend(
				dossier.getDossierId(), 0, dossier.getDossierStatus(),
				serviceContext, isNew);
		}

		return dossier;
	}

	/**
	 * @param dossierId
	 * @param fileGroupId
	 * @param dossierStatus
	 * @param serviceContext
	 * @throws NoSuchProcessOrderException
	 * @throws SystemException
	 */
	protected void sendToBackend(
		long dossierId, long fileGroupId, String dossierStatus,
		ServiceContext serviceContext, boolean isNew)
		throws NoSuchProcessOrderException, SystemException {

		Message message = new Message();

		SendToEngineMsg engineMsg = new SendToEngineMsg();

		if (!isNew) {
			engineMsg.setAction(WebKeys.ACTION_RESUBMIT_VALUE);

			engineMsg.setDossierId(dossierId);

			engineMsg.setFileGroupId(fileGroupId);

			engineMsg.setUserId(serviceContext.getUserId());

			engineMsg.setGroupId(serviceContext.getScopeGroupId());

			engineMsg.setCompanyId(serviceContext.getCompanyId());

			engineMsg.setDossierStatus(dossierStatus);

			ProcessOrder processOrder =
				ProcessOrderLocalServiceUtil.getProcessOrder(
					dossierId, fileGroupId);

			engineMsg.setProcessOrderId(processOrder.getProcessOrderId());

			engineMsg.setEvent(WebKeys.ACTION_CHANGE_VALUE);

		}
		else {
			engineMsg.setAction(WebKeys.ACTION_SUBMIT_VALUE);

			engineMsg.setDossierId(dossierId);

			engineMsg.setFileGroupId(fileGroupId);

			engineMsg.setCompanyId(serviceContext.getCompanyId());

			engineMsg.setEvent(WebKeys.ACTION_SUBMIT_VALUE);

			engineMsg.setUserId(serviceContext.getUserId());

			engineMsg.setGroupId(serviceContext.getScopeGroupId());

			engineMsg.setUserId(serviceContext.getUserId());

			engineMsg.setDossierStatus(dossierStatus);

		}

		message.put("msgToEngine", engineMsg);

		MessageBusUtil.sendMessage(
			"opencps/backoffice/engine/destination", message);

	}
}
