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

import org.opencps.backend.message.UserActionMsg;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.jms.message.body.DossierFileMsgBody;
import org.opencps.jms.message.body.DossierMsgBody;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;

/**
 * @author trungnt
 */
public class SubmitDossier {

	/**
	 * @param submitDossierMessage
	 * @return
	 */
	public Dossier syncDossier(DossierMsgBody dossierMsgBody) {

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
			syncDossierFiles = new LinkedHashMap<DossierFile, DossierPart>();

			syncDLFileEntries = new LinkedHashMap<String, DLFileEntry>();

			data = new LinkedHashMap<String, byte[]>();

			syncFileGroups = new LinkedHashMap<String, FileGroup>();

			syncFileGroupDossierParts = new LinkedHashMap<Long, DossierPart>();

			for (DossierFileMsgBody dossierFileMsgBody : dossierFileMsgBodies) {
				DossierFile syncDossierFile =
					dossierFileMsgBody.getDossierFile();

				syncDossierFiles.put(syncDossierFile,
					dossierFileMsgBody.getDossierPart());

				data.put(syncDossierFile.getOid(),
					dossierFileMsgBody.getBytes());

				DLFileEntry dlFileEntry =
					DLFileEntryLocalServiceUtil.createDLFileEntry(syncDossierFile.getFileEntryId());

				dlFileEntry.setDescription(dossierFileMsgBody.getFileDescription());

				dlFileEntry.setTitle(dossierFileMsgBody.getFileTitle());

				dlFileEntry.setMimeType(dossierFileMsgBody.getMimeType());

				dlFileEntry.setExtension(dossierFileMsgBody.getExtension());

				dlFileEntry.setName(dossierFileMsgBody.getFileName());

				syncDLFileEntries.put(syncDossierFile.getOid(), dlFileEntry);

				FileGroup syncFileGroup = dossierFileMsgBody.getFileGroup();

				if (syncFileGroup != null) {

					syncFileGroups.put(syncDossierFile.getOid(), syncFileGroup);

					syncFileGroupDossierParts.put(
						syncFileGroup.getFileGroupId(),
						dossierFileMsgBody.getFileGroupDossierPart());
				}

			}
		}

		if (syncDossier != null && syncDossierFiles != null &&
			syncDLFileEntries != null && data != null &&
			syncDossierTemplate != null && serviceContext != null) {
			try {
				dossier =
					DossierLocalServiceUtil.syncDossier(syncDossier,
						syncDossierFiles, syncFileGroups,
						syncFileGroupDossierParts, syncDLFileEntries, data,
						syncDossierTemplate, serviceContext);

				sendToBackend(dossier.getDossierId(),
					dossier.getDossierStatus(), serviceContext);

			}
			catch (Exception e) {
				_log.error(e);
			}

		}

		return dossier;
	}

	protected void sendToBackend(
		long dossierId, String dossierStatus, ServiceContext serviceContext) {

		UserActionMsg actionMsg = new UserActionMsg();
		switch (dossierStatus) {

		case PortletConstants.DOSSIER_STATUS_NEW:

			actionMsg.setAction(WebKeys.ACTION_SUBMIT_VALUE);

			actionMsg.setDossierId(dossierId);

			actionMsg.setFileGroupId(0);

			actionMsg.setLocale(serviceContext.getLocale());

			actionMsg.setUserId(serviceContext.getUserId());

			break;

		default:
			break;
		}

		MessageBusUtil.sendMessage("opencps/frontoffice/out/destination",
			actionMsg);

	}

	private Log _log = LogFactoryUtil.getLog(SubmitDossier.class.getClass());
}
