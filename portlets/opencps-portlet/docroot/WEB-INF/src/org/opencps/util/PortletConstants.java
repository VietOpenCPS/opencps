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

	public static final int DOSSIER_PART_TYPE_COMPONEMT = 1;
	public static final int DOSSIER_PART_TYPE_SUBMIT = 2;
	public static final int DOSSIER_PART_TYPE_OTHER = 3;
	public static final int DOSSIER_PART_TYPE_PRIVATE = 4;
	public static final int DOSSIER_PART_TYPE_RESULT = 5;
	
	public static final String TEMP_RANDOM_SUFFIX = "--tempRandomSuffix--";
	
	public static final String UNKNOW_ALPACA_SCHEMA = "\"schema\": {\"title\":\"No Dynamic Form\",\"description\":\"Can not load alpaca scheme\",\"type\":\"object\"}";
}
