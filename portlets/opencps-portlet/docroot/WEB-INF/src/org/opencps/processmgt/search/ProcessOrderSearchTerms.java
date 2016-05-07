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

package org.opencps.processmgt.search;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class ProcessOrderSearchTerms extends ProcessOrderDisplayTerms {

	public ProcessOrderSearchTerms(PortletRequest portletRequest) {
		super(
		    portletRequest);

		actionDatetime = ParamUtil
		    .getDate(portletRequest, ACTION_DATE_TIME, DateTimeUtil
		        .getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));

		serviceInfoId = DAOParamUtil
		    .getLong(portletRequest, SERVICE_INFO_ID);

		userId = DAOParamUtil
		    .getLong(portletRequest, USER_ID);

		receptionNo = DAOParamUtil
		    .getString(portletRequest, RECEPTION_NO);

		processStepId = DAOParamUtil
		    .getLong(portletRequest, PROCESS_STEP_ID);

		govAgencyOrganizationId = DAOParamUtil
		    .getLong(portletRequest, GOV_AGENCY_ORGANIZATION_ID);

	}
}
