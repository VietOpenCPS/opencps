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

import java.util.Calendar;
import java.util.Date;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;


/**
 * @author khoavd
 *
 */
public class BookingDateGenerator {
	
	/**
	 * Generate Booking date
	 * 
	 * @param receiveDate
	 * @param pattern
	 * @return
	 */
	public static Date dateGenerator(Date receiveDate, String pattern) {
		
		Calendar cal = Calendar.getInstance();
		
		
		
		cal.setTime(receiveDate);
		
		System.out.println("REVICE DATE ****************************************************" + receiveDate);
		
		String [] splitPattern = StringUtil.split(pattern, StringPool.SPACE);
		
		int bookingDays = 0;
		int bookingHour = 0;
		int bookingMinutes = 0;
		
		if (splitPattern.length == 2) {
		
			bookingDays = GetterUtil.getInteger(splitPattern[0],0);
			
			String [] splitHour = StringUtil.split(splitPattern[1], StringPool.COLON);
			
			if (splitHour.length == 2) {
				bookingHour = GetterUtil.getInteger(splitHour[0]);
				bookingMinutes = GetterUtil.getInteger(splitHour[1]);
			}
		}
		
		cal.add(Calendar.DATE, bookingDays);
		cal.add(Calendar.HOUR, bookingHour);
		cal.add(Calendar.MINUTE, bookingMinutes);
		
		return cal.getTime();
		
	}
	
	public static void main(String[] args) {
		
		Date now = new Date();

	    Date bookingDate = dateGenerator(now, "+1 7:33");
	    
	    System.out.println(now);

	    System.out.println(bookingDate);
    }
	
}
