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
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="javax.portlet.RenderResponse"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.dossiermgt.service.DossierTemplateLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.DossierTemplate"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.dossiermgt.search.ServiceConfigDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.dossiermgt.InvalidInWorkingUnitException"%>
<%@page import="org.opencps.dossiermgt.InvalidServiceDomainException"%>
<%@page import="org.opencps.dossiermgt.InvalidServiceConfigGovNameException"%>
<%@page import="org.opencps.dossiermgt.InvalidServiceConfigGovCodeException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthServiceConfigGovNameException"%>
<%@page import="org.opencps.dossiermgt.OutOfLengthServiceConfigGovCodeException"%>
<%
	ServiceConfig serviceConfig = (ServiceConfig) 
		request.getAttribute(WebKeys.SERVICE_CONFIG_ENTRY);
	
	DossierTemplate dossierTemplateFromRenderRequest = (DossierTemplate) request.getAttribute(WebKeys.DOSSIER_TEMPLATE_ENTRY);

	PortletURL backRender = renderResponse.createRenderURL();
	
	backRender.setParameter("mvcPath", templatePath + "serviceconfiglist.jsp");
	backRender.setParameter("tabs1", DossierMgtUtil.TOP_TABS_SERVICE_CONFIG);
	
	long dossierTemplateId = dossierTemplateFromRenderRequest != null ? dossierTemplateFromRenderRequest.getDossierTemplateId() : 0L;
	
	long serviceConfigId = serviceConfig != null ? serviceConfig.getServiceConfigId() : 0L;
	
	long serviceInfoId = serviceConfig != null ? serviceConfig.getServiceInfoId() : 0L;
	
	String dictItemServiceDomainId = "0";
	
	String backURL = ParamUtil.getString(request, "backURL"); 
	
	String tabs1 = ParamUtil.getString(request, "tabs1");
	
	if(!Validator.isNotNull(backURL)) {
		backURL = backRender.toString();
	}
	
	DictCollection dictCollectionServiceDomain = null;
	DictItem dictItemServiceDomain = null;
	List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
	List<DossierTemplate> dossierTemplates = new ArrayList<DossierTemplate>();
		
	try {
		//get all ServiceInfo
		serviceInfos = ServiceInfoLocalServiceUtil.getServiceInfos(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
				
		//get dossierTemplates
		dossierTemplates = DossierTemplateLocalServiceUtil.getAll();
		
		if(serviceConfig != null) {
			dictItemServiceDomainId = serviceConfig.getDomainCode();
		}
		
	} catch(Exception e) {
		_log.error(e);
	}
	
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="update-service-config"
	backLabel="back"
/>

<liferay-ui:error 
	exception="<%= OutOfLengthServiceConfigGovCodeException.class%>"
	message="<%= OutOfLengthServiceConfigGovCodeException.class.getName() %>"
/>

<liferay-ui:error 
	exception="<%= OutOfLengthServiceConfigGovNameException.class%>"
	message="<%= OutOfLengthServiceConfigGovNameException.class.getName() %>"
/>

<liferay-ui:error 
	exception="<%= InvalidServiceConfigGovCodeException.class%>"
	message="<%= InvalidServiceConfigGovCodeException.class.getName() %>"
/>

<liferay-ui:error 
	exception="<%= InvalidServiceConfigGovNameException.class%>"
	message="<%= InvalidServiceConfigGovNameException.class.getName() %>"
/>

<liferay-ui:error 
	exception="<%= InvalidServiceDomainException.class%>"
	message="<%= InvalidServiceDomainException.class.getName() %>"
/>

<liferay-ui:error 
	exception="<%= InvalidInWorkingUnitException.class%>"
	message="<%= InvalidInWorkingUnitException.class.getName() %>"
/>

<liferay-ui:error 
	key="<%= MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED%>"
	message="<%= MessageKeys.DOSSIER_SYSTEM_EXCEPTION_OCCURRED %>"
/>

<portlet:renderURL 
	var="renderToDictItemServiceAdmin" 
	windowState="<%=LiferayWindowState.EXCLUSIVE.toString() %>" 
>
	<portlet:param name="mvcPath" value='<%=templatePath + "ajax/dictitem_service_administration.jsp" %>'/>
</portlet:renderURL>

<portlet:renderURL 
	var="renderToServiceInfo" 
	windowState="<%=LiferayWindowState.EXCLUSIVE.toString() %>" 
>
	<portlet:param name="mvcPath" value='<%=templatePath + "ajax/service_info_ajax.jsp" %>'/>
</portlet:renderURL>

<portlet:actionURL var="updateServiceConfigURL" name="updateServiceConfig">
	<portlet:param 
		name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICECONFIGID %>" 
		value="<%=String.valueOf(serviceConfigId) %>"/>
		
	<portlet:param name="returnURL" value="<%=currentURL %>"/>
	<portlet:param name="backURL" value="<%=backURL %>"/>
</portlet:actionURL>

<aui:form 
	action="<%=updateServiceConfigURL.toString() %>"
	method="post"
	name="fm"
>
	<aui:model-context bean="<%=serviceConfig%>" model="<%=ServiceConfig.class%>" />
	
	<aui:row>
	
		<datamgt:ddr
			depthLevel="1" 
			dictCollectionCode="<%=PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_DOMAIN %>"
			itemNames="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_DOMAINCODE %>"
			itemsEmptyOption="true"	
			selectedItems="<%=dictItemServiceDomainId%>"
		/>	
	</aui:row>
	<div id = "<portlet:namespace />responseServiceConfig"></div>
	
	<aui:row>
		<aui:select name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_DOSSIERTEMPLATEID %>">
			
					<c:choose>
					   <c:when test="<%=! tabs1.equals(DossierMgtUtil.TOP_TABS_SERVICE_CONFIG) && dossierTemplateId != 0 %>">
					       <aui:option value="<%= dossierTemplateId %>">
					           <%= dossierTemplateFromRenderRequest.getTemplateName() %>
					       </aui:option>
					   </c:when>
					   
					   <c:when test="<%=Validator.isNotNull(tabs1) && tabs1.equals(DossierMgtUtil.TOP_TABS_SERVICE_CONFIG) %>">
					       <%
		                        for(DossierTemplate dossierTemplate : dossierTemplates) {
		                            %>
		                                <aui:option value="<%=dossierTemplate.getDossierTemplateId() %>">
		                                    <%=dossierTemplate.getTemplateName() %>
		                                </aui:option>
		                            <%
		                        }
		                    %>
					   </c:when>
					   
					   <c:otherwise>
					       <%
		                        for(DossierTemplate dossierTemplate : dossierTemplates) {
		                            %>
		                                <aui:option value="<%=dossierTemplate.getDossierTemplateId() %>">
		                                    <%=dossierTemplate.getTemplateName() %>
		                                </aui:option>
		                            <%
		                        }
		                    %>
					   </c:otherwise>
					</c:choose>
			
		</aui:select>
	</aui:row>
	
	<div id = "<portlet:namespace />serviceConfigGovNameCode">
		
	</div>
	
	<aui:row>
			<aui:select name="<%=ServiceConfigDisplayTerms.SERVICE_CONFIG_SERVICEMODE %>">
				<aui:option value="<%=PortletConstants.SERVICE_CONFIG_INACTIVE %>">
					<liferay-ui:message key="inactive" />
				</aui:option>
				
				<aui:option value="<%=PortletConstants.SERVICE_CONFIG_FRONTOFFICE %>">
					<liferay-ui:message key="front-office" />
				</aui:option>
			
				<aui:option value="<%=PortletConstants.SERVICE_CONFIG_BACKOFFICE %>">
					<liferay-ui:message key="back-office" />
				</aui:option>
				
				<aui:option value="<%=PortletConstants.SERVICE_CONFIG_FRONT_BACK_OFFICE %>" >
					<liferay-ui:message key="front-back-office" />
				</aui:option>
			</aui:select>
	</aui:row>
	
	<aui:row>
		<aui:button name="submit" type="submit" value="submit"/>
		<aui:button type="reset" value="clear"/>
	</aui:row>
</aui:form>

<aui:script use = "aui-base">
	
AUI().ready(function(A) {
		
		var selectDomainCode = A.one("#<portlet:namespace/>domainCode") ;
		
		var serviceInfoId = "<%= serviceInfoId %>";
		
		if(selectDomainCode){
			<portlet:namespace />sentServiceInfoId(serviceInfoId);
			<portlet:namespace />sentDomainCode(selectDomainCode.val());
			selectDomainCode.on('change', function() {
				<portlet:namespace />sentDomainCode(selectDomainCode.val());
			});
		}
		
	}); 
	
	Liferay.provide(window, '<portlet:namespace />sentDomainCode', function(domainCode){
		
		var A = AUI();
		
		A.io.request(
				'<%= renderToServiceInfo.toString() %>',
				{
					dataType : 'text/html',
					method : 'GET',
				    data:{    	
				    	"<portlet:namespace />domainCode" : domainCode,
				    	"<portlet:namespace />serviceConfigId" : '<%=serviceConfigId%>'
				    },   
				    on: {
				    	success: function(event, id, obj) {
				    		
							var instance = this;
							var res = instance.get('responseData');
							
							var responseServiceConfig = A.one("#<portlet:namespace/>responseServiceConfig");
							
							if(responseServiceConfig){
								
								responseServiceConfig.empty();
								responseServiceConfig.html(res);
							}
								
						},
				    	error: function(){}
					}
				}
			);
	},['aui-base','aui-io']);
	
		
</aui:script>

<aui:script use = "aui-base">
	Liferay.provide(window, '<portlet:namespace/>getval', function(e) {	
		var A = AUI();		
		var instance = A.one(e);
		var selectServiceInfo = instance.val();
			<portlet:namespace />sentServiceInfoId(selectServiceInfo);
	});
	Liferay.provide(window, '<portlet:namespace />sentServiceInfoId', function(serviceInfoId){
		
		var A = AUI();
		
		A.io.request(
				'<%= renderToDictItemServiceAdmin.toString() %>',
				{
					dataType : 'text/html',
					method : 'GET',
				    data:{    	
				    	"<portlet:namespace />serviceinfoId" : serviceInfoId
				    },   
				    on: {
				    	success: function(event, id, obj) {
							var instance = this;
							var res = instance.get('responseData');
							
							var serviceConfigGovNameCode = A.one("#<portlet:namespace/>serviceConfigGovNameCode");
							
							if(serviceConfigGovNameCode){
								serviceConfigGovNameCode.empty();
								serviceConfigGovNameCode.html(res);
							}
								
						},
				    	error: function(){}
					}
				}
			);
	},['aui-base','aui-io']);
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.admin.edit_service_config.jsp");
%>