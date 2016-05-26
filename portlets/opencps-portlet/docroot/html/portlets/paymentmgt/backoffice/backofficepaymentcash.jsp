<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="org.opencps.paymentmgt.model.impl.PaymentFileImpl"%>
<%@page import="org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil"%>
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

<style>
.lookup-result table {
	width: 100%;
}

.lookup-result tr td {
	padding: 5px;
	border: 1px solid #cbcbcb;
}
</style>
<%
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	String backRedirect = ParamUtil.getString(request, "redirect");
	long paymentFileId = ParamUtil.getLong(request, "paymentFileId");
	PaymentFile paymentFile = PaymentFileLocalServiceUtil.fetchPaymentFile(paymentFileId);
	String soHoSo = StringPool.BLANK;
	String coQuanQuanLyHoaDon = StringPool.BLANK;
	if(Validator.isNull(paymentFile)){
		paymentFile = new PaymentFileImpl();
	}else{
		soHoSo = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId()).getReceptionNo();
		coQuanQuanLyHoaDon = WorkingUnitLocalServiceUtil.fetchByMappingOrganisationId(themeDisplay.getScopeGroupId(), paymentFile.getGovAgencyOrganizationId()).getName();
	}
%>
<liferay-ui:header
	backURL="<%= backRedirect %>"
	title="payment-cash"
	backLabel="back"
/>
<p></p>
<aui:form name="payForm" action="">
<div class="lookup-result">
	<table>
		<tr>
			<th><liferay-ui:message key="so-ho-so"/></th>
			<th><%=HtmlUtil.escape(soHoSo) %></th>
		</tr>
		<tr>
			<td class="col-left">
				<liferay-ui:message key="thu-tuc-hanh-chinh"/>
			</td>
			<td class="col-right">
				<%=HtmlUtil.escape(coQuanQuanLyHoaDon) %>
			</td>
		</tr>		
		<tr>
			<td class="col-left">
				<liferay-ui:message key="co-quan-thuc-hien"/>
			</td>
			<td class="col-right">
				<%=HtmlUtil.escape(paymentFile.getPaymentName()) %>
			</td>
		</tr>		
		<tr>
			<td class="col-left">
				<liferay-ui:message key="ten-phi-thanh-toan"/>
			</td>
			<td class="col-right">
				<%=HtmlUtil.escape(paymentFile.getPaymentName()) %>
			</td>
		</tr>		
		<tr>
			<td class="col-left">
				<liferay-ui:message key="so-tien"/>
			</td>
			<td class="col-right">
				<%=HtmlUtil.escape(String.valueOf(paymentFile.getAmount())) %>
			</td>
		</tr>		
		<tr>
			<td class="col-left">
				<liferay-ui:message key="ghi-chu-kem-theo"/>
			</td>
			<td class="col-right">
				<%=HtmlUtil.escape(paymentFile.getRequestNote()) %>
			</td>
		</tr>		
			
	</table>
</div>
	<aui:button-row>
	<aui:input 
			name="termsOfUse"
			type="checkbox" 
			label="ban-da-thu-thu-so-tien-cua-nguoi-lam-thu-tuc"
		/>
		<aui:button name="register" type="button" value="dong-y" disabled="true" />
		<aui:button name="back" value="back" href="<%=backRedirect.toString() %>" />
	</aui:button-row>

</aui:form>
<aui:script>
	AUI().ready(function(A) {
		var termsOfUseCheckbox = A.one('#<portlet:namespace />termsOfUseCheckbox');
		if(termsOfUseCheckbox) {
			termsOfUseCheckbox.on('click',function() {
				
				var termsOfUse = A.one('#<portlet:namespace />termsOfUse');
				
				var register = A.one('#<portlet:namespace />register');
				
				if(termsOfUse.val() == 'true'){
					register.removeClass('disabled');
					register.removeAttribute('disabled');
					register.setAttribute('onClick' , 'paymentFormSubmit();');
				}else{
					register.addClass('disabled');
					register.setAttribute('disabled' , 'true');
					register.removeAttribute('onClick');
				}
			});
		}
	});
</aui:script>
<script type="text/javascript">
	function paymentFormSubmit() {
		document.getElementById('<portlet:namespace />payForm').submit();
	}
</script>