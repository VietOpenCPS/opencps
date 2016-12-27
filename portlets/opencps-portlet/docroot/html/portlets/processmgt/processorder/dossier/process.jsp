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
<%@page import="org.opencps.dossiermgt.service.DossierPartLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>
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
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.MessageKeys"%>

<%@ include file="../init.jsp"%>

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
	
	long dossierId = (Validator.isNotNull(dossier)) ? dossier.getDossierId() : 0L;		
	
	boolean isEditDossier =
		ParamUtil.getBoolean(request, "isEditDossier");
	
	String backURL = ParamUtil.getString(request, "backURL");

	String cssRequired = StringPool.BLANK;


	//Get ActionHistory
	ActionHistory latestWorkflowActionHistory = null;

	try {
		if (processWorkflow != null) {
			latestWorkflowActionHistory 
				= ActionHistoryLocalServiceUtil.getLatestActionHistory(
					processOrder.getProcessOrderId(), 
					processOrder.getProcessWorkflowId(), false);
		}
	} catch (Exception e) {}

	//Get list ProcessWorkflow
	List<ProcessWorkflow> postProcessWorkflows = new ArrayList<ProcessWorkflow>();

	try {
		postProcessWorkflows = ProcessWorkflowLocalServiceUtil.getPostProcessWorkflow(
				processOrder.getServiceProcessId(), processWorkflow.getPostProcessStepId());
	} catch (Exception e) {}

	//Get list ProcessStepDossierPart
	List<ProcessStepDossierPart> processStepDossierParts = new ArrayList<ProcessStepDossierPart>();

	if (processStepId > 0) {
		processStepDossierParts = ProcessUtils.getDossierPartByStep(processStepId);
	}

%>
<div class="ocps-dossier-process">
<aui:row cssClass="header-title custom-title">
	<aui:col width="100">
		<liferay-ui:message key="process"/>
	</aui:col>
</aui:row>
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
	  
	  <%-- <tr class="odd">
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="pre-action"/></td>
	    <td width="80%" colspan="3"><%=latestWorkflowActionHistory != null ? latestWorkflowActionHistory.getActionName() : StringPool.BLANK %></td>
	  </tr> --%>
	  
	  <tr class="even">
	    <td width="20%" class="opcs-dosier-process-key"><liferay-ui:message key="pre-action-note"/></td>
	  	<td width="80%" colspan="3"><%=latestWorkflowActionHistory != null ? latestWorkflowActionHistory.getActionNote() : StringPool.BLANK %></td>
	  </tr>
	</table>

	<%
		int index = 0;
	
		DossierPart dossierPart = null;
		
		if (processStepDossierParts != null) {
			
			for (ProcessStepDossierPart processStepDossierPart : processStepDossierParts){
				
				if (processStepDossierPart.getDossierPartId() > 0) {
					try {
						dossierPart = DossierPartLocalServiceUtil
								.getDossierPart(processStepDossierPart.getDossierPartId());
					} catch (Exception e) {
					}
				}
				
				if (Validator.isNotNull(dossierPart)) {
				
				int partType = dossierPart.getPartType();
				if(postProcessWorkflows != null && !postProcessWorkflows.isEmpty()){
					for(ProcessWorkflow postProcessWorkflow : postProcessWorkflows){
						String preCondition = Validator.isNotNull(postProcessWorkflow.getPreCondition()) ? 
							postProcessWorkflow.getPreCondition() : StringPool.BLANK; 
						
					}
				}
				
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
									class='<%="opencps dossiermgt dossier-part-row r-" + index + StringPool.SPACE + "dpid-" + String.valueOf(dossierPart.getDossierpartId())%>'
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
												name="<%=WebKeys.READ_ONLY %>" 
												value="<%=String.valueOf(processStepDossierPart.getReadOnly()) %>"
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
												name="<%=WebKeys.READ_ONLY %>" 
												value="<%=String.valueOf(processStepDossierPart.getReadOnly()) %>"
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
														name="<%=WebKeys.READ_ONLY %>" 
														value="<%=String.valueOf(processStepDossierPart.getReadOnly()) %>"
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
				}
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
		value="<%=dossier != null && Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.BLANK %>" 
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
						
						boolean showButton = BackendUtils.checkPreCondition(preCondition, dossier.getDossierId()) && 
								(Validator.isNotNull(postProcessWorkflow.getAutoEvent()) ? false : true);
						
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

	<div id = "<portlet:namespace />assignTaskContainer" class="assign-task-container"></div>

</div>

<aui:script use="aui-base,liferay-portlet-url,aui-io,aui-loading-mask-deprecated">

	function validateRequiredResult(dossierId, processStepId, processWorkflowId) {
		
		var A = AUI();

		var actionNote = A.one('#<portlet:namespace />actionNote');
		
		var requiredDossierPartIds = [];
		var requiredActionNote = false;
		var required = false;
		
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
		portletURL.setParameter("javax.portlet.action", "validateAssignTask");
		portletURL.setWindowState('<%=WindowState.NORMAL%>');
		/* var loadingMask = new A.LoadingMask(
			{
				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "validate") %>',
				target: A.one('#<portlet:namespace/>pofm')
			}
		);
		
		loadingMask.show(); */
		
		A.io.request(
			portletURL.toString(),
			{
				dataType : 'text/html',
				method : 'POST',
				sync: true,
			    data:{
			    	<portlet:namespace/>dossierId : dossierId,
			    	<portlet:namespace/>processStepId: processStepId,
			    	<portlet:namespace/>processWorkflowId: processWorkflowId
			    },   
			    on: {
			    	success: function(event, id, obj) {
			    		
			    		var response = this.get('responseData');
			    		
						responseObj = JSON.parse(response);
						
						requiredDossierPartIds = responseObj.arrayDossierpartIds;
						
						requiredActionNote = responseObj.requiedActionNote;
						
						//requiredDossierPartIds = JSON.parse(response);
						
						for(var i = 0; i < requiredDossierPartIds.length; i++){
							var id = requiredDossierPartIds[i];
							
							if(parseInt(id) > 0){
								required = true;
								var row = A.one('.dossier-part-row.dpid-' + id);
								
								if(row){
									row.attr('style', 'color:red');
								}
							}
						}
					},
			    	error: function(){}
				}
			}
		);
		
		//loadingMask.hide();
		
		if (required){
			return 'please-upload-dossier-part-required-before-send';
		}
		if (requiredActionNote == true && actionNote.val() == ''){
			actionNote.addClass('changeDefErr');
			A.one('#<portlet:namespace/>defErrActionNote').addClass('displayDefErr');
			
			return 'please-add-note-before-send';
		} else {
			actionNote.removeClass('changeDefErr');
			A.one('#<portlet:namespace/>defErrActionNote').removeClass('displayDefErr');
		}
		
		return '';
	}
	

	Liferay.provide(window, '<portlet:namespace/>assignToUser', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var assignFormDisplayStyle = '<%= assignFormDisplayStyle %>';
		
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
		portletURL.setPortletMode("normal");
		portletURL.setParameter("processWorkflowId", processWorkflowId);
		portletURL.setParameter("serviceProcessId", serviceProcessId);
		portletURL.setParameter("autoEvent", autoEvent);
		portletURL.setParameter("dossierId", dossierId);
		portletURL.setParameter("processStepId", processStepId);
		portletURL.setParameter("processOrderId", processOrderId);
		portletURL.setParameter("actionUserId", actionUserId);
		portletURL.setParameter("fileGroupId", fileGroupId);
		portletURL.setParameter("deadlinePattern", deadlinePattern);
		//display default - popup
		if(assignFormDisplayStyle == 'popup' ) {
			portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>");
			portletURL.setParameter("backURL", '<%=backURL%>');
			//<portlet:namespace/>validateRequiredResult(dossierId, processStepId, processWorkflowId);
			var msg = validateRequiredResult(dossierId, processStepId, processWorkflowId);
			if(msg != '') {
				alert(Liferay.Language.get(msg));
				return;
			} 
			openDialog(portletURL.toString(), '<portlet:namespace />assignToUser', '<%= UnicodeLanguageUtil.get(pageContext, "handle") %>');
		} 
		// Display assign to user - moit
		else if (assignFormDisplayStyle == 'form' ) {
			portletURL.setWindowState("<%=LiferayWindowState.EXCLUSIVE.toString()%>");
			var processWorkflowActionContainer = A.one('.process-workflow-action');
			A.io.request(
				portletURL.toString(),
				{
					dataType : 'text/html',
					method : 'POST',
					sync : true,
				    data:{
				    },   
				    on: {
				    	success: function(event, id, obj) {
				    		
				    		if(processWorkflowActionContainer){
				    			processWorkflowActionContainer.hide();
				    		}
				    		
							var instance = this;
							
							var res = instance.get('responseData');
							
							var assignTaskContainer = A.one("#<portlet:namespace/>assignTaskContainer");
							
							if(assignTaskContainer){
								
								assignTaskContainer.empty();
								assignTaskContainer.html(res);
								
								var submitButton = A.one('#<portlet:namespace/>submit');
								var cancelButton = A.one('#<portlet:namespace/>cancel');
								var action = A.one('#<portlet:namespace/>assignActionURL').val();
								var form =  A.one("#<portlet:namespace/>pofm");
								if(form){
									form.attr('action', action);
								}
							
								if(submitButton){
									submitButton.on('click', function(){
										var msg = validateRequiredResult(dossierId, processStepId, processWorkflowId);
										if(msg != '') {
											alert(Liferay.Language.get(msg));
											return;
										} else{
											A.io.request(
												form.attr('action'),
												{
													dataType: 'json',
													form: {
														id: form
													},
													on: {
														success: function(event, id, obj) {
															var response = this.get('responseData');
															
															alert(Liferay.Language.get(response.msg));
															
															if(response.msg == '<%=MessageKeys.DEFAULT_SUCCESS_KEY%>'){
																var redirectURL = A.one('#<portlet:namespace/>redirectURL').val();
																window.location = redirectURL;
															}
														}
													}
												}
											);
										}

									});
								}
								
								if(cancelButton){
									cancelButton.on('click', function(){
										form.attr('action', '');
										assignTaskContainer.empty();
										processWorkflowActionContainer.show();
									});
								}
							}
								
						},
				    	error: function(){}
					}
				}
			);
		}
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
<c:if test='<%=assignFormDisplayStyle.equals("form") %>'>
<portlet:resourceURL var="getDataAjax"></portlet:resourceURL>

<portlet:actionURL var="signatureURL" name="signatureBCY"></portlet:actionURL>

<aui:script>
	var assignTaskAfterSign = '<%=String.valueOf(assignTaskAfterSign)%>';

	function plugin0() {
		
	  return document.getElementById('plugin0');
	}

	plugin = plugin0;
	
	var complateSignatureURL = '<%=signatureURL%>';

	function getFileComputerHash(symbolType) {

		var url = '<%=getDataAjax%>';
		
		var nanoTime = $('#<portlet:namespace/>nanoTimePDF').val();
		
		url = url + "&nanoTimePDF="+nanoTime;
		
		var listFileToSigner = $("#<portlet:namespace/>listFileToSigner").val().split(","); 
		var listDossierPartToSigner = $("#<portlet:namespace/>listDossierPartToSigner").val().split(","); 
		var listDossierFileToSigner = $("#<portlet:namespace/>listDossierFileToSigner").val().split(","); 
		
		for ( var i = 0; i < listFileToSigner.length; i++) {
			$.ajax({
				type : 'POST',
				url : url,
				data : {
					<portlet:namespace/>index: i,
					<portlet:namespace/>indexSize: listFileToSigner.length,
					<portlet:namespace/>symbolType: symbolType,
					<portlet:namespace/>fileId: listFileToSigner[i],
					<portlet:namespace/>dossierId: $("#<portlet:namespace/>dossierId").val(),
					<portlet:namespace/>dossierPartId: listDossierPartToSigner[i],
					<portlet:namespace/>dossierFileId: listDossierFileToSigner[i],
					<portlet:namespace/>type: 'getComputerHash'
				},
				success : function(data) {
					if(data){
						var jsonData = JSON.parse(data);
						var hashComputers = jsonData.hashComputers;
						var signFieldNames = jsonData.signFieldNames;
						var filePaths = jsonData.filePaths;
						var msgs = jsonData.msg;
						var fileNames = jsonData.fileNames;
						var dossierFileIds = jsonData.dossierFileIds;
						var dossierPartIds = jsonData.dossierPartIds;
						var indexs = jsonData.indexs;
						var indexSizes = jsonData.indexSizes;
						for ( var i = 0; i < hashComputers.length; i++) {
							var hashComputer = hashComputers[i];
							var signFieldName = signFieldNames[i];
							var filePath = filePaths[i];
							var msg = msgs[i];
							var fileName = fileNames[i];
							var dossierFileId = dossierFileIds[i];
							var dossierPartId = dossierPartIds[i];
							var index = indexs[i];
							var indexSize = indexSizes[i];
							if(plugin().valid){
								if(msg === 'success'){
	 								var code = plugin().Sign(hashComputer);
	 								if(code ===0 || code === 7){
	 									var sign = plugin().Signature;
										completeSignature(sign, signFieldName, filePath, fileName, $("#<portlet:namespace/>dossierId").val(), dossierFileId, dossierPartId, index, indexSize, '<%=signatureURL%>');
										
	 								}else{
	 									alert('<%=LanguageUtil.get(pageContext, "signer-error") %>');
	 					            }
								}else{
									alert('<%=LanguageUtil.get(pageContext, "signer-error-lien-he") %>');
								}
					        	
					        } else {
					        	alert('<%=LanguageUtil.get(pageContext, "plugin-is-not-working") %>');
					        }
						}
					}
				}
			});
		}
	}

	function completeSignature(sign, signFieldName, filePath, fileName, dossierId, dossierFileId, dossierPartId, index, indexSize, urlFromSubmit) {
		var msg = '';
		var A = AUI();
		A.io.request(
			complateSignatureURL,
			{
			    dataType : 'json',
			    data:{    	
			    	<portlet:namespace/>sign : sign,
					<portlet:namespace/>signFieldName : signFieldName,
					<portlet:namespace/>filePath : filePath,
					<portlet:namespace/>fileName : fileName,
					<portlet:namespace/>dossierId : dossierId,
					<portlet:namespace/>dossierFileId: dossierFileId,
					<portlet:namespace/>dossierPartId : dossierPartId
			    },   
			    on: {
			        success: function(event, id, obj) {
			        	var instance = this;
						var res = instance.get('responseData');
						
						var msg = res.msg;
						var newis = indexSize-1;
							if (msg === 'success') {
								alert(Liferay.Language.get('signature-success'));
								if(index == newis){
									if(assignTaskAfterSign == 'true'){
										var action = A.one('#<portlet:namespace/>assignActionURL').val();
										var form =  A.one("#<portlet:namespace/>pofm");
										if(form){
											form.attr('action', action);
											document.getElementById('<portlet:namespace />pofm').submit();
										}
									}
									
								}
							} else {
								alert('<%=LanguageUtil.get(pageContext, "signer-error") %>');
							}
					},
			    	error: function(){
			    		alert('<%=LanguageUtil.get(pageContext, "signer-fail") %>');
			    	}
				}
			}
		);
	}
	
</aui:script>
</c:if>
