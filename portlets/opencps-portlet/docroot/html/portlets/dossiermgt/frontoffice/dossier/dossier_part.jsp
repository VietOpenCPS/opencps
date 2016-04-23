
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

<%@ include file="../../init.jsp"%>

<portlet:renderURL var="updateDossierFileURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "upload_dossier_file.jsp" %>'/>
</portlet:renderURL>

<%
	HashMap<Integer, List<DossierPart>> hashMap = new LinkedHashMap<Integer, List<DossierPart>>();
	
	List<DossierPart> dossierParts1 = new ArrayList<DossierPart>();
	DossierPart dossierPart1 = new DossierPartImpl();
	dossierPart1.setDossierpartId(1);
	dossierPart1.setPartType(1);
	dossierPart1.setPartName("dossierPart1");
	dossierPart1.setTreeIndex("1");
	
	dossierParts1.add(dossierPart1);
	
	hashMap.put(1, dossierParts1);
	
	
	List<DossierPart> dossierParts2 = new ArrayList<DossierPart>();
	DossierPart dossierPart2 = new DossierPartImpl();
	dossierPart2.setDossierpartId(2);
	dossierPart2.setPartType(2);
	dossierPart2.setPartName("dossierPart2");
	dossierPart2.setTreeIndex("2");
	dossierParts2.add(dossierPart2);
	
	
	DossierPart dossierPart21 = new DossierPartImpl();
	dossierPart21.setDossierpartId(21);
	dossierPart21.setPartType(2);
	dossierPart21.setPartName("dossierPart21");
	dossierPart21.setTreeIndex("2.1");
	
	dossierParts2.add(dossierPart21);
	
	hashMap.put(2, dossierParts2);
	
	List<DossierPart> dossierParts3 = new ArrayList<DossierPart>();
	DossierPart dossierPart3 = new DossierPartImpl();
	dossierPart3.setDossierpartId(3);
	dossierPart3.setPartType(4);
	dossierPart3.setPartName("dossierPart3");
	dossierPart3.setTreeIndex("3");
	
	dossierParts3.add(dossierPart3);
	
	DossierPart dossierPart31 = new DossierPartImpl();
	dossierPart31.setDossierpartId(31);
	dossierPart31.setPartType(1);
	dossierPart31.setPartName("dossierPart31");
	dossierPart31.setTreeIndex("3.1");
	
	dossierParts3.add(dossierPart31);
	
	DossierPart dossierPart32 = new DossierPartImpl();
	dossierPart32.setDossierpartId(32);
	dossierPart32.setPartType(2);
	dossierPart32.setPartName("dossierPart32");
	dossierPart32.setTreeIndex("3.2");
	
	dossierParts3.add(dossierPart32);
	
	hashMap.put(4, dossierParts3);
	
	
	int index = 0; 
	
	for (Map.Entry<Integer, List<DossierPart>> entry : hashMap.entrySet()){
		
		int key = entry.getKey();
		List<DossierPart> dossierParts = entry.getValue();
		
		if(dossierParts != null){
			%>
			<div class="opencps dossiermgt dossier-part-tree" id='<%= renderResponse.getNamespace() + "tree" + dossierParts.get(0).getDossierpartId()%>'>
				<c:choose>
					<c:when test="<%=key == PortletConstants.DOSSIER_PART_TYPE_COMPONEMT ||
							key == PortletConstants.DOSSIER_PART_TYPE_SUBMIT %>">
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
										<i id='<%="rowcheck" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' class="fa fa-square-o" aria-hidden="true"></i>
										<span class="opencps dossiermgt dossier-part-name">
											<%=dossierPart.getPartName() %>
										</span>
									</span>
								
									<span class="opencps dossiermgt dossier-part-control">
										<liferay-util:include page="/html/portlets/dossiermgt/frontoffice/dossier_file_controls.jsp"  servletContext="<%=application %>">
											<portlet:param name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"/>
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
					</c:when>
					
					<c:when test="<%=key == PortletConstants.DOSSIER_PART_TYPE_PRIVATE%>">
						<div
							id='<%=renderResponse.getNamespace() + "row-" + dossierParts.get(0).getDossierpartId() + StringPool.DASH + index %>' 
							index="<%=index %>"
							dossier-part="<%=dossierParts.get(0).getDossierpartId() %>" 
							class="opencps dossiermgt dossier-part-row root-group"
						>
							<span class='<%="level-0 opencps dossiermgt dossier-part"%>'>
								<i class="fa fa-minus-square-o" aria-hidden="true"></i>
								<span class="opencps dossiermgt dossier-part-name">
									<liferay-ui:message key="private-dossier"/>
								</span>
							</span>
							
							<span class="opencps dossiermgt dossier-part-control">
								<aui:a 
									id="<%=String.valueOf(dossierParts.get(0).getDossierpartId()) %>"
									dossier-part="<%=String.valueOf(dossierParts.get(0).getDossierpartId()) %>"
									index="<%=String.valueOf(index) %>"
									href="javascript:void(0);" 
									label="add-private-dossier" 
									cssClass="opencps dossiermgt part-file-ctr add-private-dossier" 
								/>
								<aui:input id='<%="groupNames" + dossierParts.get(0).getDossierpartId() %>' name="groupNames" type="hidden" value="<%=dossierParts.get(0).getPartName() %>"/>
							</span>
						</div>
						<div id='<%=renderResponse.getNamespace() + "privateDossierPartGroup" + dossierParts.get(0).getDossierpartId() + StringPool.DASH + index%>' class="opencps dossiermgt dossier-part-tree">
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
											<i id='<%="rowcheck" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' class="fa fa-square-o" aria-hidden="true"></i>
											<span class="opencps dossiermgt dossier-part-name">
												<%=dossierPart.getPartName() %>
											</span>
											<%
												if(dossierParts.indexOf(dossierPart) == 0){
													%>
														<aui:input name="<%=dossierPart.getPartName() %>" type="hidden" value="<%=dossierPart.getPartName()%>"/>
													<%
												}
											%>
											
										</span>
									
										<span class="opencps dossiermgt dossier-part-control">
											<liferay-util:include page="/html/portlets/dossiermgt/frontoffice/dossier_file_controls.jsp"  servletContext="<%=application %>">
												<portlet:param name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"/>
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
				</c:choose>
				
			</div>
			<aui:input name="curIndex" type="hidden" value="<%=index %>"/>
		<%
		}
	}

%>
<aui:script>
	
	AUI().ready('aui-base','liferay-portlet-url','aui-io', function(A){
		
		var rootGroups = A.all('.root-group');
		
		if(rootGroups){
			rootGroups.each(function(elm){
				var dossierPartId = A.one(elm).attr('dossier-part');
				var index = parseInt(A.one('#<portlet:namespace/>curIndex').val()) + 1;
				var groupNames = A.one('#<portlet:namespace/>groupNames' + dossierPartId).val();
				groupNames = groupNames.split(',');
				if(groupNames && groupNames.length > 0){
					for(var i = 0; i < groupNames.length; i++){
						var groupName = groupNames[i];
						
						if(groupName != ''){
							var group = A.one('#<portlet:namespace/>' + groupName);
							
							if(group == null){
								<portlet:namespace/>addPrivateDossierGroup(dossierPartId, index, groupName, null);
							}
						}
						
					}
				}
			});
		}
		
		var dossierPartRow = A.all('.dossier-part-row');
		
		//Danh dau tick cac ho so da co file upload
		if(dossierPartRow){
			dossierPartRow.each(function(row){
				
				var dossierPartId = A.one(row).attr('dossier-part');
				
				var index = A.one(row).attr('index');
				
				var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
				
				if(fileUpload && fileUpload.val() != ''){
					var count = fileUpload.val().split(',').length;
					
					if(parseInt(count) > 0){
						
						var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
						
						if(rowcheck){
							rowcheck.replaceClass('fa-square-o', 'fa-check-square-o');
						}
						
						var counterLabel = A.one('.alias-' + dossierPartId + '-' + index);
						 
						if(counterLabel){
							counterLabel.text(count);
						}
					}	
				}
			});
		}
		
		var addPrivateDossierCtrs = A.all('.add-private-dossier');
		
		//Them moi nhom ho so rieng
		if(addPrivateDossierCtrs){
			addPrivateDossierCtrs.each(function(elm){
				elm.on('click', function(){
					var dossierPartId = A.one(elm).attr('dossier-part');
					var index = parseInt(A.one('#<portlet:namespace/>curIndex').val()) + 1;
					var groupNames = A.one('#<portlet:namespace/>groupNames' + dossierPartId).val();
						
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/edit_dossier_part_group.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("dossierPartId", dossierPartId);
					portletURL.setParameter("index", index);
					portletURL.setParameter("groupNames", groupNames);
	
					<portlet:namespace/>openDossierFileDialog(portletURL.toString(), '<portlet:namespace />privateDossierPartGroup', '<%= UnicodeLanguageUtil.get(pageContext, "add-private-dossier") %>');
				});
			});
		}
		
		<portlet:namespace/>initEvent();
	});
	
	Liferay.provide(window, '<portlet:namespace/>initEvent', function() {
		var A = AUI();
		
		var uploadFileCtrs = A.all('.upload-file');
		
		var removeFileCtrs = A.all('.remove-file');
		
		var removeGroupCtrs = A.all('.remove-group');
		
		if(removeGroupCtrs){
			removeGroupCtrs.each(function(elm){
				elm.on('click', function(){
					var dossierPartId = A.one(elm).attr('dossier-part');
					var index = A.one(elm).attr('index');
					var groupName = A.one(elm).attr('groupName');
					var privateDossierPartGroup = A.one('#<portlet:namespace />privateDossierPartGroup' + dossierPartId + '-' + index);
					if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-group") %>')){
						privateDossierPartGroup.remove();
						var groupNames = A.one('#<portlet:namespace/>groupNames' + dossierPartId).val();
						groupNames = groupNames.split(',');
						var index = groupNames.indexOf(groupName);
						if (index > -1) {
							groupNames.splice(index, 1);
							A.one('#<portlet:namespace/>groupNames' + dossierPartId).val(groupNames.toString());
						}
						
					}
				});
			});
		}
		
		//Upload file
		if(uploadFileCtrs){
			uploadFileCtrs.each(function(elm){
				elm.on('click', function(){
					var dossierPartId = A.one(elm).attr('dossier-part');
					var index = A.one(elm).attr('index');
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/upload_dossier_file.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("dossierPartId", dossierPartId);
					portletURL.setParameter("index", index);
	
					<portlet:namespace/>openDossierFileDialog(portletURL.toString(), '<portlet:namespace />dossierFileId','<%= UnicodeLanguageUtil.get(pageContext, "upload-dossier-file") %>');
				});
			});
		}
		
		//Xoa file  upload
		if(removeFileCtrs){
			removeFileCtrs.each(function(elm){
				elm.on('click', function(){
					if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-dossier-file") %>')){
						
						var dossierPartId = A.one(elm).attr('dossier-part');
						
						var index = A.one(elm).attr('index');
						
						var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
						
						var dossierFileData = A.one('#<portlet:namespace/>dossierFileData' + dossierPartId + '-' + index);
						
						var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
						
						var fileIds = '';
						
						if(fileUpload){
							fileIds = fileUpload.val();
							fileUpload.val('');
						}
						
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
					}
				});
			});
		}
	});

	Liferay.provide(window, '<portlet:namespace/>openDossierFileDialog', function(uri, id, title) {
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
	
	Liferay.provide(window, '<portlet:namespace/>addPrivateDossierGroup', function(dossierPartId, index, groupName, groupNames) {
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
							if(groupNames){
								groupNames.push(groupName);
								A.one('#<portlet:namespace/>groupNames' + dossierPartId).val(groupNames.toString());
							}
						}
						
						A.one('#<portlet:namespace/>curIndex').val(index);
						<portlet:namespace/>initEvent();
					}
				}
			}
		);
	},['aui-io','liferay-portlet-url']);
	
	Liferay.on('getPrivateDossierPartGroupData',function(event) {
		var A = AUI();
		var data = event.responseData;
		var groupName = data.groupName;
		var index = data.index;
		var dossierPartId = data.dossierPartId;
		
		var groupNames = A.one('#<portlet:namespace/>groupNames' + dossierPartId).val();
		
		groupNames = groupNames.split(',');
		
		<portlet:namespace/>addPrivateDossierGroup(dossierPartId, index, groupName, groupNames);
		
	},['aui-io','liferay-portlet-url']);
	
	Liferay.on('getUploadDossierFileData',function(event) {
		var A = AUI();
		 
		var data = event.responseData;
		 
		var json = [];
		var fileIds = [];
		 
		if(data.length > 0){
			var index = data[0].index;
			var dossierPartId = data[0].dossierPartId;
			var fileEntryId = data[0].fileEntryId;
			
			var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
			var dossierFileData = A.one('#<portlet:namespace/>dossierFileData' + dossierPartId + '-' + index);
			var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
				 
			if(dossierFileData){
				//cho phep up nhieu file
				/* if(dossierFileData.val() != ''){
					json = JSON.parse(dossierFileData.val());
				} */
					
				json.push(data[0]);
				
				dossierFileData.val(JSON.stringify(json));
				
				rowcheck.replaceClass('fa-square-o', 'fa-check-square-o');
			}
			
			if(fileUpload && parseInt(fileEntryId) > 0){
				//cho phep up nhieu file
				/* if(fileUpload.val() != ''){
					fileIds = fileUpload.val().split(',');
				} */
				
				
				
				fileIds.push(fileEntryId);
				fileUpload.val(fileIds.toString());
			}
				 
			var counterLabel = A.one('.alias-' + dossierPartId + '-' + index);
				 
			if(counterLabel){
				counterLabel.text(json.length);
			}
		}
	});

</aui:script>
