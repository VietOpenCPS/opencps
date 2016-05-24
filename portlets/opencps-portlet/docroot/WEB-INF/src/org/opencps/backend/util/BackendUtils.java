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

package org.opencps.backend.util;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;


/**
 * @author khoavd
 *
 */
public class BackendUtils {
	
	public static long getGovAgencyOrgId(long dossierId) {
		
		long govAgencyOrgId = 0;
		
		try {
	        Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);
	        
	        long serviceConfigId = dossier.getServiceConfigId();
	        
	        ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil.fetchServiceConfig(serviceConfigId);
	        
	        if (Validator.isNotNull(serviceConfig)) {
	        	govAgencyOrgId = serviceConfig.getGovAgencyOrganizationId();
	        }
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
		
		return govAgencyOrgId;
	}
	
	public static int getDossierStatus(long dossierId, long fileGroupId) {
		int status = 0;
		
		try {
	        Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierId);
	        
	        if (Validator.isNotNull(dossier)) {
	        	status = dossier.getDossierStatus();
	        }
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
		
		return status;
	}
	
	public static int getDossierStatus(long stepId) {
		
		int status = 0;
		
		try {
			ProcessStep step = ProcessStepLocalServiceUtil.fetchProcessStep(stepId);
			
			if (Validator.isNotNull(step)) {
				status = GetterUtil.getInteger(step.getDossierStatus());
			} 

        }
        catch (Exception e) {
	        return 0;
        }
		
		return status;
	}
	
	public static ProcessWorkflow getFirstProcessWorkflow(long serviceProcessId) {
		ProcessWorkflow flow = null;
		
		
		try {
			flow = ProcessWorkflowLocalServiceUtil.getFirstProcessWorkflow(serviceProcessId);
			
        }
        catch (Exception e) {
        }
		
		return flow;
	}
	

	
	/**
	 * @param serviceProcessId
	 * @return
	 */
	public static long getFristStepLocalService(long serviceProcessId) {
		
		ProcessWorkflow flow = null;
		
		long stepId = 0;
		
		try {
			flow = ProcessWorkflowLocalServiceUtil.getFirstProcessWorkflow(serviceProcessId);
			
			stepId = flow.getPostProcessStepId();
        }
        catch (Exception e) {
	        // TODO: handle exception
        }
		
		return stepId;
	}
	
	/**
	 * Get Dossier by DossierId
	 * 
	 * @param dossierId
	 * @return
	 */
	public static Dossier getDossier(long dossierId) {
		Dossier dossier = null;
		
		try {
	        dossier = DossierLocalServiceUtil.fetchDossier(dossierId);
        }
        catch (Exception e) {
	        _log.error(e);
        }
		
		return dossier;
	}
	

	/**
	 * Get ProcessOrder
	 * 
	 * @param dossierId
	 * @param fileGroupId
	 * @return
	 */
	public static ProcessOrder getProcessOrder(long dossierId, long fileGroupId) {

		ProcessOrder order = null;

		try {
			order =
			    ProcessOrderLocalServiceUtil. getProcessOrder(
			        dossierId, fileGroupId);
		}
		catch (Exception e) {
			return order;
		}
		return order;

	}
	
	private static Log _log = LogFactoryUtil.getLog(BackendUtils.class);
}
