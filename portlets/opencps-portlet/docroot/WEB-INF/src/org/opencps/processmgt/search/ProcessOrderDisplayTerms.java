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
 * @author trungnt
 */
public class ProcessOrderDisplayTerms extends DisplayTerms {

	public static String PROCESS_WORKFLOW_ID = "processWorkflowId";
	public static String PROCESS_ORDER_ID = "processOrderId";
	public static String ASSIGN_TO_USER_ID = "assignToUserId";
	public static String SERVICE_INFO_ID = "serviceInfoId";
	public static String MODIFIEDDATE = "modifiedDate";
	public static String DOSSIER_TEMPLATE_ID = "dossierTemplateId";
	public static String GOV_AGENCY_ORGANIZATION_ID = "govAgencyOrganizationId";
	public static String SERVICE_PROCESS_ID = "serviceProcessId";
	public static String DOSSIER_ID = "dossierId";
	public static String FILE_GROUP_ID = "fileGroupId";
	public static String PROCESS_STEP_ID = "processStepId";
	public static String ACTION_USER_ID = "actionUserId";
	public static String EVENT = "event";
	public static String ACTION_DATE_TIME = "actionDatetime";
	public static String ACTION_NOTE = "actionNote";
	public static String GOV_AGENCY_CODE = "govAgencyCode";
	public static String GOV_AGENCY_NAME = "govAgencyName";
	public static String DOSSIER_STATUS = "dossierStatus";
	public static String RECEPTION_NO = "receptionNo";
	public static String SERVICE_NAME = "serviceName";
	public static String GOVAGENCY_NAME = "govagencyName";
	public static String SUBJECT_NAME = "subjectName";
	public static String PAYMENTVALUE = "paymentValue";
	public static String USER_ID = "userId";
	public static String GROUP_ID = "groupId";
	public static String COMPANY_ID = "companyId";
	public static String ESTIMATE_DATETIME_DAY = "estimateDatetimeDay";
	public static String ESTIMATE_DATETIME_MONTH = "estimateDatetimeMonth";
	public static String ESTIMATE_DATETIME_YEAR = "estimateDatetimeYear";
	public static String ESTIMATE_DATE = "estimateDate";
	public static String ESTIMATE_TIME = "estimateTime";
	public static String SIGNATURE = "signature";
	public static String REQUIRED = "required";
	
	
	/**
	 * @param request
	 */
	public ProcessOrderDisplayTerms(PortletRequest portletRequest) {

		super(
		    portletRequest);
		assignToUserId = ParamUtil
		    .getLong(portletRequest, ASSIGN_TO_USER_ID, 0L);
		serviceInfoId = ParamUtil
		    .getLong(portletRequest, SERVICE_INFO_ID, 0L);
		dossierTemplateId = ParamUtil
		    .getLong(portletRequest, DOSSIER_TEMPLATE_ID, 0L);
		govAgencyOrganizationId = ParamUtil
		    .getLong(portletRequest, GOV_AGENCY_ORGANIZATION_ID, 0L);
		serviceProcessId = ParamUtil
		    .getLong(portletRequest, SERVICE_PROCESS_ID, 0L);
		dossierId = ParamUtil
		    .getLong(portletRequest, DOSSIER_ID, 0L);
		fileGroupId = ParamUtil
		    .getLong(portletRequest, FILE_GROUP_ID, 0L);
		processStepId = ParamUtil
		    .getLong(portletRequest, PROCESS_STEP_ID, 0L);
		actionUserId = ParamUtil
		    .getLong(portletRequest, ACTION_USER_ID, 0L);
		actionDatetime = ParamUtil
		    .getDate(portletRequest, ACTION_DATE_TIME, DateTimeUtil
		        .getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		actionNote = ParamUtil
		    .getString(portletRequest, ACTION_NOTE);
		govAgencyCode = ParamUtil
		    .getString(portletRequest, GOV_AGENCY_CODE);
		govAgencyName = ParamUtil
		    .getString(portletRequest, GOV_AGENCY_NAME);
		dossierStatus = ParamUtil
		    .getString(portletRequest, DOSSIER_STATUS);
		receptionNo = ParamUtil
		    .getString(portletRequest, RECEPTION_NO);
		serviceName = ParamUtil
		    .getString(portletRequest, SERVICE_NAME);
		govagencyName = ParamUtil
		    .getString(portletRequest, GOV_AGENCY_NAME);
		subjectName = ParamUtil
		    .getString(portletRequest, SUBJECT_NAME);
	}

	public long getAssignToUserId() {

		return assignToUserId;
	}

	public void setAssignToUserId(long assignToUserId) {

		this.assignToUserId = assignToUserId;
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

	public String getDossierStatus() {

		return dossierStatus;
	}

	public void setDossierStatus(String dossierStatus) {

		this.dossierStatus = dossierStatus;
	}

	public String getReceptionNo() {

		return receptionNo;
	}

	public void setReceptionNo(String receptionNo) {

		this.receptionNo = receptionNo;
	}

	public String getServiceName() {

		return serviceName;
	}

	public void setServiceName(String serviceName) {

		this.serviceName = serviceName;
	}

	public String getGovagencyName() {

		return govagencyName;
	}

	public void setGovagencyName(String govagencyName) {

		this.govagencyName = govagencyName;
	}

	public String getSubjectName() {

		return subjectName;
	}

	public void setSubjectName(String subjectName) {

		this.subjectName = subjectName;
	}
	
	public long getUserId() {
	
		return userId;
	}
	
	public void setUserId(long userId) {
	
		this.userId = userId;
	}

	protected long assignToUserId;
	protected long serviceInfoId;
	protected long dossierTemplateId;
	protected long govAgencyOrganizationId;
	protected long serviceProcessId;
	protected long dossierId;
	protected long fileGroupId;
	protected long processStepId;
	protected long actionUserId;
	protected long userId;
	protected String dossierStatus;
	protected Date actionDatetime;
	protected String actionNote;
	protected String govAgencyCode;
	protected String govAgencyName;
	protected String receptionNo;
	protected String serviceName;
	protected String govagencyName;
	protected String subjectName;
}
