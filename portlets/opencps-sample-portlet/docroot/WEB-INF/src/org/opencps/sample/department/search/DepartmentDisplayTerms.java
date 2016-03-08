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

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author trungnt
 *
 */
public class DepartmentDisplayTerms extends DisplayTerms {

	public static final String CREATE_DATE = "createDate";

	public static final String DEPARTMENT_ID = "departmentId";

	public static final String DESCRIPTION = "description";

	public static final String GROUP_ID = "groupId";

	public static final String MODIFIED_DATE = "modifiedDate";

	public static final String NAME = "name";

	public static final String PARENT_ID = "folderId";

	public static final String USER_NAME = "userName";

	public DepartmentDisplayTerms(PortletRequest portletRequest) {
		super(portletRequest);
		
		createDate = ParamUtil.getDate(portletRequest, CREATE_DATE, PortletUtil.getDateTimeFormat(PortletUtil._VN_DATE_TIME_FORMAT));
		departmentId = ParamUtil.getLong(portletRequest, DEPARTMENT_ID, 0L);
		description = ParamUtil.getString(portletRequest, DESCRIPTION);
		groupId = ParamUtil.getLong(portletRequest, GROUP_ID);
		modifiedDate = ParamUtil.getDate(portletRequest, MODIFIED_DATE, PortletUtil.getDateTimeFormat(PortletUtil._VN_DATE_TIME_FORMAT));
		name = ParamUtil.getString(portletRequest, NAME);
		parentId = ParamUtil.getLong(portletRequest, PARENT_ID, 0L);
		userName = ParamUtil.getString(portletRequest, USER_NAME);
		
		groupId = setGroupId(portletRequest);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public String getDescription() {
		return description;
	}

	public long getGroupId() {
		return groupId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public String getName() {
		return name;
	}

	public long getParentId() {
		return parentId;
	}

	public List<Staff> getStaffs() {
		return staffs;
	}

	public String getUserName() {
		return userName;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
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
