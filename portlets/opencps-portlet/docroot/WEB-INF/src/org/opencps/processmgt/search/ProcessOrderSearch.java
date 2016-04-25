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

package org.opencps.processmgt.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.util.AccountMgtUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.util.ProcessOrderUtils;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;


/**
 * @author khoavd
 *
 */
public class ProcessOrderSearch extends SearchContainer<ProcessOrder>{

	public static final String EMPTY_RESULTS_MESSAGE =
	    "no-process-order-were-found";

	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	
	static {
		headerNames.add("ma-tiep-nhan");
		headerNames.add("chu-ho-so");
		headerNames.add("thu-tuc");
		headerNames.add("buoc-xu-ly");
		headerNames.add("nguoi-phu-trach");
		headerNames.add("han-xu-ly");
		
		orderableHeaders.put("ma-tiep-nhan",
			ProcessOrderDisplayTerms.MA_TIEP_NHAN);
		orderableHeaders.put("chu-ho-so",
			ProcessOrderDisplayTerms.CHU_HO_SO);
		orderableHeaders.put("thu-tuc",
			ProcessOrderDisplayTerms.THU_TUC);
		orderableHeaders.put("buoc-xu-ly",
			ProcessOrderDisplayTerms.BUOC_XU_LY);
		orderableHeaders.put("nguoi-phu-trach",
			ProcessOrderDisplayTerms.NGUOI_PHU_TRACH);
	}

	public ProcessOrderSearch(
	    PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(portletRequest, new ProcessOrderSearchTerms(portletRequest), new ProcessOrderSearchTerms(
		    portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);
		try {

			String orderByCol = ParamUtil
				.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil
				.getString(portletRequest, "orderByType");

			OrderByComparator orderByComparator = ProcessOrderUtils.
							getProcessOrderByComparator(orderByCol, orderByType);

			setOrderableHeaders(orderableHeaders);
			setOrderByCol(orderByCol);
			setOrderByType(orderByType);
			setOrderByComparator(orderByComparator);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
