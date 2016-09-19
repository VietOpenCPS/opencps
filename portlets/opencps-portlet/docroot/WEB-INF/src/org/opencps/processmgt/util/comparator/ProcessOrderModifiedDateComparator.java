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
/**
 * 
 */
package org.opencps.processmgt.util.comparator;

import org.opencps.processmgt.model.ProcessOrder;

import com.liferay.portal.kernel.util.OrderByComparator;


/**
 * @author dunglt
 * 
 */
public class ProcessOrderModifiedDateComparator extends OrderByComparator{

	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.util.OrderByComparator#compare(java.lang.Object, java.lang.Object)
	 */
	public static final String ORDER_BY_ASC =
				    "opencps_processorder.modifiedDate ASC";
	public static final String ORDER_BY_DESC =
				    "opencps_processorder.modifiedDate DESC";

	public static final String[] ORDER_BY_FIELDS = {
		"modifiedDate"
	};
	
	public ProcessOrderModifiedDateComparator() {
		this(false);
	}
	
	/**
	 * @param b
	 */
	public ProcessOrderModifiedDateComparator(boolean ascending) {

		_ascending = ascending;
	}

	@Override
	public int compare(Object arg0, Object arg1) {

		ProcessOrder processOrder = (ProcessOrder) arg0;
		ProcessOrder processOrder1 = (ProcessOrder) arg1;
		int value = 0;
		value = (processOrder.getModifiedDate()).compareTo(processOrder1.getModifiedDate());
		
		/*if (value == 0) {
			value = (processOrder.getActionDatetime()).compareTo(processOrder1.getActionDatetime());
		}*/
	    return _ascending ? value : -value;
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
