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

package org.opencps.processmgt.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

import org.opencps.accountmgt.NoSuchAccountException;
import org.opencps.accountmgt.NoSuchAccountFolderException;
import org.opencps.accountmgt.NoSuchAccountOwnOrgIdException;
import org.opencps.accountmgt.NoSuchAccountOwnUserIdException;
import org.opencps.accountmgt.NoSuchAccountTypeException;
import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.backend.util.DossierNoGenerator;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.dossiermgt.DuplicateFileGroupException;
import org.opencps.dossiermgt.EmptyFileGroupException;
import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.NoSuchDossierFileException;
import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.NoSuchDossierTemplateException;
import org.opencps.dossiermgt.PermissionDossierException;
import org.opencps.dossiermgt.RequiredDossierPartException;
import org.opencps.dossiermgt.bean.AccountBean;
import org.opencps.dossiermgt.bean.ProcessOrderBean;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.search.DossierDisplayTerms;
import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierFileLogLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.FileGroupLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.dossiermgt.util.ActorBean;
import org.opencps.jasperreport.util.JRReportUtil;
import org.opencps.jasperreport.util.JRReportUtil.DocType;
import org.opencps.pki.HashAlgorithm;
import org.opencps.pki.Helper;
import org.opencps.pki.PdfPkcs7Signer;
import org.opencps.processmgt.NoSuchWorkflowOutputException;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessStepDossierPart;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.model.StepAllowance;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.processmgt.service.StepAllowanceLocalServiceUtil;
import org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil;
import org.opencps.processmgt.util.ProcessOrderUtils;
import org.opencps.processmgt.util.ProcessUtils;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.usermgt.model.Employee;
import org.opencps.util.AccountUtil;
import org.opencps.util.BCYSignatureUtil;
import org.opencps.util.DLFileEntryUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PDFUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;
import org.opencps.util.SignatureUtil;
import org.opencps.util.WebKeys;

import vgca.svrsigner.ServerSigner;

import com.liferay.portal.RolePermissionsException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */

public class ProcessOrderPortlet extends MVCPortlet {

	private Log _log = LogFactoryUtil.getLog(ProcessOrderPortlet.class
			.getName());

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void addAttachmentFile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		boolean updated = false;

		/*
		 * AccountBean accountBean =
		 * AccountUtil.getAccountBeanFromAttribute(actionRequest);
		 */

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		UploadPortletRequest uploadPortletRequest = PortalUtil
				.getUploadPortletRequest(actionRequest);

		Dossier dossier = null;
		DossierFile dossierFile = null;
		DossierPart dossierPart = null;

		long dossierId = ParamUtil.getLong(uploadPortletRequest,
				DossierDisplayTerms.DOSSIER_ID);

		long dossierFileId = ParamUtil.getLong(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		long dossierPartId = ParamUtil.getLong(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		long fileGroupId = ParamUtil.getLong(uploadPortletRequest,
				DossierDisplayTerms.FILE_GROUP_ID);

		long size = uploadPortletRequest
				.getSize(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		int dossierFileType = ParamUtil.getInteger(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_TYPE);

		int dossierFileOriginal = ParamUtil.getInteger(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);

		String displayName = ParamUtil.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DISPLAY_NAME);

		String dossierFileNo = ParamUtil.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_NO);

		String dossierFileDate = ParamUtil.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_DATE);

		String sourceFileName = uploadPortletRequest
				.getFileName(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		/*
		 * sourceFileName = sourceFileName
		 * .concat(PortletConstants.TEMP_RANDOM_SUFFIX).concat(StringUtil
		 * .randomString());
		 */

		String redirectURL = ParamUtil.getString(uploadPortletRequest,
				"redirectURL");

		InputStream inputStream = null;

		Date fileDate = DateTimeUtil.convertStringToDate(dossierFileDate);

		try {
			dossier = DossierLocalServiceUtil.getDossier(dossierId);

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(uploadPortletRequest);
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			validateAddAttachDossierFile(dossierId, dossierPartId,
					dossierFileId, displayName, size, sourceFileName,
					inputStream, accountBean);
			inputStream = uploadPortletRequest
					.getFileAsStream(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			String filePath = uploadPortletRequest.getFile(
					DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD).getPath();

			File file = new File(filePath);

			System.out.println("#########################################"
					+ filePath);

			System.out.println(file.getPath());

			String extension = FileUtil.getExtension(sourceFileName);

			int signCheck = SignatureUtil.getSignCheck(filePath, extension);

			String signinfo = SignatureUtil.getSignInfo(filePath, extension);

			if (dossierFileId > 0) {
				dossierFile = DossierFileLocalServiceUtil
						.getDossierFile(dossierFileId);
			}

			dossierPart = DossierPartLocalServiceUtil
					.getDossierPart(dossierPartId);

			String contentType = uploadPortletRequest
					.getContentType(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			/*
			 * DLFolder dossierFolder = DLFolderUtil.getDossierFolder(
			 * serviceContext.getScopeGroupId(), dossier.getUserId(),
			 * dossier.getCounter(), serviceContext);
			 */

			DossierFileLocalServiceUtil.addDossierFile(
					serviceContext.getUserId(), dossierId, dossierPartId,
					dossierPart.getTemplateFileNo(), StringPool.BLANK,
					fileGroupId, 0, accountBean.getOwnerUserId(),
					accountBean.getOwnerOrganizationId(), displayName,
					StringPool.BLANK,
					dossierFile != null ? dossierFile.getFileEntryId() : 0,
					PortletConstants.DOSSIER_FILE_MARK_UNKNOW, dossierFileType,
					dossierFileNo, fileDate, dossierFileOriginal,
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					dossier.getFolderId(), sourceFileName, contentType,
					displayName, StringPool.BLANK, StringPool.BLANK,
					inputStream, size, signCheck, signinfo, serviceContext);

			// Add DossierLog for Add File

			int actor = 0;

			if (accountBean.isEmployee()) {
				actor = 2;
			} else if (accountBean.isBusiness() || accountBean.isCitizen()) {
				actor = 1;
			}

			ActorBean actorBean = new ActorBean(actor,
					serviceContext.getUserId());

			ProcessStep processStep = BackendUtils
					.getProcessStepByDossierId(dossierId);

			DossierFileLogLocalServiceUtil.addFileLog(serviceContext
					.getUserId(), actorBean.getActorName(), dossierId,
					fileGroupId, processStep.getProcessStepId(), false,
					displayName, 1, StringPool.BLANK,
					PortletConstants.DOSSIER_FILE_ADD,
					dossierFile != null ? dossierFile.getFileEntryId() : 0,
					WebKeys.ACTOR_EMPLOYEE);

			/*
			 * DossierLogLocalServiceUtil.addDossierLog(
			 * serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			 * serviceContext.getCompanyId(), dossierId, fileGroupId,
			 * dossier.getDossierStatus(),
			 * PortletConstants.DOSSIER_ACTION_ADD_ATTACHMENT_FILE,
			 * PortletConstants.DOSSIER_ACTION_ADD_ATTACHMENT_FILE +
			 * StringPool.SPACE + StringPool.COLON + StringPool.SPACE +
			 * displayName, new Date(), 0, 0, actorBean.getActor(),
			 * actorBean.getActorId(), actorBean.getActorName(),
			 * ProcessOrderPortlet.class.getName() + ".addAttachmentFile()");
			 */
			updated = true;

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			updated = false;
			if (e instanceof DuplicateFileException) {
				SessionErrors.add(actionRequest, DuplicateFileException.class);
			} else if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			} else if (e instanceof NoSuchDossierPartException) {
				SessionErrors.add(actionRequest,
						NoSuchDossierPartException.class);
			} else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			} else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountTypeException.class);
			} else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountFolderException.class);
			} else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnUserIdException.class);
			} else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnOrgIdException.class);
			} else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
						PermissionDossierException.class);
			} else if (e instanceof FileSizeException) {
				SessionErrors.add(actionRequest, FileSizeException.class);
			} else if (e instanceof RolePermissionsException) {
				SessionErrors
						.add(actionRequest, RolePermissionsException.class);
			} else {
				SessionErrors.add(actionRequest, "upload-error");

			}
			_log.error(e);
		} finally {
			if (updated) {
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			} else {
				actionResponse.setRenderParameter("redirectURL", redirectURL);
				actionResponse.setRenderParameter("content", "upload-file");
				actionResponse
						.setRenderParameter("jspPage",
								"/html/portlets/processmgt/processorder/modal_dialog.jsp");

			}

		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void addIndividualPartGroup(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		boolean updated = false;

		long dossierId = ParamUtil.getLong(actionRequest,
				DossierDisplayTerms.DOSSIER_ID);

		long dossierPartId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		String partName = ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.PART_NAME);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		try {
			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);
			valiadateFileGroup(dossierId, partName);
			FileGroupLocalServiceUtil.addFileGroup(serviceContext.getUserId(),
					dossierId, dossierPartId, partName,
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					serviceContext);

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			updated = false;

			if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			} else if (e instanceof EmptyFileGroupException) {
				SessionErrors.add(actionRequest, EmptyFileGroupException.class);
			} else if (e instanceof DuplicateFileGroupException) {
				SessionErrors.add(actionRequest,
						DuplicateFileGroupException.class);
			} else {
				SessionErrors.add(actionRequest,
						MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
			}

			_log.error(e);
		} finally {
			if (updated) {
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			} else {
				actionResponse.setRenderParameter("redirectURL", redirectURL);
				actionResponse.setRenderParameter("content", "individual");
				actionResponse
						.setRenderParameter("jspPage",
								"/html/portlets/processmgt/processorder/modal_dialog.jsp");

			}
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */

	public void assignToUser(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		Dossier dossier = null;

		long assignToUserId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID);

		long dossierId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.DOSSIER_ID);

		long groupId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.GROUP_ID);

		long companyId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.COMPANY_ID);

		long fileGroupId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.FILE_GROUP_ID);
		long processOrderId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
		long actionUserId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.ACTION_USER_ID);
		long processWorkflowId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID);

		long processStepId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_STEP_ID);

		String paymentValue = ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.PAYMENTVALUE);

		String estimateDate = ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.ESTIMATE_DATE);

		String estimateTime = ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.ESTIMATE_TIME);

		Date receiveDate = ParamUtil.getDate(actionRequest,
				ProcessOrderDisplayTerms.RECEIVE_DATE, sdf);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String backURL = ParamUtil.getString(actionRequest, "backURL");

		String actionNote = ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.ACTION_NOTE);
		String event = ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.EVENT);

		String assignFormDisplayStyle = ParamUtil.getString(actionRequest,
				"assignFormDisplayStyle");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		boolean signature = ParamUtil.getBoolean(actionRequest,
				ProcessOrderDisplayTerms.SIGNATURE);

		boolean sending = false;

		Date deadline = null;

		if (Validator.isNotNull(estimateDate)) {
			deadline = DateTimeUtil
					.convertStringToFullDate(estimateDate + StringPool.SPACE
							+ estimateTime + StringPool.COLON + "00");
		}

		try {

			dossier = DossierLocalServiceUtil.getDossier(dossierId);

			validateAssignTask(dossier.getDossierId(), processWorkflowId,
					processStepId);

			Message message = new Message();

			SendToEngineMsg sendToEngineMsg = new SendToEngineMsg();

			// sendToEngineMsg.setAction(WebKeys.ACTION);
			sendToEngineMsg.setActionNote(actionNote);
			sendToEngineMsg.setEvent(event);
			sendToEngineMsg.setGroupId(groupId);
			sendToEngineMsg.setCompanyId(companyId);
			sendToEngineMsg.setAssignToUserId(assignToUserId);
			sendToEngineMsg.setActionUserId(actionUserId);
			sendToEngineMsg.setDossierId(dossierId);
			sendToEngineMsg.setEstimateDatetime(deadline);
			sendToEngineMsg.setFileGroupId(fileGroupId);
			sendToEngineMsg.setPaymentValue(GetterUtil.getDouble(paymentValue));
			sendToEngineMsg.setProcessOrderId(processOrderId);
			sendToEngineMsg.setProcessWorkflowId(processWorkflowId);
			sendToEngineMsg.setReceptionNo(Validator.isNotNull(dossier
					.getReceptionNo()) ? dossier.getReceptionNo()
					: StringPool.BLANK);
			sendToEngineMsg.setSignature(signature ? 1 : 0);
			sendToEngineMsg.setDossierStatus(dossier.getDossierStatus());
			sendToEngineMsg.setActionDatetime(new Date());
			sendToEngineMsg.setActorType(WebKeys.DOSSIER_ACTOR_EMPLOYEE);
			sendToEngineMsg.setReceiveDate(receiveDate);

			message.put("msgToEngine", sendToEngineMsg);

			MessageBusUtil.sendMessage("opencps/backoffice/engine/destination",
					message);

			List<WorkflowOutput> workflowOutputs = WorkflowOutputLocalServiceUtil
					.getByProcessWF(processWorkflowId);

			// Lat co trang trai sau khi gui thanh cong len jms va
			// engine
			DossierFileLocalServiceUtil.updateDossierFileResultSyncStatus(0,
					dossierId,
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC, 0,
					workflowOutputs);

			sending = true;

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

			if (assignFormDisplayStyle.equals("form")) {
				jsonObject.put("msg", MessageKeys.DEFAULT_SUCCESS_KEY);
			}

		} catch (Exception e) {
			sending = false;
			if (e instanceof NoSuchDossierException
					|| e instanceof NoSuchWorkflowOutputException
					|| e instanceof RequiredDossierPartException) {

				SessionErrors.add(actionRequest, e.getClass());

				if (assignFormDisplayStyle.equals("form")) {
					jsonObject.put("msg", e.getClass().getName());
				}
			} else {
				SessionErrors.add(actionRequest,
						MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
				if (assignFormDisplayStyle.equals("form")) {
					jsonObject.put("msg",
							MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
				}
			}

			_log.error(e);
		} finally {

			if (assignFormDisplayStyle.equals("popup") && sending) {

				actionResponse
						.setRenderParameter("jspPage",
								"/html/portlets/processmgt/processorder/assign_to_user.jsp");
				actionResponse.setRenderParameter("backURL", backURL);

			} else if (assignFormDisplayStyle.equals("popup") && !sending) {

				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			} else {

				PortletUtil
						.writeJSON(actionRequest, actionResponse, jsonObject);
			}
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void cloneDossierFile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		/*
		 * AccountBean accountBean =
		 * AccountUtil.getAccountBeanFromAttribute(actionRequest);
		 */

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		Dossier dossier = null;
		DossierFile dossierFile = null;
		DossierPart dossierPart = null;

		boolean updated = false;

		long cloneDossierFileId = ParamUtil.getLong(actionRequest,
				"cloneDossierFileId");

		long dossierId = ParamUtil.getLong(actionRequest,
				DossierDisplayTerms.DOSSIER_ID);

		long dossierPartId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		long groupDossierPartId = ParamUtil.getLong(actionRequest,
				"groupDossierPartId");

		long fileGroupId = ParamUtil.getLong(actionRequest,
				DossierDisplayTerms.FILE_GROUP_ID);

		String groupName = ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.GROUP_NAME);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		try {

			validateCloneDossierFile(dossierId, dossierPartId,
					cloneDossierFileId, accountBean);

			dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(cloneDossierFileId);

			long fileEntryId = dossierFile.getFileEntryId();

			FileEntry fileEntry = DLAppServiceUtil.getFileEntry(fileEntryId);

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			dossier = DossierLocalServiceUtil.getDossier(dossierId);

			dossierPart = DossierPartLocalServiceUtil
					.getDossierPart(dossierPartId);

			DossierFileLocalServiceUtil.addDossierFile(
					serviceContext.getUserId(), dossierId, dossierPartId,
					dossierPart.getTemplateFileNo(), groupName, fileGroupId,
					groupDossierPartId, accountBean.getOwnerUserId(),
					accountBean.getOwnerOrganizationId(),
					dossierFile.getDisplayName(), StringPool.BLANK,
					dossierFile != null ? dossierFile.getFileEntryId() : 0,
					PortletConstants.DOSSIER_FILE_MARK_UNKNOW,
					dossierFile.getDossierFileType(),
					dossierFile.getDossierFileNo(),
					dossierFile.getDossierFileDate(),
					dossierFile.getOriginal(),
					PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
					dossier.getFolderId(), fileEntry.getTitle()
							+ StringPool.PERIOD + fileEntry.getExtension(),
					fileEntry.getMimeType(), fileEntry.getTitle(),
					StringPool.BLANK, StringPool.BLANK,
					fileEntry.getContentStream(), fileEntry.getSize(),
					dossierFile.getSignCheck(), dossierFile.getSignInfo(),
					serviceContext);

			// TODO add dossierLog for payment file

			int actor = 0;

			if (accountBean.isEmployee()) {
				actor = 2;
			} else if (accountBean.isBusiness() || accountBean.isCitizen()) {
				actor = 1;
			}

			ActorBean actorBean = new ActorBean(actor,
					serviceContext.getUserId());

			ProcessStep processStep = BackendUtils
					.getProcessStepByDossierId(dossierId);

			DossierFileLogLocalServiceUtil.addFileLog(serviceContext
					.getUserId(), actorBean.getActorName(), dossierId,
					fileGroupId, processStep.getProcessStepId(), false,
					fileEntry.getTitle(), 1, StringPool.BLANK,
					PortletConstants.DOSSIER_FILE_ADD,
					dossierFile != null ? dossierFile.getFileEntryId() : 0,
					WebKeys.ACTOR_CITIZEN);

			/*
			 * DossierLogLocalServiceUtil .addDossierLog(
			 * serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			 * serviceContext.getCompanyId(), dossierId, fileGroupId,
			 * dossier.getDossierStatus(),
			 * PortletConstants.DOSSIER_ACTION_CLONE_ATTACHMENT_FILE,
			 * PortletConstants.DOSSIER_ACTION_CLONE_ATTACHMENT_FILE +
			 * StringPool.SPACE + StringPool.COLON + StringPool.SPACE +
			 * dossierFile.getDisplayName(), new Date(), 0, 0,
			 * actorBean.getActor(), actorBean.getActorId(),
			 * actorBean.getActorName(), ProcessOrderPortlet.class.getName() +
			 * ".cloneDossierFile()");
			 */

			updated = true;

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			updated = false;
			if (e instanceof DuplicateFileException) {
				SessionErrors.add(actionRequest, DuplicateFileException.class);
			} else if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			} else if (e instanceof NoSuchDossierPartException) {
				SessionErrors.add(actionRequest,
						NoSuchDossierPartException.class);
			} else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			} else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountTypeException.class);
			} else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountFolderException.class);
			} else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnUserIdException.class);
			} else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnOrgIdException.class);
			} else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
						PermissionDossierException.class);
			} else if (e instanceof NoSuchFileEntryException) {
				SessionErrors
						.add(actionRequest, NoSuchFileEntryException.class);
			} else if (e instanceof RolePermissionsException) {
				SessionErrors
						.add(actionRequest, RolePermissionsException.class);
			} else {
				SessionErrors.add(actionRequest, "upload-error");

			}
			_log.error(e);

		} finally {
			if (updated) {
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			} else {
				actionResponse.setRenderParameter("redirectURL", redirectURL);
				actionResponse.setRenderParameter("content", "upload-file");
				actionResponse.setRenderParameter("tab1", "select-file");
				actionResponse
						.setRenderParameter("jspPage",
								"/html/portlets/processmgt/processorder/modal_dialog.jsp");

			}
		}

	}

	/**
	 * @param accountBean
	 * @param dossierId
	 * @param dossierFileId
	 * @param dossierPartId
	 * @param sourceFileName
	 * @param is
	 * @param size
	 * @param serviceContext
	 * @return
	 */
	protected DossierFile addSignatureFile(AccountBean accountBean,
			long dossierId, long dossierFileId, long dossierPartId,
			String sourceFileName, InputStream is, long size,
			ServiceContext serviceContext) {

		DossierFile dossierFile = null;

		DossierFile signDossierFile = null;

		try {

			dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);

			FileEntry fileEntry = DLAppServiceUtil.getFileEntry(dossierFile
					.getFileEntryId());

			signDossierFile = DossierFileLocalServiceUtil.addSignDossierFile(
					dossierFileId, true, fileEntry.getFolderId(),
					sourceFileName, fileEntry.getMimeType(),
					fileEntry.getTitle() + "signed",
					fileEntry.getDescription(), StringPool.BLANK, is, size,
					serviceContext);

			// TODO add dossierLog for payment file

			int actor = 0;

			if (accountBean.isEmployee()) {
				actor = 2;
			} else if (accountBean.isBusiness() || accountBean.isCitizen()) {
				actor = 1;
			}

			ActorBean actorBean = new ActorBean(actor,
					serviceContext.getUserId());

			long fileGroupId = 0;

			ProcessStep processStep = BackendUtils
					.getProcessStepByDossierId(dossierId);

			DossierFileLogLocalServiceUtil.addFileLog(serviceContext
					.getUserId(), actorBean.getActorName(), dossierId,
					fileGroupId, processStep.getProcessStepId(), false,
					fileEntry.getTitle(), 1, StringPool.BLANK,
					PortletConstants.DOSSIER_FILE_SIGN,
					dossierFile != null ? dossierFile.getFileEntryId() : 0,
					WebKeys.ACTOR_CITIZEN);

			/*
			 * DossierLogLocalServiceUtil .addDossierLog(
			 * serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			 * serviceContext.getCompanyId(), dossierId, fileGroupId,
			 * dossier.getDossierStatus(),
			 * PortletConstants.DOSSIER_ACTION_SIGN_FILE,
			 * PortletConstants.DOSSIER_ACTION_SIGN_FILE + StringPool.SPACE +
			 * StringPool.COLON + StringPool.SPACE +
			 * dossierFile.getDisplayName(), new Date(), 0, 0,
			 * actorBean.getActor(), actorBean.getActorId(),
			 * actorBean.getActorName(), ProcessOrderPortlet.class.getName() +
			 * ".addSignatureFile()");
			 */

		} catch (Exception e) {

			_log.error(e);

		}

		return signDossierFile;
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void createReport(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		/*
		 * ThemeDisplay themeDisplay = (ThemeDisplay)
		 * actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		 */

		/*
		 * AccountBean accountBean =
		 * AccountUtil.getAccountBeanFromAttribute(actionRequest);
		 */

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		long dossierFileId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		String sourceFileName = StringPool.BLANK;

		InputStream inputStream = null;

		File file = null;

		JSONObject responseJSON = JSONFactoryUtil.createJSONObject();

		String fileExportDir = StringPool.BLANK;

		try {
			validateCreateDynamicForm(dossierFileId, accountBean);

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			// Get dossier file
			DossierFile dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);

			// Get dossier part
			DossierPart dossierPart = DossierPartLocalServiceUtil
					.getDossierPart(dossierFile.getDossierPartId());

			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierFile
					.getDossierId());

			// Get account folder
			// DLFolder accountForlder = accountBean.getAccountFolder();

			// Get dossier folder
			/*
			 * DLFolder dosserFolder = DLFolderUtil.addFolder(
			 * themeDisplay.getUserId(), themeDisplay.getScopeGroupId(),
			 * themeDisplay.getScopeGroupId(), false,
			 * accountForlder.getFolderId(),
			 * String.valueOf(dossier.getCounter()), StringPool.BLANK, false,
			 * serviceContext);
			 */

			String formData = dossierFile.getFormData();
			String jrxmlTemplate = dossierPart.getFormReport();

			// Validate json string

			JSONFactoryUtil.createJSONObject(formData);

			String outputDestination = PortletPropsValues.OPENCPS_FILE_SYSTEM_TEMP_DIR;
			String fileName = System.currentTimeMillis() + StringPool.DASH
					+ dossierFileId + StringPool.DASH
					+ dossierPart.getDossierpartId() + ".pdf";

			fileExportDir = exportToPDFFile(jrxmlTemplate, formData, null,
					outputDestination, fileName);

			if (Validator.isNotNull(fileExportDir)) {

				file = new File(fileExportDir);
				inputStream = new FileInputStream(file);
				if (inputStream != null) {
					sourceFileName = fileExportDir.substring(
							fileExportDir.lastIndexOf(StringPool.SLASH) + 1,
							fileExportDir.length());
					String mimeType = MimeTypesUtil.getContentType(file);

					int actor = 0;

					if (accountBean.isEmployee()) {
						actor = 2;
					} else if (accountBean.isBusiness()
							|| accountBean.isCitizen()) {
						actor = 1;
					}

					ActorBean actorBean = new ActorBean(actor,
							serviceContext.getUserId());

					long fileGroupId = 0;
					// Add new version
					if (dossierFile.getFileEntryId() > 0) {
						DossierFile dossierFileAdded = DossierFileLocalServiceUtil
								.addDossierFile(dossierFile.getDossierFileId(),
										dossier.getFolderId(), sourceFileName,
										mimeType, dossierFile.getDisplayName(),
										StringPool.BLANK, StringPool.BLANK,
										inputStream, file.length(),
										serviceContext);

						ProcessStep processStep = BackendUtils
								.getProcessStepByDossierId(dossierFileAdded
										.getDossierId());

						DossierFileLogLocalServiceUtil.addFileLog(
								serviceContext.getUserId(),
								actorBean.getActorName(),
								dossierFileAdded.getDossierId(),
								fileGroupId,
								processStep.getProcessStepId(),
								false,
								dossierFileAdded.getDisplayName(),
								1,
								StringPool.BLANK,
								PortletConstants.DOSSIER_FILE_EXPORT,
								dossierFile != null ? dossierFile
										.getFileEntryId() : 0,
								WebKeys.ACTOR_CITIZEN);

					} else {
						// Update version 1
						DossierFile dossierFileAdd = DossierFileLocalServiceUtil
								.updateDossierFile(dossierFileId,
										dossier.getFolderId(), sourceFileName,
										mimeType, dossierFile.getDisplayName(),
										StringPool.BLANK, StringPool.BLANK,
										inputStream, file.length(),
										serviceContext);

						ProcessStep processStep = BackendUtils
								.getProcessStepByDossierId(dossierFileAdd
										.getDossierId());

						DossierFileLogLocalServiceUtil.addFileLog(
								serviceContext.getUserId(),
								actorBean.getActorName(),
								dossierFileAdd.getDossierId(),
								fileGroupId,
								processStep.getProcessStepId(),
								false,
								dossierFileAdd.getDisplayName(),
								1,
								StringPool.BLANK,
								PortletConstants.DOSSIER_FILE_SIGN,
								dossierFile != null ? dossierFile
										.getFileEntryId() : 0,
								WebKeys.ACTOR_CITIZEN);

					}
				}
			}

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			if (e instanceof NoSuchDossierFileException) {
				SessionErrors.add(actionRequest,
						NoSuchDossierFileException.class);
			} else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			} else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountTypeException.class);
			} else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountFolderException.class);
			} else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnUserIdException.class);
			} else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnOrgIdException.class);
			} else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
						PermissionDossierException.class);
			} else if (e instanceof DuplicateFileException) {
				SessionErrors.add(actionRequest, DuplicateFileException.class);
			} else if (e instanceof RolePermissionsException) {
				SessionErrors
						.add(actionRequest, RolePermissionsException.class);
			} else {
				SessionErrors.add(actionRequest, PortalException.class);
			}

			_log.error(e);
		} finally {
			responseJSON.put("fileExportDir", fileExportDir);
			PortletUtil.writeJSON(actionRequest, actionResponse, responseJSON);
			if (inputStream != null) {
				inputStream.close();
			}

			if (file.exists()) {
				file.delete();
			}
		}
	}

	public void deleteAttachmentFile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		long dossierFileId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);
		DossierFile dossierFile = null;
		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		JSONObject jsonObject = null;

		try {
			if (dossierFileId > 0) {
				ServiceContext serviceContext = ServiceContextFactory
						.getInstance(actionRequest);

				jsonObject = JSONFactoryUtil.createJSONObject();
				dossierFile = DossierFileLocalServiceUtil
						.getDossierFile(dossierFileId);

				Dossier dossier = DossierLocalServiceUtil
						.fetchDossier(dossierFile.getDossierId());

				long fileEntryId = dossierFile.getFileEntryId();
				DossierFileLocalServiceUtil.deleteDossierFile(dossierFileId,
						fileEntryId);
				jsonObject.put("deleted", Boolean.TRUE);

				SessionMessages.add(actionRequest,
						MessageKeys.DEFAULT_SUCCESS_KEY);

				int actor = 0;

				if (accountBean.isEmployee()) {
					actor = 2;
				} else if (accountBean.isBusiness() || accountBean.isCitizen()) {
					actor = 1;
				}

				ActorBean actorBean = new ActorBean(actor,
						serviceContext.getUserId());

				long fileGroupId = 0;

				// Add DossierLog Delete File

				ProcessStep processStep = BackendUtils
						.getProcessStepByDossierId(dossier.getDossierId());

				DossierFileLogLocalServiceUtil.addFileLog(
						serviceContext.getUserId(), actorBean.getActorName(),
						dossier.getDossierId(), fileGroupId,
						processStep.getProcessStepId(), false,
						dossierFile.getDisplayName(), 1, StringPool.BLANK,
						PortletConstants.DOSSIER_FILE_REMOVE,
						dossierFile != null ? dossierFile.getFileEntryId() : 0,
						WebKeys.ACTOR_CITIZEN);

				/*
				 * DossierLogLocalServiceUtil.addDossierLog(
				 * serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				 * serviceContext.getCompanyId(), dossierFile.getDossierId(),
				 * fileGroupId, dossier.getDossierStatus(),
				 * PortletConstants.DOSSIER_ACTION_REMOVE_ATTACTMENT_FILE,
				 * PortletConstants.DOSSIER_ACTION_REMOVE_ATTACTMENT_FILE +
				 * StringPool.SPACE + StringPool.COLON + StringPool.SPACE +
				 * dossierFile.getDisplayName(), new Date(), 0, 0,
				 * actorBean.getActor(), actorBean.getActorId(),
				 * actorBean.getActorName(), ProcessOrderPortlet.class.getName()
				 * + ".delDossierFile()");
				 */
			}

		} catch (Exception e) {
			jsonObject.put("deleted", Boolean.FALSE);
			_log.error(e);
		} finally {
			PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
		}

	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void hashMultipleFile(ActionRequest actionRequest,
			ActionResponse actionResponse) {

		long[] dossierFileIds = ParamUtil.getLongValues(actionRequest,
				"dossierFileIds");

		if (dossierFileIds != null && dossierFileIds.length > 0) {
			try {
				for (int i = 0; i < dossierFileIds.length; i++) {
					DossierFile dossierFile = DossierFileLocalServiceUtil
							.getDossierFile(dossierFileIds[i]);
					String tempFilePath = PDFUtil.saveAsPdf(
							PortletUtil.getTempFolderPath(actionRequest),
							dossierFile.getFileEntryId());
					System.out.println(tempFilePath);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void hashSingleFile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		JSONObject data = null;

		long dossierFileId = ParamUtil.getLong(actionRequest, "dossierFileId");

		try {

			DossierFile dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);

			data = computerHash(actionRequest, dossierFile);

		} catch (Exception e) {
			_log.error(e);
		} finally {
			if (data != null) {
				PortletUtil.writeJSON(actionRequest, actionResponse, data);
			} else {
				_log.info("################################ Can not computerHash signature");
			}

		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void signature(ActionRequest actionRequest,
			ActionResponse actionResponse) {

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		String signature = ParamUtil.getString(actionRequest, "signature");
		/*
		 * String certificate = ParamUtil .getString(actionRequest,
		 * "certificate");
		 */
		String jsonResource = ParamUtil.getString(actionRequest, "resources");

		File inputFile = null;

		File hashFile = null;

		File signFile = null;

		String inputFilePath = StringPool.BLANK;

		String outputFilePath = StringPool.BLANK;

		String hashFileTempPath = StringPool.BLANK;

		String certPath = StringPool.BLANK;

		String imagePath = StringPool.BLANK;

		String signatureFieldName = StringPool.BLANK;

		try {
			JSONObject resources = JSONFactoryUtil
					.createJSONObject(jsonResource);

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			inputFilePath = resources.getString("inputFilePath");

			outputFilePath = resources.getString("outputFilePath");

			hashFileTempPath = resources.getString("hashFileTempPath");

			certPath = resources.getString("certPath");

			imagePath = resources.getString("imagePath");

			signatureFieldName = resources.getString("signatureFieldName");

			long dossierFileId = resources.getLong("dossierFileId");
			long dossierId = resources.getLong("dossierFileId");
			long dossierPartId = resources.getLong("dossierPartId");

			PdfPkcs7Signer pdfSigner = SignatureUtil.getPdfPkcs7Signer(
					inputFilePath, certPath, hashFileTempPath, outputFilePath,
					false, imagePath);

			pdfSigner.setHashAlgorithm(HashAlgorithm.SHA1);
			pdfSigner.setSignatureFieldName(signatureFieldName);

			_log.info("********************Signature******************** "
					+ signature);

			pdfSigner.sign(Base64.decode(signature));

			signFile = new File(outputFilePath);

			inputFile = new File(inputFilePath);

			hashFile = new File(hashFileTempPath);

			InputStream is = new FileInputStream(signFile);

			addSignatureFile(accountBean, dossierId, dossierFileId,
					dossierPartId, signFile.getName(), is, signFile.length(),
					serviceContext);
			if (is != null) {
				is.close();
			}

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			_log.error(e);
		} finally {

			if (signFile != null && signFile.exists()) {
				signFile.delete();
			}

			if (inputFile != null && inputFile.exists()) {
				inputFile.delete();
			}
			if (hashFile != null && hashFile.exists()) {
				hashFile.delete();
			}

		}
	}

	/**
	 * @param actionRequest
	 * @param dossierFile
	 * @return
	 */
	protected JSONObject computerHash(ActionRequest actionRequest,
			DossierFile dossierFile) {

		JSONObject data = JSONFactoryUtil.createJSONObject();

		JSONObject resources = JSONFactoryUtil.createJSONObject();

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		String tempFolderPath = PortletUtil.getTempFolderPath(actionRequest);

		String imageFolderPath = PortletUtil
				.getResourceFolderPath(actionRequest);

		String certFolderPath = PortletUtil
				.getResourceFolderPath(actionRequest);

		String employeeEmail = ((Employee) accountBean.getAccountInstance())
				.getEmail();

		boolean isVisible = false;

		String inputFilePath = StringPool.BLANK;

		String outputFilePath = StringPool.BLANK;

		String hashFileTempPath = StringPool.BLANK;

		try {
			outputFilePath = tempFolderPath + "signed" + StringPool.DASH
					+ dossierFile.getDisplayName() + StringPool.DASH
					+ System.currentTimeMillis() + ".pdf";

			hashFileTempPath = tempFolderPath + "hash" + StringPool.DASH
					+ dossierFile.getDisplayName() + StringPool.DASH
					+ System.currentTimeMillis() + ".pdf";

			String certPath = certFolderPath + "/" + employeeEmail + ".cer";

			String imagePath = imageFolderPath + "/" + employeeEmail + ".png";

			inputFilePath = PDFUtil.saveAsPdf(tempFolderPath,
					dossierFile.getFileEntryId());

			PdfPkcs7Signer pdfSigner = SignatureUtil.getPdfPkcs7Signer(
					inputFilePath, certPath, hashFileTempPath, outputFilePath,
					isVisible, imagePath);

			// System.out.println("inputFilePath: " + inputFilePath);
			// System.out.println("hashFileTempPath: " + hashFileTempPath);
			// System.out.println("outputFilePath: " + outputFilePath);
			// System.out.println("imagePath: " + imagePath);

			pdfSigner.setHashAlgorithm(HashAlgorithm.SHA1);

			_log.info("############################################### pdfSigner.setHashAlgorithm(HashAlgorithm.SHA1)");

			byte[] hash = pdfSigner.computeHash();

			_log.info("############################################### pdfSigner.computeHash()");

			if (hash != null) {
				String hashHex = Helper.binToHex(hash);

				data.put("hashHex", hashHex);

				resources.put("inputFilePath", inputFilePath);
				resources.put("dossierFileId", dossierFile.getDossierFileId());
				resources.put("dossierId", dossierFile.getDossierId());
				resources.put("dossierPartId", dossierFile.getDossierPartId());
				resources.put("outputFilePath", outputFilePath);
				resources.put("hashFileTempPath", hashFileTempPath);
				resources.put("certPath", certPath);
				resources.put("imagePath", imagePath);

				resources.put("signatureFieldName",
						pdfSigner.getSignatureFieldName());

				data.put("resources", resources);
			} else {
				_log.info("############################################### Can not computeHash");
			}

		} catch (Exception e) {
			_log.error(e);
		}

		return data;
	}

	/**
	 * @param jrxmlTemplate
	 * @param formData
	 * @param map
	 * @param outputDestination
	 * @param fileName
	 * @return
	 */
	protected String exportToPDFFile(String jrxmlTemplate, String formData,
			Map<String, Object> map, String outputDestination, String fileName) {

		return JRReportUtil.createReportPDFFile(jrxmlTemplate, formData, map,
				outputDestination, fileName);
	}

	/**
	 * @param jrxmlTemplate
	 * @param formData
	 * @param map
	 * @param outputDestination
	 * @param fileName
	 * @param docType
	 * @return
	 */
	protected String exportReportFile(String jrxmlTemplate, String formData,
			Map<String, Object> map, String outputDestination, String fileName,
			DocType docType) {

		return JRReportUtil.createReportFile(jrxmlTemplate, formData, map,
				outputDestination, fileName, docType);
	}

	/**
	 * @param path
	 * @param renderRequest
	 * @param renderResponse
	 * @throws IOException
	 * @throws PortletException
	 */
	@Override
	protected void include(String path, RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		if (!hasPermission()) {
			path = "/html/portlets/processmgt/processorder/warning.jsp";
		}

		super.include(path, renderRequest, renderResponse);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void previewAttachmentFile(ActionRequest actionRequest,
			ActionResponse actionResponse) {

		long dossierFileId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);
		String url = DLFileEntryUtil.getDossierFileAttachmentURL(dossierFileId,
				themeDisplay);
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		// url = "http://docs.google.com/gview?url=" + url + "&embedded=true";
		jsonObject.put("url", url);
		try {
			PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
		} catch (IOException e) {
			_log.error(e);
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void previewDynamicForm(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		long dossierFileId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		HttpServletResponse response = PortalUtil
				.getHttpServletResponse(actionResponse);
		response.setContentType("text/html");

		PrintWriter writer = null;

		try {
			writer = response.getWriter();

			// Get dossier file
			DossierFile dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);

			// Get dossier part
			DossierPart dossierPart = DossierPartLocalServiceUtil
					.getDossierPart(dossierFile.getDossierPartId());

			String formData = dossierFile.getFormData();
			String jrxmlTemplate = dossierPart.getFormReport();

			// Validate json string

			JSONFactoryUtil.createJSONObject(formData);

			JRReportUtil.renderReportHTMLStream(response, writer,
					jrxmlTemplate, formData, null);

		} catch (Exception e) {
			_log.error(e);
		} finally {
			if (Validator.isNotNull(redirectURL)) {
				response.sendRedirect(redirectURL);
			}

		}

	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void removeAttachmentFile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		long dossierFileId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		_log.info("DOSSIER_FILE////////////////////////" + dossierFileId);
		;

		JSONObject jsonObject = null;

		try {

			AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);

			DossierFile dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);

			Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierFile
					.getDossierId());

			DossierPart dossierPart = DossierPartLocalServiceUtil
					.getDossierPart(dossierFile.getDossierPartId());
			jsonObject = JSONFactoryUtil.createJSONObject();
			if (dossierFileId > 0
					&& dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_OTHER) {

				_log.info("DELETE DOSSIER FILE " + dossierFileId);
				;

				if (dossierFile.getSyncStatus() != PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS) {

					DossierFileLocalServiceUtil.deleteDossierFile(
							dossierFileId, dossierFile.getFileEntryId());
				} else {
					DossierFileLocalServiceUtil
							.removeDossierFile(dossierFileId);

				}

			} else {

				DossierFileLocalServiceUtil.deleteDossierFile(dossierFileId,
						dossierFile.getFileEntryId());

			}

			jsonObject.put("deleted", Boolean.TRUE);

			// Add dossierLog for removeDossierFile

			int actor = 0;

			if (accountBean.isEmployee()) {
				actor = 2;
			} else if (accountBean.isBusiness() || accountBean.isCitizen()) {
				actor = 1;
			}

			ActorBean actorBean = new ActorBean(actor,
					serviceContext.getUserId());

			long fileGroupId = 0;

			// Add DossierLog Delete File
			ProcessStep processStep = BackendUtils
					.getProcessStepByDossierId(dossier.getDossierId());

			DossierFileLogLocalServiceUtil.addFileLog(serviceContext
					.getUserId(), actorBean.getActorName(), dossier
					.getDossierId(), fileGroupId, processStep
					.getProcessStepId(), false, dossierFile.getDisplayName(),
					1, StringPool.BLANK, PortletConstants.DOSSIER_FILE_REMOVE,
					dossierFile != null ? dossierFile.getFileEntryId() : 0,
					WebKeys.ACTOR_CITIZEN);

			/*
			 * DossierLogLocalServiceUtil.addDossierLog(
			 * serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			 * serviceContext.getCompanyId(), dossierFile.getDossierId(),
			 * fileGroupId, dossier.getDossierStatus(),
			 * PortletConstants.DOSSIER_ACTION_REMOVE_ATTACTMENT_FILE,
			 * PortletConstants.DOSSIER_ACTION_REMOVE_ATTACTMENT_FILE +
			 * StringPool.SPACE + StringPool.COLON + StringPool.SPACE +
			 * dossierFile.getDisplayName(), new Date(), 0, 0,
			 * actorBean.getActor(), actorBean.getActorId(),
			 * actorBean.getActorName(), ProcessOrderPortlet.class.getName() +
			 * ".delDossierFile()");
			 */

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			jsonObject.put("deleted", Boolean.FALSE);
			_log.error(e);
		} finally {
			PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
		}

	}

	@Override
	public void render(RenderRequest renderRequest,
			RenderResponse renderResponse) throws PortletException, IOException {

		setHasPermission(true);

		// validatePermission(renderRequest, renderResponse);

		if (hasPermission()) {
			long processOrderId = ParamUtil.getLong(renderRequest,
					ProcessOrderDisplayTerms.PROCESS_ORDER_ID);

			long dossierFileId = ParamUtil.getLong(renderRequest,
					DossierFileDisplayTerms.DOSSIER_FILE_ID);

			long dossierPartId = ParamUtil.getLong(renderRequest,
					DossierFileDisplayTerms.DOSSIER_PART_ID);

			if (processOrderId > 0) {
				try {

					ProcessOrder processOrder = ProcessOrderLocalServiceUtil
							.getProcessOrder(processOrderId);

					ProcessStep processStep = ProcessStepLocalServiceUtil
							.getProcessStep(processOrder.getProcessStepId());
					Dossier dossier = DossierLocalServiceUtil
							.getDossier(processOrder.getDossierId());

					ServiceProcess serviceProcess = ServiceProcessLocalServiceUtil
							.getServiceProcess(processOrder
									.getServiceProcessId());
					ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil
							.getServiceInfo(processOrder.getServiceInfoId());
					ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil
							.getServiceConfig(dossier.getServiceConfigId());

					DossierTemplate dossierTemplate = DossierTemplateLocalServiceUtil
							.getDossierTemplate(dossier.getDossierTemplateId());

					ProcessWorkflow processWorkflow = ProcessWorkflowLocalServiceUtil
							.getProcessWorkflow(processOrder
									.getProcessWorkflowId());

					renderRequest.setAttribute(WebKeys.PROCESS_ORDER_ENTRY,
							processOrder);
					renderRequest.setAttribute(WebKeys.PROCESS_STEP_ENTRY,
							processStep);
					renderRequest.setAttribute(WebKeys.DOSSIER_ENTRY, dossier);
					renderRequest.setAttribute(WebKeys.SERVICE_PROCESS_ENTRY,
							serviceProcess);
					renderRequest.setAttribute(WebKeys.SERVICE_INFO_ENTRY,
							serviceInfo);
					renderRequest.setAttribute(WebKeys.SERVICE_CONFIG_ENTRY,
							serviceConfig);

					renderRequest.setAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY,
							dossierTemplate);

					renderRequest.setAttribute(WebKeys.PROCESS_WORKFLOW_ENTRY,
							processWorkflow);

					if (dossierFileId > 0) {
						DossierFile dossierFile = DossierFileLocalServiceUtil
								.getDossierFile(dossierFileId);

						renderRequest.setAttribute(WebKeys.DOSSIER_FILE_ENTRY,
								dossierFile);
					}

					if (dossierPartId > 0) {
						DossierPart dossierPart = DossierPartLocalServiceUtil
								.getDossierPart(dossierPartId);
						renderRequest.setAttribute(WebKeys.DOSSIER_PART_ENTRY,
								dossierPart);
					}

				}

				catch (Exception e) {
					_log.error(e.getCause());
				}

			}
		}

		super.render(renderRequest, renderResponse);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateDynamicFormData(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		/*
		 * AccountBean accountBean =
		 * AccountUtil.getAccountBeanFromAttribute(actionRequest);
		 */

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		DossierFile dossierFile = null;

		long dossierId = ParamUtil.getLong(actionRequest,
				DossierDisplayTerms.DOSSIER_ID);
		long dossierPartId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);
		long dossierFileId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);
		long fileGroupId = ParamUtil.getLong(actionRequest,
				DossierDisplayTerms.FILE_GROUP_ID);
		long groupDossierPartId = ParamUtil.getLong(actionRequest,
				"groupDossierPartId");

		long fileEntryId = 0;

		// Default value
		int dossierFileMark = PortletConstants.DOSSIER_FILE_MARK_UNKNOW;
		int dossierFileType = ParamUtil.getInteger(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_TYPE);
		int syncStatus = PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC;
		int original = PortletConstants.DOSSIER_FILE_ORIGINAL;

		String formData = ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.FORM_DATA);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String regexStr = StringPool.BLANK;
		// Default value
		String dossierFileNo = StringPool.BLANK;
		String templateFileNo = StringPool.BLANK;
		String displayName = StringPool.BLANK;
		String groupName = ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.GROUP_NAME);
		Date dossierFileDate = null;

		try {
			validateDynamicFormData(dossierId, dossierPartId, accountBean);

			DossierFile dossierFileLastest = null;

			try {
				dossierFileLastest = DossierFileLocalServiceUtil
						.getLastestDossierFile();

			} catch (Exception e) {
				// TODO: handle exception
			}

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);

			DossierPart dossierPart = DossierPartLocalServiceUtil
					.getDossierPart(dossierPartId);

			if (Validator.isNotNull(dossierPart.getTemplateFileNo())) {
				templateFileNo = dossierPart.getTemplateFileNo();
			}

			if (Validator.isNotNull(dossierPart.getPartName())) {
				displayName = dossierPart.getPartName();
			}

			if (dossierFileId > 0) {
				dossierFile = DossierFileLocalServiceUtil
						.getDossierFile(dossierFileId);
				if (Validator.isNotNull(dossierFile)) {
					DossierFileLocalServiceUtil.deleteDossierFile(
							dossierFileId, dossierFile.getFileEntryId());
					regexStr = "_16_WAR_opencpsportlet_dossierFileId="
							+ dossierFileId;
				}
			}

			List<WorkflowOutput> wfOutputs = ProcessUtils.getWfOutputPattern(
					dossierId, fileGroupId, dossierPartId);

			for (WorkflowOutput wfOutput : wfOutputs) {
				String resultPartNo = StringPool.BLANK;
				String pattern = wfOutput.getPattern();
				boolean isReset = false;

				if (Validator.isNotNull(formData)
						&& formData
								.contains(PortletConstants.DOSSIER_PART_RESULT_PATTERN_NO)
						&& Validator.isNotNull(pattern)) {
					if (Validator.isNotNull(dossierFileLastest)
							&& Validator.isNotNull(dossierFileLastest
									.getModifiedDate())) {
						isReset = PortletUtil.isResetGenerateNumber(pattern,
								dossierFileLastest.getModifiedDate());
					}

					_log.info("wfOutputId______"
							+ wfOutput.getWorkflowOutputId());
					resultPartNo = DossierNoGenerator
							.genaratorNoReceptionOption(
									pattern,
									dossierId,
									PortletConstants.DOSSIER_PART_RESULT_PATTERN,
									isReset, wfOutput.getWorkflowOutputId());

					_log.info("resultPartNo______" + resultPartNo);

					formData = StringUtil.replace(formData,
							PortletConstants.DOSSIER_PART_RESULT_PATTERN_NO,
							resultPartNo);
				}
			}

			// if (dossierFileId == 0) {
			dossierFile = DossierFileLocalServiceUtil.addDossierFile(
					serviceContext.getUserId(), dossierId, dossierPartId,
					templateFileNo, groupName, fileGroupId, groupDossierPartId,
					accountBean.getOwnerUserId(),
					accountBean.getOwnerOrganizationId(), displayName,
					formData, fileEntryId, dossierFileMark, dossierFileType,
					dossierFileNo, dossierFileDate, original, syncStatus,
					serviceContext);

			if (Validator.isNotNull(dossierFile)) {
				JSONObject sampleDataJson = JSONFactoryUtil
						.createJSONObject(dossierPart.getSampleData());

				JSONObject formDataJson = JSONFactoryUtil
						.createJSONObject(dossierFile.getFormData());

				String dossierFileNoKey = sampleDataJson
						.getString(PortletConstants.DOSSIER_FILE_NO_KEY);
				String dossierFileDateKey = sampleDataJson
						.getString(PortletConstants.DOSSIER_FILE_NO_DATE);

				dossierFile.setDossierFileNo(formDataJson
						.getString(dossierFileNoKey));
				dossierFile.setDossierFileDate(DateTimeUtil
						.convertStringToDate(formDataJson
								.getString(dossierFileDateKey)));

				DossierFileLocalServiceUtil.updateDossierFile(dossierFile);
			}
			/*
			 * } else { dossierFile = DossierFileLocalServiceUtil
			 * .getDossierFile(dossierFileId); dossierFileMark =
			 * dossierFile.getDossierFileMark(); dossierFileType =
			 * dossierFile.getDossierFileType(); syncStatus =
			 * dossierFile.getSyncStatus(); original =
			 * dossierFile.getOriginal();
			 * 
			 * dossierFileNo = Validator.isNotNull(dossierFile
			 * .getDossierFileNo()) ? dossierFile.getDossierFileNo() :
			 * StringPool.BLANK; templateFileNo =
			 * Validator.isNotNull(dossierFile .getTemplateFileNo()) ?
			 * dossierFile.getTemplateFileNo() : StringPool.BLANK; displayName =
			 * Validator.isNotNull(dossierFile.getDisplayName()) ? dossierFile
			 * .getDisplayName() : StringPool.BLANK;
			 * 
			 * dossierFile = DossierFileLocalServiceUtil.updateDossierFile(
			 * dossierFileId, serviceContext.getUserId(), dossierId,
			 * dossierPartId, templateFileNo, fileGroupId,
			 * accountBean.getOwnerUserId(),
			 * accountBean.getOwnerOrganizationId(), displayName, formData,
			 * fileEntryId, dossierFileMark, dossierFileType, dossierFileNo,
			 * dossierFileDate, original, syncStatus, serviceContext);
			 * 
			 * int actor = 0;
			 * 
			 * if (accountBean.isEmployee()) { actor = 2; } else if
			 * (accountBean.isBusiness() || accountBean.isCitizen()) { actor =
			 * 1; }
			 * 
			 * ActorBean actorBean = new ActorBean(actor,
			 * serviceContext.getUserId());
			 * 
			 * ProcessStep processStep = BackendUtils
			 * .getProcessStepByDossierId(dossierId);
			 * 
			 * DossierFileLogLocalServiceUtil.addFileLog(
			 * serviceContext.getUserId(), actorBean.getActorName(), dossierId,
			 * fileGroupId, processStep.getProcessStepId(), false, displayName,
			 * 1, StringPool.BLANK, PortletConstants.DOSSIER_FILE_UPDATE,
			 * dossierFile != null ? dossierFile.getFileEntryId() : 0,
			 * WebKeys.ACTOR_CITIZEN);
			 * 
			 * }
			 */

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			} else if (e instanceof NoSuchDossierPartException) {
				SessionErrors.add(actionRequest,
						NoSuchDossierPartException.class);
			} else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			} else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountTypeException.class);
			} else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountFolderException.class);
			} else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnUserIdException.class);
			} else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnOrgIdException.class);
			} else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
						PermissionDossierException.class);
			} else {
				SessionErrors.add(actionRequest, PortalException.class);
			}

			_log.error(e);
		} finally {
			if (Validator.isNotNull(dossierFile)
					&& Validator.isNotNull(redirectURL)
					&& Validator.isNotNull(regexStr)) {

				String newRegexStr = "_16_WAR_opencpsportlet_dossierFileId="
						+ dossierFile.getDossierFileId();

				redirectURL = redirectURL.replaceAll(regexStr, newRegexStr);

				actionResponse.sendRedirect(redirectURL);
			} else {
				actionResponse.setRenderParameter("primaryKey", String
						.valueOf(dossierFile != null ? dossierFile
								.getDossierFileId() : 0));
				actionResponse.setRenderParameter("content",
						"declaration-online");
				actionResponse
						.setRenderParameter("jspPage",
								"/html/portlets/processmgt/processorder/modal_dialog.jsp");
			}
		}
	}

	/**
	 * @param dossierId
	 * @param partName
	 * @throws NoSuchDossierException
	 * @throws EmptyFileGroupException
	 * @throws DuplicateFileGroupException
	 */
	private void valiadateFileGroup(long dossierId, String partName)
			throws NoSuchDossierException, EmptyFileGroupException,
			DuplicateFileGroupException {

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		} else if (Validator.isNull(partName.trim())) {
			throw new EmptyFileGroupException();
		}

		int count = 0;

		try {
			count = FileGroupLocalServiceUtil.countByD_DN(dossierId,
					partName.trim());
		} catch (Exception e) {
		}

		if (count > 0) {
			throw new DuplicateFileGroupException();
		}
	}

	/**
	 * @param accountBean
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws RolePermissionsException
	 * @throws PermissionDossierException
	 */
	private void validateAccount(AccountBean accountBean)
			throws NoSuchAccountTypeException, NoSuchAccountException,
			NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
			NoSuchAccountOwnOrgIdException, RolePermissionsException,
			PermissionDossierException {

		if (accountBean == null) {
			throw new NoSuchAccountException();
		} else if (Validator.isNull(accountBean.getAccountType())) {
			throw new NoSuchAccountTypeException();
		} else if (!accountBean.getAccountType().equals(
				PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
			throw new RolePermissionsException();
		} else if (!accountBean.isEmployee()) {
			throw new PermissionDossierException();
		}
	}

	/**
	 * @param dossierId
	 * @param dossierPartId
	 * @param dossierFileId
	 * @param displayName
	 * @param size
	 * @param sourceFileName
	 * @param inputStream
	 * @param accountBean
	 * @throws NoSuchDossierException
	 * @throws NoSuchDossierPartException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws PermissionDossierException
	 * @throws FileSizeException
	 * @throws RolePermissionsException
	 */
	private void validateAddAttachDossierFile(long dossierId,
			long dossierPartId, long dossierFileId, String displayName,
			long size, String sourceFileName, InputStream inputStream,
			AccountBean accountBean) throws NoSuchDossierException,
			NoSuchDossierPartException, NoSuchAccountException,
			NoSuchAccountTypeException,

			NoSuchAccountOwnOrgIdException, PermissionDossierException,
			FileSizeException, RolePermissionsException,
			NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
			RolePermissionsException {

		validateAccount(accountBean);

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		if (dossierPartId < 0) {
			throw new NoSuchDossierPartException();
		}

		try {
			DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		} catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (size == 0) {
			throw new FileSizeException();
		} else if (size > 300000000) {
			throw new FileSizeException();
		}
	}

	/**
	 * @param dossierId
	 * @param dossierPartId
	 * @param dossierFileId
	 * @param accountBean
	 * @throws NoSuchDossierException
	 * @throws NoSuchDossierPartException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws PermissionDossierException
	 * @throws FileSizeException
	 * @throws NoSuchDossierFileException
	 * @throws NoSuchFileEntryException
	 * @throws RolePermissionsException
	 */
	private void validateCloneDossierFile(long dossierId, long dossierPartId,
			long dossierFileId, AccountBean accountBean)
			throws NoSuchDossierException, NoSuchDossierPartException,
			NoSuchAccountException, NoSuchAccountTypeException,
			NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
			NoSuchAccountOwnOrgIdException, PermissionDossierException,
			NoSuchDossierFileException, NoSuchFileEntryException,
			RolePermissionsException, RolePermissionsException {

		validateAccount(accountBean);

		if (dossierFileId <= 0) {
			throw new NoSuchDossierFileException();
		}

		DossierFile dossierFile = null;

		try {
			dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);
		} catch (Exception e) {

		}

		if (dossierFile == null) {
			throw new NoSuchDossierFileException();
		}

		if (dossierFile.getFileEntryId() <= 0) {
			throw new NoSuchFileEntryException();
		}

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		if (dossierPartId < 0) {
			throw new NoSuchDossierPartException();
		}

		try {
			DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		} catch (Exception e) {
			throw new NoSuchDossierPartException();
		}
	}

	/**
	 * @param dossierFileId
	 * @param accountBean
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws NoSuchDossierFileException
	 * @throws RolePermissionsException
	 * @throws PermissionDossierException
	 */
	private void validateCreateDynamicForm(long dossierFileId,
			AccountBean accountBean) throws NoSuchAccountTypeException,
			NoSuchAccountException, NoSuchAccountFolderException,
			NoSuchAccountOwnUserIdException, NoSuchAccountOwnOrgIdException,
			NoSuchDossierFileException, RolePermissionsException,
			PermissionDossierException {

		validateAccount(accountBean);

		if (dossierFileId < 0) {
			throw new NoSuchDossierFileException();
		}

		DossierFile dossierFile = null;

		try {
			dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (dossierFile == null) {
			throw new NoSuchDossierFileException();
		}
	}

	/**
	 * @param dossierId
	 * @param dossierPartId
	 * @param accountBean
	 * @throws NoSuchAccountTypeException
	 * @throws NoSuchAccountException
	 * @throws NoSuchAccountFolderException
	 * @throws NoSuchAccountOwnUserIdException
	 * @throws NoSuchAccountOwnOrgIdException
	 * @throws NoSuchDossierException
	 * @throws NoSuchDossierPartException
	 * @throws PermissionDossierException
	 * @throws RolePermissionsException
	 */
	private void validateDynamicFormData(long dossierId, long dossierPartId,
			AccountBean accountBean) throws NoSuchAccountTypeException,
			NoSuchAccountException, NoSuchAccountFolderException,
			NoSuchAccountOwnUserIdException, NoSuchAccountOwnOrgIdException,
			NoSuchDossierException, NoSuchDossierPartException,
			PermissionDossierException, RolePermissionsException {

		validateAccount(accountBean);
		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		if (dossierPartId < 0) {
			throw new NoSuchDossierPartException();
		}

		try {
			DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		} catch (Exception e) {
			throw new NoSuchDossierPartException();
		}
	}

	/**
	 * @param dossierId
	 * @throws PortalException
	 * @throws NoSuchDossierTemplateException
	 */
	public void validateAssignTask(long dossierId, long processWorkflowId,
			long processStepId) throws PortalException {

		boolean requiredFlag = false;

		if (processWorkflowId <= 0) {
			throw new NoSuchWorkflowOutputException();
		}

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		ProcessWorkflow processWorkflow = null;

		try {
			processWorkflow = ProcessWorkflowLocalServiceUtil
					.getProcessWorkflow(processWorkflowId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		/*
		 * if (processStepId <= 0) { throw new NoSuchProcessStepException(); }
		 */
		List<WorkflowOutput> workflowOutputs = new ArrayList<WorkflowOutput>();

		List<ProcessStepDossierPart> processStepDossierParts = new ArrayList<ProcessStepDossierPart>();

		if (Validator.isNotNull(processWorkflow)) {
			processStepDossierParts = ProcessUtils
					.getDossierPartByStep(processWorkflow.getPreProcessStepId());
		}

		if (processStepDossierParts != null) {
			for (ProcessStepDossierPart processStepDossierPart : processStepDossierParts) {

				if (processStepDossierPart.getDossierPartId() > 0) {
					try {

						List<WorkflowOutput> workflowOutputsTemp = WorkflowOutputLocalServiceUtil
								.getByProcessByPWID_DPID(processWorkflowId,
										processStepDossierPart
												.getDossierPartId());

						if (workflowOutputsTemp != null) {
							workflowOutputs.addAll(workflowOutputsTemp);
						}
					} catch (Exception e) {
					}
				}

			}
		}

		if (workflowOutputs != null && !workflowOutputs.isEmpty()) {
			for (WorkflowOutput workflowOutput : workflowOutputs) {
				if (workflowOutput.getRequired()) {

					DossierFile dossierFile = null;
					try {
						DossierPart dossierPart = DossierPartLocalServiceUtil
								.getDossierPart(workflowOutput
										.getDossierPartId());
						dossierFile = DossierFileLocalServiceUtil
								.getDossierFileInUse(dossierId,
										dossierPart.getDossierpartId());
					} catch (Exception e) {
						// TODO: handle exception
					}

					if (dossierFile == null) {
						requiredFlag = true;
						break;
					}

				}
			}
		}

		if (requiredFlag) {
			throw new RequiredDossierPartException();
		}
	}

	/**
	 * @param renderRequest
	 * @param renderResponse
	 */
	private void validatePermission(RenderRequest renderRequest,
			RenderResponse renderResponse) {

		AccountBean accountBean = AccountUtil.getAccountBean(renderRequest);

		if (accountBean == null || !accountBean.isEmployee()) {
			setHasPermission(false);
			return;
		} else {

			Employee employee = (Employee) accountBean.getAccountInstance();

			ArrayList<Role> roles = accountBean.getAccountRoles();

			List<Long> roleIds = new ArrayList<Long>();

			if (roleIds != null) {
				for (Role role : roles) {

					if (!roleIds.contains(role.getRoleId())) {
						roleIds.add(role.getRoleId());
					}
				}
			}

			String processOrderIdParam = ParamUtil.getString(renderRequest,
					ProcessOrderDisplayTerms.PROCESS_ORDER_ID);

			if (Validator.isNotNull(processOrderIdParam)
					&& !Validator.isNumber(processOrderIdParam)) {
				setHasPermission(false);
				return;
			} else if (Validator.isNotNull(processOrderIdParam)
					&& Validator.isNumber(processOrderIdParam)) {
				long processOrderId = ParamUtil.getLong(renderRequest,
						ProcessOrderDisplayTerms.PROCESS_ORDER_ID);

				if (processOrderId <= 0) {
					setHasPermission(false);
					return;
				} else {
					try {

						boolean hasProcessRole = false;

						boolean hasAssigned = false;

						ProcessOrder processOrder = ProcessOrderLocalServiceUtil
								.getProcessOrder(processOrderId);

						if (processOrder.getAssignToUserId() == employee
								.getMappingUserId()) {
							hasAssigned = true;
						}

						List<StepAllowance> stepAllowances = StepAllowanceLocalServiceUtil
								.getByProcessStep(processOrder
										.getProcessStepId());

						if (stepAllowances != null) {
							for (StepAllowance stepAllowance : stepAllowances) {

								if (roleIds.contains(stepAllowance.getRoleId())) {
									hasProcessRole = true;
									break;
								}
							}
						}

						if (!hasProcessRole && !hasAssigned) {
							setHasPermission(false);
							return;
						}

					} catch (Exception e) {
						_log.info("Resource does not exist width "
								+ "processOrderId=" + processOrderId
								+ " account=" + accountBean.getAccountType());
						_hasPermission = false;
					}
				}
			}
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void menuCounterAction(ActionRequest actionRequest,
			ActionResponse actionResponse) throws PortalException,
			SystemException, IOException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		// long groupId = themeDisplay.getScopeGroupId();

		// now read your parameters, e.g. like this:
		// long someParameter = ParamUtil.getLong(request, "someParameter");

		long serviceInfoId = ParamUtil.getLong(actionRequest, "serviceInfoId");
		String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

		List<ProcessOrderBean> processOrderSteps = new ArrayList<ProcessOrderBean>();

		List<ProcessOrderBean> processOrderServices = new ArrayList<ProcessOrderBean>();

		if (tabs1.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)) {

			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
					.getProcessOrderServiceByUser(themeDisplay.getUserId());

			for (ProcessOrderBean ett : processOrderServices) {
				processOrderSteps
						.addAll((List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
								.getUserProcessStep(themeDisplay.getUserId(),
										ett.getServiceInfoId()));
			}
			if (serviceInfoId > 0) {
				processOrderSteps = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
						.getUserProcessStep(themeDisplay.getUserId(),
								serviceInfoId);
			}
		} else {

			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
					.getProcessOrderServiceJustFinishedByUser(themeDisplay
							.getUserId());

			for (ProcessOrderBean ett : processOrderServices) {
				processOrderSteps
						.addAll((List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
								.getUserProcessStep(themeDisplay.getUserId(),
										ett.getServiceInfoId()));
			}

			if (serviceInfoId > 0) {
				processOrderSteps = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
						.getUserProcessStepJustFinished(
								themeDisplay.getUserId(), serviceInfoId);
			}
		}

		long counterVal = 0;
		JSONObject obj = null;
		for (ProcessOrderBean item : processOrderSteps) {
			obj = JSONFactoryUtil.createJSONObject();

			if (tabs1
					.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)) {

				counterVal = ProcessOrderLocalServiceUtil.countProcessOrder(
						serviceInfoId, item.getProcessStepId(),
						themeDisplay.getUserId(), themeDisplay.getUserId());

			} else {

				counterVal = ProcessOrderLocalServiceUtil
						.countProcessOrderJustFinished(serviceInfoId,
								item.getProcessStepId(),
								themeDisplay.getUserId());

			}

			obj.put("code", item.getProcessStepId());
			obj.put("counter", String.valueOf(counterVal));
			jsonArray.put(obj);
		}
		jsonObject.put("badge", jsonArray);
		PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void menuCounterServiceInfoIdAction(ActionRequest actionRequest,
			ActionResponse actionResponse) throws PortalException,
			SystemException, IOException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		// long groupId = themeDisplay.getScopeGroupId();

		// now read your parameters, e.g. like this:
		// long someParameter = ParamUtil.getLong(request, "someParameter");

		// long serviceInfoId = ParamUtil.getLong(actionRequest,
		// "serviceInfoId");

		String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

		List<ProcessOrderBean> processOrderServices = new ArrayList<ProcessOrderBean>();
		if (tabs1.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)) {
			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
					.getProcessOrderServiceByUser(themeDisplay.getUserId());
		} else {
			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil
					.getProcessOrderServiceJustFinishedByUser(themeDisplay
							.getUserId());
		}

		long counterVal = 0;
		JSONObject obj = null;
		for (ProcessOrderBean item : processOrderServices) {
			obj = JSONFactoryUtil.createJSONObject();

			if (tabs1
					.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)) {

				counterVal = ProcessOrderLocalServiceUtil.countProcessOrder(
						item.getServiceInfoId(), 0, themeDisplay.getUserId(),
						themeDisplay.getUserId());

			} else {

				counterVal = ProcessOrderLocalServiceUtil
						.countProcessOrderJustFinished(item.getServiceInfoId(),
								0, themeDisplay.getUserId());

			}

			obj.put("code", item.getServiceInfoId());
			obj.put("counter", String.valueOf(counterVal));
			jsonArray.put(obj);
		}
		jsonObject.put("badge", jsonArray);
		PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void menuCounterSubStatus(ActionRequest actionRequest,
			ActionResponse actionResponse) throws PortalException,
			SystemException, IOException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();

		// now read your parameters, e.g. like this:
		// long someParameter = ParamUtil.getLong(request, "someParameter");

		// String keywords = ParamUtil.getString(actionRequest, "keywords");

		long serviceInfoId = ParamUtil.getLong(actionRequest, "serviceInfoId");
		long processStepId = ParamUtil.getLong(actionRequest, "processStepId");
		String keywords = ParamUtil.getString(actionRequest, "keywords");

		String processOrderStage = ParamUtil.getString(actionRequest,
				"processOrderStage", "false");

		String tabs1 = ParamUtil.getString(actionRequest, "tabs1");

		long counterVal = 0;
		JSONObject obj = null;
		for (DictItem item : PortletUtil.getDictItemInUseByCode(groupId,
				"DOSSIER_SUB_STATUS", PortletConstants.TREE_VIEW_ALL_ITEM)) {

			obj = JSONFactoryUtil.createJSONObject();

			if (tabs1
					.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)) {

				counterVal = ProcessOrderLocalServiceUtil
						.countProcessOrderKeyWords(serviceInfoId,
								processStepId, themeDisplay.getUserId(),
								themeDisplay.getUserId(), keywords,
								item.getItemCode(), processOrderStage);

			}
			obj.put("code", item.getItemCode());
			obj.put("counter", String.valueOf(counterVal));
			jsonArray.put(obj);
		}

		jsonObject.put("badge", jsonArray);
		PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
	}

	private boolean _hasPermission = true;

	public boolean hasPermission() {

		return _hasPermission;
	}

	public void setHasPermission(boolean hasPermission) {

		this._hasPermission = hasPermission;
	}

	// PKI old
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		try {
			String type = ParamUtil.getString(resourceRequest, "type");
			if (type != null && type.trim().equals("getComputerHash")) {
				getComputerHash(resourceRequest, resourceResponse);
			}
		} catch (Exception e) {
			_log.error(e);
		}
	}

	public void getComputerHash(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException {

		try {
			// loop for mutil file
			int symbolType = ParamUtil
					.getInteger(resourceRequest, "symbolType");
			int index = ParamUtil.getInteger(resourceRequest, "index");

			long fileId = ParamUtil.getLong(resourceRequest, "fileId");
			long dossierFileId = ParamUtil.getLong(resourceRequest,
					"dossierFileId");
			long dossierPartId = ParamUtil.getLong(resourceRequest,
					"dossierPartId");

			long indexSize = ParamUtil.getLong(resourceRequest, "indexSize");

			FileEntry fileEntry = DLFileEntryUtil.getFileEntry(fileId);

			BCYSignatureUtil.getComputerHash(resourceRequest, resourceResponse,
					fileEntry, symbolType, dossierFileId, dossierPartId, index,
					indexSize);

		} catch (Exception e) {

			_log.error(e);
		}
	}

	public void signatureBCY(ActionRequest actionRequest,
			ActionResponse actionResponse) {
		String sign = ParamUtil.getString(actionRequest, "sign");
		String signFieldName = ParamUtil.getString(actionRequest,
				"signFieldName");
		String filePath = ParamUtil.getString(actionRequest, "filePath");
		// String fileName = ParamUtil.getString(actionRequest, "fileName");
		// long dossierId = ParamUtil.getLong(actionRequest, "dossierId");
		long dossierFileId = ParamUtil.getLong(actionRequest, "dossierFileId");
		// long dossierPartId = ParamUtil.getLong(actionRequest,
		// "dossierPartId");
		File file = null;
		JSONObject jsonFeed = JSONFactoryUtil.createJSONObject();
		if (Validator.isNotNull(sign)) {
			byte[] signature = Base64.decode(sign);

			try {
				ServerSigner signer = new ServerSigner(filePath, null);
				signer.completeSign(signature, signFieldName);
				if (filePath.endsWith(".pdf")) {
					filePath = filePath.substring(0,
							filePath.lastIndexOf(".pdf"))
							+ ".signed.pdf";
				} else {
					filePath = filePath + ".signed.pdf";
				}

				file = new File(filePath);

				InputStream is = new FileInputStream(file);

				// upload to server dossierfile update
				ServiceContext serviceContext = ServiceContextFactory
						.getInstance(actionRequest);
				serviceContext.setAddGroupPermissions(true);
				serviceContext.setAddGuestPermissions(true);
				// String mimeType = MimeTypesUtil.getContentType(file);
				DossierFile dossierFile = DossierFileLocalServiceUtil
						.fetchDossierFile(dossierFileId);

				serviceContext.setAddGroupPermissions(true);
				serviceContext.setAddGuestPermissions(true);
				_log.info("serviceContext.getScopeGroupId()"
						+ serviceContext.getScopeGroupId());

				dossierFile = DossierFileLocalServiceUtil
						.getDossierFile(dossierFileId);

				FileEntry fileEntry = DLAppServiceUtil.getFileEntry(dossierFile
						.getFileEntryId());

				DossierFileLocalServiceUtil.addSignDossierFile(dossierFileId,
						true, fileEntry.getFolderId(), file.getName(),
						fileEntry.getMimeType(), fileEntry.getTitle()
								+ "signed", fileEntry.getDescription(),
						StringPool.BLANK, is, file.length(), serviceContext);

				jsonFeed.put("msg", "success");

			} catch (Exception e) {
				jsonFeed.put("msg", "error");
				_log.error(e);
			} finally {
				if (Validator.isNotNull(file) && file.exists()) {
					file.delete();
				}
				try {
					PortletUtil.writeJSON(actionRequest, actionResponse,
							jsonFeed.toString());
				} catch (Exception e) {
					_log.error(e);
				}
			}
		} else {
			_log.info("Cannot sign the file: " + filePath);
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void exportReport(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		long dossierFileId = ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		String docType = ParamUtil.getString(actionRequest, "docType");

		InputStream inputStream = null;

		File file = null;

		// JSONObject responseJSON = JSONFactoryUtil.createJSONObject();

		String fileExportDir = StringPool.BLANK;

		try {
			validateCreateDynamicForm(dossierFileId, accountBean);

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			// Get dossier file
			DossierFile dossierFile = DossierFileLocalServiceUtil
					.getDossierFile(dossierFileId);

			// Get dossier part
			DossierPart dossierPart = DossierPartLocalServiceUtil
					.getDossierPart(dossierFile.getDossierPartId());

			String formData = dossierFile.getFormData();
			String jrxmlTemplate = dossierPart.getFormReport();

			// Validate json string

			JSONFactoryUtil.createJSONObject(formData);

			String outputDestination = PortletPropsValues.OPENCPS_FILE_SYSTEM_TEMP_DIR;
			String fileName = System.currentTimeMillis() + StringPool.DASH
					+ dossierFileId + StringPool.DASH
					+ dossierPart.getDossierpartId() + docType;

			DocType type = DocType.getEnum(docType);

			fileExportDir = exportReportFile(jrxmlTemplate, formData, null,
					outputDestination, fileName, type);

			if (Validator.isNotNull(fileExportDir)) {

				file = new File(fileExportDir);
				inputStream = new FileInputStream(file);
				String mimeType = MimeTypesUtil.getContentType(file);

				PortletUtil.sendFile(actionRequest, actionResponse, fileName,
						inputStream, file.length(), mimeType);

			}

			SessionMessages.add(actionRequest, MessageKeys.DEFAULT_SUCCESS_KEY);

		} catch (Exception e) {
			if (e instanceof NoSuchDossierFileException) {
				SessionErrors.add(actionRequest,
						NoSuchDossierFileException.class);
			} else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			} else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountTypeException.class);
			} else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountFolderException.class);
			} else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnUserIdException.class);
			} else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
						NoSuchAccountOwnOrgIdException.class);
			} else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
						PermissionDossierException.class);
			} else if (e instanceof DuplicateFileException) {
				SessionErrors.add(actionRequest, DuplicateFileException.class);
			} else if (e instanceof RolePermissionsException) {
				SessionErrors
						.add(actionRequest, RolePermissionsException.class);
			} else {
				SessionErrors.add(actionRequest, PortalException.class);
			}

			_log.error(e);
		} finally {
			// responseJSON.put("fileExportDir", fileExportDir);
			// PortletUtil.writeJSON(actionRequest, actionResponse,
			// responseJSON);

			if (inputStream != null) {
				inputStream.close();
			}

			if (file.exists()) {
				file.delete();
			}
		}
	}

	public void multiAssignToUser(ActionRequest actionRequest,
			ActionResponse actionResponse) {
		String processOrderIds = ParamUtil.getString(actionRequest,
				"processOrderIds", StringPool.BLANK);

		if (Validator.isNotNull(processOrderIds)) {

			String[] orderIds = StringUtil.split(processOrderIds,
					StringPool.COMMA);
			Dossier dossier = null;

			String redirectURL = ParamUtil.getString(actionRequest,
					"redirectURL");
			String paymentValue = ParamUtil.getString(actionRequest,
					ProcessOrderDisplayTerms.PAYMENTVALUE);
			String actionNote = ParamUtil.getString(actionRequest,
					ProcessOrderDisplayTerms.ACTION_NOTE);
			String estimateDate = ParamUtil.getString(actionRequest,
					ProcessOrderDisplayTerms.ESTIMATE_DATE);
			String estimateTime = ParamUtil.getString(actionRequest,
					ProcessOrderDisplayTerms.ESTIMATE_TIME);
			long processStepId = ParamUtil.getLong(actionRequest,
					ProcessOrderDisplayTerms.PROCESS_STEP_ID);
			long assignToUserId = ParamUtil.getLong(actionRequest,
					ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID);
			boolean signature = ParamUtil.getBoolean(actionRequest,
					ProcessOrderDisplayTerms.SIGNATURE);
			ProcessWorkflow processWorkflow = null;

			Date deadline = null;

			if (Validator.isNotNull(estimateDate)) {
				deadline = DateTimeUtil.convertStringToFullDate(estimateDate
						+ StringPool.SPACE + estimateTime + StringPool.COLON
						+ "00");
			}
			try {
				ServiceContext serviceContext = ServiceContextFactory
						.getInstance(actionRequest);

				for (int i = 0; i < orderIds.length; i++) {

					ProcessOrder processOrder = ProcessOrderLocalServiceUtil
							.getProcessOrder(GetterUtil.getLong(orderIds[i]));

					long dossierId = processOrder.getDossierId();
					long fileGroupId = processOrder.getFileGroupId();
					long actionUserId = processOrder.getActionUserId();
					long processWorkflowId = processOrder
							.getProcessWorkflowId();

					processWorkflow = ProcessWorkflowLocalServiceUtil
							.getProcessWorkflow(processWorkflowId);

					Date receiveDate = ProcessOrderUtils.getRecevieDate(
							dossierId, processWorkflowId, processStepId);

					List<ProcessWorkflow> postProcessWorkflows = new ArrayList<ProcessWorkflow>();

					postProcessWorkflows = ProcessWorkflowLocalServiceUtil
							.getPostProcessWorkflow(
									processOrder.getServiceProcessId(),
									processWorkflow.getPostProcessStepId());

					String event = postProcessWorkflows.get(0).getAutoEvent();

					dossier = DossierLocalServiceUtil.getDossier(dossierId);

					validateAssignTask(dossier.getDossierId(),
							processWorkflowId, processStepId);

					Message message = new Message();

					SendToEngineMsg sendToEngineMsg = new SendToEngineMsg();

					// sendToEngineMsg.setAction(WebKeys.ACTION);
					sendToEngineMsg.setActionNote(actionNote);
					sendToEngineMsg.setEvent(event);
					sendToEngineMsg
							.setGroupId(serviceContext.getScopeGroupId());
					sendToEngineMsg.setCompanyId(serviceContext.getCompanyId());
					sendToEngineMsg.setAssignToUserId(assignToUserId);
					sendToEngineMsg.setActionUserId(actionUserId);
					sendToEngineMsg.setDossierId(dossierId);
					sendToEngineMsg.setEstimateDatetime(deadline);
					sendToEngineMsg.setFileGroupId(fileGroupId);
					sendToEngineMsg.setPaymentValue(GetterUtil
							.getDouble(paymentValue));
					sendToEngineMsg.setProcessOrderId(GetterUtil
							.getLong(orderIds[i]));
					sendToEngineMsg.setProcessWorkflowId(postProcessWorkflows
							.get(0).getProcessWorkflowId());
					sendToEngineMsg.setReceptionNo(Validator.isNotNull(dossier
							.getReceptionNo()) ? dossier.getReceptionNo()
							: StringPool.BLANK);
					sendToEngineMsg.setSignature(signature ? 1 : 0);
					sendToEngineMsg
							.setDossierStatus(dossier.getDossierStatus());
					sendToEngineMsg.setActionDatetime(new Date());
					sendToEngineMsg
							.setActorType(WebKeys.DOSSIER_ACTOR_EMPLOYEE);
					sendToEngineMsg.setReceiveDate(receiveDate);

					message.put("msgToEngine", sendToEngineMsg);

					MessageBusUtil.sendMessage(
							"opencps/backoffice/engine/destination", message);

					List<WorkflowOutput> workflowOutputs = WorkflowOutputLocalServiceUtil
							.getByProcessWF(processWorkflowId);

					// Lat co trang trai sau khi gui thanh cong len jms va
					// engine
					DossierFileLocalServiceUtil
							.updateDossierFileResultSyncStatus(
									0,
									dossierId,
									PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
									PortletConstants.DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC,
									0, workflowOutputs);

				}

				SessionMessages.add(actionRequest, "success");

				actionResponse.sendRedirect(redirectURL);
				;

			} catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void validateAssignTask(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {
		List<WorkflowOutput> workflowOutputs = new ArrayList<WorkflowOutput>();

		List<ProcessStepDossierPart> processStepDossierParts = new ArrayList<ProcessStepDossierPart>();

		JSONObject obj = JSONFactoryUtil.createJSONObject();

		JSONArray array = JSONFactoryUtil.createJSONArray();

		long dossierId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.DOSSIER_ID);

		long processStepId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_STEP_ID);

		long processWorkflowId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID);

		processStepDossierParts = ProcessUtils
				.getDossierPartByStep(processStepId);

		if (processStepDossierParts != null) {

			for (ProcessStepDossierPart processStepDossierPart : processStepDossierParts) {

				if (processStepDossierPart.getDossierPartId() > 0) {
					try {

						List<WorkflowOutput> workflowOutputsTemp = WorkflowOutputLocalServiceUtil
								.getByProcessByPWID_DPID(processWorkflowId,
										processStepDossierPart
												.getDossierPartId());

						if (workflowOutputsTemp != null) {
							workflowOutputs.addAll(workflowOutputsTemp);
						}
					} catch (Exception e) {
					}
				}

			}
		}

		if (workflowOutputs != null && !workflowOutputs.isEmpty()) {
			for (WorkflowOutput workflowOutput : workflowOutputs) {

				if (workflowOutput.getRequired()) {

					DossierFile dossierFile = null;
					DossierPart dossierPart = null;
					try {
						dossierPart = DossierPartLocalServiceUtil
								.getDossierPart(workflowOutput
										.getDossierPartId());
						dossierFile = DossierFileLocalServiceUtil
								.getDossierFileInUse(dossierId,
										dossierPart.getDossierpartId());
					} catch (Exception e) {
						// TODO: handle exception
					}

					if (dossierFile == null && dossierPart != null) {

						array.put(dossierPart.getDossierpartId());

					}
				}
			}
		}

		obj.put("arrayDossierpartIds", array);

		// Validate nhap y kien
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);
		ExpandoValue requiedActionNote = null;
		boolean requiedActionNoteValue = false;

		try {
			requiedActionNote = ExpandoValueLocalServiceUtil.getValue(
					themeDisplay.getCompanyId(), ClassNameLocalServiceUtil
							.getClassNameId(ProcessStep.class.getName()),
					ProcessStep.class.getName(), "requiedProcessActionNote",
					processWorkflowId);

			requiedActionNoteValue = requiedActionNote.getBoolean();
		} catch (Exception e) {
			//
		}

		obj.put("requiedActionNote", requiedActionNoteValue);

		if (obj != null) {
			// PortletUtil.writeJSON(actionRequest, actionResponse, array);
			PortletUtil.writeJSON(actionRequest, actionResponse, obj);
		}

	}
}
