/**
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.datamgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author Dunglt
 */

public class DictVersionSearchTerms extends DictVersionDisplayTerms {

	public DictVersionSearchTerms(PortletRequest portletRequest) {
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
		userId = ParamUtil
			.getLong(portletRequest, USER_ID);
		version = ParamUtil
			.getString(portletRequest, VERSION);
		validatedFrom = ParamUtil
			.getDate(portletRequest, VALIDATED_FROM, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		validatedTo = ParamUtil
			.getDate(portletRequest, VALIDATED_TO, DateTimeUtil
				.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		dictVersionId = ParamUtil
			.getLong(portletRequest, DICTVERSION_ID);
		issueStatus = ParamUtil
			.getInteger(portletRequest, ISSUE_STATUS);

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

	public long getDictVersionId() {

		return dictVersionId;
	}

	public long getGroupId() {

		return groupId;
	}

	public int getIssueStatus() {

		return issueStatus;
	}

	public Date getModifiedDate() {

		return modifiedDate;
	}

	public long getUserId() {

		return userId;
	}

	public String getVersion() {

		return version;
	}

	public Date getValidatedFrom() {

		return validatedFrom;
	}

	public Date getValidatedTo() {

		return validatedTo;
	}

	public void setValidatedTo(Date validatedTo) {

		this.validatedTo = validatedTo;
	}

	public void setValidatedFrom(Date validatedFrom) {

		this.validatedTo = validatedFrom;
	}

	public void setVersion(String version) {

		this.version = version;
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

	protected Date createDate;
	protected String description;
	protected long dictCollectionId;
	protected long groupId;
	protected Date modifiedDate;
	protected long userId;
	protected String version;
	protected Date validatedFrom;
	protected Date validatedTo;
	protected long dictVersionId;
	protected int issueStatus;
}
