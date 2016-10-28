<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="org.opencps.backend.util.PaymentRequestGenerator"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="org.opencps.paymentmgt.util.PaymentMgtUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.NoSuchFileEntryException"%>
<%@page import="com.liferay.portlet.documentlibrary.NoSuchFileException"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileVersion"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLAppServiceUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.datamgt.NoSuchDictItemException"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.NoSuchDictCollectionException"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.servicemgt.NoSuchServiceInfoException"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.paymentmgt.NoSuchPaymentFileException"%>
<%@page import="org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileDisplayTerms"%>
<%@page import="org.opencps.paymentmgt.model.PaymentFile"%>
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
	PaymentFile paymentFile = null;
	long paymentFileId = ParamUtil.getLong(request, PaymentFileDisplayTerms.PAYMENT_FILE_ID, 0L);
	try {
		paymentFile = PaymentFileLocalServiceUtil.getPaymentFile(paymentFileId);
	}
	catch (NoSuchPaymentFileException e) {
		
	}
	Dossier dossier = null;
	try {
		if (paymentFile != null)
			dossier = DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
	}
	catch (NoSuchDossierException e) {
		
	}
	
	ServiceInfo serviceInfo = null;
	try {
		if (dossier != null)
			serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
	}
	catch (NoSuchServiceInfoException e) {
		
	}
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	String backURL = ParamUtil.getString(request, "backURL");
	
%>
<style>
.payment-ld .content{
	display: flex;
}
</style>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="payment-request"
/>
<div class="payment-ld">
			<div class="content" >
                <div class="box100">
<c:choose>
	<c:when test="<%= paymentFile != null %>">
		
                    <div class="box50">
                        <div>
                            <p><span><liferay-ui:message key="reception-no"></liferay-ui:message>:</span></p><%= dossier != null ? dossier.getReceptionNo() : LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="payment-name"></liferay-ui:message>:</span></p><%= paymentFile != null ? paymentFile.getPaymentName() : LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                        </div>
                        <div class="over100">
	                        <p><span><liferay-ui:message key="service-name"/>:</span> <span><%=Validator.isNotNull(serviceInfo)? HtmlUtil.escape(serviceInfo.getServiceName()): LanguageUtil.get(pageContext, "monitoring-chua-co") %></span></p>
	                    </div>
                        <div>
                            <p><span><liferay-ui:message key="administration-name"></liferay-ui:message>:</span></p><%= dossier != null? dossier.getGovAgencyName() : LanguageUtil.get(pageContext, "monitoring-chua-co")%>
                        </div>
                        <div>
                             <p><span><liferay-ui:message key="payment-name"></liferay-ui:message>:</span></p><%= paymentFile != null ? paymentFile.getPaymentName() : LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="confirm-datetime"></liferay-ui:message>:</span></p><%=paymentFile != null ? HtmlUtil.escape(DateTimeUtil.convertDateToString(paymentFile.getRequestDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT)): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="amount"></liferay-ui:message>: </span></p><span class="black bold"><%= NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount()) %> <liferay-ui:message key="vnd"></liferay-ui:message></span>
                        </div>
                    </div>
                    <div class="box50">
                        <div>
                            <p><span><liferay-ui:message key="request-note"></liferay-ui:message>:</span></p> <%= Validator.isNotNull(paymentFile) ? paymentFile.getRequestNote() : LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="payment-options"></liferay-ui:message>:</span> 
							</p>
							<%
									List<String> paymentOption = ListUtil.toList(StringUtil.split(paymentFile.getPaymentOptions()));
									
									boolean isCash = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_CASH);
									boolean isKeypay = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_KEYPAY);
									boolean isBank = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_BANK);
								%>
									<c:if test="<%= isCash %>">
										[ <liferay-ui:message key="cash"></liferay-ui:message> ]&nbsp;
									</c:if>
									<c:if test="<%= isKeypay %>">
										[ <liferay-ui:message key="keypay"></liferay-ui:message> ]&nbsp;
									</c:if>
									<c:if test="<%= isBank %>">
										[ <liferay-ui:message key="bank"></liferay-ui:message> ]
									</c:if>
									<c:if test="<%= !isBank && !isKeypay && !isCash %>">
										<font style="color: #fff;">-</font>
									</c:if>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="payment-status-detail"></liferay-ui:message>:</span> 
							</p>
							<c:if test="<%= paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REQUESTED %>">
									<liferay-ui:message key="requested"></liferay-ui:message>
								</c:if>
								<c:if test="<%= paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED %>">
									<liferay-ui:message key="confirmed"></liferay-ui:message>
								</c:if>
								<c:if test="<%= paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_APPROVED %>">
									<liferay-ui:message key="approved"></liferay-ui:message>
								</c:if>
								<c:if test="<%= paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REJECTED %>">
									<liferay-ui:message key="rejected"></liferay-ui:message>
								</c:if>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="payment-method"></liferay-ui:message>:</span> 
							</p>
							<c:choose>
                            		<c:when test="<%= paymentFile.getPaymentMethod() == PaymentMgtUtil.PAYMENT_METHOD_CASH %>">
										<liferay-ui:message key="cash"></liferay-ui:message>
									</c:when>
									<c:when test="<%= paymentFile.getPaymentMethod() == PaymentMgtUtil.PAYMENT_METHOD_KEYPAY %>">
										<liferay-ui:message key="keypay"></liferay-ui:message>
									</c:when>
									<c:when test="<%= paymentFile.getPaymentMethod() == PaymentMgtUtil.PAYMENT_METHOD_BANK %>">
										<liferay-ui:message key="bank"></liferay-ui:message>
									</c:when>
                            		<c:otherwise>
                            			<liferay-ui:message key="monitoring-chua-co"></liferay-ui:message>
                            		</c:otherwise>
                            	</c:choose>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="request-datetime"></liferay-ui:message>:</span> </p><%= Validator.isNotNull(paymentFile) ? DateTimeUtil.convertDateToString(paymentFile.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) : LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="confirm-file-entry-id"></liferay-ui:message>:</span> 
						</p>
						<%
									FileEntry fileEntry = null;
									try {
										fileEntry = DLAppServiceUtil.getFileEntry(paymentFile.getConfirmFileEntryId());
									}
									catch (NoSuchFileEntryException e) {
										
									}
									String dlURL = null;
									if (fileEntry != null) {
										FileVersion fileVersion = fileEntry.getFileVersion();
										 
										String queryString = "";							 
										boolean appendFileEntryVersion = true;
										 
										boolean useAbsoluteURL = true;
										 
										dlURL = DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, queryString, appendFileEntryVersion, useAbsoluteURL);							
									}
								%>
								<c:choose>
									<c:when test="<%= dlURL != null %>">
										<a target="_blank"  class="text-underline" href="<%= dlURL %>"><liferay-ui:message key="view-confirm-file-entry"></liferay-ui:message></a>
									</c:when>
									<c:otherwise>
										<liferay-ui:message key="monitoring-chua-co"></liferay-ui:message>
									</c:otherwise>
								</c:choose>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="approve-datetime-payment"></liferay-ui:message>:</span></p> <%= Validator.isNotNull(paymentFile.getApproveDatetime()) ? DateTimeUtil.convertDateToString(paymentFile.getApproveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : LanguageUtil.get(pageContext, "monitoring-chua-co")  %>
                        </div>
                        <div>
                            <p><span><liferay-ui:message key="approve-payment-notes"></liferay-ui:message>:</span></p> <%= Validator.isNotNull(paymentFile.getApproveNote()) ? paymentFile.getApproveNote() : LanguageUtil.get(pageContext, "monitoring-chua-co")  %>
                        </div>
                    </div>
            
	</c:when>
	<c:otherwise>
	
	</c:otherwise>
</c:choose>
    </div>
            </div>
		</div>