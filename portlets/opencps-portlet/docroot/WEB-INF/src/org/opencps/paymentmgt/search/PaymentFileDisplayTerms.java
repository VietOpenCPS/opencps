/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.paymentmgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;

public class PaymentFileDisplayTerms extends DisplayTerms {

	public static final String COMPANY_ID = "companyId";
	public static final String CREATE_DATE = "createDate";
	public static final String GROUP_ID = "groupId";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String USER_ID = "userId";

	public static final String DOSSIER_ID = "dossierId";
	public static final String FILE_GROUP_ID = "fileGroupId";
	public static final String OWNER_USER_ID = "ownerUserId";
	public static final String OWNER_ORGANIZATION_ID = "ownerOrganizationId";
	public static final String GOV_AGENCY_ORGANIZATION_ID =
	    "govAgencyOrganizationId";
	public static final String PAYMENT_NAME = "paymentName";
	public static final String REQUEST_DATETIME = "requestDatetime";
	public static final String AMOUNT = "amount";
	public static final String REQUEST_NOTE = "requestNote";
	public static final String PAYMENT_OPTIONS = "paymentOptions";
	public static final String KEYPAY_URL = "keypayUrl";
	public static final String KEYPAY_TRANSACTION_ID = "keypayTransactionId";
	public static final String KEYPAY_GOOD_CODE = "keypayGoodCode";
	public static final String KEYPAY_MERCHANT_CODE = "keypayMerchantCode";
	public static final String BANK_INFO = "bankInfo";
	public static final String PLACE_INFO = "placeInfo";
	public static final String PAYMENT_STATUS = "paymentStatus";
	public static final String PAYMENT_METHOD = "paymentMethod";
	public static final String CONFIRM_DATETIME = "confirmDatetime";
	public static final String CONFIRM_FILE_ENTRY_ID = "confirmFileEntryId";
	public static final String APPROVE_DATETIME = "approveDatetime";
	public static final String ACCOUNT_USERNAME = "accountUsername";
	public static final String APPROVE_NOTE = "approveNote";
	public static final String GOV_AGENCY_TAX_NO = "govAgencyTaxNo";
	public static final String INVOICE_TEMPLATE_NO = "invoiceTemplateNo";
	public static final String INVOICE_ISSUE_NO = "invoiceIssueNo";
	public static final String INVOICE_NO = "invoiceNo";

	public PaymentFileDisplayTerms(PortletRequest portletRequest) {

		super(portletRequest);

		createDate =
		    ParamUtil.getDate(
		        portletRequest,
		        CREATE_DATE,
		        DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));

		dossierId = ParamUtil.getLong(portletRequest, DOSSIER_ID, 0L);
		modifiedDate =
		    ParamUtil.getDate(
		        portletRequest,
		        MODIFIED_DATE,
		        DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		userId = ParamUtil
					    .getLong(portletRequest, USER_ID);

	}

	protected long dossierId;

	public long getDossierId() {

		return dossierId;
	}

	public void setDossierId(long dossierId) {

		this.dossierId = dossierId;
	}

	public long getGroupId() {

		return groupId;
	}

	public void setGroupId(long groupId) {

		this.groupId = groupId;
	}

	public long getCompanyId() {

		return companyId;
	}

	public void setCompanyId(long companyId) {

		this.companyId = companyId;
	}

	public long getUserId() {

		return userId;
	}

	public void setUserId(long userId) {

		this.userId = userId;
	}

	public Date getCreateDate() {

		return createDate;
	}

	public void setCreateDate(Date createDate) {

		this.createDate = createDate;
	}

	public Date getModifiedDate() {

		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {

		this.modifiedDate = modifiedDate;
	}

	public Date getRequestDatetime() {

		return requestDatetime;
	}

	public void setRequestDatetime(Date requestDatetime) {

		this.requestDatetime = requestDatetime;
	}

	public long getFileGroupId() {

		return fileGroupId;
	}

	public void setFileGroupId(long fileGroupId) {

		this.fileGroupId = fileGroupId;
	}

	public long getOwnerUserId() {

		return ownerUserId;
	}

	public void setOwnerUserId(long ownerUserId) {

		this.ownerUserId = ownerUserId;
	}

	public long getOwnerOrganizationId() {

		return ownerOrganizationId;
	}

	public void setOwnerOrganizationId(long ownerOrganizationId) {

		this.ownerOrganizationId = ownerOrganizationId;
	}

	public long getGovAgencyOrganizationId() {

		return govAgencyOrganizationId;
	}

	public void setGovAgencyOrganizationId(long govAgencyOrganizationId) {

		this.govAgencyOrganizationId = govAgencyOrganizationId;
	}

	public String getPaymentName() {

		return paymentName;
	}

	public void setPaymentName(String paymentName) {

		this.paymentName = paymentName;
	}

	public double getAmount() {

		return amount;
	}

	public void setAmount(double amount) {

		this.amount = amount;
	}

	public String getRequestNote() {

		return requestNote;
	}

	public void setRequestNote(String requestNote) {

		this.requestNote = requestNote;
	}

	public int getPaymentOptions() {

		return paymentOptions;
	}

	public void setPaymentOptions(int paymentOptions) {

		this.paymentOptions = paymentOptions;
	}

	public String getKeypayUrl() {

		return keypayUrl;
	}

	public void setKeypayUrl(String keypayUrl) {

		this.keypayUrl = keypayUrl;
	}

	public int getKeypayTransactionId() {

		return keypayTransactionId;
	}

	public void setKeypayTransactionId(int keypayTransactionId) {

		this.keypayTransactionId = keypayTransactionId;
	}

	public String getKeypayGoodCode() {

		return keypayGoodCode;
	}

	public void setKeypayGoodCode(String keypayGoodCode) {

		this.keypayGoodCode = keypayGoodCode;
	}

	public String getKeypayMerchantCode() {

		return keypayMerchantCode;
	}

	public void setKeypayMerchantCode(String keypayMerchantCode) {

		this.keypayMerchantCode = keypayMerchantCode;
	}

	public String getBankInfo() {

		return bankInfo;
	}

	public void setBankInfo(String bankInfo) {

		this.bankInfo = bankInfo;
	}

	public String getPlaceInfo() {

		return placeInfo;
	}

	public void setPlaceInfo(String placeInfo) {

		this.placeInfo = placeInfo;
	}

	public int getPaymentStatus() {

		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {

		this.paymentStatus = paymentStatus;
	}

	public int getPaymentMethod() {

		return paymentMethod;
	}

	public void setPaymentMethod(int paymentMethod) {

		this.paymentMethod = paymentMethod;
	}

	public Date getConfirmDatetime() {

		return confirmDatetime;
	}

	public void setConfirmDatetime(Date confirmDatetime) {

		this.confirmDatetime = confirmDatetime;
	}

	public long getConfirmFileEntryId() {

		return confirmFileEntryId;
	}

	public void setConfirmFileEntryId(long confirmFileEntryId) {

		this.confirmFileEntryId = confirmFileEntryId;
	}

	public Date getApproveDatetime() {

		return approveDatetime;
	}

	public void setApproveDatetime(Date approveDatetime) {

		this.approveDatetime = approveDatetime;
	}

	public String getAccountUserName() {

		return accountUserName;
	}

	public void setAccountUserName(String accountUserName) {

		this.accountUserName = accountUserName;
	}

	public String getApproveNote() {

		return approveNote;
	}

	public void setApproveNote(String approveNote) {

		this.approveNote = approveNote;
	}

	public String getGovAgencyTaxNo() {

		return govAgencyTaxNo;
	}

	public void setGovAgencyTaxNo(String govAgencyTaxNo) {

		this.govAgencyTaxNo = govAgencyTaxNo;
	}

	public String getInvoiceTemplateNo() {

		return invoiceTemplateNo;
	}

	public void setInvoiceTemplateNo(String invoiceTemplateNo) {

		this.invoiceTemplateNo = invoiceTemplateNo;
	}

	public String getInvoiceIssueNo() {

		return invoiceIssueNo;
	}

	public void setInvoiceIssueNo(String invoiceIssueNo) {

		this.invoiceIssueNo = invoiceIssueNo;
	}

	public String getInvoiceNo() {

		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {

		this.invoiceNo = invoiceNo;
	}

	protected long groupId;
	protected long companyId;
	protected long userId;
	protected Date createDate;
	protected Date modifiedDate;
	protected Date requestDatetime;

	protected long fileGroupId;
	protected long ownerUserId;
	protected long ownerOrganizationId;
	protected long govAgencyOrganizationId;
	protected String paymentName;
	protected double amount;
	protected String requestNote;
	protected int paymentOptions;
	protected String keypayUrl;
	protected int keypayTransactionId;
	protected String keypayGoodCode;
	protected String keypayMerchantCode;
	protected String bankInfo;
	protected String placeInfo;
	protected int paymentStatus;
	protected int paymentMethod;
	protected Date confirmDatetime;
	protected long confirmFileEntryId;
	protected Date approveDatetime;
	protected String accountUserName;
	protected String approveNote;
	protected String govAgencyTaxNo;
	protected String invoiceTemplateNo;
	protected String invoiceIssueNo;
	protected String invoiceNo;
}
