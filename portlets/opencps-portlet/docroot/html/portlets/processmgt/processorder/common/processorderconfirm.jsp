<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.util.WebKeys"%>

<%@ include file="../../init.jsp"%>

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
	long processOrderId = ParamUtil.getLong(request, "processOrderId");
	long serviceProcessId = ParamUtil.getLong(request, "serviceProcessId");
	long preProcessStepId = ParamUtil.getLong(request, "preProcessStepId");
	String actionName = ParamUtil.getString(request, "actionName");
	
	ProcessWorkflow processWorkflow = ProcessWorkflowLocalServiceUtil.fetchByF_ProcessWorkflow(serviceProcessId, preProcessStepId, actionName);
%>
<portlet:actionURL name="updateProcess" var="updateProcessURL"/>
<liferay-portlet:renderURL var="returnUrl">
	<liferay-portlet:param name="mvcPath" value="<%=templatePath + \"processordertodolist.jsp\" %>"/>
</liferay-portlet:renderURL>
<aui:form name="fm" action="<%=updateProcessURL %>" method="post">

	<aui:input name="processOrderId" type="hidden" value="<%= String.valueOf(processOrderId) %>"/>
	<aui:input name="serviceProcessId" type="hidden" value="<%= String.valueOf(serviceProcessId) %>"/>
	<aui:input name="preProcessStepId" type="hidden" value="<%= String.valueOf(preProcessStepId) %>"/>
	<aui:input name="actionName" type="hidden" value="<%= actionName %>"/>
	<aui:input name="returnURL" type="hidden" value="<%= returnUrl%>"/>
	
	<c:if test="<%=Validator.isNotNull(processWorkflow) && processWorkflow.getAssignUser() %>">
	
		<aui:select name="assignToUserId">
			<aui:option value="1" label="TODO"></aui:option>
		</aui:select>
	
	</c:if>
	
	<c:if test="<%=Validator.isNotNull(processWorkflow) && processWorkflow.getRequestPayment() %>">
	
		<aui:input name=""></aui:input>
	
	</c:if>
	
	<c:if test="<%=Validator.isNotNull(processWorkflow) && Validator.isNotNull(processWorkflow.getGenerateReceptionNo()) %>">
	
		<aui:input name=""></aui:input>
	
	</c:if>
	
	<c:if test="<%=Validator.isNotNull(processWorkflow) && processWorkflow.getGenerateDeadline() %>">
	
		<aui:input name=""></aui:input>
	
	</c:if>
	
	<aui:input type="checkbox" name="esign" label="ban-dong-y-thuc-hien-ki-dien-tu-van-ban-voi-ma-khoa-duoc-cap"></aui:input>
	
	<%= LanguageUtil.format(pageContext, "ban-co-thuc-su-muon-thuc-hien-x", actionName) %>
	
	<center>
		<aui:button-row>
			<aui:button type="button" name="submit"></aui:button>
			<aui:button type="button" name="cancel"></aui:button>
		</aui:button-row>	
	</center>
	
</aui:form>

<!-- function hidePopup(){ -->
<!--     AUI().ready('aui-dialog', function(A){ -->
<!--         A.DialogManager.hideAll(); -->
<!--     }); -->
<!-- } -->