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
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>

<%@ include file="../../init.jsp"%>
<%
	long workingUnitId = ParamUtil.getLong(request, EmployeeDisplayTerm.WORKING_UNIT_ID, 0L);

	long mainJobPosId  = ParamUtil.getLong(request, EmployeeDisplayTerm.MAIN_JOBPOS_ID);
	
	List<WorkingUnit> workingUnits = UserMgtUtil.getWorkingUnitsForEmployess(scopeGroupId, workingUnitId);
	
	List<JobPos> jobPoses = new ArrayList<JobPos>();
	
	if(workingUnitId > 0){
		try{
			jobPoses = JobPosLocalServiceUtil.getJobPoss(scopeGroupId, workingUnitId);
		}catch(Exception e){
			_log.error(e);
		}
	}
%>

<aui:col width="50">
	<aui:select 
		name='<%=EmployeeDisplayTerm.WORKING_UNIT_ID %>' 
		label='<%=StringPool.BLANK %>' 
		onChange='<%=renderResponse.getNamespace() + "getJobPosByWorkingUnitId(this)" %>'
		required='<%=true %>'
	>
		<aui:option value="0"><liferay-ui:message key="select-working-unit"/></aui:option>
		<%
			if(workingUnits != null){
				for(WorkingUnit workingUnit : workingUnits){
					%>
						<aui:option value="<%= workingUnit.getWorkingunitId()%>"><%=workingUnit.getName() %></aui:option>
					<%
				}
			}
		%>
	</aui:select>
</aui:col>

<aui:col width="50">
	<aui:select 
		name='<%=EmployeeDisplayTerm.MAIN_JOBPOS_ID %>' 
		label='<%=StringPool.BLANK %>'
		required='<%=true %>'
	>
		<aui:option value=""><liferay-ui:message key="select-jobpos"/></aui:option>
		<%
			if(jobPoses != null){
				for(JobPos jobPos : jobPoses){
					%>
						<aui:option value="<%=jobPos.getJobPosId() %>" selected="<%=mainJobPosId == jobPos.getJobPosId() %>"><%=jobPos.getTitle() %></aui:option>
					<%
				}
			}
		%>
	</aui:select>
</aui:col>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.ajax.render_workingunitid_mainjobpos.jsp");
%>