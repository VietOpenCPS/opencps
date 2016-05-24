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

import org.opencps.paymentmgt.model.PaymentConfig;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing PaymentConfig in entity cache.
 *
 * @author trungdk
 * @see PaymentConfig
 * @generated
 */
public class PaymentConfigCacheModel implements CacheModel<PaymentConfig>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(37);

		sb.append("{paymentConfigId=");
		sb.append(paymentConfigId);
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
		sb.append(", govAgencyOrganizationId=");
		sb.append(govAgencyOrganizationId);
		sb.append(", govAgencyName=");
		sb.append(govAgencyName);
		sb.append(", govAgencyTaxNo=");
		sb.append(govAgencyTaxNo);
		sb.append(", invoiceTemplateNo=");
		sb.append(invoiceTemplateNo);
		sb.append(", invoiceIssueNo=");
		sb.append(invoiceIssueNo);
		sb.append(", invoiceLastNo=");
		sb.append(invoiceLastNo);
		sb.append(", bankInfo=");
		sb.append(bankInfo);
		sb.append(", placeInfo=");
		sb.append(placeInfo);
		sb.append(", keypayDomain=");
		sb.append(keypayDomain);
		sb.append(", keypayVersion=");
		sb.append(keypayVersion);
		sb.append(", keypayMerchantCode=");
		sb.append(keypayMerchantCode);
		sb.append(", keypaySecureKey=");
		sb.append(keypaySecureKey);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public PaymentConfig toEntityModel() {
		PaymentConfigImpl paymentConfigImpl = new PaymentConfigImpl();

		paymentConfigImpl.setPaymentConfigId(paymentConfigId);
		paymentConfigImpl.setCompanyId(companyId);
		paymentConfigImpl.setGroupId(groupId);
		paymentConfigImpl.setUserId(userId);

		if (createDate == Long.MIN_VALUE) {
			paymentConfigImpl.setCreateDate(null);
		}
		else {
			paymentConfigImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			paymentConfigImpl.setModifiedDate(null);
		}
		else {
			paymentConfigImpl.setModifiedDate(new Date(modifiedDate));
		}

		paymentConfigImpl.setGovAgencyOrganizationId(govAgencyOrganizationId);

		if (govAgencyName == null) {
			paymentConfigImpl.setGovAgencyName(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setGovAgencyName(govAgencyName);
		}

		if (govAgencyTaxNo == null) {
			paymentConfigImpl.setGovAgencyTaxNo(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setGovAgencyTaxNo(govAgencyTaxNo);
		}

		if (invoiceTemplateNo == null) {
			paymentConfigImpl.setInvoiceTemplateNo(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setInvoiceTemplateNo(invoiceTemplateNo);
		}

		if (invoiceIssueNo == null) {
			paymentConfigImpl.setInvoiceIssueNo(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setInvoiceIssueNo(invoiceIssueNo);
		}

		if (invoiceLastNo == null) {
			paymentConfigImpl.setInvoiceLastNo(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setInvoiceLastNo(invoiceLastNo);
		}

		if (bankInfo == null) {
			paymentConfigImpl.setBankInfo(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setBankInfo(bankInfo);
		}

		if (placeInfo == null) {
			paymentConfigImpl.setPlaceInfo(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setPlaceInfo(placeInfo);
		}

		if (keypayDomain == null) {
			paymentConfigImpl.setKeypayDomain(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setKeypayDomain(keypayDomain);
		}

		if (keypayVersion == null) {
			paymentConfigImpl.setKeypayVersion(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setKeypayVersion(keypayVersion);
		}

		if (keypayMerchantCode == null) {
			paymentConfigImpl.setKeypayMerchantCode(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setKeypayMerchantCode(keypayMerchantCode);
		}

		if (keypaySecureKey == null) {
			paymentConfigImpl.setKeypaySecureKey(StringPool.BLANK);
		}
		else {
			paymentConfigImpl.setKeypaySecureKey(keypaySecureKey);
		}

		paymentConfigImpl.resetOriginalValues();

		return paymentConfigImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		paymentConfigId = objectInput.readLong();
		companyId = objectInput.readLong();
		groupId = objectInput.readLong();
		userId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		govAgencyOrganizationId = objectInput.readLong();
		govAgencyName = objectInput.readUTF();
		govAgencyTaxNo = objectInput.readUTF();
		invoiceTemplateNo = objectInput.readUTF();
		invoiceIssueNo = objectInput.readUTF();
		invoiceLastNo = objectInput.readUTF();
		bankInfo = objectInput.readUTF();
		placeInfo = objectInput.readUTF();
		keypayDomain = objectInput.readUTF();
		keypayVersion = objectInput.readUTF();
		keypayMerchantCode = objectInput.readUTF();
		keypaySecureKey = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(paymentConfigId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(userId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);
		objectOutput.writeLong(govAgencyOrganizationId);

		if (govAgencyName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(govAgencyName);
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

		if (invoiceLastNo == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(invoiceLastNo);
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

		if (keypayDomain == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(keypayDomain);
		}

		if (keypayVersion == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(keypayVersion);
		}

		if (keypayMerchantCode == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(keypayMerchantCode);
		}

		if (keypaySecureKey == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(keypaySecureKey);
		}
	}

	public long paymentConfigId;
	public long companyId;
	public long groupId;
	public long userId;
	public long createDate;
	public long modifiedDate;
	public long govAgencyOrganizationId;
	public String govAgencyName;
	public String govAgencyTaxNo;
	public String invoiceTemplateNo;
	public String invoiceIssueNo;
	public String invoiceLastNo;
	public String bankInfo;
	public String placeInfo;
	public String keypayDomain;
	public String keypayVersion;
	public String keypayMerchantCode;
	public String keypaySecureKey;
}