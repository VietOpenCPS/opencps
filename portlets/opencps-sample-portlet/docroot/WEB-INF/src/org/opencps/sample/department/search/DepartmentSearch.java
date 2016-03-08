/*******************************************************************************
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package org.opencps.sample.department.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.sample.department.model.Department;
import org.opencps.sample.department.util.DepartmentUtil;
import org.opencps.sample.utils.OpenCPSPortletKey;
import org.opencps.sample.utils.PortletUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletPreferencesFactoryUtil;


/**
 * @author trungnt
 *
 */
public class DepartmentSearch extends SearchContainer<Department> {
	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("id");
		headerNames.add("name");
		headerNames.add("description");
		headerNames.add("create-date");
		headerNames.add("modified-date");
		headerNames.add("author");
		headerNames.add(StringPool.BLANK);

		orderableHeaders.put("name", DepartmentDisplayTerms.NAME);
		orderableHeaders.put("create-date", DepartmentDisplayTerms.CREATE_DATE);
		orderableHeaders.put("modified-date", DepartmentDisplayTerms.MODIFIED_DATE);
		orderableHeaders.put("author", DepartmentDisplayTerms.USER_NAME);
	}

	public DepartmentSearch(PortletRequest portletRequest, int cur, int delta, PortletURL iteratorURL) {

		super(portletRequest, new DepartmentDisplayTerms(portletRequest), new DepartmentSearchTerms(portletRequest),
				DEFAULT_CUR_PARAM, cur, delta, iteratorURL, headerNames, null);

		PortletConfig portletConfig = (PortletConfig) portletRequest.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);

		DepartmentDisplayTerms displayTerms = (DepartmentDisplayTerms) getDisplayTerms();
		DepartmentSearchTerms searchTerms = (DepartmentSearchTerms) getSearchTerms();

		String portletName = portletConfig.getPortletName();

		iteratorURL.setParameter(DepartmentDisplayTerms.NAME, displayTerms.getName());
		iteratorURL.setParameter(DepartmentDisplayTerms.DESCRIPTION, displayTerms.getDescription());

		iteratorURL.setParameter(DepartmentDisplayTerms.PARENT_ID, String.valueOf(displayTerms.getParentId()));
		iteratorURL.setParameter(DepartmentDisplayTerms.GROUP_ID, String.valueOf(displayTerms.getGroupId()));
		iteratorURL.setParameter(DepartmentDisplayTerms.CREATE_DATE,
				PortletUtil.convertDateToString(displayTerms.getCreateDate(), PortletUtil._VN_DATE_TIME_FORMAT));
		iteratorURL.setParameter(DepartmentDisplayTerms.CREATE_DATE,
				PortletUtil.convertDateToString(displayTerms.getModifiedDate(), PortletUtil._VN_DATE_TIME_FORMAT));
		iteratorURL.setParameter(DepartmentDisplayTerms.USER_NAME, displayTerms.getUserName());

		try {
			PortalPreferences preferences = PortletPreferencesFactoryUtil.getPortalPreferences(portletRequest);

			String orderByCol = ParamUtil.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil.getString(portletRequest, "orderByType");

			if (Validator.isNotNull(orderByCol) && Validator.isNotNull(orderByType)) {

				preferences.setValue(OpenCPSPortletKey.STAFF_MANAGEMENT, "department-order-by-col", orderByCol);
				preferences.setValue(OpenCPSPortletKey.STAFF_MANAGEMENT, "department-order-by-type", orderByType);
			} else {
				orderByCol = preferences.getValue(OpenCPSPortletKey.STAFF_MANAGEMENT, "department-order-by-col", "id");
				orderByType = preferences.getValue(OpenCPSPortletKey.STAFF_MANAGEMENT, "department-order-by-type",
						"asc");
			}

			OrderByComparator orderByComparator = DepartmentUtil.getDepartmentOrderByComparator(orderByCol,
					orderByType);

			setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);
			setOrderByComparator(orderByComparator);
		} catch (Exception e) {
			_log.error(e);
		}
	}

	public DepartmentSearch(PortletRequest portletRequest, PortletURL iteratorURL) {

		this(portletRequest, 0, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil.getLog(DepartmentSearch.class);

}
