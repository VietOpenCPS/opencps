
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

<%
	String content = ParamUtil.getString(request, "content", "upload");
	
	long primaryKey = ParamUtil.getLong(request, "primaryKey");
	
%>

<c:choose>
	<c:when test='<%=content.equals("upload-file") %>'>
		<liferay-util:include page='<%=templatePath + "dossier_file.jsp" %>' servletContext="<%=application %>"/>
	</c:when>
	<c:when test='<%=content.equals("individual") %>'>
		<liferay-util:include page="/html/common/portlet/edit_dossier_individual_part.jsp" servletContext="<%=application %>"/>
	</c:when>
	<c:when test='<%=content.equals("declaration-online") %>'>
		<liferay-util:include page="/html/common/portlet/dossier_dynamic_form.jsp" servletContext="<%=application %>" >
			<portlet:param name="primaryKey" value="<%=String.valueOf(primaryKey) %>"/>
		</liferay-util:include>
	</c:when>
	<c:when test='<%=content.equals("view-form") %>'>
		<liferay-util:include page="/html/common/portlet/dossier_dynamic_form.jsp" servletContext="<%=application %>"/>
	</c:when>
	<c:when test='<%=content.equals("view-version") %>'>
		<liferay-util:include  page='<%=templatePath + "dossier_file_version.jsp" %>' servletContext="<%=application %>"/>
	</c:when>
</c:choose>


