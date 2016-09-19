
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
<%@page import="org.opencps.processmgt.util.ProcessMgtUtil"%>
<%@page import="org.opencps.processmgt.permissions.ProcessPermission"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.processmgt.service.ServiceProcessLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessSearchTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessSearch"%>

<%@ include file="../init.jsp" %>

<liferay-ui:error 
	key="<%=ProcessMgtUtil.DELERR_EXIST_SERVICE_CONFIG %>" 
	message="<%=ProcessMgtUtil.DELERR_EXIST_SERVICE_CONFIG %>" 
/>

<liferay-ui:error 
	key="<%=ProcessMgtUtil.DELERR_EXIST_STEP %>" 
	message="<%=ProcessMgtUtil.DELERR_EXIST_STEP %>" 
/>

<liferay-ui:error 
	key="<%=ProcessMgtUtil.DELERR_EXIST_WORKFLOW %>" 
	message="<%=ProcessMgtUtil.DELERR_EXIST_WORKFLOW %>" 
/>

<liferay-ui:success 
	key="<%=ProcessMgtUtil.DEL_PROCESS_SUCC %>" 
	message="<%=ProcessMgtUtil.DEL_PROCESS_SUCC %>" 
/>
	
<liferay-util:include page='<%= templatePath + "toolbar.jsp" %>' servletContext="<%= application %>" />

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "serviceprocesslist.jsp");
	
	boolean isPermission =
				    ProcessPermission.contains(
				        themeDisplay.getPermissionChecker(),
				        themeDisplay.getScopeGroupId(), ActionKeys.ADD_PROCESS);

%>

<div class="opencps-searchcontainer-wrapper default-box-shadow radius8">

	<liferay-ui:search-container searchContainer="<%= new ProcessSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
			
		<liferay-ui:search-container-results>
			<%
				ProcessSearchTerms searchTerms = (ProcessSearchTerms) searchContainer.getSearchTerms();
	
				total = ServiceProcessLocalServiceUtil.countProcess(scopeGroupId, searchTerms.getKeywords()); 
	
				results = ServiceProcessLocalServiceUtil.searchProcess(scopeGroupId, searchTerms.getKeywords(),
					searchContainer.getStart(), searchContainer.getEnd());
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
			
		</liferay-ui:search-container-results>
	
		<liferay-ui:search-container-row 
			className="org.opencps.processmgt.model.ServiceProcess" 
			modelVar="process" 
			keyProperty="serviceProcessId"
		>
			<%
				PortletURL editURL = renderResponse.createRenderURL();
				editURL.setParameter("mvcPath", templatePath + "edit_process.jsp");
				editURL.setParameter("serviceProcessId", String.valueOf(process.getServiceProcessId()));
				editURL.setParameter("backURL", currentURL);
				
			%>
		
			<liferay-util:buffer var="rowIndex">
				<div class="row-fluid min-width10">
					<div class="span12 bold">
						<a href="<%=editURL%>"><%=row.getPos() + 1 %></a>
					</div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="processInfo">
				<div class="row-fluid">
					<div class="span3 bold">
						<liferay-ui:message key="process-no"/>
					</div>
					<div class="span9">
						<a href="<%=editURL%>"><%= process.getProcessNo() %></a>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span3 bold">
						<liferay-ui:message key="process-name"/>
					</div>
					<div class="span9">
						<a href="<%=editURL%>"><%= process.getProcessName()  %></a>
					</div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="description">
				<div class="row-fluid">
					<div class="span3 bold">
						<liferay-ui:message key="description"/>
					</div>
					<div class="span9">
						<a href="<%=editURL%>"><%= process.getDescription() %></a>
					</div>
				</div>
				
			</liferay-util:buffer>
			
			<%
				
				row.addText(rowIndex);
				
				row.addText(processInfo);
				
				row.addText(description);
				
				if(isPermission) {
					//action column
					row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "process_actions.jsp", config.getServletContext(), request, response);
				}
			%>	
		
		</liferay-ui:search-container-row>	
	
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	
	</liferay-ui:search-container>

</div>
