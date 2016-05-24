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

package org.opencps.paymentmgt.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import org.opencps.paymentmgt.model.PaymentFile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing PaymentFile in entity cache.
 *
 * @author trungdk
 * @see PaymentFile
 * @generated
 */
public class PaymentFileCacheModel implements CacheModel<PaymentFile>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(67);

		sb.append("{paymentFileId=");
		sb.append(paymentFileId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", dossierId=");
		sb.append(dossierId);
		sb.append(", fileGroupId=");
		sb.append(fileGroupId);
		sb.append(", ownerUserId=");
		sb.append(ownerUserId);
		sb.append(", ownerOrganizationId=");
		sb.append(ownerOrganizationId);
		sb.append(", govAgencyOrganizationId=");
		sb.append(govAgencyOrganizationId);
		sb.append(", paymentName=");
		sb.append(paymentName);
		sb.append(", requestDatetime=");
		sb.append(requestDatetime);
		sb.append(", amount=");
		sb.append(amount);
		sb.append(", requestNote=");
		sb.append(requestNote);
		sb.append(", keypayUrl=");
		sb.append(keypayUrl);
		sb.append(", keypayTransactionId=");
		sb.append(keypayTransactionId);
		sb.append(", keypayGoodCode=");
		sb.append(keypayGoodCode);
		sb.append(", keypayMerchantCode=");
		sb.append(keypayMerchantCode);
		sb.append(", bankInfo=");
		sb.append(bankInfo);
		sb.append(", placeInfo=");
		sb.append(placeInfo);
		sb.append(", paymentStatus=");
		sb.append(paymentStatus);
		sb.append(", paymentMethod=");
		sb.append(paymentMethod);
		sb.append(", confirmDatetime=");
		sb.append(confirmDatetime);
		sb.append(", confirmFileEntryId=");
		sb.append(confirmFileEntryId);
		sb.append(", approveDatetime=");
		sb.append(approveDatetime);
		sb.append(", accountUserName=");
		sb.append(accountUserName);
		sb.append(", approveNote=");
		sb.append(approveNote);
		sb.append(", govAgencyTaxNo=");
		sb.append(govAgencyTaxNo);
		sb.append(", invoiceTemplateNo=");
		sb.append(invoiceTemplateNo);
		sb.append(", invoiceIssueNo=");
		sb.append(invoiceIssueNo);
		sb.append(", invoiceNo=");
		sb.append(invoiceNo);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public PaymentFile toEntityModel() {
		PaymentFileImpl paymentFileImpl = new PaymentFileImpl();

		paymentFileImpl.setPaymentFileId(paymentFileId);
		paymentFileImpl.setCompanyId(companyId);
		paymentFileImpl.setGroupId(groupId);
		paymentFileImpl.setUserId(userId);

		if (createDate == Long.MIN_VALUE) {
			paymentFileImpl.setCreateDate(null);
		}
		else {
			paymentFileImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			paymentFileImpl.setModifiedDate(null);
		}
		else {
			paymentFileImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (uuid == null) {
			paymentFileImpl.setUuid(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setUuid(uuid);
		}

		paymentFileImpl.setDossierId(dossierId);
		paymentFileImpl.setFileGroupId(fileGroupId);
		paymentFileImpl.setOwnerUserId(ownerUserId);
		paymentFileImpl.setOwnerOrganizationId(ownerOrganizationId);
		paymentFileImpl.setGovAgencyOrganizationId(govAgencyOrganizationId);

		if (paymentName == null) {
			paymentFileImpl.setPaymentName(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setPaymentName(paymentName);
		}

		if (requestDatetime == Long.MIN_VALUE) {
			paymentFileImpl.setRequestDatetime(null);
		}
		else {
			paymentFileImpl.setRequestDatetime(new Date(requestDatetime));
		}

		paymentFileImpl.setAmount(amount);

		if (requestNote == null) {
			paymentFileImpl.setRequestNote(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setRequestNote(requestNote);
		}

		if (keypayUrl == null) {
			paymentFileImpl.setKeypayUrl(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setKeypayUrl(keypayUrl);
		}

		paymentFileImpl.setKeypayTransactionId(keypayTransactionId);

		if (keypayGoodCode == null) {
			paymentFileImpl.setKeypayGoodCode(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setKeypayGoodCode(keypayGoodCode);
		}

		if (keypayMerchantCode == null) {
			paymentFileImpl.setKeypayMerchantCode(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setKeypayMerchantCode(keypayMerchantCode);
		}

		if (bankInfo == null) {
			paymentFileImpl.setBankInfo(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setBankInfo(bankInfo);
		}

		if (placeInfo == null) {
			paymentFileImpl.setPlaceInfo(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setPlaceInfo(placeInfo);
		}

		paymentFileImpl.setPaymentStatus(paymentStatus);
		paymentFileImpl.setPaymentMethod(paymentMethod);

		if (confirmDatetime == Long.MIN_VALUE) {
			paymentFileImpl.setConfirmDatetime(null);
		}
		else {
			paymentFileImpl.setConfirmDatetime(new Date(confirmDatetime));
		}

		paymentFileImpl.setConfirmFileEntryId(confirmFileEntryId);

		if (approveDatetime == Long.MIN_VALUE) {
			paymentFileImpl.setApproveDatetime(null);
		}
		else {
			paymentFileImpl.setApproveDatetime(new Date(approveDatetime));
		}

		if (accountUserName == null) {
			paymentFileImpl.setAccountUserName(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setAccountUserName(accountUserName);
		}

		if (approveNote == null) {
			paymentFileImpl.setApproveNote(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setApproveNote(approveNote);
		}

		if (govAgencyTaxNo == null) {
			paymentFileImpl.setGovAgencyTaxNo(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setGovAgencyTaxNo(govAgencyTaxNo);
		}

		if (invoiceTemplateNo == null) {
			paymentFileImpl.setInvoiceTemplateNo(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setInvoiceTemplateNo(invoiceTemplateNo);
		}

		if (invoiceIssueNo == null) {
			paymentFileImpl.setInvoiceIssueNo(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setInvoiceIssueNo(invoiceIssueNo);
		}

		if (invoiceNo == null) {
			paymentFileImpl.setInvoiceNo(StringPool.BLANK);
		}
		else {
			paymentFileImpl.setInvoiceNo(invoiceNo);
		}

		paymentFileImpl.resetOriginalValues();

		return paymentFileImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		paymentFileId = objectInput.readLong();
		companyId = objectInput.readLong();
		groupId = objectInput.readLong();
		userId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		uuid = objectInput.readUTF();
		dossierId = objectInput.readLong();
		fileGroupId = objectInput.readLong();
		ownerUserId = objectInput.readLong();
		ownerOrganizationId = objectInput.readLong();
		govAgencyOrganizationId = objectInput.readLong();
		paymentName = objectInput.readUTF();
		requestDatetime = objectInput.readLong();
		amount = objectInput.readDouble();
		requestNote = objectInput.readUTF();
		keypayUrl = objectInput.readUTF();
		keypayTransactionId = objectInput.readLong();
		keypayGoodCode = objectInput.readUTF();
		keypayMerchantCode = objectInput.readUTF();
		bankInfo = objectInput.readUTF();
		placeInfo = objectInput.readUTF();
		paymentStatus = objectInput.readInt();
		paymentMethod = objectInput.readInt();
		confirmDatetime = objectInput.readLong();
		confirmFileEntryId = objectInput.readLong();
		approveDatetime = objectInput.readLong();
		accountUserName = objectInput.readUTF();
		approveNote = objectInput.readUTF();
		govAgencyTaxNo = objectInput.readUTF();
		invoiceTemplateNo = objectInput.readUTF();
		invoiceIssueNo = objectInput.readUTF();
		invoiceNo = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(paymentFileId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(userId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (uuid == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(dossierId);
		objectOutput.writeLong(fileGroupId);
		objectOutput.writeLong(ownerUserId);
		objectOutput.writeLong(ownerOrganizationId);
		objectOutput.writeLong(govAgencyOrganizationId);

		if (paymentName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(paymentName);
		}

		objectOutput.writeLong(requestDatetime);
		objectOutput.writeDouble(amount);

		if (requestNote == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(requestNote);
		}

		if (keypayUrl == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(keypayUrl);
		}

		objectOutput.writeLong(keypayTransactionId);

		if (keypayGoodCode == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(keypayGoodCode);
		}

		if (keypayMerchantCode == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(keypayMerchantCode);
		}

		if (bankInfo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(bankInfo);
		}

		if (placeInfo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(placeInfo);
		}

		objectOutput.writeInt(paymentStatus);
		objectOutput.writeInt(paymentMethod);
		objectOutput.writeLong(confirmDatetime);
		objectOutput.writeLong(confirmFileEntryId);
		objectOutput.writeLong(approveDatetime);

		if (accountUserName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(accountUserName);
		}

		if (approveNote == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(approveNote);
		}

		if (govAgencyTaxNo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(govAgencyTaxNo);
		}

		if (invoiceTemplateNo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(invoiceTemplateNo);
		}

		if (invoiceIssueNo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(invoiceIssueNo);
		}

		if (invoiceNo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(invoiceNo);
		}
	}

	public long paymentFileId;
	public long companyId;
	public long groupId;
	public long userId;
	public long createDate;
	public long modifiedDate;
	public String uuid;
	public long dossierId;
	public long fileGroupId;
	public long ownerUserId;
	public long ownerOrganizationId;
	public long govAgencyOrganizationId;
	public String paymentName;
	public long requestDatetime;
	public double amount;
	public String requestNote;
	public String keypayUrl;
	public long keypayTransactionId;
	public String keypayGoodCode;
	public String keypayMerchantCode;
	public String bankInfo;
	public String placeInfo;
	public int paymentStatus;
	public int paymentMethod;
	public long confirmDatetime;
	public long confirmFileEntryId;
	public long approveDatetime;
	public String accountUserName;
	public String approveNote;
	public String govAgencyTaxNo;
	public String invoiceTemplateNo;
	public String invoiceIssueNo;
	public String invoiceNo;
}