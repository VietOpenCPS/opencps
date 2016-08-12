<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.kernel.search.SearchEngineUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchUtil"%>
<%@page import="com.liferay.portal.kernel.search.BooleanQuery"%>
<%@page import="com.liferay.portal.kernel.search.SearchException"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="java.util.Date"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
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
<liferay-util:include page="/html/portlets/dossiermgt/monitoring/dossierlisttoolbar.jsp" servletContext="<%=application %>" />

<%
	String keywordSearch = ParamUtil.getString(request, "keywords", StringPool.BLANK);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringdossierlist.jsp");
	SearchContext searchContext = SearchContextFactory.getInstance(request);
	searchContext.setKeywords(keywordSearch);
	searchContext.setAttribute("paginationType", "more");
	
%>

<liferay-ui:search-container searchContainer="<%= new DossierSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
			
				List<Dossier> dossiers = new ArrayList<Dossier>();
				Integer totalCount = 0;
				
				Indexer indexer = IndexerRegistryUtil.getIndexer(Dossier.class);
				_log.info("----KEYWORD----" + searchTerms.getKeywords());
				searchContext.setLike(true);

				try {
				    //Hits hits = indexer.search(searchContext);
				    BooleanQuery query = DossierSearchUtil.buildSearchQuery(searchTerms.getKeywords(), searchContext);
				    Hits hits = SearchEngineUtil.search(searchContext, query);
					System.out.println("----HITS----" + hits.getLength());
					totalCount = hits.getLength();
					searchContext.setStart(searchContainer.getStart());
					searchContext.setEnd(searchContainer.getEnd());
					hits = SearchEngineUtil.search(searchContext, query);
				    for (int i = 0; i < hits.getDocs().length; i++) {
				    	Document doc = hits.doc(i);

				        long dossierId = GetterUtil
				                .getLong(doc.get(DossierDisplayTerms.DOSSIER_ID));

				        Dossier dossier = null;

				        try {
				        	dossier = DossierLocalServiceUtil.getDossier(dossierId);
				        } catch (PortalException pe) {
				        	_log.error(pe.getLocalizedMessage());
				        } catch (SystemException se) {
				            _log.error(se.getLocalizedMessage());
				        }
						if (!dossiers.contains(dossier))
				        	dossiers.add(dossier);
					}		    
				}
				catch (SearchException se) {
				}
				
				//totalCount = dossiers.size();
				total = totalCount;
				results = dossiers;
				
				pageContext.setAttribute("results", dossiers);
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
			viewURL.setParameter("mvcPath", templatePath + "dossiermonitoringresult.jsp");
			viewURL.setParameter(DossierDisplayTerms.DOSSIER_ID, String.valueOf(dossier.getDossierId()));
			viewURL.setParameter("backURL", currentURL);
			if (!StringPool.BLANK.equals(keywordSearch)) {
				viewURL.setParameter("keywords", keywordSearch);
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
		<liferay-ui:search-container-column-text name="row-no" title="row-no" value="<%= String.valueOf(row.getPos() + 1) %>"/>
		<liferay-ui:search-container-column-text orderable="true" name="subject-name" title="subject-name" value="<%= dossier.getSubjectName() %>" href="<%= viewURL.toString() %>" />
		<liferay-ui:search-container-column-text name="govagency-name" title="govagency-name" value="<%= dossier.getGovAgencyName() %>"/>
		<liferay-ui:search-container-column-text orderable="true" name="receive-datetime" title="receive-datetime" value="<%= Validator.isNotNull(dossier.getReceiveDatetime()) ? dateFormatDate.format(dossier.getReceiveDatetime()) : \"\" %>"/>
		<liferay-ui:search-container-column-text orderable="true" name="finish-datetime" title="finish-datetime" value="<%= Validator.isNotNull(dossier.getFinishDatetime()) ? dateFormatDate.format(dossier.getFinishDatetime()) : \"\" %>"/>
		<liferay-ui:search-container-column-text name="status" title="status" value="<%= statusText %>"/>
		
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.dossierlist.jsp");
%>
