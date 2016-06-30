<%@page import="org.opencps.backend.util.PaymentRequestGenerator"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileDisplayTerms"%>
<%@page import="org.opencps.paymentmgt.model.PaymentFile"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
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
	ResultRow row =	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

	PaymentFile paymentFile = (PaymentFile)row.getObject();
	
	int paymentStatus = paymentFile.getPaymentStatus();
	
	List<String> paymentOption = ListUtil.toList(StringUtil.split(paymentFile.getPaymentOptions()));
	
	boolean isCash = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_CASH);
	boolean isKeypay = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_KEYPAY);
	boolean isBank = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_BANK);
%>

<portlet:renderURL var="viewPaymentDetail">
	<portlet:param name="<%= PaymentFileDisplayTerms.PAYMENT_FILE_ID %>"
		value="<%=String.valueOf(paymentFile.getPaymentFileId()) %>" />
	<portlet:param name="mvcPath"
		value="/html/portlets/paymentmgt/frontoffice/frontofficepaymentdetail.jsp" />
	<portlet:param name="redirectURL" value="<%= currentURL %>" />
	<portlet:param name="backURL" value="<%=currentURL %>"/>
	
</portlet:renderURL>

<portlet:renderURL var="keypayTransaction">
	<portlet:param name="<%= PaymentFileDisplayTerms.PAYMENT_FILE_ID %>"
		value="<%=String.valueOf(paymentFile.getPaymentFileId()) %>" />
	<portlet:param name="mvcPath"
		value="/html/portlets/paymentmgt/frontoffice/frontofficekeypay.jsp" />
	<portlet:param name="redirectURL" value="<%= currentURL %>" />
	<portlet:param name="backURL" value="<%=currentURL %>"/>
	
</portlet:renderURL>

<portlet:renderURL var="bankTransaction">
	<portlet:param name="<%= PaymentFileDisplayTerms.PAYMENT_FILE_ID %>"
		value="<%=String.valueOf(paymentFile.getPaymentFileId()) %>" />
	<portlet:param name="mvcPath"
		value="/html/portlets/paymentmgt/frontoffice/frontofficeconfirmbank.jsp" />
	<portlet:param name="redirectURL" value="<%= currentURL %>" />
	<portlet:param name="backURL" value="<%=currentURL %>"/>
	
</portlet:renderURL>

<liferay-ui:icon image="view" cssClass="view" message="view"
	url="<%=viewPaymentDetail.toString()%>" />

<c:if test="<%= paymentStatus == PaymentMgtUtil.PAYMENT_STATUS_REQUESTED
|| paymentStatus == PaymentMgtUtil.PAYMENT_STATUS_REJECTED%>">
    <liferay-ui:icon image="key" cssClass="view" message="keypay-transaction"
        url="<%=paymentFile.getKeypayUrl()%>" />
    
    <liferay-ui:icon image="post" cssClass="view" message="bank-transaction"
        url="<%=bankTransaction.toString()%>" />    
</c:if>
    