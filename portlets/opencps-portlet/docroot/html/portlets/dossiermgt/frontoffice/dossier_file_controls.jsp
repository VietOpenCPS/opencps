
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

<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../init.jsp"%>

<%
	DossierFile dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	DossierPart dossierPart = (DossierPart) request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	int index = ParamUtil.getInteger(request, "index");
	
	int partType = ParamUtil.getInteger(request, "partType");
	
	int level = ParamUtil.getInteger(request, "level");
	
	String groupName = ParamUtil.getString(request, "groupName");
	
%>

<c:if test="<%=true %>">
	<table width="100%">
		<tr>
			<c:choose>
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_COMPONEMT %>">
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							label="declaration-online" 
							cssClass="opencps dossiermgt part-file-ctr declaration-online"
							onClick='<%=renderResponse.getNamespace() + "declarationOnline(this)" %>'
						/>
					</td>
					
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
					</td>
					<td width="10%" align="right">
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>0</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							onClick='<%=renderResponse.getNamespace() + "removeFileUpload(this)" %>'
						>
							<i class="fa fa-times" aria-hidden="true"></i>
							<aui:input id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' name="uploadDataSchema"  type="hidden" cssClass="uploadDataSchema"/>
							<aui:input id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' name="fileUpload"  type="hidden"/>
						</aui:a>
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_SUBMIT && level > 0 %>">
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
						
					</td>
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							label="taken-from-archive" 
							cssClass="opencps dossiermgt part-file-ctr taken-from-archive"
						/>
					</td>
					<td width="10%" align="right">
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>0</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							onClick='<%=renderResponse.getNamespace() + "removeFileUpload(this)" %>'
						>
							<i class="fa fa-times" aria-hidden="true"></i>
							<aui:input id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' name="uploadDataSchema"  type="hidden" cssClass="uploadDataSchema"/>
							<aui:input id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' name="fileUpload"  type="hidden"/>
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
							href="javascript:void(0);" 
							cssClass="opencps dossiermgt part-file-ctr remove-group"
							onClick='<%=renderResponse.getNamespace() + "removeDossierGroup(this)" %>'
						>
							<i class="fa fa-minus-circle" aria-hidden="true"></i>
							
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
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
						
					</td>
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							label="taken-from-archive" 
							cssClass="opencps dossiermgt part-file-ctr taken-from-archive"
						/>
					</td>
					<td width="10%" align="right">
						
					</td>
					<td width="10%" align="right">
						
					</td>
				</c:when>
				
				<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OTHER && level > 0 %>">
					<td width="40%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							label="upload-file" 
							cssClass="opencps dossiermgt part-file-ctr upload-file" 
							onClick='<%=renderResponse.getNamespace() + "uploadFile(this)" %>'
						/>
						
					</td>
					<td width="40%" align="right">
						
					</td>
					<td width="10%" align="right">
						<span class="dossier-file-counter">
							<span class='<%="counter-value alias-" + dossierPartId + StringPool.DASH + index%>'>0</span>
						</span>
					</td>
					<td width="10%" align="right">
						<aui:a 
							id="<%=String.valueOf(dossierPartId) %>"
							dossier-part="<%=String.valueOf(dossierPartId) %>"
							index="<%=String.valueOf(index) %>"
							group-name="<%=groupName %>"
							href="javascript:void(0);" 
							cssClass="opencps dossiermgt part-file-ctr remove-file"
							onClick='<%=renderResponse.getNamespace() + "removeFileUpload(this)" %>'
						>
							<i class="fa fa-times" aria-hidden="true"></i>
							<aui:input id='<%="uploadDataSchema" + dossierPartId + StringPool.DASH + index %>' name="uploadDataSchema"  type="hidden" cssClass="uploadDataSchema"/>
							<aui:input id='<%="fileUpload" + dossierPartId + StringPool.DASH + index %>' name="fileUpload"  type="hidden"/>
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
</aui:script>