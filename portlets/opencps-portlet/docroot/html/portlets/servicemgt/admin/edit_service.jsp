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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>

<%@ include file="../init.jsp" %>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL");

	ServiceInfo servieInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_ENTRY);
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	String[] serviceSections = new String[]{"general_info", "detail_info", "template_info"};
	
	String[][] categorySections = {serviceSections};
	
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (Validator.isNull(servieInfo)) ? "add-service" : "update-service" %>'
/>

<portlet:actionURL name="updateService" var="updateServiceURL"/>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%= servieInfo != null %>">
		<liferay-ui:icon cssClass="icon-home"/> <%= servieInfo.getServiceName() %>
	</c:if> 
</liferay-util:buffer>


<aui:form name="fm" action="<%=updateServiceURL %>" method="post">

	<aui:model-context bean="<%= servieInfo %>" model="<%= ServiceInfo.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= backURL%>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
	
	<aui:input name="<%= ServiceDisplayTerms.GROUP_ID %>" type="hidden" 
		value="<%= scopeGroupId%>"/>
	<aui:input name="<%= ServiceDisplayTerms.COMPANY_ID %>" type="hidden" 
		value="<%= company.getCompanyId()%>"/>
	<aui:input name="<%= ServiceDisplayTerms.SERVICE_ID %>" type="hidden" 
		value="<%= Validator.isNotNull(servieInfo) ? servieInfo.getServiceinfoId() : StringPool.BLANK %>"/>

	<liferay-ui:form-navigator
		backURL="<%= backURL %>"
		categoryNames="<%= ServiceUtil.SERVICE_CATEGORY_NAMES %>"
		categorySections="<%= categorySections %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%= templatePath + "service/" %>'
	/>
</aui:form>


