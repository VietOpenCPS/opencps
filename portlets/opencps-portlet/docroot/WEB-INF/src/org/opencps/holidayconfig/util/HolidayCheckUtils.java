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

public class HolidayCheckUtils{
	


	/**
	 * @param startDate
	 * @param endDate
	 * @param daysDuration
	 * @return
	 */
	public static int checkActionDateOver(
	    Date startDate, Date endDate, int daysDuration) {

		int dateOverNumbers = 0;

		if (daysDuration > 0) {

			Calendar endDayCal = Calendar.getInstance();
			endDayCal.setTime(endDate);

			Calendar endDateMax =
			    HolidayUtils.getEndDate(startDate, daysDuration);

			int endDay = endDayCal.get(Calendar.DATE);
			int endDayMax = endDateMax.get(Calendar.DATE);

			dateOverNumbers = endDayMax - endDay;

			if (dateOverNumbers > 0) {
				return 0;
			}
			else {
				return Math.abs(dateOverNumbers);
			}
		}
		return dateOverNumbers;
	}
	
}