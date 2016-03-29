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

import org.opencps.usermgt.util.UserMgtUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.ResourceAction;

public class ResourceActionSearch extends SearchContainer<ResourceAction>{

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("actionId");

		orderableHeaders.put(
			"actionId", ResourceActionDisplayTerms.RESOURCE_ACTION_NAME);
	}
	public static final String EMPTY_RESULTS_MESSAGE =
					"no-resourceaction-were-found";
	public ResourceActionSearch(
		PortletRequest portletRequest, int delta, PortletURL iteratorURL) {
		super(portletRequest, new ResourceActionDisplayTerms(portletRequest), 
			new ResourceActionSearchTerm(
			portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
			headerNames, EMPTY_RESULTS_MESSAGE);
		
		ResourceActionDisplayTerms displayTerms =
						(ResourceActionDisplayTerms) getDisplayTerms();
		
		iteratorURL.setParameter(
			ResourceActionDisplayTerms.RESOURCE_ACTION_NAME,
			displayTerms.getName());
		
		try {

			String orderByCol = ParamUtil
				.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil
				.getString(portletRequest, "orderByType");

			OrderByComparator orderByComparator = UserMgtUtil
				.getResourceOrderByComparator(orderByCol, orderByType);

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
	
	public ResourceActionSearch(
		PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
			portletRequest, DEFAULT_DELTA, iteratorURL);
	}
	
	private static Log _log = LogFactoryUtil
					.getLog(ResourceActionSearch.class);
}
