package org.opencps.processmgt.portlet;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.search.ProcessOrderDisplayTerms;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.util.MessageKeys;

import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class ReAssignPortlet
 */
public class ReAssignPortlet extends MVCPortlet {

	public void reAssignToUser(ActionRequest actionRequest,
			ActionResponse actionResponse) {
		/*long processWorkflowId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID);*/
		long processOrderId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
		long assignToUserId = ParamUtil.getLong(actionRequest,
				ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID);
		String actionNote = ParamUtil.getString(actionRequest,
				ProcessOrderDisplayTerms.ACTION_NOTE);
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		
		try {
			ProcessOrder processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(processOrderId);
			processOrder.setAssignToUserId(assignToUserId);
			processOrder.setActionDatetime(new Date());
			processOrder.setModifiedDate(new Date());
			processOrder.setActionNote(actionNote);
			ProcessOrderLocalServiceUtil.updateProcessOrder(processOrder);
			
			SessionMessages.add(
				    actionRequest,
						MessageKeys.DEFAULT_SUCCESS_KEY);
		} catch (Exception e) {
			SessionMessages.add(
				    actionRequest,
						MessageKeys.DEFAULT_ERROR_KEY);
		} finally {
			try {
				actionResponse.sendRedirect(redirectURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
