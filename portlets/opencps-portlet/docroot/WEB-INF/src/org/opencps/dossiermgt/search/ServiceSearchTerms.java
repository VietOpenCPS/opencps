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

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class ServiceSearchTerms extends ServiceDisplayTerms {

	public ServiceSearchTerms(PortletRequest portletRequest) {
		super(
		    portletRequest);

		serviceName = DAOParamUtil
		    .getString(portletRequest, SERVICE_NAME);

		serviceConfigId = ParamUtil
		    .getLong(portletRequest, SERVICE_CONFIG_ID);

		serviceInfoId = ParamUtil
		    .getLong(portletRequest, SERVICE_INFO_ID);

		domainCode = DAOParamUtil
		    .getString(portletRequest, DOMAIN_CODE);

		govAgencyName = DAOParamUtil
		    .getString(portletRequest, GOVAGENCY_NAME);

		govAgencyCode = DAOParamUtil
		    .getString(portletRequest, GOVAGENCY_CODE);

	}

	public long getServiceConfigId() {

		return serviceConfigId;
	}

	public void setServiceConfigId(long serviceConfigId) {

		this.serviceConfigId = serviceConfigId;
	}

	public long getServiceInfoId() {

		return serviceInfoId;
	}

	public void setServiceInfoId(long serviceInfoId) {

		this.serviceInfoId = serviceInfoId;
	}

	public String getDomainCode() {

		return domainCode;
	}

	public void setDomainCode(String domainCode) {

		this.domainCode = domainCode;
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

	public String getServiceName() {

		return serviceName;
	}

	public void setServiceName(String serviceName) {

		this.serviceName = serviceName;
	}

	protected long serviceConfigId;
	protected long serviceInfoId;
	protected String domainCode;
	protected String govAgencyCode;
	protected String govAgencyName;
	protected String serviceName;
}
