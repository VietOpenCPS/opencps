
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
<%@page import="com.liferay.portal.UserPasswordException"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>

<%@ include file="/init.jsp" %> 

<liferay-ui:error-marker key="errorSection" value="password" />

<liferay-ui:error 
	exception="<%= UserPasswordException.class %>" 
	message="<%=UserPasswordException.class.getName() %>"
/>

<aui:row cssClass="nav-content-row-2">
	<aui:col>
		<aui:input type="password" name="<%=CitizenDisplayTerms.CURRENT_PASSWORD %>" />
	</aui:col>
</aui:row>

<aui:row cssClass="nav-content-row-2">
	<aui:col>
		<aui:input type="password" name="<%=CitizenDisplayTerms.NEW_PASSWORD %>" />
	</aui:col>
</aui:row>

<aui:row cssClass="nav-content-row-2">
	<aui:col>
		<aui:input type="password" name="<%=CitizenDisplayTerms.RE_PASSWORD %>" >
 			 <aui:validator name="equalTo">'#<portlet:namespace /><%=CitizenDisplayTerms.NEW_PASSWORD %>'</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>



