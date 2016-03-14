
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
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="org.opencps.datamgt.permissions.DictCollectionPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.datamgt.search.DictCollectionDisplayTerms"%>
<%@ include file="../init.jsp"%>

 
<%
	ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	DictCollection dictCollection = (DictCollection) row.getObject();
%> 

			
 <liferay-ui:icon-menu>
 	<c:if test="<%=DictCollectionPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_DICTCOLLECTION) %>">
 		<portlet:renderURL var="updateDictCollectionURL">
			<portlet:param name="mvcPath" value="/html/portlets/data_management/admin/edit_dictcollection.jsp"/>
			<portlet:param name="<%=DictCollectionDisplayTerms.DICTCOLLECTION_ID %>" value="<%=String.valueOf(dictCollection.getDictCollectionId()) %>"/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:renderURL> 
 		<liferay-ui:icon image="edit" message="edit" url="<%=updateDictCollectionURL.toString() %>" /> 
 	</c:if>
 	
 	<c:if test="<%=DictCollectionPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
 		<portlet:actionURL var="deleteDictCollectionURL" name="deleteDictCollection" >
			<portlet:param name="<%=DictCollectionDisplayTerms.DICTCOLLECTION_ID %>" value="<%=String.valueOf(dictCollection.getDictCollectionId()) %>"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:actionURL> 
		<liferay-ui:icon-delete image="delete" confirmation="are-you-sure-delete-entry" message="delete"  url="<%=deleteDictCollectionURL.toString() %>" />
 	</c:if>
	  
</liferay-ui:icon-menu> 