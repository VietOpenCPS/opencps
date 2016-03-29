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

package org.opencps.datamgt.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.util.DataMgtUtil;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class DictCollectionSearch extends SearchContainer<DictCollection> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames
			.add("id");
		headerNames
			.add("code");
		headerNames
			.add("name");
		headerNames
			.add("description");
		headerNames
			.add("create-date");
		headerNames
			.add("modified-date");
		headerNames
			.add("author");
		headerNames
			.add("action");

		orderableHeaders
			.put("name", DictCollectionDisplayTerms.COLLECTION_NAME);
		orderableHeaders
			.put("code", DictCollectionDisplayTerms.COLLECTION_CODE);
		orderableHeaders
			.put("create-date", DictCollectionDisplayTerms.CREATE_DATE);
		orderableHeaders
			.put("modified-date", DictCollectionDisplayTerms.MODIFIED_DATE);
		orderableHeaders
			.put("author", DictCollectionDisplayTerms.USER_ID);
	}
	public static final String EMPTY_RESULTS_MESSAGE =
		"no-dict-collection-were-found";

	public DictCollectionSearch(
		PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(
			portletRequest, new DictCollectionDisplayTerms(
				portletRequest), new DictCollectionSearchTerms(
					portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		DictCollectionDisplayTerms displayTerms =
			(DictCollectionDisplayTerms) getDisplayTerms();
		// DictCollectionSearchTerms searchTerms = (DictCollectionSearchTerms)
		// getSearchTerms();

		iteratorURL
			.setParameter(
				DictCollectionDisplayTerms.COLLECTION_NAME, displayTerms
					.getCollectionName());
		iteratorURL
			.setParameter(DictCollectionDisplayTerms.DESCRIPTION, displayTerms
				.getDescription());

		iteratorURL
			.setParameter(
				DictCollectionDisplayTerms.COLLECTION_CODE, displayTerms
					.getCollectionCode());
		iteratorURL
			.setParameter(DictCollectionDisplayTerms.GROUP_ID, String
				.valueOf(displayTerms
					.getGroupId()));
		iteratorURL
			.setParameter(DictCollectionDisplayTerms.CREATE_DATE, DateTimeUtil
				.convertDateToString(displayTerms
					.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL
			.setParameter(DictCollectionDisplayTerms.MODIFIED_DATE, DateTimeUtil
				.convertDateToString(displayTerms
					.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL
			.setParameter(DictCollectionDisplayTerms.USER_ID, String
				.valueOf(displayTerms
					.getUserId()));

		try {

			String orderByCol = ParamUtil
				.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil
				.getString(portletRequest, "orderByType");

			OrderByComparator orderByComparator = DataMgtUtil
				.getDictCollectionOrderByComparator(orderByCol, orderByType);

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

	public DictCollectionSearch(
		PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
			portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil
		.getLog(DictCollectionSearch.class);

}
