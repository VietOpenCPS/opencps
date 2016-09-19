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
<%@page import="org.opencps.accountmgt.service.BusinessDomainLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.model.BusinessDomain"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="java.util.ArrayList"%>
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
<%@page import="org.opencps.util.PortletUtil"%>

<%@ include file="../../init.jsp" %>

<%
	long businessId = 0;
	
	long dictItemTypeId = 0;
	
	boolean isViewProfile = GetterUtil.get( (Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_VIEW_PROFILE), false);
	
	boolean isAdminViewProfile = GetterUtil.get((Boolean) request.getAttribute(WebKeys.ACCOUNTMGT_ADMIN_PROFILE), false);
	
	boolean isCheckItemDomain = false;
	
	if(request.getAttribute(BusinessDisplayTerms.BUSINESS_BUSINESSID) != null && isAdminViewProfile){
		businessId = (Long) request.getAttribute(BusinessDisplayTerms.BUSINESS_BUSINESSID);
		if(businessId > 0){
			try{
				business = BusinessLocalServiceUtil.fetchBusiness(businessId);
			}catch(Exception e){
				//
			}
		}
	}
	
	businessId = business!=null ? business.getBusinessId() : 0L;
	
	List<DictItem> dictItemDomains = new ArrayList<DictItem>();
	
	DictItem dictItemType = null;
	
	DictCollection dictCollectionDomain = null;
	DictCollection dictCollectionType = null;
	
	List<BusinessDomain> businessDomains = new ArrayList<BusinessDomain>();
	
	try {
		//get list dictItemDomains
		dictCollectionDomain = DictCollectionLocalServiceUtil
						.getDictCollection(scopeGroupId, 
							PortletPropsValues.DATAMGT_MASTERDATA_BUSINESS_DOMAIN);
		if(dictCollectionDomain!=null) {
			dictItemDomains = DictItemLocalServiceUtil
							.getDictItemsByDictCollectionId(dictCollectionDomain.getDictCollectionId());	
			}
		//get list dictItem Bussiness Type
		dictCollectionType = DictCollectionLocalServiceUtil
						.getDictCollection(scopeGroupId, 
							PortletPropsValues.DATAMGT_MASTERDATA_BUSINESS_TYPE);
		if(dictCollectionType != null && business != null) {
			dictItemType = DictItemLocalServiceUtil.getDictItemInuseByItemCode(dictCollectionType.getDictCollectionId(), business.getBusinessType());
			dictItemTypeId = dictItemType.getDictItemId();
		}
		//get BusinessDomains
		if(business != null) {
			businessDomains = BusinessDomainLocalServiceUtil
							.getBusinessDomains(business.getBusinessId());
		}
		
	} catch(Exception e) {
		_log.error(e);
	}
	
%>

<aui:model-context bean="<%=business%>" model="<%=Business.class%>" />

<c:if test="<%=isAdminViewProfile  && businessId > 0%>">
	<aui:row cssClass="nav-content-row-2">
		<aui:col width="50">
			<aui:input 
				type="text"
				name="<%=BusinessDisplayTerms.BUSINESS_CREATEDDATE %>" 
				value="<%=DateTimeUtil.convertDateToString(business.getCreateDate(), DateTimeUtil._VN_DATE_FORMAT) %>"
				disabled="<%=isAdminViewProfile %>"
				label="create-date"
				cssClass="input100" 
			/>
		</aui:col>
		<aui:col width="50">
			<aui:input 
				name="account-status"  
				disabled="<%=isAdminViewProfile %>" 
				cssClass="input100" 
				type="text"
				value="<%=PortletUtil.getAccountStatus(business.getAccountStatus(), themeDisplay.getLocale())%>"
			/>
		</aui:col>
		
	</aui:row>
</c:if>

<aui:row cssClass="nav-content-row-2">
	<aui:col width="50">
		<aui:input name="<%=BusinessDisplayTerms.BUSINESS_NAME %>" cssClass="input100" >
			<aui:validator name="required" />
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:col>
	
	<aui:col width="50">
		<aui:input name="<%=BusinessDisplayTerms.BUSINESS_IDNUMBER %>" cssClass="input100"	
			>
			<aui:validator name="required" />
			<aui:validator name="maxLength">100</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<aui:row cssClass="nav-content-row-2">
	<aui:col width="50">
		<aui:input name="<%= BusinessDisplayTerms.BUSINESS_ENNAME %>" cssClass="input100">
			<aui:validator name="maxLength">255</aui:validator>
		</aui:input>
	</aui:col>
	
	<aui:col width="50">
		<aui:input name="<%=BusinessDisplayTerms.BUSINESS_SHORTNAME %>" cssClass="input100">
			<aui:validator name="maxLength">100</aui:validator>
		</aui:input>
	</aui:col>
</aui:row>

<aui:row cssClass="nav-content-row-2">
	<aui:col width="50">
		<aui:row>
			<datamgt:ddr
				cssClass="input100"
				depthLevel="1" 
				dictCollectionCode="<%=PortletPropsValues.DATAMGT_MASTERDATA_BUSINESS_TYPE %>"
				itemNames="businessType"
				itemsEmptyOption="true"	
				selectedItems="<%=String.valueOf(dictItemTypeId)%>"
				emptyOptionLabels="businessType"
				showLabel="<%=true%>"
			/>
		</aui:row>
		<aui:row>
				<aui:input name="<%=BusinessDisplayTerms.BUSINESS_ADDRESS %>" cssClass="input100">
					<aui:validator name="maxLength">500</aui:validator>
				</aui:input>
		</aui:row>
	</aui:col>
	<aui:col width="50">
		<c:if test="<%= !dictItemDomains.isEmpty() %>">
			<aui:row>
				<label><liferay-ui:message key ="businessDomain" /></label>
				<div class="fake_textarea">
				<%
					for(DictItem dictItemDomain : dictItemDomains) {
							if(businessDomains != null) {
								for(BusinessDomain businessDomainChecked : businessDomains) {
									if(dictItemDomain.getItemCode().equals(businessDomainChecked.getBusinessDomainCode())) {
										isCheckItemDomain = true;
										break;
									}
								}
							}
						%>
							<aui:input
								name="businessDomains"
								id='<%= "businessDomain" + dictItemDomain.getDictItemId()%>'
								value="<%=dictItemDomain.getItemCode() %>"
								type="checkbox" 
							    label="<%=dictItemDomain.getItemName(locale, true)%>"
							    checked="<%= isCheckItemDomain %>"
							    cssClass="getval"
							/>		
						<%
						isCheckItemDomain = false;
					}
				%>
				</div>
				<aui:input name="listBussinessDomains" type="hidden" value=""></aui:input>
			</aui:row>
		</c:if>
	</aui:col>
</aui:row>

<aui:script>
	AUI().ready(function(A) {
		var businessTypeCbs = $(".getval");
		var businessTypeCbsChecked = $(".getval:checked");
		var checkedArr = [];
		var listBussinessDomains = A.one("#<portlet:namespace />listBussinessDomains");
		
		businessTypeCbsChecked.each(function() {
			checkedArr.push($(this).attr("value"));
			listBussinessDomains.val(checkedArr);
		});
		
		businessTypeCbs.click(function() {
			if($(this).is(":checked")) {
				//alert($(this).attr("value") + ' ' + $(this).attr("id"));
				if($.inArray($(this).attr("value"), checkedArr) == -1) {
					checkedArr.push($(this).attr("value"));
				}
			} else {
				if($.inArray($(this).attr("value"), checkedArr) > -1) {
					removeItem = $(this).attr("value");
					checkedArr = $.grep(checkedArr, function(value) {
						  return value != removeItem;
					});
				} 
			}
			
			listBussinessDomains.val(checkedArr);
		});

		
	});
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog(".html.portlets.accountmgt.registration.registration_business.business_register.jsp");
%>