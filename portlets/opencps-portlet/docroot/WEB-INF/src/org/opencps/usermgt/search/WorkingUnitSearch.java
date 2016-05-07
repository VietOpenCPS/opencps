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

import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.util.UserMgtUtil;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

public class WorkingUnitSearch extends SearchContainer<WorkingUnit> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("row-index");
		headerNames.add("name");
		headerNames.add("govagencycode");
		headerNames.add("isEmployer");
		headerNames.add("action");

		orderableHeaders.put("name", WorkingUnitDisplayTerms.WORKINGUNIT_NAME);
		orderableHeaders.put(
			"govagencycode", WorkingUnitDisplayTerms.WORKINGUNIT_GOVAGENCYCODE);
		orderableHeaders.put(
			"tel-number", WorkingUnitDisplayTerms.WORKINGUNIT_TELNO);
		orderableHeaders.put("email", WorkingUnitDisplayTerms.WORKINGUNIT_EMAIL);
	}

	public static final String EMPTY_RESULTS_MESSAGE =
		"no-workingunit-were-found";

	public WorkingUnitSearch(
		PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(portletRequest, new WorkingUnitDisplayTerms(portletRequest), 
			new WorkingUnitSearchTerms(
			portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
			headerNames, EMPTY_RESULTS_MESSAGE);

		WorkingUnitDisplayTerms displayTerms =
			(WorkingUnitDisplayTerms) getDisplayTerms();

		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_NAME, displayTerms.getName());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_ENNAME,
			displayTerms.getEnNamme());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_GOVAGENCYCODE,
			displayTerms.getGovAgencyCode());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_ADDRESS,
			displayTerms.getAddress());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_CITYCODE,
			displayTerms.getCityCode());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_DISTRICTCODE,
			displayTerms.getDistrictCode());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_WARDCODE,
			displayTerms.getWardCode());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_FAXNO, displayTerms.getFaxNo());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_EMAIL, displayTerms.getEmail());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_WEBSITE,
			displayTerms.getWebsite());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_TREEINDEX,
			displayTerms.getTreeIndex());
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER,
			String.valueOf(displayTerms.isEmployer()));
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_CREATEDATE,
			DateTimeUtil.convertDateToString(
				displayTerms.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL.setParameter(
			WorkingUnitDisplayTerms.WORKINGUNIT_MODIFIEDDATE,
			DateTimeUtil.convertDateToString(
				displayTerms.getModifiedDate(),
				DateTimeUtil._VN_DATE_TIME_FORMAT));
		
		try {

			String orderByCol = ParamUtil
				.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil
				.getString(portletRequest, "orderByType");

			OrderByComparator orderByComparator = UserMgtUtil
				.getWorkingUnitOrderByComparator(orderByCol, orderByType);

			setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);
			setOrderByComparator(orderByComparator);
		}
		catch (Exception e) {
			_log
				.error(e);
		}
	}
	
	public WorkingUnitSearch(
		PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
			portletRequest, DEFAULT_DELTA, iteratorURL);
	}
	
	private static Log _log = LogFactoryUtil
					.getLog(WorkingUnitSearch.class);

}
