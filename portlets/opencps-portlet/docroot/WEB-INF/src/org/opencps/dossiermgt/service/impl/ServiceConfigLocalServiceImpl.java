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
package org.opencps.dossiermgt.service.impl;

import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.NoSuchServiceConfigException;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.base.ServiceConfigLocalServiceBaseImpl;
import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.WorkingUnitLocalServiceUtil;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the service config local service. <p> All custom
 * service methods should be put in this class. Whenever methods are added,
 * rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.dossiermgt.service.ServiceConfigLocalService} interface.
 * <p> This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author trungnt
 * @see org.opencps.dossiermgt.service.base.ServiceConfigLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil
 */
public class ServiceConfigLocalServiceImpl
	extends ServiceConfigLocalServiceBaseImpl {

	/**
	 * @param serviceInfoId
	 * @param serviceAdministrationIndex
	 * @param serviceDomainIndex
	 * @param dossierTemplateId
	 * @param govAgencyCode
	 * @param govAgencyName
	 * @param serviceLevel
	 * @param domainCode
	 * @param userId
	 * @param serviceInstruction
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceConfig addServiceConfig(
		long serviceInfoId, String serviceAdministrationIndex,
		String serviceDomainIndex, long dossierTemplateId, String govAgencyCode,
		String govAgencyName, int serviceLevel, String domainCode, long userId,
		String serviceInstruction, boolean servicePortal,
		boolean serviceOnegate, boolean serviceBackoffice,
		boolean serviceCitizen, boolean serviceBusinees,
		ServiceContext serviceContext)
		throws PortalException, SystemException {

		ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil
			.getServiceInfo(serviceInfoId);

		WorkingUnit workingUnit = null;

		long serviceConfigId = CounterLocalServiceUtil
			.increment(ServiceConfig.class
				.getName());

		ServiceConfig serviceConfig = serviceConfigPersistence
			.create(serviceConfigId);

		boolean isBackOffice = false;

		if (serviceBackoffice) {
			workingUnit = WorkingUnitLocalServiceUtil
				.getWorkingUnit(serviceInfo
					.getGroupId(), govAgencyCode);
			if (workingUnit != null) {
				isBackOffice = true;
			}
		}

		Date currentDate = new Date();

		serviceConfig
			.setUserId(userId);
		serviceConfig
			.setCompanyId(serviceContext
				.getCompanyId());
		serviceConfig
			.setGroupId(serviceContext
				.getScopeGroupId());
		serviceConfig
			.setCreateDate(currentDate);
		serviceConfig
			.setModifiedDate(currentDate);

		serviceConfig
			.setServiceInstruction(serviceInstruction);
		serviceConfig
			.setServicePortal(servicePortal);
		serviceConfig
			.setServiceOnegate(serviceOnegate);
		serviceConfig
			.setServiceBackoffice(serviceBackoffice);
		serviceConfig
			.setServiceCitizen(serviceCitizen);
		serviceConfig
			.setServiceBusinees(serviceBusinees);
		serviceConfig
			.setServiceInfoId(serviceInfoId);
		serviceConfig
			.setServiceAdministrationIndex(serviceAdministrationIndex);
		serviceConfig
			.setServiceDomainIndex(serviceDomainIndex);
		serviceConfig
			.setDomainCode(domainCode);
		serviceConfig
			.setDossierTemplateId(dossierTemplateId);
		serviceConfig
			.setGovAgencyCode(govAgencyCode);
		serviceConfig
			.setGovAgencyName(govAgencyName);
		serviceConfig
			.setServiceLevel(serviceLevel);;
		if (isBackOffice) {
			serviceConfig
				.setGovAgencyOrganizationId(workingUnit
					.getMappingOrganisationId());
		}

		return serviceConfigPersistence
			.update(serviceConfig);

	}

	/**
	 * @return
	 * @throws SystemException
	 */
	public int countAll()
		throws SystemException {

		return serviceConfigPersistence
			.countAll();
	}

	/**
	 * @param dossierTemplateId
	 * @return
	 * @throws SystemException
	 */
	public int countByDossierTemplateId(long dossierTemplateId)
		throws SystemException {

		return serviceConfigPersistence
			.countByDossierTemplateId(dossierTemplateId);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param govAgencyCode
	 * @param domainCode
	 * @return
	 */
	public int countServiceConfig(
		long groupId, String keywords, String govAgencyCode,
		String domainCode) {

		return serviceConfigFinder
			.countServiceConfig(groupId, keywords, govAgencyCode, domainCode);
	}

	/**
	 * @param groupId
	 * @param keyword
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param serviceDomainIndex
	 * @param govAgencyIndex
	 * @return
	 */
	public int countServiceConfigAdvance(
		long groupId, String keyword, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		String serviceDomainIndex, String govAgencyIndex) {

		return serviceConfigFinder
			.countServiceConfigAdvance(groupId, keyword, servicePortal,
				serviceOnegate, serviceBackoffice, serviceCitizen,
				serviceBusinees, serviceDomainIndex, govAgencyIndex);
	}

	/**
	 * @param groupId
	 * @param serviceModes
	 * @return
	 */
	public int countServiceConfigByServiceMode(
		long groupId, int[] serviceModes) {

		return serviceConfigFinder
			.countServiceConfigByServiceMode(groupId, serviceModes);
	}

	/**
	 * @param groupId
	 * @param serviceMode
	 * @return
	 * @throws SystemException
	 */
	public int countServiceConFigsByG_M(long groupId, int serviceMode)
		throws SystemException {

		return serviceConfigPersistence
			.countByG_M(groupId, serviceMode);
	}

	/**
	 * @param serviceConfigId
	 * @throws NoSuchServiceConfigException
	 * @throws SystemException
	 */
	public void deleteServiceConfigById(long serviceConfigId)
		throws NoSuchServiceConfigException, SystemException {

		serviceConfigPersistence
			.remove(serviceConfigId);
	}
	
	/**
	 * @param groupId
	 * @param keyword
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param serviceDomainIndex
	 * @param govAgencyIndex
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 */
	public List searchServiceConfigAdvance(
		long groupId, String keyword, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		String serviceDomainIndex, String govAgencyIndex, int start, int end,
		OrderByComparator orderByComparator) {

		return serviceConfigFinder
			.searchServiceConfigAdvance(groupId, keyword, servicePortal,
				serviceOnegate, serviceBackoffice, serviceCitizen,
				serviceBusinees, serviceDomainIndex, govAgencyIndex, start, end,
				orderByComparator);
	}
	
	/**
	 * @param groupId
	 * @param userId
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 */
	public List getServiceConfigRecent(
		long groupId, long userId, int servicePortal, int serviceOnegate,
		int serviceBackoffice, int serviceCitizen, int serviceBusinees,
		int start, int end, OrderByComparator orderByComparator) {

		return serviceConfigFinder
			.getServiceConfigRecent(groupId, userId, servicePortal,
				serviceOnegate, serviceBackoffice, serviceCitizen,
				serviceBusinees, start, end, orderByComparator);
	}

	/**
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 * @throws SystemException
	 */
	public List<ServiceConfig> getAll(
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {

		return serviceConfigPersistence
			.findAll(start, end, orderByComparator);
	}

	/**
	 * @param groupId
	 * @param serviceInfoId
	 * @return
	 * @throws NoSuchServiceConfigException
	 * @throws SystemException
	 */
	public ServiceConfig getServiceConfigByG_S(long groupId, long serviceInfoId)
		throws NoSuchServiceConfigException, SystemException {

		return serviceConfigPersistence
			.findByG_S(groupId, serviceInfoId);
	}
	
	/**
	 * @param groupId
	 * @param serviceInfoId
	 * @param serviceAdministrationIndex
	 * @return
	 * @throws NoSuchServiceConfigException
	 * @throws SystemException
	 */
	public ServiceConfig getServiceConfigByG_S_A(long groupId, long serviceInfoId, 
		String serviceAdministrationIndex)
					throws NoSuchServiceConfigException, SystemException {

					return serviceConfigPersistence
						.findByG_S_A(groupId, serviceInfoId, serviceAdministrationIndex);
				}
	
	/**
	 * @param groupId
	 * @param serviceInfoId
	 * @param govAgencyCode
	 * @return
	 * @throws NoSuchServiceConfigException
	 * @throws SystemException
	 */
	public ServiceConfig getServiceConfigByG_S_G(long groupId, long serviceInfoId, String govAgencyCode)
					throws NoSuchServiceConfigException, SystemException {

					return serviceConfigPersistence
						.findByG_S_G(groupId, serviceInfoId, govAgencyCode);
				}

	/**
	 * @param dossierTemplateId
	 * @return
	 * @throws SystemException
	 */
	public List<ServiceConfig> getServiceConfigs(long dossierTemplateId)
		throws SystemException {

		return serviceConfigPersistence
			.findByDossierTemplateId(dossierTemplateId);
	}

	/**
	 * @param groupId
	 * @param serviceMode
	 * @param start
	 * @param end
	 * @return
	 * @throws SystemException
	 */
	public List<ServiceConfig> getServiceConFigsByG_M(
		long groupId, int serviceMode, int start, int end)
		throws SystemException {

		return serviceConfigPersistence
			.filterFindByG_M(groupId, serviceMode, start, end);
	}

	/**
	 * @param groupId
	 * @param keywords
	 * @param govAgencyCode
	 * @param domainCode
	 * @param start
	 * @param end
	 * @return
	 */
	public List<ServiceConfig> searchServiceConfig(
		long groupId, String keywords, String govAgencyCode, String domainCode,
		int start, int end) {

		return serviceConfigFinder
			.searchServiceConfig(groupId, keywords, govAgencyCode, domainCode,
				start, end);

	}

	/**
	 * @param groupId
	 * @param serviceModes
	 * @param start
	 * @param end
	 * @param orderByComparator
	 * @return
	 */
	public List<ServiceConfig> searchServiceConfigByServiceMode(
		long groupId, int[] serviceModes, int start, int end,
		OrderByComparator orderByComparator) {

		return serviceConfigFinder
			.searchServiceConfigByServiceMode(groupId, serviceModes, start, end,
				orderByComparator);

	}
	
	/**
	 * @param serviceConfigId
	 * @param serviceInfoId
	 * @param serviceAdministrationIndex
	 * @param serviceDomainIndex
	 * @param dossierTemplateId
	 * @param govAgencyCode
	 * @param govAgencyName
	 * @param serviceLevel
	 * @param domainCode
	 * @param userId
	 * @param serviceInstruction
	 * @param servicePortal
	 * @param serviceOnegate
	 * @param serviceBackoffice
	 * @param serviceCitizen
	 * @param serviceBusinees
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceConfig updateServiceConfig(
		long serviceConfigId, long serviceInfoId,
		String serviceAdministrationIndex, String serviceDomainIndex,
		long dossierTemplateId, String govAgencyCode, String govAgencyName,
		int serviceLevel, String domainCode, long userId,
		String serviceInstruction, boolean servicePortal,
		boolean serviceOnegate, boolean serviceBackoffice,
		boolean serviceCitizen, boolean serviceBusinees,
		ServiceContext serviceContext)
		throws PortalException, SystemException {

		ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil
			.getServiceInfo(serviceInfoId);

		WorkingUnit workingUnit = null;

		ServiceConfig serviceConfig = serviceConfigPersistence
			.findByPrimaryKey(serviceConfigId);

		boolean isBackOffice = false;

		if (serviceBackoffice) {
			workingUnit = WorkingUnitLocalServiceUtil
				.getWorkingUnit(serviceInfo
					.getGroupId(), govAgencyCode);
			if (workingUnit != null) {
				isBackOffice = true;
			}
		}

		Date currentDate = new Date();

		serviceConfig
			.setUserId(userId);
		serviceConfig
			.setCompanyId(serviceContext
				.getCompanyId());
		serviceConfig
			.setGroupId(serviceContext
				.getScopeGroupId());
		serviceConfig
			.setCreateDate(currentDate);
		serviceConfig
			.setModifiedDate(currentDate);

		serviceConfig
			.setServiceInstruction(serviceInstruction);
		serviceConfig
			.setServicePortal(servicePortal);
		serviceConfig
			.setServiceOnegate(serviceOnegate);
		serviceConfig
			.setServiceBackoffice(serviceBackoffice);
		serviceConfig
			.setServiceCitizen(serviceCitizen);
		serviceConfig
			.setServiceBusinees(serviceBusinees);
		serviceConfig
			.setServiceInfoId(serviceInfoId);
		serviceConfig
			.setServiceAdministrationIndex(serviceAdministrationIndex);
		serviceConfig
			.setServiceDomainIndex(serviceDomainIndex);
		serviceConfig
			.setDomainCode(domainCode);
		serviceConfig
			.setDossierTemplateId(dossierTemplateId);
		serviceConfig
			.setGovAgencyCode(govAgencyCode);
		serviceConfig
			.setGovAgencyName(govAgencyName);
		serviceConfig
			.setServiceLevel(serviceLevel);;
		if (isBackOffice) {
			serviceConfig
				.setGovAgencyOrganizationId(workingUnit
					.getMappingOrganisationId());
		}

		return serviceConfigPersistence
			.update(serviceConfig);

	}
}
