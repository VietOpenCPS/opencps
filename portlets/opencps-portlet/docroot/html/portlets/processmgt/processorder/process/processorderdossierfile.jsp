
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
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
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.impl.DossierPartImpl"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Map"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>


<%@ include file="../../init.jsp"%>

<portlet:renderURL var="updateDossierFileURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "upload_dossier_file.jsp" %>'/>
</portlet:renderURL>

<portlet:actionURL var="deleteTempFileURL" name="deleteTempFile">
	<portlet:param name="fileEntryId" value="<%=String.valueOf(12345) %>"/>
</portlet:actionURL>

<%
	ProcessOrder processOrder = (ProcessOrder) request.getAttribute(WebKeys.PROCESS_ORDER);

	ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.fetchServiceInfo(processOrder.getServiceInfoId());

	ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil.fetchByF_serviceInfoId(themeDisplay.getScopeGroupId(), serviceInfo.getServiceinfoId());
	
	DossierTemplate dossierTemplate =
		    DossierTemplateLocalServiceUtil
		        .fetchDossierTemplate(serviceConfig
		            .getDossierTemplateId());
	
	String cmd = ParamUtil.getString(request, Constants.CMD);
	
	System.out.println(deleteTempFileURL.toString());
	
	String privateDossierGroup = StringPool.BLANK;
	
	List<DossierPart> dossierPartsLevel1 = new ArrayList<DossierPart>();
	
	if(dossierTemplate != null){
		try{
			dossierPartsLevel1 = DossierPartLocalServiceUtil.getDossierPartsByT_P(dossierTemplate.getDossierTemplateId(), 0);
		}catch(Exception e){}
	}
	
	HashMap<Integer, List<DossierPart>> hashMap = new LinkedHashMap<Integer, List<DossierPart>>();
	
	if(dossierPartsLevel1 != null){
		for(DossierPart dossierPart : dossierPartsLevel1){
			List<DossierPart> dossierPartTree = DossierMgtUtil.getTreeDossierPart(dossierPart.getDossierpartId());
			if(!dossierPartTree.isEmpty()){
				hashMap.put(dossierPart.getPartType(), dossierPartTree);
			}
		}
	}
	
	int index = 0; 
	
	for (Map.Entry<Integer, List<DossierPart>> entry : hashMap.entrySet()){
		
		int key = entry.getKey();
		
		List<DossierPart> dossierParts = entry.getValue();
		
		if(dossierParts != null){
			%>
			<div class="opencps dossiermgt dossier-part-tree" id='<%= renderResponse.getNamespace() + "tree" + dossierParts.get(0).getDossierpartId()%>'>
				<c:choose>
					<c:when test="<%=key == PortletConstants.DOSSIER_PART_TYPE_COMPONEMT ||
							key == PortletConstants.DOSSIER_PART_TYPE_SUBMIT || 
							key == PortletConstants.DOSSIER_PART_TYPE_OTHER %>">
						<%
						for(DossierPart dossierPart : dossierParts){
							
							int level = 1;
							
							String treeIndex = dossierPart.getTreeIndex();
							
							if(Validator.isNotNull(treeIndex)){
								level = StringUtil.count(treeIndex, StringPool.PERIOD);
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
										<span class="opencps dossiermgt dossier-part-name">
											<%=dossierPart.getPartName() %>
										</span>
									</span>
								
									<span class="opencps dossiermgt dossier-part-control">
										<liferay-util:include 
											page="<%= templatePath + \"dossier_file_controls_file.jsp\" %>" 
											servletContext="<%=application %>"
										>
											<portlet:param 
												name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
												value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
											/>
											<portlet:param name="index" value="<%=String.valueOf(index) %>"/>
											<portlet:param name="level" value="<%=String.valueOf(level) %>"/>
											<portlet:param name="groupName" value="<%=StringPool.BLANK%>"/>
											<portlet:param name="partType" value="<%=String.valueOf(dossierPart.getPartType()) %>"/>
										</liferay-util:include>
									</span>
								</div>
							<%
							index++;
						}
						%>
					</c:when>
					
					<c:when test="<%=key == PortletConstants.DOSSIER_PART_TYPE_PRIVATE%>">
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
												page="<%= templatePath + \"dossier_file_controls_file.jsp\" %>" 
												servletContext="<%=application %>"
											>
												<portlet:param 
													name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
													value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
												/>
												<portlet:param name="index" value="<%=String.valueOf(index) %>"/>
												<portlet:param name="level" value="<%=String.valueOf(level) %>"/>
												<portlet:param name="groupName" value="<%=dossierParts.get(0).getPartName() %>"/>
												<portlet:param name="partType" value="<%=String.valueOf(dossierPart.getPartType()) %>"/>
											</liferay-util:include>
										</span>
									</div>
									
								<%
								index++;
							}
							%>
						</div>
					</c:when>
					<c:when test="<%=key == PortletConstants.DOSSIER_PART_TYPE_RESULT%>">
						<%
						for(DossierPart dossierPart : dossierParts){
							
							int level = 1;
							
							String treeIndex = dossierPart.getTreeIndex();
							
							if(Validator.isNotNull(treeIndex)){
								level = StringUtil.count(treeIndex, StringPool.PERIOD);
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
										<span class="opencps dossiermgt dossier-part-name">
											<%=dossierPart.getPartName() %>
										</span>
									</span>
								
									<span class="opencps dossiermgt dossier-part-control">
										<liferay-util:include 
											page="<%= templatePath + \"dossier_file_controls_file.jsp\" %>" 
											servletContext="<%=application %>"
										>
											<portlet:param 
												name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
												value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
											/>
											<portlet:param name="index" value="<%=String.valueOf(index) %>"/>
											<portlet:param name="level" value="<%=String.valueOf(level) %>"/>
											<portlet:param name="groupName" value="<%=StringPool.BLANK%>"/>
											<portlet:param name="partType" value="<%=String.valueOf(dossierPart.getPartType()) %>"/>
										</liferay-util:include>
									</span>
								</div>
							<%
							index++;
						}
						%>
					</c:when>
				</c:choose>
				
			</div>
			
		<%
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
	
	
	Liferay.provide(window, '<portlet:namespace/>removeFileUpload', function(e) {
		if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-dossier-file") %>')){
			var A = AUI();
			
			var instance = A.one(e);
			
			var dossierPartId = instance.attr('dossier-part');
			
			var index = instance.attr('index');
			
			var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
			
			var dossierFileData = A.one('#<portlet:namespace/>dossierFileData' + dossierPartId + '-' + index);
			
			var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
			
			if(fileUpload && parseInt(fileUpload.val()) > 0){
				var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
				portletURL.setParameter("javax.portlet.action", "deleteTempFile");
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
		
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", '<%= templatePath + "dynamic_form.jsp" %>');
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("dossierPartId", dossierPartId);
		portletURL.setParameter("index", index);
		portletURL.setParameter("groupName", groupName);

		<portlet:namespace/>openDossierDialog(portletURL.toString(), '<portlet:namespace />dynamicForm','<%= UnicodeLanguageUtil.get(pageContext, "declaration-online") %>');
	});

	
	Liferay.provide(window, '<portlet:namespace/>viewFile', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var dossierPartId = instance.attr('dossier-part');
		
		var index = instance.attr('index');
		
		var groupName = instance.attr('group-name');
		
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", '<%= templatePath + "view_pdf_idFile.jsp" %>');
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("dossierPartId", dossierPartId);
		portletURL.setParameter("index", index);
		portletURL.setParameter("groupName", groupName);

		<portlet:namespace/>openDossierDialog(portletURL.toString(), '<portlet:namespace />dossierFileId','<%= UnicodeLanguageUtil.get(pageContext, "upload-dossier-file") %>');
	});

	Liferay.provide(window, '<portlet:namespace/>openDossierDialog', function(uri, id, title) {
		var dossierFileDialog = Liferay.Util.openWindow(
			{
				dialog: {
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
	
	Liferay.on('getUploadDataSchema',function(event) {
		
		var A = AUI();
		 
		var schema = event.responseData;
		
		if(schema){
			
			var index = schema.index;
			
			var dossierPartId = schema.dossierPartId;
			
			var fileEntryId = schema.fileEntryId;
			
			var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
			
			var uploadDataSchema = A.one('#<portlet:namespace/>uploadDataSchema' + dossierPartId + '-' + index);
			
			var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
				 
			if(uploadDataSchema){
				
				uploadDataSchema.val(JSON.stringify(schema));
				
				rowcheck.replaceClass('fa-square-o', 'fa-check-square-o');
			}
			
			if(fileUpload && parseInt(fileEntryId) > 0){
				tempFileEntryIds.push(fileEntryId);
				fileUpload.val(fileEntryId);
			}
				 
			var counterLabel = A.one('.alias-' + dossierPartId + '-' + index);
				 
			if(counterLabel){
				counterLabel.text(1);
			}
		}
	});
</aui:script>