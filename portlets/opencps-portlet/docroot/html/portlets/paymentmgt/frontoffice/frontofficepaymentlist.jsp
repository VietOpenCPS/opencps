<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="org.opencps.paymentmgt.util.comparator.PaymentFileCreateDateComparator"%>
<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileDisplayTerms"%>
<%@page import="java.util.Locale"%>
<%@page import="com.liferay.portal.service.ServiceContextThreadLocal"%>
<%@page import="com.liferay.portal.service.ServiceContext"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="org.opencps.util.AccountUtil"%>
<%@page import="org.opencps.paymentmgt.util.PaymentMgtUtil"%>
<%@page import="org.opencps.paymentmgt.service.persistence.PaymentFileUtil"%>
<%@page import="org.opencps.datamgt.NoSuchDictItemException"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.servicemgt.NoSuchServiceInfoException"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.datamgt.NoSuchDictCollectionException"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="com.liferay.portal.NoSuchOrganizationException"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.service.OrganizationLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Organization"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.paymentmgt.service.PaymentFileLocalServiceUtil"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.paymentmgt.model.PaymentFile"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileFrontOfficeSearch"%>
<%@page import="org.opencps.paymentmgt.search.PaymentFileSearchTerms"%>
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

<liferay-util:include page="/html/portlets/paymentmgt/frontoffice/toolbar.jsp" servletContext="<%=application %>" />
<%
	int itemsToDisplay_cfg = GetterUtil.getInteger(portletPreferences.getValue("itemsToDisplay", "2"));
	int paymentStatus = ParamUtil.getInteger(request, "paymentStatus", -1);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficepaymentlist.jsp");
%>

<aui:form name="payForm" action="#">

<%
	boolean isCitizen = true;
	
	long ownerObjectId = 0;
	
	ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
	if (AccountUtil.getAccountBean(user.getUserId(), scopeGroupId, serviceContext).isBusiness()) {
		isCitizen = false;
	}
	
	if (AccountUtil.getAccountBean(user.getUserId(), scopeGroupId, serviceContext).isCitizen()) {
		ownerObjectId = AccountUtil.getAccountBean(user.getUserId(), scopeGroupId, serviceContext).getOwnerUserId();
	} else if (AccountUtil.getAccountBean(user.getUserId(), scopeGroupId, serviceContext).isBusiness()) {
		ownerObjectId = AccountUtil.getAccountBean(user.getUserId(), scopeGroupId, serviceContext).getOwnerOrganizationId();
	}
	
	Integer totalCount = 0;										
	List<PaymentFile> paymentFiles = null;
%>
<div class="payment-ld">
<div class="content">
<div class="opcs-serviceinfo-list-label">
	<div class="title_box">
           <p class="file_manage_title_new"><liferay-ui:message key="danh-sach-yeu-cau-thu-phi-moi-nhat" /></p>
           <p class="count"></p>
    </div>
</div>
<!--  danh sach cap nhat yeu cau moi nhat 2 ban ghi -->
<aui:input name="<%=PaymentFileDisplayTerms.PAYMENT_FILE_IDS %>" type="hidden" />
<liferay-ui:search-container rowChecker="<%= new RowChecker(renderResponse) %>" searchContainer="<%= new PaymentFileFrontOfficeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			PaymentFileSearchTerms searchTerms = (PaymentFileSearchTerms)searchContainer.getSearchTerms();
		
			try {
				OrderByComparator orderByComparatorCreateDateTime = new PaymentFileCreateDateComparator(false);
				int[] paymentStatusNewRequest = {PaymentMgtUtil.PAYMENT_STATUS_REQUESTED, PaymentMgtUtil.PAYMENT_STATUS_REJECTED/*, PaymentMgtUtil.PAYMENT_STATUS_APPROVED*/};
				
				paymentFiles = PaymentFileLocalServiceUtil.searchCustomerPaymentFileNewRequest(scopeGroupId, searchTerms.getKeywords(), isCitizen, ownerObjectId, paymentStatusNewRequest, 0, itemsToDisplay_cfg, orderByComparatorCreateDateTime);
				totalCount = paymentFiles.size();
			}catch(Exception e){
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
					String soHoSo = StringPool.BLANK;
					String chuHoSo = StringPool.BLANK;
					
					//reception no column
					Dossier dossier = null; 
					try {
						dossier = DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
						soHoSo =  dossier.getReceptionNo();
					}
					catch (NoSuchDossierException e) {
						
					}
					
					ServiceInfo serviceInfo = null;
					try {
						if (dossier != null)
							serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());	
					}
					catch (NoSuchServiceInfoException e) {
						
					}
					if(paymentFile.getOwnerUserId() > 0){
						Citizen owner = null;
						
						try {
							owner = CitizenLocalServiceUtil.getByMappingUserId(paymentFile.getOwnerUserId());
							chuHoSo = owner.getFullName();
						} catch (Exception e) {
							
						}
					} else {
						Business owner = null;
						
						try {
							owner = BusinessLocalServiceUtil.getBymappingOrganizationId(paymentFile.getOwnerOrganizationId());
							chuHoSo = owner.getName();
						} catch (Exception e) {
							
						}
						
					}
					request.setAttribute("chuHoSo", chuHoSo);
						// payment status column
						String paymentStatusText = LanguageUtil.get(pageContext, PortletUtil.getPaymentStatusLabel(paymentFile.getPaymentStatus(), locale));

						PortletURL detailURLXem = renderResponse.createRenderURL();
						detailURLXem.setParameter("mvcPath", templatePath + "frontofficepaymentdetail.jsp");
						detailURLXem.setParameter(PaymentFileDisplayTerms.PAYMENT_FILE_ID, String.valueOf(paymentFile.getPaymentFileId()));
						detailURLXem.setParameter("redirect", currentURL);
						detailURLXem.setParameter("chuHoSo", chuHoSo);
						String classColor = "chothanhtoan";
						if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REQUESTED || paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REJECTED){
							classColor = "chothanhtoan";
						}else if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED){
							classColor = "datiepnhan";
						}else if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_APPROVED){
							classColor = "hoanthanh";
						}
						
						// no column
// 						row.addText(String.valueOf(row.getPos() + 1));		
					
						row.addText("<p><b style=\"margin-left: -20px; padding-right: 20px;\" class=\"mamau "+classColor+"\"></b><span style=\"width: 95px; display: inline-block;\">"+LanguageUtil.get(pageContext, "reception-no")+":</span> "+soHoSo+"</p><p><span>"+LanguageUtil.get(pageContext, "payment-name")+":</span> "+paymentFile.getPaymentName()+" <a href=\""+detailURLXem.toString()+"\" class=\"chitiet\">"+LanguageUtil.get(pageContext, "xem-detail")+"</a></p><p><span>"+LanguageUtil.get(pageContext, "subject-name")+":</span>"+chuHoSo+"</p>");
						
						row.addText("<p><span>"+LanguageUtil.get(pageContext, "don-vi")+":</span> "+(Validator.isNotNull(dossier)?dossier.getGovAgencyName():StringPool.BLANK)+"</span></p><p><span>"+LanguageUtil.get(pageContext, "amount")+":</span> <span class=\"sotien\">"+String.valueOf(NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount()))+"</span></p><p><span>"+LanguageUtil.get(pageContext, "payment-status")+":</span><span class=\""+classColor+"\">"+paymentStatusText+"</span></p>");	
						
						row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "paymentfile_actions.jsp", config.getServletContext(), request, response);
						row.addText("");
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator paginate="false"/>
</liferay-ui:search-container>

<!-- ket thuc danh sach cap nhat yeu cau moi nhat -->
</div>
</div>
<div class="payment-ld">
<div class="content">
<div class="opcs-serviceinfo-list-label">
	<div class="title_box">
           <p class="file_manage_title"><liferay-ui:message key="danh-sach-yeu-cau-thu-phi" /></p>
           <p class="count"></p>
    </div>
</div>

<liferay-ui:search-container rowChecker="<%= new RowChecker(renderResponse) %>" searchContainer="<%= new PaymentFileFrontOfficeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			PaymentFileSearchTerms searchTerms = (PaymentFileSearchTerms)searchContainer.getSearchTerms();
		
			try {
				paymentFiles = PaymentFileLocalServiceUtil.searchCustomerPaymentFile(scopeGroupId, searchTerms.getKeywords(), isCitizen, ownerObjectId, paymentStatus, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
				totalCount = PaymentFileLocalServiceUtil.countCustomerPaymentFile(scopeGroupId, searchTerms.getKeywords(), isCitizen, ownerObjectId, paymentStatus);
			}catch(Exception e){
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
					String soHoSo = StringPool.BLANK;
					String chuHoSo = StringPool.BLANK;
					
					//reception no column
					Dossier dossier = null; 
					try {
						dossier = DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
						soHoSo =  dossier.getReceptionNo();
					}
					catch (NoSuchDossierException e) {
						
					}
					
					ServiceInfo serviceInfo = null;
					try {
						if (dossier != null)
							serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());	
					}
					catch (NoSuchServiceInfoException e) {
						
					}
					if(paymentFile.getOwnerUserId() > 0){
						Citizen owner = null;
						
						try {
							owner = CitizenLocalServiceUtil.getByMappingUserId(paymentFile.getOwnerUserId());
							chuHoSo = owner.getFullName();
						} catch (Exception e) {
							
						}
					} else {
						Business owner = null;
						
						try {
							owner = BusinessLocalServiceUtil.getBymappingOrganizationId(paymentFile.getOwnerOrganizationId());
							chuHoSo = owner.getName();
						} catch (Exception e) {
							
						}
						
					}
					request.setAttribute("chuHoSo", chuHoSo);
						// payment status column
						String paymentStatusText = "";
						switch (paymentFile.getPaymentStatus()) {
						case PaymentMgtUtil.PAYMENT_STATUS_REQUESTED:
							paymentStatusText =
							    LanguageUtil.get(pageContext, "requested");
							break;
						case PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED:
							paymentStatusText =
							    LanguageUtil.get(pageContext, "confirmed");
							break;
						case PaymentMgtUtil.PAYMENT_STATUS_APPROVED:
							paymentStatusText =
							    LanguageUtil.get(pageContext, "approved");
							break;
						case PaymentMgtUtil.PAYMENT_STATUS_REJECTED:
							paymentStatusText =
							    LanguageUtil.get(pageContext, "rejected");
							break;
						default:
							paymentStatusText =  LanguageUtil.get(pageContext, "requested");
							break;
						}

						PortletURL detailURLXem = renderResponse.createRenderURL();
						detailURLXem.setParameter("mvcPath", templatePath + "frontofficepaymentdetail.jsp");
						detailURLXem.setParameter(PaymentFileDisplayTerms.PAYMENT_FILE_ID, String.valueOf(paymentFile.getPaymentFileId()));
						detailURLXem.setParameter("redirect", currentURL);
						detailURLXem.setParameter("chuHoSo", chuHoSo);
						String classColor = "chothanhtoan";
						if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REQUESTED || paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_REJECTED){
							classColor = "chothanhtoan";
						}else if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED){
							classColor = "datiepnhan";
						}else if(paymentFile.getPaymentStatus() == PaymentMgtUtil.PAYMENT_STATUS_APPROVED){
							classColor = "hoanthanh";
						}
						
						// no column
// 						row.addText(String.valueOf(row.getPos() + 1));		
					
						row.addText("<p><b style=\"margin-left: -20px; padding-right: 20px;\" class=\"mamau "+classColor+"\"></b><span style=\"width: 95px; display: inline-block;\">"+LanguageUtil.get(pageContext, "reception-no")+":</span> "+soHoSo+"</p><p><span>"+LanguageUtil.get(pageContext, "payment-name")+":</span> "+paymentFile.getPaymentName()+" <a href=\""+detailURLXem.toString()+"\" class=\"chitiet\">"+LanguageUtil.get(pageContext, "xem-detail")+"</a></p><p><span>"+LanguageUtil.get(pageContext, "subject-name")+":</span>"+chuHoSo+"</p>");
						
						row.addText("<p><span>"+LanguageUtil.get(pageContext, "don-vi")+":</span> "+(Validator.isNotNull(dossier)?dossier.getGovAgencyName():StringPool.BLANK)+"</span></p><p><span>"+LanguageUtil.get(pageContext, "amount")+":</span> <span class=\"sotien\">"+String.valueOf(NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount()))+"</span></p><p><span>"+LanguageUtil.get(pageContext, "payment-status")+":</span><span class=\""+classColor+"\">"+paymentStatusText+"</span></p>");	
						
						row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "paymentfile_actions.jsp", config.getServletContext(), request, response);
						row.addText("");
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>
</div>
</div>
</aui:form>

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
.table.payment tr td a.button{
	width: 145px !important;
}
</style>
<portlet:actionURL var="requestBankPaymentURL" windowState="normal" name="requestBankPaymentItems"/>
<aui:script>
    AUI().ready(function(A) {
    	var allTable = A.all(".table-striped");
    	allTable.each(function (taskNode) {
			taskNode.addClass('payment');
	    });
    });
	Liferay.provide(window,'<portlet:namespace />addPaymentItems', function() {
		var checkBoxValue = Liferay.Util.listCheckedExcept(document.<portlet:namespace />payForm, "<portlet:namespace />allRowIds");
		if(checkBoxValue==""||checkBoxValue==null){
			alert('<%= UnicodeLanguageUtil.get(pageContext, "payment-items") %>');
			return false;
		}
		if (confirm('<%= UnicodeLanguageUtil.get(pageContext, "payment-items-confirm") %>')) {
			document.<portlet:namespace />payForm.<portlet:namespace />paymentFileIds.value=checkBoxValue;
			submitForm(document.<portlet:namespace />payForm, "<%=requestBankPaymentURL.toString()%>");
		}
	},['liferay-util-list-fields']);
	
</aui:script>