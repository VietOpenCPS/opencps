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
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="java.util.Date"%>
<%@page import="javax.portlet.PortletMode"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="org.opencps.backend.util.DossierNoGenerator"%>
<%@page import="org.opencps.backend.util.PaymentRequestGenerator"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.RequiredDossierPartException"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.holidayconfig.util.HolidayUtils"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.processmgt.model.WorkflowOutput"%>
<%@page import="org.opencps.processmgt.NoSuchWorkflowOutputException"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="org.opencps.processmgt.util.ProcessOrderUtils"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.WebKeys"%>

<%@ include file="init.jsp"%>

<%
	boolean success = false;
	
	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){}
	
	ProcessOrder processOrder = (ProcessOrder) request.getAttribute(WebKeys.PROCESS_ORDER_ENTRY);
	
	DossierFile  dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	long dossierId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.DOSSIER_ID);
	long fileGroupId =  ParamUtil.getLong(request, ProcessOrderDisplayTerms.FILE_GROUP_ID);
	long processOrderId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
	long actionUserId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.ACTION_USER_ID);
	long processWorkflowId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID);
	long serviceProcessId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.SERVICE_PROCESS_ID);
	long processStepId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_STEP_ID);
	
	ProcessWorkflow workflow = ProcessMgtUtil.getProcessWorkflow(processWorkflowId);
	
	Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);

	String actionNote = ParamUtil.getString(request, ProcessOrderDisplayTerms.ACTION_NOTE);
	String event = ParamUtil.getString(request, ProcessOrderDisplayTerms.EVENT);
	String receptionNo = Validator.isNotNull(dossier) ? dossier.getReceptionNo() : StringPool.BLANK; //ParamUtil.getString(request, ProcessOrderDisplayTerms.RECEPTION_NO);
	
	//remove generate receiveNo in jsp, just generate on server
// 	if ((Validator.isNull(receptionNo) || (receptionNo.length() == 0)) 
// 			&& Validator.isNotNull(workflow) 
// 			&& workflow.getGenerateReceptionNo()) {
		
// 		// If doisser don't have receiveNo, create receiveNo
// 		receptionNo = DossierNoGenerator.genaratorNoReception(workflow.getReceptionNoPattern(), dossierId);
		
// 	}
	
	String strReceiveDate = ParamUtil.getString(request, "receiveDate");
	
	String deadlinePattern = ParamUtil.getString(request, "deadlinePattern");
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	Date receiveDate = ProcessOrderUtils.getRecevieDate(dossierId, processWorkflowId, processStepId);
	
	Date estimateDate = null;
	
	if(workflow != null && workflow.getGenerateDeadline() && Validator.isNotNull(receiveDate) && Validator.isNotNull(deadlinePattern)){
		estimateDate = HolidayUtils.getEndDate(receiveDate, deadlinePattern);
	}
	
	PortletUtil.SplitDate spd = null;
	
	if(estimateDate != null){
		spd = new PortletUtil.SplitDate(estimateDate);
	}
	
	ProcessWorkflow processWorkflow = null;
	
	List<WorkflowOutput> workflowOutputs = new ArrayList<WorkflowOutput>();
	
	
	if(processWorkflowId > 0){
		try{
			processWorkflow = ProcessWorkflowLocalServiceUtil.getProcessWorkflow(processWorkflowId);

			workflowOutputs = WorkflowOutputLocalServiceUtil.getProcessByE_S_ID_PB(processWorkflowId, true);
		}catch(Exception e){};
	}

	boolean esign = false;
	
	long assigerToUserId = ProcessMgtUtil.getAssignUser(processWorkflowId, processOrderId, workflow.getPostProcessStepId());
	
	PortletURL backTodoListURL =PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

	backTodoListURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/processordertodolist.jsp");
	backTodoListURL.setParameter("success", String.valueOf(true));
	backTodoListURL.setWindowState(LiferayWindowState.NORMAL);
	backTodoListURL.setPortletMode(PortletMode.VIEW);
	
	List<String> listFileToSigner = new ArrayList<String>();
	List<String> listDossierPartToSigner = new ArrayList<String>();
	List<String> listDossierFileToSigner = new ArrayList<String>();
	
	for (WorkflowOutput workflowOutput : workflowOutputs) {
		DossierFile dossierFileSign = DossierFileLocalServiceUtil.getDossierFileInUse(dossierId, workflowOutput.getDossierPartId());
		
		if(Validator.isNotNull(dossierFileSign)){
			listFileToSigner.add(String.valueOf(dossierFileSign.getFileEntryId()));
			listDossierPartToSigner.add(String.valueOf(workflowOutput.getDossierPartId()));
			listDossierFileToSigner.add(String.valueOf(dossierFileSign.getDossierFileId()));
		}
	}
%>

<liferay-ui:error 
	exception="<%= NoSuchDossierException.class %>" 
	message="<%=NoSuchDossierException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= NoSuchWorkflowOutputException.class %>" 
	message="<%=NoSuchWorkflowOutputException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= RequiredDossierPartException.class %>" 
	message="<%=RequiredDossierPartException.class.getName() %>"
/>

<portlet:actionURL var="assignToUserURL" name="assignToUser"/>

<aui:form name="fm" action="<%=assignToUserURL.toString() %>" method="post">

	<aui:input 
		name="assignFormDisplayStyle" 
		value="<%=assignFormDisplayStyle %>" 
		type="hidden"
	/>
	
	<aui:input 
		name="assignActionURL" 
		value="<%=assignToUserURL.toString() %>" 
		type="hidden"
	/>
	
	<aui:input 
		name="assignActionURL" 
		value="<%=assignToUserURL.toString() %>" 
		type="hidden"
	/>
	<aui:input 
		name="redirectURL" 
		value="<%=currentURL %>" 
		type="hidden"
	/>
	<aui:input 
			name="listFileToSigner" 
			value="<%=StringUtil.merge(listFileToSigner) %>" 
			type="hidden"
		/>
	<aui:input 
			name="listDossierPartToSigner" 
			value="<%=StringUtil.merge(listDossierPartToSigner) %>" 
			type="hidden"
		/>
	<aui:input 
		name="listDossierFileToSigner" 
		value="<%=StringUtil.merge(listDossierFileToSigner) %>" 
		type="hidden"
	/>

	<aui:input 
		name="<%=ProcessOrderDisplayTerms.PROCESS_STEP_ID %>" 
		value="<%=processStepId %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.GROUP_ID %>" 
		value="<%=scopeGroupId %>" 
		type="hidden"
	/>
	
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.RECEIVE_DATE %>" 
		value="<%= receiveDate %>" 
		type="hidden"
	/>
	
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.COMPANY_ID %>" 
		value="<%=company.getCompanyId() %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.EVENT %>" 
		value="<%=event %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.ACTION_USER_ID %>" 
		value="<%=actionUserId %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.DOSSIER_ID %>" 
		value="<%=dossierId %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.FILE_GROUP_ID %>" 
		value="<%=fileGroupId %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.PROCESS_ORDER_ID %>" 
		value="<%=processOrderId %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID %>" 
		value="<%=processWorkflowId %>" 
		type="hidden"
	/>
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.SERVICE_PROCESS_ID %>" 
		value="<%=serviceProcessId %>" 
		type="hidden"
	/>
	
	<aui:input 
		name="nanoTimePDF" 
		value="<%=System.currentTimeMillis() %>" 
		type="hidden"
	/>
	
	<aui:input 
		name="backURL"
		value="<%=backURL %>" 
		type="hidden"
	/>
	
	<%
		String cssCol = ProcessUtils.getCssClass(processWorkflowId);
	%>
	
	<div class="row-fluid">
	
	<c:if test="<%= processWorkflow.getAssignUser() %>">
	
			<div class="span12">
				<aui:select 
					name="<%=ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID %>" 
					label="assign-to-next-user" 
					showEmptyOption="true"
					cssClass="input100"
				>
					<%
						List<User> assignUsers = ProcessUtils.getAssignUsers(processStepId, 0);
						
						for (User userSel : assignUsers) {
					%>	
						<aui:option selected="<%= assigerToUserId == userSel.getUserId() ? true : false  %>" value="<%= userSel.getUserId() %>"><%= userSel.getFullName() %></aui:option>
					<%
						}
					%>
				</aui:select>
			</div>
		</c:if>
		
		<c:if test="<%= processWorkflow.getRequestPayment() %>">
		
			<div class="span12">
				<aui:input 
					cssClass="input100"
					name="<%=ProcessOrderDisplayTerms.PAYMENTVALUE %>" 
					label="requirement-to-pay-charges" 
					type="text"
					value="<%=Validator.isNotNull(processWorkflow.getPaymentFee()) ? PaymentRequestGenerator.getTotalPayment(processWorkflow.getPaymentFee(), dossierId) : StringPool.BLANK %>"
				/>
			</div>
		</c:if>		
		
<%-- 		<c:if test="<%= processWorkflow.getGenerateReceptionNo() %>"> --%>
<!-- 			<div class="span12"> -->
<%-- 				<aui:input  --%>
<%-- 					name="<%=ProcessOrderDisplayTerms.RECEPTION_NO %>"  --%>
<%-- 					label="reception-no"  --%>
<%-- 					value="<%= receptionNo %>" --%>
<%-- 				/> --%>
<!-- 			</div> -->
<%-- 		</c:if> --%>
		
		<c:if test="<%= processWorkflow.getGenerateDeadline() %>">
			<div class="span12">
				
				<label class="control-label custom-lebel" for='<portlet:namespace/><%="deadline" %>'>
					<liferay-ui:message key="return-date"/>
				</label>

				<liferay-ui:input-date
					dayParam="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME_DAY %>"
					disabled="<%= false %>"
					monthParam="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME_MONTH %>"
					name="<%=ProcessOrderDisplayTerms.ESTIMATE_DATE %>"
					yearParam="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME_YEAR %>"
					formName="fm"
					autoFocus="<%=true %>"
					dayValue="<%=Validator.isNotNull(spd) ? spd.getDayOfMoth() : 0 %>"
					monthValue="<%=Validator.isNotNull(spd) ? spd.getMonth() : 0 %>"
					yearValue="<%=Validator.isNotNull(spd) ? spd.getYear() : 0 %>"
					nullable="<%=spd == null ? true: false %>"
					cssClass="input100"
				/>

				<liferay-ui:input-time 
					minuteParam="<%= ProcessOrderDisplayTerms.ESTIMATE_DATETIME_HOUR %>"
					amPmParam="AM" 
					hourParam="<%= ProcessOrderDisplayTerms.ESTIMATE_DATETIME_MINUTE %>"
					cssClass="input100"
					hourValue="<%= Validator.isNotNull(spd) ? spd.getHour() : 0 %>"
					minuteValue="<%= Validator.isNotNull(spd) ? spd.getMinute() : 0 %>"
					name="<%=ProcessOrderDisplayTerms.ESTIMATE_TIME %>"
				/>
			</div>
		</c:if>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12">
			<aui:input name="<%=ProcessOrderDisplayTerms.ACTION_NOTE %>" label="action-note" type="textarea" cssClass="input100"/>
		</div>
		<div id="<portlet:namespace/>defErrActionNote" style="text-align: left; color: #b50303; margin-left:7px; margin-bottom: 10px; display: none;">
			<liferay-ui:message key="required-field"/>
		</div>
	</div>
	
	<c:if test="<%=workflowOutputs != null && !workflowOutputs.isEmpty() %>">
	
		<label class="control-label custom-lebel" >
			
			<liferay-ui:message key="esign-dossier-files"/>
		</label>
		<div class="signature-file-list">
			<table>
				<%
					for(WorkflowOutput workflowOutput : workflowOutputs){
						long dossierPartId = workflowOutput.getDossierPartId();
						DossierFile dossierFileInUse = null;
						try{
							dossierFileInUse = DossierFileLocalServiceUtil.getDossierFileInUse(dossierId, dossierPartId);
						}catch(Exception e){
							continue;
						}
						
						if(dossierFileInUse != null && dossierFileInUse.getFileEntryId() > 0){
							esign = true;
							%>
								<tr>
									<td class="file-name" width="80%">
										<%=dossierFileInUse.getDisplayName()%>
									</td>
									<td class="signature-status" width="10%">
										<c:choose>
											<c:when test="<%=dossierFileInUse.isSigned() %>">
												<i class="fa fa-check-square-o" aria-hidden="true" title="signed"></i>
											</c:when>
											<c:otherwise>
												<i class="fa fa-square-o" aria-hidden="true" ></i>
											</c:otherwise>
										</c:choose>
									</td>
									<td class="signature-verify" width="10%">
										<c:if test="<%=dossierFileInUse.isSigned() %>">
											<aui:a href="javascript:void(0);" onClick='<%="javavscript:" + renderResponse.getNamespace() + "verifySign(this)" %>' title="check-sign" dossier-file="<%=dossierFileInUse.getDossierFileId() %>">
												<i class="fa fa-pencil-square-o" aria-hidden="true"></i>
											</aui:a>
										</c:if>
									</td>
								</tr>
								
							<%
						}
					}
				%>
			</table>
		</div>
		<div style="display:none">
			<aui:select name="esignDossierFiles" multiple="<%=true %>" label="esign-dossier-files">
				<%
					for(WorkflowOutput workflowOutput : workflowOutputs){
						long dossierPartId = workflowOutput.getDossierPartId();
						DossierFile dossierFileInUse = null;
						try{
							dossierFileInUse = DossierFileLocalServiceUtil.getDossierFileInUse(dossierId, dossierPartId);
						}catch(Exception e){
							continue;
						}
						
						if(dossierFileInUse != null && dossierFileInUse.getFileEntryId() > 0){
							esign = true;
							%>
								<aui:option value="<%=dossierFileInUse.getDossierFileId() %>"><%=dossierFileInUse.getDisplayName()%></aui:option>
							<%
						}
					}
				%>
			</aui:select>
		</div>
	</c:if>
	<div class="button-holder">
		
		<c:choose>
			<c:when test="<%=esign %>">
				<aui:button type="button" value="esign" name="esign" onClick="getFileComputerHash(1);"/>
			</c:when>
			<c:otherwise>
				<aui:button type="button" value="submit" name="submit"/>
			</c:otherwise>
		</c:choose>
		
		<aui:button type="button" value="cancel" name="cancel"/>
	</div>
	
	<div id="myProgressBar" class="aui-progress-warning"></div>
</aui:form>

<div style="visibility: hidden; height: 0px; width: 0px;">
	<object id="plugin0" type="application/x-cryptolib05plugin" width="0" height="0" ></object>
</div>

<portlet:resourceURL var="getDataAjax"></portlet:resourceURL>

<portlet:actionURL var="signatureURL" name="signatureBCY"></portlet:actionURL>


<aui:script>

	AUI().ready(function(A){
		
		var submitButton = A.one('#<portlet:namespace/>submit');

 		var cancelButton = A.one('#<portlet:namespace/>cancel');
		
 		var esign = A.one('#<portlet:namespace/>esign');
		
		if(submitButton){
			submitButton.on('click', function(){
				submitForm(document.<portlet:namespace />fm);
			});
		}
		
 		if(cancelButton){
 			cancelButton.on('click', function(){
 				<portlet:namespace/>closeDialog();
 			});
 		}
		
 		var success = '<%=success%>';
		
 		if(success == 'true'){
 			var backURL = '<%=backURL%>';
 			var Util = Liferay.Util;
 			<portlet:namespace/>closeDialog();
			Util.getOpener().Liferay.fire('redirect', {responseData:{backURL:backURL}});
 		}
		
 		if(esign){
			esign.on('click', function(){
 				<portlet:namespace/>esign();
 			});
 		}
	
 	});
	
 	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
 		var backURL = '<%=backURL%>';
 		var dialog = Liferay.Util.getWindow('<portlet:namespace/>assignToUser');
 		dialog.destroy();
 		var data = {
 			'conserveHash': true
 		};
 		Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_<%= WebKeys.PROCESS_ORDER_PORTLET %>_', data);
 	});

 	Liferay.provide(window, '<portlet:namespace/>verifySign', function(e) {
 		var A = AUI();
 		var instance = A.one(e);
 		var dossierFileId = instance.attr('dossier-file');
	
 		var uri = '<%=PortletPropsValues.OPENCPS_SERVLET_VERIFY_SIGN_DOCUMENT_URL%>' + dossierFileId;
		
 		var loadingMask = new A.LoadingMask(
 			{
 				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "Verify signature ...") %>',
 				target: A.one('#<portlet:namespace/>fm')
 			}
 		);
	
 		loadingMask.show();
		
 		openDialog(uri, '<portlet:namespace />verifySignature','<%= UnicodeLanguageUtil.get(pageContext, "verify") %>');
		
 		loadingMask.hide();
 	},['aui-io','liferay-portlet-url', 'aui-loading-mask-deprecated']);
	
 	/* Liferay.provide(window, '<portlet:namespace/>esign', function() {
		
 		var A = AUI();
		
 		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
 		portletURL.setParameter("javax.portlet.action", "hashSingleFile");
 		portletURL.setWindowState('<%=WindowState.NORMAL%>');
		
 		var esignDossierFiles = A.one('#<portlet:namespace/>esignDossierFiles');
		
 		var dossierFileIds = [];
		

 		var loadingMask = new A.LoadingMask(
 			{
 				'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "esign...") %>',
 				target: A.one('#<portlet:namespace/>fm')
 			}
 		);
		
 		//console.log(loadingMask);
		
 		//console.log(loadingMask._attrs.strings.value.loading);
		
 		//console.log(loadingMask.getAttrs());
		
 		//var strings = {strings : {loading: 'xxx...'}};
		
 		//loadingMask.setAttrs(strings);

 		//loadingMask.reset();
		
		//console.log(loadingMask.getAttrs());

 		loadingMask.show();
	
		
 		if(esignDossierFiles){
 			var childs = esignDossierFiles._node.children;
 			if(childs.length > 0){
 				for(var i = 0; i < childs.length; i++){
 					var option = A.one(childs[i]);
 					dossierFileIds.push(option.attr('value'));
					
 					A.io.request(
 						portletURL.toString(),
						
 						{
						    dataType : 'json',
 						   	sync: true,
 						    data:{    	
 						    	<portlet:namespace/>dossierFileId : option.attr('value'),
 						    },   
 						    on: {
 						        success: function(event, id, obj) {
 									var instance = this;
 									var res = instance.get('responseData');
 									//console.log(res);
 									<portlet:namespace/>signature(res.hashHex, res.resources);
 								},
 						    	error: function(){
						    	
 						    	}
 							}
						}
					);
 				}			
 			}
 		}
		
 		loadingMask.hide();
 	},['aui-io','liferay-portlet-url', 'aui-loading-mask-deprecated']);
	
 	Liferay.provide(window, '<portlet:namespace/>signature', function(hex, resources) {
		
 		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.ACTION_PHASE) %>');
 		portletURL.setParameter("javax.portlet.action", "signature");
  		portletURL.setWindowState('<%=WindowState.NORMAL%>');
 		console.log(hex);
 		console.log(resources);
		
		$.sign({
		    hash: {
		        type: 'sha512',
		        hex: hex
		    },
		    document: {
		    	resources: resources
		    },
		    'backend': 'bcy',
		    beforeSign: function(signer, hash) {
		        // do something
		    },
		    afterSign: function(signer, signature) {
		    	console.log(signature.value);
				console.log(signature.certificate);
		    	console.log(signature);
		       $.ajax({
			   		type: "POST",
		       		url : portletURL.toString(),
			   		async: false,
		            data : {
		                <portlet:namespace/>signature: signature.value,
		                <portlet:namespace/>certificate: signature.certificate,
		                <portlet:namespace/>resources: JSON.stringify(signer.options.document.resources)
		            },success: function(data){
		            	console.log(data);
		            }
		        });
		    },
		    onError: function(signer, error) {
		        // do something
		    }
		});
	}, ['aui-io','liferay-portlet-url']); */
	
</aui:script>
	
<aui:script>
	var assignTaskAfterSign = '<%=String.valueOf(assignTaskAfterSign)%>';

	function formSubmit() {
		document.getElementById('<portlet:namespace />fm').action = '<%=assignToUserURL.toString() %>';
			document.getElementById('<portlet:namespace />fm').submit();
	}
	
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
									if(index == newis){
										if(assignTaskAfterSign == 'true'){
											formSubmit();
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
