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

package org.opencps.usermgt.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;

/**
 * @author trungnt
 *
 */
public class EmployeeSearchTerm extends EmployeeDisplayTerm {


	public EmployeeSearchTerm(PortletRequest portletRequest) {
		super(portletRequest);
		
		employeeNo = DAOParamUtil.getString(portletRequest,
				EmployeeDisplayTerm.EMPLOYEE_NO);
		workingUnitId = DAOParamUtil.getLong(portletRequest,
				EmployeeDisplayTerm.WORKING_UNIT_ID);
		fullName = DAOParamUtil.getString(portletRequest,
				EmployeeDisplayTerm.FULL_NAME);

		workingUnitStatus = DAOParamUtil.getInteger(portletRequest,
				EmployeeDisplayTerm.WORKING_STATUS);

	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public String getFullName() {
		return fullName;
	}

	public long getWorkingUnitId() {
		return workingUnitId;
	}

	public int getWorkingUnitStatus() {
		return workingUnitStatus;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setWorkingUnitId(long workingUnitId) {
		this.workingUnitId = workingUnitId;
	}

	public void setWorkingUnitStatus(int workingUnitStatus) {
		this.workingUnitStatus = workingUnitStatus;
	}
	
	protected String employeeNo;

	protected String fullName;

	protected long workingUnitId;

	protected int workingUnitStatus;

}
