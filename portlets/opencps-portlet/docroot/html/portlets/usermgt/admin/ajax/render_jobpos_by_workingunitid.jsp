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
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@ include file="../../init.jsp"%>
<%	

	long workingUnitId = ParamUtil.getLong(request, EmployeeDisplayTerm.WORKING_UNIT_ID, 0L);
	List<JobPos> jobPoses = new ArrayList<JobPos>();
	
	if(workingUnitId > 0){
		try{
			jobPoses = JobPosLocalServiceUtil.getJobPoss(scopeGroupId, workingUnitId);
		}catch(Exception e){
			_log.error(e);
		}
	}
	%>
		<option value=""><liferay-ui:message key="select-jobpos"/></option>
	<%
	if(jobPoses != null){
		
		for(JobPos jobPos : jobPoses){
			%>
				<option value="<%=jobPos.getJobPosId()%>"><%=jobPos.getTitle() %></option>
			<%
		}
	}
%>



<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.ajax.render_jobpos_by_workingunitid.jsp");
%>