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

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;


public class DossierFileSearchTerms extends DossierFileDisplayTerms {
	public DossierFileSearchTerms(PortletRequest portletRequest) {
		super(
			portletRequest);

		createDate = ParamUtil
			.getDate(portletRequest, DOSSIER_FILE_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
				
		displayName = ParamUtil
			.getString(portletRequest, FILE_NAME);
								
	}
	
	protected String receptionNo;	
	protected String displayName;
	protected Date dossierFileDate;
	protected String dossierFileNo;
	protected Date createDate;
	protected int dossierFileType;
	
    public String getReceptionNo() {
    
    	return receptionNo;
    }
	
    public void setReceptionNo(String receptionNo) {
    
    	this.receptionNo = receptionNo;
    }
	
    public String getDisplayName() {
    
    	return displayName;
    }
	
    public void setDisplayName(String displayName) {
    
    	this.displayName = displayName;
    }
	
    public Date getDossierFileDate() {
    
    	return dossierFileDate;
    }
	
    public void setDossierFileDate(Date dossierFileDate) {
    
    	this.dossierFileDate = dossierFileDate;
    }
	
    public String getDossierFileNo() {
    
    	return dossierFileNo;
    }
	
    public void setDossierFileNo(String dossierFileNo) {
    
    	this.dossierFileNo = dossierFileNo;
    }
	
    public Date getCreateDate() {
    
    	return createDate;
    }
	
    public void setCreateDate(Date createDate) {
    
    	this.createDate = createDate;
    }
	
    public int getDossierFileType() {
    
    	return dossierFileType;
    }
	
    public void setDossierFileType(int dossierFileType) {
    
    	this.dossierFileType = dossierFileType;
    }
}
