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
<%@page import="org.opencps.usermgt.service.EmployeeLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="java.util.Date"%>
<%@page import="org.opencps.holidayconfig.util.HolidayCheckUtils"%>
<%@page import="org.opencps.processmgt.util.OutDateStatus"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.processmgt.service.ProcessStepLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ProcessStep"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.servicemgt.model.ServiceInfo"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@page import="java.util.List"%>
<%@ include file="/init.jsp"%>
<%
	long actionUserId = (Validator.isNotNull(user)) ? user.getUserId() : 0L;
	List<ProcessOrder> processOrders = new ArrayList<ProcessOrder>();
	int totalCount = 0;
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "reassignlist.jsp");
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("col1");
	headerNames.add("col2");
	headerNames.add("col3");
	headerNames.add("col4");
	headerNames.add("col5");
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
	
	boolean success = false;

%>

<div class="opencps-searchcontainer-wrapper">
	<liferay-ui:search-container 
			emptyResultsMessage="no-process-order-were-found"
			iteratorURL="<%=iteratorURL %>"
			delta="<%=20 %>"
			deltaConfigurable="true"
			headerNames="<%= headers %>"
	>
	<liferay-ui:search-container-results>
		<%
			try {
				processOrders = ProcessOrderLocalServiceUtil.searchReAssigToUser(actionUserId, 
						searchContainer.getStart(), searchContainer.getEnd());
				totalCount = ProcessOrderLocalServiceUtil.countReAssigToUser(actionUserId);
			} catch(Exception e) {
				
			}
			
			results = processOrders;
			total = totalCount;
			
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
			className="org.opencps.processmgt.model.ProcessOrder" 
			modelVar="processOrder" 
			keyProperty="processOrderId"
	>
	
		<%
			String serviceName = StringPool.BLANK;
			String receptionNo = StringPool.BLANK;
			String assignUserName = StringPool.BLANK;
			String processStepName = StringPool.BLANK;
			ProcessStep processStep = null;
			OutDateStatus outDateStatus = new OutDateStatus();
			int outdate = 0;
			
			try {
				Dossier dossier = DossierLocalServiceUtil.getDossier(processOrder.getDossierId());
				ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(dossier.getServiceInfoId());
				
				serviceName = serviceInfo.getServiceName();
				receptionNo = dossier.getReceptionNo();
				
				processStep = ProcessStepLocalServiceUtil.getProcessStep(processOrder.getProcessStepId());
				
				processStepName = processStep.getStepName();
				
				Employee assignUser = EmployeeLocalServiceUtil.getEmployeeByMappingUserId(scopeGroupId, processOrder.getAssignToUserId());
				
				if(Validator.isNotNull(assignUser)) {
					assignUserName = assignUser.getFullName();
				}
			} catch(Exception e) {}
			
			if(Validator.isNotNull(processStep)) {
				outDateStatus = HolidayCheckUtils
						.checkActionDateOverStatus((processOrder.getActionDatetime()), new Date(), processStep.getDaysDuration());
			}
			
			/* if(Validator.isNotNull(employee)) {
				assignUserName = employee.getFullName();
			} */
			//final String hrefFix = "location.href='#'";
			
		%>
		
			<liferay-util:buffer var="boundcol1">	
					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="service-name"/>
						</div>
						<div class="span7"><%=serviceName%></div>
					</div>
					
					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="reception-no"/>
						</div>
						<div class="span7"><%=receptionNo%></div>
					</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="boundcol2">	
					<div class="row-fluid">
						
						<div class="span7 bold-label">
							<liferay-ui:message key="assign-user"/>
						</div>
						<div class="span5"><%=assignUserName%></div>
					</div>
					
					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="step-name"/>
						</div>
						<div class="span7"><%=processStepName%></div>
					</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="boundcol3">	
					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="action-date"/>
						</div>
						<div class="span7">
							<%=(Validator.isNotNull(processOrder.getActionDatetime())) 
								? DateTimeUtil.convertDateToString(processOrder.getActionDatetime(), DateTimeUtil._VN_DATE_TIME_FORMAT)
										: DateTimeUtil._EMPTY_DATE_TIME
							%>
						</div>
					</div>
					
					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="days-duration"/>
						</div>
						<div class="span7"><%=(Validator.isNotNull(processStep) ? processStep.getDaysDuration() : StringPool.DASH)%></div>
					</div>
					
					<div class="row-fluid">
						
						<div class="span5 bold-label">
							<liferay-ui:message key="days-outdate"/>
						</div>
						<div class="span7">
							<c:choose>
								<c:when test="<%=outDateStatus.isOutDate()%>">
									<%=LanguageUtil.get(locale, "late") + StringPool.SPACE + 
										String.valueOf(outDateStatus.getDaysOutdate() + StringPool.SPACE + LanguageUtil.get(locale, "day"))%>
								</c:when>
								<c:otherwise>
									<%=LanguageUtil.get(locale, "remanet") + StringPool.SPACE + 
										String.valueOf(outDateStatus.getDaysOutdate() + StringPool.SPACE +LanguageUtil.get(locale, "day"))%>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
			</liferay-util:buffer>
			<%
				row.setClassName("opencps-searchcontainer-row");
				row.addText(String.valueOf(row.getPos() + 1));
				row.addText(boundcol1);
				row.addText(boundcol2);
				row.addText(boundcol3);
				//row.addJSP("center", SearchEntry.DEFAULT_VALIGN, "/html/portlets/processmgt/reassign/reassign_action.jsp", config.getServletContext(), request, response);
			%>
		
			<liferay-ui:search-container-column-jsp 
				path="/html/portlets/processmgt/reassign/reassign_action.jsp"
				name="action" cssClass="width80"
			/>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator type="opencs_page_iterator"/>
	
	</liferay-ui:search-container>
</div>

