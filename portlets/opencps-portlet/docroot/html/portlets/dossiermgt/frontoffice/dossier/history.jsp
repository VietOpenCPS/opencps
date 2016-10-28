
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
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.persistence.DossierUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.WebKeys"%>

<%@ include file="../../init.jsp"%>

<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);

	long dossierId = dossier != null ? dossier.getDossierId() : 0L;
	
	List<DossierLog> dossierLogs = new ArrayList<DossierLog>();
	
	String serviceName = StringPool.DASH;
	String receptionNo = StringPool.DASH;
	
	ServiceInfo serviceInfo = null;
	
	if(Validator.isNotNull(dossier)) {
		receptionNo = dossier.getReceptionNo();
		try {
			serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
			serviceName = serviceInfo.getServiceName();
		} catch (Exception e) {
			
		}
	}
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/dossiermgt/frontoffice/dossier/history.jsp");

%>

<aui:row>
	<aui:col width="20" cssClass="bold">
		<liferay-ui:message key="dossier-reception-no"/> 
	</aui:col>
	<aui:col width="80">
		<%=receptionNo %>
	</aui:col>
</aui:row>

<aui:row cssClass="pd_b20">
	<aui:col width="20" cssClass="bold">
		<liferay-ui:message key="dossier-service-name"/> 
	</aui:col>
	<aui:col width="80">
		<%=serviceName%>
	</aui:col>
</aui:row>

<liferay-ui:search-container 
	emptyResultsMessage="no-history-were-found"
	iteratorURL="<%=iteratorURL %>"
	delta="<%= PAGINATE_NUMBER %>"
	deltaConfigurable="true"
>
	<liferay-ui:search-container-results>
		<%
/* 			dossierLogs = DossierLogLocalServiceUtil.getDossierLogByDossierId(dossierId, searchContainer.getStart(), searchContainer.getEnd());
 */			
 			dossierLogs = DossierLogLocalServiceUtil.findDossierLog(1, dossierId, searchContainer.getStart(), searchContainer.getEnd());

			results = dossierLogs;
			
			//total = DossierLogLocalServiceUtil.countDossierLogByDossierId(dossierId);
			total = DossierLogLocalServiceUtil.countDossierLog(1, dossierId);
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
			
			//List<ResultRow> rows = searchContainer.getResultRows();
			
		%>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.dossiermgt.model.DossierLog" 
		modelVar="dossierLog" 
		keyProperty="dossierLogId"
	>
		<aui:row cssClass="top-line pd_b20 pd_t20">
			<aui:col width="60">
				<span class="span1">
					<i class="fa fa-circle blue sx10"></i>
				</span>
				
				<span class="span3 bold">
					<liferay-ui:message key="time" />
				</span>
				
				<span class="span8">
					<%=
						Validator.isNotNull(dossierLog.getUpdateDatetime()) ?
						DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) : 
						DateTimeUtil._EMPTY_DATE_TIME 
					%>
				</span>
			</aui:col>
			<aui:col width="40">
				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="dossier-status" />
					</span>
					
					<span class="span8">
						<%=PortletUtil.getDossierStatusLabel(dossierLog.getDossierStatus(), locale)%>
					</span>
				</aui:row>
			
				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="actor" />
					</span>
					
					<span class="span8">
						<liferay-ui:message key="<%= dossierLog.getActorName() %>"/>
					</span>
				</aui:row>
				
				<aui:row>
					<span class="span4 bold">
						<liferay-ui:message key="note" />
					</span>
					
					<span class="span8">
						<liferay-ui:message key="<%= DossierMgtUtil.getDossierLogs(StringPool.BLANK, dossierLog.getMessageInfo())  %>"/>
					</span>
					
				</aui:row>
			</aui:col>
		</aui:row>
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator paginate="false"/>
</liferay-ui:search-container>

<%!
	private int PAGINATE_NUMBER = 200;
%>

