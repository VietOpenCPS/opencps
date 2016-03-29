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
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.opencps.usermgt.service.EmployeeLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.EmptyEmployeeNameException"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="org.opencps.usermgt.DuplicateEmployeeEmailException"%>
<%@page import="org.opencps.usermgt.OutOfLengthFullNameException"%>
<%@page import="org.opencps.usermgt.OutOfLengthEmployeeEmailException"%>
<%@page import="org.opencps.usermgt.EmptyEmployeeEmailException"%>
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="com.liferay.portal.service.PasswordPolicyLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.PasswordPolicy"%>
<%@page import="com.liferay.portal.UserLockoutException"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@ include file="../init.jsp"%>

<c:choose>
	<c:when test="<%=themeDisplay.isSignedIn() %>">
		<%
			Employee employee = null;
		
			WorkingUnit workingUnit = null;
			
			JobPos jobPos = null;
			
			try{
				long mappingUserId = user.getUserId();
				employee = EmployeeLocalServiceUtil.getEmployeeByMappingUserId(scopeGroupId, mappingUserId);
			}catch(Exception e){
				
			}
			
			PasswordPolicy passwordPolicy = null;

			if (user == null) {
				passwordPolicy = PasswordPolicyLocalServiceUtil.getDefaultPasswordPolicy(company.getCompanyId());
			}
			else {
				passwordPolicy = user.getPasswordPolicy();
			}
			
			String[] employeeSections = new String[]{"general_info", "working_unit", "edit_profile"};
			
			String[][] categorySections = {employeeSections};
			
			request.setAttribute(WebKeys.EMPLOYEE_ENTRY, employee);
			
			request.setAttribute(WebKeys.USER_MAPPING_ENTRY, user);
			
			
			
			if(employee != null){
				
				long workingUnitId = employee.getWorkingUnitId();
				
				if(workingUnitId > 0){
					
					try{
						workingUnit = WorkingUnitLocalServiceUtil.getWorkingUnit(workingUnitId);
					}catch(Exception e){
						
					}
				}
				
				long mainJobPosId = employee.getMainJobPosId();
				
				if(mainJobPosId > 0){
					
					try{
						jobPos = JobPosLocalServiceUtil.getJobPos(mainJobPosId);
					}catch(Exception e){
						
					}
				}
			}
			
			request.setAttribute(WebKeys.WORKING_UNIT_MAPPING_ENTRY, workingUnit);
			
			request.setAttribute(WebKeys.MAIN_JOB_POS_ENTRY, jobPos);
			
			request.setAttribute(WebKeys.USERMGT_VIEW_PROFILE, true);
		%>
		
		<c:choose>
			<c:when test="<%=employee != null %>">
				<portlet:actionURL var="updateProfileURL" name="updateProfile"/>
				<liferay-util:buffer var="htmlTop">
					<c:if test="<%= user != null %>">
						<div class="user-info">
							<div class="float-container">
								<img alt="<%= HtmlUtil.escape(user.getFullName()) %>" class="user-logo" src="<%= user.getPortraitURL(themeDisplay) %>" />
				
								<span class="user-name"><%= HtmlUtil.escape(user.getFullName()) %></span>
							</div>
						</div>
					</c:if> 
				</liferay-util:buffer>
				
				<liferay-util:buffer var="htmlBottom">
				
					<%
					boolean lockedOut = false;
				
					if ((user != null) && (passwordPolicy != null)) {
						try {
							UserLocalServiceUtil.checkLockout(user);
						}
						catch (UserLockoutException ule) {
							lockedOut = true;
						}
					}
					%>
				
					<c:if test="<%= lockedOut %>">
						<aui:button-row>
							<div class="alert alert-block"><liferay-ui:message key="this-user-account-has-been-locked-due-to-excessive-failed-login-attempts" /></div>
				
							<%
							String taglibOnClick = renderResponse.getNamespace() + "saveUser('unlock');";
							%>
				
							<aui:button onClick="<%= taglibOnClick %>" value="unlock" />
						</aui:button-row>
					</c:if>
				</liferay-util:buffer>
				
				<aui:form name="fm" action="<%=updateProfileURL %>" method="post">
				
					<liferay-ui:error exception="<%= EmptyEmployeeEmailException.class %>" 
						message="<%=EmptyEmployeeEmailException.class.getName() %>" 
					/>
					<liferay-ui:error exception="<%= OutOfLengthEmployeeEmailException.class %>" 
						message="<%=OutOfLengthEmployeeEmailException.class.getName() %>"
					/>
					
					<liferay-ui:error exception="<%= EmptyEmployeeNameException.class %>" 
						message="<%=EmptyEmployeeNameException.class.getName() %>" 
					/>
					<liferay-ui:error exception="<%= OutOfLengthFullNameException.class %>" 
						message="<%=OutOfLengthFullNameException.class.getName() %>" 
					/>
					
					<liferay-ui:error exception="<%= DuplicateEmployeeEmailException.class %>" 
						message="<%=DuplicateEmployeeEmailException.class.getName() %>" 
					/>
					<liferay-ui:error exception="<%= PortalException.class %>" 
						message="<%=PortalException.class.getName() %>" 
					/>
					
					<liferay-ui:error key="<%= MessageKeys.USERMGT_SYSTEM_EXCEPTION_OCCURRED%>" 
						message="<%=MessageKeys.USERMGT_SYSTEM_EXCEPTION_OCCURRED %>" 
					/>
					
					<aui:model-context bean="<%=employee %>" model="<%=Employee.class %>" />
					
					<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
					
					<aui:input name="<%=EmployeeDisplayTerm.EMPLOYEE_ID %>" type="hidden"/>
					<aui:input name="<%=EmployeeDisplayTerm.GROUP_ID %>" type="hidden" value="<%= scopeGroupId%>"/>
					<aui:input name="<%=EmployeeDisplayTerm.COMPANY_ID %>" type="hidden" value="<%= company.getCompanyId()%>"/>
				
					<liferay-ui:form-navigator
						backURL="<%= StringPool.BLANK %>"
						categoryNames="<%= UserMgtUtil._EMPLOYESS_CATEGORY_NAMES %>"
						categorySections="<%= categorySections %>"
						htmlBottom="<%= htmlBottom %>"
						htmlTop="<%= htmlTop %>"
						jspPath='<%="/html/portlets/usermgt/admin/employees/" %>'
					/>
				</aui:form>
			</c:when>
			
			<c:otherwise>
				<div class="portlet-msg-alert"><liferay-ui:message key="no-profile"/></div>
			</c:otherwise>
		</c:choose>

		
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-alert"><liferay-ui:message key="please-sign-in-to-view-profile"/></div>
	</c:otherwise>
</c:choose>


 