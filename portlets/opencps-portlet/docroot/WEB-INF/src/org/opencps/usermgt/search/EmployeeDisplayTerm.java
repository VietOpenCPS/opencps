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

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author trungnt
 *
 */

public class EmployeeDisplayTerm extends DisplayTerms {

	public static final String BIRTH_DATE = "birthDate";
	public static final String BIRTH_DATE_DAY = "birthDateDay";
	public static final String BIRTH_DATE_MONTH = "birthDateMonth";
	public static final String BIRTH_DATE_YEAR = "birthDateYear";
	public static final String COMPANY_ID = "companyId";
	public static final String CREATE_DATE = "createDate";
	public static final String EMAIL = "email";
	public static final String EMPLOYEE_ID = "employeeId";
	public static final String EMPLOYEE_NO = "employeeNo";
	public static final String FULL_NAME = "fullName";
	public static final String GENDER = "gender";
	public static final String GROUP_ID = "groupId";
	public static final String IS_CHANGE_PASS_WORD = "changePassWord";
	public static final String JOBPOS_ID = "jobPosId";
	public static final String MAIN_JOBPOS_ID = "mainJobPosId";
	public static final String MOBILE = "mobile";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String OLD_PASS_WORD = "oldPassWord";
	public static final String PASS_WORD = "passWord";
	public static final String RE_PASS_WORD = "rePassWord";
	public static final String SCREEN_NAME = "screenName";
	public static final String TEL_NO = "telNo";
	public static final String USER_EMAIL = "emailAddress";
	public static final String USER_ID = "userId";
	public static final String WORKING_STATUS = "workingStatus";
	public static final String WORKING_UNIT_ID = "workingUnitId";

	public EmployeeDisplayTerm(PortletRequest portletRequest) {
		super(portletRequest);

		employeeId = ParamUtil.getLong(portletRequest, EMPLOYEE_ID);

		createDate = ParamUtil.getDate(portletRequest, CREATE_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));

		birthDate = ParamUtil.getDate(portletRequest, CREATE_DATE,
				DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_FORMAT));

		email = ParamUtil.getString(portletRequest, EMAIL);

		employeeNo = ParamUtil.getString(portletRequest, EMPLOYEE_NO);

		fullName = ParamUtil.getString(portletRequest, FULL_NAME);

		gender = ParamUtil.getInteger(portletRequest, GENDER);

		workingStatus = ParamUtil.getInteger(portletRequest, WORKING_STATUS);

		workingUnitId = ParamUtil.getLong(portletRequest, WORKING_UNIT_ID);

		modifiedDate = ParamUtil.getDate(portletRequest, MODIFIED_DATE,
				DateTimeUtil
						.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		mainJobPosId = ParamUtil.getLong(portletRequest, MAIN_JOBPOS_ID);

		userId = ParamUtil.getLong(portletRequest, USER_ID);

		groupId = setGroupId(portletRequest);
	}

	protected Date birthDate;

	protected Date createDate;

	protected String email;

	protected long employeeId;

	protected String employeeNo;

	protected String fullName;

	protected int gender;

	protected long groupId;

	protected long mainJobPosId;

	protected String mobile;

	protected Date modifiedDate;

	protected String telNo;

	protected long userId;

	protected int workingStatus;

	protected long workingUnitId;

	public Date getBirthDate() {
		return birthDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public String getEmail() {
		return email;
	}
	public long getEmployeeId() {
		return employeeId;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}
	public String getFullName() {
		return fullName;
	}
	public int getGender() {
		return gender;
	}
	public long getGroupId() {
		return groupId;
	}
	public long getMainJobPosId() {
		return mainJobPosId;
	}
	public String getMobile() {
		return mobile;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public String getTelNo() {
		return telNo;
	}
	public long getUserId() {
		return userId;
	}
	public int getWorkingStatus() {
		return workingStatus;
	}
	public long getWorkingUnitId() {
		return workingUnitId;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public long setGroupId(PortletRequest portletRequest) {

		groupId = ParamUtil.getLong(portletRequest, GROUP_ID);

		if (groupId != 0) {
			return groupId;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		return themeDisplay.getScopeGroupId();
	}
	public void setMainJobPosId(long mainJobPosId) {
		this.mainJobPosId = mainJobPosId;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setWorkingStatus(int workingStatus) {
		this.workingStatus = workingStatus;
	}

	public void setWorkingUnitId(long workingUnitId) {
		this.workingUnitId = workingUnitId;
	}
}
