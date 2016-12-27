<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
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
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%> 
<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.util.DLFileEntryUtil"%>

<%@ include file="../../../init.jsp"%>
<%
	long dossierTemplateId = ParamUtil.getLong(request, "dossierTemplateId");

	long dossierId = ParamUtil.getLong(request, "dossierId");
	
	String statusName = ParamUtil.getString(request, "statusName");
	
	DossierTemplate dossierTemplate = DossierTemplateLocalServiceUtil.getDossierTemplate(dossierTemplateId);
	
	List<DossierPart> dossierPartsLevel1 = new ArrayList<DossierPart>();
	
	if(Validator.isNotNull(dossierTemplate)){
		if(dossierTemplate != null){
			try{
				List<DossierPart> lstTmp1 = DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(dossierTemplate.getDossierTemplateId(), 0, PortletConstants.DOSSIER_PART_TYPE_RESULT);
				if(lstTmp1 != null){
					dossierPartsLevel1.addAll(lstTmp1);
				}
			}catch(Exception e){}
			
			try{
				List<DossierPart> lstTmp2 = DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(dossierTemplate.getDossierTemplateId(), 0, PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT);
				if(lstTmp2 != null){
					dossierPartsLevel1.addAll(lstTmp2);
				}
			}catch(Exception e){}
		}
	}
%>

<c:if test="<%= dossierPartsLevel1 != null && !dossierPartsLevel1.isEmpty() %>">
		<%
			List<DossierFile> dossierFiles = new ArrayList<DossierFile>();
			int count = 0;
			try {
				if(Validator.isNotNull(dossierId)) {
					dossierFiles = DossierFileLocalServiceUtil
									.getDossierFileResult(dossierId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
										DossierSearchUtil.getDossierFileOrderByComparator(dossierFileListOrderByField, dossierFileListOrderByType));
				}
			} catch (Exception e) {
				
			}
			if(Validator.isNotNull(dossierFiles) && !dossierFiles.isEmpty()){
				DossierFile dossierFile = dossierFiles.get(0);
					String fileURL = StringPool.BLANK;
					
					try{
						FileEntry fileEntry = DLFileEntryUtil.getFileEntry(dossierFile.getFileEntryId());
						if(fileEntry != null){
							fileURL = DLUtil.getPreviewURL(fileEntry, fileEntry.getFileVersion(), 
									themeDisplay, StringPool.BLANK);
						}
					}catch(Exception e){
						
					}
				%>
					<a class="blue" href="<%=fileURL%>" target="_blank">
						<%=statusName  %>
					</a>
				<%
			}else{
				%>
					<span><%=statusName  %></span>
				<%	
			}
		%>
</c:if>