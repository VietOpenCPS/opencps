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

import java.util.List;

import org.opencps.processmgt.model.ProcessStepDossierPart;
import org.opencps.processmgt.service.base.ProcessStepDossierPartLocalServiceBaseImpl;
import org.opencps.processmgt.service.persistence.ProcessStepDossierPartPK;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the process step dossier part local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.processmgt.service.ProcessStepDossierPartLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.processmgt.service.base.ProcessStepDossierPartLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ProcessStepDossierPartLocalServiceUtil
 */
public class ProcessStepDossierPartLocalServiceImpl
	extends ProcessStepDossierPartLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.processmgt.service.ProcessStepDossierPartLocalServiceUtil} to access the process step dossier part local service.
	 */
	
	/**
	 * @param list
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void removeStepDossier(List<ProcessStepDossierPart> list)
	    throws PortalException, SystemException {
		
		for (ProcessStepDossierPart processStepDossier : list) {
			processStepDossierPartPersistence.remove(processStepDossier);
		}
	}
	
	/**
	 * @param processStepId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ProcessStepDossierPart> getByStep(long processStepId)
	    throws PortalException, SystemException {

		return processStepDossierPartPersistence.findByProcessStepId(processStepId);
	}
	
	/**
	 * Add ProcessServiceDossierPart
	 * 
	 * @param processStepId
	 * @param dossierPartId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ProcessStepDossierPart addPSDP(long processStepId, long dossierPartId)
	    throws PortalException, SystemException {
		
		ProcessStepDossierPart psdp = null;
		
		ProcessStepDossierPartPK pk = new ProcessStepDossierPartPK(processStepId, dossierPartId);
		
		psdp = processStepDossierPartPersistence.fetchByPrimaryKey(pk);
		
		if (Validator.isNull(psdp)) {
			psdp = processStepDossierPartPersistence.create(pk);
		}
		
		processStepDossierPartPersistence.update(psdp);
		
		return psdp;
	}
}