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

import org.opencps.usermgt.model.JobPos;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;


public class JobPosLeaderComparator extends OrderByComparator{
	public static final String ORDER_BY_ASC = "opencps_jobpos.leader ASC";
	public static final String ORDER_BY_DESC =
		"opencps__jobpos.leader DESC";
	public static final String[] ORDER_BY_FIELDS = {
		"leader"
	};
	public JobPosLeaderComparator() {

		this(false);
	}

	
	public JobPosLeaderComparator(boolean ascending) {

		_ascending = ascending;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {

		JobPos pos1 = (JobPos) arg0;
		JobPos pos2 = (JobPos) arg1;

		Integer leader1 = pos1.getLeader();
		Integer leader2 = pos2.getLeader();

		int value = leader1.compare(leader1, leader2);

		return (_ascending)? value : -value;
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
