/**
 * OpenCPS is the open source Core Public Services software Copyright (C)
 * 2016-present OpenCPS community This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or any later version. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have received a
 * copy of the GNU Affero General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.servicemgt.service.impl;

import java.util.Date;
import java.util.List;

import org.opencps.servicemgt.model.ServiceInfo;
import org.opencps.servicemgt.service.base.ServiceInfoLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.service.ServiceContext;
import com.sun.xml.internal.ws.util.ServiceFinder;

/**
 * The implementation of the service info local service. <p> All custom service
 * methods should be put in this class. Whenever methods are added, rerun
 * ServiceBuilder to copy their definitions into the
 * {@link org.opencps.servicemgt.service.ServiceInfoLocalService} interface. <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @see org.opencps.servicemgt.service.base.ServiceInfoLocalServiceBaseImpl
 * @see org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil
 */
public class ServiceInfoLocalServiceImpl
    extends ServiceInfoLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil} to
	 * access the service info local service.
	 */

	/**
	 * Search ServiceInfo
	 * 
	 * @param groupId
	 * @param keywords
	 * @param administrationCode
	 * @param domainCode
	 * @param start
	 * @param end
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ServiceInfo> searchService(
	    long groupId, String keywords, String administrationCode,
	    String domainCode, int start, int end)
	    throws PortalException, SystemException {

		return serviceInfoFinder.searchService(
		    groupId, keywords, administrationCode, domainCode, start, end);
	}

	/**
	 * Count service info
	 * 
	 * @param groupId
	 * @param keywords
	 * @param administrationCode
	 * @param domainCode
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countService(
	    long groupId, String keywords, String administrationCode,
	    String domainCode)
	    throws PortalException, SystemException {

		return serviceInfoFinder.countService(
		    groupId, keywords, administrationCode, domainCode);

	}

	/**
	 * Add ServiceInfo
	 * 
	 * @param serviceNo
	 * @param serviceName
	 * @param shortName
	 * @param serviceProcess
	 * @param serviceMethod
	 * @param serviceDossier
	 * @param serviceCondition
	 * @param serviceDuration
	 * @param serviceActors
	 * @param serviceResults
	 * @param serviceRecords
	 * @param serviceFee
	 * @param serviceInstructions
	 * @param administrationCode
	 * @param administrationIndex
	 * @param domainCode
	 * @param domainIndex
	 * @param activeStatus
	 * @param onlineUrl
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceInfo addService(
	    String serviceNo, String serviceName, String fullName,
	    String serviceProcess, String serviceMethod, String serviceDossier,
	    String serviceCondition, String serviceDuration, String serviceActors,
	    String serviceResults, String serviceRecords, String serviceFee,
	    String serviceInstructions, String administrationCode,
	    String administrationIndex, String domainCode, String domainIndex,
	    int activeStatus, String onlineUrl, long[] fileTemplateIds,
	    ServiceContext context)
	    throws PortalException, SystemException {

		ServiceInfo service = null;

		long serviceId =
		    counterLocalService.increment(ServiceInfo.class.getName());

		int hasTemplateFiles = 0;

		if (fileTemplateIds.length != 0) {
			hasTemplateFiles = 1;
		}

		Date now = new Date();

		service = serviceInfoPersistence.create(serviceId);

		service.setGroupId(context.getScopeGroupId());
		service.setCompanyId(context.getCompanyId());
		service.setUserId(context.getUserId());
		service.setCreateDate(now);
		service.setModifiedDate(now);

		service.setServiceNo(serviceNo);
		service.setServiceName(serviceName);
		service.setFullName(fullName);
		service.setServiceProcess(serviceProcess);
		service.setServiceMethod(serviceMethod);
		service.setServiceDossier(serviceDossier);
		service.setServiceCondition(serviceCondition);
		service.setServiceDuration(serviceDuration);
		service.setServiceActors(serviceActors);
		service.setServiceResults(serviceResults);
		service.setServiceRecords(serviceRecords);
		service.setServiceFee(serviceFee);
		service.setServiceInstructions(serviceInstructions);
		service.setAdministrationCode(administrationCode);
		service.setAdministrationIndex(administrationIndex);
		service.setDomainCode(domainCode);
		service.setDomainIndex(domainIndex);
		service.setActiveStatus(activeStatus);
		service.setOnlineUrl(onlineUrl);
		service.setHasTemplateFiles(hasTemplateFiles);

		// Add to AssetEntry

		long classTypeId = 0;
		boolean visible = true;
		Date startDate = null;
		Date endDate = null;
		Date expirationDate = null;
		String mimeType = ContentTypes.TEXT_HTML;
		String title = serviceName;
		String description = fullName;
		String summary = serviceProcess;
		String url = null;
		String layoutUuid = null;
		int height = 0;
		int width = 0;
		Integer priority = null;
		boolean sync = false;

		serviceFileTemplateLocalService.addServiveFiles(
		    serviceId, fileTemplateIds);

		assetEntryLocalService.updateEntry(
		    context.getUserId(), context.getScopeGroupId(),
		    ServiceInfo.class.getName(), service.getServiceinfoId(),
		    service.getUuid(), classTypeId, context.getAssetCategoryIds(),
		    context.getAssetTagNames(), visible, startDate, endDate,
		    expirationDate, mimeType, title, description, summary, url,
		    layoutUuid, height, width, priority, sync);

		serviceInfoPersistence.update(service);

		// Index ServiceInfo

		Indexer indexer =
		    IndexerRegistryUtil.nullSafeGetIndexer(ServiceInfo.class);

		indexer.reindex(service);

		return service;
	}

	/**
	 * Update service status
	 * 
	 * @param serviceInfoId
	 * @param serviceNo
	 * @param serviceName
	 * @param shortName
	 * @param serviceProcess
	 * @param serviceMethod
	 * @param serviceDossier
	 * @param serviceCondition
	 * @param serviceDuration
	 * @param serviceActors
	 * @param serviceResults
	 * @param serviceRecords
	 * @param serviceFee
	 * @param serviceInstructions
	 * @param administrationCode
	 * @param administrationIndex
	 * @param domainCode
	 * @param domainIndex
	 * @param onlineUrl
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceInfo updateService(
	    long serviceInfoId, String serviceNo, String serviceName,
	    String fullName, String serviceProcess, String serviceMethod,
	    String serviceDossier, String serviceCondition, String serviceDuration,
	    String serviceActors, String serviceResults, String serviceRecords,
	    String serviceFee, String serviceInstructions,
	    String administrationCode, String administrationIndex,
	    String domainCode, String domainIndex, String onlineUrl,
	    long[] fileTemplateIds, ServiceContext context)
	    throws PortalException, SystemException {

		ServiceInfo service = null;

		long serviceId =
		    counterLocalService.increment(ServiceInfo.class.getName());

		int hasTemplateFiles = 0;

		if (fileTemplateIds.length != 0) {
			hasTemplateFiles = 1;
		}

		Date now = new Date();

		service = serviceInfoPersistence.fetchByPrimaryKey(serviceInfoId);

		service.setUserId(context.getUserId());
		service.setModifiedDate(now);

		service.setServiceNo(serviceNo);
		service.setServiceName(serviceName);
		service.setFullName(fullName);
		service.setServiceProcess(serviceProcess);
		service.setServiceMethod(serviceMethod);
		service.setServiceDossier(serviceDossier);
		service.setServiceCondition(serviceCondition);
		service.setServiceDuration(serviceDuration);
		service.setServiceActors(serviceActors);
		service.setServiceResults(serviceResults);
		service.setServiceRecords(serviceRecords);
		service.setServiceFee(serviceFee);
		service.setServiceInstructions(serviceInstructions);
		service.setAdministrationCode(administrationCode);
		service.setAdministrationIndex(administrationIndex);
		service.setDomainCode(domainCode);
		service.setDomainIndex(domainIndex);
		service.setOnlineUrl(onlineUrl);
		service.setHasTemplateFiles(hasTemplateFiles);

		// Add to AssetEntry

		long classTypeId = 0;
		boolean visible = true;
		Date startDate = null;
		Date endDate = null;
		Date expirationDate = null;
		String mimeType = ContentTypes.TEXT_HTML;
		String title = serviceName;
		String description = fullName;
		String summary = serviceProcess;
		String url = null;
		String layoutUuid = null;
		int height = 0;
		int width = 0;
		Integer priority = null;
		boolean sync = false;

		serviceFileTemplateLocalService.addServiveFiles(
			serviceInfoId, fileTemplateIds);

		assetEntryLocalService.updateEntry(
		    context.getUserId(), context.getScopeGroupId(),
		    ServiceInfo.class.getName(), service.getServiceinfoId(),
		    service.getUuid(), classTypeId, context.getAssetCategoryIds(),
		    context.getAssetTagNames(), visible, startDate, endDate,
		    expirationDate, mimeType, title, description, summary, url,
		    layoutUuid, height, width, priority, sync);

		serviceInfoPersistence.update(service);

		// Index ServiceInfo

		Indexer indexer =
		    IndexerRegistryUtil.nullSafeGetIndexer(ServiceInfo.class);

		indexer.reindex(service);

		return service;

	}

	/**
	 * Update service status
	 * 
	 * @param serviceInfoId
	 * @param activeStatus
	 * @param onlineUrl
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceInfo updateServiceStatus(
	    long serviceInfoId, int activeStatus, String onlineUrl,
	    ServiceContext serviceContext)
	    throws PortalException, SystemException {

		return null;
	}

	/**
	 * Delete service
	 * 
	 * @param serviceInfoId
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void deleteService(long serviceInfoId)
	    throws PortalException, SystemException {

	}

	/**
	 * @param groupId
	 * @param administrationCode
	 * @param activateStatus
	 * @return
	 */
	public int countServiceInAdmin(
	    long groupId, String administrationCode, int activateStatus) {

		try {
			return serviceInfoPersistence.countByG_AC_S(
			    groupId, administrationCode, activateStatus);
		}
		catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param groupId
	 * @param domainCode
	 * @param activateStatus
	 * @return
	 */
	public int countServiceInDomain(
	    long groupId, String domainCode, int activateStatus) {

		try {
			return serviceInfoPersistence.countByG_DC_S(
			    groupId, domainCode, activateStatus);
		}
		catch (Exception e) {
			return 0;
		}
	}

}
