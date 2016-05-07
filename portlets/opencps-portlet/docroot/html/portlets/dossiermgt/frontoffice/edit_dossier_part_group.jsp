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
<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.hsqldb.SessionManager"%>
<%@ include file="../init.jsp"%>

<%

	DossierFile dossierFile = (DossierFile) request.getAttribute(WebKeys.DOSSIER_FILE_ENTRY);

	DossierPart dossierPart = (DossierPart) request.getAttribute(WebKeys.DOSSIER_PART_ENTRY);

	long dossierPartId = ParamUtil.getLong(request, DossierFileDisplayTerms.DOSSIER_PART_ID);

	int index = ParamUtil.getInteger(request, DossierFileDisplayTerms.INDEX);
	
	int size = ParamUtil.getInteger(request, DossierFileDisplayTerms.SIZE);
	
	String groupNames = ParamUtil.getString(request, DossierFileDisplayTerms.GROUP_NAMES);
	
%>


<aui:form name="fm" method="post" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "addPrivateDossierGroup();" %>'> 
	<aui:row>
		<aui:col width="100">
			<aui:input name="<%= DossierFileDisplayTerms.PART_NAME %>" type="text" cssClass="input97">
				<aui:validator name="required"/>
			</aui:input>
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
				<portlet:namespace/>closeDialog();
			});
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>responseData', function(schema) {
		var Util = Liferay.Util;
		Util.getOpener().Liferay.fire('getPrivateDossierGroupSchema', {responseData:schema});
		<portlet:namespace/>closeDialog();
	});
	
	Liferay.provide(window, '<portlet:namespace/>closeDialog', function() {
		var dialog = Liferay.Util.getWindow('<portlet:namespace/>privateDossierGroup');
		dialog.destroy(); // You can try toggle/hide whate
	});
	
	Liferay.provide(window, '<portlet:namespace/>addPrivateDossierGroup', function() {
		var A = AUI();
		var groupNames = '<%=groupNames%>';
		var dossierPartId = '<%=dossierPartId%>';
		var index = '<%=index%>';
		var size = '<%=size%>';
		groupNames = groupNames.split(',');
		
		var partName = A.one('#<portlet:namespace/>partName').val().trim();
		if(groupNames.indexOf(partName) > -1){
			alert('<%=UnicodeLanguageUtil.get(pageContext, "group-name-existing")%>');
		}else{
			<portlet:namespace/>responseData({groupName:partName, dossierPartId:dossierPartId, index:index, size:size});
		}
	});
	
</aui:script>