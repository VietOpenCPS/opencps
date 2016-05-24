
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
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@ include file="../init.jsp"%>

<%
	//DossierFile dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	DossierPart dossierPart = (DossierPart) request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	
	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);
	
	long fileEntryId = ParamUtil.getLong(request, DossierFileDisplayTerms.FILE_ENTRY_ID);
	
	int index = ParamUtil.getInteger(request, DossierFileDisplayTerms.INDEX);
	
	int level = ParamUtil.getInteger(request, DossierFileDisplayTerms.LEVEL);
	
	int partType = ParamUtil.getInteger(request, DossierFileDisplayTerms.PART_TYPE);
	
	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	
	String fileURL = StringPool.BLANK;
	
	if(fileEntryId > 0){
		FileEntry fileEntry = DLFileEntryUtil.getFileEntry(fileEntryId);
		if(fileEntry != null){
			fileURL = DLUtil.getPreviewURL(fileEntry, fileEntry.getFileVersion(), 
					themeDisplay, StringPool.BLANK);
		}
		
	}
		
%>

<c:if test="<%=true %>">
	<table width="100%">
		<tr>
			<c:choose>
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_SUBMIT%>">
					<td width="40%" align="right">
						<aui:a
							id="<%=String.valueOf(dossierPartId) %>"
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							href="javascript:void(0);" 
							label="declaration-online" 
							cssClass="opencps dossiermgt part-file-ctr declaration-online"
							onClick='<%=renderResponse.getNamespace() + "declarationOnline(this)" %>'
						/>
					</td>
					
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							file-url="<%=fileURL %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label='<%=fileEntryId <= 0 ? "upload-file" : "view-attachment" %>' 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + (fileEntryId <= 0 ? "uploadFile(this)" : "viewAttachment(this)") %>'
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
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
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
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
						
					</td>
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier="<%=String.valueOf(dossierId) %>"
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
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							file-url="<%=fileURL %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label='<%=fileEntryId <= 0 ? "upload-file" : "view-attachment" %>' 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + (fileEntryId <= 0 ? "uploadFile(this)" : "viewAttachment(this)") %>'
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
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"	
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
							dossier="<%=String.valueOf(dossierId) %>"
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
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							level = "<%=level %>"
							file-name="<%=dossierPart != null ? dossierPart.getPartName() : StringPool.BLANK %>"
							part-type="<%=partType %>"
							template-no="<%=dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK %>"
							href="javascript:void(0);" 
							label='<%=fileEntryId <= 0 ? "upload-file" : "view-attachment" %>' 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + (fileEntryId <= 0 ? "uploadFile(this)" : "viewAttachment(this)") %>'
						/>
						
					</td>
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier="<%=String.valueOf(dossierId) %>"
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
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>
								<%=fileEntryId > 0 ? 1 : 0 %>
							</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a 
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							dossier="<%=String.valueOf(dossierId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							dossier-file="<%=String.valueOf(dossierFileId) %>"
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
				
			</c:choose>
		</tr>
	</table>

</c:if>

<aui:script use="aui-base,aui-io">
	AUI().ready(function(A){
		var cancelButton = A.one('#<portlet:namespace/>cancel');
		if(cancelButton){
			cancelButton.on('click', function(){
				<portlet:namespace/>closeDialog();
			});
		}
		
		
	});
	
	Liferay.provide(window, '<portlet:namespace/>responseData', function(data) {
		var Util = Liferay.Util;
		Util.getOpener().Liferay.fire('getUploadDossierFileData', {responseData:data});
		<portlet:namespace/>closeDialog();
	});
	
	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
		var dialog = Liferay.Util.getWindow('<portlet:namespace/>dossierFileId');
		dialog.destroy(); // You can try toggle/hide whate
	});

	Liferay.provide(window, '<portlet:namespace />uploadTempFile', function() {
		var A = AUI();
		var uri = A.one('#<portlet:namespace/>fm').attr('action');
		var configs = {
             method: 'POST',
             form: {
                 id: '#<portlet:namespace/>fm',
                 upload: true
             },
             sync: true,
             on: {
             	failure: function(event, id, obj) {
				
				},
				success: function(event, id, obj) {
					var response = this.get('responseData');
					console.log(response);
				},
                complete: function(event, id, obj){
                   
                }
             }
        };
	            
	    A.io.request(uri, configs);    
		
	},['aui-io']);
	
	Liferay.provide(window, '<portlet:namespace />viewAttachment', function(e) {
		var A = AUI();
		var instance = A.one(e);
		var dossierFileId = instance.attr('dossier-file');
		var fileURL = instance.attr('file-url');
		if(fileURL == ''){
			alert('<%= UnicodeLanguageUtil.get(pageContext, "not-attachment-file") %>');
			return;
		}else{
			window.open(fileURL, '_blank');
		}
		
	});
</aui:script>