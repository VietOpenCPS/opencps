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
package org.opencps.usermgt.util.comparator;

import org.opencps.usermgt.model.WorkingUnit;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;


public class WorkingUnitEmailComparator extends OrderByComparator{

	public static final String ORDER_BY_ASC = "opencps_workingunit.email ASC";
	public static final String ORDER_BY_DESC =
		"opencps_workingunit.email DESC";
	public static final String[] ORDER_BY_FIELDS = {
		"email"
	};
	
	public WorkingUnitEmailComparator() {
		this(false);
	}

	public WorkingUnitEmailComparator(boolean ascending) {

		_ascending = ascending;
	}

	@Override
	public int compare(Object arg0, Object arg1) {

		WorkingUnit workingUnit1 = (WorkingUnit) arg0;
		WorkingUnit workingUnit2 = (WorkingUnit) arg1;

		String email1 = StringUtil.lowerCase(workingUnit1.getEmail());
		String email2 = StringUtil.lowerCase(workingUnit2.getEmail());

		int value = email1.compareTo(email2);

		return (_ascending) ? value : -value;
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
