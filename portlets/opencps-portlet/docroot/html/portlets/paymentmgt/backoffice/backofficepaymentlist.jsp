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
<liferay-util:include page="/html/portlets/paymentmgt/backoffice/toolbar.jsp" servletContext="<%=application %>" />
<%
	String paymentStatus = ParamUtil.getString(request, "paymentStatus");
	String keywords = ParamUtil.getString(request, "keywords");

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficepaymentlist.jsp");	
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	String soHoSo = StringPool.BLANK;
	String chuHoSo = StringPool.BLANK;
%>
<aui:form name="payForm" action="#">
<liferay-ui:search-container searchContainer="<%= new PaymentFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			PaymentFileSearchTerms searchTerms = (PaymentFileSearchTerms)searchContainer.getSearchTerms();
			
			String[] keywordArrs = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				keywordArrs = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			List<PaymentFile> dossierFiles = null;
			Integer totalCount = 0;
			if (keywordArrs != null || Validator.isNotNull(paymentStatus)) {
				try {
					dossierFiles = PaymentFileLocalServiceUtil.searchPaymentFiles(themeDisplay.getScopeGroupId(), paymentStatus, keywords, searchContainer.getStart(), searchContainer.getEnd());
					totalCount = PaymentFileLocalServiceUtil.countPaymentFiles(themeDisplay.getScopeGroupId(), paymentStatus, keywords);
				} catch(Exception e){
					_log.error(e);
				}
				
			}else{
				try {
					dossierFiles = PaymentFileLocalServiceUtil.getPaymentFiles(-1, -1);
					totalCount = PaymentFileLocalServiceUtil.getPaymentFilesCount();
				} catch(Exception e){
					_log.error(e);
				}
			}
			total = totalCount;
			results = dossierFiles;
			
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
				soHoSo = DossierLocalServiceUtil.fetchDossier(paymentFile.getDossierId()).getReceptionNo();
				
				if(paymentFile.getOwnerUserId() > 0){
					Citizen owner = CitizenLocalServiceUtil.getByMappingUserId(paymentFile.getOwnerUserId());
					chuHoSo = Validator.isNotNull(owner)?owner.getFullName():StringPool.BLANK;
				}else{
					Business owner = BusinessLocalServiceUtil.getBymappingOrganizationId(paymentFile.getOwnerOrganizationId());
					chuHoSo = Validator.isNotNull(owner)?owner.getName():StringPool.BLANK;
				}
				
				if (Validator.isNull(chuHoSo)) {
					try {
						chuHoSo = WorkingUnitLocalServiceUtil.fetchByMappingOrganisationId(themeDisplay.getScopeGroupId(), paymentFile.getGovAgencyOrganizationId()).getName();
					} catch (Exception e) {
						
					}
				}
				
				// no column
				row.addText(String.valueOf(row.getPos() + 1));		
			
				row.addText(String.valueOf(soHoSo));
				
				row.addText(String.valueOf(chuHoSo));	
				
				row.addText(String.valueOf(paymentFile.getPaymentName()));	
				
				row.addText(String.valueOf(NumberFormat.getInstance(new Locale("vi", "VN")).format(paymentFile.getAmount())));	
				
				row.addText(LanguageUtil.get(pageContext, PortletUtil.getPaymentStatusLabel(paymentFile.getPaymentStatus(), locale)));	
				
				row.addText(LanguageUtil.get(locale, PaymentMgtUtil.getPaymentMethod(paymentFile.getPaymentFileId())));	
				
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "payment_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

</aui:form>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.paymentmgt.backoffice.backofficepaymentlist.jsp");
%>
