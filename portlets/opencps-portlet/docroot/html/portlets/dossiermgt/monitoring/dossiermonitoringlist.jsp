<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
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
	String keywords = ParamUtil.getString(request, "keywords");
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringlist.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	
	List<Dossier> dossiers =  new ArrayList<Dossier>();
	List<String> headerNamesMonitoring = new ArrayList<String>();
	Map<String, String> orderableHeaders = new HashMap<String, String>();
	headerNamesMonitoring.add("stt");
	headerNamesMonitoring.add("dossier-id");
	headerNamesMonitoring.add("subject-name");
	headerNamesMonitoring.add("service-name");
	headerNamesMonitoring.add("gov-agency-name");
	headerNamesMonitoring.add("receive-datetime");
	headerNamesMonitoring.add("reception-no");
	headerNamesMonitoring.add("trang-thai");
	headerNamesMonitoring.add("action");
%>
<liferay-portlet:renderURL var="searchURL">
	<liferay-portlet:param name="mvcPath" value="<%=templatePath + \"dossiermonitoringlist.jsp\" %>"/>
	<liferay-portlet:param name="tabs1" value="<%=tabs1 %>"/>
</liferay-portlet:renderURL>
<aui:row>
	<aui:col>
		<aui:input name="keywords" inlineField="true"></aui:input>
		<aui:button type="button" value="search" onClick="<%= renderResponse.getNamespace() + \"fitterALL()\" %>"></aui:button>
	</aui:col>
</aui:row>
<liferay-ui:search-container searchContainer="<%= new DossierSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL,headerNamesMonitoring, orderableHeaders) %>">

	<liferay-ui:search-container-results>
		<%
			DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
			
			String[] itemNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				itemNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			dossiers = DossierLocalServiceUtil.searchDossierLikeAll(themeDisplay.getScopeGroupId(), keywords, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			total = DossierLocalServiceUtil.countDossierLikeAll(themeDisplay.getScopeGroupId(), keywords);
			results = dossiers;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.Dossier" 
			modelVar="dossier" 
			keyProperty="dossierId"
		>
			<%
				PortletURL detailURL = renderResponse.createRenderURL();
				detailURL.setParameter("mvcPath", templatePath + "dossiermgtdossiermonitoringhistory.jsp");
				detailURL.setParameter(DossierDisplayTerms.DOSSIER_ID, String.valueOf(dossier.getDossierId()));
				detailURL.setParameter(WebKeys.REDIRECT, currentURL);
				detailURL.setParameter("tabs1", tabs1);
				String tenThuTuc = StringPool.BLANK;
				if(Validator.isNotNull(dossier) && dossier.getServiceInfoId() > 0){
					ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.fetchServiceInfo(dossier.getServiceInfoId());
					tenThuTuc = Validator.isNotNull(serviceInfo)?serviceInfo.getServiceName():StringPool.BLANK;
				}
				//id column
				row.addText(String.valueOf(searchContainer.getDelta()*(searchContainer.getCur()-1) +index + 1));
				row.addText(String.valueOf(dossier.getDossierId()));
				row.addText(dossier.getSubjectName());
				row.addText(tenThuTuc);
				row.addText(dossier.getGovAgencyName());
				row.addText(DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT));
				row.addText(dossier.getReceptionNo());
				row.addText(PortletUtil.getDossierStatusLabel(dossier.getDossierStatus(), locale));
				//action column
				row.addText("<a href=\""+detailURL.toString()+"\">"+LanguageUtil.get(pageContext, "xem-lich-su") + "</a>");
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
<aui:script>
function <portlet:namespace />fitterALL() {
    var A = AUI();
	var url = '<%=searchURL.toString() %>';
	
	if(A.one('#<portlet:namespace />keywords')) {
		url += '&<portlet:namespace />keywords=' + A.one('#<portlet:namespace />keywords').get('value');
	}
	location.href = url;
}
</aui:script>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.dossiermonitoringlist.jsp");
%>