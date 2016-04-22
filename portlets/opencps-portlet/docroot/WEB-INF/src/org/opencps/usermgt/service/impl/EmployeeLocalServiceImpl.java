/**
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.usermgt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencps.usermgt.NoSuchEmployeeException;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.model.JobPos;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.base.EmployeeLocalServiceBaseImpl;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletUtil;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.portal.model.Website;
import com.liferay.portal.service.ContactLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserServiceUtil;
import com.liferay.portlet.announcements.model.AnnouncementsDelivery;

/**
 * The implementation of the employee local service.
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.usermgt.service.EmployeeLocalService} interface.
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @author trungnt
 * @see org.opencps.usermgt.service.base.EmployeeLocalServiceBaseImpl
 * @see org.opencps.usermgt.service.EmployeeLocalServiceUtil
 */
public class EmployeeLocalServiceImpl extends EmployeeLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.usermgt.service.EmployeeLocalServiceUtil} to access
	 * the employee local service.
	 */

	public Employee addEmployee(long userId, String employeeNo, String fullName,
			int gender, String telNo, String mobile, String email,
			long workingUnitId, int workingStatus, long mainJobPosId,
			long[] jobPosIds, boolean isAddUser, String accountEmail,
			String screenName, int birthDateDay, int birthDateMonth,
			int birthDateYear, String password, String reTypePassword,
			long[] groupIds, long[] userGroupIds, ServiceContext serviceContext)
			throws SystemException, PortalException {

		long employeeId = CounterLocalServiceUtil
				.increment(Employee.class.getName());

		Employee employee = employeePersistence.create(employeeId);

		// Get main JobPos
		JobPos jobPos = null;

		if (mainJobPosId > 0) {
			jobPos = jobPosPersistence.findByPrimaryKey(mainJobPosId);
		}

		// Get Working Unit
		WorkingUnit workingUnit = null;

		if (workingUnitId > 0) {
			workingUnit = workingUnitPersistence
					.findByPrimaryKey(workingUnitId);
		}

		// Get OrganizationId
		long[] organizationIds = null;

		if (workingUnit != null) {
			organizationIds = new long[]{
					workingUnit.getMappingOrganisationId()};
		}

		List<Long> roleIds = new ArrayList<Long>();

		List<Long> distinctJobPosIds = new ArrayList<Long>();

		if (jobPosIds != null && jobPosIds.length > 0) {

			for (int job = 0; job < jobPosIds.length; job++) {
				if (jobPosIds[job] > 0) {
					JobPos jobPosTemp = jobPosPersistence
							.findByPrimaryKey(jobPosIds[job]);
					if (jobPosTemp != null) {
						if (!roleIds.contains(jobPosTemp.getMappingRoleId())) {
							roleIds.add(jobPosTemp.getMappingRoleId());

						}

						if (!distinctJobPosIds
								.contains(jobPosTemp.getJobPosId())) {
							distinctJobPosIds.add(jobPosTemp.getJobPosId());
						}
					}
				}
			}
		}

		Date now = new Date();

		PortletUtil.SplitName spn = PortletUtil.splitName(fullName);

		Date birthDate = DateTimeUtil.getDate(birthDateDay, birthDateMonth,
				birthDateYear);

		User user = null;

		if (isAddUser) {
			user = userService.addUserWithWorkflow(
					serviceContext.getCompanyId(), false, password,
					reTypePassword, false, screenName, accountEmail, 0L,
					StringPool.BLANK, LocaleUtil.getDefault(),
					spn.getFirstName(), spn.getMidName(), spn.getLastName(), 0,
					0, (gender == 1), birthDateMonth, birthDateDay,
					birthDateYear,
					jobPos != null ? jobPos.getTitle() : StringPool.BLANK,
					groupIds, organizationIds, ArrayUtil.toLongArray(roleIds),
					userGroupIds, new ArrayList<Address>(),
					new ArrayList<EmailAddress>(), new ArrayList<Phone>(),
					new ArrayList<Website>(),
					new ArrayList<AnnouncementsDelivery>(), false,
					serviceContext);
		}

		employee.setUserId(userId);
		employee.setGroupId(serviceContext.getScopeGroupId());
		employee.setCompanyId(serviceContext.getCompanyId());
		employee.setCreateDate(now);
		employee.setModifiedDate(now);
		employee.setWorkingUnitId(workingUnitId);
		employee.setEmployeeNo(employeeNo);
		employee.setFullName(fullName);
		employee.setGender(gender);
		employee.setBirthdate(birthDate);
		employee.setTelNo(telNo);
		employee.setMobile(mobile);
		employee.setEmail(email);
		employee.setWorkingStatus(workingStatus);
		employee.setMainJobPosId(mainJobPosId);
		employee.setMappingUserId(user != null ? user.getUserId() : 0);
		employeePersistence.addJobPoses(employeeId,
				ArrayUtil.toLongArray(distinctJobPosIds));

		return employeePersistence.update(employee);
	}

	public int countEmployees(long groupId) throws SystemException {
		return employeePersistence.countByGroupId(groupId);
	}

	public int countEmployees(long groupId, long workingUnitId)
			throws SystemException {
		return employeePersistence.countByG_W(groupId, workingUnitId);
	}

	public int countEmployees(long groupId, String[] fullNames)
			throws SystemException {
		return employeePersistence.countByG_N(groupId, fullNames);
	}

	public int countEmployees(long groupId, String[] fullNames,
			long workingUnitId) throws SystemException {
		return employeePersistence.countByG_N_W(groupId, fullNames,
				workingUnitId);
	}

	public void deletedPermanently(long employeeId)
			throws SystemException, PortalException {
		Employee employee = employeePersistence.findByPrimaryKey(employeeId);
		long mappingUserId = employee.getMappingUserId();
		if (mappingUserId > 0) {
			userLocalService.deleteUser(mappingUserId);
		}

		employeePersistence.remove(employeeId);
	}

	public Employee getEmployeeByEmail(long groupId, String email)
			throws NoSuchEmployeeException, SystemException {
		return employeePersistence.findByG_E(groupId, email);
	}

	public Employee getEmployeeByEmployeeNo(long groupId, String employeeNo)
			throws NoSuchEmployeeException, SystemException {
		return employeePersistence.findByG_ENO(groupId, employeeNo);
	}

	public Employee getEmployeeByMappingUserId(long groupId, long mappingUserId)
			throws NoSuchEmployeeException, SystemException {
		return employeePersistence.findByG_U(groupId, mappingUserId);
	}

	public List<Employee> getEmployees(long groupId) throws SystemException {

		return employeePersistence.findByGroupId(groupId);
	}

	public List<Employee> getEmployees(long groupId, int start, int end,
			OrderByComparator orderByComparator) throws SystemException {

		return employeePersistence.findByGroupId(groupId, start, end,
				orderByComparator);
	}

	public List<Employee> getEmployees(long groupId, long workingUnitId)
			throws SystemException {

		return employeePersistence.findByG_W(groupId, workingUnitId);
	}

	public List<Employee> getEmployees(long groupId, long workingUnitId,
			int start, int end, OrderByComparator orderByComparator)
			throws SystemException {

		return employeePersistence.findByG_W(groupId, workingUnitId, start, end,
				orderByComparator);
	}

	public List<Employee> getEmployees(long groupId, long workingUnitId,
			long mainJobPosId) throws SystemException {

		return employeePersistence.findByG_W_MJP(groupId, workingUnitId,
				mainJobPosId);
	}

	public List<Employee> getEmployeesByMainJobPosId(long mainJobPosId)
			throws SystemException {
		return employeePersistence.findByMainJobPosId(mainJobPosId);
	}

	public List<Employee> getEmployees(long groupId, long workingUnitId,
			long mainJobPosId, int start, int end,
			OrderByComparator orderByComparator) throws SystemException {

		return employeePersistence.findByG_W_MJP(groupId, workingUnitId,
				mainJobPosId, start, end, orderByComparator);
	}
	public List<Employee> getEmployees(long groupId, String[] fullNames)
			throws SystemException {

		return employeePersistence.findByG_N(groupId, fullNames);
	}

	public List<Employee> getEmployees(long groupId, String[] fullNames,
			int start, int end, OrderByComparator orderByComparator)
			throws SystemException {

		return employeePersistence.findByG_N(groupId, fullNames, start, end,
				orderByComparator);
	}

	public List<Employee> getEmployees(long groupId, String[] fullNames,
			long workingUnitId, int start, int end,
			OrderByComparator orderByComparator) throws SystemException {

		return employeePersistence.findByG_N_W(groupId, fullNames,
				workingUnitId, start, end, orderByComparator);
	}

	public void updateProfile(long userId, long employeeId, String fullName,
			int gender, String telNo, String mobile, String email,
			boolean isChangePassWord, int birthDateDay, int birthDateMonth,
			int birthDateYear, String password, String reTypePassword,
			ServiceContext serviceContext)
			throws SystemException, PortalException {

		Employee employee = employeePersistence.findByPrimaryKey(employeeId);

		Date now = new Date();

		PortletUtil.SplitName spn = PortletUtil.splitName(fullName);

		User mappingUser = null;

		Date birthDate = DateTimeUtil.getDate(birthDateDay, birthDateMonth,
				birthDateYear);

		try {
			mappingUser = userLocalService
					.getUserById(employee.getMappingUserId());
		} catch (Exception e) {
			_log.error(e);
		}

		if (mappingUser != null) {
			// Reset password
			if (isChangePassWord) {
				mappingUser = userLocalService.updatePassword(
						mappingUser.getUserId(), password, reTypePassword,
						false);
			}

			// Change user name
			if (!fullName.equals(employee.getFullName())) {
				mappingUser.setFirstName(spn.getFirstName());
				mappingUser.setLastName(spn.getLastName());
				mappingUser.setMiddleName(spn.getMidName());
			}

			mappingUser = userLocalService.updateUser(mappingUser);

			// update birth date
			Contact contact = ContactLocalServiceUtil
					.getContact(mappingUser.getContactId());

			if (contact != null) {
				contact.setBirthday(birthDate);
				contact = ContactLocalServiceUtil.updateContact(contact);
			}
		}
		employee.setUserId(serviceContext.getUserId());
		employee.setGroupId(serviceContext.getScopeGroupId());
		employee.setCompanyId(serviceContext.getCompanyId());
		employee.setModifiedDate(now);
		employee.setFullName(fullName);
		employee.setGender(gender);
		employee.setBirthdate(birthDate);
		employee.setTelNo(telNo);
		employee.setMobile(mobile);
		employeePersistence.update(employee);
	}

	public void updateEmployee(long employeeId, int workingStatus,
			ServiceContext serviceContext)
			throws SystemException, PortalException {

		Employee employee = employeePersistence.findByPrimaryKey(employeeId);

		long mappingUserId = employee.getMappingUserId();

		Date now = new Date();

		if (mappingUserId > 0) {

			if (workingStatus == PortletConstants.WORKING_STATUS_ACTIVATE
					|| workingStatus == PortletConstants.WORKING_STATUS_DEACTIVATE) {

				int status = WorkflowConstants.STATUS_APPROVED;

				if (workingStatus == PortletConstants.WORKING_STATUS_ACTIVATE) {
					status = WorkflowConstants.STATUS_INACTIVE;
				}

				userLocalService.updateStatus(mappingUserId, status);

			}
		}

		workingStatus = workingStatus == PortletConstants.WORKING_STATUS_ACTIVATE
				? 0
				: 1;

		employee.setWorkingStatus(workingStatus);
		employee.setUserId(serviceContext.getUserId());
		employee.setGroupId(serviceContext.getScopeGroupId());
		employee.setCompanyId(serviceContext.getCompanyId());
		employee.setModifiedDate(now);

		employeePersistence.update(employee);

	}

	public Employee updateEmployee(long userId, long employeeId,
			String employeeNo, String fullName, int gender, String telNo,
			String mobile, String email, long workingUnitId, int workingStatus,
			long mainJobPosId, long[] jobPosIds, boolean isAddUser,
			boolean isResetPassWord, String accountEmail, String screenName,
			int birthDateDay, int birthDateMonth, int birthDateYear,
			String password, String reTypePassword, long[] groupIds,
			long[] userGroupIds, ServiceContext serviceContext)
			throws SystemException, PortalException {

		Employee employee = employeePersistence.findByPrimaryKey(employeeId);

		// Get main JobPos
		JobPos jobPos = null;

		if (mainJobPosId > 0) {
			jobPos = jobPosPersistence.findByPrimaryKey(mainJobPosId);
		}

		Date now = new Date();

		PortletUtil.SplitName spn = PortletUtil.splitName(fullName);

		Date birthDate = DateTimeUtil.getDate(birthDateDay, birthDateMonth,
				birthDateYear);

		User mappingUser = null;

		List<Long> roleIds = new ArrayList<Long>();

		List<Long> distinctJobPosIds = new ArrayList<Long>();

		if (jobPosIds != null && jobPosIds.length > 0) {

			for (int job = 0; job < jobPosIds.length; job++) {
				if (jobPosIds[job] > 0) {
					JobPos jobPosTemp = jobPosPersistence
							.findByPrimaryKey(jobPosIds[job]);
					if (jobPosTemp != null) {
						if (!roleIds.contains(jobPosTemp.getMappingRoleId())) {
							roleIds.add(jobPosTemp.getMappingRoleId());

						}

						if (!distinctJobPosIds
								.contains(jobPosTemp.getJobPosId())) {
							distinctJobPosIds.add(jobPosTemp.getJobPosId());
						}
					}
				}
			}
		}

		if (isAddUser) {
			// Get Working Unit
			WorkingUnit workingUnit = null;

			if (workingUnitId > 0) {
				workingUnit = workingUnitPersistence
						.findByPrimaryKey(workingUnitId);
			}

			// Get OrganizationId
			long[] organizationIds = null;

			if (workingUnit != null) {
				organizationIds = new long[]{
						workingUnit.getMappingOrganisationId()};
			}

			mappingUser = userService.addUserWithWorkflow(
					serviceContext.getCompanyId(), false, password,
					reTypePassword, false, screenName, accountEmail, 0L,
					StringPool.BLANK, LocaleUtil.getDefault(),
					spn.getFirstName(), spn.getMidName(), spn.getLastName(), 0,
					0, (gender == 1), birthDateMonth, birthDateDay,
					birthDateYear,
					jobPos != null ? jobPos.getTitle() : StringPool.BLANK,
					groupIds, organizationIds, ArrayUtil.toLongArray(roleIds),
					userGroupIds, new ArrayList<Address>(),
					new ArrayList<EmailAddress>(), new ArrayList<Phone>(),
					new ArrayList<Website>(),
					new ArrayList<AnnouncementsDelivery>(), false,
					serviceContext);
		} else {

			try {
				mappingUser = userLocalService
						.getUserById(employee.getMappingUserId());
			} catch (Exception e) {
				_log.error(e);
			}

			if (mappingUser != null) {
				// Reset password
				if (isResetPassWord) {
					mappingUser = userLocalService.updatePassword(
							mappingUser.getUserId(), password, reTypePassword,
							false);
				}

				// Change user name
				if (!fullName.equals(employee.getFullName())) {
					mappingUser.setFirstName(spn.getFirstName());
					mappingUser.setLastName(spn.getLastName());
					mappingUser.setMiddleName(spn.getMidName());
				}

				// update job title
				if (jobPos != null && !jobPos.getTitle()
						.equals(mappingUser.getJobTitle())) {
					mappingUser.setJobTitle(jobPos.getTitle());
				}

				// userLocalService.setRoleUsers

				for (Long roleId : roleIds) {
					userLocalService.setRoleUsers(roleId,
							new long[]{mappingUser.getUserId()});
				}

				mappingUser = userLocalService.updateUser(mappingUser);

				// update birth date
				Contact contact = ContactLocalServiceUtil
						.getContact(mappingUser.getContactId());

				if (contact != null) {
					contact.setBirthday(birthDate);
					contact = ContactLocalServiceUtil.updateContact(contact);
				}
			}
		}

		// update employee
		employee.setUserId(userId);
		employee.setGroupId(serviceContext.getScopeGroupId());
		employee.setCompanyId(serviceContext.getCompanyId());
		employee.setModifiedDate(now);
		employee.setEmployeeNo(employeeNo);
		employee.setFullName(fullName);
		employee.setGender(gender);
		employee.setBirthdate(birthDate);
		employee.setTelNo(telNo);
		employee.setMobile(mobile);
		employee.setMainJobPosId(mainJobPosId);
		employeePersistence.setJobPoses(employeeId,
				ArrayUtil.toLongArray(distinctJobPosIds));

		if (isAddUser && mappingUser != null) {
			employee.setMappingUserId(mappingUser.getUserId());
		}

		return employeePersistence.update(employee);

	}

	private Log _log = LogFactoryUtil
			.getLog(EmployeeLocalServiceImpl.class.getName());

}
