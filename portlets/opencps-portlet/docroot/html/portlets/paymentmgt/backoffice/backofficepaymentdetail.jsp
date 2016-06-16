<%@page import="java.text.DecimalFormat"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileVersion"%>
<%@page import="com.liferay.portlet.documentlibrary.NoSuchFileEntryException"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLAppServiceUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="com.liferay.util.PwdGenerator"%>
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
    title="payment-detail"
    backLabel="back"
/>
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
                <liferay-ui:message key="ngay-yeu-cau"/>
            </td>
            <td class="col-right">
                <%=Validator.isNotNull(paymentFile.getRequestDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getRequestDatetime())):StringPool.BLANK %>
            </td>
        </tr>       
        <tr>
            <td class="col-left">
                <liferay-ui:message key="so-tien"/>
            </td>
            <td class="col-right">
                <%=HtmlUtil.escape(df2.format(Double.valueOf(paymentFile.getAmount())).toString()) %>
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
                <liferay-ui:message key="tinh-trang-thanh-toan"/>
            </td>
            <td class="col-right">
                <%=LanguageUtil.get(pageContext, PortletUtil.getPaymentStatusLabel(paymentFile.getPaymentStatus(), locale)) %>
            </td>
        </tr>       
        <tr>
            <td class="col-left">
                <liferay-ui:message key="ngay-da-bao-nop"/>
            </td>
            <td class="col-right">
                <%=Validator.isNotNull(paymentFile.getConfirmDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getConfirmDatetime())):StringPool.BLANK %>
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
        <tr>
            <td class="col-left">
                <liferay-ui:message key="ngay-xac-nhan-thu-phi"/>
            </td>
            <td class="col-right">
                <%=Validator.isNotNull(paymentFile.getApproveDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getApproveDatetime())):StringPool.BLANK %>
            </td>
        </tr>   
        <tr>
            <td class="col-left">
                <liferay-ui:message key="noi-dung-xac-nhan"/>
            </td>
            <td class="col-right">
                <%=HtmlUtil.escape(paymentFile.getApproveNote()) %>
            </td>
        </tr>   
        <tr>
            <td class="col-left">
                <liferay-ui:message key="nguoi-thuc-hien"/>
            </td>
            <td class="col-right">
                <%=HtmlUtil.escape(paymentFile.getAccountUserName()) %>
            </td>
        </tr>           
    </table>
</div>
<aui:button-row>
<aui:button name="back" value="back" href="<%=backRedirect.toString() %>" />
</aui:button-row>
<style>
.label.opencps.dossiermgt.part-file-ctr{
    color: #fff !important;
    background-color: #25BB61;
    float: left;
    margin-left: 5px;
    padding: 5px 3px;
}
</style>