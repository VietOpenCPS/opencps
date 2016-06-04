<%@page import="org.opencps.usermgt.service.EmployeeLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="java.text.Format"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="org.opencps.processmgt.service.ActionHistoryLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ActionHistory"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
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
	Integer dossierId = ParamUtil.getInteger(request, "dossierId");
	
	Dossier dossier = null;
	
	try {
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
	}
	catch (NoSuchDossierException ex) {
		
	}
	List<ActionHistory> histories = ActionHistoryLocalServiceUtil.searchActionHistoryByDossierId(scopeGroupId, dossierId);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
%>
<c:if test="<%= histories.size() > 0 %>">
<table class="table table-bordered table-hover table-striped">
	<thead class="table-columns">
		<tr>
			<th class="table-first-header">
				<liferay-ui:message key="no"/>
			</th>
			<th>
				<liferay-ui:message key="step-name"/>
			</th>
			<th>
				<liferay-ui:message key="action-name"/>
			</th>
			<th>
				<liferay-ui:message key="action-datetime"/>
			</th>
			<th>
				<liferay-ui:message key="action-user"/>
			</th>
			<th>
				<liferay-ui:message key="action-note"/>
			</th>
			<th class="table-last-header">
				<liferay-ui:message key="estimatedate-status"/>
			</th>
		</tr>
	</thead>
	<tbody>
		<%
			for (int i = 0; i < histories.size(); i++) {
		%>
			<tr>
				<td>
					<%= i + 1 %>
				</td>
				<td>
					<%= histories.get(i).getStepName() %>
				</td>
				<td>
					<%= histories.get(i).getActionName() %>
				</td>
				<td>
					<%= dateFormatDate.format(histories.get(i).getActionDatetime()) %>
				</td>
				<td>
					<%
						Employee employee2 = EmployeeLocalServiceUtil.getEmployeeByMappingUserId(scopeGroupId, histories.get(i).getActionUserId());
					%>
					<%= employee2.getFullName() %>
				</td>
				<td>
					<%= histories.get(i).getActionNote() %>
				</td>
				<td>
					<%
						
					%>
				</td>
			</tr>
		<%
			}
		%>
	</tbody>
</table>
</c:if>
