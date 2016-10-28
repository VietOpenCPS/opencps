
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

<%@page import="com.liferay.portal.kernel.util.UnicodeFormatter"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>

<%@ include file="../init.jsp" %>

<%
	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute(WebKeys.SERVICE_ENTRY);
	long serviceInfoId = (serviceInfo != null) ? serviceInfo.getServiceinfoId() : 0L;
	long submitOnlinePlid = PortalUtil.getPlidFromPortletId(scopeGroupId, true,  WebKeys.P26_SUBMIT_ONLINE);
	
	DictCollection collectionDomain = null;
	DictItem curDictItem = null;
	try {
		collectionDomain = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, WebKeys.SERVICE_DOMAIN);
	} catch (Exception e) {
		
	}
	
	long plidResLong = 0;
	
	if(Long.valueOf(plidRes) ==0) {
		plidResLong = submitOnlinePlid;
	} else {
		plidResLong = Long.valueOf(plidRes);
	}
	
	ServiceConfig serviceConfig = null;
	

	try {
		serviceConfig = ServiceConfigLocalServiceUtil.getServiceConfigByG_S(scopeGroupId, serviceInfoId);
	} catch (Exception e) {
		
	}
	
	List<DictItem> dictItems = new ArrayList<DictItem>();
	
	if(Validator.isNotNull(collectionDomain)) {
		dictItems = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(collectionDomain.getDictCollectionId());
	}
	
	String backURL = ParamUtil.getString(request, "backURL");
	
	List<TemplateFile> templates = new ArrayList<TemplateFile>();
	
	int count = 0;

	if (Validator.isNotNull(serviceInfo)) {
		templates = TemplateFileLocalServiceUtil.getServiceTemplateFiles(serviceInfo.getServiceinfoId());
		count = TemplateFileLocalServiceUtil.countServiceTemplateFile(serviceInfo.getServiceinfoId());
	}
	String revIcon = StringPool.BLANK;
	String templateIdsString = ParamUtil.getString(request, "templateSearchContainerPrimaryKeys");
%>

<liferay-portlet:renderURL 
	var="servieOnlinePopURL" 
	portletName="<%=WebKeys.P26_SUBMIT_ONLINE %>"
	plid="<%=plidResLong %>"
	portletMode="VIEW"
>
	<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/submit/dossier_submit_online.jsp"/>
	<portlet:param name="serviceinfoId" value="<%=String.valueOf(serviceInfoId) %>"/>
</liferay-portlet:renderURL>


<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= Validator.isNotNull(serviceInfo) ? "update-service" : "add-service" %>'
/>

<portlet:actionURL name="updateService" var="updateServiceURL"/>

<aui:form name="fm" action="<%=updateServiceURL %>" method="post">

	<aui:model-context bean="<%= serviceInfo %>" model="<%= ServiceInfo.class %>"/>

	<aui:input name="redirectURL" type="hidden" value="<%= backURL%>"/>
	
	<aui:input name="returnURL" type="hidden" value="<%= currentURL%>"/>
	
	<aui:input
		name="<%= ServiceDisplayTerms.GROUP_ID %>" type="hidden" 
		value="<%= scopeGroupId%>"
	/>
	
	<aui:input 
		name="<%= ServiceDisplayTerms.COMPANY_ID %>" type="hidden" 
		value="<%= company.getCompanyId()%>"
	/>
	
	<aui:input 
		name="<%= ServiceDisplayTerms.SERVICE_ID %>" 
		type="hidden" 
		value="<%= Validator.isNotNull(serviceInfo) ? serviceInfo.getServiceinfoId() : StringPool.BLANK %>"
	/>
	
	<div class="opencps-bound-wrapper pd20 mg-t-20 default-box-shadow">
		<aui:a href="<%=currentURL.toString() %>" cssClass="uppercase bottom-line font18 bold">
			<liferay-ui:message key="general-service"/>
		</aui:a>
		
		<aui:row>
			<aui:col cssClass="pd_t20">
				<aui:input name="<%= ServiceDisplayTerms.SERVICE_NAME %>" cssClass="input100"></aui:input>
			</aui:col>
		</aui:row>
		
		<aui:row>
			<aui:col>
				<aui:input name="<%= ServiceDisplayTerms.SERVICE_FULLNAME %>" cssClass="input100"></aui:input>
			</aui:col>
		</aui:row>
		
		<aui:row>
			<aui:col width="50">
				<datamgt:ddr 
					cssClass="input100"
					depthLevel="1" 
					dictCollectionCode="SERVICE_ADMINISTRATION"
					itemNames="<%= ServiceDisplayTerms.SERVICE_ADMINISTRATION %>"
					itemsEmptyOption="true"
					showLabel="<%= true %>"
					selectedItems="<%= Validator.isNotNull(serviceInfo) ? serviceInfo.getAdministrationCode() : StringPool.BLANK %>"
				>
				</datamgt:ddr>
			</aui:col>
			
			<%-- <aui:col width="50">
				<datamgt:ddr 
					cssClass="input100"
					depthLevel="1" 
					dictCollectionCode="SERVICE_DOMAIN"
					itemNames="<%= ServiceDisplayTerms.SERVICE_DOMAINCODE %>"
					itemsEmptyOption="true"
					showLabel="<%=true %>"
					selectedItems="<%= Validator.isNotNull(serviceInfo) ? serviceInfo.getDomainCode() : StringPool.BLANK %>"
				>
				</datamgt:ddr>
			</aui:col> --%>
			
			<aui:select name="<%=ServiceDisplayTerms.SERVICE_DOMAINCODE %>" label="<%=ServiceDisplayTerms.SERVICE_DOMAINCODE %>">
				<aui:option value=""></aui:option>
				<%
					if(dictItems != null){
						for(DictItem dictItem : dictItems){
							if((curDictItem != null && dictItem.getDictItemId() == curDictItem.getDictItemId())||
									(curDictItem != null && dictItem.getTreeIndex().contains(curDictItem.getDictItemId() + StringPool.PERIOD))){
								continue;
							}
							
							int level = StringUtil.count(dictItem.getTreeIndex(), StringPool.PERIOD);
							String index = "|";
							for(int i = 0; i < level; i++){
								index += "_";
							}
							%>
								<aui:option value="<%=dictItem.getDictItemId() %>"><%=index + dictItem.getItemName(locale) %></aui:option>
							<%
						}
					}
				%>
			</aui:select>
			
			
		</aui:row>
		
		
		<aui:row>
			<aui:col width="50">
				<aui:input name="<%= ServiceDisplayTerms.SERVICE_NO %>" cssClass="input100"></aui:input>
			</aui:col>
			
			<aui:col width="50">
				<aui:select name="<%= ServiceDisplayTerms.SERVICE_ACTIVESTATUS %>" showEmptyOption="true" cssClass="input100">
					<aui:option value="0">
						<liferay-ui:message key="service-private"/>
					</aui:option>
					<aui:option value="1">
						<liferay-ui:message key="service-public"/>
					</aui:option>
					<aui:option value="2">
						<liferay-ui:message key="service-outdate"/>
					</aui:option>
				</aui:select>
			</aui:col>
		</aui:row>
		
		<%-- <aui:row>
			<aui:col>
				<c:choose>
					<c:when test="<%=Validator.isNotNull(serviceInfo) && Validator.isNull(serviceInfo.getOnlineUrl())%>">
						<aui:input 
							cssClass="input100" 
							name="urlOnline" 
							type="textarea" 
							value="<%=servieOnlinePopURL.toString() %>"
						/>
					</c:when>
					<c:otherwise>
						<aui:input 
							cssClass="input100" 
							name="<%= ServiceDisplayTerms.SERVICE_ONLINEURL %>" 
							type="textarea"
						/>
					</c:otherwise>
				</c:choose>	
			</aui:col>
		</aui:row> --%>
	</div>
		
	<div class="opencps-bound-wrapper pd20 mg-t-20 default-box-shadow">
		
		<aui:a href="<%=currentURL.toString() %>" cssClass="uppercase bottom-line font18 bold">
			<liferay-ui:message key="detail-service"/>
		</aui:a>
		
		<liferay-util:include 
			page="/html/portlets/servicemgt/admin/service/detail_info.jsp" 
			servletContext="<%=application %>" 
		/>
			
	</div >
		
	<div class = "opencps-bound-wrapper pd20 mg-t-20 default-box-shadow">
		<liferay-util:include 
			page="/html/portlets/servicemgt/admin/service/template_info.jsp" 
			servletContext="<%=application %>" 
		/>
	</div>
	
	<aui:row>
		<aui:col cssClass="center pd_t20">
			<aui:button type="submit" value="submit" cssClass="radius20 width180 pd_t20 height40"/>
		</aui:col>
	</aui:row>
</aui:form>

