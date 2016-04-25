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

package org.opencps.processmgt.search;

import javax.portlet.PortletRequest;

import org.opencps.util.DateTimeUtil;

import com.liferay.portal.kernel.util.ParamUtil;


/**
 * @author binhth
 *
 */
public class ProcessOrderSearchTerms extends ProcessOrderDisplayTerms{

	/**
     * @param portletRequest
     */
    public ProcessOrderSearchTerms(PortletRequest portletRequest) {

	    super(portletRequest);
	    // TODO Auto-generated constructor stub
	    processOrderId = ParamUtil.getLong(portletRequest, PROCESSORDERID);
	    companyId = ParamUtil.getLong(portletRequest, COMPANYID);
	    groupId = ParamUtil.getLong(portletRequest, GROUPID);
	    userId = ParamUtil.getLong(portletRequest, USERID);
	    createDate = ParamUtil.getDate(portletRequest, CREATEDATE, DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
	    modifiedDate = ParamUtil.getDate(portletRequest, MODIFIEDDATE , DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
	    serviceInfoId = ParamUtil.getLong(portletRequest, SERVICEINFOID);
	    dossierTemplateId = ParamUtil.getLong(portletRequest, DOSSIERTEMPLATEID);
	    govAgencyCode = ParamUtil.getString(portletRequest, GOVAGENCYCODE);
	    govAgencyName = ParamUtil.getString(portletRequest, GOVAGENCYNAME);
	    govAgencyOrganizationId = ParamUtil.getLong(portletRequest, GOVAGENCYORGANIZATIONID);
	    serviceProcessId = ParamUtil.getLong(portletRequest, SERVICEPROCESSID);
	    dossierId = ParamUtil.getLong(portletRequest, DOSSIERID);
	    fileGroupId = ParamUtil.getLong(portletRequest, FILEGROUPID);
	    processStepId = ParamUtil.getLong(portletRequest, PROCESSSTEPID);
	    actionUserId = ParamUtil.getLong(portletRequest, ACTIONUSERID);
	    actionDatetime = ParamUtil.getDate(portletRequest, ACTIONDATETIME, DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT));
	    actionNote = ParamUtil.getString(portletRequest, ACTIONNOTE);
	    assignToUserId = ParamUtil.getLong(portletRequest, ASSIGNTOUSERID);
	    processWorkflowId = ParamUtil.getLong(portletRequest, PROCESSWORKFLOWID);
	    dossierStatus = ParamUtil.getLong(portletRequest, DOSSIERSTATUS);
	    errorInfo = ParamUtil.getString(portletRequest, ERRORINFO);
	    actionStepId = ParamUtil.getLong(portletRequest, ACTIONSTEPID);
    }

}
