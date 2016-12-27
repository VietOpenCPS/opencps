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

package org.opencps.holidayconfig.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.opencps.processmgt.model.ActionHistory;
import org.opencps.processmgt.model.ProcessStep;
import org.opencps.processmgt.model.ProcessWorkflow;
import org.opencps.processmgt.model.impl.ActionHistoryImpl;
import org.opencps.processmgt.model.impl.ProcessStepImpl;
import org.opencps.processmgt.model.impl.ProcessWorkflowImpl;
import org.opencps.processmgt.service.ActionHistoryLocalServiceUtil;
import org.opencps.processmgt.service.ProcessStepLocalServiceUtil;
import org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil;
import org.opencps.processmgt.util.OutDateStatus;
import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

public class HolidayCheckUtils {

	private static Log _log = LogFactoryUtil.getLog(HolidayCheckUtils.class);

	public static OutDateStatus checkActionDateOverStatus(Date startDate,
			Date endDate, int daysDuration) {

		OutDateStatus outDateStatus = new OutDateStatus();
		long dateOverNumbers = 0;

		if (daysDuration > 0) {

			Calendar endDayCal = Calendar.getInstance();
			
			endDayCal.setTime(endDate);
			
			Calendar endDateMax = HolidayUtils.getEndDate(startDate,
					daysDuration);

			long endDay = endDayCal.getTimeInMillis();
			long endDayMax = endDateMax.getTimeInMillis();

			dateOverNumbers = endDayMax - endDay;
			
			dateOverNumbers = dateOverNumbers / (24 * 60 * 60 * 1000);

			if (dateOverNumbers > 0) {
				outDateStatus.setIsOutDate(false);
				outDateStatus.setDaysOutdate(dateOverNumbers);
				return outDateStatus;
			} else if (dateOverNumbers < 0) {
				outDateStatus.setIsOutDate(true);
				outDateStatus.setDaysOutdate(Math.abs(dateOverNumbers));
				return outDateStatus;
			}
		}
		outDateStatus.setDaysOutdate(0);
		return outDateStatus;
	}
	/**
	 * @param startDate
	 * @param endDate
	 * @param daysDuration
	 * @return
	 */
	public static int checkActionDateOver(Date startDate, Date endDate,
			int daysDuration) {

		int dateOverNumbers = 0;

		if (daysDuration > 0) {

			Calendar endDayCal = Calendar.getInstance();
			endDayCal.setTime(endDate);

			Calendar endDateMax = HolidayUtils.getEndDate(startDate,
					daysDuration);

			long endDay = endDayCal.getTimeInMillis();
			long endDayMax = endDateMax.getTimeInMillis();
			long result = 0;

			result = endDayMax - endDay;

			result = DateTimeUtil.convertTimemilisecondsToDays(result);

			dateOverNumbers = (int) result;

			if (dateOverNumbers > 0) {
				return 0;
			} else {
				return Math.abs(dateOverNumbers);
			}
		}
		return dateOverNumbers;
	}

	public static int calculatorDateOver(Date startDate, Date endDate, int daysDuration) {

		int dateOverNumbers = 0;

		if (daysDuration > 0) {

			Calendar endayCal = Calendar.getInstance();
			endayCal.setTime(endDate);

			Calendar dealineCal = HolidayUtils.getEndDate(startDate, daysDuration);

			long endDay = endayCal.getTimeInMillis();
			long deadline = dealineCal.getTimeInMillis();
			long result = 0;

			result = deadline - endDay;

			result = DateTimeUtil.convertTimemilisecondsToDays(result);

			dateOverNumbers = (int) result;
		}
		return dateOverNumbers;
	}
	
	public static int calculatorDateUntilDealine(Date startDate, Date endDate, int daysDuration) {

		int dateOverNumbers = 0;

		if (daysDuration > 0) {

			Calendar endayCal = Calendar.getInstance();
			endayCal.setTime(endDate);

			Calendar dealineCal = Calendar.getInstance();
			dealineCal.setTime(startDate);
			
			for (int i = 0; i < daysDuration; i++) {
				
				dealineCal.add(Calendar.DATE, 1);
			}

			long endDay = endayCal.getTimeInMillis();
			long deadline = dealineCal.getTimeInMillis();
			long result = 0;

			result = deadline - endDay;

			result = DateTimeUtil.convertTimemilisecondsToDays(result);

			dateOverNumbers = (int) result;
		}
		return dateOverNumbers;
	}
	
	public static String calculatorDateUntilDealineReturnFormart(Date startDate, Date endDate, int daysDuration,Locale locale) {

		String dateOverNumbers = StringPool.BLANK;

		if (daysDuration > 0 && Validator.isNotNull(startDate) && Validator.isNotNull(endDate)) {

			Calendar endayCal = Calendar.getInstance();
			endayCal.setTime(endDate);

			Calendar dealineCal = Calendar.getInstance();
			dealineCal.setTime(startDate);
			
			for (int i = 0; i < daysDuration; i++) {
				
				dealineCal.add(Calendar.DATE, 1);
			}

			long endDay = endayCal.getTimeInMillis();
			long deadline = dealineCal.getTimeInMillis();
			long result = 0;

			result = deadline - endDay;

			dateOverNumbers = DateTimeUtil.convertTimemilisecondsToFormat(result,locale);
		}
		return dateOverNumbers;
	}

	/**
	 * @param processOrderId
	 * @param processWorkflowId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static int getDayDelay(long processOrderId, long processWorkflowId) {

		ActionHistory actionHistoryNewest = new ActionHistoryImpl();

		ProcessWorkflow processWorkflow = new ProcessWorkflowImpl();

		ProcessStep processStep = new ProcessStepImpl();

		int dayDelay = 0;

		try {
			if (processOrderId > 0 && processWorkflowId > 0) {

				actionHistoryNewest = ActionHistoryLocalServiceUtil
						.getActionHistoryByProcessOrderId(processWorkflowId, 1,
								1, false).get(0);

				if (Validator.isNotNull(actionHistoryNewest.getCreateDate())) {

					processWorkflow = ProcessWorkflowLocalServiceUtil
							.getProcessWorkflow(processWorkflowId);

					if (processWorkflow.getPostProcessStepId() > 0) {
						processStep = ProcessStepLocalServiceUtil
								.getProcessStep(processWorkflow
										.getPostProcessStepId());

						if (processStep.getDaysDuration() > 0) {
							dayDelay = checkActionDateOver(
									actionHistoryNewest.getCreateDate(),
									new Date(), processStep.getDaysDuration());
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return dayDelay;

	}

}
