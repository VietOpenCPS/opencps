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

import com.liferay.portal.kernel.util.ParamUtil;


public class DossierPartSearchTerms extends DossierPartDisplayTerms{

	public DossierPartSearchTerms(PortletRequest portletRequest) {

	    super(portletRequest);
	    dossierpartId = ParamUtil.getLong(portletRequest, DOSSIERPART_DOSSIERPARTID);
	    parentId = ParamUtil.getLong(portletRequest, DOSSIERPART_PARENTID);
	    dossierTemplateId = ParamUtil.getLong(portletRequest, DOSSIERPART_DOSSIERTEMPLATEID);
	    
	    partNo = ParamUtil.getString(portletRequest, DOSSIERPART_PARTNO);
	    partName = ParamUtil.getString(portletRequest, DOSSIERPART_PARTNAME);
	    partTip = ParamUtil.getString(portletRequest, DOSSIERPART_PARTTIP);
	    treeIndex = ParamUtil.getString(portletRequest, DOSSIERPART_TREEINDEX);
	    formScript = ParamUtil.getString(portletRequest, DOSSIERPART_FORMSCRIPT);
	    sampleData = ParamUtil.getString(portletRequest, DOSSIERPART_SAMPLEDATA);
	    templateFileNo = ParamUtil.getString(portletRequest, DOSSIERPART_TEMPLATEFILENO);
	    formReport = ParamUtil.getString(portletRequest, DOSSIERPART_FORMREPORT);
	    
	    required = ParamUtil.getBoolean(portletRequest, DOSSIERPART_REQUIRED);
	    
	    partType = ParamUtil.getInteger(portletRequest, DOSSIERPART_PARTTYPE);
	    sibling = ParamUtil.getDouble(portletRequest, DOSSIERPART_SIBLING);
    }
	
    public long getDossierpartId() {
    
    	return dossierpartId;
    }
	
    public void setDossierpartId(long dossierpartId) {
    
    	this.dossierpartId = dossierpartId;
    }
	
    public long getDossierTemplateId() {
    
    	return dossierTemplateId;
    }
	
    public void setDossierTemplateId(long dossierTemplateId) {
    
    	this.dossierTemplateId = dossierTemplateId;
    }
	
    public long getParentId() {
    
    	return parentId;
    }
	
    public void setParentId(long parentId) {
    
    	this.parentId = parentId;
    }
	
    public String getPartNo() {
    
    	return partNo;
    }
	
    public void setPartNo(String partNo) {
    
    	this.partNo = partNo;
    }
	
    public String getPartName() {
    
    	return partName;
    }
	
    public void setPartName(String partName) {
    
    	this.partName = partName;
    }
	
    public String getPartTip() {
    
    	return partTip;
    }
	
    public void setPartTip(String partTip) {
    
    	this.partTip = partTip;
    }
	
    public String getTreeIndex() {
    
    	return treeIndex;
    }
	
    public void setTreeIndex(String treeIndex) {
    
    	this.treeIndex = treeIndex;
    }
	
    public String getFormScript() {
    
    	return formScript;
    }
	
    public void setFormScript(String formScript) {
    
    	this.formScript = formScript;
    }
	
    public String getSampleData() {
    
    	return sampleData;
    }
	
    public void setSampleData(String sampleData) {
    
    	this.sampleData = sampleData;
    }
	
    public String getTemplateFileNo() {
    
    	return templateFileNo;
    }
	
    public void setTemplateFileNo(String templateFileNo) {
    
    	this.templateFileNo = templateFileNo;
    }
	
    public boolean isRequired() {
    
    	return required;
    }
	
    public void setRequired(boolean required) {
    
    	this.required = required;
    }
	
    public int getPartType() {
    
    	return partType;
    }
	
    public void setPartType(int partType) {
    
    	this.partType = partType;
    }
	
    public double getSibling() {
    
    	return sibling;
    }
	
    public void setSibling(double sibling) {
    
    	this.sibling = sibling;
    }
	
    public String getFormReport() {
    
    	return formReport;
    }
	
    public void setFormReport(String formReport) {
    
    	this.formReport = formReport;
    }

	protected long dossierpartId;
	protected long dossierTemplateId;
	protected long parentId;
	
	protected String partNo;
	protected String partName;
	protected String partTip;
	protected String treeIndex;
	protected String formScript;
	protected String sampleData;
	protected String templateFileNo;
	protected String formReport;
	protected boolean required;
	
	protected int partType;  
	protected double sibling;
}
