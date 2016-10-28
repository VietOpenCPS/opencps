
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileVersion"%>
<%@page import="com.liferay.portlet.documentlibrary.NoSuchFileEntryException"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="com.liferay.portal.service.ServiceContextFactory"%>
<%@page import="com.liferay.portal.service.ServiceContext"%>
<%@page import="org.opencps.util.AccountUtil"%>
<%@page import="org.opencps.dossiermgt.bean.AccountBean"%>
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
<%@page import="org.opencps.processmgt.search.ProcessDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>

<%@ include file="../init.jsp"%>
<liferay-util:include page="/html/portlets/processmgt/backofficedossier/toptabs.jsp" servletContext="<%=application %>" />
<liferay-util:include page="/html/portlets/processmgt/backofficedossier/toolbar.jsp" servletContext="<%=application %>" />

<%
	
	long dossierTemplateId = ParamUtil.getLong(request, ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID);
	
	boolean onlyViewFileResult = ParamUtil.getBoolean(request, "onlyViewFileResult");
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "backofficedossierfilelist.jsp");
	iteratorURL.setParameter("tab1", ProcessMgtUtil.TOP_TABS_DOSSIERFILELIST);
	iteratorURL.setParameter(ProcessDisplayTerms.PROCESS_DOSSIERTEMPLATE_ID, (dossierTemplateId > 0)?String.valueOf(dossierTemplateId):StringPool.BLANK);
	iteratorURL.setParameter("onlyViewFileResult", String.valueOf(onlyViewFileResult));

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
<div class="payment-ld">
	<div class="content">
	<div class="opcs-serviceinfo-list-label">
		<div class="title_box">
	           <p class="file_manage_title"><liferay-ui:message key="danh-sach-giay-to" /></p>
	           <p class="count"></p>
	    </div>
	</div>
		<liferay-ui:search-container searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
			headerNames="<%=headers %>">
		
			<liferay-ui:search-container-results>
				<%
					DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
								
						List<DossierFile> dossierFiles = null;
						
						int totalCount = 0;
						
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
						// dossier file type column
						String dossierFileTypeText = DossierMgtUtil.getNameOfPartType(dossierFile.getDossierFileType(), themeDisplay.getLocale());
		
						// owner name column
						String ownerName = StringPool.BLANK;
						
						if(dossierFile.getOwnerOrganizationId()> 0){
							
							Business owner = BusinessLocalServiceUtil.getBymappingOrganizationId(dossierFile.getOwnerOrganizationId());
							
							if(Validator.isNotNull(owner)){
								ownerName = owner.getName();
							}
								
						}else{
							
							Citizen owner = CitizenLocalServiceUtil.getByMappingUserId(dossierFile.getOwnerUserId());
		
							if(Validator.isNotNull(owner)){
								ownerName = owner.getFullName();
							}
								
						} 
						
						Dossier dossier = DossierLocalServiceUtil.fetchDossier(dossierFile.getDossierId());
						//url file download
						FileEntry fileEntry = null;
							try {
								fileEntry = DLAppLocalServiceUtil.getFileEntry(dossierFile.getFileEntryId());
							}
						catch (NoSuchFileEntryException e) {
									
						}
						String urlDownload = null;
							if (fileEntry != null) {
								FileVersion fileVersion = fileEntry.getFileVersion();
										 
								String queryString = "";							 
								boolean appendFileEntryVersion = true;
										 
								boolean useAbsoluteURL = true;
												 
								urlDownload = DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, queryString, appendFileEntryVersion, useAbsoluteURL);							
							}
						
						// no column
						row.addText(String.valueOf(row.getPos() + 1));
						
						row.addText(dossierFileTypeText);
						
						// dossier file no column
						row.addText(dossierFile.getDossierFileNo());
						
						row.addText(Validator.isNotNull(dossierFile.getDossierFileDate())?DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_FORMAT):StringPool.BLANK);
						
						row.addText("<a target=\"_blank\" href=\""+urlDownload+"\" >" + dossierFile.getDisplayName() + "</a>");
						
						row.addText(dossierFile.getDossierId() + "/" +(Validator.isNotNull(dossier) ? "<a target=\"_blank\" href=\""+urlDownload+"\" >" + dossier.getReceptionNo() + "</a>" : StringPool.BLANK));
						
						row.addText(ownerName);
					%>	
				</liferay-ui:search-container-row> 
			
			<liferay-ui:search-iterator type="opencs_page_iterator"/>
		</liferay-ui:search-container>
	</div>
</div>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backofficedossier.backofficedossierfilelist.jsp");
%>