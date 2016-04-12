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

package org.opencps.accountmgt.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.util.AccountMgtUtil;
import org.opencps.datamgt.search.DictCollectionSearch;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;

public class CitizenSearch extends SearchContainer<Citizen>{
	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	
	static {
		
		headerNames.add("personal-id");
		headerNames.add("full-name");
		headerNames.add("gender");
		headerNames.add("birth-date");
		headerNames.add("account");
		headerNames.add("account-status");
		headerNames.add("action");
		
		orderableHeaders.put("personal-id", CitizenSearchTerm.CITIZEN_PERSONALID);
		orderableHeaders.put("full-name", CitizenSearchTerm.CITIZEN_FULLNAME);
		orderableHeaders.put("gender", CitizenSearchTerm.CITIZEN_GENDER);
		orderableHeaders.put("birth-date", CitizenSearchTerm.CITIZEN_BIRTHDATE);
		orderableHeaders.put("account", CitizenSearchTerm.CITIZEN_EMAIL);
		orderableHeaders.put("account-status", CitizenSearchTerm.CITIZEN_ACCOUNTSTATUS);
	}
	
	public static final String EMPTY_RESULTS_MESSAGE =
					"no-citizen-were-found";
	
	public CitizenSearch(
		PortletRequest portletRequest, int delta, PortletURL iteratorURL) {
		
		super(portletRequest, new CitizenSearchTerm(portletRequest), 
			
			new CitizenSearchTerm(
			portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
			headerNames, EMPTY_RESULTS_MESSAGE);
		
		CitizenDisplayTerms displayTerms = (CitizenDisplayTerms) getDisplayTerms();
		
		iteratorURL.setParameter(CitizenSearchTerm.CITIZEN_PERSONALID, 
			displayTerms.getPersonalId());
		iteratorURL.setParameter(CitizenSearchTerm.CITIZEN_FULLNAME, 
			displayTerms.getFullName());
		iteratorURL.setParameter(CitizenSearchTerm.CITIZEN_GENDER, 
			String.valueOf(displayTerms.getGender()));
		iteratorURL.setParameter(CitizenSearchTerm.CITIZEN_BIRTHDATE, 
			DateTimeUtil.convertDateToString(
			displayTerms.getBirthDate(), DateTimeUtil._VN_DATE_FORMAT));
		iteratorURL.setParameter(CitizenSearchTerm.CITIZEN_EMAIL, 
			displayTerms.getEmail());
		iteratorURL.setParameter(CitizenSearchTerm.CITIZEN_ACCOUNTSTATUS, 
			String.valueOf(displayTerms.getAccountStatus()));
		
		try {

			String orderByCol = ParamUtil
				.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil
				.getString(portletRequest, "orderByType");

			OrderByComparator orderByComparator = AccountMgtUtil
				.getCitizenOrderByComparator(orderByCol, orderByType);

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
	
	public CitizenSearch(PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
			portletRequest, DEFAULT_DELTA, iteratorURL);
	}
	
	private static Log _log = LogFactoryUtil
					.getLog(CitizenSearch.class);
}
