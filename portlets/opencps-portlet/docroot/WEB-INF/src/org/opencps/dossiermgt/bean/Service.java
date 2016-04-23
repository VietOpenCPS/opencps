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
public class Service {
	
	
	public Service() {
		
	}

	public long getCompanyId() {

		return _companyId;
	}

	public void setCompanyId(long companyId) {

		_companyId = companyId;
	}

	public long getGroupId() {

		return _groupId;
	}

	public void setGroupId(long groupId) {

		_groupId = groupId;
	}

	public long getUserId() {

		return _userId;
	}

	public void setUserId(long userId) {

		_userId = userId;
	}

	public long getServiceConfigId() {

		return _serviceConfigId;
	}

	public void setServiceConfigId(long serviceConfigId) {

		_serviceConfigId = serviceConfigId;
	}

	public long getServiceInfoId() {

		return _serviceInfoId;
	}

	public void setServiceInfoId(long serviceInfoId) {

		_serviceInfoId = serviceInfoId;
	}

	public String getGovAgencyCode() {

		return _govAgencyCode;
	}

	public void setGovAgencyCode(String govAgencyCode) {

		_govAgencyCode = govAgencyCode;
	}

	public String getGovAgencyName() {

		return _govAgencyName;
	}

	public void setGovAgencyName(String govAgencyName) {

		_govAgencyName = govAgencyName;
	}

	public long getGovAgencyOrganizationId() {

		return _govAgencyOrganizationId;
	}

	public void setGovAgencyOrganizationId(long govAgencyOrganizationId) {

		_govAgencyOrganizationId = govAgencyOrganizationId;
	}

	public String getDomainCode() {

		return _districtCode;
	}

	public void setDomainCode(String domainCode) {

		_districtCode = domainCode;
	}

	public String getServiceName() {

		return _serviceName;
	}

	public void setServiceName(String serviceName) {

		_serviceName = serviceName;
	}

	public int compareTo(Service service) {

		// TODO Auto-generated method stub
		return 0;
	}

	public String toXmlString() {

		// TODO Auto-generated method stub
		return null;
	}

	private long _companyId;
	private long _groupId;
	private long _userId;
	private String _serviceName;
	private long _serviceConfigId;
	private long _serviceInfoId;
	private String _govAgencyCode;
	private String _govAgencyName;
	private long _govAgencyOrganizationId;
	private String _districtCode;

}
