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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.naming.Context;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencps.datamgt.model.AdministrationServicedomain;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.AdministrationServicedomainLocalServiceUtil;
import org.opencps.datamgt.service.DictCollectionLocalServiceUtil;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.paymentmgt.util.PaymentMgtUtil;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;

/**
 * @author trungnt
 */
public class PortletUtil {

	public static class SplitName {

		public SplitName(String fullName) {

			_firstName = StringPool.BLANK;
			_lastName = StringPool.BLANK;
			_midName = StringPool.BLANK;

			if (Validator.isNotNull(fullName)) {

				// Comment by TrungNT. Only set first name equal fullName

				/*
				 * String[] splitNames = StringUtil .split(fullName,
				 * StringPool.SPACE); if (splitNames != null &&
				 * splitNames.length > 0) { _lastName = splitNames[0];
				 * _firstName = splitNames[splitNames.length - 1]; if
				 * (splitNames.length >= 3) { for (int i = 1; i <
				 * splitNames.length - 1; i++) { _midName += splitNames[i] +
				 * StringPool.SPACE; } } this .setLastName(_lastName); this
				 * .setFirstName(_firstName); this .setMidName(_midName); }
				 */

				this.setLastName(StringPool.BLANK);
				this.setFirstName(fullName);
				this.setMidName(StringPool.BLANK);
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
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				_miniSecond = calendar.get(Calendar.MILLISECOND);
				_second = calendar.get(Calendar.SECOND);
				_minute = calendar.get(Calendar.MINUTE);
				_hour = calendar.get(Calendar.HOUR);
				_dayOfMoth = calendar.get(Calendar.DAY_OF_MONTH);
				_dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
				_weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
				_weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
				_month = calendar.get(Calendar.MONTH);
				_year = calendar.get(Calendar.YEAR);
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
			genderLabel = LanguageUtil.get(locale, "female");
			break;
		case 1:
			genderLabel = LanguageUtil.get(locale, "male");
			break;
		default:
			genderLabel = LanguageUtil.get(locale, "male");
			break;
		}

		return genderLabel;
	}

	public static String getAccountStatus(int value, Locale locale) {

		String accountStatus = StringPool.BLANK;

		switch (value) {
		case 0:
			accountStatus = LanguageUtil.get(locale, "registered");
			break;
		case 1:
			accountStatus = LanguageUtil.get(locale, "confirmed");
			break;
		case 2:
			accountStatus = LanguageUtil.get(locale, "approved");
			break;
		case 3:
			accountStatus = LanguageUtil.get(locale, "locked");
			break;
		default:
			accountStatus = LanguageUtil.get(locale, "");
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
			leaderLabel = LanguageUtil.get(locale, "normal");
			break;
		case 1:
			leaderLabel = LanguageUtil.get(locale, "leader");
			break;
		case 2:
			leaderLabel = LanguageUtil.get(locale, "deputy");
			break;
		default:
			leaderLabel = LanguageUtil.get(locale, "normal");
			break;
		}

		return leaderLabel;
	}

	public static String getDossierStatusLabel(String itemCode, Locale locale) {

		String name = StringPool.BLANK;
		DictItem dictItem = null;

		try {
			dictItem = DictItemLocalServiceUtil.getDictItemByCode(itemCode);
			if (Validator.isNotNull(dictItem)) {
				name = dictItem.getItemName(locale);
			}
		}
		catch (Exception e) {
			_log.error(e);
		}

		return name;
	}

	public static List<DictItem> getDossierStatus(long groupId) {

		DictCollection dictCollection = null;
		List<DictItem> result = new ArrayList<DictItem>();
		try {
			dictCollection =
				DictCollectionLocalServiceUtil.getDictCollection(
					groupId, "DOSSIER_STATUS");
			if (Validator.isNotNull(dictCollection)) {
				result =
					DictItemLocalServiceUtil.getDictItemsByDictCollectionId(dictCollection.getDictCollectionId());
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public static List<DictItem> getDictItemInUseByCode(
		long groupId, String dictCollectionCode, String itemCode) {

		DictCollection dictCollection = null;
		List<DictItem> result = new ArrayList<DictItem>();
		try {
			dictCollection =
				DictCollectionLocalServiceUtil.getDictCollection(
					groupId, dictCollectionCode);
			if (Validator.isNotNull(dictCollection) &&
				(Validator.isNull(itemCode) || itemCode.equals(PortletConstants.TREE_VIEW_DEFAULT_ITEM_CODE))) {
				result =
					DictItemLocalServiceUtil.getDictItemsInUseByDictCollectionIdAndParentItemId(
						dictCollection.getDictCollectionId(), 0);
			}
			else if (Validator.isNotNull(dictCollection) &&
				(Validator.isNotNull(itemCode) && itemCode.equals(PortletConstants.TREE_VIEW_ALL_ITEM))) {
				result =
					DictItemLocalServiceUtil.getDictItemsInUseByDictCollectionId(dictCollection.getDictCollectionId());
			}
			else {
				// TODO
				// get treedata from itemCode
				DictItem dictItem =
					DictItemLocalServiceUtil.getDictItemInuseByItemCode(
						dictCollection.getDictCollectionId(), itemCode);

				result =
					DictItemLocalServiceUtil.getDictItemsByTreeIndex(
						dictItem.getDictItemId(), dictItem.getParentItemId(),
						1, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public static List<DictItem> getDictItemInUseByCodeMappingAdminCode(
		long groupId, String dictCollectionCode, String itemCode) {

		List<DictItem> result = new ArrayList<DictItem>();

		List<AdministrationServicedomain> adServicedomains =
			new ArrayList<AdministrationServicedomain>();
		try {

			adServicedomains =
				AdministrationServicedomainLocalServiceUtil.getMappingAdministrationCode(
					groupId, dictCollectionCode, itemCode);

			DictItem dictItem = null;
			for (AdministrationServicedomain administrationServicedomain : adServicedomains) {

				dictItem =
					DictItemLocalServiceUtil.getDictItemInuseByItemCode(
						administrationServicedomain.getGroupId(),
						administrationServicedomain.getServiceDomainCollectionCode(),
						administrationServicedomain.getServiceDomainCode());
				if (Validator.isNotNull(dictItem)) {

					result.add(dictItem);

				}

			}

		}
		catch (Exception e) {
			// TODO: handle exception
			_log.error(e);
		}
		return result;
	}

	public static String getDestinationFolder(String[] folderNames) {

		return StringUtil.merge(folderNames, StringPool.FORWARD_SLASH);
	}

	@Deprecated
	public static String getCitizenDossierDestinationFolder(
		long groupId, long userId) {

		return String.valueOf(groupId) + StringPool.SLASH +
			"opencps/dossierfiles/citizen" + StringPool.SLASH +
			String.valueOf(userId);
	}

	@Deprecated
	public static String getBusinessDossierDestinationFolder(
		long groupId, long orgId) {

		return String.valueOf(groupId) + StringPool.SLASH +
			"opencps/dossierfiles/business" + StringPool.SLASH +
			String.valueOf(orgId);
	}

	public static String getDossierDestinationFolder(
		long groupId, int year, int month, int day) {

		return String.valueOf(groupId) + StringPool.SLASH + "Dossiers" +
			StringPool.SLASH + String.valueOf(year) + StringPool.SLASH +
			String.valueOf(month) + StringPool.SLASH + String.valueOf(day);
	}

	public static String getDossierDestinationFolder(
		long groupId, int year, int month, int day, String oid) {

		return String.valueOf(groupId) + StringPool.SLASH + "Dossiers" +
			StringPool.SLASH + String.valueOf(year) + StringPool.SLASH +
			String.valueOf(month) + StringPool.SLASH + String.valueOf(day) +
			StringPool.SLASH + oid;
	}

	public static String getSyncPaymentDestinationFolder(
		long groupId, int year, int month, int day, String oId) {

		return String.valueOf(groupId) + StringPool.SLASH + "Payments" +
			StringPool.SLASH + "SYNC" + StringPool.SLASH +
			String.valueOf(year) + StringPool.SLASH + String.valueOf(month) +
			StringPool.SLASH + String.valueOf(day) + StringPool.SLASH +
			String.valueOf(oId);
	}

	public static String getPaymentDestinationFolder(
		long groupId, int year, int month, int day, long ownId,
		String accountType) {

		return String.valueOf(groupId) + StringPool.SLASH + "Payments" +
			StringPool.SLASH + accountType + StringPool.SLASH +
			String.valueOf(year) + StringPool.SLASH + String.valueOf(month) +
			StringPool.SLASH + String.valueOf(day) + StringPool.SLASH +
			String.valueOf(ownId);
	}

	public static DictItem getDictItem(
		String collectionCode, String itemCode, long groupId) {

		DictCollection dictCollection = null;
		DictItem dictItem = null;

		try {
			dictCollection =
				DictCollectionLocalServiceUtil.getDictCollection(
					groupId, collectionCode);
			dictItem =
				DictItemLocalServiceUtil.getDictItemInuseByItemCode(
					dictCollection.getDictCollectionId(), itemCode);
		}
		catch (Exception e) {
			// TODO: nothing
		}
		return dictItem;
	}

	public static String getActionInfoByKey(String status, Locale locale) {

		String actionInfo = StringPool.BLANK;
		switch (status) {
		case PortletConstants.DOSSIER_STATUS_NEW:
			actionInfo = LanguageUtil.get(locale, "new-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_RECEIVING:
			actionInfo = LanguageUtil.get(locale, "receiving-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_WAITING:
			actionInfo = LanguageUtil.get(locale, "waiting-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_PAYING:
			actionInfo = LanguageUtil.get(locale, "paying-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_DENIED:
			actionInfo = LanguageUtil.get(locale, "denied-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_RECEIVED:
			actionInfo = LanguageUtil.get(locale, "received-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_PROCESSING:
			actionInfo = LanguageUtil.get(locale, "processing-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_CANCELED:
			actionInfo = LanguageUtil.get(locale, "canceled-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_DONE:
			actionInfo = LanguageUtil.get(locale, "done-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_ARCHIVED:
			actionInfo = LanguageUtil.get(locale, "archived-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_SYSTEM:
			actionInfo = LanguageUtil.get(locale, "system-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_ENDED:
			actionInfo = LanguageUtil.get(locale, "ended-stt");
			break;
		case PortletConstants.DOSSIER_STATUS_ERROR:
			actionInfo = LanguageUtil.get(locale, "error-stt");
			break;
		default:
			break;
		}
		return actionInfo;
	}

	public static String getActionInfo(String status, Locale locale) {

		String actionInfo = StringPool.BLANK;
		switch (status) {
		case PortletConstants.DOSSIER_STATUS_NEW:
			actionInfo = LanguageUtil.get(locale, "new", "Create new");
			break;
		case PortletConstants.DOSSIER_STATUS_RECEIVING:
			actionInfo = LanguageUtil.get(locale, "receiving", "Receiving");
			break;
		case PortletConstants.DOSSIER_STATUS_WAITING:
			actionInfo = LanguageUtil.get(locale, "waiting", "Waiting");
			break;
		case PortletConstants.DOSSIER_STATUS_PAYING:
			actionInfo = LanguageUtil.get(locale, "paying", "Paying");
			break;
		case PortletConstants.DOSSIER_STATUS_DENIED:
			actionInfo = LanguageUtil.get(locale, "denied", "Denied");
			break;
		case PortletConstants.DOSSIER_STATUS_RECEIVED:
			actionInfo = LanguageUtil.get(locale, "received", "Received");
			break;
		case PortletConstants.DOSSIER_STATUS_PROCESSING:
			actionInfo = LanguageUtil.get(locale, "processing", "Processing");
			break;
		case PortletConstants.DOSSIER_STATUS_CANCELED:
			actionInfo = LanguageUtil.get(locale, "canceled", "Canceled");
			break;
		case PortletConstants.DOSSIER_STATUS_DONE:
			actionInfo = LanguageUtil.get(locale, "done", "Done");
			break;
		case PortletConstants.DOSSIER_STATUS_ARCHIVED:
			actionInfo = LanguageUtil.get(locale, "archived", "Archived");
			break;
		case PortletConstants.DOSSIER_STATUS_SYSTEM:
			actionInfo = LanguageUtil.get(locale, "system", "System");
			break;
		case PortletConstants.DOSSIER_STATUS_ENDED:
			actionInfo = LanguageUtil.get(locale, "ended", "Ended");
			break;
		case PortletConstants.DOSSIER_STATUS_ERROR:
			actionInfo = LanguageUtil.get(locale, "error", "Error");
			break;
		default:
			break;
		}
		return actionInfo;
	}

	public static String getMessageInfo(String status, Locale locale) {

		String messageInfo = StringPool.BLANK;
		switch (status) {
		case PortletConstants.DOSSIER_STATUS_NEW:
			messageInfo = LanguageUtil.get(locale, "new-msg", "Create new");
			break;
		case PortletConstants.DOSSIER_STATUS_RECEIVING:
			messageInfo =
				LanguageUtil.get(locale, "receiving-msg", "Receiving");
			break;
		case PortletConstants.DOSSIER_STATUS_WAITING:
			messageInfo = LanguageUtil.get(locale, "waiting-msg", "Waiting");
			break;
		case PortletConstants.DOSSIER_STATUS_PAYING:
			messageInfo = LanguageUtil.get(locale, "paying-msg", "Paying");
			break;
		case PortletConstants.DOSSIER_STATUS_DENIED:
			messageInfo = LanguageUtil.get(locale, "denied-msg", "Denied");
			break;
		case PortletConstants.DOSSIER_STATUS_RECEIVED:
			messageInfo = LanguageUtil.get(locale, "received-msg", "Received");
			break;
		case PortletConstants.DOSSIER_STATUS_PROCESSING:
			messageInfo =
				LanguageUtil.get(locale, "processing-msg", "Processing");
			break;
		case PortletConstants.DOSSIER_STATUS_CANCELED:
			messageInfo = LanguageUtil.get(locale, "canceled-msg", "Canceled");
			break;
		case PortletConstants.DOSSIER_STATUS_DONE:
			messageInfo = LanguageUtil.get(locale, "done-msg", "Done");
			break;
		case PortletConstants.DOSSIER_STATUS_ARCHIVED:
			messageInfo = LanguageUtil.get(locale, "archived-msg", "Archived");
			break;
		case PortletConstants.DOSSIER_STATUS_SYSTEM:
			messageInfo = LanguageUtil.get(locale, "system-msg", "System");
			break;
		case PortletConstants.DOSSIER_STATUS_ENDED:
			messageInfo = LanguageUtil.get(locale, "ended-msg", "Ended");
			break;
		case PortletConstants.DOSSIER_STATUS_ERROR:
			messageInfo = LanguageUtil.get(locale, "error-msg", "Error");
			break;
		default:
			break;
		}
		return messageInfo;
	}

	public static void writeJSON(
		ActionRequest actionRequest, ActionResponse actionResponse, Object json)
		throws IOException {

		HttpServletResponse response =
			PortalUtil.getHttpServletResponse(actionResponse);

		response.setContentType(ContentTypes.APPLICATION_JSON);

		ServletResponseUtil.write(response, json.toString());
		response.flushBuffer();

	}

	public static String getPaymentMethodLabel(int value, Locale locale) {

		String statusLabel = StringPool.BLANK;

		switch (value) {
		case PaymentMgtUtil.PAYMENT_METHOD_CASH:
			statusLabel = LanguageUtil.get(locale, "cash");
			break;
		case PaymentMgtUtil.PAYMENT_METHOD_KEYPAY:
			statusLabel = LanguageUtil.get(locale, "keypay");
			break;
		case PaymentMgtUtil.PAYMENT_METHOD_BANK:
			statusLabel = LanguageUtil.get(locale, "chuyen-khoan");
			break;
		default:
			statusLabel = LanguageUtil.get(locale, "cash");
			break;
		}

		return statusLabel;
	}

	public static String getEmployeeDestinationFolder(long groupId, long userId) {

		return String.valueOf(groupId) + StringPool.SLASH +
			"opencps/processmgtfiles/employee" + StringPool.SLASH +
			String.valueOf(userId);
	}

	public static String getPaymentStatusLabel(int value, Locale locale) {

		String statusLabel = StringPool.BLANK;

		switch (value) {

		case PaymentMgtUtil.PAYMENT_STATUS_REQUESTED:
			statusLabel = LanguageUtil.get(locale, "requested");
			break;
		case PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED:
			statusLabel = LanguageUtil.get(locale, "confirmed");
			break;
		case PaymentMgtUtil.PAYMENT_STATUS_APPROVED:
			statusLabel = LanguageUtil.get(locale, "approved");
			break;
		case PaymentMgtUtil.PAYMENT_STATUS_REJECTED:
			statusLabel = LanguageUtil.get(locale, "rejected");
			break;
		default:
			statusLabel = LanguageUtil.get(locale, "on-processing");
			break;
		}

		return statusLabel;
	}

	public static String getContextPath(ResourceRequest request) {

		return request.getPortletSession().getPortletContext().getRealPath("/").replace(
			"/", File.separator).replace(File.separator + ".", "");

	}

	public static String getContextPath(HttpServletRequest request) {

		return request.getSession().getServletContext().getRealPath("/").replace(
			"/", File.separator).replace(File.separator + ".", "");

	}

	public static String getContextPath(ActionRequest actionRequest) {

		HttpServletRequest request =
			PortalUtil.getHttpServletRequest(actionRequest);

		return request.getSession().getServletContext().getRealPath("/").replace(
			"/", File.separator).replace(File.separator + ".", "");

	}

	public static String getTempFolderPath(ActionRequest actionRequest) {

		return getContextPath(actionRequest) + "temp/";
	}

	public static String getTempFolderPath(HttpServletRequest request) {

		return getContextPath(request) + "temp/";
	}

	public static String getTempFolderPath(ResourceRequest resourceRequest) {

		return getContextPath(resourceRequest) + "temp/";
	}

	public static String getResourceFolderPath(ActionRequest actionRequest) {

		return getContextPath(actionRequest) + "resources/";
	}

	public static String getResourceFolderPath(HttpServletRequest request) {

		return getContextPath(request) + "resources/";
	}

	public static String getResourceFolderPath(ResourceRequest resourceRequest) {

		return getContextPath(resourceRequest) + "resources/";
	}

	public static boolean checkJMSConfig(long companyId) {

		try {
			PortletPreferences preferences =
				PrefsPropsUtil.getPreferences(companyId, true);
			String jmsJSON =
				GetterUtil.getString(preferences.getValue(
					WebKeys.JMS_CONFIGURATION, StringPool.BLANK));

			if (Validator.isNotNull(jmsJSON)) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SystemException e) {
			return false;
		}
	}

	public static Properties getJMSContextProperties(
		long companyId, String code, boolean remote, String channelName,
		String queueName, String lookup, String mom)
		throws SystemException {

		Properties properties = null;

		PortletPreferences preferences =
			PrefsPropsUtil.getPreferences(companyId, true);

		// Get jms json from configuration
		String jmsJSON =
			GetterUtil.getString(preferences.getValue(
				WebKeys.JMS_CONFIGURATION, StringPool.BLANK));

		// _log.info("(PortletUtil.getJMSContextProperties) - jmsJson: " +
		// jmsJSON);

		try {

			properties = new Properties();
			// Create json object from string
			JSONObject jmsJSONObject =
				JSONFactoryUtil.createJSONObject(jmsJSON);

			// Get mom configuration
			JSONObject momObject = jmsJSONObject.getJSONObject(mom);

			// Get lookup configuration
			JSONObject lookupObject = momObject.getJSONObject(lookup);

			JSONObject jsmServerCnfObject = null;

			if (remote && lookup.equals("remote")) {
				// Get jms remote server by gov agency code
				jsmServerCnfObject = lookupObject.getJSONObject(code);

			}
			else {
				// Local server
				jsmServerCnfObject = lookupObject;
			}

			// Analyze configuration
			if (jsmServerCnfObject != null) {

				String providerURL =
					jsmServerCnfObject.getString(WebKeys.JMS_PROVIDER_URL);

				if (remote && Validator.isNotNull(providerURL) &&
					mom.equals("jmscore")) {
					providerURL = "remote://" + providerURL;
				}

				String providerPort =
					jsmServerCnfObject.getString(WebKeys.JMS_PROVIDER_PORT);

				String userName =
					jsmServerCnfObject.getString(WebKeys.JMS_USERNAME);

				String passWord =
					jsmServerCnfObject.getString(WebKeys.JMS_PASSWORD);

				String syncCompanyId =
					jsmServerCnfObject.getString(WebKeys.JMS_COMPANY_ID);

				String syncGroupId =
					jsmServerCnfObject.getString(WebKeys.JMS_GROUP_ID);

				String syncUserId =
					jsmServerCnfObject.getString(WebKeys.JMS_USER_ID);

				if (Validator.isNotNull(channelName)) {
					JSONObject channelObject =
						jsmServerCnfObject.getJSONObject(WebKeys.JMS_CHANNEL);
					String channel = channelObject.getString(channelName);
					properties.put(WebKeys.JMS_DESTINATION, channel);
				}

				if (Validator.isNotNull(queueName)) {
					JSONObject queueObject =
						jsmServerCnfObject.getJSONObject(WebKeys.JMS_QUEUE_NAME);
					String queue = queueObject.getString(queueName);
					properties.put(WebKeys.JMS_QUEUE, queue);

				}

				properties.put(Context.PROVIDER_URL, providerURL +
					(remote
						? (StringPool.COLON + providerPort) : StringPool.BLANK));

				properties.put(Context.SECURITY_PRINCIPAL, userName);
				properties.put(Context.SECURITY_CREDENTIALS, passWord);
				properties.put(
					Context.INITIAL_CONTEXT_FACTORY,
					org.jboss.naming.remote.client.InitialContextFactory.class.getName());

				properties.put(WebKeys.JMS_COMPANY_ID, syncCompanyId);
				properties.put(WebKeys.JMS_GROUP_ID, syncGroupId);
				properties.put(WebKeys.JMS_USER_ID, syncUserId);
				properties.put(WebKeys.JMS_PROVIDER_PORT, providerPort);
				properties.put(WebKeys.JMS_PROVIDER_URL, providerURL);

			}

		}
		catch (JSONException e) {
			_log.error(e);
		}

		return properties;
	}

	public static String intToString(long number, int stringLength) {

		int numberOfDigits = String.valueOf(number).length();
		int numberOfLeadingZeroes = stringLength - numberOfDigits;
		StringBuilder sb = new StringBuilder();
		if (numberOfLeadingZeroes > 0) {
			for (int i = 0; i < numberOfLeadingZeroes; i++) {
				sb.append("0");
			}
		}
		sb.append(number);
		return sb.toString();
	}

	public static String getDossierProcessStateLabel(
		Dossier dossier, Locale locale) {

		String statusLabel = StringPool.BLANK;

		if (Validator.isNotNull(dossier.getFinishDatetime()) &&
			Validator.isNotNull(dossier.getEstimateDatetime())) {
			if (dossier.getFinishDatetime().after(dossier.getEstimateDatetime())) {
				statusLabel = LanguageUtil.get(locale, "status-late");
			}
			else if (dossier.getFinishDatetime().before(
				dossier.getEstimateDatetime())) {
				statusLabel = LanguageUtil.get(locale, "status-soon");
			}
			else if (dossier.getFinishDatetime().equals(
				dossier.getEstimateDatetime())) {
				statusLabel = LanguageUtil.get(locale, "status-ontime");
			}
		}
		else {
			Date now = new Date();

			if (Validator.isNotNull(dossier.getEstimateDatetime())) {
				if (dossier.getEstimateDatetime().before(now)) {
					statusLabel = LanguageUtil.get(locale, "status-toosoon");
				}
				else if (dossier.getEstimateDatetime().after(now)) {
					statusLabel = LanguageUtil.get(locale, "status-toolate");
				}
			}
		}

		return statusLabel;
	}

	private static Log _log =
		LogFactoryUtil.getLog(PortletUtil.class.getName());
}
