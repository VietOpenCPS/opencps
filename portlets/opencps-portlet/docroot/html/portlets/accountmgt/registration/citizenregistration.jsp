<%@page import="com.liferay.util.Normalizer"%>
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

<%@page import="org.opencps.accountmgt.DuplicateCitizenEmailException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenEmailException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenAddressException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenNameException"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.accountmgt.FileTypeFailException"%>
<%@page import="org.opencps.accountmgt.OutOfSizeFileUploadException"%>
<%@page import="org.opencps.accountmgt.InvalidFileUploadException"%>
<%@page import="org.opencps.accountmgt.InvalidWardCodeException"%>
<%@page import="org.opencps.accountmgt.InvalidDistricCodeException"%>
<%@page import="org.opencps.accountmgt.InvalidCityCodeException"%>
<%@ include file="../init.jsp" %>

<%
	Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
	long citizenID = citizen != null ? citizen.getCitizenId() : 0L;
%>

<liferay-ui:error 
	exception="<%= OutOfLengthCitizenAddressException.class %>" 
	message="<%= OutOfLengthCitizenAddressException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= OutOfLengthCitizenEmailException.class %>" 
	message="<%= OutOfLengthCitizenEmailException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= OutOfLengthCitizenNameException.class %>" 
	message="<%= OutOfLengthCitizenNameException.class.getName() %>" 
/>

<liferay-ui:error 
	exception="<%= DuplicateCitizenEmailException.class %>" 
	message="<%= DuplicateCitizenEmailException.class.getName() %>" 
/>

<liferay-ui:error 
	key="<%=MessageKeys.ACCOUNT_SYSTEM_EXCEPTION_OCCURRED %>" 
	message="system.exception.occured" 
/>

<liferay-ui:error 
	exception="<%= InvalidCityCodeException.class %>" 
	message="<%= InvalidCityCodeException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= InvalidDistricCodeException.class %>" 
	message="<%= InvalidDistricCodeException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= InvalidWardCodeException.class %>" 
	message="<%= InvalidWardCodeException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= InvalidFileUploadException.class %>" 
	message="<%= InvalidFileUploadException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= FileTypeFailException.class %>" 
	message="<%= FileTypeFailException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= OutOfSizeFileUploadException.class %>" 
	message="<%= OutOfSizeFileUploadException.class.getName() %>" 
/>

<portlet:renderURL var="switcherBusinessRegisterURL">
	<portlet:param name="mvcPath" value='<%= templatePath + "businessregistration.jsp" %>'/>
</portlet:renderURL>

<div class="opencps accountmgt fm-registration header">
	<div class="register-label">
		<liferay-ui:message key="register-citizen"/>
	</div>
	<div class="switcher-btn">
		<aui:button name="business" value="business" type="button" href="<%=switcherBusinessRegisterURL.toString() %>"/>
	</div>
</div>

<div class="bottom-horizontal-line"></div>


<portlet:actionURL var="updateCitizenURL" name="updateCitizen">
	<portlet:param 
		name="<%=CitizenDisplayTerms.CITIZEN_ID %>" 
		value="<%=String.valueOf(citizenID) %>"
	/>
	<portlet:param name="currentURL" value="<%=currentURL %>"/>
</portlet:actionURL>

<aui:form 
	action="<%=updateCitizenURL.toString() %>"
	name="fm"	
	method="post"
	enctype="multipart/form-data"
	onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "registerAccount();" %>'
>
	<liferay-util:include 
		page="/html/portlets/accountmgt/registration/citizen/general_info.jsp" 
		servletContext="<%=application %>" 
	/> 

	<liferay-util:include 
		page="/html/portlets/accountmgt/registration/citizen/contact.jsp" 
		servletContext="<%=application %>" 
	/> 
	<aui:row>
		<aui:input 
			name="termsOfUse"
			type="checkbox" 
			label="terms-of-use"
		/>
	</aui:row>
	<aui:row>
		<aui:button name="register" type="submit" value="register" disabled="true"/>
		<aui:button name="back" type="button" value="back" onClick="window.history.back();"/>
	</aui:row>
</aui:form>

<aui:script>
	AUI().ready(function(A) {
		var termsOfUseCheckbox = A.one('#<portlet:namespace />termsOfUseCheckbox');
		
		if(termsOfUseCheckbox) {
			termsOfUseCheckbox.on('click',function() {
				
				var termsOfUse = A.one('#<portlet:namespace />termsOfUse');
				
				var register = A.one('#<portlet:namespace />register');
				
				if(termsOfUse.val() == 'true'){
					register.removeClass('disabled');
					register.removeAttribute('disabled');
				}else{
					register.addClass('disabled');
					register.setAttribute('disabled' , 'true');
				}
			});
		}
		
	});

	Liferay.provide(window, '<portlet:namespace />registerAccount', function() {
		A = AUI();
		var register = A.one('#<portlet:namespace />register');
		var termsOfUse = A.one('#<portlet:namespace />termsOfUse');
		
		if(termsOfUse.val() == 'true'){
			submitForm(document.<portlet:namespace />fm);
		}else{
			return;
		}
	});
	
</aui:script>

