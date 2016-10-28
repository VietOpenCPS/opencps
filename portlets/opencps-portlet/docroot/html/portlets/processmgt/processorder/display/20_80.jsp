
<%@page import="java.util.LinkedHashMap"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearchTerms"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearch"%>
<%@page import="org.opencps.util.PortletPropsValues"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="org.opencps.processmgt.util.ProcessOrderUtils"%>
<%@page import="org.opencps.servicemgt.util.ServiceUtil"%>
<%@page import="org.opencps.backend.util.DossierNoGenerator"%>
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

<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.datamgt.model.DictItem"%>
<%@page import="org.opencps.datamgt.service.DictItemLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.bean.DossierBean"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierTemplateException"%>
<%@page import="org.opencps.dossiermgt.RequiredDossierPartException"%>
<%@page import="org.opencps.dossiermgt.search.DossierDisplayTerms"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearch"%>
<%@page import="org.opencps.dossiermgt.search.DossierSearchTerms"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.util.DossierMgtUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.util.DictItemUtil"%>


<%@ include file="../init.jsp"%>

<liferay-ui:success  key="<%=MessageKeys.DEFAULT_SUCCESS_KEY %>" message="<%=MessageKeys.DEFAULT_SUCCESS_KEY %>"/>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "processordertodolist.jsp");
	iteratorURL.setParameter("tabs1", ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS);
	
	List<ProcessOrderBean> processOrders =  new ArrayList<ProcessOrderBean>();
	
	int totalCount = 0;
	
	RowChecker rowChecker = new RowChecker(liferayPortletResponse);
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("col1");
	headerNames.add("col2");
	headerNames.add("col3");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
	String tabs1 = ParamUtil.getString(request, "tabs1", ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS);

	List<ProcessOrderBean> processOrderServices = new ArrayList<ProcessOrderBean>();
	
	List<ProcessOrderBean> processOrderSteps = new ArrayList<ProcessOrderBean>();
	
	
	
	long serviceInfoId = ParamUtil.getLong(request, "serviceInfoId");
	
	long processStepId = ParamUtil.getLong(request, "processStepId");
	
	try{
		
		if(tabs1.equals(ProcessUtils.TOP_TABS_PROCESS_ORDER_WAITING_PROCESS)){
			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getProcessOrderServiceByUser(themeDisplay.getUserId());
			
			for(ProcessOrderBean ett : processOrderServices){
				processOrderSteps.addAll((List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getUserProcessStep(themeDisplay.getUserId(), ett.getServiceInfoId()));
			}
			
			if(serviceInfoId > 0){
				processOrderSteps = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getUserProcessStep(themeDisplay.getUserId(), serviceInfoId);
			}
		}else{
			processOrderServices = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getProcessOrderServiceJustFinishedByUser(themeDisplay.getUserId());
			
			for(ProcessOrderBean ett : processOrderServices){
				processOrderSteps.addAll((List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getUserProcessStep(themeDisplay.getUserId(), ett.getServiceInfoId()));
			}
			
			if(serviceInfoId > 0){
				processOrderSteps = (List<ProcessOrderBean>) ProcessOrderLocalServiceUtil.getUserProcessStepJustFinished(themeDisplay.getUserId(), serviceInfoId);
			}
		}
		
		
		
	}catch(Exception e){}
	
	//remove duplicates process orders
	Map<String, ProcessOrderBean> cleanMap = new LinkedHashMap<String, ProcessOrderBean>();
	for (int i = 0; i < processOrderSteps.size(); i++) {
	     cleanMap.put(processOrderSteps.get(i).getProcessStepId()+"", processOrderSteps.get(i));
	}
	processOrderSteps = new ArrayList<ProcessOrderBean>(cleanMap.values());
	
	JSONObject arrayParam = JSONFactoryUtil
		    .createJSONObject();
	arrayParam.put("serviceInfoId", (serviceInfoId > 0) ? String.valueOf(serviceInfoId):StringPool.BLANK);
	arrayParam.put("processStepId", (processStepId > 0) ? String.valueOf(processStepId):StringPool.BLANK);
	arrayParam.put("tabs1", tabs1);
	
	String processStepIdJsonData = ProcessOrderUtils.generateTreeView(
			processOrderSteps, 
			LanguageUtil.get(locale, "filter-process-step").replaceAll("--", StringPool.BLANK) , 
			"radio");
%>

<aui:row>
	<aui:col width="25">
	
		<div style="margin-bottom: 25px;" class="opencps-searchcontainer-wrapper default-box-shadow radius8">
		
			<div id="processStepIdTree" class="openCPSTree"></div>
			
		</div>
	
	<div class="opencps-searchcontainer-wrapper default-box-shadow radius8">
		
		<div id="serviceInfoIdTree" class="openCPSTree"></div>
		
		<%
		
		String serviceInfoIdJsonData = ProcessOrderUtils.generateTreeView(
				processOrderServices, 
				LanguageUtil.get(locale, "service-info").replaceAll("--", StringPool.BLANK) , 
				"radio");
		%>
		
	</div>
	
<liferay-portlet:actionURL  var="menuCounterUrl" name="menuCounterAction"/>
<liferay-portlet:actionURL  var="menuCounterServiceInfoIdUrl" name="menuCounterServiceInfoIdAction"/>
<aui:script use="liferay-util-window,liferay-portlet-url">

	var serviceInfoId = '<%=String.valueOf(serviceInfoId) %>';
	var processStepId = '<%=String.valueOf(processStepId) %>';
	var serviceInfoIdJsonData = '<%=serviceInfoIdJsonData%>';
	var processStepIdJsonData = '<%=processStepIdJsonData%>';
	var arrayParam = '<%=arrayParam.toString() %>';
	AUI().ready(function(A){
		buildTreeView("serviceInfoIdTree", 
				"serviceInfoId", 
				serviceInfoIdJsonData, 
				arrayParam, 
				'<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>', 
				'<%=templatePath + "processordertodolist.jsp" %>', 
				'<%=LiferayWindowState.NORMAL.toString() %>', 
				'normal',
				'<%=menuCounterServiceInfoIdUrl.toString() %>',
				serviceInfoId,
				'<%=renderResponse.getNamespace() %>',
				'<%=hiddenTreeNodeEqualNone%>');
		buildTreeView("processStepIdTree", 
				'processStepId', 
				processStepIdJsonData, 
				arrayParam, 
				'<%= PortletURLFactoryUtil.create(request, WebKeys.PROCESS_ORDER_PORTLET, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>', 
				'<%=templatePath + "processordertodolist.jsp" %>', 
				'<%=LiferayWindowState.NORMAL.toString() %>', 
				'normal',
				'<%=menuCounterUrl.toString() %>',
				processStepId,
				'<%=renderResponse.getNamespace() %>',
				'<%=hiddenTreeNodeEqualNone%>');
		
	});
	
</aui:script>
	
	</aui:col>
	<aui:col width="75" >

		<aui:form name="fm">
			<div class="opencps-searchcontainer-wrapper">
				<liferay-ui:search-container 
						searchContainer="<%= new ProcessOrderSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
						rowChecker="<%=rowChecker%>"
						headerNames="<%= headers%>"
					>
					
						<liferay-ui:search-container-results>
							<%
								ProcessOrderSearchTerms searchTerms = (ProcessOrderSearchTerms)searchContainer.getSearchTerms();
							
								serviceInfoId = searchTerms.getServiceInfoId();
								
								processStepId = searchTerms.getProcessStepId();
								
								long assignToUserId = themeDisplay.getUserId();
								try{
									
									%>
										<%@include file="/html/portlets/processmgt/processorder/process_order_search_results.jspf" %>
									<%
								}catch(Exception e){
									_log.error(e);
								}
							
								total = totalCount;
								results = processOrders;
								
								pageContext.setAttribute("results", results);
								pageContext.setAttribute("total", total);
							%>
						</liferay-ui:search-container-results>	
							<liferay-ui:search-container-row 
								className="org.opencps.dossiermgt.bean.ProcessOrderBean" 
								modelVar="processOrder" 
								keyProperty="processOrderId"
								rowVar="row"
								stringKey="<%=true%>"
								
							>
								<%
									PortletURL processURL = renderResponse.createRenderURL();
									processURL.setParameter("mvcPath", templatePath + "process_order_detail.jsp");
									processURL.setParameter(ProcessOrderDisplayTerms.PROCESS_ORDER_ID, String.valueOf(processOrder.getProcessOrderId()));
									processURL.setParameter("backURL", currentURL);
									processURL.setParameter("isEditDossier", (processOrder.isReadOnly() || (processOrder.getAssignToUsesrId() != 0 &&  processOrder.getAssignToUsesrId() != user.getUserId())) ? String.valueOf(false) : String.valueOf(true));
								
									String deadlineVal = Validator.isNotNull(processOrder.getDealine()) ? processOrder.getDealine() : StringPool.DASH;
									
									String hrefFix = "location.href='" + processURL.toString()+"'";
									String cssStatusColor = "status-color-" + processOrder.getDossierStatus();
								%>
								
								<liferay-util:buffer var="boundcol1">
									<div class="row-fluid">	
										<div class="row-fluid">
											<div class='<%= "text-align-right span1 " + cssStatusColor%>'>
												<i class='<%="fa fa-circle sx10 " + processOrder.getDossierStatus()%>'></i>
											</div>
											<div class="span2 bold">
												<liferay-ui:message key="reception-no"/>
											</div>
											<div class="span9">
												<%=processOrder.getReceptionNo() %>
											</div>
										</div>
										
										<div class="row-fluid">
											<div class='<%= "text-align-right span1 " + cssStatusColor%>'>
											</div>
											<div class="span2 bold">
												<liferay-ui:message key="service-name"/>
											</div>
											<div class="span9">
												<%=processOrder.getServiceName() %>
											</div>
										</div>
									</div>
								</liferay-util:buffer>
								
								
								<liferay-util:buffer var="boundcol2">
								<div class="row-fluid min-width340">
									<div class="span5 bold">
										<liferay-ui:message key="subject-name"/>	
									</div>
									<div class="span7">
										<%=processOrder.getSubjectName() %>
									</div>
								</div>
								
								<div class="row-fluid" >
									<div class="span5 bold">
										 <liferay-ui:message key="assign-to-user"/>
									</div>
									
									<div class="span7">
										<%=processOrder.getAssignToUserName() %>
									</div>
								</div>
								
								<div class="row-fluid min-width340">
									<div class="span5 bold">
										<liferay-ui:message key="step-name"/>
									</div>
									<div class='<%="span7 " + cssStatusColor %>'>
										<%=processOrder.getStepName() %>
									</div>
								</div>
								
								<div class="row-fluid min-width340">
										<div class="span5 bold">
											<liferay-ui:message key="dealine"/>
										</div>
										
										<div class='<%="span7"%>'>
											<%= deadlineVal %>
										</div>
									</div>
								</liferay-util:buffer>
								<%
									
									
									String actionButt = LanguageUtil.get(portletConfig, themeDisplay.getLocale(), "action");
									row.setClassName("opencps-searchcontainer-row");
									row.addText(boundcol1);
									row.addText(boundcol2);
									row.addButton(actionButt, hrefFix);
									row.setClassName((processOrder.isReadOnly() || (processOrder.getAssignToUsesrId() != 0 &&  processOrder.getAssignToUsesrId() != user.getUserId())) ? "readonly" : StringPool.BLANK);
									
									//row.setClassHoverName("");
								%>	
							</liferay-ui:search-container-row> 
						
						<liferay-ui:search-iterator type="opencs_page_iterator"/>
					</liferay-ui:search-container>
			</div>
		</aui:form>
	</aui:col>
</aui:row>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.dossiermgt.frontoffice.display.default.jsp");
%>
