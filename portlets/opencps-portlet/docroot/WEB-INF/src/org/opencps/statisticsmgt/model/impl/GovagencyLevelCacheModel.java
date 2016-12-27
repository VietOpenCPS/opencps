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

package org.opencps.statisticsmgt.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import org.opencps.statisticsmgt.model.GovagencyLevel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing GovagencyLevel in entity cache.
 *
 * @author trungnt
 * @see GovagencyLevel
 * @generated
 */
public class GovagencyLevelCacheModel implements CacheModel<GovagencyLevel>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(17);

		sb.append("{govagencylevel=");
		sb.append(govagencylevel);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", govAgencyCode=");
		sb.append(govAgencyCode);
		sb.append(", administrationLevel=");
		sb.append(administrationLevel);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public GovagencyLevel toEntityModel() {
		GovagencyLevelImpl govagencyLevelImpl = new GovagencyLevelImpl();

		govagencyLevelImpl.setGovagencylevel(govagencylevel);
		govagencyLevelImpl.setCompanyId(companyId);
		govagencyLevelImpl.setGroupId(groupId);
		govagencyLevelImpl.setUserId(userId);

		if (createDate == Long.MIN_VALUE) {
			govagencyLevelImpl.setCreateDate(null);
		}
		else {
			govagencyLevelImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			govagencyLevelImpl.setModifiedDate(null);
		}
		else {
			govagencyLevelImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (govAgencyCode == null) {
			govagencyLevelImpl.setGovAgencyCode(StringPool.BLANK);
		}
		else {
			govagencyLevelImpl.setGovAgencyCode(govAgencyCode);
		}

		govagencyLevelImpl.setAdministrationLevel(administrationLevel);

		govagencyLevelImpl.resetOriginalValues();

		return govagencyLevelImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		govagencylevel = objectInput.readLong();
		companyId = objectInput.readLong();
		groupId = objectInput.readLong();
		userId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		govAgencyCode = objectInput.readUTF();
		administrationLevel = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(govagencylevel);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(userId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (govAgencyCode == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(govAgencyCode);
		}

		objectOutput.writeInt(administrationLevel);
	}

	public long govagencylevel;
	public long companyId;
	public long groupId;
	public long userId;
	public long createDate;
	public long modifiedDate;
	public String govAgencyCode;
	public int administrationLevel;
}