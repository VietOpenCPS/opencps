<%@page import="com.liferay.portal.kernel.util.UnicodeFormatter"%>
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

<%@ include file="../../init.jsp" %>

<%
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_ENTRY);
%>

<aui:model-context bean="<%= serviceInfo %>" model="<%= ServiceInfo.class %>"/>

<aui:row cssClass="opcps-rows">
	<aui:col width="100">
		<div class="label">
			<liferay-ui:message key="service-process"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_PROCESS %>" 
			toolbarSet="simple" initMethod="initProcess"/>
	</aui:col>
</aui:row>

<aui:row cssClass="opcps-rows">
	<aui:col width="100">
	<div class="label">
		<liferay-ui:message key="service-method"/>
	</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_METHOD %>" 
			toolbarSet="simple" initMethod="initMethod"/>
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_DOSSIER %>" type="textarea" cssClass="txtarea-medium">
		</aui:input> --%>
		<div class="label">
			<liferay-ui:message key="service-dossier"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_DOSSIER %>" initMethod="service_dossier"/>
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_CONDITION %>" type="textarea" cssClass="txtarea-medium">
		</aui:input> --%>
		<div class="label">
			<liferay-ui:message key="service-condition"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_CONDITION %>" initMethod="service_condition"/>
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_DURATION %>" type="textarea" cssClass="txtarea-medium">
		</aui:input>
		 --%>
		 <div class="label">
			<liferay-ui:message key="service-duration"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_DURATION %>" initMethod="service_duration"/>
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_ACTORS %>" type="textarea" cssClass="txtarea-medium">
		</aui:input> --%>
		<div class="label">
			<liferay-ui:message key="service-actors"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_ACTORS %>" initMethod="service_actors"/>
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_FEE %>" type="textarea" cssClass="txtarea-medium">
		</aui:input>
		 --%>
		 <div class="label">
			<liferay-ui:message key="service-fee"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_FEE %>" initMethod="service_fee"/>
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_RESULTS %>" type="textarea" cssClass="txtarea-medium">
		</aui:input> --%>
		 <div class="label">
			<liferay-ui:message key="service-results"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_RESULTS %>" initMethod="service_results"/>
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<%-- <aui:input name="<%= ServiceDisplayTerms.SERVICE_RECORDS %>" type="textarea" cssClass="txtarea-medium">
		</aui:input> --%>
		 <div class="label">
			<liferay-ui:message key="service-records"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_RECORDS %>" initMethod="service_records"/>
		
	</aui:col>
</aui:row>

<aui:row >
	<aui:col width="100">
		<div class="label">
			<liferay-ui:message key="service-instructions"/>
		</div>
		<liferay-ui:input-editor name="<%= ServiceDisplayTerms.SERVICE_INSTRUCTIONS %>" 
			toolbarSet="simple" initMethod="initInstructions"/>
	</aui:col>
</aui:row>

<aui:script>
	function <portlet:namespace />initProcess() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceProcess()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />initMethod() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceMethod()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />initInstructions() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceInstructions()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_dossier() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceDossier()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_condition() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceCondition()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_duration() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceDuration()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_actors() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceActors()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_fee() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceFee()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_results() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceResults()) : StringPool.BLANK %>";
	}
	
	function <portlet:namespace />service_records() {
		return "<%= Validator.isNotNull(serviceInfo) ? UnicodeFormatter.toString(serviceInfo.getServiceRecords()) : StringPool.BLANK %>";
	}

</aui:script>