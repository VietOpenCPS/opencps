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
	int paymentStatus = ParamUtil.getInteger(request, "paymentStatus");
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficepaymentlist.jsp");
%>
<liferay-ui:search-container searchContainer="<%= new PaymentFileFrontOfficeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			PaymentFileSearchTerms searchTerms = (PaymentFileSearchTerms)searchContainer.getSearchTerms();
		
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
				// no column
				row.addText(String.valueOf(row.getPos() + 1));
				
				//reception no column
				Dossier dossier = null; 
				try {
					dossier = DossierLocalServiceUtil.getDossier(paymentFile.getDossierId());
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
				
				row.addText(Validator.isNotNull(dossier) ? dossier.getReceptionNo() : "");
				
				//gov agency name column
				row.addText(dossier != null ? dossier.getGovAgencyName() : "");
							
				//payment name column
				row.addText(paymentFile.getPaymentName());
				
				//amount column
				row.addText(String.valueOf(NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(paymentFile.getAmount())));
				
				// payment status column
				String paymentStatusText = "";
				switch (paymentFile.getPaymentStatus()) {
				case PaymentMgtUtil.PAYMENT_STATUS_ON_PROCESSING:
					paymentStatusText = LanguageUtil.get(pageContext, "on-processing");
					break;
				case PaymentMgtUtil.PAYMENT_STATUS_REQUESTED:
					paymentStatusText = LanguageUtil.get(pageContext, "requested");
					break;
				case PaymentMgtUtil.PAYMENT_STATUS_CONFIRMED:
					paymentStatusText = LanguageUtil.get(pageContext, "confirmed");
					break;
				case PaymentMgtUtil.PAYMENT_STATUS_APPROVED:
					paymentStatusText = LanguageUtil.get(pageContext, "approved");
					break;
				case PaymentMgtUtil.PAYMENT_STATUS_REJECTED:
					paymentStatusText = LanguageUtil.get(pageContext, "rejected");
					break;
				default:
					paymentStatusText = "";		
					break;
				}
				row.addText(paymentStatusText);
				
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  "/html/portlets/paymentmgt/frontoffice/paymentfile_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
