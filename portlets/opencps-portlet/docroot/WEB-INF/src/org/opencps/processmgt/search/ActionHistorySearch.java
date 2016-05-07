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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author binhth
 */
public class ActionHistorySearch extends SearchContainer<ActionHistory> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames
		    .add(StringPool.BLANK);
		headerNames
		    .add("ngay-gio");
		headerNames
		    .add("trang-thai");
		headerNames
		    .add("thao-tac-xu-ly");
		headerNames
		    .add("thong-tin-xu-ly");
	}
	public static final String EMPTY_RESULTS_MESSAGE = "no-action-history-were-found";

	public ActionHistorySearch(
	    PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(
		    portletRequest, new ActionHistoryDisplayTerms(
		        portletRequest), new ActionHistorySearchTerms(
		            portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		ActionHistoryDisplayTerms displayTerms =
		    (ActionHistoryDisplayTerms) getDisplayTerms();

		
		//Code sau
		/*try {

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
		}*/
	}

	public ActionHistorySearch(
	    PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
		    portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil
	    .getLog(ActionHistorySearch.class);

}
