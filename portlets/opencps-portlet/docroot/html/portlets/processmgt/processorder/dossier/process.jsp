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

<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.backend.util.BackendUtils"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>
<%@page import="org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.WorkflowOutput"%>
<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.service.ActionHistoryLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ActionHistory"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.FileGroup"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.processmgt.model.ProcessStepDossierPart"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="com.liferay.portal.kernel.process.ProcessUtil"%>

<%@ include file="../../init.jsp"%>

<portlet:renderURL var="updateDossierFileURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "upload_dossier_file.jsp" %>'/>
</portlet:renderURL>

<%
	ProcessOrder processOrder =
		(ProcessOrder) request.getAttribute(WebKeys.PROCESS_ORDER_ENTRY);
	ProcessStep processStep =
		(ProcessStep) request.getAttribute(WebKeys.PROCESS_STEP_ENTRY);
	FileGroup fileGroup =
		(FileGroup) request.getAttribute(WebKeys.FILE_GROUP_ENTRY);
	Dossier dossier =
		(Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);
	ServiceProcess serviceProcess =
		(ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	ServiceInfo serviceInfo =
		(ServiceInfo) request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	ServiceConfig serviceConfig =
		(ServiceConfig) request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	DossierTemplate dossierTemplate =
		(DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
	ProcessWorkflow processWorkflow =
		(ProcessWorkflow) request.getAttribute(WebKeys.PROCESS_WORKFLOW_ENTRY);
	
	long processStepId =
			Validator.isNotNull(processStep)
				? processStep.getProcessStepId() : 0l;

	boolean isEditDossier =
		ParamUtil.getBoolean(request, "isEditDossier");
	
	String backURL = ParamUtil.getString(request, "backURL");

	String cssRequired = StringPool.BLANK;

	/* if (accountRoles != null && processStep != null) {
		for (int r = 0; r < accountRoles.size(); r++) {
	try {
		StepAllowance stepAllowance =
			StepAllowanceLocalServiceUtil.getStepAllowance(
				processStep.getProcessStepId(),
				((Role) accountRoles.get(r)).getRoleId());

		if (!stepAllowance.isReadOnly()) {
			isEditDossier = true;
			break;
		}
	}
	catch (Exception e) {
		continue;
	}
		}
	} */

	//Get ActionHistory
	ActionHistory latestWorkflowActionHistory = null;

	try {
		if (processWorkflow != null) {

	latestWorkflowActionHistory =
		ActionHistoryLocalServiceUtil.getLatestActionHistory(
			processOrder.getProcessOrderId(),
			processOrder.getProcessWorkflowId());
		}
	}
	catch (Exception e) {
	}

	//Get list ProcessWorkflow
	List<ProcessWorkflow> postProcessWorkflows =
		new ArrayList<ProcessWorkflow>();

	try {
		postProcessWorkflows =
	ProcessWorkflowLocalServiceUtil.getPostProcessWorkflow(
		processOrder.getServiceProcessId(),
		processWorkflow.getPostProcessStepId());
	}
	catch (Exception e) {
	}

	
	//Get list ProcessStepDossierPart
	List<ProcessStepDossierPart> processStepDossierParts =
		new ArrayList<ProcessStepDossierPart>();

	if (processStepId > 0) {
		processStepDossierParts =
			ProcessUtils.getDossierPartByStep(processStepId);
	}
	
	//Get list DossierPart
	List<DossierPart> dossierParts = new ArrayList<DossierPart>();
	
	if (processStepDossierParts != null) {
		for (ProcessStepDossierPart processStepDossierPart : processStepDossierParts) {
			DossierPart dossierPart = null;
			
			if(processStepDossierPart.getDossierPartId() > 0){
				try{
					dossierPart = DossierPartLocalServiceUtil.getDossierPart(processStepDossierPart.getDossierPartId());
				}catch(Exception e){}
			}
			
			if(dossierPart != null){
				dossierParts.add(dossierPart);
			}
			
		}
	}
%>
<div class="ocps-dossier-process">

	<table class="process-workflow-info">
	  <tr class="odd">
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="step-name"/></td>
	    <td width="30%"><%=processStep != null ? processStep.getStepName() : StringPool.BLANK %></td>
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="assign-to-user"/></td>
	    <td width="30%"><%=processOrder != null ? new ProcessOrderBean().getAssignToUserName(processOrder.getAssignToUserId()) : StringPool.BLANK %></td>
	  </tr>
	  
	  <tr class="even">
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="pre-action-user"/></td>
	    <td width="30%"><%=latestWorkflowActionHistory != null ? new ProcessOrderBean().getAssignToUserName(latestWorkflowActionHistory.getActionUserId()) : StringPool.BLANK %></td>
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="pre-action-date"/></td>
	    <td width="30%"><%=latestWorkflowActionHistory != null ? DateTimeUtil.convertDateToString(latestWorkflowActionHistory.getActionDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : StringPool.BLANK %></td>
	  </tr>
	  
	  
	  <tr class="odd">
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="pre-action"/></td>
	    <td width="80%" colspan="3"><%=latestWorkflowActionHistory != null ? latestWorkflowActionHistory.getActionName() : StringPool.BLANK %></td>
	  </tr>
	  
	  <tr class="even">
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="pre-action-note"/></td>
	  	<td width="80%" colspan="3"><%=latestWorkflowActionHistory != null ? latestWorkflowActionHistory.getActionNote() : StringPool.BLANK %></td>
	  </tr>
	</table>

<%
	if(dossierParts != null){
		
		int index = 0;
		
		for (DossierPart dossierPart : dossierParts){
			
			int partType = dossierPart.getPartType();
			
			%>
                <div class="opencps dossiermgt dossier-part-tree" id='<%= renderResponse.getNamespace() + "tree" + dossierPart.getDossierpartId()%>'>
				    <c:choose>
						<c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_RESULT%>">
							<%
								boolean isDynamicForm = false;
	
								if (Validator.isNotNull(dossierPart.getFormReport()) &&
									Validator.isNotNull(dossierPart.getFormScript())) {
									isDynamicForm = true;
								}
	
								int level = 1;
	
								String treeIndex = dossierPart.getTreeIndex();
	
								if (Validator.isNotNull(treeIndex)) {
									level =
										StringUtil.count(
											treeIndex, StringPool.PERIOD);
								}
	
								DossierFile dossierFile = null;
	
								if (dossier != null) {
									try {
										dossierFile =
											DossierFileLocalServiceUtil.getDossierFileInUse(
												dossier.getDossierId(),
												dossierPart.getDossierpartId());
									}
									catch (Exception e) {
									}
								}
	
								cssRequired =
									dossierPart.getRequired()
										? "cssRequired" : StringPool.BLANK;
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
									<span class="opencps dossiermgt dossier-part-name <%=cssRequired %>">
										<%=dossierPart.getPartName() %>
									</span>
								</span>
							
								<span class="opencps dossiermgt dossier-part-control">
									<liferay-util:include 
										page="/html/common/portlet/dossier_actions.jsp" 
										servletContext="<%=application %>"
									>
										<portlet:param 
											name="<%=DossierDisplayTerms.DOSSIER_ID %>" 
											value="<%=String.valueOf(dossier != null ? dossier.getDossierId() : 0) %>"
										/>
										
										<portlet:param 
											name="isDynamicForm" 
											value="<%=String.valueOf(isDynamicForm) %>"
										/>
										
										<portlet:param 
											name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
											value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.FILE_ENTRY_ID %>" 
											value="<%=String.valueOf(dossierFile != null ? dossierFile.getFileEntryId() : 0) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" 
											value="<%=String.valueOf(dossierFile != null ? dossierFile.getDossierFileId() : 0) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.LEVEL %>" 
											value="<%=String.valueOf(level) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.GROUP_NAME %>" 
											value="<%=StringPool.BLANK%>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.PART_TYPE %>" 
											value="<%=String.valueOf(dossierPart.getPartType()) %>"
										/>
										<portlet:param 
											name="isEditDossier" 
											value="<%=String.valueOf(isEditDossier) %>"
										/>
									</liferay-util:include>
								</span>
							</div>
						</c:when>
					
                        <c:when test="<%=partType == PortletConstants.DOSSIER_PART_TYPE_MULTIPLE_RESULT %>">
						<%
		
							cssRequired = dossierPart.getRequired() ? "cssRequired" : StringPool.BLANK;
								
						%>
							<div 
								id='<%=renderResponse.getNamespace() + "row-" + dossierPart.getDossierpartId() + StringPool.DASH + index %>' 
								index="<%=index %>"
								dossier-part="<%=dossierPart.getDossierpartId() %>"
								class="opencps dossiermgt dossier-part-row"
							>
								<span class='<%="level-0 opencps dossiermgt dossier-part"%>'>
									<span class="row-icon">
										<i class="fa fa-circle" aria-hidden="true"></i>
									</span>
									<span class="opencps dossiermgt dossier-part-name <%=cssRequired %>">
										<%=dossierPart.getPartName() %>
									</span>
								</span>
							
								<span class="opencps dossiermgt dossier-part-control">
									<liferay-util:include 
										page="/html/common/portlet/dossier_actions.jsp" 
										servletContext="<%=application %>"
									>
										<portlet:param 
											name="<%=DossierDisplayTerms.DOSSIER_ID %>" 
											value="<%=String.valueOf(dossier != null ? dossier.getDossierId() : 0) %>"
										/>
										
										<portlet:param 
											name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
											value="<%=String.valueOf(dossierPart.getDossierpartId()) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.FILE_ENTRY_ID %>" 
											value="<%=String.valueOf(0) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" 
											value="<%=String.valueOf(0) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.LEVEL %>" 
											value="<%=String.valueOf(0) %>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.GROUP_NAME %>" 
											value="<%=StringPool.BLANK%>"
										/>
										<portlet:param 
											name="<%=DossierFileDisplayTerms.PART_TYPE %>" 
											value="<%=String.valueOf(dossierPart.getPartType()) %>"
										/>
										<portlet:param 
											name="isEditDossier" 
											value="<%=String.valueOf(isEditDossier) %>"
										/>
									</liferay-util:include>
								</span>
							</div>
						    <%
								List<DossierFile> dossierFiles = DossierFileLocalServiceUtil.
								getDossierFileByDID_DP(dossier.getDossierId(), dossierPart.getDossierpartId());
								
								if(dossierFiles != null){
									for(DossierFile dossierFileOther : dossierFiles){
									index ++;
									%>
										<div class='<%="opencps dossiermgt dossier-part-row r-" + index%>'>
											<span class='<%="level-1 opencps dossiermgt dossier-part"%>'>
												<span class="row-icon">
													<i 
														id='<%="rowcheck" + dossierFileOther.getDossierPartId() + StringPool.DASH + index %>' 
														class='<%=dossierFileOther.getFileEntryId() > 0 ? "fa fa-check-square-o" : "fa fa-square-o" %>' 
														aria-hidden="true"
													>
													</i>
												</span>
												<span class="opencps dossiermgt dossier-part-name">
													<%=dossierFileOther.getDisplayName() %>
												</span>
											</span>
										
											<span class="opencps dossiermgt dossier-part-control">
												<liferay-util:include 
													page="/html/common/portlet/dossier_actions.jsp" 
													servletContext="<%=application %>"
												>
													<portlet:param 
														name="<%=DossierDisplayTerms.DOSSIER_ID %>" 
														value="<%=String.valueOf(dossier != null ? dossier.getDossierId() : 0) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" 
														value="<%=String.valueOf(dossierFileOther.getDossierPartId()) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.FILE_ENTRY_ID %>" 
														value="<%=String.valueOf(dossierFileOther.getFileEntryId()) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" 
														value="<%=String.valueOf(dossierFileOther.getDossierFileId()) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.LEVEL %>" 
														value="<%=String.valueOf(1) %>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.GROUP_NAME %>" 
														value="<%=StringPool.BLANK%>"
													/>
													<portlet:param 
														name="<%=DossierFileDisplayTerms.PART_TYPE %>" 
														value="<%=String.valueOf(partType) %>"
													/>
													<portlet:param 
														name="isEditDossier" 
														value="<%=String.valueOf(isEditDossier) %>"
													/>
												</liferay-util:include>
											</span>
										</div>
									<%
								}
							}	
						%>
					</c:when>
				</c:choose>
			</div>
			<%
			index++;
		}
	}
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
	name="<%=ProcessOrderDisplayTerms.ESTIMATE_DATE %>" 
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
					showButton = BackendUtils.checkPreCondition(preCondition, dossier.getDossierId());
					
					//Kiem tra neu co su kien auto event thi khong hien thi nut
					/* showButton = Validator.isNotNull(postProcessWorkflow.getAutoEvent()) ? false : true; */
		
				%>
					<c:if test="<%= showButton %>">
						<aui:button 
							type="button"
							name="<%=String.valueOf(postProcessWorkflow.getProcessWorkflowId()) %>"
							value="<%=postProcessWorkflow.getActionName() %>"
							process-workflow="<%=String.valueOf(postProcessWorkflow.getProcessWorkflowId()) %>"
							service-process="<%=String.valueOf(postProcessWorkflow.getServiceProcessId()) %>"
							process-step="<%=String.valueOf(postProcessWorkflow.getPostProcessStepId()) %>"
							deadline-pattern="<%=postProcessWorkflow.getDeadlinePattern() %>"
							auto-event="<%=Validator.isNotNull(postProcessWorkflow.getAutoEvent()) ? postProcessWorkflow.getAutoEvent() : StringPool.BLANK %>"
							receive-date="<%=Validator.isNotNull(processOrder.getActionDatetime()) ? DateTimeUtil.convertDateToString(processOrder.getActionDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : StringPool.BLANK %>"
							onClick='<%=renderResponse.getNamespace() +  "assignToUser(this)"%>'
							disabled="<%=!isEditDossier %>"
						/>
					</c:if>
				<%
			}
		}
	%>
</aui:row>

</div>
<aui:script use="aui-base,liferay-portlet-url,aui-io">

	Liferay.provide(window, '<portlet:namespace/>assignToUser', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var processWorkflowId = instance.attr('process-workflow');
		
		var serviceProcessId = instance.attr('service-process')
		
		var processStepId = instance.attr('process-step')
		
		var autoEvent = instance.attr('auto-event');
		
		var receiveDate = instance.attr('receive-date');
		
		var deadlinePattern = instance.attr('deadline-pattern');
		
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
		portletURL.setParameter("receiveDate", receiveDate);
		portletURL.setParameter("deadlinePattern", deadlinePattern);
		portletURL.setParameter("backURL", '<%=backURL%>');
	
		openDialog(portletURL.toString(), '<portlet:namespace />assignToUser', '<%= UnicodeLanguageUtil.get(pageContext, "handle") %>');
	});
	
AUI().ready('aui-base','liferay-portlet-url','aui-io', function(A){
		
		//Upload buttons
		var uploadDossierFiles = A.all('.upload-dossier-file');
		
		if(uploadDossierFiles){
			uploadDossierFiles.each(function(e){
				e.on('click', function(){
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/modal_dialog.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("content", "upload-file");
					uploadDossierFile(this, portletURL.toString(), '<portlet:namespace/>');
				});
			});
		}
		
		//View attachment buttons
		var viewAttachments = A.all('.view-attachment');
		
		if(viewAttachments){
			viewAttachments.each(function(e){
				e.on('click', function(){
					var instance = A.one(e);
					var dossierFileId = instance.attr('dossier-file');
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
					portletURL.setParameter("javax.portlet.action", "previewAttachmentFile");
					portletURL.setParameter("dossierFileId", dossierFileId);
					portletURL.setPortletMode("view");
					portletURL.setWindowState('<%=WindowState.NORMAL%>');
					
					viewDossierAttachment(this, portletURL.toString());
				});
			});
		}
		
		//Remove buttons
		var removeDossierFiles = A.all('.remove-dossier-file');
		
		if(removeDossierFiles){
			removeDossierFiles.each(function(e){
				e.on('click', function(){
					if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-dossier-file") %>')){
						
						var instance = A.one(this);
						
						var dossierFileId = instance.attr('dossier-file');
						
						if(parseInt(dossierFileId) > 0){
							var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
							portletURL.setParameter("javax.portlet.action", "removeAttachmentFile");
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
													var data = {
														'conserveHash': true
													};
													Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_<%= WebKeys.PROCESS_ORDER_PORTLET %>_', data);
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
			});	
		}
		
		//Add individual part buttons
		var addIndividualPartGroups = A.all('.add-individual-part-group');
		
		if(addIndividualPartGroups){
			addIndividualPartGroups.each(function(e){
				e.on('click', function(){
					var instance = A.one(e);
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/modal_dialog.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("content", "individual");
					addIndividualPartGroup(this, portletURL.toString(), '<portlet:namespace/>');
				});
			});
		}
		
		//Remove dossier group
		
		var removeIndividualGroups = A.all('.remove-individual-group');
		
		if(removeIndividualGroups){
			removeIndividualGroups.each(function(e){
				e.on('click', function(){
					if(confirm('<%= UnicodeLanguageUtil.get(pageContext, "are-you-sure-remove-individual-group") %>')){
						
						var instance = A.one(this);
						
						var fileGroupId = instance.attr('file-group');
						var dossierId = instance.attr('dossier');
						var dossierPartId = instance.attr('dossier-part');
						
						if(parseInt(fileGroupId) > 0){
							var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
							portletURL.setParameter("javax.portlet.action", "removeIndividualGroup");
							portletURL.setParameter("fileGroupId", fileGroupId);
							portletURL.setParameter("dossierId", dossierId);
							portletURL.setParameter("dossierPartId", dossierPartId);
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
													var data = {
														'conserveHash': true
													};

													Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_<%= WebKeys.PROCESS_ORDER_PORTLET %>_', data);
												}else{
													alert('<%= UnicodeLanguageUtil.get(pageContext, "error-while-remove-this-group") %>');
												}
											}
										}
									}
								}
							);
						}
					}
				});
			});
		}
		
		//Declare online
		var declarationOnlines = A.all('.declaration-online');
		
		if(declarationOnlines){
			declarationOnlines.each(function(e){
				e.on('click', function(){
					var instance = A.one(e);
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/modal_dialog.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("content", "declaration-online");
					dynamicForm(this, portletURL.toString(), '<portlet:namespace/>');
				});
			});
		}
		
		//View form
		var viewForms = A.all('.view-form');
		
		if(viewForms){
			viewForms.each(function(e){
				e.on('click', function(){
					var instance = A.one(e);
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/modal_dialog.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("content", "declaration-online");
					dynamicForm(this, portletURL.toString(), '<portlet:namespace/>');
				});
			});
		}
		
		//View form
		var viewVersions = A.all('.view-version');
		
		if(viewVersions){
			viewVersions.each(function(e){
				e.on('click', function(){
				
					var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
					portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/modal_dialog.jsp");
					portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
					portletURL.setPortletMode("normal");
					portletURL.setParameter("content", "view-version");
					viewVersion(this, portletURL.toString(), '<portlet:namespace/>');
				});
			});
		}
	});
	
	Liferay.on('redirect',function(event) {
		
		var backURL = event.responseData.backURL;
		
		if(backURL){
	
			window.location = backURL;
		}
	});

</aui:script>
