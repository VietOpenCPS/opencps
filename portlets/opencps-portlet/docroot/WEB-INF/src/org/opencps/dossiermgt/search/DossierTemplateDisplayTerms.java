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

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;


public class DossierTemplateDisplayTerms extends DisplayTerms{
	
	public DossierTemplateDisplayTerms(PortletRequest portletRequest) {

	    super(portletRequest);
	    dossierTemplateId = ParamUtil.getLong(portletRequest, DOSSIERTEMPLATE_DOSSIERTEMPLATEID);
	    templateName = ParamUtil.getString(portletRequest, DOSSIERTEMPLATE_TEMPLATENAME);
	    description = ParamUtil.getString(portletRequest, DOSSIERTEMPLATE_DESCRIPTION);
	    templateNo = ParamUtil.getString(portletRequest, DOSSIERTEMPLATE_TEMPLATENO);
    }
	public static final String DOSSIERTEMPLATE_DOSSIERTEMPLATEID = "dossierTemplateId";
	public static final String DOSSIERTEMPLATE_TEMPLATENAME = "templateName";
	public static final String DOSSIERTEMPLATE_DESCRIPTION = "description";
	public static final String DOSSIERTEMPLATE_TEMPLATENO = "templateNo";
	
    public long getDossierTemplateId() {
    
    	return dossierTemplateId;
    }
	
    public void setDossierTemplateId(long dossierTemplateId) {
    
    	this.dossierTemplateId = dossierTemplateId;
    }
	
    public String getTemplateName() {
    
    	return templateName;
    }
	
    public void setTemplateName(String templateName) {
    
    	this.templateName = templateName;
    }
	
    public String getDescription() {
    
    	return description;
    }
	
    public void setDescription(String description) {
    
    	this.description = description;
    }
	
    public String getTemplateNo() {
    
    	return templateNo;
    }
	
    public void setTemplateNo(String templateNo) {
    
    	this.templateNo = templateNo;
    }
	protected long dossierTemplateId;
	protected String templateName;
	protected String description;
	protected String templateNo;
}
