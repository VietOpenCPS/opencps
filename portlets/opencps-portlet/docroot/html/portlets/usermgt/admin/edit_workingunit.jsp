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

<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.usermgt.OutOfLengthUnitNameException"%>
<%@page import="org.opencps.usermgt.OutOfLengthUnitEnNameException"%>
<%@page import="org.opencps.usermgt.OutOfScopeException"%>
<%@page import="org.opencps.usermgt.DuplicatEgovAgencyCodeException"%>
<%@page import="org.opencps.usermgt.OutOfLengthUnitEmailException"%>
<%@page import="org.opencps.usermgt.DuplicatWorkingUnitEmailException"%> 
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@ include file="../init.jsp"%>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL"); 
	long workingUnitId = ParamUtil.getLong(request, 
		WorkingUnitDisplayTerms.WORKINGUNIT_ID);
	String [] workingunitSections = null;
	String isAddChild = StringPool.BLANK;
	isAddChild	= ParamUtil.getString(request, "isAddChild");
	WorkingUnit workingUnit = null;	

	try{
		workingUnit = WorkingUnitLocalServiceUtil.fetchWorkingUnit(workingUnitId);
	} catch (Exception e) {
		_log.error(e);
	}
	
	if(workingUnitId == 0 || isAddChild.equals("isAddChild")) {
		workingunitSections = new String[2];
		workingunitSections[0] = "general_workingunit";
		workingunitSections[1] = "contact_workingunit";
		
	} else {
		workingunitSections = new String[3];
		workingunitSections[0] = "general_workingunit";
		workingunitSections[1] = "contact_workingunit";
		workingunitSections[2] = "jobpos";
	}
	
	boolean success = false;

	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){
		
	}
	String[][] categorySections = {workingunitSections};
%>

<liferay-ui:header
	backURL="<%= redirectURL %>"
	title='<%= (workingUnit == null) ? "add-workingunit" : "update-workingunit" %>'
/>
<liferay-ui:error 
	exception="<%= OutOfLengthUnitNameException.class %>" 
	message="<%= OutOfLengthUnitNameException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= OutOfLengthUnitEnNameException.class %>" 
	message="<%= OutOfLengthUnitEnNameException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= OutOfLengthUnitEmailException.class %>" 
	message="<%= OutOfLengthUnitEmailException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= DuplicatEgovAgencyCodeException.class %>" 
	message="<%= DuplicatEgovAgencyCodeException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= OutOfScopeException.class %>" 
	message="<%= OutOfScopeException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= DuplicatWorkingUnitEmailException.class %>" 
	message="<%= DuplicatWorkingUnitEmailException.class.getName() %>" 
/>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_JOBPOS_UPDATE_SUCESS %>"
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_UPDATE_SUCESS) %>"
/>

<portlet:actionURL var="updateWorkingUnitURL" name="updateWorkingUnit" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
	<portlet:param name="redirectURL" value="<%=redirectURL %>"/>
	<portlet:param name="isAddChild" value="<%=isAddChild %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%= workingUnit != null %>">
        <div class="form-navigator-topper edit-workingunit">
            <div class="form-navigator-container">
                <i aria-hidden="true" class="fa edit-workingunit"></i>
                <span class="form-navigator-topper-name"><%= HtmlUtil.escape(workingUnit.getName()) %></span>
            </div>
        </div>
    </c:if> 
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">

</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=updateWorkingUnitURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= redirectURL %>"
		categoryNames= "<%= UserMgtUtil._WORKING_UNIT_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "workingunit/" %>'
		>	
	</liferay-ui:form-navigator>
	<aui:input 
		name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ID %>" 
		value="<%=String.valueOf(workingUnitId) %>"
		type="hidden"
	></aui:input>
</aui:form>

<%!
	private Log _log = LogFactoryUtil.
		getLog("html.portlets.usermgt.admin.edit_workingunit.jsp");
%>

<aui:script use='liferay-util-window'>
	AUI().ready(function(A) {
		var success = '<%= success%>';
		if(success == 'true') {
			Liferay.Util.getOpener().<portlet:namespace/>closePopup('<portlet:namespace/>dialog');
		}
	});
	
    
</aui:script>