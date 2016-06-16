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

package org.opencps.util;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.dossiermgt.bean.AccountBean;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;

/**
 * @author trungnt
 */
public class AccountUtil {

	private static AccountBean _accountBean;

	/**
	 * @param accountInstance
	 * @param accountType
	 * @param accountFolder
	 * @param accountRoles
	 * @param accountOrgs
	 * @param ownerUserId
	 * @param ownerOrganizationId
	 */
	public static void initAccount(
		Object accountInstance, String accountType, DLFolder accountFolder,
		List<Role> accountRoles, List<Organization> accountOrgs,
		long ownerUserId, long ownerOrganizationId) {

		AccountBean accountBean = new AccountBean(
			accountInstance, accountType, accountFolder, accountRoles,
			accountOrgs, ownerUserId, ownerOrganizationId);

		AccountUtil
			.setAccountBean(accountBean);
	}

	/**
	 * @param request
	 */
	public static void destroy(HttpServletRequest request) {

		HttpSession session = request
			.getSession();
		session
			.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		session
			.removeAttribute(WebKeys.ACCOUNT_TYPE);
		session
			.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		session
			.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		session
			.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		session
			.removeAttribute(WebKeys.ACCOUNT_ROLES);
		session
			.removeAttribute(WebKeys.CITIZEN_ENTRY);
		session
			.removeAttribute(WebKeys.BUSINESS_ENTRY);
		session
			.removeAttribute(WebKeys.EMPLOYEE_ENTRY);

		request
			.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		request
			.removeAttribute(WebKeys.ACCOUNT_TYPE);
		request
			.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		request
			.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		request
			.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		request
			.removeAttribute(WebKeys.ACCOUNT_ROLES);
		request
			.removeAttribute(WebKeys.CITIZEN_ENTRY);
		request
			.removeAttribute(WebKeys.BUSINESS_ENTRY);
		request
			.removeAttribute(WebKeys.EMPLOYEE_ENTRY);

		setAccountBean(null);

		if (session != null) {
			session
				.invalidate();
		}

		// System.gc();
	}

	/**
	 * @param request
	 * @param sessionInvalidate
	 */
	public static void destroy(
		HttpServletRequest request, boolean sessionInvalidate) {

		HttpSession session = request
			.getSession();
		session
			.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		session
			.removeAttribute(WebKeys.ACCOUNT_TYPE);
		session
			.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		session
			.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		session
			.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		session
			.removeAttribute(WebKeys.ACCOUNT_ROLES);
		session
			.removeAttribute(WebKeys.CITIZEN_ENTRY);
		session
			.removeAttribute(WebKeys.BUSINESS_ENTRY);
		session
			.removeAttribute(WebKeys.EMPLOYEE_ENTRY);
		session
			.removeAttribute(WebKeys.ACCOUNT_BEAN);

		request
			.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		request
			.removeAttribute(WebKeys.ACCOUNT_TYPE);
		request
			.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		request
			.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		request
			.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		request
			.removeAttribute(WebKeys.ACCOUNT_ROLES);
		request
			.removeAttribute(WebKeys.CITIZEN_ENTRY);
		request
			.removeAttribute(WebKeys.BUSINESS_ENTRY);
		request
			.removeAttribute(WebKeys.EMPLOYEE_ENTRY);

		setAccountBean(null);

		if (session != null && sessionInvalidate) {
			session
				.invalidate();
		}

		// System.gc();
	}

	/**
	 * @param userId
	 * @param groupId
	 * @param serviceContext
	 * @return
	 */
	public static AccountBean getAccountBean(
		long userId, long groupId, ServiceContext serviceContext) {

		AccountBean accountBean = null;

		Object accountInstance = null;

		DLFolder accountFolder = null;

		List<Role> accountRoles = new ArrayList<Role>();

		List<Organization> accountOrgs = new ArrayList<Organization>();

		long ownerUserId = 0;

		long ownerOrganizationId = 0;

		String accountType = StringPool.BLANK;

		String dossierDestinationFolder = StringPool.BLANK;

		User user = null;

		try {
			user = UserLocalServiceUtil
				.getUser(userId);
			List<UserGroup> userGroups = user
				.getUserGroups();
			if (userGroups != null) {
				for (UserGroup userGroup : userGroups) {
					if (userGroup
						.getName().equals(
							PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) ||
						userGroup
							.getName().equals(
								PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS) ||
						userGroup
							.getName().equals(
								PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
						accountType = userGroup
							.getName();
						break;
					}

				}
			}

			if (accountType
				.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
				Citizen citizen = CitizenLocalServiceUtil
					.getCitizen(user
						.getUserId());

				accountInstance = citizen;

				ownerUserId = citizen
					.getMappingUserId();

				dossierDestinationFolder = PortletUtil
					.getCitizenDossierDestinationFolder(citizen
						.getGroupId(), citizen
							.getUserId());

			}
			else if (accountType
				.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

				Business business = BusinessLocalServiceUtil
					.getBusiness(user
						.getUserId());

				ownerOrganizationId = business
					.getMappingOrganizationId();

				dossierDestinationFolder = PortletUtil
					.getBusinessDossierDestinationFolder(business
						.getGroupId(), business
							.getMappingOrganizationId());

				accountInstance = business;

			}
			else if (accountType
				.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
				Employee employee = EmployeeLocalServiceUtil
					.getEmployeeByMappingUserId(groupId, userId);

				accountInstance = employee;

				try {
					WorkingUnit workingUnit = WorkingUnitLocalServiceUtil
						.getWorkingUnit(employee
							.getWorkingUnitId());

					ownerOrganizationId = workingUnit
						.getMappingOrganisationId();
				}
				catch (Exception e) {
					_log
						.warn(e
							.getCause());
				}

			}

			if (Validator
				.isNotNull(dossierDestinationFolder)) {
				_log
					.info(dossierDestinationFolder);
				try {

					serviceContext
						.setAddGroupPermissions(true);
					serviceContext
						.setAddGuestPermissions(true);
					accountFolder = DLFolderUtil
						.getTargetFolder(user
							.getUserId(), groupId, groupId, false, 0,
							dossierDestinationFolder, StringPool.BLANK, false,
							serviceContext);

				}
				catch (Exception e) {
					_log
						.warn(e
							.getCause());
				}
			}
		}
		catch (Exception e) {
			_log
				.error(e);
		}
		finally {
			accountBean = new AccountBean(
				accountInstance, accountType, accountFolder, accountRoles,
				accountOrgs, ownerUserId, ownerOrganizationId);
		}

		return accountBean;
	}


	/**
	 * @param request
	 * @return
	 */
	public static AccountBean getAccountBean(HttpServletRequest request) {

		HttpSession session = request
			.getSession();

		AccountBean accountBean = (AccountBean) session
			.getAttribute(WebKeys.ACCOUNT_BEAN);

		return accountBean;
	}

	/**
	 * @param actionRequest
	 * @return
	 */
	public static AccountBean getAccountBean(ActionRequest actionRequest) {

		HttpServletRequest request = PortalUtil
			.getHttpServletRequest(actionRequest);
		HttpSession session = request
			.getSession();

		AccountBean accountBean = (AccountBean) session
			.getAttribute(WebKeys.ACCOUNT_BEAN);

		return accountBean;
	}
	
	public static void setAccountBean(AccountBean accountBean) {

		_accountBean = accountBean;
	}

	public static AccountBean getAccountBean() {

		return _accountBean;
	}

	private static Log _log = LogFactoryUtil
		.getLog(AccountUtil.class
			.getName());
}
