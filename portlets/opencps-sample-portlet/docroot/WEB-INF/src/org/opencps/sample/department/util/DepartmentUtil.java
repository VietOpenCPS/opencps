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

package org.opencps.sample.department.util;

import org.opencps.sample.department.util.comparator.DepartmentCreateDateComparator;
import org.opencps.sample.department.util.comparator.DepartmentModifiedDateComparator;
import org.opencps.sample.department.util.comparator.DepartmentNameComparator;
import org.opencps.sample.department.util.comparator.DepartmentUserNameComparator;

import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author trungnt
 *
 */
public class DepartmentUtil {

	public static OrderByComparator getDepartmentOrderByComparator(String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator orderByComparator = null;

		if (orderByCol.equals("create-date")) {
			orderByComparator = new DepartmentCreateDateComparator(orderByAsc);
		} else if (orderByCol.equals("modified-date")) {
			orderByComparator = new DepartmentModifiedDateComparator(orderByAsc);
		} else if (orderByCol.equals("name")) {
			orderByComparator = new DepartmentNameComparator(orderByAsc);
		} else if (orderByCol.equals("author")) {
			orderByComparator = new DepartmentUserNameComparator(orderByAsc);
		}

		return orderByComparator;
	}
}
