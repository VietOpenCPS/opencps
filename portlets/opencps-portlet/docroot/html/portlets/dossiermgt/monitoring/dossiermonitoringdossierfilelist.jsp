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
<liferay-util:include page="/html/portlets/dossiermgt/monitoring/toptabs.jsp" servletContext="<%=application %>" />
<liferay-util:include page="/html/portlets/dossiermgt/monitoring/toolbar.jsp" servletContext="<%=application %>" />

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringdossierfilelist.jsp");	
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
%>

<liferay-ui:search-container searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
			
			String[] keywordArrs = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				keywordArrs = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			if (keywordArrs != null) {
				List<DossierFile> dossierFiles = null;
				Integer totalCount = 0;
				
				try {
					dossierFiles = DossierFileLocalServiceUtil.searchDossierFile(scopeGroupId, searchTerms.getKeywords(), -1, -1, true, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					totalCount = DossierFileLocalServiceUtil.countDossierFile(scopeGroupId, searchTerms.getKeywords(), -1, -1, true);
				} catch(Exception e){
					_log.error(e);
				}
			
				total = totalCount;
				results = dossierFiles;
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);				
			}
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
					row.addText(OrganizationLocalServiceUtil.getOrganization(dossierFile.getOwnerOrganizationId()).getName());
				}
				else {
					row.addText("");
				}
				
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.dossierfilelist.jsp");
%>