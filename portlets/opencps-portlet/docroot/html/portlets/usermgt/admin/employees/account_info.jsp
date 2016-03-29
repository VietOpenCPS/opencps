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
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.DuplicateUserEmailAddressException"%>
<%@page import="com.liferay.portal.UserPasswordException"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.DuplicateUserScreenNameException"%>
<%@ include file="../../init.jsp"%>

<%
	User mappingUser = (User)request.getAttribute(WebKeys.USER_MAPPING_ENTRY);
	String accountEmail = (String)request.getAttribute(WebKeys.TURN_BACK_ACCOUNT_EMAIL);
	String screenName = (String)request.getAttribute(WebKeys.TURN_BACK_SCREEN_NAME);
%>
<aui:model-context bean="<%=mappingUser%>" model="<%=User.class%>" />
<liferay-ui:error-marker key="errorSection" value="account_info" />

<liferay-ui:error exception="<%= DuplicateUserEmailAddressException.class %>" 
	message="<%=DuplicateUserEmailAddressException.class.getName() %>" 
/>

<liferay-ui:error exception="<%= DuplicateUserScreenNameException.class %>" 
	message="<%=DuplicateUserScreenNameException.class.getName() %>" 
/>
<liferay-ui:error exception="<%= UserPasswordException.class %>">
	<%
		UserPasswordException upe = (UserPasswordException)errorException;
	%>

	<c:if test="<%= upe.getType() == UserPasswordException.PASSWORD_LENGTH %>">

	<%
		int passwordPolicyMinLength = 3;

	%>

	<%= LanguageUtil.format(pageContext, "that-password-is-too-short-or-too-long-please-make-sure-your-password-is-between-x-and-512-characters", String.valueOf(passwordPolicyMinLength), false) %>
</c:if>
</liferay-ui:error>

<aui:row>
	<aui:col width="100">
		<aui:input 
			name="isMappingUser" 
			type="checkbox" 
			inlineField="<%= true %>" 
			inlineLabel="right" 
			disabled="<%=mappingUser != null ? true : false %>"
			value="<%=mappingUser != null ? true : false %>"
		/>
	</aui:col>
</aui:row>

<div id="<portlet:namespace/>accountInfo">
	<aui:row>
		<aui:col width="50">
			<aui:input 
				name="<%= EmployeeDisplayTerm.SCREEN_NAME%>"
				disabled="<%=mappingUser != null ? true : false  %>"
				required="<%=false %>"
				id="mappingUserScreenName"
				type="text"
			>
			</aui:input>
		</aui:col>
		
		<aui:col width="50">
			<aui:input 
				name="<%= EmployeeDisplayTerm.USER_EMAIL%>"
				disabled="<%=mappingUser != null ? true : false  %>"
				type="text"
			>
				<%-- <aui:validator name="required"/> --%>
				<aui:validator name="email"/>
				<aui:validator name="maxLength">
					<%= PortletPropsValues.USERMGT_EMPLOYEE_EMAIL_LENGTH %>
				</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="50">
			<aui:input name="<%= EmployeeDisplayTerm.PASS_WORD%>" type="password"></aui:input>
		</aui:col>
		
		<aui:col width="50">
			<aui:input name="<%= EmployeeDisplayTerm.RE_PASS_WORD%>" type="password">
				<aui:validator name="equalTo">'#<portlet:namespace /><%= EmployeeDisplayTerm.PASS_WORD%>'</aui:validator>
			</aui:input>
		</aui:col>
	</aui:row>

</div>