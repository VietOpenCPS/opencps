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


/**
 * @author khoavd
 *
 */
public class ProcessOrderDisplayTerms extends DisplayTerms{

	public static final String MA_TIEP_NHAN = "ma-tiep-nhan";
	public static final String CHU_HO_SO = "chu-ho-so";
	public static final String THU_TUC = "thu-tuc";
	public static final String BUOC_XU_LY = "buoc-xu-ly";
	public static final String NGUOI_PHU_TRACH = "nguoi-phu-trach";
	public static final String HAN_XU_LY = "han-xu-ly";
	
	public static final String PROCESSORDERID = "processOrderId";
	public static final String COMPANYID = "companyId";
	public static final String GROUPID = "groupId";
	public static final String USERID = "userId";
	public static final String CREATEDATE = "createDate";
	public static final String MODIFIEDDATE = "modifiedDate";
	public static final String SERVICEINFOID = "serviceInfoId";
	public static final String DOSSIERTEMPLATEID = "dossierTemplateId";
	public static final String GOVAGENCYORGANIZATIONID = "govAgencyOrganizationId";
	public static final String SERVICEPROCESSID = "serviceProcessId";
	public static final String DOSSIERID = "dossierId";
	public static final String FILEGROUPID = "fileGroupId";
	public static final String PROCESSSTEPID = "processStepId";
	public static final String ACTIONUSERID = "actionUserId";
	public static final String ACTIONDATETIME = "actionDatetime";
	public static final String PROCESSWORKFLOWID = "processWorkflowId";
	public static final String ASSIGNTOUSERID = "assignToUserId";
	public static final String DOSSIERSTATUS = "dossierStatus";
	public static final String ACTIONSTEPID = "actionStepId";
	public static final String GOVAGENCYCODE = "govAgencyCode";
	public static final String GOVAGENCYNAME = "govAgencyName";
	public static final String ACTIONNOTE = "actionNote";
	public static final String ERRORINFO = "errorInfo";

	/**
     * @param portletRequest
     */
    public ProcessOrderDisplayTerms(PortletRequest portletRequest) {

	    super(portletRequest);
	    // TODO Auto-generated constructor stub
	    processOrderId = ParamUtil.getLong(portletRequest, PROCESSORDERID);
	    companyId = ParamUtil.getLong(portletRequest, COMPANYID);
	    groupId = ParamUtil.getLong(portletRequest, GROUPID);
	    userId = ParamUtil.getLong(portletRequest, USERID);
	    createDate = ParamUtil.getDate(portletRequest, CREATEDATE, DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
	    modifiedDate = ParamUtil.getDate(portletRequest, MODIFIEDDATE , DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
	    serviceInfoId = ParamUtil.getLong(portletRequest, SERVICEINFOID);
	    dossierTemplateId = ParamUtil.getLong(portletRequest, DOSSIERTEMPLATEID);
	    govAgencyCode = ParamUtil.getString(portletRequest, GOVAGENCYCODE);
	    govAgencyName = ParamUtil.getString(portletRequest, GOVAGENCYNAME);
	    govAgencyOrganizationId = ParamUtil.getLong(portletRequest, GOVAGENCYORGANIZATIONID);
	    serviceProcessId = ParamUtil.getLong(portletRequest, SERVICEPROCESSID);
	    dossierId = ParamUtil.getLong(portletRequest, DOSSIERID);
	    fileGroupId = ParamUtil.getLong(portletRequest, FILEGROUPID);
	    processStepId = ParamUtil.getLong(portletRequest, PROCESSSTEPID);
	    actionUserId = ParamUtil.getLong(portletRequest, ACTIONUSERID);
	    actionDatetime = ParamUtil.getDate(portletRequest, ACTIONDATETIME, DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
	    actionNote = ParamUtil.getString(portletRequest, ACTIONNOTE);
	    assignToUserId = ParamUtil.getLong(portletRequest, ASSIGNTOUSERID);
	    processWorkflowId = ParamUtil.getLong(portletRequest, PROCESSWORKFLOWID);
	    dossierStatus = ParamUtil.getLong(portletRequest, DOSSIERSTATUS);
	    errorInfo = ParamUtil.getString(portletRequest, ERRORINFO);
	    actionStepId = ParamUtil.getLong(portletRequest, ACTIONSTEPID);
    }
	
    protected long processOrderId;
    protected long companyId;
    protected long groupId;
    protected long userId;
    protected Date createDate;
    protected Date modifiedDate;
    protected long serviceInfoId;
    protected long dossierTemplateId;
    protected String govAgencyCode;
    protected String govAgencyName;
    protected long govAgencyOrganizationId;
    protected long serviceProcessId;
    protected long dossierId;
    protected long fileGroupId;
    protected long processStepId;
    protected long actionUserId;
    protected Date actionDatetime;
    protected String actionNote;
    protected long assignToUserId;
    protected long processWorkflowId;
    protected long dossierStatus;
    protected String errorInfo;
    protected long actionStepId;
	
    public long getProcessOrderId() {
    
    	return processOrderId;
    }
	
    public void setProcessOrderId(long processOrderId) {
    
    	this.processOrderId = processOrderId;
    }
	
    public long getCompanyId() {
    
    	return companyId;
    }
	
    public void setCompanyId(long companyId) {
    
    	this.companyId = companyId;
    }
	
    public long getGroupId() {
    
    	return groupId;
    }
	
    public void setGroupId(long groupId) {
    
    	this.groupId = groupId;
    }
	
    public long getUserId() {
    
    	return userId;
    }
	
    public void setUserId(long userId) {
    
    	this.userId = userId;
    }
	
    public Date getCreateDate() {
    
    	return createDate;
    }
	
    public void setCreateDate(Date createDate) {
    
    	this.createDate = createDate;
    }
	
    public Date getModifiedDate() {
    
    	return modifiedDate;
    }
	
    public void setModifiedDate(Date modifiedDate) {
    
    	this.modifiedDate = modifiedDate;
    }
	
    public long getServiceInfoId() {
    
    	return serviceInfoId;
    }
	
    public void setServiceInfoId(long serviceInfoId) {
    
    	this.serviceInfoId = serviceInfoId;
    }
	
    public long getDossierTemplateId() {
    
    	return dossierTemplateId;
    }
	
    public void setDossierTemplateId(long dossierTemplateId) {
    
    	this.dossierTemplateId = dossierTemplateId;
    }
	
    public String getGovAgencyCode() {
    
    	return govAgencyCode;
    }
	
    public void setGovAgencyCode(String govAgencyCode) {
    
    	this.govAgencyCode = govAgencyCode;
    }
	
    public String getGovAgencyName() {
    
    	return govAgencyName;
    }
	
    public void setGovAgencyName(String govAgencyName) {
    
    	this.govAgencyName = govAgencyName;
    }
	
    public long getGovAgencyOrganizationId() {
    
    	return govAgencyOrganizationId;
    }
	
    public void setGovAgencyOrganizationId(long govAgencyOrganizationId) {
    
    	this.govAgencyOrganizationId = govAgencyOrganizationId;
    }
	
    public long getServiceProcessId() {
    
    	return serviceProcessId;
    }
	
    public void setServiceProcessId(long serviceProcessId) {
    
    	this.serviceProcessId = serviceProcessId;
    }
	
    public long getDossierId() {
    
    	return dossierId;
    }
	
    public void setDossierId(long dossierId) {
    
    	this.dossierId = dossierId;
    }
	
    public long getFileGroupId() {
    
    	return fileGroupId;
    }
	
    public void setFileGroupId(long fileGroupId) {
    
    	this.fileGroupId = fileGroupId;
    }
	
    public long getProcessStepId() {
    
    	return processStepId;
    }
	
    public void setProcessStepId(long processStepId) {
    
    	this.processStepId = processStepId;
    }
	
    public long getActionUserId() {
    
    	return actionUserId;
    }
	
    public void setActionUserId(long actionUserId) {
    
    	this.actionUserId = actionUserId;
    }
	
    public Date getActionDatetime() {
    
    	return actionDatetime;
    }
	
    public void setActionDatetime(Date actionDatetime) {
    
    	this.actionDatetime = actionDatetime;
    }
	
    public String getActionNote() {
    
    	return actionNote;
    }
	
    public void setActionNote(String actionNote) {
    
    	this.actionNote = actionNote;
    }
	
    public long getAssignToUserId() {
    
    	return assignToUserId;
    }
	
    public void setAssignToUserId(long assignToUserId) {
    
    	this.assignToUserId = assignToUserId;
    }
	
    public long getProcessWorkflowId() {
    
    	return processWorkflowId;
    }
	
    public void setProcessWorkflowId(long processWorkflowId) {
    
    	this.processWorkflowId = processWorkflowId;
    }
	
    public long getDossierStatus() {
    
    	return dossierStatus;
    }
	
    public void setDossierStatus(long dossierStatus) {
    
    	this.dossierStatus = dossierStatus;
    }
	
    public String getErrorInfo() {
    
    	return errorInfo;
    }
	
    public void setErrorInfo(String errorInfo) {
    
    	this.errorInfo = errorInfo;
    }
	
    public long getActionStepId() {
    
    	return actionStepId;
    }
	
    public void setActionStepId(long actionStepId) {
    
    	this.actionStepId = actionStepId;
    }
}
