
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

<%@page import="com.liferay.portlet.documentlibrary.util.DLUtil"%>
<%@page import="org.opencps.util.DLFileEntryUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@ include file="/init.jsp"%>

<%
	
	DossierPart dossierPart = null;
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);
	
	long fileEntryId = ParamUtil.getLong(request, DossierFileDisplayTerms.FILE_ENTRY_ID);
	
	int index = ParamUtil.getInteger(request, DossierFileDisplayTerms.INDEX);
	
	int level = ParamUtil.getInteger(request, DossierFileDisplayTerms.LEVEL);
	
	int partType = ParamUtil.getInteger(request, DossierFileDisplayTerms.PART_TYPE);

	long processOrderId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
	
	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	String fileURL = StringPool.BLANK;
	try{
		if(dossierPartId > 0){
			dossierPart = DossierPartLocalServiceUtil.getDossierPart(dossierPartId);
		}
		
		if(fileEntryId > 0){
			FileEntry fileEntry = DLFileEntryUtil.getFileEntry(fileEntryId);
			if(fileEntry != null){
				fileURL = DLUtil.getPreviewURL(fileEntry, fileEntry.getFileVersion(), 
						themeDisplay, StringPool.BLANK);
			}
			
		}
	}
	catch(Exception e){
		
	}
%>

<c:if test="<%=true %>">
	<table width="100%">
		<tr>
			<c:choose>
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_SUBMIT%>">
					<td width="40%" align="right">
						<c:if test="<%=dossierPart != null && Validator.isNotNull(dossierPart.getFormReport()) && Validator.isNotNull(dossierPart.getFormScript()) %>">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							label="declaration-online" 
							cssClass="label opencps dossiermgt part-file-ctr declaration-online"
							onClick='<%=renderResponse.getNamespace() + "declarationOnline(this)" %>'
						/>
						</c:if>
					</td>
					
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="label opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
					</td>
					<td width="10%" align="right">
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>
								<%=fileEntryId > 0 ? 1 : 0 %>
							</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							onClick='<%=renderResponse.getNamespace() + "removeFileUpload(this)" %>'
						>
							<i class="fa fa-times" aria-hidden="true"></i>
							<aui:input 
								id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' 
								name="uploadDataSchema" 
								type="hidden" 
								cssClass="uploadDataSchema"
							/>
							<aui:input 
								id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' 
								name="fileUpload" 
								type="hidden"
								value="<%=fileEntryId %>"
							/>
							<aui:input 
								id='<%="dossierFile" + dossierPartId + StringPool.DASH + index %>' 
								name="dossierFile" 
								type="hidden"
								value="<%=dossierFileId%>"
							/>
						</aui:a>
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OTHER && level == 0 %>">
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="label opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
						
					</td>
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							label="taken-from-archive" 
							cssClass="opencps dossiermgt part-file-ctr taken-from-archive"
						/>
					</td>
					<td width="10%" align="right">
						
					</td>
					<td width="10%" align="right">
						<aui:input 
							id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' 
							name="uploadDataSchema" 
							type="hidden" 
							cssClass="uploadDataSchema"
						/>
						<aui:input 
							id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' 
							name="fileUpload" 
							type="hidden"
							value="<%=fileEntryId %>"
						/>
						<aui:input 
							id='<%="dossierFile" + dossierPartId + StringPool.DASH + index %>' 
							name="dossierFile" 
							type="hidden"
							value="<%=dossierFileId%>"
						/>
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OTHER && level > 0 %>">
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="label opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
						
					</td>
					<td width="40%" align="right">
						
					</td>
					<td width="10%" align="right">
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>
								<%=fileEntryId > 0 ? 1 : 0 %>
							</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a 
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							dossier-part="<%=String.valueOf(dossierPartId) %>"	
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							onClick='<%=renderResponse.getNamespace() + "removeFileUpload(this)" %>'
						>
							<i class="fa fa-times" aria-hidden="true"></i>
							<aui:input 
								id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' 
								name="uploadDataSchema" 
								type="hidden" 
								cssClass="uploadDataSchema"
							/>
							<aui:input 
								id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' 
								name="fileUpload" 
								type="hidden"
								value="<%=fileEntryId %>"
							/>
							<aui:input 
								id='<%="dossierFile" + dossierPartId + StringPool.DASH + index %>' 
								name="dossierFile" 
								type="hidden"
								value="<%=dossierFileId%>"
							/>
						</aui:a>
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_PRIVATE%>">
					<td width="40%" align="right">
						
					</td>
					<td width="40%" align="right">
						
					</td>
					<td width="10%" align="right">
						
					</td>
					<td width="10%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							cssClass="opencps dossiermgt part-file-ctr remove-group"
							onClick='<%=renderResponse.getNamespace() + "removeDossierGroup(this)" %>'
						>
							<i class="fa fa-minus-circle" aria-hidden="true"></i>
							
						</aui:a>
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OPTION && level == 0 %>">
					<td width="40%" align="right">
						
					</td>
					<td width="40%" align="right">
						
					</td>
					<td width="10%" align="right">
						
					</td>
					<td width="10%" align="right">
						
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OPTION && level > 0 %>">
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="label opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
						
					</td>
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							label="taken-from-archive" 
							cssClass="label opencps dossiermgt part-file-ctr taken-from-archive"
						/>
					</td>
					<td width="10%" align="right">
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>
								<%=fileEntryId > 0 ? 1 : 0 %>
							</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a 
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							onClick='<%=renderResponse.getNamespace() + "removeFileUpload(this)" %>'
						>
							<i class="fa fa-times" aria-hidden="true"></i>
							<aui:input 
								id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' 
								name="uploadDataSchema" 
								type="hidden" 
								cssClass="uploadDataSchema"
							/>
							<aui:input 
								id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' 
								name="fileUpload" 
								type="hidden"
								value="<%=fileEntryId %>"
							/>
							<aui:input 
								id='<%="dossierFile" + dossierPartId + StringPool.DASH + index %>' 
								name="dossierFile" 
								type="hidden"
								value="<%=dossierFileId%>"
							/>
						</aui:a>
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_RESULT %>">
					<td width="40%" align="right">
						<c:if test="<%=dossierPart != null && Validator.isNotNull(dossierPart.getFormReport()) && Validator.isNotNull(dossierPart.getFormScript()) %>">
							 <aui:a 
								id="<%=String.valueOf(dossierPartId) %>"
								process-order="<%=String.valueOf(processOrderId) %>"
								dossier-file="<%=String.valueOf(dossierFileId) %>"
								dossier-part="<%=String.valueOf(dossierPartId) %>"
								href="javascript:void(0);" 
								label="declaration-online" 
								cssClass="label opencps dossiermgt part-file-ctr declaration-online"
								onClick='<%=renderResponse.getNamespace() + "declarationOnline(this)" %>'
							/>
						</c:if>
					</td>
					
					<td width="40%" align="right">
						<c:choose>
							<c:when test="<%=fileEntryId > 0 %>">
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									dossier-part="<%=String.valueOf(dossierPartId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									file-url="<%=fileURL %>"
									index="<%=String.valueOf(index) %>"
									group-name="<%=groupName %>"
									level = "<%=level %>"
									file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
									part-type="<%=partType %>"
									template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
									href="javascript:void(0);" 
									label="view-attachment" 
									cssClass="label opencps dossiermgt part-file-ctr view-attachment" 
									onClick='<%=renderResponse.getNamespace() + "viewAttachment(this)" %>'
								/>
							</c:when>
							<c:otherwise>
								<aui:a 
									id="<%=String.valueOf(dossierPartId) %>"
									process-order="<%=String.valueOf(processOrderId) %>"
									dossier-file="<%=String.valueOf(dossierFileId) %>"
									dossier-part="<%=String.valueOf(dossierPartId) %>"
									file-entry="<%=String.valueOf(fileEntryId) %>"
									href="javascript:void(0);" 
									label="upload-file" 
									cssClass="label opencps dossiermgt part-file-ctr upload-file" 
									onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
								/>
							</c:otherwise>
						</c:choose>
						
					</td>
					<td width="10%" align="right">
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>
								<%=fileEntryId > 0 ? 1 : 0 %>
							</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							process-order="<%=String.valueOf(processOrderId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							id="<%=String.valueOf(dossierPartId) %>"
							onClick='<%=renderResponse.getNamespace() + "removeFileUpload(this)" %>'
						>
							<i class="fa fa-times" aria-hidden="true"></i>
							<aui:input 
								id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' 
								name="uploadDataSchema" 
								type="hidden" 
								cssClass="uploadDataSchema"
							/>
							<aui:input 
								id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' 
								name="fileUpload" 
								type="hidden"
								value="<%=fileEntryId %>"
							/>
							<aui:input 
								id='<%="dossierFile" + dossierPartId + StringPool.DASH + index %>' 
								name="dossierFile" 
								type="hidden"
								value="<%=dossierFileId%>"
							/>
						</aui:a>
					</td>
				</c:when>
				
			</c:choose>
		</tr>
	</table>
</c:if>

