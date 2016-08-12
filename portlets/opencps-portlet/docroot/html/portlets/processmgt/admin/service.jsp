
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.util.WebKeys"%>
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

<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.service.ServiceInfoProcessLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ServiceInfoProcess"%>
<%@page import="org.opencps.processmgt.model.ServiceProcess"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>

<%@ include file="../init.jsp" %>
<%
	ServiceProcess serviceProcess = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	
	long serviceProcessId = Validator.isNotNull(serviceProcess) ? serviceProcess.getServiceProcessId() : 0L;
	
	List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
	List<ServiceInfoProcess> serviceInfoProcesses = new ArrayList<ServiceInfoProcess>();
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath +
		"process/service.jsp");
	
	try {
		serviceInfoProcesses = ServiceInfoProcessLocalServiceUtil.getServiceInfoProcessByProcessId(serviceProcessId);
		
	} catch (Exception e) {
		
	}
	
%>

<portlet:renderURL var="chooseServiceURL" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="serviceProcessId" value="<%=String.valueOf(serviceProcessId) %>"/>
	<portlet:param name="backURL" value="<%=currentURL %>"/>
	<portlet:param name="mvcPath" value='<%=templatePath + "choose_service.jsp" %>'/>
</portlet:renderURL>

<aui:button name="chooseService" value="choose-service" />


<liferay-ui:search-container 
		emptyResultsMessage="no-service-were-found"
		iteratorURL="<%=iteratorURL %>"
		delta="<%=20 %>"
		deltaConfigurable="true"
>
	<liferay-ui:search-container-results>
		<%
			try {
				for(ServiceInfoProcess serviceInfoProcess : serviceInfoProcesses) {
					serviceInfos.add(ServiceInfoLocalServiceUtil.getServiceInfo(serviceInfoProcess.getServiceinfoId()));
				}
			} catch (Exception e) {
				
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
	
		<portlet:actionURL var="deteleRelaSeInfoAndProcessURL" name="deteleRelaSeInfoAndProcess" >
			<portlet:param name="serviceProcessId" value="<%=String.valueOf(serviceProcessId) %>"/>
			<portlet:param name="serviceInfoId" value="<%=String.valueOf(service.getServiceinfoId()) %>"/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:actionURL>
		
		<liferay-ui:search-container-column-text 
				name="row-index" value="<%= String.valueOf(row.getPos() +1 ) %>"
			/>
		<liferay-ui:search-container-column-text 
				name="service-name" value="<%=service.getServiceName() %>"
			/>
		<liferay-ui:search-container-column-text 
				name="service-domain" value="<%=DictItemUtil.getNameDictItem(service.getDomainCode()) %>"
			/>
		<liferay-ui:search-container-column-text 
				name="service-administration" value="<%=DictItemUtil.getNameDictItem(service.getAdministrationCode()) %>"
			/>
		<%
			 final String hrefFix = "location.href='" + deteleRelaSeInfoAndProcessURL .toString()+"'";
		%>
			
		<liferay-ui:search-container-column-button
			href="<%= hrefFix %>" 
			name="delete" 
		/>
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
</liferay-ui:search-container>

<aui:script>

	AUI().ready(function(A) {
		var btnChoose = A.one("#<portlet:namespace/>chooseService");
		
		if(btnChoose) {
			btnChoose.on('click', function(){
				Liferay.Util.openWindow({
					dialog : {
						centered : true,
						height : 900,
						modal : true,
						width : 1100
					},
					id : '<portlet:namespace/>dialog',
					title : '',
					uri : '<%=chooseServiceURL %>'
				});
			});
		}
		
	});
	
</aui:script>
