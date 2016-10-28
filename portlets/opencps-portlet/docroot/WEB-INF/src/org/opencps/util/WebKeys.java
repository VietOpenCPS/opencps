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

/**
 * @author trungnt
 */
public class WebKeys implements com.liferay.portal.kernel.util.WebKeys {

	public static final String DATA_MANAGEMENT_ADMIN_PORTLET =
		"1_WAR_opencpsportlet";
	
	public static final String USER_MGT_PORTLET = "2_WAR_opencpsportlet";

	public static final String DOSSIER_MGT_PORTLET = "13_WAR_opencpsportlet";

	public static final String SERVICE_MGT_DIRECTORY = "10_WAR_opencpsportlet";

	public static final String P26_SUBMIT_ONLINE = "26_WAR_opencpsportlet";

	public static final String PAYMENT_MGT_PORTLET = "20_WAR_opencpsportlet";

	public static final String PROCESS_ORDER_PORTLET = "16_WAR_opencpsportlet";
	
	public static final String DOSSIER_BACKOFFICE_MANAGEMENT_PORTLET = "18_WAR_opencpsportlet";
	
	public static final String DICT_COLLECTION_ENTRY = "DICT_COLLECTION_ENTRY";

	public static final String DICT_VERSION_ENTRY = "DICT_VERSION_ENTRY";

	public static final String DICT_ITEM_ENTRY = "DICT_ITEM_ENTRY";

	public static final String WORKING_UNIT_ENTRY = "WORKING_UNIT_ENTRY";

	public static final String EMPLOYEE_ENTRY = "employee";

	public static final String TURN_BACK_EMPLOYEE_ENTRY =
		"TURN_BACK_EMPLOYEE_ENTRY";

	public static final String JOBPOS_ENTRY = "JOBPOS_ENTRY";

	public static final String USER_MAPPING_ENTRY = "USER_MAPPING_ENTRY";

	public static final String TURN_BACK_USER_MAPPING_ENTRY =
		"TURN_BACK_USER_MAPPING_ENTRY";

	public static final String WORKING_UNIT_MAPPING_ENTRY =
		"WORKING_UNIT_MAPPING_ENTRY";

	public static final String MAIN_JOB_POS_ENTRY = "MAIN_JOB_POS_ENTRY";

	public static final String TURN_BACK_SCREEN_NAME = "TURN_BACK_SCREEN_NAME";

	public static final String TURN_BACK_ACCOUNT_EMAIL =
		"TURN_BACK_ACCOUNT_EMAIL";

	public static final String TURN_BACK_JOBPOS_INDEXES =
		"TURN_BACK_JOBPOS_INDEXES";

	public static final String USERMGT_VIEW_PROFILE = "USERMGT_VIEW_PROFILE";

	public static final String SERVICE_ENTRY = "SERVICE_ENTRY";

	public static final String CITIZEN_ENTRY = "citizen";

	public static final String MAPPING_USERID = "MAPPING_USERID";

	public static final String BUSINESS_ENTRY = "business";

	public static final String ACCOUNTMGT_ADMIN_PROFILE =
		"ACCOUNTMGT_ADMIN_PROFILE";

	public static final String ACCOUNTMGT_VIEW_PROFILE =
		"ACCOUNTMGT_VIEW_PROFILE";

	public static final String ACCOUNT_TYPE = "accountType";

	public static final String SERVICE_TEMPLATE_ENTRY =
		"SERVICE_TEMPLATE_ENTRY";

	public static final String DOSSIER_PART_ENTRY = "DOSSIER_PART_ENTRY";

	public static final String DOSSIER_TEMPLATE_ENTRY =
		"DOSSIER_TEMPLATE_ENTRY";

	public static final String SERVICE_CONFIG_ENTRY = "SERVICE_CONFIG_ENTRY";

	public static final String SERVICE_PROCESS_ENTRY = "SERVICE_PROCESS_ENTRY";

	public static final String PROCESS_STEP_ENTRY = "PROCESS_STEP_ENTRY";

	public static final String PROCESS_WORKFLOW_ENTRY =
		"PROCESS_WORKFLOW_ENTRY";

	public static final String MENU_ACTIVE = "active";

	public static final String PROCESS_ORDER = "PROCESS_ORDER";

	public static final String DOSSIER_ENTRY = "DOSSIER_ENTRY";

	public static final String DOSSIER_FILE_ENTRY = "DOSSIER_FILE_ENTRY";

	public static final String RESPONSE_UPLOAD_TEMP_DOSSIER_FILE =
		"RESPONSE_UPLOAD_TEMP_DOSSIER_FILE";

	public static final String SERVICE_INFO_ENTRY = "SERVICE_INFO_ENTRY";

	public static final String STEP_ALLOWANCE_ENTRY = "STEP_ALLOWANCE_ENTRY";

	public static final String WORKFLOW_ENTRY = "WORKFLOW_ENTRY";

	public static final String DICT_ITEM_SELECTED = "DICT_ITEM_SELECTED";

	public static final String FORM_DATA = "FORM_DATA";

	public static final String PROCESS_ORDER_ENTRY = "PROCESS_ORDER_ENTRY";

	public static final String FILE_GROUP_ENTRY = "FILE_GROUP_ENTRY";

	public static final String FILE_ID = "dossierFileId";

	public static final int DOSSIER_MONITORING_LIST = 1;

	public static final int DOSSIER_MONITORING_UPDATE = 2;

	public static final String ACTION_SUBMIT_VALUE = "submit";

	public static final String ACTION_RESUBMIT_VALUE = "resubmit";

	public static final String ACTION_CHANGE_VALUE = "change";

	public static final String ACTION_REPAIR_VALUE = "repair";

	public static final String ACTION_PAY_VALUE = "pay";

	public static final String ACTION_CANCEL_VALUE = "cancel";

	public static final String ACTION_CLOSE_VALUE = "close";

	public static final String ACCOUNT_FOLDER = "accountFolder";

	public static final String ACCOUNT_ROLES = "accountRoles";

	public static final String ACCOUNT_ORGANIZATION = "accountOrgs";

	public static final String ACCOUNT_OWNERUSERID = "ownerUserId";

	public static final String ACCOUNT_OWNERORGANIZATIONID =
		"ownerOrganizationId";

	public static final String ACTOR_ACTION_SYSTEM = "system";

	public static final String ACTOR_ACTION_CITIZEN = "citizen";

	public static final String ACTOR_ACTION_EMPLOYEE = "employee";

	public static final String DOSSIER_LOG_PAYMENT_REQUEST = "paymentRequest";

	public static final String DOSSIER_LOG_CHANGE_REQUEST = "changeRequest";

	public static final String DOSSIER_LOG_RESUBMIT_REQUEST = "resubmitRequest";

	public static final String DOSSIER_LOG_CANCEL_REQUEST = "cancelRequest";

	public static final String PRE_CONDITION_PAYOK = "payok";

	public static final String PRE_CONDITION_TAG_LABEL = "tag label";

	public static final String PRE_CONDITION_CANCEL = "cancel";

	public static final String PRE_CONDITION_SERVICE_ID = "service id";

	public static final String PRE_CONDITION_ONEGATE = "onegate";

	public static final String PRE_CONDITION_ONELINE = "oneline";

	public static final int PAYMENT_METHOD_CASH = 1;

	public static final int PAYMENT_METHOD_KEYPAY = 2;

	public static final int PAYMENT_METHOD_BANK = 3;

	public static final String REQUEST_COMMAND_CANCEL = "cancel";

	public static final String REQUEST_COMMAND_REPAIR = "repair";

	public static final String ACCOUNT_BEAN = "accountBean";

	public static final String AUTO_EVENT_SUBMIT = "submit";
	public static final String AUTO_EVENT_ONEGATE = "onegate";
	public static final String AUTO_EVENT_REPAIR = "repair";
	public static final String AUTO_EVENT_CHANGE = "change";
	public static final String AUTO_EVENT_MINUTELY = "minutely";
	public static final String AUTO_EVENT_5_MINUTELY = "5-minutely";
	public static final String AUTO_EVENT_HOURLY = "hourly";
	public static final String AUTO_EVENT_DAILY = "daily";
	public static final String AUTO_EVENT_WEEKLY = "weekly";

	public static final String EMPLOYEE_ROLE_NAME = "OCPS_EMPLOYE";
	public static final String CITIZEN_BUSINESS_ROLE_NAME = "OCPS_CITIZEN";

	public static final String JMS_CONFIGURATION = "JMS_CONFIGURATION";
	public static final String JMS_CHANNEL = "JMS_CHANNEL";
	public static final String JMS_PROVIDER_URL = "JMS_PROVIDER_URL";
	public static final String JMS_PROVIDER_PORT = "JMS_PROVIDER_PORT";
	public static final String JMS_USERNAME = "JMS_USERNAME";
	public static final String JMS_PASSWORD = "JMS_PASSWORD";
	public static final String JMS_DESTINATION = "JMS_DESTINATION";
	public static final String JMS_COMPANY_ID = "JMS_COMPANY_ID";
	public static final String JMS_GROUP_ID = "JMS_GROUP_ID";
	public static final String JMS_USER_ID = "JMS_USER_ID";
	public static final String JMS_QUEUE_OPENCPS = "OPENCPS";
	public static final String JMS_QUEUE_OPENCPS_FRONTOFFICE = "OPENCPS_FRONTOFFICE";
	public static final String JMS_QUEUE_NAME = "JMS_QUEUE_NAME";
	public static final String JMS_MOM_HORNETQ = "hornetq";
	public static final String JMS_CORE = "jmscore";
	public static final String JMS_QUEUE = "JMS_QUEUE";
	public static final String ORDER_BY_ASC = "asc";
	public static final String ORDER_BY_DESC = "desc";
	public static final String SERVICE_DOMAIN = "SERVICE_DOMAIN";
	
	public static final int DOSSIER_ACTOR_SYSTEM = 0; 
	public static final String DOSSIER_ACTOR_SYSTEM_NAME = "SYSTEM"; 
	public static final int DOSSIER_ACTOR_CITIZEN = 1; 
	public static final int DOSSIER_ACTOR_EMPLOYEE = 2;
	
	public static final String SYNC_PAY_SEND_CONFIRM = "SYNC_PAY_SEND_CONFIRM";
	public static final String SYNC_PAY_CONFIRM = "SYNC_PAY_CONFIRM";
	
}
