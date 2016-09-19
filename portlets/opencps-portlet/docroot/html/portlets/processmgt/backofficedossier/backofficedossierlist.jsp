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
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessDisplayTerms"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>

<%@ include file="../init.jsp"%>

<liferay-util:include 
	page="/html/portlets/processmgt/backofficedossier/toptabs.jsp" 
	servletContext="<%=application %>" 
/>
<liferay-util:include 
	page="/html/portlets/processmgt/backofficedossier/toolbar.jsp" 
	servletContext="<%=application %>" 
/>

<%
	String dossierStatus = 	ParamUtil.getString(request, "dossierStatusValue");
	String domainCode = ParamUtil.getString(request, DossierDisplayTerms.SERVICE_DOMAIN_CODE);

	String backURL = ParamUtil.getString(request, "backURL");
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficedossierlist.jsp");
	iteratorURL.setParameter("tab1", ProcessMgtUtil.TOP_TABS_DOSSIERLIST);
	iteratorURL.setParameter("dossierStatusValue", dossierStatus);
	iteratorURL.setParameter(DossierDisplayTerms.SERVICE_DOMAIN_CODE, domainCode);
	
	request.setAttribute(DossierDisplayTerms.SERVICE_DOMAIN_CODE, domainCode);
	/* request.setAttribute(DossierDisplayTerms.DOSSIER_STATUS, dossierStatus); */
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("boundcol2");
	headerNames.add("boundcol3");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone); 
	
%>

<c:choose>
	<c:if test="<%=employee != null %>">
		<%
			List<String> govAgencyCodes = new ArrayList<String>();
			try {

				WorkingUnit workingUnit =
					WorkingUnitLocalServiceUtil.getWorkingUnit(employee.getWorkingUnitId());
				govAgencyCodes.add(workingUnit.getGovAgencyCode());

			}
			catch (Exception e) {
			}
		%>
		<div class="opencps-searchcontainer-wrapper">
			<liferay-ui:search-container searchContainer="<%= new DossierBackOfficeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" 
				headerNames="<%= headers %>">
					
				<liferay-ui:search-container-results>
					<%
						DossierSearchTerms searchTerms =
							(DossierSearchTerms) searchContainer.getSearchTerms();

						total =
							DossierLocalServiceUtil.countDossierByKeywordDomainAndStatus(
								scopeGroupId, searchTerms.getKeywords(),
								domainCode, govAgencyCodes, dossierStatus);

						results =
							DossierLocalServiceUtil.searchDossierByKeywordDomainAndStatus(
								scopeGroupId, searchTerms.getKeywords(),
								domainCode, govAgencyCodes, dossierStatus,
								searchContainer.getStart(),
								searchContainer.getEnd(),
								searchContainer.getOrderByComparator());

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
								
						String receiveDatetime = StringPool.BLANK;
						// receive datetime column
						if (Validator.isNotNull(dossier.getReceiveDatetime())) {
							receiveDatetime =  dateFormatDate.format(dossier.getReceiveDatetime());				
						}
						
						ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
						
						String finishDate = StringPool.BLANK;
						
						// finish datetime column
						if (Validator.isNotNull(dossier.getFinishDatetime())) {
							finishDate = dateFormatDate.format(dossier.getFinishDatetime());				
						}
						
						String statusText = "";
						if (Validator.isNotNull(dossier.getFinishDatetime()) && Validator.isNotNull(dossier.getEstimateDatetime())) {
							if (dossier.getFinishDatetime().after(dossier.getEstimateDatetime())) {
								statusText = LanguageUtil.get(pageContext, "status-late");
							}
							else if (dossier.getFinishDatetime().before(dossier.getEstimateDatetime())) {
								statusText = LanguageUtil.get(pageContext, "status-soon");
							}
							else if (dossier.getFinishDatetime().equals(dossier.getEstimateDatetime())) {
								statusText = LanguageUtil.get(pageContext, "status-ontime");
							}
						}
						else {
							Date now = new Date();
							
							if (Validator.isNotNull(dossier.getEstimateDatetime())) {
								if (dossier.getEstimateDatetime().before(now)) {
									statusText = LanguageUtil.get(pageContext, "status-toosoon");
								}
								else if (dossier.getEstimateDatetime().after(now)) {
									statusText = LanguageUtil.get(pageContext, "status-toolate");
								}
							}
						}			
					%>	
					<!-- uxtheme -->
					<liferay-util:buffer var="info">
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
					</liferay-util:buffer>
					
					<liferay-util:buffer var="detail">
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
					</liferay-util:buffer>
					
					<%
						row.setClassName("opencps-searchcontainer-row");
						row.addText(String.valueOf(row.getPos() + 1), viewURL);
						row.addText(info);
						row.addText(detail);
					%>
				
			</liferay-ui:search-container-row>	
		
			<liferay-ui:search-iterator type="opencs_page_iterator"/>
		
		</liferay-ui:search-container>
		</div>
	</c:if>
	
	<c:otherwise>
		<div class="portlet-msg-warring">
			<liferay-ui:message key="your-account-not-permission-access-this-resource-or-your-session-has-finished-please-login-to-continue"/>
		</div>
	</c:otherwise>
</c:choose>


<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backoffice.backofficedossierlist.jsp");
%>
