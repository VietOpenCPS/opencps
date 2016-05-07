<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.impl.WorkflowOutputImpl"%>
<%@page import="org.opencps.processmgt.model.WorkflowOutput"%>
<%@page import="org.opencps.processmgt.service.StepAllowanceLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="com.liferay.portal.kernel.process.ProcessUtil"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="org.opencps.processmgt.model.impl.StepAllowanceImpl"%>
<%@page import="java.util.Collections"%>
<%@page import="org.opencps.processmgt.model.StepAllowance"%>
<%@page import="org.opencps.processmgt.model.ServiceProcess"%>
<%@page import="org.opencps.servicemgt.search.ServiceDisplayTerms"%>
<%@page import="org.opencps.processmgt.model.ProcessStep"%>
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
	
	ServiceProcess serviceProcess = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	
%>

<portlet:actionURL name="updateService" var="updateServiceURL"/>

<aui:form name="serviceFm" method="POST" action="<%= updateServiceURL %>">

	<aui:input name="redirectURL" type="hidden" value="<%= redirectURL %>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL %>"/>
	
	<aui:input name="<%= ServiceDisplayTerms.GROUP_ID %>" type="hidden" 
		value="<%= scopeGroupId%>"/>
	<aui:input name="<%= ServiceDisplayTerms.COMPANY_ID %>" type="hidden" 
		value="<%= company.getCompanyId()%>"/>
		
	<aui:input name="serviceProcessId" type="hidden" 
		value="<%= Validator.isNotNull(serviceProcess) ? serviceProcess.getPrimaryKey() : StringPool.BLANK %>"/>
	
	<%
		
	%>
	
	<aui:button-row>
		<aui:button type="submit" value="<%= Constants.ADD %>"/>
		<aui:button type="cancel" name="cencel" />
	</aui:button-row>
	
</aui:form>

