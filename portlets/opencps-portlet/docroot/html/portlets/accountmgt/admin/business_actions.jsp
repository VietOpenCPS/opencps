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

<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.accountmgt.permissions.BusinessPermission"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../init.jsp" %>


<%
	ResultRow row =	(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

	Business business = (Business)row.getObject();
%>

<liferay-ui:icon-menu>
	<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="updateBusiness"> 
			<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" value="<%=String.valueOf(business.getBusinessId()) %>"/>
			<portlet:param name="mvcPath" value="/html/portlets/accountmgt/admin/update_profile.jsp"/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:renderURL>
	
		<liferay-ui:icon 
			image="edit" 
			message="edit"
			url="<%=updateBusiness.toString()%>" 
		/>
	</c:if>
	
	<c:if test="<%= business.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_CONFIRMED%>">
		<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
			<portlet:actionURL var="updateStatusURL" name="updateStatus">
				<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS %>" value="<%=String.valueOf(PortletConstants.ACCOUNT_STATUS_APPROVED) %>"/>
				<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" value="<%=String.valueOf(business.getBusinessId()) %>"/>
				<portlet:param name="curAccountStatus" value="<%=String.valueOf(business.getAccountStatus()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
	
			<liferay-ui:icon
			 	image="publish" 
			 	message="approve"
				url="<%= updateStatusURL.toString()%>" 
			/>
		</c:if>
	</c:if>
	
	<c:if test="<%= business.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_APPROVED%>">
		<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
			<portlet:actionURL var="updateStatusURL" name="updateStatus">
				<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS %>" value="<%=String.valueOf(PortletConstants.ACCOUNT_STATUS_LOCKED) %>"/>
				<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" value="<%=String.valueOf(business.getBusinessId()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
	
			<liferay-ui:icon-deactivate
			 	label="deactivate"
				url="<%= updateStatusURL.toString()%>" 
			/>
		</c:if>
	</c:if>
	
	<c:if test="<%= business.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_LOCKED%>">
		<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.UPDATE) %>">
			<portlet:actionURL var="updateStatusURL" name="updateStatus">
				<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_ACCOUNTSTATUS %>" value="<%=String.valueOf(PortletConstants.ACCOUNT_STATUS_APPROVED) %>"/>
				<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" value="<%=String.valueOf(business.getBusinessId()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
	
			<liferay-ui:icon
			 	image="activate" 
			 	message="activate"
				url="<%= updateStatusURL.toString()%>" 
			/>
		</c:if>
	</c:if>
	
	<c:if test="<%= business.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_CONFIRMED ||
		business.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_REGISTERED ||
		business.getAccountStatus() == PortletConstants.ACCOUNT_STATUS_LOCKED%>">
	
		<c:if test="<%=BusinessPermission.contains(permissionChecker, scopeGroupId, ActionKeys.DELETE) %>">
			<portlet:actionURL var="deleteBusinessURL" name="deleteBusiness">
				<portlet:param name="<%=BusinessDisplayTerms.BUSINESS_BUSINESSID %>" value="<%=String.valueOf(business.getBusinessId()) %>"/>
				<portlet:param name="redirectURL" value="<%=currentURL %>"/>
			</portlet:actionURL>
			
			<liferay-ui:icon-delete
				confirmation="are-you-sure-remove-this-account"
			 	message="delete"
				url="<%=deleteBusinessURL.toString()%>" 
			/>
		</c:if>
	</c:if>

</liferay-ui:icon-menu>