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
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@ include file="/init.jsp"%>
<%
	ResultRow row =
		(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	ProcessOrder processOrder = (ProcessOrder) row.getObject();
%>

<aui:button value="action" name="action" onClick="<%= renderResponse.getNamespace() + \"reAssignAction(\" + processOrder.getProcessOrderId() + \") \" %>"/>

<aui:script>
	Liferay.provide(window, '<portlet:namespace />reAssignAction', function(processOrderId) {
		var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.RE_ASSIGN_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
		portletURL.setParameter("mvcPath", "/html/portlets/processmgt/reassign/re_assign_to_user.jsp");
		portletURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>"); 
		portletURL.setPortletMode("normal");
		
		portletURL.setParameter("<%= ProcessOrderDisplayTerms.PROCESS_ORDER_ID %>", 
				processOrderId);
		
		openDialog(portletURL.toString(), '<portlet:namespace />reAssignForm','<%= UnicodeLanguageUtil.get(pageContext, "re-assign") %>');
	},['liferay-portlet-url']);
</aui:script>