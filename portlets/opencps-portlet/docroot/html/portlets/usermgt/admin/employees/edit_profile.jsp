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
<%@page import="com.liferay.portal.model.UserConstants"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portlet.usersadmin.util.UsersAdminUtil"%>
<%@page import="com.liferay.portal.util.PortletKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.UserPasswordException"%>
<%@page import="com.liferay.portal.security.auth.AuthException"%>
<%@page import="com.liferay.portal.service.GroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Group"%>
<%@page import="com.liferay.portal.service.LayoutLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Layout"%>
<%@ include file="../../init.jsp"%>

<%
	User mappingUser = (User)request.getAttribute(WebKeys.USER_MAPPING_ENTRY);
	String accountEmail = (String)request.getAttribute(WebKeys.TURN_BACK_ACCOUNT_EMAIL);
	String screenName = (String)request.getAttribute(WebKeys.TURN_BACK_SCREEN_NAME);
%>

<aui:model-context bean="<%=mappingUser%>" model="<%=User.class%>" />

<liferay-ui:error-marker key="errorSection" value="edit_profile" />

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

<liferay-ui:error exception="<%= AuthException.class %>" message="<%=AuthException.class.getName() %>"/>

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
			
			<aui:input 
				name="<%= EmployeeDisplayTerm.USER_EMAIL%>"
				disabled="<%=mappingUser != null ? true : false  %>"
				type="text"
			>
				<aui:validator name="email"/>
				<aui:validator name="maxLength">
					<%= PortletPropsValues.USERMGT_EMPLOYEE_EMAIL_LENGTH %>
				</aui:validator>
			</aui:input>
		</aui:col>
		
		<aui:col width="50">
			<c:choose>
				<c:when test='<%= UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, mappingUser, "portrait") %>'>
					
					<%
						Group group = GroupLocalServiceUtil.getFriendlyURLGroup(company.getCompanyId(), "/control_panel");
						Layout ctrLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(group.getGroupId(), true, "/manage");
					%>
					
					<liferay-portlet:renderURL var="editUserPortraitURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>" 
						portletName="<%=PortletKeys.MY_ACCOUNT %>" plid="<%=ctrLayout != null ? ctrLayout.getPlid() : layout.getPlid() %>">
						<liferay-portlet:param name="struts_action" value="/my_account/edit_user_portrait" />
						<liferay-portlet:param name="redirect" value="<%= currentURL %>" />
						<liferay-portlet:param name="p_u_i_d" value="<%= String.valueOf(mappingUser.getUserId()) %>" />
						<liferay-portlet:param name="portrait_id" value="<%= String.valueOf(mappingUser.getPortraitId()) %>" />
					</liferay-portlet:renderURL>

					<liferay-ui:logo-selector
						currentLogoURL="<%= mappingUser.getPortraitURL(themeDisplay) %>"
						defaultLogoURL="<%= UserConstants.getPortraitURL(themeDisplay.getPathImage(), mappingUser.isMale(), 0) %>"
						editLogoURL="<%= editUserPortraitURL %>"
						imageId="<%= mappingUser.getPortraitId() %>"
						logoDisplaySelector=".user-logo"
					/>
				</c:when>
				<c:otherwise>
					<img alt="<liferay-ui:message key="portrait" />" src="<%= mappingUser.getPortraitURL(themeDisplay) %>" />
				</c:otherwise>
			</c:choose>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:input name="changePassWord" type="checkbox" checked="false"/>
	</aui:row>
	
	<div id = "<portlet:namespace />showOrHidePasswordsField" >
		<aui:row>
			<aui:col width="90">
				<aui:input name="<%= EmployeeDisplayTerm.OLD_PASS_WORD%>" type="password"></aui:input>
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
</div>	

<aui:script>
	 AUI().ready(function(A) {
		
		var changePassword = A.one('#<portlet:namespace />changePassWordCheckbox');
		var showOrHidePasswordsField = A.one('#<portlet:namespace />showOrHidePasswordsField');
		if(changePassword.val() == 'false') {
			showOrHidePasswordsField.hide();
		}
		if(changePassword) {
			var changePasswordValue = A.one('#<portlet:namespace />changePassWord');
			changePassword.on('click', function() {
				if(changePasswordValue.val() == "true") {
					showOrHidePasswordsField.show();
				} else {
					showOrHidePasswordsField.hide();
				}
			});
		}
	}); 
</aui:script>