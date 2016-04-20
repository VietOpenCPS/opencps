<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.servicemgt.search.ServiceDisplayTerms"%>
<%@page import="org.opencps.processmgt.model.ServiceProcess"%>
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

<%@ include file="../init.jsp"%>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL");

	ServiceProcess servieProcess = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	
	String tabName = ParamUtil.getString(request, "tab", "general");

	
	String backURL = ParamUtil.getString(request, "backURL");
	String[] processSections = new String [] {};

	if(Validator.isNotNull(servieProcess)) {
		processSections = new String[]{"general", "step", "action", "service"};
	} else {
		processSections = new String[]{"general"};
	}
	
	
	String[][] categorySections = {processSections};
	
%>
<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (Validator.isNull(servieProcess)) ? "add-process" : "update-process" %>'
/>

<portlet:actionURL name="updateProcess" var="updateProcessURL"/>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%= servieProcess != null %>">
		<liferay-ui:icon cssClass="icon-home"/> <%= servieProcess.getProcessName() %>
	</c:if> 
</liferay-util:buffer>


<aui:form name="fm" action="<%=updateProcessURL %>" method="post">

	<aui:model-context bean="<%= servieProcess %>" model="<%= ServiceProcess.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= backURL%>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
	
	<aui:input name="<%= ServiceDisplayTerms.GROUP_ID %>" type="hidden" 
		value="<%= scopeGroupId%>"/>
	<aui:input name="<%= ServiceDisplayTerms.COMPANY_ID %>" type="hidden" 
		value="<%= company.getCompanyId()%>"/>
	<aui:input name="serviceProcessId" type="hidden" 
		value="<%= Validator.isNotNull(servieProcess) ? servieProcess.getServiceProcessId() : StringPool.BLANK %>"/>

	<liferay-ui:form-navigator
		backURL="<%= backURL %>"
		categoryNames='<%= new String [] {"process-info"} %>'
		categorySections="<%= categorySections %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%= templatePath + "process/" %>'
		formName="fm"
	/>
</aui:form>

