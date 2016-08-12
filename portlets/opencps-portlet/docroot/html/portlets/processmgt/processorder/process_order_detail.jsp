
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

<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@ include file="../init.jsp"%>

<%
	ProcessOrder processOrder = (ProcessOrder)request.getAttribute(WebKeys.PROCESS_ORDER_ENTRY);
	ProcessStep processStep = (ProcessStep)request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);
	Dossier dossier = (Dossier)request.getAttribute(WebKeys.DOSSIER_ENTRY);
	ServiceProcess serviceProcess = (ServiceProcess)request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	ServiceInfo serviceInfo = (ServiceInfo)request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	ServiceConfig serviceConfig = (ServiceConfig)request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	
	//DossierPart dossierPart = (DossierPart)request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	String[] processOrderSections = new String[]{"dossier_info", "dossier_content", "process", "history"};
	
	String[][] categorySections = {processOrderSections};
%>
<%-- <div class="ocps-custom-header">
	<label class="opcps-label">
		<liferay-ui:message key="process-order" />
	</label>
	<span class="ocps-span">
		<a href="<%=backURL %>"><liferay-ui:message key="back"/></a>
	</span>
</div> --%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="process-order"
/>

<portlet:actionURL var="updateProcessOrderURL" name="updateDossier"/>
<div class="ocps-history-process-bound-navigator">
<liferay-util:buffer var="htmlTop">
	<c:if test="<%= processOrder != null %>">
		<div class="form-navigator-topper dossier-info">
			<div class="form-navigator-container">
				<i aria-hidden="true" class="fa fa-suitcase"></i>
				<span class="form-navigator-topper-name"><%= Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.BLANK %></span>
			</div>
		</div>
	</c:if> 
</liferay-util:buffer>

<liferay-util:buffer var="htmlBottom">

</liferay-util:buffer>

<aui:form name="fm" action="<%=updateProcessOrderURL %>" method="post">

	<aui:model-context bean="<%= processOrder %>" model="<%= ProcessOrder.class %>" />
	
	<aui:input 
		name="redirectURL" 
		type="hidden" 
		value="<%= backURL%>"
	/>
	<aui:input 
		name="returnURL" 
		type="hidden" 
		value="<%= currentURL%>"
	/>
	<div class="opencps-form-navigator-container">
		<liferay-ui:form-navigator
			backURL="<%= backURL %>"
			categoryNames="<%= ProcessUtils._PROCESS_ORDER_CATEGORY_NAMES %>"
			categorySections="<%= categorySections %>"
			htmlBottom="<%= htmlBottom %>"
			htmlTop="<%= htmlTop %>"
			jspPath='<%=templatePath + "dossier/" %>'
			showButtons="<%=false%>"
			displayStyle="left-navigator"
		/>
	</div>
</aui:form>
</div>
<aui:script>
	
	AUI().ready(function(A){
		var varDossierId = A.one("#<portlet:namespace/>dossierId").val();
		if(getCookie('dossierId') != ''){
			var allFormNav = A.all(".form-navigator-content .tab-pane");
			allFormNav.each(function (taskNode) {
            	taskNode.removeClass('active');
            	A.one("#"+taskNode.attr('id')+"Tab").removeClass('tab-selected').removeClass('tab-focused').removeClass('active');
				if(taskNode.attr('id') == '_<%= WebKeys.PROCESS_ORDER_PORTLET %>_process'){
					taskNode.addClass('active');
					A.one("#"+taskNode.attr('id')+"Tab").addClass('tab-selected').addClass('active');
				}
            });
			setCookie('dossierId','');
		}else{
			setCookie('dossierId','');
		}
	});
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.processorder.process_order_detail.jsp");
%>