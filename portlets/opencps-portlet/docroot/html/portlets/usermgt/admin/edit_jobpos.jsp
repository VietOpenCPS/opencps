
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
%>
<%@ include file="../init.jsp"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.search.JobPosDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>

<%
	long workingUnitId = ParamUtil.getLong(request, "workingUnitId");
	long jobPosId = ParamUtil.getLong(request, JobPosDisplayTerms.ID_JOBPOS);
	
	JobPos jobPos = null;
	
	try {
		jobPos = JobPosLocalServiceUtil.fetchJobPos(jobPosId);
	} catch(Exception e) {
		_log.error(e);
	}
		
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	String returnURL = currentURL;
	String [] jobPosSections = {"general_jobpos","role_jobpos"};
	String [][] categorySections = {jobPosSections};
	
	boolean success = false;

	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){
		
	}
	
%>

<liferay-ui:header
	backURL="<%= redirectURL %>"
	title='<%= (jobPos == null) ? "add-jobpos" : "update-jobpos" %>'
/>


<liferay-ui:error 
	key="<%=MessageKeys.USERMGT_JOBPOS_DELETE_ERROR %>" 
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_DELETE_ERROR) %>"
/>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_JOBPOS_DELETE_SUCCESS %>"
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_DELETE_SUCCESS) %>"
/>

<portlet:actionURL var="updateJobPosURL" name="updateJobPoses">
	<portlet:param name="workingUnitId" value="<%=String.valueOf(workingUnitId) %>"/>
	<portlet:param name="returnURL" value="<%=returnURL %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%= jobPos != null %>">
        <div class="form-navigator-topper edit-jobpos">
            <div class="form-navigator-container">
                <i aria-hidden="true" class="fa edit-jobpos"></i>
                <span class="form-navigator-topper-name"><%= HtmlUtil.escape(jobPos.getTitle()) %></span>
            </div>
        </div>
    </c:if> 
</liferay-util:buffer>
<div class = "class-to-action">
	<liferay-util:buffer var="htmlBot">

	</liferay-util:buffer>
</div>


<aui:form name="fm1" 
	method="post" 
	action="<%=updateJobPosURL.toString() %>">
<div class="opencps-form-navigator-container">
	<liferay-ui:form-navigator 
		backURL="<%= redirectURL %>"
		categoryNames= "<%= UserMgtUtil._JOBPOS_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "jobpos/" %>'
		displayStyle="left-navigator"
		>
	</liferay-ui:form-navigator>
</div>
	<aui:input name="<%=JobPosDisplayTerms.ID_JOBPOS %>" 
		type="hidden" />
		
</aui:form>

<%! 
	Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.edit_jobpos.jsp");
%>
<aui:script use='liferay-util-window'>
	AUI().ready(function(A) {
		var success = '<%= success%>';
		if(success == 'true') {
			Liferay.Util.getOpener().<portlet:namespace/>closePopup('<portlet:namespace/>dialog');
		}
	});
</aui:script>