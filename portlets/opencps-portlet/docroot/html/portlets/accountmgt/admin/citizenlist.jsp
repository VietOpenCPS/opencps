<%@page import="org.opencps.util.ActionKeys"%>
<%@page import="org.opencps.accountmgt.permissions.CitizenPermission"%>
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
<%@page import="org.opencps.util.DateTimeUtil"%>
<%@page import="org.opencps.util.PortletUtil"%>
<%@page import="org.opencps.accountmgt.search.CitizenSearch"%>
<%@page import="org.opencps.accountmgt.service.CitizenLocalServiceUtil"%>
<%@page import="org.opencps.accountmgt.search.CitizenSearchTerm"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.persistence.metamodel.ListAttribute"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.opencps.util.WebKeys"%>
<%@page import="org.opencps.accountmgt.model.Citizen"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchEntry"%>
<%@page import="org.opencps.accountmgt.search.CitizenDisplayTerms"%>
<%@page import="org.opencps.accountmgt.util.AccountMgtUtil"%>
<%@ include file="../init.jsp" %>


<liferay-util:include page="/html/portlets/accountmgt/admin/toptabs.jsp" servletContext="<%=application %>" />

<%
	Citizen citizen = (Citizen) request.getAttribute(WebKeys.CITIZEN_ENTRY);
	long citizenId = citizen != null ? citizen.getCitizenId() : 0L;
	
	int accountStatus = ParamUtil.getInteger(request, CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS);
	 
	int countRegistered = 0;
	int countConfirmed = 0;
	int countApproved = 0;
	int countLocked = 0;
	
	List<Citizen> citizenRegistered = null;
	List<Citizen> citizenConfirmed = null;
	List<Citizen> citizenApproved = null;
	List<Citizen> citizenLocked = null;
	
	try {
		citizenRegistered = CitizenLocalServiceUtil.getCitizens(scopeGroupId, 0);
		citizenConfirmed = CitizenLocalServiceUtil.getCitizens(scopeGroupId, 1);
		citizenApproved = CitizenLocalServiceUtil.getCitizens(scopeGroupId, 2);
		citizenLocked = CitizenLocalServiceUtil.getCitizens(scopeGroupId, 3);
		
		if(citizenRegistered != null) {
			countRegistered = citizenRegistered.size();
		} else if(citizenConfirmed!=null) {
			countConfirmed = citizenConfirmed.size();
		} else if(citizenApproved != null) {
			countApproved = citizenApproved.size();
		} else if(citizenLocked!=null) {
			countLocked = citizenLocked.size(); 
		}
	} catch(Exception e) {
		
	}
	/* PortletURL searchURL = renderResponse.createRenderURL();
	searchURL.setParameter("tabs1", AccountMgtUtil.TOP_TABS_CITIZEN);
	searchURL.setParameter("mvcPath", "/html/portlets/accountmgt/admin/citizenlist.jsp");
	 */
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("mvcPath", templatePath + "citizenlist.jsp");
	iteratorURL.setParameter(CitizenDisplayTerms.CITIZEN_ACCOUNTSTATUS, String.valueOf(accountStatus));
	List<Citizen> citizens = new ArrayList<Citizen>();
	
	
	int totalCount = 0;
	
	
%>

<aui:row>
	<aui:col width="20">
		<liferay-ui:message key="account.status.total" />  : <%=countLocked +
			countConfirmed + countRegistered + countApproved
		%>
	</aui:col>
	<aui:col width="20">
		<liferay-ui:message key="account.status.registered" />  : <%=countRegistered %>
	</aui:col>
	<aui:col width="20">
		<liferay-ui:message key="account.status.confirmed" />  : <%=countConfirmed %>
	</aui:col>
	<aui:col width="20">
		<liferay-ui:message key="account.status.approved" />  : <%=countApproved %>
	</aui:col>
	<aui:col width="20">
		<liferay-ui:message key="account.status.locked" />  : <%=countLocked %>
	</aui:col>
	
</aui:row>

<c:if test="<%=CitizenPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_CITIZEN) %>" >
	<liferay-util:include page='<%=templatePath + "toolbar.jsp" %>' servletContext="<%=application %>" />
</c:if>

<liferay-ui:search-container searchContainer="<%= new CitizenSearch(
	renderRequest ,SearchContainer.DEFAULT_DELTA, iteratorURL) %>">
	
	<liferay-ui:search-container-results>
		<%
			CitizenSearchTerm searchTerms = (CitizenSearchTerm) searchContainer.getSearchTerms();
			
			if(Validator.isNotNull(searchTerms.getKeywords())) {
				citizens = CitizenLocalServiceUtil.getCitizens(themeDisplay.getScopeGroupId(), searchTerms.getKeywords());
			} else if(accountStatus!=0) {
				citizens = CitizenLocalServiceUtil.getCitizens(themeDisplay.getScopeGroupId(), accountStatus);
			} else if(Validator.isNotNull(searchTerms.getKeywords()) && accountStatus!=0)  {
				citizens = CitizenLocalServiceUtil.getCitizens(themeDisplay.getScopeGroupId(), searchTerms.getKeywords(), accountStatus);
			} else {
				citizens = CitizenLocalServiceUtil.getCitizens(searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
			}
			
			totalCount = CitizenLocalServiceUtil.countAll();
			total = totalCount;
			results = citizens;
			pageContext.setAttribute("results", results);
			pageContext.setAttribute("total", total);
		%>
	
	</liferay-ui:search-container-results>
	<liferay-ui:search-container-row 
		className="org.opencps.accountmgt.model.Citizen" 
		modelVar="citiZen" 
		keyProperty="citizenId"
	>
		<%
			String gender = StringPool.BLANK;
			gender = PortletUtil.getGender(citiZen.getGender(), themeDisplay.getLocale());
			
			String accoutStatus = StringPool.BLANK;
			
			accoutStatus = PortletUtil.getAccountStatus(citiZen.getAccountStatus(), themeDisplay.getLocale());
			
			
			row.addText(citiZen.getPersonalId());
			row.addText(citiZen.getFullName());
			row.addText(gender);
			row.addText(DateTimeUtil.convertDateToString(citiZen.getBirthdate(), DateTimeUtil._VN_DATE_FORMAT));
			row.addText(citiZen.getEmail());
			row.addText(accoutStatus);
			row.addJSP("center", SearchEntry.DEFAULT_VALIGN,  "/html/portlets/accountmgt/admin/citizen_actions.jsp", config.getServletContext(), request, response);
			
		%>
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>	