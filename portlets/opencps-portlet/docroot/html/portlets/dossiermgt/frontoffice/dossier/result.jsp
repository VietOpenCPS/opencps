
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
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@ include file="../../init.jsp"%>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);
%>

<c:choose>
	<c:when test="<%=dossier != null && dossier.getDossierStatus() != PortletConstants.DOSSIER_STATUS_NEW %>">
		<%
			int[] actors = new int[]{PortletConstants};
			List<DossierLog> dossierLogs = DossierLogLocalServiceUtil.findRequiredProcessDossier(dossier.getDossierId(), actors, requestCommands);
		%>
		<aui:row>
			<aui:col width="25">
				<liferay-ui:message key="dossier-create-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getCreateDate(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
			<aui:col width="25">
				<liferay-ui:message key="dossier-submit-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getSubmitDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
		</aui:row>
		<aui:row>
			<aui:col width="25">
				<liferay-ui:message key="dossier-receive-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
			<aui:col width="25">
				<liferay-ui:message key="dossier-reception-no"/>
			</aui:col>
			<aui:col width="25">
				<%=dossier.getReceptionNo() %>
			</aui:col>
		</aui:row>
		<aui:row>
			<aui:col width="25">
				<liferay-ui:message key="dossier-estimate-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getEstimateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
			<aui:col width="25">
				<liferay-ui:message key="dossier-finish-date"/>
			</aui:col>
			<aui:col width="25">
				<%=DateTimeUtil.convertDateToString(dossier.getFinishDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>
			</aui:col>
		</aui:row>
		<aui:row>
			<aui:col width="25">
				<liferay-ui:message key="dossier-update-date"/>
			</aui:col>
			<aui:col width="25"></aui:col>
			<aui:col width="25">
				<liferay-ui:message key="dossier-status"/>
			</aui:col>
			<aui:col width="25"></aui:col>
		</aui:row>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-info">
			<liferay-ui:message key="no-dossier-result-info"/>
		</div>
	</c:otherwise>
</c:choose>
