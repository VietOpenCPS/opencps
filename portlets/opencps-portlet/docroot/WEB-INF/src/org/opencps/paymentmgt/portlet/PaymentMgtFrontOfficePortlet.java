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

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.opencps.backend.message.UserActionMsg;
import org.opencps.backend.util.BackendUtils;
import org.opencps.backend.util.PaymentUrlGenerator;
import org.opencps.dossiermgt.bean.AccountBean;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLogLocalServiceUtil;
import org.opencps.dossiermgt.util.ActorBean;
import org.opencps.dossiermgt.util.DossierMgtUtil;
import org.opencps.paymentmgt.NoSuchPaymentFileException;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.search.PaymentFileDisplayTerms;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.paymentmgt.util.PaymentMgtUtil;
import org.opencps.servicemgt.DuplicateFileNameException;
import org.opencps.servicemgt.DuplicateFileNoException;
import org.opencps.servicemgt.IOFileUploadException;
import org.opencps.util.AccountUtil;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.LiferayFileItemException;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungdk
 */
public class PaymentMgtFrontOfficePortlet extends MVCPortlet {

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void keypayTransaction(ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, SystemException, PortalException {

		addProcessActionSuccessMessage = false;
		long paymentFileId =
			ParamUtil.getLong(actionRequest, PaymentFileDisplayTerms.PAYMENT_FILE_ID, 0L);
		PaymentFile paymentFile = null;
		try {

			paymentFile = PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);
		}
		catch (NoSuchPaymentFileException e) {

		}

		String url_redirect = StringPool.BLANK;

		if (Validator.isNotNull(paymentFile)) {
			url_redirect = paymentFile.getKeypayUrl();

			if (url_redirect.trim().length() <= 0) {
//				paymentFile =
//					PaymentUrlGenerator.generatorPayURL(
//						PortalUtil.getScopeGroupId(actionRequest),
//						paymentFile.getGovAgencyOrganizationId(), paymentFile.getPaymentFileId(),
//						"", paymentFile.getDossierId());
//
//				url_redirect = paymentFile.getKeypayUrl();
			}

			paymentFile.setModifiedDate(new Date());
			paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_REQUESTED);

			PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
		}

		actionResponse.sendRedirect(url_redirect);
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void requestBankPayment(ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		AccountBean accountBean = AccountUtil.getAccountBean(actionRequest);

		PaymentFile paymentFile = null;

		long paymentFileId =
			ParamUtil.getLong(actionRequest, PaymentFileDisplayTerms.PAYMENT_FILE_ID);

		// String redirectURL = ParamUtil.getString(actionRequest,
		// "redirectURL");
		// String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) +
			SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			DLFolder dlFolder =
				DLFolderUtil.getPaymentFolder(
					themeDisplay.getScopeGroupId(), new Date(), accountBean.isBusiness()
						? accountBean.getOwnerOrganizationId() : accountBean.getOwnerUserId(),
					accountBean.getAccountType(), serviceContext);

			FileEntry fileEntry =
				updateFileEntry(dlFolder.getFolderId(), actionRequest, serviceContext);

			if (paymentFileId > 0) {

				Message message = new Message();

				paymentFile = PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);

				boolean trustServiceMode =
					BackendUtils.checkServiceMode(paymentFile.getDossierId());

				if (!trustServiceMode) {
					Dossier dossier =
						DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());

					UserActionMsg actionMsg = new UserActionMsg();

					actionMsg.setAction(org.opencps.util.WebKeys.ACTION_PAY_VALUE);

					actionMsg.setPaymentFileId(paymentFileId);

					actionMsg.setDossierId(paymentFile.getDossierId());

					actionMsg.setCompanyId(dossier.getCompanyId());

					actionMsg.setGovAgencyCode(dossier.getGovAgencyCode());

					message.put("msgToEngine", actionMsg);

				}

				paymentFile.setConfirmFileEntryId(fileEntry.getFileEntryId());
				paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED);
				paymentFile.setPaymentMethod(PaymentMgtUtil.PAYMENT_METHOD_BANK);
				paymentFile.setModifiedDate(new Date());
				paymentFile.setConfirmDatetime(new Date());

				PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);

				ActorBean actorBean = new ActorBean(1, serviceContext.getUserId());
				
				Locale vnLocale = new Locale("vi", "VN");

				NumberFormat vnFormat =
					NumberFormat.getCurrencyInstance(vnLocale);

				// Add log baonop
				StringBuffer msgInforSb = new StringBuffer();

				msgInforSb.append(LanguageUtil.get(
					serviceContext.getLocale(), PortletConstants.DOSSIER_ACTION_REQUEST_PAYMENT));
				msgInforSb.append(StringPool.SPACE);
				msgInforSb.append(StringPool.COLON);
				msgInforSb.append(StringPool.SPACE);
				msgInforSb.append(StringPool.OPEN_PARENTHESIS);
				msgInforSb.append(vnFormat.format(paymentFile.getAmount()));
				msgInforSb.append(StringPool.CLOSE_PARENTHESIS);
				msgInforSb.append(StringPool.SPACE);
				msgInforSb.append(LanguageUtil.get(serviceContext.getLocale(), "for-dossier"));
				msgInforSb.append(StringPool.SPACE);
				msgInforSb.append(DossierMgtUtil.getServiceName(paymentFile.getDossierId()));

				Dossier dossier = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId());

				DossierLogLocalServiceUtil.addDossierLog(
					serviceContext.getUserId(), serviceContext.getScopeGroupId(),
					serviceContext.getCompanyId(), paymentFile.getDossierId(),
					paymentFile.getFileGroupId(), dossier.getDossierStatus(),
					PortletConstants.DOSSIER_ACTION_REQUEST_PAYMENT, msgInforSb.toString(),
					new Date(), 1, 2, actorBean.getActor(), actorBean.getActorId(),
					actorBean.getActorName(), PaymentMgtFrontOfficePortlet.class.getName() +
						".requestBankPayment()", 0, 0, false);

				MessageBusUtil.sendMessage("opencps/frontoffice/out/destination", message);

				SessionMessages.add(actionRequest, MessageKeys.PAYMENT_FILE_CONFIRM_BANK_SUCCESS);
			}

		}
		catch (Exception e) {
			if (e instanceof DuplicateFileNameException) {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_FILE_NAME_EXCEPTION);
			}
			else if (e instanceof DuplicateFileNoException) {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_FILE_NO_EXCEPTION);

			}
			else if (e instanceof IOFileUploadException) {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_UPLOAD_EXCEPTION);
			}
			else {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_EXCEPTION_OCCURRED);
			}
		}

	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	public void requestBankPaymentItems(ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long[] paymentFileIds =
			ParamUtil.getLongValues(actionRequest, PaymentFileDisplayTerms.PAYMENT_FILE_IDS);

		// String redirectURL = ParamUtil.getString(actionRequest,
		// "redirectURL");
		// String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) +
			SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

		try {
			// ServiceContext serviceContext =
			// ServiceContextFactory.getInstance(actionRequest);

			PaymentFile paymentFile = null;
			for (int i = 0; i < paymentFileIds.length; i++) {

				paymentFile = PaymentFileLocalServiceUtil.getPaymentFile(paymentFileIds[i]);
				if (paymentFile != null) {
					paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED);
					paymentFile.setPaymentMethod(PaymentMgtUtil.PAYMENT_METHOD_BANK);
					paymentFile.setModifiedDate(new Date());
					PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
				}
			}
			SessionMessages.add(actionRequest, MessageKeys.PAYMENT_FILE_CONFIRM_BANK_SUCCESS);
		}
		catch (Exception e) {
			if (e instanceof DuplicateFileNameException) {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_FILE_NAME_EXCEPTION);
			}
			else if (e instanceof DuplicateFileNoException) {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_FILE_NO_EXCEPTION);

			}
			else if (e instanceof IOFileUploadException) {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_UPLOAD_EXCEPTION);
			}
			else {
				SessionErrors.add(actionRequest, MessageKeys.SERVICE_TEMPLATE_EXCEPTION_OCCURRED);
			}
		}

	}

	/**
	 * @param folderId
	 * @param actionRequest
	 * @param serviceContext
	 * @return
	 * @throws LiferayFileItemException
	 * @throws FileSizeException
	 * @throws IOFileUploadException
	 * @throws Exception
	 */
	protected FileEntry updateFileEntry(
		long folderId, ActionRequest actionRequest, ServiceContext serviceContext)
		throws LiferayFileItemException, FileSizeException, IOFileUploadException {

		UploadPortletRequest uploadPortletRequest =
			PortalUtil.getUploadPortletRequest(actionRequest);

		Date now = new Date();

		String cmd = ParamUtil.getString(uploadPortletRequest, Constants.CMD);

		String sourceFileName =
			now.getTime() + StringPool.UNDERLINE + uploadPortletRequest.getFileName("uploadedFile");
		String title = ParamUtil.getString(uploadPortletRequest, "fileName");

		String description = ParamUtil.getString(uploadPortletRequest, "description");
		String changeLog = ParamUtil.getString(uploadPortletRequest, "changeLog");

		InputStream inputStream = null;

		try {
			String contentType = uploadPortletRequest.getContentType("uploadedFile");

			long size = uploadPortletRequest.getSize("uploadedFile");

			if ((cmd.equals(Constants.ADD) || cmd.equals(Constants.ADD_DYNAMIC)) && (size == 0)) {

				contentType = MimeTypesUtil.getContentType(title);
			}

			inputStream = uploadPortletRequest.getFileAsStream("uploadedFile");

			FileEntry fileEntry = null;

			// Add file entry

			fileEntry =
				DLAppServiceUtil.addFileEntry(
					serviceContext.getScopeGroupId(), folderId, sourceFileName, contentType,
					sourceFileName, description, changeLog, inputStream, size, serviceContext);

			AssetPublisherUtil.addAndStoreSelection(
				actionRequest, DLFileEntry.class.getName(), fileEntry.getFileEntryId(), -1);

			AssetPublisherUtil.addRecentFolderId(
				actionRequest, DLFileEntry.class.getName(), folderId);

			return fileEntry;
		}
		catch (Exception e) {
			_log.error(e);
			UploadException uploadException =
				(UploadException) actionRequest.getAttribute(WebKeys.UPLOAD_EXCEPTION);

			if (uploadException != null) {
				if (uploadException.isExceededLiferayFileItemSizeLimit()) {
					throw new LiferayFileItemException();
				}
				else if (uploadException.isExceededSizeLimit()) {
					throw new FileSizeException(uploadException.getCause());
				}
			}

			throw new IOFileUploadException("File upload exception");
		}
		finally {
			StreamUtil.cleanUp(inputStream);
		}
	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void doKeyPayURLRedirect(ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long paymentFileId = ParamUtil.getLong(actionRequest, "paymentFileId");

		String okURL = "false";

		JSONObject responseJSON = JSONFactoryUtil.createJSONObject();

		try {
			if (paymentFileId > 0) {
				PaymentFile paymentFile =
					PaymentFileLocalServiceUtil.fetchPaymentFile(paymentFileId);

				// paymentFile.setPaymentResponseStatus(PaymentMgtUtil.PAYMENT_STATUS_KEYPAY_PENDING);

				PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
			}
			okURL = "true";
		}
		catch (Exception e) {
			okURL = "false";
			_log.error(e);
		}
		finally {
			responseJSON.put("okURL", okURL);
			PortletUtil.writeJSON(actionRequest, actionResponse, responseJSON);
		}
	}

	private Log _log = LogFactoryUtil.getLog(PaymentMgtFrontOfficePortlet.class.getName());

}