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

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author trungnt
 */
public class DictCollectionDisplayTerms extends DisplayTerms {

	// public static final String COMPANY_ID = "companyId";

	public static final String COLLECTION_CODE = "collectionCode";

	public static final String COLLECTION_NAME = "collectionName";

	public static final String CREATE_DATE = "createDate";

	public static final String DESCRIPTION = "description";

	public static final String DICTCOLLECTION_ID = "dictCollectionId";

	public static final String GROUP_ID = "groupId";

	public static final String MODIFIED_DATE = "modifiedDate";

	public static final String USER_ID = "userId";

	public DictCollectionDisplayTerms(PortletRequest portletRequest) {
		super(
			portletRequest);

		createDate = ParamUtil
			.getDate(portletRequest, CREATE_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		dictCollectionId = ParamUtil
			.getLong(portletRequest, DICTCOLLECTION_ID, 0L);
		description = ParamUtil
			.getString(portletRequest, DESCRIPTION);
		modifiedDate = ParamUtil
			.getDate(portletRequest, MODIFIED_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		collectionName = ParamUtil
			.getString(portletRequest, COLLECTION_NAME);
		collectionCode = ParamUtil
			.getString(portletRequest, COLLECTION_CODE);
		userId = ParamUtil
			.getLong(portletRequest, USER_ID);

		groupId = setGroupId(portletRequest);
	}

	public long setGroupId(PortletRequest portletRequest) {

		groupId = ParamUtil
			.getLong(portletRequest, GROUP_ID);

		if (groupId != 0) {
			return groupId;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest
			.getAttribute(WebKeys.THEME_DISPLAY);

		return themeDisplay
			.getScopeGroupId();
	}

	public String getCollectionCode() {

		return collectionCode;
	}

	public void setCollectionCode(String collectionCode) {

		this.collectionCode = collectionCode;
	}

	public String getCollectionName() {

		return collectionName;
	}

	public void setCollectionName(String collectionName) {

		this.collectionName = collectionName;
	}

	public Date getCreateDate() {

		return createDate;
	}

	public void setCreateDate(Date createDate) {

		this.createDate = createDate;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public long getDictCollectionId() {

		return dictCollectionId;
	}

	public void setDictCollectionId(long dictCollectionId) {

		this.dictCollectionId = dictCollectionId;
	}

	public Date getModifiedDate() {

		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {

		this.modifiedDate = modifiedDate;
	}

	public long getUserId() {

		return userId;
	}

	public void setUserId(long userId) {

		this.userId = userId;
	}

	public long getGroupId() {

		return groupId;
	}

	protected String collectionCode;
	protected String collectionName;
	protected Date createDate;
	protected String description;
	protected long dictCollectionId;
	protected long groupId;
	protected Date modifiedDate;
	protected long userId;
}
