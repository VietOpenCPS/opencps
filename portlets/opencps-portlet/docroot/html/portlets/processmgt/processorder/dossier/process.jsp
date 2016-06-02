
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>

<%@page import="org.opencps.backend.util.BackendUtils"%>
<%@page import="org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.WorkflowOutput"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
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
<%@page import="org.opencps.dossiermgt.service.FileGroupLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.FileGroup"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.processmgt.service.ActionHistoryLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ActionHistory"%>
<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>

<%@ include file="../../init.jsp"%>

<portlet:renderURL var="updateDossierFileURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "upload_dossier_file.jsp" %>'/>
</portlet:renderURL>

<portlet:actionURL var="deleteTempFileURL" name="deleteTempFile">
	<portlet:param name="fileEntryId" value="<%=String.valueOf(12345) %>"/>
</portlet:actionURL>

<%
	ProcessOrder processOrder = (ProcessOrder)request.getAttribute(WebKeys.PROCESS_ORDER_ENTRY);
	ProcessStep processStep = (ProcessStep)request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);
	FileGroup fileGroup = (FileGroup) request.getAttribute(WebKeys.FILE_GROUP_ENTRY);
	Dossier dossier = (Dossier)request.getAttribute(WebKeys.DOSSIER_ENTRY);
	ServiceProcess serviceProcess = (ServiceProcess)request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	ServiceInfo serviceInfo = (ServiceInfo)request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	ServiceConfig serviceConfig = (ServiceConfig)request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
	ProcessWorkflow processWorkflow = (ProcessWorkflow) request.getAttribute(WebKeys.PROCESS_WORKFLOW_ENTRY);
	
	ActionHistory latestWorkflowActionHistory = null;
	
	try{
		if(processWorkflow != null){
			
			latestWorkflowActionHistory = ActionHistoryLocalServiceUtil.
					getLatestActionHistory(processOrder.getProcessOrderId(), processOrder.getProcessWorkflowId());
		}
	}catch(Exception e){}
	
	
	List<ProcessWorkflow> postProcessWorkflows = new ArrayList<ProcessWorkflow>();
	
	try{
		postProcessWorkflows = ProcessWorkflowLocalServiceUtil.getPostProcessWorkflow(processOrder.getServiceProcessId(), processWorkflow.getPostProcessStepId());
	}catch(Exception e){}
	
	List<WorkflowOutput> workflowOutputs = null;
	
	if(processWorkflow != null){
		try{
			workflowOutputs = WorkflowOutputLocalServiceUtil.getByProcessWF(processWorkflow.getProcessWorkflowId());
			
		}catch(Exception e){
			//Nothing todo
		}
	}
	
%>

<table class="process-workflow-info">
  <tr class="odd">
    <td width="20%"><liferay-ui:message key="step-name"/></td>
    <td width="30%"><%=processStep != null ? processStep.getStepName() : StringPool.BLANK %></td>
    <td width="20%"><liferay-ui:message key="assign-to-user"/></td>
    <td width="30%"><%=processOrder != null ? new ProcessOrderBean().getAssignToUserName(processOrder.getAssignToUserId()) : StringPool.BLANK %></td>
  </tr>
  
  <tr class="even">
    <td width="20%"><liferay-ui:message key="pre-action-user"/></td>
    <td width="30%"><%=latestWorkflowActionHistory != null ? new ProcessOrderBean().getAssignToUserName(latestWorkflowActionHistory.getActionUserId()) : StringPool.BLANK %></td>
    <td width="20%"><liferay-ui:message key="pre-action-date"/></td>
    <td width="30%"><%=latestWorkflowActionHistory != null ? DateTimeUtil.convertDateToString(latestWorkflowActionHistory.getActionDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : StringPool.BLANK %></td>
  </tr>
  
  
  <tr class="odd">
    <td width="20%"><liferay-ui:message key="pre-action"/></td>
    <td width="80%" colspan="3"><%=processStep != null ? processStep.getStepName() : StringPool.BLANK %></td>
  </tr>
  
  <tr class="even">
    <td width="20%"><liferay-ui:message key="pre-action-note"/></td>
  	<td width="80%" colspan="3"><%=latestWorkflowActionHistory != null ? latestWorkflowActionHistory.getActionNote() : StringPool.BLANK %></td>
  </tr>
</table>

<hr class="canbo-xuly-hoso">

<%-- <aui:row cssClass="process-workflow-info-row">
	<aui:col width="20">
		<liferay-ui:message key="step-name"/>
	</aui:col>
	<aui:col width="30">
		<%=processStep != null ? processStep.getStepName() : StringPool.BLANK %>
	</aui:col>
	<aui:col width="20">
		<liferay-ui:message key="assign-to-user"/>
	</aui:col>
	
	<aui:col width="30">
		<%=processOrder != null ? new ProcessOrderBean().getAssignToUserName(processOrder.getAssignToUserId()) : StringPool.BLANK %>
	</aui:col>
</aui:row>

<aui:row cssClass="process-workflow-info-row">
	<aui:col width="20">
		<liferay-ui:message key="pre-action-user"/>
	</aui:col>
	<aui:col width="30">
		<%=latestWorkflowActionHistory != null ? new ProcessOrderBean().getAssignToUserName(latestWorkflowActionHistory.getActionUserId()) : StringPool.BLANK %>
	</aui:col>
	<aui:col width="20">
		<liferay-ui:message key="pre-action-date"/>
	</aui:col>
	
	<aui:col width="30">
		<%=latestWorkflowActionHistory != null ? DateTimeUtil.convertDateToString(latestWorkflowActionHistory.getActionDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : StringPool.BLANK %>
	</aui:col>
</aui:row>

<aui:row cssClass="process-workflow-info-row">
	<aui:col width="20">
		<liferay-ui:message key="pre-action"/>
	</aui:col>
	<aui:col width="80">
		<%=latestWorkflowActionHistory.getActionName() %>
	</aui:col>
</aui:row>

<aui:row cssClass="process-workflow-info-row">
	<aui:col width="20">
		<liferay-ui:message key="pre-action-note"/>
	</aui:col>
	<aui:col width="80">
		<%=latestWorkflowActionHistory != null ? latestWorkflowActionHistory.getActionNote() : StringPool.BLANK %>
	</aui:col>
</aui:row> --%>

<%

	List<DossierPart> dossierPartsLevel1 = new ArrayList<DossierPart>();
	
	if(dossierTemplate != null){
		try{
			dossierPartsLevel1 = DossierPartLocalServiceUtil.getDossierPartsByT_P_PT(dossierTemplate.getDossierTemplateId(), 0, PortletConstants.DOSSIER_PART_TYPE_RESULT);
		}catch(Exception e){}
	}
	
	int index = 0; 
	
	if(dossierPartsLevel1 != null){
		for (DossierPart dossierPartLevel1 : dossierPartsLevel1){
			
			int partType = dossierPartLevel1.getPartType();
		
			List<DossierPart> dossierParts = DossierMgtUtil.getTreeDossierPart(dossierPartLevel1.getDossierpartId());
			
			if(dossierParts != null){
				%>
				<div class="opencps dossiermgt dossier-part-tree" id='<%= renderResponse.getNamespace() + "tree" + dossierParts.get(0).getDossierpartId()%>'>
					<c:choose>
						<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_RESULT%>">
							<%
							for(DossierPart dossierPart : dossierParts){
								
								//Toi uu thuat toan tim kiem sau
								boolean hasProcecssOrderResul = true;
								if(workflowOutputs != null){
									for(WorkflowOutput workflowOutput : workflowOutputs){
										if(workflowOutput.getDossierPartId() == dossierPart.getDossierpartId()){
											hasProcecssOrderResul = true;
											break;
										}
									}
								}
								
								if(hasProcecssOrderResul){
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
											<%
											dossierFile = DossierFileLocalServiceUtil.getDossierFileByD_P(processOrder.getDossierId(), dossierPart.getDossierpartId());
											%>
												<liferay-util:include 
													page="/html/common/dossiers/controls.jsp" 
													servletContext="<%=application %>"
												>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
														value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
													/>
													<portlet:param 
														name="<%=ProcessOrderDisplayTerms.PROCESS_ORDER_ID %>" 
														value="<%=String.valueOf(processOrder != null ? processOrder.getProcessOrderId() : 0) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" 
														value="<%=String.valueOf(dossierFile != null ? dossierFile.getDossierFileId() : 0) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.PART_TYPE %>" 
														value="<%=String.valueOf(dossierPart.getPartType()) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.FILE_ENTRY_ID %>" 
														value="<%=String.valueOf(dossierFile != null ? dossierFile.getFileEntryId() : 0) %>"
													/>
												</liferay-util:include>
											</span>
										</div>
									<%
									index++;
								}
							}
							%>
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


<aui:input 
	name="<%=ProcessOrderDisplayTerms.DOSSIER_ID %>" 
	value="<%=dossier != null ? dossier.getDossierId() : 0 %>" 
	type="hidden"
/>

<aui:input 
	name="<%=ProcessOrderDisplayTerms.PROCESS_ORDER_ID %>" 
	value="<%=processOrder != null ? processOrder.getProcessOrderId() : 0 %>" 
	type="hidden"
/>
<aui:input 
	name="<%=ProcessOrderDisplayTerms.ACTION_USER_ID %>" 
	value="<%=user != null ? user.getUserId() : 0 %>" 
	type="hidden"
/>

<aui:input 
	name="<%=DossierDisplayTerms.RECEPTION_NO %>" 
	value="<%=dossier != null && Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : 0 %>" 
	type="hidden"
/>

<aui:input 
	name="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME %>" 
	
	type="hidden"
/>

<aui:input 
	name="<%=ProcessOrderDisplayTerms.FILE_GROUP_ID %>" 
	value="<%=fileGroup != null ? fileGroup.getFileGroupId() : 0 %>" 
	type="hidden"
/>

<aui:row cssClass="process-workflow-action">
	<%
		if(postProcessWorkflows != null && !postProcessWorkflows.isEmpty()){
			for(ProcessWorkflow postProcessWorkflow : postProcessWorkflows){
				String preCondition = Validator.isNotNull(postProcessWorkflow.getPreCondition()) ? 
					postProcessWorkflow.getPreCondition() : StringPool.BLANK;
					
					boolean showButton = true;
					
					if(preCondition.contains(WebKeys.PRE_CONDITION_PAYOK)){
						if(!BackendUtils.checkPaymentStatus(dossier.getDossierId())){
							showButton = false;
						}
					}
					
				%>
					<c:if test="<%=showButton %>">
						<aui:button 
							type="button"
							name="<%=String.valueOf(postProcessWorkflow.getProcessWorkflowId()) %>"
							value="<%=postProcessWorkflow.getActionName() %>"
							process-workflow="<%=String.valueOf(postProcessWorkflow.getProcessWorkflowId()) %>"
							service-process="<%=String.valueOf(postProcessWorkflow.getServiceProcessId()) %>"
							process-step="<%=String.valueOf(postProcessWorkflow.getPostProcessStepId()) %>"
							auto-event="<%=Validator.isNotNull(postProcessWorkflow.getAutoEvent()) ? postProcessWorkflow.getAutoEvent() : StringPool.BLANK %>"
							onClick='<%=renderResponse.getNamespace() +  "assignToUser(this)"%>'
						/>
					</c:if>
				<%
			}
		}
	%>
</aui:row>


<aui:script use="aui-base,liferay-portlet-url,aui-io">

	Liferay.provide(window, '<portlet:namespace/>assignToUser', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var processWorkflowId = instance.attr('process-workflow');
		
		var serviceProcessId = instance.attr('service-process')
		
		var processStepId = instance.attr('process-step')
		
		var autoEvent = instance.attr('auto-event');
		
		var dossierId = A.one('#<portlet:namespace/>dossierId').val();
		
		var processOrderId = A.one('#<portlet:namespace/>processOrderId').val();
		
		var actionUserId = A.one('#<portlet:namespace/>actionUserId').val();
		
		var fileGroupId = A.one('#<portlet:namespace/>fileGroupId').val();
		
		var receptionNo = A.one('#<portlet:namespace/>receptionNo').val();
		
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/assign_to_user.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("processWorkflowId", processWorkflowId);
		portletURL.setParameter("serviceProcessId", serviceProcessId);
		portletURL.setParameter("autoEvent", autoEvent);
		portletURL.setParameter("dossierId", dossierId);
		portletURL.setParameter("processStepId", processStepId);
		portletURL.setParameter("processOrderId", processOrderId);
		portletURL.setParameter("actionUserId", actionUserId);
		portletURL.setParameter("fileGroupId", fileGroupId);
		portletURL.setParameter("receptionNo", receptionNo);
		
		<portlet:namespace/>assignDialog(portletURL.toString(), '<portlet:namespace />assignToUser', '<%= UnicodeLanguageUtil.get(pageContext, "handle") %>');
	});
	
	Liferay.provide(window, '<portlet:namespace/>removeFileUpload', function(e) {
		if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-dossier-file") %>')){
			var A = AUI();
			
			var instance = A.one(e);
			
			var dossierFileId = instance.attr('dossier-file');
			
			if(parseInt(dossierFileId) > 0){
				var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');

				portletURL.setParameter("javax.portlet.action", "deleteAttachmentFile");
				portletURL.setParameter("dossierFileId", dossierFileId);
				portletURL.setPortletMode("view");
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
										Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_<%= WebKeys.PROCESS_ORDER_PORTLET %>_');
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
		
		var processOrderId = instance.attr('process-order');
		
		var dossierFileId = instance.attr('dossier-file');
		var dossierId = A.one('#<portlet:namespace/>dossierId').val();
		var dossierPartId = instance.attr('dossier-part');
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/dynamic_form.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("processOrderId", processOrderId);
		portletURL.setParameter("dossierFileId", dossierFileId);
		portletURL.setParameter("dossierId", dossierId);
		portletURL.setParameter("dossierPartId", dossierPartId);
		
		<portlet:namespace/>openDossierDialog(portletURL.toString(), '<portlet:namespace />dynamicForm','<%= UnicodeLanguageUtil.get(pageContext, "declaration-online") %>');
	});

	
	Liferay.provide(window, '<portlet:namespace/>uploadFile', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var processOrderId = instance.attr('process-order');
		
		var dossierFileId = instance.attr('dossier-file');
		
		var dossierPartId = instance.attr('dossier-part');
		
		var fileEntryId = instance.attr('file-entry');
		
		if(parseInt(fileEntryId) > 0){
			alert('<%=UnicodeLanguageUtil.get(pageContext, "remove-old-file-before-upload")%>');
			return;
		}
		
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/upload_dossier_file.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		portletURL.setParameter("processOrderId", processOrderId);
		portletURL.setParameter("dossierFileId", dossierFileId);
		portletURL.setParameter("dossierPartId", dossierPartId);
		<portlet:namespace/>openDossierDialog(portletURL.toString(), '<portlet:namespace />dossierFileId','<%= UnicodeLanguageUtil.get(pageContext, "upload-file-result") %>');
	});

	Liferay.provide(window, '<portlet:namespace/>openDossierDialog', function(uri, id, title) {
		var dossierFileDialog = Liferay.Util.openWindow(
			{
				dialog: {
					cache: false,
					cssClass: 'opencps-dossiermgt-upload-dossier-file',
					modal: true,
					
				},
				cache: false,
				id: id,
				title: title,
				uri: uri
				
			},function(evt){
				
			}
		);
	});
	
	Liferay.provide(window, '<portlet:namespace/>assignDialog', function(uri, id, title) {
		var dossierFileDialog = Liferay.Util.openWindow(
			{
				dialog: {
					cache: false,
					cssClass: 'opencps-processmgt-assign',
					modal: true,
					
				},
				cache: false,
				id: id,
				title: title,
				uri: uri
				
			},function(evt){
				
			}
		);
	});
	
	
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
			
			var displayName = schema.displayName;
			
			var dossierPartId = schema.dossierPartId;
			
			var level = schema.level;
			
			var fileEntryId = schema.fileEntryId;
			
			var dossierFileId = schema.dossierFileId;
			
			var partType = schema.partType;
			
			if(partType == '<%=PortletConstants.DOSSIER_PART_TYPE_OTHER%>' && parseInt(level) == 0){
				var index = parseInt(A.one('#<portlet:namespace/>curIndex').val()) + 1;
				var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/common/dossiers/render_other_dossier_part.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.EXCLUSIVE.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("dossierPartId", dossierPartId);
					portletURL.setParameter("index", index);
					portletURL.setParameter("displayName", displayName);
				
				A.io.request(
					portletURL.toString(),
					{
						on: {
							success: function(event, id, obj) {
								var response = this.get('responseData');
								
								var tree = A.one('#<portlet:namespace />tree' + dossierPartId);
								
								if(tree){
									tree.append(response);
								}
								
								A.one('#<portlet:namespace/>curIndex').val(index);
								
								<portlet:namespace/>rePaintStatus(schema, dossierPartId, dossierFileId, fileEntryId, index);
							}
						}
					}
				);
			}
			
	
			if(partType != '<%=PortletConstants.DOSSIER_PART_TYPE_OTHER%>'){
				
				<portlet:namespace/>rePaintStatus(schema, dossierPartId, dossierFileId, fileEntryId, index);
			}			
		}
	},['aui-io','liferay-portlet-url']);
	
	Liferay.provide(window, '<portlet:namespace/>rePaintStatus', function(schema, dossierPartId, dossierFileId, fileEntryId, index) {
		var A = AUI();
		
		var uploadDataSchema = A.one('#<portlet:namespace/>uploadDataSchema' + dossierPartId + '-' + index);
		
		var fileUpload = A.one('#<portlet:namespace/>fileUpload' + dossierPartId + '-' + index);
		
		var dossierFile = A.one('#<portlet:namespace/>dossierFile' + dossierPartId + '-' + index);
		
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
		
		if(uploadDataSchema){
			
			var rowcheck = A.one('#rowcheck' + dossierPartId + '-' + index);
			
			if(uploadDataSchema.val() == ''){
				uploadDataSchema.val(JSON.stringify(schema));
			}else{
				var formData = JSON.parse(uploadDataSchema.val());
				schema.formData = formData.formData;
				uploadDataSchema.val(JSON.stringify(schema));
			}
			
			rowcheck.replaceClass('fa-square-o', 'fa-check-square-o');
		}
	});
</aui:script>