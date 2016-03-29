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
public class DictItemDisplayTerms extends DisplayTerms {

	public static final String CREATE_DATE = "createDate";

	public static final String DESCRIPTION = "description";

	public static final String DICTCOLLECTION_ID = "dictCollectionId";

	public static final String DICTITEM_ID = "dictItemId";

	public static final String DICTVERSION_ID = "dictVersionId";

	public static final String GROUP_ID = "groupId";

	public static final String ISSUE_STATUS = "issueStatus";

	public static final String ITEM_CODE = "itemCode";

	public static final String ITEM_NAME = "itemName";

	public static final String MODIFIED_DATE = "modifiedDate";

	public static final String PARENTITEM_ID = "parentItemId";

	public static final String TREEINDEX = "treeIndex";

	public static final String USER_ID = "userId";

	public DictItemDisplayTerms(PortletRequest portletRequest) {
		super(
			portletRequest);

		createDate = ParamUtil
			.getDate(portletRequest, CREATE_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		dictCollectionId = ParamUtil
			.getLong(portletRequest, DICTCOLLECTION_ID, 0L);
		dictItemId = ParamUtil
			.getLong(portletRequest, DICTITEM_ID, 0L);
		description = ParamUtil
			.getString(portletRequest, DESCRIPTION);
		modifiedDate = ParamUtil
			.getDate(portletRequest, MODIFIED_DATE, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		itemName = ParamUtil
			.getString(portletRequest, ITEM_NAME);
		itemCode = ParamUtil
			.getString(portletRequest, ITEM_CODE);
		userId = ParamUtil
			.getLong(portletRequest, USER_ID);
		treeIndex = ParamUtil
			.getString(portletRequest, TREEINDEX);
		issueStatus = ParamUtil
			.getInteger(portletRequest, ISSUE_STATUS);
		dictVersionId = ParamUtil
			.getLong(portletRequest, DICTVERSION_ID);
		parentItemId = ParamUtil
			.getLong(portletRequest, PARENTITEM_ID);
		groupId = setGroupId(portletRequest);
	}

	public Date getCreateDate() {

		return createDate;
	}

	public String getDescription() {

		return description;
	}

	public long getDictCollectionId() {

		return dictCollectionId;
	}

	public long getDictItemId() {

		return dictItemId;
	}

	public long getDictVersionId() {

		return dictVersionId;
	}

	public long getGroupId() {

		return groupId;
	}

	public int getIssueStatus() {

		return issueStatus;
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

	public long getParentItemId() {

		return parentItemId;
	}

	public String getTreeIndex() {

		return treeIndex;
	}

	public long getUserId() {

		return userId;
	}

	public void setCreateDate(Date createDate) {

		this.createDate = createDate;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public void setDictCollectionId(long dictCollectionId) {

		this.dictCollectionId = dictCollectionId;
	}

	public void setDictItemId(long dictItemId) {

		this.dictItemId = dictItemId;
	}

	public void setDictVersionId(long dictVersionId) {

		this.dictVersionId = dictVersionId;
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

	public void setIssueStatus(int issueStatus) {

		this.issueStatus = issueStatus;
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

	public void setParentItemId(long parentItemId) {

		this.parentItemId = parentItemId;
	}

	public void setTreeIndex(String treeIndex) {

		this.treeIndex = treeIndex;
	}

	public void setUserId(long userId) {

		this.userId = userId;
	}

	protected Date createDate;
	protected String description;
	protected long dictCollectionId;
	protected long dictItemId;
	protected long dictVersionId;
	protected long groupId;
	protected int issueStatus;
	protected String itemCode;
	protected String itemName;
	protected Date modifiedDate;
	protected long parentItemId;
	protected String treeIndex;
	protected long userId;
}
