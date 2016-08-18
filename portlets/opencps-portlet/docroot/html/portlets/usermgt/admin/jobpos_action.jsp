<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
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

<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.usermgt.search.JobPosDisplayTerms"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.usermgt.permissions.JobPosPermission"%>
<%@ include file="../init.jsp"%>

<%
	long workingUnitId = ParamUtil.getLong(request, "workingUnitId");
	ResultRow row =
		(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	JobPos jobPos = (JobPos)row.getObject();
%>

<%-- <liferay-ui:icon-menu> --%>
	<portlet:renderURL var="updateJobPos" windowState="<%=LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="mvcPath"
			value="/html/portlets/usermgt/admin/update_jobpos.jsp" />
		<portlet:param name="<%=JobPosDisplayTerms.ID_JOBPOS%>"
			value="<%=String.valueOf(jobPos.getJobPosId())%>" />
		<portlet:param name="workingUnitId" 
			value="<%=String.valueOf(workingUnitId) %>"/>
		<portlet:param name="redirectURL" value="<%=currentURL%>" />
	</portlet:renderURL>
	<c:if test="<%=JobPosPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<aui:button value="edit" onClick="<%= \"javascript:\" + renderResponse.getNamespace() + \"showPopup('\" + updateJobPos +\"');\" %>"></aui:button>
	
	</c:if>

	<portlet:actionURL var="deleteJobPosURL" name="deleteJobPos">
		<portlet:param name="<%=JobPosDisplayTerms.ID_JOBPOS%>"
			value="<%=String.valueOf(jobPos.getJobPosId())%>" />
		<portlet:param name="redirectURL" value="<%=currentURL%>" />
	</portlet:actionURL>
	
	<c:if test="<%=JobPosPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
		<liferay-ui:icon-delete cssClass="search-container-action fa delete" image="delete" message="delete"
		url="<%=deleteJobPosURL.toString()%>" confirmation="do-you-want-to-delete-entry" />
	</c:if>

<aui:script>
	Liferay.provide(window, '<portlet:namespace />showPopup', function(url){
		Liferay.Util.openWindow({
			dialog : {
				centered : true,
				height : 800,
				modal : true,
				width : 1000
			},
			id : '<portlet:namespace/>dialog',
			title : '',
			uri : url
		});
	});
</aui:script>