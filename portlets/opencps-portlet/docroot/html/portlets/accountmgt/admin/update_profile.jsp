
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

<%@page import="org.opencps.accountmgt.OutOfSizeFileUploadException"%>
<%@page import="org.opencps.accountmgt.FileTypeFailException"%>
<%@page import="org.opencps.accountmgt.InvalidFileUploadException"%>
<%@page import="org.opencps.accountmgt.InvalidWardCodeException"%>
<%@page import="org.opencps.accountmgt.InvalidDistricCodeException"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.accountmgt.InvalidCityCodeException"%>

<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessRepresentativeRoleException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessShortNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessRepresentativeNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthBusinessEnNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenNameException"%>
<%@page import="org.opencps.accountmgt.OutOfLengthCitizenAddressException"%>
<%@page import="com.liferay.portal.UserPasswordException"%>
<%@ include file="../init.jsp" %>


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
/>
<liferay-ui:error 
	exception="<%= OutOfLengthBusinessShortNameException.class %>" 
	message="<%= OutOfLengthBusinessShortNameException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= OutOfLengthBusinessRepresentativeNameException.class %>" 
	message="<%= OutOfLengthBusinessRepresentativeNameException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= OutOfLengthBusinessRepresentativeRoleException.class %>" 
	message="<%= OutOfLengthBusinessRepresentativeRoleException.class.getName() %>" 
/>

<liferay-ui:error 
	key="<%=MessageKeys.DATAMGT_SYSTEM_EXCEPTION_OCCURRED %>" 
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
<%
	long citizenId = ParamUtil.getLong(request, CitizenDisplayTerms.CITIZEN_ID);
	long businessId = ParamUtil.getLong(request, BusinessDisplayTerms.BUSINESS_BUSINESSID);
	String [] ProfileSections = null;
	String [][] categorySections = null;
	String path = StringPool.BLANK;
	Citizen citizen = null;
	Business business = null;
	String backURL = ParamUtil.getString(request, "backURL");
	if(citizenId > 0 ) {
		ProfileSections = new String[2];
		ProfileSections[0] = "general_info";
		ProfileSections[1] = "contact";
		
		path = "/html/portlets/accountmgt/registration/citizen/";
	} else if(businessId > 0) {
		
		ProfileSections = new String[2];
		ProfileSections[0] = "general_info";
		ProfileSections[1] = "contact";
		
		path = "/html/portlets/accountmgt/registration/business/";
	}
	String [][] categorySectionss = {ProfileSections};
	categorySections = categorySectionss; 
	
	
	
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= "update-profile" %>'
/>

<portlet:actionURL var="updateCitizenProfileURL" name="updateCitizenProfile" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
</portlet:actionURL>

<portlet:actionURL var="updateBusinessProfileURL" name="updateBusinessProfile" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<liferay-ui:icon iconCssClass="icon-home" />
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">

</liferay-util:buffer>

<aui:form 
	name="fm" 
	method="post" 
	action='<%= citizenId > 0 ? updateCitizenProfileURL.toString() : updateBusinessProfileURL.toString() %>'
	enctype="multipart/form-data"	
>
	
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= "<%= UserMgtUtil._PROFILE_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath="<%=path%>"
		>	
	</liferay-ui:form-navigator>
	
	<c:choose>
		<c:when test="<%=citizenId > 0 %>">
			<aui:input 
				name="<%=CitizenDisplayTerms.CITIZEN_ID %>" 
				value="<%=String.valueOf(citizen != null ? citizen.getCitizenId() : 0) %>"
				type="hidden"
			/>
		</c:when>
		<c:otherwise>
			<aui:input 
				name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" 
				value="<%=String.valueOf(business != null ? business.getBusinessId() : 0) %>"
				type="hidden"
			/>
		</c:otherwise>
	</c:choose>
</aui:form>

