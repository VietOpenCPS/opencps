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

import org.opencps.dossiermgt.portlet.DossierMgtFrontOfficePortlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;

/**
 * @author trungdk
 */
public class DossierFileSearchUtil {
	public static BooleanQuery buildSearchQuery(String keywords,
			SearchContext searchContext) {
		BooleanQuery query = BooleanQueryFactoryUtil.create(searchContext);
		keywords = keywords.toLowerCase();
		try {
			query.addTerm(DossierFileDisplayTerms.DISPLAY_NAME, keywords, false);
			query.addTerm(DossierFileDisplayTerms.FORM_DATA, keywords, false);
			query.addTerm(DossierFileDisplayTerms.DOSSIER_FILE_NO, keywords,
					false);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			_log.error(e);
		}
		/*
		String[] terms = StringUtil.split(keywords);
		for (String term : terms) {
			TermQuery termDisplayName = TermQueryFactoryUtil.create(
					searchContext, DossierFileDisplayTerms.DISPLAY_NAME, term);
			TermQuery termFormData = TermQueryFactoryUtil.create(searchContext,
					DossierFileDisplayTerms.FORM_DATA, term);
			TermQuery termFileNo = TermQueryFactoryUtil.create(searchContext,
					DossierFileDisplayTerms.DOSSIER_FILE_NO, term);

			try {
				query.add(termDisplayName, BooleanClauseOccur.SHOULD);
				query.add(termFormData, BooleanClauseOccur.SHOULD);
				query.add(termFileNo, BooleanClauseOccur.SHOULD);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		return query;
	}
	private static Log _log =
			LogFactoryUtil.getLog(DossierFileSearchUtil.class.getName());
}
