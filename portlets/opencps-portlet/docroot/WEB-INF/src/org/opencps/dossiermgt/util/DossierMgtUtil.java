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
package org.opencps.dossiermgt.util;


import java.util.Locale;

import org.opencps.dossiermgt.comparator.DossierTemplateNameComparator;
import org.opencps.dossiermgt.comparator.DossierTemplateNoComparator;
import org.opencps.dossiermgt.search.DossierTemplateDisplayTerms;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;


public class DossierMgtUtil {
	
	public static final String TOP_TABS_DOSSIER_TEMPLATE = "top_tab_dossier_template";
	public static final String TOP_TABS_DOSSIER_PART = "top_tabs_dossier_part";
	public static final String TOP_TABS_DOSSIER_SERVICE = "top_tabs_dossier_service";
	
	public static final String[] _DOSSIER_CATEGORY_NAMES = {
		"dossier-info"
	};
	public static OrderByComparator getDossierTemplateOrderByComparator(
		String orderByCol, String orderByType) {
		
		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}
		
		OrderByComparator orderByComparator = null;
		
		if (orderByCol.equals(DossierTemplateDisplayTerms
			.DOSSIERTEMPLATE_TEMPLATENO)) {
			orderByComparator = new DossierTemplateNoComparator(orderByAsc);
		}
		else if (orderByCol.equals(DossierTemplateDisplayTerms
			.DOSSIERTEMPLATE_TEMPLATENAME)) {
			orderByComparator = new DossierTemplateNameComparator(orderByAsc);
		}
		
		return orderByComparator;
	}
	
	public static String getNameOfPartType(int partType, Locale locale) {
		String partTypeName = StringPool.BLANK;
		switch (partType) {
		case 1: 
			partTypeName = LanguageUtil.get(locale, "paper submited");
			break;
		case 2: 
			partTypeName = LanguageUtil.get(locale, "Other papers group");
			break;
		case 3: 
			partTypeName = LanguageUtil.get(locale, "groups optional");
			break;
		case 4: 
			partTypeName = LanguageUtil.get(locale, "own records");
			break;
		case 5: 
			partTypeName = LanguageUtil.get(locale, "papers results");
			break;	
		default:
			partTypeName = LanguageUtil.get(locale, StringPool.BLANK);
			break;
		}
		
		return partTypeName;
	}
}
