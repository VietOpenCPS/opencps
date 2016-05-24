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
<%@ include file="../../init.jsp"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigDisplayTerms"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.servicemgt.search.ServiceDisplayTerms"%>

<%
	ServiceInfo serviceInfo = null; 
	DictItem dictItemServiceAdmin = null;

	long serviceinfoId = ParamUtil.getLong(request, ServiceDisplayTerms.SERVICE_ID);
		
	try {
		serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceinfoId);
		if(serviceInfo != null) {
			dictItemServiceAdmin = DictItemLocalServiceUtil
							.fetchDictItem(Long.parseLong(serviceInfo.getAdministrationCode()));
		}
		
	} catch (Exception e) {
	}
%>

<%--cap thu tuc hanh chinh , required --%>

<c:choose>
	<c:when test="<%= Validator.isNotNull(dictItemServiceAdmin) %>">
		<aui:row>
			<aui:col width="50">
				<aui:input cssClass="input100"
					type="text" 
					name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_GOVAGENCYNAME %>" 
					value="<%=dictItemServiceAdmin.getItemName(themeDisplay.getLocale(), true) %>"
				>
				</aui:input>
			</aui:col>
			
			<aui:col width="50">
				<aui:input cssClass="input100"
					type="text" 
					name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_GOVAGENCYCODE %>" 
					value="<%=dictItemServiceAdmin.getItemCode() %>"
				/>
			</aui:col>
		</aui:row>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-error">
			<liferay-ui:message key="no-workingunit-were-found"/>
		</div>
	</c:otherwise>
</c:choose>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.dossiermgt.admin.ajax.dictitem_service_administration.jsp");
%>