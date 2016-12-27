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
<%@page import="org.opencps.util.SignatureUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>

<%@ include file="/init.jsp"%>

<%
	boolean isDynamicForm = ParamUtil.getBoolean(request, "isDynamicForm");

	int isOnlineData = ParamUtil.getInteger(request, "isOnlineData", 0);

	boolean isEditDossier = ParamUtil.getBoolean(request, "isEditDossier");

	boolean isReadOnly = ParamUtil.getBoolean(request, "isReadOnly");
	
	boolean isChildDossierPart = GetterUtil.getBoolean(ParamUtil.getBoolean(request, "isChildDossierPart"), false);

	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long childDossierPartId = ParamUtil.getLong(request, "childDossierPartId");
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);
	
	long fileEntryId = ParamUtil.getLong(request, DossierFileDisplayTerms.FILE_ENTRY_ID);
	
	long processOrderId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
	
	long fileGroupId = ParamUtil.getLong(request, DossierDisplayTerms.FILE_GROUP_ID);
	
	long groupDossierPartId = ParamUtil.getLong(request, "groupDossierPartId");
	
	int partType = ParamUtil.getInteger(request, DossierFileDisplayTerms.PART_TYPE);
	
	int level = ParamUtil.getInteger(request, DossierFileDisplayTerms.LEVEL);
	
	boolean showVersionItemReference = ParamUtil.getBoolean(request, "showVersionItemReference", true);
	
	boolean readOnly = false;
	readOnly = ParamUtil.getBoolean(request,WebKeys.READ_ONLY);
	
	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	//boolean isCBXL = ParamUtil.getBoolean(request, "isCBXL", false);
	
	int version  = 0;
	
	StringBuilder sbMessage = new StringBuilder();
	
	try {
		
		DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFile(dossierFileId);
		
		int signCheck = dossierFile.getSignCheck();
		
		if(signCheck == 0) {
			sbMessage.append(LanguageUtil.get(portletConfig ,locale , "no-sign"));
		} else if(signCheck == 2){
			sbMessage.append(LanguageUtil.get(portletConfig, locale, "invalid-sign"));
		} else if(signCheck == 1){
			sbMessage.append(LanguageUtil.get(portletConfig, locale, "signer-info"));
			sbMessage.append(" : ");
			sbMessage.append(SignatureUtil.getSignerInfo(dossierFileId));
		}
	} catch (Exception e) {
		
	}
	
	if(dossierId > 0 && dossierPartId > 0){
		try{
			if(isChildDossierPart && fileGroupId > 0){
				version = DossierFileLocalServiceUtil.countDossierFileByDID_DP_GF(dossierId, childDossierPartId, fileGroupId);
			}else{
				if( partType == PortletConstants.DOSSIER_PART_TYPE_OTHER || partType==PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT){
					
					DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFileInUse(dossierId, dossierPartId);
					
					if(Validator.isNotNull(dossierFile)) {
						version = 1;
					}
				}else {
					
					version = DossierFileLocalServiceUtil.countDossierFileByDID_DP(
							dossierId, dossierPartId);
					
					/* List<DossierFile> dossierFiles = DossierFileLocalServiceUtil.getDossierFileByDID_DP(dossierId, dossierPartId);
					
					for(DossierFile file : dossierFiles) {
						if(file.getSyncStatus() == 2) {
							version++;
							hasDossierFileSync = true;
						} 
						
						if(file.getRemoved() == 0 && file.getSyncStatus() != 2) {
							hasDossierFileNoSync = true;
						}
					}

					if(version == DossierFileLocalServiceUtil.countDossierFileByDID_DP(
									dossierId, dossierPartId) && hasDossierFileSync) {
						
							version = DossierFileLocalServiceUtil.countDossierFileByDID_DP(
								dossierId, dossierPartId);
						
					} else if(version == 0) {
						if(hasDossierFileNoSync) {
							version = 1;
						} else {
							version = 0;
						}
						
					} else {
						if(hasDossierFileNoSync) {
							version = version + 1;
						}
					}
					 */
					 
					/* if(isCBXL){
						version = DossierFileLocalServiceUtil.countDossierFileByDID_SS_DP(dossierId, dossierPartId, PortletConstants.DOSSIER_FILE_SYNC_STATUS_SYNCSUCCESS);
					}else{
						version = DossierFileLocalServiceUtil.countDossierFileByDID_DP(dossierId, dossierPartId);
					} */
				}
				
			}
			
			
		}catch(Exception e){}
					
	}
	
	if(readOnly){
		isEditDossier = false;
		isReadOnly = true;
	}
	
%>

<table class="dossier-actions-wraper">
	<tr>
		<c:choose>
			<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_SUBMIT%>">
				<td width="80%" align="right">
					<c:choose>
						<c:when test="<%=isDynamicForm && fileEntryId <= 0 && isOnlineData <= 0%>">
							<c:if test="<%=isEditDossier%>">
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									label="declaration-online" 
									cssClass="label opencps dossiermgt part-file-ctr declaration-online"
									title="declaration-online"
								/>
							</c:if>
						</c:when>
						<c:when test="<%= ( isDynamicForm && fileEntryId > 0 ) || isOnlineData > 0  %>">
							<c:if test="<%=!isReadOnly %>">
							<aui:a 
								id="<%=String.valueOf(dossierPartId) %>"
								dossier="<%=String.valueOf(dossierId) %>"
								dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
								dossier-file="<%=String.valueOf(dossierFileId) %>"
								file-group="<%=String.valueOf(fileGroupId) %>"
								group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
								group-name="<%=groupName %>"
								href="javascript:void(0);" 
								label="view-form" 
								cssClass="label opencps dossiermgt part-file-ctr view-form"
								title="view-form"
							>
							<i class="fa fa-search"></i>
							</aui:a>
							</c:if>
							<c:if test="<%=!showVersionItemReference %>">
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									label="view-content" 
									cssClass="label opencps dossiermgt part-file-ctr view-attachment"
									title="view-attachment"
								/>
							</c:if>
							
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="<%=fileEntryId > 0 %>">
									<aui:a 
										id="<%=String.valueOf(dossierPartId) %>"
										dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
										dossier-file="<%=String.valueOf(dossierFileId) %>"
										group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
										group-name="<%=groupName %>"
										href="javascript:void(0);" 
										label="view-content" 
										cssClass="label opencps dossiermgt part-file-ctr view-attachment"
										title="view-attachment"
									/>
									
									<i title="<%= sbMessage.toString() %>" class="fa fa-certificate" id = "<portlet:namespace />signInfoMsg" />
								</c:when>
								<c:otherwise>
									<c:if test="<%=isEditDossier %>">
										<aui:a 
											id="<%=String.valueOf(dossierPartId) %>"
											dossier="<%=String.valueOf(dossierId) %>"
											dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
											dossier-file="<%=String.valueOf(dossierFileId) %>"
											file-group="<%=String.valueOf(fileGroupId) %>"
											group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
											group-name="<%=groupName %>"
											href="javascript:void(0);" 
											label="upload-file" 
											cssClass="label opencps dossiermgt part-file-ctr upload-dossier-file"
											title="upload-file"
										/>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td width="10%" align="right" >
					<c:if test="<%=showVersionItemReference %>">
						<span class="dossier-version-counter">
							<span class="counter-value" title='<%=LanguageUtil.get(pageContext, "version") %>'>
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									cssClass="view-version"
								>
									<%=version %>
								</aui:a>
							</span>
						</span>
					</c:if>
				</td>
				
				<td width="10%" align="right">
					<c:if test="<%=isEditDossier%>">
						<aui:a
							cssClass="opencps dossiermgt part-file-ctr remove-dossier-file"
							dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							title="remove"
						>
							<i class="fa fa-certificate" aria-hidden="true" ></i>
							
						</aui:a>
					</c:if>
				</td>
				
			</c:when>
			
			<c:when test="<%=(partType == PortletConstants.DOSSIER_PART_TYPE_OTHER || partType==PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT) && level == 0 %>">
				<td width="80%" align="right">
					<c:if test="<%=isEditDossier%>">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							file-group="<%=String.valueOf(fileGroupId) %>"
							group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="label opencps dossiermgt part-file-ctr upload-dossier-file"
							title="upload-file"
						/>
					</c:if>
				</td>
				<td width="10%" align="right">
					
				</td>
				<td width="10%" align="right">
					
				</td>
				
			</c:when>
			
			<c:when test="<%=(partType == PortletConstants.DOSSIER_PART_TYPE_OTHER || partType==PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT) && level > 0 %>">
				<td width="80" align="right">
					<c:choose>
						<c:when test="<%=fileEntryId > 0 %>">
							<aui:a 
								id="<%=String.valueOf(dossierPartId) %>"
								dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
								dossier-file="<%=String.valueOf(dossierFileId) %>"
								group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
								group-name="<%=groupName %>"
								href="javascript:void(0);" 
								label="view-content" 
								cssClass="label opencps dossiermgt part-file-ctr view-attachment"
								title="view-attachment"
							/>
							
							<i title="<%= sbMessage.toString() %>" class="fa fa-certificate" id = "<portlet:namespace />signInfoMsg" />
						</c:when>
						<c:otherwise>
							<c:if test="<%=isEditDossier %>">
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									label="upload-file" 
									cssClass="label opencps dossiermgt part-file-ctr upload-dossier-file"
									title="upload-file"
								/>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td width="10%" align="right" >
					<c:if test="<%=showVersionItemReference %>">
						<span class="dossier-version-counter">
							<span class="counter-value" title='<%=LanguageUtil.get(pageContext, "version") %>'>
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									cssClass="view-version"
								>
									<%=version %>
								</aui:a>
							</span>
						</span>
					</c:if>
				</td>
				
				<td width="10%" align="right">
					<c:if test="<%=isEditDossier %>">
						<aui:a 
							cssClass="opencps dossiermgt part-file-ctr remove-dossier-file"
							dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							title="remove"
						>
							<i class="fa fa-certificate" aria-hidden="true"></i>
							
						</aui:a>
					</c:if>
				</td>
				
			</c:when>
			
			<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_PRIVATE%>">
				<td width="80%" align="right">
					
				</td>
				
				<td width="10%" align="right">
					
				</td>
				<td width="10%" align="right">
					<c:if test="<%=isEditDossier %>">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
							file-group="<%=String.valueOf(fileGroupId) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							cssClass="opencps dossiermgt part-file-ctr remove-individual-group"
							title="remove-group"
						>
							<i class="fa fa-minus-circle" aria-hidden="true"></i>
							
						</aui:a>
					</c:if>
				</td>
			
			</c:when>
			
			<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OPTION && level == 0 %>">
				<td width="80%" align="right">
					
				</td>
				
				<td width="10%" align="right">
					
				</td>
				<td width="10%" align="right">
					
				</td>
			</c:when>
			
			<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OPTION && level > 0 %>">
				<td width="80%" align="right">
					<c:choose>
						<c:when test="<%=fileEntryId > 0 %>">
							<aui:a 
								id="<%=String.valueOf(dossierPartId) %>"
								dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
								dossier-file="<%=String.valueOf(dossierFileId) %>"
								group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
								group-name="<%=groupName %>"
								href="javascript:void(0);" 
								label="view-content" 
								cssClass="label opencps dossiermgt part-file-ctr view-attachment"
								title="view-attachment"
							/>
							
							<i title="<%= sbMessage.toString() %>" class="fa fa-certificate" id = "<portlet:namespace />signInfoMsg" />
						</c:when>
						<c:otherwise>
							<c:if test="<%=isEditDossier %>">
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									label="upload-file" 
									cssClass="label opencps dossiermgt part-file-ctr upload-dossier-file"
									title="upload-file"
								/>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td width="10%" align="right" >
					<c:if test="<%=showVersionItemReference %>">
						<span class="dossier-version-counter">
							<span class="counter-value" title='<%=LanguageUtil.get(pageContext, "version") %>'>
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									cssClass="view-version"
								>
									<%=version %>
								</aui:a>
							</span>
						</span>
					</c:if>
				</td>
				
				<td width="10%" align="right">
					<c:if test="<%=isEditDossier%>">
						<aui:a 
							cssClass="opencps dossiermgt part-file-ctr remove-dossier-file"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
							group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							title="remove"
						>
							<i class="fa fa-certificate" aria-hidden="true" ></i>
							
						</aui:a>
					</c:if>
				</td>
			</c:when>
			
			<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_RESULT %>">
				<td width="80%" align="right">
					<c:choose>
						<c:when test="<%=isDynamicForm && fileEntryId <= 0  %>">
							<c:if test="<%=isEditDossier%>">
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									label="declaration-online" 
									cssClass="label opencps dossiermgt part-file-ctr declaration-online"
									title="declaration-online"
								/>
							</c:if>
						</c:when>
						<c:when test="<%=isDynamicForm && fileEntryId > 0  %>">
							<c:if test="<%=!isReadOnly %>">
							<aui:a 
								id="<%=String.valueOf(dossierPartId) %>"
								dossier="<%=String.valueOf(dossierId) %>"
								dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
								dossier-file="<%=String.valueOf(dossierFileId) %>"
								file-group="<%=String.valueOf(fileGroupId) %>"
								group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
								group-name="<%=groupName %>"
								href="javascript:void(0);" 
								label="view-form" 
								cssClass="label opencps dossiermgt part-file-ctr view-form"
								title="view-form"
							>
							<i class="fa fa-search"></i>
							</aui:a>
							</c:if>
							<c:if test="<%=!showVersionItemReference %>">
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									label="view-content" 
									cssClass="label opencps dossiermgt part-file-ctr view-attachment"
									title="view-attachment"
								/>
							</c:if>
							
							<aui:a 
								id="<%=String.valueOf(dossierPartId) %>"
								dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
								dossier-file="<%=String.valueOf(dossierFileId) %>"
								group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
								group-name="<%=groupName %>"
								href="javascript:void(0);" 
								label="view-content" 
								cssClass="label opencps dossiermgt part-file-ctr view-attachment"
								title="view-attachment"
							/>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="<%=fileEntryId > 0 %>">
									<aui:a 
										id="<%=String.valueOf(dossierPartId) %>"
										dossier-part="<%=String.valueOf(dossierPartId) %>"
										dossier-file="<%=String.valueOf(dossierFileId) %>"
										group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
										group-name="<%=groupName %>"
										href="javascript:void(0);" 
										label="view-content" 
										cssClass="label opencps dossiermgt part-file-ctr view-attachment"
										
										title="view-attachment"
									/>
									
									<i title="<%= sbMessage.toString() %>" class="fa fa-certificate" id = "<portlet:namespace />signInfoMsg" />
								</c:when>
								<c:otherwise>
									<c:if test="<%=isEditDossier %>">
										<aui:a 
											id="<%=String.valueOf(dossierPartId) %>"
											dossier="<%=String.valueOf(dossierId) %>"
											dossier-part="<%=String.valueOf(dossierPartId) %>"
											dossier-file="<%=String.valueOf(dossierFileId) %>"
											file-group="<%=String.valueOf(fileGroupId) %>"
											group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
											group-name="<%=groupName %>"
											href="javascript:void(0);" 
											label="upload-file" 
											cssClass="label opencps dossiermgt part-file-ctr upload-dossier-file"
											title="upload-file"
										/>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td width="10%" align="right">
					<c:if test="<%=showVersionItemReference %>">
						<span class="dossier-version-counter">
							<span class="counter-value" title='<%=LanguageUtil.get(pageContext, "version") %>'>
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier="<%=String.valueOf(dossierId) %>"
									dossier-part="<%=String.valueOf(isChildDossierPart ? childDossierPartId : dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-group="<%=String.valueOf(fileGroupId) %>"
									group-dossier-part="<%=String.valueOf(groupDossierPartId) %>"
									group-name="<%=groupName %>"
									href="javascript:void(0);" 
									cssClass="view-version"
								>
									<%=version %>
								</aui:a>
							</span>
						</span>
					</c:if>
				</td>
				
				<td width="10%" align="right">
					<c:if test="<%=isEditDossier %>">
						<aui:a
							cssClass="opencps dossiermgt part-file-ctr remove-dossier-file"
							process-order="<%=String.valueOf(processOrderId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							title="remove"
						>
							<i class="fa fa-certificate" aria-hidden="true"  ></i>
							
						</aui:a>
					</c:if>
				</td>

			</c:when>
			
		</c:choose>
	</tr>
</table>

<aui:script>
	AUI().ready('aui-tooltip', 'aui-base', function(A){
		
		var items = A.all('#<portlet:namespace />signInfoMsg');
		
		items.each(function(item) {
			console.log("aaaaaaa");
			item.on('mouseover',function(){
				new A.Tooltip(
			      {
			        trigger: item,
			        position: 'right'
			      }
			    ).render();
			})
		});

	}); 
	
	
	
	
</aui:script>
