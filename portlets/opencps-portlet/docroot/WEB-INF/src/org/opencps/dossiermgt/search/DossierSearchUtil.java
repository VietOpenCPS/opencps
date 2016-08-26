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
		/*
		for (String term : terms) {
			TermQuery termReceptionNo = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.RECEPTION_NO, term);
			TermQuery termCityName = TermQueryFactoryUtil.create(searchContext,
					DossierDisplayTerms.CITY_NAME, term);
			TermQuery termExternalRefNo = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.EXTERNALREF_NO, term);
			TermQuery termExternalRef = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.EXTERNALREF_URL, term);
			TermQuery termGovAgencyName = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.GOVAGENCY_NAME, term);
			TermQuery termSubjectName = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.SUBJECT_NAME, term);
			TermQuery termAddress = TermQueryFactoryUtil.create(searchContext,
					DossierDisplayTerms.ADDRESS, term);
			TermQuery termDistrictName = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.DISTRICT_NAME, term);
			TermQuery termWardName = TermQueryFactoryUtil.create(searchContext,
					DossierDisplayTerms.WARD_NAME, term);
			TermQuery termContactName = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.CONTACT_NAME, term);
			TermQuery termContactTelNo = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.CONTACT_TEL_NO, term);
			TermQuery termContactEmail = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.CONTACT_EMAIL, term);
			TermQuery termNote = TermQueryFactoryUtil.create(searchContext,
					DossierDisplayTerms.NOTE, term);
			TermQuery termDossierId = TermQueryFactoryUtil.create(
					searchContext, DossierDisplayTerms.DOSSIER_ID, term);

			try {
				query.add(termReceptionNo, BooleanClauseOccur.SHOULD);
				query.add(termCityName, BooleanClauseOccur.SHOULD);
				query.add(termExternalRefNo, BooleanClauseOccur.SHOULD);
				query.add(termExternalRef, BooleanClauseOccur.SHOULD);
				query.add(termGovAgencyName, BooleanClauseOccur.SHOULD);
				query.add(termSubjectName, BooleanClauseOccur.SHOULD);
				query.add(termAddress, BooleanClauseOccur.SHOULD);
				query.add(termDistrictName, BooleanClauseOccur.SHOULD);
				query.add(termWardName, BooleanClauseOccur.SHOULD);
				query.add(termContactName, BooleanClauseOccur.SHOULD);
				query.add(termContactEmail, BooleanClauseOccur.SHOULD);
				query.add(termContactTelNo, BooleanClauseOccur.SHOULD);
				query.add(termNote, BooleanClauseOccur.SHOULD);
				query.add(termDossierId, BooleanClauseOccur.SHOULD);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		return query;
	}
}
