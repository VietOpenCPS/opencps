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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>

<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessRepresentativeRoleException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessShortNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessRepresentativeNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessEnNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenAddressException"%>
<%@page import="com.liferay.portal.kernel.management.jmx.GetAttributesAction"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="com.liferay.portal.UserPasswordException"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.service.ServiceContextFactory"%>
<%@page import="com.liferay.portal.service.ServiceContext"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="com.liferay.portal.service.PasswordPolicyLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.PasswordPolicy"%>
<%@page import="com.liferay.portal.UserLockoutException"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.service.UserGroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.UserGroup"%>
<%@page import="org.omg.PortableInterceptor.USER_EXCEPTION"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@ include file="/init.jsp" %>



<c:choose>
	<c:when test="<%=themeDisplay.isSignedIn() %>">
		<liferay-ui:error 
				exception="<%= UserPasswordException.class %>" 
			message="<%= UserPasswordException.class.getName() %>" 
		/>
		
		<liferay-ui:error 
			exception="<%= OutOfLengthCitizenAddressException.class %>" 
			message="<%= OutOfLengthCitizenAddressException.class.getName() %>" 
		/>
		
		<liferay-ui:error 
			exception="<%= OutOfLengthCitizenNameException.class %>" 
			message="<%= OutOfLengthCitizenNameException.class.getName() %>" 
		/>
		
		<liferay-ui:error 
			exception="<%= OutOfLengthBusinessNameException.class %>" 
			message="<%= OutOfLengthBusinessNameException.class.getName() %>" 
		/>
		
		<liferay-ui:error 
			exception="<%= OutOfLengthBusinessEnNameException.class %>" 
			message="<%= OutOfLengthBusinessEnNameException.class.getName() %>" 
		/>
		
		<liferay-ui:error 
			exception="<%= OutOfLengthBusinessEnNameException.class %>" 
			message="<%= OutOfLengthBusinessShortNameException.class.getName() %>" 
		/>
		
		<liferay-ui:error 
			exception="<%= OutOfLengthBusinessEnNameException.class %>" 
			message="<%= OutOfLengthBusinessEnNameException.class.getName() %>" 
		/><liferay-ui:error 
			exception="<%= OutOfLengthBusinessShortNameException.class %>" 
			message="<%= OutOfLengthBusinessShortNameException.class.getName() %>" 
		/><liferay-ui:error 
			exception="<%= OutOfLengthBusinessRepresentativeNameException.class %>" 
			message="<%= OutOfLengthBusinessRepresentativeNameException.class.getName() %>" 
		/><liferay-ui:error 
			exception="<%= OutOfLengthBusinessRepresentativeRoleException.class %>" 
			message="<%= OutOfLengthBusinessRepresentativeRoleException.class.getName() %>" 
		/>
		
		<liferay-ui:error 
			key="<%=MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED %>" 
			message="system.exception.occured" 
		/>
		<portlet:actionURL var="updateCitizenProfileURL" name="updateCitizenProfile" >
			<portlet:param name="returnURL" value="<%=currentURL %>"/>
		</portlet:actionURL>
		
		<portlet:actionURL var="updateBusinessProfileURL" name="updateBusinessProfile" >
			<portlet:param name="returnURL" value="<%=currentURL %>"/>
		</portlet:actionURL>
		<%

			String path = StringPool.BLANK;
			
			String [] profileSections = null;
				
		%>
		<aui:form name="fm" 
			method="post" 
			action='<%=accountType.equals("CITIZEN") ? updateCitizenProfileURL.toString() : updateBusinessProfileURL.toString() %>'>
		
		<liferay-util:buffer var="htmlTop">
			
			<div class="form-navigator-topper account-info">
				<div class="form-navigator-container">
					<i aria-hidden="true" class="fa account-info"></i>
					<img alt="<%= HtmlUtil.escape(user.getFullName()) %>" class="user-logo" src="<%= user.getPortraitURL(themeDisplay) %>" />
					<span class="user-name"><%= HtmlUtil.escape(user.getFullName()) %></span>
				</div>
			</div>
			
		</liferay-util:buffer>
		
		<liferay-util:buffer var="htmlBott">
	
			<%
				boolean lockedOut = false;
				PasswordPolicy passwordPolicy = null;
				passwordPolicy = PasswordPolicyLocalServiceUtil
								.getDefaultPasswordPolicy(company.getCompanyId());
				
				if ((passwordPolicy != null)) {
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
		
		<c:choose>
			<c:when test="<%=accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS) %>">
				<%
					profileSections = new String[3];
					profileSections[0] = "general_info";
					profileSections[1] = "contact";
					profileSections[2] = "password";
						
					path = "/html/portlets/accountmgt/registration/business/";
					
					String [][] categorySections = {profileSections};
					
					//Business business = (Business) request.getAttribute(WebKeys.BUSINESS_ENTRY);
				%>
				
				<aui:input 
					name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" 
					value="<%=String.valueOf(business != null ? business.getBusinessId() : 0) %>"
					type="hidden"
				/>
				
				<liferay-ui:form-navigator 
					backURL="<%= currentURL %>"
					categoryNames= "<%= UserMgtUtil._CITIZEN_CATEGORY_NAMES %>"	
					categorySections="<%=categorySections %>" 
					htmlBottom="<%= htmlBott %>"
					htmlTop="<%= htmlTop %>"
					jspPath="<%=path%>"
					>	
				</liferay-ui:form-navigator>
			</c:when>
			<c:when test="<%=accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) %>">
				<%
					profileSections = new String[3];
					profileSections[0] = "general_info";
					profileSections[1] = "contact";
					profileSections[2] = "password";
					path = "/html/portlets/accountmgt/registration/citizen/";
					String [][] categorySections = {profileSections};
					//Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
				%>
				<aui:input 
					name="<%=CitizenDisplayTerms.CITIZEN_ID %>" 
					value="<%=String.valueOf(citizen != null ? citizen.getCitizenId() : 0) %>"
					type="hidden"
				/>
				
				<liferay-ui:form-navigator 
					backURL="<%= currentURL %>"
					categoryNames= "<%= UserMgtUtil._CITIZEN_CATEGORY_NAMES %>"	
					categorySections="<%=categorySections %>" 
					htmlBottom="<%= htmlBott %>"
					htmlTop="<%= htmlTop %>"
					jspPath="<%=path%>"
					>	
				</liferay-ui:form-navigator>
			</c:when>

			<c:otherwise>
				<div class="portlet-msg-alert"><liferay-ui:message key="not-account-profile"/></div>
			</c:otherwise>
		</c:choose>
		</aui:form>
	</c:when> 
	
	<c:otherwise>
		<div class="portlet-msg-alert"><liferay-ui:message key="please-sign-in-to-view-profile"/></div>
	</c:otherwise>
</c:choose>


<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.citizenprofile.jsp");
%>