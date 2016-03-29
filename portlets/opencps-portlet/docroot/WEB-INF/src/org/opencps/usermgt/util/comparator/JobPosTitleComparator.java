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


public class JobPosTitleComparator extends OrderByComparator{
	
	public static final String ORDER_BY_ASC = "opencps_jobpos.title ASC";
	public static final String ORDER_BY_DESC =
		"opencps_jobpos.title DESC";
	public static final String[] ORDER_BY_FIELDS = {
		"title"
	};
	public JobPosTitleComparator() {

		this(false);
	}

	
	public JobPosTitleComparator(boolean ascending) {

		_ascending = ascending;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {

		JobPos pos1 = (JobPos) arg0;
		JobPos pos2 = (JobPos) arg1;

		String title1 = StringUtil.lowerCase(pos1.getTitle());
		String title2 = StringUtil.lowerCase(pos2.getTitle());

		int value = title1.compareTo(title2);

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
