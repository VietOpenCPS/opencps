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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import org.opencps.dossiermgt.comparator.DossierTemplateNameComparator;
import org.opencps.dossiermgt.comparator.DossierTemplateNoComparator;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.search.DossierTemplateDisplayTerms;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.documentlibrary.model.DLFolder;

/**
 * @author trungnt
 */
public class DossierMgtUtil {

	public static final String TOP_TABS_DOSSIER_TEMPLATE =
	    "top_tabs_dossier_template";
	public static final String TOP_TABS_DOSSIER_PART = "top_tabs_dossier_part";
	public static final String TOP_TABS_SERVICE_CONFIG =
	    "top_tabs_service_config";
	public static final String DOSSIER_PART_TOOLBAR = "dossierPartToolBar";
	public static final String SERVICE_CONFIG_TOOLBAR = "serviceConfigToolBar";

	public static final String TOP_TABS_DOSSIER = "dossier";
	public static final String TOP_TABS_DOSSIER_FILE = "dossier-file";
	public static final String TOP_TABS_EXTERNAL_DOSSIER = "external-dossier";

	public static String[] _DOSSIER_CATEGORY_NAMES = {
	    "update-dossier-info"
	};

	
	/**
	 * @param orderByCol
	 * @param orderByType
	 * @return
	 */
	public static OrderByComparator getDossierTemplateOrderByComparator(
	    String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType
		    .equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator orderByComparator = null;

		if (orderByCol
		    .equals(DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENO)) {
			orderByComparator = new DossierTemplateNoComparator(orderByAsc);
		}
		else if (orderByCol
		    .equals(DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENAME)) {
			orderByComparator = new DossierTemplateNameComparator(orderByAsc);
		}

		return orderByComparator;
	}

	/**
	 * @param partType
	 * @param locale
	 * @return
	 */
	public static String getNameOfPartType(int partType, Locale locale) {

		String partTypeName = StringPool.BLANK;
		switch (partType) {
		case 1:
			partTypeName = LanguageUtil
			    .get(locale, "paper-submited");
			break;
		case 2:
			partTypeName = LanguageUtil
			    .get(locale, "other-papers-group");
			break;
		case 3:
			partTypeName = LanguageUtil
			    .get(locale, "groups-optional");
			break;
		case 4:
			partTypeName = LanguageUtil
			    .get(locale, "own-records");
			break;
		case 5:
			partTypeName = LanguageUtil
			    .get(locale, "papers-results");
			break;
		default:
			partTypeName = LanguageUtil
			    .get(locale, StringPool.BLANK);
			break;
		}

		return partTypeName;
	}

	/**
	 * @param mode
	 * @param locale
	 * @return
	 */
	public static String getNameOfServiceConfigMode(int mode, Locale locale) {

		String modeName = StringPool.BLANK;
		switch (mode) {
		case 0:
			modeName = LanguageUtil
			    .get(locale, "inactive");
			break;
		case 1:
			modeName = LanguageUtil
			    .get(locale, "front-office");
			break;
		case 2:
			modeName = LanguageUtil
			    .get(locale, "back-office");
			break;
		case 3:
			modeName = LanguageUtil
			    .get(locale, "front-back-office");
			break;
		default:
			modeName = LanguageUtil
			    .get(locale, StringPool.BLANK);
			break;
		}

		return modeName;
	}

	/**
	 * @param dossierpartId
	 * @return
	 */
	public static List<DossierPart> getTreeDossierPart(long dossierpartId) {

		List<DossierPart> dossierPartsResult = new ArrayList<DossierPart>();

		Stack<DossierPart> dossierPartsStack = new Stack<DossierPart>();

		try {
			DossierPart dossierPart = DossierPartLocalServiceUtil
			    .getDossierPart(dossierpartId);

			dossierPartsStack
			    .add(dossierPart);

			DossierPart dossierPartIndex = null;

			while (!dossierPartsStack
			    .isEmpty()) {
				dossierPartIndex = dossierPartsStack
				    .pop();

				List<DossierPart> dossierPartsChild =
				    new ArrayList<DossierPart>();
				dossierPartsChild = DossierPartLocalServiceUtil
				    .getDossierPartsByParentId(dossierPartIndex
				        .getDossierpartId());

				if (!dossierPartsChild
				    .isEmpty()) {
					dossierPartsStack
					    .addAll(dossierPartsChild);
				}

				dossierPartsResult
				    .add(dossierPartIndex);
			}
			return dossierPartsResult;
		}
		catch (Exception e) {
			_log
			    .error(e);
		}

		return new ArrayList<DossierPart>();
	}

	private static Log _log = LogFactoryUtil
				    .getLog(DossierMgtUtil.class
				        .getName());
}
