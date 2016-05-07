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
<%@ include file="../init.jsp"%>


<liferay-util:include page="/html/portlets/usermgt/admin/toptabs.jsp" servletContext="<%=application %>" />

<liferay-util:include page="/html/portlets/usermgt/admin/toolbar.jsp" servletContext="<%=application %>" />

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "workingunits.jsp");
	
	List<WorkingUnit> workingUnits = new ArrayList<WorkingUnit>();
	int totalCount = 0;
	
	String isEmployeeRequest = ParamUtil.getString(request, WorkingUnitDisplayTerms.WORKINGUNIT_ISEMPLOYER);
    boolean isEmployee = false;
	if(Validator.isNotNull(isEmployeeRequest) && isEmployeeRequest.equals("isEmploy")) {
		isEmployee = true;
	}
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

<liferay-ui:search-container searchContainer="<%= new WorkingUnitSearch(
	renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	<liferay-ui:search-container-results>
		<%@include file="/html/portlets/usermgt/admin/workingunit_search_results.jspf" %>
	</liferay-ui:search-container-results>
	
	<liferay-ui:search-container-row 
		className="org.opencps.usermgt.model.WorkingUnit" 
		modelVar="workingUnit" 
		keyProperty="workingunitId"
	>
	
		<%
		    row.addText(String.valueOf(row.getPos() +1 ));
			row.addText(workingUnit.getName());
			row.addText(workingUnit.getGovAgencyCode());
			String isEmployer = "<i class=\"opencps-icon employees\"></i>";
			
			if(workingUnit.getIsEmployer() == false) {
				isEmployer = "<i class=\"opencps-icon not-employee\"></i>";
			}
			
			row.addText(isEmployer);
			
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  templatePath + "workingunit_action.jsp", config.getServletContext(), request, response);
		%>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>

