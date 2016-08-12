
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

<%@ include file="../../init.jsp" %>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceFileTemplate"%>
<%@page import="org.opencps.servicemgt.service.ServiceFileTemplateLocalServiceUtil"%>
<%
	TemplateFile templateFile = (TemplateFile) request.getAttribute(WebKeys.SERVICE_TEMPLATE_ENTRY);
	long templateFileId = Validator.isNotNull(templateFile) ? templateFile.getTemplatefileId() : 0L;
	
	List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
	List<ServiceFileTemplate> serviceFileTemplates = new ArrayList<ServiceFileTemplate>();
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath +
		"template/add_to_service.jsp");
	
	try {
		if(templateFileId > 0) {
			serviceFileTemplates = ServiceFileTemplateLocalServiceUtil.getServiceFileTemplatesByTemplateFile(templateFileId);
		}
	} catch (Exception e) {
		
	}
	

%>

<portlet:renderURL var="chooseServiceInfosURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value='<%=templatePath + "edit_choose_serviceinfos.jsp" %>'/>
	<portlet:param name="templateFileId" value="<%=String.valueOf(templateFileId) %>"/>
	<portlet:param name="backURL" value="<%=currentURL %>"/>
</portlet:renderURL>

<aui:row>
	<aui:button name="choose" value="choose-serviceinfos" />
</aui:row>

<liferay-ui:search-container 
		emptyResultsMessage="no-service-were-found"
		iteratorURL="<%=iteratorURL %>"
		delta="<%=20 %>"
		deltaConfigurable="true"
>
	<liferay-ui:search-container-results>
		<%
			try {
				for(ServiceFileTemplate serviceFileTemplate : serviceFileTemplates) {
					serviceInfos.add(ServiceInfoLocalServiceUtil.fetchServiceInfo(serviceFileTemplate.getServiceinfoId()));
						}
			} catch(Exception e) {
				
			}
			
			results = serviceInfos;
			total = serviceInfos.size();
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
		<liferay-ui:search-container-row 
					className="org.opencps.servicemgt.model.ServiceInfo" 
					modelVar="service" 
					keyProperty="serviceinfoId"
				>
				<c:if test="<%=Validator.isNotNull(service) %>">
					<portlet:actionURL var="deteleRelaSeInfoAndTempFileURL" name="deteleRelaSeInfoAndTempFile" >
							<portlet:param name="templateFileId" value="<%=String.valueOf(templateFileId) %>"/>
							<portlet:param name="serviceInfoId" value="<%=String.valueOf(service.getServiceinfoId()) %>"/>
							<portlet:param name="backURL" value="<%=currentURL %>"/>
						</portlet:actionURL>
						
						<liferay-ui:search-container-column-text 
								name="row-index" value="<%= String.valueOf(row.getPos() +1 ) %>"
							/>
						<liferay-ui:search-container-column-text 
								name="service-no" value="<%=service.getServiceNo() %>"
							/>
						<liferay-ui:search-container-column-text 
								name="service-name" value="<%=service.getServiceName() %>"
							/>
						<%
							 final String hrefFix = "location.href='" + deteleRelaSeInfoAndTempFileURL .toString()+"'";
						%>
							
						<liferay-ui:search-container-column-button
							href="<%= hrefFix %>" 
							name="delete" 
						/>
				</c:if>
				
		</liferay-ui:search-container-row>
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>
<aui:script use="liferay-util-window">
	A.one('#<portlet:namespace/>choose').on('click', function(event) {
		Liferay.Util.openWindow({
			dialog : {
				centered : true,
				height : 900,
				modal : true,
				width : 1100
			},
			id : '<portlet:namespace/>dialog',
			title : '',
			uri : '<%=chooseServiceInfosURL %>'
		});
	});
</aui:script>

<aui:script>
	Liferay.provide(window, '<portlet:namespace/>closePopup', function(
			dialogId) {
		var A = AUI();
		// Closing the dialog
		var dialog = Liferay.Util.Window.getById(dialogId);
		dialog.destroy();
		
		window.location.reload();
	}, [ 'liferay-util-window' ]);
</aui:script>