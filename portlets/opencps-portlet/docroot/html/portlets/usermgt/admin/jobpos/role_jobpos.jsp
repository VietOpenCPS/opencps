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
<%@page import="org.opencps.usermgt.search.ResourceActionSearch"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portal.model.ResourceAction"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@ include file="../../init.jsp"%>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath +
		"jobpos/role_jobpos.jsp");
	List<ResourceAction> resourceActions = new ArrayList<ResourceAction>();
	int totalCount = 0;
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

<aui:script>
AUI().ready(        

	    function(customA) {
	        customA.all('.taglib-page-iterator').hide(); 
	    }
	);

/*  Liferay.provide(window, '<portlet:namespace />updateJobPoses',
		 	function() {
	 var resourceActionIds = Liferay.Util
	 .listCheckedExcept(document.<portlet:namespace />fm, '<portlet:namespace />allRowIds'); 
	 
	 if(resourceActionIds && 
			 confirm('<%= UnicodeLanguageUtil
					 .get(pageContext, 
							 "are-you-sure-you-want-to-add-the-selected-messages") %>')) {
		document.<portlet:namespace />fm.<portlet:namespace />resourceActionIds.value = resourceActionIds;
		
		submitForm(document.<portlet:namespace />updateJobPoses);
	 }
	 
 }); */
</aui:script>