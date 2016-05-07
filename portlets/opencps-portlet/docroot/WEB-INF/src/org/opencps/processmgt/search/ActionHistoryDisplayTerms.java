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

package org.opencps.processmgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author binhth
 */
public class ActionHistoryDisplayTerms extends DisplayTerms {



	public static final String PROCESSORDERID = "processOrderId";
	public static final String PROCESSWORKFLOWID = "processWorkflowId";
	public static final String ACTIONDATETIME = "actionDatetime";
	public static final String STEPNAME = "stepName";
	public static final String ACTIONNAME = "actionName";
	public static final String ACTIONNOTE = "actionNote";
	public static final String ACTIONUSERID = "actionUserId";
	public static final String DAYSDOING = "daysDoing";
	public static final String DAYS_DELAY = "daysDelay";


	/**
	 * @param request
	 */
	public ActionHistoryDisplayTerms(PortletRequest portletRequest) {

		super(
		    portletRequest);
		processOrderId = ParamUtil
		    .getLong(portletRequest, PROCESSORDERID, 0L);
		processWorkflowId = ParamUtil
					    .getLong(portletRequest, PROCESSWORKFLOWID, 0L);
		actionDatetime = ParamUtil
					    .getDate(portletRequest, ACTIONDATETIME, DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_FORMAT));
		stepName = ParamUtil
					    .getString(portletRequest, STEPNAME, StringPool.BLANK);
		actionName = ParamUtil
					    .getString(portletRequest, ACTIONNAME, StringPool.BLANK);
		actionNote = ParamUtil
					    .getString(portletRequest, ACTIONNOTE, StringPool.BLANK);
		actionUserId = ParamUtil
					    .getLong(portletRequest, ACTIONUSERID, 0L);
		daysDoing = ParamUtil
					    .getLong(portletRequest, DAYSDOING, 0L);
		daysDelay = ParamUtil
					    .getLong(portletRequest, DAYS_DELAY, 0L);
	}

	protected long processOrderId;
	protected long processWorkflowId;
	protected Date actionDatetime;
	protected String stepName;
	protected String actionName;
	protected String actionNote;
	protected long actionUserId;
	protected long daysDoing;
	protected long daysDelay;

	
    public long getProcessOrderId() {
    
    	return processOrderId;
    }
	
    public void setProcessOrderId(long processOrderId) {
    
    	this.processOrderId = processOrderId;
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
	
    public long getDaysDoing() {
    
    	return daysDoing;
    }
	
    public void setDaysDoing(long daysDoing) {
    
    	this.daysDoing = daysDoing;
    }
	
    public long getDaysDelay() {
    
    	return daysDelay;
    }
	
    public void setDaysDelay(long daysDelay) {
    
    	this.daysDelay = daysDelay;
    }
}
