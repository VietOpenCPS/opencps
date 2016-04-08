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
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@ include file="../../init.jsp" %>

<%

	Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);

	DictCollection dictCollection = null;
	
	DictItem dictItemCity = null;
	DictItem dictItemDistrict = null;
	DictItem dictItemWard = null;
	
	StringBuilder getAddress = new StringBuilder();
	String url = StringPool.BLANK;
	
	long citizenID = citizen != null ? citizen.getCitizenId() : 0L;
	
	boolean isViewProfile = GetterUtil.get( (Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_VIEW_PROFILE), false);
	
	boolean isAdminViewProfile = GetterUtil.get((Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_ADMIN_PROFILE), false);
	DLFileEntry dlFileEntry = null;
	
	try {
		dictCollection = DictCollectionLocalServiceUtil
						.getDictCollection(scopeGroupId, "ADMINISTRATIVE_REGION");
		
		if(dictCollection != null) {
			long dictCollectionId = dictCollection.getDictCollectionId();
			dictItemCity = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollectionId, citizen.getCityCode());
			dictItemDistrict = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollectionId, citizen.getDistrictCode());
			dictItemWard = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollectionId, citizen.getWardCode());
			
			if(dictItemCity != null && dictItemDistrict!= null && dictItemWard!=null) {
				getAddress.append(dictItemCity.getDictItemId()+ ",");
				getAddress.append(dictItemWard.getDictItemId()+ ",");
				getAddress.append(dictItemDistrict.getDictItemId());
			}
		}
		
		dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(citizen.getAttachFile());
		
		if(dlFileEntry != null) {
			 url = themeDisplay.getPortalURL()+"/c/document_library/get_file?uuid="+dlFileEntry.getUuid()+"&groupId="+themeDisplay.getScopeGroupId() ;
		}
		
	} catch(Exception e) {
		
	}
%>



<aui:model-context bean="<%=citizen %>" model="<%=Citizen.class%>" />

<aui:row>
	<aui:col width="100">
		<aui:input 
			name="<%=CitizenDisplayTerms.CITIZEN_ADDRESS %>" 
			cssClass="input100"
		>
			<aui:validator name="required" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="100">
		<datamgt:ddr 
			cssClass="input100"
			depthLevel="3" 
			dictCollectionCode="ADMINISTRATIVE_REGION"
			itemNames="cityId,districtId,wardId"
			itemsEmptyOption="true,true,true"	
			selectedItems="<%=getAddress.toString() %>"
		/>	
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="50">
		<aui:input 
			name="<%=CitizenDisplayTerms.CITIZEN_EMAIL %>"
			disabled="<%=isViewProfile ||  isAdminViewProfile%>"
		>
			<aui:validator name="required" />
			<aui:validator name="email" />
			<aui:validator name="maxLength">255</aui:validator>	
		</aui:input>
	</aui:col>
	
	<aui:col width="50">
		<aui:input name="<%=CitizenDisplayTerms.CITIZEN_TELNO %>">
			<aui:validator name="required" />
			<aui:validator name="minLength">10</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<c:if test="<%= !isViewProfile && !isAdminViewProfile %>">
	<aui:row>
		<aui:col width="100">
		<aui:input type="file" name="<%=CitizenDisplayTerms.CITIZEN_ATTACHFILE %>" />
		</aui:col>
	</aui:row>
</c:if>

<a href="<%=url%>"><liferay-ui:message key="url.file.entry"></liferay-ui:message></a>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.registration/citizen.contact_info.jsp");
%>
