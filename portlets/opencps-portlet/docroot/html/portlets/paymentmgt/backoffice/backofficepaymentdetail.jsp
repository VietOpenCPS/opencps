<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil"%>
<%@page import="org.opencps.backend.util.PaymentRequestGenerator"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="java.util.List"%>
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
    cssClass="upercase"
/>
<div class="payment-ld">

<div class="content overfolow">
                <div class="box50">
                	<div>
                        <p><span><liferay-ui:message key="subject-name"/>:</span></p> <%=Validator.isNotNull(dossier)? HtmlUtil.escape(dossier.getSubjectName()): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="reception-no"/>:</span></p> <%= Validator.isNotNull(soHoSo) ?HtmlUtil.escape(soHoSo): "-" %>
                    </div>
                    <div class="over100">
                        <p><span><liferay-ui:message key="thu-tuc-hanh-chinh"/>:</span> <span><%=Validator.isNotNull(serviceInfo)? HtmlUtil.escape(serviceInfo.getServiceName()): "-" %></span></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="co-quan-thuc-hien"/>:</span> </p><%= Validator.isNotNull(coQuanQuanLyHoaDon) ?HtmlUtil.escape(coQuanQuanLyHoaDon): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ten-phi-thanh-toan"/>:</span> </p><%=Validator.isNotNull(paymentFile.getPaymentName()) ?HtmlUtil.escape(paymentFile.getPaymentName()): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ngay-yeu-cau"/>:</span> </p><%=Validator.isNotNull(paymentFile.getRequestDatetime())?HtmlUtil.escape(DateTimeUtil.convertDateToString(paymentFile.getRequestDatetime(), DateTimeUtil._VN_DATE_FORMAT)): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="so-tien"/>:</span> </p><span class="black bold"><%= NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount()) %> <liferay-ui:message key="vnd"></liferay-ui:message></span>
                    </div>
                </div>
                <div class="box50">
                    <div>
                        <p><span><liferay-ui:message key="tinh-trang-thanh-toan"/>:</span> </p><%=LanguageUtil.get(pageContext, PortletUtil.getPaymentStatusLabel(paymentFile.getPaymentStatus(), locale)) %>
                    </div>                
                    <div>
                        <p><span><liferay-ui:message key="payment-method"/>:</span> 
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
                           		-
                           	</c:otherwise>
                         </c:choose>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ngay-da-bao-nop"/>:</span></p> <%=Validator.isNotNull(paymentFile.getConfirmDatetime())?HtmlUtil.escape(DateTimeUtil.convertDateToString(paymentFile.getConfirmDatetime(), DateTimeUtil._VN_DATE_FORMAT)): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="request-note"/>:</span> </p><%=Validator.isNotNull(paymentFile.getRequestNote())? HtmlUtil.escape(paymentFile.getRequestNote()): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="confirm-file-entry-id"/>:</span> 
							
						</p>
						<%
	                        FileEntry fileEntry = null;
	                        try {
	                            fileEntry = DLAppLocalServiceUtil.getFileEntry(paymentFile.getConfirmFileEntryId());
	                        }
	                        catch (NoSuchFileEntryException e) {
	                        }
	                        
	                        String dlURL = StringPool.BLANK;
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
								<a target="_blank" href="<%= dlURL %>"><liferay-ui:message key="view-confirm-file-entry"></liferay-ui:message></a>
							</c:when>
							<c:otherwise>
								-
							</c:otherwise>
						</c:choose>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ngay-xac-nhan-thu-phi"/>:</span> </p><%=Validator.isNotNull(paymentFile.getApproveDatetime())?HtmlUtil.escape(DateTimeUtil.convertDateToString(paymentFile.getApproveDatetime(), DateTimeUtil._VN_DATE_FORMAT)): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="noi-dung-xac-nhan"/>:</span> </p><%=Validator.isNotNull(paymentFile.getApproveNote())? HtmlUtil.escape(paymentFile.getApproveNote()): "-" %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="nguoi-thuc-hien"/>:</span> </p><%= Validator.isNotNull(paymentFile.getAccountUserName())? HtmlUtil.escape(paymentFile.getAccountUserName()): "-" %>
                    </div>                
                </div>
            </div>
            </div>
