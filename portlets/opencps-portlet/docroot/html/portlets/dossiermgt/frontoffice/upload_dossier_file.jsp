
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
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portlet.documentlibrary.DuplicateFileException"%>
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
	
	String tempFolderName = user != null ? user.getScreenName() : StringPool.BLANK;
	
	FileEntry fileEntry = (FileEntry)request.getAttribute("DOCUMENT_LIBRARY_FILE_ENTRY");

	long repositoryId = BeanParamUtil.getLong(fileEntry, request, "repositoryId");
	
	long folderId = BeanParamUtil.getLong(fileEntry, request, "folderId");
	
	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	long dossierFileId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_FILE_ID);

	if (repositoryId <= 0) {

		// add_asset.jspf only passes in groupId

		repositoryId = BeanParamUtil.getLong(fileEntry, request, "groupId");
	}

	int index = ParamUtil.getInteger(request, DossierFileDisplayTerms.INDEX);
	
	int level = ParamUtil.getInteger(request, DossierFileDisplayTerms.LEVEL);
	
	String groupName = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAME);
	
	String fileName = ParamUtil.getString(request, DossierFileDisplayTerms.FILE_NAME);
	
	String templateFileNo = ParamUtil.getString(request, DossierDisplayTerms.TEMPLATE_FILE_NO);
	
	String partType = ParamUtil.getString(request, DossierFileDisplayTerms.PART_TYPE);
	
	JSONObject responseData = (JSONObject)request.getAttribute(WebKeys.RESPONSE_UPLOAD_TEMP_DOSSIER_FILE);
	
%>

<portlet:actionURL var="addTempFileURL" name="addTempFile">
	<portlet:param name="<%=DossierFileDisplayTerms.TEMP_FOLDER_NAME %>" value="<%=tempFolderName%>"/>
	<portlet:param name="<%=DossierFileDisplayTerms.FOLDE_ID %>" value="<%=String.valueOf(folderId)%>"/>
</portlet:actionURL>

<portlet:actionURL var="addAttachmentFileURL" name="addAttachmentFile">
	<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID %>" value="<%=String.valueOf(dossier != null ? dossier.getDossierId() : 0L)%>"/>
	<portlet:param name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" value="<%=String.valueOf(dossierFileId)%>"/>
</portlet:actionURL>

<liferay-ui:error message="upload-error" key="upload-error"/>

<liferay-ui:error 
	exception="<%= DuplicateFileException.class %>" 
	message="<%= MessageKeys.DOSSIER_FILE_DUPLICATE_NAME %>"
/>

<aui:form 
	name="fm" 
	method="post" 
	action="<%=addAttachmentFileURL%>" 
	enctype="multipart/form-data"
>
	<aui:input name="redirectURL" type="hidden" value="<%=currentURL %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.INDEX %>" type="hidden" value="<%=index %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.LEVEL %>" type="hidden" value="<%=level %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.PART_TYPE %>" type="hidden" value="<%=partType %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_ID %>" type="hidden" value="<%=dossierFileId %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.GROUP_NAME %>" type="hidden" value="<%=groupName %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.FILE_NAME %>" type="hidden" value="<%=fileName %>"/>
	<aui:input name="<%=DossierDisplayTerms.TEMPLATE_FILE_NO %>" type="hidden" value="<%=templateFileNo %>"/>
	<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" type="hidden" value="<%=dossierPart != null ? dossierPart.getDossierpartId() : dossierPartId %>"/>
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%= DossierFileDisplayTerms.DISPLAY_NAME %>" type="text">
				<aui:validator name="required"/>
			</aui:input>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%= DossierFileDisplayTerms.DOSSIER_FILE_NO %>" type="text"/>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="100">
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
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_FILE_UPLOAD %>" type="file"/>
		</aui:col>
	</aui:row>
	
	<aui:row>
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
		
		var responseData = '<%=responseData != null ? responseData.toString() : StringPool.BLANK%>';
		
		var jsonData = {};
		
		if(success == 'true'){
			
			<portlet:namespace/>closeDialog();
		}
		
	});
	
	
	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
		Liferay.Util.getOpener().Liferay.Portlet.refresh('#p_p_id_<%= WebKeys.DOSSIER_MGT_PORTLET %>_');
		var dialog = Liferay.Util.getWindow('<portlet:namespace/>dossierFileId');
		dialog.destroy(); // You can try toggle/hide whate
	});

</aui:script>