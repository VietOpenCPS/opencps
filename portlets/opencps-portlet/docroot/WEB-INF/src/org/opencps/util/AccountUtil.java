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
import javax.portlet.RenderRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.PortalSessionContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

/**
 * @author trungnt
 */
public class AccountUtil {

	private static AccountBean _accountBean;

	/**
	 * @param request
	 * @return
	 */
	public static AccountBean initAccount(HttpServletRequest request) {

		HttpSession session = request.getSession();

		try {
			initAccount(request, null);

		}
		catch (Exception e) {
			_log.error(e);
		}

		return (AccountBean) session.getAttribute(org.opencps.util.WebKeys.ACCOUNT_BEAN);
	}

	/**
	 * @param request
	 * @param response
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void initAccount(
		HttpServletRequest request, HttpServletResponse response)
		throws PortalException, SystemException {

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute(WebKeys.USER);

		if (user == null) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

			if (themeDisplay != null) {
				user = themeDisplay.getUser();
			}
		}

		// long companyId = PortalUtil.getCompanyId(request);

		// long scopeGroupId = PortalUtil.getScopeGroupId(request);

		ServiceContext serviceContext =
			ServiceContextFactory.getInstance(request);

		Object accountInstance = null;

		AccountBean accountBean =
			(AccountBean) session.getAttribute(org.opencps.util.WebKeys.ACCOUNT_BEAN);

		String accountType =
			GetterUtil.getString(session.getAttribute(org.opencps.util.WebKeys.ACCOUNT_TYPE));

		Citizen citizen =
			(Citizen) session.getAttribute(org.opencps.util.WebKeys.CITIZEN_ENTRY);

		Business business =
			(Business) session.getAttribute(org.opencps.util.WebKeys.BUSINESS_ENTRY);

		Employee employee =
			(Employee) session.getAttribute(org.opencps.util.WebKeys.EMPLOYEE_ENTRY);

		ArrayList<Role> accountRoles =
			(ArrayList<Role>) session.getAttribute(org.opencps.util.WebKeys.ACCOUNT_ROLES);

		ArrayList<Organization> accountOrgs =
			(ArrayList<Organization>) session.getAttribute(org.opencps.util.WebKeys.ACCOUNT_ORGANIZATION);

		long ownerUserId =
			GetterUtil.getLong(
				session.getAttribute(org.opencps.util.WebKeys.ACCOUNT_OWNERUSERID),
				0L);

		long ownerOrganizationId =
			GetterUtil.getLong(
				session.getAttribute(org.opencps.util.WebKeys.ACCOUNT_OWNERORGANIZATIONID),
				0L);

		if (!user.isDefaultUser() && Validator.isNull(accountBean)) {

			try {

				// Clean account bean
				AccountUtil.destroy(request, false);

				accountBean =
					AccountUtil.getAccountBean(
						user.getUserId(), serviceContext.getScopeGroupId(),
						serviceContext);

				if (accountBean != null) {

					accountType = accountBean.getAccountType();

					if (accountBean.isBusiness()) {
						business = (Business) accountBean.getAccountInstance();
						accountInstance = business;
					}
					else if (accountBean.isCitizen()) {
						citizen = (Citizen) accountBean.getAccountInstance();
						accountInstance = citizen;
					}
					else if (accountBean.isEmployee()) {
						employee = (Employee) accountBean.getAccountInstance();
						accountInstance = employee;
					}

					ownerOrganizationId = accountBean.getOwnerOrganizationId();
					ownerUserId = accountBean.getOwnerUserId();

					accountOrgs = accountBean.getAccountOrgs();
					accountRoles = accountBean.getAccountRoles();

					request.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_TYPE, accountType);
					request.setAttribute(
						org.opencps.util.WebKeys.CITIZEN_ENTRY, citizen);
					request.setAttribute(
						org.opencps.util.WebKeys.BUSINESS_ENTRY, business);

					request.setAttribute(
						org.opencps.util.WebKeys.EMPLOYEE_ENTRY, employee);

					request.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_OWNERORGANIZATIONID,
						ownerOrganizationId);

					request.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_OWNERUSERID,
						ownerUserId);

					request.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_ROLES, accountRoles);

					request.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_ORGANIZATION,
						accountOrgs);

					// Store session

					session.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_TYPE, accountType);
					session.setAttribute(
						org.opencps.util.WebKeys.CITIZEN_ENTRY, citizen);
					session.setAttribute(
						org.opencps.util.WebKeys.BUSINESS_ENTRY, business);

					session.setAttribute(
						org.opencps.util.WebKeys.EMPLOYEE_ENTRY, employee);

					session.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_OWNERORGANIZATIONID,
						ownerOrganizationId);

					session.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_OWNERUSERID,
						ownerUserId);

					session.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_ROLES, accountRoles);

					session.setAttribute(
						org.opencps.util.WebKeys.ACCOUNT_ORGANIZATION,
						accountOrgs);

				}
				else {
					_log.info(AccountUtil.class.getName() +
						": ########################################: AccountBean is null");
				}

			}
			catch (Exception e) {
				_log.error(e);
			}
			finally {
				accountBean =
					new AccountBean(
						accountInstance, accountType, accountRoles,
						accountOrgs, ownerUserId, ownerOrganizationId);

				session.setAttribute(
					org.opencps.util.WebKeys.ACCOUNT_BEAN, accountBean);

				_log.info(AccountUtil.class.getName() +
					": ########################################: AccountType=" +
					accountBean.getAccountType() + " OwnerUserId=" +
					accountBean.getOwnerUserId() + " OwnerOrganizationId=" +
					accountBean.getOwnerOrganizationId());
			}

		}
	}

	/**
	 * @param accountInstance
	 * @param accountType
	 * @param accountRoles
	 * @param accountOrgs
	 * @param ownerUserId
	 * @param ownerOrganizationId
	 */
	public static void initAccount(
		Object accountInstance, String accountType,
		ArrayList<Role> accountRoles, ArrayList<Organization> accountOrgs,
		long ownerUserId, long ownerOrganizationId) {

		AccountBean accountBean =
			new AccountBean(
				accountInstance, accountType, accountRoles, accountOrgs,
				ownerUserId, ownerOrganizationId);

		AccountUtil.setAccountBean(accountBean);
	}

	/**
	 * @param request
	 */
	public static void destroy(HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		session.removeAttribute(WebKeys.ACCOUNT_TYPE);
		session.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		session.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		session.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		session.removeAttribute(WebKeys.ACCOUNT_ROLES);
		session.removeAttribute(WebKeys.CITIZEN_ENTRY);
		session.removeAttribute(WebKeys.BUSINESS_ENTRY);
		session.removeAttribute(WebKeys.EMPLOYEE_ENTRY);
		session.removeAttribute(WebKeys.ACCOUNT_BEAN);

		request.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		request.removeAttribute(WebKeys.ACCOUNT_TYPE);
		request.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		request.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		request.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		request.removeAttribute(WebKeys.ACCOUNT_ROLES);
		request.removeAttribute(WebKeys.CITIZEN_ENTRY);
		request.removeAttribute(WebKeys.BUSINESS_ENTRY);
		request.removeAttribute(WebKeys.EMPLOYEE_ENTRY);
		request.removeAttribute(WebKeys.ACCOUNT_BEAN);

		setAccountBean(null);

		if (session != null) {
			session.invalidate();
		}

		// System.gc();
	}

	/**
	 * @param request
	 * @param sessionInvalidate
	 */
	public static void destroy(
		HttpServletRequest request, boolean sessionInvalidate) {

		HttpSession session = request.getSession();
		session.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		session.removeAttribute(WebKeys.ACCOUNT_TYPE);
		session.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		session.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		session.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		session.removeAttribute(WebKeys.ACCOUNT_ROLES);
		session.removeAttribute(WebKeys.CITIZEN_ENTRY);
		session.removeAttribute(WebKeys.BUSINESS_ENTRY);
		session.removeAttribute(WebKeys.EMPLOYEE_ENTRY);
		session.removeAttribute(WebKeys.ACCOUNT_BEAN);

		request.removeAttribute(WebKeys.ACCOUNT_FOLDER);
		request.removeAttribute(WebKeys.ACCOUNT_TYPE);
		request.removeAttribute(WebKeys.ACCOUNT_ORGANIZATION);
		request.removeAttribute(WebKeys.ACCOUNT_OWNERORGANIZATIONID);
		request.removeAttribute(WebKeys.ACCOUNT_OWNERUSERID);
		request.removeAttribute(WebKeys.ACCOUNT_ROLES);
		request.removeAttribute(WebKeys.CITIZEN_ENTRY);
		request.removeAttribute(WebKeys.BUSINESS_ENTRY);
		request.removeAttribute(WebKeys.EMPLOYEE_ENTRY);
		request.removeAttribute(WebKeys.ACCOUNT_BEAN);

		setAccountBean(null);

		if (session != null && sessionInvalidate) {
			session.invalidate();
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

		ArrayList<Role> accountRoles = new ArrayList<Role>();

		ArrayList<Organization> accountOrgs = new ArrayList<Organization>();

		long ownerUserId = 0;

		long ownerOrganizationId = 0;

		String accountType = StringPool.BLANK;

		User user = null;

		try {

			user = UserLocalServiceUtil.getUser(userId);

			accountRoles.addAll(RoleLocalServiceUtil.getUserRoles(user.getUserId()));

			accountOrgs.addAll(OrganizationLocalServiceUtil.getUserOrganizations(user.getUserId()));

			List<UserGroup> userGroups = user.getUserGroups();
			if (userGroups != null) {
				for (UserGroup userGroup : userGroups) {
					if (userGroup.getName().equals(
						PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) ||
						userGroup.getName().equals(
							PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS) ||
						userGroup.getName().equals(
							PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
						accountType = userGroup.getName();

						break;
					}

				}
			}

			if (accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
				Citizen citizen =
					CitizenLocalServiceUtil.getCitizen(user.getUserId());

				accountInstance = citizen;

				ownerUserId = citizen.getMappingUserId();

			}
			else if (accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

				Business business =
					BusinessLocalServiceUtil.getBusiness(user.getUserId());

				ownerOrganizationId = business.getMappingOrganizationId();

				accountInstance = business;

			}
			else if (accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
				Employee employee =
					EmployeeLocalServiceUtil.getEmployeeByMappingUserId(
						groupId, userId);

				accountInstance = employee;

				try {
					WorkingUnit workingUnit =
						WorkingUnitLocalServiceUtil.getWorkingUnit(employee.getWorkingUnitId());

					ownerOrganizationId =
						workingUnit.getMappingOrganisationId();
				}
				catch (Exception e) {
					_log.warn(e.getCause());
				}

			}

		}
		catch (Exception e) {
			_log.error(e);
		}
		finally {
			accountBean =
				new AccountBean(
					accountInstance, accountType, accountRoles, accountOrgs,
					ownerUserId, ownerOrganizationId);
		}

		return accountBean;
	}

	/**
	 * @param request
	 * @return
	 */
	public static AccountBean getAccountBean(HttpServletRequest request) {

		HttpSession session = request.getSession();

		AccountBean accountBean =
			(AccountBean) session.getAttribute(WebKeys.ACCOUNT_BEAN);

		return accountBean;
	}

	/**
	 * @param actionRequest
	 * @return
	 */
	public static AccountBean getAccountBean(ActionRequest actionRequest) {

		HttpServletRequest request =
			PortalUtil.getHttpServletRequest(actionRequest);
		HttpSession session = request.getSession();

		AccountBean accountBean = null;

		try {
			accountBean =
				(AccountBean) session.getAttribute(WebKeys.ACCOUNT_BEAN);
		}
		catch (Exception e) {

			//_log.error(e);
			AccountUtil.destroy(request, false);
			// reInitAccount
			accountBean = initAccount(request);
		}

		return accountBean;
	}

	/**
	 * @param renderRequest
	 * @return
	 */
	public static AccountBean getAccountBean(RenderRequest renderRequest) {

		HttpServletRequest request =
			PortalUtil.getHttpServletRequest(renderRequest);

		HttpSession session =
			PortalSessionContext.get(request.getRequestedSessionId());

		AccountBean accountBean = null;

		try {
			accountBean =
				(AccountBean) session.getAttribute(WebKeys.ACCOUNT_BEAN);
		}
		catch (Exception e) {

			//_log.error(e);
			AccountUtil.destroy(request, false);
			// reInitAccount
			accountBean = initAccount(request);
		}

		return accountBean;
	}

	/**
	 * @param actionRequest
	 * @return
	 */
	public static AccountBean getAccountBeanFromAttribute(
		ActionRequest actionRequest) {

		HttpServletRequest request =
			PortalUtil.getHttpServletRequest(actionRequest);

		ServletContext servletContext = request.getServletContext();

		AccountBean accountBean = null;

		try {
			accountBean =
				(AccountBean) servletContext.getAttribute(WebKeys.ACCOUNT_BEAN);
		}
		catch (Exception e) {

			//_log.error(e);
			AccountUtil.destroy(request, false);
			// reInitAccount
			accountBean = initAccount(request);
		}

		return accountBean;
	}

	/**
	 * @param renderRequest
	 * @return
	 */
	public static AccountBean getAccountBeanFromAttribute(
		RenderRequest renderRequest) {

		HttpServletRequest request =
			PortalUtil.getHttpServletRequest(renderRequest);

		ServletContext servletContext = request.getServletContext();

		AccountBean accountBean = null;

		try {
			accountBean =
				(AccountBean) servletContext.getAttribute(WebKeys.ACCOUNT_BEAN);
		}
		catch (Exception e) {

			//_log.error(e);
			AccountUtil.destroy(request, false);
			// reInitAccount
			accountBean = initAccount(request);
		}

		return accountBean;
	}

	public static void setAccountBean(AccountBean accountBean) {

		_accountBean = accountBean;
	}

	public static AccountBean getAccountBean() {

		return _accountBean;
	}

	private static Log _log =
		LogFactoryUtil.getLog(AccountUtil.class.getName());
}
