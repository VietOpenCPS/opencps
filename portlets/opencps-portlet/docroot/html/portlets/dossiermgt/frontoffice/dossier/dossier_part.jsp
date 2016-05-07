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
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierFileException"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.model.impl.DossierPartImpl"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.util.WebKeys"%>

<%@ include file="../../init.jsp"%>

<portlet:renderURL var="updateDossierFileURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "upload_dossier_file.jsp" %>'/>
</portlet:renderURL>

<portlet:actionURL var="deleteTempFileURL" name="deleteTempFile">
	<portlet:param name="fileEntryId" value="<%=String.valueOf(12345) %>"/>
</portlet:actionURL>

<liferay-ui:error-marker key="errorSection" value="dossier_part" />

<liferay-ui:error exception="<%= EmptyDossierFileException.class %>" message="<%=EmptyDossierFileException.class.getName() %>"/>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);

	ServiceConfig serviceConfig = (ServiceConfig) request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
	
	String cmd = ParamUtil.getString(request, Constants.CMD);
	
	String privateDossierGroup = StringPool.BLANK;
	
	List<DossierPart> dossierPartsLevel1 = new ArrayList<DossierPart>();
	
	if(dossierTemplate != null){
		try{
			dossierPartsLevel1 = DossierPartLocalServiceUtil.getDossierPartsByT_P(dossierTemplate.getDossierTemplateId(), 0);
		}catch(Exception e){}
	}
	
	int index = 0; 
	
	if(dossierPartsLevel1 != null){
		for (DossierPart dossierPartLevel1 : dossierPartsLevel1){
			System.out.println(dossierPartLevel1.getPartName() + "-" + dossierPartLevel1.getPartType());
			int partType = dossierPartLevel1.getPartType();
			
			List<DossierPart> dossierParts = DossierMgtUtil.getTreeDossierPart(dossierPartLevel1.getDossierpartId());
			
			if(dossierParts != null){
				%>
				<div class="opencps dossiermgt dossier-part-tree" id='<%= renderResponse.getNamespace() + "tree" + dossierParts.get(0).getDossierpartId()%>'>
					<c:choose>
						<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_OPTION ||
								partType == PortletConstants.DOSSIER_PART_TYPE_SUBMIT || 
								partType == PortletConstants.DOSSIER_PART_TYPE_OTHER %>">
							<%
							for(DossierPart dossierPart : dossierParts){
								
								int level = 1;
								
								String treeIndex = dossierPart.getTreeIndex();
								
								if(Validator.isNotNull(treeIndex)){
									level = StringUtil.count(treeIndex, StringPool.PERIOD);
								}
								
								DossierFile dossierFile = null;
								
								if(dossier != null){
									try{
										dossierFile = DossierFileLocalServiceUtil.getDossierFileByD_P(dossier.getDossierId(), 
												dossierPart.getDossierpartId());
									}catch(Exception e){}
								}
								
								%>
									<div 
										id='<%=renderResponse.getNamespace() + "row-" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' 
										index="<%=index %>"
										dossier-part="<%=dossierPart.getDossierpartId() %>"
										class="opencps dossiermgt dossier-part-row"
									>
										<span class='<%="level-" + level + " opencps dossiermgt dossier-part"%>'>
											<span class="row-icon">
												<i 
													id='<%="rowcheck" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' 
													class='<%=dossierFile != null ? "fa fa-check-square-o" : "fa fa-square-o" %>' 
													aria-hidden="true"
												>
												</i>
											</span>
											<span class="opencps dossiermgt dossier-part-name">
												<%=dossierPart.getPartName() %>
											</span>
										</span>
									
										<span class="opencps dossiermgt dossier-part-control">
											<liferay-util:include 
												page="/html/portlets/dossiermgt/frontoffice/dossier_file_controls.jsp" 
												servletContext="<%=application %>"
											>
												<portlet:param 
													name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
													value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
												/>
												<portlet:param name="<%=DossierFileDisplayTerms.FILE_ENTRY_ID %>" value="<%=String.valueOf(dossierFile != null ? dossierFile.getFileEntryId() : 0) %>"/>
												<portlet:param name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" value="<%=String.valueOf(dossierFile != null ? dossierFile.getDossierFileId() : 0) %>"/>
												<portlet:param name="<%=DossierFileDisplayTerms.INDEX %>" value="<%=String.valueOf(index) %>"/>
												<portlet:param name="<%=DossierFileDisplayTerms.LEVEL %>" value="<%=String.valueOf(level) %>"/>
												<portlet:param name="<%=DossierFileDisplayTerms.GROUP_NAME %>" value="<%=StringPool.BLANK%>"/>
												<portlet:param name="<%=DossierFileDisplayTerms.PART_TYPE %>" value="<%=String.valueOf(dossierPart.getPartType()) %>"/>
											</liferay-util:include>
										</span>
									</div>
								<%
								index++;
							}
							%>
						</c:when>
						
						<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_PRIVATE%>">
							<%
								privateDossierGroup = dossierParts.get(0).getPartName();
							%>
							<div
								id='<%=renderResponse.getNamespace() + "row-" + dossierParts.get(0).getDossierpartId() + StringPool.DASH + index %>' 
								index="<%=index %>"
								dossier-part-size="<%=dossierParts.size() %>"
								dossier-part="<%=dossierParts.get(0).getDossierpartId() %>" 
								class="opencps dossiermgt dossier-part-row root-group"
							>
								<span class='<%="level-0 opencps dossiermgt dossier-part"%>'>
									<span class="row-icon">
										<i class="fa fa-minus-square-o" aria-hidden="true"></i>
									</span>
									<span class="opencps dossiermgt dossier-part-name">
										<liferay-ui:message key="private-dossier"/>
									</span>
								</span>
								
								<span class="opencps dossiermgt dossier-part-control">
									<aui:a 
										id="<%=String.valueOf(dossierParts.get(0).getDossierpartId()) %>"
										dossier-part="<%=String.valueOf(dossierParts.get(0).getDossierpartId()) %>"
										index="<%=String.valueOf(index) %>"
										dossier-part-size="<%=dossierParts.size() %>"
										href="javascript:void(0);" 
										label="add-private-dossier" 
										cssClass="opencps dossiermgt part-file-ctr add-private-dossier"
										onClick='<%=renderResponse.getNamespace() + "addPrivateDossierGroup(this)" %>'
									/>
									
								</span>
							</div>
							<div 
								id='<%=renderResponse.getNamespace() + "privateDossierPartGroup" + dossierParts.get(0).getDossierpartId() + StringPool.DASH + index%>' 
								class="opencps dossiermgt dossier-part-tree"
							>
								<%
								for(DossierPart dossierPart : dossierParts){
									
									int level = 1;
									
									String treeIndex = dossierPart.getTreeIndex();
									
									if(Validator.isNotNull(treeIndex)){
										level = StringUtil.count(treeIndex, StringPool.PERIOD);
									}
									
									DossierFile dossierFile = null;
									
									if(dossier != null){
										try{
											dossierFile = DossierFileLocalServiceUtil.getDossierFileByD_P(dossier.getDossierId(), 
													dossierPart.getDossierpartId());
										}catch(Exception e){}
									}
									
									%>
										<div 
											id='<%=renderResponse.getNamespace() + "row-" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' 
											index="<%=index %>"
											dossier-part="<%=dossierPart.getDossierpartId() %>"
											class="opencps dossiermgt dossier-part-row"
										>
											<span class='<%="level-" + level + " opencps dossiermgt dossier-part"%>'>
												<span class="row-icon">
													<i 
														id='<%="rowcheck" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' 
														class="fa fa-square-o" 
														aria-hidden="true">
													</i>
												</span>
												<%
													String dossierGroup = StringPool.SPACE;
													if(dossierParts.indexOf(dossierPart) == 0){
														dossierGroup = StringPool.SPACE +  "dossier-group" + StringPool.SPACE;
													}
												%>
												<span class='<%="opencps dossiermgt" +  dossierGroup + "dossier-part-name" %>'>
													<%=dossierPart.getPartName() %>
												</span>
											</span>
										
											<span class="opencps dossiermgt dossier-part-control">
												<liferay-util:include 
													page="/html/portlets/dossiermgt/frontoffice/dossier_file_controls.jsp"  
													servletContext="<%=application %>"
												>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
														value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
													/>
													<portlet:param name="<%=DossierFileDisplayTerms.FILE_ENTRY_ID %>" value="<%=String.valueOf(dossierFile != null ? dossierFile.getFileEntryId() : 0) %>"/>
													<portlet:param name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" value="<%=String.valueOf(dossierFile != null ? dossierFile.getDossierFileId() : 0) %>"/>
													<portlet:param name="<%=DossierFileDisplayTerms.INDEX %>" value="<%=String.valueOf(index) %>"/>
													<portlet:param name="<%=DossierFileDisplayTerms.LEVEL %>" value="<%=String.valueOf(level) %>"/>
													<portlet:param name="<%=DossierFileDisplayTerms.GROUP_NAME %>" value="<%=dossierParts.get(0).getPartName()%>"/>
													<portlet:param name="<%=DossierFileDisplayTerms.PART_TYPE %>" value="<%=String.valueOf(dossierPart.getPartType()) %>"/>
												</liferay-util:include>
											</span>
										</div>
										
									<%
									index++;
								}
								%>
							</div>
						</c:when>
					</c:choose>
					
				</div>
				
			<%
			}
		}
	}
	%>
		<aui:input name="curIndex" type="hidden" value="<%=index %>"/>
	<%
%>
<aui:script>
	
	var privateDossierGroup = '<%=privateDossierGroup%>';
	
	var tempFileEntryIds = []; 
	
	/* $(window).on('beforeunload', function(e) {
		return "Sure U are?";
	});

	$(window).on('unload', function(e) {
		alert(tempFileEntryIds);
	}); */

	AUI().ready('aui-base','liferay-portlet-url','aui-io', function(A){
		
		//reset all uploadDataSchema
		
		var uploadDataSchemas = A.all('.uploadDataSchema');
		
		if(uploadDataSchemas){
			uploadDataSchemas.each(function(node){
				node.val('');
			});
		}
		
		//conver to array
		privateDossierGroup = privateDossierGroup.split(',');
		
		var addPrivateDossierCtrs = A.all('.add-private-dossier');
		
	});
	
	Liferay.provide(window, '<portlet:namespace/>addPrivateDossierGroup', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var dossierPartId = instance.attr('dossier-part');
	
		var size = parseInt(instance.attr('dossier-part-size'));
		
		var index = parseInt(A.one('#<portlet:namespace/>curIndex').val()) + 1;
		
		var groupNames = privateDossierGroup.toString();
			
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/edit_dossier_part_group.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("dossierPartId", dossierPartId);
		portletURL.setParameter("index", index);
		portletURL.setParameter("size", size);
		portletURL.setParameter("groupNames", groupNames);
			
		<portlet:namespace/>openDossierDialog(portletURL.toString(), '<portlet:namespace />privateDossierGroup', '<%= UnicodeLanguageUtil.get(pageContext, "add-private-dossier") %>');
	});
	
	
	Liferay.provide(window, '<portlet:namespace/>removeDossierGroup', function(e) {
		if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-group") %>')){
			var A = AUI();
			
			var instance = A.one(e);
			
			var dossierPartId = instance.attr('dossier-part');
			
			var index = instance.attr('index');
			
			var groupName = instance.attr('group-name');
			
			var privateDossierPartGroup = A.one('#<portlet:namespace />privateDossierPartGroup' + dossierPartId + '-' + index);
			
			privateDossierPartGroup.remove();
			
			var groupNameIndex = privateDossierGroup.indexOf(groupName);
			
			if (groupNameIndex > -1) {
				privateDossierGroup.splice(groupNameIndex, 1);
			}
		}
	});
	
	
	Liferay.provide(window, '<portlet:namespace/>removeFileUpload', function(e) {
		if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-dossier-file") %>')){
			var A = AUI();
			
			var instance = A.one(e);
			
			var dossierPartId = instance.attr('dossier-part');
			
			var index = instance.attr('index');

			var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
			
			var dossierFileData = A.one('#<portlet:namespace/>dossierFileData' + dossierPartId + '-' + index);
			
			var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
			
			var dossierFileId = parseInt(A.one('#<portlet:namespace/>dossierFile' + dossierPartId + '-' + index).val());
			
			if(fileUpload && parseInt(fileUpload.val()) > 0){
				var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
				
				if(dossierFileId > 0){
					portletURL.setParameter("javax.portlet.action", "deleteDossierFile");
					portletURL.setParameter("dossierFileId", dossierFileId);
				}else{
					portletURL.setParameter("javax.portlet.action", "deleteTempFile");
				}
				
				portletURL.setPortletMode("view");
				portletURL.setParameter("fileEntryId", fileUpload.val());
				portletURL.setWindowState('<%=WindowState.NORMAL%>');
				
				A.io.request(
					portletURL.toString(),
					{
						on: {
							success: function(event, id, obj) {
								var response = this.get('responseData');
								if(response){
									response = JSON.parse(response);
									
									if(response.deleted == true){
										
										fileUpload.val('');

										if(dossierFileData){
											dossierFileData.val('');
										}
										
										if(rowcheck){
											rowcheck.replaceClass('fa-check-square-o', 'fa-square-o');
										}
										
										var counterLabel = A.one('.alias-' + dossierPartId + '-' + index);
										 
										if(counterLabel){
											counterLabel.text(0);
										}
									}else{
										alert('<%= UnicodeLanguageUtil.get(pageContext, "error-while-remove-this-file") %>');
									}
								}
							}
						}
					}
				);
			}
			
		}
	});
	
	
	Liferay.provide(window, '<portlet:namespace/>declarationOnline', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var dossierPartId = instance.attr('dossier-part');
		
		var index = instance.attr('index');
		
		var groupName = instance.attr('group-name');

		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/dynamic_form.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("dossierPartId", dossierPartId);
		portletURL.setParameter("index", index);
		portletURL.setParameter("groupName", groupName);
		
		<portlet:namespace/>openDossierDialog(portletURL.toString(), '<portlet:namespace />dynamicForm','<%= UnicodeLanguageUtil.get(pageContext, "declaration-online") %>');
	});

	
	Liferay.provide(window, '<portlet:namespace/>uploadFile', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var dossierPartId = instance.attr('dossier-part');
		
		var index = instance.attr('index');
		
		var groupName = instance.attr('group-name');
		
		var fileName = instance.attr('file-name');
		
		var templateFileNo = instance.attr('template-no');
		
		var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
		
		var dossierFileId = parseInt(A.one('#<portlet:namespace/>dossierFile' + dossierPartId + '-' + index).val());
		
		if(fileUpload && parseInt(fileUpload.val()) > 0){
			alert('<%=UnicodeLanguageUtil.get(pageContext, "remove-old-file-before-upload")%>');
			return;
		}
		
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/upload_dossier_file.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("dossierPartId", dossierPartId);
		portletURL.setParameter("index", index);
		portletURL.setParameter("groupName", groupName);
		portletURL.setParameter("fileName", fileName);
		portletURL.setParameter("templateFileNo", templateFileNo);
		portletURL.setParameter("dossierFileId", dossierFileId);
		<portlet:namespace/>openDossierDialog(portletURL.toString(), '<portlet:namespace />dossierFileId','<%= UnicodeLanguageUtil.get(pageContext, "upload-dossier-file") %>');
	});

	Liferay.provide(window, '<portlet:namespace/>openDossierDialog', function(uri, id, title) {
		var dossierFileDialog = Liferay.Util.openWindow(
			{
				dialog: {
					cache: false,
					cssClass: 'opencps-dossiermgt-upload-dossier-file',
					modal: true,
					height: 480,
					width: 800
				},
				cache: false,
				id: id,
				title: title,
				uri: uri
				
			},function(evt){
				
			}
		);
	});
	
	Liferay.provide(window, '<portlet:namespace/>renderPrivateDossierGroup', function(dossierPartId, index, groupName) {
		var A = AUI();
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
			portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/render_private_dossier_part.jsp");
			portletURL.setWindowState("<%=LiferayWindowState.EXCLUSIVE.toString()%>"); 
			portletURL.setPortletMode("normal");
			portletURL.setParameter("dossierPartId", dossierPartId);
			portletURL.setParameter("index", index);
			portletURL.setParameter("groupName", groupName);
			
		A.io.request(
			portletURL.toString(),
			{
				on: {
					success: function(event, id, obj) {
						var response = this.get('responseData');
						var tree = A.one('#<portlet:namespace />tree' + dossierPartId);
		
						if(tree){
							tree.append(response);
							privateDossierGroup.push(groupName);
						}
					}
				}
			}
		);
	},['aui-io','liferay-portlet-url']);
	
	Liferay.on('getPrivateDossierGroupSchema',function(event) {
		
		var A = AUI();
		
		var schema = event.responseData;
		
		var groupName = schema.groupName;
		
		var dossierPartId = schema.dossierPartId;
		
		var index = schema.index;
		
		var size = schema.size;
		
		A.one('#<portlet:namespace/>curIndex').val(parseInt(index) + parseInt(size));
		
		<portlet:namespace/>renderPrivateDossierGroup(dossierPartId, index, groupName);
		
	},['aui-io']);
	
	Liferay.on('getDynamicFormDataSchema',function(event) {
		
		var A = AUI();
		
		var schema = event.responseData;
		
		var dossierPartId = schema.dossierPartId;
		
		var index = schema.index;
		
		var formData = schema.formData;
		
		var uploadDataSchema = A.one('#<portlet:namespace/>uploadDataSchema' + dossierPartId + '-' + index);
		
		var data = uploadDataSchema.val();
		
		if(data != ''){
			data = JSON.parse(data);
			data.formData = formData;
		}else{
			var object = new Object();
			object.formData = formData;
			data = object;
		}
				
		uploadDataSchema.val(JSON.stringify(data));
		
	});
	
	Liferay.on('getUploadDataSchema',function(event) {
		
		var A = AUI();
		 
		var schema = event.responseData;
		
		if(schema){
			
			var index = schema.index;
			
			var dossierPartId = schema.dossierPartId;
			
			var fileEntryId = schema.fileEntryId;
			
			var dossierFileId = schema.dossierFileId;
			
			var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
			
			var uploadDataSchema = A.one('#<portlet:namespace/>uploadDataSchema' + dossierPartId + '-' + index);
			
			var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
			
			var dossierFile = A.one('#<portlet:namespace/>dossierFile' + dossierPartId + '-' + index);
				 
			if(uploadDataSchema){
				
				if(uploadDataSchema.val() == ''){
					uploadDataSchema.val(JSON.stringify(schema));
				}else{
					var formData = JSON.parse(uploadDataSchema.val());
					schema.formData = formData.formData;
					uploadDataSchema.val(JSON.stringify(schema));
				}
								
				rowcheck.replaceClass('fa-square-o', 'fa-check-square-o');
			}
			
			if(fileUpload && parseInt(fileEntryId) > 0){
				tempFileEntryIds.push(fileEntryId);
				fileUpload.val(fileEntryId);
			}
			
			if(dossierFile){
				dossierFile.val(dossierFileId);
			}
				 
			var counterLabel = A.one('.alias-' + dossierPartId + '-' + index);
				 
			if(counterLabel){
				counterLabel.text(1);
			}
		}
	});
</aui:script>
