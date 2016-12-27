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
* 
* 
* 
*/

package org.opencps.accountmgt.portlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.opencps.accountmgt.InvalidCityCodeException;
import org.opencps.accountmgt.InvalidDistricCodeException;
import org.opencps.accountmgt.InvalidWardCodeException;
import org.opencps.accountmgt.OutOfLengthBusinessEnNameException;
import org.opencps.accountmgt.OutOfLengthBusinessNameException;
import org.opencps.accountmgt.OutOfLengthBusinessRepresentativeNameException;
import org.opencps.accountmgt.OutOfLengthBusinessRepresentativeRoleException;
import org.opencps.accountmgt.OutOfLengthBusinessShortNameException;
import org.opencps.accountmgt.OutOfLengthCitizenAddressException;
import org.opencps.accountmgt.OutOfLengthCitizenNameException;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.search.CitizenDisplayTerms;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.accountmgt.util.AccountMgtUtil;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.usermgt.search.EmployeeDisplayTerm;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.MessageBusUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.UserPasswordException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.PwdGenerator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */
public class AccountMgtPortlet extends MVCPortlet {

	public void deleteBusiness(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long businessId = ParamUtil
		    .getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID, 0L);
		String redirectURL = ParamUtil
		    .getString(actionRequest, "redirectURL");
		try {
			BusinessLocalServiceUtil
			    .deleteBusinessByBusinessId(businessId);
		}
		catch (Exception e) {
			_log
			    .error(e);
			SessionErrors
			    .add(actionRequest, MessageKeys.ACCOUNT_BUSINESS_DELETE_ERROR);
		}
		finally {
			if (Validator
			    .isNotNull(redirectURL)) {
				actionResponse
				    .sendRedirect(redirectURL);
			}
		}
	}

	public void deleteCitizen(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {
		
		long citizenId = ParamUtil
		    .getLong(actionRequest, CitizenDisplayTerms.CITIZEN_ID, 0L);
		String redirectURL = ParamUtil
		    .getString(actionRequest, "redirectURL");
		try {
			CitizenLocalServiceUtil
			    .deleteCitizenByCitizenId(citizenId);
		}
		catch (Exception e) {
			_log
			    .error(e);
			SessionErrors
			    .add(actionRequest, MessageKeys.ACCOUNT_CITIZEN_DELETE_ERROR);
		}
		finally {
			if (Validator
			    .isNotNull(redirectURL)) {
				actionResponse
				    .sendRedirect(redirectURL);
			}
		}
	}

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long citizenId = ParamUtil
		    .getLong(renderRequest, CitizenDisplayTerms.CITIZEN_ID);

		long businessId = ParamUtil
		    .getLong(renderRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		try {

			if (citizenId > 0) {
				
				renderRequest
				    .setAttribute(CitizenDisplayTerms.CITIZEN_ID, citizenId);
			}

			if (businessId > 0) {
				renderRequest
				    .setAttribute(BusinessDisplayTerms.BUSINESS_BUSINESSID, businessId);
			}

			renderRequest
			    .setAttribute(WebKeys.ACCOUNTMGT_ADMIN_PROFILE, true);
		}

		catch (Exception e) {
			_log
			    .error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void updateStatus(
	    ActionRequest actionRequest, ActionResponse actionResponse) throws IOException {

		long citizenId = ParamUtil
		    .getLong(actionRequest, CitizenDisplayTerms.CITIZEN_ID, 0L);
		long businessId = ParamUtil
		    .getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID, 0L);

		int curAccountStatus = ParamUtil
		    .getInteger(actionRequest, "curAccountStatus", -1);

		int accountStatus = -1;
		
		String password = PwdGenerator
			    .getPassword();
		
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		
		try {
			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(actionRequest);

			if (citizenId > 0) {
				accountStatus = ParamUtil
				    .getInteger(
				        actionRequest,
				        CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS, -1);
				curAccountStatus = ParamUtil
				    .getInteger(actionRequest, "curAccountStatus", -1);

				if (accountStatus >= 0) {
					CitizenLocalServiceUtil
					    .updateStatus(citizenId, serviceContext
					        .getUserId(), accountStatus);
				}

			}

			if (businessId > 0) {
				accountStatus = ParamUtil
				    .getInteger(
				        actionRequest,
				        BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS, -1);

				if (accountStatus >= 0) {
					BusinessLocalServiceUtil
					    .updateStatus(businessId, serviceContext
					        .getUserId(), accountStatus);
				}

			}

			// Lan Dau Kich Hoat Tai Khoan
			if (curAccountStatus > 0 && accountStatus > 0 &&
			    curAccountStatus == PortletConstants.ACCOUNT_STATUS_CONFIRMED) {

				User mappingUser = null;

				long mappingUserId = 0;

				if (citizenId > 0) {
					Citizen citizen = CitizenLocalServiceUtil
					    .fetchCitizen(citizenId);
					mappingUserId = citizen
					    .getMappingUserId();

				}
				else if (businessId > 0) {
					Business business = BusinessLocalServiceUtil
					    .fetchBusiness(businessId);
					mappingUserId = business
					    .getMappingUserId();
				}

				mappingUser = UserLocalServiceUtil
						.updatePassword(mappingUserId, password, password, false);
				
				if (mappingUser != null) {
					MessageBusUtil.sendEmailActiveAccount(mappingUser, password, serviceContext);
				}
			}
		}
		catch (Exception e) {
			_log
			    .error(e);
		} finally {
			if (Validator.isNotNull(redirectURL)) {
				actionResponse.sendRedirect(redirectURL);
			}
		}

	}

	public void updateCitizenProfile(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long citizenId = ParamUtil
		    .getLong(actionRequest, CitizenDisplayTerms.CITIZEN_ID);
		String returnURL = ParamUtil
		    .getString(actionRequest, "returnURL");
		String address = ParamUtil
		    .getString(actionRequest, CitizenDisplayTerms.CITIZEN_ADDRESS);
		String telNo = ParamUtil
		    .getString(actionRequest, EmployeeDisplayTerm.TEL_NO);
		String curPass = ParamUtil
		    .getString(actionRequest, CitizenDisplayTerms.CURRENT_PASSWORD);
		String newPass = ParamUtil
		    .getString(actionRequest, CitizenDisplayTerms.NEW_PASSWORD);
		String rePass = ParamUtil
		    .getString(actionRequest, CitizenDisplayTerms.RE_PASSWORD);
		long cityId = ParamUtil
		    .getLong(actionRequest, CitizenDisplayTerms.CITIZEN_CITY_ID);
		long districtId = ParamUtil
		    .getLong(actionRequest, CitizenDisplayTerms.CITIZEN_DISTRICT_ID);
		long wardId = ParamUtil
		    .getLong(actionRequest, CitizenDisplayTerms.CITIZEN_WARD_ID);

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		boolean isChangePassWord = false;
		if (Validator
		    .isNotNull(curPass) && Validator
		        .isNotNull(newPass) &&
		    Validator
		        .isNotNull(rePass)) {
			isChangePassWord = true;
		}

		try {
			
			AccountRegPortlet
			    .validateCitizen(
			        citizenId, StringPool.BLANK, StringPool.BLANK, address,
			        StringPool.BLANK, telNo, 1, StringPool.BLANK, cityId, districtId, wardId
			        , StringPool.BLANK);

			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(actionRequest);

			city = DictItemLocalServiceUtil
			    .getDictItem(cityId);

			district = DictItemLocalServiceUtil
			    .getDictItem(districtId);

			ward = DictItemLocalServiceUtil
			    .getDictItem(wardId);

			if (citizenId > 0) {
				CitizenLocalServiceUtil
				    .updateCitizen(citizenId, address, city
				        .getItemCode(), district
				            .getItemCode(),
				        ward
				            .getItemCode(),
				        city
				            .getItemName(serviceContext
				                .getLocale(), true),
				        district
				            .getItemName(serviceContext
				                .getLocale(), true),
				        ward
				            .getItemName(serviceContext
				                .getLocale(), true),
				        telNo, isChangePassWord, newPass, rePass, serviceContext
				            .getScopeGroupId(),
				        serviceContext);

			}
		}
		catch (Exception e) {
			if (e instanceof UserPasswordException) {
				SessionErrors
				    .add(actionRequest, UserPasswordException.class);
			}
			else if (e instanceof OutOfLengthCitizenAddressException) {
				SessionErrors
				    .add(
				        actionRequest,
				        OutOfLengthCitizenAddressException.class);
			}
			else if (e instanceof OutOfLengthCitizenNameException) {
				SessionErrors
				    .add(actionRequest, OutOfLengthCitizenNameException.class);
			}
			else if(e instanceof InvalidCityCodeException) {
				SessionErrors.add(
				    actionRequest, InvalidCityCodeException.class);
			}
			else if(e instanceof InvalidDistricCodeException) {
				SessionErrors.add(
				    actionRequest, InvalidDistricCodeException.class);
			}
			else if(e instanceof InvalidWardCodeException) {
				SessionErrors.add(
				    actionRequest, InvalidWardCodeException.class);
			}
			else {
				SessionErrors
				    .add(
				        actionRequest,
				        MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED);
				_log.error(e);
			}

			if (Validator
			    .isNotNull(returnURL)) {
				actionResponse
				    .sendRedirect(returnURL);
			}
		}
	}

	public void updateBusinessProfile(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		long businessId = ParamUtil
		    .getLong(actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		long cityId = ParamUtil
		    .getLong(actionRequest, BusinessDisplayTerms.BUSINESS_CITY_ID);
		long districtId = ParamUtil
		    .getLong(actionRequest, BusinessDisplayTerms.BUSINESS_DISTRICT_ID);
		long wardId = ParamUtil
		    .getLong(actionRequest, BusinessDisplayTerms.BUSINESS_WARD_ID);

		String returnURL = ParamUtil
		    .getString(actionRequest, "returnURL");
		String name = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.BUSINESS_NAME);
		String enName = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.BUSINESS_ENNAME);
		String idNumber = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.BUSINESS_IDNUMBER);
		String shortName = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.BUSINESS_SHORTNAME);
		long type = ParamUtil
		    .getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		String address = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.BUSINESS_ADDRESS);
		String email = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.BUSINESS_EMAIL);
		String telNo = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.BUSINESS_TELNO);
		String representativeName = ParamUtil
		    .getString(
		        actionRequest,
		        BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME);
		String representativeRole = ParamUtil
		    .getString(
		        actionRequest,
		        BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE);
		String[] domain = ParamUtil
		    .getParameterValues(
		        actionRequest, BusinessDisplayTerms.BUSINESS_DOMAIN);
		String [] listBussinessDomains = ParamUtil
						.getParameterValues(actionRequest, "listBussinessDomains");
		String curPass = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.CURRENT_PASSWORD);
		String newPass = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.NEW_PASSWORD);
		String rePass = ParamUtil
		    .getString(actionRequest, BusinessDisplayTerms.RE_PASSWORD);
		boolean isChangePassWord = false;
		if (Validator
		    .isNotNull(curPass) && Validator
		        .isNotNull(newPass) &&
		    Validator
		        .isNotNull(rePass)) {
			isChangePassWord = true;
		}

		String backURL = ParamUtil.getString(actionRequest, "backURL");
		
		int dateDayIDNumber =
		    ParamUtil.getInteger(
		        actionRequest, BusinessDisplayTerms.DATE_DAY);
		int dateMonthIDNumber =
		    ParamUtil.getInteger(
		    		actionRequest, BusinessDisplayTerms.DATE_MONTH);
		int dateYearIDNumber =
		    ParamUtil.getInteger(
		    		actionRequest, BusinessDisplayTerms.DATE_YEAR);
		
		Date dateOfIdNumber = DateTimeUtil.getDate(dateDayIDNumber, dateMonthIDNumber,
				dateYearIDNumber);
		
		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;
		
		DictItem busType = null;

		try {

			AccountRegPortlet
			    .validateBusiness(
			        businessId, email, StringPool.BLANK, enName, shortName,
			        address, representativeName, representativeRole, cityId, districtId, wardId,
			        1, StringPool.BLANK, StringPool.BLANK);

			city = DictItemLocalServiceUtil
			    .getDictItem(cityId);

			district = DictItemLocalServiceUtil
			    .getDictItem(districtId);

			ward = DictItemLocalServiceUtil
			    .getDictItem(wardId);
			
			busType = DictItemLocalServiceUtil
						    .getDictItem(type);
			ServiceContext serviceContext = ServiceContextFactory
			    .getInstance(actionRequest);

			if (businessId > 0) {
				district
				    .getItemName(serviceContext
				        .getLocale(), true);
				BusinessLocalServiceUtil
				    .updateBusiness(
				        businessId, name, enName, shortName, busType.getItemCode(), idNumber,
				        address, city
				            .getItemCode(),
				        district
				            .getItemCode(),
				        ward
				            .getItemCode(),
				        city
				            .getItemName(serviceContext
				                .getLocale(), true),
				        district
				            .getItemName(serviceContext
				                .getLocale(), true),
				        ward
				            .getItemName(serviceContext
				                .getLocale(), true),
				        telNo, representativeName, representativeRole, listBussinessDomains,
				        isChangePassWord, curPass, rePass, serviceContext
				            .getScopeGroupId(),
				        serviceContext, dateOfIdNumber);
				
				if(Validator.isNotNull(backURL)) {
					actionResponse.sendRedirect(backURL);
				}

			}

		}
		catch (Exception e) {
			if (e instanceof UserPasswordException) {
				SessionErrors
				    .add(actionRequest, UserPasswordException.class);
			}
			else if (e instanceof OutOfLengthBusinessNameException) {
				SessionErrors
				    .add(actionRequest, OutOfLengthBusinessNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessEnNameException) {
				SessionErrors
				    .add(
				        actionRequest,
				        OutOfLengthBusinessEnNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessShortNameException) {
				SessionErrors
				    .add(
				        actionRequest,
				        OutOfLengthBusinessShortNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessRepresentativeNameException) {
				SessionErrors
				    .add(
				        actionRequest,
				        OutOfLengthBusinessRepresentativeNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessRepresentativeRoleException) {
				SessionErrors
				    .add(
				        actionRequest,
				        OutOfLengthBusinessRepresentativeRoleException.class);
			}
			else if(e instanceof InvalidCityCodeException) {
				SessionErrors.add(
				    actionRequest, InvalidCityCodeException.class);
			}
			else if(e instanceof InvalidDistricCodeException) {
				SessionErrors.add(
				    actionRequest, InvalidDistricCodeException.class);
			}
			else if(e instanceof InvalidWardCodeException) {
				SessionErrors.add(
				    actionRequest, InvalidWardCodeException.class);
			}
			else {
				SessionErrors
				    .add(
				        actionRequest,
				        MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED);
			}
			
			if (Validator
			    .isNotNull(returnURL)) {
				actionResponse
				    .sendRedirect(returnURL);
				_log.error(e);
			}

		}
	}
	
	@Override
 	public void serveResource(ResourceRequest resourceRequest,
 			ResourceResponse resourceResponse) throws IOException,
 			PortletException {
 		// TODO Auto-generated method stub
		String type = resourceRequest.getParameter("type");
		if(type.equals(AccountMgtUtil.TOP_TABS_CITIZEN)){
			exportToExcelCitizen(resourceRequest, resourceResponse);
		}else{
			exportToExcelBusiness(resourceRequest, resourceResponse);
		}
 		
 		super.serveResource(resourceRequest, resourceResponse);
	}
	
	public void exportToExcelCitizen(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
 		
 		try {
 			Date dt = new Date();
 			
 			String fileName = "account" + DateTimeUtil.convertDateToString(new Date(), DateTimeUtil._DATE_TIME_TO_NAME);
 			
  			String keywords = resourceRequest.getParameter("word");
 			String acountStatus = resourceRequest.getParameter("status");
 			
 			if(Validator.isNull(acountStatus)) acountStatus = "-1";
 			
 			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest
					.getAttribute(WebKeys.THEME_DISPLAY);

 			String csvSeparatorString = ",";
 			String[] headerStrings = { "STT", "CMTND", "NAME", "GENDER", "DATE OF BIRTH", "ACCOUNT", "STATUS",};
 			
 			StringBundler sb = new StringBundler();
 			for(String st : headerStrings) {
 				sb.append(getFormatString(st));
 				sb.append(csvSeparatorString);
 			}
 			sb.setIndex(sb.index() - 1);
 			sb.append(CharPool.NEW_LINE);
 			int status = Integer.parseInt(acountStatus);
 			
 			List<Citizen> allCitizens = CitizenLocalServiceUtil.searchCitizen(themeDisplay.getScopeGroupId(), keywords, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
 			for (int i = 0; i < allCitizens.size(); i++) {
 				Citizen ct = allCitizens.get(i);
 				sb.append(getFormatString(String.valueOf(i + 1)));
 				sb.append(csvSeparatorString);
 				sb.append(getFormatString(ct.getPersonalId()));
 				sb.append(csvSeparatorString);
 				sb.append(getFormatString(ct.getFullName()));
 				sb.append(csvSeparatorString);
 				String gender = ct.getGender() == 1 ? "MALE":"FEMALE";
 				sb.append(getFormatString(gender));
 				sb.append(csvSeparatorString);
 				sb.append(DateTimeUtil.convertDateToString(ct.getBirthdate(), DateTimeUtil._VN_DATE_FORMAT));
 				sb.append(csvSeparatorString);
 				sb.append(getFormatString(ct.getEmail()));
 				sb.append(csvSeparatorString);
 				String accoutStatus = StringPool.BLANK;
 				accoutStatus = LanguageUtil.get(getPortletConfig(), themeDisplay.getLocale(), PortletUtil.getAccountStatus(ct.getAccountStatus(), themeDisplay.getLocale()));
 				sb.append(getFormatString(accoutStatus));
 				sb.append(csvSeparatorString);
 				sb.append(CharPool.NEW_LINE);
 			}
 			fileName += ".csv";
 			String contentType = ContentTypes.APPLICATION_TEXT;
 			byte[] bs = sb.toString().getBytes();
 			PortletResponseUtil.sendFile(resourceRequest, resourceResponse, fileName, bs, contentType);
 			
 		} catch (Exception e) {
 			System.err.println(e.getMessage());
 		} finally {
 			resourceRequest.setAttribute("status", "1");
 		}
 	}
 	
	public void exportToExcelBusiness(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
 		
 		try {
 			Date dt = new Date();
 			
 			String fileName = "account_business" + DateTimeUtil.convertDateToString(new Date(), DateTimeUtil._DATE_TIME_TO_NAME);
 			
  			String keywords = resourceRequest.getParameter("word");
 			String acountStatus = resourceRequest.getParameter("status");
 			
 			if(Validator.isNull(acountStatus)) acountStatus = "-1";
 			
 			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest
					.getAttribute(WebKeys.THEME_DISPLAY);

 			String csvSeparatorString = ",";
 			String[] headerStrings = { "STT", "BUSINESS CODE", "LOAI HINH TO CHUC", "ACCOUNT", "STATUS",};
 			
 			StringBundler sb = new StringBundler();
 			for(String st : headerStrings) {
 				sb.append(getFormatString(st));
 				sb.append(csvSeparatorString);
 			}
 			sb.setIndex(sb.index() - 1);
 			sb.append(CharPool.NEW_LINE);
 			int status = Integer.parseInt(acountStatus);
 			
 			List<Business> allBusinesses = BusinessLocalServiceUtil.searchBusiness(themeDisplay.getScopeGroupId(), keywords, status, StringPool.BLANK, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
 			for (int i = 0; i < allBusinesses.size(); i++) {
 				Business ct = allBusinesses.get(i);
 				sb.append(getFormatString(String.valueOf(i + 1)));
 				sb.append(csvSeparatorString);
 				sb.append(getFormatString(ct.getIdNumber()));
 				sb.append(csvSeparatorString);
 				sb.append(getFormatString(ct.getBusinessType()));
 				sb.append(csvSeparatorString);
 				sb.append(getFormatString(ct.getEmail()));
 				sb.append(csvSeparatorString);
 				String accoutStatus = StringPool.BLANK;
 				accoutStatus = LanguageUtil.get(getPortletConfig(), themeDisplay.getLocale(), PortletUtil.getAccountStatus(ct.getAccountStatus(), themeDisplay.getLocale()));
 				sb.append(getFormatString(accoutStatus));
 				sb.append(csvSeparatorString);
 				sb.append(CharPool.NEW_LINE);
 			}
 			fileName += ".csv";
 			String contentType = ContentTypes.APPLICATION_TEXT;
 			byte[] bs = sb.toString().getBytes();
 			PortletResponseUtil.sendFile(resourceRequest, resourceResponse, fileName, bs, contentType);
 			
 		} catch (Exception e) {
 			System.err.println(e.getMessage());
 		} finally {
 			resourceRequest.setAttribute("status", "1");
 		}
 	}

 	private String getFormatString(String valueString) {
 		StringBundler sBundler = new StringBundler();
 		sBundler.append(CharPool.QUOTE);
 		sBundler.append(StringUtil.replace(valueString, CharPool.QUOTE, StringPool.DOUBLE_QUOTE));
 		sBundler.append(CharPool.QUOTE);
 		return sBundler.toString();
 	}
 	
	private Log _log = LogFactoryUtil
	    .getLog(AccountMgtPortlet.class
	        .getName());
}
