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

package org.opencps.taglib.accountmgt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;
import org.opencps.util.AccountUtil;
import org.opencps.util.DLFolderUtil;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.taglib.util.IncludeTag;

/**
 * @author trungnt
 */
public class DefineObjectsTag extends IncludeTag {

	@Override
	public int doStartTag() {

		HttpServletRequest request = (HttpServletRequest) pageContext
		    .getRequest();

		ThemeDisplay themeDisplay = (ThemeDisplay) request
		    .getAttribute(WebKeys.THEME_DISPLAY);

		if (themeDisplay == null) {
			return SKIP_BODY;
		}

		HttpSession session = request
		    .getSession();
		Object accountInstance = null;

		String accountType = GetterUtil
		    .getString(session
		        .getAttribute(org.opencps.util.WebKeys.ACCOUNT_TYPE));

		Citizen citizen = (Citizen) session
		    .getAttribute(org.opencps.util.WebKeys.CITIZEN_ENTRY);

		Business business = (Business) session
		    .getAttribute(org.opencps.util.WebKeys.BUSINESS_ENTRY);

		Employee employee = (Employee) session
		    .getAttribute(org.opencps.util.WebKeys.EMPLOYEE_ENTRY);

		DLFolder accountFolder = (DLFolder) session
		    .getAttribute(org.opencps.util.WebKeys.ACCOUNT_FOLDER);

		List<Role> accountRoles = (List<Role>) session
		    .getAttribute(org.opencps.util.WebKeys.ACCOUNT_ROLES);

		List<Organization> accountOrgs = (List<Organization>) session
		    .getAttribute(org.opencps.util.WebKeys.ACCOUNT_ORGANIZATION);

		long ownerUserId = GetterUtil
		    .getLong(session
		        .getAttribute(org.opencps.util.WebKeys.ACCOUNT_OWNERUSERID),
		        0L);

		long ownerOrganizationId = GetterUtil
		    .getLong(session
		        .getAttribute(
		            org.opencps.util.WebKeys.ACCOUNT_OWNERORGANIZATIONID),
		        0L);

		if (themeDisplay
		    .isSignedIn() && Validator
		        .isNull(accountType)) {
			String dossierDestinationFolder = StringPool.BLANK;
			try {
				
				AccountUtil.destroy(request, false);
				
				List<UserGroup> userGroups = new ArrayList<UserGroup>();

				User user = themeDisplay
				    .getUser();
				userGroups = user
				    .getUserGroups();

				accountRoles = RoleLocalServiceUtil
				    .getUserRoles(user
				        .getUserId());
				request
				    .setAttribute(
				        org.opencps.util.WebKeys.ACCOUNT_TYPE, accountType);

				session
				    .setAttribute(
				        org.opencps.util.WebKeys.ACCOUNT_TYPE, accountType);

				accountOrgs = OrganizationLocalServiceUtil
				    .getUserOrganizations(user
				        .getUserId());

				if (!userGroups
				    .isEmpty()) {
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

				request
				    .setAttribute(
				        org.opencps.util.WebKeys.ACCOUNT_TYPE, accountType);

				session
				    .setAttribute(
				        org.opencps.util.WebKeys.ACCOUNT_TYPE, accountType);

				if (accountType
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN)) {
					citizen = CitizenLocalServiceUtil
					    .getCitizen(user
					        .getUserId());

					accountInstance = citizen;

					dossierDestinationFolder = PortletUtil
					    .getCitizenDossierDestinationFolder(citizen
					        .getGroupId(), citizen
					            .getUserId());
					request
					    .setAttribute(
					        org.opencps.util.WebKeys.CITIZEN_ENTRY, citizen);
					session
					    .setAttribute(
					        org.opencps.util.WebKeys.CITIZEN_ENTRY, citizen);

				}
				else if (accountType
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) {

					business = BusinessLocalServiceUtil
					    .getBusiness(user
					        .getUserId());

					ownerOrganizationId = business
					    .getMappingOrganizationId();

					dossierDestinationFolder = PortletUtil
					    .getBusinessDossierDestinationFolder(business
					        .getGroupId(), business
					            .getMappingOrganizationId());

					accountInstance = citizen;

					request
					    .setAttribute(
					        org.opencps.util.WebKeys.BUSINESS_ENTRY, business);

					session
					    .setAttribute(
					        org.opencps.util.WebKeys.BUSINESS_ENTRY, business);
				}
				else if (accountType
				    .equals(
				        PortletPropsValues.USERMGT_USERGROUP_NAME_EMPLOYEE)) {
					employee = EmployeeLocalServiceUtil
					    .getEmployeeByMappingUserId(themeDisplay
					        .getScopeGroupId(), user
					            .getUserId());

					accountInstance = citizen;

					try {
						WorkingUnit workingUnit = WorkingUnitLocalServiceUtil
						    .getWorkingUnit(employee
						        .getWorkingUnitId());

						ownerOrganizationId = workingUnit
						    .getMappingOrganisationId();
					}
					catch (Exception e) {

					}

					request
					    .setAttribute(
					        org.opencps.util.WebKeys.EMPLOYEE_ENTRY, employee);
					session
					    .setAttribute(
					        org.opencps.util.WebKeys.EMPLOYEE_ENTRY, employee);
				}

				if (Validator
				    .isNotNull(dossierDestinationFolder)) {
					try {
						ServiceContext serviceContext = ServiceContextFactory
						    .getInstance(request);
						serviceContext
						    .setAddGroupPermissions(true);
						serviceContext
						    .setAddGuestPermissions(true);
						accountFolder = DLFolderUtil
						    .getTargetFolder(themeDisplay
						        .getUserId(), themeDisplay
						            .getScopeGroupId(),
						        themeDisplay
						            .getScopeGroupId(),
						        false, 0, dossierDestinationFolder,
						        StringPool.BLANK, false, serviceContext);
						request
						    .setAttribute(
						        org.opencps.util.WebKeys.ACCOUNT_FOLDER,
						        accountFolder);
						session
						    .setAttribute(
						        org.opencps.util.WebKeys.ACCOUNT_FOLDER,
						        accountFolder);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch (Exception e) {
				_log
				    .error(e);
			}
			finally {
				AccountUtil
				    .initAccount(
				        accountInstance, accountType, accountFolder,
				        accountRoles, accountOrgs, ownerUserId,
				        ownerOrganizationId);
			}

		}

		return SKIP_BODY;
	}

	private Log _log = LogFactoryUtil
	    .getLog(DefineObjectsTag.class
	        .getName());
}
