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

<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="org.opencps.dossiermgt.DuplicateFileGroupException"%>
<%@page import="org.opencps.dossiermgt.EmptyFileGroupException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierFileDisplayTerms"%>
<%@ include file="/init.jsp"%>

<%
	boolean success = false;
	
	try{
		success = !SessionMessages.isEmpty(renderRequest) && SessionErrors.isEmpty(renderRequest);
		
	}catch(Exception e){
		
	}

	long dossierId = ParamUtil.getLong(request, DossierDisplayTerms.DOSSIER_ID);

	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);
	
	String modalDialogId = ParamUtil.getString(request, "modalDialogId");
	
	//System.out.println(modalDialogId);
	
	String redirectURL = ParamUtil.getString(request, "redirectURL");
%>

<liferay-ui:error 
	exception="<%= NoSuchDossierException.class %>" 
	message="<%= NoSuchDossierException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= EmptyFileGroupException.class %>" 
	message="<%= EmptyFileGroupException.class.getName() %>" 
/>
<liferay-ui:error 
	exception="<%= DuplicateFileGroupException.class %>" 
message="<%= DuplicateFileGroupException.class.getName() %>" 
/>
<portlet:actionURL var="addIndividualPartGroupURL" name="addIndividualPartGroup"/>

<aui:form name="fm" method="post" action="<%=addIndividualPartGroupURL.toString() %>" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "addIndividualPartGroup();" %>'> 
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%= DossierFileDisplayTerms.PART_NAME %>" type="text">
				<aui:validator name="required"/>
			</aui:input>
			<aui:input name="redirectURL" type="hidden" value="<%=Validator.isNull(redirectURL) ? currentURL : redirectURL %>"/>
			<aui:input name="<%=DossierDisplayTerms.DOSSIER_ID %>" type="hidden" value="<%=dossierId %>"/>
			<aui:input name="<%=DossierFileDisplayTerms.DOSSIER_PART_ID %>" type="hidden" value="<%=dossierPartId %>"/>
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
		
		if(cancelButton){
			cancelButton.on('click', function(){
				closeDialog('<portlet:namespace/>' + '<%=modalDialogId%>', null);
			});
		}
		
		var success = '<%=success%>';
		
		if(success == 'true'){
			closeDialog('<portlet:namespace/>add-individual-part-group', '<%=WebKeys.DOSSIER_MGT_PORTLET%>_');
		}
	});

	Liferay.provide(window, '<portlet:namespace/>addIndividualPartGroup', function() {
		var A = AUI();
		
		submitForm(document.<portlet:namespace />fm);
	});
	
</aui:script>