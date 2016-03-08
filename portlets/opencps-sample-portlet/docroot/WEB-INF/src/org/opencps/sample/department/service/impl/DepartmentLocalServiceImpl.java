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

package org.opencps.sample.department.service.impl;

import java.util.Date;

import org.opencps.sample.department.model.Department;
import org.opencps.sample.department.service.base.DepartmentLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * The implementation of the department local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.sample.department.service.DepartmentLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author trungnt
 * @see org.opencps.sample.department.service.base.DepartmentLocalServiceBaseImpl
 * @see org.opencps.sample.department.service.DepartmentLocalServiceUtil
 */
public class DepartmentLocalServiceImpl extends DepartmentLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * org.opencps.sample.department.service.DepartmentLocalServiceUtil} to
	 * access the department local service.
	 */

	/**
	 * <p>
	 * Add department
	 * </p>
	 * 
	 * @param parentId
	 *            is the primary key of the department's parent (optionally
	 *            <code>0</code>)
	 * @param name
	 *            the department's name
	 * @param description
	 *            the department's description (optionally <code>empty</code>)
	 * @param serviceContext
	 *            the service context to be applied. Can receiving the
	 *            scopeGroupId, companyId, userId from serviceContext
	 *            <ul>
	 *            <li>scopeGroupId - groupId of the site</li>
	 *            <li>userId - primary key of the use account</li>
	 *            </ul>
	 * @return the department entity
	 * @throws SystemException
	 *             if a system exception occurred
	 * @throws PortalException
	 *             if the parent folder or file entry could not be found, or if
	 *             the file shortcut's information was invalid
	 */
	public Department addDepartment(long parentId, String name,
			String description, ServiceContext serviceContext)
			throws SystemException, PortalException {
		long departmentId = CounterLocalServiceUtil.increment(Department.class
				.getName());
		Department department = departmentPersistence.create(departmentId);

		Date now = new Date();

		User user = UserLocalServiceUtil.getUser(serviceContext.getUserId());

		department.setGroupId(serviceContext.getScopeGroupId());
		department.setCompanyId(serviceContext.getCompanyId());
		department.setUserId(serviceContext.getUserId());
		department.setUserName(user != null ? user.getScreenName()
				: StringPool.BLANK);
		department.setCreateDate(now);
		department.setModifiedDate(now);

		department.setParentId(parentId);
		department.setName(name);
		department.setDescription(description);

		return departmentPersistence.update(department);
	}

	/**
	 * @param departmentId
	 * @param parentId
	 * @param name
	 * @param description
	 * @param serviceContext
	 * @return
	 */
	public Department updateDepartment(long departmentId, long parentId,
			String name, String description, ServiceContext serviceContext) {
		return null;
	}
}