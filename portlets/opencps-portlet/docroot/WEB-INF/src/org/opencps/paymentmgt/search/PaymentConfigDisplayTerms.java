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


public class PaymentConfigDisplayTerms extends DisplayTerms {
	public static final String COMPANY_ID = "companyId";
	public static final String CREATE_DATE = "createDate";
	public static final String GROUP_ID = "groupId";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String USER_ID = "userId";

	public static final String PAYMENT_CONFIG_ID = "paymentConfigId";
	public static final String GOV_AGENCY_ORGANIZATION_ID = "govAgencyOrganizationId";
	public static final String GOV_AGENCY_NAME = "govAgencyName";
	public static final String GOV_AGENCY_TAX_NO = "govAgencyTaxNo";
	public static final String INVOICE_TEMPLATE_NO = "invoiceTemplateNo";
	public static final String INVOICE_ISSUE_NO = "invoiceIssueNo";
	public static final String INVOICE_LAST_NO = "invoiceLastNo";
	public static final String BANK_INFO = "bankInfo";
	public static final String PLACE_INFO = "placeInfo";
	public static final String KEYPAY_DOMAIN = "keypayDomain";
	public static final String KEYPAY_VERSION = "keypayVersion";
	public static final String KEYPAY_MERCHANT_CODE = "keypayMerchantCode";
	public static final String KEYPAY_SECURE_KEY = "keypaySecureKey";
	
	public PaymentConfigDisplayTerms(PortletRequest portletRequest) {

		super(portletRequest);

		createDate =
		    ParamUtil.getDate(
		        portletRequest,
		        CREATE_DATE,
		        DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));

		modifiedDate =
		    ParamUtil.getDate(
		        portletRequest,
		        MODIFIED_DATE,
		        DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		userId = ParamUtil
					    .getLong(portletRequest, USER_ID);

	}
	
	protected long userId;
	protected Date createDate;
	protected Date modifiedDate;
	
	protected long govAgencyOrganizationId;
	protected String govAgencyName;
	protected String govAgencyTaxNo;
	protected String invoiceTemplateNo;
	protected String invoiceIssueNo;
	protected String invoiceLastNo;
	protected String bankInfo;
	protected String placeInfo;
	protected String keypayDomain;
	protected String keypayVersion;
	protected String keypayMerchantCode;
	protected String keypaySecureKey;

	
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
	
    public long getGovAgencyOrganizationId() {
    
    	return govAgencyOrganizationId;
    }
	
    public void setGovAgencyOrganizationId(long govAgencyOrganizationId) {
    
    	this.govAgencyOrganizationId = govAgencyOrganizationId;
    }
	
    public String getGovAgencyName() {
    
    	return govAgencyName;
    }
	
    public void setGovAgencyName(String govAgencyName) {
    
    	this.govAgencyName = govAgencyName;
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
	
    public String getInvoiceLastNo() {
    
    	return invoiceLastNo;
    }
	
    public void setInvoiceLastNo(String invoiceLastNo) {
    
    	this.invoiceLastNo = invoiceLastNo;
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
	
    public String getKeypayDomain() {
    
    	return keypayDomain;
    }
	
    public void setKeypayDomain(String keypayDomain) {
    
    	this.keypayDomain = keypayDomain;
    }
	
    public String getKeypayVersion() {
    
    	return keypayVersion;
    }
	
    public void setKeypayVersion(String keypayVersion) {
    
    	this.keypayVersion = keypayVersion;
    }
	
    public String getKeypayMerchantCode() {
    
    	return keypayMerchantCode;
    }
	
    public void setKeypayMerchantCode(String keypayMerchantCode) {
    
    	this.keypayMerchantCode = keypayMerchantCode;
    }
	
    public String getKeypaySecureKey() {
    
    	return keypaySecureKey;
    }
	
    public void setKeypaySecureKey(String keypaySecureKey) {
    
    	this.keypaySecureKey = keypaySecureKey;
    }
}
