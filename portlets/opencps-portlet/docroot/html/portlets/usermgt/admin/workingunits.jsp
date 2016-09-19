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
<%@page import="org.opencps.usermgt.WorkingUnitHasChildException"%>
<%@page import="org.opencps.usermgt.NoSuchWorkingUnitException"%>
<%@page import="org.opencps.usermgt.EmployeeHasExistedException"%>
<%@page import="org.opencps.usermgt.JopPosHasExistedException"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="com.liferay.taglib.aui.RowTag"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.usermgt.service.WorkingUnitLocalServiceUtil"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitSearchTerms"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitSearch"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.opencps.usermgt.model.WorkingUnit"%>
<%@page import="java.util.List"%>
<%@page import="org.opencps.usermgt.search.WorkingUnitDisplayTerms"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="org.opencps.util.MessageKeys"%>
<%@page import="org.opencps.util.PortletConstants"%>
<%@page import="org.opencps.usermgt.util.UserMgtUtil"%>

<%@ include file="../init.jsp"%>


<liferay-util:include page="/html/portlets/usermgt/admin/toptabs.jsp" servletContext="<%=application %>" />

<liferay-util:include page="/html/portlets/usermgt/admin/toolbar.jsp" servletContext="<%=application %>" />

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "workingunits.jsp");
	iteratorURL.setParameter("tabs1", UserMgtUtil.TOP_TABS_WORKINGUNIT);
	List<WorkingUnit> workingUnits = new ArrayList<WorkingUnit>();
	int totalCount = 0;
	
	String isEmployeeRequest = ParamUtil.getString(request, WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER);
    boolean isEmployee = false;
	if(Validator.isNotNull(isEmployeeRequest) && isEmployeeRequest.equals("isEmploy")) {
		isEmployee = true;
	}
	
	List<String> headerNames = new ArrayList<String>();
	
	headerNames.add("row-index");
	headerNames.add("working-unit-info");
	headerNames.add("action");
	
	String headers = StringUtil.merge(headerNames, StringPool.COMMA);
%>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_WORKINGUNIT_UPDATE_SUCESS %>" 
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_WORKINGUNIT_UPDATE_SUCESS) %>"
/>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_WORKINGUNIT_DELETE_SUCCESS %>" 
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_WORKINGUNIT_DELETE_SUCCESS) %>"
/>

<liferay-ui:success 
	key="<%=MessageKeys.USERMGT_WORKINGUNIT_DELETE_ERROR %>" 
	message="<%=LanguageUtil.get(pageContext, 
		MessageKeys.USERMGT_WORKINGUNIT_DELETE_ERROR) %>"
/>

<liferay-ui:error 
	exception="<%=NoSuchWorkingUnitException.class %>"
	message="<%=NoSuchWorkingUnitException.class.getName() %>"	
/>

<liferay-ui:error 
	exception="<%=JopPosHasExistedException.class  %>"
	message="<%=JopPosHasExistedException.class.getName() %>"	
/>

<liferay-ui:error 
	exception="<%=EmployeeHasExistedException.class %>"
	message="<%=EmployeeHasExistedException.class.getName() %>"	
/>

<liferay-ui:error 
	exception="<%=WorkingUnitHasChildException.class %>"
	message="<%=WorkingUnitHasChildException.class.getName() %>"	
/>

<liferay-ui:error 
	key="<%=MessageKeys.USERMGT_SYSTEM_EXCEPTION_OCCURRED %>"
	message="<%=MessageKeys.USERMGT_SYSTEM_EXCEPTION_OCCURRED %>"	
/>

<div class="opencps-searchcontainer-wrapper default-box-shadow radius8">
	<liferay-ui:search-container searchContainer="<%= new WorkingUnitSearch(
		renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>" headerNames="<%=headers %>">
		<liferay-ui:search-container-results>
			<%@include file="/html/portlets/usermgt/admin/workingunit_search_results.jspf" %>
		</liferay-ui:search-container-results>
		
		<liferay-ui:search-container-row 
			className="org.opencps.usermgt.model.WorkingUnit" 
			modelVar="workingUnit" 
			keyProperty="workingunitId"
		>
			<liferay-util:buffer var="rowIndex">
				<div class="row-fluid min-width10">
					<div class="span12 bold">
						<%=row.getPos() + 1 %>
					</div>
				</div>
			</liferay-util:buffer>
			
			<liferay-util:buffer var="workingunitInfo">
				<div class="row-fluid">
					<div class="span4 bold">
						<liferay-ui:message key="name"/>
					</div>
					<div class="span8">
						<%= workingUnit.getName() %>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span4 bold">
						<liferay-ui:message key="govagencycode"/>
					</div>
					<div class="span8">
						<%= workingUnit.getGovAgencyCode() %>
					</div>
				</div>
				
				<div class="row-fluid">
					<div class="span4 bold">
						<liferay-ui:message key="isEmployer"/>
					</div>
					<div class="span8">
						<%
							String isEmployer = "<i class=\"opencps-icon employees\"></i>";
							
							if(workingUnit.getIsEmployer() == false) {
								isEmployer = "<i class=\"opencps-icon not-employee\"></i>";
							}
						%>
						<%= isEmployer %>
					</div>
				</div>
			</liferay-util:buffer>
		
			<%
				row.setClassName("opencps-searchcontainer-row");
			    row.addText(rowIndex);
				row.addText(workingunitInfo);
	
				//row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  templatePath + "workingunit_action.jsp", config.getServletContext(), request, response);
			%>
			
			<liferay-ui:search-container-column-jsp 
				path='<%=templatePath + "workingunit_action.jsp" %>' 
				name="action" cssClass="width80"
			/>
		</liferay-ui:search-container-row>
		
		<liferay-ui:search-iterator type="opencs_page_iterator"/>
	</liferay-ui:search-container>
</div>
