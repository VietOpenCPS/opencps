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
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.opencps.processmgt.NoSuchProcessOrderException;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.util.ProcessMgtUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungdk
 */

public class ProcessMgtOneGatePorlet extends MVCPortlet {
	/**
	 * @param actionRequest
	 * @param actionResponse
	 * @throws IOException
	 */
	public void dossierReturnURL(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {
		long processOrderId = ParamUtil.getLong(actionRequest, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
		String actionNote = ParamUtil.getString(actionRequest, ProcessOrderDisplayTerms.ACTION_NOTE);
		addProcessActionSuccessMessage = false;
		ProcessOrder processOrder = null;
		try {
			processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(processOrderId);
			processOrder.setActionNote(actionNote);
			processOrder.setActionDatetime(new Date());
			processOrder.setProcessStepId(-1);
			ProcessOrderLocalServiceUtil.updateProcessOrder(processOrder);
			SessionMessages.add(actionRequest, "dossier-return-success");
			actionResponse.setRenderParameter("jspPage", templatePath + "dossierreturnsuccess.jsp");
			actionResponse.setRenderParameter("tabs1", ProcessMgtUtil.TOP_TABS_ONEGATE_RETURNLIST);
			actionResponse.setRenderParameter(ProcessOrderDisplayTerms.PROCESS_ORDER_ID, String.valueOf(processOrderId));		
		}
		catch (SystemException e) {
			SessionErrors.add(actionRequest, "dossier-return-error");
			actionResponse.setRenderParameter("jspPage", templatePath + "dossierreturn.jsp");
			actionResponse.setRenderParameter("tabs1", ProcessMgtUtil.TOP_TABS_ONEGATE_RETURNLIST);
			actionResponse.setRenderParameter(ProcessOrderDisplayTerms.PROCESS_ORDER_ID, String.valueOf(processOrderId));		
		}
		catch (PortalException e) {
			SessionErrors.add(actionRequest, "dossier-return-error");			
			actionResponse.setRenderParameter("jspPage", templatePath + "dossierreturn.jsp");
			actionResponse.setRenderParameter("tabs1", ProcessMgtUtil.TOP_TABS_ONEGATE_RETURNLIST);
			actionResponse.setRenderParameter(ProcessOrderDisplayTerms.PROCESS_ORDER_ID, String.valueOf(processOrderId));		
		}
	}
}
