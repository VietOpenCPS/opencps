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

import org.opencps.dossiermgt.comparator.DossierFileDossierFileDateComparator;
import org.opencps.dossiermgt.portlet.DossierMgtFrontOfficePortlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author trungdk
 */
public class DossierSearchUtil {

	private static Log _log =
			LogFactoryUtil.getLog(DossierMgtFrontOfficePortlet.class.getName());
	
	public static BooleanQuery buildSearchQuery(String keywords,
			SearchContext searchContext) {
		BooleanQuery query = BooleanQueryFactoryUtil.create(searchContext);
		keywords = keywords.toLowerCase();
		String[] terms = StringUtil.split(keywords);
		try {
			query.addTerm(DossierDisplayTerms.RECEPTION_NO, keywords, false);
			query.addTerm(DossierDisplayTerms.CITY_NAME, keywords, false);
			query.addTerm(DossierDisplayTerms.EXTERNALREF_NO, keywords, false);
			query.addTerm(DossierDisplayTerms.EXTERNALREF_URL, keywords, false);
			query.addTerm(DossierDisplayTerms.GOVAGENCY_NAME, keywords, false);
			query.addTerm(DossierDisplayTerms.SUBJECT_NAME, keywords, false);
			query.addTerm(DossierDisplayTerms.CITY_NAME, keywords, false);
			query.addTerm(DossierDisplayTerms.ADDRESS, keywords, false);
			query.addTerm(DossierDisplayTerms.DISTRICT_NAME, keywords, false);
			query.addTerm(DossierDisplayTerms.WARD_NAME, keywords, false);
			query.addTerm(DossierDisplayTerms.CONTACT_NAME, keywords, false);
			query.addTerm(DossierDisplayTerms.CONTACT_TEL_NO, keywords, false);
			query.addTerm(DossierDisplayTerms.CONTACT_EMAIL, keywords, false);
			query.addTerm(DossierDisplayTerms.NOTE, keywords, false);
			query.addTerm(DossierDisplayTerms.DOSSIER_ID, keywords, false);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			_log.error(e);
		}
		
		return query;
	}
	
	public static OrderByComparator getDossierFileOrderByComparator(
		String orderByCol, String orderByType) {
		

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator orderByComparator = null;
		
		if (orderByCol.equals(DossierFileDisplayTerms.DOSSIER_FILE_DATE)) {
			orderByComparator = new DossierFileDossierFileDateComparator(orderByAsc);
		}
		
		return orderByComparator;
	}
}
