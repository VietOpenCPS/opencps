
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

<%@page import="javax.portlet.WindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileSearch"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@ include file="/init.jsp"%>

<%

	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);
	
	String modalDialogId = ParamUtil.getString(request, "modalDialogId");
	
	String redirectURL = ParamUtil.getString(request, "redirectURL");

	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("#");
	headerNames.add("create-date");
	headerNames.add("dossier-file-no");
	headerNames.add("dossier-file-date");
	headerNames.add("display-name");
	headerNames.add("content");
	
	String headers = StringUtil.merge(headerNames);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier_file_version.jsp");
	iteratorURL.setParameter("tab1", "select-file");
	
	iteratorURL.setParameter("dossierId", String.valueOf(dossierId));
	iteratorURL.setParameter("dossierPartId", String.valueOf(dossierPartId));
	iteratorURL.setParameter("dossierFileId", String.valueOf(dossierFileId));
	iteratorURL.setParameter("redirectURL", redirectURL);
	iteratorURL.setParameter("modalDialogId", modalDialogId);
	
	DossierPart dossierPart = null;
	
	try{
		if(dossierPartId > 0){
			dossierPart = DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		}
	}catch(Exception e){}
	
	List<DossierFile> dossierFiles = new ArrayList<DossierFile>();
	int totalCount = 0;
	
	if(dossierPart != null && dossierId > 0){
	
		try{			
			if(dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_OTHER &&
				dossierPart.getPartType() != PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT){
				dossierFiles = DossierFileLocalServiceUtil.getDossierFileByDID_DP(dossierId, dossierPartId);
				totalCount = DossierFileLocalServiceUtil.countDossierFileByDID_DP(dossierId, dossierPartId);
			}else{
				
				if(dossierFileId > 0){
					totalCount = 1;
					DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
					dossierFiles.add(dossierFile);
				}
				
			}
			
		}catch(Exception e){}
	}
%>

<c:choose>
	<c:when test="<%=totalCount > 0 %>">
		<liferay-ui:search-container 
			searchContainer="<%= new DossierFileSearch(renderRequest, 100, iteratorURL) %>" 
			headerNames="<%= headers %>"
		>
		
			<liferay-ui:search-container-results>
				<%
					DossierFileSearchTerms searchTerms = (DossierFileSearchTerms)searchContainer.getSearchTerms();
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
					<liferay-ui:search-container-column-text 
						title="#"
						name="#"
						value="<%=String.valueOf(row.getPos() + searchContainer.getStart() + 1) %>"
					/>
					<liferay-ui:search-container-column-text 
						title="create-date"
						name="create-date"
						value="<%=DateTimeUtil.convertDateToString(dossierFile.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>"
					/>
					<liferay-ui:search-container-column-text 
						title="dossier-file-no"
						name="dossier-file-no"
						value="<%=dossierFile.getDossierFileNo() %>"
					/>
					<liferay-ui:search-container-column-text 
						title="dossier-file-date"
						name="dossier-file-date"
						value="<%=Validator.isNotNull(dossierFile.getDossierFileDate()) ? DateTimeUtil.convertDateToString(dossierFile.getDossierFileDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) : StringPool.DASH %>"
					/>
					<liferay-ui:search-container-column-text 
						title="display-name"
						name="display-name"
						value="<%=Validator.isNotNull(dossierFile.getDisplayName()) ? dossierFile.getDisplayName() : StringPool.DASH %>"
					/>
					
					<liferay-ui:search-container-column-text 
						title="version"
						name="version"
						value="<%=String.valueOf(dossierFile.getVersion()) %>"
					/>
					
					<liferay-ui:search-container-column-button href='<%="viewAttachment(" + dossierFile.getDossierFileId() + ")" %>' name="view" valign="center" cssClass="view-attachment"/>
		
				</liferay-ui:search-container-row> 
			
			<liferay-ui:search-iterator type="opencs_page_iterator"/>
		</liferay-ui:search-container>
	</c:when>
	<c:otherwise>
		<div class="alert alert-info"><liferay-ui:message key="no-dossierfile-were-found"/></div>
	</c:otherwise>
</c:choose>
<aui:script>
	
	Liferay.provide(window, 'viewAttachment', function(dossierFileId) {
		
		var A = AUI();
	
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_BACKOFFICE_MANAGEMENT_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
		portletURL.setParameter("javax.portlet.action", "previewAttachmentFile");
		portletURL.setParameter("dossierFileId", dossierFileId);
		portletURL.setPortletMode("view");
		portletURL.setWindowState('<%=WindowState.NORMAL%>');
		
		viewDossierAttachment(this, portletURL.toString());
	},['aui-base','liferay-portlet-url','aui-io']);

</aui:script>
