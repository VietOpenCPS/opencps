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

package org.opencps.processmgt.search;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author binhth
 */
public class ActionHistorySearchTerms extends ActionHistoryDisplayTerms {

	public ActionHistorySearchTerms(PortletRequest portletRequest) {
		super(
		    portletRequest);

		processOrderId = ParamUtil.getLong(portletRequest, PROCESSORDERID, 0L);
		
		processWorkflowId = ParamUtil.getLong(portletRequest, PROCESSWORKFLOWID, 0L);
		
		actionDatetime = ParamUtil.getDate(portletRequest, ACTIONDATETIME, DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_FORMAT));
		
		stepName = ParamUtil.getString(portletRequest, STEPNAME, StringPool.BLANK);
		
		actionName = ParamUtil.getString(portletRequest, ACTIONNAME, StringPool.BLANK);
		
		actionNote = ParamUtil.getString(portletRequest, ACTIONNOTE, StringPool.BLANK);
		
		actionUserId = ParamUtil.getLong(portletRequest, ACTIONUSERID, 0L);
		
		daysDoing = ParamUtil.getLong(portletRequest, DAYSDOING, 0L);
		
		daysDelay = ParamUtil.getLong(portletRequest, DAYS_DELAY, 0L);

	}
}
