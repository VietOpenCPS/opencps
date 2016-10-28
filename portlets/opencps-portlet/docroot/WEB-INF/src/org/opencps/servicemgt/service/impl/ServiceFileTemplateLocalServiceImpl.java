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

import java.util.ArrayList;
import java.util.List;

import org.opencps.servicemgt.model.ServiceFileTemplate;
import org.opencps.servicemgt.permissions.ServiceTemplatePermission;
import org.opencps.servicemgt.service.base.ServiceFileTemplateLocalServiceBaseImpl;
import org.opencps.servicemgt.service.persistence.ServiceFileTemplatePK;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the service file template local service. <p> All custom
 * service methods should be put in this class. Whenever methods are added,
 * rerun ServiceBuilder to copy their definitions into the
 * {@link org.opencps.servicemgt.service.ServiceFileTemplateLocalService}
 * interface. <p> This is a local service. Methods of this service will not have
 * security checks based on the propagated JAAS credentials because this service
 * can only be accessed from within the same VM. </p>
 *
 * @author khoavd
 * @see org.opencps.servicemgt.service.base.ServiceFileTemplateLocalServiceBaseImpl
 * @see org.opencps.servicemgt.service.ServiceFileTemplateLocalServiceUtil
 */
public class ServiceFileTemplateLocalServiceImpl
    extends ServiceFileTemplateLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS: Never reference this interface directly. Always use
	 * {@link
	 * org.opencps.servicemgt.service.ServiceFileTemplateLocalServiceUtil} to
	 * access the service file template local service.
	 */

	/**
	 * @param serviceId
	 * @param templateFileIds
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void addServiveFiles(long serviceId, long[] templateFileIds)
	    throws PortalException, SystemException {

		List<ServiceFileTemplate> currentFileTemplates =
		    new ArrayList<ServiceFileTemplate>();
		currentFileTemplates = serviceFileTemplatePersistence.findByServiceinfoId(serviceId);
		// Remove current fileTemplate

		for (ServiceFileTemplate sft : currentFileTemplates) {
			serviceFileTemplatePersistence.remove(sft);
		}

		// Add new file template
		for (long fileTemplateId : templateFileIds) {
			addServiceFile(serviceId, fileTemplateId);
		}

	}
	
	/* (non-Javadoc)
	 * @see org.opencps.servicemgt.service.ServiceFileTemplateLocalService#addFileServices(long, long[])
	 */
	public void addFileServices(long templatefileId, long [] serviceInfoIds) 
					throws SystemException, PortalException {
		List<ServiceFileTemplate> currentFileTemplates =
					    new ArrayList<ServiceFileTemplate>();
		currentFileTemplates = serviceFileTemplatePersistence.findByTemplatefileId(templatefileId);
		for(ServiceFileTemplate sft : currentFileTemplates) {
			serviceFileTemplatePersistence.remove(sft);
		}
		
		for(long serviceinfoId : serviceInfoIds) {
			addServiceFile(serviceinfoId, templatefileId);
		}
	}
	
	
	/**
	 * Add serviceFile
	 * 
	 * @param serviceInfoId
	 * @param templatefileId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ServiceFileTemplate addServiceFile(
	    long serviceInfoId, long templatefileId)
	    throws PortalException, SystemException {

		ServiceFileTemplate sft = null;

		ServiceFileTemplatePK serviceFilePK =
		    new ServiceFileTemplatePK(serviceInfoId, templatefileId);

		// Check exist ServiceFile
		sft = serviceFileTemplatePersistence.fetchByPrimaryKey(serviceFilePK);

		if (Validator.isNull(sft)) {
			sft = serviceFileTemplatePersistence.create(serviceFilePK);

			serviceFileTemplatePersistence.update(sft);
		}

		return sft;

	}
	
	/**
	 * Delete ServiceFile
	 * 
	 * @param serviceInfoId
	 * @param templatefileId
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void deleteServiceFile(long serviceInfoId, long templatefileId)
	    throws PortalException, SystemException {

		ServiceFileTemplate sft = null;

		ServiceFileTemplatePK serviceFilePK =
		    new ServiceFileTemplatePK(serviceInfoId, templatefileId);

		// Get ServiceFile wanted remove
		sft = serviceFileTemplatePersistence.fetchByPrimaryKey(serviceFilePK);

		if (Validator.isNotNull(sft)) {
			serviceFileTemplatePersistence.remove(sft);
		}
		else {
		}

	}
	
	public List<ServiceFileTemplate> getServiceFileTemplatesByTemplateFile(long templateFileId)
					throws SystemException {
		return serviceFileTemplatePersistence.findByTemplatefileId(templateFileId);
	}

}
