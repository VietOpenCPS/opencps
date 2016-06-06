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
.lookup-result table {
	width: 100%;
}

.lookup-result tr td {
	padding: 5px;
	border: 1px solid #cbcbcb;
}
</style>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="payment-request"
	backLabel="back"
/>
<div class="lookup-result">
<c:choose>
	<c:when test="<%= paymentFile != null %>">
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
				<td class="col-left"><liferay-ui:message key="payment-name"></liferay-ui:message></td>
				<td class="col-right"><%= paymentFile != null ? paymentFile.getPaymentName() : "" %></td>
			</tr>
			<tr>
				<td class="col-left"><liferay-ui:message key="administration-name"></liferay-ui:message></td>
				<td class="col-right">
					<c:if test="<%= dossier != null %>">
						<%= dossier.getGovAgencyName() %>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="col-left"><liferay-ui:message key="request-datetime"></liferay-ui:message></td>
				<td class="col-right"><%= Validator.isNotNull(paymentFile.getRequestDatetime()) ? DateTimeUtil.convertDateToString(paymentFile.getRequestDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : "" %></td>
			</tr>
			<tr>
				<td class="col-left"><liferay-ui:message key="amount"></liferay-ui:message></td>
				<td class="col-right"><%= NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount()) %></td>
			</tr>
			<tr>
				<td class="col-left"><liferay-ui:message key="request-note"></liferay-ui:message></td>
				<td class="col-right"><%= Validator.isNotNull(paymentFile.getRequestNote()) ? paymentFile.getRequestNote() : "" %></td>
			</tr>
			<tr>
				<td class="col-left"><liferay-ui:message key="payment-options"></liferay-ui:message></td>
				<td class="col-right">
					<%
						boolean isCash = (((paymentFile.getPaymentOptions()) & 1) != 0);
						boolean isKeypay = (((paymentFile.getPaymentOptions() >>> 1) & 1) != 0);
						boolean isBank = (((paymentFile.getPaymentOptions() >>> 2) & 1) != 0);
					%>
					<c:if test="<%= isCash %>">
						[<liferay-ui:message key="cash"></liferay-ui:message>]&nbsp;
					</c:if>
					<c:if test="<%= isKeypay %>">
						[<liferay-ui:message key="keypay"></liferay-ui:message>]&nbsp;
					</c:if>
					<c:if test="<%= isBank %>">
						[<liferay-ui:message key="bank"></liferay-ui:message>]
					</c:if>
				</td>
			</tr>
			<c:if test="<%= isBank %>">
			<tr>
				<td class="col-left"><liferay-ui:message key="bank-info"></liferay-ui:message></td>
				<td class="col-right">
					<%= paymentFile.getBankInfo() %>
				</td>
			</tr>
			</c:if>
			<c:if test="<%= isCash %>">
			<tr>
				<td class="col-left"><liferay-ui:message key="place-info"></liferay-ui:message></td>
				<td class="col-right">
					<%= paymentFile.getPlaceInfo() %>
				</td>
			</tr>
			</c:if>
			<tr>
				<td class="col-left"><liferay-ui:message key="payment-status-detail"></liferay-ui:message></td>
				<td class="col-right">
					<c:if test="<%= paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_ON_PROCESSING %>">
						<liferay-ui:message key="on-processing"></liferay-ui:message>
					</c:if>
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
				</td>
			</tr>			
			<tr>
				<td class="col-left"><liferay-ui:message key="payment-method"></liferay-ui:message></td>
				<td class="col-right">
					<c:if test="<%= paymentFile.getPaymentMethod() == 1 %>">
						<liferay-ui:message key="cash"></liferay-ui:message>
					</c:if>
					<c:if test="<%= paymentFile.getPaymentMethod() == 2 %>">
						<liferay-ui:message key="keypay"></liferay-ui:message>
					</c:if>
					<c:if test="<%= paymentFile.getPaymentMethod() == 3 %>">
						<liferay-ui:message key="bank"></liferay-ui:message>
					</c:if>
				</td>
			</tr>			
			<tr>
				<td class="col-left"><liferay-ui:message key="confirm-datetime"></liferay-ui:message></td>
				<td class="col-right">
					<%= Validator.isNotNull(paymentFile.getConfirmDatetime()) ? DateTimeUtil.convertDateToString(paymentFile.getConfirmDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : ""  %>
				</td>
			</tr>			
			<tr>
				<td class="col-left"><liferay-ui:message key="confirm-file-entry-id"></liferay-ui:message></td>
				<td class="col-right">
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
					<c:if test="<%= dlURL != null %>">
						<a target="_blank" href="<%= dlURL %>"><liferay-ui:message key="view-confirm-file-entry"></liferay-ui:message></a>
					</c:if>
				</td>
			</tr>			
			<tr>
				<td class="col-left"><liferay-ui:message key="approve-datetime"></liferay-ui:message></td>
				<td class="col-right">
					<%= Validator.isNotNull(paymentFile.getApproveDatetime()) ? DateTimeUtil.convertDateToString(paymentFile.getApproveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : ""  %>
				</td>
			</tr>			
		</table>
	</c:when>
	<c:otherwise>
	
	</c:otherwise>
</c:choose>
</div>