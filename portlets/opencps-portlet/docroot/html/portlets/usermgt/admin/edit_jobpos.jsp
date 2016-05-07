
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
%>

<liferay-ui:header
	backURL="<%= redirectURL %>"
	title='<%= (jobPos == null) ? "add-jobpos" : "update-jobpos" %>'
/>

<liferay-ui:error 
	key="<%=MessageKeys.USERMGT_JOBPOS_UPDATE_ERROR %>"
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_UPDATE_ERROR) %>"
/>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_JOBPOS_UPDATE_SUCESS %>"
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_UPDATE_SUCESS) %>"
/>
<portlet:actionURL var="updateJobPosURL" name="updateJobPoses">
	<portlet:param name="workingUnitId" value="<%=String.valueOf(workingUnitId) %>"/>
	<portlet:param name="redirectURL" value="<%=redirectURL %>"/>
	<portlet:param name="returnURL" value="<%=returnURL %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">

</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">

</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=updateJobPosURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= redirectURL %>"
		categoryNames= "<%= UserMgtUtil._JOBPOS_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "jobpos/" %>'
		>	
	</liferay-ui:form-navigator>
	<aui:input name="<%=JobPosDisplayTerms.ID_JOBPOS %>" 
		type="hidden" />
		
</aui:form>

<%! 
	Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.edit_jobpos.jsp");
%>
