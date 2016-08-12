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
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearchTerms"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderSearch"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="org.opencps.processmgt.util.ProcessUtils"%>
<%@page import="org.opencps.dossiermgt.bean.ProcessOrderBean"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.search.ProcessOrderDisplayTerms"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />
<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "processordertodolist.jsp");
	iteratorURL.setParameter("tabs1", ProcessUtils.TOP_TABS_PROCESS_ORDER_FINISHED_PROCESSING);
	
	List<ProcessOrderBean> processOrders =  new ArrayList<ProcessOrderBean>();
	
	int totalCount = 0;
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("boundcol1");
	headerNames.add("boundcol2");
	headerNames.add("boundcol3");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
	//RowChecker rowChecker = new RowChecker(liferayPortletResponse);
%>
<aui:form name="fm">
<div class="opencps-searchcontainer-wrapper">
		<liferay-ui:search-container 
			searchContainer="<%= new ProcessOrderSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
			headerNames="<%= headers %>"
		>
		
			<liferay-ui:search-container-results>
				<%
					ProcessOrderSearchTerms searchTerms = (ProcessOrderSearchTerms)searchContainer.getSearchTerms();
					
					long serviceInfoId = searchTerms.getServiceInfoId();
				
					long processStepId = searchTerms.getProcessStepId();
					
					try{
						
						%>
							<%@include file="/html/portlets/processmgt/processorder/processorder_justfinished_searchresults.jspf" %>
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
						String deadlineVal = Validator.isNotNull(processOrder.getDealine()) ? processOrder.getDealine() : StringPool.DASH;
					%>
					
						<liferay-util:buffer var="boundcol1">
								<div class="row-fluid">
									<div class="span1">
										<i class='<%="fa fa-circle sx10 " + processOrder.getReceptionNo()%>'></i>
									</div>
									
									<div class="row-fluid">
										<div width="5px"></div>
										<div class="span5 bold-label">
											<liferay-ui:message key="service-name"/>
										</div>
										<div class="span6">
											<%=processOrder.getServiceName() %>
										</div>
									</div>
								</div>
							</liferay-util:buffer>
							
							
							<liferay-util:buffer var="boundcol2">
							<div class="row-fluid">
								<div width="5px"></div>
								<div class="span5 bold-label"><liferay-ui:message key="subject-name"/></div>
								<div class="span6">
									<%=processOrder.getSubjectName() %>
								</div>
							</div>
							
							<div class="row-fluid">
								<div width="5px"></div>
								<div class="span5 bold-label">
									 <liferay-ui:message key="assign-to-user"/>
								</div>
								
								<div class="span6">
									<%=processOrder.getAssignToUserName() %>
								</div>
							</div>
							
							<div class="row-fluid">
								<div width="5px"></div>
								<div class="span5 bold-label">
									<liferay-ui:message key="step-name"/>
								</div>
								<div class="span6">
									<%=processOrder.getStepName() %>
								</div>
							</div>
							
							<div class="row-fluid">
								<div width="5px"></div>
									<div class="span5 bold-label">
										<liferay-ui:message key="dealine"/>
									</div>
									
									<div class='<%="span6"%>'>
										<%= deadlineVal %>
									</div>
								</div>
							</liferay-util:buffer>
				
					<%
						row.setClassName("opencps-searchcontainer-row");
						row.addText(boundcol1);
						row.addText(boundcol2);
						row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  "/html/portlets/processmgt/processorder/justfinished_actions.jsp", config.getServletContext(), request, response);
						
					%>	
				</liferay-ui:search-container-row> 
			
			<liferay-ui:search-iterator type="opencs_page_iterator"/>
		</liferay-ui:search-container>
	</div>
</aui:form>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.processmgt.processorder.processorderjustfinishedlist.jsp");
%>