
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

<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierFile"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.bean.BeanParamUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
<%@page import="org.opencps.dossiermgt.model.DossierPart"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="org.hsqldb.SessionManager"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../init.jsp"%>

<%
	boolean success = false;

	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){
		
	}
	
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);

	DossierFile dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	DossierPart dossierPart = (DossierPart) request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);
	

	Date defaultDossierFileDate = dossierFile != null && dossierFile.getDossierFileDate() != null ? 
			dossierFile.getDossierFileDate() : DateTimeUtil.convertStringToDate("01/01/1970");
			
	PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultDossierFileDate);
	
%>

<portlet:actionURL var="addAttachmentFileURL" name="addAttachmentFile"/>

<liferay-ui:error message="upload-error" key="upload-error"/>

<aui:form 
	name="fm" 
	method="post" 
	action="<%=addAttachmentFileURL%>" 
	enctype="multipart/form-data"
>
	<aui:input 
		name="redirectURL" 
		type="hidden" 
		value="<%=currentURL %>"
	/>
	<aui:input 
		name="<%=DossierDisplayTerms.DOSSIER_ID %>" 
		type="hidden" 
		value="<%=dossier != null ? dossier.getDossierId() : 0 %>"
	/>
	<aui:input 
		name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID%>" 
		type="hidden" 
		value="<%=dossierFile != null ? dossierFile.getDossierFileId() : 0 %>"
	/>
	<aui:input 
		name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID%>" 
		type="hidden" 
		value="<%=dossierPart != null ? dossierPart.getDossierpartId() : 0 %>"
	/>
	<aui:input 
		name="<%=BusinessDisplayTerms.BUSINESS_MAPPINGORGANIZATIONID%>" 
		type="hidden" 
		value="<%=business != null ? business.getMappingOrganizationId() : 0 %>"
	/>
	<aui:input 
		name="<%=BusinessDisplayTerms.BUSINESS_MAPPINGORGANIZATIONID%>" 
		type="hidden" 
		value="<%=business != null ? business.getMappingOrganizationId() : 0 %>"
	/>
	<aui:input 
		name="<%=DossierFileDisplayTerms.DOSSIER_FILE_TYPE%>" 
		type="hidden" 
		value="<%=dossierFile != null ? dossierFile.getDossierFileType() : 0 %>"
	/>
	<aui:input 
		name="<%=DossierDisplayTerms.ACCOUNT_TYPE %>" 
		type="hidden" 
		value="<%= citizen != null ? PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN : business != null ? PortletPropsValues.USERMGT_USERGROUP_NAME_BUSINESS : PortletPropsValues.USERMGT_USERGROUP_NAME_CITIZEN%>"
	/>
	<aui:input 
		name="<%=DossierDisplayTerms.TEMPLATE_FILE_NO %>" 
		type="hidden" 
		value="<%=dossierFile != null ? dossierFile.getTemplateFileNo() : StringPool.BLANK %>"
	/>
	<aui:input 
		name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ORIGINAL%>" 
		type="hidden" 
		value="<%=PortletConstants.DOSSIER_FILE_ORIGINAL%>"
	/>
	
	<aui:row>
		<aui:input 
			name="<%= DossierFileDisplayTerms.DISPLAY_NAME %>" 
			type="textarea"
		>
			<aui:validator name="required"/>
		</aui:input>
		<aui:input 
			name="<%= DossierFileDisplayTerms.DOSSIER_FILE_NO %>" 
			type="text"
		/>
		<label class="control-label custom-lebel" for='<portlet:namespace/><%=DossierFileDisplayTerms.DOSSIER_FILE_DATE %>'>
			<liferay-ui:message key="dossier-file-date"/>
		</label>
		<liferay-ui:input-date
			dayParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_DAY %>"
			dayValue="<%=spd.getDayOfMoth() %>"
			disabled="<%= false %>"
			monthParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_MONTH %>"
			monthValue="<%=spd.getMonth() %>"
			name="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE%>"
			yearParam="<%=DossierFileDisplayTerms.DOSSIER_FILE_DATE_YEAR %>"
			yearValue="<%=spd.getYear() %>"
			formName="fm"
			autoFocus="<%=true %>"
			nullable="<%=dossierFile == null || dossierFile.getDossierFileDate() == null ? true : false %>"
		/>
		<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD %>" type="file"/>
		<aui:button name="agree" type="submit" value="agree"/>
		<aui:button name="cancel" type="button" value="cancel"/>
	</aui:row>
</aui:form>

<aui:script use="aui-base,aui-io">
	AUI().ready(function(A){
		
		var cancelButton = A.one('#<portlet:namespace/>cancel');
		
		var success = '<%=success%>';
		
		if(cancelButton){
			cancelButton.on('click', function(){
				<portlet:namespace/>closeDialog();
			});
		}
		
		if(success == 'true'){
			<portlet:namespace/>closeDialog();
		}
		
	});
	
	
	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
		var dialog = Liferay.Util.getWindow('<portlet:namespace/>dossierFileId');
		dialog.destroy();
		var data = {
			'conserveHash': true
		};
		Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_<%= WebKeys.PROCESS_ORDER_PORTLET %>_', data);
	});

</aui:script>
