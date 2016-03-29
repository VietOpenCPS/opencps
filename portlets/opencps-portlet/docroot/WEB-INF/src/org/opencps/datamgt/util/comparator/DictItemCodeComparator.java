/*******************************************************************************
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package org.opencps.datamgt.util.comparator;

import org.opencps.datamgt.model.DictItem;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author trungnt
 */
public class DictItemCodeComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "opencps_dictitem.itemCode ASC";

	public static final String ORDER_BY_DESC = "opencps_dictitem.itemCode DESC";

	public static final String[] ORDER_BY_FIELDS = {
		"itemCode"
	};

	public DictItemCodeComparator() {
		this(
			false);
	}

	public DictItemCodeComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {

		DictItem dictItem1 = (DictItem) obj1;
		DictItem dictItem2 = (DictItem) obj2;
		String code1 = StringUtil
			.toLowerCase(dictItem1
				.getItemCode());
		String code2 = StringUtil
			.toLowerCase(dictItem2
				.getItemCode());

		int value = code1
			.compareTo(code2);

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public String getOrderBy() {

		if (_ascending) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	@Override
	public String[] getOrderByFields() {

		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {

		return _ascending;
	}

	private boolean _ascending;

}
