
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

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.statisticsmgt.service.DossiersStatisticsLocalServiceUtil"%>
<%@page import="org.opencps.statisticsmgt.model.DossiersStatistics"%>

<%@ include file="../../init.jsp" %>

<%
	List<DossiersStatistics> dossiersStatistics = new ArrayList<DossiersStatistics>();
	try {
		for(int i = 1; i <= currentMonth; i++){
			DossiersStatistics statistics = DossiersStatisticsLocalServiceUtil
					.getDossiersStatisticsByG_GC_DC_M_Y_L(scopeGroupId, StringPool.BLANK,
							StringPool.BLANK, i, currentYear, 0);
			dossiersStatistics.add(statistics);
		}
	} catch (Exception e) {

	}
%>

<c:choose>
	<c:when test="<%=dossiersStatistics != null && !dossiersStatistics.isEmpty() %>">
		<%
			int remainingNumber = 0;
			int receivedNumber = 0;
			int ontimeNumber = 0;
			int overtimeNumber = 0;
			int processingNumber = 0;
			int delayingNumber = 0;
			
			for(DossiersStatistics statistics : dossiersStatistics){
				receivedNumber += statistics.getReceivedNumber();
				ontimeNumber += statistics.getOntimeNumber();
				overtimeNumber += statistics.getOvertimeNumber();
			}
			
			processingNumber += dossiersStatistics.get(dossiersStatistics.size() - 1).getProcessingNumber();
			delayingNumber += dossiersStatistics.get(dossiersStatistics.size() - 1).getDelayingNumber();
		%>
		
		<c:choose>
			<c:when test="<%=portletDisplayDDMTemplateId > 0 %>">
				<%= PortletDisplayTemplateUtil.renderDDMTemplate(pageContext, portletDisplayDDMTemplateId, dossiersStatistics) %>
			</c:when>
			
			<c:otherwise>
			
				<div class="widget-wrapper">
					<div class="widget-header">
						<span class="span8">
							<liferay-ui:message key="stats-in-year"/>
						</span>
						<span class="span4"><%=currentYear %></span>
					</div>
					
					<div class="widget-content">
						<span class="span8">
							<liferay-ui:message key="remaining-number"/>
						</span>
						<span class="span4"><%=remainingNumber %></span>
					</div>
					
					<div class="widget-content">
						<span class="span8">
							<liferay-ui:message key="received-number"/>
						</span>
						<span class="span4"><%=receivedNumber %></span>
					</div>
					
					<div class="widget-content">
						<span class="span8">
							<liferay-ui:message key="ontime-number"/>
						</span>
						<span class="span4"><%=ontimeNumber %></span>
					</div>
					
					<div class="widget-content">
						<span class="span8">
							<liferay-ui:message key="overtime-number"/>
						</span>
						<span class="span4"><%=overtimeNumber %></span>
					</div>
					
					<div class="widget-content">
						<span class="span8">
							<liferay-ui:message key="processing-number"/>
						</span>
						<span class="span4"><%=processingNumber %></span>
					</div>
					
					<div class="widget-content">
						<span class="span8">
							<liferay-ui:message key="delaying-number"/>
						</span>
						<span class="span4"><%=delayingNumber %></span>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-alert"><liferay-ui:message key="not-found-stats"/></div>
	</c:otherwise>
</c:choose>