/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.holidayconfig.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import org.opencps.holidayconfig.model.HolidayConfigExtend;
import org.opencps.holidayconfig.model.HolidayConfigExtendModel;

import java.io.Serializable;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the HolidayConfigExtend service. Represents a row in the &quot;opencps_holidayconfig_extend&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link org.opencps.holidayconfig.model.HolidayConfigExtendModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link HolidayConfigExtendImpl}.
 * </p>
 *
 * @author nhanhoang
 * @see HolidayConfigExtendImpl
 * @see org.opencps.holidayconfig.model.HolidayConfigExtend
 * @see org.opencps.holidayconfig.model.HolidayConfigExtendModel
 * @generated
 */
public class HolidayConfigExtendModelImpl extends BaseModelImpl<HolidayConfigExtend>
	implements HolidayConfigExtendModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a holiday config extend model instance should use the {@link org.opencps.holidayconfig.model.HolidayConfigExtend} interface instead.
	 */
	public static final String TABLE_NAME = "opencps_holidayconfig_extend";
	public static final Object[][] TABLE_COLUMNS = {
			{ "holidayExtendId", Types.BIGINT },
			{ "key_", Types.VARCHAR },
			{ "description", Types.VARCHAR },
			{ "status", Types.INTEGER },
			{ "companyId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "userId", Types.BIGINT }
		};
	public static final String TABLE_SQL_CREATE = "create table opencps_holidayconfig_extend (holidayExtendId LONG not null primary key,key_ VARCHAR(75) null,description VARCHAR(75) null,status INTEGER,companyId LONG,groupId LONG,userId LONG)";
	public static final String TABLE_SQL_DROP = "drop table opencps_holidayconfig_extend";
	public static final String ORDER_BY_JPQL = " ORDER BY holidayConfigExtend.holidayExtendId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY opencps_holidayconfig_extend.holidayExtendId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.entity.cache.enabled.org.opencps.holidayconfig.model.HolidayConfigExtend"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.finder.cache.enabled.org.opencps.holidayconfig.model.HolidayConfigExtend"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = false;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
				"lock.expiration.time.org.opencps.holidayconfig.model.HolidayConfigExtend"));

	public HolidayConfigExtendModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _holidayExtendId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setHolidayExtendId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _holidayExtendId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return HolidayConfigExtend.class;
	}

	@Override
	public String getModelClassName() {
		return HolidayConfigExtend.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("holidayExtendId", getHolidayExtendId());
		attributes.put("key", getKey());
		attributes.put("description", getDescription());
		attributes.put("status", getStatus());
		attributes.put("companyId", getCompanyId());
		attributes.put("groupId", getGroupId());
		attributes.put("userId", getUserId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long holidayExtendId = (Long)attributes.get("holidayExtendId");

		if (holidayExtendId != null) {
			setHolidayExtendId(holidayExtendId);
		}

		String key = (String)attributes.get("key");

		if (key != null) {
			setKey(key);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}
	}

	@Override
	public long getHolidayExtendId() {
		return _holidayExtendId;
	}

	@Override
	public void setHolidayExtendId(long holidayExtendId) {
		_holidayExtendId = holidayExtendId;
	}

	@Override
	public String getKey() {
		if (_key == null) {
			return StringPool.BLANK;
		}
		else {
			return _key;
		}
	}

	@Override
	public void setKey(String key) {
		_key = key;
	}

	@Override
	public String getDescription() {
		if (_description == null) {
			return StringPool.BLANK;
		}
		else {
			return _description;
		}
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}

	@Override
	public int getStatus() {
		return _status;
	}

	@Override
	public void setStatus(int status) {
		_status = status;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	@Override
	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			HolidayConfigExtend.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public HolidayConfigExtend toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (HolidayConfigExtend)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		HolidayConfigExtendImpl holidayConfigExtendImpl = new HolidayConfigExtendImpl();

		holidayConfigExtendImpl.setHolidayExtendId(getHolidayExtendId());
		holidayConfigExtendImpl.setKey(getKey());
		holidayConfigExtendImpl.setDescription(getDescription());
		holidayConfigExtendImpl.setStatus(getStatus());
		holidayConfigExtendImpl.setCompanyId(getCompanyId());
		holidayConfigExtendImpl.setGroupId(getGroupId());
		holidayConfigExtendImpl.setUserId(getUserId());

		holidayConfigExtendImpl.resetOriginalValues();

		return holidayConfigExtendImpl;
	}

	@Override
	public int compareTo(HolidayConfigExtend holidayConfigExtend) {
		int value = 0;

		if (getHolidayExtendId() < holidayConfigExtend.getHolidayExtendId()) {
			value = -1;
		}
		else if (getHolidayExtendId() > holidayConfigExtend.getHolidayExtendId()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof HolidayConfigExtend)) {
			return false;
		}

		HolidayConfigExtend holidayConfigExtend = (HolidayConfigExtend)obj;

		long primaryKey = holidayConfigExtend.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
	}

	@Override
	public CacheModel<HolidayConfigExtend> toCacheModel() {
		HolidayConfigExtendCacheModel holidayConfigExtendCacheModel = new HolidayConfigExtendCacheModel();

		holidayConfigExtendCacheModel.holidayExtendId = getHolidayExtendId();

		holidayConfigExtendCacheModel.key = getKey();

		String key = holidayConfigExtendCacheModel.key;

		if ((key != null) && (key.length() == 0)) {
			holidayConfigExtendCacheModel.key = null;
		}

		holidayConfigExtendCacheModel.description = getDescription();

		String description = holidayConfigExtendCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			holidayConfigExtendCacheModel.description = null;
		}

		holidayConfigExtendCacheModel.status = getStatus();

		holidayConfigExtendCacheModel.companyId = getCompanyId();

		holidayConfigExtendCacheModel.groupId = getGroupId();

		holidayConfigExtendCacheModel.userId = getUserId();

		return holidayConfigExtendCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(15);

		sb.append("{holidayExtendId=");
		sb.append(getHolidayExtendId());
		sb.append(", key=");
		sb.append(getKey());
		sb.append(", description=");
		sb.append(getDescription());
		sb.append(", status=");
		sb.append(getStatus());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(25);

		sb.append("<model><model-name>");
		sb.append("org.opencps.holidayconfig.model.HolidayConfigExtend");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>holidayExtendId</column-name><column-value><![CDATA[");
		sb.append(getHolidayExtendId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>key</column-name><column-value><![CDATA[");
		sb.append(getKey());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>description</column-name><column-value><![CDATA[");
		sb.append(getDescription());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>status</column-name><column-value><![CDATA[");
		sb.append(getStatus());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = HolidayConfigExtend.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			HolidayConfigExtend.class
		};
	private long _holidayExtendId;
	private String _key;
	private String _description;
	private int _status;
	private long _companyId;
	private long _groupId;
	private long _userId;
	private String _userUuid;
	private HolidayConfigExtend _escapedModel;
}