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

<%@page import="org.opencps.util.AccountUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="com.liferay.portal.kernel.management.jmx.DoOperationAction"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>

<%@ include file="../init.jsp"%>

<%
	
	Dossier dossier = (Dossier)request.getAttribute(WebKeys.DOSSIER_ENTRY);
	
	ServiceConfig serviceConfig = (ServiceConfig)request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	
	DossierPart dossierPart = (DossierPart)request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	String cmd = ParamUtil.getString(request, Constants.CMD, Constants.UPDATE);
	
	String[] dossierSections = dossier != null ? 
		new String[]{"dossier_info", "dossier_part", "result", "history"} : 
		new String[]{"dossier_info"};
	
	String[][] categorySections = {dossierSections};
	
	boolean isEditDossier = ParamUtil.getBoolean(request, "isEditDossier");
	
	ProcessOrder processOrder = null;
	ProcessWorkflow workFlow = null;
	try {
		if(Validator.isNotNull(dossier)) {
			processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(dossier.getDossierId(), 0);
			workFlow = ProcessWorkflowLocalServiceUtil.getByS_PreP_AN(processOrder.getServiceProcessId(), processOrder.getProcessStepId(), "Thông báo hủy hồ sơ");
		}
	}
	catch (Exception e) {
		
	}
%>

<c:choose>
	<c:when test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) && Validator.isNotNull(accountType) &&
				(accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN) ||
				accountType.equals(PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS)) %>">
		 <liferay-ui:header
			backURL="<%= backURL %>"
			title='<%= (dossier == null) ? "add-dossier" : (cmd.equals(Constants.VIEW) ? "view-dossier" : "update-dossier") %>'
		/>
		
		<portlet:actionURL var="updateDossierURL" name="updateDossier"/>
		
		<liferay-util:buffer var="htmlTop">
			<c:if test="<%= dossier != null %>">
				<div class="form-navigator-topper dossier-info">
					<div class="form-navigator-container">
						<i aria-hidden="true" class="fa fa-suitcase"></i>
						<span class="form-navigator-topper-name"><%= Validator.isNotNull(dossier.getReceptionNo()) ? dossier.getReceptionNo() : StringPool.BLANK %></span>
					</div>
				</div>
			</c:if> 
		</liferay-util:buffer>
		
		<liferay-util:buffer var="htmlBottom">
			
		 	<c:choose>
		 		<c:when test="<%=Validator.isNotNull(dossier)%>">
					<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">	
						<c:if test="<%=dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) || 
				 			dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_WAITING)%>">
			 			
					 		<c:if test="<%=dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) %>">
						 		<portlet:actionURL var="updateDossierStatusURL" name="updateDossierStatus">
									<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID %>" value="<%=String.valueOf(dossier.getDossierId()) %>"/>
									<portlet:param name="<%=DossierDisplayTerms.DOSSIER_STATUS %>" value="<%=String.valueOf(PortletConstants.DOSSIER_STATUS_NEW) %>"/>
									<portlet:param name="backURL" value="<%=currentURL %>"/>
								</portlet:actionURL> 
						 		<liferay-ui:icon
						 			cssClass="search-container-action fa forward"
						 			image="forward"
						 			message="send" 
						 			url="<%=updateDossierStatusURL.toString() %>" 
						 		/>
					 		</c:if>
					 		
					 		<c:if test="<%=dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_WAITING) %>">
						 		<portlet:actionURL var="updateDossierStatusURL" name="updateDossierStatus">
									<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID %>" value="<%=String.valueOf(dossier.getDossierId()) %>"/>
									<portlet:param name="<%=DossierDisplayTerms.DOSSIER_STATUS %>" value="<%=String.valueOf(PortletConstants.DOSSIER_STATUS_WAITING) %>"/>
									<portlet:param name="backURL" value="<%=currentURL %>"/>
								</portlet:actionURL> 
						 		<liferay-ui:icon
						 			cssClass="search-container-action fa forward"
						 			image="reply"
						 			message="resend" 
						 			url="<%=updateDossierStatusURL.toString() %>" 
						 		/> 
					 		</c:if>
					 	</c:if>
					 	<c:if test="<%=DossierPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) && dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_NEW) %>">
					 		<portlet:actionURL var="deleteDossierURL" name="deleteDossier" >
								<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID %>" value="<%=String.valueOf(dossier.getDossierId()) %>"/>
								<portlet:param name="redirectURL" value="<%=currentURL %>"/>
								<portlet:param name="dossierStatus" value="<%=dossier.getDossierStatus() %>"/>
							</portlet:actionURL> 
							<liferay-ui:icon-delete 
								image="delete"
								cssClass="search-container-action fa delete"
								confirmation="are-you-sure-delete-entry" 
								message="delete"  
								url="<%=deleteDossierURL.toString() %>" 
							/>
					 	</c:if>
			 		</c:if>
			  		<c:if test="<%= (dossier.getDossierStatus().equals(PortletConstants.DOSSIER_STATUS_PROCESSING) && workFlow != null) %>">
					 		<portlet:actionURL var="cancelDossierURL" name="cancelDossier" >
								<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID %>" value="<%=String.valueOf(dossier.getDossierId()) %>"/>
								<portlet:param name="redirectURL" value="<%=currentURL %>"/>
							</portlet:actionURL> 
							<liferay-ui:icon-delete 
								image="undo"
								cssClass="search-container-action fa undo"
								confirmation="are-you-sure-cancel-entry" 
								message="cancel"  
								url="<%=cancelDossierURL.toString() %>" 
							/>
					</c:if>  		
			  		
		 		</c:when>		
		 	</c:choose>
		 	
		 	<%
		 	 boolean checlShowBtnSubmitAndCacel = true;
		 	 if(cmd.equals(Constants.VIEW)) {
		 		checlShowBtnSubmitAndCacel = false;
		 	 }
		 	 
		 	%>
		 	<c:if test= "<%=checlShowBtnSubmitAndCacel %>">
			 	<div>	
			 		<aui:button 
			 			type="submit" 
			 			cssClass="button-add" 
			 			icon="icon-plus"
			 			value="edit-dossier-btn"
			 		/>	
			 		<%-- <aui:button 
	 					href="<%=backURLFromList.toString() %>" 
	 					cssClass="button-del" 
	 					value="canceled-dossier-btn"
			 			icon="icon-remove"
			 		/> --%>
			 	</div>
		 	</c:if>
		</liferay-util:buffer>
	
		<aui:form name="fm" action="<%=updateDossierURL %>" method="post">
		
			<aui:model-context bean="<%= dossier %>" model="<%= Dossier.class %>" />
			
			<aui:input name="<%= DossierDisplayTerms.REDIRECT_PAYMENT_URL %>" value="<%= redirectPaymentURL %>" type="hidden"></aui:input>
			
			<aui:input 
				name="backURL" 
				type="hidden" 
				value="<%= backURL%>"
			/>

			<aui:input 
				name="redirectURL" 
				type="hidden" 
				value="<%= currentURL%>"
			/>
			
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
				name="<%=DossierDisplayTerms.GROUP_ID %>" 
				type="hidden" 
				value="<%= scopeGroupId%>"
			/>
			<aui:input 
				name="<%=DossierDisplayTerms.COMPANY_ID %>" 
				type="hidden" 
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
	</c:when>
	
	<c:otherwise>
		<div class="portlet-msg-alert"><liferay-ui:message key="your-account-not-authorized-update-dossier"/></div>
	</c:otherwise>
 
</c:choose>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.edit_dossier.jsp");
%>
