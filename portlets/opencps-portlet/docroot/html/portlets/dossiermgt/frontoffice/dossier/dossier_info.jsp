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
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierCityCodeException"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierDistrictCodeException"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierWardCodeException"%>
<%@page import="org.opencps.dossiermgt.InvalidDossierObjectException"%>
<%@page import="org.opencps.dossiermgt.CreateDossierFolderException"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierSubjectNameException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierSubjectNameException"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierSubjectIdException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierSubjectIdException"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierAddressException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierContactEmailException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierContactNameException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierContactTelNoException"%>
<%@page import="org.opencps.dossiermgt.EmptyDossierContactNameException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthDossierAddressException"%>
<%@page import="org.opencps.dossiermgt.InvalidDossierObjectException"%>
<%@ include file="../../init.jsp"%>


<%
	Dossier dossier = (Dossier) request.getAttribute(WebKeys.DOSSIER_ENTRY);

	ServiceConfig serviceConfig = (ServiceConfig) request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_INFO_ENTRY);
	
	Citizen citizen = (Citizen)request.getAttribute(WebKeys.CITIZEN_ENTRY);
	
	Business business = (Business)request.getAttribute(WebKeys.BUSINESS_ENTRY);
	
	String itemSelected = GetterUtil.getString(request.getAttribute(WebKeys.DICT_ITEM_SELECTED), StringPool.BLANK);
	
	String cmd = ParamUtil.getString(request, Constants.CMD);
%>

<aui:model-context bean="<%=dossier%>" model="<%=Dossier.class%>" />

<liferay-ui:error-marker key="errorSection" value="dossier_info" />

<liferay-ui:error 
	exception="<%= EmptyDossierCityCodeException.class %>" 
	message="<%=EmptyDossierCityCodeException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= EmptyDossierDistrictCodeException.class %>"
	message="<%=EmptyDossierDistrictCodeException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= EmptyDossierWardCodeException.class %>" 
	message="<%=EmptyDossierWardCodeException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= InvalidDossierObjectException.class %>" 
	message="<%=InvalidDossierObjectException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= CreateDossierFolderException.class %>" 
	message="<%=CreateDossierFolderException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= EmptyDossierSubjectNameException.class %>" 
	message="<%=EmptyDossierSubjectNameException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= OutOfLengthDossierSubjectNameException.class %>" 
	message="<%=OutOfLengthDossierSubjectNameException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= EmptyDossierSubjectIdException.class %>" 
	message="<%=EmptyDossierSubjectIdException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= EmptyDossierAddressException.class %>" 
	message="<%=EmptyDossierAddressException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= OutOfLengthDossierContactEmailException.class %>" 
	message="<%=OutOfLengthDossierContactEmailException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= OutOfLengthDossierContactNameException.class %>" 
	message="<%=OutOfLengthDossierContactNameException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= OutOfLengthDossierContactTelNoException.class %>" 
	message="<%=OutOfLengthDossierContactTelNoException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= EmptyDossierContactNameException.class %>" 
	message="<%=EmptyDossierContactNameException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= OutOfLengthDossierAddressException.class %>" 
	message="<%=OutOfLengthDossierAddressException.class.getName() %>"
/>
<liferay-ui:error 
	exception="<%= InvalidDossierObjectException.class %>" 
	message="<%=InvalidDossierObjectException.class.getName() %>"
/>

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
			type="text"
			value="<%=citizen != null ? citizen.getFullName() : business != null ? business.getName() : StringPool.BLANK %>"
		>
			<aui:validator name="required"/>
			<aui:validator name="maxLength">
				<%= PortletPropsValues.DOSSIERMGT_DOSSIER_SUBJECT_NAME_LENGTH %>
			</aui:validator>
		</aui:input>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input 
			name="<%=DossierDisplayTerms.SUBJECT_ID %>" 
			cssClass="input90" 
			type="text"
			value="<%=citizen != null ? citizen.getPersonalId() : business != null ? business.getIdNumber() : StringPool.BLANK %>"
		>
			<aui:validator name="required"/>
			<aui:validator name="maxLength">
				<%= PortletPropsValues.DOSSIERMGT_DOSSIER_SUBJECT_ID_LENGTH %>
			</aui:validator>
		</aui:input>	
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<aui:input 
			name="<%=DossierDisplayTerms.ADDRESS %>" 
			cssClass="input97" 
			type="text"
			value="<%=citizen != null ? citizen.getAddress() : business != null ? business.getAddress() : StringPool.BLANK %>"
		>
			<aui:validator name="required"/>
			<aui:validator name="maxLength">
				<%= PortletPropsValues.DOSSIERMGT_DOSSIER_ADDRESS_LENGTH %>
			</aui:validator>
		</aui:input>	
	</aui:col>
</aui:row>

<aui:row>
	<datamgt:ddr 
		depthLevel="3" 
		dictCollectionCode="<%=PortletPropsValues.DATAMGT_MASTERDATA_ADMINISTRATIVE_REGION %>"
		itemNames='<%=StringUtil.merge(new String[]{DossierDisplayTerms.CITY_ID,DossierDisplayTerms.DISTRICT_ID,DossierDisplayTerms.WARD_ID}) %>'
		itemsEmptyOption="true,true,true"
		cssClass="input100"
		selectedItems="<%=itemSelected %>"
	/>
</aui:row>

<aui:row>
	<aui:col width="30">
		<aui:input name="<%=DossierDisplayTerms.CONTACT_NAME %>" 
			cssClass="input90" 
			type="text"
			value="<%=citizen != null ? citizen.getFullName() : business != null ? business.getName() : StringPool.BLANK %>"
		>
			<aui:validator name="required"/>
			<aui:validator name="maxLength">
				<%= PortletPropsValues.DOSSIERMGT_DOSSIER_CONTACT_NAME_LENGTH %>
			</aui:validator>
		</aui:input>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input 
			name="<%=DossierDisplayTerms.CONTACT_TEL_NO %>" 
			cssClass="input90" 
			type="text"
			value="<%=citizen != null && Validator.isNotNull(citizen.getTelNo()) ? citizen.getTelNo() : business != null && Validator.isNotNull(business.getTelNo())? business.getTelNo() : StringPool.BLANK %>"
		>
			<aui:validator name="maxLength">
				<%= PortletPropsValues.DOSSIERMGT_DOSSIER_CONTACT_TEL_NO_LENGTH %>
			</aui:validator>
		</aui:input>	
	</aui:col>
	
	<aui:col width="30">
		<aui:input 
			name="<%=DossierDisplayTerms.CONTACT_EMAIL %>" 
			cssClass="input90" 
			type="text"
			value="<%=citizen != null && Validator.isNotNull(citizen.getEmail()) ? citizen.getEmail() : business != null && Validator.isNotNull(business.getEmail())? business.getEmail() : StringPool.BLANK %>"
		>
			<aui:validator name="email"/>
			<aui:validator name="maxLength">
				<%= PortletPropsValues.DOSSIERMGT_DOSSIER_CONTACT_EMAIL_LENGTH %>
			</aui:validator>
		</aui:input>	
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<aui:input name="<%=DossierDisplayTerms.NOTE %>" cssClass="input97" type="textarea">
			<aui:validator name="maxLength">
				<%= PortletPropsValues.DOSSIERMGT_DOSSIER_NOTE_LENGTH %>
			</aui:validator>
		</aui:input>	
	</aui:col>
</aui:row>

<aui:script>
	AUI().ready('aui-base','aui-form-validator', function(A){
		var rules = {
			'<portlet:namespace/>cityId': {
				required: true
			} ,
			'<portlet:namespace/>districtId': {
				required: true
			} ,
			'<portlet:namespace/>wardId': {
				required: true
			} 
		};
				             	            
		var validator1 = new A.FormValidator({
			boundingBox: document.<portlet:namespace />fm,
			validateOnInput: true,
			rules: rules
		});
	});
</aui:script>