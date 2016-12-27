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

package org.opencps.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author trungnt
 */
public class DateTimeUtil {

	public static String convertDateToString(Date date, String pattern) {

		DateFormat dateFormat =
			DateFormatFactoryUtil.getSimpleDateFormat(pattern);
		if (date == null || Validator.isNull(pattern)) {
			return StringPool.BLANK;
		}
		dateFormat.setTimeZone(TimeZoneUtil.getDefault());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return dateFormat.format(calendar.getTime());
	}

	public static DateFormat getDateTimeFormat(String pattern) {

		DateFormat dateFormat =
			DateFormatFactoryUtil.getSimpleDateFormat(pattern);
		if (Validator.isNotNull(pattern)) {
			pattern = _VN_DATE_TIME_FORMAT;
		}
		dateFormat.setTimeZone(TimeZoneUtil.getDefault());

		return dateFormat;
	}

	public int getDayFromDate(Date date) {

		int day = 1;

		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			day = calendar.get(Calendar.DAY_OF_MONTH);

			calendar.setTime(date);
			day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		return day;
	}

	public int getMonthFromDate(Date date) {

		int month = 1;

		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			month = calendar.get(Calendar.MONTH);

			calendar.setTime(date);
			month = calendar.get(Calendar.MONTH);
		}

		return month;
	}

	public int getYearFromDate(Date date) {

		int year = 1990;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			year = calendar.get(Calendar.YEAR);

			calendar.setTime(date);
			year = calendar.get(Calendar.YEAR);
		}

		return year;
	}

	public static Date convertStringToDate(String strDate) {

		DateFormat df = getDateTimeFormat(_VN_DATE_FORMAT);
		Date date = null;
		try {
			if (Validator.isNotNull(strDate)) {
				date = df.parse(strDate);
			}

		}
		catch (ParseException e) {
			_log.error(e);
		}
		return date;
	}
	
	public static Date convertStringToFullDate(String strDate) {
		
		DateFormat df = getDateTimeFormat(_VN_DATE_TIME_FORMAT);
		Date date = null;
		try {
			if (Validator.isNotNull(strDate)) {
				date = df.parse(strDate);
			}

		}
		catch (ParseException e) {
			_log.error(e);
		}
		return date;
	}

	public static Date getDate(int day, int month, int year) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

	public static String getStringDate() {

		Calendar calendar = Calendar.getInstance();

		StringBuffer sb = new StringBuffer();

		int month = calendar.get(Calendar.MONTH) + 1;

		int day = calendar.get(Calendar.DAY_OF_MONTH);

		sb.append(calendar.get(Calendar.YEAR));

		if (month < 10) {
			sb.append(0);
			sb.append(month);
		}
		else {
			sb.append(month);
		}

		if (day < 10) {
			sb.append(0);
			sb.append(day);
		}
		else {
			sb.append(day);
		}

		return sb.toString();
	}

	public static Calendar getInstance(Date date, int... ignores) {

		Calendar calendar = Calendar.getInstance();
		if (ignores != null && ignores.length > 0) {
			for (int f = 0; f < ignores.length; f++) {
				calendar.set(ignores[f], 0);
			}
		}
		return calendar;
	}
	
	public static int convertTimemilisecondsToDays(long time) {

		int days = 0;
		days = (int) (time / (24 * 60 * 60 * 1000));

		return days;
	}

	public static long convertTimemilisecondsToHours(long time) {

		long hours = 0;

		hours = time / (60 * 60 * 1000);

		return hours;
	}

	public static long convertTimemilisecondsToMinutes(long time) {

		long minutes = 0;

		minutes = time / (60 * 1000);

		return minutes;
	}

	public static long convertTimemilisecondsToSeconds(long time) {

		long seconds = 0;

		seconds = time / 1000;

		return seconds;
	}

	public static String convertTimemilisecondsToFormat(long time, Locale locale) {

		String format = new String(DATE_TIME_FORMAT);
		String day = LanguageUtil.get(locale, "day");
		
		format = format.replace("{D}", day);
		
		long diffSeconds = 0;
		long diffMinutes = 0;
		long diffHours = 0;
		long diffDays = 0;
		
		diffSeconds = time / 1000 % 60;
		diffMinutes = time / (60 * 1000) % 60;
		diffHours = time / (60 * 60 * 1000) % 24;
		diffDays = time / (24 * 60 * 60 * 1000);

		format = StringUtil.replace(format, "{d}", String.valueOf(diffDays));
		format = StringUtil.replace(format, "{HH}", String.valueOf(diffHours));
		format = StringUtil.replace(format, "{mm}", String.valueOf(diffMinutes));
		format = StringUtil.replace(format, "{ss}", String.valueOf(diffSeconds));

		return format;
	}
	private static final String DATE_TIME_FORMAT = "{d} {D} {HH}:{mm}:{ss}";

	public static final String _TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static final String _VN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

	public static final String _VN_DATE_FORMAT = "dd/MM/yyyy";

	public static final String _EMPTY_DATE_TIME = "__/__/__";
	
	public static final String _DATE_TIME_TO_NAME = "yyyyMMdd";

	private static Log _log = LogFactoryUtil.getLog(DateTimeUtil.class);
}
