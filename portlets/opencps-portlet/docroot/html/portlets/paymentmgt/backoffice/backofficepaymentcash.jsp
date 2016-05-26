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
<aui:input type="checkbox" name="confirmSubmit" onClick="confirmSubmitFunction();" label="ban-da-thu-thu-so-tien-cua-nguoi-lam-thu-tuc" />
<aui:button-row>
	<a id="<portlet:namespace/>xacNhan" class="btn" disabled onclick="" ><liferay-ui:message key="dong-y"/></a>
	<a id="<portlet:namespace/>xacNhan2" class="btn" onclick="" ><liferay-ui:message key="dong-y"/></a>
	<aui:button name="back" value="back" href="<%=backRedirect.toString() %>" />
</aui:button-row>

</aui:form>
<script type="text/javascript">
	var A = AUI();
	function confirmSubmitFunction() {
		
		if(A.one("#<portlet:namespace/>confirmSubmit").val()){
			alert(A.one("#<portlet:namespace/>xacNhan"));
			A.one("#<portlet:namespace/>xacNhan").removeAttribute('disabled').attr('onclick', "paymentFormSubmit();");
		}
		if(!A.one("#<portlet:namespace/>confirmSubmit").val()){
			alert(A.one("#<portlet:namespace/>xacNhan"));
			A.one("#<portlet:namespace/>xacNhan2").attr("disabled", true);
			A.one("#<portlet:namespace/>xacNhan").attr("disabled", true);
		}
	}
	function paymentFormSubmit() {
		document.getElementById('<portlet:namespace />payForm').submit();
	}
</script>