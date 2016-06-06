<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.util.PortletKeys"%>
<%@page import="org.opencps.dossiermgt.search.DossierBackOfficeSearch"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessDisplayTerms"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>
<%@ include file="../init.jsp"%>
<liferay-util:include page="/html/portlets/processmgt/backofficedossier/toptabs.jsp" servletContext="<%=application %>" />
<liferay-util:include page="/html/portlets/processmgt/backofficedossier/toolbar.jsp" servletContext="<%=application %>" />
<%
	String backURL = ParamUtil.getString(request, "backURL");
	User mappingUser = (User)request.getAttribute(WebKeys.USER_MAPPING_ENTRY);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficedossierlist.jsp");
	iteratorURL.setParameter("tab1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);
	
	String domainCode = ParamUtil.getString(request, DossierDisplayTerms.SERVICE_DOMAIN_CODE);
	String dossierStatus = 	ParamUtil.getString(request, DossierDisplayTerms.DOSSIER_STATUS);
	request.setAttribute(DossierDisplayTerms.SERVICE_DOMAIN_CODE, domainCode);
	request.setAttribute(DossierDisplayTerms.DOSSIER_STATUS, dossierStatus);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("no");
	headerNames.add("receive-datetime");
	headerNames.add("reception-no");
	headerNames.add("subjectname");
	headerNames.add("serviceinfo-name");
	headerNames.add("finish-datetime");
	headerNames.add("dossier-status");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	Integer dossierStatusInt = -1;
	if (!"".equals(dossierStatus)) {
		dossierStatusInt = Integer.parseInt(dossierStatus);	
	}
%>

<liferay-ui:search-container searchContainer="<%= new DossierBackOfficeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
	headerNames="<%= headers %>">
		
	<liferay-ui:search-container-results>
		<%
			DossierSearchTerms searchTerms = (DossierSearchTerms) searchContainer.getSearchTerms();
						
			total =  DossierLocalServiceUtil.countDossierByKeywordDomainAndStatus(scopeGroupId, searchTerms.getKeywords(), 
					domainCode, dossierStatusInt);

			results = DossierLocalServiceUtil.searchDossierByKeywordDomainAndStatus(scopeGroupId, searchTerms.getKeywords(), 
					domainCode, dossierStatusInt, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
		
	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row 
		className="org.opencps.dossiermgt.model.Dossier" 
		modelVar="dossier" 
		keyProperty="dossierId"
	>
		<%			
			PortletURL viewURL = renderResponse.createRenderURL();
			viewURL.setParameter("mvcPath", templatePath + "backofficedossieroverview.jsp");
			viewURL.setParameter("dossierId", String.valueOf(dossier.getDossierId()));
			viewURL.setParameter("backURL", currentURL);

			// no column
			row.addText(String.valueOf(row.getPos() + 1), viewURL);		
		
			// receive datetime column
			if (Validator.isNotNull(dossier.getReceiveDatetime())) {
				row.addText(dateFormatDate.format(dossier.getReceiveDatetime()));				
			}
			else {
				row.addText(StringPool.BLANK);
			}
			
			// reception no column
			row.addText(dossier.getReceptionNo(), viewURL);
			
			// subjectname column
			row.addText(dossier.getSubjectName(), viewURL);
			
			// serviceinfo name column
			ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
			row.addText(serviceInfo.getServiceName());
						
			// finish datetime column
			if (Validator.isNotNull(dossier.getFinishDatetime())) {
				row.addText(dateFormatDate.format(dossier.getFinishDatetime()));				
			}
			else {
				row.addText("");
			}
			
			// dossierstatus column
			String dossierStatusText = "";
			switch (dossier.getDossierStatus()) {
			case PortletConstants.DOSSIER_STATUS_NEW:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-new");
				break;
			case PortletConstants.DOSSIER_STATUS_RECEIVING:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-receiving");
				break;
			case PortletConstants.DOSSIER_STATUS_WAITING:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-waiting");
				break;
			case PortletConstants.DOSSIER_STATUS_PAYING:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-paying");
				break;
			case PortletConstants.DOSSIER_STATUS_PROCESSING:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-processing");
				break;
			case PortletConstants.DOSSIER_STATUS_DONE:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-done");
				break;
			case PortletConstants.DOSSIER_STATUS_SYSTEM:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-system");
				break;
			case PortletConstants.DOSSIER_STATUS_ERROR:
				dossierStatusText = LanguageUtil.get(pageContext, "dossier-status-error");
				break;
			default:
				dossierStatusText = "";
				break;
			}
			row.addText(String.valueOf(dossierStatusText));			
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator/>

</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backoffice.backofficedossierlist.jsp");
%>