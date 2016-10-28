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

package org.opencps.paymentmgt.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.backend.message.SendToBackOfficeMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLogLocalServiceUtil;
import org.opencps.dossiermgt.util.ActorBean;
import org.opencps.jasperreport.util.JRReportUtil;
import org.opencps.paymentmgt.NoSuchPaymentFileException;
import org.opencps.paymentmgt.model.PaymentConfig;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.search.PaymentFileDisplayTerms;
import org.opencps.paymentmgt.service.PaymentConfigLocalServiceUtil;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.paymentmgt.util.PaymentMgtUtil;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungdk
 */
public class PaymentMgtBackOfficePortlet extends MVCPortlet {

	private Log _log =
		LogFactoryUtil.getLog(PaymentMgtBackOfficePortlet.class.getName());

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void confirmPaymentRequested(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long paymentFileId =
			ParamUtil.getLong(
				actionRequest, PaymentFileDisplayTerms.PAYMENT_FILE_ID);
		int confirmHopLe =
			ParamUtil.getInteger(actionRequest, "confirmHopLeHidden", 0);

		String lyDo = ParamUtil.getString(actionRequest, "lyDo");

		PaymentFile paymentFile = null;

		try {
			paymentFile =
				PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);

			if (paymentFile != null) {
				boolean trustServiceMode =
					BackendUtils.checkServiceMode(paymentFile.getDossierId());

				if (!trustServiceMode) {
					Dossier dossier =
						DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
					SendToBackOfficeMsg toBackOffice =
						new SendToBackOfficeMsg();

					Message sendToBackOffice = new Message();

					toBackOffice.setActorName(WebKeys.ACTION_PAY_VALUE);

					toBackOffice.setDossierId(paymentFile.getDossierId());

					toBackOffice.setPaymentFile(paymentFile);

					toBackOffice.setCompanyId(dossier.getCompanyId());

					toBackOffice.setGovAgencyCode(dossier.getGovAgencyCode());

					sendToBackOffice.put("toBackOffice", toBackOffice);

					MessageBusUtil.sendMessage(
						"opencps/backoffice/out/destination", sendToBackOffice);

				}

				if (confirmHopLe == 1) {
					paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_APPROVED);
				}
				else if (confirmHopLe == 0) {
					paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_REJECTED);
					paymentFile.setApproveNote(lyDo);
				}

				paymentFile.setModifiedDate(new Date());

				PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);

				ServiceContext serviceContext =
					ServiceContextFactory.getInstance(actionRequest);

				ActorBean actorBean =
					new ActorBean(2, serviceContext.getUserId());

				String msgInfo = StringPool.BLANK;

				if (Validator.isNull(lyDo)) {
					msgInfo = PortletConstants.DOSSIER_ACTION_CONFIRM_PAYMENT;
				}
				else {
					msgInfo = lyDo;
				}

				// String dossierStatus =
				// PaymentMgtUtil.getDossierStatus(paymentFile);

				// Add dossierLog for confirm payment
				DossierLogLocalServiceUtil.addDossierLog(

					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(),
					serviceContext.getCompanyId(), paymentFile.getDossierId(),
					paymentFile.getFileGroupId(), null,
					PortletConstants.DOSSIER_ACTION_CONFIRM_PAYMENT, msgInfo,
					new Date(), 1, 2, actorBean.getActor(),
					actorBean.getActorId(), actorBean.getActorName(),
					PaymentMgtBackOfficePortlet.class.getName());

				SessionMessages.add(
					actionRequest,
					MessageKeys.PAYMENT_FILE_CONFIRM_BANK_SUCCESS);
			}

		}
		catch (NoSuchPaymentFileException e) {
			SessionErrors.add(
				actionRequest, MessageKeys.PAYMENT_FILE_CONFIRM_BANK_ERROR);
		}
		catch (SystemException e) {
			SessionErrors.add(
				actionRequest, MessageKeys.PAYMENT_FILE_CONFIRM_BANK_ERROR);

		}
		catch (PortalException e) {
			SessionErrors.add(
				actionRequest, MessageKeys.PAYMENT_FILE_CONFIRM_BANK_ERROR);

		}

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

		long paymentFileId = ParamUtil.getLong(actionRequest, "paymentFileId");

		File file = null;

		InputStream inputStream = null;

		JSONObject responseJSON = JSONFactoryUtil.createJSONObject();

		String fileExportDir = StringPool.BLANK;
		String urlFileDowLoad = StringPool.BLANK;
		try {
			if (paymentFileId > 0) {
				ServiceContext serviceContext =
					ServiceContextFactory.getInstance(actionRequest);
				serviceContext.setAddGroupPermissions(true);
				serviceContext.setAddGuestPermissions(true);
				// Get PaymentFile
				PaymentFile paymentFile =
					PaymentFileLocalServiceUtil.fetchPaymentFile(paymentFileId);
				long govAgencyOrganizationId = -1;
				long userId = themeDisplay.getUserId();
				Employee employee =
					EmployeeLocalServiceUtil.getEmployeeByMappingUserId(
						themeDisplay.getScopeGroupId(), userId);
				WorkingUnit workingUnit =
					WorkingUnitLocalServiceUtil.fetchWorkingUnit(employee.getWorkingUnitId());
				govAgencyOrganizationId =
					workingUnit.getMappingOrganisationId();
				// govAgencyOrganizationId = 24787;
				PaymentConfig paymentConfig =
					PaymentConfigLocalServiceUtil.getPaymentConfigByGovAgency(
						themeDisplay.getScopeGroupId(), govAgencyOrganizationId);
				// Get account folder
				String dossierDestinationFolder =
					PortletUtil.getEmployeeDestinationFolder(
						themeDisplay.getScopeGroupId(), userId);
				DLFolder accountForlder =
					DLFolderUtil.getTargetFolder(
						themeDisplay.getUserId(),
						themeDisplay.getScopeGroupId(),
						themeDisplay.getScopeGroupId(), false, 0,
						dossierDestinationFolder, StringPool.BLANK, false,
						serviceContext);;

				// Get dossier folder
				DLFolder paymentFolder =
					DLFolderUtil.addFolder(
						themeDisplay.getUserId(),
						themeDisplay.getScopeGroupId(),
						themeDisplay.getScopeGroupId(), false,
						accountForlder.getFolderId(),
						String.valueOf(paymentConfig.getPaymentConfigId()),
						StringPool.BLANK, false, serviceContext);

				// TODO
				String formData = StringPool.BLANK;
				JSONObject payloadJSON = JSONFactoryUtil.createJSONObject();
				JSONObject resultJSON = JSONFactoryUtil.createJSONObject();
				payloadJSON.put("paymentFileId", paymentFile.getPaymentFileId());
				payloadJSON.put("dossierId", paymentFile.getDossierId());
				payloadJSON.put("fileGroupId", paymentFile.getFileGroupId());
				payloadJSON.put("ownerUserId", paymentFile.getOwnerUserId());
				payloadJSON.put(
					"ownerOrganizationId", paymentFile.getOwnerOrganizationId());
				// TODO

				Citizen citizen = null;
				if (paymentFile.getOwnerOrganizationId() != 0)
					workingUnit =
						WorkingUnitLocalServiceUtil.fetchByMappingOrganisationId(
							themeDisplay.getScopeGroupId(),
							paymentFile.getOwnerOrganizationId());
				else if (paymentFile.getOwnerUserId() != 0)
					citizen =
						CitizenLocalServiceUtil.getByMappingUserId(paymentFile.getOwnerUserId());
				if (workingUnit != null) {
					payloadJSON.put(
						"ownerOrganizationName", workingUnit.getName());
					payloadJSON.put(
						"ownerOrganizationAddress", workingUnit.getAddress());
				}
				else if (citizen != null) {
					payloadJSON.put(
						"ownerOrganizationName", citizen.getFullName());
					payloadJSON.put(
						"ownerOrganizationAddress", citizen.getAddress());
				}
				else {
					payloadJSON.put("ownerOrganizationName", "");
					payloadJSON.put("ownerOrganizationAddress", "");
				}

				payloadJSON.put(
					"govAgencyOrganizationId",
					paymentFile.getGovAgencyOrganizationId());
				payloadJSON.put("paymentName", paymentFile.getPaymentName());
				payloadJSON.put(
					"requestDatetime", DateTimeUtil.convertDateToString(
						paymentFile.getRequestDatetime(),
						DateTimeUtil._VN_DATE_TIME_FORMAT));
				payloadJSON.put("amount", paymentFile.getAmount());
				// TODO
				payloadJSON.put("amountNumber", paymentFile.getAmount());
				payloadJSON.put("amountString", paymentFile.getAmount());

				payloadJSON.put("requestNote", paymentFile.getRequestNote());
				payloadJSON.put("keypayUrl", paymentFile.getKeypayUrl());
				payloadJSON.put(
					"keypayTransactionId", paymentFile.getKeypayTransactionId());
				payloadJSON.put(
					"keypayGoodCode", paymentFile.getKeypayGoodCode());
				payloadJSON.put(
					"keypayMerchantCode", paymentFile.getKeypayMerchantCode());
				payloadJSON.put("bankInfo", paymentFile.getBankInfo());
				payloadJSON.put("placeInfo", paymentFile.getPlaceInfo());
				payloadJSON.put(
					"paymentStatus",
					PortletUtil.getPaymentStatusLabel(
						paymentFile.getPaymentStatus(), Locale.getDefault()));
				payloadJSON.put(
					"paymentMethod",
					PortletUtil.getPaymentMethodLabel(
						paymentFile.getPaymentMethod(), Locale.getDefault()));
				// TODO

				payloadJSON.put(
					"confirmDatetime", DateTimeUtil.convertDateToString(
						paymentFile.getConfirmDatetime(),
						DateTimeUtil._VN_DATE_TIME_FORMAT));
				payloadJSON.put(
					"confirmFileEntryId", paymentFile.getConfirmFileEntryId());
				payloadJSON.put(
					"approveDatetime", DateTimeUtil.convertDateToString(
						paymentFile.getApproveDatetime(),
						DateTimeUtil._VN_DATE_TIME_FORMAT));
				payloadJSON.put(
					"accountUserName", paymentFile.getAccountUserName());
				payloadJSON.put("approveNote", paymentFile.getApproveNote());
				payloadJSON.put(
					"govAgencyTaxNo", paymentFile.getGovAgencyTaxNo());
				payloadJSON.put(
					"invoiceTemplateNo", paymentFile.getInvoiceTemplateNo());
				payloadJSON.put(
					"invoiceIssueNo", paymentFile.getInvoiceIssueNo());
				payloadJSON.put("invoiceNo", paymentFile.getInvoiceNo());

				payloadJSON.put(
					"cf_paymentConfigId", paymentConfig.getPaymentConfigId());
				payloadJSON.put(
					"cf_govAgencyOrganizationId",
					paymentConfig.getGovAgencyOrganizationId());
				payloadJSON.put(
					"cf_govAgencyName", paymentConfig.getGovAgencyName());
				payloadJSON.put(
					"cf_govAgencyTaxNo", paymentConfig.getGovAgencyTaxNo());
				payloadJSON.put(
					"cf_invoiceTemplateNo",
					paymentConfig.getInvoiceTemplateNo());
				payloadJSON.put(
					"cf_invoiceIssueNo", paymentConfig.getInvoiceIssueNo());
				payloadJSON.put(
					"cf_invoiceLastNo", paymentConfig.getInvoiceLastNo());
				payloadJSON.put("cf_bankInfo", paymentConfig.getBankInfo());
				payloadJSON.put("cf_placeInfo", paymentConfig.getPlaceInfo());
				payloadJSON.put(
					"cf_keypayDomain", paymentConfig.getKeypayDomain());
				payloadJSON.put(
					"cf_keypayVersion", paymentConfig.getKeypayVersion());
				payloadJSON.put(
					"cf_keypayMerchantCode",
					paymentConfig.getKeypayMerchantCode());
				payloadJSON.put(
					"cf_keypaySecureKey", paymentConfig.getKeypaySecureKey());
				resultJSON.put("opencps", payloadJSON);
				System.out.println("PaymentMgtBackOfficePortlet.createReport()" +
					resultJSON.toString());

				String jrxmlTemplate = paymentConfig.getReportTemplate();

				// Validate json string
				formData = resultJSON.toString();
				// JSONFactoryUtil
				// .createJSONObject(formData);

				String outputDestination =
					PortletPropsValues.OPENCPS_FILE_SYSTEM_TEMP_DIR;
				String fileName =
					System.currentTimeMillis() + StringPool.DASH +
						paymentConfig.getPaymentConfigId() + StringPool.DASH +
						paymentFile.getPaymentFileId() + ".pdf";

				fileExportDir =
					exportToPDFFile(
						jrxmlTemplate, formData, null, outputDestination,
						fileName);

				if (Validator.isNotNull(fileExportDir)) {

					file = new File(fileExportDir);
					inputStream = new FileInputStream(file);
					if (inputStream != null) {
						String sourceFileName =
							fileExportDir.substring(
								fileExportDir.lastIndexOf(StringPool.SLASH) + 1,
								fileExportDir.length());
						System.out.println(sourceFileName);

						System.out.println(file.getName());

						String mimeType = MimeTypesUtil.getContentType(file);

						FileEntry fileEntry =
							DLAppServiceUtil.addFileEntry(
								serviceContext.getScopeGroupId(),
								paymentFolder.getFolderId(), sourceFileName,
								mimeType, fileName, StringPool.BLANK,
								StringPool.BLANK, inputStream, file.length(),
								serviceContext);
						fileExportDir = getURL(fileEntry);
						// String tenFileExport = "defaultPDF.pdfs";
						if (fileExportDir.contains(".pdfs")) {
							urlFileDowLoad =
								fileExportDir.replace(".pdfs", ".pdf") +
									"#view=FitH&scrollbar=0&page=1&toolbar=0&statusbar=0&messages=0&navpanes=0";
						}
						else if (fileExportDir.contains(".doc")) {
							urlFileDowLoad =
								"https://docs.google.com/viewer?url=" +
									PortalUtil.getPortalURL(actionRequest) +
									fileExportDir + "&embedded=true";
						}
						else {
							urlFileDowLoad =
								fileExportDir +
									"#view=FitH&scrollbar=0&page=1&toolbar=0&statusbar=0&messages=0&navpanes=0";
						}
					}
				}
			}
		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			responseJSON.put("fileExportDir", urlFileDowLoad);
			PortletUtil.writeJSON(actionRequest, actionResponse, responseJSON);
			inputStream.close();
			file.delete();

		}
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

		return JRReportUtil.createReportPDFfFile(
			jrxmlTemplate, formData, map, outputDestination, fileName);
	}

	private String getURL(FileEntry fileEntry) {

		try {
			String url =
				"/documents/" + fileEntry.getGroupId() + StringPool.SLASH +
					fileEntry.getFolderId() + StringPool.SLASH +
					fileEntry.getTitle() + "?version=" + fileEntry.getVersion();
			return url;
		}
		catch (Exception e) {
			_log.error(e);
		}

		return "";
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	@Deprecated
	public void updateConfirmPayment(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long paymentFileId = ParamUtil.getLong(actionRequest, "paymentFileId");
		PaymentFile paymentFile = null;
		try {
			paymentFile =
				PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);
			paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED);
			paymentFile.setPaymentMethod(PaymentMgtUtil.PAYMENT_METHOD_BANK);
			paymentFile.setModifiedDate(new Date());
			PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);

			ActorBean actorBean = new ActorBean(2, serviceContext.getUserId());

			// Add dossierLog for confirm payment
			DossierLogLocalServiceUtil.addDossierLog(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				serviceContext.getCompanyId(), paymentFile.getDossierId(),
				paymentFile.getFileGroupId(), null,
				PortletConstants.DOSSIER_ACTION_CONFIRM_PAYMENT,
				PortletConstants.DOSSIER_ACTION_CONFIRM_PAYMENT, new Date(), 1,
				2, actorBean.getActor(), actorBean.getActorId(),
				actorBean.getActorName(),
				PaymentMgtBackOfficePortlet.class.getName());

			addProcessActionSuccessMessage = false;
			SessionMessages.add(actionRequest, "confirm-payment-cash-success");
		}
		catch (PortalException e) {
			SessionErrors.add(actionRequest, "confirm-payment-cash-error");
		}
		catch (SystemException e) {
			SessionErrors.add(actionRequest, "confirm-payment-cash-error");
		}

	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void updateConfirmPaymentCash(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long paymentFileId = ParamUtil.getLong(actionRequest, "paymentFileId");

		PaymentFile paymentFile = null;

		Message sendToBackOffice = new Message();

		try {

			paymentFile =
				PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);

			boolean trustServiceMode =
				BackendUtils.checkServiceMode(paymentFile.getDossierId());

			if (!trustServiceMode) {
				Dossier dossier =
					DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
				SendToBackOfficeMsg toBackOffice = new SendToBackOfficeMsg();

				toBackOffice.setActorName(WebKeys.ACTION_PAY_VALUE);

				toBackOffice.setDossierId(paymentFile.getDossierId());

				toBackOffice.setPaymentFile(paymentFile);

				toBackOffice.setCompanyId(dossier.getCompanyId());

				toBackOffice.setGovAgencyCode(dossier.getGovAgencyCode());

				sendToBackOffice.put("toBackOffice", toBackOffice);

			}

			paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_APPROVED);
			paymentFile.setPaymentMethod(PaymentMgtUtil.PAYMENT_METHOD_CASH);
			paymentFile.setModifiedDate(new Date());
			PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
			addProcessActionSuccessMessage = false;

			ServiceContext serviceContext =
				ServiceContextFactory.getInstance(actionRequest);

			ActorBean actorBean = new ActorBean(2, serviceContext.getUserId());

			// Add dossierLog for confirm payment
			DossierLogLocalServiceUtil.addDossierLog(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				serviceContext.getCompanyId(), paymentFile.getDossierId(),
				paymentFile.getFileGroupId(), null,
				PortletConstants.DOSSIER_ACTION_CONFIRM_CASH_PAYMENT,
				PortletConstants.DOSSIER_ACTION_CONFIRM_PAYMENT, new Date(), 1,
				2, actorBean.getActor(), actorBean.getActorId(),
				actorBean.getActorName(),
				PaymentMgtBackOfficePortlet.class.getName());
			SessionMessages.add(actionRequest, "confirm-payment-cash-success");

			MessageBusUtil.sendMessage(
				"opencps/backoffice/out/destination", sendToBackOffice);
		}
		catch (PortalException e) {
			SessionErrors.add(actionRequest, "confirm-payment-cash-error");
		}
		catch (SystemException e) {
			SessionErrors.add(actionRequest, "confirm-payment-cash-error");
		}

	}
}
