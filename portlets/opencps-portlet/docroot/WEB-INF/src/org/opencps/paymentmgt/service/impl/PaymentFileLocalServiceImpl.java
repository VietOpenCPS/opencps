/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.paymentmgt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.NoSuchDossierException;
import org.opencps.paymentmgt.NoSuchPaymentFileException;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.base.PaymentFileLocalServiceBaseImpl;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;

/**
 * The implementation of the Payment file local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.paymentmgt.service.PaymentFileLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungdk
 * @see org.opencps.paymentmgt.service.base.PaymentFileLocalServiceBaseImpl
 * @see org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil
 */
public class PaymentFileLocalServiceImpl
	extends PaymentFileLocalServiceBaseImpl {

	private Log _log = LogFactoryUtil.getLog(PaymentFileLocalServiceImpl.class);

	/**
	 * @param userId
	 * @param dossierId
	 * @param fileGroupId
	 * @param ownerUserId
	 * @param ownerOrganizationId
	 * @param govAgencyOrganizationId
	 * @param paymentName
	 * @param requestDatetime
	 * @param amount
	 * @param requestNote
	 * @param placeInfo
	 * @param serviceContext
	 * @return
	 * @throws SystemException
	 */
	public PaymentFile addPaymentFile(
		long userId, long dossierId, long fileGroupId, long ownerUserId,
		long ownerOrganizationId, long govAgencyOrganizationId,
		String paymentName, Date requestDatetime, Double amount,
		String requestNote, String placeInfo, ServiceContext serviceContext)
		throws SystemException {

		long paymentFileId =
			counterLocalService.increment(PaymentFile.class.getName());
		PaymentFile paymentFile = paymentFilePersistence.create(paymentFileId);
		
		Date now = new Date();

		paymentFile.setUserId(userId);

		paymentFile.setGroupId(serviceContext.getScopeGroupId());

		paymentFile.setCompanyId(serviceContext.getCompanyId());

		paymentFile.setCreateDate(now);

		paymentFile.setModifiedDate(now);

		paymentFile.setOid(PortalUUIDUtil.generate());

		paymentFile.setDossierId(dossierId);
		paymentFile.setFileGroupId(fileGroupId);
		paymentFile.setOwnerUserId(ownerUserId);
		paymentFile.setOwnerOrganizationId(ownerOrganizationId);
		paymentFile.setPaymentName(paymentName);
		paymentFile.setRequestDatetime(requestDatetime);
		paymentFile.setAmount(amount);
		paymentFile.setRequestNote(requestNote);
		paymentFile.setPlaceInfo(placeInfo);
		paymentFile.setPaymentStatus(0);
		paymentFile.setGovAgencyOrganizationId(govAgencyOrganizationId);

		// govAgencyOrganizationId > 0 insert
		// paymentFile.setGovAgencyTaxNo(govAgencyTaxNo);
		// paymentFile.setInvoiceTemplateNo(invoiceTemplateNo);
		// paymentFile.setInvoiceIssueNo(invoiceIssueNo);
		// paymentFile.setInvoiceNo(invoiceNo);

		return paymentFilePersistence.update(paymentFile);
	}

	/**
	 * @param dossierId
	 * @param fileGroupId
	 * @param ownerUserId
	 * @param ownerOrganizationId
	 * @param govAgencyOrganizationId
	 * @param paymentName
	 * @param requestDatetime
	 * @param amount
	 * @param requestNote
	 * @param placeInfo
	 * @return
	 * @throws SystemException
	 */
	public PaymentFile addPaymentFile(
		long dossierId, long fileGroupId, long ownerUserId,
		long ownerOrganizationId, long govAgencyOrganizationId,
		String paymentName, Date requestDatetime, Double amount,
		String requestNote, String placeInfo, String paymentOptions)
		throws SystemException {

		long paymentFileId =
			counterLocalService.increment(PaymentFile.class.getName());
		PaymentFile paymentFile = paymentFilePersistence.create(paymentFileId);
		
		Date now = new Date();

		paymentFile.setCreateDate(now);

		paymentFile.setModifiedDate(now);

		paymentFile.setOid(PortalUUIDUtil.generate());

		paymentFile.setDossierId(dossierId);
		paymentFile.setFileGroupId(fileGroupId);
		paymentFile.setOwnerUserId(ownerUserId);
		paymentFile.setOwnerOrganizationId(ownerOrganizationId);
		paymentFile.setPaymentName(paymentName);
		paymentFile.setRequestDatetime(requestDatetime);
		paymentFile.setAmount(amount);
		paymentFile.setRequestNote(requestNote);
		paymentFile.setPlaceInfo(placeInfo);
		paymentFile.setPaymentStatus(0);
		paymentFile.setPaymentOptions(paymentOptions);
		paymentFile.setGovAgencyOrganizationId(govAgencyOrganizationId);
		
		return paymentFilePersistence.update(paymentFile);
	}

	/**
	 * @param dossierId
	 * @param fileGroupId
	 * @param syncPaymentFile
	 * @return
	 * @throws SystemException
	 * @throws NoSuchDossierException 
	 */
	public PaymentFile syncPaymentFile(
		long dossierId, long fileGroupId, PaymentFile syncPaymentFile)
		throws SystemException {

		long paymentFileId =
			counterLocalService.increment(PaymentFile.class.getName());
		PaymentFile paymentFile = paymentFilePersistence.create(paymentFileId);
		
		Date now = new Date();

		paymentFile.setAccountUserName(syncPaymentFile.getAccountUserName());
		paymentFile.setAmount(syncPaymentFile.getAmount());
		paymentFile.setApproveDatetime(syncPaymentFile.getApproveDatetime());
		paymentFile.setApproveNote(syncPaymentFile.getApproveNote());
		paymentFile.setBankInfo(syncPaymentFile.getBankInfo());
		paymentFile.setCompanyId(syncPaymentFile.getCompanyId());
		paymentFile.setConfirmDatetime(syncPaymentFile.getConfirmDatetime());
		paymentFile.setConfirmFileEntryId(syncPaymentFile.getConfirmFileEntryId());
		paymentFile.setCreateDate(now);
		paymentFile.setDossierId(dossierId);
		paymentFile.setFileGroupId(fileGroupId);
		paymentFile.setGovAgencyOrganizationId(syncPaymentFile.getGovAgencyOrganizationId());
		paymentFile.setGovAgencyTaxNo(syncPaymentFile.getGovAgencyTaxNo());
		paymentFile.setInvoiceIssueNo(syncPaymentFile.getInvoiceIssueNo());
		paymentFile.setGroupId(syncPaymentFile.getGroupId());
		paymentFile.setInvoiceNo(syncPaymentFile.getInvoiceNo());
		paymentFile.setInvoiceTemplateNo(syncPaymentFile.getInvoiceTemplateNo());
		paymentFile.setKeypayGoodCode(syncPaymentFile.getKeypayGoodCode());
		paymentFile.setKeypayUrl(syncPaymentFile.getKeypayUrl());
		paymentFile.setKeypayMerchantCode(syncPaymentFile.getKeypayMerchantCode());
		paymentFile.setKeypayTransactionId(syncPaymentFile.getKeypayTransactionId());
		paymentFile.setModifiedDate(now);
		paymentFile.setOid(syncPaymentFile.getOid());
		paymentFile.setOwnerOrganizationId(syncPaymentFile.getOwnerOrganizationId());
		paymentFile.setOwnerUserId(syncPaymentFile.getOwnerUserId());
		paymentFile.setOwnerUserUuid(syncPaymentFile.getOwnerUserUuid());

		paymentFile.setPaymentMethod(syncPaymentFile.getPaymentMethod());
		paymentFile.setPaymentName(syncPaymentFile.getPaymentName());
		paymentFile.setPaymentOptions(syncPaymentFile.getPaymentOptions());
		paymentFile.setPaymentStatus(syncPaymentFile.getPaymentStatus());
		paymentFile.setPlaceInfo(syncPaymentFile.getPlaceInfo());
		paymentFile.setRequestDatetime(syncPaymentFile.getRequestDatetime());
		paymentFile.setRequestNote(syncPaymentFile.getRequestNote());
		paymentFile.setSyncStatus(2);
		paymentFile.setUserId(syncPaymentFile.getUserId());
		
		return paymentFilePersistence.update(paymentFile);
	}

	/**
	 * @param dossierId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countAllPaymentFile(long dossierId)
		throws PortalException, SystemException {

		return paymentFilePersistence.countByD_(dossierId);
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param isCitizen
	 * @param customerId
	 * @param paymentStatus
	 * @return
	 */
	public int countCustomerPaymentFile(
		long groupId, String keyword, boolean isCitizen, long customerId,
		int paymentStatus) {

		return paymentFileFinder.countCustomerPaymentFile(
			groupId, keyword, isCitizen, customerId, paymentStatus);
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param isCitizen
	 * @param customerId
	 * @param paymentStatus
	 * @return
	 */
	public int countCustomerPaymentFileNewRequest(
		long groupId, String keyword, boolean isCitizen, long customerId,
		int[] paymentStatus) {

		return paymentFileFinder.countCustomerPaymentFileNewRequest(
			groupId, keyword, isCitizen, customerId, paymentStatus);
	}

	/**
	 * @param dossierId
	 * @param paymentStatus
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countPaymentFile(long dossierId, int paymentStatus)
		throws PortalException, SystemException {

		return paymentFilePersistence.countByD_P(dossierId, paymentStatus);
	}

	/**
	 * @param groupId
	 * @param paymentStatus
	 * @param keywords
	 * @return
	 */
	public int countPaymentFiles(
		long groupId, int paymentStatus, long govAgencyOrganizationId, String keywords) {

		try {
			return paymentFileFinder.countPaymentFiles(
				groupId, paymentStatus, govAgencyOrganizationId, keywords);
		}
		catch (SystemException e) {
			// TODO Auto-generated catch block
			_log.error(e);
		}
		return 0;
	}

	/**
	 * @param keypayGoodCode
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public PaymentFile getByGoodCode(String keypayGoodCode)
		throws PortalException, SystemException {

		return paymentFilePersistence.fetchByG_C(keypayGoodCode);
	}

	/**
	 * @param keypayTransactionId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public PaymentFile getByTransactionId(long keypayTransactionId)
		throws PortalException, SystemException {

		return paymentFilePersistence.fetchByT_I(keypayTransactionId);
	}

	/**
	 * @param dossierId
	 * @return
	 * @throws SystemException
	 */
	public List<PaymentFile> getPaymentFileByD_(long dossierId)
		throws SystemException {

		return paymentFilePersistence.findByD_(dossierId);
	}

	/**
	 * @param groupId
	 * @param keypayGoodCode
	 * @return
	 * @throws SystemException
	 */
	public PaymentFile getPaymentFileByGoodCode(
		long groupId, String keypayGoodCode)
		throws SystemException {

		return paymentFilePersistence.fetchByGoodCode(groupId, keypayGoodCode);
	}

	/**
	 * @param keypayTransactionId
	 * @param keypayGoodCode
	 * @param amount
	 * @return
	 * @throws SystemException
	 */
	public PaymentFile getPaymentFileByMerchantResponse(
		long keypayTransactionId, String keypayGoodCode, double amount)
		throws SystemException {

		return paymentFilePersistence.fetchByMerchantResponse(
			keypayTransactionId, keypayGoodCode, amount);
	}

	/**
	 * @param oId
	 * @return
	 * @throws NoSuchPaymentFileException
	 * @throws SystemException
	 */
	public PaymentFile getPaymentFileByOID(String oId)
		throws NoSuchPaymentFileException, SystemException {

		return paymentFilePersistence.findByOID(oId);
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param isCitizen
	 * @param customerId
	 * @param paymentStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List<PaymentFile> searchCustomerPaymentFile(
		long groupId, String keyword, boolean isCitizen, long customerId,
		int paymentStatus, int start, int end, OrderByComparator obc) {

		return paymentFileFinder.searchCustomerPaymentFile(
			groupId, keyword, isCitizen, customerId, paymentStatus, start, end,
			obc);
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param isCitizen
	 * @param customerId
	 * @param paymentStatus
	 * @param start
	 * @param end
	 * @param obc
	 * @return
	 */
	public List<PaymentFile> searchCustomerPaymentFileNewRequest(
		long groupId, String keyword, boolean isCitizen, long customerId,
		int[] paymentStatus, int start, int end, OrderByComparator obc) {

		return paymentFileFinder.searchCustomerPaymentFileNewRequest(
			groupId, keyword, isCitizen, customerId, paymentStatus, start, end,
			obc);
	}

	/**
	 * @param groupId
	 * @param paymentStatus
	 * @param keywords
	 * @param start
	 * @param end
	 * @return
	 */
	public List<PaymentFile> searchPaymentFiles(
		long groupId, int paymentStatus, long govAgencyOrganizationId, String keywords, int start, int end) {

		List<PaymentFile> listPaymentFile = new ArrayList<PaymentFile>();

		try {
			listPaymentFile =
				paymentFileFinder.searchPaymentFiles(
					groupId, paymentStatus, govAgencyOrganizationId, keywords, start, end);
		}
		catch (SystemException e) {
			// TODO Auto-generated catch block
			_log.error(e);
		}
		return listPaymentFile;
	}

	/**
	 * @param oid
	 * @param typeUpdate
	 * @param paymentStatus
	 * @param paymentMethod
	 * @param approveNote
	 * @param bytes
	 * @param folderId
	 * @param sourceFileName
	 * @param mimeType
	 * @param title
	 * @param description
	 * @param changeLog
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public PaymentFile syncPaymentFile(
		String oid, String typeUpdate, int paymentStatus, int paymentMethod,
		String approveNote, byte[] bytes, String sourceFileName,
		String mimeType, String title, String description, String changeLog,
		ServiceContext serviceContext)
		throws PortalException, SystemException {

		System.out.println("////////////////////////////////paymentFileOID " +
			oid);

		PaymentFile paymentFile =
			paymentFileLocalService.getPaymentFileByOID(oid);

		System.out.println("////////////////////////////////paymentFile.getOid() " +
			paymentFile.getOid());

		paymentFile.setPaymentStatus(paymentStatus);
		paymentFile.setPaymentMethod(paymentMethod);

		if (Validator.isNotNull(approveNote)) {
			paymentFile.setApproveNote(approveNote);
		} else {
			paymentFile.setApproveNote(StringPool.BLANK);
		}


		User user = UserLocalServiceUtil.getUser(serviceContext.getUserId());

		PermissionChecker permissionChecker;

		try {
			permissionChecker = PermissionCheckerFactoryUtil.create(user);
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}
		catch (Exception e) {
		}

		if (bytes != null && bytes.length > 0 &&
			Validator.isNotNull(sourceFileName) && Validator.isNotNull(title) &&
			Validator.isNotNull(mimeType) &&
			typeUpdate.equals(WebKeys.SYNC_PAY_SEND_CONFIRM)) {

			DLFolder dlFolder =
				DLFolderUtil.getSyncPaymentFolder(
					serviceContext.getScopeGroupId(), new Date(), oid,
					serviceContext);

			System.out.println("////////////////////////////////Add Folder " +
				dlFolder.getFolderId());

			FileEntry fileEntry =
				dlAppLocalService.addFileEntry(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(), dlFolder.getFolderId(),
					sourceFileName, mimeType, title, description, changeLog,
					bytes, serviceContext);

			System.out.println("////////////////////////////////Add FileEntry " +
				fileEntry.getFileEntryId());

			paymentFile.setConfirmFileEntryId(fileEntry.getFileEntryId());
		}

		paymentFilePersistence.update(paymentFile);

		return paymentFile;

	}

	/**
	 * @param paymentFileId
	 * @param keypayUrl
	 * @param keypayTransactionId
	 * @param keypayGoodCode
	 * @param keypayMerchantCode
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public PaymentFile updatePaymentFile(
		long paymentFileId, String keypayUrl, long keypayTransactionId,
		String keypayGoodCode, String keypayMerchantCode)
		throws PortalException, SystemException {

		PaymentFile paymentFile =
			paymentFilePersistence.fetchByPrimaryKey(paymentFileId);

		paymentFile.setKeypayUrl(keypayUrl);
		paymentFile.setKeypayTransactionId(keypayTransactionId);
		paymentFile.setKeypayGoodCode(keypayGoodCode);
		paymentFile.setKeypayMerchantCode(keypayMerchantCode);

		paymentFilePersistence.update(paymentFile);

		return paymentFile;

	}
	/**
	 * @param paymentStatus
	 * @param paymentGateStatus
	 * @param orderByComparator
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<PaymentFile> getPaymentFileByParam(
		int[] paymentStatus, int[] paymentGateStatus,int[] recheckStatus, boolean orderByASC)
		throws PortalException, SystemException {

		return paymentFileFinder.getPaymentFileByParam(paymentStatus,paymentGateStatus,recheckStatus,orderByASC);

	}

}