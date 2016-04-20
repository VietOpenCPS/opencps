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
<%@ include file="../init.jsp"%>


<liferay-util:include page="/html/portlets/usermgt/admin/toptabs.jsp" servletContext="<%=application %>" />

<liferay-util:include page="/html/portlets/usermgt/admin/toolbar.jsp" servletContext="<%=application %>" />

<%
	long workingUnitId = (Long)request.getAttribute(EmployeeDisplayTerm.WORKING_UNIT_ID);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "employees.jsp");
	iteratorURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_EMPLOYEE);
	
	List<Employee> employees = new ArrayList<Employee>();
	int totalCount = 0;
%>
<liferay-ui:success key="<%=MessageKeys.USERMGT_ADD_SUCCESS %>" message="<%=MessageKeys.USERMGT_ADD_SUCCESS %>"/>
<liferay-ui:success key="<%=MessageKeys.USERMGT_UPDATE_SUCCESS %>" message="<%=MessageKeys.USERMGT_UPDATE_SUCCESS %>"/>

<liferay-ui:success key="<%=MessageKeys.USERMGT_EMPLOYEE_DELETE_SUCCESS %>" message="<%=MessageKeys.USERMGT_EMPLOYEE_DELETE_SUCCESS %>"/>
<liferay-ui:error key="<%=MessageKeys.USERMGT_EMPLOYEE_DELETE_ERROR %>" message="<%=MessageKeys.USERMGT_EMPLOYEE_DELETE_ERROR %>"/>


<liferay-ui:search-container searchContainer="<%= new EmployeeSearch(renderRequest, SearchContainer.DEFAULT_DELTA, iteratorURL) %>">

	<liferay-ui:search-container-results>
		<%
			EmployeeSearchTerm searchTerms = (EmployeeSearchTerm)searchContainer.getSearchTerms();
			
			String[] fullNames = null;
			
			if(Validator.isNotNull(searchTerms.getKeywords())){
				fullNames = CustomSQLUtil.keywords(searchTerms.getKeywords());
			}
			
			try{

				%>
					<%@include file="/html/portlets/usermgt/admin/employee_search_results.jspf" %>
				<%
			}catch(Exception e){
				_log.error(e);
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
				PortletURL editURL = renderResponse.createRenderURL();
				editURL.setParameter("mvcPath", templatePath + "edit_employee.jsp");
				editURL.setParameter(EmployeeDisplayTerm.EMPLOYEE_ID, String.valueOf(employee.getEmployeeId()));
				editURL.setParameter("backURL", currentURL);
				
				// no column
				row.addText(String.valueOf(row.getPos() + 1));
			
				// employee no
				//row.addText(employee.getEmployeeNo(), editURL);
				row.addText(employee.getEmployeeNo());
				
				// full name
				row.addText(employee.getFullName());
				
				// working unit
				long workingUnitIdTemp = employee.getWorkingUnitId();
				
				String workingUnitName = StringPool.BLANK;
				
				if(workingUnitIdTemp > 0){
					try{
						WorkingUnit workingUnit = WorkingUnitLocalServiceUtil.getWorkingUnit(workingUnitIdTemp);
						if(workingUnit != null){
							workingUnitName = workingUnit.getName();
						}
					}catch(Exception e){
						// nothing todo
					}
				}
				row.addText(workingUnitName);
				
				// screen name
				String screenName = StringPool.BLANK;
				
				long mappingUserId = employee.getMappingUserId();
				
				if(mappingUserId > 0){
					try{
						User mappingUser = UserLocalServiceUtil.getUser(mappingUserId);
						if(mappingUser != null){
							screenName = mappingUser.getScreenName();
						}
					}catch(Exception e){
						// nothing todo
					}
				}
				
				row.addText(screenName, editURL);
				
				String status = "<i class=\"opencps-icon checked\"></i>";
				
				if(employee.getWorkingStatus() == PortletConstants.WORKING_STATUS_DEACTIVATE){
					status = "<i class=\"opencps-icon removed\"></i>";
				}
				
				row.addText(status);
				
				
				//action column
				row.addJSP("center", SearchEntry.DEFAULT_VALIGN, templatePath + "employee_actions.jsp", config.getServletContext(), request, response);
			%>	
		</liferay-ui:search-container-row> 
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.employees.jsp");
%>

