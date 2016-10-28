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

package org.opencps.backend.message;

import java.util.Date;

import org.opencps.paymentmgt.model.PaymentFile;

/**
 * @author khoavd
 */
public class SendToBackOfficeMsg {

	/**
	 * @return the processOrderId
	 */
	public long getProcessOrderId() {

		return processOrderId;
	}

	/**
	 * @param processOrderId
	 *            the processOrderId to set
	 */
	public void setProcessOrderId(long processOrderId) {

		this.processOrderId = processOrderId;
	}

	/**
	 * @return the dossierId
	 */
	public long getDossierId() {

		return dossierId;
	}

	/**
	 * @param dossierId
	 *            the dossierId to set
	 */
	public void setDossierId(long dossierId) {

		this.dossierId = dossierId;
	}

	/**
	 * @return the fileGroupId
	 */
	public long getFileGroupId() {

		return fileGroupId;
	}

	/**
	 * @param fileGroupId
	 *            the fileGroupId to set
	 */
	public void setFileGroupId(long fileGroupId) {

		this.fileGroupId = fileGroupId;
	}

	/**
	 * @return the dossierStatus
	 */
	public String getDossierStatus() {

		return dossierStatus;
	}

	/**
	 * @param dossierStatus
	 *            the dossierStatus to set
	 */
	public void setDossierStatus(String dossierStatus) {

		this.dossierStatus = dossierStatus;
	}

	/**
	 * @return the actionInfo
	 */
	public String getActionInfo() {

		return actionInfo;
	}

	/**
	 * @param actionInfo
	 *            the actionInfo to set
	 */
	public void setActionInfo(String actionInfo) {

		this.actionInfo = actionInfo;
	}

	/**
	 * @return the messageInfo
	 */
	public String getMessageInfo() {

		return messageInfo;
	}

	/**
	 * @param messageInfo
	 *            the messageInfo to set
	 */
	public void setMessageInfo(String messageInfo) {

		this.messageInfo = messageInfo;
	}

	/**
	 * @return the sendResult
	 */
	public int getSendResult() {

		return sendResult;
	}

	/**
	 * @param sendResult
	 *            the sendResult to set
	 */
	public void setSendResult(int sendResult) {

		this.sendResult = sendResult;
	}

	/**
	 * @return the requestPayment
	 */
	public int getRequestPayment() {

		return requestPayment;
	}

	/**
	 * @param requestPayment
	 *            the requestPayment to set
	 */
	public void setRequestPayment(int requestPayment) {

		this.requestPayment = requestPayment;
	}

	/**
	 * @return the updateDatetime
	 */
	public Date getUpdateDatetime() {

		return updateDatetime;
	}

	/**
	 * @param updateDatetime
	 *            the updateDatetime to set
	 */
	public void setUpdateDatetime(Date updateDatetime) {

		this.updateDatetime = updateDatetime;
	}

	/**
	 * @return the receptionNo
	 */
	public String getReceptionNo() {

		return receptionNo;
	}

	/**
	 * @param receptionNo
	 *            the receptionNo to set
	 */
	public void setReceptionNo(String receptionNo) {

		this.receptionNo = receptionNo;
	}

	/**
	 * @return the receiveDatetime
	 */
	public Date getReceiveDatetime() {

		return receiveDatetime;
	}

	/**
	 * @param receiveDatetime
	 *            the receiveDatetime to set
	 */
	public void setReceiveDatetime(Date receiveDatetime) {

		this.receiveDatetime = receiveDatetime;
	}

	/**
	 * @return the estimateDatetime
	 */
	public Date getEstimateDatetime() {

		return estimateDatetime;
	}

	/**
	 * @param estimateDatetime
	 *            the estimateDatetime to set
	 */
	public void setEstimateDatetime(Date estimateDatetime) {

		this.estimateDatetime = estimateDatetime;
	}

	/**
	 * @return the finishDatetime
	 */
	public Date getFinishDatetime() {

		return finishDatetime;
	}

	/**
	 * @param finishDatetime
	 *            the finishDatetime to set
	 */
	public void setFinishDatetime(Date finishDatetime) {

		this.finishDatetime = finishDatetime;
	}

	/**
	 * @return the processWorkflowId
	 */
	public long getProcessWorkflowId() {

		return processWorkflowId;
	}

	/**
	 * @param processWorkflowId
	 *            the processWorkflowId to set
	 */
	public void setProcessWorkflowId(long processWorkflowId) {

		this.processWorkflowId = processWorkflowId;
	}

	/**
	 * @return the paymentFile
	 */
	public PaymentFile getPaymentFile() {

		return paymentFile;
	}

	/**
	 * @param paymentFile
	 *            the paymentFile to set
	 */
	public void setPaymentFile(PaymentFile paymentFile) {

		this.paymentFile = paymentFile;
	}

	/**
	 * @return the companyId
	 */
	public long getCompanyId() {

		return companyId;
	}

	/**
	 * @param companyId
	 *            the companyId to set
	 */
	public void setCompanyId(long companyId) {

		this.companyId = companyId;
	}

	/**
	 * @return the govAgencyCode
	 */
	public String getGovAgencyCode() {

		return govAgencyCode;
	}

	/**
	 * @param govAgencyCode
	 *            the govAgencyCode to set
	 */
	public void setGovAgencyCode(String govAgencyCode) {

		this.govAgencyCode = govAgencyCode;
	}

	/**
	 * @return the submitDateTime
	 */
	public Date getSubmitDateTime() {

		return submitDateTime;
	}

	/**
	 * @param submitDateTime
	 *            the submitDateTime to set
	 */
	public void setSubmitDateTime(Date submitDateTime) {

		this.submitDateTime = submitDateTime;
	}

	/**
	 * @return the requestCommand
	 */
	public String getRequestCommand() {

		return requestCommand;
	}

	/**
	 * @param requestCommand
	 *            the requestCommand to set
	 */
	public void setRequestCommand(String requestCommand) {

		this.requestCommand = requestCommand;
	}

	/**
	 * @return the userActorAction
	 */
	public long getUserActorAction() {

		return userActorAction;
	}

	/**
	 * @param userActorAction
	 *            the userActorAction to set
	 */
	public void setUserActorAction(long userActorAction) {

		this.userActorAction = userActorAction;
	}

	/**
	 * @return the actor
	 */
	public int getActor() {

		return actor;
	}

	/**
	 * @param actor
	 *            the actor to set
	 */
	public void setActor(int actor) {

		this.actor = actor;
	}

	/**
	 * @return the actorId
	 */
	public long getActorId() {

		return actorId;
	}

	/**
	 * @param actorId
	 *            the actorId to set
	 */
	public void setActorId(long actorId) {

		this.actorId = actorId;
	}

	/**
	 * @return the actorName
	 */
	public String getActorName() {

		return actorName;
	}

	/**
	 * @param actorName
	 *            the actorName to set
	 */
	public void setActorName(String actorName) {

		this.actorName = actorName;
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

	public String getDossierLogOId() {

		return dossierLogOId;
	}

	/**
	 * @return the isPayment
	 */
	public boolean isPayment() {

		return isPayment;
	}

	/**
	 * @param isPayment
	 *            the isPayment to set
	 */
	public void setPayment(boolean isPayment) {

		this.isPayment = isPayment;
	}

	/**
	 * @return the isResubmit
	 */
	public boolean isResubmit() {

		return isResubmit;
	}

	/**
	 * @param isResubmit
	 *            the isResubmit to set
	 */
	public void setResubmit(boolean isResubmit) {

		this.isResubmit = isResubmit;
	}

	public void setDossierLogOId(String dossierLogOId) {

		this.dossierLogOId = dossierLogOId;
	}

	public String getActionHistoryOId() {

		return actionHistoryOId;
	}

	public void setActionHistoryOId(String actionHistoryOId) {

		this.actionHistoryOId = actionHistoryOId;
	}

	
    /**
     * @return the isCreateProcessOrder
     */
    public boolean isCreateProcessOrder() {
    
    	return isCreateProcessOrder;
    }

	
    /**
     * @param isCreateProcessOrder the isCreateProcessOrder to set
     */
    public void setCreateProcessOrder(boolean isCreateProcessOrder) {
    
    	this.isCreateProcessOrder = isCreateProcessOrder;
    }

	
    /**
     * @return the isCreateReceptionNo
     */
    public boolean isCreateReceptionNo() {
    
    	return isCreateReceptionNo;
    }

	
    /**
     * @param isCreateReceptionNo the isCreateReceptionNo to set
     */
    public void setCreateReceptionNo(boolean isCreateReceptionNo) {
    
    	this.isCreateReceptionNo = isCreateReceptionNo;
    }

	
    /**
     * @return the paymentDate
     */
    public Date getPaymentDate() {
    
    	return paymentDate;
    }

	
    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(Date paymentDate) {
    
    	this.paymentDate = paymentDate;
    }

	
    /**
     * @return the resubmitDate
     */
    public Date getResubmitDate() {
    
    	return resubmitDate;
    }

	
    /**
     * @param resubmitDate the resubmitDate to set
     */
    public void setResubmitDate(Date resubmitDate) {
    
    	this.resubmitDate = resubmitDate;
    }

	
    /**
     * @return the createProcessOrderDate
     */
    public Date getCreateProcessOrderDate() {
    
    	return createProcessOrderDate;
    }

	
    /**
     * @param createProcessOrderDate the createProcessOrderDate to set
     */
    public void setCreateProcessOrderDate(Date createProcessOrderDate) {
    
    	this.createProcessOrderDate = createProcessOrderDate;
    }

	
    /**
     * @return the createRecptionDate
     */
    public Date getCreateRecptionDate() {
    
    	return createRecptionDate;
    }

	
    /**
     * @param createRecptionDate the createRecptionDate to set
     */
    public void setCreateRecptionDate(Date createRecptionDate) {
    
    	this.createRecptionDate = createRecptionDate;
    }

	
    public String getStepName() {
    
    	return stepName;
    }

	
    public void setStepName(String stepName) {
    
    	this.stepName = stepName;
    }

	protected String requestCommand;
	protected long processOrderId;
	protected long dossierId;
	protected long fileGroupId;
	protected String dossierStatus;
	protected String actionInfo;
	protected String messageInfo;
	protected int sendResult;
	protected int requestPayment;
	protected Date updateDatetime;
	protected String receptionNo;
	protected Date receiveDatetime;
	protected Date estimateDatetime;
	protected Date finishDatetime;
	protected long processWorkflowId;
	protected long companyId;
	protected String govAgencyCode;
	protected Date submitDateTime;
	protected PaymentFile paymentFile;
	protected long userActorAction;
	protected int actor;
	protected long actorId;
	protected String actorName;
	protected int syncStatus;
	protected String dossierLogOId;
	protected String actionHistoryOId;
	protected boolean isPayment;
	protected boolean isResubmit;
	protected boolean isCreateProcessOrder;
	protected boolean isCreateReceptionNo;
	
	protected Date paymentDate;
	protected Date resubmitDate;
	protected Date createProcessOrderDate;
	protected Date createRecptionDate;
	
	protected String stepName;
	

}
