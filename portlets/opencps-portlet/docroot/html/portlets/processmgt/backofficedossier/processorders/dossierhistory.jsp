
<%@page import="org.opencps.dossiermgt.service.FileGroupLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.FileGroup"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.service.ProcessOrderLocalService"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.servicemgt.service.ServiceInfoLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.processmgt.service.ActionHistoryLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ActionHistory"%>
<%@page import="org.opencps.processmgt.model.ProcessOrder"%>
<%@ include file="../../init.jsp"%>

<%
	long dossierId = ParamUtil.getLong(request, "dossierId");
	
	Dossier dossier = null;
	
	try {
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
	}
	catch (NoSuchDossierException ex) {
		
	}
	
	ProcessOrder processOrder = null;
	
	try {
		processOrder = ProcessOrderLocalServiceUtil.getProcessOrder(dossierId, 0);
	}
	catch (Exception ex) {
		
	}
	
	long processOrderId = Validator.isNotNull(processOrder) ? processOrder.getProcessOrderId() : 0L;

	System.out.print("******#############*********** dossierId " + dossierId);
	
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "dossier/history.jsp");
	
	List<ActionHistory> actionHistories = new ArrayList<ActionHistory>();
	
	SimpleDateFormat sdf = new SimpleDateFormat();
	
	
	ServiceInfo serviceInfo = null;
	String receptionNo = StringPool.BLANK;
	String serviceName = StringPool.BLANK;
	if(Validator.isNotNull(processOrder)) {
		try {
			/* dossier = DossierLocalServiceUtil.getDossier(processOrder.getDossierId()); */
			receptionNo = dossier.getReceptionNo();
			
			serviceInfo = ServiceInfoLocalServiceUtil.getServiceInfo(processOrder.getServiceInfoId());
			serviceName = serviceInfo.getServiceName();
		} catch (Exception e) {
			
		}
	}
	 
	
%>
<div class="ocps-title-detail">
	<div class="ocps-title-detail-top">	
		<label class="service-reception-label">
			<liferay-ui:message key="reception-no"/> 
		</label>
		<p class="service-reception-no"><%=receptionNo %></p>
	</div>
	<div class="ocps-title-detail-bot">
		<label class="service-name-label">
			<liferay-ui:message key="dossier-name"/> 
		</label>
		<p class="service-service-name"><%=serviceName%></p>
	</div>
</div>

<div class="bound-search-container-history">
	<liferay-ui:search-container 
		emptyResultsMessage="no-action-history-were-found"
		iteratorURL="<%=iteratorURL %>"
		delta="<%=20 %>"
		deltaConfigurable="true"
		>
		<liferay-ui:search-container-results>
			<%
				actionHistories =  ActionHistoryLocalServiceUtil.getActionHistoryByProcessOrderId(processOrderId, searchContainer.getStart(), searchContainer.getEnd());
				
				results = actionHistories;
				total = ActionHistoryLocalServiceUtil
					.countActionHistoryByProcessId(processOrderId);
				pageContext.setAttribute("results", results);
				pageContext.setAttribute("total", total);
			%>
		</liferay-ui:search-container-results>
		<liferay-ui:search-container-row 
			className="org.opencps.processmgt.model.ActionHistory" 
			modelVar="actionHistory" 
			keyProperty="actionHistoryId"
		>
		
			 <%
				String date = StringPool.BLANK;
				
				if (Validator.isNotNull(actionHistory.getCreateDate())) {
					date = DateTimeUtil.
									convertDateToString(actionHistory.getCreateDate(),
										DateTimeUtil._VN_DATE_FORMAT);
				}
				
				String userActionName = StringPool.BLANK;
				
				try {
					if (Validator.isNotNull(actionHistory.getActionUserId()) || actionHistory.getActionUserId() != 0) {
						userActionName = UserLocalServiceUtil
										.getUser(actionHistory.getActionUserId()).getFullName();
					}
				} catch (Exception e ) {
					
				}
				
			%>
			
				<aui:row cssClass="top-line pd_b20 pd_t20">
					<aui:col width="50">
						<aui:row>
							<span class="span4 bold">
								<liferay-ui:message key="step-name" />
							</span>
							
							<span class="span8">
								<%=actionHistory.getStepName()%>
							</span>
						</aui:row>
						
						<aui:row>
							<span class="span4 bold">
								<liferay-ui:message key="action-date" />
							</span>
							
							<span class="span8">
								<%=date%>
							</span>
						</aui:row>
					</aui:col>
					<aui:col width="50">
						<aui:row>
							<span class="span4 bold">
								<liferay-ui:message key="action-user" />
							</span>
							
							<span class="span8">
								<%=userActionName%>
							</span>
						</aui:row>
					
						<aui:row>
							<span class="span4 bold">
								<liferay-ui:message key="action-name" />
							</span>
							
							<span class="span8">
								<%=actionHistory.getActionName()%>
							</span>
						</aui:row>
						
						<aui:row>
							<span class="span4 bold">
								<liferay-ui:message key="days-delay" />
							</span>
							
							<span class="span8">
								<%=String.valueOf(actionHistory.getDaysDelay())%>
							</span>
							
						</aui:row>
					</aui:col>
				</aui:row>
				
		</liferay-ui:search-container-row>
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	</liferay-ui:search-container>
</div>












<%-- <%@page import="org.opencps.usermgt.service.EmployeeLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="java.text.Format"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="org.opencps.processmgt.service.ActionHistoryLocalServiceUtil"%>
<%@page import="org.opencps.processmgt.model.ActionHistory"%>
<%@page import="org.opencps.dossiermgt.NoSuchDossierException"%>
<%@page import="org.opencps.dossiermgt.service.DossierLocalServiceUtil"%>
<%@page import="org.opencps.dossiermgt.model.Dossier"%>
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
<%@ include file="../../init.jsp"%>
<%
	Integer dossierId = ParamUtil.getInteger(request, "dossierId");
	
	Dossier dossier = null;
	
	try {
		dossier = DossierLocalServiceUtil.getDossier(dossierId);
	}
	catch (NoSuchDossierException ex) {
		
	}
	List<ActionHistory> histories = ActionHistoryLocalServiceUtil.searchActionHistoryByDossierId(0, dossierId);
	Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
%>
<c:if test="<%= histories.size() > 0 %>">
<table class="table table-bordered table-hover table-striped">
	<thead class="table-columns">
		<tr>
			<th class="table-first-header">
				<liferay-ui:message key="no"/>
			</th>
			<th>
				<liferay-ui:message key="step-name"/>
			</th>
			<th>
				<liferay-ui:message key="action-name"/>
			</th>
			<th>
				<liferay-ui:message key="action-datetime"/>
			</th>
			<th>
				<liferay-ui:message key="action-user"/>
			</th>
			<th>
				<liferay-ui:message key="action-note"/>
			</th>
			<th class="table-last-header">
				<liferay-ui:message key="estimatedate-status"/>
			</th>
		</tr>
	</thead>
	<tbody>
		<%
			for (int i = 0; i < histories.size(); i++) {
		%>
			<tr>
				<td>
					<%= i + 1 %>
				</td>
				<td>
					<%= histories.get(i).getStepName() %>
				</td>
				<td>
					<%= histories.get(i).getActionName() %>
				</td>
				<td>
					<%= dateFormatDate.format(histories.get(i).getActionDatetime()) %>
				</td>
				<td>
					<%
						Employee employee2 = EmployeeLocalServiceUtil.getEmployeeByMappingUserId(scopeGroupId, histories.get(i).getActionUserId());
					%>
					<%= employee2.getFullName() %>
				</td>
				<td>
					<%= histories.get(i).getActionNote() %>
				</td>
				<td>
					<%
						
					%>
				</td>
			</tr>
		<%
			}
		%>
	</tbody>
</table>
</c:if>
 --%>