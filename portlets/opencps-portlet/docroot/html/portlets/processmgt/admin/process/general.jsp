<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="org.opencps.processmgt.model.ServiceProcess"%>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>

<%@ include file="../../init.jsp" %>

<%
	ServiceProcess serviceProcess = (ServiceProcess) request.getAttribute(WebKeys.SERVICE_PROCESS_ENTRY);
	List<DossierTemplate> dossierTemplates = ProcessUtils.getDossierTemplate(renderRequest);
%>

<aui:model-context bean="<%= serviceProcess %>" model="<%= ServiceProcess.class %>"/>

<aui:row>
	<aui:col width="100">
		<aui:input cssClass="input50" name="processNo" >
			<aui:validator name="required" errorMessage="not-empty"></aui:validator>
		</aui:input>
	</aui:col>
</aui:row>
<aui:row>
	<aui:col width="100">
		<aui:input cssClass="input100" name="processName" >
			<aui:validator name="required" errorMessage="not-empty"></aui:validator>
		</aui:input>
	</aui:col>
</aui:row>
<aui:row>
	<aui:col width="100">
		<aui:input type="textarea" cssClass="input100" name="description" >
		</aui:input>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<aui:select name="dossierTemplateId" showEmptyOption="true">
			<%
				for (DossierTemplate dt : dossierTemplates) {
			%>
				<aui:option value="<%= Long.toString(dt.getDossierTemplateId()) %>"><%= dt.getTemplateName() %></aui:option>
			<%
				}
			%>
		</aui:select>
	</aui:col>
</aui:row>