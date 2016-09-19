
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

<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="org.opencps.accountmgt.NoSuchCitizenException"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
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

<%@ include file="../init.jsp"%>
<liferay-util:include page="/html/portlets/processmgt/backofficedossier/toptabs.jsp" servletContext="<%=application %>" />
<liferay-util:include page="/html/portlets/processmgt/backofficedossier/toolbar.jsp" servletContext="<%=application %>" />

<%
	String backURL = ParamUtil.getString(request, "backURL");
	User mappingUser = (User)request.getAttribute(WebKeys.USER_MAPPING_ENTRY);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficedossierfilelist.jsp");
	iteratorURL.setParameter("tab1", ProcessMgtUtil.TOP_TABS_DOSSIERFILELIST);
	
	long dossierTemplateId = ParamUtil.getLong(request, ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	
	boolean onlyViewFileResult = ParamUtil.getBoolean(request, "onlyViewFileResult");
	request.setAttribute(ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID, dossierTemplateId);
	request.setAttribute("onlyViewFileResult", onlyViewFileResult);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-index");
	headerNames.add("dossierfile-filetype");
	headerNames.add("dossierfile-fileno");
	headerNames.add("dossierfile-filedate");
	headerNames.add("dossierfile-displayname");
	headerNames.add("dossierfile-receptionno");
	headerNames.add("subjectname");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
%>

<liferay-ui:search-container searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>" headerNames="<%= headers %>">

	<liferay-ui:search-container-results>
		<%
			DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
						
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
				
				// dossier file type column
				String dossierFileTypeText = StringPool.BLANK;
				
				switch (dossierFile.getDossierFileType()) {
				case DossierMgtUtil.DOSSIERFILETYPE_INPUT:
					dossierFileTypeText = LanguageUtil.get(pageContext, "dossier-filetype-input");
					break;
				case DossierMgtUtil.DOSSIERFILETYPE_OUTPUT:
					dossierFileTypeText = LanguageUtil.get(pageContext, "dossier-filetype-output");
					break;
				default:
					dossierFileTypeText = StringPool.BLANK;
					break;
				}
				row.addText(dossierFileTypeText);
				
				// dossier file no column
				row.addText(dossierFile.getDossierFileNo());
								
				// dossier file date column
				if (Validator.isNotNull(dossierFile.getDossierFileDate())) {
					row.addText(dateFormatDate.format(dossierFile.getDossierFileDate()));					
				}
				else {
					row.addText(StringPool.BLANK);
				}
				
				// dossier display name column
				row.addText(dossierFile.getDisplayName());
				
				// reception no column
				Dossier dossier = null;
				try {
					dossier = DossierLocalServiceUtil.getDossier(dossierFile.getDossierId());
				}
				catch (NoSuchDossierException e) {
					
				}
				row.addText(dossier != null ? dossier.getReceptionNo() : StringPool.BLANK);

				// owner name column
				Citizen citizenOwner = null;
				Business businessOwner = null;
				try {
					if (dossier != null) {
						if (dossier.getOwnerOrganizationId() == 0) {
							citizenOwner = CitizenLocalServiceUtil.getByMappingUserId(dossier.getUserId());							
						}
						else {
							businessOwner = BusinessLocalServiceUtil.getBymappingOrganizationId(dossier.getOwnerOrganizationId());
						}
					}
					else
						citizenOwner = CitizenLocalServiceUtil.getByMappingUserId(dossierFile.getOwnerUserId());
				}
				catch (SystemException e) {
					
				}
				row.addText(citizenOwner != null ? citizenOwner.getFullName() : (businessOwner != null ? businessOwner.getName() : StringPool.BLANK));
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backofficedossier.backofficedossierfilelist.jsp");
%>