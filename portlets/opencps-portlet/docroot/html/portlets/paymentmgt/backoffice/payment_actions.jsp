<%@page import="org.opencps.paymentmgt.util.PaymentMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
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

<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.accountmgt.permissions.BusinessPermission"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../init.jsp" %>


<%
	ResultRow row =	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

	PaymentFile rowPay = (PaymentFile)row.getObject();
	
	PortletURL detailURL = renderResponse.createRenderURL();
	detailURL.setParameter("mvcPath", templatePath + "backofficepaymentdetail.jsp");
	detailURL.setParameter("paymentFileId", String.valueOf(rowPay.getPaymentFileId()));
	detailURL.setParameter("redirect", currentURL);
%>

	<a href="<%=detailURL.toString() %>" ><liferay-ui:message key="detail" ></liferay-ui:message></a> | 

<c:choose>
	<c:when test="<%=
			rowPay.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REQUESTED || rowPay.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REJECTED %>">
		<%
			detailURL.setParameter("mvcPath", templatePath + "backofficepaymentcash.jsp");
		%>
		<a href="<%=detailURL.toString() %>" ><liferay-ui:message key="thu-phi" ></liferay-ui:message></a>
	</c:when>
	
	<c:when test="<%=rowPay.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED %>">
		<%
			detailURL.setParameter("mvcPath", templatePath + "backofficepaymentconfirm.jsp");
		%>
		<a href="<%=detailURL.toString() %>" ><liferay-ui:message key="xac-nhan" ></liferay-ui:message></a>
	</c:when>
	
	<c:when test="<%=rowPay.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_APPROVED %>">
		<%
			detailURL.setParameter("mvcPath", templatePath + "backofficepaymentbill.jsp");
		%>
		<a href="<%=detailURL.toString() %>" ><liferay-ui:message key="in-bien-lai" ></liferay-ui:message></a>
	</c:when>
	
</c:choose>
