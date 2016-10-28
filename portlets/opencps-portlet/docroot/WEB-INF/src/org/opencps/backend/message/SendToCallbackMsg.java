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
public class SendToCallbackMsg {
	
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
     * @return the syncStatus
     */
    public String getSyncStatus() {
    
    	return syncStatus;
    }
	
    /**
     * @param syncStatus the syncStatus to set
     */
    public void setSyncStatus(String syncStatus) {
    
    	this.syncStatus = syncStatus;
    }
	
    /**
     * @return the dossierStatus
     */
    public String getDossierStatus() {
    
    	return dossierStatus;
    }

	
    /**
     * @param dossierStatus the dossierStatus to set
     */
    public void setDossierStatus(String dossierStatus) {
    
    	this.dossierStatus = dossierStatus;
    }
	
    public long getUserId() {
    
    	return userId;
    }

	
    public void setUserId(long userId) {
    
    	this.userId = userId;
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

	
    public long getProcessWorkflowId() {
    
    	return processWorkflowId;
    }

	
    public void setProcessWorkflowId(long processWorkflowId) {
    
    	this.processWorkflowId = processWorkflowId;
    }

	
    public Date getActionDatetime() {
    
    	return actionDatetime;
    }

	
    public void setActionDatetime(Date actionDatetime) {
    
    	this.actionDatetime = actionDatetime;
    }

	
    public String getStepName() {
    
    	return stepName;
    }

	
    public void setStepName(String stepName) {
    
    	this.stepName = stepName;
    }

	
    public String getActionName() {
    
    	return actionName;
    }

	
    public void setActionName(String actionName) {
    
    	this.actionName = actionName;
    }

	
    public String getActionNote() {
    
    	return actionNote;
    }

	
    public void setActionNote(String actionNote) {
    
    	this.actionNote = actionNote;
    }

	
    public long getActionUserId() {
    
    	return actionUserId;
    }

	
    public void setActionUserId(long actionUserId) {
    
    	this.actionUserId = actionUserId;
    }

	
    public int getDaysDoing() {
    
    	return daysDoing;
    }

	
    public void setDaysDoing(int daysDoing) {
    
    	this.daysDoing = daysDoing;
    }

	
    public int getDaysDelay() {
    
    	return daysDelay;
    }

	
    public void setDaysDelay(int daysDelay) {
    
    	this.daysDelay = daysDelay;
    }
	protected long processOrderId;
	protected String syncStatus;
	protected String dossierStatus;
	
	protected long userId;
	protected long groupId;
	protected long companyId;
	protected long processWorkflowId;
	protected Date actionDatetime;
	protected String stepName;
	protected String actionName;
	protected String actionNote;
	protected long actionUserId;
	protected int daysDoing;
	protected int daysDelay;

}
