<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
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
<%@page import="java.util.Date"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.util.PortletKeys"%>
<%@page import="org.opencps.dossiermgt.search.DossierBackOfficeSearch"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessDisplayTerms"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>

<%@ include file="../init.jsp"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<%
	String dossierStatus = ParamUtil.getString(request, DossierDisplayTerms.DOSSIER_STATUS, StringPool.BLANK);
	
	String serviceDomainCode = ParamUtil.getString(request, DossierDisplayTerms.SERVICE_DOMAIN_CODE);	

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficedossierlist.jsp");
	iteratorURL.setParameter("tab1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);
	iteratorURL.setParameter(DossierDisplayTerms.DOSSIER_STATUS, dossierStatus);
	iteratorURL.setParameter(DossierDisplayTerms.SERVICE_DOMAIN_CODE, serviceDomainCode);
	iteratorURL.setParameter(DossierDisplayTerms.SUBMIT_DATETIME, serviceDomainCode);
	
	List<String> govAgencyCodes = new ArrayList<String>();
	if(Validator.isNotNull(employee)){
		
		WorkingUnit workingUnit =
				WorkingUnitLocalServiceUtil.fetchWorkingUnit(employee.getWorkingUnitId());
		
		if(Validator.isNotNull(workingUnit)) govAgencyCodes.add(workingUnit.getGovAgencyCode());
		
	}
	
%>

<div class="opencps-searchcontainer-wrapper">
	<liferay-ui:search-container searchContainer="<%= new DossierBackOfficeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
		>
				
		<liferay-ui:search-container-results>
			<%
				DossierSearchTerms searchTerms =
					(DossierSearchTerms) searchContainer.getSearchTerms();

				DictItem domainItem = null;
				String treeIndex = StringPool.BLANK;
				if(Validator.isNotNull(serviceDomainCode)){
					domainItem = DictItemLocalServiceUtil.fetchDictItem(Integer.valueOf(serviceDomainCode));
				}
			
				if(Validator.isNotNull(domainItem)){
					treeIndex = domainItem.getTreeIndex();
				}
				total =
					DossierLocalServiceUtil.countDossierByKeywordDomainAndStatus(
						scopeGroupId, searchTerms.getKeywords(),
						treeIndex, govAgencyCodes, dossierStatus);
				
				OrderByComparator orderByComparator = DossierMgtUtil.getDossierOrderByComparator(DossierDisplayTerms.SUBMIT_DATETIME, "desc");
				
				results =
					DossierLocalServiceUtil.searchDossierByKeywordDomainAndStatus(
						scopeGroupId, searchTerms.getKeywords(),
						treeIndex, govAgencyCodes, dossierStatus,
						searchContainer.getStart(),
						searchContainer.getEnd(),
						orderByComparator);

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
							
				String receiveDatetime = Validator.isNotNull(dossier.getFinishDatetime()) ?
						DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_FORMAT): StringPool.BLANK;	
				// receive datetime column
				if (Validator.isNotNull(dossier.getReceiveDatetime())) {
					receiveDatetime = DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_FORMAT);				
				}
						
				ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
					
				String finishDate = Validator.isNotNull(dossier.getFinishDatetime()) ?
						DateTimeUtil.convertDateToString(dossier.getFinishDatetime(), DateTimeUtil._VN_DATE_FORMAT) : StringPool.BLANK;
						
				String statusText = PortletUtil.getDossierProcessStateLabel(dossier, locale);
				
				row.setClassName("opencps-searchcontainer-row");
				
			%>	
			
			<liferay-ui:search-container-column-text name="stt" href="<%=viewURL %>">
				<%=String.valueOf(row.getPos() + 1) %>
			</liferay-ui:search-container-column-text>
			 <liferay-ui:search-container-column-text>
				<div class="row-fluid">
					<div class="span5 bold-label">
						<liferay-ui:message key="receive-datetime"/>
					</div>
						
					<div class="span7">
						<a href="<%=viewURL.toString()%>"><%=receiveDatetime %></a>
					</div>
				</div>
						
				<div class="row-fluid">
					<div class="span5 bold-label">
						<liferay-ui:message key="reception-no"/>
					</div>
							
					<div class="span7">
						<a href="<%=viewURL.toString()%>"><%=dossier.getReceptionNo() %></a>
					</div>
				</div>
					
				<div class="row-fluid">
					<div class="span5 bold-label">
						<liferay-ui:message key="subjectname"/>
					</div>
						
					<div class="span7">
						<a href="<%=viewURL.toString()%>"><%=dossier.getSubjectName() %></a>
					</div>
				</div>
			</liferay-ui:search-container-column-text>
			
			<liferay-ui:search-container-column-text>
				<div class="row-fluid">
					<div class="span3 bold-label">
						<liferay-ui:message key="serviceinfo-name"/>
					</div>
						
					<div class="span9">
						<a href="<%=viewURL.toString()%>"><%=serviceInfo.getServiceName() %></a>
					</div>
				</div>
						
				<div class="row-fluid">
					<div class="span3 bold-label">
						<liferay-ui:message key="finish-datetime"/>
					</div>
							
					<div class="span9">
						<a href="<%=viewURL.toString()%>"><%=finishDate %></a>
					</div>
				</div>
					
				<div class="row-fluid">
					<div class="span3 bold-label">
						<liferay-ui:message key="process-status"/>
					</div>
						
					<div class="span9">
						<a href="<%=viewURL.toString()%>"><%=statusText %></a>
					</div>
				</div>
			</liferay-ui:search-container-column-text>
					
	</liferay-ui:search-container-row>	
		
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
		
	</liferay-ui:search-container>
</div>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backoffice.backofficedossierlist.jsp");
%>
