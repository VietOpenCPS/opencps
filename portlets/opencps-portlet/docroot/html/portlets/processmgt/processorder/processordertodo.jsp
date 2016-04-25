<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.util.WebKeys"%>

<%@ include file="../init.jsp"%>

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
 **/
%>

<%
	String redirectURL = ParamUtil.getString(request, "redirectURL");

	ProcessOrder processOrder = (ProcessOrder) request.getAttribute(WebKeys.PROCESS_ORDER);
	
	String backURL = ParamUtil.getString(request, WebKeys.CURRENT_URL);
	String[] processSections = new String [] {};

	processSections = new String[]{"processorderdossier", "processorderdossierfile", "processorderdoing", "processorderactionhistory"};
	
	
	String[][] categorySections = {processSections};
	
%>
<portlet:actionURL name="updateProcess" var="updateProcessURL"/>

<liferay-util:buffer var="htmlBottom">
	<aui:button value="back" href="<%=backURL %>" cssClass="btn-right" ></aui:button>
</liferay-util:buffer>
<aui:form name="fm" action="<%=updateProcessURL %>" method="post">

	<aui:model-context bean="<%= processOrder %>" model="<%= ProcessOrder.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= backURL%>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
	
	<liferay-ui:form-navigator
		backURL="<%= backURL %>"
		categoryNames='<%= new String [] {"phieu-xu-ly-ho-so"} %>'
		categorySections="<%= categorySections %>"
		jspPath='<%= templatePath + "process/" %>'
		formName="fm"
		showButtons="false"
		htmlBottom="<%=htmlBottom %>"
	/>
</aui:form>

<style>
	.btn-right{
		float: right;
	}
</style>