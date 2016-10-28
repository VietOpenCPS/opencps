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
<%@page import="org.opencps.util.DictItemUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@ include file="init.jsp" %>
<%
	long serviceinfoId = ParamUtil.getLong(request, "serviceinfoId");
	ServiceInfo serviceInfo = null;
	try {
		serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceinfoId);
	} catch (Exception e) {
		//nothing to do
	}
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	ServiceConfig scf = null;
	try {
		scf = ServiceConfigLocalServiceUtil.getServiceConfigByG_S(scopeGroupId, serviceinfoId);
	} catch(Exception e){
		//
	}
	boolean serviceIsConfiged;
	if(Validator.isNotNull(scf) && scf.getServiceLevel() >= 3){
		serviceIsConfiged = true;
	} else {
		serviceIsConfiged = false;
	}
%>

<liferay-portlet:renderURL 
		var="renderToSubmitOnline" 
		portletName="<%=WebKeys.P26_SUBMIT_ONLINE %>"
		plid="<%=Long.valueOf(plidServiceDetail) %>"
		portletMode="VIEW"
		windowState="<%=LiferayWindowState.NORMAL.toString() %>"
	>
	<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/submit/dossier_submit_online.jsp"/>
	<portlet:param name="serviceinfoId" value="<%=String.valueOf(serviceinfoId) %>"/>
	<portlet:param name="backURL" value="<%=backURL %>"/>
</liferay-portlet:renderURL>


<div class="ocps-service-detal-bound-all">
	<div class="ocps-custom-header">
		<label class="opcps-label">
			<liferay-ui:message key="service-detail" />
		</label>
		<span class="ocps-span">
			<a href="<%=backURL %>"><liferay-ui:message key="back"/></a>
		</span>
	</div>
	
	<div class="ocps-hide-header">
		<liferay-ui:header
			backURL="<%= backURL %>"
			title="service"
		/>
	</div>
	
	
	
	<c:if test="<%= Validator.isNotNull(serviceInfo) %>">
		<div class="service-detail-wrapper">
			<table>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-no"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceNo() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-name"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceName() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-process"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceProcess() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-method"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceMethod() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
					<liferay-ui:message key="service-dossier"/>
						
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceDossier() %>	
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-condition"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceCondition() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-duration"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceDuration() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-actors"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceActors() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-fee"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceFee() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-results"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceResults() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="service-records"/>
					</td>
					<td class="col-right">
						<%= serviceInfo.getServiceRecords() %>
					</td>
				</tr>
				<tr>
					<td class="col-left">
						<liferay-ui:message key="template_info"/>
					</td>
					<td class="col-right">
						
						<%
							List<TemplateFile> templates = new ArrayList<TemplateFile>();
						
							if (Validator.isNotNull(serviceInfo)) {
								templates = TemplateFileLocalServiceUtil.getServiceTemplateFiles(serviceInfo.getServiceinfoId());
							}
						%>
						<ul>
							<%
								for (TemplateFile tf : templates) {
							%>
								<li> <a href="<%= ServiceUtil.getDLFileURL(tf.getFileEntryId()) %>"> <%= tf.getFileName() %> </a></li>
							<%		
								}
							%>
						</ul>
						
					</td>
				</tr>
				<tr>
					<c:if test="<%= serviceIsConfiged %>">
						<td class="col-left" colspan="2">
							<aui:button href="<%= renderToSubmitOnline.toString() %>" cssClass="des-sub-button radius20" value="online-url-button"></aui:button>
						</td>
					</c:if>
				</tr>
			</table>
		</div>
	</c:if>
</div>
