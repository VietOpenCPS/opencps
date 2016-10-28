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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ddm" prefix="liferay-ddm" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<style>
html, body {height: 100%; min-height: 100%;}
*{
  margin: 0;
	padding: 0;
}
.confirm-page {
	width: 500px;
	padding: 30px 0 0;
	text-align: center;
	position: relative;
	top: 50%;
	left: 50%;
	-webkit-transform: translate(-50%, -85%);
	transform: translate(-50%, -85%);
}
.confirm-page p {
	margin-bottom: 20px;
}
.confirm-page .title {
	font-weight: bold;
	font-size: 28px;
	margin-bottom: 30px;
	text-decoration: none;
}
.confirm-page .content {
	padding: 30px 0;
	border: 1px solid #ccc;
	border-width: 1px 0;
}
.confirm-page a {
	border-radius: 5px;
	background-color: #0074cd;
	color: white;
	padding: 7px 15px;
	text-decoration: none;
}
.confirm-page .title.error {
	color: red;
}
</style>

<div class="confirm-page">
	<p class="title"><liferay-ui:message key="confirm-email-address-success"/></p>
	<div class="content">
		<p><liferay-ui:message key="turn-back-to-portal"/></p>
		<a href="/home" ><liferay-ui:message key="portal-forward-link"/></a>
	</div>
</div>

