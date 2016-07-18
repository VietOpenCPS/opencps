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

import java.util.Locale;


/**
 * @author khoavd
 *
 */
public class UserActionMsg {
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
     * @return the userId
     */
    public long getUserId() {
    
    	return userId;
    }

	
    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
    
    	this.userId = userId;
    }

	
    /**
     * @return the locale
     */
    public Locale getLocale() {
    
    	return locale;
    }

	
    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
    
    	this.locale = locale;
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
	protected String action;
	protected long dossierId;
	protected long fileGroupId;
	protected long userId;
	protected Locale locale;
	protected long processOrderId;
	
    /**
     * @return the groupId
     */
    public long getGroupId() {
    
    	return groupId;
    }

	
    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(long groupId) {
    
    	this.groupId = groupId;
    }
	protected long groupId;
	
    /**
     * @return the noReceptionNo
     */
    public String getNoReceptionNo() {
    
    	return noReceptionNo;
    }

	
    /**
     * @param noReceptionNo the noReceptionNo to set
     */
    public void setNoReceptionNo(String noReceptionNo) {
    
    	this.noReceptionNo = noReceptionNo;
    }
	protected String noReceptionNo;
}
