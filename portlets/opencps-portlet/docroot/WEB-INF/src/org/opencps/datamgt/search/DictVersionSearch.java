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

package org.opencps.datamgt.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.datamgt.model.DictVersion;
import org.opencps.datamgt.util.DataMgtUtil;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author Dunglt
 */

public class DictVersionSearch extends SearchContainer<DictVersion> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("id");
		headerNames.add("version");
		headerNames.add("issue-status");
		headerNames.add("description");
		headerNames.add("create-date");
		headerNames.add("modified-date");
		headerNames.add("validatedfrom-date");
		headerNames.add("validatedto-date");
		headerNames.add("author");
		headerNames.add("action");
		
		orderableHeaders.put("create-date", DictVersionDisplayTerms.CREATE_DATE);
		orderableHeaders.put("modified-date", DictVersionDisplayTerms.MODIFIED_DATE);
		orderableHeaders.put("version", DictVersionDisplayTerms.VERSION);
		orderableHeaders.put("validatedfrom-date", DictVersionDisplayTerms.VALIDATED_FROM);
		orderableHeaders.put("validatedto-date", DictVersionDisplayTerms.VALIDATED_TO);
	}
	public static final String EMPTY_RESULTS_MESSAGE =
		"no-dict-version-were-found";

	public DictVersionSearch(
		PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(
			portletRequest, new DictVersionDisplayTerms(
				portletRequest), new DictVersionSearchTerms(
					portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		DictVersionDisplayTerms displayTerms =
			(DictVersionDisplayTerms) getDisplayTerms();
		// DictVersionSearchTerms searchTerms = (DictVersionSearchTerms)
		// getSearchTerms();

		iteratorURL
			.setParameter(DictVersionDisplayTerms.DESCRIPTION, displayTerms
				.getDescription());

		iteratorURL
			.setParameter(DictVersionDisplayTerms.GROUP_ID, String
				.valueOf(displayTerms
					.getGroupId()));
		iteratorURL
			.setParameter(DictVersionDisplayTerms.CREATE_DATE, DateTimeUtil
				.convertDateToString(displayTerms
					.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL
			.setParameter(DictVersionDisplayTerms.MODIFIED_DATE, DateTimeUtil
				.convertDateToString(displayTerms
					.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL
			.setParameter(DictVersionDisplayTerms.VALIDATED_FROM, DateTimeUtil
				.convertDateToString(displayTerms
					.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL
			.setParameter(DictVersionDisplayTerms.VALIDATED_TO, DateTimeUtil
				.convertDateToString(displayTerms
					.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL
			.setParameter(DictVersionDisplayTerms.USER_ID, String
				.valueOf(displayTerms
					.getUserId()));
		iteratorURL
			.setParameter(DictVersionDisplayTerms.VERSION, displayTerms
				.getVersion());
		iteratorURL
			.setParameter(DictVersionDisplayTerms.ISSUE_STATUS, String
				.valueOf(displayTerms
					.getIssueStatus()));

		try {

			String orderByCol = ParamUtil
				.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil
				.getString(portletRequest, "orderByType");

			OrderByComparator orderByComparator = DataMgtUtil
				.getDictVersionOrderByComparator(orderByCol, orderByType);

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

	public DictVersionSearch(
		PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
			portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil
		.getLog(DictVersionSearch.class);
}
