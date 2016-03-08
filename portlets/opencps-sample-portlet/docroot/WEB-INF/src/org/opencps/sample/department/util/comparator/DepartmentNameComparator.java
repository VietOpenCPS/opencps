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
package org.opencps.sample.department.util.comparator;

import org.opencps.sample.department.model.Department;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author trungnt
 *
 */
public class DepartmentNameComparator extends OrderByComparator {
	public static final String ORDER_BY_ASC = "opencps_sample.name ASC";

	public static final String ORDER_BY_DESC = "opencps_sample.name DESC";

	public static final String[] ORDER_BY_FIELDS = { "name" };

	public DepartmentNameComparator() {
		this(false);
	}

	public DepartmentNameComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		Department department1 = (Department) obj1;
		Department department2 = (Department) obj2;
		String name1 = StringUtil.toLowerCase(department1.getName());
		String name2 = StringUtil.toLowerCase(department2.getName());

		int value = name1.compareTo(name2);

		if (_ascending) {
			return value;
		} else {
			return -value;
		}
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		} else {
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
