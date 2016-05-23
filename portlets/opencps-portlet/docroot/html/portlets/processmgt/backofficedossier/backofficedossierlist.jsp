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
	
	String domainCode = ParamUtil.getString(request, ProcessDisplayTerms.PROCESS_DOMAINCODE);
	String dossierStatus = 	ParamUtil.getString(request, ProcessDisplayTerms.PROCESS_DOSSIERSTATUS);
	request.setAttribute(ProcessDisplayTerms.PROCESS_DOMAINCODE, domainCode);
	request.setAttribute(ProcessDisplayTerms.PROCESS_DOSSIERSTATUS, dossierStatus);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("no");
	headerNames.add("receive-datetime");
	headerNames.add("reception-no");
	headerNames.add("subjectname");
	headerNames.add("serviceinfo-name");
	headerNames.add("dossier-status");
	headerNames.add("finish-datetime");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	Integer dossierStatusInt = -1;
	if (!"".equals(dossierStatus)) {
		dossierStatusInt = Integer.parseInt(dossierStatus);	
	}
%>

<liferay-ui:search-container searchContainer="<%= new DossierSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
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
			row.addText(dateFormatDate.format(dossier.getReceiveDatetime()));
			
			// reception no column
			row.addText(dossier.getReceptionNo(), viewURL);
			
			// subjectname column
			row.addText(dossier.getSubjectName(), viewURL);
			
			// serviceinfo name column
			ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
			row.addText(serviceInfo.getServiceName());
			
			// dossierstatus column
			DictItem itemStatus = DictItemLocalServiceUtil.getDictItem(dossier.getDossierStatus());
			row.addText(itemStatus.getItemName());
			
			// finish datetime column
			row.addText(dateFormatDate.format(dossier.getFinishDatetime()));
		%>	
	
	</liferay-ui:search-container-row>	

	<liferay-ui:search-iterator/>

</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backoffice.backofficedossierlist.jsp");
%>