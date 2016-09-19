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

package org.opencps.jms.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.FileGroup;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.FileGroupLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.jms.context.JMSContext;
import org.opencps.jms.context.JMSHornetqContext;
import org.opencps.jms.message.SubmitDossierMessage;
import org.opencps.jms.message.SubmitPaymentFileMessage;
import org.opencps.jms.message.SyncFromBackOfficeMessage;
import org.opencps.jms.message.body.DossierFileMsgBody;
import org.opencps.jms.message.body.DossierMsgBody;
import org.opencps.jms.message.body.PaymentFileMsgBody;
import org.opencps.jms.message.body.SyncFromBackOfficeMsgBody;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.util.DLFileEntryUtil;
import org.opencps.util.PortletConstants;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;

/**
 * @author trungnt
 */
public class JMSMessageBodyUtil {

	public synchronized static void receiveMessage(
		JMSContext context, Message jsmMessage)
		throws JMSException, IOException, NamingException, PortalException,
		SystemException {

		if (jsmMessage instanceof TextMessage) {

		}
		else if (jsmMessage instanceof ObjectMessage) {
		}
		else if (jsmMessage instanceof BytesMessage) {
			BytesMessage bytesMessage = (BytesMessage) jsmMessage;

			_log.info("BytesMessage/////////////////////////////////////");

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			if (object instanceof SyncFromBackOfficeMsgBody) {

				SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody =
					(SyncFromBackOfficeMsgBody) object;

				SyncFromBackOfficeMessage syncFromBackOfficeMessage =
					new SyncFromBackOfficeMessage(context);

				syncFromBackOfficeMessage.receiveLocalMessage(syncFromBackOfficeMsgBody);
			}
			else if (object instanceof DossierMsgBody) {
				DossierMsgBody dossierMsgBody = (DossierMsgBody) object;
				_log.info("Msg is Dossier x");
				SubmitDossierMessage submitDossierMessage =
					new SubmitDossierMessage(context);

				submitDossierMessage.receiveLocalMessage(dossierMsgBody);
			}
		}
		else if (jsmMessage instanceof StreamMessage) {
		}
		else {
			_log.info("No message...");

		}
	}

	public synchronized static void receiveMessage(
		JMSHornetqContext context, Message jsmMessage)
		throws JMSException, IOException, NamingException, PortalException,
		SystemException {

		if (jsmMessage instanceof TextMessage) {

		}
		else if (jsmMessage instanceof ObjectMessage) {
		}
		else if (jsmMessage instanceof BytesMessage) {
			BytesMessage bytesMessage = (BytesMessage) jsmMessage;

			_log.info("BytesMessage/////////////////////////////////////");

			byte[] result = new byte[(int) bytesMessage.getBodyLength()];

			bytesMessage.readBytes(result);

			Object object = JMSMessageUtil.convertByteArrayToObject(result);

			if (object instanceof SyncFromBackOfficeMsgBody) {

				SyncFromBackOfficeMsgBody syncFromBackOfficeMsgBody =
					(SyncFromBackOfficeMsgBody) object;

				SyncFromBackOfficeMessage syncFromBackOfficeMessage =
					new SyncFromBackOfficeMessage(context);

				syncFromBackOfficeMessage.receiveLocalMessage(syncFromBackOfficeMsgBody);
			}
			else if (object instanceof DossierMsgBody) {
				DossierMsgBody dossierMsgBody = (DossierMsgBody) object;

				SubmitDossierMessage submitDossierMessage =
					new SubmitDossierMessage(context);

				submitDossierMessage.receiveLocalMessage(dossierMsgBody);

				//
			}
			else if (object instanceof PaymentFileMsgBody) {
				_log.info("////// /////////////////// PaymentFileMgsBody");

				PaymentFileMsgBody paymentMsgBody = (PaymentFileMsgBody) object;

				SubmitPaymentFileMessage submitPaymentFileMessage =
					new SubmitPaymentFileMessage(context);

				submitPaymentFileMessage.reviceLocalMessage(paymentMsgBody);

			}
		}
		else if (jsmMessage instanceof StreamMessage) {
		}
		else {
			_log.info("No message...");

		}
	}

	/**
	 * @param dossierId
	 * @return
	 */
	public static DossierMsgBody getDossierMsgBody(
		long dossierId, long fileGroupId) {

		DossierMsgBody dossierMsgBody = new DossierMsgBody();

		try {
			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);
			DossierTemplate dossierTemplate =
				DossierTemplateLocalServiceUtil.getDossierTemplate(dossier.getDossierTemplateId());
			ServiceConfig serviceConfig =
				ServiceConfigLocalServiceUtil.getServiceConfig(dossier.getServiceConfigId());

			ServiceInfo serviceInfo =
				ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());

			List<DossierFile> dossierFiles =
				DossierFileLocalServiceUtil.getDossierFileByD_S_R(
					dossierId,
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC, 0);

			_log.info("##################################################### SIZE" +
				dossierFiles.size());

			List<DossierFileMsgBody> dossierFileMsgBodies =
				new ArrayList<DossierFileMsgBody>();
			if (dossierFiles != null) {
				for (DossierFile dossierFile : dossierFiles) {
					DossierFileMsgBody dossierFileMsgBody =
						new DossierFileMsgBody();

					if (dossierFile.getGroupFileId() > 0) {
						FileGroup fileGroup =
							FileGroupLocalServiceUtil.getFileGroup(dossierFile.getGroupFileId());
						dossierFileMsgBody.setFileGroup(fileGroup);
						DossierPart fileGroupDossierPart =
							DossierPartLocalServiceUtil.getDossierPart(fileGroup.getDossierPartId());
						dossierFileMsgBody.setFileGroupDossierPart(fileGroupDossierPart);
					}

					DossierPart dossierPart =
						DossierPartLocalServiceUtil.getDossierPart(dossierFile.getDossierPartId());

					if (dossierFile.getFileEntryId() > 0) {

						_log.info("#####################################################" +
							dossierFile.getFileEntryId());

						DLFileEntry dlFileEntry =
							DLFileEntryUtil.getDLFileEntry(dossierFile.getFileEntryId());
						dossierFileMsgBody.setFileDescription(dlFileEntry.getDescription());
						dossierFileMsgBody.setExtension(dlFileEntry.getExtension());
						dossierFileMsgBody.setFileName(dlFileEntry.getName());
						dossierFileMsgBody.setFileTitle(dlFileEntry.getTitle());
						dossierFileMsgBody.setMimeType(dlFileEntry.getMimeType());

						byte[] bytes =
							JMSMessageUtil.convertInputStreamToByteArray(dlFileEntry.getContentStream());

						dossierFileMsgBody.setBytes(bytes);

					}

					dossierFileMsgBody.setDossierPart(dossierPart);

					dossierFileMsgBody.setDossierFile(dossierFile);

					dossierFileMsgBodies.add(dossierFileMsgBody);
				}
			}

			dossierMsgBody.setDossier(dossier);
			dossierMsgBody.setDossierTemplate(dossierTemplate);
			dossierMsgBody.setLstDossierFileMsgBody(dossierFileMsgBodies);
			dossierMsgBody.setServiceConfig(serviceConfig);
			dossierMsgBody.setServiceInfo(serviceInfo);
		}
		catch (Exception e) {
			_log.error(e);
		}

		return dossierMsgBody;
	}

	/**
	 * @param dossier
	 * @return
	 */
	public static DossierMsgBody getDossierMsgBody(Dossier dossier) {

		DossierMsgBody dossierMsgBody = new DossierMsgBody();

		try {

			DossierTemplate dossierTemplate =
				DossierTemplateLocalServiceUtil.getDossierTemplate(dossier.getDossierTemplateId());
			ServiceConfig serviceConfig =
				ServiceConfigLocalServiceUtil.getServiceConfig(dossier.getServiceConfigId());

			ServiceInfo serviceInfo =
				ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());

			List<DossierFile> dossierFiles =
				DossierFileLocalServiceUtil.getDossierFileByD_S_R(
					dossier.getDossierId(),
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC, 0);

			List<DossierFileMsgBody> dossierFileMsgBodies =
				getDossierFileMsgBody(dossierFiles);

			dossierMsgBody.setDossier(dossier);
			dossierMsgBody.setDossierTemplate(dossierTemplate);
			dossierMsgBody.setLstDossierFileMsgBody(dossierFileMsgBodies);
			dossierMsgBody.setServiceConfig(serviceConfig);
			dossierMsgBody.setServiceInfo(serviceInfo);
		}
		catch (Exception e) {
			_log.error(e);
		}

		return dossierMsgBody;
	}

	/**
	 * @param paymentFile
	 * @return
	 */
	public static PaymentFileMsgBody getPaymentFileMsgBody(
		PaymentFile paymentFile) {

		PaymentFileMsgBody paymentFileMsgBody = new PaymentFileMsgBody();

		try {
			paymentFileMsgBody.setPaymentMethod(paymentFile.getPaymentMethod());
			paymentFileMsgBody.setConfirmDatetime(paymentFile.getConfirmDatetime());
			paymentFileMsgBody.setConfirmNote(StringPool.BLANK);
			paymentFileMsgBody.setApproveDatetime(paymentFile.getApproveDatetime());
			paymentFileMsgBody.setAccountUserName(paymentFile.getAccountUserName());
			paymentFileMsgBody.setApproveNote(paymentFile.getApproveNote());
			paymentFileMsgBody.setGovAgencyTaxNo(paymentFile.getGovAgencyTaxNo());
			paymentFileMsgBody.setInvoiceTemplateNo(paymentFile.getInvoiceTemplateNo());
			paymentFileMsgBody.setInvoiceIssueNo(paymentFile.getInvoiceIssueNo());
			paymentFileMsgBody.setInvoiceNo(paymentFile.getInvoiceNo());
			paymentFileMsgBody.setSyncStatus(paymentFile.getSyncStatus());
			paymentFileMsgBody.setOid(paymentFile.getOid());

			if (paymentFile.getConfirmFileEntryId() != 0) {
				DLFileEntry dlFileEntry =
					DLFileEntryUtil.getDLFileEntry(paymentFile.getConfirmFileEntryId());

				byte[] confirmFileEntry =
					JMSMessageUtil.convertInputStreamToByteArray(dlFileEntry.getContentStream());

				paymentFileMsgBody.setConfirmFileEntry(confirmFileEntry);

				paymentFileMsgBody.setExtension(dlFileEntry.getExtension());
				paymentFileMsgBody.setFileDescription(dlFileEntry.getDescription());
				paymentFileMsgBody.setFileName(dlFileEntry.getName());
				paymentFileMsgBody.setFileTitle(dlFileEntry.getTitle());
				paymentFileMsgBody.setMimeType(dlFileEntry.getMimeType());

			}

		}
		catch (Exception e) {
			_log.error(e);
		}

		return paymentFileMsgBody;
	}

	/**
	 * @param dossierFiles
	 * @return
	 */
	public static List<DossierFileMsgBody> getDossierFileMsgBody(
		List<DossierFile> dossierFiles) {

		List<DossierFileMsgBody> dossierFileMsgBodies =
			new ArrayList<DossierFileMsgBody>();

		try {

			if (dossierFiles != null) {
				for (DossierFile dossierFile : dossierFiles) {
					DossierFileMsgBody dossierFileMsgBody =
						new DossierFileMsgBody();

					if (dossierFile.getGroupFileId() > 0) {
						FileGroup fileGroup =
							FileGroupLocalServiceUtil.getFileGroup(dossierFile.getGroupFileId());
						dossierFileMsgBody.setFileGroup(fileGroup);
						DossierPart fileGroupDossierPart =
							DossierPartLocalServiceUtil.getDossierPart(fileGroup.getDossierPartId());
						dossierFileMsgBody.setFileGroupDossierPart(fileGroupDossierPart);
					}

					DossierPart dossierPart =
						DossierPartLocalServiceUtil.getDossierPart(dossierFile.getDossierPartId());

					if (dossierFile.getFileEntryId() > 0) {
						DLFileEntry dlFileEntry =
							DLFileEntryUtil.getDLFileEntry(dossierFile.getFileEntryId());
						dossierFileMsgBody.setFileDescription(dlFileEntry.getDescription());
						dossierFileMsgBody.setExtension(dlFileEntry.getExtension());
						dossierFileMsgBody.setFileName(dlFileEntry.getName());
						dossierFileMsgBody.setFileTitle(dlFileEntry.getTitle());
						dossierFileMsgBody.setMimeType(dlFileEntry.getMimeType());

						byte[] bytes =
							JMSMessageUtil.convertInputStreamToByteArray(dlFileEntry.getContentStream());

						dossierFileMsgBody.setBytes(bytes);

					}

					dossierFileMsgBody.setDossierPart(dossierPart);

					dossierFileMsgBody.setDossierFile(dossierFile);

					dossierFileMsgBodies.add(dossierFileMsgBody);
				}
			}

		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return dossierFileMsgBodies;

	}

	public static class AnalyzeDossierFile {

		public AnalyzeDossierFile(List<DossierFileMsgBody> dossierFileMsgBodies) {

			if (Validator.isNotNull(dossierFileMsgBodies)) {
				LinkedHashMap<DossierFile, DossierPart> syncDossierFiles =
					new LinkedHashMap<DossierFile, DossierPart>();

				LinkedHashMap<String, DLFileEntry> syncDLFileEntries =
					new LinkedHashMap<String, DLFileEntry>();

				LinkedHashMap<String, byte[]> data =
					new LinkedHashMap<String, byte[]>();

				LinkedHashMap<String, FileGroup> syncFileGroups =
					new LinkedHashMap<String, FileGroup>();

				LinkedHashMap<Long, DossierPart> syncFileGroupDossierParts =
					new LinkedHashMap<Long, DossierPart>();

				for (DossierFileMsgBody dossierFileMsgBody : dossierFileMsgBodies) {
					DossierFile syncDossierFile =
						dossierFileMsgBody.getDossierFile();

					syncDossierFiles.put(
						syncDossierFile, dossierFileMsgBody.getDossierPart());

					data.put(
						syncDossierFile.getOid(), dossierFileMsgBody.getBytes());

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

						syncFileGroups.put(
							syncDossierFile.getOid(), syncFileGroup);

						syncFileGroupDossierParts.put(
							syncFileGroup.getFileGroupId(),
							dossierFileMsgBody.getFileGroupDossierPart());
					}

				}

				this.setData(data);
				this.setSyncDLFileEntries(syncDLFileEntries);
				this.setSyncDossierFiles(syncDossierFiles);
				this.setSyncFileGroupDossierParts(syncFileGroupDossierParts);
				this.setSyncFileGroups(syncFileGroups);
			}
		}

		public LinkedHashMap<DossierFile, DossierPart> getSyncDossierFiles() {

			return _syncDossierFiles;
		}

		public void setSyncDossierFiles(
			LinkedHashMap<DossierFile, DossierPart> syncDossierFiles) {

			this._syncDossierFiles = syncDossierFiles;
		}

		public LinkedHashMap<String, FileGroup> getSyncFileGroups() {

			return _syncFileGroups;
		}

		public void setSyncFileGroups(
			LinkedHashMap<String, FileGroup> syncFileGroups) {

			this._syncFileGroups = syncFileGroups;
		}

		public LinkedHashMap<Long, DossierPart> getSyncFileGroupDossierParts() {

			return _syncFileGroupDossierParts;
		}

		public void setSyncFileGroupDossierParts(
			LinkedHashMap<Long, DossierPart> syncFileGroupDossierParts) {

			this._syncFileGroupDossierParts = syncFileGroupDossierParts;
		}

		public LinkedHashMap<String, DLFileEntry> getSyncDLFileEntries() {

			return _syncDLFileEntries;
		}

		public void setSyncDLFileEntries(
			LinkedHashMap<String, DLFileEntry> syncDLFileEntries) {

			this._syncDLFileEntries = syncDLFileEntries;
		}

		public LinkedHashMap<String, byte[]> getData() {

			return _data;
		}

		public void setData(LinkedHashMap<String, byte[]> data) {

			this._data = data;
		}

		protected LinkedHashMap<DossierFile, DossierPart> _syncDossierFiles;

		protected LinkedHashMap<String, FileGroup> _syncFileGroups;

		protected LinkedHashMap<Long, DossierPart> _syncFileGroupDossierParts;

		protected LinkedHashMap<String, DLFileEntry> _syncDLFileEntries;

		protected LinkedHashMap<String, byte[]> _data;
	}

	private static Log _log =
		LogFactoryUtil.getLog(DossierMsgBody.class.getName());

}
