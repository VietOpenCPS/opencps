
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

<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.service.WorkflowOutputLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.WorkflowOutput"%>
<%@page import="org.opencps.backend.util.PaymentRequestGenerator"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.backend.util.BookingDateGenerator"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierTemplateException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.RequiredDossierPartException"%>
<%@ include file="../init.jsp"%>

<%
	boolean success = false;
	
	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){
		
	}
	ProcessOrder processOrder = (ProcessOrder) request.getAttribute(WebKeys.PROCESS_ORDER_ENTRY);
	
	DossierFile  dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	long dossierId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.DOSSIER_ID);
	long fileGroupId =  ParamUtil.getLong(request, ProcessOrderDisplayTerms.FILE_GROUP_ID);
	long processOrderId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_ORDER_ID);
	long actionUserId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.ACTION_USER_ID);
	long processWorkflowId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_WORKFLOW_ID);
	long serviceProcessId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.SERVICE_PROCESS_ID);
	long processStepId = ParamUtil.getLong(request, ProcessOrderDisplayTerms.PROCESS_STEP_ID);
	
	String actionNote = ParamUtil.getString(request, ProcessOrderDisplayTerms.ACTION_NOTE);
	String event = ParamUtil.getString(request, ProcessOrderDisplayTerms.EVENT);
	String receptionNo = ParamUtil.getString(request, ProcessOrderDisplayTerms.RECEPTION_NO);
	
	String strReceiveDate = ParamUtil.getString(request, "receiveDate");
	
	String deadlinePattern = ParamUtil.getString(request, "deadlinePattern");
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	Date receiveDate = null;
	
	if(Validator.isNotNull(strReceiveDate)){
		receiveDate = DateTimeUtil.convertStringToDate(strReceiveDate);
	}
	
	Date estimateDate = null;
	
	if(receiveDate != null && Validator.isNotNull(deadlinePattern)){
		estimateDate = BookingDateGenerator.dateGenerator(receiveDate, deadlinePattern);
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
	
%>

<liferay-ui:error 
	exception="<%= NoSuchDossierException.class %>" 
	message="<%=NoSuchDossierException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= NoSuchDossierTemplateException.class %>" 
	message="<%=NoSuchDossierTemplateException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= RequiredDossierPartException.class %>" 
	message="<%=RequiredDossierPartException.class.getName() %>"
/>

<portlet:actionURL var="assignToUserURL" name="assignToUser"/>

<aui:form name="fm" action="<%=assignToUserURL.toString() %>" method="post">
	<aui:input 
		name="redirectURL" 
		value="<%=currentURL %>" 
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
		name="backURL"
		value="<%=backURL %>" 
		type="hidden"
	/>
	<aui:select 
		name="<%=ProcessOrderDisplayTerms.ASSIGN_TO_USER_ID %>" 
		label="assign-to-next-user" 
		showEmptyOption="true"
	>
		<%
			List<User> assignUsers = ProcessUtils.getAssignUsers(processStepId, 0);
			for (User userSel : assignUsers) {
		%>	
			<aui:option value="<%= userSel.getUserId() %>"><%= userSel.getFullName() %></aui:option>
		<%
			}
		%>
	</aui:select>
	
	<c:if test="<%=processWorkflow != null &&  processWorkflow.isRequestPayment()%>">
		<aui:input 
			name="<%=ProcessOrderDisplayTerms.PAYMENTVALUE %>" 
			label="requirement-to-pay-charges" 
			type="text"
			value="<%=Validator.isNotNull(processWorkflow.getPaymentFee()) ? PaymentRequestGenerator.getTotalPayment(processWorkflow.getPaymentFee()) : StringPool.BLANK %>"
		/>
	</c:if>
	
	<aui:input 
		name="<%=ProcessOrderDisplayTerms.RECEPTION_NO %>" 
		label="reception-no" 
		value="<%=receptionNo %>"
		type="text"
	/>
	
	<label class="control-label custom-lebel" 
		for='<portlet:namespace/><%="deadline" %>'
	>
		<liferay-ui:message key="return-date"/>
	</label>
	
	<liferay-ui:input-date
		dayParam="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME_DAY %>"
		disabled="<%= false %>"
		monthParam="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME_MONTH %>"
		name="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME %>"
		yearParam="<%=ProcessOrderDisplayTerms.ESTIMATE_DATETIME_YEAR %>"
		formName="fm"
		autoFocus="<%=true %>"
		dayValue="<%=spd != null ? spd.getDayOfMoth() : 0 %>"
		monthValue="<%=spd != null ? spd.getMonth() : 0 %>"
		yearValue="<%=spd != null ? spd.getYear() : 0 %>"
		nullable="<%=spd == null ? true: false %>"
	/>
	
	
	
	<aui:input name="<%=ProcessOrderDisplayTerms.ACTION_NOTE %>" label="action-note" type="textarea"/>
	
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

	<aui:button type="submit" value="submit" name="submit"/>
	<c:if test="<%=esign %>">
		<aui:button type="button" value="esign" name="esign"/>
	</c:if>
	<aui:button type="button" value="cancel" name="cancel"/>
	<div id="myProgressBar" class="aui-progress-warning"></div>
</aui:form>

<aui:script>
	AUI().ready(function(A){

		var cancelButton = A.one('#<portlet:namespace/>cancel');
		
		var esign = A.one('#<portlet:namespace/>esign');
		
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
	
	Liferay.provide(window, '<portlet:namespace/>esign', function() {
		
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
		    	//console.log(signature);
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
	}, ['aui-io','liferay-portlet-url']);
	
</aui:script>