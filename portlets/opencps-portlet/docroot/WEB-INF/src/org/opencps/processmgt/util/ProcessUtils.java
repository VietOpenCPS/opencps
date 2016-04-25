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

package org.opencps.processmgt.util;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;

import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;

/**
 * @author khoavd
 */
public class ProcessUtils {
	
	/**
	 * Get DossierTemplate by GroupId
	 * 
	 * @param renderRequest
	 * @return
	 */
	public static List<DossierTemplate> getDossierTemplate(
	    RenderRequest renderRequest) {

		List<DossierTemplate> dossierTemplates =
		    new ArrayList<DossierTemplate>();

		try {
			ServiceContext context =
			    ServiceContextFactory.getInstance(renderRequest);

			dossierTemplates = DossierTemplateLocalServiceUtil.getDossierTemplatesByGroupId(context.getScopeGroupId());

		}
		catch (Exception e) {
			return dossierTemplates;
		}

		return dossierTemplates;
	}

	/**
	 * @param renderRequest
	 * @return
	 */
	public static List<Role> getRoles(RenderRequest renderRequest) {

		List<Role> roles = new ArrayList<Role>();
		try {
			roles =
			    RoleLocalServiceUtil.getTypeRoles(RoleConstants.TYPE_REGULAR);

		}
		catch (Exception e) {
			return new ArrayList<Role>();
		}

		return roles;
	}
	
	
	/**
	 * Get processName
	 * 
	 * @param serviceProcessId
	 * @return
	 */
	public static String getServiceProcessName(long serviceProcessId) {

		String processName = StringPool.BLANK;

		ServiceProcess process = null;

		try {
			process =
			    ServiceProcessLocalServiceUtil.fetchServiceProcess(serviceProcessId);
		}
		catch (Exception e) {
			return processName;
		}

		if (Validator.isNotNull(process)) {
			processName = process.getProcessName();
		}

		return processName;
	}
	

	/**
	 * @param processStepId
	 * @return
	 */
	public static String getProcessStepName(long processStepId) {

		String stepName = StringPool.BLANK;

		ProcessStep step = null;

		try {
			step = ProcessStepLocalServiceUtil.fetchProcessStep(processStepId);
		}
		catch (Exception e) {
			return stepName;
		}

		if (Validator.isNotNull(step)) {
			stepName = step.getStepName();
		}

		return stepName;
	}

}
