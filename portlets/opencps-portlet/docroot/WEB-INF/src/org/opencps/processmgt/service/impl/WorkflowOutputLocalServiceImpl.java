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

import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.processmgt.service.base.WorkflowOutputLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the workflow output local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.processmgt.service.WorkflowOutputLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.processmgt.service.base.WorkflowOutputLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil
 */
public class WorkflowOutputLocalServiceImpl
	extends WorkflowOutputLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil} to access the workflow output local service.
	 */
	/**
	 * Update Workflow
	 * 
	 * @param workflowOutputId
	 * @param dossierPartId
	 * @param required
	 * @param esign
	 * @param postback
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public WorkflowOutput updateWorkflowOutput(long workflowOutputId,
	    long dossierPartId, long processWorkflowId, boolean required, boolean esign, boolean postback)
	    throws PortalException, SystemException {
		
		WorkflowOutput output = null;
		
		output = workflowOutputPersistence.fetchByPrimaryKey(workflowOutputId);
		
		if (Validator.isNotNull(output)) {
			output.setDossierPartId(dossierPartId);
			output.setProcessWorkflowId(processWorkflowId);
			output.setEsign(esign);
			output.setPostback(postback);
			output.setRequired(required);
			
			workflowOutputPersistence.update(output);
		}
		
		return output;
	}
	

	/**
	 * @param dossierPartId
	 * @param required
	 * @param esign
	 * @param postback
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public WorkflowOutput addWorkflowOutput(
	    long dossierPartId, long processWorkflowId, boolean required, boolean esign, boolean postback)
	    throws PortalException, SystemException {
		
		WorkflowOutput output = null;
		
		long workflowOutputId = counterLocalService.increment(WorkflowOutput.class.getName());
		
		output = workflowOutputPersistence.create(workflowOutputId);
		
		if (Validator.isNotNull(output)) {
			output.setDossierPartId(dossierPartId);
			output.setProcessWorkflowId(processWorkflowId);
			output.setEsign(esign);
			output.setPostback(postback);
			output.setRequired(required);
			
			workflowOutputPersistence.update(output);
		}
		
		return output;
	}
	
	/**
	 * @param processWorkflowId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<WorkflowOutput> getByProcessWF(long processWorkflowId)
	    throws PortalException, SystemException {
		return workflowOutputPersistence.findByP_W_ID(processWorkflowId);
	}
}