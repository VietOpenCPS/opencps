
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

<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.dossiermgt.bean.DossierBean"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="com.liferay.portal.kernel.management.jmx.DoOperationAction"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.RequiredDossierPartException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierTemplateException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<%
	String dossierStatus = ParamUtil.getString(request, "dossierStatus", StringPool.BLANK);
	
	long serviceDomainId = ParamUtil.getLong(request, "serviceDomainId");

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "frontofficedossierlist.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	iteratorURL.setParameter("dossierStatus", String.valueOf(dossierStatus));
	iteratorURL.setParameter("serviceDomainId", String.valueOf(serviceDomainId));
	
	List<Dossier> dossiers =  new ArrayList<Dossier>();
	
	int totalCount = 0;
%>

<liferay-ui:error 
	exception="<%= NoSuchDossierException.class %>" 
	message="<%=NoSuchDossierException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= NoSuchDossierTemplateException.class %>" 
	message="<%=NoSuchDossierTemplateException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= RequiredDossierPartException.class %>" 
	message="<%=RequiredDossierPartException.class.getName() %>"
/>

<%-- <portlet:actionURL var="TestConsumerURL" name="TestConsumer"/>

<aui:button href="<%= TestConsumerURL.toString()%>" name="TestConsumer" value="TestConsumer"/> --%>

<liferay-ui:search-container searchContainer="<%= new DossierSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
			
			searchTerms.setDossierStatus(dossierStatus);
			
			DictItem domainItem = null;
		
			
			try{
				if(serviceDomainId > 0){
					domainItem = DictItemLocalServiceUtil.getDictItem(serviceDomainId);
				}

				if(domainItem != null){
					searchTerms.setServiceDomainIndex(domainItem.getTreeIndex());
				}
				
				%>
					<%@include file="/html/portlets/dossiermgt/frontoffice/dosier_search_results.jspf" %>
				<%
			}catch(Exception e){
				_log.error(e);
			}
		
			total = totalCount;
			results = dossiers;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.bean.DossierBean" 
			modelVar="dossierBean" 
			keyProperty="dossierId"
		>
			<%

				Dossier dossier = dossierBean.getDossier();
				//id column
				row.addText(String.valueOf(row.getPos() + 1 + searchContainer.getStart()));
				//row.addText(DateTimeUtil.convertDateToString(dossier.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT));
				
				row.addText(dossierBean.getServiceName());
				row.addText(dossier.getGovAgencyName());
				row.addText(PortletUtil.getDossierStatusLabel(dossier.getDossierStatus(), locale));
				row.addText(Validator.isNotNull(dossier.getReceiveDatetime()) ? DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT): StringPool.DASH);
				row.addText(dossier.getReceptionNo());
				row.addText(Validator.isNotNull(dossier.getFinishDatetime()) ? DateTimeUtil.convertDateToString(dossier.getFinishDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT): StringPool.DASH);
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN,"/html/portlets/dossiermgt/frontoffice/dossier_actions.jsp", config.getServletContext(), request, response);
				
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.frontofficedossierlist.jsp");
%>
