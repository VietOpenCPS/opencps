
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

<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.processmgt.permissions.ProcessOrderPermission"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>

<%@ include file="../init.jsp"%>

<%
	String tabs1 = ParamUtil.getString(request, "tabs1", ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS);
	PortletURL searchURL = renderResponse.createRenderURL();
	
	List<ProcessOrderBean> processOrderBeans = new ArrayList<ProcessOrderBean>();
	
	try{
		processOrderBeans = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getUserProcessStep(themeDisplay.getUserId());
	}catch(Exception e){}
%>

<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-button-container  nav-display-style-buttons pull-left" >
		<c:if test="<%=ProcessOrderPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ASSIGN_PROCESS_ORDER) && tabs1.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)%>">
			<portlet:renderURL var="processDossierURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
				<portlet:param name="mvcPath" value="/html/portlets/processmgt/processorder/processordertodolist.jsp"/>
				<portlet:param name="backURL" value="<%=currentURL %>"/>
			</portlet:renderURL>
			<aui:nav-item 
				cssClass="item-config"
				id="processDossier" 
				label="process-dossier" 
				iconCssClass="icon-plus icon-config"  
				href='<%="javascript:" + renderResponse.getNamespace() + "processMultipleDossier()" %>'
			/>
		</c:if>
	</aui:nav>
	
	<aui:nav-bar-search cssClass="pull-right">
		<div class="form-search">
			<aui:form action="<%= searchURL %>" method="post" name="fmSearch">
				<aui:row>
					<aui:col width="100">
						<aui:select 
							name="dossierStatus" 
							label="step-name" 
							inlineField="<%=true %>" 
							inlineLabel="left"
							onChange='<%=renderResponse.getNamespace() + "searchByProcecssStep(this)"%>'
						>
							<aui:option value="0"><liferay-ui:message key="all"/></aui:option>
							<%
							
								if(processOrderBeans != null){
									for(ProcessOrderBean processOrderBean : processOrderBeans){
										%>
											<aui:option value="<%= processOrderBean.getProcessStepId()%>"><%=processOrderBean.getStepName() %></aui:option>
										<%
									}
								}
								
							%>
						</aui:select>
					</aui:col>
				</aui:row>
			</aui:form>
		</div>
	</aui:nav-bar-search>
</aui:nav-bar>

<aui:script use="liferay-util-list-fields,liferay-portlet-url">
	Liferay.provide(window, '<portlet:namespace/>processMultipleDossier', function() {
	
		var A = AUI();
		
		var currentURL = '<%=currentURL.toString()%>';
		
		var processOrderIds = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, '<portlet:namespace />allRowIds');
		
		processOrderIds = processOrderIds.split(",");
	
		if(processOrderIds.length > 1){
			alert('<%= UnicodeLanguageUtil.get(pageContext, "multiple-process-order-handle-is-developing") %>');
			return;
		}else if(processOrderIds.length == 0){
			alert('<%= UnicodeLanguageUtil.get(pageContext, "you-need-select-any-process-order-to-process") %>');
			return;
		}else{
			var portletURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
			portletURL.setParameter("mvcPath", "/html/portlets/processmgt/processorder/process_order_detail.jsp");
			portletURL.setWindowState("<%=LiferayWindowState.NORMAL.toString()%>"); 
			portletURL.setPortletMode("normal");
		
			portletURL.setParameter("processOrderId", processOrderIds[0]);
			portletURL.setParameter("backURL", currentURL);
			window.location.href = portletURL.toString();
		}
	});
	
	Liferay.provide(window, '<portlet:namespace/>searchByProcecssStep', function(e) {
		
		var A = AUI();
		
		var instance = A.one(e);
		
		var processStepId = instance.attr(instance.val());
		
		submitForm(document.<portlet:namespace />fmSearch);
	});
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.toolbar.jsp");
%>