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
import org.opencps.dossiermgt.model.DossierLog;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class DossierLogSearch extends SearchContainer<DossierLog> {

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("stt");
		headerNames.add("update-date-time");
		headerNames.add("dossier-id");
		headerNames.add("reception-no");
		headerNames.add("dossier-status");
		headerNames.add("action-info");
		headerNames.add("message-info");
		headerNames.add("level");
		
	}
	public static final String EMPTY_RESULTS_MESSAGE =
	    "no-dossier-log-were-found";

	public DossierLogSearch(
	    PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(
		    portletRequest, new DossierLogDisplayTerms(
		        portletRequest), new DossierLogSearchTerms(
		            portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
		    	headerNames, EMPTY_RESULTS_MESSAGE);

		DossierLogDisplayTerms displayTerms =
		    (DossierLogDisplayTerms) getDisplayTerms();
		
	}

	public DossierLogSearch(
	    PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
		    portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil
	    .getLog(DossierLogSearch.class);

}
