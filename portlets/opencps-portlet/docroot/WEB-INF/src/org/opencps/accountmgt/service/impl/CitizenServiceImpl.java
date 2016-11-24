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

package org.opencps.accountmgt.service.impl;

import java.util.Date;
import java.util.Locale;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.accountmgt.service.base.CitizenServiceBaseImpl;
import org.opencps.datamgt.model.DictItem;
import org.opencps.util.PortletConstants;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.ac.AccessControlled;

/**
 * The implementation of the citizen remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.accountmgt.service.CitizenService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.accountmgt.service.base.CitizenServiceBaseImpl
 * @see org.opencps.accountmgt.service.CitizenServiceUtil
 */
public class CitizenServiceImpl extends CitizenServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.opencps.accountmgt.service.CitizenServiceUtil} to access the citizen remote service.
	 */
	
	@JSONWebService(value = "get-user-info-by-email")
	@AccessControlled(guestAccessEnabled = true)
	public JSONObject getUserInfoByEmail(String email) throws SystemException, PortalException {

		JSONObject jsonObject = JSONFactoryUtil
			.createJSONObject();
		
		Citizen citizen = null;
		Business business = null;
		
		try {
			citizen = CitizenLocalServiceUtil.getCitizen(email);
		} catch (Exception e) {
			// TODO: handle exception
			_log.error(e);
		}
		
		try {
			business = BusinessLocalServiceUtil.getBusiness(email);
		} catch (Exception e) {
			// TODO: handle exception
			_log.error(e);
		}
		
		if(Validator.isNotNull(citizen)){
			
			jsonObject.put("typeOfUser", PortletConstants.ACCOUNT_API_TYPE_CITIZEN);
			
			jsonObject.put("emailAddress", citizen.getEmail());
			
			jsonObject.put("name", citizen.getFullName());
			
			jsonObject.put("idNumber", citizen.getPersonalId());
			
			jsonObject.put("gender", citizen.getGender());
			
			jsonObject.put("birthdate", citizen.getBirthdate().getTime());
			
			jsonObject.put("address", citizen.getAddress());
			
			jsonObject.put("cityCode", citizen.getCityCode());
			
			jsonObject.put("districtCode", citizen.getDistrictCode());
			
			jsonObject.put("wardCode", citizen.getWardCode());
			
			jsonObject.put("telNo", citizen.getTelNo());
			
		}else if(Validator.isNotNull(business)){
			
			jsonObject.put("typeOfUser", PortletConstants.ACCOUNT_API_TYPE_BUSINESS);
			
			jsonObject.put("emailAddress", business.getEmail());
			
			jsonObject.put("name", business.getName());
			
			jsonObject.put("enName", business.getEnName());
			
			jsonObject.put("shortName", business.getShortName());
			
			jsonObject.put("idNumber", business.getIdNumber());
			
			jsonObject.put("gender", "");
			
			jsonObject.put("birthdate", new Date().getTime());
			
			jsonObject.put("address", business.getAddress());
			
			jsonObject.put("cityCode", business.getCityCode());
			
			jsonObject.put("districtCode", business.getDistrictCode());
			
			jsonObject.put("wardCode", business.getWardCode());
			
			jsonObject.put("telNo", business.getTelNo());
			
		}
		
		return jsonObject;
	}
	
	private Log _log =
			LogFactoryUtil.getLog(CitizenServiceImpl.class.getName());
}