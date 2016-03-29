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
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.usermgt.permissions.WorkingUnitPermission"%>
<%@page import="com.liferay.portal.service.permission.PortletPermissionUtil"%>
<%@page import="org.opencps.util.ActionKeys"%>

<%@ include file="../init.jsp"%>

<%
	String tabs1 = ParamUtil.getString(request, "tabs1", UserMgtUtil.TOP_TABS_WORKINGUNIT);
	PortletURL searchURL = renderResponse.createRenderURL();
	
	long workingUnitId = ParamUtil.getLong(request, EmployeeDisplayTerm.WORKING_UNIT_ID, 0L);
	
	List<WorkingUnit> workingUnits = new ArrayList<WorkingUnit>();
	
	try{
		workingUnits = WorkingUnitLocalServiceUtil.getWorkingUnits(scopeGroupId, 0);
		
	}catch(Exception e){
		_log.error(e);
	}
	
	request.setAttribute(EmployeeDisplayTerm.WORKING_UNIT_ID, workingUnitId);
%>

<c:choose>
	<c:when test="<%= tabs1.equals(UserMgtUtil.TOP_TABS_WORKINGUNIT)%>">
		<portlet:renderURL var="editWorkingUnitURL">
			<portlet:param name="mvcPath" value='<%= templatePath + "edit_workingunit.jsp" %>'/>
			<portlet:param name="redirectURL" value="<%=currentURL %>"/>
		</portlet:renderURL>
		<c:if test="<%=WorkingUnitPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_WORKINGUNIT) %>">
			<aui:button name="add-workingunit" value="add-workingunit" href="<%= editWorkingUnitURL%>"/>
		</c:if>
		
		
	</c:when>
	
	<c:when test="<%= tabs1.equals(UserMgtUtil.TOP_TABS_EMPLOYEE)%>">
		<%
			searchURL.setParameter("mvcPath", templatePath + "employees.jsp");
			searchURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_EMPLOYEE);
		%>
		<portlet:renderURL var="editEmployeeURL">
			<portlet:param name="mvcPath" value='<%= templatePath + "edit_employee.jsp" %>'/>
			<portlet:param name="backURL" value="<%=currentURL %>"/>
		</portlet:renderURL>
		
		<aui:row>
			<aui:form action="<%= searchURL %>" method="post" name="fm">
				<aui:row>
					<aui:col width="30">
						<aui:input 
							id="keywords1" 
							name="keywords" 
							label="<%=StringPool.BLANK %>"
							placeholder='<%= LanguageUtil.get(locale, "full-name") %>' 
							type="text"
						/>
					</aui:col>
					
					<aui:col width="30">
						<%
							searchURL.setParameter(EmployeeDisplayTerm.WORKING_UNIT_ID, String.valueOf(workingUnitId));
						%>
						<aui:select name="<%=EmployeeDisplayTerm.WORKING_UNIT_ID %>" label="<%=StringPool.BLANK %>">
							<aui:option value="0"></aui:option>
							<%
								if(workingUnits != null){
									for(WorkingUnit workingUnit : workingUnits){
										%>
											<aui:option value="<%=workingUnit.getWorkingunitId() %>" selected="<%=workingUnitId == workingUnit.getWorkingunitId()%>">
												<%=workingUnit.getName() %>
											</aui:option>
										<%
									}
								}
							%>
						</aui:select> 
					</aui:col>
					
					<aui:col width="30">
						<aui:input 
							name="search" 
							type="submit" value="search" 
							label="<%=StringPool.BLANK %>" 
							cssClass="opencps usermgt toolbar search-btn"
						/>
					</aui:col>
				</aui:row>
			</aui:form>
		</aui:row>
		
		<aui:row>
			<aui:col width="100">
				<aui:button name="add-employee" value="add-employee" href="<%=editEmployeeURL %>"/>
				<div class="bottom-horizontal-line"></div>
			</aui:col>
		</aui:row>
		
	</c:when>
	
	<c:otherwise>
		<div class="portlet-msg-portlet"><liferay-ui:message key="no-found-resource"/></div>
	</c:otherwise>
</c:choose>

<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.toolbar.jsp");
%>
