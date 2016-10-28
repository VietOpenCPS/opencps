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

package org.opencps.holidayconfig.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import org.opencps.holidayconfig.model.HolidayConfig;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing HolidayConfig in entity cache.
 *
 * @author nhanhoang
 * @see HolidayConfig
 * @generated
 */
public class HolidayConfigCacheModel implements CacheModel<HolidayConfig>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{holidayId=");
		sb.append(holidayId);
		sb.append(", holiday=");
		sb.append(holiday);
		sb.append(", description=");
		sb.append(description);
		sb.append(", createdDate=");
		sb.append(createdDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", remove=");
		sb.append(remove);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public HolidayConfig toEntityModel() {
		HolidayConfigImpl holidayConfigImpl = new HolidayConfigImpl();

		holidayConfigImpl.setHolidayId(holidayId);

		if (holiday == Long.MIN_VALUE) {
			holidayConfigImpl.setHoliday(null);
		}
		else {
			holidayConfigImpl.setHoliday(new Date(holiday));
		}

		if (description == null) {
			holidayConfigImpl.setDescription(StringPool.BLANK);
		}
		else {
			holidayConfigImpl.setDescription(description);
		}

		if (createdDate == Long.MIN_VALUE) {
			holidayConfigImpl.setCreatedDate(null);
		}
		else {
			holidayConfigImpl.setCreatedDate(new Date(createdDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			holidayConfigImpl.setModifiedDate(null);
		}
		else {
			holidayConfigImpl.setModifiedDate(new Date(modifiedDate));
		}

		holidayConfigImpl.setRemove(remove);

		holidayConfigImpl.resetOriginalValues();

		return holidayConfigImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		holidayId = objectInput.readLong();
		holiday = objectInput.readLong();
		description = objectInput.readUTF();
		createdDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		remove = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(holidayId);
		objectOutput.writeLong(holiday);

		if (description == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeLong(createdDate);
		objectOutput.writeLong(modifiedDate);
		objectOutput.writeInt(remove);
	}

	public long holidayId;
	public long holiday;
	public String description;
	public long createdDate;
	public long modifiedDate;
	public int remove;
}