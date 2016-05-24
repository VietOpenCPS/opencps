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
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;


public class DossierFileSearch extends SearchContainer<DossierFile> {
	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("no");
		//headerNames.add("create-date");
		headerNames.add("dossierfile-fileno");
		headerNames.add("dossierfile-filedate");
		headerNames.add("dossierfile-displayname");
		headerNames.add("govagency-name");
		headerNames.add("owner-name");
		//headerNames.add("dossierfile-filetype");
	}
	public static final String EMPTY_RESULTS_MESSAGE =
	    "no-dossierfile-were-found";

	public DossierFileSearch(
	    PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(
		    portletRequest, new DossierFileDisplayTerms(
		        portletRequest), new DossierFileSearchTerms(
		            portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
		    	headerNames, EMPTY_RESULTS_MESSAGE);

		DossierFileDisplayTerms displayTerms =
		    (DossierFileDisplayTerms) getDisplayTerms();
		
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

	public DossierFileSearch(
	    PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
		    portletRequest, DEFAULT_DELTA, iteratorURL);
	}

	private static Log _log = LogFactoryUtil
	    .getLog(DossierFileSearch.class);

}
