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
package org.opencps.usermgt.search;

import java.util.Date;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

public class WorkingUnitDisplayTerms extends DisplayTerms {
	
	public static final String WORKINGUNIT_MANAGERWORKINGUNITID = "managerWorkingUnitId";
	public static final String WORKINGUNIT_SIBLING = "managerWorkingUnitId";
	public static final String WORKINGUNIT_PARENTWORKINGUNITID = "parentWorkingUnitId";
	public static final String WORKINGUNIT_ID = "workingunitId";
	public static final String WORKINGUNIT_NAME = "name";
	public static final String WORKINGUNIT_ENNAME = "enName";
	public static final String WORKINGUNIT_GOVAGENCYCODE = "govAgencyCode";
	public static final String WORKINGUNIT_ADDRESS = "address";
	public static final String WORKINGUNIT_CITYCODE = "cityCode";
	public static final String WORKINGUNIT_DISTRICTCODE = "districtCode";
	public static final String WORKINGUNIT_WARDCODE = "wardCode";
	public static final String WORKINGUNIT_TELNO = "telNo";
	public static final String WORKINGUNIT_FAXNO = "faxNo";
	public static final String WORKINGUNIT_EMAIL = "email";
	public static final String WORKINGUNIT_WEBSITE = "website";
	public static final String WORKINGUNIT_TREEINDEX = "treeIndex";
	public static final String WORKINGUNIT_ISEMPLOYER = "isEmployer";
	public static final String WORKINGUNIT_CREATEDATE = "createDate";
	public static final String WORKINGUNIT_MODIFIEDDATE = "modifiedDate";
	public static final String GROUP_ID = "groupId";

	public WorkingUnitDisplayTerms(PortletRequest request) {

		super(request);
		
		managerWorkingUnitId = ParamUtil.getLong(request, WORKINGUNIT_MANAGERWORKINGUNITID);
		sibling = ParamUtil.getInteger(request, WORKINGUNIT_SIBLING);
		workingUnitId = ParamUtil.getLong(request, WORKINGUNIT_ID);
		name = ParamUtil.getString(request, WORKINGUNIT_NAME);
		enNamme = ParamUtil.getString(request, WORKINGUNIT_ENNAME);
		govAgencyCode = ParamUtil.getString(request, WORKINGUNIT_GOVAGENCYCODE);
		address = ParamUtil.getString(request, WORKINGUNIT_ADDRESS);
		cityCode = ParamUtil.getString(request, WORKINGUNIT_CITYCODE);
		districtCode = ParamUtil.getString(request, WORKINGUNIT_DISTRICTCODE);
		wardCode = ParamUtil.getString(request, WORKINGUNIT_WARDCODE);
		telNo = ParamUtil.getString(request, WORKINGUNIT_TELNO);
		faxNo = ParamUtil.getString(request, WORKINGUNIT_FAXNO);
		email = ParamUtil.getString(request, WORKINGUNIT_EMAIL);
		website = ParamUtil.getString(request, WORKINGUNIT_WEBSITE);
		treeIndex = ParamUtil.getString(request, WORKINGUNIT_TREEINDEX);
		isEmployer = ParamUtil.getBoolean(request, WORKINGUNIT_ISEMPLOYER);
		groupId = setGroupId(request);

		createDate =
			ParamUtil.getDate(
				request,
				WORKINGUNIT_CREATEDATE,
				DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
		modifiedDate =
			ParamUtil.getDate(
				request,
				WORKINGUNIT_MODIFIEDDATE,
				DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));

		groupId = setGroupId(request);

	}

	public long setGroupId(PortletRequest portletRequest) {

		groupId = ParamUtil.getLong(portletRequest, GROUP_ID);

		if (groupId != 0) {
			return groupId;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);

		return themeDisplay.getScopeGroupId();
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getEnNamme() {

		return enNamme;
	}

	public void setEnNamme(String enNamme) {

		this.enNamme = enNamme;
	}

	public String getGovAgencyCode() {

		return govAgencyCode;
	}

	public void setGovAgencyCode(String govAgencyCode) {

		this.govAgencyCode = govAgencyCode;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getCityCode() {

		return cityCode;
	}

	public void setCityCode(String cityCode) {

		this.cityCode = cityCode;
	}

	public String getDistrictCode() {

		return districtCode;
	}

	public void setDistrictCode(String districtCode) {

		this.districtCode = districtCode;
	}

	public String getWardCode() {

		return wardCode;
	}

	public void setWardCode(String wardCode) {

		this.wardCode = wardCode;
	}

	public String getTelNo() {

		return telNo;
	}

	public void setTelNo(String telNo) {

		this.telNo = telNo;
	}

	public String getFaxNo() {

		return faxNo;
	}

	public void setFaxNo(String faxNo) {

		this.faxNo = faxNo;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getWebsite() {

		return website;
	}

	public void setWebsite(String website) {

		this.website = website;
	}

	public String getTreeIndex() {

		return treeIndex;
	}

	public void setTreeIndex(String treeIndex) {

		this.treeIndex = treeIndex;
	}

	public boolean isEmployer() {

		return isEmployer;
	}

	public void setEmployer(boolean isEmployer) {

		this.isEmployer = isEmployer;
	}

	public Date getCreateDate() {

		return createDate;
	}

	public void setCreateDate(Date createDate) {

		this.createDate = createDate;
	}

	public Date getModifiedDate() {

		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {

		this.modifiedDate = modifiedDate;
	}
	

	public long getWorkingUnitId() {
	
		return workingUnitId;
	}

	
	public void setWorkingUnitId(long workingUnitId) {
	
		this.workingUnitId = workingUnitId;
	}

	public long getParentWorkingUnitId() {
	
		return parentWorkingUnitId;
	}

	
	public void setParentWorkingUnitId(long parentWorkingUnitId) {
	
		this.parentWorkingUnitId = parentWorkingUnitId;
	}


	protected long managerWorkingUnitId;
	
	public long getManagerWorkingUnitId() {
	
		return managerWorkingUnitId;
	}

	
	public void setManagerWorkingUnitId(long managerWorkingUnitId) {
	
		this.managerWorkingUnitId = managerWorkingUnitId;
	}
	
	public int getSibling() {
	
		return sibling;
	}

	
	public void setSibling(int sibling) {
	
		this.sibling = sibling;
	}

	protected int sibling;
	protected long parentWorkingUnitId;
	protected long workingUnitId;
	protected long groupId;
	protected String treeIndex;
	protected String name;
	protected String enNamme;
	protected String govAgencyCode;
	protected String address;
	protected String cityCode;
	protected String districtCode;
	protected String wardCode;
	protected String telNo;
	protected String faxNo;
	protected String email;
	protected String website;
	protected boolean isEmployer;
	protected Date createDate;
	protected Date modifiedDate;
}
