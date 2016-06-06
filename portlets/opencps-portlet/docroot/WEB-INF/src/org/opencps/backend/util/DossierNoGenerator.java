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
import java.util.Random;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;


/**
 * @author khoavd
 *
 */
public class DossierNoGenerator {
	
	/**
	 * Generate noReception with pattern
	 * 
	 * @param pattern
	 * @return
	 */
	public static String genaratorNoReception(String pattern) {
		
		String noReception = StringPool.BLANK;
		
		pattern = StringUtil.lowerCase(pattern);
		pattern = StringUtil.trim(pattern, ' ');
		
		StringBuffer sbNoReception = new StringBuffer(pattern);
		
		Calendar cal = Calendar.getInstance();
		
		String strYearTypeOne = Integer.toString(cal.get(Calendar.YEAR));
		String strYearTypeTwo = Integer.toString(cal.get(Calendar.YEAR)).substring(2);
		
		String strMonth =
		    (cal.get(Calendar.MONTH) + 1) < 10
		        ? "0" + Integer.toString(cal.get(Calendar.MONTH) + 1)
		        : Integer.toString(cal.get(Calendar.MONTH) + 1);
		String strDay =
		    cal.get(Calendar.DAY_OF_MONTH) < 10
		        ? "0" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH))
		        : Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		
		if (_validateParttern(pattern)) {
			int numberSerial = StringUtil.count(pattern, "n");
			
			String serialNumber = noGenarator(numberSerial);
			
			sbNoReception.replace(pattern.indexOf('n') - 1 , pattern.lastIndexOf('n') + 2, serialNumber);
			
			pattern = sbNoReception.toString();
			
			if (pattern.contains(FIX_YEAR_PATTERN_TYPE_1)) {
				pattern = StringUtil.replace(pattern, FIX_YEAR_PATTERN_TYPE_1, strYearTypeOne);
			}
			
			if (StringUtil.contains(pattern, FIX_YEAR_PATTERN_TYPE_2)) {
				pattern = StringUtil.replace(pattern, FIX_YEAR_PATTERN_TYPE_2, strYearTypeTwo);
			}
			
			if (pattern.contains(FIX_MONTH_PATTERN)) {
				pattern = StringUtil.replace(pattern, FIX_MONTH_PATTERN, strMonth);
			}
			
			if (pattern.contains(FIX_DAY_PATTERN)) {
				pattern = StringUtil.replace(pattern, FIX_DAY_PATTERN, strDay);
			}
			
			noReception = pattern;
			
		} else {
			
			StringBuffer sbNoReceptionDefault = new StringBuffer();
			
			String serialNumber = noGenarator(FIX_DEFAULT_SERIAL_NUMBER);
			
			sbNoReceptionDefault.append(strYearTypeOne);
			sbNoReceptionDefault.append(strMonth);
			sbNoReceptionDefault.append(strDay);
			sbNoReceptionDefault.append(serialNumber);
			
			noReception = sbNoReceptionDefault.toString();
			
		}
		
		return noReception;
	}
	
	private static boolean _validateParttern(String pattern) {
		boolean isValidator = true;
		
		pattern = StringUtil.lowerCase(pattern);
		
		if (!pattern.contains(FIX_YEAR_PATTERN_TYPE_1) &&
		    !pattern.contains(FIX_YEAR_PATTERN_TYPE_2)) {
			isValidator = false;
		}
		
		if (!pattern.contains(FIX_MONTH_PATTERN)) {
			isValidator = false;
		}
		
		if (!pattern.contains(FIX_DAY_PATTERN)) {
			isValidator = false;
		}
		
		if (!pattern.contains(FIX_SERIAL_PATERN)) {
			isValidator = false;
		}
		
		return isValidator;
	}

	/**
	 * @return
	 */
	public static String noGenarator(int lengNumber) {
		char[] chars = "012346789".toCharArray();
		
		StringBuilder sb = new StringBuilder();
		
		Random random = new Random();
		
		for (int i = 0; i < lengNumber; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		
		return sb.toString();
	}
	
	public static final String FIX_YEAR_PATTERN_TYPE_1 = "{yyyy}";

	public static final String FIX_YEAR_PATTERN_TYPE_2 = "{yy}";
	
	public static final String FIX_MONTH_PATTERN = "{mm}";
	
	public static final String FIX_DAY_PATTERN = "{dd}";
	
	public static final String FIX_SERIAL_PATERN = "{nn";
	
	public static final int FIX_DEFAULT_SERIAL_NUMBER = 6;
	
}
