
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
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.dossiermgt.service.DossierLogLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.dossiermgt.model.DossierLog"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ include file="../../init.jsp"%>

<%
	int totalCount = 0;
	int level = ParamUtil.getInteger(request, "levelReq");
	String status = ParamUtil.getString(request, "statusReq");
	Date fromDate =ParamUtil.getDate(request, "fromDateReq", DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_FORMAT));
	Date toDate =  ParamUtil.getDate(request, "toDateReq", DateTimeUtil.getDateTimeFormat(DateTimeUtil._VN_DATE_FORMAT));
	//sdf.parse(ParamUtil.getString(request, "toDateReq"));
	 
	String toDay = ParamUtil.getString(request, "toDayReq");
	String toMonth = ParamUtil.getString(request, "toMonthReq");
	String toYear = ParamUtil.getString(request, "toYearReq");
	String fromDay = ParamUtil.getString(request, "fromDayReq");
	String fromMonth = ParamUtil.getString(request, "fromMonthReq");
	String fromYear = ParamUtil.getString(request, "fromYearReq");
	String backURL = ParamUtil.getString(request, "currentURL");
	Date date = new Date();

	List<DossierLog> dossierLogs = new ArrayList<DossierLog>();
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", "/html/portlets/dossiermgt/log/ajax/search_result.jsp");
	iteratorURL.setParameter("levelReq", String.valueOf(level));
	iteratorURL.setParameter("statusReq", String.valueOf(status));
	iteratorURL.setParameter("fromDateReq", DateTimeUtil.convertDateToString(fromDate, DateTimeUtil._VN_DATE_FORMAT));
	iteratorURL.setParameter("toDateReq", DateTimeUtil.convertDateToString(toDate, DateTimeUtil._VN_DATE_FORMAT));
	iteratorURL.setParameter("currentURL", backURL);
	iteratorURL.setWindowState(LiferayWindowState.NORMAL);
	
%>
<liferay-ui:header
	backURL="<%= backURL %>"
	title="result"
	backLabel="back"
/>
<liferay-ui:search-container 
		emptyResultsMessage="no-log-were-found"
		iteratorURL="<%=iteratorURL %>"
		delta="<%=20 %>"
		deltaConfigurable="true"
		>
		<liferay-ui:search-container-results>
			<%
				dossierLogs = DossierLogLocalServiceUtil.searchAdminLog(
					fromDate, toDate, 
					level, status, searchContainer.getStart(), searchContainer.getEnd());
				results = dossierLogs;
				totalCount = DossierLogLocalServiceUtil.countAnminLog(fromDate, toDate, level, status);			
				total = totalCount;
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
				String syncStatusName = LanguageUtil.get(portletConfig ,themeDisplay.getLocale(), DossierMgtUtil.getSynchStatus(dossierLog.getSyncStatus(), themeDisplay.getLocale()));
				String statusName = LanguageUtil.get(portletConfig ,themeDisplay.getLocale(), PortletUtil.getActionInfoByKey(dossierLog.getDossierStatus(), themeDisplay.getLocale()));
			%>
			<liferay-ui:search-container-column-text 
				name="row-no" value="<%=String.valueOf(row.getPos()+1) %>"
			/>
			
			<liferay-ui:search-container-column-text 
				name="time" value="<%=DateTimeUtil.convertDateToString(dossierLog.getUpdateDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT) %>"
			/>
			
			<liferay-ui:search-container-column-text 
				name="dossier-id" value="<%=String.valueOf(dossierLog.getDossierId()) %>"
			/>
			
			<liferay-ui:search-container-column-text 
				name="action" value="<%=dossierLog.getActionInfo() %>"
			/>	
			<liferay-ui:search-container-column-text 
				name="sync-status" value="<%=syncStatusName%>"
			/>	
			<liferay-ui:search-container-column-text 
				name="status" value="<%=statusName%>"
			/>	
			<liferay-ui:search-container-column-text 
				name="level" value="<%=String.valueOf(dossierLog.getLevel()) %>"
			/>	
		</liferay-ui:search-container-row>
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
 </liferay-ui:search-container>