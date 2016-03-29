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
import com.liferay.portal.model.ResourceAction;


public class ResourceActionActionIdComparator extends OrderByComparator{
	public static final String ORDER_BY_ASC = "resourceaction.actionId ASC";
	public static final String ORDER_BY_DESC =
		"resourceaction.actionId DESC";
	
	public static final String[] ORDER_BY_FIELDS = {
		"actionId"
	};
	
public ResourceActionActionIdComparator() {

	this(false);
}


public ResourceActionActionIdComparator(boolean ascending) {

	_ascending = ascending;
}

@Override
public int compare(Object arg0, Object arg1) {

	ResourceAction resourceAction1 = (ResourceAction) arg0;
	ResourceAction resourceAction2 = (ResourceAction) arg1;

	String actionId1 = StringUtil.lowerCase(resourceAction1.getActionId());
	String actionId2 = StringUtil.lowerCase(resourceAction1.getActionId());

	int value = actionId1.compareTo(actionId2);

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
