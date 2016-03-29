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

package org.opencps.usermgt.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.util.UserMgtUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 *
 */
public class EmployeeSearch extends SearchContainer<Employee> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("STT");
		headerNames.add("employee-no");
		headerNames.add("full-name");
		headerNames.add("working-unit");
		headerNames.add("screen-name");
		headerNames.add("working-unit-status");
		headerNames.add("action");

		/*orderableHeaders.put("employee-no", EmployeeDisplayTerm.EMPLOYEE_NO);*/
		orderableHeaders.put("full-name", EmployeeDisplayTerm.FULL_NAME);
		/*orderableHeaders.put("working-unit-id",
				EmployeeDisplayTerm.WORKING_UNIT_ID);*/
		orderableHeaders.put("working-unit-status",
				EmployeeDisplayTerm.WORKING_STATUS);

	}
	public static final String EMPTY_RESULTS_MESSAGE = "no-employee-collection-were-found";

	public EmployeeSearch(PortletRequest portletRequest, int delta,
			PortletURL iteratorURL) {

		super(portletRequest, new EmployeeDisplayTerm(portletRequest),
				new EmployeeSearchTerm(portletRequest), DEFAULT_CUR_PARAM,
				delta, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		EmployeeDisplayTerm displayTerms = (EmployeeDisplayTerm) getDisplayTerms();

		iteratorURL.setParameter(EmployeeDisplayTerm.EMPLOYEE_NO,
				displayTerms.getEmployeeNo());
		iteratorURL.setParameter(EmployeeDisplayTerm.FULL_NAME,
				displayTerms.getFullName());
		iteratorURL.setParameter(EmployeeDisplayTerm.WORKING_UNIT_ID,
				String.valueOf(displayTerms.getWorkingUnitId()));
		iteratorURL.setParameter(EmployeeDisplayTerm.WORKING_STATUS,
				String.valueOf(displayTerms.getWorkingStatus()));

		try {

			String orderByCol = ParamUtil.getString(portletRequest,
					"orderByCol");
			String orderByType = ParamUtil.getString(portletRequest,
					"orderByType");

			OrderByComparator orderByComparator = UserMgtUtil
					.getEmployeeOrderByComparator(orderByCol, orderByType);

			setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);
			setOrderByComparator(orderByComparator);
		} catch (Exception e) {
			_log.error(e);
		}
	}

	public EmployeeSearch(PortletRequest portletRequest,
			PortletURL iteratorURL) {

		this(portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil.getLog(EmployeeSearch.class);
}
