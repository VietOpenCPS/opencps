<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="org.opencps.dossiermgt.search.DossierLogSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierLogSearch"%>
<%@page import="org.opencps.dossiermgt.search.DossierLogDisplayTerms"%>
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
<%@page import="org.opencps.dossiermgt.model.impl.DossierImpl"%>
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
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.List"%>
<%@ include file="../init.jsp"%>
<liferay-util:include page="<%=templatePath + \"toptabs.jsp\" %>" servletContext="<%=application %>" />

<%
	String tabs1 = ParamUtil.getString(request, "tabs1");
	boolean levelBoolean = ParamUtil.getBoolean(request, DossierLogDisplayTerms.LEVEL);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringnewlog.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	
	List<DossierLog> dossierLogs =  new ArrayList<DossierLog>();
	
%>
<liferay-portlet:renderURL var="searchURL">
	<liferay-portlet:param name="mvcPath" value="<%=templatePath + \"dossiermonitoringnewlog.jsp\" %>"/>
	<liferay-portlet:param name="tabs1" value="<%=tabs1 %>"/>
</liferay-portlet:renderURL>
<aui:row>
	<aui:col>
		<aui:input type="checkbox" onChange="<%= renderResponse.getNamespace() + \"fitterALL()\" %>" name="<%=DossierLogDisplayTerms.LEVEL %>" inlineField="true"></aui:input>
		<aui:input type="checkbox" name="refresh" inlineField="true"></aui:input>
	</aui:col>
</aui:row>
<liferay-ui:search-container searchContainer="<%= new DossierLogSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
		
			DossierLogSearchTerms searchTerms = (DossierLogSearchTerms)searchContainer.getSearchTerms();
			if(levelBoolean){
				dossierLogs = DossierLogLocalServiceUtil.findByF_Level_Warring_Error(themeDisplay.getScopeGroupId());
			}else{
				dossierLogs = DossierLogLocalServiceUtil.findByF_Level_All(themeDisplay.getScopeGroupId());
			}
			
			total = dossierLogs.size();
			results = dossierLogs;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.DossierLog" 
			modelVar="dossierLog" 
			keyProperty="dossierLogId"
		>
			<%

				//id column
				row.addText(String.valueOf(searchContainer.getDelta()*(searchContainer.getCur()-1) +index + 1));
				row.addText(DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT));
				row.addText(String.valueOf(dossierLog.getDossierId()));
				Dossier ettCurrent = DossierLocalServiceUtil.fetchDossier(dossierLog.getDossierId());
				row.addText(ettCurrent.getReceptionNo());
				row.addText(PortletUtil.getDossierStatusLabel(dossierLog.getDossierStatus(), locale));
				row.addText(dossierLog.getActionInfo());
				row.addText(dossierLog.getMessageInfo());
				row.addText(PortletUtil.getDossierLogLevelLabel(dossierLog.getLevel(), locale));
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
<aui:script>
function <portlet:namespace />fitterALL() {
    var A = AUI();
	var url = '<%=searchURL.toString() %>';
	
	if(A.one('#<portlet:namespace /><%=DossierLogDisplayTerms.LEVEL %>')) {
		url += '&<portlet:namespace /><%=DossierLogDisplayTerms.LEVEL %>=' + A.one('#<portlet:namespace /><%=DossierLogDisplayTerms.LEVEL %>').get('value');
	}
	location.href = url;
}
</aui:script>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.dossiermonitoringnewlog.jsp");
%>