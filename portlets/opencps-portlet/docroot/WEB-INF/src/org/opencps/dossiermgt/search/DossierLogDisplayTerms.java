/**
* OpenCPS is the open source Core Public Services software
* Copyright (C) 2016-present OpenCPS community

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
*/

package org.opencps.dossiermgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author trungnt
 */
public class DossierLogDisplayTerms extends DisplayTerms {

	public static final String DOSSIERID = "dossierId";
	public static final String FILEGROUPID = "fileGroupId";
	public static final String DOSSIERSTATUS = "dossierStatus";
	public static final String ACTIONINFO = "actionInfo";
	public static final String MESSAGEINFO = "messageInfo";
	public static final String UPDATEDATETIME = "updateDatetime";
	public static final String LEVEL = "level";

	public DossierLogDisplayTerms(PortletRequest portletRequest) {
		super(
		    portletRequest);
		dossierId = ParamUtil.getLong(portletRequest, DOSSIERID, 0L);
		fileGroupId = ParamUtil.getInteger(portletRequest, FILEGROUPID, 0);
		dossierStatus = ParamUtil.getInteger(portletRequest, DOSSIERSTATUS, 0);
		actionInfo = ParamUtil.getString(portletRequest, ACTIONINFO);
		messageInfo = ParamUtil.getString(portletRequest, MESSAGEINFO);
		updateDatetime = ParamUtil.getDate(portletRequest, UPDATEDATETIME, DateTimeUtil
		        .getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		level = ParamUtil.getInteger(portletRequest, LEVEL, 0);
	}

	protected long dossierId;
	protected int fileGroupId;
	protected int dossierStatus;
	protected String actionInfo;
	protected String messageInfo;
	protected Date updateDatetime;
	protected int level;
	
    public long getDossierId() {
    
    	return dossierId;
    }
	
    public void setDossierId(long dossierId) {
    
    	this.dossierId = dossierId;
    }
	
    public int getFileGroupId() {
    
    	return fileGroupId;
    }
	
    public void setFileGroupId(int fileGroupId) {
    
    	this.fileGroupId = fileGroupId;
    }
	
    public int getDossierStatus() {
    
    	return dossierStatus;
    }
	
    public void setDossierStatus(int dossierStatus) {
    
    	this.dossierStatus = dossierStatus;
    }
	
    public String getActionInfo() {
    
    	return actionInfo;
    }
	
    public void setActionInfo(String actionInfo) {
    
    	this.actionInfo = actionInfo;
    }
	
    public String getMessageInfo() {
    
    	return messageInfo;
    }
	
    public void setMessageInfo(String messageInfo) {
    
    	this.messageInfo = messageInfo;
    }
	
    public Date getUpdateDatetime() {
    
    	return updateDatetime;
    }
	
    public void setUpdateDatetime(Date updateDatetime) {
    
    	this.updateDatetime = updateDatetime;
    }
	
    public int getLevel() {
    
    	return level;
    }
	
    public void setLevel(int level) {
    
    	this.level = level;
    }
}
