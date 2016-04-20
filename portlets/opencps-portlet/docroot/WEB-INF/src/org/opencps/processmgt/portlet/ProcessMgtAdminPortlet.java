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

package org.opencps.processmgt.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.service.ProcessStepDossierPartLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.processmgt.service.StepAllowanceLocalServiceUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author khoavd
 */
public class ProcessMgtAdminPortlet extends MVCPortlet {

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateProcessStep(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {


		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		long processStepId = ParamUtil.getLong(actionRequest, "processStepId");

		String stepName = ParamUtil.getString(actionRequest, "stepName");

		int sequenceNo = ParamUtil.getInteger(actionRequest, "sequenceNo");

		String dossierStatus =
		    ParamUtil.getString(actionRequest, "dossierStatus");

		int daysDuration = ParamUtil.getInteger(actionRequest, "daysDuration");

		long referenceDossierPartId =
		    ParamUtil.getLong(actionRequest, "referenceDossierPartId");

		String externalAppUrl =
		    ParamUtil.getString(actionRequest, "externalAppUrl");

		long serviceProcessId =
		    ParamUtil.getLong(actionRequest, "serviceProcessId");
		
		List<Long> dossiserParts = new ArrayList<Long>();

		List<Long> roles = new ArrayList<Long>();

		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);
			if (processStepId <= 0) {
				// TODO: Update validator here

				// Add ProcessStep

				ProcessStep step = ProcessStepLocalServiceUtil.addStep(
				    serviceProcessId, stepName, sequenceNo, dossierStatus,
				    daysDuration, referenceDossierPartId, externalAppUrl,
				    serviceContext);
				
				if (Validator.isNotNull(step)) {

					// Add ProcessStepDossiserPart
					for (long dossierPartId : dossiserParts) {
						ProcessStepDossierPartLocalServiceUtil.addPSDP(
						    step.getProcessStepId(), dossierPartId);
					}

					// Add ProcessStep Role
					for (long roleId : roles) {
						StepAllowanceLocalServiceUtil.addAllowance(
						    step.getProcessStepId(), roleId, false);
					}
				}

				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}
			else {
				// TODO: Update validator here

				// Update ProcessStep
				ProcessStepLocalServiceUtil.updateStep(
				    processStepId, serviceProcessId, stepName, sequenceNo,
				    dossierStatus, daysDuration, referenceDossierPartId,
				    externalAppUrl, serviceContext);
				
				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
				
			}
		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}

	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void updateProcess(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long serviceProcessId =
		    ParamUtil.getLong(actionRequest, "serviceProcessId");

		String processNo = ParamUtil.getString(actionRequest, "processNo");

		String processName = ParamUtil.getString(actionRequest, "processName");

		String description = ParamUtil.getString(actionRequest, "description");

		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");

		String returnURL = ParamUtil.getString(actionRequest, "returnURL");

		SessionMessages.add(
		    actionRequest, PortalUtil.getPortletId(actionRequest) +
		        SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			if (serviceProcessId <= 0) {
				// TODO: Update validator

				// Add ServiceProcess

				ServiceProcessLocalServiceUtil.addProcess(
				    processNo, processName, description, serviceContext);
				// Redirect page

				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}

			}
			else {
				// TODO: Update validator here


				// Update ServiceProcess

				ServiceProcessLocalServiceUtil.updateProcess(
				    serviceProcessId, processNo, processName, description);

				// Redirect page
				if (Validator.isNotNull(redirectURL)) {
					actionResponse.sendRedirect(redirectURL);
				}
			}

		}
		catch (Exception e) {
			if (Validator.isNotNull(returnURL)) {
				actionResponse.sendRedirect(returnURL);
			}
		}

	}

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {
		

		long serviceProcessId =
		    ParamUtil.getLong(renderRequest, "serviceProcessId");

		ServiceProcess serviceProcess = null;

		try {
			serviceProcess =
			    ServiceProcessLocalServiceUtil.fetchServiceProcess(serviceProcessId);
		}
		catch (Exception e) {
			_log.error(e);
		}

		renderRequest.setAttribute(
		    WebKeys.SERVICE_PROCESS_ENTRY, serviceProcess);
		super.render(renderRequest, renderResponse);

	}

	private Log _log = LogFactoryUtil.getLog(ProcessMgtAdminPortlet.class);

}
