
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
<%@page import="org.opencps.dossiermgt.service.DossierFileLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierTemplateException"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@page import="org.opencps.dossiermgt.RequiredDossierPartException"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.util.WebKeys"%>

<%@ include file="../init.jsp"%>

<%
	Dossier dossier =
		(Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);

	ServiceConfig serviceConfig =
		(ServiceConfig) request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);

	DossierPart dossierPart =
		(DossierPart) request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	
	List<DossierFile> dossierFiles = new ArrayList<DossierFile>();

	String backURL = ParamUtil.getString(request, "backURL");

	String cmd =
		ParamUtil.getString(request, Constants.CMD, Constants.UPDATE);

	String[] dossierSections = dossier != null ? new String[] {
		"dossier_part", "dossier_info", "result", "history"
	} : new String[] {
		"dossier_info"
	};

	String[][] categorySections = {
		dossierSections
	};

	boolean isEditDossier =
		ParamUtil.getBoolean(request, "isEditDossier");

	ProcessOrder processOrder = null;
	ProcessWorkflow workFlow = null;
	try {
		if (Validator.isNotNull(dossier)) {
			processOrder =
				ProcessOrderLocalServiceUtil.getProcessOrder(
					dossier.getDossierId(), 0);

			if (processOrder != null) {
				workFlow =
					ProcessWorkflowLocalServiceUtil.getProcessWorkflowByEvent(
						processOrder.getServiceProcessId(),
						WebKeys.PRE_CONDITION_CANCEL,
						processOrder.getProcessStepId());
			}
		}
	}
	catch (Exception e) {

	}
	
	try {
		if(Validator.isNotNull(dossier)) {
			dossierFiles = DossierFileLocalServiceUtil.getDossierFileByDossierId(dossier.getDossierId());
		}
	} catch (Exception e) {
		
	}

	boolean quickCreateDossier = dossier == null ? true : false;

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

<liferay-portlet:renderURL var="backDossierList">
	<portlet:param 
		name="mvcPath" 
		value="/html/portlets/dossiermgt/frontoffice/frontofficedossierlist.jsp"
	/>
</liferay-portlet:renderURL>

<portlet:actionURL var="deleteDossierSuggesstionURL" name="deleteDossierSuggesstion">
	<portlet:param name="dossierId" value='<%= Validator.isNotNull(dossier) ? String.valueOf(dossier.getDossierId()) : "0"%>'/>
	<portlet:param name="currentURL" value="<%=currentURL %>"/>
</portlet:actionURL>
<portlet:actionURL var="updateDossierStatusURL" name="updateDossierStatus">
	<portlet:param 
		name="<%=DossierDisplayTerms.DOSSIER_ID %>" 
		value='<%=dossier != null ? String.valueOf(dossier.getDossierId()) : "" %>'
	/>
		<portlet:param 
		name="<%=DossierDisplayTerms.DOSSIER_STATUS %>" 
		value="<%= (dossier != null && dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_WAITING)) 
			? String.valueOf(PortletConstants.DOSSIER_STATUS_WAITING) 
			: String.valueOf(PortletConstants.DOSSIER_STATUS_NEW) %>"
	/>
	<portlet:param 
		name="backURL" 
		value="<%=backDossierList %>"
	/>
	
	<portlet:param 
		name="redirectURL" 
		value="<%=currentURL %>"
	/>
	<portlet:param 
		name="redirectURL" 
		value="<%=backDossierList %>"
	/>
</portlet:actionURL>

<c:choose>
	<c:when
		test="<%=DossierPermission.contains(
			permissionChecker, scopeGroupId, ActionKeys.UPDATE) &&
			Validator.isNotNull(accountType) &&
			(accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) || 
			accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS))%>"
	>

		<liferay-ui:header 
			backURL="<%= backURL %>"
			title=' ' 
		/>

		<portlet:actionURL var="updateDossierURL" name="updateDossier" />
		
		<portlet:actionURL var="quickUpdateDossierURL" name="quickUpdateDossier"/>

		<liferay-util:buffer var="htmlTop">
			<c:if test="<%= dossier != null %>">
				<div class="form-navigator-topper dossier-info">
					<div class="form-navigator-container">
						<i aria-hidden="true" class="fa fa-suitcase"></i> <span
							class="form-navigator-topper-name"><%= Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.BLANK %></span>
					</div>
				</div>
			</c:if>
		</liferay-util:buffer>

		<liferay-util:buffer var="htmlBottom">

			<c:if test="<%= cmd.equals(Constants.VIEW) ? false : true %>">
				
				<c:if test="<%=Validator.isNotNull(dossier)%>">
					
					<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
						<c:if test="<%=dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) || 
									dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_WAITING)%>">

							<%
								String jsUpdateDossierStatus = "javascript:" + renderResponse.getNamespace() + "updateDossierStatus()";
							%>
							<c:if test="<%=dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) %>">
							
								<c:choose>
									<c:when test="<%= dossierFiles.size() == 0 %>">
										<aui:button 
											cssClass="btn des-sub-button radius20"
											name="submitDossierSuggestion" 
											value="dossier-suggestion">
										</aui:button>
									</c:when>
									<c:otherwise>
										<liferay-ui:icon-delete 
											image="undo"
											cssClass="search-container-action fa delete"
											confirmation="are-you-sure-cancel-entry" message="delete-dossier-file"
											url="<%=deleteDossierSuggesstionURL.toString() %>"
										/>
									</c:otherwise>
								</c:choose>
								
								<liferay-ui:icon 
									cssClass="search-container-action fa forward"
									image="forward" message="send-dossier"
									url="<%=jsUpdateDossierStatus %>"
								/>
							</c:if>

							<c:if test="<%=dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_WAITING) %>">
								<liferay-ui:icon
									cssClass="search-container-action fa forward check-before-send"
									image="reply" message="resend"
									url="<%=jsUpdateDossierStatus %>"
								/>
							</c:if>
						</c:if>

						<c:if test="<%=showBackToListButton %>">
							<liferay-ui:icon 
								image="back"
								cssClass="search-container-action fa undo input100"
								message="back-dossier-list"
								url="<%= backDossierList.toString() %>"
							/>
						</c:if>

					</c:if>
					
					<c:if test="<%= (dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_PROCESSING) && workFlow != null) %>">
						<portlet:actionURL var="cancelDossierURL" name="cancelDossier">
							<portlet:param 
								name="<%=DossierDisplayTerms.DOSSIER_ID %>"
								value="<%=String.valueOf(dossier.getDossierId()) %>"
							/>
							<portlet:param name="redirectURL" value="<%=currentURL %>" />
						</portlet:actionURL>
						
						<liferay-ui:icon-delete 
							image="undo"
							cssClass="search-container-action fa undo"
							confirmation="are-you-sure-cancel-entry" message="cancel"
							url="<%=cancelDossierURL.toString() %>"
						/>
					</c:if>

					<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) && dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) %>">
						<portlet:actionURL var="deleteDossierURL" name="deleteDossier">
							<portlet:param 
								name="<%=DossierDisplayTerms.DOSSIER_ID %>"
								value="<%=String.valueOf(dossier.getDossierId()) %>" 
							/>
							
							<portlet:param name="redirectURL" value="<%=backDossierList %>" />
							
							<portlet:param 
								name="dossierStatus"
								value="<%=dossier.getDossierStatus() %>" 
							/>
						</portlet:actionURL>
						
						<liferay-ui:icon-delete 
							image="delete"
							cssClass="search-container-action fa delete"
							confirmation="are-you-sure-delete-entry" message="delete-dossier"
							url="<%=deleteDossierURL.toString() %>" 
						/>
					</c:if>

				</c:if>

				<div>
					<aui:button 
						type="submit" 
						cssClass="btn des-sub-button radius20"
						icon="add" 
						value="edit-dossier-btn" 
					/>
				</div>
			</c:if>

		</liferay-util:buffer>

		<aui:form name="fm" action="<%=(dossier == null && allowQuickCreateDossier) ? quickUpdateDossierURL :updateDossierURL %>" method="post">

			<aui:model-context bean="<%= dossier %>" model="<%= Dossier.class %>" />

			<aui:input 
				name="<%= DossierDisplayTerms.REDIRECT_PAYMENT_URL %>"
				value="<%= redirectPaymentURL %>" type="hidden"
			/>

			<aui:input name="backURL" type="hidden" value="<%= backURL%>" />

			<aui:input name="redirectURL" type="hidden" value="<%= currentURL%>" />

			<aui:input 
				name="isEditDossier" 
				type="hidden"
				value="<%= isEditDossier%>"
			/>

			<aui:input 
				name="<%=WebKeys.ACCOUNT_TYPE %>" 
				type="hidden"
				value="<%= accountType%>"
			/>

			<aui:input 
				name="<%=DossierDisplayTerms.DOSSIER_ID %>" 
				type="hidden"
				value="<%= dossier != null ? dossier.getDossierId() : 0%>" 
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.DOSSIER_TEMPLATE_ID %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getDossierTemplateId() : 0%>" 
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.SERVICE_INFO_ID %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getServiceInfoId() : 0%>"
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.SERVICE_CONFIG_ID %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getServiceConfigId() : 0%>" 
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.GOVAGENCY_ORGANIZATION_ID %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getGovAgencyOrganizationId() : 0%>"
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.SERVICE_DOMAIN_INDEX %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getServiceDomainIndex() : StringPool.BLANK%>"
			/>
			
			<aui:input
				name="<%=DossierDisplayTerms.SERVICE_ADMINISTRATION_INDEX %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getServiceConfigId() : StringPool.BLANK%>" 
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.GOVAGENCY_CODE %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getGovAgencyCode() : StringPool.BLANK%>"
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.GOVAGENCY_NAME %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getGovAgencyName() : StringPool.BLANK%>"
			/>
			
			<aui:input 
				name="<%=DossierDisplayTerms.SERVICE_MODE %>"
				type="hidden"
				value="<%= serviceConfig != null ? serviceConfig.getServiceMode() : 0%>"
			/>

			<aui:input 
				name="<%=DossierDisplayTerms.TEMPLATE_FILE_NO %>"
				type="hidden"
				value="<%= dossierPart != null ? dossierPart.getTemplateFileNo() : StringPool.BLANK%>"
			/>

			<aui:input 
				name="<%=DossierDisplayTerms.GROUP_ID %>" type="hidden"
				value="<%= scopeGroupId%>" 
			/>
			
			<aui:input
				name="<%=DossierDisplayTerms.COMPANY_ID %>" type="hidden"
				value="<%= company.getCompanyId()%>"
			/>

			<div class="opencps-form-navigator-container">
				<liferay-ui:form-navigator 
					displayStyle="left-navigator"
					backURL="<%= backURL %>"
					categoryNames="<%= DossierMgtUtil._DOSSIER_CATEGORY_NAMES %>"
					categorySections="<%= categorySections %>"
					htmlBottom="<%= htmlBottom %>" 
					htmlTop="<%= htmlTop %>"
					jspPath='<%=templatePath + "dossier/" %>' 
					showButtons="<%=false %>"
				/>
			</div>
		</aui:form>

		<aui:script use="aui-loading-mask-deprecated">
			AUI().ready(function(A){
				
				var allowQuickCreateDossier = '<%=allowQuickCreateDossier%>'
				var quickCreateDossier = '<%=quickCreateDossier%>';
				
				if(quickCreateDossier ==='true' && allowQuickCreateDossier ==='true'){
					var loadingMask = new A.LoadingMask(
						{
							'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "rending...") %>',
							target: A.one('#<portlet:namespace/>fm')
						}
					);
					
					loadingMask.show();
					submitForm(document.<portlet:namespace />fm);
				}
			});
		</aui:script>

	</c:when>

	<c:otherwise>
		<div class="portlet-msg-alert">
			<liferay-ui:message key="your-account-not-authorized-update-dossier"/>
		</div>
	</c:otherwise>
 
</c:choose>

<aui:script>
	
	AUI().ready(function(A){
		var submitDossierSuggestion = A.one("#<portlet:namespace/>submitDossierSuggestion")
		if(submitDossierSuggestion) {
			submitDossierSuggestion.on('click', function() {
				
				var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.DOSSIER_MGT_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
				portletURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/dossier_suggestion.jsp");
				portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
				portletURL.setPortletMode("normal");
				portletURL.setParameter("dossierId", '<%= Validator.isNotNull(dossier) ? String.valueOf(dossier.getDossierId()) : "0" %>');
				portletURL.setParameter("serviceConfigId", '<%= Validator.isNotNull(serviceConfig) ? String.valueOf(serviceConfig.getServiceConfigId()) : "0" %>');
				portletURL.setParameter("dossierPartId", '<%= Validator.isNotNull(dossierPart) ? String.valueOf(dossierPart.getDossierpartId()) : "0" %>');
				
				openDialog(portletURL.toString(), 'submit-dossier-suggesstion', Liferay.Language.get("submit-dossier-suggesstion"));
			});
		}
	});
	
	Liferay.provide(
			window,
			'<portlet:namespace/>updateDossierStatus',
				function(actionURL) {
					var A = AUI(); 
					
					var required = false;
					
					var requiredDossierParts = A.all('#<portlet:namespace/>requiredDossierPart');
					
					if(requiredDossierParts) {
						
						requiredDossierParts.each(function(requiredDossierPart){
							var requiredDossierPartIds = requiredDossierPart.val().trim().split(",");
							console.log(dossierPartId);
							if(requiredDossierPartIds != ''){
								for(var i = 0; i < requiredDossierPartIds.length; i++){
									var dossierPartId = requiredDossierPartIds[i];
									console.log(dossierPartId);
									if(parseInt(dossierPartId) > 0){
										required = true;
										var row = A.one('.dossier-part-row.dpid-' + dossierPartId);
										if(row){
											row.attr('style', 'color:red');
										}
									}
								}
							}
						});
						
						
						if(required === true) {
							alert('<%= LanguageUtil.get(themeDisplay.getLocale(), "please-upload-dossier-part-required-before-send") %>');
						} else {
							location.href = '<%= updateDossierStatusURL %>';
						}
					}
				},
			['aui-base']
	);
	
</aui:script>

<%!
private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.edit_dossier.jsp");
%>
