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

package org.opencps.sample.department.search;

import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;

import org.opencps.sample.staff.model.Staff;
import org.opencps.sample.utils.PortletUtil;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 *
 */
public class DepartmentSearchTerms extends DepartmentDisplayTerms {
	public DepartmentSearchTerms(PortletRequest portletRequest) {
		super(portletRequest);
		
		createDate = ParamUtil.getDate(portletRequest, CREATE_DATE, PortletUtil.getDateTimeFormat(PortletUtil._VN_DATE_TIME_FORMAT));
		departmentId = DAOParamUtil.getLong(portletRequest, DEPARTMENT_ID);
		description = DAOParamUtil.getString(portletRequest, DESCRIPTION);
		modifiedDate = ParamUtil.getDate(portletRequest, MODIFIED_DATE, PortletUtil.getDateTimeFormat(PortletUtil._VN_DATE_TIME_FORMAT));
		name = DAOParamUtil.getString(portletRequest, NAME);
		parentId = DAOParamUtil.getLong(portletRequest, PARENT_ID);
		userName = DAOParamUtil.getString(portletRequest, USER_NAME);
		
		groupId = setGroupId(portletRequest);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<Staff> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	protected Date createDate;

	protected long departmentId;

	protected String description;

	protected long groupId;

	protected Date modifiedDate;

	protected String name;

	protected long parentId;

	protected List<Staff> staffs;

	protected String userName;
}
