/**
* OpenCPS is the open source Core Public Services software
* Copyright (C) 2016-present OpenCPS community

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package org.opencps.processmgt.util;

import java.util.List;

import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.service.ActionHistoryLocalServiceUtil;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;

public class ProcessMgtUtil {
	public static final String TOP_TABS_DOSSIERLIST = "top_tabs_dossierlist";
	public static final String TOP_TABS_DOSSIERFILELIST = "top_tabs_dossierfilelist";
	
	public static final String DELERR_EXIST_STEP = "step-exist";
	public static final String DELERR_EXIST_SERVICE_CONFIG = "service-config-exist";
	public static final String DELERR_EXIST_WORKFLOW = "workflow-exist";
	
	public static final String DEL_PROCESS_SUCC = "delete-process-success";
	
	public static final String[] _PROCESS_ORDER_CATEGORY_NAMES = {
		"process-order"
	};	
	
	
	/**
	 * @param processWorkflowId
	 * @return
	 */
	public static long getAssignUser(
	    long processWorkflowId, long processOrderId, long preProcessStepId) {

		long userId = 0;

		try {
			if (processWorkflowId > 0) {
				ProcessWorkflow processWorkflow =
				    ProcessWorkflowLocalServiceUtil.getProcessWorkflow(processWorkflowId);
				
				userId = processWorkflow.getActionUserId();

				if (userId == 0) {
					List<ActionHistory> actionList =
					    ActionHistoryLocalServiceUtil.getActionHistoryRecent(
					        processOrderId, preProcessStepId);

					
					if (actionList.size() != 0) {
						userId = actionList.get(0).getActionUserId();
					}
					
					if(userId != 0) {
						 ProcessOrder processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(processOrderId);
						
						userId = processOrder.getAssignToUserId(); 
						
						if(userId == 0) {
							userId = processOrder.getActionUserId();
						}
						
					}
					
					System.out.println("___________assuserId  " + userId);
					
				}
			}
			
		}
		catch (Exception e) {
			userId = 0;
		}

		return userId;
	}
	
	/**
	 * @param processWorkflowId
	 * @return
	 */
	public static ProcessWorkflow getProcessWorkflow (long processWorkflowId) {
		
		ProcessWorkflow workflow = null;
		try {
			
			if (processWorkflowId != 0) {
				workflow = ProcessWorkflowLocalServiceUtil.getProcessWorkflow(processWorkflowId);
			}
        }
        catch (Exception e) {
	       
        }
		
		return workflow;
	}
}
