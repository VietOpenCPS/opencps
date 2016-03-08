/*******************************************************************************
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package org.opencps.sample.portlet;

import java.io.IOException;

/**
 * @author trungnt
 *
 * Portlet implementation class StaffManagementPortle
 */

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.sample.department.DuplicateDepartmentException;
import org.opencps.sample.department.EmptyDepartmentNameException;
import org.opencps.sample.department.OutOfLengthDepartmentDescriptionException;
import org.opencps.sample.department.OutOfLengthDepartmentNameException;
import org.opencps.sample.department.model.Department;
import org.opencps.sample.department.search.DepartmentDisplayTerms;
import org.opencps.sample.department.service.DepartmentLocalServiceUtil;
import org.opencps.sample.utils.OpenCPSWebkeys;
import org.opencps.sample.utils.PortletPropsValues;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * <p>
 * Add or update department record
 * </p>
 */
public class StaffManagementPortlet extends MVCPortlet {
	public void editDepartment(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException {

		long departmentId = ParamUtil.getLong(actionRequest, DepartmentDisplayTerms.DEPARTMENT_ID);
		long parentId = ParamUtil.getLong(actionRequest, DepartmentDisplayTerms.PARENT_ID);
		
		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
		String name = ParamUtil.getString(actionRequest, DepartmentDisplayTerms.NAME, StringPool.BLANK);
		String description = ParamUtil.getString(actionRequest, DepartmentDisplayTerms.DESCRIPTION, StringPool.BLANK);
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		try {
			// Validate form
			validateDepartment(departmentId, name, description, cmd);

			ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);
			if (cmd.equals(Constants.ADD)) {
				DepartmentLocalServiceUtil.addDepartment(parentId, name, description, serviceContext);
				SessionMessages.add(actionRequest,
						PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_REFRESH_PORTLET,
						actionRequest);
			} else if (cmd.equals(Constants.UPDATE)) {

			} else {
				SessionErrors.add(actionRequest, EmptyDepartmentNameException.class);
			}

		} catch (Exception e) {
			if (e instanceof EmptyDepartmentNameException) {
				SessionErrors.add(actionRequest, EmptyDepartmentNameException.class);
			} else if (e instanceof OutOfLengthDepartmentNameException) {
				SessionErrors.add(actionRequest, EmptyDepartmentNameException.class);
			} else if (e instanceof OutOfLengthDepartmentDescriptionException) {
				SessionErrors.add(actionRequest, OutOfLengthDepartmentDescriptionException.class);
			} else if (e instanceof DuplicateDepartmentException) {
				SessionErrors.add(actionRequest, DuplicateDepartmentException.class);
			} else {

			}
		} finally {
			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL);
			}
		}

	}

	protected void validateDepartment(long departmentId, String name, String description, String cmd)
			throws EmptyDepartmentNameException, DuplicateDepartmentException, OutOfLengthDepartmentNameException,
			OutOfLengthDepartmentDescriptionException {
		if (Validator.isNull(name)) {
			throw new EmptyDepartmentNameException();
		} else {
			if (name.trim().length() > PortletPropsValues._OPENCPS_SAMPLE_DEPARTMENT_NAME_LENGTH) {
				throw new OutOfLengthDepartmentNameException();
			}
		}

		if (description.trim().length() > PortletPropsValues._OPENCPS_SAMPLE_DEPARTMENT_NAME_LENGTH) {
			throw new OutOfLengthDepartmentDescriptionException();
		}

		if (cmd.equals(Constants.UPDATE) && departmentId > 0) {
			boolean isDuplication = false;
			try {
				Department department = DepartmentLocalServiceUtil.getDepartment(departmentId);
				if (name.equalsIgnoreCase(department.getName())) {
					isDuplication = true;
				}
			} catch (PortalException e) {
				_log.error(e);
			} catch (SystemException e) {
				_log.error(e);
			}
			if (isDuplication) {
				throw new DuplicateDepartmentException();
			}
		}

	}

	@Override
	public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		long departmentId = ParamUtil.getLong(request, "departmentId", 0);
		Department department = null;
		if (departmentId > 0) {
			try {
				department = DepartmentLocalServiceUtil.getDepartment(departmentId);
			} catch (Exception e) {
				_log.error(e);
			}
		}

		request.setAttribute(OpenCPSWebkeys.DEPARTMENT, department);
		super.render(request, response);
	}

	private Log _log = LogFactoryUtil.getLog(StaffManagementPortlet.class.getName());
}
