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

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.TermQueryFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author trungdk
 */
public class DossierFileSearchUtil {
	public static BooleanQuery buildSearchQuery(String keywords, SearchContext searchContext) {
		BooleanQuery query = BooleanQueryFactoryUtil.create(searchContext);
	
		try {
			query.addTerm(DossierFileDisplayTerms.DISPLAY_NAME, keywords);
			query.addTerm(DossierFileDisplayTerms.FORM_DATA, keywords);
			query.addTerm(DossierFileDisplayTerms.DOSSIER_FILE_NO, keywords);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] terms = StringUtil.split(keywords);
		if (terms.length == 1) {
			TermQuery termDisplayName = TermQueryFactoryUtil.create(searchContext, DossierFileDisplayTerms.DISPLAY_NAME, terms[0] + "*");		
			TermQuery termFormData = TermQueryFactoryUtil.create(searchContext, DossierFileDisplayTerms.FORM_DATA, terms[0]);		
			TermQuery termFileNo = TermQueryFactoryUtil.create(searchContext, DossierFileDisplayTerms.DOSSIER_FILE_NO, terms[0]);		
			
			try {
				query.add(termDisplayName, BooleanClauseOccur.SHOULD);
				query.add(termFormData, BooleanClauseOccur.SHOULD);
				query.add(termFileNo, BooleanClauseOccur.SHOULD);	
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else {
			for (String term : terms) {
				TermQuery termDisplayName = TermQueryFactoryUtil.create(searchContext, DossierFileDisplayTerms.DISPLAY_NAME, term);		
				TermQuery termFormData = TermQueryFactoryUtil.create(searchContext, DossierFileDisplayTerms.FORM_DATA, term);		
				TermQuery termFileNo = TermQueryFactoryUtil.create(searchContext, DossierFileDisplayTerms.DOSSIER_FILE_NO, term);		
				
				try {
					query.add(termDisplayName, BooleanClauseOccur.SHOULD);
					query.add(termFormData, BooleanClauseOccur.SHOULD);
					query.add(termFileNo, BooleanClauseOccur.SHOULD);	
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}							
			}
		}		
		return query;
	}
}
