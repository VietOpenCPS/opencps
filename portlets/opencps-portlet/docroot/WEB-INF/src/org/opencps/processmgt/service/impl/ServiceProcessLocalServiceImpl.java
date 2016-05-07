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

package org.opencps.processmgt.service.impl;

import java.util.Date;
import java.util.List;

import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.processmgt.service.base.ServiceProcessLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the service process local service. <p> All custom
 * service methods should be put in this class. Whenever methods are added,
 * rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.processmgt.service.ServiceProcessLocalService} interface.
 * <p> This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @see org.opencps.processmgt.service.base.ServiceProcessLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ServiceProcessLocalServiceUtil
 */
public class ServiceProcessLocalServiceImpl
    extends ServiceProcessLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link org.opencps.processmgt.service.ServiceProcessLocalServiceUtil} to
	 * access the service process local service.
	 */

	/**
	 * Search Process
	 * 
	 * @param groupId
	 * @param keywords
	 * @param start
	 * @param end
	 * @return
	 */
	public List<ServiceProcess> searchProcess(
	    long groupId, String keywords, int start, int end) {

		return serviceProcessFinder.searchProcess(groupId, keywords, start, end);
	}

	/**
	 * Count Process
	 * 
	 * @param groupId
	 * @param keywords
	 * @return
	 */
	public int countProcess(long groupId, String keywords) {

		return serviceProcessFinder.countProcess(groupId, keywords);
	}

	/**
	 * Update Service Process
	 * 
	 * @param serviceProcessId
	 * @param processNo
	 * @param processName
	 * @param description
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceProcess updateProcess(
	    long serviceProcessId, String processNo, String processName,
	    long dossierTemplateId, String description)
	    throws PortalException, SystemException {

		ServiceProcess serviceProcess =
		    ServiceProcessLocalServiceUtil.fetchServiceProcess(serviceProcessId);

		if (Validator.isNotNull(serviceProcess)) {
			serviceProcess.setModifiedDate(new Date());
			serviceProcess.setProcessNo(processNo);
			serviceProcess.setProcessName(processName);
			serviceProcess.setDescription(description);
			serviceProcess.setDossierTemplateId(dossierTemplateId);

			serviceProcessPersistence.update(serviceProcess);
		}

		return serviceProcess;

	}

	/**
	 * Add Service Process
	 * 
	 * @param processNo
	 * @param processName
	 * @param description
	 * @param context
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceProcess addProcess(
	    String processNo, String processName, String description,
	    long dossierTemplateId, ServiceContext context)
	    throws PortalException, SystemException {

		long serviceProcessId =
		    counterLocalService.increment(ServiceProcess.class.getName());

		ServiceProcess serviceProcess =
		    serviceProcessPersistence.create(serviceProcessId);

		Date now = new Date();

		if (Validator.isNotNull(serviceProcess)) {
			serviceProcess.setCompanyId(context.getCompanyId());
			serviceProcess.setGroupId(context.getScopeGroupId());
			serviceProcess.setCreateDate(now);
			serviceProcess.setModifiedDate(now);
			serviceProcess.setUserId(context.getUserId());

			serviceProcess.setProcessNo(processNo);
			serviceProcess.setProcessName(processName);
			serviceProcess.setDescription(description);
			serviceProcess.setDossierTemplateId(dossierTemplateId);
			
			serviceProcessPersistence.update(serviceProcess);
		}

		return serviceProcess;
	}

	public List<ServiceProcess> getServiceProcesses(long groupId, long dossierTemplateId) 
					throws SystemException {
		return serviceProcessPersistence.findByG_T(groupId, dossierTemplateId);
	}
	
	public int countByG_T(long groupId ,long dossierTemplateId) throws SystemException {
		return serviceProcessPersistence.countByG_T(groupId, dossierTemplateId);
	}

}