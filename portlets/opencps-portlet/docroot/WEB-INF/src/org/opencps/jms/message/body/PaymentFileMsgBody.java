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

package org.opencps.jms.message.body;

import java.io.Serializable;
import java.util.Date;

import com.liferay.portal.service.ServiceContext;

/**
 * @author trungnt
 */
public class PaymentFileMsgBody implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return the paymentMethod
	 */
	public int getPaymentMethod() {

		return paymentMethod;
	}

	/**
	 * @param paymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(int paymentMethod) {

		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the confirmDatetime
	 */
	public Date getConfirmDatetime() {

		return confirmDatetime;
	}

	/**
	 * @param confirmDatetime
	 *            the confirmDatetime to set
	 */
	public void setConfirmDatetime(Date confirmDatetime) {

		this.confirmDatetime = confirmDatetime;
	}

	/**
	 * @return the confirmFileEntry
	 */
	public byte[] getConfirmFileEntry() {

		return confirmFileEntry;
	}

	/**
	 * @param confirmFileEntry
	 *            the confirmFileEntry to set
	 */
	public void setConfirmFileEntry(byte[] confirmFileEntry) {

		this.confirmFileEntry = confirmFileEntry;
	}

	/**
	 * @return the confirmNote
	 */
	public String getConfirmNote() {

		return confirmNote;
	}

	/**
	 * @param confirmNote
	 *            the confirmNote to set
	 */
	public void setConfirmNote(String confirmNote) {

		this.confirmNote = confirmNote;
	}

	/**
	 * @return the approveDatetime
	 */
	public Date getApproveDatetime() {

		return approveDatetime;
	}

	/**
	 * @param approveDatetime
	 *            the approveDatetime to set
	 */
	public void setApproveDatetime(Date approveDatetime) {

		this.approveDatetime = approveDatetime;
	}

	/**
	 * @return the accountUserName
	 */
	public String getAccountUserName() {

		return accountUserName;
	}

	/**
	 * @param accountUserName
	 *            the accountUserName to set
	 */
	public void setAccountUserName(String accountUserName) {

		this.accountUserName = accountUserName;
	}

	/**
	 * @return the approveNote
	 */
	public String getApproveNote() {

		return approveNote;
	}

	/**
	 * @param approveNote
	 *            the approveNote to set
	 */
	public void setApproveNote(String approveNote) {

		this.approveNote = approveNote;
	}

	/**
	 * @return the govAgencyTaxNo
	 */
	public String getGovAgencyTaxNo() {

		return govAgencyTaxNo;
	}

	/**
	 * @param govAgencyTaxNo
	 *            the govAgencyTaxNo to set
	 */
	public void setGovAgencyTaxNo(String govAgencyTaxNo) {

		this.govAgencyTaxNo = govAgencyTaxNo;
	}

	/**
	 * @return the invoiceTemplateNo
	 */
	public String getInvoiceTemplateNo() {

		return invoiceTemplateNo;
	}

	/**
	 * @param invoiceTemplateNo
	 *            the invoiceTemplateNo to set
	 */
	public void setInvoiceTemplateNo(String invoiceTemplateNo) {

		this.invoiceTemplateNo = invoiceTemplateNo;
	}

	/**
	 * @return the invoiceIssueNo
	 */
	public String getInvoiceIssueNo() {

		return invoiceIssueNo;
	}

	/**
	 * @param invoiceIssueNo
	 *            the invoiceIssueNo to set
	 */
	public void setInvoiceIssueNo(String invoiceIssueNo) {

		this.invoiceIssueNo = invoiceIssueNo;
	}

	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {

		return invoiceNo;
	}

	/**
	 * @param invoiceNo
	 *            the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {

		this.invoiceNo = invoiceNo;
	}

	/**
	 * @return the syncStatus
	 */
	public int getSyncStatus() {

		return syncStatus;
	}

	/**
	 * @param syncStatus
	 *            the syncStatus to set
	 */
	public void setSyncStatus(int syncStatus) {

		this.syncStatus = syncStatus;
	}

	/**
	 * @return the serviceContext
	 */
	public ServiceContext getServiceContext() {

		return serviceContext;
	}

	/**
	 * @param serviceContext
	 *            the serviceContext to set
	 */
	public void setServiceContext(ServiceContext serviceContext) {

		this.serviceContext = serviceContext;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {

		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {

		this.extension = extension;
	}

	/**
	 * @return the fileDescription
	 */
	public String getFileDescription() {

		return fileDescription;
	}

	/**
	 * @param fileDescription
	 *            the fileDescription to set
	 */
	public void setFileDescription(String fileDescription) {

		this.fileDescription = fileDescription;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {

		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {

		this.fileName = fileName;
	}

	/**
	 * @return the fileTitle
	 */
	public String getFileTitle() {

		return fileTitle;
	}

	/**
	 * @param fileTitle
	 *            the fileTitle to set
	 */
	public void setFileTitle(String fileTitle) {

		this.fileTitle = fileTitle;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {

		return mimeType;
	}

	/**
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(String mimeType) {

		this.mimeType = mimeType;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {

		return oid;
	}

	/**
	 * @param oid
	 *            the oid to set
	 */
	public void setOid(String oid) {

		this.oid = oid;
	}

	/**
	 * @return the typeUpdate
	 */
	public String getTypeUpdate() {

		return typeUpdate;
	}

	/**
	 * @param typeUpdate
	 *            the typeUpdate to set
	 */
	public void setTypeUpdate(String typeUpdate) {

		this.typeUpdate = typeUpdate;
	}

	public int getPaymentStatus() {

		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {

		this.paymentStatus = paymentStatus;
	}

	private int paymentStatus;
	private int paymentMethod;
	private Date confirmDatetime;
	private byte[] confirmFileEntry;
	private String confirmNote;
	private Date approveDatetime;
	private String accountUserName;
	private String approveNote;
	private String govAgencyTaxNo;
	private String invoiceTemplateNo;
	private String invoiceIssueNo;
	private String invoiceNo;
	private int syncStatus;
	private ServiceContext serviceContext;

	private String extension;

	private String fileDescription;

	private String fileName;

	private String fileTitle;

	private String mimeType;

	private String oid;

	private String typeUpdate;

}
