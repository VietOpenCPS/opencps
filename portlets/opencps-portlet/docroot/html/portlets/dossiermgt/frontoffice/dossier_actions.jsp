
<%@page import="org.opencps.backend.util.BackendUtils"%>
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

<%@page
	import="org.opencps.processmgt.service.ProcessWorkflowLocalServiceUtil"%>
<%@page
	import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessWorkflow"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.dossiermgt.bean.DossierBean"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="org.opencps.dossiermgt.permissions.DossierPermission"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>

<%@page pageEncoding="UTF-8"%>

<%@ include file="../init.jsp"%>


<%
	ServiceConfig serviceConfig =
		(ServiceConfig) request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	ResultRow row =
		(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	DossierBean dossierBean = (DossierBean) row.getObject();

	Dossier dossier = dossierBean.getDossier();

	ProcessWorkflow workFlow = null;

	try {
		ProcessOrder processOrder =
			ProcessOrderLocalServiceUtil.getProcessOrder(
				dossier.getDossierId(), 0);

		if (processOrder != null) {
			workFlow =
				ProcessWorkflowLocalServiceUtil.getProcessWorkflowByEvent(
					processOrder.getServiceProcessId(),
					WebKeys.PRE_CONDITION_CANCEL,
					processOrder.getProcessStepId());
		}
	}
	catch (Exception e) {

	}
%>


<%-- <liferay-ui:icon-menu> --%>
<portlet:renderURL var="viewDossierURL">
	<portlet:param name="mvcPath"
		value='<%=templatePath + "edit_dossier.jsp"%>' />
	<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
		value="<%=String.valueOf(dossier.getDossierId())%>" />
	<portlet:param name="<%=Constants.CMD%>" value="<%=Constants.VIEW%>" />
	<portlet:param name="isEditDossier" value="<%=String.valueOf(false)%>" />
	<portlet:param name="redirectURL" value="<%=currentURL%>" />
	<portlet:param name="backURL" value="<%=currentURL %>"/>
</portlet:renderURL>
<liferay-ui:icon cssClass="search-container-action fa view" image="view"
	message="view" url="<%=viewDossierURL.toString()%>" />

<c:choose>
	<c:when
		test="<%=dossier.getDossierStatus().equals(
						PortletConstants.DOSSIER_STATUS_NEW) ||
						dossier.getDossierStatus().equals(
							PortletConstants.DOSSIER_STATUS_WAITING)%>">
		<c:if
			test="<%=DossierPermission.contains(
							permissionChecker, scopeGroupId, ActionKeys.UPDATE)%>">
			<portlet:renderURL var="updateDossierURL">
				<portlet:param name="mvcPath"
					value='<%=templatePath + "edit_dossier.jsp"%>' />
				<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
					value="<%=String.valueOf(dossier.getDossierId())%>" />
				<portlet:param name="redirectURL" value="<%=currentURL%>" />
				<portlet:param name="backURL" value="<%=currentURL%>" />
				<portlet:param name="isEditDossier"
					value="<%=String.valueOf(true)%>" />
			</portlet:renderURL>
			<liferay-ui:icon cssClass="search-container-action fa edit"
				image="edit" message="edit" url="<%=updateDossierURL.toString()%>" />
			<c:if
				test="<%=dossier.getDossierStatus().equals(
								PortletConstants.DOSSIER_STATUS_NEW)%>">
				<portlet:actionURL var="updateDossierStatusURL"
					name="updateDossierStatus">
					<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
						value="<%=String.valueOf(dossier.getDossierId())%>" />
					<portlet:param name="<%=DossierDisplayTerms.DOSSIER_STATUS%>"
						value="<%=String.valueOf(PortletConstants.DOSSIER_STATUS_NEW)%>" />
					<portlet:param name="redirectURL" value="<%=currentURL%>" />
				</portlet:actionURL>
				<liferay-ui:icon cssClass="search-container-action fa forward"
					image="forward" message="send"
					url="<%=updateDossierStatusURL.toString()%>" />
			</c:if>

			<c:if
				test="<%=dossier.getDossierStatus().equals(
								PortletConstants.DOSSIER_STATUS_WAITING)%>">
				<portlet:actionURL var="updateDossierStatusURL"
					name="updateDossierStatus">
					<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
						value="<%=String.valueOf(dossier.getDossierId())%>" />
					<portlet:param name="<%=DossierDisplayTerms.DOSSIER_STATUS%>"
						value="<%=String.valueOf(PortletConstants.DOSSIER_STATUS_WAITING)%>" />
					<portlet:param name="redirectURL" value="<%=currentURL%>" />
				</portlet:actionURL>
				<liferay-ui:icon cssClass="search-container-action fa forward"
					image="reply" message="resend"
					url="<%=updateDossierStatusURL.toString()%>" />
			</c:if>
		</c:if>

		<c:if
			test="<%=DossierPermission.contains(
							permissionChecker, scopeGroupId, ActionKeys.DELETE) &&
							dossier.getDossierStatus().equals(
								PortletConstants.DOSSIER_STATUS_NEW)%>">
			<portlet:actionURL var="deleteDossierURL" name="deleteDossier">
				<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
					value="<%=String.valueOf(dossier.getDossierId())%>" />
				<portlet:param name="redirectURL" value="<%=currentURL%>" />
				<portlet:param name="dossierStatus"
					value="<%=dossier.getDossierStatus()%>" />
			</portlet:actionURL>
			<liferay-ui:icon-delete image="delete"
				cssClass="search-container-action fa delete"
				confirmation="are-you-sure-delete-entry" message="delete"
				url="<%=deleteDossierURL.toString()%>" />
		</c:if>
	</c:when>

	<c:when test="<%= BackendUtils.isDossierCancel(dossier.getDossierId()) %>">
		<portlet:actionURL var="cancelDossierURL" name="cancelDossier">
			<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
				value="<%=String.valueOf(dossier.getDossierId())%>" />
			<portlet:param name="redirectURL" value="<%=currentURL%>" />
		</portlet:actionURL>

		<liferay-ui:icon cssClass="search-container-action fa forward"
			image="reply" message="cancel-dossier"
			url="<%=cancelDossierURL.toString()%>" />

	</c:when>
	
	<c:when test="<%= BackendUtils.isDossierChange(dossier.getDossierId()) %>">
		<portlet:actionURL var="changeDossierURL" name="changeDossier">
			<portlet:param name="<%=DossierDisplayTerms.DOSSIER_ID%>"
				value="<%=String.valueOf(dossier.getDossierId())%>" />
			<portlet:param name="redirectURL" value="<%=currentURL%>" />
		</portlet:actionURL>
		<liferay-ui:icon cssClass="search-container-action fa forward"
			image="reply" message="change-dossier"
			url="<%=changeDossierURL.toString()%>" />

	</c:when>
	
</c:choose>
<%-- </liferay-ui:icon-menu> --%>
