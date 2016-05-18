<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
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

<%@ include file="../../init.jsp"%>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);
	long dossierId = dossier != null ? dossier.getDossierId() : 0L;
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/dossier/history.jsp");
	List<DossierLog> dossierLogs = new ArrayList<DossierLog>();
	
%>

<liferay-ui:search-container 
		emptyResultsMessage="no-history-were-found"
		iteratorURL="<%=iteratorURL %>"
		delta="<%=20 %>"
		deltaConfigurable="true"
		>
		<liferay-ui:search-container-results>
			<%
				dossierLogs = DossierLogLocalServiceUtil.getDossierLogByDossierId(dossierId);
				
				results = dossierLogs;
				total = DossierLogLocalServiceUtil.countDossierLogByDossierId(dossierId);
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>
		<liferay-ui:search-container-row 
					className="org.opencps.dossiermgt.model.DossierLog" 
					modelVar="dossierLog" 
					keyProperty="dossierLogId"
				>
				
				<liferay-ui:search-container-column-text 
					name="row" 
					value="<%=String.valueOf(row.getPos() +1) %>" 
				/>
				
				<liferay-ui:search-container-column-text 
					name="time" 
					value="<%=DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>" 
				/>
				
				<liferay-ui:search-container-column-text 
					name="actor" 
					value="<%=dossierLog.getActor() %>" 
				/>
				
				<liferay-ui:search-container-column-text 
					name="action" 
					value="<%= dossierLog.getActionInfo() %>" 
				/>
				
				<liferay-ui:search-container-column-text 
					name="note" 
					value="<%=dossierLog.getMessageInfo() %>" 
				/>
				
				
		</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
