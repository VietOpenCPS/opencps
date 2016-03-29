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

package org.opencps.datamgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author trungnt
 */
public class DictItemSearchTerms extends DictItemDisplayTerms {

	public DictItemSearchTerms(PortletRequest portletRequest) {
		super(
			portletRequest);

		createDate = ParamUtil
			.getDate(portletRequest, CREATE_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		dictCollectionId = DAOParamUtil
			.getLong(portletRequest, DICTCOLLECTION_ID);
		description = DAOParamUtil
			.getString(portletRequest, DESCRIPTION);
		modifiedDate = ParamUtil
			.getDate(portletRequest, MODIFIED_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		itemCode = DAOParamUtil
			.getString(portletRequest, ITEM_CODE);
		itemName = DAOParamUtil
			.getString(portletRequest, ITEM_NAME);
		userId = DAOParamUtil
			.getLong(portletRequest, USER_ID);

		groupId = setGroupId(portletRequest);
	}

	public Date getCreateDate() {

		return createDate;
	}

	public long getDictCollectionId() {

		return dictCollectionId;
	}

	public long getGroupId() {

		return groupId;
	}

	public String getItemCode() {

		return itemCode;
	}

	public String getItemName() {

		return itemName;
	}

	public Date getModifiedDate() {

		return modifiedDate;
	}

	public long getUserId() {

		return userId;
	}

	public void setCreateDate(Date createDate) {

		this.createDate = createDate;
	}

	public void setDictCollectionId(long dictCollectionId) {

		this.dictCollectionId = dictCollectionId;
	}

	public void setGroupId(long groupId) {

		this.groupId = groupId;
	}

	public void setItemCode(String itemCode) {

		this.itemCode = itemCode;
	}

	public void setItemName(String itemName) {

		this.itemName = itemName;
	}

	public void setModifiedDate(Date modifiedDate) {

		this.modifiedDate = modifiedDate;
	}

	public void setUserId(long userId) {

		this.userId = userId;
	}

	protected Date createDate;

	protected long dictCollectionId;

	protected long groupId;

	protected String itemCode;

	protected String itemName;

	protected Date modifiedDate;

	protected long userId;
}
