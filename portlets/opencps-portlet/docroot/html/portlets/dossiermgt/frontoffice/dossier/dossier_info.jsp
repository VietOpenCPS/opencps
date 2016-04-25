
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
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>

<%@ include file="../../init.jsp"%>


<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);
	ServiceConfig serviceConfig = (ServiceConfig) request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	String cmd = ParamUtil.getString(request, Constants.CMD);
	
%>

<aui:model-context bean="<%=dossier%>" model="<%=Dossier.class%>" />

<liferay-ui:error-marker key="errorSection" value="dossier_info" />

<aui:row>
	<aui:col width="70">
		<aui:input 
			name="<%=DossierDisplayTerms.SERVICE_NAME %>" 
			cssClass="input96" 
			disabled="<%=true %>"
			type="text"
			value="<%=serviceInfo != null ? serviceInfo.getServiceName() : StringPool.BLANK %>"
		/>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input 
			name="<%=DossierDisplayTerms.SERVICE_NO %>" 
			cssClass="input90" 
			disabled="<%=true %>"
			type="text"
			value="<%=serviceInfo != null ? serviceInfo.getServiceNo() : StringPool.BLANK %>"
		/>	
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="70">
		<aui:input 
			name="<%=DossierDisplayTerms.GOVAGENCY_NAME%>"
			cssClass="input96" 
			disabled="<%=true %>"
			value="<%=serviceConfig != null ? serviceConfig.getGovAgencyName() : StringPool.BLANK %>"
		/>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input 
			name="<%=DossierDisplayTerms.GOVAGENCY_CODE %>" 
			cssClass="input90"
			disabled="<%=true %>"
			value="<%=serviceConfig != null ? serviceConfig.getGovAgencyCode() : StringPool.BLANK %>"
		/>	
	</aui:col>
</aui:row>


<aui:row>
	<aui:col width="70">
		<aui:input 
			name="<%=DossierDisplayTerms.SUBJECT_NAME %>" 
			cssClass="input96"
		/>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input 
			name="<%=DossierDisplayTerms.SUBJECT_ID %>" 
			cssClass="input90" 
		/>	
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<aui:input name="<%=DossierDisplayTerms.ADDRESS %>" cssClass="input97" type="text"/>	
	</aui:col>
</aui:row>

<aui:row>
	<datamgt:ddr 
		depthLevel="3" 
		dictCollectionCode="<%=PortletPropsValues.DATAMGT_MASTERDATA_ADMINISTRATIVE_REGION %>"
		itemNames='<%=StringUtil.merge(new String[]{DossierDisplayTerms.CITY_ID,DossierDisplayTerms.DISTRICT_ID,DossierDisplayTerms.WARD_ID}) %>'
		itemsEmptyOption="true,true,true"
		cssClass="input100"
	/>
</aui:row>

<aui:row>
	<aui:col width="30">
		<aui:input name="<%=DossierDisplayTerms.CONTACT_NAME %>" cssClass="input90" type="text"/>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input name="<%=DossierDisplayTerms.CONTACT_TEL_NO %>" cssClass="input90" type="text"/>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input name="<%=DossierDisplayTerms.CONTACT_EMAIL %>" cssClass="input90" type="text"/>	
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<aui:input name="<%=DossierDisplayTerms.NOTE %>" cssClass="input97" type="textarea"/>	
	</aui:col>
</aui:row>