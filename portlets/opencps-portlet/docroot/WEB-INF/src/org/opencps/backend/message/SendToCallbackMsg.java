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
	protected long processOrderId;
	protected String syncStatus;
}
