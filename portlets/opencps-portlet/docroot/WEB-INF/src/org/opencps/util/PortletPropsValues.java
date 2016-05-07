
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

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

/**
 * @author trungnt
 */
public class PortletPropsValues {

	// Data Management Validate
	public static final int DATAMGT_DICTCOLLECTION_CODE_LENGHT = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DATAMGT_DICTCOLLECTION_CODE_LENGHT), 100);
	public static final int DATAMGT_DICTCOLLECTION_NAME_LENGHT = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DATAMGT_DICTCOLLECTION_NAME_LENGHT), 255);
	public static final int DATAMGT_DICTITEM_CODE_LENGHT = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DATAMGT_DICTITEM_CODE_LENGHT), 100);
	public static final int DATAMGT_DICTITEM_NAME_LENGHT = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DATAMGT_DICTITEM_NAME_LENGHT), 255);

	// Data Management Master data
	public static final String[] DATAMGT_MASTERDATA_CODE = GetterUtil
	    .getStringValues(PortletProps
	        .get(PortletPropsKeys.DATAMGT_MASTERDATA_CODE), new String[0]);

	public static final String DATAMGT_MASTERDATA_BUSINESS_DOMAIN = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.DATAMGT_MASTERDATA_BUSINESS_DOMAIN));

	public static final String DATAMGT_MASTERDATA_BUSINESS_TYPE = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.DATAMGT_MASTERDATA_BUSINESS_TYPE));

	public static final String DATAMGT_MASTERDATA_SERVICE_DOMAIN = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.DATAMGT_MASTERDATA_SERVICE_DOMAIN));
	public static final String DATAMGT_MASTERDATA_SERVICE_ADMINISTRATION =
	    GetterUtil
	        .getString(PortletProps
	            .get(
	                PortletPropsKeys.DATAMGT_MASTERDATA_SERVICE_ADMINISTRATION));

	public static final String DATAMGT_MASTERDATA_ADMINISTRATIVE_REGION =
	    GetterUtil
	        .getString(PortletProps
	            .get(
	                PortletPropsKeys.DATAMGT_MASTERDATA_ADMINISTRATIVE_REGION));
	
	public static final String DATAMGT_MASTERDATA_ADMINISTRATION_REGION = 
			 GetterUtil
		        .getString(PortletProps
		            .get(
		                PortletPropsKeys.DATAMGT_MASTERDATA_ADMINISTRATION_REGION));

	// validate file

	public static final String[] ACCOUNTMGT_FILE_TYPE = GetterUtil
	    .getStringValues(PortletProps
	        .getArray(PortletPropsKeys.ACCOUNTMGT_FILE_TYPE));
	public static final long ACCOUNTMGT_FILE_SIZE = GetterUtil
	    .getLong(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_FILE_SIZE));
	// User Management
	public static final int[] USERMGT_JOBPOS_LEADER = GetterUtil
	    .getIntegerValues(PortletProps
	        .getArray(PortletPropsKeys.USERMGT_JOBPOS_LEADER), new int[0]);

	public static final String USERMGT_USERGROUP_NAME_EMPLOYEE = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.USERMGT_USERGROUP_NAME_EMPLOYEE));

	public static final String USERMGT_USERGROUP_NAME_BUSINESS = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.USERMGT_USERGROUP_NAME_BUSINESS));

	public static final String USERMGT_USERGROUP_NAME_CITIZEN = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.USERMGT_USERGROUP_NAME_CITIZEN));

	public static final String USERMGT_JOBPOS_NOMAL = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.JOBPOS_NOMAL));
	public static final String USERMGT_JOBPOS_BOSS = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.JOBPOS_LEADER));
	public static final String USERMGT_JOBPOS_DEPUTY = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.JOBPOS_DEPUTY));

	public static final int[] USERMGT_GENDER_VALUES = GetterUtil
	    .getIntegerValues(PortletProps
	        .getArray(PortletPropsKeys.USERMGT_GENDER_VALUES), new int[0]);

	// User Management Validate
	public static final int USERMGT_EMPLOYEE_FULLNAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_EMPLOYEE_FULLNAME_LENGTH));
	public static final int USERMGT_EMPLOYEE_EMAIL_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_EMPLOYEE_EMAIL_LENGTH));
	public static final int USERMGT_EMPLOYEE_EMPLOYEENO_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_EMPLOYEE_EMPLOYEENO_LENGTH));
	public static final int USERMGT_EMPLOYEE_TELNO_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_EMPLOYEE_TELNO_LENGTH));
	public static final int USERMGT_EMPLOYEE_MOBILE_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_EMPLOYEE_MOBILE_LENGTH));
	// working unit

	public static final int USERMGT_WORKINGUNIT_NAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_WORKINGUNIT_NAME_LENGTH));

	public static final int USERMGT_WORKINGUNIT_ADRESS_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_WORKINGUNIT_ADRESS_LENGTH));

	public static final int USERMGT_WORKINGUNIT_FAXNO_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_WORKINGUNIT_FAXNO_LENGTH));

	public static final int USERMGT_WORKINGUNIT_EMAIL_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_WORKINGUNIT_EMAIL_LENGTH));

	public static final int USERMGT_WORKINGUNIT_WEBSITE_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_WORKINGUNIT_WEBSITE_LENGTH));

	public static final int USERMGT_WORKINGUNIT_ENNAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.USERMGT_WORKINGUNIT_ENNAME_LENGTH));

	// citizen validate
	public static final int ACCOUNTMGT_CITIZEN_NAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_CITIZEN_NAME_LENGTH));

	public static final int ACCOUNTMGT_CITIZEN_ADDRESS_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_CITIZEN_ADDRESS_LENGTH));
	public static final int ACCOUNTMGT_CITIZEN_EMAIL_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_CITIZEN_EMAIL_LENGTH));

	// business validate
	public static final int ACCOUNTMGT_BUSINESS_NAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_BUSINESS_NAME_LENGTH));
	public static final int ACCOUNTMGT_BUSINESS_ENNAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_BUSINESS_ENNAME_LENGTH));
	public static final int ACCOUNTMGT_BUSINESS_EMAIL_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_BUSINESS_EMAIL_LENGTH));
	public static final int ACCOUNTMGT_BUSINESS_ADDRESS_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_BUSINESS_ADDRESS_LENGTH));
	public static final int ACCOUNTMGT_BUSINESS_SHORTNAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.ACCOUNTMGT_BUSINESS_SHORTNAME_LENGTH));
	public static final int ACCOUNTMGT_BUSINESS_REPRESENTATIVENAME_LENGTH =
	    GetterUtil
	        .getInteger(PortletProps
	            .get(
	                PortletPropsKeys.ACCOUNTMGT_BUSINESS_REPRESENTATIVENAME_LENGTH));
	public static final int ACCOUNTMGT_BUSINESS_REPRESENTATIVEROLE_LENGTH =
	    GetterUtil
	        .getInteger(PortletProps
	            .get(
	                PortletPropsKeys.ACCOUNTMGT_BUSINESS_REPRESENTATIVEROLE_LENGTH));

	// resource action

	public static final String USERMGT_WORKINGUNIT_RESOURCE = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.USERMGT_WORKINGUNIT_RESOURCE));
	public static final String USERMGT_JOBPOS_RESOURCE = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.USERMGT_JOBPOS_RESOURCE));
	public static final String USERMGT_EMPLOYEE_RESOURCE = GetterUtil
	    .getString(PortletProps
	        .get(PortletPropsKeys.USERMGT_EMPLOYEE_RESOURCE));

	// dossier validare

	public static final int DOSSIERMGT_TEMPLATE_NAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_TEMPLATE_NAME_LENGTH));

	public static final int DOSSIERMGT_TEMPLATE_NUMBER_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_TEMPLATE_NUMBER_LENGTH));

	public static final int DOSSIERMGT_PART_NAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_PART_NAME_LENGTH));

	public static final int DOSSIERMGT_PART_NUMBER_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_PART_NUMBER_LENGTH));

	public static final int DOSSIERMGT_PART_TEMPLATE_FILE_NUMBER_LENGTH =
	    GetterUtil
	        .getInteger(PortletProps
	            .get(
	                PortletPropsKeys.DOSSIERMGT_PART_TEMPLATE_FILE_NUMBER_LENGTH));
	public static final int DOSSIERMGT_SERVICE_CONFIG_GOVNAME_LENGTH =
	    GetterUtil
	        .getInteger(PortletProps
	            .get(
	                PortletPropsKeys.DOSSIERMGT_SERVICE_CONFIG_GOVNAME_LENGTH));
	public static final int DOSSIERMGT_SERVICE_CONFIG_GOVCODE_LENGTH =
	    GetterUtil
	        .getInteger(PortletProps
	            .get(
	                PortletPropsKeys.DOSSIERMGT_SERVICE_CONFIG_GOVCODE_LENGTH));

	public static final int DOSSIERMGT_DOSSIER_SUBJECT_NAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_DOSSIER_SUBJECT_NAME_LENGTH));
	public static final int DOSSIERMGT_DOSSIER_SUBJECT_ID_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_DOSSIER_SUBJECT_ID_LENGTH));
	public static final int DOSSIERMGT_DOSSIER_ADDRESS_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_DOSSIER_ADDRESS_LENGTH));
	public static final int DOSSIERMGT_DOSSIER_CONTACT_NAME_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_DOSSIER_CONTACT_NAME_LENGTH));
	public static final int DOSSIERMGT_DOSSIER_CONTACT_TEL_NO_LENGTH =
	    GetterUtil
	        .getInteger(PortletProps
	            .get(
	                PortletPropsKeys.DOSSIERMGT_DOSSIER_CONTACT_TEL_NO_LENGTH));
	public static final int DOSSIERMGT_DOSSIER_CONTACT_EMAIL_LENGTH = GetterUtil
	    .getInteger(PortletProps
	        .get(PortletPropsKeys.DOSSIERMGT_DOSSIER_CONTACT_EMAIL_LENGTH));
	
	public static final int DOSSIERMGT_DOSSIER_NOTE_LENGTH = GetterUtil
				    .getInteger(PortletProps
				        .get(PortletPropsKeys.DOSSIERMGT_DOSSIER_NOTE_LENGTH));
}
