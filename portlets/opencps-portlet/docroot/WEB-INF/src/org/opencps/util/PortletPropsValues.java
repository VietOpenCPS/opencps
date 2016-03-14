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
	public static final int DATAMGT_DICTCOLLECTION_CODE_LENGHT = GetterUtil
			.getInteger(PortletProps.get(PortletPropsKeys.DATAMGT_DICTCOLLECTION_CODE_LENGHT), 100);
	public static final int DATAMGT_DICTCOLLECTION_NAME_LENGHT = GetterUtil
			.getInteger(PortletProps.get(PortletPropsKeys.DATAMGT_DICTCOLLECTION_NAME_LENGHT), 255);
	public static final int DATAMGT_DICTITEM_CODE_LENGHT = GetterUtil
			.getInteger(PortletProps.get(PortletPropsKeys.DATAMGT_DICTITEM_CODE_LENGHT), 100);
	public static final int DATAMGT_DICTITEM_NAME_LENGHT = GetterUtil
			.getInteger(PortletProps.get(PortletPropsKeys.DATAMGT_DICTITEM_NAME_LENGHT), 255);
	public static final String[] DATAMGT_MASTERDATA_CODE = GetterUtil
			.getStringValues(PortletProps.get(PortletPropsKeys.DATAMGT_MASTERDATA_CODE), new String[0]);
}
