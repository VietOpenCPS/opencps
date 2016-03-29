
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
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
<%@ include file="../init.jsp"%>
<%
	long mngworkingUnitId =
		ParamUtil.getLong(request, "mngworkingUnitId");
	List<WorkingUnit> unitsRoot = new ArrayList<WorkingUnit>();
	List<WorkingUnit> units = new ArrayList<WorkingUnit>();
			units = WorkingUnitLocalServiceUtil.getWorkingUnits(
			scopeGroupId, mngworkingUnitId);
	if(units.isEmpty()) {
		WorkingUnit workingUnit1 = WorkingUnitLocalServiceUtil
						.fetchWorkingUnit(mngworkingUnitId);
		if(workingUnit1 != null ) {
			unitsRoot.add(0, workingUnit1);
		}
	} else {
		WorkingUnit workingUnit1 = WorkingUnitLocalServiceUtil
						.fetchWorkingUnit(mngworkingUnitId);
		if(workingUnit1 != null ) {
			unitsRoot.add(0, workingUnit1);
		}
		unitsRoot = units;
	}
%>

<aui:select
	name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_PARENTWORKINGUNITID%>">
	<%
		if (unitsRoot.isEmpty()) {
	%>
	<aui:option value="<%=0%>">---</aui:option>

	<%
		}
			else {

				for (WorkingUnit unitt : unitsRoot) {
	%>
	<aui:option value="<%=unitt.getWorkingunitId()%>"><%=unitt.getWorkingunitId()%></aui:option>

	<%
		}
			}
	%>
</aui:select>
<%-- <aui:select
	name='<%=WorkingUnitDisplayTerms.WORKINGUNIT_PARENTWORKINGUNITID%>'
	label="parent-working-unit-id">
	<%
		if (workingUnits.isEmpty()) {
				List<WorkingUnit> workingUnits1 =
					new ArrayList<WorkingUnit>();

				workingUnits1 =
					WorkingUnitLocalServiceUtil.getWorkingUnit(
						scopeGroupId, true);

				if (workingUnits1.isEmpty()) {
	%>

	<aui:option value="<%=0%>"></aui:option>

	<%
		}
				else {
					for (WorkingUnit unit1 : workingUnits1) {
	%>

	<aui:option value='<%=unit1.getWorkingunitId()%>'>
		<%=unit1.getWorkingunitId()%>
	</aui:option>

	<%
		}
				}
			}
			else {

				for (WorkingUnit unit : workingUnits) {
	%>

	<aui:option value='<%=unit.getWorkingunitId()%>'>
		<%=unit.getWorkingunitId()%>
	</aui:option>

	<%
		}
			}
	%>


</aui:select> --%>

