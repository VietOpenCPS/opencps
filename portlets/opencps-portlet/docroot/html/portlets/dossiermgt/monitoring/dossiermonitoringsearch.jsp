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
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="java.text.Format"%>
<%@page import="org.opencps.dossiermgt.service.DossierStatusLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierStatus"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierNewProcessingSearch"%>
<liferay-util:include page="/html/portlets/dossiermgt/monitoring/toptabs.jsp" servletContext="<%=application %>" />
<liferay-util:include page="/html/portlets/dossiermgt/monitoring/toolbar.jsp" servletContext="<%=application %>" />
<%
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossiermonitoringsearch.jsp");	
%>
<aui:row>
	<aui:col width="50">
		<liferay-ui:search-container searchContainer="<%= new DossierNewProcessingSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
		
			<liferay-ui:search-container-results>
				<%
					DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
					Integer totalCount = 0;										
					List<Dossier> newdossierreceiveds = null;		
					DictCollection dictCollection = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, "DOSSIER_STATUS");
					DictItem dictItem = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollection.getDictCollectionId(), "received");
					try {
						newdossierreceiveds = DossierLocalServiceUtil.getDossierByStatus(scopeGroupId, (int)dictItem.getDictItemId(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = DossierLocalServiceUtil.countDossierByStatus(scopeGroupId, (int)dictItem.getDictItemId());
					}catch(Exception e){
					}
				
					total = totalCount;
					results = newdossierreceiveds;
					
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
						// no column
						row.addText(Validator.isNull(dossier.getModifiedDate()) ? "" : dateFormatDate.format(dossier.getModifiedDate()));

						//subjectname column
						row.addText(dossier.getSubjectName());
						
						ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
						//serviceinfo-name column
						row.addText(serviceInfo.getServiceName());					
					%>	
				</liferay-ui:search-container-row> 
			
			<liferay-ui:search-iterator/>
		</liferay-ui:search-container>	
	</aui:col>
	<aui:col width="50">
		<liferay-ui:search-container searchContainer="<%= new DossierNewProcessingSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
		
			<liferay-ui:search-container-results>
				<%
					DossierSearchTerms searchTerms = (DossierSearchTerms)searchContainer.getSearchTerms();
					Integer totalCount = 0;										
					List<Dossier> newdossierreceiveds = null;		
					DictCollection dictCollection = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, "DOSSIER_STATUS");
					DictItem dictItem = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollection.getDictCollectionId(), "done");
					try {
						newdossierreceiveds = DossierLocalServiceUtil.getDossierByStatus(scopeGroupId, (int)dictItem.getDictItemId(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = DossierLocalServiceUtil.countDossierByStatus(scopeGroupId, (int)dictItem.getDictItemId());
					}catch(Exception e){
					}
				
					total = totalCount;
					results = newdossierreceiveds;
					
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
						// no column
						row.addText(Validator.isNull(dossier.getModifiedDate()) ? "" : dateFormatDate.format(dossier.getModifiedDate()));

						//subjectname column
						row.addText(dossier.getSubjectName());
						
						ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
						//serviceinfo-name column
						row.addText(serviceInfo.getServiceName());					
					%>	
				</liferay-ui:search-container-row> 
			
			<liferay-ui:search-iterator/>
		</liferay-ui:search-container>	
	</aui:col>
</aui:row>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.monitoring.search.jsp");
%>