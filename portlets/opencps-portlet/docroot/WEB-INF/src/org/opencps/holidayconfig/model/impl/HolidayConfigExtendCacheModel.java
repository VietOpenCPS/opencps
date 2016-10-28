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

import org.opencps.holidayconfig.model.HolidayConfigExtend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing HolidayConfigExtend in entity cache.
 *
 * @author nhanhoang
 * @see HolidayConfigExtend
 * @generated
 */
public class HolidayConfigExtendCacheModel implements CacheModel<HolidayConfigExtend>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(9);

		sb.append("{holidayExtendId=");
		sb.append(holidayExtendId);
		sb.append(", key=");
		sb.append(key);
		sb.append(", description=");
		sb.append(description);
		sb.append(", active=");
		sb.append(active);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public HolidayConfigExtend toEntityModel() {
		HolidayConfigExtendImpl holidayConfigExtendImpl = new HolidayConfigExtendImpl();

		holidayConfigExtendImpl.setHolidayExtendId(holidayExtendId);

		if (key == null) {
			holidayConfigExtendImpl.setKey(StringPool.BLANK);
		}
		else {
			holidayConfigExtendImpl.setKey(key);
		}

		if (description == null) {
			holidayConfigExtendImpl.setDescription(StringPool.BLANK);
		}
		else {
			holidayConfigExtendImpl.setDescription(description);
		}

		holidayConfigExtendImpl.setActive(active);

		holidayConfigExtendImpl.resetOriginalValues();

		return holidayConfigExtendImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		holidayExtendId = objectInput.readLong();
		key = objectInput.readUTF();
		description = objectInput.readUTF();
		active = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(holidayExtendId);

		if (key == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(key);
		}

		if (description == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeInt(active);
	}

	public long holidayExtendId;
	public String key;
	public String description;
	public int active;
}