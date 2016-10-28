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

package org.opencps.dossiermgt.bean;

import java.util.Date;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author trungnt
 */
public class ProcessOrderBean {

	public long getCompanyId() {

		return _companyId;
	}

	public void setCompanyId(long companyId) {

		this._companyId = companyId;
	}

	public long getGroupId() {

		return _groupId;
	}

	public void setGroupId(long groupId) {

		this._groupId = groupId;
	}

	public long getUserId() {

		return _userId;
	}

	public void setUserId(long userId) {

		this._userId = userId;
	}

	public long getServiceInfoId() {

		return _serviceInfoId;
	}

	public void setServiceInfoId(long serviceInfoId) {

		this._serviceInfoId = serviceInfoId;
	}

	public long getDossierTemplateId() {

		return _dossierTemplateId;
	}

	public void setDossierTemplateId(long dossierTemplateId) {

		this._dossierTemplateId = dossierTemplateId;
	}

	public String getGovAgencyCode() {

		return _govAgencyCode;
	}

	public void setGovAgencyCode(String govAgencyCode) {

		this._govAgencyCode = govAgencyCode;
	}

	public String getGovAgencyName() {

		return _govAgencyName;
	}

	public void setGovAgencyName(String govAgencyName) {

		this._govAgencyName = govAgencyName;
	}

	public long getGovAgencyOrganizationId() {

		return _govAgencyOrganizationId;
	}

	public void setGovAgencyOrganizationId(long govAgencyOrganizationId) {

		this._govAgencyOrganizationId = govAgencyOrganizationId;
	}

	public long getServiceProcessId() {

		return _serviceProcessId;
	}

	public void setServiceProcessId(long serviceProcessId) {

		this._serviceProcessId = serviceProcessId;
	}

	public long getDossierId() {

		return _dossierId;
	}

	public void setDossierId(long dossierId) {

		this._dossierId = dossierId;
	}

	public long getFileGroupId() {

		return _fileGroupId;
	}

	public void setFileGroupId(long fileGroupId) {

		this._fileGroupId = fileGroupId;
	}

	public long getProcessStepId() {

		return _processStepId;
	}

	public void setProcessStepId(long processStepId) {

		this._processStepId = processStepId;
	}

	public long getActionUserId() {

		return _actionUserId;
	}
	
	public String getActionUserName() {

		User user = null;
		try {
			user = UserLocalServiceUtil
			    .fetchUserById(this
			        .getActionUserId());

		}
		catch (Exception e) {
			_log
			    .error(e
			        .getMessage());
		}

		this._actionUserName = user != null ? user
		    .getFullName() : StringPool.BLANK;

		setActionUserName(_actionUserName);

		return _actionUserName;
	}
	
	public String getActionUserName(long actionUserId){
		User user = null;
		try {
			user = UserLocalServiceUtil
			    .fetchUserById(actionUserId);

		}
		catch (Exception e) {
			_log
			    .error(e
			        .getMessage());
		}

		this._actionUserName = user != null ? user
		    .getFullName() : StringPool.BLANK;

		setActionUserName(_actionUserName);

		return _actionUserName;
	}

	public void setActionUserId(long actionUserId) {

		this._actionUserId = actionUserId;
	}

	public Date getActionDatetime() {

		return _actionDatetime;
	}

	public void setActionDatetime(Date actionDatetime) {

		this._actionDatetime = actionDatetime;
	}

	public long getAssignToUsesrId() {

		return _assignToUserId;
	}

	public void setAssignToUserId(long assignToUserId) {

		this._assignToUserId = assignToUserId;
	}

	public String getDossierStatus() {

		return _dossierStatus;
	}

	public void setDossierStatus(String dossierStatus) {

		this._dossierStatus = dossierStatus;
	}

	public long getServiceConfigId() {

		return _serviceConfigId;
	}

	public void setServiceConfigId(long serviceConfigId) {

		this._serviceConfigId = serviceConfigId;
	}

	public String getSubjectId() {

		return _subjectId;
	}

	public void setSubjectId(String subjectId) {

		this._subjectId = subjectId;
	}

	public String getSubjectName() {

		return _subjectName;
	}

	public void setSubjectName(String subjectName) {

		this._subjectName = subjectName;
	}

	public String getServiceName() {

		return _serviceName;
	}

	public void setServiceName(String serviceName) {

		this._serviceName = serviceName;
	}

	public String getStepName() {

		return _stepName;
	}

	public void setStepName(String stepName) {

		this._stepName = stepName;
	}

	public String getSequenceNo() {

		return _sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {

		this._sequenceNo = sequenceNo;
	}

	public int getDaysDuration() {

		return _daysDuration;
	}

	public void setDaysDuration(int daysDuration) {

		this._daysDuration = daysDuration;
	}

	public long getReferenceDossierPartId() {

		return _referenceDossierPartId;
	}

	public void setReferenceDossierPartId(long referenceDossierPartId) {

		this._referenceDossierPartId = referenceDossierPartId;
	}

	public String getAssignToUserName() {

		User user = null;
		try {
			user = UserLocalServiceUtil
			    .fetchUserById(this
			        .getAssignToUsesrId());

		}
		catch (Exception e) {
			_log
			    .error(e
			        .getMessage());
		}

		this._assignToUserName = user != null ? user
		    .getFullName() : StringPool.BLANK;

		setAssignToUserName(_assignToUserName);

		return _assignToUserName;
	}

	public String getAssignToUserName(long assignToUserId) {

		User user = null;
		try {
			user = UserLocalServiceUtil
			    .fetchUserById(assignToUserId);

		}
		catch (Exception e) {
			_log
			    .error(e
			        .getMessage());
		}

		this._assignToUserName = user != null ? user
		    .getFullName() : StringPool.BLANK;

		setAssignToUserName(_assignToUserName);

		return _assignToUserName;
	}

	public void setAssignToUserName(String assignToUserName) {

		this._assignToUserName = assignToUserName;
	}

	public String getDealine() {

		return _dealine;
	}

	public void setDealine(String dealine) {

		this._dealine = dealine;
	}
	
	public String getReceptionNo() {
	
		return _receptionNo;
	}

	public void setReceptionNo(String receptionNo) {
	
		this._receptionNo = receptionNo;
	}
	
	public long getProcessOrderId() {
	
		return _processOrderId;
	}
	
	public void setProcessOrderId(long processOrderId) {
	
		this._processOrderId = processOrderId;
	}
	
	public void setActionUserName(String _actionUserName) {
		this._actionUserName = _actionUserName;
	}
	
	public boolean isReadOnly() {
	
		return _readOnly;
	}

	public void setReadOnly(boolean readOnly) {
	
		this._readOnly = readOnly;
	}

	private long _processOrderId;
	private long _companyId;
	private long _groupId;
	private long _userId;
	private long _serviceInfoId;
	private long _dossierTemplateId;
	private String _govAgencyCode;
	private String _govAgencyName;
	private long _govAgencyOrganizationId;
	private long _serviceProcessId;
	private long _dossierId;
	private long _fileGroupId;
	private long _processStepId;
	private long _actionUserId;
	private Date _actionDatetime;
	private long _assignToUserId;
	private String _dossierStatus;
	
	private long _serviceConfigId;
	private String _subjectId;
	private String _subjectName;
	private String _receptionNo;
	
	private String _serviceName;

	private String _stepName;
	private String _sequenceNo;
	private int _daysDuration;
	private long _referenceDossierPartId;

	private String _assignToUserName;
	private String _actionUserName;
	private String _dealine;

	private String _actionNote;
	
	public String getActionNote() {
		return _actionNote;
	}

	public void setActionNote(String _actionNote) {
		this._actionNote = _actionNote;
	}

	private boolean _readOnly;

	private Log _log = LogFactoryUtil
	    .getLog(ProcessOrderBean.class
	        .getName());
}
