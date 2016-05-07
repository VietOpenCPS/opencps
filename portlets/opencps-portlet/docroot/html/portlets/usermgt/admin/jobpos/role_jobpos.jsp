
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

<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="com.liferay.portal.service.ResourceActionLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.search.ResourceActionSearchTerm"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="java.util.Collection"%>
<%@page import="com.liferay.portal.kernel.util.ContextPathUtil"%>
<%@page import="com.liferay.portal.model.ResourceConstants"%>
<%@page import="com.sun.xml.internal.bind.v2.runtime.unmarshaller.Scope"%>
<%@page import="com.liferay.portal.service.ResourcePermissionLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.ResourcePermission"%>
<%@page import="org.opencps.usermgt.search.ResourceActionSearch"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.model.ResourceAction"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@ include file="../../init.jsp"%>


<liferay-ui:success 
	key="DELETE_PERMISSION_SUCCESS"
	message="DELETE_PERMISSION_SUCCESS"
/>

<liferay-ui:error 
	key="DELETE_PERMISSION_ERROR"
	message="DELETE_PERMISSION_ERROR"
/>

<%
	JobPos jobPos = (JobPos) request.getAttribute(WebKeys.JOBPOS_ENTRY);	

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath +
		"jobpos/role_jobpos.jsp");
	List<ResourceAction> resourceActions = new ArrayList<ResourceAction>();
	List<ResourcePermission> resourcePermissions = new ArrayList<ResourcePermission>();
	int totalCount = 0;
	int [] scopes = new int[1];
	scopes[0] = ResourceConstants.SCOPE_GROUP;
	long jobPosId = Validator.isNotNull(jobPos) ? jobPos.getJobPosId() : 0L;
	int j = 0;
	List<Long> permissionIds = new ArrayList<Long>();
%>

<%
	String [] listActionName= {
		PortletPropsValues.USERMGT_WORKINGUNIT_RESOURCE,
		PortletPropsValues.USERMGT_JOBPOS_RESOURCE, 
		PortletPropsValues.USERMGT_EMPLOYEE_RESOURCE
	};
	for(int i = 0; i < listActionName.length; i++) {
			if(i == 0) {
				%>
					<liferay-ui:message key="working-unit"/>
				<% 	
			} else if(i == 1) {
				%>
					<liferay-ui:message key="job-pos"/>
				<% 
			} else if(i == 2) {
				%>
				<liferay-ui:message key="employee"/>
			<% 	
		}
		%>
			
			<liferay-ui:search-container 
				searchContainer="<%= new ResourceActionSearch(
					renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
				rowChecker="<%=new RowChecker(renderResponse)%>"
			>
	
				<liferay-ui:search-container-results>
					<%
						ResourceActionSearchTerm searchTerms = 
						(ResourceActionSearchTerm) searchContainer.getSearchTerms();
						
						resourceActions = ResourceActionLocalServiceUtil
										.getResourceActions(listActionName[i]);
																
						totalCount = resourceActions.size();
						total = totalCount;
						results = resourceActions;
						pageContext.setAttribute("results", results);
						pageContext.setAttribute("total", total);
					%>
				</liferay-ui:search-container-results>
				
				<liferay-ui:search-container-row 
					className="com.liferay.portal.model.ResourceAction" 
					modelVar="resourceAction" 
					keyProperty="resourceActionId"
				>
					<%
						row.addText(resourceAction.getActionId());
					%>
				</liferay-ui:search-container-row>
				
				<liferay-ui:search-iterator/>
			</liferay-ui:search-container>
			
			<%-- <aui:input 
				name="<resourceActionIds" 
				type="hidden" 
			/> --%>
		<%
		
	}
%>


	<c:if test="<%= jobPosId > 0 %>">
		<liferay-ui:search-container 
		emptyResultsMessage="no-permission-were-found"
		iteratorURL="<%=iteratorURL %>"
		delta="<%=20 %>"
		deltaConfigurable="true"
		>
			<liferay-ui:search-container-results>
				<%
					resourcePermissions = ResourcePermissionLocalServiceUtil
						.getRoleResourcePermissions
							(jobPos.getMappingRoleId(), 
								scopes, searchContainer.getStart(), searchContainer.getEnd());
					List<ResourceAction> hasPermissions = new ArrayList<ResourceAction>();
					for(ResourcePermission resourcePermission : resourcePermissions) {
						List<ResourceAction> activeResoueceAction = 
							ResourceActionLocalServiceUtil.getResourceActions(resourcePermission.getName());
						for(int i = 0; i < UserMgtUtil.getBinary(resourcePermission.getActionIds()).size(); i++) {
							int binaryIndexValue = UserMgtUtil.getBinary(resourcePermission.getActionIds()).get(i);
							if(binaryIndexValue == 1) {
								double bitwide = Math.pow(2, i);
								for(ResourceAction resAction : activeResoueceAction) {
									if(resAction.getBitwiseValue() == (long) bitwide) {
										hasPermissions.add(resAction);
										permissionIds.add(resourcePermission.getResourcePermissionId());
									}
								}
							}
							
						}
					}
				
					totalCount = hasPermissions.size();
					results = hasPermissions;
					total = totalCount;
					pageContext.setAttribute("results", results);
					pageContext.setAttribute("total", total);
				%>
			</liferay-ui:search-container-results>
			<liferay-ui:search-container-row 
					className="com.liferay.portal.model.ResourceAction" 
					modelVar="actionInPermission" 
					keyProperty="resourceActionId"
				>
					<portlet:actionURL name="deletePermissionAction" var="deletePermissionActionURL">
						<portlet:param name="bitwide" value="<%=String.valueOf(actionInPermission.getBitwiseValue()) %>"/>
						<portlet:param name="permissionId" value="<%=String.valueOf(permissionIds.get(j)) %>"/>
						<portlet:param name="returnURL" value="<%=currentURL %>"/>
					</portlet:actionURL>
					
					<liferay-ui:search-container-column-text 
						name="portlet-name" value="<%= portletDisplay.getTitle() %>"
					/>
					
					<liferay-ui:search-container-column-text 
						name="permission-name" value="<%= actionInPermission.getName()%>"
					/>
					
					<liferay-ui:search-container-column-text
						name="permission-role" value="<%= actionInPermission.getActionId()%>"
					/>
					
					 <%
						 final String hrefFix = "location.href='" + deletePermissionActionURL .toString()+"'";
					 %>
					
					<liferay-ui:search-container-column-button
						
						href="<%= hrefFix %>" 
						name="delete" />
					<%
						j++;
					%>
					
				</liferay-ui:search-container-row>
			<liferay-ui:search-iterator/>
		</liferay-ui:search-container>
	</c:if>
	<%
		j=0;
	%>
<aui:script>
AUI().ready(

	    function(customA) {
	        customA.all('.taglib-page-iterator').hide(); 
	    }
	);

</aui:script>