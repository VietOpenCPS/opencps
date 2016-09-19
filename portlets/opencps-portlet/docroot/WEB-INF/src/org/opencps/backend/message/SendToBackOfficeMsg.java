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
public class SendToBackOfficeMsg{
	

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
     * @param userActorAction the userActorAction to set
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
     * @param actor the actor to set
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
     * @param actorId the actorId to set
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
     * @param actorName the actorName to set
     */
    public void setActorName(String actorName) {
    
    	this.actorName = actorName;
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

}
