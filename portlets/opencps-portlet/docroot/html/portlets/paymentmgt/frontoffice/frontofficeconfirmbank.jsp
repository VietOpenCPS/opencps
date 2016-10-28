
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
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
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

<%@ include file="../init.jsp"%>
<%
	String backURL = ParamUtil.getString(request, "backURL");
	String redirectURL = ParamUtil.getString(request, "redirectURL");
	String chuHoSo = ParamUtil.getString(request, "chuHoSo");
	
	long paymentFileId =
		ParamUtil.getLong(
			request, PaymentFileDisplayTerms.PAYMENT_FILE_ID, 0L);
	
	
	PaymentFile paymentFile = null;
	
	try {
		paymentFile =
			PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);
	}
	catch (NoSuchPaymentFileException e) {

	}

	Dossier dossier = null;
	
	if (paymentFile != null) {
		try {
			dossier =
				DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
		}
		catch (NoSuchDossierException e) {

		}
	}
	
	ServiceInfo serviceInfo = null;
	
	if (dossier != null) {
		try {
			serviceInfo =
				ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
		}
		catch (NoSuchServiceInfoException e) {

		}
	}
%>
<style>
.payment-ld button {
	background: none #0090ff;
	border: 0;
	color: white;
	text-transform: uppercase;
	padding: 7px 25px ;
	border-radius: 20px;
	font-family: 'Roboto-Bold';
	position: relative;
	left: 50%;
	transform: translateX(-50%);
	margin-top: 20px !important;
}
</style>

<liferay-ui:header 
	backURL="<%= backURL.toString() %>"
	title="request-bank" cssClass="upercase" 
/>


<portlet:actionURL var="requestBankPaymentURL" windowState="normal" name="requestBankPayment"/>

<div class="payment-ld">
	<aui:form name="requestBankPayment" action="<%= requestBankPaymentURL.toString() %>" method="post" enctype="multipart/form-data">
		<aui:input name="<%= PaymentFileDisplayTerms.PAYMENT_FILE_ID %>" type="hidden" value="<%= paymentFileId %>"></aui:input>
		<aui:input name="redirectURL" type="hidden" value="<%= redirectURL %>"></aui:input>
		<aui:input name="returnURL" type="hidden" value="<%= backURL %>"></aui:input>
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>"/>	

		<div class="content">
			<div class="box100 row-eq-height">
				<div class="box50">
					<div>
					   <p><span><liferay-ui:message key="reception-no"></liferay-ui:message>:</span></p> <%= dossier != null ? dossier.getReceptionNo() : LanguageUtil.get(pageContext, "monitoring-chua-co")  %>
					</div>
					
					<div>
					    <p><span><liferay-ui:message key="payment-name"></liferay-ui:message>:</span></p> <%= paymentFile != null ? paymentFile.getPaymentName() : LanguageUtil.get(pageContext, "monitoring-chua-co")  %>
					</div>
					
					<div class="over100">
					   <p><span><liferay-ui:message key="service-name"/>:</span> <span><%=Validator.isNotNull(serviceInfo)? HtmlUtil.escape(serviceInfo.getServiceName()): LanguageUtil.get(pageContext, "monitoring-chua-co") %></span></p>
					</div>
					
					<div>
						<p><span><liferay-ui:message key="administration-name"></liferay-ui:message>:</span> </p>
						<c:if test="<%= dossier != null %>">
						<%= dossier.getGovAgencyName() %>
						</c:if>
					</div>
					
					<div>
					    <p><span><liferay-ui:message key="subject-name"></liferay-ui:message>:</span> </p><%= chuHoSo != null ? chuHoSo : LanguageUtil.get(pageContext, "monitoring-chua-co")  %>
					</div>
					
					<div>
					    <p><span><liferay-ui:message key="ngay-yeu-cau"></liferay-ui:message>:</span> </p><%=paymentFile != null ? HtmlUtil.escape(DateTimeUtil.convertDateToString(paymentFile.getRequestDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT)): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
					</div>
					
					<div>
					   <p><span><liferay-ui:message key="amount"></liferay-ui:message>: </span> </p><span class="black bold"><%= NumberFormat.getInstance(new Locale("vi","VN")).format(paymentFile.getAmount()) %> <liferay-ui:message key="vnd"></liferay-ui:message></span>
					</div>
				</div>
				
				<div class="box50 text-center bor-left">
					<div>
						<div class="image_placeholder" style="width: 126px; height: 120px;" ></div>
						
						<h5 class="upercase"><liferay-ui:message key="dinh-kem-tep-chung-tu"></liferay-ui:message></h5>
						
						<p>
						   <liferay-ui:message key="chung-tu-thanh-toan"></liferay-ui:message><br><liferay-ui:message key="hoac-hoa-don-chung-nhan-giao-dich-chuyen-khoan-duoc-in-ra"></liferay-ui:message>
						</p>
						
						<div class="paymentUploadBTN">
							<aui:input type="file" cssClass="input-file" name="uploadedFile" label="uploaded-file-payment">
								<aui:validator name="acceptFiles">
								'<%= StringUtil.merge(PortletPropsValues.ACCOUNTMGT_FILE_TYPE) %>'
								</aui:validator>
							</aui:input>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	    <button type="submit" class="baonop"><liferay-ui:message key="request-bank-payment"></liferay-ui:message> <i class="fa fa-chevron-right ML5"></i></button>
	</aui:form>
</div>