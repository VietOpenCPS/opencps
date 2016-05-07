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

import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author trungnt
 */
public class ProcessOrderSearch extends SearchContainer<ProcessOrder> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames
		    .add(StringPool.BLANK);
		headerNames
		    .add("reception-no");
		headerNames
		    .add("subject-name");
		headerNames
		    .add("service-name");
		headerNames
		    .add("process-step");
		headerNames
		    .add("action-user-id");
		headerNames
		    .add("action-date-time");
		headerNames
		    .add("action");
	}
	public static final String EMPTY_RESULTS_MESSAGE = "no-dossier-were-found";

	public ProcessOrderSearch(
	    PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(
		    portletRequest, new ProcessOrderDisplayTerms(
		        portletRequest), new ProcessOrderSearchTerms(
		            portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		ProcessOrderDisplayTerms displayTerms =
		    (ProcessOrderDisplayTerms) getDisplayTerms();

		iteratorURL
		    .setParameter(ProcessOrderDisplayTerms.SERVICE_INFO_ID, String
		        .valueOf(displayTerms
		            .getServiceInfoId()));

		iteratorURL
		    .setParameter(ProcessOrderDisplayTerms.USER_ID, String
		        .valueOf(displayTerms
		            .getUserId()));

		iteratorURL
		    .setParameter(
		        ProcessOrderDisplayTerms.GOV_AGENCY_ORGANIZATION_ID, String
		            .valueOf(displayTerms
		                .getGovAgencyOrganizationId()));
		iteratorURL
		    .setParameter(ProcessOrderDisplayTerms.PROCESS_STEP_ID, String
		        .valueOf(displayTerms
		            .getProcessStepId()));

		iteratorURL
		    .setParameter(ProcessOrderDisplayTerms.ACTION_USER_ID, String
		        .valueOf(displayTerms
		            .getActionUserId()));

		iteratorURL
		    .setParameter(ProcessOrderDisplayTerms.RECEPTION_NO, displayTerms
		        .getReceptionNo());
		iteratorURL
		    .setParameter(
		        ProcessOrderDisplayTerms.ACTION_DATE_TIME, DateTimeUtil
		            .convertDateToString(displayTerms
		                .getActionDatetime(),
		                DateTimeUtil._VN_DATE_TIME_FORMAT));
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

	public ProcessOrderSearch(
	    PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
		    portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil
	    .getLog(ProcessOrderSearch.class);

}
