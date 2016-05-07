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
<%@ include file="../init.jsp"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.DuplicateDossierTemplateNumberException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierTemplateNumberException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierTemplateNameException"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%

	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
	long dossierTemplateId = dossierTemplate != null ? dossierTemplate.getDossierTemplateId() : 0L;	
	
	PortletURL setRenderTemplateId = renderResponse.createRenderURL();
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	String [] dossierTemplateSections = null;
	
	if(dossierTemplateId == 0) {
		dossierTemplateSections = new String [1];
		dossierTemplateSections[0] = "edit_dossier_template";
	} else {
		dossierTemplateSections = new String[3];
		dossierTemplateSections[0] = "edit_dossier_template";
		dossierTemplateSections[1] = "dossierpartlist";
		dossierTemplateSections[2] = "dossierservicelist";
	}
	session.setAttribute(DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID, dossierTemplateId);
	String[][] categorySections = {dossierTemplateSections};
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="update-dossier"
	backLabel="back"
/>

<liferay-ui:error 
	exception="<%=OutOfLengthDossierTemplateNameException.class %>"
	message="<%= OutOfLengthDossierTemplateNameException.class.getName() %>"
/>

<liferay-ui:error 
	exception="<%=OutOfLengthDossierTemplateNumberException.class %>"
	message="<%= OutOfLengthDossierTemplateNumberException.class.getName() %>"
/>

<liferay-ui:error 
	exception="<%=DuplicateDossierTemplateNumberException.class %>"
	message="<%= DuplicateDossierTemplateNumberException.class.getName() %>"
/>

<liferay-ui:error 
	key="<%= MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED %>"
	message="<%= MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED %>"
/>

<portlet:renderURL var="renderToToolBar" windowState="<%=LiferayWindowState.EXCLUSIVE.toString()%>">
	<portlet:param name="mvcPath" value='<%= templatePath + "toolbar.jsp" %>'/>
</portlet:renderURL>

<portlet:actionURL name="updateDossier" var="updateDossierURL" >
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
	<portlet:param name="backURL" value="<%=backURL %>"/>
</portlet:actionURL>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%=dossierTemplate!=null %>">
		<h4><%=dossierTemplate.getTemplateName() %></h4>
	</c:if>
</liferay-util:buffer>

<liferay-util:buffer var="htmlBot">
	<div class="button-holder ">
			<aui:button name="submit" type="submit" value="submit"/>
			<aui:button name="cancel" value="cancel" href="<%=backURL %>"/>	
	</div>
</liferay-util:buffer>

<aui:form name="fm" 
	method="post" 
	action="<%=updateDossierURL.toString() %>">
	<liferay-ui:form-navigator 
		backURL="<%= currentURL %>"
		categoryNames= "<%= DossierMgtUtil._DOSSIER_CATEGORY_NAMES %>"	
		categorySections="<%=categorySections %>" 
		htmlBottom="<%= htmlBot %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "dossier_common/" %>'
		showButtons="false"
		
		>	
	</liferay-ui:form-navigator>
</aui:form>
<aui:script>
	
	AUI().ready('liferay-portlet-url',function(A) {	
		var dossierPartLink = A.one('#<portlet:namespace />dossierpartlistLink');
		var dossierServiceLink = A.one('#<portlet:namespace />dossierservicelistLink'); 
		var dossierTemplateLink = A.one('#<portlet:namespace />edit_dossier_templateLink');
		
		if(dossierTemplateLink) {
			var submitBtn = A.one('#<portlet:namespace />submit');
			dossierTemplateLink.on('click',function(){
				submitBtn.show();
			});
		} 
		
		if(dossierPartLink) {
			var submitBtn = A.one('#<portlet:namespace />submit');
			dossierPartLink.on('click',function(){
				submitBtn.hide();
				<portlet:namespace />sentToolBarSignal('dossierPartToolBar');
			});
		} 
		
		if(dossierServiceLink) {
			var submitBtn = A.one('#<portlet:namespace />submit');
			dossierServiceLink.on('click',function(){
				submitBtn.hide();
				<portlet:namespace />sentToolBarSignal('serviceConfigToolBar');
			});
		} 
		
	});
	
Liferay.provide(window, '<portlet:namespace/>sentToolBarSignal', function(tbSignal) {
		
		var A = AUI();
		
		A.io.request(
			'<%= renderToToolBar.toString() %>',
			{
				dataType : 'text/html',
				method : 'GET',
			    data:{    	
			    	"<portlet:namespace />tabs1" : tbSignal,
			    	"<portlet:namespace />currURL" : '<%=currentURL %>'
			    },   
			    on: {
			    	success: function(event, id, obj) {
						var instance = this;
						var res = instance.get('responseData');
						
						var toolbarResponse = A.all("#<portlet:namespace/>toolbarResponse");
						
						if(toolbarResponse){
							toolbarResponse.empty();
							toolbarResponse.html(res);
						}
						
					},
			    	error: function(){}
				}
			}
		);
	},['aui-base','aui-io']);
</aui:script>