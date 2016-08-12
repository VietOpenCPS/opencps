<%@page import="com.liferay.portal.NoSuchOrganizationException"%>
<%@page import="com.liferay.portal.kernel.search.SearchException"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.SearchEngineUtil"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchUtil"%>
<%@page import="com.liferay.portal.kernel.search.BooleanQuery"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.service.OrganizationLocalServiceUtil"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="javax.portlet.PortletURL"%>
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
<div class="home-search">
	<liferay-util:include page="/html/portlets/dossiermgt/monitoring/toolbar.jsp" servletContext="<%=application %>" />	
</div>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringdossierfilelist.jsp");	
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
%>

<liferay-ui:search-container searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
			SearchContext searchContext = SearchContextFactory.getInstance(request);
			searchContext.setKeywords(searchTerms.getKeywords());
			searchContext.setAttribute("paginationType", "more");
			
				List<DossierFile> dossierFiles = new ArrayList<DossierFile>();
				Integer totalCount = 0;
				
				Indexer indexer = IndexerRegistryUtil.getIndexer(DossierFile.class);
				_log.info("----KEYWORD----" + searchTerms.getKeywords());
				searchContext.setLike(true);

				try {
				    //Hits hits = indexer.search(searchContext);
				    BooleanQuery query = DossierFileSearchUtil.buildSearchQuery(searchTerms.getKeywords(), searchContext);
				    Hits hits = SearchEngineUtil.search(searchContext, query);
					System.out.println("----HITS----" + hits.getLength());
					totalCount = hits.getLength();
					searchContext.setStart(searchContainer.getStart());
					searchContext.setEnd(searchContainer.getEnd());
					hits = SearchEngineUtil.search(searchContext, query);
				    for (int i = 0; i < hits.getDocs().length; i++) {
				    	Document doc = hits.doc(i);

				        long dossierFileId = GetterUtil
				                .getLong(doc.get(DossierFileDisplayTerms.DOSSIER_FILE_ID));

				        DossierFile dossierFile = null;

				        try {
				        	dossierFile = DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
				        } catch (PortalException pe) {
				        	_log.error(pe.getLocalizedMessage());
				        } catch (SystemException se) {
				            _log.error(se.getLocalizedMessage());
				        }
						if (!dossierFiles.contains(dossierFile))
				        	dossierFiles.add(dossierFile);
					}		    
				}
				catch (SearchException se) {
				}
			
				//totalCount = dossierFiles.size();
				total = totalCount;
				results = dossierFiles;
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);				
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.DossierFile" 
			modelVar="dossierFile" 
			keyProperty="dossierFileId"
		>
			<%				
			    // no column
				row.addText(String.valueOf(row.getPos() + 1));		
			
				// file no column
				row.addText(dossierFile.getDossierFileNo());
				
				// file date column
				row.addText(Validator.isNotNull(dossierFile.getDossierFileDate()) ? dateFormatDate.format(dossierFile.getDossierFileDate()) : "");
				
				// display name column
				row.addText(dossierFile.getDisplayName());
				
				// gov agency name column
				row.addText("");
				
				// owner name column
				if (Validator.isNotNull(dossierFile.getOwnerUserId())) {
					row.addText(UserLocalServiceUtil.getUser(dossierFile.getOwnerUserId()).getFullName());
				}
				else if (Validator.isNotNull(dossierFile.getOwnerOrganizationId())) {
					try {
						row.addText(OrganizationLocalServiceUtil.getOrganization(dossierFile.getOwnerOrganizationId()).getName());
					}
					catch (NoSuchOrganizationException e) {
						row.addText("");
					}
				}
				else {
					row.addText("");
				}
				
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.dossierfilelist.jsp");
%>
