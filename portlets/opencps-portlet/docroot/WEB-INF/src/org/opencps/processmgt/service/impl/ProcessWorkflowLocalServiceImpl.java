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

import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.service.base.ProcessWorkflowLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the process workflow local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.processmgt.service.ProcessWorkflowLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.processmgt.service.base.ProcessWorkflowLocalServiceBaseImpl
 * @see org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil
 */
public class ProcessWorkflowLocalServiceImpl
	extends ProcessWorkflowLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil} to access the process workflow local service.
	 */

	
	/**
	 * Search ProcessWorkflow
	 * 
	 * @param serviceProcessId
	 * @param start
	 * @param end
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ProcessWorkflow> searchWorkflow(
	    long serviceProcessId, int start, int end)
	    throws PortalException, SystemException {

		return processWorkflowPersistence.findByS_P_ID(
		    serviceProcessId, start, end);
	}

	/**
	 * Count ProcessWorkflow
	 * 
	 * @param serviceProcessId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public int countWorkflow(long serviceProcessId)
	    throws PortalException, SystemException {

		return processWorkflowPersistence.countByS_P_ID(serviceProcessId);
	}
}