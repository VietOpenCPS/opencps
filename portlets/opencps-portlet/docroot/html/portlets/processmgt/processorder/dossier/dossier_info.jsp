
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

<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@ include file="../../init.jsp"%>

<%
	ProcessOrder processOrder = (ProcessOrder)request.getAttribute(WebKeys.PROCESS_ORDER_ENTRY);
	ProcessStep processStep = (ProcessStep)request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);
	Dossier dossier = (Dossier)request.getAttribute(WebKeys.DOSSIER_ENTRY);
	ServiceProcess serviceProcess = (ServiceProcess)request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	ServiceInfo serviceInfo = (ServiceInfo)request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	ServiceConfig serviceConfig = (ServiceConfig)request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
%>
<div class="ocps-dossier-info">
<table class="table table-bordered dossier-info-table">
	<tr>
		<td width="150">
			<liferay-ui:message key="dossier-no"/>
		</td>
		<td >
			<%=dossier != null ? dossier.getDossierId() :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="service-name"/>
		</td>
		<td >
			<%=serviceInfo != null ? serviceInfo.getServiceName() :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="subject-name"/>
		</td>
		<td >
			<%=dossier != null ? dossier.getSubjectName() :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="address"/>
		</td>
		<td >
			<%=dossier != null ? ( dossier.getAddress() + ", " + dossier.getWardName() + ", "  + dossier.getDistrictName() + ", "  + dossier.getCityName() ):  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="contact-name"/>
		</td>
		<td >
			<%=dossier != null && Validator.isNotNull(dossier.getContactName()) ? dossier.getContactName() :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="contact-tel-no"/>
		</td>
		<td >
			<%=dossier != null && Validator.isNotNull(dossier.getContactTelNo()) ? dossier.getContactTelNo() :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="email"/>
		</td>
		<td >
			<%=dossier != null && Validator.isNotNull(dossier.getContactEmail()) ? dossier.getContactEmail() :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="reception-date"/>
		</td>
		<td >
			<%=processOrder != null ? DateTimeUtil.convertDateToString(processOrder.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="reception-no"/>
		</td>
		<td >
			<%=dossier != null &&  Validator.isNotNull(dossier.getReceptionNo())? dossier.getReceptionNo() :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="estimate-date"/>
		</td>
		<td >
			<%=StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="complate-date"/>
		</td>
		<td >
			<%=StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="dossier-status"/>
		</td>
		<td >
			<%=dossier != null ? PortletUtil.getDossierStatusLabel(dossier.getDossierStatus(), locale) :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="modified-date"/>
		</td>
		<td >
			<%=processOrder != null ? DateTimeUtil.convertDateToString(processOrder.getModifiedDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) :  StringPool.BLANK %>
		</td>
	</tr>
	<tr>
		<td >
			<liferay-ui:message key="note"/>
		</td>
		<td >
			<%=dossier != null && Validator.isNotNull(dossier.getNote()) ? dossier.getNote() : StringPool.BLANK%>
		</td>
	</tr>
</table>
</div>
