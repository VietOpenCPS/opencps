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
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
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
<aui:nav-bar cssClass="custom-toolbar">
	<aui:nav id="toolbarContainer" cssClass="nav-display-style-buttons pull-left" >
		<c:choose>
			<c:when test="<%= tabs1.equals(UserMgtUtil.TOP_TABS_WORKINGUNIT)%>">
				<portlet:renderURL var="editWorkingUnitURL">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_workingunit.jsp" %>'/>
					<portlet:param name="redirectURL" value="<%=currentURL %>"/>
				</portlet:renderURL>
				<c:if test="<%=WorkingUnitPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_WORKINGUNIT) %>">
					<aui:nav-item 
						id="add-workingunit" 
						label="add-workingunit" 
						iconCssClass="icon-plus"  
						href="<%=editWorkingUnitURL %>"
					/>
				</c:if>
			</c:when>
			
			<c:when test="<%= tabs1.contentEquals(UserMgtUtil.TOP_TABS_EMPLOYEE)%>">
				
				<portlet:renderURL var="editEmployeeURL">
					<portlet:param name="mvcPath" value='<%= templatePath + "edit_employee.jsp" %>'/>
					<portlet:param name="backURL" value="<%=currentURL %>"/>
				</portlet:renderURL>
				
				<aui:nav-item 
					id="add-employee" 
					label="add-employee" 
					iconCssClass="icon-plus"  
					href="<%=editEmployeeURL %>"
				/>
					
			</c:when>
			
		</c:choose>
	</aui:nav>
	
		<aui:nav-bar-search cssClass="pull-right">
			<div class="form-search">
			   <%
	                 searchURL.setParameter("mvcPath", templatePath + "employees.jsp");
	                 searchURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_EMPLOYEE);
	           %>
			<aui:form action="<%= searchURL %>" method="post" name="fm">
			<c:choose>
			     <c:when test="<%= tabs1.contentEquals(UserMgtUtil.TOP_TABS_EMPLOYEE)%>">
	                    <aui:row>
	                        <aui:col width="50">
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
	                        
	                        <aui:col width="50">
	                            <liferay-ui:input-search 
	                                id="keywords1" 
	                                name="keywords" 
	                                placeholder='<%= LanguageUtil.get(locale, "keyword") %>' 
	                            />
	                        </aui:col>
	                    </aui:row>
			     </c:when>
			    
			     <c:when test="<%=tabs1.contentEquals(UserMgtUtil.TOP_TABS_WORKINGUNIT) %>">
			          <%
                        searchURL.setParameter("mvcPath", templatePath + "workingunits.jsp");
                        searchURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_WORKINGUNIT);
                      %>
                      <aui:row>
                        <aui:col width="20">
                            <aui:select name="<%=WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER %>" label="<%=StringPool.BLANK %>">
	                             <aui:option value='<%= "fillall" %>'>
                                    <liferay-ui:message key="all" />
                                </aui:option>
	                            <aui:option value='<%= "isEmploy" %>'>
	                                <liferay-ui:message key="fill-by-is-employer" />
	                            </aui:option>
	                             <aui:option value='<%= "isNotEmploy" %>'>
	                                <liferay-ui:message key="fill-by-is-not-employer" />
	                            </aui:option>
	                      </aui:select>
                        </aui:col>
                        </aui:row>
                        <aui:button type="submit" name="fill" value="fill"/>
			     </c:when>
			</c:choose>
				</aui:form>
			</div>
		</aui:nav-bar-search>
</aui:nav-bar>
<%!
	private Log _log = LogFactoryUtil.getLog("html.portlets.usermgt.admin.toolbar.jsp");
%>
