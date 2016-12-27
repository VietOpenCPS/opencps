<%@page import="org.opencps.util.PortletConstants"%>
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
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.DLFileEntryUtil"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
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
	//headerNames.add("dossierfile-displayname");
	//headerNames.add("dossierfile-receptionno");
	//headerNames.add("subjectname");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
%>
<div class="opencps-searchcontainer-wrapper">
	<div class="opcs-serviceinfo-list-label">
		<div class="title_box">
	           <p class="file_manage_title"><liferay-ui:message key="danh-sach-giay-to" /></p>
	           <p class="count"></p>
	    </div>
	</div>
	
	<liferay-ui:search-container 
		searchContainer="<%= new DossierFileSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
		headerNames="<%=headers %>"
	>
	
		<liferay-ui:search-container-results>
			<%
				DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
									
				List<DossierFile> dossierFiles = null;
				
				int totalCount = 0;
				
				try {
					dossierFiles = DossierFileLocalServiceUtil
						.searchDossierFile(
								scopeGroupId,
								searchTerms.getKeywords(),
								dossierTemplateId,
								-1,
								PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
								0, onlyViewFileResult,
								searchContainer.getStart(),
								searchContainer.getEnd(),
								searchContainer.getOrderByComparator());
					
					totalCount = DossierFileLocalServiceUtil
						.countDossierFile(
								scopeGroupId,
								searchTerms.getKeywords(),
								dossierTemplateId,
								-1,
								PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS,
								0, onlyViewFileResult);
				} catch (Exception e) {
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
				String urlDownload = StringPool.BLANK;	
				
				long fileEntryId = dossierFile.getFileEntryId();
				
				if(fileEntryId > 0) {
					 FileEntry fileEntry =
							DLFileEntryUtil.getFileEntry(fileEntryId);
					 if(Validator.isNotNull(fileEntry) ) {
						 try {
							 urlDownload = DLUtil.getPreviewURL(
										fileEntry, fileEntry.getFileVersion(),
										themeDisplay, StringPool.BLANK);
						 } catch(Exception e) {
							 
						 }
					 }
				}

				
			%>	
			
			<liferay-util:buffer var="dossierFileInfo">
				<div class="row-fluid">
					<div class="span4 bold-label">
						 <liferay-ui:message key="dossier-file-no"/>
					</div>
					<div class="span8">
						<%=Validator.isNotNull(dossierFile.getDossierFileNo()) ? dossierFile.getDossierFileNo() : StringPool.DASH %>
					</div>
				</div>
				
				<div class="row-fluid">
					
					<div class="span4 bold-label">
						 <liferay-ui:message key="dossier-file-date"/>
					</div>
					<div class="span8">
						<%=
							Validator.isNotNull(dossierFile.getDossierFileDate())? 
								DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_FORMAT) :
								DateTimeUtil._EMPTY_DATE_TIME
						%>
					</div>
				</div>
				
				
			</liferay-util:buffer>
			
			<liferay-util:buffer var="dossierFileName">
				<div class="row-fluid">
					<div class="span4 bold-label">
						 <liferay-ui:message key="dossier-file-name"/>
					</div>
					<div class="span8">
						<a href="<%=urlDownload%>" target="_blank">
							<%=dossierFile.getDisplayName() %>
						</a>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span4 bold-label">
						 <liferay-ui:message key="dossier-file-type"/>
					</div>
					<div class="span8">
						<%=dossierFileTypeText %>
					</div>
				</div>
				
			</liferay-util:buffer>
			
			<liferay-util:buffer var="dossierInfo">
				
				<div class="row-fluid">
					<div class="span4 bold-label">
						 <liferay-ui:message key="reception-no"/>
					</div>
					<div class="span8">
						<a href="<%=urlDownload%>" target="_blank">
							<%=Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.DASH %>
						</a>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span4 bold-label">
						 <liferay-ui:message key="subject-name"/>
					</div>
					<div class="span8">
						<%=ownerName %>
					</div>
				</div>
				
			</liferay-util:buffer>
			
			<%
				row.addText(String.valueOf(row.getPos() + 1));
				
				row.addText(dossierFileInfo);
				
				// dossier file no column
				row.addText(dossierFileName);
				
				row.addText(dossierInfo);
				
				//row.addText(String.valueOf(row.getPos() + 1));
				
				//row.addText(dossierFileTypeText);
				
				// dossier file no column
				//row.addText(dossierFile.getDossierFileNo());
				
				//row.addText(Validator.isNotNull(dossierFile.getDossierFileDate())?DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_FORMAT):StringPool.BLANK);
					
					//row.addText("<a target=\"_blank\" href=\""+urlDownload+"\" >" + dossierFile.getDisplayName() + "</a>");
					//ow.addText(dossierFile.getDisplayName(), urlDownload);
					//row.addText(dossierFile.getDossierId() + "/" +(Validator.isNotNull(dossier) ? "<a target=\"_blank\" href=\""+urlDownload+"\" >" + dossier.getReceptionNo() + "</a>" : StringPool.BLANK));
					
					//row.addText(ownerName);
				%>
			</liferay-ui:search-container-row> 
			
			<liferay-ui:search-iterator type="opencs_page_iterator"/>
		</liferay-ui:search-container>
</div>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.backofficedossier.backofficedossierfilelist.jsp");
%>