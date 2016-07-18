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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencps.accountmgt.NoSuchAccountException;
import org.opencps.accountmgt.NoSuchAccountFolderException;
import org.opencps.accountmgt.NoSuchAccountOwnOrgIdException;
import org.opencps.accountmgt.NoSuchAccountOwnUserIdException;
import org.opencps.accountmgt.NoSuchAccountTypeException;
import org.opencps.backend.message.SendToEngineMsg;
import org.opencps.dossiermgt.DuplicateFileGroupException;
import org.opencps.dossiermgt.EmptyFileGroupException;
import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.dossiermgt.NoSuchDossierFileException;
import org.opencps.dossiermgt.NoSuchDossierPartException;
import org.opencps.dossiermgt.NoSuchDossierTemplateException;
import org.opencps.dossiermgt.PermissionDossierException;
import org.opencps.dossiermgt.RequiredDossierPartException;
import org.opencps.dossiermgt.bean.AccountBean;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.search.DossierDisplayTerms;
import org.opencps.dossiermgt.search.DossierFileDisplayTerms;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.FileGroupLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.dossiermgt.util.DossierMgtUtil;
import org.opencps.jasperreport.util.JRReportUtil;
import org.opencps.pki.HashAlgorithm;
import org.opencps.pki.Helper;
import org.opencps.pki.PdfPkcs7Signer;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.usermgt.model.Employee;
import org.opencps.util.AccountUtil;
import org.opencps.util.DLFileEntryUtil;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PDFUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;
import org.opencps.util.SignatureUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */

public class ProcessOrderPortlet extends MVCPortlet {

	private Log _log =
		LogFactoryUtil.getLog(ProcessOrderPortlet.class.getName());

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void addAttachmentFile(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		boolean updated = false;

		AccountBean accountBean =
			AccountUtil.getAccountBeanFromAttribute(actionRequest);

		UploadPortletRequest uploadPortletRequest =
			PortalUtil.getUploadPortletRequest(actionRequest);

		Dossier dossier = null;
		DossierFile dossierFile = null;
		DossierPart dossierPart = null;

		long dossierId =
			ParamUtil.getLong(uploadPortletRequest,
				DossierDisplayTerms.DOSSIER_ID);

		long dossierFileId =
			ParamUtil.getLong(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		long dossierPartId =
			ParamUtil.getLong(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		long fileGroupId =
			ParamUtil.getLong(uploadPortletRequest,
				DossierDisplayTerms.FILE_GROUP_ID);

		long size =
			uploadPortletRequest.getSize(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		int dossierFileType =
			ParamUtil.getInteger(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_TYPE);

		int dossierFileOriginal =
			ParamUtil.getInteger(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL);

		String displayName =
			ParamUtil.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DISPLAY_NAME);

		String dossierFileNo =
			ParamUtil.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_NO);

		String dossierFileDate =
			ParamUtil.getString(uploadPortletRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_DATE);

		String sourceFileName =
			uploadPortletRequest.getFileName(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

		/*
		 * sourceFileName = sourceFileName
		 * .concat(PortletConstants.TEMP_RANDOM_SUFFIX).concat(StringUtil
		 * .randomString());
		 */

		String redirectURL =
			ParamUtil.getString(uploadPortletRequest, "redirectURL");

		InputStream inputStream = null;

		Date fileDate = DateTimeUtil.convertStringToDate(dossierFileDate);

		try {
			dossier = DossierLocalServiceUtil.getDossier(dossierId);

			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(uploadPortletRequest);
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			validateAddAttachDossierFile(dossierId, dossierPartId,
				dossierFileId, displayName, size, sourceFileName, inputStream,
				accountBean);
			inputStream =
				uploadPortletRequest.getFileAsStream(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			if (dossierFileId > 0) {
				dossierFile =
					DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
			}

			dossierPart =
				DossierPartLocalServiceUtil.getDossierPart(dossierPartId);

			String contentType =
				uploadPortletRequest.getContentType(DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD);

			DLFolder dossierFolder =
				DLFolderUtil.getDossierFolder(serviceContext.getScopeGroupId(),
					dossier.getUserId(), dossier.getCounter(), serviceContext);

			DossierFileLocalServiceUtil.addDossierFile(
				serviceContext.getUserId(), dossierId, dossierPartId,
				dossierPart.getTemplateFileNo(), StringPool.BLANK, fileGroupId,
				0, accountBean.getOwnerUserId(),
				accountBean.getOwnerOrganizationId(), displayName,
				StringPool.BLANK,
				dossierFile != null ? dossierFile.getFileEntryId() : 0,
				PortletConstants.DOSSIER_FILE_MARK_UNKNOW, dossierFileType,
				dossierFileNo, fileDate, dossierFileOriginal,
				PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				dossierFolder.getFolderId(), sourceFileName, contentType,
				displayName, StringPool.BLANK, StringPool.BLANK, inputStream,
				size, serviceContext);

			updated = true;

		}
		catch (Exception e) {
			updated = false;
			if (e instanceof DuplicateFileException) {
				SessionErrors.add(actionRequest, DuplicateFileException.class);
			}
			else if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			}
			else if (e instanceof NoSuchDossierPartException) {
				SessionErrors.add(actionRequest,
					NoSuchDossierPartException.class);
			}
			else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			}
			else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountTypeException.class);
			}
			else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountFolderException.class);
			}
			else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnUserIdException.class);
			}
			else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnOrgIdException.class);
			}
			else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
					PermissionDossierException.class);
			}
			else if (e instanceof FileSizeException) {
				SessionErrors.add(actionRequest, FileSizeException.class);
			}
			else {
				SessionErrors.add(actionRequest, "upload-error");

			}
			_log.error(e);
		}
		finally {
			if (updated) {
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}
			else {
				actionResponse.setRenderParameter("redirectURL", redirectURL);
				actionResponse.setRenderParameter("content", "upload-file");
				actionResponse.setRenderParameter("jspPage",
					"/html/portlets/processmgt/processorder/modal_dialog.jsp");

			}

		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void addIndividualPartGroup(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		boolean updated = false;

		long dossierId =
			ParamUtil.getLong(actionRequest, DossierDisplayTerms.DOSSIER_ID);

		long dossierPartId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		String partName =
			ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.PART_NAME);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		try {
			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);
			valiadateFileGroup(dossierId, partName);
			FileGroupLocalServiceUtil.addFileGroup(serviceContext.getUserId(),
				dossierId, dossierPartId, partName,
				PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				serviceContext);
		}
		catch (Exception e) {
			updated = false;

			if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			}
			else if (e instanceof EmptyFileGroupException) {
				SessionErrors.add(actionRequest, EmptyFileGroupException.class);
			}
			else if (e instanceof DuplicateFileGroupException) {
				SessionErrors.add(actionRequest,
					DuplicateFileGroupException.class);
			}
			else {
				SessionErrors.add(actionRequest,
					MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
			}

			_log.error(e);
		}
		finally {
			if (updated) {
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}
			else {
				actionResponse.setRenderParameter("redirectURL", redirectURL);
				actionResponse.setRenderParameter("content", "individual");
				actionResponse.setRenderParameter("jspPage",
					"/html/portlets/processmgt/processorder/modal_dialog.jsp");

			}
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void assignToUser(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		Dossier dossier = null;

		long assignToUserId =
			ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID);

		String paymentValue =
			ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.PAYMENTVALUE);

		String estimateDatetime =
			ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.ESTIMATE_DATETIME);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String backURL = ParamUtil.getString(actionRequest, "backURL");

		long dossierId =
			ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.DOSSIER_ID);

		long groupId =
			ParamUtil.getLong(actionRequest, ProcessOrderDisplayTerms.GROUP_ID);

		long companyId =
			ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.COMPANY_ID);

		long fileGroupId =
			ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.FILE_GROUP_ID);
		long processOrderId =
			ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
		long actionUserId =
			ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.ACTION_USER_ID);
		long processWorkflowId =
			ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID);
		/*
		 * long serviceProcessId = ParamUtil.getLong(actionRequest,
		 * ProcessOrderDisplayTerms.SERVICE_PROCESS_ID); long processStepId =
		 * ParamUtil.getLong(actionRequest,
		 * ProcessOrderDisplayTerms.PROCESS_STEP_ID);
		 */

		String actionNote =
			ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.ACTION_NOTE);
		String event =
			ParamUtil.getString(actionRequest, ProcessOrderDisplayTerms.EVENT);

		boolean signature =
			ParamUtil.getBoolean(actionRequest,
				ProcessOrderDisplayTerms.SIGNATURE);

		boolean sending = false;

		Date deadline = null;

		if (Validator.isNotNull(estimateDatetime)) {
			deadline = DateTimeUtil.convertStringToDate(estimateDatetime);
		}

		try {

			dossier = DossierLocalServiceUtil.getDossier(dossierId);

			validateAssignTask(dossier.getDossierId());

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
			sendToEngineMsg.setReceptionNo(Validator.isNotNull(dossier.getReceptionNo())
				? dossier.getReceptionNo() : StringPool.BLANK);
			sendToEngineMsg.setSignature(signature ? 1 : 0);
			message.put("msgToEngine", sendToEngineMsg);
			MessageBusUtil.sendMessage("opencps/backoffice/engine/destination",
				message);

			sending = true;
		}
		catch (Exception e) {
			sending = false;
			if (e instanceof NoSuchDossierException ||
				e instanceof NoSuchDossierTemplateException ||
				e instanceof RequiredDossierPartException) {

				SessionErrors.add(actionRequest, e.getClass());
			}
			else {
				SessionErrors.add(actionRequest,
					MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED);
			}

			_log.error(e);
		}
		finally {
			if (sending) {
				actionResponse.setRenderParameter("jspPage",
					"/html/portlets/processmgt/processorder/assign_to_user.jsp");
				actionResponse.setRenderParameter("backURL", backURL);

			}
			else {
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void cloneDossierFile(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		AccountBean accountBean =
			AccountUtil.getAccountBeanFromAttribute(actionRequest);

		Dossier dossier = null;
		DossierFile dossierFile = null;
		DossierPart dossierPart = null;

		boolean updated = false;

		long cloneDossierFileId =
			ParamUtil.getLong(actionRequest, "cloneDossierFileId");

		long dossierId =
			ParamUtil.getLong(actionRequest, DossierDisplayTerms.DOSSIER_ID);

		long dossierPartId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		long groupDossierPartId =
			ParamUtil.getLong(actionRequest, "groupDossierPartId");

		long fileGroupId =
			ParamUtil.getLong(actionRequest, DossierDisplayTerms.FILE_GROUP_ID);

		String groupName =
			ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.GROUP_NAME);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		try {

			validateCloneDossierFile(dossierId, dossierPartId,
				cloneDossierFileId, accountBean);

			dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(cloneDossierFileId);

			long fileEntryId = dossierFile.getFileEntryId();

			FileEntry fileEntry = DLAppServiceUtil.getFileEntry(fileEntryId);

			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			dossier = DossierLocalServiceUtil.getDossier(dossierId);

			dossierPart =
				DossierPartLocalServiceUtil.getDossierPart(dossierPartId);

			DLFolder accountFolder = accountBean.getAccountFolder();

			DLFolder dossierFolder =
				DLFolderUtil.addFolder(serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getScopeGroupId(), false,
					accountFolder.getFolderId(),
					String.valueOf(dossier.getCounter()), StringPool.BLANK,
					false, serviceContext);

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
				dossierFile.getDossierFileDate(), dossierFile.getOriginal(),
				PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC,
				dossierFolder.getFolderId(), fileEntry.getTitle() +
					StringPool.PERIOD + fileEntry.getExtension(),
				fileEntry.getMimeType(), fileEntry.getTitle(),
				StringPool.BLANK, StringPool.BLANK,
				fileEntry.getContentStream(), fileEntry.getSize(),
				serviceContext);

			updated = true;

		}
		catch (Exception e) {
			updated = false;
			if (e instanceof DuplicateFileException) {
				SessionErrors.add(actionRequest, DuplicateFileException.class);
			}
			else if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			}
			else if (e instanceof NoSuchDossierPartException) {
				SessionErrors.add(actionRequest,
					NoSuchDossierPartException.class);
			}
			else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			}
			else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountTypeException.class);
			}
			else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountFolderException.class);
			}
			else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnUserIdException.class);
			}
			else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnOrgIdException.class);
			}
			else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
					PermissionDossierException.class);
			}
			else if (e instanceof NoSuchFileEntryException) {
				SessionErrors.add(actionRequest, NoSuchFileEntryException.class);
			}
			else {
				SessionErrors.add(actionRequest, "upload-error");

			}
			_log.error(e);

		}
		finally {
			if (updated) {
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}
			else {
				actionResponse.setRenderParameter("redirectURL", redirectURL);
				actionResponse.setRenderParameter("content", "upload-file");
				actionResponse.setRenderParameter("tab1", "select-file");
				actionResponse.setRenderParameter("jspPage",
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
	protected DossierFile addSignatureFile(
		AccountBean accountBean, long dossierId, long dossierFileId,
		long dossierPartId, String sourceFileName, InputStream is, long size,
		ServiceContext serviceContext) {

		DossierFile dossierFile = null;

		DossierFile signDossierFile = null;

		try {

			dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(dossierFileId);

			FileEntry fileEntry =
				DLAppServiceUtil.getFileEntry(dossierFile.getFileEntryId());

			signDossierFile =
				DossierFileLocalServiceUtil.addSignDossierFile(dossierFileId,
					true, fileEntry.getFolderId(), sourceFileName,
					fileEntry.getMimeType(), fileEntry.getTitle() + "signed",
					fileEntry.getDescription(), StringPool.BLANK, is, size,
					serviceContext);

		}
		catch (Exception e) {

			_log.error(e);

		}

		return signDossierFile;
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void createReport(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		AccountBean accountBean =
			AccountUtil.getAccountBeanFromAttribute(actionRequest);

		long dossierFileId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		String sourceFileName = StringPool.BLANK;

		InputStream inputStream = null;

		File file = null;

		JSONObject responseJSON = JSONFactoryUtil.createJSONObject();

		String fileExportDir = StringPool.BLANK;

		try {
			validateCreateDynamicForm(dossierFileId, accountBean);

			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			// Get dossier file
			DossierFile dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(dossierFileId);

			// Get dossier part
			DossierPart dossierPart =
				DossierPartLocalServiceUtil.getDossierPart(dossierFile.getDossierPartId());

			Dossier dossier =
				DossierLocalServiceUtil.getDossier(dossierFile.getDossierId());

			// Get account folder
			DLFolder accountForlder = accountBean.getAccountFolder();

			// Get dossier folder
			DLFolder dosserFolder =
				DLFolderUtil.addFolder(themeDisplay.getUserId(),
					themeDisplay.getScopeGroupId(),
					themeDisplay.getScopeGroupId(), false,
					accountForlder.getFolderId(),
					String.valueOf(dossier.getCounter()), StringPool.BLANK,
					false, serviceContext);

			String formData = dossierFile.getFormData();
			String jrxmlTemplate = dossierPart.getFormReport();

			// Validate json string

			JSONFactoryUtil.createJSONObject(formData);

			String outputDestination =
				PortletPropsValues.OPENCPS_FILE_SYSTEM_TEMP_DIR;
			String fileName =
				System.currentTimeMillis() + StringPool.DASH + dossierFileId +
					StringPool.DASH + dossierPart.getDossierpartId() + ".pdf";

			fileExportDir =
				exportToPDFFile(jrxmlTemplate, formData, null,
					outputDestination, fileName);

			if (Validator.isNotNull(fileExportDir)) {

				file = new File(fileExportDir);
				inputStream = new FileInputStream(file);
				if (inputStream != null) {
					sourceFileName =
						fileExportDir.substring(
							fileExportDir.lastIndexOf(StringPool.SLASH) + 1,
							fileExportDir.length());
					String mimeType = MimeTypesUtil.getContentType(file);

					// Add new version
					if (dossierFile.getFileEntryId() > 0) {
						DossierFileLocalServiceUtil.addDossierFile(
							dossierFile.getDossierFileId(),
							dosserFolder.getFolderId(), sourceFileName,
							mimeType, dossierFile.getDisplayName(),
							StringPool.BLANK, StringPool.BLANK, inputStream,
							file.length(), serviceContext);
					}
					else {
						// Update version 1
						DossierFileLocalServiceUtil.updateDossierFile(
							dossierFileId, dosserFolder.getFolderId(),
							sourceFileName, mimeType,
							dossierFile.getDisplayName(), StringPool.BLANK,
							StringPool.BLANK, inputStream, file.length(),
							serviceContext);
					}
				}
			}
		}
		catch (Exception e) {
			if (e instanceof NoSuchDossierFileException) {
				SessionErrors.add(actionRequest,
					NoSuchDossierFileException.class);
			}
			else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			}
			else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountTypeException.class);
			}
			else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountFolderException.class);
			}
			else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnUserIdException.class);
			}
			else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnOrgIdException.class);
			}
			else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
					PermissionDossierException.class);
			}
			else if (e instanceof DuplicateFileException) {
				SessionErrors.add(actionRequest, DuplicateFileException.class);
			}
			else {
				SessionErrors.add(actionRequest, PortalException.class);
			}

			_log.error(e);
		}
		finally {
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

	public void deleteAttachmentFile(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long dossierFileId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);
		DossierFile dossierFile = null;

		JSONObject jsonObject = null;

		try {
			if (dossierFileId > 0) {
				jsonObject = JSONFactoryUtil.createJSONObject();
				dossierFile =
					DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
				long fileEntryId = dossierFile.getFileEntryId();
				DossierFileLocalServiceUtil.deleteDossierFile(dossierFileId,
					fileEntryId);
				jsonObject.put("deleted", Boolean.TRUE);
			}

		}
		catch (Exception e) {
			jsonObject.put("deleted", Boolean.FALSE);
			_log.error(e);
		}
		finally {
			PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
		}

	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void hashMultipleFile(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long[] dossierFileIds =
			ParamUtil.getLongValues(actionRequest, "dossierFileIds");

		if (dossierFileIds != null && dossierFileIds.length > 0) {
			try {
				for (int i = 0; i < dossierFileIds.length; i++) {
					DossierFile dossierFile =
						DossierFileLocalServiceUtil.getDossierFile(dossierFileIds[i]);
					String tempFilePath =
						PDFUtil.saveAsPdf(
							PortletUtil.getTempFolderPath(actionRequest),
							dossierFile.getFileEntryId());
					System.out.println(tempFilePath);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void hashSingleFile(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		JSONObject data = null;

		long dossierFileId = ParamUtil.getLong(actionRequest, "dossierFileId");

		try {

			DossierFile dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(dossierFileId);

			data = computerHash(actionRequest, dossierFile);

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			PortletUtil.writeJSON(actionRequest, actionResponse, data);
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void signature(
		ActionRequest actionRequest, ActionResponse actionResponse) {

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
			JSONObject resources =
				JSONFactoryUtil.createJSONObject(jsonResource);

			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);
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

			PdfPkcs7Signer pdfSigner =
				SignatureUtil.getPdfPkcs7Signer(inputFilePath, certPath,
					hashFileTempPath, outputFilePath, false, imagePath);

			pdfSigner.setHashAlgorithm(HashAlgorithm.SHA1);
			pdfSigner.setSignatureFieldName(signatureFieldName);

			_log.info("********************Signature******************** " +
				signature);

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

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {

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
	protected JSONObject computerHash(
		ActionRequest actionRequest, DossierFile dossierFile) {

		JSONObject data = JSONFactoryUtil.createJSONObject();

		JSONObject resources = JSONFactoryUtil.createJSONObject();

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		String tempFolderPath = PortletUtil.getTempFolderPath(actionRequest);

		String imageFolderPath =
			PortletUtil.getResourceFolderPath(actionRequest);

		String certFolderPath =
			PortletUtil.getResourceFolderPath(actionRequest);

		String employeeEmail =
			((Employee) accountBean.getAccountInstance()).getEmail();

		boolean isVisible = false;

		String inputFilePath = StringPool.BLANK;

		String outputFilePath = StringPool.BLANK;

		String hashFileTempPath = StringPool.BLANK;

		try {
			outputFilePath =
				tempFolderPath + "signed" + StringPool.DASH +
					dossierFile.getDisplayName() + StringPool.DASH +
					System.currentTimeMillis() + ".pdf";

			hashFileTempPath =
				tempFolderPath + "hash" + StringPool.DASH +
					dossierFile.getDisplayName() + StringPool.DASH +
					System.currentTimeMillis() + ".pdf";

			String certPath = certFolderPath + "/" + employeeEmail + ".cer";

			String imagePath = imageFolderPath + "/" + employeeEmail + ".png";

			inputFilePath =
				PDFUtil.saveAsPdf(tempFolderPath, dossierFile.getFileEntryId());

			PdfPkcs7Signer pdfSigner =
				SignatureUtil.getPdfPkcs7Signer(inputFilePath, certPath,
					hashFileTempPath, outputFilePath, isVisible, imagePath);

			System.out.println("inputFilePath: " + inputFilePath);
			System.out.println("hashFileTempPath: " + hashFileTempPath);
			System.out.println("outputFilePath: " + outputFilePath);
			System.out.println("imagePath: " + imagePath);

			pdfSigner.setHashAlgorithm(HashAlgorithm.SHA1);

			byte[] hash = pdfSigner.computeHash(0, 0, 144, 80);

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

		}
		catch (Exception e) {
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
	protected String exportToPDFFile(
		String jrxmlTemplate, String formData, Map<String, Object> map,
		String outputDestination, String fileName) {

		return JRReportUtil.createReportPDFfFile(jrxmlTemplate, formData, map,
			outputDestination, fileName);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void previewAttachmentFile(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long dossierFileId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);
		ThemeDisplay themeDisplay =
			(ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String url =
			DLFileEntryUtil.getDossierFileAttachmentURL(dossierFileId,
				themeDisplay);
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		// url = "http://docs.google.com/gview?url=" + url + "&embedded=true";
		jsonObject.put("url", url);
		try {
			PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
		}
		catch (IOException e) {
			_log.error(e);
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void previewDynamicForm(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long dossierFileId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		HttpServletResponse response =
			PortalUtil.getHttpServletResponse(actionResponse);
		response.setContentType("text/html");

		PrintWriter writer = null;

		try {
			writer = response.getWriter();

			// Get dossier file
			DossierFile dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(dossierFileId);

			// Get dossier part
			DossierPart dossierPart =
				DossierPartLocalServiceUtil.getDossierPart(dossierFile.getDossierPartId());

			String formData = dossierFile.getFormData();
			String jrxmlTemplate = dossierPart.getFormReport();

			// Validate json string

			JSONFactoryUtil.createJSONObject(formData);

			JRReportUtil.renderReportHTMLStream(response, writer,
				jrxmlTemplate, formData, null);

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
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
	public void removeAttachmentFile(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long dossierFileId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		JSONObject jsonObject = null;

		try {

			DossierFile dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(dossierFileId);

			DossierPart dossierPart =
				DossierPartLocalServiceUtil.getDossierPart(dossierFile.getDossierPartId());
			jsonObject = JSONFactoryUtil.createJSONObject();
			if (dossierFileId > 0 &&
				dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_OTHER) {

				DossierFileLocalServiceUtil.removeDossierFile(dossierFileId);

			}
			else {

				DossierFileLocalServiceUtil.deleteDossierFile(dossierFileId,
					dossierFile.getFileEntryId());

			}

			jsonObject.put("deleted", Boolean.TRUE);

		}
		catch (Exception e) {
			jsonObject.put("deleted", Boolean.FALSE);
			_log.error(e);
		}
		finally {
			PortletUtil.writeJSON(actionRequest, actionResponse, jsonObject);
		}

	}

	@Override
	public void render(
		RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException, IOException {

		long processOrderId =
			ParamUtil.getLong(renderRequest,
				ProcessOrderDisplayTerms.PROCESS_ORDER_ID);

		long dossierFileId =
			ParamUtil.getLong(renderRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);

		long dossierPartId =
			ParamUtil.getLong(renderRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);

		if (processOrderId > 0) {
			try {
				ServiceContext serviceContext =
					ServiceContextFactory.getInstance(renderRequest);

				ProcessOrder processOrder =
					ProcessOrderLocalServiceUtil.getProcessOrder(processOrderId);
				ProcessStep processStep =
					ProcessStepLocalServiceUtil.getProcessStep(processOrder.getProcessStepId());
				Dossier dossier =
					DossierLocalServiceUtil.getDossier(processOrder.getDossierId());

				AccountBean accountBean =
					AccountUtil.getAccountBean(dossier.getUserId(),
						serviceContext.getScopeGroupId(), serviceContext);
				ServiceProcess serviceProcess =
					ServiceProcessLocalServiceUtil.getServiceProcess(processOrder.getServiceProcessId());
				ServiceInfo serviceInfo =
					ServiceInfoLocalServiceUtil.getServiceInfo(processOrder.getServiceInfoId());
				ServiceConfig serviceConfig =
					ServiceConfigLocalServiceUtil.getServiceConfig(dossier.getServiceConfigId());

				DossierTemplate dossierTemplate =
					DossierTemplateLocalServiceUtil.getDossierTemplate(dossier.getDossierTemplateId());

				ProcessWorkflow processWorkflow =
					ProcessWorkflowLocalServiceUtil.getProcessWorkflow(processOrder.getProcessWorkflowId());

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

				HttpServletRequest request =
					PortalUtil.getHttpServletRequest(renderRequest);

				ServletContext servletContext = request.getServletContext();

				servletContext.setAttribute(WebKeys.ACCOUNT_BEAN, accountBean);

				if (dossierFileId > 0) {
					DossierFile dossierFile =
						DossierFileLocalServiceUtil.getDossierFile(dossierFileId);

					renderRequest.setAttribute(WebKeys.DOSSIER_FILE_ENTRY,
						dossierFile);
				}

				if (dossierPartId > 0) {
					DossierPart dossierPart =
						DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
					renderRequest.setAttribute(WebKeys.DOSSIER_PART_ENTRY,
						dossierPart);
				}

			}

			catch (Exception e) {
				_log.error(e.getCause());
			}

		}
		super.render(renderRequest, renderResponse);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateDynamicFormData(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		AccountBean accountBean =
			AccountUtil.getAccountBeanFromAttribute(actionRequest);

		DossierFile dossierFile = null;

		long dossierId =
			ParamUtil.getLong(actionRequest, DossierDisplayTerms.DOSSIER_ID);
		long dossierPartId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_PART_ID);
		long dossierFileId =
			ParamUtil.getLong(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_ID);
		long fileGroupId =
			ParamUtil.getLong(actionRequest, DossierDisplayTerms.FILE_GROUP_ID);
		long groupDossierPartId =
			ParamUtil.getLong(actionRequest, "groupDossierPartId");

		long fileEntryId = 0;

		// Default value
		int dossierFileMark = PortletConstants.DOSSIER_FILE_MARK_UNKNOW;
		int dossierFileType =
			ParamUtil.getInteger(actionRequest,
				DossierFileDisplayTerms.DOSSIER_FILE_TYPE);
		int syncStatus = PortletConstants.DOSSIER_FILE_SYNC_STATUS_NOSYNC;
		int original = PortletConstants.DOSSIER_FILE_ORIGINAL;

		String formData =
			ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.FORM_DATA);

		// Default value
		String dossierFileNo = StringPool.BLANK;
		String templateFileNo = StringPool.BLANK;
		String displayName = StringPool.BLANK;
		String groupName =
			ParamUtil.getString(actionRequest,
				DossierFileDisplayTerms.GROUP_NAME);
		Date dossierFileDate = null;

		try {
			validateDynamicFormData(dossierId, dossierPartId, accountBean);

			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);

			DossierPart dossierPart =
				DossierPartLocalServiceUtil.getDossierPart(dossierPartId);

			if (Validator.isNotNull(dossierPart.getTemplateFileNo())) {
				templateFileNo = dossierPart.getTemplateFileNo();
			}

			if (Validator.isNotNull(dossierPart.getPartName())) {
				displayName = dossierPart.getPartName();
			}

			if (dossierFileId == 0) {
				dossierFile =
					DossierFileLocalServiceUtil.addDossierFile(
						serviceContext.getUserId(), dossierId, dossierPartId,
						templateFileNo, groupName, fileGroupId,
						groupDossierPartId, accountBean.getOwnerUserId(),
						accountBean.getOwnerOrganizationId(), displayName,
						formData, fileEntryId, dossierFileMark,
						dossierFileType, dossierFileNo, dossierFileDate,
						original, syncStatus, serviceContext);
			}
			else {
				dossierFile =
					DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
				dossierFileMark = dossierFile.getDossierFileMark();
				dossierFileType = dossierFile.getDossierFileType();
				syncStatus = dossierFile.getSyncStatus();
				original = dossierFile.getOriginal();

				dossierFileNo =
					Validator.isNotNull(dossierFile.getDossierFileNo())
						? dossierFile.getDossierFileNo() : StringPool.BLANK;
				templateFileNo =
					Validator.isNotNull(dossierFile.getTemplateFileNo())
						? dossierFile.getTemplateFileNo() : StringPool.BLANK;
				displayName =
					Validator.isNotNull(dossierFile.getDisplayName())
						? dossierFile.getDisplayName() : StringPool.BLANK;

				dossierFile =
					DossierFileLocalServiceUtil.updateDossierFile(
						dossierFileId, serviceContext.getUserId(), dossierId,
						dossierPartId, templateFileNo, fileGroupId,
						accountBean.getOwnerUserId(),
						accountBean.getOwnerOrganizationId(), displayName,
						formData, fileEntryId, dossierFileMark,
						dossierFileType, dossierFileNo, dossierFileDate,
						original, syncStatus, serviceContext);
			}
		}
		catch (Exception e) {
			if (e instanceof NoSuchDossierException) {
				SessionErrors.add(actionRequest, NoSuchDossierException.class);
			}
			else if (e instanceof NoSuchDossierPartException) {
				SessionErrors.add(actionRequest,
					NoSuchDossierPartException.class);
			}
			else if (e instanceof NoSuchAccountException) {
				SessionErrors.add(actionRequest, NoSuchAccountException.class);
			}
			else if (e instanceof NoSuchAccountTypeException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountTypeException.class);
			}
			else if (e instanceof NoSuchAccountFolderException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountFolderException.class);
			}
			else if (e instanceof NoSuchAccountOwnUserIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnUserIdException.class);
			}
			else if (e instanceof NoSuchAccountOwnOrgIdException) {
				SessionErrors.add(actionRequest,
					NoSuchAccountOwnOrgIdException.class);
			}
			else if (e instanceof PermissionDossierException) {
				SessionErrors.add(actionRequest,
					PermissionDossierException.class);
			}
			else {
				SessionErrors.add(actionRequest, PortalException.class);
			}

			_log.error(e);
		}
		finally {
			actionResponse.setRenderParameter("primaryKey",
				String.valueOf(dossierFile != null
					? dossierFile.getDossierFileId() : 0));
			actionResponse.setRenderParameter("content", "declaration-online");
			actionResponse.setRenderParameter("jspPage",
				"/html/portlets/processmgt/processorder/modal_dialog.jsp");
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
		}
		else if (Validator.isNull(partName.trim())) {
			throw new EmptyFileGroupException();
		}

		int count = 0;

		try {
			count =
				FileGroupLocalServiceUtil.countByD_DN(dossierId,
					partName.trim());
		}
		catch (Exception e) {
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
	 */
	private void validateAccount(AccountBean accountBean)
		throws NoSuchAccountTypeException, NoSuchAccountException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException {

		if (accountBean == null) {
			throw new NoSuchAccountException();
		}
		else if (Validator.isNull(accountBean.getAccountType())) {
			throw new NoSuchAccountTypeException();
		}
		else if (accountBean.getAccountFolder() == null) {
			throw new NoSuchAccountFolderException();
		}

		else if (accountBean.isCitizen() && accountBean.getOwnerUserId() == 0) {
			throw new NoSuchAccountOwnUserIdException();
		}

		else if (accountBean.isBusiness() &&
			accountBean.getOwnerOrganizationId() == 0) {
			throw new NoSuchAccountOwnOrgIdException();
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
	 */
	private void validateAddAttachDossierFile(
		long dossierId, long dossierPartId, long dossierFileId,
		String displayName, long size, String sourceFileName,
		InputStream inputStream, AccountBean accountBean)
		throws NoSuchDossierException, NoSuchDossierPartException,
		NoSuchAccountException, NoSuchAccountTypeException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException, PermissionDossierException,
		FileSizeException {

		validateAccount(accountBean);

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		if (dossierPartId < 0) {
			throw new NoSuchDossierPartException();
		}

		Dossier dossier = null;

		try {
			dossier = DossierLocalServiceUtil.getDossier(dossierId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (accountBean.isBusiness()) {
			if (dossier.getOwnerOrganizationId() != accountBean.getOwnerOrganizationId()) {
				throw new PermissionDossierException();
			}

		}
		else if (accountBean.isCitizen()) {
			if (dossier.getUserId() != accountBean.getOwnerUserId()) {
				throw new PermissionDossierException();
			}

		}

		try {
			DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (size == 0) {
			throw new FileSizeException();
		}
		else if (size > 300000000) {
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
	 */
	private void validateCloneDossierFile(
		long dossierId, long dossierPartId, long dossierFileId,
		AccountBean accountBean)
		throws NoSuchDossierException, NoSuchDossierPartException,
		NoSuchAccountException, NoSuchAccountTypeException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException, PermissionDossierException,
		NoSuchDossierFileException, NoSuchFileEntryException {

		validateAccount(accountBean);

		if (dossierFileId <= 0) {
			throw new NoSuchDossierFileException();
		}

		DossierFile dossierFile = null;

		try {
			dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
		}
		catch (Exception e) {

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

		Dossier dossier = null;

		try {
			dossier = DossierLocalServiceUtil.getDossier(dossierId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (accountBean.isBusiness()) {
			if (dossier.getOwnerOrganizationId() != accountBean.getOwnerOrganizationId()) {
				throw new PermissionDossierException();
			}

		}
		else if (accountBean.isCitizen()) {
			if (dossier.getUserId() != accountBean.getOwnerUserId()) {
				throw new PermissionDossierException();
			}

		}

		try {
			DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		}
		catch (Exception e) {
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
	 */
	private void validateCreateDynamicForm(
		long dossierFileId, AccountBean accountBean)
		throws NoSuchAccountTypeException, NoSuchAccountException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException, NoSuchDossierFileException {

		validateAccount(accountBean);

		if (dossierFileId < 0) {
			throw new NoSuchDossierFileException();
		}

		DossierFile dossierFile = null;

		try {
			dossierFile =
				DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
		}
		catch (Exception e) {
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
	 */
	private void validateDynamicFormData(
		long dossierId, long dossierPartId, AccountBean accountBean)
		throws NoSuchAccountTypeException, NoSuchAccountException,
		NoSuchAccountFolderException, NoSuchAccountOwnUserIdException,
		NoSuchAccountOwnOrgIdException, NoSuchDossierException,
		NoSuchDossierPartException, PermissionDossierException {

		validateAccount(accountBean);
		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		if (dossierPartId < 0) {
			throw new NoSuchDossierPartException();
		}

		Dossier dossier = null;

		try {
			dossier = DossierLocalServiceUtil.getDossier(dossierId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}

		if (accountBean.isBusiness()) {
			if (dossier.getOwnerOrganizationId() != accountBean.getOwnerOrganizationId()) {
				throw new PermissionDossierException();
			}

		}
		else if (accountBean.isCitizen()) {
			if (dossier.getUserId() != accountBean.getOwnerUserId()) {
				throw new PermissionDossierException();
			}

		}

		try {
			DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		}
		catch (Exception e) {
			throw new NoSuchDossierPartException();
		}
	}

	/**
	 * @param dossierId
	 * @throws NoSuchDossierException
	 * @throws NoSuchDossierTemplateException
	 * @throws RequiredDossierPartException
	 */
	public void validateAssignTask(long dossierId)
		throws NoSuchDossierException, NoSuchDossierTemplateException,
		RequiredDossierPartException {

		if (dossierId <= 0) {
			throw new NoSuchDossierException();
		}

		Dossier dossier = null;

		try {
			dossier = DossierLocalServiceUtil.getDossier(dossierId);
		}
		catch (Exception e) {
		}

		if (dossier == null) {
			throw new NoSuchDossierException();
		}

		DossierTemplate dossierTemplate = null;

		try {
			dossierTemplate =
				DossierTemplateLocalServiceUtil.getDossierTemplate(dossier.getDossierTemplateId());
		}
		catch (Exception e) {
		}

		if (dossierTemplate == null) {
			throw new NoSuchDossierTemplateException();
		}

		List<DossierPart> dossierPartsLevel1 = new ArrayList<DossierPart>();

		try {
			List<DossierPart> lstTmp1 =
				DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(
					dossierTemplate.getDossierTemplateId(), 0,
					PortletConstants.DOSSIER_PART_TYPE_RESULT);

			if (lstTmp1 != null) {

				dossierPartsLevel1.addAll(lstTmp1);
			}
		}
		catch (Exception e) {
		}

		try {
			List<DossierPart> lstTmp2 =
				DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(
					dossierTemplate.getDossierTemplateId(), 0,
					PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT);
			if (lstTmp2 != null) {

				dossierPartsLevel1.addAll(lstTmp2);
			}
		}
		catch (Exception e) {
		}

		boolean requiredFlag = false;

		if (dossierPartsLevel1 != null) {
			for (DossierPart dossierPartLevel1 : dossierPartsLevel1) {
				List<DossierPart> dossierParts =
					DossierMgtUtil.getTreeDossierPart(dossierPartLevel1.getDossierpartId());

				if (requiredFlag) {
					break;
				}

				for (DossierPart dossierPart : dossierParts) {

					if ((dossierPart.getPartType() == PortletConstants.DOSSIER_PART_TYPE_RESULT || dossierPart.getPartType() == PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT) &&
						dossierPart.getRequired()) {
						DossierFile dossierFile = null;
						try {
							dossierFile =
								DossierFileLocalServiceUtil.getDossierFileInUse(
									dossierId, dossierPart.getDossierpartId());
						}
						catch (Exception e) {
							// TODO: handle exception
						}

						if (dossierFile == null) {
							requiredFlag = true;
							break;
						}

					}
				}
			}
		}

		if (requiredFlag) {
			throw new RequiredDossierPartException();
		}
	}
}
