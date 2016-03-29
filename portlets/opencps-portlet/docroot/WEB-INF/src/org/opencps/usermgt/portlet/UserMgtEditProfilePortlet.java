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

package org.opencps.usermgt.portlet;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.usermgt.DuplicateEmployeeEmailException;
import org.opencps.usermgt.EmptyEmployeeNameException;
import org.opencps.usermgt.OutOfLengthEmployeeEmailException;
import org.opencps.usermgt.OutOfLengthFullNameException;
import org.opencps.usermgt.OutOfLengthMobileException;
import org.opencps.usermgt.OutOfLengthTelNoException;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.model.JobPos;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.search.EmployeeDisplayTerm;
import org.opencps.usermgt.search.WorkingUnitDisplayTerms;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;
import org.opencps.usermgt.service.JobPosLocalServiceUtil;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.WebKeys;

import com.liferay.portal.UserPasswordException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */

public class UserMgtEditProfilePortlet extends MVCPortlet {
	@Override
	public void render(RenderRequest renderRequest,
			RenderResponse renderResponse) throws PortletException, IOException {

		long workingUnitId = ParamUtil.getLong(renderRequest,
				WorkingUnitDisplayTerms.WORKINGUNIT_ID);

		long employeeId = ParamUtil.getLong(renderRequest,
				EmployeeDisplayTerm.EMPLOYEE_ID);

		try {
			if (workingUnitId > 0) {
				WorkingUnit workingUnit = WorkingUnitLocalServiceUtil
						.getWorkingUnit(workingUnitId);
				renderRequest.setAttribute(WebKeys.WORKING_UNIT_ENTRY,
						workingUnit);
			}

			if (employeeId > 0) {
				Employee employee = EmployeeLocalServiceUtil
						.getEmployee(employeeId);

				if (employee != null) {
					long mappingUserId = employee.getMappingUserId();

					if (mappingUserId > 0) {
						User mappingUser = UserLocalServiceUtil
								.getUser(mappingUserId);

						renderRequest.setAttribute(WebKeys.USER_MAPPING_ENTRY,
								mappingUser);
					}

					long mappingWorkingUnitId = employee.getWorkingUnitId();

					if (mappingWorkingUnitId > 0) {
						WorkingUnit mappingWorkingUnit = WorkingUnitLocalServiceUtil
								.getWorkingUnit(mappingWorkingUnitId);

						renderRequest.setAttribute(
								WebKeys.WORKING_UNIT_MAPPING_ENTRY,
								mappingWorkingUnit);

					}

					long mainJobPosId = employee.getMainJobPosId();

					if (mainJobPosId > 0) {
						JobPos mainJobPos = JobPosLocalServiceUtil
								.getJobPos(mainJobPosId);
						renderRequest.setAttribute(WebKeys.MAIN_JOB_POS_ENTRY,
								mainJobPos);
					}
				}

				renderRequest.setAttribute(WebKeys.EMPLOYEE_ENTRY, employee);
			}
		} catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void updateProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) {

		long employeeId = ParamUtil.getLong(actionRequest,
				EmployeeDisplayTerm.EMPLOYEE_ID);
		String email = ParamUtil.getString(actionRequest,
				EmployeeDisplayTerm.EMAIL);

		String fullName = ParamUtil.getString(actionRequest,
				EmployeeDisplayTerm.FULL_NAME);
		String mobile = ParamUtil.getString(actionRequest,
				EmployeeDisplayTerm.MOBILE);
		String telNo = ParamUtil.getString(actionRequest,
				EmployeeDisplayTerm.TEL_NO);
		String oldPassWord = ParamUtil.getString(actionRequest,
				EmployeeDisplayTerm.OLD_PASS_WORD);
		String passWord = ParamUtil.getString(actionRequest,
				EmployeeDisplayTerm.PASS_WORD);
		String rePassWord = ParamUtil.getString(actionRequest,
				EmployeeDisplayTerm.RE_PASS_WORD);
		int gender = ParamUtil.getInteger(actionRequest,
				EmployeeDisplayTerm.GENDER);
		int birthDateDay = ParamUtil.getInteger(actionRequest,
				EmployeeDisplayTerm.BIRTH_DATE_DAY);
		int birthDateMonth = ParamUtil.getInteger(actionRequest,
				EmployeeDisplayTerm.BIRTH_DATE_MONTH);
		int birthDateYear = ParamUtil.getInteger(actionRequest,
				EmployeeDisplayTerm.BIRTH_DATE_YEAR);
		boolean isChangePassWord = ParamUtil.getBoolean(actionRequest,
				EmployeeDisplayTerm.IS_CHANGE_PASS_WORD, false);

		Date birthDate = DateTimeUtil.getDate(birthDateDay, birthDateMonth,
				birthDateYear);
		try {

			ServiceContext serviceContext = ServiceContextFactory
					.getInstance(actionRequest);

			User mappingUser = UserLocalServiceUtil.getUser(serviceContext
					.getUserId());

			// Validate before update
			validate(employeeId, fullName, email, mobile, telNo, birthDate,
					mappingUser.getEmailAddress(), isChangePassWord,
					oldPassWord, passWord, rePassWord, serviceContext);

			EmployeeLocalServiceUtil.updateProfile(serviceContext.getUserId(),
					employeeId, fullName, gender, telNo, mobile,
					mappingUser.getEmailAddress(), isChangePassWord,
					birthDateDay, birthDateMonth, birthDateYear, passWord,
					rePassWord, serviceContext);

		} catch (Exception e) {

			PortalUtil.copyRequestParameters(actionRequest, actionResponse);

			if (e instanceof OutOfLengthEmployeeEmailException) {
				SessionErrors.add(actionRequest,
						OutOfLengthEmployeeEmailException.class);
			} else if (e instanceof EmptyEmployeeNameException) {
				SessionErrors.add(actionRequest,
						EmptyEmployeeNameException.class);
			} else if (e instanceof OutOfLengthFullNameException) {
				SessionErrors.add(actionRequest,
						OutOfLengthFullNameException.class);
			} else if (e instanceof AuthException) {
				SessionErrors.add(actionRequest, AuthException.class);
			} else if (e instanceof UserPasswordException) {
				SessionErrors.add(actionRequest, UserPasswordException.class);
			} else if (e instanceof DuplicateEmployeeEmailException) {
				SessionErrors.add(actionRequest,
						DuplicateEmployeeEmailException.class);
			} else if (e instanceof OutOfLengthMobileException) {
				SessionErrors.add(actionRequest,
						OutOfLengthMobileException.class);
			} else if (e instanceof OutOfLengthTelNoException) {
				SessionErrors.add(actionRequest,
						OutOfLengthTelNoException.class);
			} else {
				SessionErrors.add(actionRequest,

				MessageKeys.USERMGT_SYSTEM_EXCEPTION_OCCURRED);
			}
			_log.error(e);
		}
	}

	protected void validate(long employeeId, String fullName, String email,
			String mobile, String telNo, Date birthDate, String userEmail,
			boolean isChangePassWord, String oldPassWord, String passWord,
			String rePassWord, ServiceContext serviceContext)
			throws OutOfLengthEmployeeEmailException,
			EmptyEmployeeNameException, OutOfLengthFullNameException,
			AuthException, UserPasswordException,
			DuplicateEmployeeEmailException, OutOfLengthMobileException,
			OutOfLengthTelNoException {

		if (email.length() > PortletPropsValues.USERMGT_EMPLOYEE_EMAIL_LENGTH) {
			throw new OutOfLengthEmployeeEmailException();
		}
		if (Validator.isNull(fullName)) {
			throw new EmptyEmployeeNameException();
		}

		if (fullName.length() > PortletPropsValues.USERMGT_EMPLOYEE_FULLNAME_LENGTH) {
			throw new OutOfLengthFullNameException();
		}

		if (mobile.length() > PortletPropsValues.USERMGT_EMPLOYEE_MOBILE_LENGTH) {
			throw new OutOfLengthMobileException();
		}

		if (telNo.length() > PortletPropsValues.USERMGT_EMPLOYEE_TELNO_LENGTH) {
			throw new OutOfLengthTelNoException();
		}

		long userId = 0;
		if (isChangePassWord) {

			try {
				userId = UserLocalServiceUtil.authenticateForBasic(
						serviceContext.getCompanyId(), "emailAddress",
						userEmail, oldPassWord);
			} catch (Exception e) {
				// Nothing todo
			}

			if (userId == 0 || userId != serviceContext.getUserId()) {
				throw new AuthException();
			} else {
				if (passWord.length() > 512) {
					throw new UserPasswordException(
							UserPasswordException.PASSWORD_LENGTH);
				}
			}
		}

		Employee employee = null;

		try {
			employee = EmployeeLocalServiceUtil.getEmployeeByEmail(
					serviceContext.getScopeGroupId(), email);
		} catch (Exception e) {
			// Nothing todo
		}

		if (employee != null && employee.getEmployeeId() != employeeId) {
			throw new DuplicateEmployeeEmailException();
		}

	}

	private Log _log = LogFactoryUtil.getLog(UserMgtEditProfilePortlet.class
			.getName());

}
