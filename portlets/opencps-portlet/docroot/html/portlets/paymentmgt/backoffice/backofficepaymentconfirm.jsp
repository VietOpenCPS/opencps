<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileVersion"%>
<%@page import="com.liferay.portlet.documentlibrary.NoSuchFileEntryException"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLAppServiceUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileDisplayTerms"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="org.opencps.usermgt.NoSuchWorkingUnitException"%>
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
    Dossier dossier = null;
    ServiceInfo serviceInfo = null;
    if(Validator.isNull(paymentFile)){
        paymentFile = new PaymentFileImpl();
    }else{
        soHoSo = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId()).getReceptionNo();
        try {
             WorkingUnit wunit = WorkingUnitLocalServiceUtil.fetchByMappingOrganisationId(themeDisplay.getScopeGroupId(), paymentFile.getGovAgencyOrganizationId());
             if (wunit != null)
                 coQuanQuanLyHoaDon = wunit.getName();
        } catch (Exception e) {

        }
        dossier = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId());
        serviceInfo = ServiceInfoLocalServiceUtil.fetchServiceInfo(dossier.getServiceInfoId());
    }
    DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
%>
<liferay-ui:header
    backURL="<%= backRedirect %>"
    title="payment-confirm"
    backLabel="back"
/>

<c:choose>
<c:when test="<%= !StringPool.BLANK.equals(coQuanQuanLyHoaDon) %>">
<liferay-ui:success key="<%= MessageKeys.PAYMENT_FILE_CONFIRM_CASH_SUCCESS %>" message="payment.file.confirm.cash.success"
/>
<liferay-ui:error key="<%= MessageKeys.PAYMENT_FILE_CONFIRM_CASH_ERROR %>" message="payment.file.confirm.cash.error" />

<p></p>
<portlet:actionURL var="confirmPaymentRequestedURL" windowState="normal" name="confirmPaymentRequested"/>
<aui:form name="payForm" action="<%= confirmPaymentRequestedURL.toString() %>">
<aui:input type="hidden" name="<%= PaymentFileDisplayTerms.PAYMENT_FILE_ID %>" value="<%= String.valueOf(paymentFileId) %>"></aui:input>
<div class="lookup-result">
   <table>
            <tr style="background: rgb(250, 229, 211) none repeat scroll 0% 0%; text-align: left;">
                <th style="padding: 6px; border: 1px solid lightgray;"><liferay-ui:message key="so-ho-so"/></th>
                <th style="padding: 6px; border: 1px solid lightgray;"><%=HtmlUtil.escape(soHoSo) %></th>
            </tr>
            <tr>
                <td class="col-left">
                    <liferay-ui:message key="thu-tuc-hanh-chinh"/>
                </td>
                <td class="col-right">
                    <%=HtmlUtil.escape(serviceInfo.getServiceName()) %>
                </td>
            </tr>       
            <tr>
                <td class="col-left">
                    <liferay-ui:message key="co-quan-thuc-hien"/>
                </td>
                <td class="col-right">
                    <%=HtmlUtil.escape(coQuanQuanLyHoaDon) %>
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
                <%=HtmlUtil.escape(df2.format(Double.valueOf(paymentFile.getAmount())).toString()) %>
            </td>
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
                <%= LanguageUtil.get(pageContext, PortletUtil.getPaymentMethodLabel(paymentFile.getPaymentMethod(), locale)) %>
            </td>
        </tr>   
        <tr>
            <td class="col-left">
                <liferay-ui:message key="chung-tu-kem-theo"/>
            </td>
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
                        <a class="label opencps dossiermgt part-file-ctr" target="_blank" href="<%= dlURL %>"><liferay-ui:message key="view-confirm-file-entry"></liferay-ui:message></a>
                    </c:if>
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
</c:when>
<c:otherwise>
    <div class="alert">
        <strong><liferay-ui:message key="warning">!</liferay-ui:message></strong>&nbsp;<liferay-ui:message key="working-unit-not-found"></liferay-ui:message>
    </div>
</c:otherwise>
</c:choose>
<style>
.label.opencps.dossiermgt.part-file-ctr{
    color: #fff !important;
    background-color: #25BB61;
    float: left;
    margin-left: 5px;
    padding: 5px 3px;
}
</style>