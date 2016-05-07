<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalService"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
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
	String serviceName = ParamUtil.getString(request, DossierDisplayTerms.SERVICE_NAME);
	String govAgencyCode = ParamUtil.getString(request, DossierDisplayTerms.GOVAGENCY_CODE);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringnewupdate.jsp");
	iteratorURL.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	PortletURL iteratorURL2 = renderResponse.createRenderURL();
	iteratorURL2.setParameter("mvcPath", templatePath + "dossiermonitoringnewupdate.jsp");
	iteratorURL2.setParameter("tabs1", DossierMgtUtil.TOP_TABS_DOSSIER);
	List<Dossier> dossiers =  new ArrayList<Dossier>();
	List<String> headerNamesMonitoring = new ArrayList<String>();
	Map<String, String> orderableHeaders = new HashMap<String, String>();
	headerNamesMonitoring.add("stt");
	headerNamesMonitoring.add("subject-name");
	headerNamesMonitoring.add("service-name");
	headerNamesMonitoring.add("receive-datetime");
	
	//list linh vuc thu tuc
	List<ServiceInfo> listServiceInfos = ServiceInfoLocalServiceUtil.findByGroupId(themeDisplay.getScopeGroupId());
	
	//list cap hanh chinh thuc hien
	List<DictItem> listDictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(PortletConstants.DICTITEM_CO_QUAN_QUAN_LY_TTHC);
%>
<liferay-portlet:renderURL var="searchURL">
	<liferay-portlet:param name="mvcPath" value="<%=templatePath + \"dossiermonitoringnewupdate.jsp\" %>"/>
	<liferay-portlet:param name="tabs1" value="<%=tabs1 %>"/>
</liferay-portlet:renderURL>
<aui:row>
		<aui:col width="50">
			<aui:select name="<%=DossierDisplayTerms.SERVICE_NAME %>">
				<aui:option value="" label="loc-theo-linh-vuc-thu-tuc"></aui:option>
				<%
					for(ServiceInfo serviceInfo: listServiceInfos){
				%>
					<aui:option value="<%=serviceInfo.getServiceinfoId() %>" label="<%=HtmlUtil.escape(serviceInfo.getServiceName()) %>"></aui:option>
				<%} %>
			</aui:select>
		</aui:col>
		<aui:col width="50">
			<aui:select name="<%=DossierDisplayTerms.GOVAGENCY_CODE %>" >
				<aui:option value="" label="loc-theo-cap-hanh-chinh-thuc-hien"></aui:option>
				<%
					for(DictItem dictItem: listDictItems){
				%>
					<aui:option value="<%=dictItem.getItemCode() %>" label="<%=HtmlUtil.escape(dictItem.getItemName(locale)) %>"></aui:option>
				<%} %>
			</aui:select>
		</aui:col>
	</aui:row>
<aui:row>
	<aui:col>
	<aui:button type="button" value="search" onClick="<%= renderResponse.getNamespace() + \"fitterALL()\" %>"></aui:button>
	</aui:col>
</aui:row>
<aui:row>
	<aui:col width="50">
	<liferay-ui:message key="ho-so-moi-tiep-nhan"></liferay-ui:message>
		<liferay-ui:search-container searchContainer="<%= new DossierSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL,headerNamesMonitoring, orderableHeaders) %>">

	<liferay-ui:search-container-results>
		<%
			DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
			
			String[] itemNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				itemNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			dossiers = DossierLocalServiceUtil.searchDossierMonitoringNewUpdate(themeDisplay.getScopeGroupId(), serviceName, govAgencyCode, null, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			total = DossierLocalServiceUtil.countDossierMonitoringNewUpdate(themeDisplay.getScopeGroupId(), serviceName, govAgencyCode, null);
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
				row.addText("<a href=\""+detailURL.toString()+"\">"+dossier.getSubjectName() + "</a>");
				row.addText("<a href=\""+detailURL.toString()+"\">"+tenThuTuc+ "</a>");
				row.addText(DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT));
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
	</aui:col>
	<aui:col width="50">
	<liferay-ui:message key="ho-so-moi-hoan-thanh"></liferay-ui:message>
			<liferay-ui:search-container searchContainer="<%= new DossierSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL2,headerNamesMonitoring, orderableHeaders) %>">

	<liferay-ui:search-container-results>
		<%
			DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
			
			String[] itemNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				itemNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			dossiers = DossierLocalServiceUtil.searchDossierMonitoringNewUpdate(themeDisplay.getScopeGroupId(), serviceName, govAgencyCode, ""+PortletConstants.DOSSIER_STATUS_DONE, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			total = DossierLocalServiceUtil.countDossierMonitoringNewUpdate(themeDisplay.getScopeGroupId(), serviceName, govAgencyCode, ""+PortletConstants.DOSSIER_STATUS_DONE);
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
				row.addText("<a href=\""+detailURL.toString()+"\">"+dossier.getSubjectName() + "</a>");
				row.addText("<a href=\""+detailURL.toString()+"\">"+tenThuTuc+ "</a>");
				row.addText(DateTimeUtil.convertDateToString(dossier.getReceiveDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT));
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
	</aui:col>
</aui:row>
<aui:script>
function <portlet:namespace />fitterALL() {
    var A = AUI();
	var url = '<%=searchURL.toString() %>';
	
	if(A.one('#<portlet:namespace /><%=DossierDisplayTerms.SERVICE_NAME %>')) {
		url += '&<portlet:namespace /><%=DossierDisplayTerms.SERVICE_NAME %>=' + A.one('#<portlet:namespace /><%=DossierDisplayTerms.SERVICE_NAME %>').get('value');
	}
	if(A.one('#<portlet:namespace /><%=DossierDisplayTerms.GOVAGENCY_CODE %>')) {
		url += '&<portlet:namespace /><%=DossierDisplayTerms.GOVAGENCY_CODE %>=' + A.one('#<portlet:namespace /><%=DossierDisplayTerms.GOVAGENCY_CODE %>').get('value');
	}
	location.href = url;
}
</aui:script>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.frontofficedossierlist.jsp");
%>