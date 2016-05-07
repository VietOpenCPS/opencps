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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictCollectionLocalServiceUtil;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author trungnt
 */
public class PortletUtil {

	public static class SplitName {

		public SplitName(String fullName) {

			_firstName = StringPool.BLANK;
			_lastName = StringPool.BLANK;
			_midName = StringPool.BLANK;

			if (Validator
			    .isNotNull(fullName)) {
				String[] splitNames = StringUtil
				    .split(fullName, StringPool.SPACE);
				if (splitNames != null && splitNames.length > 0) {
					_lastName = splitNames[0];

					_firstName = splitNames[splitNames.length - 1];

					if (splitNames.length >= 3) {
						for (int i = 1; i < splitNames.length - 1; i++) {
							_midName += splitNames[i] + StringPool.SPACE;
						}
					}
					this
					    .setLastName(_lastName);
					this
					    .setFirstName(_firstName);
					this
					    .setMidName(_midName);
				}
			}
		}

		public String getFirstName() {

			return _firstName;
		}

		public void setFirstName(String firstName) {

			this._firstName = firstName;
		}

		public String getLastName() {

			return _lastName;
		}

		public void setLastName(String lastName) {

			this._lastName = lastName;
		}

		public String getMidName() {

			return _midName;
		}

		public void setMidName(String midName) {

			this._midName = midName;
		}

		private String _firstName;
		private String _lastName;
		private String _midName;
	}

	public static class SplitDate {

		public SplitDate(Date date) {

			if (date != null) {
				Calendar calendar = Calendar
				    .getInstance();
				calendar
				    .setTime(date);

				_miniSecond = calendar
				    .get(Calendar.MILLISECOND);
				_second = calendar
				    .get(Calendar.SECOND);
				_minute = calendar
				    .get(Calendar.MINUTE);
				_hour = calendar
				    .get(Calendar.HOUR);
				_dayOfMoth = calendar
				    .get(Calendar.DAY_OF_MONTH);
				_dayOfYear = calendar
				    .get(Calendar.DAY_OF_YEAR);
				_weekOfMonth = calendar
				    .get(Calendar.WEEK_OF_MONTH);
				_weekOfYear = calendar
				    .get(Calendar.WEEK_OF_YEAR);
				_month = calendar
				    .get(Calendar.MONTH);
				_year = calendar
				    .get(Calendar.YEAR);
			}
		}

		public int getMiniSecond() {

			return _miniSecond;
		}

		public void setMiniSecond(int _miniSecond) {

			this._miniSecond = _miniSecond;
		}

		public int getSecond() {

			return _second;
		}

		public void setSecond(int _second) {

			this._second = _second;
		}

		public int getMinute() {

			return _minute;
		}

		public void setMinute(int _minute) {

			this._minute = _minute;
		}

		public int getHour() {

			return _hour;
		}

		public void setHour(int _hour) {

			this._hour = _hour;
		}

		public int getDayOfMoth() {

			return _dayOfMoth;
		}

		public void setDayOfMoth(int _dayOfMoth) {

			this._dayOfMoth = _dayOfMoth;
		}

		public int getDayOfYear() {

			return _dayOfYear;
		}

		public void setDayOfYear(int _dayOfYear) {

			this._dayOfYear = _dayOfYear;
		}

		public int getWeekOfMonth() {

			return _weekOfMonth;
		}

		public void setWeekOfMonth(int _weekOfMonth) {

			this._weekOfMonth = _weekOfMonth;
		}

		public int getWeekOfYear() {

			return _weekOfYear;
		}

		public void setWeekOfYear(int _weekOfYear) {

			this._weekOfYear = _weekOfYear;
		}

		public int getMonth() {

			return _month;
		}

		public void setMonth(int _month) {

			this._month = _month;
		}

		public int getYear() {

			return _year;
		}

		public void setYear(int _year) {

			this._year = _year;
		}

		private int _miniSecond;
		private int _second;
		private int _minute;
		private int _hour;
		private int _dayOfMoth;
		private int _dayOfYear;
		private int _weekOfMonth;
		private int _weekOfYear;
		private int _month;
		private int _year;
	}

	public static SplitDate splitDate(Date date) {

		return new SplitDate(date);
	};

	public static SplitName splitName(String fullName) {

		return new SplitName(fullName);
	};

	public static String getGender(int value, Locale locale) {

		String genderLabel = StringPool.BLANK;

		switch (value) {
		case 0:
			genderLabel = LanguageUtil
			    .get(locale, "female");
			break;
		case 1:
			genderLabel = LanguageUtil
			    .get(locale, "male");
			break;
		default:
			genderLabel = LanguageUtil
			    .get(locale, "male");
			break;
		}

		return genderLabel;
	}

	public static String getAccountStatus(int value, Locale locale) {

		String accountStatus = StringPool.BLANK;

		switch (value) {
		case 0:
			accountStatus = LanguageUtil
			    .get(locale, "registered");
			break;
		case 1:
			accountStatus = LanguageUtil
			    .get(locale, "confirmed");
			break;
		case 2:
			accountStatus = LanguageUtil
			    .get(locale, "approved");
			break;
		case 3:
			accountStatus = LanguageUtil
			    .get(locale, "locked");
			break;
		default:
			accountStatus = LanguageUtil
			    .get(locale, "");
			break;

		}

		return accountStatus;
	}

	public static int changeAccountStatus(int value) {

		int changeAccountStatus = 0;

		switch (value) {
		case 0:
			changeAccountStatus = 4; // huy
			break;
		case 1:
			changeAccountStatus = 2; // xac nhan;
			break;
		case 2:
			changeAccountStatus = 3; // khoa
			break;
		case 3:
			changeAccountStatus = 1; // mo lai;
			break;
		default:
			changeAccountStatus = 0;
			break;
		}

		return changeAccountStatus;
	}

	public static String getLeaderLabel(int value, Locale locale) {

		String leaderLabel = StringPool.BLANK;

		switch (value) {
		case 0:
			leaderLabel = LanguageUtil
			    .get(locale, "normal");
			break;
		case 1:
			leaderLabel = LanguageUtil
			    .get(locale, "leader");
			break;
		case 2:
			leaderLabel = LanguageUtil
			    .get(locale, "deputy");
			break;
		default:
			leaderLabel = LanguageUtil
			    .get(locale, "normal");
			break;
		}

		return leaderLabel;
	}

	public static String getDossierStatusLabel(int value, Locale locale) {

		String statusLabel = StringPool.BLANK;

		switch (value) {
		case 0:
			statusLabel = LanguageUtil
			    .get(locale, "new");
			break;
		case 1:
			statusLabel = LanguageUtil
			    .get(locale, "receiving");
			break;
		case 2:
			statusLabel = LanguageUtil
			    .get(locale, "waiting");
			break;
		case 3:
			statusLabel = LanguageUtil
			    .get(locale, "paying");
			break;
		case 4:
			statusLabel = LanguageUtil
			    .get(locale, "denied");
			break;
		case 5:
			statusLabel = LanguageUtil
			    .get(locale, "received");
			break;
		case 6:
			statusLabel = LanguageUtil
			    .get(locale, "processing");
			break;
		case 7:
			statusLabel = LanguageUtil
			    .get(locale, "canceled");
			break;
		case 8:
			statusLabel = LanguageUtil
			    .get(locale, "done");
			break;
		case 9:
			statusLabel = LanguageUtil
			    .get(locale, "archived");
			break;
		case 10:
			statusLabel = LanguageUtil
			    .get(locale, "system");
			break;
		case 11:
			statusLabel = LanguageUtil
			    .get(locale, "error");
			break;
		default:
			statusLabel = LanguageUtil
			    .get(locale, "new");
			break;
		}

		return statusLabel;
	}

	public static List<Integer> getDossierStatus() {

		List<Integer> dossierStatus = new ArrayList<Integer>();

		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_NEW);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_RECEIVING);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_WAITING);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_PAYING);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_DENIED);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_RECEIVED);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_PROCESSING);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_CANCELED);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_DONE);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_ARCHIVED);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_SYSTEM);
		dossierStatus
		    .add(PortletConstants.DOSSIER_STATUS_ERROR);

		return dossierStatus;
	}

	public static String getDestinationFolder(String[] folderNames) {

		return StringUtil
		    .merge(folderNames, StringPool.FORWARD_SLASH);
	}

	public static String getCitizenDossierDestinationFolder(
	    long groupId, long userId) {

		return String
		    .valueOf(groupId) + StringPool.SLASH +
		    "opencps/dossierfiles/citizen" + StringPool.SLASH + String
		        .valueOf(userId);
	}

	public static String getBusinessDossierDestinationFolder(
	    long groupId, long orgId) {

		return String
		    .valueOf(groupId) + StringPool.SLASH +
		    "opencps/dossierfiles/business" + StringPool.SLASH + String
		        .valueOf(orgId);
	}

	public static DictItem getDictItem(
	    String collectionCode, String itemCode, long groupId) {

		DictCollection dictCollection = null;
		DictItem dictItem = null;

		try {
			dictCollection = DictCollectionLocalServiceUtil
			    .getDictCollection(groupId, collectionCode);
			dictItem = DictItemLocalServiceUtil
			    .getDictItemInuseByItemCode(dictCollection
			        .getDictCollectionId(), itemCode);
		}
		catch (Exception e) {
			// TODO: nothing
		}
		return dictItem;
	}

	public static String getActionInfo(int status, Locale locale) {

		String actionInfo = StringPool.BLANK;
		switch (status) {
		case 0:
			actionInfo = LanguageUtil
			    .get(locale, "new", "Create new");
			break;
		case 1:
			actionInfo = LanguageUtil
			    .get(locale, "receiving", "Receiving");
			break;
		case 2:
			actionInfo = LanguageUtil
			    .get(locale, "waiting", "Waiting");
			break;
		case 3:
			actionInfo = LanguageUtil
			    .get(locale, "paying", "Paying");
			break;
		case 4:
			actionInfo = LanguageUtil
			    .get(locale, "denied", "Denied");
			break;
		case 5:
			actionInfo = LanguageUtil
			    .get(locale, "received", "Received");
			break;
		case 6:
			actionInfo = LanguageUtil
			    .get(locale, "processing", "Processing");
			break;
		case 7:
			actionInfo = LanguageUtil
			    .get(locale, "canceled", "Canceled");
			break;
		case 8:
			actionInfo = LanguageUtil
			    .get(locale, "done", "Done");
			break;
		case 9:
			actionInfo = LanguageUtil
			    .get(locale, "archived", "Archived");
			break;
		case 10:
			actionInfo = LanguageUtil
			    .get(locale, "system", "System");
			break;
		case 11:
			actionInfo = LanguageUtil
			    .get(locale, "error", "Error");
			break;
		default:
			break;
		}
		return actionInfo;
	}
	
	public static String getMessageInfo(int status, Locale locale) {

		String messageInfo = StringPool.BLANK;
		switch (status) {
		case 0:
			messageInfo = LanguageUtil
			    .get(locale, "new-msg", "Create new");
			break;
		case 1:
			messageInfo = LanguageUtil
			    .get(locale, "receiving-msg", "Receiving");
			break;
		case 2:
			messageInfo = LanguageUtil
			    .get(locale, "waiting-msg", "Waiting");
			break;
		case 3:
			messageInfo = LanguageUtil
			    .get(locale, "paying-msg", "Paying");
			break;
		case 4:
			messageInfo = LanguageUtil
			    .get(locale, "denied-msg", "Denied");
			break;
		case 5:
			messageInfo = LanguageUtil
			    .get(locale, "received-msg", "Received");
			break;
		case 6:
			messageInfo = LanguageUtil
			    .get(locale, "processing-msg", "Processing");
			break;
		case 7:
			messageInfo = LanguageUtil
			    .get(locale, "canceled-msg", "Canceled");
			break;
		case 8:
			messageInfo = LanguageUtil
			    .get(locale, "done-msg", "Done");
			break;
		case 9:
			messageInfo = LanguageUtil
			    .get(locale, "archived-msg", "Archived");
			break;
		case 10:
			messageInfo = LanguageUtil
			    .get(locale, "system-msg", "System");
			break;
		case 11:
			messageInfo = LanguageUtil
			    .get(locale, "error-msg", "Error");
			break;
		default:
			break;
		}
		return messageInfo;
	}
}
