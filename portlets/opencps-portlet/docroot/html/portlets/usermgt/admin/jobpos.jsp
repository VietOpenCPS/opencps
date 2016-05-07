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

<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.search.JobPosSearchTerms"%>
<%@page import="org.opencps.usermgt.search.JobPosSearch"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.usermgt.permissions.JobPosPermission"%>
<%@ include file="../init.jsp"%>

<%
	long workingUnitId = ParamUtil.getLong(request, "workingUnitId");
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "jobpos.jsp");
	List<JobPos> jobPos = new ArrayList<JobPos>();
	int totalCount = 0;
%>

<liferay-ui:error 
	key="<%=MessageKeys.USERMGT_JOBPOS_DELETE_ERROR %>" 
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_DELETE_ERROR) %>"
/>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_JOBPOS_DELETE_SUCCESS %>"
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_DELETE_SUCCESS) %>"
/>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_JOBPOS_UPDATE_SUCESS %>"
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_JOBPOS_UPDATE_SUCESS) %>"
/>

<portlet:renderURL var="updateJobPosURL">
	<portlet:param name="mvcPath" value='<%=templatePath + "edit_jobpos.jsp" %>'/>
	<portlet:param name="workingUnitId" value="<%=String.valueOf(workingUnitId) %>"/>
	<portlet:param name="redirectURL" value="<%=currentURL %>"/>
</portlet:renderURL>

<c:if test="<%=JobPosPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_JOBPOS	) %>">
	<aui:button 
	name="add-jobpos"
	value="add-jobpos"
	onClick ="<%=updateJobPosURL.toString() %>"	
/>
</c:if>

		

<liferay-ui:search-container searchContainer="<%= new JobPosSearch(renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	
	<liferay-ui:search-container-results>
		<%@include file="/html/portlets/usermgt/admin/jobpos_search_results.jspf"%>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.usermgt.model.JobPos" 
		modelVar="jobPosSearch" 
		keyProperty="jobPosId"
	>
		<%
			String leaderName = PortletUtil.getLeaderLabel(jobPosSearch.getLeader(), locale);
		    row.addText(String.valueOf(row.getPos() + 1));
			row.addText(jobPosSearch.getTitle());
			row.addText(leaderName);
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  templatePath +
				"jobpos_action.jsp", config.getServletContext(), request, response);
		%>
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
