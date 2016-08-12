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
    cssClass="upercase"
/>
<div class="payment-ld">
<div class="content overfolow">
                <div class="box50">
                    <div>
                        <p><span><liferay-ui:message key="so-ho-so"/>:</span></p> <%= Validator.isNotNull(soHoSo) ?HtmlUtil.escape(soHoSo): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div class="over100">
                        <p><span><liferay-ui:message key="thu-tuc-hanh-chinh"/>:</span> <span><%=Validator.isNotNull(serviceInfo.getServiceName())? HtmlUtil.escape(serviceInfo.getServiceName()): LanguageUtil.get(pageContext, "monitoring-chua-co") %></span></p>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="co-quan-thuc-hien"/>:</span> </p><%= Validator.isNotNull(coQuanQuanLyHoaDon) ?HtmlUtil.escape(coQuanQuanLyHoaDon): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ten-phi-thanh-toan"/>:</span> </p><%=Validator.isNotNull(paymentFile.getPaymentName()) ?HtmlUtil.escape(paymentFile.getPaymentName()): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ngay-yeu-cau"/>:</span> </p><%=Validator.isNotNull(paymentFile.getRequestDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getRequestDatetime())): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="so-tien"/>:</span> </p><span class="red"><%=HtmlUtil.escape(df2.format(Double.valueOf(paymentFile.getAmount())).toString()) %> <liferay-ui:message key="vnd"/></span>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="tinh-trang-thanh-toan"/>:</span> </p><%=LanguageUtil.get(pageContext, PortletUtil.getPaymentStatusLabel(paymentFile.getPaymentStatus(), locale)) %>
                    </div>                
                </div>
                <div class="box50">
                    <div>
                        <p><span><liferay-ui:message key="ngay-da-bao-nop"/>:</span></p> <%=Validator.isNotNull(paymentFile.getConfirmDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getConfirmDatetime())): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="hinh-thuc-thuc-hien"/>:</span> 
	                        	
	                	</p>
	                			<%
									List<String> paymentOption = ListUtil.toList(StringUtil.split(paymentFile.getPaymentOptions()));
									
									boolean isCash = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_CASH);
									boolean isKeypay = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_KEYPAY);
									boolean isBank = paymentOption.contains(PaymentRequestGenerator.PAY_METHOD_BANK);
								%>
									
									<c:if test="<%= isCash %>">
										[ <liferay-ui:message key="cash"></liferay-ui:message> ]&nbsp;
									</c:if>
									<c:if test="<%= isKeypay %>">
										[ <liferay-ui:message key="keypay"></liferay-ui:message> ]&nbsp;
									</c:if>
									<c:if test="<%= isBank %>">
										[ <liferay-ui:message key="bank"></liferay-ui:message> ]
									</c:if>
									<c:if test="<%= !isBank && !isKeypay && !isCash %>">
										<font style="color: #fff;">-</font>
									</c:if>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="chung-tu-kem-theo"/>:</span> 
							
						</p>
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
		                    <c:choose>
									<c:when test="<%= dlURL != null %>">
										<a target="_blank" href="<%= dlURL %>"><liferay-ui:message key="view-confirm-file-entry"></liferay-ui:message></a>
									</c:when>
									<c:otherwise>
										<liferay-ui:message key="monitoring-chua-co"></liferay-ui:message>
									</c:otherwise>
								</c:choose>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ghi-chu-kem-theo"/>:</span> </p><%=Validator.isNotNull(paymentFile.getRequestNote())? HtmlUtil.escape(paymentFile.getRequestNote()): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="ngay-xac-nhan-thu-phi"/>:</span> </p><%=Validator.isNotNull(paymentFile.getApproveDatetime())?HtmlUtil.escape(dateFormatDate.format(paymentFile.getApproveDatetime())): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="noi-dung-xac-nhan"/>:</span> </p><%=Validator.isNotNull(paymentFile.getApproveNote())? HtmlUtil.escape(paymentFile.getApproveNote()): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>
                    <div>
                        <p><span><liferay-ui:message key="nguoi-thuc-hien"/>:</span> </p><%= Validator.isNotNull(paymentFile.getAccountUserName())? HtmlUtil.escape(paymentFile.getAccountUserName()): LanguageUtil.get(pageContext, "monitoring-chua-co") %>
                    </div>                
                </div>
            </div>
            </div>
