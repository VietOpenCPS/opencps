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

/**
 * @author trungnt
 */
public class ServiceBean {
	
	
	private long _companyId;

	private long _dossierTemplateId;

	private String _fullName;

	private String _govAgencyCode;

	private String _govAgencyIndex;

	private String _govAgencyName;

	private long _govAgencyOrganizationId;

	private long _groupId;

	private String _serviceAdministrationIndex;

	private boolean _serviceBackoffice;

	private boolean _serviceBusinees;

	private boolean _serviceCitizen;

	private long _serviceConfigId;

	private String _serviceDomainIndex;

	private long _serviceInfoId;

	private int _serviceLevel;

	private String _serviceName;

	private String _serviceNo;

	private boolean _serviceOnegate;

	private boolean _servicePortal;

	private long _serviceProcessId;

	
	private long _userId;

	
	public long getCompanyId() {
	
		return _companyId;
	}

	
	public long getDossierTemplateId() {
	
		return _dossierTemplateId;
	}

	
	public String getFullName() {
	
		return _fullName;
	}

	
	public String getGovAgencyCode() {
	
		return _govAgencyCode;
	}

	
	public String getGovAgencyIndex() {
	
		return _govAgencyIndex;
	}

	
	public String getGovAgencyName() {
	
		return _govAgencyName;
	}

	
	public long getGovAgencyOrganizationId() {
	
		return _govAgencyOrganizationId;
	}

	
	public long getGroupId() {
	
		return _groupId;
	}

	
	public String getServiceAdministrationIndex() {
	
		return _serviceAdministrationIndex;
	}

	
	public long getServiceConfigId() {
	
		return _serviceConfigId;
	}

	
	public String getServiceDomainIndex() {
	
		return _serviceDomainIndex;
	}

	
	public long getServiceInfoId() {
	
		return _serviceInfoId;
	}

	
	public int getServiceLevel() {
	
		return _serviceLevel;
	}

	
	public String getServiceName() {
	
		return _serviceName;
	}

	
	public String getServiceNo() {
	
		return _serviceNo;
	}

	
	public long getServiceProcessId() {
	
		return _serviceProcessId;
	}

	
	public long getUserId() {
	
		return _userId;
	}

	
	public boolean isServiceBackoffice() {
	
		return _serviceBackoffice;
	}

	
	public boolean isServiceBusinees() {
	
		return _serviceBusinees;
	}

	
	public boolean isServiceCitizen() {
	
		return _serviceCitizen;
	}

	
	public boolean isServiceOnegate() {
	
		return _serviceOnegate;
	}

	
	public boolean isServicePortal() {
	
		return _servicePortal;
	}

	
	public void setCompanyId(long companyId) {
	
		this._companyId = companyId;
	}

	
	public void setDossierTemplateId(long dossierTemplateId) {
	
		this._dossierTemplateId = dossierTemplateId;
	}

	
	public void setFullName(String fullName) {
	
		this._fullName = fullName;
	}

	
	public void setGovAgencyCode(String govAgencyCode) {
	
		this._govAgencyCode = govAgencyCode;
	}

	
	public void setGovAgencyIndex(String govAgencyIndex) {
	
		this._govAgencyIndex = govAgencyIndex;
	}

	
	public void setGovAgencyName(String govAgencyName) {
	
		this._govAgencyName = govAgencyName;
	}

	
	public void setGovAgencyOrganizationId(long govAgencyOrganizationId) {
	
		this._govAgencyOrganizationId = govAgencyOrganizationId;
	}

	
	public void setGroupId(long groupId) {
	
		this._groupId = groupId;
	}

	
	public void setServiceAdministrationIndex(String serviceAdministrationIndex) {
	
		this._serviceAdministrationIndex = serviceAdministrationIndex;
	}

	
	public void setServiceBackoffice(boolean serviceBackoffice) {
	
		this._serviceBackoffice = serviceBackoffice;
	}

	
	public void setServiceBusinees(boolean serviceBusinees) {
	
		this._serviceBusinees = serviceBusinees;
	}

	
	public void setServiceCitizen(boolean serviceCitizen) {
	
		this._serviceCitizen = serviceCitizen;
	}

	
	public void setServiceConfigId(long serviceConfigId) {
	
		this._serviceConfigId = serviceConfigId;
	}

	
	public void setServiceDomainIndex(String serviceDomainIndex) {
	
		this._serviceDomainIndex = serviceDomainIndex;
	}

	
	public void setServiceInfoId(long serviceInfoId) {
	
		this._serviceInfoId = serviceInfoId;
	}

	
	public void setServiceLevel(int serviceLevel) {
	
		this._serviceLevel = serviceLevel;
	}

	
	public void setServiceName(String serviceName) {
	
		this._serviceName = serviceName;
	}

	
	public void setServiceNo(String serviceNo) {
	
		this._serviceNo = serviceNo;
	}

	
	public void setServiceOnegate(boolean serviceOnegate) {
	
		this._serviceOnegate = serviceOnegate;
	}

	
	public void setServicePortal(boolean servicePortal) {
	
		this._servicePortal = servicePortal;
	}

	
	public void setServiceProcessId(long serviceProcessId) {
	
		this._serviceProcessId = serviceProcessId;
	}

	public void setUserId(long userId) {
	
		this._userId = userId;
	}

}
