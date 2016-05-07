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
public class PortletConstants {

	public static final int DRAFTING = 0;
	public static final int INUSE = 1;
	public static final int EXPIRED = 2;

	public static final int WORKING_STATUS_ACTIVATE = 1;
	public static final int WORKING_STATUS_DEACTIVATE = 0;

	public static final int ACCOUNT_STATUS_REGISTERED = 0;
	public static final int ACCOUNT_STATUS_CONFIRMED = 1;
	public static final int ACCOUNT_STATUS_APPROVED = 2;
	public static final int ACCOUNT_STATUS_LOCKED = 3;
	
	public static final int DOSSIER_TYPE_PAPER_SUBMITED = 1;
	public static final int DOSSIER_TYPE_OTHER_PAPERS_GROUP = 2;
	public static final int DOSSIER_TYPE_GROUPS_OPTIONAL = 3;
	public static final int DOSSIER_TYPE_OWN_RECORDS = 4;
	public static final int DOSSIER_TYPE_PAPERS_RESULTS = 5;
	
	public static final int SERVICE_CONFIG_INACTIVE = 0;
	public static final int SERVICE_CONFIG_FRONTOFFICE = 1;
	public static final int SERVICE_CONFIG_BACKOFFICE = 2;
	public static final int SERVICE_CONFIG_FRONT_BACK_OFFICE = 3;
	
	
	public static enum DestinationRoot {
			CITIZEN("Citizen"), BUSINESS("Business");

		private String value;

		DestinationRoot(String value) {
			this.value = value;
		}

		public String getValue() {

			return value;
		}

		@Override
		public String toString() {

			return this
			    .getValue();
		}

		public static DestinationRoot getEnum(String value) {

			for (DestinationRoot v : values())
				if (v
				    .getValue().equalsIgnoreCase(value))
					return v;
			throw new IllegalArgumentException();
		}
	}

	public static final int DOSSIER_PART_TYPE_SUBMIT = 1;
	public static final int DOSSIER_PART_TYPE_OTHER = 2;
	public static final int DOSSIER_PART_TYPE_OPTION = 3;
	public static final int DOSSIER_PART_TYPE_PRIVATE = 4;
	public static final int DOSSIER_PART_TYPE_RESULT = 5;
	
	public static final int DOSSIER_FILE_SYNC_STATUS_NOSYNC = 0;
	public static final int DOSSIER_FILE_SYNC_STATUS_REQUIREDSYNC = 1;
	public static final int DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS = 2;
	public static final int DOSSIER_FILE_SYNC_STATUS_SYNCERROR = 3;
	
	public static final int DOSSIER_FILE_TYPE_INPUT = 1;
	public static final int DOSSIER_FILE_TYPE_OUTPUT = 2;
	
	public static final int DOSSIER_FILE_MARK_UNKNOW = 0;
	public static final int DOSSIER_FILE_MARK_ORIGINAL = 1;
	public static final int DOSSIER_FILE_MARK_NOTARIZED = 2;
	public static final int DOSSIER_FILE_MARK_SCAN = 3;
	
	public static final int DOSSIER_FILE_ORIGINAL = 0;
	public static final int DOSSIER_FILE_DOSSIERRESULT = 1;
	
	public static final int DOSSIER_FILE_REMOVED = 1;
	
	public static final int DOSSIER_STATUS_NEW = 0;
	public static final int DOSSIER_STATUS_RECEIVING = 1;
	public static final int DOSSIER_STATUS_WAITING = 2;
	public static final int DOSSIER_STATUS_PAYING = 3;
	public static final int DOSSIER_STATUS_DENIED = 4;
	public static final int DOSSIER_STATUS_RECEIVED = 5;
	public static final int DOSSIER_STATUS_PROCESSING = 6;
	public static final int DOSSIER_STATUS_CANCELED = 7;
	public static final int DOSSIER_STATUS_DONE = 8;
	public static final int DOSSIER_STATUS_ARCHIVED = 9;
	public static final int DOSSIER_STATUS_SYSTEM = 10;
	public static final int DOSSIER_STATUS_ERROR = 11;
	
	public static final int DOSSIER_LOG_NORMAL = 0;
	public static final int DOSSIER_LOG_WARNING = 2;
	public static final int DOSSIER_LOG_ERROR = 3;
	
	public static final int DOSSIER_SOURCE_DIRECT = 0;
	public static final int DOSSIER_SOURCE_INDIRECT = 1;
	
	public static enum AlpacaArguments{
		data,schema,options,view,render,postRender,error,connector;
	}
	
	public static final String TEMP_RANDOM_SUFFIX = "--tempRandomSuffix--";
	
	public static final String UNKNOW_ALPACA_SCHEMA = "{\"schema\": {\"title\":\"No Dynamic Form\",\"description\":\"Can not load alpaca scheme\",\"type\":\"object\"}}";
}
