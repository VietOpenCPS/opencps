<%@page import="org.opencps.paymentmgt.util.PaymentMgtUtil"%>
<%@page import="org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil"%>
<%@page import="org.opencps.paymentmgt.model.PaymentFile"%>
<%@page import="org.opencps.servicemgt.NoSuchServiceInfoException"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.datamgt.NoSuchDictItemException"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.NoSuchDictCollectionException"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
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
	HttpServletRequest r = PortalUtil.getHttpServletRequest(renderRequest);
	String responseCode = PortalUtil.getOriginalServletRequest(r).getParameter("response_code");
	String trans_id = PortalUtil.getOriginalServletRequest(r).getParameter("trans_id");
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

<portlet:renderURL var="backURL">
	<portlet:param name="mvcPath"
		value="/html/portlets/paymentmgt/frontoffice/frontofficepaymentlist.jsp" />	
</portlet:renderURL>

<liferay-ui:header
	backURL="<%= backURL.toString() %>"
	title="payment-list"
	backLabel="back"
/>

<c:choose>
	<c:when test="<%= responseCode != null && responseCode.equals(\"00\") %>">
		<div class="alert alert-success">
			<liferay-ui:message key="keypay-success"></liferay-ui:message>
		</div>
		<%
			String good_code = PortalUtil.getOriginalServletRequest(r).getParameter("good_code");
			String receptionNo = good_code.split("GC_")[1];
			System.out.println("----RECEPTION NO----" + receptionNo);
			System.out.println("----GOOD CODE----" + good_code);
			Dossier dossier = null;
			try {
				dossier = DossierLocalServiceUtil.getDossierByReceptionNo(receptionNo);
				System.out.println("----DOSSIER----" + dossier.getDossierId());
			}
			catch (SystemException e) {
				
			}
			ServiceInfo serviceInfo = null;
			try {
				if (dossier != null)
					serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
			}
			catch (NoSuchServiceInfoException e) {
				
			}
			PaymentFile paymentFile = null;
			try {
				paymentFile = PaymentFileLocalServiceUtil.getPaymentFileByGoodCode(scopeGroupId, good_code);
				paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_APPROVED);
				PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
				System.out.println("----PAYMENT FILE----" + paymentFile.getAmount());
			}
			catch (SystemException e) {
				
			}
		%>
		<c:if test="<%= dossier != null && paymentFile != null %>">
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
					<td class="col-left"><liferay-ui:message key="administration-name"></liferay-ui:message></td>
					<td class="col-right">
						<c:if test="<%= dossier != null %>">
							<%= dossier.getGovAgencyName() %>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="col-left"><liferay-ui:message key="payment-name"></liferay-ui:message></td>
					<td class="col-right"><%= paymentFile != null ? paymentFile.getPaymentName() : "" %></td>
				</tr>
				<tr>
					<td class="col-left"><liferay-ui:message key="amount"></liferay-ui:message></td>
					<td class="col-right"><%= paymentFile.getAmount() %></td>
				</tr>
				<tr>
					<td class="col-left"><liferay-ui:message key="trans_id"></liferay-ui:message></td>
					<td class="col-right"><%= trans_id %></td>
				</tr>
			</table>	
		</div>	
		</c:if>
	</c:when>
	<c:when test="<%= responseCode != null && responseCode.equals(\"10\") %>">
		<div class="alert alert-error">
			<liferay-ui:message key="good-code-not-valid"></liferay-ui:message>
		</div>
	</c:when>
	<c:otherwise>
		<%
			String good_code = PortalUtil.getOriginalServletRequest(r).getParameter("good_code");
			String receptionNo = good_code.split("GC_")[1];
			System.out.println("----RECEPTION NO----" + receptionNo);
			System.out.println("----GOOD CODE----" + good_code);

			PaymentFile paymentFile = null;
			try {
				paymentFile = PaymentFileLocalServiceUtil.getPaymentFileByGoodCode(scopeGroupId, good_code);
				paymentFile.setPaymentStatus(PaymentMgtUtil.PAYMENT_STATUS_REJECTED);
				PaymentFileLocalServiceUtil.updatePaymentFile(paymentFile);
			}
			catch (SystemException e) {
				
			}
			
		%>
	</c:otherwise>
</c:choose>