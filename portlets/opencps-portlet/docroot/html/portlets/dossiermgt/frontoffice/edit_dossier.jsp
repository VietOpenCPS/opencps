
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
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="com.liferay.portal.kernel.management.jmx.DoOperationAction"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>


<%
	
	Dossier dossier = (Dossier)request.getAttribute(WebKeys.DOSSIER_ENTRY);

	ServiceConfig serviceConfig = (ServiceConfig)request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	String[] dossierSections = new String[]{"dossier_info", "dossier_part", "result", "history"};
	
	String[][] categorySections = {dossierSections};

%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (dossier == null) ? "add-dossier" : "update-dossier" %>'
/>

<portlet:actionURL var="updateDossierURL" name="updateDossier"/>

<liferay-util:buffer var="htmlTop">
	<c:if test="<%= dossier != null %>">
		<div class="dossier-info">
			<div class="float-container">
				<span class="dossier-name"><%= HtmlUtil.escape(dossier.getSubjectName()) %></span>
			</div>
		</div>
	</c:if> 
</liferay-util:buffer>

<liferay-util:buffer var="htmlBottom">

</liferay-util:buffer>

<aui:form name="fm" action="<%=updateDossierURL %>" method="post">

	<aui:model-context bean="<%= dossier %>" model="<%= Dossier.class %>" />
	
	<aui:input name="redirectURL" type="hidden" value="<%= backURL%>"/>
	<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
	
	<aui:input name="<%=DossierDisplayTerms.GROUP_ID %>" type="hidden" value="<%= scopeGroupId%>"/>
	<aui:input name="<%=DossierDisplayTerms.COMPANY_ID %>" type="hidden" value="<%= company.getCompanyId()%>"/>

	<liferay-ui:form-navigator
		backURL="<%= backURL %>"
		categoryNames="<%= DossierMgtUtil._DOSSIER_CATEGORY_NAMES %>"
		categorySections="<%= categorySections %>"
		htmlBottom="<%= htmlBottom %>"
		htmlTop="<%= htmlTop %>"
		jspPath='<%=templatePath + "dossier/" %>'
	/>
</aui:form>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.edit_dossier.jsp");
%>
