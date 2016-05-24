<%@page import="java.util.Date"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="org.opencps.dossiermgt.search.DossierListSearch"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
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
<%@ include file="../init.jsp"%>
<liferay-util:include page="/html/portlets/dossiermgt/monitoring/dossierlisttoolbar.jsp" servletContext="<%=application %>" />

<%
	String keywordSearch = ParamUtil.getString(request, "keywords");
	_log.info("----KEYWORD----" + keywordSearch);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringdossierlist.jsp");
%>

<liferay-ui:search-container searchContainer="<%= new DossierSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
			
			String[] keywordArrs = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				keywordArrs = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			if (keywordArrs != null) {
				List<Dossier> dossiers = null;
				Integer totalCount = 0;
				
				try {
					dossiers = DossierLocalServiceUtil.getDossier(scopeGroupId, searchTerms.getKeywords(), -1, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					totalCount = DossierLocalServiceUtil.countDossier(scopeGroupId, searchTerms.getKeywords(), -1);
				} catch(Exception e){
					_log.error(e);
				}
			
				total = totalCount;
				results = dossiers;
				
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);				
			}
		%>
	</liferay-ui:search-container-results>	
		<liferay-ui:search-container-row 
			className="org.opencps.dossiermgt.model.Dossier" 
			modelVar="dossier" 
			keyProperty="dossierId"
		>
		<%
			String statusText = "";
			if (Validator.isNotNull(dossier.getFinishDatetime())) {
				if (dossier.getFinishDatetime().after(dossier.getEstimateDatetime())) {
					statusText = LanguageUtil.get(locale, "status-late");
				}
				else if (dossier.getFinishDatetime().before(dossier.getEstimateDatetime())) {
					statusText = LanguageUtil.get(locale, "status-soon");
				}
				else if (dossier.getFinishDatetime().equals(dossier.getEstimateDatetime())) {
					statusText = LanguageUtil.get(locale, "status-ontime");
				}
			}
			else {
				Date now = new Date();
				if (dossier.getEstimateDatetime().before(now)) {
					statusText = LanguageUtil.get(locale, "status-toosoon");
				}
				else if (dossier.getEstimateDatetime().after(now)) {
					statusText = LanguageUtil.get(locale, "status-toolate");
				}
			}
		%>
		<liferay-ui:search-container-column-text name="row-no" title="row-no" value="<%= String.valueOf(row.getPos() + 1) %>"/>
		<liferay-ui:search-container-column-text orderable="true" name="subject-name" title="subject-name" value="<%= dossier.getSubjectName() %>"/>
		<liferay-ui:search-container-column-text name="govagency-name" title="govagency-name" value="<%= dossier.getGovAgencyName() %>"/>
		<liferay-ui:search-container-column-text orderable="true" name="receive-datetime" title="receive-datetime" value="<%= Validator.isNotNull(dossier.getReceiveDatetime()) ? dateFormatDate.format(dossier.getReceiveDatetime()) : \"\" %>"/>
		<liferay-ui:search-container-column-text orderable="true" name="finish-datetime" title="finish-datetime" value="<%= Validator.isNotNull(dossier.getFinishDatetime()) ? dateFormatDate.format(dossier.getFinishDatetime()) : \"\" %>"/>
		<liferay-ui:search-container-column-text name="status" title="status" value="<%= statusText %>"/>
		
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.dossierlist.jsp");
%>