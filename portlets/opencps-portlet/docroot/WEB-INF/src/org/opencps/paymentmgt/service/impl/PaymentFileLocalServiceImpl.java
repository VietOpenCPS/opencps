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

import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.model.DossierFile;

import java.util.List;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.paymentmgt.model.PaymentFile;
import org.opencps.paymentmgt.service.base.PaymentFileLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * The implementation of the Payment file local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.paymentmgt.service.PaymentFileLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author trungdk
 * @see org.opencps.paymentmgt.service.base.PaymentFileLocalServiceBaseImpl
 * @see org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil
 */
public class PaymentFileLocalServiceImpl extends PaymentFileLocalServiceBaseImpl {
	
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
	
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil} to access the Payment file local service.
	 */
	public PaymentFile addPaymentFile(long userId,
		long dossierId, long fileGroupId,long ownerUserId, long ownerOrganizationId,long govAgencyOrganizationId, String paymentName,
		Date requestDatetime,Double amount, String requestNote, String placeInfo, 
	    ServiceContext serviceContext)
	    throws SystemException {

		long paymentFileId = counterLocalService
		    .increment(PaymentFile.class
		        .getName());
		PaymentFile paymentFile = paymentFilePersistence
		    .create(paymentFileId);

		Date now = new Date();

		paymentFile.setUserId(userId);
		
		paymentFile.setGroupId(serviceContext.getScopeGroupId());
		
		paymentFile.setCompanyId(serviceContext.getCompanyId());
		
		paymentFile.setCreateDate(now);
		
		paymentFile.setModifiedDate(now);
//		paymentFile
//		    .setUuid(PortalUUIDUtil
//		        .generate());
		
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
		
		//govAgencyOrganizationId > 0 insert
//		paymentFile.setGovAgencyTaxNo(govAgencyTaxNo);
//		paymentFile.setInvoiceTemplateNo(invoiceTemplateNo);
//		paymentFile.setInvoiceIssueNo(invoiceIssueNo);
//		paymentFile.setInvoiceNo(invoiceNo);
        

		return paymentFilePersistence.update(paymentFile);
	}
	
	public List<PaymentFile> searchPaymentFiles(long groupId, String paymentStatus, String keywords, int start, int end) {
		return paymentFileFinder.searchPaymentFiles(groupId, paymentStatus, keywords, start, end);
    }
	public int countPaymentFiles(long groupId, String paymentStatus, String keywords) {
		return paymentFileFinder.countPaymentFiles(groupId, paymentStatus, keywords);
    }
	
	public int countCustomerPaymentFile(long groupId, String keyword, boolean isCitizen, long customerId, int paymentStatus) {
		return paymentFileFinder.countCustomerPaymentFile(groupId, keyword, isCitizen, customerId, paymentStatus);
	}
	
	public List<PaymentFile> searchCustomerPaymentFile(long groupId, String keyword, boolean isCitizen, long customerId, int paymentStatus, int start, int end, OrderByComparator obc) {
		return paymentFileFinder.searchCustomerPaymentFile(groupId, keyword, isCitizen, customerId, paymentStatus, start, end, obc);
	}
	
	public PaymentFile getPaymentFileByGoodCode(long groupId, String keypayGoodCode) throws SystemException {
		return paymentFilePersistence.fetchByGoodCode(groupId, keypayGoodCode);
	}
	
	public PaymentFile getPaymentFileByMerchantResponse(long keypayTransactionId, String keypayGoodCode, double amount) throws SystemException {
		return paymentFilePersistence.fetchByMerchantResponse(keypayTransactionId, keypayGoodCode, amount);
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
	    String requestNote, String placeInfo)
	    throws SystemException {

		long paymentFileId =
		    counterLocalService.increment(PaymentFile.class.getName());
		PaymentFile paymentFile = paymentFilePersistence.create(paymentFileId);

		Date now = new Date();


		paymentFile.setCreateDate(now);

		paymentFile.setModifiedDate(now);

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

		return paymentFilePersistence.update(paymentFile);
	}

}
