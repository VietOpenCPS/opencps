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
	
	public static final String TOP_TABS_DOSSIER_TEMPLATE = "top_tabs_dossier_template";
	public static final String TOP_TABS_DOSSIER_PART = "top_tabs_dossier_part";
	public static final String TOP_TABS_SERVICE_CONFIG = "top_tabs_service_config";
	public static final String DOSSIER_PART_TOOLBAR = "dossierPartToolBar";
	public static final String SERVICE_CONFIG_TOOLBAR = "serviceConfigToolBar";
	public static final String TOP_TABS_DOSSIER = "dossier";
	public static final String TOP_TABS_DOSSIER_FILE = "dossier-file";
	public static final String TOP_TABS_EXTERNAL_DOSSIER = "external-dossier";
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
			partTypeName = LanguageUtil.get(locale, "paper-submited");
			break;
		case 2: 
			partTypeName = LanguageUtil.get(locale, "other-papers-group");
			break;
		case 3: 
			partTypeName = LanguageUtil.get(locale, "groups-optional");
			break;
		case 4: 
			partTypeName = LanguageUtil.get(locale, "own-records");
			break;
		case 5: 
			partTypeName = LanguageUtil.get(locale, "papers-results");
			break;	
		default:
			partTypeName = LanguageUtil.get(locale, StringPool.BLANK);
			break;
		}
		
		return partTypeName;
	}
	
	public static String getNameOfServiceConfigMode(int mode, Locale locale) {
		String modeName = StringPool.BLANK;
		switch (mode) {
		case 0: 
			modeName = LanguageUtil.get(locale, "inactive");
			break;
		case 1: 
			modeName = LanguageUtil.get(locale, "front-office");
			break;
		case 2: 
			modeName = LanguageUtil.get(locale, "back-office");
			break;
		case 3: 
			modeName = LanguageUtil.get(locale, "front-back-office");
			break;
		default:
			modeName = LanguageUtil.get(locale, StringPool.BLANK);
			break;
		}
		
		return modeName;
	}

	
	/*public static List<DossierPart> getTreeDossierPart(long dossierpartId, List<DossierPart> dossierParts) throws
	PortalException, SystemException {
		
		DossierPart dossierPart = DossierPartLocalServiceUtil.getDossierPart(dossierpartId);
		
		dossierParts.add(dossierPart);
		
		List<DossierPart> dossierPartsChild = new ArrayList<DossierPart>();
		dossierPartsChild = DossierPartLocalServiceUtil.getDossierPartsByParentId(dossierPart.getParentId());
		
		if(dossierPartsChild.isEmpty()) {
			return dossierParts;
		}
		
		for(DossierPart child : dossierPartsChild) {
			return getTreeDossierPart(child.getDossierpartId(), dossierParts);
		}
		return dossierParts;
			
	}*/
}



	

