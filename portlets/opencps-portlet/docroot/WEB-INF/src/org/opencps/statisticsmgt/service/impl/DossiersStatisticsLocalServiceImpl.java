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

package org.opencps.statisticsmgt.service.impl;

import java.util.Date;
import java.util.List;

import org.opencps.statisticsmgt.NoSuchDossiersStatisticsException;
import org.opencps.statisticsmgt.model.DossiersStatistics;
import org.opencps.statisticsmgt.service.base.DossiersStatisticsLocalServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the dossiers statistics local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.statisticsmgt.service.DossiersStatisticsLocalService}
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author trungnt
 * @see org.opencps.statisticsmgt.service.base.DossiersStatisticsLocalServiceBaseImpl
 * @see org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil
 */
public class DossiersStatisticsLocalServiceImpl extends
		DossiersStatisticsLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil} to
	 * access the dossiers statistics local service.
	 */

	/**
	 * @param groupId
	 * @param companyId
	 * @param userId
	 * @param remainingNumber
	 * @param receivedNumber
	 * @param ontimeNumber
	 * @param processingNumber
	 * @param delayingNumber
	 * @param month
	 * @param year
	 * @param govAgencyCode
	 * @param domainCode
	 * @param administrationLevel
	 * @return
	 * @throws SystemException
	 */
	public DossiersStatistics addDossiersStatistics(long groupId,
			long companyId, long userId, int remainingNumber,
			int receivedNumber, int ontimeNumber, int overtimeNumber,
			int processingNumber, int delayingNumber, int month, int year,
			String govAgencyCode, String domainCode, int administrationLevel)
			throws SystemException {

		long dossierStatisticId = CounterLocalServiceUtil
				.increment(DossiersStatistics.class.getName());

		DossiersStatistics dossiersStatistics = dossiersStatisticsPersistence
				.create(dossierStatisticId);

		Date now = new Date();
		dossiersStatistics.setAdministrationLevel(administrationLevel);
		dossiersStatistics.setCompanyId(companyId);
		dossiersStatistics.setCreateDate(now);
		dossiersStatistics.setDelayingNumber(delayingNumber);
		dossiersStatistics.setDomainCode(domainCode);
		dossiersStatistics.setGovAgencyCode(govAgencyCode);
		dossiersStatistics.setGroupId(groupId);
		dossiersStatistics.setModifiedDate(now);
		dossiersStatistics.setMonth(month);
		dossiersStatistics.setOntimeNumber(ontimeNumber);
		dossiersStatistics.setOvertimeNumber(overtimeNumber);
		dossiersStatistics.setProcessingNumber(processingNumber);
		dossiersStatistics.setReceivedNumber(receivedNumber);
		dossiersStatistics.setRemainingNumber(remainingNumber);
		dossiersStatistics.setUserId(userId);
		dossiersStatistics.setYear(year);

		return dossiersStatisticsPersistence.update(dossiersStatistics);
	}

	/**
	 * @param groupId
	 * @param year
	 * @return
	 */
	public List<Integer> getMonths(long groupId, int year) {
		return dossiersStatisticsFinder.getStatisticsMonths(groupId, year);
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @param field
	 * @param delayStatus
	 * @return
	 * @throws SystemException
	 */
	public List generalStatistics(long groupId, int month, int year,
			String field, int delayStatus) throws SystemException {
		return dossiersStatisticsFinder.generalStatistics(groupId, month, year,
				field, delayStatus);
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @return
	 * @throws SystemException
	 */
	public List statisticsByDomain(long groupId, int month, int year,
			String field, int delayStatus) throws SystemException {
		return dossiersStatisticsFinder.statisticsByDomain(groupId, month,
				year, field, delayStatus);
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @return
	 * @throws SystemException
	 */
	public List statisticsByGovAgency(long groupId, int month, int year,
			String field, int delayStatus) throws SystemException {
		return dossiersStatisticsFinder.statisticsByGovAgency(groupId, month,
				year, field, delayStatus);
	}

	/**
	 * @param domainCode
	 * @param month
	 * @param year
	 * @return
	 * @throws SystemException
	 * @throws NoSuchDossiersStatisticsException
	 */
	public DossiersStatistics getDossiersStatisticsByDC_M_Y(long groupId,
			String domainCode, int month, int year) throws SystemException,
			NoSuchDossiersStatisticsException {
		return dossiersStatisticsPersistence.findByG_DC_M_Y(groupId,
				domainCode, month, year);
	}

	/**
	 * @param groupId
	 * @param month
	 * @param year
	 * @param administrationLevel
	 * @return
	 * @throws SystemException
	 * @throws NoSuchDossiersStatisticsException
	 */
	public DossiersStatistics getDossiersStatisticsByG_M_Y(long groupId,
			int month, int year) throws SystemException,
			NoSuchDossiersStatisticsException {
		return dossiersStatisticsPersistence.findByG_M_Y(groupId, month, year);
	}

	/**
	 * @param govAgencyCode
	 * @param domainCode
	 * @param month
	 * @param year
	 * @return
	 * @throws SystemException
	 * @throws NoSuchDossiersStatisticsException
	 */
	public DossiersStatistics getDossiersStatisticsByG_GC_DC_M_Y_L(
			long groupId, String govAgencyCode, String domainCode, int month,
			int year, int administrationLevel) throws SystemException,
			NoSuchDossiersStatisticsException {
		return dossiersStatisticsPersistence.findByG_GC_DC_M_Y_L(groupId,
				govAgencyCode, domainCode, month, year, administrationLevel);
	}

	/**
	 * @param govAgencyCode
	 * @param domainCode
	 * @param year
	 * @return
	 * @throws SystemException
	 */
	public List<DossiersStatistics> getDossiersStatisticsByG_GC_DC_Y(
			long groupId, String govAgencyCode, String domainCode, int year)
			throws SystemException {
		return dossiersStatisticsPersistence.findByG_GC_DC_Y(groupId,
				govAgencyCode, domainCode, year);
	}

	/**
	 * @param dossierStatisticId
	 * @param remainingNumber
	 * @param receivedNumber
	 * @param ontimeNumber
	 * @param overtimeNumber
	 * @param processingNumber
	 * @param delayingNumber
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 */
	public DossiersStatistics updateDossiersStatistics(long dossierStatisticId,
			int remainingNumber, int receivedNumber, int ontimeNumber,
			int overtimeNumber, int processingNumber, int delayingNumber)
			throws SystemException, PortalException {

		DossiersStatistics dossiersStatistics = dossiersStatisticsLocalService
				.getDossiersStatistics(dossierStatisticId);

		Date now = new Date();

		dossiersStatistics.setDelayingNumber(delayingNumber);
		dossiersStatistics.setModifiedDate(now);
		dossiersStatistics.setOntimeNumber(ontimeNumber);
		dossiersStatistics.setOvertimeNumber(overtimeNumber);
		dossiersStatistics.setProcessingNumber(processingNumber);
		dossiersStatistics.setReceivedNumber(receivedNumber);
		dossiersStatistics.setRemainingNumber(remainingNumber);

		return dossiersStatisticsPersistence.update(dossiersStatistics);
	}
}