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

<%@ include file="../../init.jsp"%>
<%
	long workingUnitId = ParamUtil.getLong(request, EmployeeDisplayTerm.WORKING_UNIT_ID, 0L);
	List<WorkingUnit> workingUnits = UserMgtUtil.getWorkingUnitsForEmployess(scopeGroupId, workingUnitId);
%>

<aui:col width="50">
	<aui:select 
		name='<%=EmployeeDisplayTerm.WORKING_UNIT_ID + 0 %>' 
		label='<%=StringPool.BLANK %>' 
		onChange='<%=renderResponse.getNamespace() + "getJobPosByWorkingUnitId(this)" %>'
		showEmptyOption='<%=true %>'
	>
		
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
		name='<%=EmployeeDisplayTerm.JOBPOS_ID + 0 %>' 
		label='<%=StringPool.BLANK %>' 
		showEmptyOption='<%=true %>'
	>
	</aui:select>
</aui:col>