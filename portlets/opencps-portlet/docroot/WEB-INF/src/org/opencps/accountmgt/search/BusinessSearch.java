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

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.util.AccountMgtUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;


public class BusinessSearch extends SearchContainer<Business>{
	
	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	static {
		headerNames.add("id-number");
		headerNames.add("name");
		headerNames.add("business-type");
		headerNames.add("account");
		headerNames.add("account-status");
		headerNames.add("action");
		
		orderableHeaders.put("id-number",
			BusinessDisplayTerms.BUSINESS_IDNUMBER);
		orderableHeaders.put("name", BusinessDisplayTerms.BUSINESS_NAME);
		orderableHeaders.put("business-type", 
			BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		orderableHeaders.put("account", BusinessDisplayTerms.BUSINESS_EMAIL);
		orderableHeaders.put("account-status", 
			BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS);	
	}
	
	public static final String EMPTY_RESULTS_MESSAGE =
					"no-business-were-found";
	
	public BusinessSearch(
		PortletRequest portletRequest, int delta, PortletURL iteratorURL) {
			
			super(portletRequest, new BusinessSearchTerm(portletRequest), 
			
			new BusinessSearchTerm(
			portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
			headerNames, EMPTY_RESULTS_MESSAGE);
			
			BusinessDisplayTerms displayTerms = (BusinessDisplayTerms) 
							getDisplayTerms();
			
			iteratorURL.setParameter(BusinessDisplayTerms.BUSINESS_IDNUMBER, 
				displayTerms.getIdNumber());
			iteratorURL.setParameter(BusinessDisplayTerms.BUSINESS_NAME, 
							displayTerms.getName());
			iteratorURL.setParameter(BusinessDisplayTerms.BUSINESS_BUSINESSTYPE, 
				displayTerms.getBusinessType());
			iteratorURL.setParameter(BusinessDisplayTerms.BUSINESS_EMAIL, 
				displayTerms.getEmail());
			iteratorURL.setParameter(BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS, 
				String.valueOf(displayTerms.getAccountStatus()));
			

			try {

				String orderByCol = ParamUtil
					.getString(portletRequest, "orderByCol");
				String orderByType = ParamUtil
					.getString(portletRequest, "orderByType");

				OrderByComparator orderByComparator = AccountMgtUtil
					.getBusinessOrderByComparator(orderByCol, orderByType);

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
	
	public BusinessSearch(PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
			portletRequest, DEFAULT_DELTA, iteratorURL);
	}
	
	
	private static Log _log = LogFactoryUtil
					.getLog(BusinessSearch.class);

}
