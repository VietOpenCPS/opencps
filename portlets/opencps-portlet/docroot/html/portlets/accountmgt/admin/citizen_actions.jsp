
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
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.accountmgt.permissions.CitizenPermission"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../init.jsp" %>

<%
	ResultRow row =	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

	citizen = (Citizen)row.getObject();

%>

<%-- <liferay-ui:icon-menu> --%>
	<c:if test="<%=CitizenPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="updateCitizen"> 
			<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=String.valueOf(citizen.getCitizenId()) %>"/>
			<portlet:param name="mvcPath" value="/html/portlets/accountmgt/admin/update_profile.jsp"/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:renderURL>
	
		<liferay-ui:icon 
			image="edit" 
			cssClass="edit"
			message="edit"
			url="<%=updateCitizen.toString()%>" 
		/>
	</c:if>
	
	<c:if test="<%= citizen.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_CONFIRMED%>">
		<c:if test="<%=CitizenPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
			<portlet:actionURL var="updateStatusURL" name="updateStatus">
				<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS %>" value="<%=String.valueOf(PortletConstants.ACCOUNT_STATUS_APPROVED) %>"/>
				<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=String.valueOf(citizen.getCitizenId()) %>"/>
				<portlet:param name="curAccountStatus" value="<%=String.valueOf(citizen.getAccountStatus()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
	
			<liferay-ui:icon
			 	image="publish"
			 	cssClass="publish" 
			 	message="approve"
				url="<%= updateStatusURL.toString()%>" 
			/>
		</c:if>
	</c:if>
	
	<c:if test="<%= citizen.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_APPROVED%>">
		<c:if test="<%=CitizenPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
			<portlet:actionURL var="updateStatusURL" name="updateStatus">
				<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS %>" value="<%=String.valueOf(PortletConstants.ACCOUNT_STATUS_LOCKED) %>"/>
				<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=String.valueOf(citizen.getCitizenId()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
	
			<liferay-ui:icon
				image="deactivate"
			 	label="deactivate"
			 	cssClass="deactivate"
				url="<%= updateStatusURL.toString()%>" 
			/>
		</c:if>
	</c:if>
	
	<c:if test="<%= citizen.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_LOCKED%>">
		<c:if test="<%=CitizenPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
			<portlet:actionURL var="updateStatusURL" name="updateStatus">
				<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS %>" value="<%=String.valueOf(PortletConstants.ACCOUNT_STATUS_APPROVED) %>"/>
				<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=String.valueOf(citizen.getCitizenId()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
	
			<liferay-ui:icon
			 	image="activate"
			 	cssClass="activate" 
			 	message="activate"
				url="<%= updateStatusURL.toString()%>" 
			/>
		</c:if>
	</c:if>
	
	<c:if test="<%= citizen.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_CONFIRMED ||
		citizen.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_REGISTERED ||
		citizen.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_LOCKED%>">
	
		<c:if test="<%=CitizenPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
			<portlet:actionURL var="deleteCitizenURL" name="deleteCitizen">
				<portlet:param name="<%=CitizenDisplayTerms.CITIZEN_ID %>" value="<%=String.valueOf(citizen.getCitizenId()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
			
			<liferay-ui:icon-delete
			 	confirmation="are-you-sure-remove-this-account"
			 	message="delete"
			 	cssClass="delete"
				url="<%=deleteCitizenURL.toString()%>" 
			/>
		</c:if>
	</c:if>

<%-- </liferay-ui:icon-menu> --%>