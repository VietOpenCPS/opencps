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

	List<ProcessOrderBean> processOrderServices = new ArrayList<ProcessOrderBean>();
	
	List<ProcessOrderBean> processOrderSteps = new ArrayList<ProcessOrderBean>();
	
	long serviceInfoId = ParamUtil.getLong(request, "serviceInfoId");
	
	long processStepId = ParamUtil.getLong(request, "processStepId");
	
	String dossierSubStatus = ParamUtil.getString(request, "dossierSubStatus");
	
	String todolistDisplayStyle = GetterUtil.getString(portletPreferences.getValue("todolistDisplayStyle", "default"));
	
	try{
		
		if(tabs1.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)){
			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getProcessOrderServiceByUser(themeDisplay.getUserId());
			if(serviceInfoId > 0){
				processOrderSteps = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getUserProcessStep(themeDisplay.getUserId(), serviceInfoId);
			}
		}else{
			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getProcessOrderServiceJustFinishedByUser(themeDisplay.getUserId());
			if(serviceInfoId > 0){
				processOrderSteps = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getUserProcessStepJustFinished(themeDisplay.getUserId(), serviceInfoId);
			}
		}
		
		
		
	}catch(Exception e){}
	
	int colWidth = 25;
	if(!todolistDisplayStyle.equals("treemenu_left")){
		colWidth = 20;
	}
%>
<liferay-portlet:renderURL varImpl="searchURL" portletName="<%=WebKeys.PROCESS_ORDER_PORTLET %>">
	<liferay-portlet:param name="tabs1" value="<%=tabs1 %>"/>
	<c:choose>
		<c:when test="<%=tabs1.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS) %>">
			<liferay-portlet:param name="mvcPath" value='<%=templatePath +  "processordertodolist.jsp"%>'/>
		</c:when>
		<c:otherwise>
			<liferay-portlet:param name="mvcPath" value='<%=templatePath +  "processorderjustfinishedlist.jsp"%>'/>
		</c:otherwise>
	</c:choose>
</liferay-portlet:renderURL>

<aui:nav-bar cssClass="opencps-toolbar custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-button-container  nav-display-style-buttons pull-left" >
		<c:if test="<%=ProcessOrderPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ASSIGN_PROCESS_ORDER) && 
			tabs1.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS) &&
			serviceInfoId > 0 && processStepId > 0%>">
			<portlet:renderURL var="processDossierURL" windowState="<%=LiferayWindowState.NORMAL.toString() %>">
				<portlet:param name="mvcPath" value='<%=templatePath + "processordertodolist.jsp" %>'/>
				<portlet:param name="backURL" value="<%=currentURL %>"/>
			</portlet:renderURL>
			
			<div id ="<portlet:namespace />multiAssignBtn"> 
			
			</div>
			<aui:nav-item 
				cssClass="item-config search-input input-keyword"
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
			<liferay-portlet:renderURLParams varImpl="searchURL" />
				<aui:row>
					<c:choose>
						<c:when test="<%=!todolistDisplayStyle.equals(\"treemenu_left\") %>">
							<aui:col width="<%=colWidth %>" cssClass="search-col div100">
								<datamgt:ddr 
									depthLevel="1" 
									dictCollectionCode="DOSSIER_SUB_STATUS" 
									showLabel="<%=false%>"
									emptyOptionLabels="filter-by-subStatus-left"
									itemsEmptyOption="true"
									itemNames="dossierSubStatus"
									optionValueType="code"
									selectedItems="<%=dossierSubStatus %>"
									cssClass="search-input select-box"
								/>
							
							</aui:col>
						</c:when>
							<c:otherwise>
								<aui:input name="dossierSubStatus" type="hidden" value="<%=dossierSubStatus %>"></aui:input>
							</c:otherwise>
					</c:choose>
					<aui:col width="<%=colWidth %>" cssClass="search-col div100">
						<aui:select 
							name="processOrderStage" 
							label="<%=StringPool.BLANK %>" 
							inlineField="<%=true %>" 
							inlineLabel="left"
							onChange='<%=renderResponse.getNamespace() + "searchByProcecssStep(this)"%>'
							cssClass="search-input select-box"
						>
							<aui:option value="<%=false %>"><liferay-ui:message key="filter-process-order-stage-0"/></aui:option>
							<aui:option value="<%=true %>"><liferay-ui:message key="filter-process-order-stage-1"/></aui:option>
						</aui:select>
					</aui:col>
					<aui:col width="<%=colWidth %>" cssClass="search-col">
						<aui:select 
							name="serviceInfoId" 
							label="<%=StringPool.BLANK %>" 
							inlineField="<%=true %>" 
							inlineLabel="left"
							onChange='<%=renderResponse.getNamespace() + "searchByProcecssOrderService(this)"%>'
							cssClass="search-input select-box"
						>
							<aui:option value="0" title="service-info"><liferay-ui:message key="filter-service-info"/></aui:option>
							<%
							
								if(processOrderServices != null){
									for(ProcessOrderBean processOrderService : processOrderServices){
										%>
											<aui:option title="<%=processOrderService.getServiceName()%>" value="<%= processOrderService.getServiceInfoId()%>">
												<%=StringUtil.shorten(processOrderService.getServiceName(), 50) %>
											</aui:option>
										<%
									}
								}
								
							%>
						</aui:select>
					</aui:col>
				
					<aui:col width="<%=colWidth %>" cssClass="search-col">
						<aui:select 
							name="processStepId" 
							label="<%=StringPool.BLANK %>" 
							inlineField="<%=true %>" 
							inlineLabel="left"
							onChange='<%=renderResponse.getNamespace() + "searchByProcecssStep(this)"%>'
							cssClass="search-input select-box"
						>
							<aui:option value="0"><liferay-ui:message key="filter-process-step"/></aui:option>
							<%
							
								if(processOrderSteps != null){
									for(ProcessOrderBean processOrderStep : processOrderSteps){
										%>
											<aui:option value="<%= processOrderStep.getProcessStepId()%>"><%=processOrderStep.getStepName() %></aui:option>
										<%
									}
								}
								
							%>
						</aui:select>
					</aui:col>
					<aui:col width="<%=colWidth %>" cssClass="search-col">
						<liferay-ui:input-search 
							id="keywords"
							name="keywords"
							title='<%= LanguageUtil.get(locale, "keywords") %>'
							placeholder='<%=LanguageUtil.get(locale, "keywords") %>'
							cssClass="search-input input-keyword"
						/>
					</aui:col>
				</aui:row>
				<%-- <aui:row>
					<aui:col width="30">
						<liferay-ui:input-date 
		 					nullable="true"
		 					dayParam="estimateDatetimeDayFrom"
		 					dayValue="<%= 0 %>"
		 					monthParam="estimateDatetimeDayMonthFrom"
		 					monthValue="<%= 0 %>"
		 					name="estimateDatetimeFrom"
		 					yearParam="estimateDatetimeYearFrom"
		 					yearValue="<%= 0 %>"
		 					formName="fmSearch"
		 					autoFocus="<%=true %>"
		 					cssClass="input100"
		 					
		 				>
		 				</liferay-ui:input-date>
					</aui:col>
					
					<aui:col width="30">
						<liferay-ui:input-date 
		 					nullable="true"
		 					dayParam="estimateDatetimeDayTo"
		 					dayValue="<%= 0 %>"
		 					monthParam="estimateDatetimeDayMonthTo"
		 					monthValue="<%= 0 %>"
		 					name="estimateDatetimeTo"
		 					yearParam="estimateDatetimeYearTo"
		 					yearValue="<%=0 %>"
		 					formName="fmSearch"
		 					autoFocus="<%=true %>"
		 					cssClass="input100"
		 				>
		 				</liferay-ui:input-date> --%>
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
		
		if(processOrderIds != ''){
			if(processOrderIds.length > 1){
				// alert('<%= UnicodeLanguageUtil.get(pageContext, "multiple-process-order-handle-is-developing") %>');
				var multiAssignURL = Liferay.PortletURL.createURL('<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>');
				multiAssignURL.setParameter("mvcPath","/html/portlets/processmgt/processorder/assign_multil_process_order.jsp");
				multiAssignURL.setParameter("processOrderIds",processOrderIds.toString());
				multiAssignURL.setWindowState("<%=LiferayWindowState.POP_UP.toString()%>");
				multiAssignURL.setPortletMode("normal");
				openDialog(multiAssignURL.toString(), "assign-multi-dossier", "assign-multi-dossier");
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
		}else{
			alert('<%= UnicodeLanguageUtil.get(pageContext, "you-need-select-any-process-order-to-process") %>');
			return;
		}
	});
	
	A.one('#<portlet:namespace />dossierSubStatus').on('change', function(){
		submitForm(document.<portlet:namespace />fmSearch);
	});
	
	Liferay.provide(window, '<portlet:namespace/>searchByProcecssStep', function(e) {
		submitForm(document.<portlet:namespace />fmSearch);
	},['liferay-portlet-url']);
	
	Liferay.provide(window, '<portlet:namespace/>searchByProcecssOrderService', function(e) {
		submitForm(document.<portlet:namespace />fmSearch);
	},['liferay-portlet-url']);
</aui:script>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.toolbar.jsp");
%>