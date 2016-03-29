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

package org.opencps.datamgt.util;

import org.opencps.datamgt.search.DictCollectionDisplayTerms;
import org.opencps.datamgt.search.DictItemDisplayTerms;
import org.opencps.datamgt.search.DictVersionDisplayTerms;
import org.opencps.datamgt.util.comparator.DictCollectionCodeComparator;
import org.opencps.datamgt.util.comparator.DictCollectionCreateDateComparator;
import org.opencps.datamgt.util.comparator.DictCollectionModifiedDateComparator;
import org.opencps.datamgt.util.comparator.DictCollectionNameComparator;
import org.opencps.datamgt.util.comparator.DictItemCodeComparator;
import org.opencps.datamgt.util.comparator.DictItemCreateDateComparator;
import org.opencps.datamgt.util.comparator.DictItemModifiedDateComparator;
import org.opencps.datamgt.util.comparator.DictItemNameComparator;
import org.opencps.datamgt.util.comparator.DictVersionCreateDateComparator;
import org.opencps.datamgt.util.comparator.DictVersionModifiedDateComparator;
import org.opencps.datamgt.util.comparator.DictVersionValidatedFromComparator;
import org.opencps.datamgt.util.comparator.DictVersionValidatedToComparator;
import org.opencps.datamgt.util.comparator.DictVersionVersionComparator;

import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author trungnt
 */

public class DataMgtUtil {

	public static OrderByComparator getDictCollectionOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator orderByComparator = null;

		if (orderByCol.equals(DictCollectionDisplayTerms.CREATE_DATE)) {
			orderByComparator =
				new DictCollectionCreateDateComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictCollectionDisplayTerms.MODIFIED_DATE)) {
			orderByComparator =
				new DictCollectionModifiedDateComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictCollectionDisplayTerms.COLLECTION_NAME)) {
			orderByComparator = new DictCollectionNameComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictCollectionDisplayTerms.COLLECTION_CODE)) {
			orderByComparator = new DictCollectionCodeComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictCollectionDisplayTerms.USER_ID)) {

		}

		return orderByComparator;
	}

	public static OrderByComparator getDictVersionOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;
		if (orderByType.endsWith("asc")) {
			orderByAsc = true;
		}

		OrderByComparator orderByComparator = null;

		if (orderByCol.equals(DictVersionDisplayTerms.CREATE_DATE)) {
			orderByComparator = new DictVersionCreateDateComparator();
		}
		else if (orderByCol.equals(DictVersionDisplayTerms.MODIFIED_DATE)) {
			orderByComparator = new DictVersionModifiedDateComparator();
		}
		else if (orderByCol.equals(DictVersionDisplayTerms.VALIDATED_FROM)) {
			orderByComparator = new DictVersionValidatedFromComparator();
		}
		else if (orderByCol.equals(DictVersionDisplayTerms.VALIDATED_TO)) {
			orderByComparator = new DictVersionValidatedToComparator();
		}
		else if (orderByCol.equals(DictVersionDisplayTerms.VERSION)) {
			orderByComparator = new DictVersionVersionComparator();
		}
		return orderByComparator;
	}

	public static OrderByComparator getDictItemOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator orderByComparator = null;

		if (orderByCol.equals(DictItemDisplayTerms.CREATE_DATE)) {
			orderByComparator = new DictItemCreateDateComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictItemDisplayTerms.MODIFIED_DATE)) {
			orderByComparator = new DictItemModifiedDateComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictItemDisplayTerms.ITEM_NAME)) {
			orderByComparator = new DictItemNameComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictItemDisplayTerms.ITEM_CODE)) {
			orderByComparator = new DictItemCodeComparator(orderByAsc);
		}
		else if (orderByCol.equals(DictItemDisplayTerms.USER_ID)) {

		}

		return orderByComparator;
	}

	public static final String TOP_TABS_DICTITEM = "dict-item";
	public static final String TOP_TABS_DICTCOLLECTION = "dict-collection";
	public static final String TOP_TABS_DICTVERSION = "dict-version";
}
