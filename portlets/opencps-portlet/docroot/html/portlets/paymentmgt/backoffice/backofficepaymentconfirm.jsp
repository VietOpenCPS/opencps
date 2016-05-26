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
	title="payment-confirm"
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
		<tr>
			<td class="col-left">
				<liferay-ui:message key="hinh-thuc-thuc-hien"/>
			</td>
			<td class="col-right">
				<%= HtmlUtil.escape(PortletUtil.getPaymentMethodLabel(paymentFile.getPaymentMethod(), locale)) %>
			</td>
		</tr>	
		<tr>
			<td class="col-left">
				<liferay-ui:message key="chung-tu-kem-theo"/>
			</td>
			<td class="col-right">
				<%=HtmlUtil.escape("") %>
			</td>
		</tr>		
	</table>
</div>

	<aui:button-row>
		<aui:row>
			<aui:col width="30">
				<aui:input type="radio" onChange="paymentFormConfirm('1');" name="confirmHopLe" value="1" label="hop-le" inlineField="true"></aui:input>
				<aui:input type="radio" onChange="paymentFormConfirm('0');" name="confirmHopLe" value="0" label="khong-hop-le" inlineField="true"></aui:input>
				<aui:input type="hidden" name="confirmHopLeHidden" value="0" />
			</aui:col>
			<aui:col width="70">
				<aui:input type="text" cssClass="input100" name="lyDo" placeholder="nhap-ly-do-khong-hop-le" label=""></aui:input>
			</aui:col>
		</aui:row>
		<aui:button name="register" type="button" value="dong-y" disabled="true" />
		<aui:button name="back" value="back" href="<%=backRedirect.toString() %>" />
	</aui:button-row>

</aui:form>
<script type="text/javascript">
	var A = AUI();
	function paymentFormConfirm(code) {
		var register = A.one('#<portlet:namespace />register');
		var confirmHopLeHidden = A.one('#<portlet:namespace />confirmHopLeHidden');
		confirmHopLeHidden.val(code);
		register.removeClass('disabled');
		register.removeAttribute('disabled');
		register.setAttribute('onClick' , 'paymentFormSubmit();');
	}
	function paymentFormSubmit() {
		var lyDo = A.one("#<portlet:namespace />lyDo");
		var confirmHopLeHidden = AUI().one("#<portlet:namespace />confirmHopLeHidden");
		if(confirmHopLeHidden.val() == 0 && lyDo.val() == ''){
			alert('<liferay-ui:message key="khong-hop-le-yeu-cau-nhap-ro-ly-do"/>');
		}else{
			document.getElementById('<portlet:namespace />payForm').submit();
		}
	}
</script>