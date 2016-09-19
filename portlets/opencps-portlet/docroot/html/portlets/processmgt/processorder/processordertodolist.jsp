
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
<%@page import="org.opencps.processmgt.util.ProcessOrderUtils"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearchTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearch"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@ include file="init.jsp"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />

<%
	//get config to load jsp display dossier
	String templatesToDisplay_cfg = GetterUtil.getString(portletPreferences.getValue("templatesToDisplay", "default"));

%>

<liferay-util:include page='<%=templatePath + "display/" + templatesToDisplay_cfg + ".jsp" %>' servletContext="<%=application %>" />

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.processorder.processordertodolist.jsp");
%>
