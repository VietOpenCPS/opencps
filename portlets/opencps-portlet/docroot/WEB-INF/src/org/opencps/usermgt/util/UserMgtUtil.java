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

package org.opencps.usermgt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.search.EmployeeDisplayTerm;
import org.opencps.usermgt.search.JobPosDisplayTerms;
import org.opencps.usermgt.search.ResourceActionDisplayTerms;
import org.opencps.usermgt.search.WorkingUnitDisplayTerms;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;
import org.opencps.usermgt.util.comparator.EmployeeNameComparator;
import org.opencps.usermgt.util.comparator.EmployeeWorkingStatusComparator;
import org.opencps.usermgt.util.comparator.JobPosLeaderComparator;
import org.opencps.usermgt.util.comparator.JobPosTitleComparator;
import org.opencps.usermgt.util.comparator.ResourceActionActionIdComparator;
import org.opencps.usermgt.util.comparator.WorkingUnitAdressComporator;
import org.opencps.usermgt.util.comparator.WorkingUnitEmailComparator;
import org.opencps.usermgt.util.comparator.WorkingUnitGovagencyCodeComparator;
import org.opencps.usermgt.util.comparator.WorkingUnitNameComparator;
import org.opencps.usermgt.util.comparator.WorkingUnitSiblingComparator;
import org.opencps.usermgt.util.comparator.WorkingUnitTelNoComparator;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author trungnt
 */
public class UserMgtUtil {

	public static final String TOP_TABS_EMPLOYEE = "employee";
	public static final String TOP_TABS_WORKINGUNIT = "working-unit";
	public static final String[] _EMPLOYESS_CATEGORY_NAMES = {
		"employee-info"
	};
	public static final String[] _WORKING_UNIT_CATEGORY_NAMES = {
		"workingunit-info"
	};
	
	public static final String[] _CITIZEN_CATEGORY_NAMES = {
		"citizen-info"
	};
	
	public static final String[] _PROFILE_CATEGORY_NAMES = {
		"profile-info"
	};
	public static final String[] _JOBPOS_CATEGORY_NAMES = {
		"jobpos-info"
	};

	public static OrderByComparator getWorkingUnitOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}
		OrderByComparator orderByComparator = null;

		if (orderByCol.equals(WorkingUnitDisplayTerms.WORKINGUNIT_NAME)) {
			orderByComparator = new WorkingUnitNameComparator(orderByAsc);
		}
		else if (orderByCol.equals(WorkingUnitDisplayTerms.WORKINGUNIT_ADDRESS)) {
			orderByComparator = new WorkingUnitAdressComporator(orderByAsc);
		}
		else if (orderByCol.equals(WorkingUnitDisplayTerms.WORKINGUNIT_TELNO)) {
			orderByComparator = new WorkingUnitTelNoComparator(orderByAsc);
		}
		else if (orderByCol.equals(WorkingUnitDisplayTerms.WORKINGUNIT_EMAIL)) {
			orderByComparator = new WorkingUnitEmailComparator(orderByAsc);
		}
		else if (orderByCol.equals(WorkingUnitDisplayTerms.WORKINGUNIT_GOVAGENCYCODE)) {
			orderByComparator =
				new WorkingUnitGovagencyCodeComparator(orderByAsc);
		}
		else if ((orderByCol.equals(WorkingUnitDisplayTerms.WORKINGUNIT_SIBLING))) {
			orderByComparator = new WorkingUnitSiblingComparator(orderByAsc);
		}

		return orderByComparator;
	}

	public static OrderByComparator getJobPosOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}
		OrderByComparator orderByComparator = null;

		if (orderByCol.equals(JobPosDisplayTerms.TITLE_JOBPOS)) {
			orderByComparator = new JobPosTitleComparator(orderByAsc);
		}
		else if (orderByCol.equals(JobPosDisplayTerms.LEADER_JOBPOS)) {
			orderByComparator = new JobPosLeaderComparator(orderByAsc);
		}

		return orderByComparator;
	}

	public static OrderByComparator getEmployeeOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}
		OrderByComparator orderByComparator = null;

		if (orderByCol.equals(EmployeeDisplayTerm.FULL_NAME)) {
			orderByComparator = new EmployeeNameComparator(orderByAsc);
		}
		else if (orderByCol.equals(EmployeeDisplayTerm.WORKING_STATUS)) {
			orderByComparator = new EmployeeWorkingStatusComparator(orderByAsc);
		}
		else if (orderByCol.equals(WorkingUnitDisplayTerms.WORKINGUNIT_GOVAGENCYCODE)) {
			orderByComparator =
				new WorkingUnitGovagencyCodeComparator(orderByAsc);
		}

		return orderByComparator;
	}
	
	public static OrderByComparator getResourceOrderByComparator(
		String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}
		OrderByComparator orderByComparator = null;
		
		if (orderByCol.equals(ResourceActionDisplayTerms.RESOURCE_ACTION_NAME)) {
			orderByComparator = new ResourceActionActionIdComparator(orderByAsc);
		}
		
		return orderByComparator;
	}

	public static List<WorkingUnit> getWorkingUnitsForEmployess(
		long groupId, long workingUnitId) {

		List<WorkingUnit> workingUnits = new ArrayList<WorkingUnit>();
		if (workingUnitId > 0) {
			try {
				WorkingUnit workingUnitRoot =
					WorkingUnitLocalServiceUtil.getWorkingUnit(workingUnitId);
				workingUnits.add(workingUnitRoot);
				List<WorkingUnit> childWorkingUnits =
					WorkingUnitLocalServiceUtil.getWorkingUnits(
						groupId, workingUnitId);
				workingUnits.addAll(childWorkingUnits);
			}
			catch (Exception e) {
				_log.warn(e);
			}
		}

		return workingUnits;

	}
	
	public static Stack<Integer> getBinary(long input) {
		Stack<Integer> binaryStack = new Stack<Integer>();
		while(input != 0) {
			binaryStack.push((int)input%2);
			input = input/2;
		}
		return binaryStack;
	}

	private static Log _log =
		LogFactoryUtil.getLog(UserMgtUtil.class.getName());
}
