<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
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
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>

<%
	long serviceInfoId = ParamUtil.getLong(request, "serviceinfoId");
	String govAdmin = ParamUtil.getString(request, "administrationCode");
	ServiceInfo serviceInfo = null;
	ServiceConfig serviceConfig = null;
	String backURL = ParamUtil.getString(request, "backURL");
	
	try {
		
		serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceInfoId);
		serviceConfig = ServiceConfigLocalServiceUtil
						.getServiceConfigByG_S_G(scopeGroupId, serviceInfo.getServiceinfoId(), govAdmin);
	} catch (Exception e) {
		//nothing to do
	}
	
	long frontServicePlid = PortalUtil.getPlidFromPortletId(scopeGroupId, WebKeys.DOSSIER_MGT_PORTLET);
	long plidSubmit = 0;
	
	if(Long.valueOf(plidRes) == 0) {
		plidSubmit = frontServicePlid;
	} else {
		plidSubmit = Long.valueOf(plidRes);
	}
	
	
%>

<liferay-portlet:renderURL var="servieOnlinePopURL"
 		portletName="<%=WebKeys.DOSSIER_MGT_PORTLET %>"
		plid="<%=plidSubmit %>"
		portletMode="VIEW"
		windowState="<%=LiferayWindowState.NORMAL.toString() %>"
		>
			<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/frontoffice/edit_dossier.jsp"/>
			<portlet:param name="<%=DossierDisplayTerms.SERVICE_CONFIG_ID %>" value="<%=String.valueOf(serviceConfig.getServiceConfigId()) %>"/>
			<portlet:param name="<%=Constants.CMD %>" value="<%=Constants.ADD %>"/>
			<portlet:param name="backURL" value="<%=backURL %>"/>
			<portlet:param name="isEditDossier" value="<%=String.valueOf(true) %>"/>
</liferay-portlet:renderURL>

<aui:row cssClass="serice-des">
	<liferay-ui:message key="service-description-dvc"/>
</aui:row>
	
<aui:row cssClass="des-detail">		
	<c:choose>
		<c:when test="<%=Validator.isNotNull(serviceConfig) %>">
			<c:choose>
				<c:when test="<%=serviceConfig.getServiceInstruction().equalsIgnoreCase(StringPool.BLANK) %>">
					
				</c:when>
				<c:otherwise>
					<%= serviceConfig.getServiceInstruction() %>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<liferay-ui:message key="no-config"/>
		</c:otherwise>
	</c:choose>	
</aui:row>

<aui:row cssClass="btn-submit-online">
	<c:if test = "<%=Validator.isNotNull(serviceConfig) && Validator.isNotNull(serviceInfo) && (serviceConfig.getServiceLevel() >= 3)%>">
		<aui:button type="button" name="submitonline" value="dossier-submit-online" href="<%=Validator.isNotNull(serviceConfig.getServiceUrl()) ? serviceConfig.getServiceUrl() : servieOnlinePopURL.toString() %>" />
	</c:if>
</aui:row>


