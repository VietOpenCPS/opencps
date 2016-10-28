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
<%@page import="javax.validation.Valid"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="javax.portlet.PortletMode"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="org.opencps.datamgt.service.DictCollectionLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.datamgt.model.DictCollection"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.ServiceConfig"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@ include file="init.jsp"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%
	
	String backURL1 = ParamUtil.getString(request, "backURL");
	String onlineURL = ParamUtil.getString(request, "onlineURL");
	long serviceinfoId = ParamUtil.getLong(request, "serviceinfoId");
	long serviceConfigId = ParamUtil.getLong(request, "serviceConfigId");
	
	DictItem dictItem = null;
	long plidServiceDetailRes = 0;
	
	long directServicePlid = PortalUtil.getPlidFromPortletId(scopeGroupId, true,  WebKeys.SERVICE_MGT_DIRECTORY);
	
	if(Long.valueOf(plidServiceDetail) == 0) {
		plidServiceDetailRes = directServicePlid;
	} else {
		plidServiceDetailRes = Long.valueOf(plidServiceDetail);
	}
	ServiceInfo serviceInfo = null;
	ServiceConfig serviceConfig = null;
	DictCollection collection = null;
	List<DictItem> listAdmin = new ArrayList<DictItem>();
	List<ServiceConfig> listServiceConfig = new ArrayList<ServiceConfig>();
	
	if(serviceConfigId > 0) {
		try {
			serviceConfig = ServiceConfigLocalServiceUtil.getServiceConfig(serviceConfigId);
		} catch (Exception e) {
			_log.error(e);
		}
	}
	
	
	long serviceInfoIdToDetail = Validator.isNotNull(serviceConfig) ? serviceConfig.getServiceInfoId() : serviceinfoId;
	
	if(serviceInfoIdToDetail > 0) {
		try {
			serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceInfoIdToDetail);
		} catch (Exception e) {
			_log.error(e);
		}
	}
	
	try {
		//Lay thong tin co quan thuc hien theo serviceConfigId tu man hinh tiep nhan ho so
		if(Validator.isNotNull(serviceConfigId)){
			dictItem = PortletUtil.getDictItem(PortletPropsValues.DATAMGT_MASTERDATA_GOVERNMENT_AGENCY, serviceConfig.getGovAgencyCode(), scopeGroupId);
			if(dictItem != null){
				listAdmin.add(dictItem);
			}
		}
		//Lay thong tin co quan thuc hien tu dich vu cong END
		
		//Lay thong tin co quan thuc hien theo serviceinfoId tu man hinh thu tuc hanh chinh 
		if(Validator.isNotNull(serviceinfoId)){
			listServiceConfig = ServiceConfigLocalServiceUtil.getServiceConfigsByS_G(serviceinfoId, scopeGroupId);
			if(Validator.isNotNull(listServiceConfig)){
				for(ServiceConfig s: listServiceConfig){
					dictItem = PortletUtil.getDictItem(PortletPropsValues.DATAMGT_MASTERDATA_GOVERNMENT_AGENCY, s.getGovAgencyCode(), scopeGroupId);
					if(dictItem != null){
						listAdmin.add(dictItem);
					}
				}
			}
		}
		//Lay thong tin co quan thuc hien theo serviceinfoId tu man hinh thu tuc hanh chinh END
	} catch (Exception e) {
		//nothing to do
	}
	
	PortletURL returnServiceURL = PortletURLFactoryUtil.create(request, WebKeys.SERVICE_MGT_DIRECTORY, PortalUtil.getPlidFromPortletId(themeDisplay.getScopeGroupId(),  WebKeys.SERVICE_MGT_DIRECTORY), PortletRequest.RENDER_PHASE);
	returnServiceURL.setWindowState(WindowState.MAXIMIZED);
	returnServiceURL.setPortletMode(PortletMode.VIEW);
	returnServiceURL.setParameter("mvcPath", "/html/portlets/servicemgt/directory/service_detail.jsp");
	returnServiceURL.setParameter("serviceinfoId", Validator.isNotNull(serviceInfo)?String.valueOf(serviceInfo.getServiceinfoId()):String.valueOf(0));
%>
<liferay-ui:header 
	backURL="<%=Validator.isNotNull(backURL1)?backURL1:returnServiceURL.toString() %>"
	title="guide-sibmition-dossier"
	cssClass="submit-headers"
/>

<portlet:renderURL var="referToSubmitOnline" windowState="<%=LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/submit/ajax/url_online.jsp"/>
	<portlet:param name="backURL" value="<%=backURL1 %>"/>
</portlet:renderURL>
<liferay-portlet:renderURL 
		var="detailServiceURL" 
		portletName="<%=WebKeys.SERVICE_MGT_DIRECTORY %>"
		plid="<%=plidServiceDetailRes %>"
		portletMode="VIEW"
	>
		<portlet:param name="mvcPath" value="/html/portlets/servicemgt/directory/service_detail.jsp"/>
		<portlet:param name="serviceinfoId" value="<%= String.valueOf(serviceInfoIdToDetail) %>"/>
</liferay-portlet:renderURL>
<div class="ocps-submit-online">
	<aui:row>
		<aui:col width="30">
			<liferay-ui:message key="service-label" />
		</aui:col>
		
		<aui:col width="70">
			<c:if test="<%=serviceInfo != null %>">
				<a href="<%= detailServiceURL.toString() %>">
					<span><%=serviceInfo.getServiceName() %></span>
				</a>
			</c:if>
		</aui:col>
	</aui:row>
	<aui:row>
		<aui:col width="50">
			<aui:select name="administrationCode" label="co-quan-thuc-hien" cssClass="submit-online input100">
				<%
					if(listAdmin!=null && !listAdmin.isEmpty()){
						for(DictItem d : listAdmin){
							%>
								<aui:option value="<%=d.getItemCode() %>">
									<%=d.getItemName(themeDisplay.getLocale(),true) %>
								</aui:option>
							<%
						}
					}
				%>
			</aui:select>
		</aui:col>
	</aui:row>
	<div id = "<portlet:namespace />submitOnlineRes"></div>
</div>
<aui:script>
	AUI().ready(function(A) {
		var adminCodeSel = A.one("#<portlet:namespace/>administrationCode");
		var serviceId = '<%= serviceInfoIdToDetail %>';
		var backURL = '<%=currentURL %>';
		var serviceConfigId = '<%= serviceConfigId %>';
		<portlet:namespace />getOnlineURL(adminCodeSel.val(), serviceId);
		if(adminCodeSel) {
			adminCodeSel.on('change',function() {
				<portlet:namespace />getOnlineURL(adminCodeSel.val(), serviceId);
			});
		}
	});
	
	Liferay.provide(window, '<portlet:namespace />getOnlineURL', function(adminCode, serviceId) {
		var A = AUI();
		A.io.request(
				'<%=referToSubmitOnline.toString() %>',
				{
					dataType : 'text/html',
					method : 'GET',
				    data:{    
				    	"<portlet:namespace />administrationCode" : adminCode,
				    	"<portlet:namespace />serviceinfoId" : serviceId
				    },   
				    on: {
				    	success: function(event, id, obj) {
							var instance = this;
							var res = instance.get('responseData');
							
							var submitOnlineRes = A.one("#<portlet:namespace/>submitOnlineRes");
							
							if(submitOnlineRes){
								submitOnlineRes.empty();
								submitOnlineRes.html(res);
							}
								
						},
				    	error: function(){}
					}
				}
			);
	},['aui-base','aui-io']);
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.submit.dossier_submit_online.jsp");
%>