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

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;

import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.DossierTemplate;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil;
import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessStepDossierPart;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.ServiceProcess;
import org.opencps.processmgt.model.StepAllowance;
import org.opencps.processmgt.model.WorkflowOutput;
import org.opencps.processmgt.model.impl.ActionHistoryImpl;
import org.opencps.processmgt.model.impl.ProcessStepDossierPartImpl;
import org.opencps.processmgt.model.impl.ProcessStepImpl;
import org.opencps.processmgt.model.impl.StepAllowanceImpl;
import org.opencps.processmgt.model.impl.WorkflowOutputImpl;
import org.opencps.processmgt.service.ActionHistoryLocalServiceUtil;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepDossierPartLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.processmgt.service.StepAllowanceLocalServiceUtil;
import org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author khoavd
 */
public class ProcessUtils {

	/**
	 * @param processStepId
	 * @param state
	 *            <p>
	 *            state = 1 -> get StepAllowance width readOnly = true
	 *            </p>
	 *            <p>
	 *            state = 0 -> get StepAllowance width readOnly = false
	 *            </p>
	 *            <p>
	 *            state = -1 -> getAll StepAllowance
	 *            </p>
	 * @return
	 */
	public static List<User> getAssignUsers(long processStepId, int state) {

		List<User> users = new ArrayList<User>();

		List<StepAllowance> stepAllowances = null;

		try {
			if (state == 1) {
				stepAllowances = StepAllowanceLocalServiceUtil
						.getByProcessStep(processStepId, true);
			} else if (state == 0) {
				stepAllowances = StepAllowanceLocalServiceUtil
						.getByProcessStep(processStepId, false);
			} else {
				stepAllowances = StepAllowanceLocalServiceUtil
						.getByProcessStep(processStepId);
			}

			if (stepAllowances != null) {
				for (StepAllowance stepAllowance : stepAllowances) {

					long roleId = stepAllowance.getRoleId();
					if (roleId > 0) {
						List<User> usersTemp = UserLocalServiceUtil
								.getRoleUsers(roleId);
						if (usersTemp != null && !usersTemp.isEmpty()) {
							users.addAll(usersTemp);
						}
					}
				}
			}

			ListUtil.distinct(users);
		} catch (Exception e) {
			_log.error(e);
		}

		return users;

	}

	/**
	 * Get Workflow Output Removed
	 * 
	 * @param beforeList
	 * @param afterList
	 * @return
	 */
	public static List<WorkflowOutput> getWorkflowOutputRemove(
			List<WorkflowOutput> beforeList, List<WorkflowOutput> afterList) {

		List<WorkflowOutput> removeWorkflow = new ArrayList<WorkflowOutput>();

		for (WorkflowOutput before : beforeList) {

			boolean isNotContain = true;

			for (WorkflowOutput after : afterList) {

				if (Validator.equals(before.getWorkflowOutputId(),
						after.getWorkflowOutputId())) {
					isNotContain = false;
				}
			}

			if (isNotContain) {
				removeWorkflow.add(before);
			}

		}

		return removeWorkflow;

	}

	/**
	 * @param actionRequest
	 * @param processWorkflowId
	 * @return
	 */
	public static List<WorkflowOutput> getWorkflowOutput(
			ActionRequest actionRequest, long processWorkflowId) {
		List<WorkflowOutput> outputs = new ArrayList<WorkflowOutput>();

		String outputIndexString = ParamUtil.getString(actionRequest,
				"outputIndexs");

		int[] outputIndexes = StringUtil.split(outputIndexString, 0);

		for (int outputIndex : outputIndexes) {

			WorkflowOutput output = new WorkflowOutputImpl();

			long workflowOutputId = ParamUtil.getLong(actionRequest,
					"workflowOutputId" + outputIndex);
			long dossierPartId = ParamUtil.getLong(actionRequest,
					"dossierPartId" + outputIndex);
			boolean required = ParamUtil.getBoolean(actionRequest, "required"
					+ outputIndex);
			boolean esign = ParamUtil.getBoolean(actionRequest, "esign"
					+ outputIndex);
			boolean postback = ParamUtil.getBoolean(actionRequest, "postback"
					+ outputIndex);
			String wfOutputPattern = ParamUtil.getString(actionRequest,
					"wfOutputPattern" + outputIndex);

			output.setWorkflowOutputId(workflowOutputId);
			output.setProcessWorkflowId(processWorkflowId);
			output.setDossierPartId(dossierPartId);
			output.setRequired(required);
			output.setEsign(esign);
			output.setPostback(postback);
			output.setPattern(wfOutputPattern);

			outputs.add(output);
		}

		return outputs;
	}

	/**
	 * @param beforeList
	 * @param afterList
	 * @return
	 */
	public static List<ProcessStepDossierPart> getStepDossierRemove(
			List<ProcessStepDossierPart> beforeList,
			List<ProcessStepDossierPart> afterList) {

		List<ProcessStepDossierPart> removeStepList = new ArrayList<ProcessStepDossierPart>();

		for (ProcessStepDossierPart before : beforeList) {

			boolean isNotContain = true;

			if (afterList.contains(before)) {
				isNotContain = false;
			}

			if (isNotContain) {
				removeStepList.add(before);
			}
		}

		return removeStepList;

	}

	/**
	 * @param beforeList
	 * @param afterList
	 * @return
	 */
	public static List<StepAllowance> getStepAllowanceRemove(
			List<StepAllowance> beforeList, List<StepAllowance> afterList) {

		List<StepAllowance> removeStepAllowances = new ArrayList<StepAllowance>();

		for (StepAllowance before : beforeList) {

			boolean isNotContain = true;

			for (StepAllowance after : afterList) {

				if (Validator.equals(before.getStepAllowanceId(),
						after.getStepAllowanceId())) {
					isNotContain = false;
				}

			}

			if (isNotContain) {
				removeStepAllowances.add(before);
			}

		}

		return removeStepAllowances;

	}

	/**
	 * @param actionRequest
	 * @param processStepId
	 * @return
	 */
	public static List<ProcessStepDossierPart> getStepDossiers(
			ActionRequest actionRequest, long processStepId) {
		List<ProcessStepDossierPart> ls = new ArrayList<ProcessStepDossierPart>();

		String dossierIndexString = ParamUtil.getString(actionRequest,
				"dossierIndexs");

		int[] dossierIndexes = StringUtil.split(dossierIndexString, 0);

		for (int dossierIndex : dossierIndexes) {
			ProcessStepDossierPart doisserPart = new ProcessStepDossierPartImpl();

			long dossierPartId = ParamUtil.getLong(actionRequest, "dossierPart"
					+ dossierIndex);
			boolean readOnly = ParamUtil.getBoolean(actionRequest,
					"partReadOnly" + dossierIndex);

			doisserPart.setDossierPartId(dossierPartId);
			doisserPart.setProcessStepId(processStepId);
			doisserPart.setReadOnly(readOnly);

			ls.add(doisserPart);
		}

		return ls;
	}

	/**
	 * @param actionRequest
	 * @return
	 */
	public static List<StepAllowance> getStepAllowance(
			ActionRequest actionRequest, long processStepId) {
		List<StepAllowance> ls = new ArrayList<StepAllowance>();

		String stepAllowanceIndexsString = ParamUtil.getString(actionRequest,
				"stepAllowanceIndexs");

		int[] stepAllowanceIndexs = StringUtil.split(stepAllowanceIndexsString,
				0);

		for (int stepIndex : stepAllowanceIndexs) {

			long stepAllowanceId = ParamUtil.getLong(actionRequest,
					"stepAllowanceId" + stepIndex);
			long roleId = ParamUtil
					.getLong(actionRequest, "roleId" + stepIndex);
			boolean readOnly = ParamUtil.getBoolean(actionRequest, "readOnly"
					+ stepIndex);

			StepAllowance stepAllowance = new StepAllowanceImpl();

			stepAllowance.setStepAllowanceId(stepAllowanceId);
			stepAllowance.setProcessStepId(processStepId);
			stepAllowance.setReadOnly(readOnly);
			stepAllowance.setRoleId(roleId);

			ls.add(stepAllowance);
		}

		return ls;
	}

	/**
	 * Get DossierPartByType
	 * 
	 * @param dossierTemplateId
	 * @param partType
	 * @return
	 */
	public static List<DossierPart> getDossierParts(long dossierTemplateId,
			int partType) {

		List<DossierPart> ls = new ArrayList<DossierPart>();

		try {
			ls = DossierPartLocalServiceUtil.getDossierPartsByT_T(
					dossierTemplateId, partType);
		} catch (Exception e) {
			return new ArrayList<DossierPart>();
		}

		return ls;
	}

	/**
	 * Get DossierTemplate by GroupId
	 * 
	 * @param renderRequest
	 * @return
	 */
	public static List<DossierTemplate> getDossierTemplate(
			RenderRequest renderRequest) {

		List<DossierTemplate> dossierTemplates = new ArrayList<DossierTemplate>();

		try {
			ServiceContext context = ServiceContextFactory
					.getInstance(renderRequest);

			dossierTemplates = DossierTemplateLocalServiceUtil
					.getDossierTemplatesByGroupId(context.getScopeGroupId());

		} catch (Exception e) {
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
			roles = RoleLocalServiceUtil
					.getTypeRoles(RoleConstants.TYPE_REGULAR);

		} catch (Exception e) {
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
			process = ServiceProcessLocalServiceUtil
					.fetchServiceProcess(serviceProcessId);
		} catch (Exception e) {
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
	public static String getPreProcessStepName(long processStepId) {

		String stepName = StringPool.BLANK;

		ProcessStep step = null;

		try {
			step = ProcessStepLocalServiceUtil.fetchProcessStep(processStepId);
		} catch (Exception e) {
			return stepName;
		}

		if (Validator.isNotNull(step)) {
			stepName = step.getStepName();
		}

		return stepName;
	}

	public static String getPostProcessStepName(long processStepId) {

		String stepName = StringPool.BLANK;

		ProcessStep step = null;

		try {
			step = ProcessStepLocalServiceUtil.fetchProcessStep(processStepId);
		} catch (Exception e) {
			return stepName;
		}

		if (Validator.isNotNull(step)) {
			stepName = step.getStepName();
		}

		return stepName;
	}

	/**
	 * @param processStepId
	 * @return
	 */
	public static List<ProcessStepDossierPart> getDossierPartByStep(
			long processStepId) {
		List<ProcessStepDossierPart> ls = new ArrayList<ProcessStepDossierPart>();

		try {
			if (processStepId != 0) {
				ls = ProcessStepDossierPartLocalServiceUtil
						.getByStep(processStepId);
			}
		} catch (Exception e) {
			_log.error(e);
		}

		return ls;
	}

	/**
	 * @param processOrderId
	 * @param preProcessStepId
	 * @return
	 */
	public static List<ActionHistory> getActionHistory(long processOrderId,
			long preProcessStepId) {

		List<ActionHistory> ls = new ArrayList<ActionHistory>();

		try {
			ls = ActionHistoryLocalServiceUtil.getActionHistoryRecent(
					processOrderId, preProcessStepId);
		} catch (Exception e) {

		}

		return ls;
	}

	public static final String TOP_TABS_PROCESS_ORDER_WAITING_PROCESS = "waiting-process";
	public static final String TOP_TABS_PROCESS_ORDER_FINISHED_PROCESSING = "finished-processing";

	public static String[] _PROCESS_ORDER_CATEGORY_NAMES = { "process-order" };

	/**
	 * @param processWorkflowId
	 * @return
	 */
	public static String getCssClass(long processWorkflowId) {

		String cssClass = StringPool.BLANK;

		int count = 0;

		try {
			ProcessWorkflow processWorkflow = ProcessWorkflowLocalServiceUtil
					.fetchProcessWorkflow(processWorkflowId);

			if (processWorkflow.getAssignUser()) {
				count = count + 1;
			}

			if (processWorkflow.getGenerateDeadline()) {
				count = count + 1;
			}

			if (processWorkflow.getGenerateReceptionNo()) {
				count = count + 1;
			}

			if (processWorkflow.getRequestPayment()) {
				count = count + 1;
			}

			if (count != 0) {
				cssClass = "span" + Integer.toString(12 / count);
			}

		} catch (Exception e) {

		}

		return cssClass;

	}

	/**
	 * @param processStepId
	 * @return
	 */
	public static ProcessStep getPostProcessStep(long processStepId) {

		ProcessStep step = new ProcessStepImpl();

		try {
			step = ProcessStepLocalServiceUtil.getProcessStep(processStepId);
		} catch (Exception e) {

		}

		return step;
	}

	public static ActionHistory getActionHistoryByLogId(long logId) {
		ActionHistory actionHistory = new ActionHistoryImpl();

		try {
			actionHistory = ActionHistoryLocalServiceUtil
					.getActionHistoryByLogId(logId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return actionHistory;
	}

	public static String getDossierPartPattern(long dossierId,
			long fileGroupId, long dossierPartId) {
		String parttern = StringPool.BLANK;

		try {
			ProcessOrder processOrder = ProcessOrderLocalServiceUtil
					.getProcessOrder(dossierId, fileGroupId);

			List<WorkflowOutput> workflowOutputs = WorkflowOutputLocalServiceUtil
					.getByProcessByPWID_DPID(
							processOrder.getProcessWorkflowId(), dossierPartId);

			parttern = workflowOutputs.get(0).getPattern();

		} catch (Exception e) {
			// TODO: handle exception
		}

		return parttern;
	}

	public static List<WorkflowOutput> getWfOutputPattern(long dossierId, long fileGroupId,
			long dossierPartId) {
		List<WorkflowOutput> workflowOutputs = new ArrayList<WorkflowOutput>();

		try {
			ProcessOrder processOrder = ProcessOrderLocalServiceUtil
					.getProcessOrder(dossierId, fileGroupId);

			List<ProcessWorkflow> processWorkflows = ProcessWorkflowLocalServiceUtil
					.getPostProcessWorkflow(processOrder.getServiceProcessId(),
							processOrder.getProcessStepId());

			
			for (ProcessWorkflow processWorkflow : processWorkflows) {
				List<WorkflowOutput> workflowOutputsTemp = WorkflowOutputLocalServiceUtil
						.getByProcessByPWID_DPID(
								processWorkflow.getProcessWorkflowId(),
								dossierPartId);
				for (WorkflowOutput workflowOutput : workflowOutputsTemp) {
					if (Validator.isNotNull(workflowOutput.getDossierPartId())) {
						DossierPart dossierPart = DossierPartLocalServiceUtil
								.getDossierPart(workflowOutput
										.getDossierPartId());
						if(Validator.isNotNull(dossierPart.getFormScript()) && Validator.isNotNull(workflowOutput.getPattern())) {
							workflowOutputs.add(workflowOutput);
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return workflowOutputs;
	}

	private static Log _log = LogFactoryUtil.getLog(ProcessUtils.class
			.getName());
}
