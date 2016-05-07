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
<%@page import="java.util.ArrayList"%>
<%@page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.accountmgt.search.BusinessDisplayTerms"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Business"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.accountmgt.service.BusinessLocalServiceUtil"%>
<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@ include file="../../init.jsp" %>

<%
	Business business = (Business) request.getAttribute(WebKeys.BUSINESS_ENTRY);
	long businessId = business!=null ? business.getBusinessId() : 0L;

	boolean isViewProfile = GetterUtil.get( (Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_VIEW_PROFILE), false);
	
	boolean isAdminViewProfile = GetterUtil.get((Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_ADMIN_PROFILE), false);
	
	String selectItems = StringPool.BLANK;
	String url = StringPool.BLANK;
	
	DictItem dictItemCity = null;
	DictItem dictItemDistrict = null;
	DictItem dictItemWard = null;
	
	DLFileEntry dlFileEntry = null;
	
	List<DictItem> dictItems = new ArrayList<DictItem>();
	DictCollection dictCollection = null;
	DictCollection dictCollectionAddress = null;
	
	try {
		
		dictCollection = DictCollectionLocalServiceUtil
						.getDictCollection(scopeGroupId, 
							PortletPropsValues.DATAMGT_MASTERDATA_BUSINESS_DOMAIN);
		if(dictCollection!=null) {
			dictItems = DictItemLocalServiceUtil
							.getDictItemsByDictCollectionId(dictCollection.getDictCollectionId());
		}
		
		dictCollectionAddress = DictCollectionLocalServiceUtil
						.getDictCollection(scopeGroupId, "ADMINISTRATIVE_REGION");
		
		if(dictCollectionAddress != null && business != null) {
			long dictCollectionId = dictCollectionAddress.getDictCollectionId();
			dictItemCity = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollectionId, business.getCityCode());
			dictItemDistrict = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollectionId, business.getDistrictCode());
			dictItemWard = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollectionId, business.getWardCode());
			
			if(dictItemCity != null && dictItemDistrict!= null && dictItemWard!=null) {
				String [] strs = new String[3];
				strs[0] = String.valueOf(dictItemCity.getDictItemId());
				strs[1] = String.valueOf(dictItemDistrict.getDictItemId());
				strs[2] = String.valueOf(dictItemWard.getDictItemId());
				
				selectItems = StringUtil.merge(strs);
				
			}
			dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(business.getAttachFile());
		}
		
		if(dlFileEntry != null) {
			 url = themeDisplay.getPortalURL()+"/c/document_library/get_file?uuid="+dlFileEntry.getUuid()+"&groupId="+themeDisplay.getScopeGroupId() ;
		}
	} catch(Exception e) {
		_log.error(e);
	}
	
%>

<aui:model-context bean="<%=business%>" model="<%=Business.class%>" />

<aui:row>
	<aui:input name="<%=BusinessDisplayTerms.BUSINESS_ADDRESS %>">
		<aui:validator name="required" />
		<aui:validator name="maxLength">500</aui:validator>
	</aui:input>
</aui:row>

<aui:row>
	<aui:col width="100">
		<datamgt:ddr 
			cssClass="input100"
			depthLevel="3" 
			dictCollectionCode="ADMINISTRATIVE_REGION"
			itemNames="cityId,districtId,wardId"
			itemsEmptyOption="true,true,true"	
			selectedItems="<%=selectItems.toString() %>"
		/>	
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="50">
		<aui:input 
			name="<%=BusinessDisplayTerms.BUSINESS_EMAIL %>"
			disabled="<%=isViewProfile || isAdminViewProfile %>"
		>
			<aui:validator name="required" />
			<aui:validator name="email" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:col>
	
	<aui:col width="50">
		<aui:input name="<%=BusinessDisplayTerms.BUSINESS_TELNO %>">
			<aui:validator name="required" />
			<aui:validator name="maxLength">20</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<aui:row>
	<aui:col width="50">
		<aui:input name="<%=BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME %>">
			<aui:validator name="required" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:col> 
	
	<aui:col width="50">
		<aui:input name="<%=BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE %>">
			<aui:validator name="required" />
			<aui:validator name="maxLength">100</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<c:if test="<%= !isViewProfile && !isAdminViewProfile %>">
	<aui:row>
		<aui:input type="file" name="attachFile" >
			<aui:validator name="acceptFiles">
				'<%= StringUtil.merge(PortletPropsValues.ACCOUNTMGT_FILE_TYPE) %>'
			</aui:validator>
			<aui:validator name="required" />
		</aui:input>
	</aui:row>
</c:if>

<c:if test="<%=isAdminViewProfile  && businessId > 0 %>">
	<a href="<%=url%>"><liferay-ui:message key="url.file.entry"></liferay-ui:message></a>
</c:if>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.registration.registration_business.business_register.jsp");
%>