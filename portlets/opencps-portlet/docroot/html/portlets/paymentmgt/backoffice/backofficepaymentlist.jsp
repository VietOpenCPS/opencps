
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
<%@page import="org.opencps.util.AccountUtil"%>
<%@page import="org.opencps.dossiermgt.bean.AccountBean"%>
<%@page import="org.opencps.paymentmgt.util.PaymentMgtUtil"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil"%>
<%@page import="org.opencps.paymentmgt.model.PaymentFile"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileSearchTerms"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileSearch"%>
<%@page import="java.text.Format"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="com.liferay.portal.service.OrganizationLocalServiceUtil"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="../init.jsp"%>

<liferay-util:include 
	page="/html/portlets/paymentmgt/backoffice/toolbar.jsp" 
	servletContext="<%=application %>" 
/>

<%
	int paymentStatus = ParamUtil.getInteger(request, "paymentStatus",-1);

	String keywords = ParamUtil.getString(request, "keywords");

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficepaymentlist.jsp");	
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	String dossierNo = StringPool.BLANK;
	String dossierOwnerName = StringPool.BLANK;
%>
<div class="payment-ld">
	<div class="content">
		<aui:form name="payForm" action="#">
			<div class="opcs-serviceinfo-list-label">
				<div class="title_box">
					<p class="file_manage_title"><liferay-ui:message key="danh-sach-ho-so-thu-phi" /></p>
					<p class="count"></p>
				</div>
			</div>
			
			<liferay-ui:search-container searchContainer="<%= new PaymentFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
			
				<liferay-ui:search-container-results>
					<%
						PaymentFileSearchTerms searchTerms = (PaymentFileSearchTerms)searchContainer.getSearchTerms();
						
						String[] keywordArrs = null;
						
						if(Validator.isNotNull(searchTerms.getKeywords())){
							keywordArrs = CustomSQLUtil.keywords(searchTerms.getKeywords());
						}
						List<PaymentFile> paymentFiles = null;
						
						Integer totalCount = 0;
						
						AccountBean accountBean = AccountUtil.getAccountBean(request);
						
						long govOrganizationId = accountBean.getOwnerOrganizationId();
						
						try {
							paymentFiles = PaymentFileLocalServiceUtil.searchPaymentFiles(themeDisplay.getScopeGroupId(), paymentStatus, govOrganizationId, keywords, searchContainer.getStart(), searchContainer.getEnd());
							totalCount = PaymentFileLocalServiceUtil.countPaymentFiles(themeDisplay.getScopeGroupId(), paymentStatus, govOrganizationId, keywords);
						} catch(Exception e){
							_log.error(e);
						}
						
						total = totalCount;
						results = paymentFiles;
						
						pageContext.setAttribute("results", results);
						pageContext.setAttribute("total", total);
					%>
				</liferay-ui:search-container-results>	
					<liferay-ui:search-container-row 
						className="org.opencps.paymentmgt.model.PaymentFile" 
						modelVar="paymentFile" 
						keyProperty="paymentFileId"
					>
						<%				
							 
							Dossier dossier = null;
							try {
								dossier = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId());
								dossierNo =  dossier.getReceptionNo();
							} catch (Exception e) {
								//nothing to do
							}
							
							if(paymentFile.getOwnerUserId() > 0){
								Citizen owner = null;
								
								try {
									owner = CitizenLocalServiceUtil.getByMappingUserId(paymentFile.getOwnerUserId());
									dossierOwnerName = owner.getFullName();
								} catch (Exception e) {
									
								}
							} else {
								Business owner = null;
								
								try {
									owner = BusinessLocalServiceUtil.getBymappingOrganizationId(paymentFile.getOwnerOrganizationId());
									dossierOwnerName = owner.getName();
								} catch (Exception e) {
									
								}
								
							}
							String paymentMothodLabel = StringPool.BLANK;
							if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_APPROVED){
								paymentMothodLabel = LanguageUtil.get(pageContext, PortletUtil.getPaymentMethodLabel(paymentFile.getPaymentMethod(), locale)); 
							}
							PortletURL detailURL = renderResponse.createRenderURL();
							detailURL.setParameter("mvcPath", templatePath + "backofficepaymentdetail.jsp");
							detailURL.setParameter("paymentFileId", String.valueOf(paymentFile.getPaymentFileId()));
							detailURL.setParameter("redirect", currentURL);
							
							String classColor = "chothanhtoan";
							if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REQUESTED ){
								classColor = "chothanhtoan";
							}else if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED){
								classColor = "datiepnhan";
							}else if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_APPROVED){
								classColor = "hoanthanh";
							}else if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REJECTED){
								classColor = "loi";
							}
							
							// no column
							row.addText(String.valueOf(row.getPos() + 1));		
						
						row.addText("<p><b style=\"margin-left: -20px; padding-right: 20px;\" class=\"mamau "+classColor+"\"></b><span style=\"width: 95px; display: inline-block;\">"+LanguageUtil.get(pageContext, "reception-no")+":</span> "+dossierNo+"</p><p><span>"+LanguageUtil.get(pageContext, "payment-name")+":</span> "+paymentFile.getPaymentName()+" <a href=\""+detailURL.toString()+"\" class=\"chitiet\">"+LanguageUtil.get(pageContext, "xem-detail")+"</a></p><p><span>"+LanguageUtil.get(pageContext, "subject-name")+":</span>"+dossierOwnerName+"</p>");
						
						row.addText("<p><span>"+LanguageUtil.get(pageContext, "payment-status")+":</span> <span class=\""+classColor+"\">"+LanguageUtil.get(pageContext, PortletUtil.getPaymentStatusLabel(paymentFile.getPaymentStatus(), locale))+"</span></p><p><span>"+LanguageUtil.get(pageContext, "amount")+":</span> <span class=\"sotien\">"+String.valueOf(NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount()))+"</span></p><p><span>"+LanguageUtil.get(pageContext, "payment-method")+":</span>"+paymentMothodLabel+"</p>");	
						
						row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "payment_actions.jsp", config.getServletContext(), request, response);
						
					%>	
					
					<liferay-ui:search-container-column-text>
						
						
					</liferay-ui:search-container-column-text>
				</liferay-ui:search-container-row> 
			
			<liferay-ui:search-iterator type="opencs_page_iterator"/>
		</liferay-ui:search-container>
		
		</aui:form>
	</div>
</div>

<style>
.table.payment tr td:nth-child(2){
   max-width: 600px;
}
.table.payment tr td:nth-child(3){
   max-width: 300px;
}
.table.payment tr td:nth-child(4){
   width: 190px;
}
</style>
<aui:script>
    AUI().ready(function(A) {
    	var allTable = A.all(".table-striped");
    	allTable.each(function (taskNode) {
			taskNode.addClass('payment');
	    });
    });
</aui:script>
<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.paymentmgt.backoffice.backofficepaymentlist.jsp");
%>
