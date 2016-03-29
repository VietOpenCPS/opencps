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
<%@ include file="/init.jsp" %>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.servicemgt.permissions.LevelPermission"%>
<%@page import="org.opencps.servicemgt.permissions.CategoryPermission"%>
<%@page import="org.opencps.servicemgt.permissions.DocumentPermission"%>
<%@page import="org.opencps.servicemgt.permissions.ServicePermission"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>


