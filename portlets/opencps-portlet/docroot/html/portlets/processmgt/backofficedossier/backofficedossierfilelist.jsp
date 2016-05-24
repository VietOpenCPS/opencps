<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="java.text.Format"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>
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
	iteratorURL.setParameter("mvcPath", templatePath + "backofficedossierfilelist.jsp");
	
	long dossierTemplateId = ParamUtil.getLong(request, ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	
	boolean onlyViewFileResult = ParamUtil.getBoolean(request, "onlyViewFileResult");
	request.setAttribute(ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID, dossierTemplateId);
	request.setAttribute("onlyViewFileResult", onlyViewFileResult);
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
					dossierFiles = DossierFileLocalServiceUtil.searchDossierFile(scopeGroupId, searchTerms.getKeywords(), dossierTemplateId, -1, onlyViewFileResult, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					totalCount = DossierFileLocalServiceUtil.countDossierFile(scopeGroupId, searchTerms.getKeywords(), dossierTemplateId, -1, onlyViewFileResult);
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
				
				// create date column
				row.addText(dateFormatDate.format(dossierFile.getCreateDate()));
				
				// dossier file no column
				row.addText(dossierFile.getTemplateFileNo());
				
				// dossier file date column
				row.addText(dateFormatDate.format(dossierFile.getDossierFileDate()));
				
				// dossier display name column
				row.addText(dossierFile.getDisplayName());
				
				// reception no column
				Dossier dossier = DossierLocalServiceUtil.getDossier(dossierFile.getDossierId());
				row.addText(dossier.getReceptionNo());
				
				// dossier file type
				if (dossierFile.getDossierFileType() == DossierMgtUtil.DOSSIERFILETYPE_INPUT) {
					row.addText(LanguageUtil.get(portletConfig, locale, "dossier-filetype-input"));
				}
				else if (dossierFile.getDossierFileType() == DossierMgtUtil.DOSSIERFILETYPE_OUTPUT) {
					row.addText(LanguageUtil.get(portletConfig, locale, "dossier-filetype-output"));
				}
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backofficedossier.backofficedossierfilelist.jsp");
%>