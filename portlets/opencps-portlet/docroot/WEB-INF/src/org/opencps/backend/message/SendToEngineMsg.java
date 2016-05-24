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


/**
 * @author khoavd
 *
 */
public class SendToEngineMsg {
	
	
    /**
     * @return the dossierId
     */
    public long getDossierId() {
    
    	return dossierId;
    }
	
    /**
     * @param dossierId the dossierId to set
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
     * @param fileGroupId the fileGroupId to set
     */
    public void setFileGroupId(long fileGroupId) {
    
    	this.fileGroupId = fileGroupId;
    }
	
    /**
     * @return the event
     */
    public String getEvent() {
    
    	return event;
    }
	
    /**
     * @param event the event to set
     */
    public void setEvent(String event) {
    
    	this.event = event;
    }
	
    /**
     * @return the processOrderId
     */
    public long getProcessOrderId() {
    
    	return processOrderId;
    }
	
    /**
     * @param processOrderId the processOrderId to set
     */
    public void setProcessOrderId(long processOrderId) {
    
    	this.processOrderId = processOrderId;
    }
	
    /**
     * @return the processWorkflowId
     */
    public long getProcessWorkflowId() {
    
    	return processWorkflowId;
    }
	
    /**
     * @param processWorkflowId the processWorkflowId to set
     */
    public void setProcessWorkflowId(long processWorkflowId) {
    
    	this.processWorkflowId = processWorkflowId;
    }
	
    /**
     * @return the actionUserId
     */
    public long getActionUserId() {
    
    	return actionUserId;
    }
	
    /**
     * @param actionUserId the actionUserId to set
     */
    public void setActionUserId(long actionUserId) {
    
    	this.actionUserId = actionUserId;
    }
	
    /**
     * @return the actionNote
     */
    public String getActionNote() {
    
    	return actionNote;
    }
	
    /**
     * @param actionNote the actionNote to set
     */
    public void setActionNote(String actionNote) {
    
    	this.actionNote = actionNote;
    }
	
    /**
     * @return the assignToUserId
     */
    public long getAssignToUserId() {
    
    	return assignToUserId;
    }
	
    /**
     * @param assignToUserId the assignToUserId to set
     */
    public void setAssignToUserId(long assignToUserId) {
    
    	this.assignToUserId = assignToUserId;
    }
	
    /**
     * @return the receptionNo
     */
    public String getReceptionNo() {
    
    	return receptionNo;
    }
	
    /**
     * @param receptionNo the receptionNo to set
     */
    public void setReceptionNo(String receptionNo) {
    
    	this.receptionNo = receptionNo;
    }
	
    /**
     * @return the estimateDatetime
     */
    public Date getEstimateDatetime() {
    
    	return estimateDatetime;
    }
	
    /**
     * @param estimateDatetime the estimateDatetime to set
     */
    public void setEstimateDatetime(Date estimateDatetime) {
    
    	this.estimateDatetime = estimateDatetime;
    }
	
    /**
     * @return the paymentValue
     */
    public double getPaymentValue() {
    
    	return paymentValue;
    }
	
    /**
     * @param paymentValue the paymentValue to set
     */
    public void setPaymentValue(double paymentValue) {
    
    	this.paymentValue = paymentValue;
    }
	
    /**
     * @return the signature
     */
    public int getSignature() {
    
    	return signature;
    }
	
    /**
     * @param signature the signature to set
     */
    public void setSignature(int signature) {
    
    	this.signature = signature;
    }
	
    /**
     * @return the action
     */
    public String getAction() {
    
    	return action;
    }

	
    /**
     * @param action the action to set
     */
    public void setAction(String action) {
    
    	this.action = action;
    }
	protected long dossierId;
	protected long fileGroupId;
	protected String event;
	protected long processOrderId;
	protected long processWorkflowId;
	protected long actionUserId;
	protected String actionNote;
	protected long assignToUserId;
	protected String receptionNo;
	protected Date estimateDatetime;
	protected double paymentValue;
	protected int signature;
	protected String action;
}
