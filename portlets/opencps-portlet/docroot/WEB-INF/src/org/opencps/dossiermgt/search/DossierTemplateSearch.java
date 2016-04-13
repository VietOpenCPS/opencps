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

import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.util.DossierMgtUtil;
import org.opencps.usermgt.search.WorkingUnitDisplayTerms;
import org.opencps.usermgt.search.WorkingUnitSearch;
import org.opencps.usermgt.search.WorkingUnitSearchTerms;
import org.opencps.usermgt.util.UserMgtUtil;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;


public class DossierTemplateSearch extends SearchContainer<DossierTemplate>{
	static List<String> headerNames = new ArrayList<String>();
	static Map<String, String> orderableHeaders = new HashMap<String, String>();
	
	static {
		headerNames.add("STT");
		headerNames.add("template-number");
		headerNames.add("template_name");
		headerNames.add("template-description");
		headerNames.add("action");
		
		
		orderableHeaders.put(
			"template-number", DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENO);
		orderableHeaders.put(
			"template_name", DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENAME);
		orderableHeaders.put(
			"template-description", DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DESCRIPTION);
	}
	
	public static final String EMPTY_RESULTS_MESSAGE =
		"no-dossier-template-were-found";
	

	public DossierTemplateSearch(
		PortletRequest portletRequest, int delta, PortletURL iteratorURL) {

		super(portletRequest, new DossierTemplateDisplayTerms(portletRequest), 
			new DossierTemplateSearchTerms(
			portletRequest), DEFAULT_CUR_PARAM, delta, iteratorURL, 
			headerNames, EMPTY_RESULTS_MESSAGE);

		DossierTemplateDisplayTerms displayTerms =
			(DossierTemplateDisplayTerms) getDisplayTerms();
		
		iteratorURL.setParameter(
			DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENAME,
			displayTerms.getTemplateName());
		
		iteratorURL.setParameter(
			DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENO,
			displayTerms.getTemplateNo());
		
		iteratorURL.setParameter(
			DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DESCRIPTION, 
			displayTerms.getDescription());
			
		try {

			String orderByCol = ParamUtil
				.getString(portletRequest, "orderByCol");
			String orderByType = ParamUtil
				.getString(portletRequest, "orderByType");

			OrderByComparator orderByComparator = DossierMgtUtil
				.getDossierTemplateOrderByComparator(orderByCol, orderByType);

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
	
	public DossierTemplateSearch(
		PortletRequest portletRequest, PortletURL iteratorURL) {

		this(
			portletRequest, DEFAULT_DELTA, iteratorURL);
	}
	
	private static Log _log = LogFactoryUtil
					.getLog(DossierTemplateSearch.class);
}
