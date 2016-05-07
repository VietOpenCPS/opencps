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
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="org.opencps.dossiermgt.search.DossierTemplateDisplayTerms"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%
	DossierTemplate dossierTemplate = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);
	long dossierTemplateId = dossierTemplate != null ? dossierTemplate.getDossierTemplateId() : 0L;
	String backURL = ParamUtil.getString(request, "redirectURL");
%>
<aui:model-context bean="<%=dossierTemplate%>" model="<%=DossierTemplate.class%>" />
<aui:input 
	type="hidden" 
	name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DOSSIERTEMPLATEID %>" 
	value="<%=String.valueOf(dossierTemplateId) %>"
/>
<aui:row>
	<aui:input 
		name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENO %>"
		cssClass="input50"
	>
		<aui:validator name="required" />
		<aui:validator name="maxLength">100</aui:validator>
	</aui:input>
</aui:row>

<aui:row>
	<aui:input 
		name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_TEMPLATENAME %>"
		cssClass="input95"
	>
		<aui:validator name="required" />
		<aui:validator name="maxLength">255</aui:validator>
	</aui:input>
</aui:row>


<aui:row>
	<aui:input 
		type="textarea"
		name="<%=DossierTemplateDisplayTerms.DOSSIERTEMPLATE_DESCRIPTION %>"
		cssClass="input95"
	>
	</aui:input>
</aui:row>
