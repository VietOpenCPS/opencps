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

import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLogLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.holidayconfig.util.HolidayUtils;
import org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil;
import org.opencps.processmgt.model.ProcessOrder;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.impl.ProcessStepImpl;
import org.opencps.processmgt.service.ProcessOrderLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.util.PortletConstants;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author khoavd
 */
public class BackendUtils {

	public static final String PRE_CONDITION_PAYOK = "payok";

	public static final String PRE_CONDITION_TAG_LABEL = "tag_";

	public static final String PRE_CONDITION_CANCEL = "cancel";

	public static final String PRE_CONDITION_SERVICE_ID = "service_";

	public static final String PRE_CONDITION_ONEGATE = "onegate";

	public static final String PRE_CONDITION_ONELINE = "oneline";

	public static final String PRE_CONDITION_REPAIR = "repair";

	public static final String PRE_CONDITION_WAITING = "waiting";

	/**
	 * @param pattern
	 * @return
	 */
	public static String buildActionName(String pattern) {

		String actionName = StringPool.BLANK;
		// TODO: implement here
		actionName = pattern;
		return actionName;
	}

	/**
	 * @param pattern
	 * @return
	 */
	public static boolean checkPreCondition(String pattern, long dossierId) {

		boolean validPreCondition = true;

		List<String> lsCondition = ListUtil.toList(StringUtil.split(pattern,
				StringPool.COMMA));

		boolean validPayok = true;
		boolean validCancel = true;
		boolean validTagLabel = true;
		boolean validService = true;
		boolean validOnline = true;
		boolean validOnegate = true;
		boolean validRepair = true;
		boolean validDelay = true;
		boolean validWaiting = true;

		for (String condition : lsCondition) {
			if (StringUtil.equalsIgnoreCase(
					StringUtil.split(condition, StringPool.UNDERLINE)[0],
					PRE_CONDITION_PAYOK)) {

				validPayok = _checkPayOkCondition(dossierId);

				continue;
			}

			if (StringUtil.equalsIgnoreCase(
					StringUtil.split(condition, StringPool.UNDERLINE)[0],
					PRE_CONDITION_CANCEL)) {

				validCancel = _checkRequestCommandlCondition(dossierId,
						WebKeys.REQUEST_COMMAND_CANCEL);

				continue;
			}

			if (StringUtil.equalsIgnoreCase(
					StringUtil.split(condition, StringPool.UNDERLINE)[0],
					PRE_CONDITION_TAG_LABEL)) {

				validTagLabel = _checkTagLabelCondition();

				continue;
			}

			if (StringUtil.equalsIgnoreCase(
					StringUtil.split(condition, StringPool.UNDERLINE)[0],
					PRE_CONDITION_SERVICE_ID)) {

				validService = _checkServiceCondition();

				continue;
			}

			if (StringUtil.equalsIgnoreCase(
					StringUtil.split(condition, StringPool.UNDERLINE)[0],
					PRE_CONDITION_ONEGATE)) {

				validOnegate = _checkOnegateCondition();

				continue;
			}

			if (StringUtil.equalsIgnoreCase(
					StringUtil.split(condition, StringPool.UNDERLINE)[0],
					PRE_CONDITION_ONELINE)) {

				validOnline = _checkOnlineCondition();

				continue;
			}

			if (StringUtil.equalsIgnoreCase(
					StringUtil.split(condition, StringPool.UNDERLINE)[0],
					PRE_CONDITION_WAITING)) {

				String[] splitCondition = StringUtil.split(condition,
						StringPool.UNDERLINE);

				String waitingTime = String.valueOf(0);

				if (splitCondition.length == 2) {
					waitingTime = splitCondition[1];
				}

				validWaiting = _checkWaitingCondition(dossierId,
						waitingTime);
				continue;
			}

		}

		if (validPayok && validCancel && validOnline && validOnegate
				&& validTagLabel && validService && validRepair && validWaiting) {
			validPreCondition = true;
		} else {
			validPreCondition = false;
		}

		return validPreCondition;
	}

	private static boolean _checkWaitingCondition(long dossierId, String pattern) {
		boolean isCondition = true;

		try {
			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);

			String dossierStatus = dossier.getDossierStatus();

			ProcessOrder processOrder = ProcessOrderLocalServiceUtil
					.getProcessOrder(dossierId, dossier.getGroupId());

			if (dossierStatus.contains(PortletConstants.DOSSIER_STATUS_WAITING)) {

				Date endDate = HolidayUtils.getEndDate(processOrder.getActionDatetime(),
						pattern);
				
				Date now = new Date();
				
				if(now.compareTo(endDate) >=0 ){
					isCondition = false;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return isCondition;
	}

	private static boolean _checkPayOkCondition(long dossierId) {

		boolean isCondition = true;

		int countAllPayment = 0;

		int countPaymentComplated = 0;

		try {
			countAllPayment = PaymentFileLocalServiceUtil
					.countAllPaymentFile(dossierId);

			countPaymentComplated = PaymentFileLocalServiceUtil
					.countPaymentFile(dossierId, 2);

			if (!((countAllPayment - countPaymentComplated) == 0)) {
				isCondition = false;
			}
		} catch (Exception e) {
			isCondition = false;
		}

		return isCondition;
	}

	/**
	 * @param dossierId
	 * @param requestCommand
	 * @return
	 */
	private static boolean _checkRequestCommandlCondition(long dossierId,
			String requestCommand) {

		boolean isCondition = true;

		int countRequestCommand = 0;

		try {
			countRequestCommand = DossierLogLocalServiceUtil
					.countDossierByRequestCommand(dossierId, requestCommand);
		} catch (Exception e) {
			_log.error(e);
		}

		if (countRequestCommand != 0) {
			isCondition = true;
		} else {
			isCondition = false;
		}

		return isCondition;
	}

	/**
	 * @return
	 */
	private static boolean _checkTagLabelCondition() {

		// TODO: implement here
		return true;
	}

	/**
	 * @return
	 */
	private static boolean _checkServiceCondition() {

		// TODO: implement here
		return true;
	}

	/**
	 * @return
	 */
	private static boolean _checkOnlineCondition() {

		// TODO: implement here
		return true;
	}

	/**
	 * @return
	 */
	private static boolean _checkOnegateCondition() {

		// TODO: implement here
		return true;
	}

	/**
	 * @param dossierId
	 * @return
	 */
	public static boolean checkPaymentStatus(long dossierId) {

		boolean paymentStatus = true;

		int countAllPayment = 0;

		int countPaymentCompleted = 0;

		try {
			countAllPayment = PaymentFileLocalServiceUtil
					.countAllPaymentFile(dossierId);

			countPaymentCompleted = PaymentFileLocalServiceUtil
					.countPaymentFile(dossierId, 2);

			if (!((countAllPayment - countPaymentCompleted) == 0)) {
				paymentStatus = false;
			}
		} catch (Exception e) {
			paymentStatus = false;
		}

		return paymentStatus;
	}

	/**
	 * @param dossierId
	 * @return
	 */
	public static long getGovAgencyOrgId(long dossierId) {

		long govAgencyOrgId = 0;

		try {
			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);

			long serviceConfigId = dossier.getServiceConfigId();

			ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil
					.fetchServiceConfig(serviceConfigId);

			if (Validator.isNotNull(serviceConfig)) {
				govAgencyOrgId = serviceConfig.getGovAgencyOrganizationId();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return govAgencyOrgId;
	}

	public static String getDossierStatus(long dossierId, long fileGroupId) {

		String status = StringPool.BLANK;

		try {
			Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierId);

			if (Validator.isNotNull(dossier)) {
				status = dossier.getDossierStatus();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return status;
	}

	public static int getDossierStatus(long stepId) {

		int status = 0;

		try {
			ProcessStep step = ProcessStepLocalServiceUtil
					.fetchProcessStep(stepId);

			if (Validator.isNotNull(step)) {
				status = GetterUtil.getInteger(step.getDossierStatus());
			}

		} catch (Exception e) {
			return 0;
		}

		return status;
	}

	public static ProcessWorkflow getFirstProcessWorkflow(long serviceProcessId) {

		ProcessWorkflow flow = null;

		try {
			flow = ProcessWorkflowLocalServiceUtil
					.getFirstProcessWorkflow(serviceProcessId);

		} catch (Exception e) {
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
			flow = ProcessWorkflowLocalServiceUtil
					.getFirstProcessWorkflow(serviceProcessId);

			stepId = flow.getPostProcessStepId();
		} catch (Exception e) {
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
		} catch (Exception e) {
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
			order = ProcessOrderLocalServiceUtil.getProcessOrder(dossierId,
					fileGroupId);
		} catch (Exception e) {
			return order;
		}
		return order;

	}

	/**
	 * @param dossierId
	 * @return
	 */
	public static boolean checkServiceMode(long dossierId) {

		boolean trustServiceMode = false;

		try {
			Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierId);

			long serviceConfigId = dossier.getServiceConfigId();

			ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil
					.fetchServiceConfig(serviceConfigId);

			if (serviceConfig.getServicePortal()
					&& serviceConfig.getServiceBackoffice()) {
				trustServiceMode = true;
			}
		} catch (Exception e) {
			_log.error(e);
		}

		return trustServiceMode;
	}

	/**
	 * @param dossierId
	 * @return
	 */
	public static boolean isDossierCancel(long dossierId) {

		boolean isCancel = false;

		if (dossierId != 0) {
			try {
//				ProcessOrder processOrder = ProcessOrderLocalServiceUtil
//						.getProcessOrder(dossierId, 0);
//
//				long processStepId = processOrder.getProcessStepId();
//				long serviceProcessId = processOrder.getServiceProcessId();
//
//				List<ProcessWorkflow> lsPRC_WFL = new ArrayList<ProcessWorkflow>();
//
//				lsPRC_WFL = ProcessWorkflowLocalServiceUtil
//						.getPostProcessWorkflow(serviceProcessId, processStepId);
//
//				for (ProcessWorkflow prc_wf : lsPRC_WFL) {
//					if (Validator.isNotNull(prc_wf.getPreCondition())
//							&& (StringUtil.trim(prc_wf.getPreCondition())
//									.contains(WebKeys.ACTION_CANCEL_VALUE))) {
//						isCancel = true;
//						break;
//					}
//				}
				Dossier dossider = DossierLocalServiceUtil.getDossier(dossierId);
				
				if(Validator.isNotNull(dossider) && !dossider.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_ENDED)){
					isCancel = true;
				}
			} catch (Exception e) {
			}
		}

		return isCancel;

	}

	/**
	 * @param dossierId
	 * @return
	 */
	public static boolean isDossierChange(long dossierId) {

		boolean isChange = false;

		if (dossierId != 0) {
			try {
				ProcessOrder processOrder = ProcessOrderLocalServiceUtil
						.getProcessOrder(dossierId, 0);

				String dossierStatus = processOrder.getDossierStatus();

				if (Validator.equals(dossierStatus,
						PortletConstants.DOSSIER_STATUS_DONE)) {
					isChange = true;
				}

			} catch (Exception e) {
			}
		}

		return isChange;

	}

	/**
	 * Get processStepById
	 * 
	 * @param dossierId
	 * @return
	 */
	public static ProcessStep getProcessStepByDossierId(long dossierId) {

		ProcessStep processStep = new ProcessStepImpl();

		try {
			ProcessOrder processOrder = ProcessOrderLocalServiceUtil
					.getProcessOrder(dossierId, 0);

			long processStepId = processOrder.getProcessStepId();

			processStep = ProcessStepLocalServiceUtil
					.getProcessStep(processStepId);
		} catch (Exception e) {

		}

		return processStep;
	}

	private static Log _log = LogFactoryUtil.getLog(BackendUtils.class);
}