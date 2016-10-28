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

package org.opencps.accountmgt.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import net.sourceforge.jtds.jdbc.DateTime;

import org.opencps.accountmgt.DuplicateBusinessEmailException;
import org.opencps.accountmgt.DuplicateCitizenEmailException;
import org.opencps.accountmgt.FileTypeFailException;
import org.opencps.accountmgt.InvalidCityCodeException;
import org.opencps.accountmgt.InvalidDistricCodeException;
import org.opencps.accountmgt.InvalidFileUploadException;
import org.opencps.accountmgt.InvalidWardCodeException;
import org.opencps.accountmgt.OutOfLengthBusinessEmailException;
import org.opencps.accountmgt.OutOfLengthBusinessEnNameException;
import org.opencps.accountmgt.OutOfLengthBusinessNameException;
import org.opencps.accountmgt.OutOfLengthBusinessRepresentativeNameException;
import org.opencps.accountmgt.OutOfLengthBusinessRepresentativeRoleException;
import org.opencps.accountmgt.OutOfLengthBusinessShortNameException;
import org.opencps.accountmgt.OutOfLengthCitizenAddressException;
import org.opencps.accountmgt.OutOfLengthCitizenEmailException;
import org.opencps.accountmgt.OutOfLengthCitizenNameException;
import org.opencps.accountmgt.OutOfSizeFileUploadException;
import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.model.impl.BusinessImpl;
import org.opencps.accountmgt.model.impl.CitizenImpl;
import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.search.CitizenDisplayTerms;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.MessageBusUtil;
import org.opencps.util.MessageKeys;
import org.opencps.util.PortletConstants;
import org.opencps.util.PortletPropsValues;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * @author trungnt
 */
public class AccountRegPortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long citizenId =
		    ParamUtil.getLong(renderRequest, CitizenDisplayTerms.CITIZEN_ID);

		long businessId =
		    ParamUtil.getLong(
		        renderRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		try {
			if (citizenId > 0) {
				Citizen citizen =
				    CitizenLocalServiceUtil.fetchCitizen(citizenId);
				renderRequest.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
			}

			if (businessId > 0) {
				Business business =
				    BusinessLocalServiceUtil.fetchBusiness(businessId);
				renderRequest.setAttribute(WebKeys.BUSINESS_ENTRY, business);
			}
		}

		catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void updateBusiness(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		UploadPortletRequest uploadPortletRequest =
		    PortalUtil.getUploadPortletRequest(actionRequest);

		long businessId =
		    ParamUtil.getLong(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);
		String [] listBussinessDomains = ParamUtil
						.getParameterValues(uploadPortletRequest, "listBussinessDomains");
		long cityId =
		    ParamUtil.getLong(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_CITY_ID);
		long districtId =
		    ParamUtil.getLong(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_DISTRICT_ID);
		long wardId =
		    ParamUtil.getLong(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_WARD_ID);

		long repositoryId = 0;

		long size =
		    uploadPortletRequest.getSize(BusinessDisplayTerms.BUSINESS_ATTACHFILE);
		String currentURL =
		    ParamUtil.getString(uploadPortletRequest, "currentURL");

		String name =
		    ParamUtil.getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_NAME);
		String enName =
		    ParamUtil.getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_ENNAME);
		String idNumber =
		    ParamUtil.getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_IDNUMBER);
		String shortName =
		    ParamUtil.getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_SHORTNAME);
		long type =
		    ParamUtil.getLong(
		        uploadPortletRequest,
		        BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		String address =
		    ParamUtil.getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_ADDRESS);
		String email =
		    ParamUtil.getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_EMAIL);
		String telNo =
		    ParamUtil.getString(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_TELNO);
		String representativeName =
		    ParamUtil.getString(
		        uploadPortletRequest,
		        BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME);
		String representativeRole =
		    ParamUtil.getString(
		        uploadPortletRequest,
		        BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE);
		String contentType =
		    uploadPortletRequest.getContentType(BusinessDisplayTerms.BUSINESS_ATTACHFILE);

		String sourceFileName =
		    uploadPortletRequest.getFileName(BusinessDisplayTerms.BUSINESS_ATTACHFILE);

		String[] domain =
		    ParamUtil.getParameterValues(
		        uploadPortletRequest, BusinessDisplayTerms.BUSINESS_DOMAIN);
		
		String businessTypeCode = StringPool.BLANK;

		Date defaultBirthDate = DateTimeUtil.convertStringToDate("01/01/1970");

		PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);
		contentType =
					    Validator.isNotNull(contentType)
					        ? MimeTypesUtil.getContentType(contentType) : StringPool.BLANK;
		String title = "Business File";
		
		String emailConfirmToAdmin = ParamUtil.getString(uploadPortletRequest, "emailConfirmToAdmin");
		
		String adminEmailUserAddedBody = ParamUtil.getString(uploadPortletRequest, "adminEmailUserAddedBody");

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		DictItem businessType = null;

		InputStream inputStream = null;
		
		boolean registered = false;

		try {
			
			validateBusiness(
			    businessId, email, sourceFileName, enName, shortName, address,
			    representativeName, representativeRole, cityId, districtId, wardId,
			    size, sourceFileName);
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(uploadPortletRequest);

			inputStream =
			    uploadPortletRequest.getFileAsStream(BusinessDisplayTerms.BUSINESS_ATTACHFILE);

			repositoryId = serviceContext.getScopeGroupId();
			city = DictItemLocalServiceUtil.getDictItem(cityId);
			district = DictItemLocalServiceUtil.getDictItem(districtId);
			ward = DictItemLocalServiceUtil.getDictItem(wardId);
			if(type != 0) {
				businessType = DictItemLocalServiceUtil.getDictItem(type);
				businessTypeCode = businessType.getItemCode();
			} 
			
			if (businessId == 0) {

				Business business =
				    BusinessLocalServiceUtil.addBusiness(
				        name, enName, shortName, businessTypeCode, idNumber,
				        address, city.getItemCode(), district.getItemCode(),
				        ward.getItemCode(), city.getItemName(
				            serviceContext.getLocale(), true),
				        district.getItemName(serviceContext.getLocale(), true),
				        ward.getItemName(serviceContext.getLocale(), true),
				        telNo, email, representativeName, representativeRole,
				        listBussinessDomains, spd.getDayOfMoth(), spd.getMonth(),
				        spd.getYear(), repositoryId, sourceFileName,
				        contentType, title, inputStream, size, serviceContext);

				if (business != null) {
					User mappingUser =
					    UserLocalServiceUtil.getUser(business.getMappingUserId());
					MessageBusUtil.sendEmailAddressVerification(
					    business.getUuid(), mappingUser, email,
					    PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS,
					    serviceContext);
					//check reg cfg
					int step = ParamUtil.getInteger(uploadPortletRequest, "businessRegStep_cfg");
					if(step == 2){
						BusinessLocalServiceUtil
					    .updateStatus(business.getBusinessId(), serviceContext
					        .getUserId(), PortletConstants.ACCOUNT_STATUS_APPROVED);
					}
					SessionMessages.add(
					    actionRequest,
							MessageKeys.ACCOUNT_UPDATE_CUCCESS);
					// Gui email thong bao toi quan tri sau khi thuc hien dang ky thanh cong
						MessageBusUtil.sendEmailConfirmToAdmin(business.getUuid(),
								mappingUser, email, emailConfirmToAdmin,
								PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS, business,
								serviceContext);
					// Gui email thong bao toi quan tri sau khi thuc hien dang ky thanh cong -----END-----
				}
			} else {

			}
			
			registered = true;
		}

		catch (Exception e) {
			
			registered = false;
			
			BusinessImpl business = new BusinessImpl();
			
			business.setName(name);
			business.setIdNumber(idNumber);
			business.setEnName(enName);
			business.setShortName(shortName);
			business.setEmail(email);
			business.setTelNo(telNo);
			business.setAddress(address);
			business.setBusinessType(String.valueOf(type));
			business.setRepresentativeName(representativeName);
			business.setRepresentativeRole(representativeRole);
			business.setCityCode(String.valueOf(cityId));
			business.setDistrictCode(String.valueOf(districtId));
			business.setWardCode(String.valueOf(wardId));
			
			actionRequest.setAttribute("businessValidate", business);
			
			String busDomains = StringUtil.merge(listBussinessDomains,StringPool.COMMA);
			actionResponse.setRenderParameter("busDomains", busDomains);
			
			if (e instanceof DuplicateBusinessEmailException) {
				SessionErrors.add(
				    actionRequest, DuplicateBusinessEmailException.class);
			}
			else if (e instanceof OutOfLengthBusinessEmailException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthBusinessEmailException.class);
			}
			else if (e instanceof OutOfLengthBusinessNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthBusinessNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessEnNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthBusinessEnNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessShortNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthBusinessShortNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessRepresentativeNameException) {
				SessionErrors.add(
				    actionRequest,
				    OutOfLengthBusinessRepresentativeNameException.class);
			}
			else if (e instanceof OutOfLengthBusinessRepresentativeRoleException) {
				SessionErrors.add(
				    actionRequest,
				    OutOfLengthBusinessRepresentativeRoleException.class);
			}
			else if (e instanceof OutOfLengthCitizenAddressException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthCitizenAddressException.class);
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
			else if(e instanceof InvalidFileUploadException){
				SessionErrors.add(
				    actionRequest, InvalidFileUploadException.class);
			}
			else if(e instanceof FileTypeFailException) {
				SessionErrors.add(
				    actionRequest, FileTypeFailException.class);
			}
			else if(e instanceof OutOfSizeFileUploadException) {
				SessionErrors.add(
				    actionRequest, OutOfSizeFileUploadException.class);
			}
			else {
				SessionErrors.add(
				    actionRequest,
				    MessageKeys.ACCOUNT_SYSTEM_EXCEPTION_OCCURRED);
				_log.error(e);
			}
			
		}finally {
			if(registered){
				actionResponse.sendRedirect(currentURL);
			}else{
				actionResponse.setRenderParameter("mvcPath", "/html/portlets/accountmgt/registration/registration.jsp");
				actionResponse.setRenderParameter("type", "business");
			}
			
		}

	}

	public void updateCitizen(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws IOException {

		UploadPortletRequest uploadPortletRequest =
		    PortalUtil.getUploadPortletRequest(actionRequest);

		long citizenId =
		    ParamUtil.getLong(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_ID);

		long cityId =
		    ParamUtil.getLong(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_CITY_ID);
		long districtId =
		    ParamUtil.getLong(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_DISTRICT_ID);
		long wardId =
		    ParamUtil.getLong(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_WARD_ID);

		long size =
		    uploadPortletRequest.getSize(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

		long repositoryId = 0;
		String currentURL =
		    ParamUtil.getString(uploadPortletRequest, "currentURL");

		String fullName =
		    ParamUtil.getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_FULLNAME);
		String personId =
		    ParamUtil.getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_PERSONALID);
		String adress =
		    ParamUtil.getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_ADDRESS);
		String email =
		    ParamUtil.getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_EMAIL);
		String telNo =
		    ParamUtil.getString(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_TELNO);

		String contentType =
		    uploadPortletRequest.getContentType(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

		String sourceFileName =
		    uploadPortletRequest.getFileName(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

		int birthDateDay =
		    ParamUtil.getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.BIRTH_DATE_DAY);
		int birthDateMonth =
		    ParamUtil.getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.BIRTH_DATE_MONTH);
		int birthDateYear =
		    ParamUtil.getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.BIRTH_DATE_YEAR);
		int gender =
		    ParamUtil.getInteger(
		        uploadPortletRequest, CitizenDisplayTerms.CITIZEN_GENDER);

		contentType =
		    Validator.isNotNull(contentType)
		        ? MimeTypesUtil.getContentType(contentType) : StringPool.BLANK;
		String title = "Personal File";

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		InputStream inputStream = null;

		boolean registered = false;
		
		try {

			validateCitizen(
			    citizenId, fullName, personId, adress, email, telNo, size,
			    contentType, cityId, districtId, wardId, sourceFileName);

			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);

			inputStream =
			    uploadPortletRequest.getFileAsStream(CitizenDisplayTerms.CITIZEN_ATTACHFILE);

			repositoryId = serviceContext.getScopeGroupId();

			city = DictItemLocalServiceUtil.getDictItem(cityId);

			district = DictItemLocalServiceUtil.getDictItem(districtId);

			ward = DictItemLocalServiceUtil.getDictItem(wardId);

			if (citizenId == 0) {
				Citizen citizen =
				    CitizenLocalServiceUtil.addCitizen(
				        fullName, personId, gender, birthDateDay,
				        birthDateMonth, birthDateYear, adress,
				        city.getItemCode(), district.getItemCode(),
				        ward.getItemCode(),
				        city.getItemName(serviceContext.getLocale(), true),
				        district.getItemName(serviceContext.getLocale(), true),
				        ward.getItemName(serviceContext.getLocale(), true),
				        email,telNo, repositoryId, sourceFileName, contentType,
				        title, inputStream, size, serviceContext);

				if (citizen != null) {
					User mappingUser =
					    UserLocalServiceUtil.getUser(citizen.getMappingUserId());
					MessageBusUtil.sendEmailAddressVerification(
					    citizen.getUuid(), mappingUser, email,
					    PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN,
					    serviceContext);
					//check reg cfg
					int step = ParamUtil.getInteger(uploadPortletRequest, "citizenRegStep_cfg");
					if(step == 2){
						CitizenLocalServiceUtil
					    .updateStatus(citizen.getCitizenId(), serviceContext
					        .getUserId(), PortletConstants.ACCOUNT_STATUS_APPROVED);
						
					}
				}
				SessionMessages.add(
				    actionRequest,
				    MessageKeys.ACCOUNT_UPDATE_CUCCESS);
			}
			else {

			}

			registered = true;
		}
		catch (Exception e) {
			
			registered = false;
			
			List<String> lstBirthDate = new ArrayList<String>();
			
			lstBirthDate.add(String.valueOf(birthDateDay));
			lstBirthDate.add(String.valueOf(birthDateMonth));
			lstBirthDate.add(String.valueOf(birthDateYear));
			
			Date birthDate = DateTimeUtil.getDate(birthDateDay, birthDateMonth,
					birthDateYear);
			CitizenImpl citizen = new CitizenImpl();
			
			citizen.setFullName(fullName);
			citizen.setEmail(email);
			citizen.setPersonalId(personId);
			citizen.setTelNo(telNo);
			citizen.setBirthdate(birthDate);
			citizen.setGender(gender);
			citizen.setAddress(adress);
			citizen.setCityCode(String.valueOf(cityId));;
			citizen.setDistrictCode(String.valueOf(districtId));
			citizen.setWardCode(String.valueOf(wardId));
			
			actionRequest.setAttribute("citizenValidate", citizen);
			
			if (e instanceof OutOfLengthCitizenAddressException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthCitizenAddressException.class);
			}
			else if (e instanceof OutOfLengthCitizenEmailException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthCitizenEmailException.class);
			}
			else if (e instanceof OutOfLengthCitizenNameException) {
				SessionErrors.add(
				    actionRequest, OutOfLengthCitizenNameException.class);
			}
			else if (e instanceof DuplicateCitizenEmailException) {
				SessionErrors.add(
				    actionRequest, DuplicateCitizenEmailException.class);
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
			else if(e instanceof InvalidFileUploadException){
				SessionErrors.add(
				    actionRequest, InvalidFileUploadException.class);
			}
			else if(e instanceof FileTypeFailException) {
				SessionErrors.add(
				    actionRequest, FileTypeFailException.class);
			}
			else if(e instanceof OutOfSizeFileUploadException) {
				SessionErrors.add(
				    actionRequest, OutOfSizeFileUploadException.class);
			}
			else {
				SessionErrors.add(
					actionRequest,
				    MessageKeys.ACCOUNT_SYSTEM_EXCEPTION_OCCURRED);
				_log.error(e);
			}

		} finally {
			if(registered){
				actionResponse.sendRedirect(currentURL);
			}else{
				actionResponse.setRenderParameter("mvcPath", "/html/portlets/accountmgt/registration/registration.jsp");
				actionResponse.setRenderParameter("type", "citizen");
			}
			
		}
	}

	protected static void validateCitizen(
	    long citizenId, String fullName, String personalId, String address,
	    String email, String telNo, long size, String mimeType, long cityId,
	    long districId, long wardId, String sourceFileName)
	    throws OutOfLengthCitizenAddressException,
	    OutOfLengthCitizenNameException, OutOfLengthCitizenEmailException,
	    DuplicateCitizenEmailException, InvalidCityCodeException,
	    InvalidDistricCodeException, InvalidWardCodeException,
	    InvalidFileUploadException, OutOfSizeFileUploadException, FileTypeFailException {

		Citizen citizen = null;

		try {
			if (!email.equals(StringPool.BLANK)) {
				citizen = CitizenLocalServiceUtil.getCitizen(email);
			}
		}
		catch (Exception e) {
			// Nothing todo
		}
		if (citizenId == 0 && citizen != null) {
			throw new DuplicateCitizenEmailException();
		}
		if (citizenId > 0 && citizen != null && citizen.getCitizenId() != citizenId) {
			throw new DuplicateCitizenEmailException();
		}

		if (fullName.length() > PortletPropsValues.ACCOUNTMGT_CITIZEN_NAME_LENGTH) {
			throw new OutOfLengthCitizenNameException();
		}
		else if (address.length() > PortletPropsValues.ACCOUNTMGT_CITIZEN_ADDRESS_LENGTH) {
			throw new OutOfLengthCitizenAddressException();
		}
		else if (email.length() > PortletPropsValues.ACCOUNTMGT_CITIZEN_EMAIL_LENGTH) {
			throw new OutOfLengthCitizenEmailException();
		} 
		else if(!Validator.isNotNull(cityId) || cityId == 0) {
			throw new InvalidCityCodeException();
		}
		else if(!Validator.isNotNull(districId) || districId == 0) {
			throw new InvalidDistricCodeException();
		}
		else if(!Validator.isNotNull(wardId) || wardId == 0) {
			throw new InvalidWardCodeException();
		} 
		else if(size == 0) {
			//off required file upload
//			throw new InvalidFileUploadException();
		} 
		else if(size > PortletPropsValues.ACCOUNTMGT_FILE_SIZE) {
			throw new OutOfSizeFileUploadException();
		}
		else if(Validator.isNotNull(sourceFileName) && !isFileType(sourceFileName)) {
			throw new FileTypeFailException();
		}

	}

	protected static void validateBusiness(
	    long businessId, String email, String name, String enName,
	    String shortName, String address, String representativeName,
	    String representativeRole, long cityId, long districId, long wardId,
	    long size, String sourceFileName)
	    throws DuplicateBusinessEmailException,
	    OutOfLengthBusinessEmailException, OutOfLengthBusinessNameException,
	    OutOfLengthBusinessEnNameException, OutOfLengthCitizenAddressException,
	    OutOfLengthBusinessRepresentativeNameException,
	    OutOfLengthBusinessRepresentativeRoleException,
	    OutOfLengthBusinessShortNameException, InvalidCityCodeException, 
	    InvalidWardCodeException, InvalidDistricCodeException, FileTypeFailException,
	    OutOfSizeFileUploadException, InvalidFileUploadException {

		Business business = null;

		try {
			if (!email.equals(StringPool.BLANK)) {
				business = BusinessLocalServiceUtil.getBusiness(email);
			}

		}
		catch (Exception e) {
			// Nothing todo
		}

		if (businessId == 0 && business != null) {
			throw new DuplicateBusinessEmailException();
		}

		if (businessId != 0 && business != null &&
		    business.getBusinessId() != businessId) {
			throw new DuplicateBusinessEmailException();
		}
		if (email.length() > PortletPropsValues.ACCOUNTMGT_BUSINESS_EMAIL_LENGTH) {
			throw new OutOfLengthBusinessEmailException();
		}
		else if (name.length() > PortletPropsValues.ACCOUNTMGT_BUSINESS_NAME_LENGTH) {
			throw new OutOfLengthBusinessNameException();
		}
		else if (enName.length() > PortletPropsValues.ACCOUNTMGT_BUSINESS_ENNAME_LENGTH) {
			throw new OutOfLengthBusinessEnNameException();
		}
		else if (address.length() > PortletPropsValues.ACCOUNTMGT_BUSINESS_ADDRESS_LENGTH) {
			throw new OutOfLengthCitizenAddressException();
		}
		else if (representativeName.length() > PortletPropsValues.ACCOUNTMGT_BUSINESS_REPRESENTATIVENAME_LENGTH) {
			throw new OutOfLengthBusinessRepresentativeNameException();
		}
		else if (representativeRole.length() > PortletPropsValues.ACCOUNTMGT_BUSINESS_REPRESENTATIVEROLE_LENGTH) {
			throw new OutOfLengthBusinessRepresentativeRoleException();
		}
		else if (shortName.length() > PortletPropsValues.ACCOUNTMGT_BUSINESS_SHORTNAME_LENGTH) {
			throw new OutOfLengthBusinessShortNameException();
		}
		else if(!Validator.isNotNull(cityId) || cityId == 0) {
			throw new InvalidCityCodeException();
		}
		else if(!Validator.isNotNull(districId) || districId == 0) {
			throw new InvalidDistricCodeException();
		}
		else if(!Validator.isNotNull(wardId) || wardId == 0) {
			throw new InvalidWardCodeException();
		}
		else if(size == 0) {
			//off required file upload
//			throw new InvalidFileUploadException();
		} 
		else if(size > PortletPropsValues.ACCOUNTMGT_FILE_SIZE) {
			throw new OutOfSizeFileUploadException();
		}
		else if(Validator.isNotNull(sourceFileName) && !isFileType(sourceFileName)) {
			throw new FileTypeFailException();
		}
	}
	
	public static boolean isFileType(String sourceFileName) {
		String fileType = MimeTypesUtil.getContentType(sourceFileName);
		for(String str : PortletPropsValues.ACCOUNTMGT_FILE_TYPE) {
			if(fileType.endsWith(str)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	private Log _log = LogFactoryUtil.getLog(AccountRegPortlet.class.getName());
}
