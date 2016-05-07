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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.datamgt.util.DataMgtUtil;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class DossierSearch extends SearchContainer<Dossier> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("no");
		headerNames.add("create-date");
		headerNames.add("service-name");
		headerNames.add("gov-agency-name");
		headerNames.add("dossier-status");
		headerNames.add("receive-datetime");
		headerNames.add("reception-no");
		headerNames.add("action");

		orderableHeaders
		    .put("gov-agency-name", DossierDisplayTerms.GOVAGENCY_NAME);
		orderableHeaders
		    .put("receive-datetime", DossierDisplayTerms.RECEIVE_DATETIME);
		orderableHeaders
		    .put("create-date", DossierDisplayTerms.CREATE_DATE);
		orderableHeaders
		    .put("dossier-status", DossierDisplayTerms.DOSSIER_STATUS);
		
	}
	public static final String EMPTY_RESULTS_MESSAGE =
	    "no-dossier-were-found";

	public DossierSearch(
	    PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(
		    portletRequest, new DossierDisplayTerms(
		        portletRequest), new DossierSearchTerms(
		            portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
		    	headerNames, EMPTY_RESULTS_MESSAGE);

		DossierDisplayTerms displayTerms =
		    (DossierDisplayTerms) getDisplayTerms();
		
		iteratorURL
		    .setParameter(DossierDisplayTerms.GOVAGENCY_NAME, displayTerms
		        .getGovAgencyName());
		iteratorURL
		    .setParameter(DossierDisplayTerms.DOSSIER_STATUS, String.valueOf(displayTerms
		        .getDossierStatus()));

		iteratorURL
		    .setParameter(DossierDisplayTerms.GROUP_ID, String
		        .valueOf(displayTerms
		            .getGroupId()));
		iteratorURL
		    .setParameter(DossierDisplayTerms.CREATE_DATE, DateTimeUtil
		        .convertDateToString(displayTerms
		            .getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		iteratorURL
		    .setParameter(DossierDisplayTerms.RECEIVE_DATETIME, DateTimeUtil
		        .convertDateToString(displayTerms
		            .getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT));
		
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

	public DossierSearch(
	    PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
		    portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil
	    .getLog(DossierSearch.class);

}
