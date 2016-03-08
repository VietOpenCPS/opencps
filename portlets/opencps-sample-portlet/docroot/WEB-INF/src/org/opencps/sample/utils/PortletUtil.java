/*******************************************************************************
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package org.opencps.sample.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author trungnt
 *
 */
public class PortletUtil {
	public static String convertDateToString(Date date, String pattern) {
		DateFormat dateFormat = DateFormatFactoryUtil
				.getSimpleDateFormat(pattern);
		if (date == null || Validator.isNull(pattern)) {
			return StringPool.BLANK;
		}
		dateFormat.setTimeZone(TimeZoneUtil.getDefault());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return dateFormat.format(calendar.getTime());
	}

	public static DateFormat getDateTimeFormat(String pattern) {
		DateFormat dateFormat = DateFormatFactoryUtil
				.getSimpleDateFormat(pattern);
		if (Validator.isNotNull(pattern)) {
			pattern = _VN_DATE_TIME_FORMAT;
		}
		dateFormat.setTimeZone(TimeZoneUtil.getDefault());

		return dateFormat;
	}

	public static final String _TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static final String _VN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
}
