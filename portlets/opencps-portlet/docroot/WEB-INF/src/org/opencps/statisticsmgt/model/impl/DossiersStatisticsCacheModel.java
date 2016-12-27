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

import org.opencps.statisticsmgt.model.DossiersStatistics;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing DossiersStatistics in entity cache.
 *
 * @author trungnt
 * @see DossiersStatistics
 * @generated
 */
public class DossiersStatisticsCacheModel implements CacheModel<DossiersStatistics>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(35);

		sb.append("{dossierStatisticId=");
		sb.append(dossierStatisticId);
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
		sb.append(", remainingNumber=");
		sb.append(remainingNumber);
		sb.append(", receivedNumber=");
		sb.append(receivedNumber);
		sb.append(", ontimeNumber=");
		sb.append(ontimeNumber);
		sb.append(", overtimeNumber=");
		sb.append(overtimeNumber);
		sb.append(", processingNumber=");
		sb.append(processingNumber);
		sb.append(", delayingNumber=");
		sb.append(delayingNumber);
		sb.append(", month=");
		sb.append(month);
		sb.append(", year=");
		sb.append(year);
		sb.append(", govAgencyCode=");
		sb.append(govAgencyCode);
		sb.append(", domainCode=");
		sb.append(domainCode);
		sb.append(", administrationLevel=");
		sb.append(administrationLevel);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DossiersStatistics toEntityModel() {
		DossiersStatisticsImpl dossiersStatisticsImpl = new DossiersStatisticsImpl();

		dossiersStatisticsImpl.setDossierStatisticId(dossierStatisticId);
		dossiersStatisticsImpl.setCompanyId(companyId);
		dossiersStatisticsImpl.setGroupId(groupId);
		dossiersStatisticsImpl.setUserId(userId);

		if (createDate == Long.MIN_VALUE) {
			dossiersStatisticsImpl.setCreateDate(null);
		}
		else {
			dossiersStatisticsImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			dossiersStatisticsImpl.setModifiedDate(null);
		}
		else {
			dossiersStatisticsImpl.setModifiedDate(new Date(modifiedDate));
		}

		dossiersStatisticsImpl.setRemainingNumber(remainingNumber);
		dossiersStatisticsImpl.setReceivedNumber(receivedNumber);
		dossiersStatisticsImpl.setOntimeNumber(ontimeNumber);
		dossiersStatisticsImpl.setOvertimeNumber(overtimeNumber);
		dossiersStatisticsImpl.setProcessingNumber(processingNumber);
		dossiersStatisticsImpl.setDelayingNumber(delayingNumber);
		dossiersStatisticsImpl.setMonth(month);
		dossiersStatisticsImpl.setYear(year);

		if (govAgencyCode == null) {
			dossiersStatisticsImpl.setGovAgencyCode(StringPool.BLANK);
		}
		else {
			dossiersStatisticsImpl.setGovAgencyCode(govAgencyCode);
		}

		if (domainCode == null) {
			dossiersStatisticsImpl.setDomainCode(StringPool.BLANK);
		}
		else {
			dossiersStatisticsImpl.setDomainCode(domainCode);
		}

		dossiersStatisticsImpl.setAdministrationLevel(administrationLevel);

		dossiersStatisticsImpl.resetOriginalValues();

		return dossiersStatisticsImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		dossierStatisticId = objectInput.readLong();
		companyId = objectInput.readLong();
		groupId = objectInput.readLong();
		userId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		remainingNumber = objectInput.readInt();
		receivedNumber = objectInput.readInt();
		ontimeNumber = objectInput.readInt();
		overtimeNumber = objectInput.readInt();
		processingNumber = objectInput.readInt();
		delayingNumber = objectInput.readInt();
		month = objectInput.readInt();
		year = objectInput.readInt();
		govAgencyCode = objectInput.readUTF();
		domainCode = objectInput.readUTF();
		administrationLevel = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(dossierStatisticId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(userId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);
		objectOutput.writeInt(remainingNumber);
		objectOutput.writeInt(receivedNumber);
		objectOutput.writeInt(ontimeNumber);
		objectOutput.writeInt(overtimeNumber);
		objectOutput.writeInt(processingNumber);
		objectOutput.writeInt(delayingNumber);
		objectOutput.writeInt(month);
		objectOutput.writeInt(year);

		if (govAgencyCode == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(govAgencyCode);
		}

		if (domainCode == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(domainCode);
		}

		objectOutput.writeInt(administrationLevel);
	}

	public long dossierStatisticId;
	public long companyId;
	public long groupId;
	public long userId;
	public long createDate;
	public long modifiedDate;
	public int remainingNumber;
	public int receivedNumber;
	public int ontimeNumber;
	public int overtimeNumber;
	public int processingNumber;
	public int delayingNumber;
	public int month;
	public int year;
	public String govAgencyCode;
	public String domainCode;
	public int administrationLevel;
}