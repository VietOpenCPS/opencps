<%@page import="com.liferay.portal.kernel.dao.search.DisplayTerms"%>
<%@page import="javax.mail.search.SearchTerm"%>
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
<%@page import="org.opencps.usermgt.search.EmployeeSearch"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.model.Employee"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.util.dao.orm.CustomSQLUtil"%>
<%@page import="org.opencps.usermgt.search.EmployeeSearchTerm"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.usermgt.service.EmployeeLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="org.opencps.usermgt.search.EmployeeDisplayTerm"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.usermgt.model.JobPos"%>
<%@page import="org.opencps.usermgt.service.JobPosLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@ include file="../init.jsp"%>

<liferay-util:include page='<%=templatePath + "toptabs.jsp" %>' servletContext="<%=application %>" />

<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />
<%
	long workingUnitId = (Long)request.getAttribute(EmployeeDisplayTerm.WORKING_UNIT_ID);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "employees.jsp");
	iteratorURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_EMPLOYEE);
	
	List<Employee> employees = new ArrayList<Employee>();
	int totalCount = 0;
%>
<liferay-ui:search-container 
	searchContainer="<%= new EmployeeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>"
	
>
	<liferay-ui:search-container-results>
		<%
		EmployeeSearchTerm searchTerms = (EmployeeSearchTerm)searchContainer.getSearchTerms();
		
		String[] fullNames = null;
		
		if(Validator.isNotNull(searchTerms.getKeywords())){
			fullNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
		}
		
		try{

				if(searchTerms.isAdvancedSearch()){
					if(fullNames != null && workingUnitId > 0){
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, fullNames, workingUnitId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId, fullNames, workingUnitId);
					}else if(fullNames != null && workingUnitId <= 0){
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, fullNames, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId, fullNames);
					}else if(fullNames == null && workingUnitId > 0){
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, workingUnitId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId, workingUnitId);
					}else{
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId);
					}
				}else{
					if(fullNames != null && workingUnitId > 0){
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, fullNames, workingUnitId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId, fullNames, workingUnitId);
					}else if(fullNames != null && workingUnitId <= 0){
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, fullNames, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId, fullNames);
					}else if(fullNames == null && workingUnitId > 0){
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, workingUnitId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId, workingUnitId);
					}else{
						employees = EmployeeLocalServiceUtil.getEmployees(scopeGroupId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
						totalCount = EmployeeLocalServiceUtil.countEmployees(scopeGroupId);
					}
				}
		}catch(Exception e){
			//nothing to do
		}
	
		total = totalCount;
		results = employees;
		
		pageContext.setAttribute("results", results);
		pageContext.setAttribute("total", total);
		%>
	</liferay-ui:search-container-results>
	<liferay-ui:search-container-row 
		className="org.opencps.usermgt.model.Employee" 
		modelVar="employee" 
		keyProperty="employeeId"
	>
	
		<%
			JobPos jobPos = JobPosLocalServiceUtil.getJobPos(employee.getMainJobPosId());
			String jobName = LanguageUtil.get(portletConfig ,themeDisplay.getLocale(), PortletUtil.getLeaderLabel(jobPos.getLeader(), themeDisplay.getLocale()));
			WorkingUnit workingUnit = WorkingUnitLocalServiceUtil.getWorkingUnit(employee.getWorkingUnitId());
		%>
		<liferay-ui:search-container-column-text 
			name="row-index" 
			value="<%= String.valueOf(row.getPos() + 1) %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="fullname" 
			value="<%= employee.getFullName() %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="job-name" 
			value="<%= jobName %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="working-unit-name" 
			value="<%= workingUnit.getName() %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="tel-no" 
			value="<%= employee.getTelNo() %>"
		/>
		
		<liferay-ui:search-container-column-text 
			name="email" 
			value="<%= employee.getEmail() %>"
		/>
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
