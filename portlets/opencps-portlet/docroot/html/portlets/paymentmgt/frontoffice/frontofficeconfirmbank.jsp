
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.servicemgt.NoSuchServiceInfoException"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.paymentmgt.NoSuchPaymentFileException"%>
<%@page import="org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil"%>
<%@page import="org.opencps.paymentmgt.model.PaymentFile"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileDisplayTerms"%>
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
<%@ include file="../init.jsp"%>
<%
	String backURL = ParamUtil.getString(request, "backURL");
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	long paymentFileId = ParamUtil.getLong(request, PaymentFileDisplayTerms.PAYMENT_FILE_ID, 0L);
	PaymentFile paymentFile = null;
	try {
		paymentFile = PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);
	}
	catch (NoSuchPaymentFileException e) {
		
	}
	
	Dossier dossier = null;
	if (paymentFile != null) {
		try {
			dossier = DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
		}
		catch (NoSuchDossierException e) {
			
		}
	}
	ServiceInfo serviceInfo = null;
	if (dossier != null) {
		try {
			serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
		}
		catch (NoSuchServiceInfoException e) {
			
		}
	}
%>
<style>
.lookup-result table {
	width: 100%;
}

.lookup-result tr td {
	padding: 5px;
	border: 1px solid #cbcbcb;
}
</style>

<liferay-ui:header backURL="<%= backURL.toString() %>"
	title="request-bank" backLabel="back" />

<div class="lookup-result">
	<table>
		<tr style="background: #fae5d3;">
			<td class="col-left"><liferay-ui:message key="reception-no"></liferay-ui:message></td>
			<td class="col-right"><%= dossier != null ? dossier.getReceptionNo() : "" %></td>
		</tr>
		<tr>
			<td class="col-left"><liferay-ui:message key="service-name"></liferay-ui:message></td>
			<td class="col-right"><%= serviceInfo != null ? serviceInfo.getServiceName() : "" %></td>
		</tr>
		<tr>
			<td class="col-left"><liferay-ui:message
					key="administration-name"></liferay-ui:message></td>
			<td class="col-right"><c:if test="<%= dossier != null %>">
					<%= dossier.getGovAgencyName() %>
				</c:if></td>
		</tr>
		<tr>
			<td class="col-left"><liferay-ui:message key="payment-name"></liferay-ui:message></td>
			<td class="col-right"><%= paymentFile != null ? paymentFile.getPaymentName() : "" %></td>
		</tr>
		<tr>
			<td class="col-left"><liferay-ui:message key="amount"></liferay-ui:message></td>
			<td class="col-right"><%= paymentFile.getAmount() %></td>
		</tr>
	</table>
</div>
<portlet:actionURL var="requestBankPaymentURL" windowState="normal" name="requestBankPayment"/>

<aui:form name="requestBankPayment" action="<%= requestBankPaymentURL.toString() %>" method="post" enctype="multipart/form-data">
	<aui:input name="<%= PaymentFileDisplayTerms.PAYMENT_FILE_ID %>" type="hidden" value="<%= paymentFileId %>"></aui:input>
	<aui:input name="redirectURL" type="hidden" value="<%= redirectURL %>"></aui:input>
	<aui:input name="returnURL" type="hidden" value="<%= backURL %>"></aui:input>
	<aui:input name="<%= Constants.CMD %>" type="hidden" 
		value="<%= Constants.ADD %>"/>	
	<aui:row>
		<aui:col width="50">
			<aui:input name="uploadedFile" type="file" label="uploaded-file"/>
		</aui:col>
	</aui:row>
	<aui:row>
		<span><liferay-ui:message key="request-bank-hint"></liferay-ui:message></span>
	</aui:row>
	<aui:button type="submit" value="request-bank-payment"></aui:button>
</aui:form>