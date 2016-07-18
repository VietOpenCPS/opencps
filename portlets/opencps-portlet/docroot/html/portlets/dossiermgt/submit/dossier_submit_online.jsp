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
<%@ include file="init.jsp"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%
	
	String backURL = ParamUtil.getString(request, "backURL");
	String onlineURL = ParamUtil.getString(request, "onlineURL");
	long serviceinfoId = ParamUtil.getLong(request, "serviceinfoId");
	
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
	
	try {
		collection = DictCollectionLocalServiceUtil.getDictCollection(scopeGroupId, 
			PortletPropsValues.DATAMGT_MASTERDATA_SERVICE_ADMINISTRATION);
		
		if(Validator.isNotNull(collection)) {
			listAdmin = DictItemLocalServiceUtil.getDictItemsByDictCollectionId(collection.getDictCollectionId());
		}
	} catch (Exception e) {
		//nothing to do
	}
	
	try {
		serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(serviceinfoId);
	} catch (Exception e) {
		//nothing to do
	}
	
%>
<portlet:renderURL var="referToSubmitOnline" windowState="<%=LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="mvcPath" value="/html/portlets/dossiermgt/submit/ajax/url_online.jsp"/>
</portlet:renderURL>
<liferay-portlet:renderURL 
		var="detailServiceURL" 
		portletName="<%=WebKeys.SERVICE_MGT_DIRECTORY %>"
		plid="<%=plidServiceDetailRes %>"
		portletMode="VIEW"
	>
		<portlet:param name="mvcPath" value="/html/portlets/servicemgt/directory/service_detail.jsp"/>
		<portlet:param name="serviceinfoId" value="<%= String.valueOf(serviceinfoId) %>"/>
</liferay-portlet:renderURL>

<aui:row>
	<aui:col width="30">
		<liferay-ui:message key="service-label" />
	</aui:col>
	
	<aui:col width="70">
		<c:if test="<%=serviceInfo != null %>">
			<a href="<%= detailServiceURL.toString() %>">
				<span style="color:blue"><%=serviceInfo.getServiceName() %></span>
			</a>
		</c:if>
	</aui:col>
</aui:row>
<aui:row>
	<aui:col width="100">
		<aui:select name="administrationCode" cssClass="input100">
			<%
				for(DictItem dictItem : listAdmin) {
					%>
						<aui:option value="<%=dictItem.getItemCode() %>">
							<%=dictItem.getItemName(themeDisplay.getLocale(),true) %>
						</aui:option>
					<%
				}
			%>
		</aui:select>
	</aui:col>
</aui:row>
<div id = "<portlet:namespace />submitOnlineRes"></div>
<aui:script>
	AUI().ready(function(A) {
		var adminCodeSel = A.one("#<portlet:namespace/>administrationCode");
		var serviceId = '<%= serviceinfoId %>';
		var backURL = '<%=currentURL %>';
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