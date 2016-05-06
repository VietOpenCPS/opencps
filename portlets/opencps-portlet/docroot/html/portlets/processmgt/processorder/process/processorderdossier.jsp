<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalService"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
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
 */
%>
<%
	ProcessOrder processOrder = (ProcessOrder) request.getAttribute(WebKeys.PROCESS_ORDER);
	Dossier dossier = DossierLocalServiceUtil.fetchDossier(processOrder.getDossierId());
%>

<table style="width: 100%;" class="info-td">
	<tr style="background-color: rgb(245, 245, 245); font-weight: bold;">
		<td style="width: 30%;"><liferay-ui:message key="ho-so-so" /></th>
		<td><%=(Validator.isNotNull(dossier)?String.valueOf(dossier.getDossierId()):StringPool.BLANK) %></th>
	</tr>
	<tr >
		<td style="width: 30%;"><liferay-ui:message key="thu-tuc" /></td>
		<td><%=(Validator.isNotNull(dossier)?String.valueOf(dossier.getServiceInfoId()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="chu-ho-so" /></td>
		<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getSubjectName()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td><liferay-ui:message key="dia-chi" /></td>
		<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getAddress()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="nguoi-lien-he" /></td>
		<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getContactName()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="dien-thoai" /></td>
		<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getContactTelNo()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="email-ngay-tiep-nhan" /></td>
		<td><%=(Validator.isNotNull(dossier)?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getReceiveDatetime()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="ma-so-tiep-nhan" /></td>
		<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getReceptionNo()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="ngay-hen-tra" /></td>
		<td><%=(Validator.isNotNull(dossier)?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getEstimateDatetime()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="ngay-hoan-thanh" /></td>
		<td><%=(Validator.isNotNull(dossier)?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getFinishDatetime()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="trang-thai-ho-so" /></td>
		<td><%=(Validator.isNotNull(dossier)?dossier.getDossierStatus():StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="ngay-cap-nhat" /></td>
		<td><%=(Validator.isNotNull(dossier)?DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_TIME_FORMAT).format(dossier.getSubmitDatetime()):StringPool.BLANK) %></td>
	</tr>
	<tr>
		<td style="width: 30%;"><liferay-ui:message key="ghi-chu-ho-so" /></td>
		<td><%=(Validator.isNotNull(dossier)?HtmlUtil.escape(dossier.getNote()):StringPool.BLANK) %></td>
	</tr>
</table>
<style>
.info-td td{
	border: 1px solid gainsboro;
	padding: 5px;
}
</style>