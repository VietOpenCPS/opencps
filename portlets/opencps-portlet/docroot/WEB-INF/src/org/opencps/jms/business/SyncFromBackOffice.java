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

import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.jms.message.body.DossierFileMsgBody;
import org.opencps.jms.message.body.SyncFromBackOfficeMsgBody;
import org.opencps.jms.util.JMSMessageBodyUtil.AnalyzeDossierFile;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

/**
 * @author trungnt
 */
public class SyncFromBackOffice {

	/**
	 * @param syncFromBackOfficeMsgBody
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void syncDossierStatus(
		SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody)
		throws SystemException, PortalException {

		ServiceContext serviceContext =
			syncFromBackOfficeMsgBody.getServiceContext();

		LinkedHashMap<DossierFile, DossierPart> syncDossierFiles = null;

		LinkedHashMap<String, FileGroup> syncFileGroups = null;

		LinkedHashMap<Long, DossierPart> syncFileGroupDossierParts = null;

		LinkedHashMap<String, DLFileEntry> syncDLFileEntries = null;

		LinkedHashMap<String, byte[]> data = null;

		List<DossierFileMsgBody> dossierFileMsgBodies =
			syncFromBackOfficeMsgBody.getLstDossierFileMsgBody();

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

		System.out.println("####################SyncFromBackOffice: Starting synchronize DossierStatus");

		DossierLocalServiceUtil.syncDossierStatus(
			syncFromBackOfficeMsgBody.getOid(),
			syncFromBackOfficeMsgBody.getFileGroupId(),
			syncFromBackOfficeMsgBody.getDossierStatus(),
			syncFromBackOfficeMsgBody.getReceptionNo(),
			syncFromBackOfficeMsgBody.getEstimateDatetime(),
			syncFromBackOfficeMsgBody.getReceiveDatetime(),
			syncFromBackOfficeMsgBody.getFinishDatetime(),
			syncFromBackOfficeMsgBody.getActor(),
			syncFromBackOfficeMsgBody.getActorId(),
			syncFromBackOfficeMsgBody.getActorName(),
			syncFromBackOfficeMsgBody.getRequestCommand(),
			syncFromBackOfficeMsgBody.getActionInfo(),
			syncFromBackOfficeMsgBody.getMessageInfo(), syncDossierFiles,
			syncFileGroups, syncFileGroupDossierParts, syncDLFileEntries, data,
			syncFromBackOfficeMsgBody.getPaymentFile(), serviceContext);

		// TODO add log
	}
}
