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
<%@page import="org.opencps.dossiermgt.service.FileGroupLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.FileGroup"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>

<%@ include file="/init.jsp"%>

<%
	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);

	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long fileGroupId = ParamUtil.getLong(request, DossierDisplayTerms.FILE_GROUP_ID);

	int index = ParamUtil.getInteger(request, DossierFileDisplayTerms.INDEX);
	
	boolean isEditDossier = ParamUtil.getBoolean(request, "isEditDossier");
	
	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	String cssRequiredPage = StringPool.BLANK;
	
	String urlDownload = StringPool.BLANK;
%>

<div class="opencps dossiermgt dossier-part-tree">
	<c:choose>
		<c:when test="<%=fileGroupId > 0 %>">
			<%
				
				FileGroup fileGroup = FileGroupLocalServiceUtil.getFileGroup(fileGroupId);
			
				List<DossierPart> dossierParts = DossierMgtUtil.getTreeDossierPart(dossierPartId);
			
				for(DossierPart dossierPart : dossierParts){
					
					int level = 0;
					
					String treeIndex = dossierPart.getTreeIndex();
					
					if(Validator.isNotNull(treeIndex)){
						level = StringUtil.count(treeIndex, StringPool.PERIOD) + 1;
					}
					
					DossierFile dossierFile = null;
					
					if(dossierId > 0){
						try{
							
							dossierFile = DossierFileLocalServiceUtil.
								getDossierFileInUseByGroupFileId(dossierId, dossierPart.getDossierpartId(), fileGroup.getFileGroupId());
							
						}catch(Exception e){}
					}
					
					cssRequiredPage = dossierPart.getRequired() ? "cssRequired" : StringPool.BLANK;
					
					urlDownload = DossierMgtUtil.getURLDownloadTemplateFile(themeDisplay, dossierPart.getTemplateFileNo());
					
					%>
						<div class='<%="opencps dossiermgt dossier-part-row r-" + index%>'>
							<span class='<%="level-" + level + " opencps dossiermgt dossier-part"%>'>
								<span class="row-icon">
								
									<c:choose>
										<c:when test="<%=level > 1 %>">
											<i 
												id='<%="rowcheck" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' 
												class='<%=dossierFile != null &&  dossierFile.getFileEntryId() > 0 && dossierFile.getGroupFileId() == fileGroupId ? "fa fa-check-square-o" : "fa fa-square-o" %>'
												aria-hidden="true">
											</i>
										</c:when>
										
										<c:otherwise>
											<i class="fa fa-folder-o" aria-hidden="true"></i>
										</c:otherwise>
									</c:choose>
								</span>
								
								<span class="opencps dossiermgt dossier-part-name <%=cssRequiredPage %>">
									<c:choose>
										<c:when test="<%=level > 1 %>">
											<%=dossierPart.getPartName() %>
											<c:if test="<%=Validator.isNotNull(urlDownload) %>">
												<a target="_blank" class="download-dossier-file" href="<%=urlDownload%>"><liferay-ui:message key="download-file-entry" /></a>
											</c:if>
										</c:when>
										<c:otherwise>
											<%=fileGroup.getDisplayName() %>
											<c:if test="<%=Validator.isNotNull(urlDownload) %>">
												<a target="_blank" class="download-dossier-file" href="<%=urlDownload%>"><liferay-ui:message key="download-file-entry" /></a>
											</c:if>
										</c:otherwise>
									</c:choose>
								</span>
							</span>
						
							<span class="opencps dossiermgt dossier-part-control">
								<liferay-util:include 
									page="/html/common/portlet/dossier_actions.jsp" 
									servletContext="<%=pageContext.getServletContext() %>"
								>
									<portlet:param 
										name="<%=DossierDisplayTerms.DOSSIER_ID %>" 
										value="<%=String.valueOf(dossierId) %>"
									/>
									
									<portlet:param 
										name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
										value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
									/>
									
									<c:if test="<%=level > 1 %>">
										<portlet:param 
											name="childDossierPartId" 
											value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
										/>
										
										<portlet:param 
											name="isChildDossierPart" 
											value="<%=String.valueOf(true) %>"
										/>
									</c:if>
									
									
									<portlet:param 
										name="<%=DossierFileDisplayTerms.FILE_ENTRY_ID %>" 
										value="<%=String.valueOf(dossierFile != null ? dossierFile.getFileEntryId() : 0) %>"
									/>
									<portlet:param 
										name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" 
										value="<%=String.valueOf(dossierFile != null ? dossierFile.getDossierFileId() : 0) %>"
									/>
									<portlet:param 
										name="<%=DossierFileDisplayTerms.INDEX %>" 
										value="<%=String.valueOf(index) %>"
									/>
									<portlet:param 
										name="<%=DossierFileDisplayTerms.GROUP_NAME %>" 
										value="<%=dossierParts.get(0).getPartName()%>"
									/>
									<portlet:param 
										name="<%=DossierFileDisplayTerms.PART_TYPE %>" 
										value="<%=String.valueOf(dossierPart.getPartType()) %>"
									/>
								</liferay-util:include>
							</span>
						</div>
						
					<%
					index++;
				}
			%>
		</c:when>
		<c:otherwise>
			<!-- Nothing to display  -->
		</c:otherwise>
	</c:choose>
</div>
