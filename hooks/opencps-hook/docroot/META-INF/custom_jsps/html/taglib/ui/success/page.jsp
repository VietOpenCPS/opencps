<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@ include file="/html/taglib/init.jsp" %>

<%
String key = (String)request.getAttribute("liferay-ui:success:key");
String message = (String)request.getAttribute("liferay-ui:success:message");
boolean translateMessage = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:success:translateMessage"));
String successMessage = (String)SessionMessages.get(portletRequest, "message-x");
%>

<c:if test="<%= SessionMessages.contains(portletRequest, key) %>">
	<div class="alert alert-success">
		<c:choose>
			<c:when test="<%= translateMessage && Validator.isNull(successMessage) %>">
				<%= LanguageUtil.get(pageContext, message) %>
			</c:when>
			<c:when test="<%= Validator.isNotNull(successMessage) %>">
				<%= successMessage %>
			</c:when>
			<c:otherwise>
				<%= message %>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>