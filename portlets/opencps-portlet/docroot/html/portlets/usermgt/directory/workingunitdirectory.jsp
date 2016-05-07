
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
<%@ include file="../init.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="java.util.List"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitSearchTerms"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />

<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "workingunitdirectory.jsp");
	
	List<WorkingUnit> workingUnits = new ArrayList<WorkingUnit>();
	int totalCount = 0;
	
	String isEmployeeRequest = ParamUtil.getString(request, WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER);
    boolean isEmployee = false;
	if(Validator.isNotNull(isEmployeeRequest) && isEmployeeRequest.equals("isEmploy")) {
		isEmployee = true;
	}
%>
<liferay-ui:search-container 
	emptyResultsMessage="no-workingunit-were-found"
	iteratorURL="<%=iteratorURL %>"
	delta="<%=20 %>"
	deltaConfigurable="true"
>
	<liferay-ui:search-container-results>
		<%
            if(Validator.isNotNull(isEmployeeRequest) && !isEmployeeRequest.equals("fillall")) {
                workingUnits = WorkingUnitLocalServiceUtil
                                .getWorkingUnit(scopeGroupId, isEmployee, 
                                    searchContainer.getStart(), searchContainer.getEnd(),
                                    searchContainer.getOrderByComparator());
                totalCount = WorkingUnitLocalServiceUtil.countByG_E(scopeGroupId, isEmployee);
            } else {
                 workingUnits = WorkingUnitLocalServiceUtil.getWorkingUnit(searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
                 totalCount = WorkingUnitLocalServiceUtil.countAll();
            }
            total = totalCount;
            results = workingUnits;
            pageContext.setAttribute("results", results);
            pageContext.setAttribute("total", total);

	%>
	</liferay-ui:search-container-results>
	<liferay-ui:search-container-row 
		className="org.opencps.usermgt.model.WorkingUnit" 
		modelVar="workingUnit" 
		keyProperty="workingunitId"
	>
		<liferay-ui:search-container-column-text 
			name="row-index" 
			value="<%= String.valueOf(row.getPos() + 1) %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="name" 
			value="<%= workingUnit.getName() %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="address" 
			value="<%= workingUnit.getAddress() %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="tel-no" 
			value="<%= workingUnit.getTelNo() %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="email" 
			value="<%= workingUnit.getEmail() %>"
		/>
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
